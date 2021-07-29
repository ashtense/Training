package com.ashwani.special.backup.xmlprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CDSDerivationUtil {

	private CDSDerivationUtil() {
		// Private constructor to stop instance being created.
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, List<CustomField>> extractCDS() throws IOException {
		final Map<String, List<CustomField>> derivedCDSMap = new HashMap<>();
		final InputStream fileStream = new FileInputStream(new File("src/main/resources/cds.json"));
		final ObjectMapper objectMapper = new ObjectMapper();
		final Map<String, Object> cdsMap = objectMapper.readValue(fileStream, Map.class);
		final List<Map> fieldGroups = (List<Map>) cdsMap.get("recordTypes");
		fieldGroups.forEach(fieldGroup -> {
			final List<Map> fields = (List<Map>) fieldGroup.get("fields");
			final List<CustomField> tempFields = new ArrayList<>();
			fields.forEach(field -> {
				final CustomField tempCustomField = new CustomField();
				tempCustomField.setName((String) field.get("name"));
				tempCustomField.setSequenceNo((int) field.get("sequenceNo"));
				tempFields.add(tempCustomField);
			});
			derivedCDSMap.put((String) fieldGroup.get("recordTypeId"), tempFields);
		});
		return derivedCDSMap;
	}

	public static String processOnCDSData(final Map<String, String> currentDataMap,
			final Map<String, List<CustomField>> extractedCDSData, final String currentKey) {
		final StringBuilder tempDataString = new StringBuilder();
		final List<CustomField> lstFields = extractedCDSData.get(currentKey);
		if ((lstFields != null) && !lstFields.isEmpty()) {
			for (final CustomField currentField : lstFields) {
				final String currentFieldsData = getCurrentFieldData(currentDataMap, currentField.getName(),
						currentKey);
				if (tempDataString.length() == 0) {
					tempDataString.append(currentFieldsData);
				} else {
					if (currentFieldsData != null) {
						tempDataString.append(DataProcessingConstants.DATA_SEPARATOR + currentFieldsData.trim());
					} else {
						tempDataString.append(DataProcessingConstants.DATA_SEPARATOR + currentFieldsData);
					}
				}
			}
			final String intermediaryOutput = tempDataString.toString().replaceAll("null", "").trim();
			final String finalOutput = intermediaryOutput.replaceAll("\n", "").trim();
			return finalOutput;
		}
		return null;
	}

	private static String getCurrentFieldData(Map<String, String> currentDataMap, String fieldName,
			String currentFieldGroup) {
		if (currentDataMap.containsKey(fieldName)) {
			return currentDataMap.get(fieldName);
		}

		fieldName = fieldName.replace(DataProcessingConstants.WES, DataProcessingConstants.WES_SHORTENED);
		if (currentDataMap.containsKey(fieldName)) {
			return currentDataMap.get(fieldName);
		}

		final String[] splitArray = StringUtils.split(fieldName, DataProcessingConstants.CATALOG_SAVE_SEPARATOR);

		if (splitArray.length == 2) {
			final String possibleDataField = splitArray[1];
			if (currentDataMap.containsKey(possibleDataField) && splitArray[0].equalsIgnoreCase(currentFieldGroup)) {
				return currentDataMap.get(possibleDataField);
			} else {
				fieldName = fieldName.replace(DataProcessingConstants.WES, DataProcessingConstants.WES_SHORTENED);
				if (currentDataMap.containsKey(fieldName)) {
					return currentDataMap.get(fieldName);
				}
			}
		}

		return currentDataMap.get(fieldName);
	}
}
