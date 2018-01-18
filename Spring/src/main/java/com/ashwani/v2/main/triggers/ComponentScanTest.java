package com.ashwani.v2.main.triggers;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.ashwani.TextEditor.SpellChecker;
import com.ashwani.TextEditor.TextEditor;
import com.ashwani.v2.configs.GlobalConfig;
import com.ashwani.v2.model.Circle;
import com.ashwani.v2.model.Shape;

public class ComponentScanTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GlobalConfig.class);
		
		Shape shape = context.getBean(Shape.class);
		Circle circle = shape.getCircle();
		circle.doIt();
		
		Circle circle2 = context.getBean(Circle.class);
		circle2.doIt();
		
		TextEditor textEditor = context.getBean(TextEditor.class);
		SpellChecker spellChecker = context.getBean(SpellChecker.class);
		
		//textEditor.setSpellChecker(spellChecker);
		textEditor.spellCheck();
		
		context.registerShutdownHook();
	}
}