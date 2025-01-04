package org.processmining.poemsconformancecheckingforbpmn.algorithms.utils.converters;

public interface Converter<I, O> {
    O convert(I input);
}
