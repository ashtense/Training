package com.ashwani.training.jaxb;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JaxbMarshallerV1 {

	public static void main(String[] args) throws JAXBException {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("Ashwani");
		customer.setAge(26);
		
		File outputFile = new File("C:\\TEMP\\file.xml");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(customer, outputFile);
		jaxbMarshaller.marshal(customer, System.out);
	}
	
}
