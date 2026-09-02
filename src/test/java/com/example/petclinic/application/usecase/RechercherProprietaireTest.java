package com.example.petclinic.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.ProprietairePort;
import java.util.List;
import org.junit.jupiter.api.Test;

class RechercherProprietaireTest {

    @Test
    void rechercheTousLesProprietairesQuandLeNomEstVide() {
        // Given
        ProprietairePort proprietaires = org.mockito.Mockito.mock(ProprietairePort.class);
        List<Proprietaire> resultat = List.of();
        when(proprietaires.findAll()).thenReturn(resultat);

        // When
        List<Proprietaire> proprietairesTrouves = new RechercherProprietaire(proprietaires).executer("");

        // Then
        assertThat(proprietairesTrouves).isSameAs(resultat);
    }
}
