package com.example.petclinic.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Proprietaire {

    private Long id;
    private String prenom;
    private String nom;
    private String adresse;
    private String ville;
    private String telephone;
    private final List<Animal> animaux = new ArrayList<>();

    public Proprietaire(String prenom, String nom, String adresse, String ville, String telephone) {
        this.prenom = prenom;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.telephone = telephone;
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

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public List<Animal> getAnimaux() {
        return animaux;
    }
}
