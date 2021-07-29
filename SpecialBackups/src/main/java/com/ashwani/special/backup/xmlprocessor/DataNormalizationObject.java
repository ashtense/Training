package com.ashwani.special.backup.xmlprocessor;

import java.util.List;

public class DataNormalizationObject {

	private String triggeringFieldGroup;
	private List<String> lstTargetFieldGroups;
	private List<String> lstNormalizingFields;

	public DataNormalizationObject withTriggeringFieldGroup(final String triggeringFieldGroup) {
		setTriggeringFieldGroup(triggeringFieldGroup);
		return this;
	}

	public DataNormalizationObject withTargetFieldGroups(final List<String> lstTargetFieldGroups) {
		setLstTargetFieldGroups(lstTargetFieldGroups);
		return this;
	}

	public DataNormalizationObject withNormalizingFields(final List<String> lstNormalizingFields) {
		setLstNormalizingFields(lstNormalizingFields);
		return this;
	}

	public String getTriggeringFieldGroup() {
		return triggeringFieldGroup;
	}

	public void setTriggeringFieldGroup(String triggeringFieldGroup) {
		this.triggeringFieldGroup = triggeringFieldGroup;
	}

	public List<String> getLstTargetFieldGroups() {
		return lstTargetFieldGroups;
	}

	public void setLstTargetFieldGroups(List<String> lstTargetFieldGroups) {
		this.lstTargetFieldGroups = lstTargetFieldGroups;
	}

	public List<String> getLstNormalizingFields() {
		return lstNormalizingFields;
	}

	public void setLstNormalizingFields(List<String> lstNormalizingFields) {
		this.lstNormalizingFields = lstNormalizingFields;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((lstTargetFieldGroups == null) ? 0 : lstTargetFieldGroups.hashCode());
		result = (prime * result) + ((triggeringFieldGroup == null) ? 0 : triggeringFieldGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DataNormalizationObject other = (DataNormalizationObject) obj;
		if (lstTargetFieldGroups == null) {
			if (other.lstTargetFieldGroups != null) {
				return false;
			}
		} else if (!lstTargetFieldGroups.equals(other.lstTargetFieldGroups)) {
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

}
