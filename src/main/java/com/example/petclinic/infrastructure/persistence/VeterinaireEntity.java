package com.example.petclinic.infrastructure.persistence;

import com.example.petclinic.domain.model.Specialite;
import com.example.petclinic.domain.model.Veterinaire;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "veterinaires")
public class VeterinaireEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "veterinaires_specialites",
            joinColumns = @JoinColumn(name = "veterinaire_id"),
            inverseJoinColumns = @JoinColumn(name = "specialite_id"))
    private Set<SpecialiteEntity> specialites = new HashSet<>();

    protected VeterinaireEntity() {}

    public Veterinaire toDomain() {
        Set<Specialite> result = new HashSet<>();
        specialites.forEach(s -> result.add(s.toDomain()));
        Veterinaire v = new Veterinaire(prenom, nom, result);
        v.setId(id);
        return v;
    }
}
