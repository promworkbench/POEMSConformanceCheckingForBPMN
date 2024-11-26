package org.processmining.sccwbpmnnpos.models.stochastic.language;

import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.Collection;
import java.util.Set;

public interface StochasticLanguage<L, E extends Collection<L>> extends Iterable<StochasticLanguageEntry<L, E>>, StochasticObject {
    Set<L> getAlphabet();
}
