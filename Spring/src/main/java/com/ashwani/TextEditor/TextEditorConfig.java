package com.ashwani.TextEditor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.ashwani.postprocessors")
public class TextEditorConfig {

	@Bean
	public TextEditor textEditor(){
		return new TextEditor();
	}
	
	@Bean
	public SpellChecker spellChecker(){
		return new SpellChecker();
	}
}
