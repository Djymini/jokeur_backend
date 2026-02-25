package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.services.NewsRssService;
import com.ashleydev.jokeur_api.exposition.dtos.notification.NewsResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.NewsEntity;
import com.ashleydev.jokeur_api.persistence.repositories.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("integration")
@Transactional
class NewsRssServiceIntegrationTest {

    @Autowired
    private NewsRssService newsRssService;

    @Autowired
    private NewsRepository newsRepository;

    @BeforeEach
    void setUp() {
        newsRepository.deleteAll();
    }
    Pageable pageable = PageRequest.of(0, 10);

    @Test
    void testGetAllNews_with_empty_bdd() {
        Page<NewsResponseDto> notifications = newsRssService.getAllNews(pageable);
        assertNotNull(notifications);
        assertTrue(notifications.isEmpty());
    }

    @Test
    void testGetAllNews_with_data() {

        NewsEntity n1 = createNews("Chat mignon", "https://example.com/1");
        NewsEntity n2 = createNews("Chien joueur", "https://example.com/2");
        newsRepository.save(n1);
        newsRepository.save(n2);

        Page<NewsResponseDto> notifications = newsRssService.getAllNews(pageable);

        assertEquals(2, notifications.getContent().size());
        assertEquals(2, notifications.getTotalElements());
        assertEquals(0, notifications.getNumber());
    }

    @Test
    void testFindByLink_when_article_existing() {

        NewsEntity notification = createNews("Test", "https://example.com/test");
        newsRepository.save(notification);

        Optional<NewsEntity> found = newsRepository.findByLink("https://example.com/test");

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }

    @Test
    void testFindByLink_when_article_not_existing() {
        Optional<NewsEntity> found = newsRepository.findByLink("https://example.com/inexistant");

        assertFalse(found.isPresent());
    }

    @Test
    void testSaveNotification_correcte_persistence() {
        NewsEntity notification = createNews("Santé du chat", "https://example.com/sante");

        NewsEntity saved = newsRepository.save(notification);
        NewsEntity retrieved = newsRepository.findById(saved.getId()).orElse(null);

        assertNotNull(retrieved);
        assertEquals("Santé du chat", retrieved.getTitle());
        assertEquals("https://example.com/sante", retrieved.getLink());
    }

    private NewsEntity createNews(String title, String link) {
        NewsEntity news = new NewsEntity();
        news.setTitle(title);
        news.setLink(link);
        news.setSummary("Description de test");
        news.setSent(false);
        return news;
    }
}