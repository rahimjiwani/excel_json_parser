package com.rahim.jiwani.parser.requestDto;

public class SchemaRequestDto {

	private String fieldName;
	private String dataType;
	private Boolean isRequired;
	private Boolean isIgnore;
	private Integer length;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Boolean getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
	public Boolean getIsIgnore() {
		return isIgnore;
	}
	public void setIsIgnore(Boolean isIgnore) {
		this.isIgnore = isIgnore;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	
}
