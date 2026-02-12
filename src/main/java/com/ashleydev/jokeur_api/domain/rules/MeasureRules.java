package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.measure.MeasureTypeNotValidateException;
import java.util.ArrayList;

public class MeasureRules {

  public static void validateType(String type) {
    checkType(type);
  }

  private static void checkType(String type) {
    ArrayList<String> measureTypeArray = new ArrayList<>();
    measureTypeArray.add("WEIGHT");
    measureTypeArray.add("BPM");
    measureTypeArray.add("RESPIRATORY_RATE");
    measureTypeArray.add("TEMPERATURE");

    if (!measureTypeArray.contains(type.toUpperCase())) {
      throw new MeasureTypeNotValidateException("Type is not valid");
    }
  }
}
