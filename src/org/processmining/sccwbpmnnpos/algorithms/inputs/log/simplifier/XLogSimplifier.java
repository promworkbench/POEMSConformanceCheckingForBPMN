package org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;

public interface XLogSimplifier {
    public SimplifiedEventLog simplify(XLog log, XEventClassifier classifier);

    public SimplifiedEventLog simplify(XLog log);
}
