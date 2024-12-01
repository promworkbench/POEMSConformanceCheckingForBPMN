package org.processmining.sccwbpmnnpos.models.utils;

import org.processmining.plugins.graphviz.dot.Dot;

import java.util.Collection;
import java.util.Set;

public interface AlphabetCollection<E> extends Collection<E> {
    Set<E> getAlphabet();

    Dot toGraphViz();
}
