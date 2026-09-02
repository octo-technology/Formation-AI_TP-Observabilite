package com.example.petclinic.infrastructure.adapter.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.petclinic.domain.model.TypeAnimal;
import com.example.petclinic.infrastructure.adapter.persistence.TypeAnimalPersistenceAdapter;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProprietaireControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TypeAnimalPersistenceAdapter typeAnimalPersistenceAdapter;

    @Test
    void exposeLaRouteDesProprietaires() throws Exception {
        // Given
        String route = "/proprietaires";

        // When
        var resultat = mockMvc.perform(get(route));

        // Then
        resultat.andExpect(status().isOk());
    }

    @Test
    void exposeLesRoutesFrancaisesDesProprietaires() throws Exception {
        // Given
        String routeDeCreation = "/proprietaires/nouveau";
        String routeDeDetails = "/proprietaires/1";

        // When
        var resultatDeCreation = mockMvc.perform(get(routeDeCreation));
        var resultatDeDetails = mockMvc.perform(get(routeDeDetails));

        // Then
        resultatDeCreation.andExpect(status().isOk());
        resultatDeDetails.andExpect(status().isOk());
    }

    @Test
    void proposeLesTypesAnimauxDisponiblesDansLeFormulaire() throws Exception {
        mockMvc.perform(get("/proprietaires/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<select")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"type\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(">chat</option>")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(">chien</option>")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(">hamster</option>")));
    }

    @Test
    void actualiseLaListeAvecUnTypeAjouteEnBase() throws Exception {
        typeAnimalPersistenceAdapter.save(new TypeAnimal("furet"));

        mockMvc.perform(get("/proprietaires/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(">furet</option>")));
    }

    @ParameterizedTest
    @MethodSource("anciennesRoutesAnglaises")
    void rejetteLesAnciennesRoutesAnglaisesDesProprietaires(String route) throws Exception {
        // When
        var resultat = mockMvc.perform(get(route));

        // Then
        resultat.andExpect(status().isNotFound());
    }

    private static Stream<Arguments> anciennesRoutesAnglaises() {
        return Stream.of(Arguments.of("/owners"), Arguments.of("/owners/new"), Arguments.of("/owners/1"));
    }
}
