/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.isolated.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

public class PECIXMLDataConsumer {

	private final File xmlFile = new File("C:/Users/solanka/Desktop/PECI/Testv1.xml");
	private final PECIXMLDataProcessor processor = new PECIXMLDataProcessor();

	public void processXMLData() {
		try {
			processor.processPECIXml(new FileInputStream(xmlFile));
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}
}
