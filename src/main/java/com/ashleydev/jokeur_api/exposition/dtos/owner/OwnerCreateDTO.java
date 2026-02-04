package com.ashleydev.jokeur_api.exposition.dtos.owner;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OwnerCreateDTO {

  @NotBlank
  @Email
  @Size(max = 100)
  private String email;

  @NotBlank
  @Size(max = 80)
  private String name;

  @Size(max = 20)
  private String phoneNumber;

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
