package org.processmining.sccwbpmnnpos.models.bpmn.execution.node;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.BpmnFiringChange;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.exceptions.BpmnNodeNotEnabledException;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;

import java.util.Collection;

public interface ExecutableBpmnNode {
    BPMNDiagram getModel();

    BPMNNode getNode();

    boolean isChoice();

    boolean isEnabledIn(final BpmnMarking marking);

    int getTimesEnabledIn(final BpmnMarking marking);

    Collection<BpmnNodeFiringOption> getFiringOptions();

    BpmnNodeFiringOption getDefaultFiringOption();

    void setDefaultFiringOption(final BpmnNodeFiringOption firingOption);

    BpmnFiringChange fire(final BpmnMarking marking, final BpmnNodeFiringOption firingOption, final int fireTimes) throws BpmnNodeNotEnabledException;

    default BpmnFiringChange fire(final BpmnMarking marking, final int fireTimes) throws BpmnNodeNotEnabledException {
        return this.fire(marking, getDefaultFiringOption(), fireTimes);
    }

    default BpmnFiringChange fireOne(final BpmnMarking marking, final BpmnNodeFiringOption firingOption) throws BpmnNodeNotEnabledException {
        return this.fire(marking, firingOption, 1);
    }

    default BpmnFiringChange fireOne(final BpmnMarking marking) throws BpmnNodeNotEnabledException {
        return this.fire(marking, 1);
    }

    default BpmnFiringChange fireAll(final BpmnMarking marking, final BpmnNodeFiringOption firingOption) throws BpmnNodeNotEnabledException {
        int timesEnabled = getTimesEnabledIn(marking);
        return this.fire(marking, firingOption, timesEnabled);
    }

    default BpmnFiringChange fireAll(final BpmnMarking marking) throws BpmnNodeNotEnabledException {
        int timesEnabled = getTimesEnabledIn(marking);
        return this.fire(marking, timesEnabled);
    }

    String toString();
}
