package com.ashwani.TextEditor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTextEditor {
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("TEBeans.xml");
		TextEditor tex = (TextEditor) context.getBean("textEditor");
		tex.spellCheck();
		
		/*AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TextEditorConfig.class);
		TextEditor textEditor = context.getBean(TextEditor.class);
		textEditor.spellCheck();*/
	}
}