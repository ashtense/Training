package com.ashwani.special.backup.xmlprocessor;

import java.util.ArrayList;

public class SpecialPECICase implements Cloneable {

	private String triggeringFieldGroup;
	private String targetFieldGroup;
	private ArrayList<Event> dataList;

	public SpecialPECICase withTriggeringFieldGroup(final String triggeringFieldGroup) {
		setTriggeringFieldGroup(triggeringFieldGroup);
		return this;
	}

	public SpecialPECICase withTargetFieldGroup(final String targetFieldGroup) {
		setTargetFieldGroup(targetFieldGroup);
		return this;
	}

	public SpecialPECICase withDataList(final ArrayList<Event> dataList) {
		setDataList(dataList);
		return this;
	}

	public String getTriggeringFieldGroup() {
		return triggeringFieldGroup;
	}

	public void setTriggeringFieldGroup(final String triggeringFieldGroup) {
		this.triggeringFieldGroup = triggeringFieldGroup;
	}

	public String getTargetFieldGroup() {
		return targetFieldGroup;
	}

	public void setTargetFieldGroup(final String targetFieldGroup) {
		this.targetFieldGroup = targetFieldGroup;
	}

	public ArrayList<Event> getDataList() {
		return dataList;
	}

	public void setDataList(final ArrayList<Event> dataList) {
		this.dataList = dataList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((dataList == null) ? 0 : dataList.hashCode());
		result = (prime * result) + ((targetFieldGroup == null) ? 0 : targetFieldGroup.hashCode());
		result = (prime * result) + ((triggeringFieldGroup == null) ? 0 : triggeringFieldGroup.hashCode());
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
		final SpecialPECICase other = (SpecialPECICase) obj;
		if (dataList == null) {
			if (other.dataList != null) {
				return false;
			}
		} else if (!dataList.equals(other.dataList)) {
			return false;
		}
		if (targetFieldGroup == null) {
			if (other.targetFieldGroup != null) {
				return false;
			}
		} else if (!targetFieldGroup.equals(other.targetFieldGroup)) {
			return false;
		}
		if (triggeringFieldGroup == null) {
			if (other.triggeringFieldGroup != null) {
				return false;
			}
		} else if (!triggeringFieldGroup.equals(other.triggeringFieldGroup)) {
			return false;
		}
		return true;
	}

	@Override
	public SpecialPECICase clone() throws CloneNotSupportedException {
		return (SpecialPECICase) super.clone();
	}

}