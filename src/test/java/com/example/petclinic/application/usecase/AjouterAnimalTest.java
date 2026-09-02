package com.example.petclinic.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.model.TypeAnimal;
import com.example.petclinic.domain.port.ProprietairePort;
import com.example.petclinic.domain.port.TypeAnimalPort;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AjouterAnimalTest {

    @Test
    void ajouteUnAnimalAuProprietaire() {
        // Given
        ProprietairePort proprietaires = mock(ProprietairePort.class);
        TypeAnimalPort typesAnimaux = mock(TypeAnimalPort.class);
        Proprietaire proprietaire = new Proprietaire("Jean", "DUPONT", "rue", "Paris", "0102030405");
        TypeAnimal typeAnimal = new TypeAnimal("chien");
        when(proprietaires.findById(1L)).thenReturn(Optional.of(proprietaire));
        when(typesAnimaux.findAll()).thenReturn(List.of(typeAnimal));

        // When
        new AjouterAnimal(proprietaires, typesAnimaux).executer(1L, "Rex", "chien", LocalDate.of(2020, 1, 1));

        // Then
        assertThat(proprietaire.getAnimaux()).singleElement().satisfies(animal -> {
            assertThat(animal.getNom()).isEqualTo("Rex");
            assertThat(animal.getType()).isSameAs(typeAnimal);
        });
    }
}
