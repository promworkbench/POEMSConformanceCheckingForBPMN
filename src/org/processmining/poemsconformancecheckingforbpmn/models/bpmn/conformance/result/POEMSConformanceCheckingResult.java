package org.processmining.poemsconformancecheckingforbpmn.models.bpmn.conformance.result;

public interface POEMSConformanceCheckingResult extends ConformanceCheckingResult {
    @Override
    default Double conformanceValue() {
        return getConformanceLowerBound();
    }

    Double getConformanceUpperBound();

    Double getConformanceLowerBound();
}
