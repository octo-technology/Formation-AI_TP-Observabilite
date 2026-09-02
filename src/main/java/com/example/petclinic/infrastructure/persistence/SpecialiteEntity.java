package com.example.petclinic.infrastructure.persistence;

import com.example.petclinic.domain.model.Specialite;
import jakarta.persistence.*;

@Entity
@Table(name = "specialites")
public class SpecialiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    protected SpecialiteEntity() {}

    public Specialite toDomain() {
        Specialite s = new Specialite(libelle);
        s.setId(id);
        return s;
    }
}
