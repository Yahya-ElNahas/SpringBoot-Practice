package com.practice.main.localization.application;

import com.practice.main.localization.domain.Localization;
import com.practice.main.localization.infrastructure.LocalizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizationCacheService {

    private final LocalizationRepository localizationRepository;

    @Cacheable(value = "localization", key = "#code + '_' + #language")
    public String getMessage(String code, String language) {
        return localizationRepository.findByCodeAndLanguage(code, language)
                .or(() -> localizationRepository.findByCodeAndLanguage(code, "en"))
                .map(Localization::getMessage)
                .orElse(code);
    }
}