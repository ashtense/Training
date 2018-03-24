package com.ashwani.ideas.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class VersionCheckerTest {

	VersionChecker versionChecker = new VersionChecker();
	File incommingFile = null;
	String probableOutputFileLocation = null;

	@Before
	public void setUp() {
		incommingFile = new File("/Users/ashwanisolanki/Desktop/input.txt");
		probableOutputFileLocation = incommingFile.getPath().replace("input.txt", "output.txt");
	}

	@Test
	public void checkIfOutputFileCreated() throws IOException {
		versionChecker.versionCheckerProcess(incommingFile);
		File outputFile = new File(probableOutputFileLocation);
		assertTrue(outputFile.exists());
	}

	@Test
	public void checkIfOutputFileHasExpectedData() throws IOException {
		versionChecker.versionCheckerProcess(incommingFile);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(probableOutputFileLocation)));
		assertTrue(bufferedReader.readLine().contains("Ubuntu"));
		bufferedReader.close();
	}

	@Test(expected = NullPointerException.class)
	public void throwsExceptionIfNullFileSent() throws IOException {
		versionChecker.versionCheckerProcess(null);
	}
}
