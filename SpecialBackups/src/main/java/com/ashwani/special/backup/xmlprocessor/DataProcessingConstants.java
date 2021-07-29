package com.ashwani.special.backup.xmlprocessor;

public final class DataProcessingConstants {

	private DataProcessingConstants() {
	}

	public static final String EFFECTIVE_MOMENT = "Effective_Moment";
	public static final String EFFECTIVE_CHANGE = "Effective_Change";
	public static final String WID = "WID";
	public static final String WORKER = "Worker";
	public static final String FIELD_GROUP_SEPARATOR = "__";
	public static final String NEW_LINE = "\n";
	public static final String DATA_SEPARATOR = "|";
	public static final String DERIVED_EVENT_CODE_WITH_SEPARATOR = FIELD_GROUP_SEPARATOR + "Derived_Event_Code";
	public static final String WORKER_SUMMARY = "Worker_Summary";
	public static final int MANDATORY_FIELD_GROUP_SIZE = 9;
	public static final String MANDATORY_FIELD_GROUP_KEY = "RT0001";
	public static final int INDEX_ONE = 1;
	public static final int INDEX_ZERO = 0;
	public static final String BLANK_STRING = "";
	public static final String WES = "Workers_Effective_Stack";
	public static final String WES_SHORTENED = "WES";
	public static final String WES_SUMMARY = WES_SHORTENED + "__Summary";
	public static final String SYS_CEID = "SYS_CEID";
	public static final String SUMMARY = "Summary";
	public static final String EMPLOYEE_ID = "Employee_ID";
	public static final String SYS_FIRST_NAME = "SYS_FirstName";
	public static final String WES_AGGREGATE = "Worker_Effective_Stack_Aggregate__";
	public static final String OPERATION = "operation";
	public static final String SYS_EMAIL = "SYS_Email";
	public static final String PECI_DEFAULT_MAIL = "noreply.gcc@adp.com";
	public static final String CATALOG_SAVE_SEPARATOR = "--";
	public static final String PAY_GROUP_COUNTRY = "Pay_Group_Country";
	public static final String COUNTRY = "country";
	public static final String DERIVED_EVENT_CODE = "Derived_Event_Code";
	public static final String EFFECTIVE_STACK_REFERENCE_ID = "Effective_Stack_ReferenceId";
	public static final String DELETED_ATTRIBUTE = "deleted";
	public static final String WES_AGGREGATE_DOC_LEVEL = "Worker_Effective_Stack_Aggregate";
	public static final String TRIGGER_FIELD_SEPARATOR = ",";
	public static final String STACK_WORKER_SUMMARY = WES_SHORTENED + FIELD_GROUP_SEPARATOR + WORKER
			+ FIELD_GROUP_SEPARATOR + WORKER_SUMMARY;
}
