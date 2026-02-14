package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.OwnerService;
import com.ashleydev.jokeur_api.exposition.dtos.owner.OwnerCreateDTO;
import com.ashleydev.jokeur_api.exposition.dtos.owner.OwnerResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.owner.OwnerUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
public class OwnerController {

  private final OwnerService ownerService;

  @Autowired
  public OwnerController(OwnerService ownerService) {
    this.ownerService = ownerService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public OwnerResponseDTO create(@Valid @RequestBody OwnerCreateDTO dto) {
    return ownerService.create(dto);
  }

  @GetMapping("/{ownerId}")
  public OwnerResponseDTO getById(@PathVariable Long ownerId) {
    return ownerService.getById(ownerId);
  }

  @GetMapping("/by-email")
  public OwnerResponseDTO getByEmail(@RequestParam String email) {
    return ownerService.getByEmail(email);
  }

  @PatchMapping("/{ownerId}")
  public OwnerResponseDTO updatePartial(@PathVariable Long ownerId, @Valid @RequestBody OwnerUpdateDTO dto) {
    return ownerService.updatePartial(ownerId, dto);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{ownerId}")
  public void delete(@PathVariable Long ownerId) {
    ownerService.delete(ownerId);
  }
}
