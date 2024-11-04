package com.lsadf.lsadf_backend.configurations.http_clients;

import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

import java.util.Optional;

public class OptionalFeignFormatterRegistrar implements FormatterRegistrar {

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addConverter(Optional.class, String.class, optional -> optional.isPresent() ? optional.get().toString() : null);
    }
}
