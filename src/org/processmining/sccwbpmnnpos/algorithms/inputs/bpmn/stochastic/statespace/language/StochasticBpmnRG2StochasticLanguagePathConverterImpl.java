package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmnReachabilityEdge;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachabilitygraph.ReachabilityGraphUtils;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.Sampler;
import org.processmining.sccwbpmnnpos.algorithms.utils.stochastics.graph.StochasticGraphPathSamplingStrategy;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.BpmnMarking;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.BpmnToken;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPathImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.BpmnStochasticPathLanguage;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.BpmnStochasticPathLanguageImpl;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.RepetitiveEventBasedPartiallyOrderedSet;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.Collection;
import java.util.Objects;

public class StochasticBpmnRG2StochasticLanguagePathConverterImpl implements StochasticBpmnRG2StochasticLanguageConverter {
    private final StochasticGraphPathSamplingStrategy<IntermediatePathOption> samplingStrategy;

    public StochasticBpmnRG2StochasticLanguagePathConverterImpl(StochasticGraphPathSamplingStrategy<IntermediatePathOption> samplingStrategy) {
        this.samplingStrategy = samplingStrategy;
    }

    @Override
    public BpmnStochasticPathLanguage convert(ReachabilityGraph rg) {
        Sampler<IntermediatePathOption> sampler = samplingStrategy.getSampler();
        BpmnStochasticPathLanguageImpl stochasticLanguage = new BpmnStochasticPathLanguageImpl();
        State initialState = ReachabilityGraphUtils.getInitialState(rg, BpmnMarking.class);
        addNextSample(sampler, rg, getNewPath(), Probability.ONE, initialState);
        for (IntermediatePathOption intermediatePathOption : sampler) {
            BpmnPartiallyOrderedPath newPath = constructNewPath(intermediatePathOption);
            BpmnMarking marking = (BpmnMarking) intermediatePathOption.getNextState().getIdentifier();
            if (marking.isFinal()) {
                stochasticLanguage.add(newPath, intermediatePathOption.getProbability());
                if (stochasticLanguage.size() >= 20) {
                    break;
                }
            } else {
                addNextSample(sampler, rg, newPath, intermediatePathOption.getProbability(),
                        intermediatePathOption.getNextState());
            }

        }
        return stochasticLanguage;
    }

    private BpmnPartiallyOrderedPath constructNewPath(IntermediatePathOption intermediatePathOption) {
        BpmnPartiallyOrderedPath newPath = getNewPath();
        BpmnPartiallyOrderedPath basePath = intermediatePathOption.getPath();
        BpmnPartiallyOrderedPath additionPath = intermediatePathOption.getAdditionPath();

        try {
            newPath.concatenate(basePath);
            newPath.concatenate(additionPath);

            BpmnMarking marking = (BpmnMarking) (intermediatePathOption.getCurrentState().getIdentifier());

            if (marking.isInitial()) {
                return newPath;
            }

            TObjectIntMap<BPMNNode> totalSourceCount = new TObjectIntHashMap<>();
            for (BpmnToken token : marking) {
                totalSourceCount.adjustOrPutValue(token.getSourceNode(), 1, 1);
            }

            TObjectIntMap<BPMNNode> sourceCount = new TObjectIntHashMap<>();
            TObjectIntMap<BPMNNode> targetCount = new TObjectIntHashMap<>();

            for (BpmnToken token : marking) {
                BPMNNode source = token.getSourceNode();
                BPMNNode target = token.getSinkNode();
                int sourceIndex = sourceCount.adjustOrPutValue(source, 1, 1);
                int targetIndex = targetCount.adjustOrPutValue(target, 1, 1);
                newPath.connect(source, basePath.getTimesFired(source) - totalSourceCount.get(source) + sourceIndex,
                        target, targetIndex + basePath.getTimesFired(target));
            }
            return newPath;
        } catch (PartialOrderLoopNotAllowedException e) {
            throw new RuntimeException("This should not happen because we are merging repetitive partial orders", e);
        }
    }

    private void addNextSample(Sampler<IntermediatePathOption> sampler, ReachabilityGraph rg,
                               BpmnPartiallyOrderedPath path, Probability pathProbability, State state) {
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
    }
}
