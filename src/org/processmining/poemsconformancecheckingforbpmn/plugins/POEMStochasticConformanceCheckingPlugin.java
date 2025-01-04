package org.processmining.poemsconformancecheckingforbpmn.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.*;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.YourAlgorithm;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceChecking;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.conformance_checking.poems.POEMSConformanceCheckingEMSC24Adapter;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.log.simplifier.impl.BasicXLogSimplifier;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.stochastic_language.StochasticLanguageGenerator;
import org.processmining.poemsconformancecheckingforbpmn.dialogs.SBPMNSamplingConfigurationDialog;
import org.processmining.poemsconformancecheckingforbpmn.models.POEMSCCResults;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result.POEMSConformanceCheckingResult;
import org.processmining.poemsconformancecheckingforbpmn.models.bpmn.stochastic.language.trace.BpmnStochasticPOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLog;
import org.processmining.poemsconformancecheckingforbpmn.models.log.SimplifiedEventLogVariant;
import org.processmining.poemsconformancecheckingforbpmn.models.log.stochastic.language.EventLogStochasticTOTraceLanguage;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.ActivityFactory;
import org.processmining.poemsconformancecheckingforbpmn.models.utils.activity.factory.CachedActivityFactory;
import org.processmining.poemsconformancecheckingforbpmn.parameters.YourParameters;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

