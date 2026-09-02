package com.example.petclinic.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Animal {

    private Long id;
    private final String nom;
    private final TypeAnimal type;
    private final LocalDate dateNaissance;
    private final Proprietaire proprietaire;
    private final List<Visite> visites = new ArrayList<>();

    public Animal(String nom, TypeAnimal type, LocalDate dateNaissance, Proprietaire proprietaire) {
        this.nom = nom;
        this.type = type;
        this.dateNaissance = dateNaissance;
        this.proprietaire = proprietaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public TypeAnimal getType() {
        return type;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public Proprietaire getProprietaire() {
        return proprietaire;
    }

    public List<Visite> getVisites() {
        return visites;
    }
}
