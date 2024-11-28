package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.execution;

import org.processmining.sccwbpmnnpos.algorithms.utils.converters.Converter;
import org.processmining.sccwbpmnnpos.models.bpmn.activity.BpmnNode2ActivityFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.trace.BpmnPartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

public interface BpmnPOPath2TraceConverter extends Converter<BpmnPartiallyOrderedPath, BpmnPartiallyOrderedTrace> {
    static BpmnPOPath2TraceConverter getInstance(ActivityFactory activityFactory) {
        return new BpmnPOPath2TraceConverterImpl(BpmnNode2ActivityFactory.getInstance(activityFactory));
    }

    BpmnPartiallyOrderedTrace convert(BpmnPartiallyOrderedPath path);
}
