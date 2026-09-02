package com.example.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConfigurationSpringBootITest {

    @Autowired
    private Environment environnement;

    @Test
    void chargeLaConfigurationContractuelleDeLaApplication() {
        // Then
        assertThat(environnement.getProperty("spring.application.name")).isEqualTo("petclinic");
        assertThat(environnement.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:h2:mem:petclinic;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        assertThat(environnement.getProperty("spring.datasource.username")).isEqualTo("sa");
        assertThat(environnement.getProperty("spring.h2.console.enabled", Boolean.class)).isTrue();
        assertThat(environnement.getProperty("spring.h2.console.path")).isEqualTo("/h2-console");
        assertThat(environnement.getProperty("spring.thymeleaf.cache", Boolean.class)).isFalse();
        assertThat(environnement.getProperty("management.tracing.enabled", Boolean.class)).isFalse();
        assertThat(environnement.getProperty("management.tracing.sampling.probability", Double.class))
                .isEqualTo(1.0);
        assertThat(environnement.getProperty("management.opentelemetry.tracing.export.otlp.endpoint"))
                .isEqualTo("http://localhost:4318/v1/traces");
        assertThat(environnement.getProperty("logging.pattern.correlation"))
                .isEqualTo("[petclinic,%X{traceId:-},%X{spanId:-}] ");
        assertThat(environnement.getProperty("logging.include-application-name", Boolean.class)).isFalse();
        assertThat(environnement.getProperty("management.defaults.metrics.export.enabled", Boolean.class)).isFalse();
    }
}
