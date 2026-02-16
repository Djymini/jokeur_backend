package com.ashleydev.jokeur_api.persistence.entities;

import com.ashleydev.jokeur_api.domain.enums.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.PetColor;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "health_record")
public class HealthRecordEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_owner", nullable = false)
  private OwnerEntity owner;

  @Column(name = "pet_name", nullable = false, length = 50)
  private String petName;

  @Enumerated(EnumType.STRING)
  @Column(name = "animal_type", nullable = false, length = 20)
  private AnimalType animalType;

  @Enumerated(EnumType.STRING)
  @Column(name = "breed", length = 50)
  private PetBreed breed;

  @Enumerated(EnumType.STRING)
  @Column(name = "sex", nullable = false, length = 12)
  private PetSex sex;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "current_weight", precision = 4, scale = 2)
  private BigDecimal currentWeight;

  @Enumerated(EnumType.STRING)
  @Column(name = "color", length = 20)
  private PetColor color;

  @Column(name = "identification_number", length = 20, unique = true)
  private String identificationNumber;

  @Column(name = "tattoo", length = 50, unique = true)
  private String tattooNumber;

  @Column(name = "allergy", length = 100)
  private String allergy;

  public HealthRecordEntity() {}

  public Long getId() {
    return id;
  }

  public OwnerEntity getOwner() {
    return owner;
  }

  public void setOwner(OwnerEntity owner) {
    this.owner = owner;
  }

  public String getPetName() {
    return petName;
  }

  public void setPetName(String petName) {
    this.petName = petName;
  }

  public AnimalType getAnimalType() {
    return animalType;
  }

  public void setAnimalType(AnimalType animalType) {
    this.animalType = animalType;
  }

  public PetBreed getBreed() {
    return breed;
  }

  public void setBreed(PetBreed breed) {
    this.breed = breed;
  }

  public PetSex getSex() {
    return sex;
  }

  public void setSex(PetSex sex) {
    this.sex = sex;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public BigDecimal getCurrentWeight() {
    return currentWeight;
  }

  public void setCurrentWeight(BigDecimal currentWeight) {
    this.currentWeight = currentWeight;
  }

  public PetColor getColor() {
    return color;
  }

  public void setColor(PetColor color) {
    this.color = color;
  }

  public String getIdentificationNumber() {
    return identificationNumber;
  }

  public void setIdentificationNumber(String identificationNumber) {
    this.identificationNumber = identificationNumber;
  }

  public String getTattooNumber() {
    return tattooNumber;
  }

  public void setTattooNumber(String tattooNumber) {
    this.tattooNumber = tattooNumber;
  }

  public String getAllergy() {
    return allergy;
  }

  public void setAllergy(String allergy) {
    this.allergy = allergy;
  }
}
