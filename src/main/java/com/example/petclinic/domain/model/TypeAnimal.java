package com.example.petclinic.domain.model;

public class TypeAnimal {

    private Long id;
    private final String libelle;

    public TypeAnimal(String libelle) {
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }
}
