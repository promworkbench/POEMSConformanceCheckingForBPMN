package org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.impl;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogTrace;
import org.processmining.sccwbpmnnpos.models.log.impl.basic.MSetSimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.impl.basic.SimplifiedEventLogTraceImpl;
import org.processmining.sccwbpmnnpos.models.utils.activity.ActivityImpl;
import org.processmining.sccwbpmnnpos.models.utils.activity.ActivityRegistry;

import java.util.Objects;

public class BasicXLogSimplifier implements XLogSimplifier {
    private final XEventClassifier defaultClassifier;
    private final ActivityRegistry activityRegistry;

    public BasicXLogSimplifier(XEventClassifier defaultClassifier, ActivityRegistry activityRegistry) {
        this.activityRegistry = activityRegistry;
        this.defaultClassifier = new XEventNameClassifier();
    }

    @Override
    public SimplifiedEventLog simplify(final XLog log, final XEventClassifier classifier) {
        assert Objects.nonNull(log);
        assert Objects.nonNull(classifier);

        final MSetSimplifiedEventLog simpleEventLog = new MSetSimplifiedEventLog();

        for (final XTrace trace : log) {
            final SimplifiedEventLogTraceImpl simplifiedTrace = new SimplifiedEventLogTraceImpl(simpleEventLog);
            int i = 0;
            for (XEvent event : trace) {
                final String activityLabel = classifier.getClassIdentity(event);
                simplifiedTrace.add(new ActivityImpl(activityLabel, activityRegistry));
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
