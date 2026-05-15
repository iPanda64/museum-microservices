package com.museum.export.services;

import com.museum.export.domain.contracts.ExportStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExportFactory {

    private final Map<String, ExportStrategy> strategyMap;

    public ExportFactory(List<ExportStrategy> strategies) {
        this.strategyMap =  strategies.stream()
                .collect(Collectors.toMap(
                        g -> g.getFormat().toUpperCase(),
                        Function.identity()
                ));
    }

    public List<String> getRegisteredFormats() {
        return strategyMap.keySet().stream().toList();
    }

    public ExportStrategy getStrategy(String format) {
        ExportStrategy strategy = strategyMap.get(format.toUpperCase());

        if (strategy  == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }

        return strategy;
    }
}
