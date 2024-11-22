package org.processmining.sccwbpmnnpos.models.conformance.result;

public class BpmnPoemsConformanceCheckingResult implements ConformanceCheckingResult {
    private final double conformanceValue;

    public BpmnPoemsConformanceCheckingResult(double conformanceValue) {
        this.conformanceValue = conformanceValue;
    }

    @Override
    public double getConformanceValue() {
        return 0;
    }
}
