package com.ashwani.special.backup.xmlprocessor;

import java.util.ArrayList;
import java.util.List;

public class Event implements Cloneable {

	private String name;
	private String data;
	private boolean isData;
	private boolean isEndEvent;
	private boolean isStartEvent;
	private List<EventAttribute> attributes;

	public Event withName(final String name) {
		setName(name);
		return this;
	}

	public Event withData(final String data) {
		setData(data);
		setDataFlag(true);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isData() {
		return isData;
	}

	public void setDataFlag(final boolean isData) {
		this.isData = isData;
	}

	public String getData() {
		return data;
	}

	public void setData(final String data) {
		this.data = data;
		setDataFlag(true);
	}

	public boolean isEndEvent() {
		return isEndEvent;
	}

	public void setEndEventFlag(final boolean isEndEvent) {
		this.isEndEvent = isEndEvent;
	}

	public List<EventAttribute> getAttributes() {
		if (attributes == null) {
			attributes = new ArrayList<>();
			return attributes;
		}
		return attributes;
	}

	public void addAttributes(final EventAttribute attribute) {
		if (attributes == null) {
			attributes = new ArrayList<>();
			attributes.add(attribute);
		} else {
			attributes.add(attribute);
		}
	}

	public boolean isStartEvent() {
		return isStartEvent;
	}

	public void setStartEventFlag(final boolean isStartEvent) {
		this.isStartEvent = isStartEvent;
	}

	@Override
	public String toString() {
		return name + "$$" + data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (isData ? 1231 : 1237);
		result = (prime * result) + (isEndEvent ? 1231 : 1237);
		result = (prime * result) + (isStartEvent ? 1231 : 1237);
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Event other = (Event) obj;
		if (isData != other.isData) {
			return false;
		}
		if (isEndEvent != other.isEndEvent) {
			return false;
		}
		if (isStartEvent != other.isStartEvent) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public Event clone() throws CloneNotSupportedException {
		return (Event) super.clone();
	}
}
