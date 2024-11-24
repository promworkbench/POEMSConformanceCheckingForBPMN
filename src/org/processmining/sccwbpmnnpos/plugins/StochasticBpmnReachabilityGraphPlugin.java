package org.processmining.sccwbpmnnpos.plugins;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.transitionsystem.TransitionSystem;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace.Bpmn2MinimalReachabilityGraphConverter;
import org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace.Bpmn2MinimalReachabilityGraphConverterImpl;
import org.processmining.sccwbpmnnpos.algorithms.inputs.model.statespace.BpmnUnboundedException;
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
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

@Plugin(
        name = "POEMS BPMN Reachability Graph",
        parameterLabels = {"Stochastic BPMN",},
        returnLabels = {"Transition System"},
        returnTypes = {TransitionSystem.class},
        help = YourHelp.TEXT,
        level = PluginLevel.Local
)
public class StochasticBpmnReachabilityGraphPlugin {
    private final Bpmn2MinimalReachabilityGraphConverter bpmn2StochasticLanguageConverter;
    private final BpmnDiagramBuilder diagramBuilder;

    public StochasticBpmnReachabilityGraphPlugin() {
        MultisetFactory multisetFactory = new DefaultMultisetFactory();
        MultisetUtils multisetUtils = new SimpleMultisetUtils(multisetFactory);
        BpmnTokenFactory tokenFactory = new SimpleBpmnTokenFactory();
        BpmnMarkingFactory markingFactory = new DefaultBpmnMarkingFactory(multisetFactory);
        BpmnMarkingUtils markingUtils = new SimpleBpmnMarkingUtils(multisetUtils, markingFactory);
        ExecutableBpmnNodeFactory executableNodeFactory =
                new CachedExecutableBpmnNodeFactory(new SimpleExecutableBpmnNodeFactory(tokenFactory,
                        markingFactory, markingUtils));

        CartesianProductCalculator cartesianProductCalculator = new NestedLoopsCartesianProductCalculator();
        bpmn2StochasticLanguageConverter =
                new Bpmn2MinimalReachabilityGraphConverterImpl(executableNodeFactory, markingFactory,
                        cartesianProductCalculator);
        diagramBuilder = new BpmnDiagramBuilderImpl();

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
    @PluginVariant(variantLabel = "Stochastic BPMN Transition System", requiredParameterLabels = {0}, help = "")
    public TransitionSystem run(PluginContext context, StochasticBPMNDiagram diagram) throws BpmnUnboundedException {
        return bpmn2StochasticLanguageConverter.convert(diagram);
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param bpmn The first input is a bpmn model.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen", author = "Aleksandar Kuzmanoski", email = "aleksandar" +
            ".kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "Stochastic BPMN Transition System", requiredParameterLabels = {0}, help = "")
    public TransitionSystem run(PluginContext context, Bpmn bpmn) throws BpmnUnboundedException {
        return bpmn2StochasticLanguageConverter.convert(diagramBuilder.build(bpmn));
    }
}