@Plugin(
        name = "BPMN POEMS Conformance Checking",
        parameterLabels = {
                "Stochastic BPMN",
                "Event Log",
                "Model Probability Mass"
        },
        returnLabels = {"Model-Log Conformance measure"},
        returnTypes = {POEMSConformanceCheckingResult.class},
        help = "BPMN Partially Ordered Earth Movers' Stochastic (POEMS) Conformance Checking. It constructs " +
                "stochastic languages out of the event log (trace) nad the stochastic BPMN model (partially ordered " +
                "trace)" +
                " and then compares these languages using Earth Movers' Distance (EMD).",
        level = PluginLevel.NightlyBuild,
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
public class POEMStochasticConformanceCheckingPlugin extends YourAlgorithm {
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
            email = "aleksandar.kuzmanoski@rwth-aachen.de"
    )
    @PluginVariant(
            variantLabel = "Samples the Stochastic BPMN with random sampler until it reaches the maximal achievable " +
                    "probability" +
                    "with precision of four decimal points. Meaning if the model can generate 87% of the language " +
                    "(the rest is either in deadlock or livelock)" +
                    "it will stop when it reached 86.9999% coverage.",
            requiredParameterLabels = {
                    0,
                    1
            }
    )
    public POEMSConformanceCheckingResult runDefault(
            UIPluginContext context,
            StochasticBPMNDiagram sBpmnDiagram,
            XLog log
    ) throws BpmnNoOptionToCompleteException, BpmnUnboundedException, InterruptedException {
        SBPMNSamplingConfigurationDialog dialog = new SBPMNSamplingConfigurationDialog();
        InteractionResult result = context.showWizard(
                "POEMS Configuration",
                true,
                true,
                dialog
        );
        if (result != InteractionResult.FINISHED) {
            context.getFutureResult(0).cancel(false);
            return null;
        }
        ActivityFactory activityFactory = ActivityFactory.getInstance();
        StochasticLanguageGenerator languageGenerator = StochasticLanguageGenerator.getInstance(activityFactory);
        EventLogStochasticTOTraceLanguage logLanguage = languageGenerator.trace(log);
        BpmnStochasticPOTraceLanguage modelLanguage = languageGenerator.poTrace(sBpmnDiagram);
        POEMSConformanceChecking conformanceChecker = new POEMSConformanceCheckingEMSC24Adapter(activityFactory);
        return conformanceChecker.calculateConformance(
                modelLanguage,
                logLanguage
        );
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context      The context to run in.
     * @param sBpmnDiagram The first input is a stochastic BPMN.
     * @param log          The second input.
     * @param parameters   The parameters to use.
     * @return The output.
     */
    @UITopiaVariant(
            affiliation = "RWTH Aachen",
            author = "Aleksandar Kuzmanoski",
            email = "aleksandar.kuzmanoski@rwth-aachen.de"
    )
    @PluginVariant(
            variantLabel = "Earth Movers' Stochastic Conformance Checking of Partially Ordered BPMN Paths",
            requiredParameterLabels = {
                    0,
                    1,
                    2
            }
    )
    public POEMSCCResults run(
            PluginContext context,
            StochasticBPMNDiagram sBpmnDiagram,
            XLog log,
            YourParameters parameters
    ) {
        // Apply the algorithm depending on whether a connection already exists.
        XLogSimplifier simplifier = new BasicXLogSimplifier(
                new XEventNameClassifier(),
                new CachedActivityFactory()
        );
        SimplifiedEventLog simpleLog = simplifier.simplify(log);
        System.out.println(simpleLog.getTotalTraces());
        for (SimplifiedEventLogVariant variant : simpleLog) {
            System.out.println(variant);
            break;
        }
        return null;
    }

    /**
     * The plug-in variant that runs in a UI context and uses a dialog to get the parameters.
     *
     * @param context The context to run in.
     * @param input1  The first input.
     * @param input2  The second input.
     * @return The output.
     *//*
    @UITopiaVariant(
            affiliation = "Your affiliation",
            author = "Your name",
            email = "Your e-mail address"
    )
    @PluginVariant(
            variantLabel = "Your plug-in name, dialog",
            requiredParameterLabels = {
                    0,
                    1
            }
    )
    public YourOutput runUI(
            UIPluginContext context,
            YourFirstInput input1,
            YourSecondInput input2
    ) {
        // Get the default parameters.
        YourParameters parameters = new YourParameters(
                input1,
                input2
        );
        // Get a dialog for this parameters.
        YourDialog dialog = new YourDialog(
                context,
                input1,
                input2,
                parameters
        );
        // Show the dialog. User can now change the parameters.
        InteractionResult result = context.showWizard(
                "Your dialog title",
                true,
                true,
                dialog
        );
        // User has close the dialog.
        if (result == InteractionResult.FINISHED) {
            // Apply the algorithm depending on whether a connection already exists.
            return runConnections(
                    context,
                    input1,
                    input2,
                    parameters
            );
        }
        // Dialog got canceled.
        return null;
    }

    *//**
     * Apply the algorithm depending on whether a connection already exists.
     *
     * @param context The context to run in.
     * @param input1  The first input.
     * @param input2  The second input.
     * @return The output.
     *//*
    private YourOutput runConnections(
            PluginContext context,
            YourFirstInput input1,
            YourSecondInput input2,
            YourParameters parameters
    ) {
        if (parameters.isTryConnections()) {
            // Try to find a connection that matches the inputs and the parameters.
            Collection<YourConnection> connections;
            try {
                connections = context.getConnectionManager().getConnections(
                        YourConnection.class,
                        context,
                        input1,
                        input2
                );
                for (YourConnection connection : connections) {
                    if (connection.getObjectWithRole(YourConnection.FIRSTINPUT)
                            .equals(input1) && connection.getObjectWithRole(YourConnection.SECONDINPUT)
                            .equals(input2) && connection.getParameters().equals(parameters)) {
                        // Found a match. Return the associated output as result of the algorithm.
                        return connection
                                .getObjectWithRole(YourConnection.OUTPUT);
                    }
                }
            } catch (ConnectionCannotBeObtained e) {
            }
        }
        // No connection found. Apply the algorithm to compute a fresh output result.
        YourOutput output = apply(
                context,
                input1,
                input2,
                parameters
        );
        if (parameters.isTryConnections()) {
            // Store a connection containing the inputs, output, and parameters.
            context.getConnectionManager().addConnection(
                    new YourConnection(
                            input1,
                            input2,
                            output,
                            parameters
                    ));
        }
        // Return the output.
        return output;
    }

    *//**
     * The plug-in variant that allows one to test the dialog to get the parameters.
     *
     * @param context The context to run in.
     * @return The output.
     *//*
    @UITopiaVariant(
            affiliation = "Your affiliation",
            author = "Your name",
            email = "Your e-mail address"
    )
    @PluginVariant(
            variantLabel = "Your plug-in name, dialog",
            requiredParameterLabels = {}
    )
    public YourOutput testUI(UIPluginContext context) {
        // Create default inputs.
        YourFirstInput input1 = new YourFirstInput();
        YourSecondInput input2 = new YourSecondInput();
        // Get the default parameters.
        YourParameters parameters = new YourParameters(
                input1,
                input2
        );
        // Get a dialog for this parameters.
        YourDialog dialog = new YourDialog(
                context,
                input1,
                input2,
                parameters
        );
        // Show the dialog. User can now change the parameters.
        InteractionResult result = context.showWizard(
                "Your dialog title",
                true,
                true,
                dialog
        );
        // User has close the dialog.
        if (result == InteractionResult.FINISHED) {
            // Apply the algorithm depending on whether a connection already exists.
            return runConnections(
                    context,
                    input1,
                    input2,
                    parameters
            );
        }
        // Dialog got canceled.
        return null;
    }*/

}
