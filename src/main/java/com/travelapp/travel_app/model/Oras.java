/**
 * Clasa pentru gestionarea orașelor din aplicație.
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.model;

import com.travelapp.travel_app.annotations.IncepeCuLiteraMare;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
public class Oras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @IncepeCuLiteraMare(message = "Numele orașului trebuie să înceapă cu literă mare.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Numele orașului trebuie să conțină doar litere.")
    @NotBlank(message = "Numele orașului nu poate fi gol.")
    @Size(max = 100, message = "Numele orașului nu poate depăși 100 de caractere.")
    @Column(name = "nume", nullable = false, length = 100)
    private String nume;

    @ManyToOne
    @JoinColumn(name = "tara_id")
    private Tara tara;

    @IncepeCuLiteraMare(message = "Descrierea trebuie să înceapă cu literă mare.")
    @Size(max = 100, message = "Descrierea nu poate depăși 100 de caractere.")
    @Lob
    @Column(name = "descriere")
    private String descriere;

    // Adaugă lista de atracții asociate acestui oraș
    @OneToMany(mappedBy = "oras", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Atractie> atractii;

    @Transient
    private double progres;

    // Getter pentru progres
    public double getProgres() {
        return progres;
    }

    // Setter pentru progres
    public void setProgres(double progres) {
        this.progres = progres;
    }
    // Getter pentru lista de atracții
    public List<Atractie> getAtractii() {
        return atractii;
    }

    public void setAtractii(List<Atractie> atractii) {
        this.atractii.clear(); // Golește colecția curentă
        if (atractii != null) {
            this.atractii.addAll(atractii); // Adaugă noile elemente
        }
    }

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

    public Tara getTara() {
        return tara;
    }

    public void setTara(Tara tara) {
        this.tara = tara;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

}