package com.ashleydev.jokeur_api.annotations.aspect;

import com.ashleydev.jokeur_api.annotations.ValidateVaccine;
import com.ashleydev.jokeur_api.exceptions.vaccin.VaccinNotFoundException;
import com.ashleydev.jokeur_api.persistence.repositories.vaccine.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class VaccineValidationAspect extends ValidationAspectBase {

  @Autowired
  private VaccineRepository vaccineRepository;

  @Before("@annotation(validateAnnotation)")
  public void validate(JoinPoint joinPoint, ValidateVaccine validateAnnotation) {
    Object[] args = joinPoint.getArgs();
    String fieldName = validateAnnotation.idField();
    Long id = extractId(args, fieldName);

    if (id != null && !vaccineRepository.existsById(id)) {
      throw new VaccinNotFoundException(id);
    }
  }
}
