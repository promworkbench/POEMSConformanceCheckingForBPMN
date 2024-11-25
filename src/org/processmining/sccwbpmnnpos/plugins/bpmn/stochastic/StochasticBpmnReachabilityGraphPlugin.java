package org.processmining.sccwbpmnnpos.plugins.bpmn.stochastic;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnNoOptionToCompleteException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.statespace.BpmnUnboundedException;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2PartiallyOrderedReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.bpmn.stochastic.statespace.StochasticBpmn2ReachabilityGraphConverter;
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
import org.processmining.sccwbpmnnpos.models.bpmn.stochastic.execution.node.factory.ExecutableStochasticBpmnNodeFactoryImpl;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.DefaultMultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.factory.MultisetFactory;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.MultisetUtils;
import org.processmining.sccwbpmnnpos.models.utils.multiset.utils.SimpleMultisetUtils;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilderImpl;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

@Plugin(
        name = "Stochastic BPMN Partially Ordered Reachability Graph",
        parameterLabels = {"Stochastic BPMN",},
        returnLabels = {"Transition System"},
        returnTypes = {TransitionSystem.class},
        help = YourHelp.TEXT,
        level = PluginLevel.Local
)
public class StochasticBpmnReachabilityGraphPlugin {
    private final StochasticBpmn2ReachabilityGraphConverter converter;
    private final StochasticBPMNDiagramBuilder diagramBuilder;

    public StochasticBpmnReachabilityGraphPlugin() {
        MultisetFactory multisetFactory = new DefaultMultisetFactory();
        MultisetUtils multisetUtils = new SimpleMultisetUtils(multisetFactory);
        BpmnTokenFactory tokenFactory = new SimpleBpmnTokenFactory();
        BpmnMarkingFactory markingFactory = new DefaultBpmnMarkingFactory(multisetFactory);
        BpmnMarkingUtils markingUtils = new SimpleBpmnMarkingUtils(multisetUtils, markingFactory);
        SimpleExecutableBpmnNodeFactory simpleExecutableBpmnNodeFactory =
                new SimpleExecutableBpmnNodeFactory(tokenFactory,
                markingFactory, markingUtils);
        ExecutableStochasticBpmnNodeFactoryImpl stochasticNodeFactory =
                new ExecutableStochasticBpmnNodeFactoryImpl(simpleExecutableBpmnNodeFactory, tokenFactory,
                        markingFactory, markingUtils);
        ExecutableBpmnNodeFactory executableNodeFactory = new CachedExecutableBpmnNodeFactory(stochasticNodeFactory);

        CartesianProductCalculator cartesianProductCalculator = new NestedLoopsCartesianProductCalculator();
        converter =
                new StochasticBpmn2PartiallyOrderedReachabilityGraphConverter(executableNodeFactory, markingFactory,
                        cartesianProductCalculator);
        diagramBuilder = new StochasticBPMNDiagramBuilderImpl();
    }


    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param diagram The first input is a stochastic BPMN.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen", author = "Aleksandar Kuzmanoski", email = "aleksandar" +
            ".kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "Stochastic BPMN Partially Ordered Reachability Graph", requiredParameterLabels = {0}, help = "")
    public TransitionSystem run(PluginContext context, StochasticBPMNDiagram diagram) throws BpmnNoOptionToCompleteException, BpmnUnboundedException {
        return converter.convert(diagram);
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param bpmn    The first input is a stochastic bpmn model.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen", author = "Aleksandar Kuzmanoski", email = "aleksandar" +
            ".kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "Stochastic BPMN Partially Ordered Reachability Graph", requiredParameterLabels = {0}, help = "")
    public TransitionSystem run(PluginContext context, StochasticBpmn bpmn) throws BpmnNoOptionToCompleteException,
            BpmnUnboundedException {
        return converter.convert(diagramBuilder.build(bpmn));
    }
}
