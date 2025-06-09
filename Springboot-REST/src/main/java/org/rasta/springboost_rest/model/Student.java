package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    private Integer studentId;

    @NotBlank(message = "Pole imię nie może być puste!")
    @Size(min = 2, max = 50, message = "Imię musi zawierać od {min} do {max} znaków!")
    private String imie;

    @NotBlank(message = "Pole nazwisko nie może być puste!")
    @Size(min = 2, max = 100, message = "Nazwisko musi zawierać od {min} do {max} znaków!")
    private String nazwisko;

    @NotBlank(message = "Pole numer indeksu nie może być puste!")
    @Pattern(regexp = "^[0-9]{6}$", message = "Numer indeksu powinien składać się z 6 cyfr!")
    private String nrIndeksu;

    @JsonIgnoreProperties("studenci")
    private Set<Projekt> projekty;
}