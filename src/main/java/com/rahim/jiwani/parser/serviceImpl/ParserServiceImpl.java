package com.rahim.jiwani.parser.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rahim.jiwani.parser.requestDto.SchemaRequestDto;
import com.rahim.jiwani.parser.resposneDto.ParserResponseDto;
import com.rahim.jiwani.parser.service.ParserService;

@Service
public class ParserServiceImpl implements ParserService {

	@Override
	public ParserResponseDto parse(MultipartFile file, List<SchemaRequestDto> schema) throws IOException, JSONException {
		Workbook storeWb = null;
		storeWb = new XSSFWorkbook(file.getInputStream());	
		Sheet storeSheet = storeWb.getSheetAt(0);
		int rowCount = storeSheet.getLastRowNum()-storeSheet.getFirstRowNum();
		
		JSONArray jArray = new JSONArray();	
		List<String> errors = new ArrayList<String>();
		for (int i = 1; i < rowCount+1; i++) {
	        Row row = storeSheet.getRow(i);
	        JSONObject innerObject = new JSONObject();
			for (int j = 0; j < schema.size(); j++) {
				SchemaRequestDto cellRules = schema.get(j);
				if(cellRules.getIsIgnore() == null || !cellRules.getIsIgnore()){
					Cell cell = row.getCell(j, MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if(cell != null){
						if (cellRules.getDataType().equalsIgnoreCase("ALPHANUMERIC")) {
							if(CellType.STRING == cell.getCellTypeEnum()){
								if(cellRules.getIsRequired() != null && cellRules.getIsRequired()){
									if(cell.getStringCellValue() != null && !cell.getStringCellValue().trim().isEmpty()){
										if(cellRules.getLength() != null && cell.getStringCellValue().trim().length() > cellRules.getLength()){
											errors.add(cell.getAddress() + " Length is greater than "+cellRules.getLength());
										}else{
											innerObject.put(cellRules.getFieldName(), cell.getStringCellValue());
										}
									}
									else{
										errors.add(cell.getAddress() + "Is Required");
									}
								}else{
									innerObject.put(cellRules.getFieldName(), cell.getStringCellValue());
								}
								
							}else{
								errors.add(cell.getAddress() + " DataType Should be Alpha Numeric");
							}
							
						} else if (cellRules.getDataType().equalsIgnoreCase("NUMERIC")) {
							if (CellType.NUMERIC == cell.getCellTypeEnum()) {
								if(cellRules.getIsRequired() != null && cellRules.getIsRequired()){
									innerObject.put(cellRules.getFieldName(), Double.toString(cell.getNumericCellValue()));
								}
							} else if(cell.getCellTypeEnum() == CellType.BLANK){
								errors.add(cell.getAddress() + "Is Required");
							}
							else{
								errors.add(cell.getAddress() + " DataType Should be Numeric");
							}
						}
					}else{
						if(cellRules.getIsRequired() != null && cellRules.getIsRequired()){
							errors.add(row.createCell(j).getAddress() + " Cannot be Empty");
						}
					}
				}
			}
			jArray.put(innerObject);
	    } 
		
		ParserResponseDto response = new ParserResponseDto();
		response.setParsedJson(jArray.toString());
		response.setErrors(errors);
		return response;
	}

}
