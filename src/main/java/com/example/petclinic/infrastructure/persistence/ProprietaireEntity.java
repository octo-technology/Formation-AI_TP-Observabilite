package com.example.petclinic.infrastructure.persistence;

import com.example.petclinic.domain.model.Proprietaire;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proprietaires")
public class ProprietaireEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;
    private String adresse;
    private String ville;
    private String telephone;

    @OneToMany(
            mappedBy = "proprietaire",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<AnimalEntity> animaux = new ArrayList<>();

    protected ProprietaireEntity() {}

    public ProprietaireEntity(Proprietaire p) {
        id = p.getId();
        prenom = p.getPrenom();
        nom = p.getNom();
        adresse = p.getAdresse();
        ville = p.getVille();
        telephone = p.getTelephone();
        p.getAnimaux().forEach(a -> animaux.add(new AnimalEntity(a, this)));
    }

    public Proprietaire toDomain() {
        Proprietaire p = new Proprietaire(prenom, nom, adresse, ville, telephone);
        p.setId(id);
        animaux.forEach(a -> p.getAnimaux().add(a.toDomain(p)));
        return p;
    }

    public Long getId() {
        return id;
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

    public List<AnimalEntity> getAnimaux() {
        return animaux;
    }
}
