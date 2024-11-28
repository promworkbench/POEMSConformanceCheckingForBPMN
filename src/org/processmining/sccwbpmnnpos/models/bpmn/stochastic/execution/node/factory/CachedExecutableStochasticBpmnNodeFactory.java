package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.CachedExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;

public class CachedExecutableStochasticBpmnNodeFactory implements ExecutableStochasticBpmnNodeFactory {
    private final ExecutableBpmnNodeFactory inner;

    public CachedExecutableStochasticBpmnNodeFactory(ExecutableStochasticBpmnNodeFactory inner) {
        this.inner = new CachedExecutableBpmnNodeFactory(inner);
    }

    @Override
    public ExecutableBpmnNode create(BPMNNode node) {
        return inner.create(node);
    }
}
