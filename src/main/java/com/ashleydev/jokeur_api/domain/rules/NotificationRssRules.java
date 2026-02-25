package com.ashleydev.jokeur_api.domain.rules;

import com.rometools.rome.feed.synd.SyndEntry;

public class NotificationRssRules {

  /**
   * cette methode filtre un article suivant les critère (chat, chien) et fait partie de categorie article de santé pour les animeaux
   * @param entry : c'est un item de flux Rss qui desine un article
   * @return vrai si l'article qui respect les critère de regle 1,2,3
   */
  public static boolean articleAboutAnimal(SyndEntry entry) {
    if (entry == null) {
      return false;
    }
    if (entry.getTitle() == null || entry.getTitle().isBlank()) {
      return false;
    }

    String content = (entry.getTitle() + " " + (entry.getDescription() != null ? entry.getDescription().getValue() : "")).toLowerCase();

    boolean isDogOrCat = content.contains("chien") || content.contains("chat");

    boolean isHealth =
      content.contains("santé") ||
      content.contains("maladie") ||
      content.contains("vaccin") ||
      content.contains("parasite") ||
      content.contains("tique") ||
      content.contains("puce");

    return isDogOrCat || isHealth;
  }
}
