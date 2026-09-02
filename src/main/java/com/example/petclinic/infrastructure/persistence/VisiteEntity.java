package com.example.petclinic.infrastructure.persistence;

import com.example.petclinic.domain.model.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visites")
public class VisiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_visite")
    private LocalDate date;

    private String motif;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private AnimalEntity animal;

    protected VisiteEntity() {}

    public VisiteEntity(Visite v, AnimalEntity a) {
        id = v.getId();
        date = v.getDate();
        motif = v.getMotif();
        animal = a;
    }

    public Visite toDomain(Animal a) {
        Visite v = new Visite(date, motif, a);
        v.setId(id);
        return v;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMotif() {
        return motif;
    }
}
