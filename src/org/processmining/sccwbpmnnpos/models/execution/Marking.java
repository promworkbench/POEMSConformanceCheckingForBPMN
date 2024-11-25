package org.processmining.sccwbpmnnpos.models.execution;

import org.processmining.sccwbpmnnpos.models.utils.multiset.Multiset;

public interface Marking<TOKEN> extends Multiset<TOKEN> {
    boolean isInitial();

    boolean isFinal();

    boolean equals(Object other);

    int hashCode();

    String toString();
}
