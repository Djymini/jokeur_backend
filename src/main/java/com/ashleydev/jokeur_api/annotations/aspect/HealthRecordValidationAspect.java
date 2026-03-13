package com.ashleydev.jokeur_api.annotations.aspect;

import com.ashleydev.jokeur_api.annotations.ValidateHealthRecord;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HealthRecordValidationAspect extends ValidationAspectBase {

  @Autowired
  private HealthRecordRepository healthRecordRepository;

  @Before("@annotation(validateAnnotation)")
  public void validate(JoinPoint joinPoint, ValidateHealthRecord validateAnnotation) {
    Object[] args = joinPoint.getArgs();
    String fieldName = validateAnnotation.idField();
    Long id = extractId(joinPoint, args, fieldName);

    if (id != null && !healthRecordRepository.existsById(id)) {
      throw new HealthRecordNotFoundException(id);
    }
  }
}
