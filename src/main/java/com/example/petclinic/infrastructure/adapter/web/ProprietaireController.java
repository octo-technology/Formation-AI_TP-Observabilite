package com.example.petclinic.infrastructure.adapter.web;

import com.example.petclinic.application.usecase.AjouterAnimal;
import com.example.petclinic.application.usecase.AjouterVisite;
import com.example.petclinic.application.usecase.CreerProprietaire;
import com.example.petclinic.application.usecase.RechercherProprietaire;
import com.example.petclinic.application.usecase.TrouverProprietaire;
import com.example.petclinic.domain.model.Proprietaire;
import com.example.petclinic.domain.port.TypeAnimalPort;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProprietaireController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProprietaireController.class);

    private final RechercherProprietaire rechercherProprietaire;
    private final CreerProprietaire creerProprietaire;
    private final TrouverProprietaire trouverProprietaire;
    private final AjouterAnimal ajouterAnimal;
    private final AjouterVisite ajouterVisite;
    private final TypeAnimalPort typeAnimalPort;

    public ProprietaireController(
            RechercherProprietaire rechercherProprietaire,
            CreerProprietaire creerProprietaire,
            TrouverProprietaire trouverProprietaire,
            AjouterAnimal ajouterAnimal,
            AjouterVisite ajouterVisite,
            TypeAnimalPort typeAnimalPort) {
        this.rechercherProprietaire = rechercherProprietaire;
        this.creerProprietaire = creerProprietaire;
        this.trouverProprietaire = trouverProprietaire;
        this.ajouterAnimal = ajouterAnimal;
        this.ajouterVisite = ajouterVisite;
        this.typeAnimalPort = typeAnimalPort;
    }

    @GetMapping({"/", "/proprietaires"})
    public String rechercher(@RequestParam(defaultValue = "") String nom, Model modele) {
        LOGGER.info("Recherche des proprietaires");
        modele.addAttribute("proprietaires", rechercherProprietaire.executer(nom));
        modele.addAttribute("nom", nom);
        return "proprietaires/liste";
    }

    @GetMapping("/proprietaires/nouveau")
    public String nouveau(Model modele) {
        modele.addAttribute("proprietaire", new Proprietaire("", "", "", "", ""));
        return "proprietaires/formulaire";
    }

    @PostMapping("/proprietaires")
    public String creer(@Valid Proprietaire proprietaire, BindingResult resultat) {
        if (resultat.hasErrors()) return "proprietaires/formulaire";
        return "redirect:/proprietaires/" + creerProprietaire.executer(proprietaire).getId();
    }

    @GetMapping("/proprietaires/{id}")
    public String details(@PathVariable Long id, Model modele) {
        modele.addAttribute("proprietaire", trouverProprietaire.executer(id));
        modele.addAttribute("typesAnimaux", typeAnimalPort.findAll());
        return "proprietaires/details";
    }

    @PostMapping("/proprietaires/{proprietaireId}/animaux")
    public String ajouterAnimal(
            @PathVariable Long proprietaireId,
            @RequestParam String nom,
            @RequestParam String type,
            @RequestParam LocalDate dateNaissance) {
        ajouterAnimal.executer(proprietaireId, nom, type, dateNaissance);
        return "redirect:/proprietaires/" + proprietaireId;
    }

    @PostMapping("/proprietaires/{proprietaireId}/animaux/{animalId}/visites")
    public String ajouterVisite(
            @PathVariable Long proprietaireId,
            @PathVariable Long animalId,
            @RequestParam LocalDate date,
            @RequestParam String motif) {
        ajouterVisite.executer(proprietaireId, animalId, date, motif);
        return "redirect:/proprietaires/" + proprietaireId;
    }
}
