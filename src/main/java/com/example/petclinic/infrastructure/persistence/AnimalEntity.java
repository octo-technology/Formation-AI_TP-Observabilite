package com.example.petclinic.infrastructure.persistence;

import com.example.petclinic.domain.model.Animal;
import com.example.petclinic.domain.model.Proprietaire;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animaux")
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private ProprietaireEntity proprietaire;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "type_animal_id")
    private TypeAnimalEntity type;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VisiteEntity> visites = new ArrayList<>();

    protected AnimalEntity() {}

    public AnimalEntity(Animal a, ProprietaireEntity p) {
        id = a.getId();
        nom = a.getNom();
        dateNaissance = a.getDateNaissance();
        proprietaire = p;
        type = new TypeAnimalEntity(a.getType());
        a.getVisites().forEach(v -> visites.add(new VisiteEntity(v, this)));
    }

    public Animal toDomain(Proprietaire p) {
        Animal a = new Animal(nom, type.toDomain(), dateNaissance, p);
        a.setId(id);
        visites.forEach(v -> a.getVisites().add(v.toDomain(a)));
        return a;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public TypeAnimalEntity getType() {
        return type;
    }

    public List<VisiteEntity> getVisites() {
        return visites;
    }
}
