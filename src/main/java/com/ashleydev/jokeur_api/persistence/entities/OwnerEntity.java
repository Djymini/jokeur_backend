package com.ashleydev.jokeur_api.persistence.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "owner")
public class OwnerEntity {

  private static final int EMAIL_MAX_LENGTH = 100;
  private static final int NAME_MAX_LENGTH = 80;
  private static final int PHONE_MAX_LENGTH = 20;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_owner", nullable = false)
  private Long idOwner;

  @Column(nullable = false, length = EMAIL_MAX_LENGTH)
  private String email;

  @Column(nullable = false, length = NAME_MAX_LENGTH)
  private String name;

  @Column(length = PHONE_MAX_LENGTH)
  private String phoneNumber;

  public OwnerEntity() {}

  public Long getIdOwner() {
    return idOwner;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
