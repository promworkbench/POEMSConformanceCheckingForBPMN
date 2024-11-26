package org.processmining.sccwbpmnnpos.plugins;

import java.util.Collection;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.sccwbpmnnpos.algorithms.conformance_checking.YourAlgorithm;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.XLogSimplifier;
import org.processmining.sccwbpmnnpos.algorithms.inputs.log.simplifier.impl.BasicXLogSimplifier;
import org.processmining.sccwbpmnnpos.connections.YourConnection;
import org.processmining.sccwbpmnnpos.dialogs.YourDialog;
import org.processmining.sccwbpmnnpos.help.YourHelp;
import org.processmining.sccwbpmnnpos.models.POEMSCCResults;
import org.processmining.sccwbpmnnpos.models.YourFirstInput;
import org.processmining.sccwbpmnnpos.models.YourOutput;
import org.processmining.sccwbpmnnpos.models.YourSecondInput;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLog;
import org.processmining.sccwbpmnnpos.models.log.SimplifiedEventLogVariant;
import org.processmining.sccwbpmnnpos.models.utils.activity.SimpleActivityRegistry;
import org.processmining.sccwbpmnnpos.parameters.YourParameters;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

@Plugin(
        name = "BPMN POEMS Conformance Checking",
        parameterLabels = {"Stochastic BPMN", "Event Log", "Model Probability Mass"},
        returnLabels = {"Model-Log Conformance measure"},
        returnTypes = {POEMSCCResults.class},
        help = YourHelp.TEXT,
        level = PluginLevel.Local
)
public class POEMStochasticConformanceCheckingPlugin extends YourAlgorithm {

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context    The context to run in.
     * @param sBpmnDiagram     The first input is a stochastic BPMN.
     * @param log     The second input.
     * @param parameters The parameters to use.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen", author = "Aleksandar Kuzmanoski", email = "aleksandar.kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "Earth Movers' Stochastic Conformance Checking of Partially Ordered BPMN Paths", requiredParameterLabels = {0, 1, 2}, help = "")
    public POEMSCCResults run(PluginContext context, StochasticBPMNDiagram sBpmnDiagram, XLog log, YourParameters parameters) {
        // Apply the algorithm depending on whether a connection already exists.
        XLogSimplifier simplifier = new BasicXLogSimplifier(new XEventNameClassifier(), new SimpleActivityRegistry());
        SimplifiedEventLog simpleLog = simplifier.simplify(log);
        System.out.println(simpleLog.getTotalTraces());
        for (SimplifiedEventLogVariant variant : simpleLog) {
            System.out.println(variant);
            break;
        }
        return null;
    }

    /**
     * The plug-in variant that runs in any context and uses the default parameters.
     *
     * @param context The context to run in.
     * @param sBpmnDiagram  The first input.
     * @param log  The second input.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen", author = "Aleksandar Kuzmanoski", email = "Yaleksandar.kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "Earth Movers' Stochastic Conformance Checking of Partially Ordered BPMN Paths", requiredParameterLabels = {0, 1})
    public YourOutput runDefault(PluginContext context, StochasticBPMNDiagram sBpmnDiagram, XLog log) {
        // Apply the algorithm depending on whether a connection already exists.
        XLogSimplifier simplifier = new BasicXLogSimplifier(new XEventNameClassifier(), new SimpleActivityRegistry());
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
     */
    @UITopiaVariant(affiliation = "Your affiliation", author = "Your name", email = "Your e-mail address")
    @PluginVariant(variantLabel = "Your plug-in name, dialog", requiredParameterLabels = {0, 1})
    public YourOutput runUI(UIPluginContext context, YourFirstInput input1, YourSecondInput input2) {
        // Get the default parameters.
        YourParameters parameters = new YourParameters(input1, input2);
        // Get a dialog for this parameters.
        YourDialog dialog = new YourDialog(context, input1, input2, parameters);
        // Show the dialog. User can now change the parameters.
        InteractionResult result = context.showWizard("Your dialog title", true, true, dialog);
        // User has close the dialog.
        if (result == InteractionResult.FINISHED) {
            // Apply the algorithm depending on whether a connection already exists.
            return runConnections(context, input1, input2, parameters);
        }
        // Dialog got canceled.
        return null;
    }

    /**
     * The plug-in variant that allows one to test the dialog to get the parameters.
     *
     * @param context The context to run in.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "Your affiliation", author = "Your name", email = "Your e-mail address")
    @PluginVariant(variantLabel = "Your plug-in name, dialog", requiredParameterLabels = {})
    public YourOutput testUI(UIPluginContext context) {
        // Create default inputs.
        YourFirstInput input1 = new YourFirstInput();
        YourSecondInput input2 = new YourSecondInput();
        // Get the default parameters.
        YourParameters parameters = new YourParameters(input1, input2);
        // Get a dialog for this parameters.
        YourDialog dialog = new YourDialog(context, input1, input2, parameters);
        // Show the dialog. User can now change the parameters.
        InteractionResult result = context.showWizard("Your dialog title", true, true, dialog);
        // User has close the dialog.
        if (result == InteractionResult.FINISHED) {
            // Apply the algorithm depending on whether a connection already exists.
            return runConnections(context, input1, input2, parameters);
        }
        // Dialog got canceled.
        return null;
    }

    /**
     * Apply the algorithm depending on whether a connection already exists.
     *
     * @param context The context to run in.
     * @param input1  The first input.
     * @param input2  The second input.
     * @return The output.
     */
    private YourOutput runConnections(PluginContext context, YourFirstInput input1, YourSecondInput input2, YourParameters parameters) {
        if (parameters.isTryConnections()) {
            // Try to find a connection that matches the inputs and the parameters.
            Collection<YourConnection> connections;
            try {
                connections = context.getConnectionManager().getConnections(
                        YourConnection.class, context, input1, input2);
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
        YourOutput output = apply(context, input1, input2, parameters);
        if (parameters.isTryConnections()) {
            // Store a connection containing the inputs, output, and parameters.
            context.getConnectionManager().addConnection(
                    new YourConnection(input1, input2, output, parameters));
        }
        // Return the output.
        return output;
    }

}