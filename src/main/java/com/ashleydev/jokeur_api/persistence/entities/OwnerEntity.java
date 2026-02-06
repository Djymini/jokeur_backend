package com.ashleydev.jokeur_api.persistence.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "owner")
public class OwnerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_owner", nullable = false)
  private Long idOwner;

  @Column(nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 80)
  private String name;

  @Column(length = 20)
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
