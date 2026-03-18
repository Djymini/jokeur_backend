package com.ashleydev.jokeur_api;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class HealthRecordExportRequest {

  private LocalDate from;

  private LocalDate to;

  private List<@NotNull MeasureType> measureTypes;

  private boolean includeVaccines;

  @AssertTrue(message = "The start date must be earlier than the end date")
  public boolean isDateRangeValid() {
    if (from == null || to == null) return true;
    return !from.isAfter(to);
  }
}
