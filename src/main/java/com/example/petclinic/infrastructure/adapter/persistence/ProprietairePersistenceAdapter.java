package com.example.petclinic.infrastructure.adapter.persistence;

import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.ProprietairePort;
import com.example.petclinic.infrastructure.persistence.ProprietaireEntity;
import com.example.petclinic.infrastructure.persistence.ProprietaireJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProprietairePersistenceAdapter implements ProprietairePort {

    private final ProprietaireJpaRepository repository;

    public ProprietairePersistenceAdapter(ProprietaireJpaRepository repository) {
        this.repository = repository;
    }

    public List<Proprietaire> findAll() {
        return repository.findAll().stream().map(ProprietaireEntity::toDomain).toList();
    }

    public List<Proprietaire> findByNom(String nom) {
        return repository.findByNomContainingIgnoreCase(nom).stream()
                .map(ProprietaireEntity::toDomain)
                .toList();
    }

    public Optional<Proprietaire> findById(Long id) {
        return repository.findById(id).map(ProprietaireEntity::toDomain);
    }

    public Proprietaire save(Proprietaire p) {
        return repository.save(new ProprietaireEntity(p)).toDomain();
    }
}
