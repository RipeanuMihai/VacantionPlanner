/**
 * DTO pentru reprezentarea unei țări cu steagul asociat.
 *
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.dto;

// DTO pentru reprezentarea unei țări cu steagul asociat
public class TaraCuSteag {
    private Integer id;
    private String nume;
    private String steag;

    // Constructor
    public TaraCuSteag(Integer id, String nume, String steag) {
        this.id = id;
        this.nume = nume;
        this.steag = steag;
    }

    // Getteri și setteri
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

    public String getSteag() {
        return steag;
    }

    public void setSteag(String flag) {
        this.steag = flag;
    }
}