package org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.impl;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.impl.basic.MSetSimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.impl.basic.EventLogTraceImpl;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

import java.util.Objects;

public class BasicXLogSimplifier implements XLogSimplifier {
    private final XEventClassifier defaultClassifier;
    private final ActivityFactory activityFactory;

    public BasicXLogSimplifier(XEventClassifier defaultClassifier, ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
        this.defaultClassifier = new XEventNameClassifier();
    }

    @Override
    public SimplifiedEventLog simplify(final XLog log, final XEventClassifier classifier) {
        assert Objects.nonNull(log);
        assert Objects.nonNull(classifier);

        final MSetSimplifiedEventLog simpleEventLog = new MSetSimplifiedEventLog();

        for (final XTrace trace : log) {
            final EventLogTraceImpl simplifiedTrace = new EventLogTraceImpl(simpleEventLog);
            int i = 0;
            for (XEvent event : trace) {
                final String activityLabel = classifier.getClassIdentity(event);
                simplifiedTrace.add(activityFactory.create(activityLabel));
            }
            simpleEventLog.add(simplifiedTrace, 1);
        }
        return simpleEventLog;
    }

    @Override
    public SimplifiedEventLog simplify(XLog log) {
        return this.simplify(log, defaultClassifier);
    }
}
