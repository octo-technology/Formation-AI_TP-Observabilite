package com.example.petclinic.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.ProprietairePort;
import org.junit.jupiter.api.Test;

class CreerProprietaireTest {

    @Test
    void creeUnProprietaire() {
        // Given
        ProprietairePort proprietaires = org.mockito.Mockito.mock(ProprietairePort.class);
        Proprietaire proprietaire = new Proprietaire("Jean", "DUPONT", "rue", "Paris", "0102030405");
        when(proprietaires.save(proprietaire)).thenReturn(proprietaire);

        // When
        Proprietaire proprietaireCree = new CreerProprietaire(proprietaires).executer(proprietaire);

        // Then
        assertThat(proprietaireCree).isSameAs(proprietaire);
    }
}
