/**
 * Interfață pentru gestionarea operațiilor asupra entității Atractie.
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.Atractie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Interfața care extinde JpaRepository pentru a putea folosi operațiile CRUD
public interface AtractieRepository extends JpaRepository<Atractie, Integer> {
    List<Atractie> findByOrasId(Integer orasId);
}