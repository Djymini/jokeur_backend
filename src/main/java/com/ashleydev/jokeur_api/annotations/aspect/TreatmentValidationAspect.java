package com.ashleydev.jokeur_api.annotations.aspect;

import com.ashleydev.jokeur_api.annotations.ValidateTreatment;
import com.ashleydev.jokeur_api.exceptions.treatment.TreatmentNotFoundException;
import com.ashleydev.jokeur_api.persistence.repositories.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TreatmentValidationAspect extends ValidationAspectBase {

  @Autowired
  private TreatmentRepository treatmentRepository;

  @Before("@annotation(validateAnnotation)")
  public void validate(JoinPoint joinPoint, ValidateTreatment validateAnnotation) {
    Object[] args = joinPoint.getArgs();
    String fieldName = validateAnnotation.idField();
    Long id = extractId(joinPoint, args, fieldName);

    if (id != null && !treatmentRepository.existsById(id)) {
      throw new TreatmentNotFoundException(id);
    }
  }
}
