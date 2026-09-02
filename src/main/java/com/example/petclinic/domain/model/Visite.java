package com.example.petclinic.domain.model;

import java.time.LocalDate;

public class Visite {

    private Long id;
    private final LocalDate date;
    private final String motif;
    private final Animal animal;

    public Visite(LocalDate date, String motif, Animal animal) {
        this.date = date;
        this.motif = motif;
        this.animal = animal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMotif() {
        return motif;
    }

    public Animal getAnimal() {
        return animal;
    }
}
