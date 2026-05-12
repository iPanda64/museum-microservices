package com.museum.export.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.contracts.ExportGenerator;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JsonExportGenerator implements ExportGenerator {

    private final ObjectMapper objectMapper;

    public JsonExportGenerator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Register modules if needed, but for simple POJOs/Records it works out of the box
    }

    @Override
    public String getFormat() { return "JSON"; }

    @Override
    public String generate(List<Artwork> artworks) {
        try {
            return objectMapper.writeValueAsString(artworks);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to generate JSON export", e);
        }
    }
}
