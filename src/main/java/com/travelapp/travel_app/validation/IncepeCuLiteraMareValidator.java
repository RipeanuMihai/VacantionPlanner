/**
 * Validator personalizat pentru validarea faptului că un șir începe
 * cu o literă majusculă, utilizat împreună cu adnotarea @StartsWithCapital.
 *
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.validation;

import com.travelapp.travel_app.annotations.IncepeCuLiteraMare;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Validator personalizat pentru validarea faptului că un șir începe cu o literă majusculă
public class IncepeCuLiteraMareValidator implements ConstraintValidator<IncepeCuLiteraMare, String> {
    @Override
    // Metoda de validare
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return Character.isUpperCase(value.charAt(0));
    }
}