package com.ashleydev.jokeur_api.persistence.entities;

import com.ashleydev.jokeur_api.domain.enums.TreatmentFrequencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "treatment")
public class TreatmentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", length = 120)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "frequency", nullable = false, length = 20)
  private TreatmentFrequencyType frequency;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "health_record_id", nullable = false)
  private HealthRecordEntity healthRecordEntity;

  @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
  @JoinColumn(name = "reminder_id", nullable = false)
  private ReminderEntity reminderEntity;
}
