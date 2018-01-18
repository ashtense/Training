/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

public class Cycle {

	private int id;
	private String name;

	public Cycle() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Cycle(final String drama) {
		super();
		name = drama;
	}

	static {
		System.err.println("called first");
	}

	public static void main(final String[] args) {
		final Cycle cycle = new Cycle("GERONIMO");
	}
}
