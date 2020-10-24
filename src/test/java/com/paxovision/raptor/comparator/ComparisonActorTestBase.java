package com.paxovision.raptor.comparator;

import java.util.Map;
import java.util.function.Function;

public class ComparisonActorTestBase {
    protected Map getEmployeeRecord(Function<DataBuilder, DataBuilder> s) {
        DataBuilder builder = s.apply(new DataBuilder());
        return builder.build();
    }
}
