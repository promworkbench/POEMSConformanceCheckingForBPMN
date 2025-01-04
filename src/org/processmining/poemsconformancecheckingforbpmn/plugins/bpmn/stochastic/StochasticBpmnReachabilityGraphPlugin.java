package org.processmining.poemsconformancecheckingforbpmn.plugins.bpmn.stochastic;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2POReachabilityGraphConverter;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilderImpl;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

@Plugin(
        name = "Stochastic BPMN to Stochastic Partially Ordered Reachability Graph (SPORG)",
        parameterLabels = {"Stochastic BPMN",},
        returnLabels = {"Stochastic PO Reachability Graph (SPORG)"},
        returnTypes = {ReachabilityGraph.class},
        help = "It generates a stochastic partially ordered reachability graph (SPORG) from a BPMN. The SPORG has reachable " +
                "markings as states, and partially ordered runs of the BPMN model with no choices in them as " +
                "transitions. Moreover, each transition is assigned with a probability value such that all the outgoing" +
                "transitions of a state in the SPORG sum up to 1.",
        level = PluginLevel.Local
)
public class StochasticBpmnReachabilityGraphPlugin {
    private final StochasticBpmn2POReachabilityGraphConverter converter;
    private final StochasticBPMNDiagramBuilder diagramBuilder;

    public StochasticBpmnReachabilityGraphPlugin() {
        converter = StochasticBpmn2POReachabilityGraphConverter.getInstance();
        diagramBuilder = new StochasticBPMNDiagramBuilderImpl();
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param diagram The first input is a stochastic BPMN.
     * @return The output.
     */
    @UITopiaVariant(
            affiliation = "RWTH Aachen",
            author = "Aleksandar Kuzmanoski",
            email = "aleksandar.kuzmanoski@rwth-aachen.de"
    )
    @PluginVariant(
            variantLabel = "From Stochastic BPMN Diagram",
            requiredParameterLabels = {0}
    )
    public ReachabilityGraph run(
            PluginContext context,
            StochasticBPMNDiagram diagram
    ) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        ReachabilityGraph reachabilityGraph = converter.convert(diagram);
        context.getFutureResult(0).setLabel(reachabilityGraph.getLabel() + " Stochastic Reachability Graph");
        return reachabilityGraph;
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param bpmn    The first input is a stochastic bpmn model.
     * @return The output.
     */
    @UITopiaVariant(
            affiliation = "RWTH Aachen",
            author = "Aleksandar Kuzmanoski",
            email = "aleksandar.kuzmanoski@rwth-aachen.de"
    )
    @PluginVariant(
            variantLabel = "From Stochastic BPMN Model (uses the default Diagram)",
            requiredParameterLabels = {0}
    )
    public ReachabilityGraph run(
            PluginContext context,
            StochasticBpmn bpmn
    ) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        return converter.convert(diagramBuilder.build(
                bpmn,
                ""
        ));
    }
}
