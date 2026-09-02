package com.example.petclinic.domain.port;

import com.example.petclinic.domain.model.TypeAnimal;
import java.util.List;

public interface TypeAnimalPort {

    List<TypeAnimal> findAll();

    TypeAnimal save(TypeAnimal typeAnimal);
}
