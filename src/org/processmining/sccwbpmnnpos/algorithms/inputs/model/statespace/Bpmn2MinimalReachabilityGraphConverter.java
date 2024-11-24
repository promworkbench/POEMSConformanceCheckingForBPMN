package org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;

public interface Bpmn2MinimalReachabilityGraphConverter {
    TransitionSystem convert(BPMNDiagram bpmnDiagram) throws BpmnUnboundedException;
}
