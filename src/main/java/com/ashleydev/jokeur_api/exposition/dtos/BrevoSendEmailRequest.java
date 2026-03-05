package com.ashleydev.jokeur_api.exposition.dtos;

import java.util.List;

public record BrevoSendEmailRequest(Sender sender, List<To> to, String subject, String htmlContent) {
  public record Sender(String email, String name) {}

  public record To(String email, String name) {}
}
