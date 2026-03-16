package com.ashleydev.jokeur_api.domain.builders;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.rules.HealthRecordExportRules;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfBuilder {

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Autowired
  private HealthRecordExportRules exportRules;

  public byte[] build(HealthRecordEntity healthRecord, HealthRecordExportRequest request) {
    Context ctx = buildContext(healthRecord, request);
    String html = templateEngine.process("export/health-record-export", ctx);

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(html);
      renderer.layout();
      renderer.createPDF(baos);
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Erreur lors de la génération du PDF", e);
    }
  }

  private Context buildContext(HealthRecordEntity healthRecord, HealthRecordExportRequest request) {
    Context ctx = new Context();
    ctx.setVariable("healthRecord", healthRecord);
    ctx.setVariable("from", request.getFrom());
    ctx.setVariable("to", request.getTo());

    if (exportRules.hasMeasureTypes(request)) {
      ctx.setVariable("measuresByType", exportRules.filterMeasuresByTypeAndDateRange(healthRecord.getMeasures(), request));
    }

    if (request.isIncludeVaccines()) {
      ctx.setVariable("vaccines", exportRules.filterVaccinesSortedByDate(healthRecord.getVaccines()));
    }

    return ctx;
  }
}
