package com.example.petclinic.infrastructure.adapter.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.petclinic.domain.model.Veterinaire;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class VeterinairePersistenceAdapterITest {

    @Autowired private VeterinairePersistenceAdapter veterinairePersistenceAdapter;

    @Test
    void retrouveTousLesVeterinaires() {
        // When
        List<Veterinaire> veterinaires = veterinairePersistenceAdapter.findAll();

        // Then
        assertThat(veterinaires).hasSize(6);
        assertThat(veterinaires).extracting(Veterinaire::getNom)
                .containsExactlyInAnyOrder("MARTIN", "BERNARD", "DUBOIS", "MOREAU", "LAURENT", "PETIT");
    }

    @Test
    void convertitLesSpecialitesDesVeterinaires() {
        // When
        Veterinaire veterinaire = veterinairePersistenceAdapter.findAll().stream()
                .filter(v -> v.getNom().equals("DUBOIS"))
                .findFirst()
                .orElseThrow();

        // Then
        assertThat(veterinaire.getSpecialites()).extracting(specialite -> specialite.getLibelle())
                .containsExactlyInAnyOrder("Chirurgie", "Dentisterie");
    }
}
