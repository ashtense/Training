/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.isolated.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

public class XMLDataProcessingV11 {

	public final class DataProcessingConstants {

		private DataProcessingConstants() {
		}

		public static final String EFFECTIVE_MOMENT = "Effective_Moment";
		public static final String EFFECTIVE_CHANGE = "Effective_Change";
		public static final String WID = "WID";
		public static final String WORKER = "Worker";
		public static final String FIELD_GROUP_SEPARATOR = "__";
		public static final String NEW_LINE = "\n";
		public static final String DATA_SEPARATOR = "|";
		public static final String DERIVED_EVENT_CODE = FIELD_GROUP_SEPARATOR + "Derived_Event_Code";
		public static final String WORKER_SUMMARY = "Worker_Summary";
		public static final int MANDATORY_FIELD_GROUP_SIZE = 9;
		public static final String MANDATORY_FIELD_GROUP_KEY = "RT0001";
		public static final int INDEX_ONE = 1;
		public static final String BLANK_STRING = "";
		public static final String WES = "Workers_Effective_Stack";
		public static final String WES_SUMMARY = WES + "__Summary";
		public static final String SYS_CEID = "SYS_CEID";
		public static final String EMPLOYEE_ID = "Employee_ID";
	}

	enum MandatoryRecordDataFields {
		Pay_Group_Country("SYS_Country"), Payroll_Company_ID("SYS_PayCompany"), Pay_Group_ID("SYS_PayGroup");

		private final String mandatoryRecordField;

		MandatoryRecordDataFields(String mandatoryRecordField) {
			this.mandatoryRecordField = mandatoryRecordField;
		}

		public String getMandatoryRecordField() {
			return this.mandatoryRecordField;
		}
	}

	enum FieldGroupOperations {
		isUpdated, isAdded, isDeleted
	}

	private static final File xmlFile = new File("C:/Users/solanka/Desktop/PECI/Testv3.xml");
	private static List<String> fieldGroupStack = new ArrayList<>();
	private static StringBuilder dataStack = new StringBuilder();
	private static Map<String, LinkedList<Event>> dataMap = new LinkedHashMap<>();
	private static StringBuilder currentEffectiveDateTime = new StringBuilder();
	private static final StringBuilder currentWorkerID = new StringBuilder();
	private static Map<String, String> effectiveStackSummaryDataMap = new HashMap<>();
	private static Map<String, String> attributeMap = new HashMap<>();
	private static Map<String, List<CustomField>> extractedCDSData;

