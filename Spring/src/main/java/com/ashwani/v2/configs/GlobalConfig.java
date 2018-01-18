package com.ashwani.v2.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ashwani.TextEditor.TextEditorConfig;

@Configuration
@ComponentScan("com.ashwani.v2.model")
@Import(TextEditorConfig.class)
public class GlobalConfig {

}