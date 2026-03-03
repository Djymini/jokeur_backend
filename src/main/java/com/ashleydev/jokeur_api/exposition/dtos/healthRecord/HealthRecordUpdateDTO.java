package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthRecordUpdateDTO {

  @Size(min = 2, max = 50, message = "petName must be between 2 and 50 characters")
  private String petName;

  private PetBreed breed;

  private PetSex sex;

  private LocalDate birthDate;

  @DecimalMin(value = "0.01", message = "currentWeight must be > 0")
  @DecimalMax(value = "999.99", message = "currentWeight must be < 1000")
  @Digits(integer = 3, fraction = 2, message = "currentWeight format is invalid")
  private BigDecimal currentWeight;

  private PetColor color;

  @Size(max = 20)
  @Pattern(regexp = "^[A-Za-z0-9]{2,20}$", message = "identificationNumber format is invalid")
  private String identificationNumber;

  @Size(max = 50)
  @Pattern(regexp = "^[A-Za-z0-9]{2,10}$", message = "tattooNumber format is invalid")
  private String tattoo;

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
      tattoo != null ||
      allergy != null
    );
  }
}
