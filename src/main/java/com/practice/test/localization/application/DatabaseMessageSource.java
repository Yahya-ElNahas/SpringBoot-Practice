package com.practice.test.localization.application;

import com.practice.test.localization.domain.Localization;
import com.practice.test.localization.infrastructure.LocalizationRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component("messageSource")
@RequiredArgsConstructor
public class DatabaseMessageSource implements MessageSource {

    private final LocalizationRepository localizationRepo;

    @Override
    @NullMarked
    public String getMessage(
            String code,
            Object @Nullable [] args,
            @Nullable Locale locale
    ) throws NoSuchMessageException {
        String language = locale != null ? locale.getLanguage() : "en";
        String message = localizationRepo.findByCodeAndLanguage(code, language)
                .map(Localization::getMessage)
                .orElse(code);

        return format(message, args);
    }

    @Override
    public @Nullable String getMessage(
            @NonNull String code,
            Object @Nullable [] args,
            @Nullable String defaultMessage,
            @Nullable Locale locale
    ) {
        return getMessage(code, args, locale);
    }

    @Override
    @NullMarked
    public String getMessage(
            MessageSourceResolvable resolvable,
            @Nullable Locale locale
    ) throws NoSuchMessageException {
        String code = resolvable.getCodes()[0];
        return getMessage(code, resolvable.getArguments(), locale);
    }

    private String format(String message, Object[] args) {
        return MessageFormat.format(message, args != null ? args : new Object[]{});
    }
}