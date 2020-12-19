package com.hutchison.swandraft.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class ListConverter implements AttributeConverter<List<Object>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Object> snapshots) {
        try {
            return objectMapper.writeValueAsString(snapshots);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Error writing snapshots to JSON", e);
        }
    }

    @Override
    public List<Object> convertToEntityAttribute(String snapshotJson) {
        try {
            return objectMapper.readValue(snapshotJson, List.class);
        } catch (final IOException e) {
            throw new RuntimeException("Error writing snapshots to JSON", e);
        }
    }
}
