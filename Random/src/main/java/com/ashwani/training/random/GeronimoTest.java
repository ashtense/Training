package com.ashwani.training.random;

public class GeronimoTest {

	public static void main(String[] args) {
		final String testString = "GB,Emerson Automation Solutions Final Control UK Ltd.,UK Monthly,1212274,E1212274,010884,,Brian,McGeorge,Robert,BM,MR.,Brian McGeorge,,,Brian McGeorge,Brian,McGeorge,26/04/1955,,,,,,,MR.,,,,,,,,M,,26/04/1955,NIC,YT983395C,,,,,,YT983395C,Primary Mailing,26/10/2015,,16 Earnsheugh Avenue,Cove,,,,,,Aberdeen,AB12 3RH,,,,,GB,16 Earnsheugh Avenue,Cove,Aberdeen,ABS,,,01,26/10/2015,,TSB BANK PLC,873453,TSB BANK PLC,Brian McGeorge,06042284,Primary,873453,,,GB,,,100.00,BACS,,,,GBP,,,,,,01/03/2017,Annually,Basic,45675.00,Monthly Calendar,GBP,26/10/2015,HIRE,HIRE,,,,,26/10/2015,26/10/2015,,FR,,,,,,,,FR,100.00,,39,39,,,01/09/2017,,,,,EE,,,26/10/2015,01,2VAW//0037,100.00,EMP,Emerson Automation Solutions Final Control UK Ltd.,GBAberdeenWellheads,2VAW,,,ACTIVE_PROCESS,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
		final String[] split = testString.split(",", -1);
		System.out.println(split.length);
	}
}
