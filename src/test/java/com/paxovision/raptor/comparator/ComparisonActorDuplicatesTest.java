package com.paxovision.raptor.comparator;

import com.paxovision.db.actor.ComparisonActor;
import com.paxovision.db.actor.ComparisonActorBuilder;
import com.paxovision.db.comparator.ComparisonConfig;
import com.paxovision.db.comparator.ComparisonConfig.ComparisonConfigBuilder;
import com.paxovision.db.comparator.DataSetBuilder;
import com.paxovision.db.comparator.EquivalenceComparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ComparisonActorDuplicatesTest extends ComparisonActorTestBase {

    @Test
    @DisplayName("Check if duplicates not throwing error , it will compare the first key that is stored")
    @SuppressWarnings("squid:S1192")
    void checkDuplicateNoError() {

        List<Map<String, String>> list1 = new ArrayList<>();
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-18")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));
        List<Map<String, String>> list2 = new ArrayList<>();

        list2.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-18")));
        list2.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));
        list2.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));

        ComparisonConfig comparisonConfig =
                new ComparisonConfigBuilder()
                        .withCompareKey("employeeId")
                        .withErrorOnDuplicate(false)
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
    @DisplayName("Exclude Duplicates in Comparison")
    @SuppressWarnings("squid :S1192")
    void checkExcludeDuplicate() {

        List<Map<String, String>> list1 = new ArrayList<>();
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-18")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));
        list1.add(getEmployeeRecord(t -> t.employeeId("124").age(20).startDate("2019-03-17")));

        List<Map<String, String>> list2 = new ArrayList<>();

        list2.add(getEmployeeRecord(t -> t.employeeId("124").age(20).startDate("2019-03-17")));

        ComparisonConfig comparisonConfig =
                new ComparisonConfigBuilder()
                        .withCompareKey("employeeId")
                        .withErrorOnDuplicate(false)
                        .withExcludeOnDuplicate(true)
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
    @DisplayName("Include Duplicates in Comparison. It will result in only in side 1 error")
    @SuppressWarnings("squid :S1192")
    void checkExcludeDuplicateError() {

        List<Map<String, String>> list1 = new ArrayList<>();
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-18")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));
        list1.add(getEmployeeRecord(t -> t.employeeId("124").age(20).startDate("2019-03-17")));

        List<Map<String, String>> list2 = new ArrayList<>();
        list2.add(getEmployeeRecord(t -> t.employeeId("124").age(20).startDate("2019-03-17")));

        ComparisonConfig comparisonConfig =
                new ComparisonConfigBuilder()
                        .withCompareKey("employeeId")
                        .withErrorOnDuplicate(false)
                        .withExcludeOnDuplicate(false)
                        .build();

        ComparisonActor actor =
                new ComparisonActorBuilder()
                        .withCompareConfig(comparisonConfig)
                        .withComparatorClass(EquivalenceComparator.class)
                        .withDataSet1(new DataSetBuilder().withData(list1).build())
                        .withDataSet2(new DataSetBuilder().withData(list2).build())
                        .build();

        AssertionError error = Assertions.assertThrows(AssertionError.class, actor::compare);
        //System.out.println(error.getMessage());
        org.assertj.core.api.Assertions.assertThat(error.getMessage())
                .contains("\"startdate\": {\"side1\": \"2019-03-18\"}");

    }

    @Test
    @DisplayName( "Check if duplicates throwing error , it will compare the first key that is stored. But there is duplicate")
    @SuppressWarnings("squid :S1192")
    void checkDuplicateError() {

        List<Map<String, String>> list1 = new ArrayList<>();
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-18")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-16")));

        List<Map<String, String>> list2 = new ArrayList<>();
        list2.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-18")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-17")));
        list1.add(getEmployeeRecord(t -> t.employeeId("123").age(20).startDate("2019-03-16")));

        ComparisonConfig comparisonConfig =
                new ComparisonConfigBuilder()
                        .withCompareKey("employeeId")
                        .withErrorOnDuplicate(true)
                        .build();

        ComparisonActor actor =
                new ComparisonActorBuilder()
                        .withCompareConfig(comparisonConfig)
                        .withComparatorClass(EquivalenceComparator.class)
                        .withDataSet1(new DataSetBuilder().withData(list1).build())
                        .withDataSet2(new DataSetBuilder().withData(list2).build())
                        .build();

        Assertions.assertThrows(AssertionError.class, actor::compare);
    }

}
