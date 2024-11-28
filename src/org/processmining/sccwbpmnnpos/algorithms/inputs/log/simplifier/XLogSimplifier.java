package org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.impl.BasicXLogSimplifier;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

public interface XLogSimplifier {
    SimplifiedEventLog simplify(XLog log, XEventClassifier classifier);

    SimplifiedEventLog simplify(XLog log);

    static XLogSimplifier getInstance(XEventClassifier defaultClassifier, ActivityFactory activityFactory) {
        return new BasicXLogSimplifier(defaultClassifier, activityFactory);
    }

    static XLogSimplifier getInstance(ActivityFactory activityFactory) {
        return new BasicXLogSimplifier(new XEventNameClassifier(), activityFactory);
    }
}
