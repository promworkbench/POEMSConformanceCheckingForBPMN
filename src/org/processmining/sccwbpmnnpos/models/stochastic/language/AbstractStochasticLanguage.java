package org.processmining.sccwbpmnnpos.models.stochastic.language;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.sccwbpmnnpos.models.utils.AlphabetCollection;

public abstract class AbstractStochasticLanguage<L, E extends AlphabetCollection<L>> implements StochasticLanguage<L, E> {
    @Override
    public Dot toGraphViz() {
        Dot dot = new Dot();
        dot.setDirection(Dot.GraphDirection.bottomTop);
        for (StochasticLanguageEntry<L, E> leStochasticLanguageEntry : this) {
            Dot graphViz = leStochasticLanguageEntry.getElement().toGraphViz();
            for (DotNode node : graphViz.getNodes()) {
                dot.addNode(node);
            }
            for (DotEdge edge : graphViz.getEdges()) {
                dot.addEdge(edge);
            }
        }
        return dot;
    }
}
