package com.example.petclinic.infrastructure.adapter.persistence;

import com.example.petclinic.domain.model.TypeAnimal;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TypeAnimalPersistenceAdapterITest {

    @Autowired
    private TypeAnimalPersistenceAdapter typeAnimalPersistenceAdapter;

    @Test
    void chargeLesTypesAnimauxFrancais() {
        // When
        var typesAnimaux = typeAnimalPersistenceAdapter.findAll();

        // Then
        assertThat(typesAnimaux).extracting(TypeAnimal::getLibelle)
                .containsExactlyInAnyOrder("chat", "chien", "lezard", "serpent", "oiseau", "hamster");
    }

    @Test
    void retrouveUnTypeAnimalParSonLibelle() {
        // When
        TypeAnimal typeAnimal = typeAnimalPersistenceAdapter.findAll().stream()
                .filter(type -> type.getLibelle().equals("chat"))
                .findFirst().orElseThrow();

        // Then
        assertThat(typeAnimal.getId()).isEqualTo(1L);
        assertThat(typeAnimal.getLibelle()).isEqualTo("chat");
    }
}
