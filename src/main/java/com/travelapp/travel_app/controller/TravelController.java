/**
 * Controller pentru gestionarea operațiunilor web în aplicația TravelApp.
 * Oferă funcționalități pentru gestionarea țărilor, orașelor și atracțiilor,
 * inclusiv vizualizare, adăugare, editare și ștergere.
 *
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.controller;

import com.travelapp.travel_app.dto.TaraCuSteag;
import com.travelapp.travel_app.model.Atractie;
import com.travelapp.travel_app.model.Oras;
import com.travelapp.travel_app.model.Status;
import com.travelapp.travel_app.model.Tara;
import com.travelapp.travel_app.repository.AtractieRepository;
import com.travelapp.travel_app.repository.OrasRepository;
import com.travelapp.travel_app.repository.TaraRepository;
import com.travelapp.travel_app.service.CountryCityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web")
public class TravelController {

    @Autowired
    private TaraRepository taraRepository;

    @Autowired
    private OrasRepository orasRepository;

    @Autowired
    private AtractieRepository atractieRepository;

    @Autowired
    private CountryCityService countryCityService;

    @GetMapping("/harta")
    public String showHarta() {
        return "harta"; // Numele fișierului HTML din templates (harta.html)
    }

    // Vizualizează toate țările
    @GetMapping("/tara")
    public String getTari(Model model) {
        // Obține toate țările din baza de date
        List<Tara> tari = taraRepository.findAll();

        // Obține steagurile utilizând API-ul extern
        List<TaraCuSteag> tariCuSteaguri = tari.stream().map(tara -> {
            String flag = countryCityService.getFlagForCountry(tara.getNume());
            return new TaraCuSteag(tara.getId(), tara.getNume(), flag);
        }).collect(Collectors.toList());

        // Totalul țărilor pe continente (valori hardcodate)
        Map<String, Long> totalTariPeContinente = Map.of(
                "Africa", 54L,
                "Asia", 49L,
                "Europe", 44L,
                "Americas", 35L,
                "Oceania", 14L,
                "Antarctic", 1L
        );

        // Grupare țări pe continente și numărare
        Map<String, Long> tariPeContinente = tariCuSteaguri.stream()
                .collect(Collectors.groupingBy(
                        tara -> countryCityService.getContinentForCountry(tara.getNume()),
                        Collectors.counting()
                ));

        // Asigură-te că toate continentele sunt incluse în `tariPeContinente`
        Map<String, Long> tariPeContinenteCompleta = new HashMap<>(totalTariPeContinente);
        totalTariPeContinente.keySet().forEach(continent ->
                tariPeContinenteCompleta.put(continent, tariPeContinente.getOrDefault(continent, 0L))
        );

        // Calcul procente pentru fiecare continent
        Map<String, String> procenteContinente = tariPeContinenteCompleta.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            long totalTariContinent = totalTariPeContinente.getOrDefault(entry.getKey(), 1L);
                            return String.format("%.2f%%", (entry.getValue() * 100.0) / totalTariContinent);
                        }
                ));

        // Adaugă datele în model pentru Thymeleaf
        model.addAttribute("tari", tariCuSteaguri);
        model.addAttribute("procenteContinente", procenteContinente);
        model.addAttribute("tariPeContinente", tariPeContinenteCompleta);
        model.addAttribute("totalTariPeContinente", totalTariPeContinente);

        return "tara";
    }

    // Pagina pentru adăugarea unei țări
    @GetMapping("/tara/add")
    public String showAddTaraPage() {
        return "addTara"; // Thymeleaf va încărca pagina `addTara.html`
    }

    // Procesarea formularului de adăugare
    @PostMapping("/tara/add")
    public String addTara(@RequestParam String countryName, Model model) {
        // Verifică dacă numele țării este gol
        if (countryName == null || countryName.trim().isEmpty()) {
            model.addAttribute("error", "Numele țării este obligatoriu!");
            return "addTara";
        }

        // Verifică dacă țara există deja în baza de date
        if (taraRepository.existsByNume(countryName)) {
            model.addAttribute("error", "Țara există deja în baza de date!");
            return "addTara";
        }

        // Verifică dacă țara există în API-ul extern
        try {
            String flagUrl = countryCityService.getFlagForCountry(countryName); // Utilizează API-ul extern
            if (flagUrl == null || flagUrl.isEmpty()) {
                model.addAttribute("error", "Țara nu este validă sau nu există în API-ul extern!");
                return "addTara";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Eroare la conectarea la API-ul extern!");
            return "addTara";
        }

        // Creează entitatea Tara și salvează în baza de date
        Tara tara = new Tara();
        tara.setNume(countryName);
        taraRepository.save(tara);

        return "redirect:/web/tara"; // Redirecționează către lista de țări
    }

    // Vizualizează orașele dintr-o țară
    @GetMapping("/tara/{taraId}/orase")
    public String viewOraseByTara(@PathVariable("taraId") Integer taraId, Model model) {
        // Găsim țara în baza de date
        Tara tara = taraRepository.findById(taraId).orElse(null);
        if (tara == null) {
            model.addAttribute("error", "Țara specificată nu există.");
            return "error"; // pagina de eroare
        }

        // Lista orașelor din țara specificată
        List<Oras> orase = tara.getOrase();

        // Calculăm progresul atracțiilor pentru fiecare oraș
        for (Oras oras : orase) {
            int totalAtractii = oras.getAtractii().size();
            long atractiiFinalizate = oras.getAtractii().stream()
                    .filter(atractie -> atractie.getStatus() == Status.FINALIZATA)
                    .count();
            double progres = totalAtractii > 0 ? (double) atractiiFinalizate / totalAtractii * 100.00 : 0.0;
            progres = Math.round(progres * 100.00) / 100.00; // Rotunjire la două zecimale
            oras.setProgres(progres); // Setăm progresul în procent (rotunjit la int)
        }

        // Adăugăm țara și orașele în model
        model.addAttribute("tara", tara);
        model.addAttribute("orase", orase);

        return "orase"; // returnăm numele șablonului Thymeleaf
    }

    // Șterge o țară
    @DeleteMapping("/tara/delete/{id}")
    public String deleteTara(@PathVariable("id") Integer id, Model model) {
        // Verifică dacă țara există
        if (!taraRepository.existsById(id)) {
            model.addAttribute("error", "Țara nu a fost găsită!");
            return "error"; // Pagina de eroare
        }

        // Șterge țara din baza de date
        taraRepository.deleteById(id); // Șterge țara din baza de date
        return "redirect:/web/tara"; // Redirecționează către lista de țări
    }

    // Sterge un oraș
    @DeleteMapping("/oras/delete/{id}")
    public String deleteOras(@PathVariable("id") Integer id, Model model) {
        // Verifică dacă orașul există
        Optional<Oras> orasOptional = orasRepository.findById(id);
        if (!orasRepository.existsById(id)) {
            model.addAttribute("error", "Orașul cu ID-ul specificat nu există.");
            return "error";
        }

        // Găsește orașul și țara pentru redirecționare
        Oras oras = orasOptional.get();
        Integer taraId = oras.getTara().getId(); // ID-ul țării pentru redirecționare
        // Folosește metoda personalizată pentru ștergere
        orasRepository.deleteOrasById(id);

        model.addAttribute("success", "Orașul a fost șters cu succes.");
        return "redirect:/web/tara/" + taraId + "/orase"; // Redirecționare către lista de țări
    }

    // Formular pentru adăugarea unei atracții
    @GetMapping("/oras/{orasId}/atractie/add")
    public String showAddAtractiePage(@PathVariable("orasId") Integer orasId, Model model) {
        // Găsește orașulx
        Optional<Oras> orasOptional = orasRepository.findById(orasId);
        if (orasOptional.isEmpty()) {
            model.addAttribute("error", "Orașul nu a fost găsit!");
            return "error";
        }

        // Adaugă orașul și un obiect nou pentru atracție în model
        Oras oras = orasOptional.get();
        Atractie atractie = new Atractie();
        model.addAttribute("atractie", atractie);
        model.addAttribute("oras", oras);
        model.addAttribute("statuses", Status.values());
        return "addAtractie";
    }

    // Adaugă o atracție
    @PostMapping("/oras/{orasId}/atractie/add")
    public String addAtractie(@PathVariable("orasId") Integer orasId, @Valid @ModelAttribute("atractie") Atractie atractie, BindingResult bindingResult, Model model) {
        // Găsește orașul
        Optional<Oras> orasOptional = orasRepository.findById(orasId);
        if (orasOptional.isEmpty()) {
            model.addAttribute("error", "Orașul nu a fost găsit!");
            return "error";
        }

        // Verifică erori de validare
        Oras oras = orasOptional.get();
        if (bindingResult.hasErrors()) {
            model.addAttribute("oras", oras);
            model.addAttribute("statuses", Status.values());
            return "addAtractie";
        }

        // Adaugă orașul la atracție și salvează în baza de date
        atractie.setOras(oras);
        atractieRepository.save(atractie);
        return "redirect:/web/oras/" + orasId + "/atractii";
    }

    // Șterge o atracție
    @DeleteMapping("/atractie/delete/{id}")
    public String deleteAttraction(@PathVariable("id") Integer id, Model model) {
        // Găsește atracția și orașul pentru redirecționare
        return atractieRepository.findById(id)
                .map(atractie -> {
                    Integer orasId = atractie.getOras().getId();
                    atractieRepository.delete(atractie);
                    return "redirect:/web/oras/" + orasId + "/atractii";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Atracția nu a fost găsită!");
                    return "error";
                });
    }

    // Formular pentru editarea unei țări
    @GetMapping("/oras/edit/{id}")
    public String showEditOrasForm(@PathVariable("id") Integer id, Model model) {
        Optional<Oras> orasOptional = orasRepository.findById(id);
        if (orasOptional.isEmpty()) {
            model.addAttribute("error", "Orașul nu a fost găsit!");
            return "redirect:/web/orase";
        }
        model.addAttribute("oras", orasOptional.get());
        return "editOras";
    }

    // Actualizare oraș
    @PutMapping("/oras/edit/{orasId}")
    public String updateCity(@PathVariable("orasId") Integer orasId, @Valid @ModelAttribute("oras") Oras oras, BindingResult bindingResult, Model model) {
        // Validare
        if (bindingResult.hasErrors()) {
            oras.setId(orasId);
            model.addAttribute("oras", oras);
            return "editOras";
        }

        // Găsire oraș
        Optional<Oras> orasExistenta = orasRepository.findById(orasId);
        if (orasExistenta.isEmpty()) {
            model.addAttribute("error", "Orașul nu a fost găsit!");
            return "error";
        }

        // Actualizare oraș
        Oras orasActualizat = orasExistenta.get();
        orasActualizat.setNume(oras.getNume());
        orasActualizat.setDescriere(oras.getDescriere());
        orasRepository.save(orasActualizat);

        return "redirect:/web/tara/" + orasActualizat.getTara().getId() + "/orase";
    }

    // Formular pentru editarea unei atracții
    @GetMapping("/atractie/edit/{id}")
    public String editAtractie(@PathVariable("id") Integer id, Model model) {
        // Găsire atracție
        Optional<Atractie> atractieOptional = atractieRepository.findById(id);
        if (atractieOptional.isEmpty()) {
            model.addAttribute("error", "Atracția nu a fost găsită!");
            return "error";
        }

        // Adăugare atracție în model
        Atractie atractie = atractieOptional.get();
        model.addAttribute("atractie", atractie);
        model.addAttribute("statuses", Status.values());
        return "editAtractie";
    }

    // Procesare modificare atracție
    @PutMapping("/atractie/edit/{id}")
    public String updateAtractie(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("atractie") Atractie atractie,
                                 BindingResult bindingResult,
                                 Model model) {
        // Validare
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Status.values());
            return "editAtractie";
        }

        // Găsire atracție
        Optional<Atractie> atractieOptional = atractieRepository.findById(id);
        if (atractieOptional.isEmpty()) {
            model.addAttribute("error", "Atracția nu a fost găsită!");
            return "error";
        }

        // Actualizare atracție
        Atractie existingAtractie = atractieOptional.get();
        existingAtractie.setNume(atractie.getNume());
        existingAtractie.setDescriere(atractie.getDescriere());
        existingAtractie.setStatus(atractie.getStatus());

        existingAtractie.setOras(existingAtractie.getOras());

        atractieRepository.save(existingAtractie);

        return "redirect:/web/oras/" + existingAtractie.getOras().getId() + "/atractii";
    }

    // Formular pentru editarea unei țări
    @GetMapping("/tara/edit/{id}")
    public String showEditTaraForm(@PathVariable("id") Integer id, Model model) {
        Optional<Tara> taraOptional = taraRepository.findById(id);
        if (taraOptional.isPresent()) {
            model.addAttribute("tara", taraOptional.get());
            return "editTara"; // Numele șablonului Thymeleaf pentru editare
        } else {
            model.addAttribute("error", "Țara nu a fost găsită!");
            return "error"; // Pagina de eroare
        }
    }

    // Procesare editare țară
    @PutMapping("/tara/edit/{id}")
    public String editTara(@PathVariable("id") Integer id, @RequestParam("countryName") String countryName, Model model) {
        // Validare nume țară
        if (countryName == null || countryName.trim().isEmpty()) {
            model.addAttribute("error", "Numele țării este obligatoriu!");
            return "editTara";
        }

        // Găsire țară existentă
        Optional<Tara> existingTara = taraRepository.findById(id);
        if (existingTara.isEmpty()) {
            model.addAttribute("error", "Țara nu a fost găsită!");
            return "editTara";
        }

        // Verificare duplicate
        boolean duplicateExists = taraRepository.findAll().stream()
                .anyMatch(t -> t.getNume().equalsIgnoreCase(countryName) && !t.getId().equals(id));
        if (duplicateExists) {
            model.addAttribute("error", "Țara există deja în baza de date!");
            model.addAttribute("tara", existingTara.get());
            return "editTara";
        }

        // Verificare validitate țară în API extern
        try {
            String flagUrl = countryCityService.getFlagForCountry(countryName);
            if (flagUrl == null || flagUrl.isEmpty()) {
                model.addAttribute("error", "Țara nu este validă sau nu există în API-ul extern!");
                return "editTara";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Eroare la conectarea la API-ul extern: " + e.getMessage());
            return "editTara";
        }

        // Actualizare țară
        Tara tara = existingTara.get();
        tara.setNume(countryName);
        taraRepository.save(tara);

        // Redirecționare la lista de țări
        return "redirect:/web/tara";
    }

    // Formular pentru adăugarea unui oraș
    @GetMapping("/tara/{taraId}/oras/add")
    public String showAddOrasForm(@PathVariable("taraId") Integer taraId, Model model) {
        // Găsim țara în baza de date
        Optional<Tara> tara = taraRepository.findById(taraId);
        if (tara.isEmpty()) {
            model.addAttribute("error", "Țara nu a fost găsită!");
            return "error";
        }

        // Adăugăm țara și un obiect nou pentru oraș în model
        model.addAttribute("oras", new Oras()); // Obiect nou pentru formular
        model.addAttribute("tara", tara.get()); // Datele țării
        return "addOras"; // Template-ul Thymeleaf
    }

    // Procesare adăugare oraș
    @PostMapping("/tara/{taraId}/oras/add")
    public String addOras(@PathVariable("taraId") Integer taraId,
                          @Valid @ModelAttribute("oras") Oras oras,
                          BindingResult result,
                          Model model) {
        // Găsim țara în baza de date
        Optional<Tara> tara = taraRepository.findById(taraId);
        if (tara.isEmpty()) {
            model.addAttribute("error", "Țara nu a fost găsită!");
            return "error";
        }

        // Validare
        if (result.hasErrors()) {
            model.addAttribute("tara", tara.get());
            return "addOras";
        }

        // Adăugăm țara la oraș și salvăm în baza de date
        oras.setTara(tara.get());
        orasRepository.save(oras); // Salvăm orașul
        return "redirect:/web/tara/" + taraId + "/orase"; // Redirecționăm către lista orașelor
    }

    // Vizualizează atracțiile dintr-un oraș
    @GetMapping("/oras/{orasId}/atractii")
    public String viewAtractiiByOras(@PathVariable("orasId") Integer orasId, Model model) {
        // Găsește orașul
        Optional<Oras> oras = orasRepository.findById(orasId);
        if (oras.isEmpty()) {
            model.addAttribute("error", "Orașul nu a fost găsit!");
            return "error"; // Pagina de eroare
        }

        // Găsește toate atracțiile din oraș
        List<Atractie> atractii = atractieRepository.findByOrasId(orasId);
        model.addAttribute("oras", oras.get());
        model.addAttribute("atractii", atractii);
        return "atractii"; // Thymeleaf va căuta un fișier atractii.html
    }
}