package com.ashleydev.jokeur_api.config;

// import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

// import reactor.netty.http.client.HttpClient;

@Configuration
public class AppConfig {

  @Bean
  public WebClient brevoWebClient() {
    return WebClient.builder().baseUrl("https://api.brevo.com/v3").build();
  }
}
