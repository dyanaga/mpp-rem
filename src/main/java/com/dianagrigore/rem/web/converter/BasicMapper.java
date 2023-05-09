package com.dianagrigore.rem.web.converter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BasicMapper<SOURCE, TARGET> {

    public abstract TARGET convertSource(SOURCE source, String expand);

    public abstract SOURCE convertTarget(TARGET target);

    public TARGET convertSource(SOURCE source) {
        return convertSource(source, null);
    }

    public List<TARGET> convertSource(List<SOURCE> sourceList) {
        return convertSource(sourceList, null);
    }

    public List<TARGET> convertSource(List<SOURCE> sourceList, String expand) {
        if (Objects.isNull(sourceList)) {
            return List.of();
        }
        return sourceList.stream()
                .map(source -> convertSource(source, expand))
                .collect(Collectors.toList());
    }

    public List<SOURCE> convertTarget(List<TARGET> targetList) {
        if (Objects.isNull(targetList)) {
            return List.of();
        }
        return targetList.stream()
                .map(this::convertTarget)
                .collect(Collectors.toList());
    }
}
