/**
 * Interfață pentru gestionarea operațiilor asupra entității Tara.
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.Tara;
import org.springframework.data.jpa.repository.JpaRepository;

// Interfața care extinde JpaRepository pentru a putea folosi operațiile CRUD
public interface TaraRepository extends JpaRepository<Tara, Integer> {
    boolean existsByNume(String countryName);
}