package com.ashwani.special.backup.xmlprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.stream.Stream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

public class XMLDataProcessingV9 {

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

	private static final File xmlFile = new File(
			"C:/Users/solanka/Desktop/PECI/WDI_TD3002_20170224020830_UKPECI_HRMD01_DUT8G2I.xml");
	private static List<String> fieldGroupStack = new ArrayList<>();
	private static StringBuilder dataStack = new StringBuilder();
	private static Map<String, LinkedList<String>> dataMap = new LinkedHashMap<>();
	private static StringBuilder currentEffectiveDateTime = new StringBuilder();
	private static final StringBuilder currentWorkerID = new StringBuilder();
	private static Map<String, String> effectiveStackSummaryDataMap = new HashMap<>();
	private static Map<String, String> attributeMap = new HashMap<>();
	private static int counter = 1;

	public static void main(final String[] args) throws FileNotFoundException, XMLStreamException {
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
		processDataMap(currentCustomEvent, tempFieldGroup);
	}

	private static void processDataMap(final Event currentCustomEvent, final String tempFieldGroup) {
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
					manipulateData(dataList, currentCustomEvent, currentKey);
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
			final String currentKey) {
		if (currentKey.equalsIgnoreCase(DataProcessingConstants.WES_SUMMARY)) {
			dataList.add(0, dataList.get(0));
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
		tempData.replace(0, 23, "WES");
		System.out.println(counter + "@" + tempData.toString());
		counter++;
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