	public static void main(final String[] args) throws XMLStreamException, IOException {
		extractedCDSData = CDSDerivationUtil.extractCDS();
		final InputStream inputStream = new FileInputStream(xmlFile);
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		final XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(inputStream);
		while (eventReader.hasNext()) {
			final XMLEvent currentEvent = eventReader.nextEvent();
			final Event currentCustomEvent = generateCustomEvent(currentEvent);
			if ((currentCustomEvent != null) && currentCustomEvent.isStartEvent()) {
				handleStartEvent(currentCustomEvent);
			}
			if ((currentCustomEvent != null) && currentCustomEvent.isData()) {
				dataStack.append(currentCustomEvent.getData());
			}
			if ((currentCustomEvent != null) && currentCustomEvent.isEndEvent()) {
				handleEndEvent(currentCustomEvent);
			}
		}
		try {
			inputStream.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleStartEvent(final Event currentCustomEvent) {
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

	private static void handleEndEvent(final Event currentCustomEvent) {
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_MOMENT)) {
			currentEffectiveDateTime.append(dataStack.toString().trim());
		}
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_CHANGE)) {
			currentEffectiveDateTime.setLength(0);
		}
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EMPLOYEE_ID)) {
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
				final LinkedList<Event> dataList = dataMap.get(tempFieldGroup);
				dataList.add(new Event().withName(currentCustomEvent.getName().trim())
						.withData(dataStack.toString().trim()));
			} else {
				final LinkedList<Event> tempData = new LinkedList<>();
				tempData.add(new Event().withName(currentCustomEvent.getName().trim())
						.withData(dataStack.toString().trim()));
				dataMap.put(tempFieldGroup, tempData);
			}
		}
		processDataMap(currentCustomEvent, tempFieldGroup);
	}

	private static void processDataMap(final Event currentCustomEvent, final String tempFieldGroup) {
		if ((dataStack.length() == 0)
				|| ((dataStack.length() == 1) && dataStack.toString().contains("\n") && (tempFieldGroup == null))) {
			final Iterator<String> keyIterator = dataMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String currentKey = keyIterator.next();
				if (currentKey.contains(currentCustomEvent.getName())) {
					final LinkedList<Event> dataList = dataMap.get(currentKey);
					if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_CHANGE)) {
						currentKey = currentKey.concat(DataProcessingConstants.DERIVED_EVENT_CODE);
					}
					manipulateData(dataList, currentCustomEvent, currentKey);
					if (currentKey.contains(DataProcessingConstants.WES)) {
						currentKey = currentKey.replace(DataProcessingConstants.WES, "WES");
					}
					sendDataToHazelCast(currentKey, dataList);
					keyIterator.remove();
					if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WORKER_SUMMARY)) {
						handleMandatoryFieldGroup();
					}
				}
			}
		}
		dataStack.setLength(0);
	}

	private static void handleMandatoryFieldGroup() {
		handleMandatoryFieldGroupCountryCode();
		final LinkedList<Event> tempMandatoryRecord = new LinkedList<>();
		tempMandatoryRecord.add(new Event().withData(currentWorkerID.toString()));
		Stream.of(MandatoryRecordDataFields.values())
				.forEach(mandatoryFields -> tempMandatoryRecord
						.add(new Event().withName(mandatoryFields.getMandatoryRecordField())
								.withData(effectiveStackSummaryDataMap.get(mandatoryFields.name()))));
		if (tempMandatoryRecord.size() != DataProcessingConstants.MANDATORY_FIELD_GROUP_SIZE) {
			for (int i = tempMandatoryRecord.size() - 1; i < (DataProcessingConstants.MANDATORY_FIELD_GROUP_SIZE
					- DataProcessingConstants.INDEX_ONE); i++) {
				tempMandatoryRecord.add(new Event().withData(DataProcessingConstants.BLANK_STRING));
			}
		}
		tempMandatoryRecord.get(0).setName(DataProcessingConstants.SYS_CEID);
		sendDataToHazelCast(DataProcessingConstants.MANDATORY_FIELD_GROUP_KEY, tempMandatoryRecord);
	}

	private static void handleMandatoryFieldGroupCountryCode() {
		if (effectiveStackSummaryDataMap.containsKey(MandatoryRecordDataFields.Pay_Group_Country.name())
				&& (effectiveStackSummaryDataMap.get(MandatoryRecordDataFields.Pay_Group_Country.name())
						.length() == 3)) {
			return;
		}
		final Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("src/resources/country_iso_code_mapping.properties"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		final Optional<String> gccCountryISOCode = Optional.ofNullable((String) properties
				.get(effectiveStackSummaryDataMap.get(MandatoryRecordDataFields.Pay_Group_Country.name()).trim()));
		if (gccCountryISOCode.isPresent()) {
			effectiveStackSummaryDataMap.replace(MandatoryRecordDataFields.Pay_Group_Country.name(),
					gccCountryISOCode.get());
		}
	}

	private static void manipulateData(final LinkedList<Event> dataList, final Event currentCustomEvent,
			final String currentKey) {
		if (currentKey.equalsIgnoreCase(DataProcessingConstants.WES_SUMMARY)) {
			dataList.add(0, new Event().withName(DataProcessingConstants.SYS_CEID).withData(dataList.get(0).getData()));
		}
		if (StringUtils.isNotEmpty(currentEffectiveDateTime.toString())) {
			dataList.add(0, new Event().withName(DataProcessingConstants.EFFECTIVE_MOMENT)
					.withData(currentEffectiveDateTime.toString()));
		}
		if (StringUtils.isNotEmpty(currentWorkerID.toString())) {
			dataList.add(0,
					new Event().withName(DataProcessingConstants.SYS_CEID).withData(currentWorkerID.toString().trim()));
		}
		final Optional<String> operationalAttribute = Optional
				.ofNullable(attributeMap.get(currentCustomEvent.getName()));
		if (operationalAttribute.isPresent()) {
			dataList.add(2, new Event().withName(currentKey + "__" + "operation").withData(operationalAttribute.get()));
			attributeMap.remove(currentCustomEvent.getName());
		} else if (currentKey.contains(DataProcessingConstants.EFFECTIVE_CHANGE)
				&& !currentKey.contains(DataProcessingConstants.DERIVED_EVENT_CODE)) {
			dataList.add(2, new Event().withName(currentKey + "__" + "operation")
					.withData(DataProcessingConstants.BLANK_STRING));
		}
	}

	private static void sendDataToHazelCast(final String currentFieldGroup, final LinkedList<Event> dataList) {
		Map<String, String> currentDataMap = new HashMap<>();
		currentDataMap.put(currentFieldGroup, currentFieldGroup);
		dataList.forEach(event -> {
			if (event.getName() != null) {
				currentDataMap.put(event.getName(), event.getData());
			}
		});
		CDSDerivationUtil.processOnCDSData(currentDataMap, extractedCDSData, currentFieldGroup);
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