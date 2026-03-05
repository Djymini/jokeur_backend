package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.BrevoSendEmailRequest;
/* import java.net.URLEncoder;
import java.nio.charset.StandardCharsets; */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class SenderMailService {

  @Autowired
  private WebClient webClient;

  @Value("${brevo.apiKey}")
  private String apiKey;

  @Value("${brevo.senderEmail}")
  private String senderEmail;

  @Value("${brevo.senderName}")
  private String senderName;

  public SenderMailService(WebClient brevoWebClient) {
    this.webClient = brevoWebClient;
  }

  public void sendResetPasswordEmail(String toEmail) {
    // String resetLink = "http://localhost:4200/reset-password?token=" +
    // URLEncoder.encode(token, StandardCharsets.UTF_8);

    String resetLink = "http://localhost:4200/reset-password";
    String html = """
      <div style="font-family: Arial, sans-serif; line-height: 1.5;">
        <h2>Réinitialisation de votre mot de passe</h2>
        <p>Vous avez demandé la réinitialisation de votre mot de passe.</p>
        <p>Pour choisir un nouveau mot de passe, cliquez sur le bouton ci-dessous :</p>

        <p style="margin: 24px 0;">
          <a href="%s"
             style="background:#111827;color:white;padding:12px 18px;text-decoration:none;border-radius:6px;display:inline-block;">
            Réinitialiser mon mot de passe
          </a>
        </p>

        <!-- <p>Ce lien est valable pendant <b>30 minutes</b>.</p> -->

        <p style="color:#6b7280;font-size: 13px;">
          Si vous n’êtes pas à l’origine de cette demande, vous pouvez ignorer cet email.
        </p>

        <hr style="border:none;border-top:1px solid #e5e7eb;margin:24px 0;" />

        <p style="color:#6b7280;font-size: 12px;">
          Si le bouton ne fonctionne pas, copiez-collez ce lien dans votre navigateur :<br/>
          <a href="%s">%s</a>
        </p>
      </div>
      """.formatted(resetLink, resetLink, resetLink);

    BrevoSendEmailRequest payload = new BrevoSendEmailRequest(
      new BrevoSendEmailRequest.Sender(senderEmail, senderName),
      List.of(new BrevoSendEmailRequest.To(toEmail, null)),
      "Réinitialisation de votre mot de passe",
      html
    );

    try {
      webClient.post().uri("/smtp/email").header("api-key", apiKey).bodyValue(payload).retrieve().toBodilessEntity().block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("Erreur Brevo (" + e.getStatusCode() + "): " + e.getResponseBodyAsString(), e);
    }
  }
}
