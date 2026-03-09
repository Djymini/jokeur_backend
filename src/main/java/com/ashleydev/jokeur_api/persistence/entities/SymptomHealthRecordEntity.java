package com.ashleydev.jokeur_api.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "symptom_health_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SymptomHealthRecordEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "symptom_id", nullable = false)
  private SymptomEntity symptom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "health_record_id", nullable = false)
  private HealthRecordEntity healthRecord;

  @Column(name = "observation_date", nullable = false)
  private LocalDate observationDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(length = 1000)
  private String observation;

  @Column(name = "is_active")
  private Boolean isActive = true;

  public boolean isActive() {
    return endDate == null || endDate.isAfter(LocalDate.now());
  }
}
