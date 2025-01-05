package org.processmining.poemsconformancecheckingforbpmn.dialogs;

import com.fluxicon.slickerbox.factory.SlickerFactory;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.stopping.SamplingStoppingCriterionType;
import org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.stochastics.sampling.strategy.transition.TansitionSamplingStrategyType;
import org.processmining.poemsconformancecheckingforbpmn.parameters.*;
import org.processmining.stochasticbpmn.models.stochastic.Probability;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class SBPMNSamplingConfigurationDialog extends ProMPropertiesPanel implements SBPMNSamplingParameters {
    private static final String TITLE = "BPMN POEMS Conformance Checking Configuration";

    private final JComboBox<TansitionSamplingStrategyType> samplingStrategy;
    private final JCheckBox sampleSizeCheckBox;
    private final JCheckBox sampleProbabilityCheckBox;
    private final JFormattedTextField sampleSize;
    private final JFormattedTextField sampleProbability;

    public SBPMNSamplingConfigurationDialog() {
        super(TITLE);
        SlickerFactory slickerFactory = SlickerFactory.instance();
        samplingStrategy = new JComboBox<>(TansitionSamplingStrategyType.values());
        samplingStrategy.setSelectedItem(TansitionSamplingStrategyType.SIMPLE_RANDOM);
        addProperty(
                "Sampling Strategy",
                samplingStrategy
        );

        sampleProbability = createDecimalFormattedTextField(
                0.0,
                1.0,
                0.000001
        );
        sampleProbability.setEnabled(false);
        sampleSize = createLongFormattedTextField(
                0L,
                Long.MAX_VALUE,
                1000
        );

        sampleProbabilityCheckBox = slickerFactory.createCheckBox(
                "Sample Probability",
                false
        );
        sampleProbabilityCheckBox.addChangeListener(changeEvent -> sampleProbability.setEnabled(sampleProbabilityCheckBox.isSelected()));

        sampleSizeCheckBox = slickerFactory.createCheckBox(
                "Sample Size",
                true
        );
        sampleSizeCheckBox.addChangeListener(changeEvent -> sampleSize.setEnabled(sampleSizeCheckBox.isSelected()));
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(
                checkboxPanel,
                BoxLayout.Y_AXIS
        ));
        checkboxPanel.add(sampleSizeCheckBox);
        checkboxPanel.add(sampleProbabilityCheckBox);

        addProperty(
                "Stopping Criterion",
                checkboxPanel
        );
        addProperty(
                "Sample Size",
                sampleSize
        );
        addProperty(
                "Sample Probability",
                sampleProbability
        );
    }

    private static JFormattedTextField createDecimalFormattedTextField(
            double min,
            double max,
            double defaultValue
    ) {
        // Create a NumberFormatter to enforce the min and max values
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(15);
        NumberFormatter numberFormatter = new NumberFormatter(format);

        numberFormatter.setMinimum(min); // Set minimum value
        numberFormatter.setMaximum(max); // Set maximum value
        numberFormatter.setAllowsInvalid(true); // Prevent invalid characters
        numberFormatter.setCommitsOnValidEdit(true); // Commit value on valid input

        // Create the JFormattedTextField with the NumberFormatter
        JFormattedTextField textField = new JFormattedTextField(numberFormatter);
        textField.setColumns(10); // Set preferred width
        textField.setValue(defaultValue);  // Set an initial valid value
        return textField;
    }

    private static JFormattedTextField createLongFormattedTextField(
            long min,
            long max,
            long defaultValue
    ) {
        // Create a NumberFormatter to enforce the min and max values
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(0);
        NumberFormatter numberFormatter = new NumberFormatter(format);

        numberFormatter.setMinimum(min); // Set minimum value
        numberFormatter.setMaximum(max); // Set maximum value
        numberFormatter.setAllowsInvalid(true); // Prevent invalid characters
        numberFormatter.setCommitsOnValidEdit(true); // Commit value on valid input

        // Create the JFormattedTextField with the NumberFormatter
        JFormattedTextField textField = new JFormattedTextField(numberFormatter);
        textField.setColumns(10); // Set preferred width
        textField.setValue(defaultValue);  // Set an initial valid value
        return textField;
    }

    @Override
    public SamplingStoppingCriteriaParameters getStoppingCriteriaParameters() {
        Map<SamplingStoppingCriterionType, Object> map = new HashMap<>();
        if (sampleSizeCheckBox.isSelected()) {
            map.put(
                    SamplingStoppingCriterionType.SAMPLE_SIZE,
                    (SampleSizeParameters) () -> Long.parseLong(sampleSize.getValue().toString())
            );
        }

        if (sampleProbabilityCheckBox.isSelected()) {
            map.put(
                    SamplingStoppingCriterionType.SAMPLE_SIZE,
                    (SampleProbabilityPrecisionParameters) () -> Probability.of(sampleProbability.getValue().toString())
            );
        }
        return () -> map;
    }

    @Override
    public SamplingStrategyParameters getSamplingStrategyParameters() {
        return () -> (TansitionSamplingStrategyType) samplingStrategy.getSelectedItem();
    }
}
