package com.ashleydev.jokeur_api.persistence.repositories.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import java.math.BigDecimal;

public interface HealthRecordMyAnimalsView {
  Long getHealthRecordId();
  String getPetName();
  AnimalType getAnimalType();
  PetBreed getBreed();
  PetSex getSex();
  BigDecimal getCurrentWeight();
}
