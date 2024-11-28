package org.processmining.sccwbpmnnpos.algorithms.utils.converters;

public interface Converter<I, O> {
    O convert(I input);
}
