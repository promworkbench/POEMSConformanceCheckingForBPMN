package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;

public interface Bpmn2ReachabilityGraphConverter {
    TransitionSystem convert(BPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException;
}
