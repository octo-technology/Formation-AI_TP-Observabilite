package com.example.petclinic.infrastructure.adapter.persistence;

import com.example.petclinic.domain.model.Animal;
import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.model.TypeAnimal;
import com.example.petclinic.domain.model.Visite;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProprietairePersistenceAdapterITest {

    @Autowired
    private ProprietairePersistenceAdapter proprietairePersistenceAdapter;

    @Test
    void retrouveUnProprietaireAvecSesAnimauxEtSesVisites() {
        // When
        Proprietaire proprietaire = proprietairePersistenceAdapter.findById(6L).orElseThrow();

        // Then
        assertThat(proprietaire.getNom()).isEqualTo("MERCIER");
        assertThat(proprietaire.getAnimaux()).extracting(Animal::getNom)
                .containsExactlyInAnyOrder("Samantha", "Max");
        assertThat(proprietaire.getAnimaux()).flatExtracting(Animal::getVisites)
                .hasSize(4);
    }

    @Test
    void persisteLesRelationsEnCascade() {
        // Given
        TypeAnimal typeAnimal = proprietairePersistenceAdapter.findById(3L).orElseThrow().getAnimaux().get(0).getType();
        Proprietaire proprietaire = new Proprietaire("Lucie", "PERRIN", "1 rue des Fleurs", "Dijon",
                "03 80 12 34 56");
        Animal animal = new Animal("Rex", typeAnimal, LocalDate.of(2020, 5, 12), proprietaire);
        animal.getVisites().add(new Visite(LocalDate.of(2026, 1, 15), "Bilan annuel", animal));
        proprietaire.getAnimaux().add(animal);

        // When
        proprietaire = proprietairePersistenceAdapter.save(proprietaire);

        // Then
        Proprietaire recharge = proprietairePersistenceAdapter.findById(proprietaire.getId()).orElseThrow();
        assertThat(recharge.getAnimaux()).singleElement().satisfies(animalRecharge -> {
            assertThat(animalRecharge.getNom()).isEqualTo("Rex");
            assertThat(animalRecharge.getType().getLibelle()).isEqualTo("chien");
            assertThat(animalRecharge.getVisites()).singleElement()
                    .extracting(Visite::getMotif).isEqualTo("Bilan annuel");
        });
    }
}
