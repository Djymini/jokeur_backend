package com.ashleydev.jokeur_api.config;

import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

  @Autowired
  private SymptomRepository symptomRepository;

  @Autowired
  private DataSource dataSource;

  @PostConstruct
  public void init() {
    if (symptomRepository.count() > 0) return;
    try (Connection conn = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("symptom.sql"));
      System.out.println("Données initiales insérées avec succès !");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
