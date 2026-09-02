package com.example.petclinic.application.usecase;

import com.example.petclinic.domain.model.Veterinaire;
import com.example.petclinic.domain.port.VeterinairePort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListerVeterinaire {

    private final VeterinairePort veterinairePort;

    public ListerVeterinaire(VeterinairePort veterinairePort) {
        this.veterinairePort = veterinairePort;
    }

    public List<Veterinaire> executer() {
        return veterinairePort.findAll();
    }
}
