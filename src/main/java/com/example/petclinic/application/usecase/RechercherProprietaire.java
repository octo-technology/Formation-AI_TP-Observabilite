package com.example.petclinic.application.usecase;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.ProprietairePort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RechercherProprietaire {

    private final ProprietairePort proprietairePort;

    public RechercherProprietaire(ProprietairePort proprietairePort) {
        this.proprietairePort = proprietairePort;
    }

    public List<Proprietaire> executer(String nom) {
        return nom.isBlank() ? proprietairePort.findAll() : proprietairePort.findByNom(nom);
    }
}
