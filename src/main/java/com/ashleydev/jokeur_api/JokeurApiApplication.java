package com.ashleydev.jokeur_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class JokeurApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(JokeurApiApplication.class, args);
  }

  @GetMapping("/test")
  public String sayHello() {
    return "Hello World!";
  }
}
