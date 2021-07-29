package com.ashwani.special.backup.xmlprocessor;

import java.util.ArrayList;
import java.util.List;

public class SameLevelMerge {

	private String targetField;
	private List<String> lstTriggeringFields;
	private List<ArrayList<Event>> dataList;

	public SameLevelMerge withTargetField(final String targetField) {
		setTargetField(targetField);
		return this;
	}

	public void addTriggeringField(final String triggeringField) {
		if (lstTriggeringFields == null) {
			lstTriggeringFields = new ArrayList<>();
			lstTriggeringFields.add(triggeringField);
			return;
		}
		lstTriggeringFields.add(triggeringField);
	}

	public String getTargetField() {
		return targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public List<String> getLstTriggeringFields() {
		return lstTriggeringFields;
	}

	public void setLstTriggeringFields(List<String> lstTriggeringFields) {
		this.lstTriggeringFields = lstTriggeringFields;
	}

	public List<ArrayList<Event>> getDataList() {
		return dataList;
	}

	public void setDataList(List<ArrayList<Event>> dataList) {
		this.dataList = dataList;
	}
}