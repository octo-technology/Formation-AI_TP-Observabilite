package com.example.petclinic.infrastructure.adapter.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(properties = "management.tracing.enabled=true")
@AutoConfigureMockMvc
class CorrelationLogsITest {

    @Autowired private MockMvc mockMvc;

    @Test
    void produitUnLogCorreléAvecLaRequete(CapturedOutput sortie) throws Exception {
        mockMvc.perform(get("/proprietaires")).andExpect(status().isOk());

        assertThat(sortie.getOut()).containsPattern("\\[petclinic,[0-9a-f]{32},[0-9a-f]{16}\\]");
    }
}
