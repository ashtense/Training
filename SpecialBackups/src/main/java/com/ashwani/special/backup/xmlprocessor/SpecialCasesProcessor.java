package com.ashwani.special.backup.xmlprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpecialCasesProcessor {

	private final List<SpecialPECICase> lstTriggeringCases = new ArrayList<>();
	private final List<SpecialPECICase> lstTargetCases = new ArrayList<>();
	private final Map<String, List<ArrayList<Event>>> alreadyPushedNormalizationTargetMulitMap = new HashMap<>();
	private final Map<String, List<ArrayList<Event>>> alreadyPushedSameLevelTriggerMulitMap = new HashMap<>();

	public void pushNormalizationTargetObject(String currentFieldGroup, ArrayList<Event> dataList) {
		if (alreadyPushedNormalizationTargetMulitMap.containsKey(currentFieldGroup)) {
			alreadyPushedNormalizationTargetMulitMap.get(currentFieldGroup).add(dataList);
		} else {
			final List<ArrayList<Event>> tempEventArray = new ArrayList<>();
			tempEventArray.add(dataList);
			alreadyPushedNormalizationTargetMulitMap.put(currentFieldGroup, tempEventArray);
		}
	}

	public Map<String, List<ArrayList<Event>>> pushNormalizationTriggeringObject(String currentFieldGroup,
			ArrayList<Event> dataList, DataNormalizationObject dataNormalizingObject) {
		final Map<String, List<ArrayList<Event>>> finalNormalizedDataMultiMap = new HashMap<>();

		if (dataNormalizingObject == null) {
			return finalNormalizedDataMultiMap;
		}

		final ArrayList<Event> incomingNormalizingData = new ArrayList<>();

		// Filtering data required for normalization
		dataNormalizingObject.getLstNormalizingFields().forEach(normalizingObject -> {
			final Optional<Event> matchingNormalizingData = dataList.stream()
					.filter(dataEvent -> dataEvent.getName().equalsIgnoreCase(normalizingObject)).findFirst();
			if (matchingNormalizingData.isPresent()) {
				incomingNormalizingData.add(matchingNormalizingData.get());
			}
		});

		// Final target data preparation.
		dataNormalizingObject.getLstTargetFieldGroups().forEach(targetFieldGroups -> {
			if (alreadyPushedNormalizationTargetMulitMap.containsKey(targetFieldGroups)) {
				final List<ArrayList<Event>> lstAlreadyPushedDataArray = alreadyPushedNormalizationTargetMulitMap
						.get(targetFieldGroups);
				processAlreadyPushedDataList(lstAlreadyPushedDataArray, dataNormalizingObject, incomingNormalizingData);
				finalNormalizedDataMultiMap.put(targetFieldGroups, lstAlreadyPushedDataArray);
			}
		});

		alreadyPushedNormalizationTargetMulitMap.clear();
		finalNormalizedDataMultiMap.put(currentFieldGroup, Arrays.asList(dataList));
		return finalNormalizedDataMultiMap;
	}

	private void processAlreadyPushedDataList(List<ArrayList<Event>> lstAlreadyPushedDataArray,
			DataNormalizationObject dataNormalizingObject, ArrayList<Event> incomingNormalizingData) {
		lstAlreadyPushedDataArray.parallelStream().forEach(dataList -> {
			dataList.addAll(getNormalizedDataForCurrentTarget(dataNormalizingObject.getTriggeringFieldGroup(),
					incomingNormalizingData));
		});
	}

	private ArrayList<Event> getNormalizedDataForCurrentTarget(String triggeringFieldGroup,
			ArrayList<Event> incomingNormalizingData) {
		final ArrayList<Event> lstCurrentTriggerBasedData = new ArrayList<>();
		incomingNormalizingData.forEach(normalizerData -> {
			try {
				final Event clonedNormalizerEvent = normalizerData.clone();
				clonedNormalizerEvent.setName(triggeringFieldGroup + DataProcessingConstants.CATALOG_SAVE_SEPARATOR
						+ normalizerData.getName());
				lstCurrentTriggerBasedData.add(clonedNormalizerEvent);
			} catch (final CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
		return lstCurrentTriggerBasedData;
	}

	public static SpecialCasesProcessor valueOf(final SpecialCasesProcessor processor) {
		return processor;
	}

	public List<SpecialPECICase> clearCurrentEmployeeCases() {
		final List<SpecialPECICase> lstTempCases = new ArrayList<>();
		if (lstTargetCases.isEmpty()) {
			lstTempCases.addAll(lstTriggeringCases);
			lstTriggeringCases.clear();
			lstTargetCases.clear();
			return lstTempCases;
		}
		alreadyPushedNormalizationTargetMulitMap.clear();
		alreadyPushedSameLevelTriggerMulitMap.clear();
		lstTriggeringCases.clear();
		lstTargetCases.clear();
		return lstTempCases;
	}

	public void prepareForSameLevelMerge(String triggeringFieldGroup, ArrayList<Event> dataList) {
		if (alreadyPushedSameLevelTriggerMulitMap.containsKey(triggeringFieldGroup)) {
			alreadyPushedSameLevelTriggerMulitMap.get(triggeringFieldGroup).add(dataList);
		} else {
			final List<ArrayList<Event>> tempDataList = new ArrayList<>();
			tempDataList.add(dataList);
			alreadyPushedSameLevelTriggerMulitMap.put(triggeringFieldGroup, tempDataList);
		}
	}

	public SameLevelMerge mergeSameLevelCase(String targetFieldGroup, ArrayList<Event> dataList,
			SameLevelMerge intendedCase) {
		final List<ArrayList<Event>> outGoingDataList = new ArrayList<>();
		for (final String intendedTriggeringField : intendedCase.getLstTriggeringFields()) {
			if (alreadyPushedSameLevelTriggerMulitMap.containsKey(intendedTriggeringField)) {
				final List<ArrayList<Event>> lstTriggeringDataList = alreadyPushedSameLevelTriggerMulitMap
						.get(intendedTriggeringField);
				if (lstTriggeringDataList.size() > 1) {
					if (outGoingDataList.isEmpty()) {
						lstTriggeringDataList.forEach(incommingDataList -> {
							final ArrayList<Event> lstTempDataList = new ArrayList<>();
							lstTempDataList.addAll(dataList);
							prepareMergingData(lstTempDataList, incommingDataList, intendedTriggeringField);
							outGoingDataList.add(lstTempDataList);
						});
					} else {
						final List<ArrayList<Event>> tempOutGoingDataList = new ArrayList<>();
						outGoingDataList.forEach(existingOutGoingData -> {
							lstTriggeringDataList.forEach(incommingDataList -> {
								final ArrayList<Event> lstTempDataList = new ArrayList<>();
								lstTempDataList.addAll(existingOutGoingData);
								prepareMergingData(lstTempDataList, incommingDataList, intendedTriggeringField);
								tempOutGoingDataList.add(lstTempDataList);
							});
						});
						outGoingDataList.clear();
						outGoingDataList.addAll(tempOutGoingDataList);
					}
				} else {
					if (!outGoingDataList.isEmpty()) {
						outGoingDataList.forEach(existingOutGoingData -> {
							prepareMergingData(existingOutGoingData, lstTriggeringDataList.get(0),
									intendedTriggeringField);
						});
					} else {
						final ArrayList<Event> lstTempDataList = new ArrayList<>();
						lstTempDataList.addAll(dataList);
						prepareMergingData(lstTempDataList, lstTriggeringDataList.get(0), intendedTriggeringField);
						outGoingDataList.add(lstTempDataList);
					}
				}
			}
		}
		intendedCase.setDataList(outGoingDataList.isEmpty() ? null : outGoingDataList);
		alreadyPushedSameLevelTriggerMulitMap.clear();
		return intendedCase;
	}

	private void prepareMergingData(ArrayList<Event> lstTempDataList, ArrayList<Event> incommingDataList,
			String intendedTriggeringField) {
		incommingDataList.forEach(dataEvent -> {
			try {
				final Event tempDataEvent = dataEvent.clone();
				tempDataEvent.setName(intendedTriggeringField + DataProcessingConstants.CATALOG_SAVE_SEPARATOR
						+ tempDataEvent.getName());
				lstTempDataList.add(tempDataEvent);
			} catch (final CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
	}

}