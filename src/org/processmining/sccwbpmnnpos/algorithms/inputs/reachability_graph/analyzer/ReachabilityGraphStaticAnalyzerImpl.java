package org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.analyzer;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.ReachabilityGraphUtils;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysis;
import org.processmining.sccwbpmnnpos.algorithms.inputs.reachability_graph.stochastic.analyzer.StochasticReachabilityGraphStaticAnalysisDTO;
import org.processmining.sccwbpmnnpos.models.execution.Marking;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class ReachabilityGraphStaticAnalyzerImpl<M extends Marking<?>> implements ReachabilityGraphStaticAnalyzer<M> {
    private final Class<M> clazz;

    public ReachabilityGraphStaticAnalyzerImpl(Class<M> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ReachabilityGraphStaticAnalysis<M> analyze(ReachabilityGraph rg) {
        M initialState = ReachabilityGraphUtils.getInitialMarking(rg, clazz);
        Set<M> deadlockMarkings = ReachabilityGraphUtils.getDeadlockMarkings(rg, clazz);
        Set<M> markingsWithNoOptionToComplete = ReachabilityGraphUtils.getMarkingsWithNoOptionToComplete(rg, clazz);

        return new ReachabilityGraphStaticAnalysisDTO<>(rg, initialState, deadlockMarkings,
                markingsWithNoOptionToComplete, clazz);
    }
}
