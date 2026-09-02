package com.example.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class PetClinicApplicationTests {

    @Autowired
    private ApplicationContext contexte;

    @Test
    void demarreLeContexteDeApplication() {
        // Then
        assertThat(contexte).isNotNull();
        assertThat(contexte.getBean(PetClinicApplication.class)).isNotNull();
    }

    @Test
    void regroupeLesPortsEtLesAdaptersDansLesPackagesDedies() {
        // Then
        assertThat(Files.isDirectory(Path.of("src/main/java/com/example/petclinic/domain/port"))).isTrue();
        assertThat(Files.isDirectory(Path.of("src/main/java/com/example/petclinic/infrastructure/adapter"))).isTrue();
        assertThat(Files.exists(Path.of(
                "src/main/java/com/example/petclinic/infrastructure/adapter/persistence/ProprietairePersistenceAdapter.java")))
                .isTrue();
        assertThat(Files.exists(Path.of(
                "src/main/java/com/example/petclinic/infrastructure/adapter/web/ProprietaireController.java")))
                .isTrue();
    }
}
