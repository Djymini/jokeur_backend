package com.ashleydev.jokeur_api.persistence.entities;

import com.ashleydev.jokeur_api.domain.enums.AnimalType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "health_record")
public class HealthRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pet_name", nullable = false, length = 50)
    private String petName;

    @Column(name = "breed", length = 50)
    private String breed;

    @Column(name = "sex", nullable = false, length = 12)
    private String sex;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "current_weight", precision = 4, scale = 2)
    private BigDecimal currentWeight;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "identification_number", unique = true, length = 20)
    private String identificationNumber;

    @Column(name = "tatoo", unique = true, length = 50)
    private String tattoo;

    @Column(name = "allergy", length = 100)
    private String allergy;

    @Enumerated(EnumType.STRING)
    @Column(name = "animal_type", nullable = false, length = 20)
    private AnimalType animalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner", nullable = false)
    private OwnerEntity owner;

    @OneToMany(mappedBy = "healthRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ReminderEntity> reminders;

   // @OneToMany(mappedBy = "healthRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  //  private List<Measure> measures;

   // @OneToMany(mappedBy = "healthRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   // private List<Symptom> symptoms;

   // @OneToMany(mappedBy = "healthRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   // private List<Treatment> treatments;

  //  @OneToMany(mappedBy = "healthRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   // private List<FoodPlan> foodPlans;

  //  @ManyToMany(mappedBy = "healthRecords")
  //  private Set<Veterinarian> veterinarians;
}
