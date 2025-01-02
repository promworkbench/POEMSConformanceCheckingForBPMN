package org.processmining.sccwbpmnnpos.models.stochastic.language;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.sccwbpmnnpos.models.stochastic.Sample;
import org.processmining.sccwbpmnnpos.models.utils.AlphabetCollection;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.Collection;
import java.util.Set;

public interface StochasticLanguage<L, E extends AlphabetCollection<L>> extends Sample<StochasticLanguageEntry<L, E>> {
    Set<L> getAlphabet();

    Dot toGraphViz();
}
