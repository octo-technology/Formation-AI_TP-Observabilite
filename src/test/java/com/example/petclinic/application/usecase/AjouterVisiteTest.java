package com.example.petclinic.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.petclinic.domain.model.Animal;
import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.model.TypeAnimal;
import com.example.petclinic.domain.port.ProprietairePort;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AjouterVisiteTest {

    @Test
    void ajouteUneVisiteAUnAnimal() {
        // Given
        ProprietairePort proprietaires = mock(ProprietairePort.class);
        Proprietaire proprietaire = new Proprietaire("Jean", "DUPONT", "rue", "Paris", "0102030405");
        Animal animal = new Animal("Rex", new TypeAnimal("chien"), LocalDate.of(2020, 1, 1), proprietaire);
        animal.setId(2L);
        proprietaire.getAnimaux().add(animal);
        when(proprietaires.findById(1L)).thenReturn(Optional.of(proprietaire));

        // When
        new AjouterVisite(proprietaires).executer(1L, 2L, LocalDate.of(2026, 1, 15), "Bilan annuel");

        // Then
        assertThat(animal.getVisites()).singleElement().satisfies(visite -> {
            assertThat(visite.getDate()).isEqualTo(LocalDate.of(2026, 1, 15));
            assertThat(visite.getMotif()).isEqualTo("Bilan annuel");
        });
    }
}
