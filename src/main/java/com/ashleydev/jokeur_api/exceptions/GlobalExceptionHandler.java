package com.ashleydev.jokeur_api.exceptions;

import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordUpdateEmptyException;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordValidationException;
import com.ashleydev.jokeur_api.exceptions.measure.MeasureNotFoundException;
import com.ashleydev.jokeur_api.exceptions.measure.MeasureTypeNotValidateException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerEmailAlreadyUsedException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerUpdateEmptyException;
import com.ashleydev.jokeur_api.exceptions.reminder.ReminderNotFoundException;
import com.ashleydev.jokeur_api.exceptions.user.UserEmailAlreadyUsedException;
import com.ashleydev.jokeur_api.exceptions.user.UserNotFoundException;
import com.ashleydev.jokeur_api.exceptions.vaccin.VaccinNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getAllErrors()
      .forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(OwnerNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleOwnerNotFound(OwnerNotFoundException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "OWNER_NOT_FOUND");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(OwnerEmailAlreadyUsedException.class)
  public ResponseEntity<Map<String, String>> handleOwnerEmailAlreadyUsed(OwnerEmailAlreadyUsedException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "OWNER_EMAIL_ALREADY_USED");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(HealthRecordNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleHealthRecordNotFound(HealthRecordNotFoundException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "HEALTH_RECORD_NOT_FOUND");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(HealthRecordUpdateEmptyException.class)
  public ResponseEntity<Map<String, String>> handleHealthRecordUpdateEmpty(HealthRecordUpdateEmptyException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "HEALTH_RECORD_UPDATE_EMPTY");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(HealthRecordValidationException.class)
  public ResponseEntity<Map<String, String>> handleHealthRecordValidation(HealthRecordValidationException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "HEALTH_RECORD_VALIDATION_ERROR");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleUnexpected(Exception ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "INTERNAL_SERVER_ERROR");
    body.put("message", "Unexpected error.");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(OwnerUpdateEmptyException.class)
  public ResponseEntity<Map<String, Object>> handleOwnerUpdateEmpty(OwnerUpdateEmptyException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", "OWNER_UPDATE_EMPTY");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(MeasureNotFoundException.class)
  public ResponseEntity<String> handleMeasureNotFound(MeasureNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(MeasureTypeNotValidateException.class)
  public ResponseEntity<Map<String, Object>> handleMeasureTypeNotValidate(MeasureTypeNotValidateException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", "MEASURE_TYPE_NOT_VALIDATE");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(VaccinNotFoundException.class)
  public ResponseEntity<String> handleVaccinNotFound(VaccinNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(ReminderNotFoundException.class)
  public ResponseEntity<String> handleReminderNotFound(ReminderNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<Map<String, String>> handlePathVariableError(MissingPathVariableException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "PATH_VARIABLE_ERROR");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "USER_NOT_FOUND");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(UserEmailAlreadyUsedException.class)
  public ResponseEntity<Map<String, String>> handleUserEmailAlreadyUsed(UserEmailAlreadyUsedException ex) {
    Map<String, String> body = new HashMap<>();
    body.put("error", "USER_EMAIL_ALREADY_USED");
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<String> handleNotFoundError(NoHandlerFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur 404 : Le chemin que vous avez demandé n'existe pas.");
  }
}
