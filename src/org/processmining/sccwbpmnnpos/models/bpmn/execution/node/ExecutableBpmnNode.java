package org.processmining.sccwbpmnnpos.models.bpmn.execution.node;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChange;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.exceptions.BpmnNodeNotEnabledException;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

import java.util.Collection;
import java.util.List;

public interface ExecutableBpmnNode {
    BPMNDiagram getModel();

    BPMNNode getNode();

    boolean isChoice();

    boolean isEnabledIn(final BpmnMarking marking);

    int getTimesEnabledIn(final BpmnMarking marking);

    Collection<BpmnMarking> getProduceOptions();

    Collection<BpmnMarking> getConsumeOptions();

    List<List<BpmnNodeFiringOption>> getFiringOptions(BpmnMarking marking);

    List<BpmnNodeFiringOption> getDefaultFiringOption(BpmnMarking marking);

    void setDefaultProduceOption(final BpmnMarking firingOption);

    BpmnFiringChange fire(final BpmnMarking marking, final BpmnNodeFiringOption firingOption, final int fireTimes) throws BpmnNodeNotEnabledException;

    default BpmnFiringChange fireOne(final BpmnMarking marking, final BpmnNodeFiringOption firingOption) throws BpmnNodeNotEnabledException {
        return this.fire(marking, firingOption, 1);
    }

    default BpmnFiringChange fireAll(final BpmnMarking marking, final BpmnNodeFiringOption firingOption) throws BpmnNodeNotEnabledException {
        int timesEnabled = getTimesEnabledIn(marking);
        return this.fire(marking, firingOption, timesEnabled);
    }

    String toString();

    int getProducesTokensCount();

    int getConsumesTokensCount();
}
