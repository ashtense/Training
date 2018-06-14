package com.ashwani.training.random;

public class StringSplit {

	public static void main(String[] args) {
		final String strURL = "http://digistyle.bonprix.net/images/9/8/2/2/prev_15029822.jpg";
		System.err.println(strURL.split("[/]")[strURL.split("[/]").length - 1]);

		String strParam = "Pantaloni-stretch Bengalin, \"straight\" kjgkjgjk";
		strParam = strParam.contains("&") ? strParam.replace("&", "&amp;") : strParam;
		strParam = strParam.contains("\"") ? strParam.replace("\"", "&quot;") : strParam;
		strParam = strParam.contains("'") ? strParam.replace("'", "&apos;") : strParam;
		strParam = strParam.contains("<") ? strParam.replace("<", "&lt;") : strParam;
		strParam = strParam.contains(">") ? strParam.replace(">", "&gt;") : strParam;
		System.err.println(strParam);

		final String toTest = "P0027|\\\"SYS_CEID|\\\"P0027_INTCA|\\\"P0027_ORDNO|\\\"P0027_IOPER|\\\"P0027_INFTY|\\\"P0027_SUBTY|\\\"P0027_BEGDA|\\\"P0027_ENDDA|\\\"P0027_OBJPS|\\\"P0027_SPRPS|\\\"P0027_SEQNR|\\\"P0027_EXTRA|\\\"P0027_KSTAR|\\\"P0027_KBU01|\\\"P0027_KST01|\\\"P0027_KPR01|\\\"P0027_PSP01|\\\"P0027_AUF01|\\\"P0027_KBU02|\\\"P0027_KST02|\\\"P0027_KPR02|\\\"P0027_PSP02|\\\"P0027_AUF02|\\\"P0027_KBU03|\\\"P0027_KST03|\\\"P0027_KPR03|\\\"P0027_PSP03|\\\"P0027_AUF03|\\\"P0027_KBU04|\\\"P0027_KST04|\\\"P0027_KPR04|\\\"P0027_PSP04|\\\"P0027_AUF04|\\\"P0027_KBU05|\\\"P0027_KST05|\\\"P0027_KPR05|\\\"P0027_PSP05|\\\"P0027_AUF05|\\\"P0027_KBU06|\\\"P0027_KST06|\\\"P0027_KPR06|\\\"P0027_PSP06|\\\"P0027_AUF06|\\\"P0027_KBU07|\\\"P0027_KST07|\\\"P0027_KPR07|\\\"P0027_PSP07|\\\"P0027_AUF07|\\\"P0027_KBU08|\\\"P0027_KST08|\\\"P0027_KPR08|\\\"P0027_PSP08|\\\"P0027_AUF08|\\\"P0027_KBU09|\\\"P0027_KST09|\\\"P0027_KPR09|\\\"P0027_PSP09|\\\"P0027_AUF09|\\\"P0027_KBU10|\\\"P0027_KST10|\\\"P0027_KPR10|\\\"P0027_PSP10|\\\"P0027_AUF10|\\\"P0027_KBU11|\\\"P0027_KST11|\\\"P0027_KPR11|\\\"P0027_PSP11|\\\"P0027_AUF11|\\\"P0027_KBU12|\\\"P0027_KST12|\\\"P0027_KPR12|\\\"P0027_PSP12|\\\"P0027_AUF12|\\\"P0027_KBU13|\\\"P0027_KST13|\\\"P0027_KPR13|\\\"P0027_PSP13|\\\"P0027_AUF13|\\\"P0027_KBU14|\\\"P0027_KST14|\\\"P0027_KPR14|\\\"P0027_PSP14|\\\"P0027_AUF14|\\\"P0027_KBU15|\\\"P0027_KST15|\\\"P0027_KPR15|\\\"P0027_PSP15|\\\"P0027_AUF15|\\\"P0027_KBU16|\\\"P0027_KST16|\\\"P0027_KPR16|\\\"P0027_PSP16|\\\"P0027_AUF16|\\\"P0027_KBU17|\\\"P0027_KST17|\\\"P0027_KPR17|\\\"P0027_PSP17|\\\"P0027_AUF17|\\\"P0027_KBU18|\\\"P0027_KST18|\\\"P0027_KPR18|\\\"P0027_PSP18|\\\"P0027_AUF18|\\\"P0027_KBU19|\\\"P0027_KST19|\\\"P0027_KPR19|\\\"P0027_PSP19|\\\"P0027_AUF19|\\\"P0027_KBU20|\\\"P0027_KST20|\\\"P0027_KPR20|\\\"P0027_PSP20|\\\"P0027_AUF20|\\\"P0027_PSE01|\\\"P0027_PSE02|\\\"P0027_PSE03|\\\"P0027_PSE04|\\\"P0027_PSE05|\\\"P0027_PSE06|\\\"P0027_PSE07|\\\"P0027_KGB01|\\\"P0027_KGB02";

		final String testString = "ptrtmp5b1c91c10183dc000caef6e2";
		System.out.println("Geronimo: " + testString.substring(0, 6));
	}
}