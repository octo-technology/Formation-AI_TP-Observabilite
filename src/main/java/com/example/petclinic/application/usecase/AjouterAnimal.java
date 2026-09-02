package com.example.petclinic.application.usecase;

import com.example.petclinic.domain.model.Animal;
import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.model.TypeAnimal;
import com.example.petclinic.domain.port.ProprietairePort;
import com.example.petclinic.domain.port.TypeAnimalPort;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AjouterAnimal {

    private final ProprietairePort proprietairePort;
    private final TypeAnimalPort typeAnimalPort;

    public AjouterAnimal(ProprietairePort proprietairePort, TypeAnimalPort typeAnimalPort) {
        this.proprietairePort = proprietairePort;
        this.typeAnimalPort = typeAnimalPort;
    }

    public void executer(Long proprietaireId, String nom, String type, LocalDate dateNaissance) {
        Proprietaire proprietaire = proprietairePort.findById(proprietaireId).orElseThrow();
        TypeAnimal typeAnimal = trouverOuCreerType(type);
        proprietaire.getAnimaux().add(new Animal(nom, typeAnimal, dateNaissance, proprietaire));
        proprietairePort.save(proprietaire);
    }

    private TypeAnimal trouverOuCreerType(String type) {
        return typeAnimalPort.findAll().stream()
                .filter(candidate -> candidate.getLibelle().equalsIgnoreCase(type))
                .findFirst()
                .orElseGet(() -> typeAnimalPort.save(new TypeAnimal(type)));
    }
}
