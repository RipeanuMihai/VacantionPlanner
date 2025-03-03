/**
 * Controler REST pentru gestionarea operațiunilor API, incluzând funcționalități
 * de autocomplete pentru țări și obținerea listei de țări selectate.
 *
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelapp.travel_app.service.CountryCityService;
import com.travelapp.travel_app.service.TaraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Controlerul care gestionează operațiunile API
@RestController
@RequestMapping("/api")
public class APIController {

    private final CountryCityService countryCityService;
    private final TaraService taraService;

    // Constructor
    public APIController(CountryCityService countryCityService, TaraService taraService) {
        this.taraService = taraService;
        this.countryCityService = countryCityService;
    }

    // Endpoint pentru autocomplete
    @GetMapping("/autocomplete/tari")
    public ResponseEntity<?> getCountries(@RequestParam String query) {
        // Obținem răspunsul API-ului extern
        String rawResponse = countryCityService.getCountries(query);

        // Parsăm răspunsul API-ului extern și returnăm doar numele țărilor și steagurile
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Parsăm răspunsul API-ului extern
            List<Map<String, Object>> rawList = mapper.readValue(rawResponse, new TypeReference<>() {
            });
            // Simplificăm lista de țări
            List<Map<String, String>> simplifiedList = rawList.stream()
                    // Pentru fiecare țară, obținem numele și URL-ul steagului
                    .map(entry -> {
                        Map<String, String> country = new HashMap<>();
                        country.put("name", ((Map<String, String>) entry.get("name")).get("common"));
                        country.put("flag", ((Map<String, String>) entry.get("flags")).get("png"));
                        return country;
                    })
                    .collect(Collectors.toList());

            // Returnăm răspunsul simplificat
            return ResponseEntity.ok(simplifiedList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la procesarea răspunsului API.");
        }
    }

    @GetMapping("/selected-countries")
    public ResponseEntity<List<String>> getSelectedCountries() {
        // Obținem lista de țări selectate
        List<String> selectedCountries = taraService.getAllSelectedCountries();
        return ResponseEntity.ok(selectedCountries);
    }
}