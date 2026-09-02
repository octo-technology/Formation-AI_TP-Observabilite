package com.example.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@SpringBootTest
class BaseDeDonneesITest {

    @Autowired
    private DataSource sourceDeDonnees;

    @Test
    void chargeLesDonneesDeDemonstration() {
        // Then
        assertThat(sourceDeDonnees).isNotNull();
    }

    @Test
    void utiliseDesColonnesFrancaisesDansLaBase() throws Exception {
        // When
        try (var connexion = sourceDeDonnees.getConnection()) {
            // Then
            assertThat(connexion.getMetaData().getColumns(null, null, "PROPRIETAIRES", "PRENOM").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "PROPRIETAIRES", "NOM").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "ANIMAUX", "NOM").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "ANIMAUX", "DATE_NAISSANCE").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "ANIMAUX", "PROPRIETAIRE_ID").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "TYPES_ANIMAUX", "LIBELLE").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "VISITES", "DATE_VISITE").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "VISITES", "MOTIF").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "VETERINAIRES", "PRENOM").next()).isTrue();
            assertThat(connexion.getMetaData().getColumns(null, null, "SPECIALITES", "LIBELLE").next()).isTrue();
        }
    }

    @Test
    void creeLeSchemaAvecSesTablesContraintesEtIndex() throws Exception {
        var base = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .build();
        try {
            new ResourceDatabasePopulator(new ClassPathResource("schema.sql")).execute(base);

            try (var connexion = base.getConnection()) {
                var tables = connexion.getMetaData().getTables(null, null, "%", new String[] {"TABLE"});
                var nomsTables = new java.util.HashSet<String>();
                while (tables.next()) {
                    nomsTables.add(tables.getString("TABLE_NAME"));
                }

                assertThat(nomsTables)
                        .contains("PROPRIETAIRES", "ANIMAUX", "VISITES", "VETERINAIRES", "SPECIALITES",
                                "TYPES_ANIMAUX", "VETERINAIRES_SPECIALITES");
                assertThat(connexion.getMetaData().getIndexInfo(null, null, "ANIMAUX", false, false).next())
                        .isTrue();
                assertThat(connexion.getMetaData().getIndexInfo(null, null, "VISITES", false, false).next())
                        .isTrue();
            }
        } finally {
            base.shutdown();
        }
    }
}
