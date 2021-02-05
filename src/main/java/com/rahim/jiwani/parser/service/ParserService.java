package com.rahim.jiwani.parser.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import com.rahim.jiwani.parser.requestDto.SchemaRequestDto;
import com.rahim.jiwani.parser.resposneDto.ParserResponseDto;

public interface ParserService {

	public ParserResponseDto parse(MultipartFile file, List<SchemaRequestDto> schema) throws IOException, JSONException;
}
