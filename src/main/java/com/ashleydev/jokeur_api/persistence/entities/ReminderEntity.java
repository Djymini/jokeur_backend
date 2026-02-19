package com.ashleydev.jokeur_api.persistence.entities;

import com.ashleydev.jokeur_api.domain.enums.ReminderStatus;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reminder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReminderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 20)
  private ReminderType type;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "reminder_date")
  private LocalDate reminderDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private ReminderStatus status = ReminderStatus.PENDING;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  @JsonBackReference
  private OwnerEntity owner;
}
