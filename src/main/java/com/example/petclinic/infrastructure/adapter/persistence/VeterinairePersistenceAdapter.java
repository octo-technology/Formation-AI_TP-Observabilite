package com.example.petclinic.infrastructure.adapter.persistence;

import com.example.petclinic.domain.model.Veterinaire;
import com.example.petclinic.domain.port.VeterinairePort;
import com.example.petclinic.infrastructure.persistence.VeterinaireEntity;
import com.example.petclinic.infrastructure.persistence.VeterinaireJpaRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VeterinairePersistenceAdapter implements VeterinairePort {

    private final VeterinaireJpaRepository repository;

    public VeterinairePersistenceAdapter(VeterinaireJpaRepository repository) {
        this.repository = repository;
    }

    public List<Veterinaire> findAll() {
        return repository.findAll().stream().map(VeterinaireEntity::toDomain).toList();
    }
}
