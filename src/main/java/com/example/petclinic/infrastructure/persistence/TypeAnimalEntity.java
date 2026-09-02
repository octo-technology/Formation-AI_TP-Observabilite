package com.example.petclinic.infrastructure.persistence;

import com.example.petclinic.domain.model.TypeAnimal;
import jakarta.persistence.*;

@Entity
@Table(name = "types_animaux")
public class TypeAnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    protected TypeAnimalEntity() {}

    public TypeAnimalEntity(TypeAnimal t) {
        id = t.getId();
        libelle = t.getLibelle();
    }

    public TypeAnimal toDomain() {
        TypeAnimal t = new TypeAnimal(libelle);
        t.setId(id);
        return t;
    }

    public Long getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
}
