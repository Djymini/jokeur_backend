package com.ashleydev.jokeur_api.annotations.aspect;

import com.ashleydev.jokeur_api.exceptions.annotation.AspectExtractIdImpossibleException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class ValidationAspectBase {

  protected Long extractId(JoinPoint joinPoint, Object[] args, String fieldName) {
    Long idInMethodArgs = isMethodHasFielName(joinPoint, fieldName);
    if (idInMethodArgs != null) return idInMethodArgs;

    for (Object arg : args) {
      try {
        var field = arg.getClass().getMethod(fieldName);
        return (Long) field.invoke(arg);
      } catch (Exception e) {
        throw new AspectExtractIdImpossibleException(this.getClass());
      }
    }
    return null;
  }

  private Long isMethodHasFielName(JoinPoint joinPoint, String fieldName) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String[] parametersName = signature.getParameterNames();
    Class[] parametersType = signature.getParameterTypes();
    Object[] argsJoinPoint = joinPoint.getArgs();

    for (int i = 0; i < parametersName.length; i++) if (
      parametersType[i].getName().equals("java.lang.Long") && parametersName[i].equals(fieldName)
    ) return (Long) argsJoinPoint[i];

    return null;
  }
}
