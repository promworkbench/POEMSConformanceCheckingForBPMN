package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.path.BpmnPOReachabilityGraphPathConstructor;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmnReachabilityEdge;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.ReachabilityGraphUtils;
import org.processmining.sccwbpmnnpos.algorithms.inputs.stochastic_language.stopping.StochasticLanguageGeneratorStopper;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.Sampler;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.sampling.strategy.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPathImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguageImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.RepetitiveEventBasedPartiallyOrderedSet;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.Collection;
import java.util.Objects;

public class StochasticBpmnPORG2StochasticPathLanguageConverterImpl implements StochasticBpmnPORG2StochasticPathLanguageConverter {
    private final StochasticGraphPathSamplingStrategy<IntermediatePathOption> samplingStrategy;
    private final StochasticLanguageGeneratorStopper stopper;
    private final BpmnPOReachabilityGraphPathConstructor pathConstructor;
    private final int maxPathLength;

    public StochasticBpmnPORG2StochasticPathLanguageConverterImpl(StochasticGraphPathSamplingStrategy<IntermediatePathOption> samplingStrategy, StochasticLanguageGeneratorStopper stopper, BpmnPOReachabilityGraphPathConstructor pathConstructor,
                                                                  int maxPathLength
    ) {
        this.samplingStrategy = samplingStrategy;
        this.stopper = stopper;
        this.pathConstructor = pathConstructor;
        this.maxPathLength = maxPathLength;
    }

    @Override
    public BpmnStochasticPOPathLanguage convert(ReachabilityGraph rg) {
        Sampler<IntermediatePathOption> sampler = samplingStrategy.getSampler();
        BpmnStochasticPOPathLanguageImpl stochasticLanguage = new BpmnStochasticPOPathLanguageImpl();
        State initialState = ReachabilityGraphUtils.getInitialState(rg, BpmnMarking.class);
        addNextSample(sampler, rg, getNewPath(), Probability.ONE, initialState);
        for (IntermediatePathOption pathOption : sampler) {
            BpmnPartiallyOrderedPath newPath = pathConstructor.construct(pathOption.getPath(), pathOption.getAdditionPath(), pathOption.getCurrentMarking());
            BpmnMarking marking = (BpmnMarking) pathOption.getNextState().getIdentifier();
            if (marking.isFinal()) {
                stochasticLanguage.add(newPath, pathOption.getProbability());
                if (stopper.shouldStop(stochasticLanguage)) {
                    break;
                }
            } else {
                addNextSample(sampler, rg, newPath, pathOption.getProbability(),
                        pathOption.getNextState());
            }
        }
        return stochasticLanguage;
    }

    private void addNextSample(Sampler<IntermediatePathOption> sampler, ReachabilityGraph rg,
                               BpmnPartiallyOrderedPath path, Probability pathProbability, State state) {
        long traceSize = path.stream().filter(n -> n instanceof Activity).count();
        if (traceSize > maxPathLength) {
            return;
        }
        Collection<Transition> transitions = rg.getOutEdges(state);
        for (Transition transition : transitions) {
            StochasticBpmnReachabilityEdge rgEdge = (StochasticBpmnReachabilityEdge) (transition.getIdentifier());
            IntermediatePathOption nextPath = new IntermediatePathOption(path, pathProbability, rgEdge.getPath(),
                    rgEdge.getProbability(), state, transition.getTarget());
            sampler.add(nextPath);
        }
    }

    private BpmnPartiallyOrderedPath getNewPath() {
        return new BpmnPartiallyOrderedPathImpl(new RepetitiveEventBasedPartiallyOrderedSet<>());
    }

    public static class IntermediateState implements StochasticObject {
        private final State state;
        private final Probability probability;

        private IntermediateState(State state, Probability probability) {
            this.state = state;
            this.probability = probability;
        }

        @Override
        public Probability getProbability() {
            return probability;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof IntermediateState)) return false;
            IntermediateState that = (IntermediateState) object;
            return Objects.equals(state, that.state);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(state);
        }
    }

    public static class IntermediatePathOption implements StochasticGraphPathSamplingStrategy.PathOption<IntermediateState, StochasticObject> {
        private final BpmnPartiallyOrderedPath path;
        private final Probability pathProbability;
        private final BpmnPartiallyOrderedPath additionPath;
        private final Probability additionPathProbability;
        private final State currentState;
        private final State nextState;

        private IntermediatePathOption(BpmnPartiallyOrderedPath path, Probability pathProbability,
                                       BpmnPartiallyOrderedPath additionPath, Probability pathOptionProbability,
                                       State currentState, State nextState) {
            this.path = path;
            this.pathProbability = pathProbability;
            this.additionPath = additionPath;
            this.additionPathProbability = pathOptionProbability;
            this.currentState = currentState;
            this.nextState = nextState;
        }

        public BpmnPartiallyOrderedPath getAdditionPath() {
            return additionPath;
        }

        public Probability getPathProbability() {
            return pathProbability;
        }

        public Probability getAdditionPathProbability() {
            return additionPathProbability;
        }

        public BpmnPartiallyOrderedPath getPath() {
            return path;
        }

        @Override
        public IntermediateState getState() {
            return new IntermediateState(getCurrentState(), getPathProbability());
        }

        @Override
        public StochasticObject getPathOption() {
            return new StochasticObject() {
                @Override
                public Probability getProbability() {
                    return getAdditionPathProbability();
                }
            };
        }

        public State getNextState() {
            return nextState;
        }

        public State getCurrentState() {
            return currentState;
        }

        public BpmnMarking getCurrentMarking() {
            return (BpmnMarking) getCurrentState().getIdentifier();
        }
    }
}
