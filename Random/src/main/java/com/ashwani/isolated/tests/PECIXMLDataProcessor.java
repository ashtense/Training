/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.isolated.tests;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

public class PECIXMLDataProcessor {

	final class DataProcessingConstants {

		private DataProcessingConstants() {
		}

		private static final String EFFECTIVE_MOMENT = "Effective_Moment";
		private static final String EFFECTIVE_CHANGE = "Effective_Change";
		private static final String WID = "WID";
		private static final String WORKER = "Worker";
		private static final String FIELD_GROUP_SEPARATOR = "__";
		private static final String NEW_LINE = "\n";
		private static final String DATA_SEPARATOR = "|";
		private static final String DERIVED_EVENT_CODE = FIELD_GROUP_SEPARATOR + "Derived_Event_Code";
		private static final String WORKER_SUMMARY = "Worker_Summary";
		private static final int MANDATORY_FIELD_GROUP_SIZE = 9;
		private static final String MANDATORY_FIELD_GROUP_KEY = "RT0001";
		private static final int INDEX_ONE = 1;
		private static final String BLANK_STRING = "";
		private static final String WES_SUMMARY = "Workers_Effective_Stack__Summary";
	}

	enum MandatoryRecordDataFields {
		Pay_Group_Country, Payroll_Company_ID, Pay_Group_ID
	}

	enum FieldGroupOperations {
		isUpdated, isAdded, isDeleted
	}

	public void processPECIXml(final FileInputStream fileStream) throws XMLStreamException {
		final StringBuilder currentFieldGroupAttribute = new StringBuilder();
		final List<String> fieldGroupStack = new ArrayList<>();
		final StringBuilder dataStack = new StringBuilder();
		final Map<String, LinkedList<String>> dataMap = new LinkedHashMap<>();
		final StringBuilder currentEffectiveDateTime = new StringBuilder();
		final StringBuilder currentWorkerID = new StringBuilder();
		final Map<String, String> effectiveStackSummaryDataMap = new HashMap<>();
		final Map<String, String> attributeMap = new HashMap<>();

		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		final XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(fileStream);
		while (eventReader.hasNext()) {
			final XMLEvent currentEvent = eventReader.nextEvent();
			final Event currentCustomEvent = generateCustomEvent(currentEvent);
			if ((currentCustomEvent != null) && currentCustomEvent.isStartEvent()) {
				handleStartEvent(currentCustomEvent, currentFieldGroupAttribute, fieldGroupStack, attributeMap);
			}
			if ((currentCustomEvent != null) && currentCustomEvent.isData()) {
				dataStack.append(currentCustomEvent.getData());
			}
			if ((currentCustomEvent != null) && currentCustomEvent.isEndEvent()) {
				handleEndEvent(currentCustomEvent, currentEffectiveDateTime, dataStack, currentWorkerID,
						effectiveStackSummaryDataMap, fieldGroupStack, dataMap, currentFieldGroupAttribute,
						attributeMap);
			}
		}
	}

	private static void handleStartEvent(final Event currentCustomEvent, final StringBuilder currentFieldGroupAttribute,
			final List<String> fieldGroupStack, final Map<String, String> attributeMap) {
		if (!currentCustomEvent.getAttributes().isEmpty()) {
			currentCustomEvent.getAttributes().forEach(attribute -> {
				final boolean anyMatch = Stream.of(FieldGroupOperations.values()).anyMatch(
						operationAttribute -> operationAttribute.name().equalsIgnoreCase(attribute.getName()));
				if (anyMatch) {
					attributeMap.put(currentCustomEvent.getName(), attribute.getName());
				}
			});
		}
		fieldGroupStack.add(currentCustomEvent.getName());
	}

