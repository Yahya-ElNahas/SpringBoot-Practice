package com.practice.main.common.localization.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Slf4j
@Component("messageSource")
@RequiredArgsConstructor
public class DatabaseMessageSource implements MessageSource {

    private final LocalizationService service;

    @Override
    public String getMessage(
            String code,
            Object [] args,
            Locale locale
    ) throws NoSuchMessageException {
        String language = locale != null ? locale.getLanguage() : "en";
        String message = service.getLocalizedMessage(code, language);

        return format(message != null ? message : code, args);
    }

    @Override
    public String getMessage(
            String code,
            Object [] args,
            String defaultMessage,
            Locale locale
    ) {
        try {
            return getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            return defaultMessage;
        }
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