package ar.edu.itba.paw.models;

import org.joda.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

@Converter
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime time) {
        return time == null ? null : new Timestamp(time.toDateTime().getMillis());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return new LocalDateTime(timestamp);
    }
}
