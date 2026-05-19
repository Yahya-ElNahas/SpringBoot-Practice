package com.practice.main.common.localization.application;

import com.practice.main.common.localization.domain.Localization;
import com.practice.main.common.localization.infrastructure.LocalizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizationService {

    private final LocalizationRepository localizationRepository;

    @Cacheable(value = "localization", key = "#code + '_' + #language")
    public String getLocalizedMessage(String code, String language) {
        return localizationRepository.findByCodeAndLanguage(code, language)
                .or(() -> localizationRepository.findByCodeAndLanguage(code, "en"))
                .map(Localization::getMessage)
                .orElse(code);
    }
}