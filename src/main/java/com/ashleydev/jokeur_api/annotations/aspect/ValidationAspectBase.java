package com.ashleydev.jokeur_api.annotations.aspect;

import com.ashleydev.jokeur_api.exceptions.annotation.AspectExtractIdImpossibleException;

public class ValidationAspectBase {

  protected Long extractId(Object[] args, String fieldName) {
    for (Object arg : args) {
      if (arg instanceof Long) return (Long) arg;

      try {
        var field = arg.getClass().getMethod(fieldName);
        return (Long) field.invoke(arg);
      } catch (Exception e) {
        throw new AspectExtractIdImpossibleException(this.getClass());
      }
    }
    return null;
  }
}
