package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.AgendaRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AgendaRulesTest {
    private LocalDateTime date1;
    private LocalDateTime date2;
    private LocalDateTime date3;

    @BeforeEach
    void setup() {
        date1 = LocalDateTime.of(2000, 1, 28, 9, 30);
        date2 = LocalDateTime.of(2000, 9, 28, 9, 30);
        date3 = LocalDateTime.of(2000, 2, 28, 9, 30);
    }

    @Test
    @DisplayName("Should give 1 for all month")
    void shouldGive1() {
        LocalDateTime response1 = AgendaRules.getFirstDayOfMonth(date1);
        LocalDateTime response2 = AgendaRules.getFirstDayOfMonth(date2);
        LocalDateTime response3 = AgendaRules.getFirstDayOfMonth(date3);

        assertEquals(response1, LocalDateTime.of(date1.getYear(), date1.getMonth(), 1, 0, 0));
        assertEquals(response2, LocalDateTime.of(date2.getYear(), date2.getMonth(), 1, 0, 0));
        assertEquals(response3, LocalDateTime.of(date3.getYear(), date3.getMonth(), 1, 0, 0));
    }

    @Test
    @DisplayName("Should give good end number day for all month")
    void shouldGiveGoodEndNumber() {
        LocalDateTime response1 = AgendaRules.getLastDayOfMonth(date1);
        LocalDateTime response2 = AgendaRules.getLastDayOfMonth(date2);
        LocalDateTime response3 = AgendaRules.getLastDayOfMonth(date3);

        assertEquals(response1, LocalDateTime.of(date1.getYear(), date1.getMonth(), 31, 23, 59).plusDays(8));
        assertEquals(response2, LocalDateTime.of(date2.getYear(), date2.getMonth(), 30, 23, 59).plusDays(8));
        assertEquals(response3, LocalDateTime.of(date3.getYear(), date3.getMonth(), 28, 23, 59).plusDays(8));
    }
}
