package ar.edu.itba.paw.persistence.utils;

import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

    public String sanitizeWildcards(final String input) {
        return input.replaceAll("(%|\\\\)", "\\\\$1");
    }
}
