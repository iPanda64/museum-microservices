package com.museum.statistics.services;

import com.museum.statistics.domain.contracts.StatisticProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StatisticProviderFactory {

    private final Map<String, StatisticProvider> providerMap;

    public StatisticProviderFactory(List<StatisticProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(
                        p -> p.getPath().toLowerCase(),
                        Function.identity()
                ));
    }

    public List<String> getRegisteredPaths() {
        return providerMap.keySet().stream().toList();
    }

    public StatisticProvider getProvider(String path) {
        StatisticProvider provider = providerMap.get(path.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported statistic type: " + path);
        }
        return provider;
    }
}
