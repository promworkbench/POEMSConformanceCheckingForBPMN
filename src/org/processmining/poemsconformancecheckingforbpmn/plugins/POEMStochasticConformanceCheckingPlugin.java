package org.processmining.poemsconformancecheckingforbpmn.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.*;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.bpmn.BpmnPoemsConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.dialogs.SBPMNSamplingConfigurationDialog;
import org.processmining.poemsconformancecheckingforbpmn.models.StochasticBPMNConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.parameters.SamplingStoppingCriterionFromParametersProvider;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.util.function.Supplier;

@Plugin(
        name = "BPMN POEMS Conformance Checking",
        parameterLabels = {
                "Stochastic BPMN",
                "Event Log",
                "Model Probability Mass"
        },
        returnLabels = {"Model-Log Conformance measure"},
        returnTypes = {StochasticBPMNConformanceCheckingResult.class},
        help = "BPMN Partially Ordered Earth Movers' Stochastic (POEMS) Conformance Checking. It constructs " +
                "stochastic languages out of the event log (trace) nad the stochastic BPMN model (partially ordered " + "trace)" + " and then compares these languages using Earth Movers' Distance (EMD).",
        level = PluginLevel.Regular,
        categories = {PluginCategory.ConformanceChecking},
        handlesCancel = true,
        url = "https://github.com/promworkbench/POEMSConformanceCheckingForBPMN",
        keywords = {
                "Stochastic Conformance Checking",
                "BPMN Parallel Semantics"
        },
        quality = PluginQuality.Good,
        userAccessible = true
)
public class POEMStochasticConformanceCheckingPlugin {
    /**
     * The plug-in variant that runs in any context and uses the default parameters.
     *
     * @param context      The context to run in.
     * @param sBpmnDiagram The first input.
     * @param log          The second input.
     * @return The output.
     */
    @UITopiaVariant(
            affiliation = "RWTH Aachen",
            author = "Aleksandar Kuzmanoski",
            email = "aleksandar.kuzmanoski@rwth-aachen.de",
            website = "https://github.com/promworkbench/POEMSConformanceCheckingForBPMN"
    )
    @PluginVariant(
            variantLabel =
                    "Samples the Stochastic BPMN with random sampler until it reaches the maximal achievable " +
                            "probability" + "with precision of four decimal points. Meaning if the model can generate" +
                            " 87% of the language " + "(the rest is either in deadlock or livelock)" + "it will stop " +
                            "when it reached 86.9999% coverage.",
            requiredParameterLabels = {
                    0,
                    1
            }
    )
    public StochasticBPMNConformanceCheckingResult runDefault(
            UIPluginContext context,
            StochasticBPMNDiagram sBpmnDiagram,
            XLog log
    ) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException {
        SBPMNSamplingConfigurationDialog dialog = new SBPMNSamplingConfigurationDialog();
        InteractionResult interactionResult = context.showWizard(
                "POEMS Configuration",
                true,
                true,
                dialog
        );
        if (interactionResult != InteractionResult.FINISHED) {
            context.getFutureResult(0).cancel(false);
            return null;
        }
        Supplier<Boolean> canceller = () -> context.getProgress().isCancelled();
        SamplingStoppingCriterionFromParametersProvider samplingStoppingCriterion =
                new SamplingStoppingCriterionFromParametersProvider(
                        dialog.getStoppingCriteriaParameters(),
                        canceller
                );
        BpmnPoemsConformanceChecking conformanceChecker = BpmnPoemsConformanceChecking.getInstance(
                new XEventNameClassifier(),
                dialog.getSamplingStrategyParameters().getSamplingStrategyType(),
                samplingStoppingCriterion,
                canceller
        );
        POEMSConformanceCheckingResult result = conformanceChecker.calculateConformance(
                sBpmnDiagram,
                log
        );
        return includeHTMLTags -> String.format(
                "The conformance value lies between %s and %s",
                result.getConformanceLowerBound(),
                result.getConformanceUpperBound()
        );
    }
}