	private static void handleEndEvent(final Event currentCustomEvent, final StringBuilder currentEffectiveDateTime,
			final StringBuilder dataStack, final StringBuilder currentWorkerID,
			final Map<String, String> effectiveStackSummaryDataMap, final List<String> fieldGroupStack,
			final Map<String, LinkedList<String>> dataMap, final StringBuilder currentFieldGroupAttribute,
			final Map<String, String> attributeMap) {
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_MOMENT)) {
			currentEffectiveDateTime.append(dataStack.toString().trim());
		}
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_CHANGE)) {
			currentEffectiveDateTime.setLength(0);
		}
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WID)) {
			currentWorkerID.append(dataStack.toString().trim());
		}
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WORKER)) {
			currentWorkerID.setLength(0);
		}

		final boolean anyMatch = Stream.of(MandatoryRecordDataFields.values())
				.anyMatch(mandatoryField -> mandatoryField.name().equalsIgnoreCase(currentCustomEvent.getName()));
		if (anyMatch) {
			effectiveStackSummaryDataMap.put(currentCustomEvent.getName(), dataStack.toString());
		}

		fieldGroupStack.remove(currentCustomEvent.getName());
		String tempFieldGroup = null;
		if ((dataStack.length() != 0)
				&& !((dataStack.length() == 1) && dataStack.toString().contains(DataProcessingConstants.NEW_LINE))) {
			tempFieldGroup = generateFieldGroupName(fieldGroupStack);
			if (dataMap.containsKey(tempFieldGroup)) {
				final LinkedList<String> dataList = dataMap.get(tempFieldGroup);
				dataList.add(dataStack.toString().trim());
			} else {
				final LinkedList<String> tempData = new LinkedList<>();
				tempData.add(dataStack.toString().trim());
				dataMap.put(tempFieldGroup, tempData);
			}
		}
		processDataMap(currentCustomEvent, tempFieldGroup, dataStack, dataMap, currentWorkerID,
				effectiveStackSummaryDataMap, currentEffectiveDateTime, currentFieldGroupAttribute, attributeMap);
	}

	private static void processDataMap(final Event currentCustomEvent, final String tempFieldGroup,
			final StringBuilder dataStack, final Map<String, LinkedList<String>> dataMap,
			final StringBuilder currentWorkerID, final Map<String, String> effectiveStackSummaryDataMap,
			final StringBuilder currentEffectiveDateTime, final StringBuilder currentFieldGroupAttribute,
			final Map<String, String> attributeMap) {
		if ((dataStack.length() == 0)
				|| ((dataStack.length() == 1) && dataStack.toString().contains("\n") && (tempFieldGroup == null))) {
			final Iterator<String> keyIterator = dataMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String currentKey = keyIterator.next();
				if (currentKey.contains(currentCustomEvent.getName())) {
					final LinkedList<String> dataList = dataMap.get(currentKey);
					if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_CHANGE)) {
						currentKey = currentKey.concat(DataProcessingConstants.DERIVED_EVENT_CODE);
					}
					manipulateData(dataList, currentCustomEvent, currentKey, currentEffectiveDateTime, currentWorkerID,
							currentFieldGroupAttribute, attributeMap);
					sendDataToHazelCast(currentKey, dataList);
					keyIterator.remove();
					if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WORKER_SUMMARY)) {
						handleMandatoryFieldGroup(currentWorkerID, effectiveStackSummaryDataMap);
					}
				}
			}
		}
		dataStack.setLength(0);
	}

	private static void handleMandatoryFieldGroup(final StringBuilder currentWorkerID,
			final Map<String, String> effectiveStackSummaryDataMap) {
		final LinkedList<String> tempMandatoryRecord = new LinkedList<>();
		tempMandatoryRecord.add(currentWorkerID.toString());
		Stream.of(MandatoryRecordDataFields.values()).forEach(
				mandatoryFields -> tempMandatoryRecord.add(effectiveStackSummaryDataMap.get(mandatoryFields.name())));
		if (tempMandatoryRecord.size() != DataProcessingConstants.MANDATORY_FIELD_GROUP_SIZE) {
			for (int i = tempMandatoryRecord.size() - 1; i < (DataProcessingConstants.MANDATORY_FIELD_GROUP_SIZE
					- DataProcessingConstants.INDEX_ONE); i++) {
				tempMandatoryRecord.add("");
			}
		}
		sendDataToHazelCast(DataProcessingConstants.MANDATORY_FIELD_GROUP_KEY, tempMandatoryRecord);
	}

	private static void manipulateData(final LinkedList<String> dataList, final Event currentCustomEvent,
			final String currentKey, final StringBuilder currentEffectiveDateTime, final StringBuilder currentWorkerID,
			final StringBuilder currentFieldGroupAttribute, final Map<String, String> attributeMap) {
		if (currentKey.equalsIgnoreCase(DataProcessingConstants.WES_SUMMARY)) {
			dataList.add(0, dataList.get(0).trim());
		}
		if (currentKey.contains(DataProcessingConstants.WORKER_SUMMARY)) {
			dataList.add(0, currentWorkerID.toString().trim());
		}
		if (StringUtils.isNotEmpty(currentEffectiveDateTime.toString())) {
			dataList.add(0, currentEffectiveDateTime.toString());
		}
		if (StringUtils.isNotEmpty(currentWorkerID.toString()) && !dataList.contains(currentWorkerID.toString())) {
			dataList.add(0, currentWorkerID.toString().trim());
		}
		final Optional<String> operationalAttribute = Optional
				.ofNullable(attributeMap.get(currentCustomEvent.getName()));
		if (operationalAttribute.isPresent()) {
			dataList.add(2, operationalAttribute.get());
			attributeMap.remove(currentCustomEvent.getName());
		} else if (currentKey.contains(DataProcessingConstants.EFFECTIVE_CHANGE)
				&& !currentKey.contains(DataProcessingConstants.DERIVED_EVENT_CODE)) {
			dataList.add(2, DataProcessingConstants.BLANK_STRING);
		}
	}

	private static void sendDataToHazelCast(final String currentKey, final LinkedList<String> dataList) {
		final StringBuilder tempData = new StringBuilder();
		tempData.append(currentKey);
		dataList.forEach(dataString -> {
			if (tempData.length() == 0) {
				tempData.append(dataString.trim());
			} else {
				tempData.append(DataProcessingConstants.DATA_SEPARATOR + dataString.trim());
			}
		});
		System.out.println(tempData.toString());
	}

	@SuppressWarnings("unchecked")
	private static Event generateCustomEvent(final XMLEvent currentEvent) {
		Event event = null;
		if (currentEvent.isStartElement()) {
			event = new Event().withName(currentEvent.asStartElement().getName().getLocalPart());
			final Iterator<Attribute> attributes = currentEvent.asStartElement().getAttributes();
			while (attributes.hasNext()) {
				final Attribute currentAttribute = attributes.next();
				event.addAttributes(new EventAttribute().withName(currentAttribute.getName().getLocalPart())
						.withValue(currentAttribute.getValue()));
			}
			event.setStartEventFlag(Boolean.TRUE);
		} else if (currentEvent.isCharacters()) {
			event = new Event().withData(currentEvent.asCharacters().getData());
			event.setDataFlag(Boolean.TRUE);
		} else if (currentEvent.isEndElement()) {
			event = new Event().withName(currentEvent.asEndElement().getName().getLocalPart());
			event.setEndEventFlag(Boolean.TRUE);
		}
		return event;
	}

	private static String generateFieldGroupName(final List<String> fieldGroupList) {
		final StringBuilder fieldGroupName = new StringBuilder();
		fieldGroupList.forEach(fieldGroup -> {
			if (fieldGroupName.length() == 0) {
				fieldGroupName.append(fieldGroup);
			} else {
				fieldGroupName.append(DataProcessingConstants.FIELD_GROUP_SEPARATOR + fieldGroup);
			}
		});
		return fieldGroupName.toString().trim();
	}
}
