package com.paxovision.raptor.comparator;

import static org.assertj.core.api.Assertions.assertThat;
import com.google.common.collect.ImmutableMap;
import com.paxovision.db.actor.ComparisonActor;
import com.paxovision.db.actor.ComparisonActorBuilder;
import com.paxovision.db.comparator.ComparisonConfig;
import com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder;
import com.paxovision.db.comparator.DataSetBuilder;
import com.paxovision.db.comparator.EquivalenceComparator;
import com.paxovision.db.comparator.diffreport.DiffReport.OutputFormat;
import com.paxovision.db.comparator.equivalence.DateEquivalence;
import com.paxovision.db.comparator.equivalence.Equivalence;
import com.paxovision.db.comparator.equivalence.NumberEquivalence;
import com.paxovision.db.comparator.model.MapDiffResult;
import com.paxovision.db.comparator.model.StringListKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import  java.util.List;
import java.util.Map;
import  org.junit.jupiter.api.Assertions;
import  org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/** This is basic comparator using Equivalence */
@SuppressWarnings("squid:S1192")
public class ComparisonActorEquivalenceTest extends ComparisonActorTestBase {

    @Test
    @SuppressWarnings("squid :S1192")
    @DisplayName("Compare Columns Test")
    void checkCompareWithOnlyCompareColumns() {
        List<Map<String, Object>> list1 = new ArrayList<>();
        list1.add(getEmployeeRecord(s -> s.employeeId("1").startDate("2019-03-18").age(20)));
        List<Map<String, Object>> list2 = new ArrayList<>();
        list2.add(getEmployeeRecord(s -> s.employeeId("1").startDate("2019-03-15").age(20)));

        ComparisonConfig comparisonConfig =
                new ComparisonConfigBuilder()
                        .withCompareKey("employeeId")
                        .withCompareColumns("age")
                        .build();

        ComparisonActor actor =
                new ComparisonActorBuilder()
                        .withCompareConfig(comparisonConfig)
                        .withComparatorClass(EquivalenceComparator.class)
                        .withDataSet1(new DataSetBuilder().withData(list1).build())
                        .withDataSet2(new DataSetBuilder().withData(list2).build())
                        .build();


        actor.compare();
    }

    @Test
    @SuppressWarnings("squid :S1192")
    @DisplayName("Diff Data Type Test")
    void checkCompareWithDiffDatType() {
        List<Map<String, Object>> list1 = new ArrayList<>();
        list1.add(getEmployeeRecord(s -> s.employeeId("1").startDate("2019-03-18").age(25)));
        List<Map<String, Object>> list2 = new ArrayList<>();
        list2.add(getEmployeeRecord(s -> s.employeeId("1").startDate("2019-03-18").age(25)));

        ComparisonConfig comparisonConfig =
                new ComparisonConfigBuilder()
                        .withCompareKey("employeeId")
                        .build();

        ComparisonActor actor =
                new ComparisonActorBuilder()
                        .withCompareConfig(comparisonConfig)
                        .withComparatorClass(EquivalenceComparator.class)
                        .withDataSet1(new DataSetBuilder().withData(list1).build())
                        .withDataSet2(new DataSetBuilder().withData(list2).build())
                        .build();


        actor.compare();
    }

}
