package org.processmining.sccwbpmnnpos.plugins.bpmn;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2PartiallyOrderedReachabilityGraphConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.Bpmn2ReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.CartesianProductCalculator;
import org.processmining.sccwbpmnnpos.algorithms.utils.cartesianproduct.NestedLoopsCartesianProductCalculator;
import org.processmining.sccwbpmnnpos.help.YourHelp;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.BpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.factory.DefaultBpmnMarkingFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.BpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.token.factory.SimpleBpmnTokenFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.BpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.marking.utils.SimpleBpmnMarkingUtils;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.CachedExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.ExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.bpmn.execution.node.factory.SimpleExecutableBpmnNodeFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.DefaultMultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.MultisetUtils;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.SimpleMultisetUtils;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.BpmnDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.BpmnDiagramBuilderImpl;

@Plugin(
        name = "BPMN Partially Ordered Reachability Graph",
        parameterLabels = {"BPMN",},
        returnLabels = {"Transition System"},
        returnTypes = {TransitionSystem.class},
        help = YourHelp.TEXT,
        level = PluginLevel.Local
)
public class BpmnReachabilityGraphPlugin {
    private final Bpmn2ReachabilityGraphConverter converter;
    private final BpmnDiagramBuilder diagramBuilder;

    public BpmnReachabilityGraphPlugin() {
        MultisetFactory multisetFactory = new DefaultMultisetFactory();
        MultisetUtils multisetUtils = new SimpleMultisetUtils(multisetFactory);
        BpmnTokenFactory tokenFactory = new SimpleBpmnTokenFactory();
        BpmnMarkingFactory markingFactory = new DefaultBpmnMarkingFactory(multisetFactory);
        BpmnMarkingUtils markingUtils = new SimpleBpmnMarkingUtils(multisetUtils, markingFactory);
        SimpleExecutableBpmnNodeFactory simpleExecutableBpmnNodeFactory =
                new SimpleExecutableBpmnNodeFactory(tokenFactory,
                        markingFactory, markingUtils);
        ExecutableBpmnNodeFactory executableNodeFactory =
                new CachedExecutableBpmnNodeFactory(simpleExecutableBpmnNodeFactory);

        CartesianProductCalculator cartesianProductCalculator = new NestedLoopsCartesianProductCalculator();
        converter =
                new Bpmn2PartiallyOrderedReachabilityGraphConverterImpl(executableNodeFactory, markingFactory,
                        cartesianProductCalculator);
        diagramBuilder = new BpmnDiagramBuilderImpl();
    }


    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param diagram The first input is a BPMN Diagram.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen", author = "Aleksandar Kuzmanoski", email = "aleksandar" +
            ".kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "BPMN Partially Ordered Reachability Graph", requiredParameterLabels = {0}, help = "")
    public TransitionSystem run(PluginContext context, BPMNDiagram diagram) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        return converter.convert(diagram);
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param bpmn    The first input is a bpmn model.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen", author = "Aleksandar Kuzmanoski", email = "aleksandar" +
            ".kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "BPMN Partially Ordered Reachability Graph", requiredParameterLabels = {0}, help = "")
    public TransitionSystem run(PluginContext context, Bpmn bpmn) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        return converter.convert(diagramBuilder.build(bpmn));
    }
}
