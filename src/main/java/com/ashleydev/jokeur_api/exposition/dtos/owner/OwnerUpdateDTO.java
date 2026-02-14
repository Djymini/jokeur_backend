package com.ashleydev.jokeur_api.exposition.dtos.owner;

import jakarta.validation.constraints.Size;

public class OwnerUpdateDTO {

  @Size(max = 80)
  private String name;

  @Size(max = 20)
  private String phoneNumber;

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public boolean hasAtLeastOneField() {
    return name != null || phoneNumber != null;
  }
}
