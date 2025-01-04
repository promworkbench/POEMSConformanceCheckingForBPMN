package org.processmining.poemsconformancecheckingforbpmn.models.stochastic.language.trace;

import cern.colt.Arrays;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.StochasticLanguage;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.StochasticTraceIterator;

import java.util.ArrayList;

public class EMSC24StochasticLanguageAdapter<T> implements StochasticLanguage<T> {
    private final Activity2IndexKey activity2IndexKey;
    private final ArrayList<int[]> traces;
    private final double[] probabilities;

    public EMSC24StochasticLanguageAdapter(Activity2IndexKey activity2IndexKey, ArrayList<int[]> traces,
                                           double[] probabilities) {
        this.activity2IndexKey = activity2IndexKey;
        this.traces = traces;
        this.probabilities = probabilities;
    }


    @Override
    public int size() {
        return traces.size();
    }

    @Override
    public int[] getTrace(int i) {
        return traces.get(i);
    }

    @Override
    public String getTraceString(int i) {
        return Arrays.toString(this.getActivityKey().toTraceString(this.getTrace(i)));
    }

    @Override
    public StochasticTraceIterator<T> iterator() {
        return new StochasticTraceIterator<T>() {
            int pathIndex = -1;

            public int[] next() {
                return this.nextPath();
            }

            public boolean hasNext() {
                return this.pathIndex < traces.size() - 1;
            }

            public int getTraceIndex() {
                return this.pathIndex;
            }

            public double getProbability() {
                return probabilities[this.pathIndex];
            }

            public int[] nextPath() {
                ++this.pathIndex;
                return this.getPath();
            }

            public int[] getPath() {
                return traces.get(this.pathIndex);
            }
        };
    }

    @Override
    public Activity2IndexKey getActivityKey() {
        return activity2IndexKey;
    }
}
