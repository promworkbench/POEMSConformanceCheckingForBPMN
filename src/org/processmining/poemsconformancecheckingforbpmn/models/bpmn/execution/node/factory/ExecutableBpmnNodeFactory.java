package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.ExecutableBpmnNode;

public interface ExecutableBpmnNodeFactory {
    static ExecutableBpmnNodeFactory getInstance() {
        return new CachedExecutableBpmnNodeFactory(new SimpleExecutableBpmnNodeFactory(BpmnTokenFactory.getInstance(), BpmnMarkingFactory.getInstance(), BpmnMarkingUtils.getInstance()));
    }

    ExecutableBpmnNode create(final BPMNNode node);
}
