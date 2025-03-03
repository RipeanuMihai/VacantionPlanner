/**
 * Configurație pentru aplicația TravelApp, inclusiv definitia bean-ului RestTemplate.
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;

// Configurație
@Configuration
public class Config {

    // Definirea bean-ului RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Crează și returnează un bean RestTemplate
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter(); // Crează și returnează un bean HiddenHttpMethodFilter
    }
}