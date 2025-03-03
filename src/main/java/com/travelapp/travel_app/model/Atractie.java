/**
 * Clasa pentru gestionarea atracțiilor turistice din aplicație.
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.model;

import com.travelapp.travel_app.annotations.IncepeCuLiteraMare;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Atractie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Numele trebuie să conțină doar litere.")
    @IncepeCuLiteraMare(message = "Numele atractiei trebuie să înceapă cu literă mare.")
    @NotBlank(message = "Numele atractiei este obligatoriu.")
    @Column(name = "nume", nullable = false, length = 100)
    private String nume;

    @IncepeCuLiteraMare(message = "Descrierea trebuie să înceapă cu literă mare.")
    @Size(max = 100, message = "Descrierea nu poate depăși 100 de caractere.")
    @Lob
    @Column(name = "descriere")
    private String descriere;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Statusul atractiei este obligatoriu.")
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oras_id")
    private Oras oras;

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

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public com.travelapp.travel_app.model.Oras getOras() {
        return oras;
    }

    public void setOras(com.travelapp.travel_app.model.Oras oras) {
        this.oras = oras;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}