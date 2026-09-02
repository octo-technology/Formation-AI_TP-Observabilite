package com.example.petclinic.domain.model;

import java.util.HashSet;
import java.util.Set;

public class Veterinaire {

    private Long id;
    private final String prenom;
    private final String nom;
    private final Set<Specialite> specialites;

    public Veterinaire(String prenom, String nom, Set<Specialite> specialites) {
        this.prenom = prenom;
        this.nom = nom;
        this.specialites = new HashSet<>(specialites);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public Set<Specialite> getSpecialites() {
        return specialites;
    }
}
