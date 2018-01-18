package com.ashwani.classconfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ashwani.HelloWorld;
import com.ashwani.TextEditor.SpellChecker;
import com.ashwani.TextEditor.TextEditor;
import com.ashwani.TextEditor.TextEditorConfig;

public class ClassConfigMainApp {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(HelloWorldConfig.class);
		HelloWorld objHelloWorld = context.getBean(HelloWorld.class);
		objHelloWorld.setMessage("--hustle until you fall down.");
		objHelloWorld.getMessage();
		
		/*
		 * You can add multiple configuration classes in one context.
		 * But you will need AnnotationConfigApplicationContext.
		 */
		AnnotationConfigApplicationContext annContext = new AnnotationConfigApplicationContext(TextEditorConfig.class, HelloWorldConfig.class);
		TextEditor objTextEditor = annContext.getBean(TextEditor.class);
		SpellChecker objSpellChecker = annContext.getBean(SpellChecker.class);
		objTextEditor.setSpellChecker(objSpellChecker);
		objTextEditor.spellCheck();
		HelloWorld hello = annContext.getBean(HelloWorld.class);
		hello.setMessage("- Now don't be flattered, you moron.");
		hello.getMessage();
	}
}