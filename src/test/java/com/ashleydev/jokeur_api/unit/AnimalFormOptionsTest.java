package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalFormOptions;
import com.ashleydev.jokeur_api.domain.enums.pets.Option;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AnimalFormOptionsTest {

    @Test
    void shouldContainAllAnimalTypes() {
        assertThat(AnimalFormOptions.ANIMAL_TYPES).hasSize(2);
        assertThat(AnimalFormOptions.ANIMAL_TYPES)
                .extracting(Option::code)
                .containsExactly("DOG", "CAT");
    }

    @Test
    void shouldContainAllSexes() {
        assertThat(AnimalFormOptions.SEXES).hasSize(3);
        assertThat(AnimalFormOptions.SEXES)
                .extracting(Option::code)
                .containsExactly("MALE", "FEMALE", "UNKNOWN");
    }

    @Test
    void shouldContainAllColors() {
        assertThat(AnimalFormOptions.COLORS).hasSize(6);
        assertThat(AnimalFormOptions.COLORS)
                .extracting(Option::code)
                .containsExactly("BLACK", "WHITE", "BROWN", "GINGER", "GREY", "MIXED");
    }

    @Test
    void shouldGroupBreedsByAnimalType() {
        Map<String, List<Option>> breeds = AnimalFormOptions.BREEDS_BY_ANIMAL_TYPE;

        assertThat(breeds).containsKeys("DOG", "CAT");

        assertThat(breeds.get("DOG"))
                .extracting(Option::code)
                .contains("LABRADOR", "BULLDOG", "GERMAN_SHEPHERD");

        assertThat(breeds.get("CAT"))
                .extracting(Option::code)
                .contains("SIAMESE", "MAINE_COON", "EUROPEAN", "BRITISH_SHORTHAIR");
    }

    @Test
    void shouldExcludeOtherFromBreedsByAnimalType() {
        Map<String, List<Option>> breeds = AnimalFormOptions.BREEDS_BY_ANIMAL_TYPE;

        breeds.values().forEach(list ->
                assertThat(list).extracting(Option::code).doesNotContain("OTHER")
        );
    }
}