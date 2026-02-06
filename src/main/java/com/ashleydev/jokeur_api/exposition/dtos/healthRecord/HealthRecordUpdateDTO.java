package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.PetColor;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class HealthRecordUpdateDTO {

  @Size(max = 50, message = "petName must not exceed 50 characters")
  private String petName;

  private PetBreed breed;

  private PetSex sex;

  private LocalDate birthDate;

  @Digits(integer = 2, fraction = 2, message = "currentWeight format is invalid (max 2 digits + 2 decimals)")
  private BigDecimal currentWeight;

  private PetColor color;

  @Size(max = 20, message = "identificationNumber must not exceed 20 characters")
  private String identificationNumber;

  @Size(max = 50, message = "tattooNumber must not exceed 50 characters")
  private String tattooNumber;

  @Size(max = 100, message = "allergy must not exceed 100 characters")
  private String allergy;

  public boolean hasAtLeastOneField() {
    return (
      petName != null ||
      breed != null ||
      sex != null ||
      birthDate != null ||
      currentWeight != null ||
      color != null ||
      identificationNumber != null ||
      tattooNumber != null ||
      allergy != null
    );
  }

  public String getPetName() {
    return petName;
  }

  public void setPetName(String petName) {
    this.petName = petName;
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
