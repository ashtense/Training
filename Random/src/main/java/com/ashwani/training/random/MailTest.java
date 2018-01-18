package com.ashwani.training.random;

import java.io.IOException;

public class MailTest {

	public static void main(String[] args) {
		String cmd = "cmd.exe /c start \"\" \""
				+ "mailto:someone@example.com?Subject=Hello%20again&body=your%20textBody%20here" + "\"";
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}