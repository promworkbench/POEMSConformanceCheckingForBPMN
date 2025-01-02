package org.processmining.sccwbpmnnpos.algorithms.conformance_checking.poems;

import org.processmining.earthmoversstochasticconformancechecking.distancematrix.DistanceMatrix;
import org.processmining.earthmoversstochasticconformancechecking.distancematrix.DistanceMatrixTotalVsPartialCertainLevenshtein;
import org.processmining.earthmoversstochasticconformancechecking.distancematrix.TransposedDistanceMatrixWrapper;
import org.processmining.earthmoversstochasticconformancechecking.parameters.EMSCParametersBounds;
import org.processmining.earthmoversstochasticconformancechecking.parameters.partialorder.EMSCParametersLogTotalModelPartialCertainDefault;
import org.processmining.earthmoversstochasticconformancechecking.reallocationmatrix.ReallocationMatrix;
import org.processmining.earthmoversstochasticconformancechecking.reallocationmatrix.epsa.ComputeReallocationMatrix2;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.*;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.partialorder.PartialOrderUtils;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.BpmnPoemsPOEMSConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.partial.StochasticPOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.partial.converter.StochasticTracePOLanguageConverter;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.total.StochasticTOTraceLanguage;
import org.processmining.sccwbpmnnpos.models.stochastic.language.trace.total.converter.StochasticTraceTOLanguageConverter;
import org.processmining.sccwbpmnnpos.models.trace.partial_order.PartiallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.trace.total_order.TotallyOrderedTrace;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.activity.factory.ActivityFactory;

public class POEMSConformanceCheckingEMSC24Adapter implements POEMSConformanceChecking {
    private final ActivityFactory activityFactory;

    public POEMSConformanceCheckingEMSC24Adapter(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Override
    public <A extends Activity, PO extends PartiallyOrderedTrace<A>, TO extends TotallyOrderedTrace<A>> POEMSConformanceCheckingResult calculateConformance(final StochasticPOTraceLanguage<A, PO> poStochasticLanguage, final StochasticTOTraceLanguage<A, TO> stochasticLanguage) throws InterruptedException {
        StochasticTracePOLanguageConverter<A, PO> poLanguageConverter =
                StochasticTracePOLanguageConverter.getInstance();
        StochasticTraceTOLanguageConverter<A, TO> toLanguageConverter =
                StochasticTraceTOLanguageConverter.getInstance();
        Activity2IndexKey activity2IndexKey = new Activity2IndexKey();
        for (Activity activity : activityFactory.getAlphabet()) {
            activity2IndexKey.feed(activity.getLabel());
            activity2IndexKey.reIndex();
        }

        StochasticLanguage<PartialOrderCertain> modelLanguage = poLanguageConverter.toEMSC24(poStochasticLanguage,
                activity2IndexKey);
        StochasticLanguage<TotalOrder> logLanguage = toLanguageConverter.toEMSC24(stochasticLanguage,
                activity2IndexKey);

        EMSCParametersLogTotalModelPartialCertainDefault parameters = new EMSCParametersLogTotalModelPartialCertainDefault();
        Pair<Double, Double> result = compute(parameters,
                logLanguage, modelLanguage, () -> false);

        return new BpmnPoemsPOEMSConformanceCheckingResult(result.getA(), result.getB());
    }

    public static <A extends Order, B extends PartialOrder> Pair<Double, Double> compute(EMSCParametersBounds<A, B> parameters, StochasticLanguage<A> languageA, StochasticLanguage<B> languageB, ProMCanceller canceller) throws InterruptedException {
        if (languageA.size() > 0 && languageB.size() > 0) {
            double truncatedMass = 1.0 - StochasticLanguageUtils.getSumProbability(languageB);
            if (parameters.getDistanceMatrix() == null) {
                double higher = compute(parameters, languageA, languageB, parameters.getDistanceMatrixBest(), canceller);
                if (!canceller.isCancelled() && !Double.isNaN(higher)) {
                    double lower = higher - truncatedMass;
                    return Pair.of(lower, higher);
                } else {
                    return null;
                }
            } else {
                double higher = compute(parameters, languageA, languageB, parameters.getDistanceMatrixBest(), canceller);
                if (!canceller.isCancelled() && !Double.isNaN(higher)) {
                    double lower = compute(parameters, languageA, languageB, parameters.getDistanceMatrix(), canceller) - truncatedMass;
                    return !canceller.isCancelled() && !Double.isNaN(higher) ? Pair.of(lower, higher) : null;
                } else {
                    return null;
                }
            }
        } else {
            return languageA.size() == 0 && languageB.size() == 0 ? Pair.of((Double) null, 1.0) : Pair.of((Double)null, 0.0);
        }
    }

    public static <B extends PartialOrder, A extends Order> double compute(EMSCParametersBounds<A, B> parameters, StochasticLanguage<A> languageA, StochasticLanguage<B> languageB, DistanceMatrix<A, B> distanceMatrix, ProMCanceller canceller) throws InterruptedException {
        if (StochasticLanguageUtils.getSumProbability(languageA) < StochasticLanguageUtils.getSumProbability(languageB)) {
            distanceMatrix = distanceMatrix.clone();
            distanceMatrix.init(languageA, languageB, canceller);
            if (canceller.isCancelled()) {
                return Double.NaN;
            } else {
                Pair<ReallocationMatrix, Double> p = ComputeReallocationMatrix2.computeWithDistanceMatrixInitialised(languageA, languageB, distanceMatrix, parameters, canceller);
                return !canceller.isCancelled() && p != null ? (Double)p.getB() : Double.NaN;
            }
        } else {
            distanceMatrix = distanceMatrix.clone();
            DistanceMatrix<B, A> transposedDistanceMatrix = new TransposedDistanceMatrixWrapper<>(distanceMatrix);
            transposedDistanceMatrix.init(languageB, languageA, canceller);
            if (canceller.isCancelled()) {
                return Double.NaN;
            } else {
                Pair<ReallocationMatrix, Double> p = ComputeReallocationMatrix2.computeWithDistanceMatrixInitialised(languageB, languageA, transposedDistanceMatrix, parameters, canceller);
                return !canceller.isCancelled() && p != null ? (Double)p.getB() : Double.NaN;
            }
        }
    }
}
