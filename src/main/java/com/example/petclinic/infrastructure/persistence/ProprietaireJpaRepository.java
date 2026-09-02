package com.example.petclinic.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietaireJpaRepository extends JpaRepository<ProprietaireEntity, Long> {

    List<ProprietaireEntity> findByNomContainingIgnoreCase(String nom);
}
