package com.example.petclinic.application.usecase;

import com.example.petclinic.domain.model.Animal;
import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.model.Visite;
import com.example.petclinic.domain.port.ProprietairePort;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AjouterVisite {

    private final ProprietairePort proprietairePort;

    public AjouterVisite(ProprietairePort proprietairePort) {
        this.proprietairePort = proprietairePort;
    }

    public void executer(Long proprietaireId, Long animalId, LocalDate date, String motif) {
        Proprietaire proprietaire = proprietairePort.findById(proprietaireId).orElseThrow();
        Animal animal =
                proprietaire.getAnimaux().stream()
                        .filter(candidate -> candidate.getId().equals(animalId))
                        .findFirst()
                        .orElseThrow();
        animal.getVisites().add(new Visite(date, motif, animal));
        proprietairePort.save(proprietaire);
    }
}
