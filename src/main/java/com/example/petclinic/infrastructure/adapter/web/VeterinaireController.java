package com.example.petclinic.infrastructure.adapter.web;

import com.example.petclinic.application.usecase.ListerVeterinaire;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VeterinaireController {
    private final ListerVeterinaire listerVeterinaire;

    public VeterinaireController(ListerVeterinaire listerVeterinaire) {
        this.listerVeterinaire = listerVeterinaire;
    }

    @GetMapping("/veterinaires")
    public String lister(Model model) {
        model.addAttribute("veterinaires", listerVeterinaire.executer());
        return "veterinaires/liste";
    }
}
