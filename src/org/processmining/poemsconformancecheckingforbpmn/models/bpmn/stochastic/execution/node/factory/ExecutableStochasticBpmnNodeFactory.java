package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.execution.node.factory;

import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.execution.node.factory.SimpleExecutableBpmnNodeFactory;

public interface ExecutableStochasticBpmnNodeFactory extends ExecutableBpmnNodeFactory {
    static ExecutableStochasticBpmnNodeFactory getInstance() {
        BpmnTokenFactory token = BpmnTokenFactory.getInstance();
        BpmnMarkingFactory marking = BpmnMarkingFactory.getInstance();
        BpmnMarkingUtils utils = BpmnMarkingUtils.getInstance();
        SimpleExecutableBpmnNodeFactory normalFactory = new SimpleExecutableBpmnNodeFactory(token, marking, utils);
        return new CachedExecutableStochasticBpmnNodeFactory(new ExecutableStochasticBpmnNodeFactoryImpl(normalFactory, token, marking, utils));
    }
}
