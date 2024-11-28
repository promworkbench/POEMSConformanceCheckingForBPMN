package org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.language.path;

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
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.ExecutableBpmnNode;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPath;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.path.BpmnPartiallyOrderedPathImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPOPathLanguageImpl;
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.language.path.BpmnStochasticPOPathLanguage;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.exceptions.PartialOrderLoopNotAllowedException;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.RepetitiveEventBasedPartiallyOrderedSet;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.Collection;
import java.util.Objects;

public class StochasticBpmnPORG2StochasticPathLanguageConverterImpl implements StochasticBpmnPORG2StochasticPathLanguageConverter {
    private final StochasticGraphPathSamplingStrategy<IntermediatePathOption> samplingStrategy;
    private final ExecutableStochasticBpmnNodeFactory executableNodeFactory;

    public StochasticBpmnPORG2StochasticPathLanguageConverterImpl(StochasticGraphPathSamplingStrategy<IntermediatePathOption> samplingStrategy, ExecutableStochasticBpmnNodeFactory executableNodeFactory) {
        this.samplingStrategy = samplingStrategy;
        this.executableNodeFactory = executableNodeFactory;
    }

    @Override
    public BpmnStochasticPOPathLanguage convert(ReachabilityGraph rg) {
        Sampler<IntermediatePathOption> sampler = samplingStrategy.getSampler();
        BpmnStochasticPOPOPathLanguageImpl stochasticLanguage = new BpmnStochasticPOPOPathLanguageImpl();
        State initialState = ReachabilityGraphUtils.getInitialState(rg, BpmnMarking.class);
        addNextSample(sampler, rg, getNewPath(), Probability.ONE, initialState);
        for (IntermediatePathOption intermediatePathOption : sampler) {
            BpmnPartiallyOrderedPath newPath = constructNewPath(intermediatePathOption);
            BpmnMarking marking = (BpmnMarking) intermediatePathOption.getNextState().getIdentifier();
            if (marking.isFinal()) {
                stochasticLanguage.add(newPath, intermediatePathOption.getProbability());
                if (stochasticLanguage.size() >= 1000) {
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
        } catch (PartialOrderLoopNotAllowedException e) {
            throw new RuntimeException("This should not happen because we are merging repetitive partial orders", e);
        }

        BpmnMarking marking = (BpmnMarking) (intermediatePathOption.getCurrentState().getIdentifier());

        if (marking.isInitial()) {
            return newPath;
        }

        TObjectIntMap<BPMNNode> tokensProduced = new TObjectIntHashMap<>();
        for (BpmnToken token : marking) {
            tokensProduced.adjustOrPutValue(token.getSourceNode(), 1, 1);
        }

        TObjectIntMap<BPMNNode> tokensConsumed = new TObjectIntHashMap<>();
        for (BpmnToken token : marking) {
            tokensConsumed.adjustOrPutValue(token.getSinkNode(), 1, 1);
        }

        TObjectIntMap<BPMNNode> sourceCount = new TObjectIntHashMap<>();
        TObjectIntMap<BPMNNode> targetCount = new TObjectIntHashMap<>();

        for (BpmnToken token : marking) {
            BPMNNode source = token.getSourceNode();

            ExecutableBpmnNode executableSource = executableNodeFactory.create(source);
            int localSourceIndex = sourceCount.adjustOrPutValue(source, 1, 1);
            int sourceTimesFired = (int) Math.ceil(1.0 * tokensProduced.get(source) / executableSource.getProducesTokensCount());
            int sourceFiringIndex = (int) Math.ceil(1.0 * localSourceIndex / executableSource.getProducesTokensCount());
            int sourceIndex = basePath.getTimesFired(source) - sourceTimesFired + sourceFiringIndex;

            BPMNNode target = token.getSinkNode();
            ExecutableBpmnNode executableTarget = executableNodeFactory.create(target);
            int localTargetIndex = targetCount.adjustOrPutValue(target, 1, 1);
            int firingTargetIndex = (int) Math.ceil(1.0 * localTargetIndex / executableTarget.getConsumesTokensCount());
            int targetIndex = basePath.getTimesFired(target) + firingTargetIndex;
            try {
                newPath.connect(source, sourceIndex,
                        target, targetIndex);
            } catch (PartialOrderLoopNotAllowedException e) {
                throw new RuntimeException("This should not happen because we are merging repetitive partial orders",
                        e);
            }
        }
        return newPath;

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
