package com.ashwani.training.config.reflection;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigAttempt {
	public static void main(final String[] args) throws JsonParseException, JsonMappingException, IOException {
		final File configFile = new File("/Data/GitRepositories/Training/Random/src/main/resources/config.json");
		System.out.println(configFile.getName() + " : " + configFile.exists());
		final ObjectMapper objectMapper = new ObjectMapper();
		/*
		 * final List<ConfigObject> readValue = objectMapper.readValue(configFile, new
		 * TypeReference<List<ConfigObject>>() { });
		 */
		final ConfigObject readValue = objectMapper.readValue(configFile, ConfigObject.class);
		System.out.println(readValue.getProperty().size());
	}
}
