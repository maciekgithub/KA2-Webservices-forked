package pl.euler.bgs.restapi.web.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

import static org.apache.commons.lang3.StringUtils.upperCase;

/**
 * Converter which recognizes case-insensitive the enum parameters on web methods.
 * @param <T> type of enum
 */
public class CaseInsensitiveEnumConverter<T extends Enum<T>> extends PropertyEditorSupport {
    private static final Logger log = LoggerFactory.getLogger(CaseInsensitiveEnumConverter.class);

    private final Class<T> typeParameterClass;

    public CaseInsensitiveEnumConverter(Class<T> typeParameterClass) {
        super();
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        String upperCased = StringUtils.trim(upperCase(text));
        T enumValue = T.valueOf(typeParameterClass, upperCased);
        log.debug("Recognized text enum property {} as enum value: {}", text, enumValue);
        setValue(enumValue);
    }

}

