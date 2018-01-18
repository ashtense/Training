/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.training.random;

public class Person {

	private long id;
	private String name;

	Person() {
		super();
	}

	Person(final String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	Person withId(final long id) {
		setId(id);
		return this;
	}

	Person withName(final String name) {
		setName(name);
		return this;
	}

	Person build() {
		return this;
	}
}
