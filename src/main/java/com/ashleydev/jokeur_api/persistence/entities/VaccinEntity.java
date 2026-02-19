package com.ashleydev.jokeur_api.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "measure")
public class VaccinEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", length = 120)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "vaccin_date", nullable = false, updatable = false)
  private LocalDate vaccinDate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "health_record_id", nullable = false)
  private HealthRecordEntity healthRecordEntity;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "reminder_id", nullable = false)
  private ReminderEntity reminderEntity;

  @Column(name = "creation_date", nullable = false, updatable = false)
  private LocalDate creationDate;

  @PrePersist
  public void onCreate() {
    creationDate = LocalDate.now();
  }
}
