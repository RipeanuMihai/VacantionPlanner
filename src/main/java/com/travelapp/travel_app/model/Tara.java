/**
 * Clasa pentru gestionarea țărilor din aplicație.
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tara {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100, message = "Numele țării nu poate depăși 100 de caractere.")
    @NotBlank(message = "Numele țării este obligatoriu.")
    @Column(name = "nume", nullable = false, length = 100)
    private String nume;

    @OneToMany(mappedBy = "tara", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Oras> orase = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<Oras> getOrase() {
        return orase;
    }

    public void setOrase(List<Oras> orase) {
        this.orase = orase;
    }

}