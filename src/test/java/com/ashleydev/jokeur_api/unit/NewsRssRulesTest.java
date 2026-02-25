package com.ashleydev.jokeur_api.unit;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import org.junit.jupiter.api.Test;

import static com.ashleydev.jokeur_api.domain.rules.NewsRssRules.articleAboutAnimal;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewsRssRulesTest {


    @Test
    void should_return_false_when_entry_null(){
        assertFalse(articleAboutAnimal(null));
    }

    @Test
    void should_return_false_when_title_is_Null() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(null);
        assertFalse(articleAboutAnimal(entry));
    }

    @Test
    void should_return_false_when_title_is_blank() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("   ");
        assertFalse(articleAboutAnimal(entry));
    }

    @Test
    void should_return_true_when_article_contains_chat() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Mon chat est adorable");
        assertTrue(articleAboutAnimal(entry));
    }

    @Test
    void should_return_true_when_article_contains_chien() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Article sur les chiens");
        assertTrue(articleAboutAnimal(entry));
    }

    @Test
    void should_return_true_when_article_contains_sante() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Conseils santé pour animaux");
        assertTrue(articleAboutAnimal(entry));
    }

    @Test
    void should_return_true_when_article_contains_vaccin() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Calendrier de vaccin");
        assertTrue(articleAboutAnimal(entry));
    }

    @Test
    void should_return_true_when_article_not_contains_keywords() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Article sur la météo");
        assertFalse(articleAboutAnimal(entry));
    }

    @Test
    void should_return_true_when_case_insensitive_to_lowercase() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("CHAT ET CHIEN");
        assertTrue(articleAboutAnimal(entry));
    }

    @Test
    void should_return_true_when_description_is_null_and_title_valide() {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Article sur le chien");
        entry.setDescription(null);
        assertTrue(articleAboutAnimal(entry));
    }

}
