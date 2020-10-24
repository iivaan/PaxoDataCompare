package com.paxovision.db.actor;

import com.paxovision.db.exception.RaptorException;
import com.paxovision.db.comparator.Comparator;
import com.paxovision.db.comparator.ComparisonConfig;
import com.paxovision.db.comparator.DataSet;
import com.paxovision.db.comparator.EquivalenceComparator;
import com.paxovision.db.comparator.model.MapDiffResult;
import java.util.Objects;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder
public class ComparisonActor <C extends Comparator> implements Actor{

    private ComparisonConfig compareConfig;
    private DataSet dataSet1;
    private DataSet dataSet2;
    private Class<C> comparatorClass;

    public ComparisonActor(
                            Class<C> comparatorClass,
                            DataSet dataSet1,
                            DataSet dataSet2,
                            ComparisonConfig compareConfig) {

        this.comparatorClass =
                comparatorClass == null ? (Class<C>) EquivalenceComparator.class : comparatorClass;
        this.dataSet1 = dataSet1;
        this.dataSet2 = dataSet2;
        this.compareConfig = compareConfig;
    }

    public MapDiffResult compare() {
        try {
            Objects.requireNonNull(dataSet1);
            Objects.requireNonNull(dataSet2);
            Comparator comparator =
                    comparatorClass
                            .getConstructor(ComparisonConfig.class, DataSet.class, DataSet.class)
                            .newInstance(compareConfig, dataSet1, dataSet2);
            return comparator.compare();
        } catch (Exception nse) {
            throw new RaptorException("Exception when compare", nse);
        }
    }

    @Override
    public String getName() {
        return "Compare Actor";
    }

}
