package com.ashwani.special.backup.xmlprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

public class PECIXMLDataConsumer {

	private final File xmlFile = new File("C:/Users/solanka/Desktop/PECI/Testv4.xml");
	private final PECIXMLDataProcessor processor = new PECIXMLDataProcessor();

	// @Test
	public void processXMLData() throws XMLStreamException, IOException {
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		final XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlFile));
		processor.processPECIXml(eventReader);
	}
}
