package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.poemsconformancecheckingforbpmn.models.stochastic.Sample;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.AlphabetCollection;

import java.util.Set;

public interface StochasticLanguage<L, E extends AlphabetCollection<L>> extends Sample<StochasticLanguageEntry<L, E>> {
    Set<L> getAlphabet();

    Dot toGraphViz();
}
