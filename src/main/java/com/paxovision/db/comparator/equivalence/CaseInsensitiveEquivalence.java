package com.paxovision.db.comparator.equivalence;

public class CaseInsensitiveEquivalence implements Equivalence<String>{
    public boolean doEquivalent(String a, String b) {
        if (!isEitherSideNull(a , b)) {
            return a.equalsIgnoreCase(b);
        }
        return false;
    }
}


