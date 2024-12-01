package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;

public interface Bpmn2POReachabilityGraphConverter {
    static Bpmn2POReachabilityGraphConverter getInstance() {
        return new Bpmn2POReachabilityGraphConverterImpl(ExecutableBpmnNodeFactory.getInstance(),
                BpmnMarkingUtils.getInstance(), CartesianProductCalculator.getInstance());
    }

    ReachabilityGraph convert(BPMNDiagram bpmnDiagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException;
}
