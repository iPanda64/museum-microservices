package com.museum.export.services;

import com.museum.export.domain.aggregate.Artwork;
import com.museum.export.domain.contracts.ExportStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final ExportFactory factory;

    public List<String> getAvailableFormats() {
        return factory.getRegisteredFormats();
    }

    public String generateExport(String format, List<Artwork> artworks) {
        ExportStrategy strategy = factory.getStrategy(format);
        return strategy.generate(artworks);
    }
}
