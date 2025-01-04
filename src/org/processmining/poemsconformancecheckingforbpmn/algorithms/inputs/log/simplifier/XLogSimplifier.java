package org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.simplifier;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.simplifier.impl.BasicXLogSimplifier;
import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLog;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;

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
