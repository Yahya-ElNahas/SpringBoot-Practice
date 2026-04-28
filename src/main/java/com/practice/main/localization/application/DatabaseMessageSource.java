package com.practice.main.localization.application;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component("messageSource")
@RequiredArgsConstructor
public class DatabaseMessageSource implements MessageSource {

    private final LocalizationCacheService service;

    @Override
    @Cacheable(value = "localization", key = "#code + '_' + (#locale != null ? #locale.getLanguage() : 'en')")
    public String getMessage(
            String code,
            Object [] args,
            Locale locale
    ) throws NoSuchMessageException {
        String language = locale != null ? locale.getLanguage() : "en";
        String message = service.getMessage(code, language);

        return format(message, args);
    }

    @Override
    public String getMessage(
            String code,
            Object [] args,
            String defaultMessage,
            Locale locale
    ) {
        return getMessage(code, args, locale);
    }

    @Override
    public String getMessage(
            MessageSourceResolvable resolvable,
            Locale locale
    ) throws NoSuchMessageException {
        String code = resolvable.getCodes()[0];
        return getMessage(code, resolvable.getArguments(), locale);
    }

    private String format(String message, Object[] args) {
        return MessageFormat.format(message, args != null ? args : new Object[]{});
    }
}