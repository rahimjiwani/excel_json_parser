package com.rahim.jiwani.parser.resposneDto;

import java.util.List;

public class ParserResponseDto {

	private List<String> errors;
	private String parsedJson;
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public String getParsedJson() {
		return parsedJson;
	}
	public void setParsedJson(String parsedJson) {
		this.parsedJson = parsedJson;
	}
	
}
