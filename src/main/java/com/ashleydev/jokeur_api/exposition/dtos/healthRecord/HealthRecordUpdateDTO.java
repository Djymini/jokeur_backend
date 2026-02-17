package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.PetColor;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class HealthRecordUpdateDTO {

  @Size(max = 50, message = "petName must not exceed 50 characters")
  private String petName;

  private PetBreed breed;

  private PetSex sex;

  private LocalDate birthDate;

  @Digits(integer = 2, fraction = 2, message = "currentWeight format is invalid (max 2 digits + 2 decimals)")
  private BigDecimal currentWeight;

  private PetColor color;

  @Size(max = 20, message = "id must not exceed 20 characters")
  private String id;

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
      id != null ||
      tattooNumber != null ||
      allergy != null
    );
  }


}
