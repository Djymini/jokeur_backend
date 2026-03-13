package com.ashleydev.jokeur_api.persistence.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {

  @Column(nullable = true, unique = true)
  private String pseudo;

  @Column(nullable = true, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  private String firstname;

  @Column(nullable = true, unique = false)
  private String phoneNumber;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true, unique = false)
  private String address;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<ReminderEntity> reminders;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<HealthRecordEntity> healthRecords;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<AppointmentEntity> appointments;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "reset_token_hash", length = 64)
  private String resetTokenHash;

  @Column(name = "reset_token_expires_at")
  private LocalDateTime resetTokenExpiresAt;

  /* Méthodes UserDetails (utilisées par Spring Security) */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(() -> "ROLE_" + role.name());
  }

  @Override
  public String getUsername() {
    return email; // 👈 l'email sert d'identifiant
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
