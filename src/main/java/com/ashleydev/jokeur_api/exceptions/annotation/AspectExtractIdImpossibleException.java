package com.ashleydev.jokeur_api.exceptions.annotation;

public class AspectExtractIdImpossibleException extends RuntimeException {

  public AspectExtractIdImpossibleException(Class aspect) {
    super("Extraction de l'id impossible pour : " + aspect.getName());
  }
}
