package com.example.petclinic.application.usecase;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.ProprietairePort;
import org.springframework.stereotype.Service;

@Service
public class CreerProprietaire {

    private final ProprietairePort proprietairePort;

    public CreerProprietaire(ProprietairePort proprietairePort) {
        this.proprietairePort = proprietairePort;
    }

    public Proprietaire executer(Proprietaire proprietaire) {
        return proprietairePort.save(proprietaire);
    }
}
