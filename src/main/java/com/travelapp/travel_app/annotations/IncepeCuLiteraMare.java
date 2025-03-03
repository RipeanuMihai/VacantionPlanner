/**
 * Anotare personalizată pentru validarea dacă o valoare începe cu literă mare.
 * Utilizează validatorul {@link StartsWithCapitalValidator}.
 *
 * @author Ripeanu Mihai
 * @version 11 Ianuarie 2025
 */

package com.travelapp.travel_app.annotations;

import com.travelapp.travel_app.validation.IncepeCuLiteraMareValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Anotarea personalizată
@Constraint(validatedBy = IncepeCuLiteraMareValidator.class)
// Se aplică pe câmpuri și metode
@Target({ ElementType.FIELD, ElementType.METHOD })
// Se reține în timpul rulării
@Retention(RetentionPolicy.RUNTIME)
// Interfața pentru validarea dacă o valoare începe cu literă mare
public @interface IncepeCuLiteraMare {
    // Mesajul de eroare
    String message() default "Valoarea trebuie să înceapă cu literă mare.";
    // Grupurile de validare
    Class<?>[] groups() default {};
    // Payload
    Class<? extends Payload>[] payload() default {};
}