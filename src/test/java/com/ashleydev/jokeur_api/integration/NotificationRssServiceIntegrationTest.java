package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.services.NotificationRssService;
import com.ashleydev.jokeur_api.exposition.dtos.NotificationResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.NotificationEntity;
import com.ashleydev.jokeur_api.persistence.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("integration")
@Transactional
class NotificationRssServiceIntegrationTest {

    @Autowired
    private NotificationRssService notificationRssService;

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
    }

    @Test
    void testGetAllNotifications_with_empty_bdd() {
        // When
        List<NotificationResponseDto> notifications = notificationRssService.getAllNotifications();

        // Then
        assertNotNull(notifications);
        assertTrue(notifications.isEmpty());
    }

    @Test
    void testGetAllNotifications_with_data() {

        NotificationEntity n1 = createNotification("Chat mignon", "https://example.com/1");
        NotificationEntity n2 = createNotification("Chien joueur", "https://example.com/2");
        notificationRepository.save(n1);
        notificationRepository.save(n2);


        List<NotificationResponseDto> notifications = notificationRssService.getAllNotifications();

        // Then
        assertEquals(2, notifications.size());
    }

    @Test
    void testFindByLink_when_article_existing() {
        // Given
        NotificationEntity notification = createNotification("Test", "https://example.com/test");
        notificationRepository.save(notification);

        // When
        Optional<NotificationEntity> found = notificationRepository.findByLink("https://example.com/test");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getTitle());
    }

    @Test
    void testFindByLink_when_article_not_existing() {
        // When
        Optional<NotificationEntity> found = notificationRepository.findByLink("https://example.com/inexistant");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void testSaveNotification_correcte_persistence() {
        // Given
        NotificationEntity notification = createNotification("Santé du chat", "https://example.com/sante");

        // When
        NotificationEntity saved = notificationRepository.save(notification);
        NotificationEntity retrieved = notificationRepository.findById(saved.getId()).orElse(null);

        // Then
        assertNotNull(retrieved);
        assertEquals("Santé du chat", retrieved.getTitle());
        assertEquals("https://example.com/sante", retrieved.getLink());
    }

    private NotificationEntity createNotification(String title, String link) {
        NotificationEntity notification = new NotificationEntity();
        notification.setTitle(title);
        notification.setLink(link);
        notification.setSummary("Description de test");
        notification.setSent(false);
        return notification;
    }
}