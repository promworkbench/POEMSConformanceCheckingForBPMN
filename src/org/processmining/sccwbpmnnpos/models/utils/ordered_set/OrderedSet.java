package org.processmining.sccwbpmnnpos.models.utils.ordered_set;

import java.util.Collection;

public interface OrderedSet<ELEMENT> extends Collection<ELEMENT> {
    Collection<ELEMENT> getAlphabet();

    Collection<ELEMENT> getAscendants(ELEMENT element);

    Collection<ELEMENT> getPredecessors(ELEMENT element);

    Collection<ELEMENT> getEnabled(Collection<ELEMENT> executed);

    boolean isEnabled(ELEMENT element, Collection<ELEMENT> executed);

    int size();

    String toString();
}
