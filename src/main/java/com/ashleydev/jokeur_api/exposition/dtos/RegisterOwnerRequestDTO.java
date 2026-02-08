package com.ashleydev.jokeur_api.exposition.dtos;

import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.entities.Role;

public record RegisterOwnerRequestDTO(
    /*String pseudo,*/
    String username,
    String name,
    String firstname,
    String email,
    String password
) {
    public OwnerEntity toEntity() {
        OwnerEntity owner = new OwnerEntity();
        owner.setPseudo(username);
        owner.setName(name);
        owner.setFirstname(firstname);
        owner.setEmail(email);
        owner.setRole(Role.OWNER);
        // On ne SET pas le mot de passe dans le Mapper
        return owner;
    }
}
