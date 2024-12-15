package org.processmining.sccwbpmnnpos.plugins.bpmn;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.ReachabilityGraph;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2POReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.BpmnDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.BpmnDiagramBuilderImpl;

@Plugin(
        name = "BPMN to Partially Ordered Reachability Graph (PORG)",
        parameterLabels = {"BPMN"},
        returnLabels = {
                "PO Reachability Graph (PORG)"
        },
        returnTypes = {ReachabilityGraph.class},
        help = "It generates a partially ordered reachability graph (PORG) from a BPMN. The PORG has reachable " +
                "markings as states, and partially ordered runs of the BPMN model with no choices in them as " +
                "transitions.",
        level = PluginLevel.Local
)
public class BpmnReachabilityGraphPlugin {
    private final Bpmn2POReachabilityGraphConverter converter;
    private final BpmnDiagramBuilder diagramBuilder;

    public BpmnReachabilityGraphPlugin() {
        converter = Bpmn2POReachabilityGraphConverter.getInstance();
        diagramBuilder = new BpmnDiagramBuilderImpl();
    }


    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param diagram The first input is a BPMN Diagram.
     * @return The output.
     */
    @UITopiaVariant(
            affiliation = "RWTH Aachen",
            author = "Aleksandar Kuzmanoski",
            email = "aleksandar" +
                    ".kuzmanoski@rwth-aachen.de"
    )
    @PluginVariant(
            variantLabel = "From BPMN Diagram",
            requiredParameterLabels = {0}
    )
    public ReachabilityGraph run(
            PluginContext context,
            BPMNDiagram diagram
    ) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        ReachabilityGraph reachabilityGraph = converter.convert(diagram);
        context.getFutureResult(0).setLabel(reachabilityGraph.getLabel() + " Reachability Graph");
        return reachabilityGraph;
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param bpmn    The first input is a bpmn model.
     * @return The output.
     */
    @UITopiaVariant(
            affiliation = "RWTH Aachen",
            author = "Aleksandar Kuzmanoski",
            email = "aleksandar" +
                    ".kuzmanoski@rwth-aachen.de"
    )
    @PluginVariant(
            variantLabel = "From BPMN Model, uses the default Diagram",
            requiredParameterLabels = {0}
    )
    public ReachabilityGraph run(
            PluginContext context,
            Bpmn bpmn
    ) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        return converter.convert(diagramBuilder.build(
                bpmn,
                ""
        ));
    }
}
