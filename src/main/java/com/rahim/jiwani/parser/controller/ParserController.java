package com.rahim.jiwani.parser.controller;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rahim.jiwani.parser.requestDto.SchemaRequestDto;
import com.rahim.jiwani.parser.service.ParserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/api")
public class ParserController {

	@Autowired
	public ParserService parserService;
	
	@GetMapping("/hello")
	public String hello() {
		return "hi";
	}
	
	@PostMapping("/parse")
	public Object parser(@RequestPart("file") MultipartFile file, @RequestPart("schema") List<SchemaRequestDto> schema) {
		try {
			return parserService.parse(file, schema);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getOriginalFilename() + schema;
	}
	
	@PostMapping("/test")
	public Object parser(@RequestBody List<SchemaRequestDto> data) {
		return data;
	}
}
