package com.ashwani.ideas.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionChecker {

	public void versionCheckerProcess(File incommingFile) throws IOException {
		File outputFile = new File("/Users/ashwanisolanki/Desktop/output.txt");

		BufferedReader bufferedReader = new BufferedReader(new FileReader(incommingFile));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

		ServerConfigStack stackServerConfig = new ServerConfigStack();
		bufferedReader.lines().forEach(line -> {
			stackServerConfig.addToConfigStack(generateServerConfig(line));
		});
		bufferedReader.close();
		findOutdatedPackagesFromStack(stackServerConfig, bufferedWriter);
		bufferedWriter.close();
	}

	private void findOutdatedPackagesFromStack(ServerConfigStack stackServerConfig, BufferedWriter bufferedWriter) {
		Map<String, List<ServerConfigObject>> multiMapServerConfigs = stackServerConfig.getMultiMapServerConfigs();
		multiMapServerConfigs.entrySet().forEach(entry -> {
			if (entry.getValue().size() > 1 && checkIfInstalledOnMultipleServers(entry.getValue())) {
				try {
					bufferedWriter.write(entry.getKey());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Boolean checkIfInstalledOnMultipleServers(List<ServerConfigObject> lstServerConfigs) {
		String firstServer = null;
		String firstServerVersion = null;
		for (ServerConfigObject serverConfigObject : lstServerConfigs) {
			if (firstServer == null) {
				firstServer = serverConfigObject.getServerName();
				firstServerVersion = serverConfigObject.getVersion();
				continue;
			}
			if (!firstServer.equalsIgnoreCase(serverConfigObject.getServerName())
					&& firstServerVersion.equalsIgnoreCase(serverConfigObject.getVersion())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private ServerConfigObject generateServerConfig(String line) {
		if (line != null) {
			String[] arrConfigs = line.split(",");
			ServerConfigObject tempServerConfig = new ServerConfigObject();
			tempServerConfig.setServerName(arrConfigs[0]);
			tempServerConfig.setSoftwareName(arrConfigs[1] + " : " + arrConfigs[2]);
			tempServerConfig.setVersion(arrConfigs[3]);
			return tempServerConfig;
		}
		return null;
	}
}

class ServerConfigStack {

	@SuppressWarnings("rawtypes")
	Map<String, List<ServerConfigObject>> multiMapServerConfigs = new HashMap();

	public void addToConfigStack(final ServerConfigObject incomingServerConfig) {
		if (multiMapServerConfigs.containsKey(incomingServerConfig.getSoftwareName())) {
			multiMapServerConfigs.get(incomingServerConfig.getSoftwareName()).add(incomingServerConfig);
		} else {
			List<ServerConfigObject> lstTempConfigObjects = new ArrayList<>();
			lstTempConfigObjects.add(incomingServerConfig);
			multiMapServerConfigs.put(incomingServerConfig.getSoftwareName(), lstTempConfigObjects);
		}
	}

	public Map<String, List<ServerConfigObject>> getMultiMapServerConfigs() {
		return multiMapServerConfigs;
	}

	public void setMultiMapServerConfigs(Map<String, List<ServerConfigObject>> multiMapServerConfigs) {
		this.multiMapServerConfigs = multiMapServerConfigs;
	}
}

class ServerConfigObject {

	private String serverName;
	private String softwareName;
	private String version;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serverName == null) ? 0 : serverName.hashCode());
		result = prime * result + ((softwareName == null) ? 0 : softwareName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerConfigObject other = (ServerConfigObject) obj;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;
		if (softwareName == null) {
			if (other.softwareName != null)
				return false;
		} else if (!softwareName.equals(other.softwareName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
