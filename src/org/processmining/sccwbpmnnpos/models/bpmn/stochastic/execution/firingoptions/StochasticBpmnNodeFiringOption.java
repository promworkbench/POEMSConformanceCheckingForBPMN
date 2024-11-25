package org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.firingoptions;

import org.processmining.sccwbpmnnpos.models.bpmn.execution.firable.alternatives.BpmnNodeFiringOption;

public interface StochasticBpmnNodeFiringOption extends BpmnNodeFiringOption {
    double getProbability();
}
