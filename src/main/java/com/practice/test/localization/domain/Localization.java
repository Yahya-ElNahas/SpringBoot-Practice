package com.practice.test.localization.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@IdClass(LocalizationId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Localization {

    @Id
    private String code;

    @Id
    private String language;

    private String message;
}