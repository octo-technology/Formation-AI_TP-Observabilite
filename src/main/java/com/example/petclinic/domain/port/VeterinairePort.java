package com.example.petclinic.domain.port;

import com.example.petclinic.domain.model.Veterinaire;
import java.util.List;

public interface VeterinairePort {

    List<Veterinaire> findAll();
}
