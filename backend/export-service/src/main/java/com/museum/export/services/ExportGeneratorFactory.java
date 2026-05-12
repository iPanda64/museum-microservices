package com.museum.export.services;

import com.museum.export.domain.contracts.ExportGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExportGeneratorFactory {

    private final Map<String, ExportGenerator> generatorMap;

    public ExportGeneratorFactory(List<ExportGenerator> generators) {
        this.generatorMap = generators.stream()
                .collect(Collectors.toMap(
                        g -> g.getFormat().toUpperCase(),
                        Function.identity()
                ));
    }

    public List<String> getRegisteredFormats() {
        return generatorMap.keySet().stream().toList();
    }

    public ExportGenerator getGenerator(String format) {
        ExportGenerator generator = generatorMap.get(format.toUpperCase());

        if (generator == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }

        return generator;
    }
}
