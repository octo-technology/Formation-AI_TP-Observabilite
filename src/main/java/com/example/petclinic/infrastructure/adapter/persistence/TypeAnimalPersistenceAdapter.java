package com.example.petclinic.infrastructure.adapter.persistence;

import com.example.petclinic.domain.model.TypeAnimal;
import com.example.petclinic.domain.port.TypeAnimalPort;
import com.example.petclinic.infrastructure.persistence.TypeAnimalEntity;
import com.example.petclinic.infrastructure.persistence.TypeAnimalJpaRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TypeAnimalPersistenceAdapter implements TypeAnimalPort {
    private final TypeAnimalJpaRepository repository;

    public TypeAnimalPersistenceAdapter(TypeAnimalJpaRepository repository) {
        this.repository = repository;
    }

    public List<TypeAnimal> findAll() {
        return repository.findAll().stream().map(TypeAnimalEntity::toDomain).toList();
    }

    public TypeAnimal save(TypeAnimal t) {
        return repository.save(new TypeAnimalEntity(t)).toDomain();
    }
}
