package com.example.petclinic.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.petclinic.domain.model.Veterinaire;
import com.example.petclinic.domain.port.VeterinairePort;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListerVeterinaireTest {

    @Test
    void listeLesVeterinaires() {
        // Given
        VeterinairePort veterinaires = org.mockito.Mockito.mock(VeterinairePort.class);
        List<Veterinaire> resultat = List.of();
        when(veterinaires.findAll()).thenReturn(resultat);

        // When
        List<Veterinaire> veterinairesListes = new ListerVeterinaire(veterinaires).executer();

        // Then
        assertThat(veterinairesListes).isSameAs(resultat);
    }
}
