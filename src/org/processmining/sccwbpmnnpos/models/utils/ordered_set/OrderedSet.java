package org.processmining.sccwbpmnnpos.models.utils.ordered_set;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.sccwbpmnnpos.models.utils.AlphabetCollection;

import java.util.Collection;

public interface OrderedSet<ELEMENT> extends AlphabetCollection<ELEMENT> {

    Collection<ELEMENT> getAscendants(ELEMENT element);

    Collection<ELEMENT> getPredecessors(ELEMENT element);

    Collection<ELEMENT> getEnabled(Collection<ELEMENT> executed);

    boolean isEnabled(ELEMENT element, Collection<ELEMENT> executed);

    int size();

    String toString();

    int getNumberOfConnections();
}
