package com.rahim.jiwani.parser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

	@GetMapping("/")
	public String hello() {
		return "index.html";
	}
}
