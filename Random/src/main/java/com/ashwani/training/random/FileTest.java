/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

import java.io.File;

public class FileTest {

	public static void main(final String[] args) {
		final File ff = new File("D:\\abc.jpg");
		System.err.println(ff.getName());
		System.err.println(ff.getName().split("[.]")[1]);

	}

}
