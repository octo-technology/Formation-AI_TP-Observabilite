package com.example.petclinic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeAnimalJpaRepository extends JpaRepository<TypeAnimalEntity, Long> {}
