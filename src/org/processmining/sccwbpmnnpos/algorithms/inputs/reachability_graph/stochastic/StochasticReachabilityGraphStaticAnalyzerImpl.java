package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.ReachabilityGraphUtils;
import org.processmining.sccwbpmnnpos.models.execution.Marking;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.util.*;

public class StochasticReachabilityGraphStaticAnalyzerImpl<M extends Marking<?>> implements StochasticReachabilityGraphStaticAnalyzer<M> {
    private final Class<M> clazz;

    public StochasticReachabilityGraphStaticAnalyzerImpl(Class<M> clazz) {
        this.clazz = clazz;
    }

    @Override
    public StochasticReachabilityGraphStaticAnalysis<M> analyze(ReachabilityGraph rg) {
        M initialState = ReachabilityGraphUtils.getInitialMarking(rg, clazz);
        Set<M> deadlockMarkings = ReachabilityGraphUtils.getDeadlockMarkings(rg, clazz);
        Set<M> markingsWithNoOptionToComplete = ReachabilityGraphUtils.getMarkingsWithNoOptionToComplete(rg, clazz);
        Map<M, Probability> reachabilityProbabilities = getReachabilityProbabilities(rg);

        return new StochasticReachabilityGraphStaticAnalysisDTO<>(rg, initialState, deadlockMarkings,
                markingsWithNoOptionToComplete, reachabilityProbabilities);
    }

    public Map<M, Probability> getReachabilityProbabilities(ReachabilityGraph rg) {
        Map<M, Probability> probabilityMap = new HashMap<>();
        Set<M> finalMarkings = ReachabilityGraphUtils.getFinalMarkings(rg, clazz);
        for (M finalMarking : finalMarkings) {
            probabilityMap.put(finalMarking, Probability.ONE);
        }
        ArrayList<M> ascendants = new ArrayList<>(ReachabilityGraphUtils.getAscendants(rg, finalMarkings, clazz));
        TObjectIntMap<M> ascendantsIndex = new TObjectIntHashMap<>();
        int i = 1;
        for (M ascendant : ascendants) {
            ascendantsIndex.put(ascendant, i++);
        }
        int matrixSize = ascendants.size();
        SparseDoubleMatrix2D connectivityMatrix = new SparseDoubleMatrix2D(matrixSize, matrixSize);
        SparseDoubleMatrix1D b = new SparseDoubleMatrix1D(matrixSize);
        for (M item : ascendants) {
            int itemIndex = ascendantsIndex.get(item);
            connectivityMatrix.setQuick(itemIndex - 1, itemIndex - 1, 1);
            Collection<Transition> transitions = rg.getOutEdges(rg.getNode(item));
            for (Transition transition : transitions) {
                M target = clazz.cast(transition.getTarget().getIdentifier());
                StochasticObject so = (StochasticObject) (transition.getIdentifier());
                Probability probability = probabilityMap.get(target);
                if (Objects.nonNull(probability)) {
                    b.setQuick(itemIndex - 1, b.getQuick(itemIndex - 1) + (so.getProbability().multiply(probability)).doubleValue());
                    continue;
                }
                int targetIndex = ascendantsIndex.get(target);
                if (ascendantsIndex.getNoEntryValue() == targetIndex) {
                    continue;
                }
                double previousValue = connectivityMatrix.getQuick(itemIndex - 1, targetIndex - 1);
                connectivityMatrix.setQuick(itemIndex - 1, targetIndex - 1, previousValue - so.getProbability().doubleValue());
            }
        }
        Algebra algebra = new Algebra();
        DoubleMatrix1D result = algebra.mult(algebra.inverse(connectivityMatrix), b);
        for (i = 0; i < matrixSize; i++) {
            M item = ascendants.get(i);
            Probability probability = Probability.of(result.get(i));
            probabilityMap.put(item, probability);
        }
        return probabilityMap;
    }
}
