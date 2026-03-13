package com.ashleydev.jokeur_api.annotations.aspect;

import com.ashleydev.jokeur_api.annotations.ValidateAppointment;
import com.ashleydev.jokeur_api.exceptions.appointment.AppointmentNotFoundException;
import com.ashleydev.jokeur_api.persistence.repositories.AppoinmentRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AppointmentValidationAspect extends ValidationAspectBase {

  @Autowired
  private AppoinmentRepository appoinmentRepository;

  @Before("@annotation(validateAnnotation)")
  public void validate(JoinPoint joinPoint, ValidateAppointment validateAnnotation) {
    Object[] args = joinPoint.getArgs();
    String fieldName = validateAnnotation.idField();
    Long id = extractId(joinPoint, args, fieldName);

    if (id != null && !appoinmentRepository.existsById(id)) {
      throw new AppointmentNotFoundException(id);
    }
  }
}
