/**
 * Interfață pentru gestionarea operațiilor asupra entității Oras.
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.Oras;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Interfața care extinde JpaRepository pentru a putea folosi operațiile CRUD
@Repository
public interface OrasRepository extends JpaRepository<Oras, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Oras o WHERE o.id = :id")
    void deleteOrasById(@Param("id") Integer id);
}