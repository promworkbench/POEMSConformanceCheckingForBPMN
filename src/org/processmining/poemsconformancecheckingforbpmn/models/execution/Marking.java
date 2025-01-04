package org.processmining.poemsconformancecheckingforbpmn.models.execution;

import org.processmining.poemsconformancecheckingforbpmn.models.utils.multiset.Multiset;

public interface Marking<TOKEN> extends Multiset<TOKEN> {
    boolean isInitial();

    boolean isFinal();

    boolean equals(Object other);

    int hashCode();

    String toString();
}
