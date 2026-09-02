package com.example.petclinic.domain.model;

public class Specialite {

    private Long id;
    private final String libelle;

    public Specialite(String libelle) {
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
