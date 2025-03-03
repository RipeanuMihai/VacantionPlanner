/**
 * Serviciu pentru gestionarea interacțiunilor cu API-ul extern care furnizează
 * informații despre țări, cum ar fi steagurile și continentele.
 *
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

// Serviciul care se ocupă de apelarea API-ului extern pentru a obține informații despre țări
@Service
public class CountryCityService {

    // URL-ul API-ului extern pentru a obține informații despre țară
    private static final String COUNTRY_API_URL = "https://restcountries.com/v3.1/name/{country}";

    // RestTemplate este folosit pentru a face cereri HTTP către API-ul extern
    private final RestTemplate restTemplate;

    // Constructor
    public CountryCityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Metoda care apelează API-ul extern pentru a obține informații despre țară
    public String getCountries(String query) {
        // Apelăm API-ul extern pentru a obține informații despre țară
        String url = COUNTRY_API_URL.replace("{country}", query);
        // Obținem răspunsul API-ului extern
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Response from API: " + response);
        return response;
    }

    // Metoda care obține steagul unei țări
    public String getFlagForCountry(String countryName) {
        try {
            // Construim URL-ul pentru API-ul extern
            String apiResponse = getCountries(countryName);

            // Parsează răspunsul API-ului extern
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> responseList = mapper.readValue(apiResponse, new TypeReference<>() {});

            // Obținem URL-ul steagului
            if (!responseList.isEmpty()) {
                Map<String, Object> countryData = responseList.get(0);
                Map<String, String> flags = (Map<String, String>) countryData.get("flags");
                return flags.get("png"); // Returnăm URL-ul steagului
            }
        } catch (Exception e) {
            e.printStackTrace(); // Logăm erorile
        }

        // Returnăm null dacă țara nu este găsită
        return null;
    }

    // Metoda care obține continentul unei țări
    public String getContinentForCountry(String countryName) {
        try {
            // Construim URL-ul pentru API-ul extern
            String apiResponse = getCountries(countryName);

            // Parsează răspunsul API-ului extern
            ObjectMapper mapper = new ObjectMapper();
            // Obținem lista de țări
            List<Map<String, Object>> responseList = mapper.readValue(apiResponse, new TypeReference<>() {});

            // Obținem continentul
            if (!responseList.isEmpty()) {
                Map<String, Object> countryData = responseList.get(0);
                return (String) countryData.get("region"); // "region" conține continentul
            }
        } catch (Exception e) {
            e.printStackTrace(); // Logăm erorile
        }

        // Returnăm "Unknown" dacă continentul nu este găsit
        return "Unknown";
    }
}