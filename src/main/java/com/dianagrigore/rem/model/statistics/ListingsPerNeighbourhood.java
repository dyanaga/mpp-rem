package com.dianagrigore.rem.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListingsPerNeighbourhood {
    private String neighbourhood;
    private long count;
}
