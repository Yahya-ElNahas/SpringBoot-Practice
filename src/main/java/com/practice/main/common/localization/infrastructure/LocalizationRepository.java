package com.practice.main.common.localization.infrastructure;

import com.practice.main.common.localization.domain.Localization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocalizationRepository extends JpaRepository<Localization, UUID> {

    Optional<Localization> findByCodeAndLanguage(String code, String language);
}