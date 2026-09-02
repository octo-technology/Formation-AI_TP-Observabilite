package com.example.petclinic.domain.port;

import com.example.petclinic.domain.model.Proprietaire;
import java.util.List;
import java.util.Optional;

public interface ProprietairePort {

    List<Proprietaire> findAll();

    List<Proprietaire> findByNom(String nom);

    Optional<Proprietaire> findById(Long id);

    Proprietaire save(Proprietaire proprietaire);
}
