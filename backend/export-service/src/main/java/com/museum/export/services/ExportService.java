package com.museum.export.services;

import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.contracts.ExportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final ExportGeneratorFactory generatorFactory;

    public List<String> getAvailableFormats() {
        return generatorFactory.getRegisteredFormats();
    }

    public String generateExport(String format, List<Artwork> artworks) {
        ExportGenerator generator = generatorFactory.getGenerator(format);
        return generator.generate(artworks);
    }
}
