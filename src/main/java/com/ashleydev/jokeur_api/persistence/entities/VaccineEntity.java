package com.ashleydev.jokeur_api.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vaccine")
public class VaccineEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", length = 120)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "vaccinator", length = 120)
  private String vaccinator;

  @Column(name = "vaccine_date", nullable = false, updatable = false)
  private LocalDate vaccineDate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "health_record_id", nullable = false)
  private HealthRecordEntity healthRecordEntity;

  @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
  @JoinColumn(name = "reminder_id", nullable = false)
  private ReminderEntity reminderEntity;

  @Column(name = "creation_date", nullable = false, updatable = false)
  private LocalDate creationDate;

  @PrePersist
  public void onCreate() {
    creationDate = LocalDate.now();
  }
}
