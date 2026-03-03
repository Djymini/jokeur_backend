package com.ashleydev.jokeur_api.exposition.dtos;

import com.ashleydev.jokeur_api.persistence.entities.Role;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;

public record RegisterUserRequestDTO(
  /*String pseudo,*/
  String username,
  String name,
  String firstname,
  String email,
  String password
) {
  public UserEntity toEntity() {
    UserEntity user = new UserEntity();
    user.setPseudo(username);
    user.setName(name);
    user.setFirstname(firstname);
    user.setEmail(email);
    user.setRole(Role.OWNER);
    // On ne SET pas le mdp dans le Mapper
    return user;
  }
}
