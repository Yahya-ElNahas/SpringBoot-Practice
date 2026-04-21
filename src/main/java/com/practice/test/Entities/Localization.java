package com.practice.test.Entities;

import com.practice.test.Entities.Composites.LocalizationId;
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
