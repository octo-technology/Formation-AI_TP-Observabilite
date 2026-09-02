package com.example.petclinic.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.ProprietairePort;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TrouverProprietaireTest {

    @Test
    void trouveUnProprietaireParSonIdentifiant() {
        // Given
        ProprietairePort proprietaires = mock(ProprietairePort.class);
        Proprietaire proprietaire = new Proprietaire("Jean", "DUPONT", "rue", "Paris", "0102030405");
        when(proprietaires.findById(1L)).thenReturn(Optional.of(proprietaire));

        // When
        Proprietaire proprietaireTrouve = new TrouverProprietaire(proprietaires).executer(1L);

        // Then
        assertThat(proprietaireTrouve).isSameAs(proprietaire);
    }
}
