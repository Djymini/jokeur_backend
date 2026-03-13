package com.ashleydev.jokeur_api.persistence.entities;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "health_record")
public class HealthRecordEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "pet_name", nullable = false, length = 50)
  private String petName;

  @Enumerated(EnumType.STRING)
  @Column(name = "breed", nullable = false, length = 50)
  private PetBreed breed;

  @Enumerated(EnumType.STRING)
  @Column(name = "sex", nullable = false, length = 12)
  private PetSex sex;

  @Column(name = "photoKey")
  private String photoKey;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "current_weight", precision = 5, scale = 2)
  private BigDecimal currentWeight;

  @Enumerated(EnumType.STRING)
  @Column(name = "color", nullable = false, length = 20)
  private PetColor color;

  @Column(name = "identification_number", unique = true, length = 20)
  private String identificationNumber;

  @Column(name = "tattoo", length = 50, unique = true)
  private String tattoo;

  @Column(name = "allergy", length = 100)
  private String allergy;

  @Enumerated(EnumType.STRING)
  @Column(name = "animal_type", nullable = false, length = 20)
  private AnimalType animalType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @OneToMany(mappedBy = "healthRecordEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MeasureEntity> measures = new ArrayList<>();

  @OneToMany(mappedBy = "healthRecordEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VaccineEntity> vaccines = new ArrayList<>();

  @OneToMany(mappedBy = "healthRecordEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TreatmentEntity> treatments = new ArrayList<>();
}
