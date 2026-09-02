package com.example.petclinic.application.usecase;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.ProprietairePort;
import org.springframework.stereotype.Service;

@Service
public class TrouverProprietaire {

    private final ProprietairePort proprietairePort;

    public TrouverProprietaire(ProprietairePort proprietairePort) {
        this.proprietairePort = proprietairePort;
    }

    public Proprietaire executer(Long id) {
        return proprietairePort.findById(id).orElseThrow();
    }
}
