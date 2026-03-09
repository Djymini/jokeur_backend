package com.ashleydev.jokeur_api;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class HealthRecordExportRequest {

  private LocalDate from;
  private LocalDate to;
  private List<MeasureType> measureTypes;
  private boolean includeVaccines;
}
