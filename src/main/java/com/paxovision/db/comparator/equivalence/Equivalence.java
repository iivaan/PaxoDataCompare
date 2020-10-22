package com.paxovision.db.comparator.equivalence;


import org.skyscreamer.jsonassert.ValueMatcher;

public interface Equivalence<T> extends ValueMatcher<T> {
    boolean doEquivalent(T a, T b);

    @Override
    default boolean equal(T varl, T var2) {
        return doEquivalent(varl, var2);
    }

    /**
    *   need to call explicitly as some rule they want to compare one side as null as valid
    *   comparison
    *   @param a
    *   @param b
    *   @return
    */

    default boolean isEitherSideNull(T a, T b) {
        return a == null || b == null;
    }

    default boolean doDefaultEquivalent(T a, T b) {
        if (a == null && b == null) {
            return true;
        }
        return doEquivalent(a, b);
    }

}

