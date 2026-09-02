package com.example.petclinic.infrastructure.adapter.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class VeterinaireControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exposeLaRouteDesVeterinaires() throws Exception {
        // Given
        String route = "/veterinaires";

        // When
        var resultat = mockMvc.perform(get(route));

        // Then
        resultat.andExpect(status().isOk());
    }

}
