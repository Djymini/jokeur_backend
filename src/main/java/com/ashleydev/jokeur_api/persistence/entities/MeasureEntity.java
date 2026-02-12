package com.ashleydev.jokeur_api.persistence.entities;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "measure")
public class MeasureEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_measure", nullable = false)
  private Long idMeasure;

  @Column(name = "value", nullable = false)
  private int value;

  @Enumerated(EnumType.STRING)
  @Column(name = "measure_type", nullable = false, length = 20)
  private MeasureType measureType;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "health_record_number", nullable = false)
  private HealthRecordEntity healthRecordEntity;

  @Column(name = "creation_date", nullable = false, updatable = false)
  private LocalDate creationDate;

  @PrePersist
  public void onCreate() {
    creationDate = LocalDate.now();
  }
}
