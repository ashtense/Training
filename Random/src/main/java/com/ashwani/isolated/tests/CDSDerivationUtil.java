package com.ashwani.isolated.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ashwani.isolated.tests.XMLDataProcessingV11.DataProcessingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CDSDerivationUtil {

	private CDSDerivationUtil() {
		// Private constructor to stop instance being created.
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, List<CustomField>> extractCDS() throws IOException {
		Map<String, List<CustomField>> derivedCDSMap = new HashMap<>();
		InputStream fileStream = new FileInputStream(new File("src/resources/cds1.json"));
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> cdsMap = objectMapper.readValue(fileStream, Map.class);
		List<Map> fieldGroups = (List<Map>) cdsMap.get("recordTypes");
		fieldGroups.forEach(fieldGroup -> {
			List<Map> fields = (List<Map>) fieldGroup.get("fields");
			List<CustomField> tempFields = new ArrayList<>();
			fields.forEach(field -> {
				CustomField tempCustomField = new CustomField();
				tempCustomField.setName((String) field.get("name"));
				tempCustomField.setSequenceNo((int) field.get("sequenceNo"));
				tempFields.add(tempCustomField);
			});
			derivedCDSMap.put((String) fieldGroup.get("recordTypeId"), tempFields);
		});
		return derivedCDSMap;
	}

	public static void processOnCDSData(Map<String, String> currentDataMap,
			Map<String, List<CustomField>> extractedCDSData, String currentKey) {
		StringBuilder tempDataString = new StringBuilder();
		List<CustomField> lstFields = extractedCDSData.get(currentKey);
		lstFields.forEach(currentField -> {
			if(tempDataString.length() ==0){
				tempDataString.append(currentDataMap.get(currentField.getName()));
			}else{
				tempDataString.append(DataProcessingConstants.DATA_SEPARATOR + currentDataMap.get(currentField.getName()));
			}
		});
		String replaceAll = tempDataString.toString().replaceAll("null", "");
		System.out.println(replaceAll);
	}
}
