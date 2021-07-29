package com.ashwani.special.backup.xmlprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Stream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PECIXMLDataProcessor {

	enum FieldGroupOperations {
		isUpdated, isAdded, isDeleted
	}

	enum PersistentFields {
		Effective_Moment, Derived_Event_Code
	}

	enum NormalizationType {
		NONE, TARGET, TRIGGER
	}

	SpecialCasesProcessor specialCasesProcessor = SpecialCasesProcessor.valueOf(new SpecialCasesProcessor());
	private ArrayList<Event> currentEffectiveStackSummaryData = new ArrayList<>();
	/**
	 * Key - Triggering FieldGroup Value - Target FieldG`roup
	 */
	Map<String, String> mapPersistentFields = new TreeMap<>();
	private String effectiveStackReferenceId = new String();
	private final List<DataNormalizationObject> lstDataNormalizationCases = new ArrayList<>();
	private final List<SameLevelMerge> lstSameLevelMergeCases = new ArrayList<>();

	public static void main(final String[] args) throws XMLStreamException, IOException {
		final File xmlFile = new File("C:/Users/solanka/Desktop/WDI_TD3002_20170607045839_SGPECI_HRMD01_DUT8G2I.xml");
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		final XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlFile));
		final PECIXMLDataProcessor processor = new PECIXMLDataProcessor();
		processor.processPECIXml(eventReader);
	}

	public void processPECIXml(final XMLEventReader eventReader) throws XMLStreamException, IOException {
		final List<String> fieldGroupStack = new ArrayList<>();
		final StringBuilder dataStack = new StringBuilder();
		final Map<String, ArrayList<Event>> dataMap = new LinkedHashMap<>();
		final StringBuilder currentEmployeeID = new StringBuilder();
		final Map<String, String> effectiveStackSummaryDataMap = new HashMap<>();
		final Map<String, String> attributeMap = new HashMap<>();
		final Map<String, List<CustomField>> extractedCDSData = CDSDerivationUtil.extractCDS();
		loadNormalizationMappings();
		loadSameLevelMergeCases();
		while (eventReader.hasNext()) {
			final XMLEvent currentEvent = eventReader.nextEvent();
			final Event currentCustomEvent = generateCustomEvent(currentEvent);
			if ((currentCustomEvent != null) && currentCustomEvent.isStartEvent()) {
				handleStartEvent(currentCustomEvent, attributeMap, fieldGroupStack);
			}
			if ((currentCustomEvent != null) && currentCustomEvent.isData()) {
				handleDataEvent(fieldGroupStack, dataStack, attributeMap, currentCustomEvent);
			}
			if ((currentCustomEvent != null) && currentCustomEvent.isEndEvent()) {
				handleEndEvent(currentCustomEvent, fieldGroupStack, dataStack, dataMap, currentEmployeeID,
						effectiveStackSummaryDataMap, attributeMap, extractedCDSData);
			}
		}
	}

	private void loadSameLevelMergeCases() {
		final Properties properties = new Properties();
		InputStream fileStream = null;
		try {
			fileStream = new FileInputStream("src/main/resources/fieldFlatteningMappings.properties");
			properties.load(fileStream);
			for (final Entry<Object, Object> entry : properties.entrySet()) {
				final SameLevelMerge sameLevelMerge = new SameLevelMerge().withTargetField(entry.getKey().toString());
				final String[] arrTriggeringFields = entry.getValue().toString()
						.split(DataProcessingConstants.TRIGGER_FIELD_SEPARATOR);
				sameLevelMerge.setLstTriggeringFields(Arrays.asList(arrTriggeringFields));
				lstSameLevelMergeCases.add(sameLevelMerge);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (fileStream != null) {
				try {
					fileStream.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * <p>
	 * Performs custom logic for PECI xml files for all data consisting elements
	 * found Following are the actions taken: <br>
	 * 1.) Add the data element on dataStack to be used later for preparing a
	 * fieldGroup's data. <br>
	 * 2.) Check if the current data Element comes enclosed in a key whose attribute
	 * has isDeleted. If this is the case, then empty the dataStack so the
	 * particular field would be empty in the final fieldGroup with data.
	 * </p>
	 *
	 * @param fieldGroupStack
	 * @param dataStack
	 * @param attributeMap
	 * @param currentCustomEvent
	 */
	private void handleDataEvent(final List<String> fieldGroupStack, final StringBuilder dataStack,
			final Map<String, String> attributeMap, final Event currentCustomEvent) {
		dataStack.append(currentCustomEvent.getData());
		final String currentDataKey = fieldGroupStack.get(fieldGroupStack.size() - DataProcessingConstants.INDEX_ONE);
		if ((dataStack.length() > 0) && !attributeMap.isEmpty() && attributeMap.containsKey(currentDataKey)
				&& attributeMap.get(currentDataKey).equalsIgnoreCase(DataProcessingConstants.DELETED_ATTRIBUTE)) {
			dataStack.setLength(DataProcessingConstants.INDEX_ZERO);
			attributeMap.remove(currentDataKey);
		}
	}

	/**
	 * <p>
	 * This method would load fieldGroups normalization cases into
	 * lstDataNormalizationCases.<br>
	 * By Normalization in PECI we mean to copy some particular unique fields from a
	 * parent fieldGroup to a number of childFieldGroups.<br>
	 * Type of list is DataNormalizationObject. Find object's description in the
	 * class.
	 * </p>
	 */
	private void loadNormalizationMappings() {
		final Properties properties = new Properties();
		InputStream fileStream = null;
		try {
			fileStream = new FileInputStream("src/main/resources/normalizationMappings.properties");
			properties.load(fileStream);
			properties.entrySet().stream().forEach(normalizationMappings -> {
				lstDataNormalizationCases.add(extractNormalizationObject(normalizationMappings));
			});
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (fileStream != null) {
				try {
					fileStream.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private DataNormalizationObject extractNormalizationObject(Entry<Object, Object> normalizationMappings) {
		final List<String> keyList = Arrays.asList(((String) normalizationMappings.getKey()).split(","));
		DataNormalizationObject tempNormalizationObject = null;
		if (CollectionUtils.isNotEmpty(keyList)) {
			tempNormalizationObject = new DataNormalizationObject().withTriggeringFieldGroup(keyList.get(0))
					.withTargetFieldGroups(keyList.subList(DataProcessingConstants.INDEX_ONE, keyList.size()))
					.withNormalizingFields(Arrays.asList(((String) normalizationMappings.getValue()).split(",")));
		}
		return tempNormalizationObject;
	}

	/**
	 * <p>
	 * Performs custom logic for PECI XML files for all the start elements.
	 * Following are the actions taken: <br>
	 * . 1.) Read attribute from event object and collect if they match a particular
	 * criteria for a function later on.<br>
	 * 2.) Generate UUID for every effectiveStack coming in file and store it in a
	 * global variable to be put into every fieldGroup data.<br>
	 * 3.) Logic to check if the first element of file is a valid tag element from a
	 * PECI XML file.<br>
	 * 4.) <b>Most Important</b>: Add the start elements name on fieldGroupStack so
	 * that it can be used later to generate exact fieldGroup name.
	 * </p>
	 *
	 * @param currentCustomEvent
	 * @param attributeMap
	 * @param fieldGroupStack
	 */
	private void handleStartEvent(final Event currentCustomEvent, final Map<String, String> attributeMap,
			final List<String> fieldGroupStack) {
		if (!currentCustomEvent.getAttributes().isEmpty()) {
			currentCustomEvent.getAttributes().forEach(attribute -> {
				final boolean anyMatch = Stream.of(FieldGroupOperations.values()).anyMatch(
						operationAttribute -> operationAttribute.name().equalsIgnoreCase(attribute.getName()));
				if (anyMatch) {
					attributeMap.put(currentCustomEvent.getName(), attribute.getName().substring(2).toLowerCase());
				}
			});
		}
		// EffectiveStackReferenceId starts at every new Effective_Stack
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WES)) {
			effectiveStackReferenceId = getEffectiveStackReferenceId();
		}
		if ((fieldGroupStack.size() == DataProcessingConstants.INDEX_ZERO)
				&& !(currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WES_AGGREGATE_DOC_LEVEL)
						|| currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WES))) {
			throw new RuntimeException("Invalid file uploaded.");
		}
		fieldGroupStack.add(currentCustomEvent.getName());
	}

	/**
	 * <p>
	 * Performs custom logic for PECI XML files for all the end elements. Following
	 * actions are taken:<br>
	 * 1.) Separate persistent fields like Derived_Event_Code,Effective_Moment etc
	 * in different dataStructures. Persistent fields are those fields which has
	 * data to be used later in multiple/all fieldGroups.<br>
	 * 2.) Identify the EmployeeID so that it can be <b>used as CEID</b> for GCC
	 * storage data.<br>
	 * 3.) Check if a worker's data is finished and if some record is still left to
	 * be merged/normalized. If so then process the remaining data. This can occur
	 * when we are expecting further data for merging/normalizing but required data
	 * doesn't come.<br>
	 * 4.) Identify the currentFieldGroup,gather the data from dataStuck and put it
	 * in dataMap for further processing.<br>
	 * </p>
	 *
	 * @throws IOException
	 */
	private void handleEndEvent(final Event currentCustomEvent, final List<String> fieldGroupStack,
			final StringBuilder dataStack, final Map<String, ArrayList<Event>> dataMap,
			final StringBuilder currentEmployeeID, final Map<String, String> effectiveStackSummaryDataMap,
			final Map<String, String> attributeMap, final Map<String, List<CustomField>> extractedCDSData)
			throws IOException {

		final Optional<PersistentFields> matchingField = Stream.of(PersistentFields.values())
				.filter(persistentField -> persistentField.name().equalsIgnoreCase(currentCustomEvent.getName()))
				.findFirst();

		if (matchingField.isPresent()) {
			mapPersistentFields.put(currentCustomEvent.getName(), dataStack.toString().trim());
		}

		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EMPLOYEE_ID)) {
			currentEmployeeID.append(dataStack.toString().trim());
		}
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WORKER)) {
			currentEmployeeID.setLength(DataProcessingConstants.INDEX_ZERO);
			final List<SpecialPECICase> lstSpecialCasesToProcess = specialCasesProcessor.clearCurrentEmployeeCases();
			if ((lstSpecialCasesToProcess != null) && !lstSpecialCasesToProcess.isEmpty()) {
				lstSpecialCasesToProcess.forEach(specialCasesLeftToProcess -> {
					normalizationFunction(specialCasesLeftToProcess.getTriggeringFieldGroup(),
							specialCasesLeftToProcess.getDataList(), extractedCDSData);
				});
			}
		}
		/**
		 * Remove the fieldGroup's latest keyword so that the correct fieldGroup can be
		 * derived upon in string tempFieldGroup.
		 */
		fieldGroupStack.remove(currentCustomEvent.getName());
		String tempFieldGroup = null;
		if ((dataStack.toString().trim().length() != DataProcessingConstants.INDEX_ZERO)
				&& !((dataStack.length() == DataProcessingConstants.INDEX_ONE)
						&& dataStack.toString().contains(DataProcessingConstants.NEW_LINE))) {
			tempFieldGroup = generateFieldGroupName(fieldGroupStack);

			if (dataMap.containsKey(tempFieldGroup)) {
				final ArrayList<Event> dataList = dataMap.get(tempFieldGroup);
				dataList.add(new Event().withName(currentCustomEvent.getName().trim())
						.withData(dataStack.toString().trim()));
			} else {
				final ArrayList<Event> tempData = new ArrayList<>();
				tempData.add(new Event().withName(currentCustomEvent.getName().trim())
						.withData(dataStack.toString().trim()));
				dataMap.put(tempFieldGroup, tempData);
			}
		}
		processDataMap(currentCustomEvent, tempFieldGroup, dataStack, dataMap, currentEmployeeID,
				effectiveStackSummaryDataMap, attributeMap, extractedCDSData);
	}

	/**
	 * <p>
	 * Here we identify when to make a new fieldGroup. It may either be just a
	 * regular data element or a new fieldGroup. Following actions are also taken:
	 * <br>
	 * 1.) Change the Effective_Change fieldGroup's name to Derived_Event_Code. Just
	 * the fieldGroup name.
	 * </p>
	 *
	 * @throws IOException
	 */
	private void processDataMap(final Event currentCustomEvent, final String tempFieldGroup,
			final StringBuilder dataStack, final Map<String, ArrayList<Event>> dataMap,
			final StringBuilder currentEmployeeID, final Map<String, String> effectiveStackSummaryDataMap,
			final Map<String, String> attributeMap, final Map<String, List<CustomField>> extractedCDSData)
			throws IOException {
		if ((dataStack.toString().trim().length() == DataProcessingConstants.INDEX_ZERO)
				|| ((dataStack.length() == DataProcessingConstants.INDEX_ONE) && dataStack.toString().contains("\n")
						&& (tempFieldGroup == null))) {
			final Iterator<String> keyIterator = dataMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String currentKey = keyIterator.next();
				if (currentKey.contains(currentCustomEvent.getName())) {
					final ArrayList<Event> dataList = dataMap.get(currentKey);
					if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_CHANGE)) {
						currentKey = currentKey.concat(DataProcessingConstants.DERIVED_EVENT_CODE_WITH_SEPARATOR);
					}
					/**
					 * Shorten the FieldGroup's names by replacing
					 * Workers_Effective_Stack/Workers_Effective_Stack_Aggregate with WES
					 */
					if (currentKey.contains(DataProcessingConstants.WES)) {
						currentKey = currentKey.replace(DataProcessingConstants.WES,
								DataProcessingConstants.WES_SHORTENED);
					}
					if (currentKey.contains(DataProcessingConstants.WES_AGGREGATE)) {
						currentKey = currentKey.replace(DataProcessingConstants.WES_AGGREGATE,
								DataProcessingConstants.BLANK_STRING);
					}
					/**
					 * Error out if Summary Pay Group Country did not match with CDS country.
					 */
					if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.SUMMARY)) {
						final Optional<Event> payGroupCountry = dataList.stream().filter(
								event -> event.getName().equalsIgnoreCase(DataProcessingConstants.PAY_GROUP_COUNTRY))
								.findFirst();
						errorOutForNonMatchingCountry(payGroupCountry);
					}
					manipulateData(dataList, currentCustomEvent, currentKey, currentEmployeeID, attributeMap);
					preCDSDataProcess(currentKey, dataList, currentCustomEvent, extractedCDSData);
					keyIterator.remove();
					if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.WORKER_SUMMARY)) {
						handleStackSummaryForWorker(currentCustomEvent, currentEmployeeID, effectiveStackSummaryDataMap,
								extractedCDSData);
					}
				}
			}
		}
		dataStack.setLength(DataProcessingConstants.INDEX_ZERO);
	}

	@SuppressWarnings("unchecked")
	private void errorOutForNonMatchingCountry(final Optional<Event> payGroupCountry) throws IOException {
		final Map<String, Object> cdsMap = extractCDSFromJsonFile();
		final List<Map<String, Object>> countryMapList = (List<Map<String, Object>>) cdsMap
				.get(DataProcessingConstants.COUNTRY);
		final Optional<Map<String, Object>> findAny = countryMapList.stream()
				.filter(map -> map.get("id").equals(payGroupCountry.get().getData())).findAny();
		if (!findAny.isPresent()) {
			throw new RuntimeException("Paygroup Country should be same as CDS country");
		}

	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> extractCDSFromJsonFile() throws IOException {
		final InputStream fileStream = new FileInputStream(new File("src/main/resources/cds.json"));
		final ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(fileStream, Map.class);
	}

	/**
	 * <p>
	 * Here we just manipulate some parts of dataList. Following manipulations are
	 * done here:<br>
	 * 1.) Adding Worker_ID as CE_ID in every fieldGroup.(Even in
	 * Effective_Stack_Summaryy)<br>
	 * 2.) Adding Effective_Moment & Derived_Event_Code in every fieldGroup.<br>
	 * 3.) Checking if there is an attribute for the current fieldGroup and if there
	 * is then add that attribute's value in the operations field.<br>
	 * 4.) Handling Effective_Stack_ReferenceID in every fieldGroup of that
	 * particular Effective_Stack.
	 * </p>
	 *
	 * @param attributeMap
	 */
	private void manipulateData(final ArrayList<Event> dataList, final Event currentCustomEvent,
			final String currentKey, final StringBuilder currentEmployeeID, final Map<String, String> attributeMap) {
		if (currentKey.equalsIgnoreCase(DataProcessingConstants.WES_SUMMARY)) {
			dataList.add(new Event().withName(DataProcessingConstants.SYS_CEID)
					.withData(dataList.get(DataProcessingConstants.INDEX_ZERO).getData()));
		}
		if (mapPersistentFields.containsKey(DataProcessingConstants.EFFECTIVE_MOMENT)) {
			dataList.add(new Event().withName(DataProcessingConstants.EFFECTIVE_MOMENT)
					.withData(mapPersistentFields.get(DataProcessingConstants.EFFECTIVE_MOMENT)));
		}
		if (mapPersistentFields.containsKey(DataProcessingConstants.DERIVED_EVENT_CODE)) {
			dataList.add(new Event().withName(DataProcessingConstants.DERIVED_EVENT_CODE)
					.withData(mapPersistentFields.get(DataProcessingConstants.DERIVED_EVENT_CODE)));
		}

		if (StringUtils.isNotEmpty(currentEmployeeID.toString())) {
			dataList.add(new Event().withName(DataProcessingConstants.SYS_CEID)
					.withData(currentEmployeeID.toString().trim()));
		}

		/*
		 * Checking if there is an attribute for the current fieldGroup and if there is
		 * then add that attribute's value in the operations field.
		 */
		final Optional<String> operationalAttribute = Optional
				.ofNullable(attributeMap.get(currentCustomEvent.getName()));
		if (operationalAttribute.isPresent()) {
			dataList.add(new Event().withName(DataProcessingConstants.OPERATION).withData(operationalAttribute.get()));
			attributeMap.remove(currentCustomEvent.getName());
		} else if (currentKey.contains(DataProcessingConstants.EFFECTIVE_CHANGE)
				&& !currentKey.contains(DataProcessingConstants.DERIVED_EVENT_CODE_WITH_SEPARATOR)) {
			dataList.add(new Event().withName(DataProcessingConstants.OPERATION)
					.withData(DataProcessingConstants.BLANK_STRING));
		}
		/*
		 * Adding unique effective stack reference on every data list so that every set
		 * of fieldGroup data can be traced back to the Effective_Stack its coming from.
		 */
		dataList.add(new Event().withName(DataProcessingConstants.EFFECTIVE_STACK_REFERENCE_ID)
				.withData(effectiveStackReferenceId));
	}

	private void handleStackSummaryForWorker(final Event currentCustomEvent, final StringBuilder currentEmployeeID,
			final Map<String, String> effectiveStackSummaryDataMap,
			final Map<String, List<CustomField>> extractedCDSData) {
		currentEffectiveStackSummaryData.stream()
				.filter(effectiveStackElement -> effectiveStackElement.getName()
						.equalsIgnoreCase(DataProcessingConstants.SYS_CEID))
				.findFirst().get().setData(currentEmployeeID.toString());
		final String effectiveStackSummaryKey = DataProcessingConstants.WES_SUMMARY
				.replaceAll(DataProcessingConstants.WES, DataProcessingConstants.WES_SHORTENED);
		preCDSDataProcess(effectiveStackSummaryKey, currentEffectiveStackSummaryData, currentCustomEvent,
				extractedCDSData);
	}

	private void preCDSDataProcess(final String currentFieldGroup, final ArrayList<Event> dataList,
			final Event currentCustomEvent, final Map<String, List<CustomField>> extractedCDSData) {
		if (currentCustomEvent.getName().equalsIgnoreCase(DataProcessingConstants.SUMMARY)) {
			currentEffectiveStackSummaryData = dataList;
			final Optional<Event> effectiveStackReferenceEvent = currentEffectiveStackSummaryData.parallelStream()
					.filter(events -> events.getName()
							.equalsIgnoreCase(DataProcessingConstants.EFFECTIVE_STACK_REFERENCE_ID))
					.findFirst();
			if (effectiveStackReferenceEvent.isPresent()) {
				effectiveStackReferenceEvent.get().setData(effectiveStackReferenceId);
			} else {
				currentEffectiveStackSummaryData
						.add(new Event().withName(DataProcessingConstants.EFFECTIVE_STACK_REFERENCE_ID)
								.withData(effectiveStackReferenceId));
			}
			return;
		}

		// Search for same level trigger
		if (checkForSameLevelMergeTriggeringCase(currentFieldGroup, dataList)) {
			return;
		}

		// Search for same level target
		if (checkForSameLevelMergeTarget(currentFieldGroup, dataList, extractedCDSData)) {
			return;
		}

		normalizationFunction(currentFieldGroup, dataList, extractedCDSData);
	}

	private Boolean checkForSameLevelMergeTarget(final String currentFieldGroup, final ArrayList<Event> dataList,
			final Map<String, List<CustomField>> extractedCDSData) {
		for (final SameLevelMerge sameLevelMergeCases : lstSameLevelMergeCases) {
			if (sameLevelMergeCases.getTargetField().equalsIgnoreCase(currentFieldGroup)) {
				final SameLevelMerge sameLevelMergedCase = specialCasesProcessor.mergeSameLevelCase(currentFieldGroup,
						dataList, sameLevelMergeCases);
				if (sameLevelMergedCase.getDataList() != null) {
					sameLevelMergedCase.getDataList().forEach(mergedDataLists -> {
						normalizationFunction(currentFieldGroup, mergedDataLists, extractedCDSData);
					});
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private boolean checkForSameLevelMergeTriggeringCase(final String currentFieldGroup,
			final ArrayList<Event> dataList) {
		for (final SameLevelMerge sameLevelMergeCases : lstSameLevelMergeCases) {
			final Optional<String> optionalSameLevelTriggerCase = sameLevelMergeCases.getLstTriggeringFields().stream()
					.filter(sameLevelTriggeringField -> sameLevelTriggeringField.equalsIgnoreCase(currentFieldGroup))
					.findFirst();
			if (optionalSameLevelTriggerCase.isPresent()) {
				specialCasesProcessor.prepareForSameLevelMerge(currentFieldGroup, dataList);
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private void normalizationFunction(final String currentFieldGroup, final ArrayList<Event> dataList,
			final Map<String, List<CustomField>> extractedCDSData) {
		final NormalizationType currentFieldGroupNormalizationType = getCurrentFieldGroupNormalizationType(
				currentFieldGroup);
		if (NormalizationType.NONE.equals(currentFieldGroupNormalizationType)) {
			processOnCDS(currentFieldGroup, dataList, extractedCDSData);
			return;
		}
		if (NormalizationType.TARGET.equals(currentFieldGroupNormalizationType)) {
			specialCasesProcessor.pushNormalizationTargetObject(currentFieldGroup, dataList);
			return;
		} else {
			final Optional<DataNormalizationObject> normalizationObject = lstDataNormalizationCases.stream()
					.filter(normalizationCases -> normalizationCases.getTriggeringFieldGroup()
							.equalsIgnoreCase(currentFieldGroup))
					.findFirst();
			final Map<String, List<ArrayList<Event>>> normalizedDataMultiMap = specialCasesProcessor
					.pushNormalizationTriggeringObject(currentFieldGroup, dataList,
							normalizationObject.isPresent() ? normalizationObject.get() : null);
			if (!normalizedDataMultiMap.isEmpty()) {
				normalizedDataMultiMap.entrySet().forEach(entry -> {
					entry.getValue().forEach(lstProcessedData -> {
						processOnCDS(entry.getKey(), lstProcessedData, extractedCDSData);
					});
				});
			}
		}
	}

	private NormalizationType getCurrentFieldGroupNormalizationType(String currentFieldGroup) {
		for (int i = 0; i < lstDataNormalizationCases.size(); i++) {
			final DataNormalizationObject dataNormalizationObject = lstDataNormalizationCases.get(i);
			if (dataNormalizationObject.getTriggeringFieldGroup().equalsIgnoreCase(currentFieldGroup)) {
				return NormalizationType.TRIGGER;
			}
			final boolean targetFound = dataNormalizationObject.getLstTargetFieldGroups().stream()
					.anyMatch(targetFieldGroup -> targetFieldGroup.equalsIgnoreCase(currentFieldGroup));
			if (targetFound) {
				return NormalizationType.TARGET;
			}
		}
		return NormalizationType.NONE;
	}

	private void processOnCDS(final String currentFieldGroup, final ArrayList<Event> dataList,
			final Map<String, List<CustomField>> extractedCDSData) {
		final Map<String, String> currentDataMap = new HashMap<>();
		currentDataMap.put(currentFieldGroup, currentFieldGroup);
		dataList.forEach(event -> {
			if (event.getName() != null) {
				currentDataMap.put(event.getName(), event.getData());
			}
		});

		final String processOnCDSData = CDSDerivationUtil.processOnCDSData(currentDataMap, extractedCDSData,
				currentFieldGroup);
		if (processOnCDSData != null) {
			System.out.println(processOnCDSData);
		}
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param currentEvent
	 * @return A custom object which makes it easier to read XMLEvent tags,
	 *         including data,key and attributes.
	 */
	@SuppressWarnings("unchecked")
	private Event generateCustomEvent(final XMLEvent currentEvent) {
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
			event = new Event().withData(currentEvent.asCharacters().getData().trim());
			event.setDataFlag(Boolean.TRUE);
		} else if (currentEvent.isEndElement()) {
			event = new Event().withName(currentEvent.asEndElement().getName().getLocalPart());
			event.setEndEventFlag(Boolean.TRUE);
		}
		return event;
	}

	private String generateFieldGroupName(final List<String> fieldGroupList) {
		final StringBuilder fieldGroupName = new StringBuilder();
		fieldGroupList.forEach(fieldGroup -> {
			if (fieldGroupName.length() == DataProcessingConstants.INDEX_ZERO) {
				fieldGroupName.append(fieldGroup);
			} else {
				fieldGroupName.append(DataProcessingConstants.FIELD_GROUP_SEPARATOR + fieldGroup);
			}
		});
		return fieldGroupName.toString().trim();
	}

	private String getEffectiveStackReferenceId() {
		final UUID effectiveStackReferenceId = UUID.randomUUID();
		return effectiveStackReferenceId.toString();
	}
}
