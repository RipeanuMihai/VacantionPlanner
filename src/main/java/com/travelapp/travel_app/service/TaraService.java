/**
 * Serviciu pentru gestionarea operațiunilor asupra entității Tara,
 * inclusiv preluarea, adăugarea și actualizarea țărilor din baza de date.
 *
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.service;

import com.travelapp.travel_app.repository.TaraRepository;
import org.springframework.stereotype.Service;
import com.travelapp.travel_app.model.Tara;

import java.util.List;
import java.util.stream.Collectors;

// Serviciu
@Service
public class TaraService {

    private final TaraRepository taraRepository;

    public TaraService(TaraRepository taraRepository) {
        this.taraRepository = taraRepository;
    }

    // Metoda pentru adăugarea unei țări
    public List<String> getAllSelectedCountries() {
        return taraRepository.findAll()
                .stream()
                .map(Tara::getNume)
                .collect(Collectors.toList());
    }
}