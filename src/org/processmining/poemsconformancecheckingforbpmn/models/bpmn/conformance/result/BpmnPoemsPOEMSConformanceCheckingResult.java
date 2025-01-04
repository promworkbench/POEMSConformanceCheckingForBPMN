package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result;

public class BpmnPoemsPOEMSConformanceCheckingResult implements POEMSConformanceCheckingResult {
    private final Double conformanceUpperBound;
    private final Double conformanceLowerBound;

    public BpmnPoemsPOEMSConformanceCheckingResult(Double conformanceLowerBound, Double conformanceUpperBound) {
        this.conformanceUpperBound = conformanceUpperBound;
        this.conformanceLowerBound = conformanceLowerBound;
    }

    @Override
    public Double getConformanceUpperBound() {
        return conformanceUpperBound;
    }

    public Double getConformanceLowerBound() {
        return conformanceLowerBound;
    }

    @Override
    public String toString() {
        return "[" + getConformanceLowerBound() + ", " + getConformanceUpperBound() + "]";
    }
}
