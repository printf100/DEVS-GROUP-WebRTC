package com.devs.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class DevsGroupApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevsGroupApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "redirect:/group/";
	}
}
