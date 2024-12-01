package org.processmining.sccwbpmnnpos.models.trace.partial_order.converters.emsc24;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.processmining.earthmoversstochasticconformancechecking.stochasticlanguage.Activity2IndexKey;
import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.partial.eventbased.EventBasedPartiallyOrderedSet.Event;
import org.processmining.sccwbpmnnpos.models.trace.partial_order.PartiallyOrderedTrace;

public class PartiallyOrderedTrace2EMSC24PartialOrderConverterImpl<T extends Activity> implements PartiallyOrderedTrace2EMSC24PartialOrderConverter<T> {

    @Override
    public int[] convert(PartiallyOrderedTrace<T> poTrace, Activity2IndexKey activity2IndexKey) {
        int size = poTrace.size();
        int[] newPoTrace = new int[1 + size * 2 + poTrace.getNumberOfConnections()];
        newPoTrace[0] = size;
        TObjectIntMap<Event<T>> index = new TObjectIntHashMap<>(10, 0.5F, -1);
        int nodeIndex = 1;
        int edgeIndex = 1 + 2 * size;
        for (Event<T> event : poTrace.getPartiallyOrderedSet()) {
            newPoTrace[nodeIndex] = activity2IndexKey.toIndex(event.getItem().getLabel());
            newPoTrace[nodeIndex + size] = edgeIndex;
            index.put(event, nodeIndex - 1);
            nodeIndex++;
            for (Event<T> predecessor : poTrace.getPartiallyOrderedSet().getPredecessors(event)) {
                int predecessorIdx = index.get(predecessor);
                newPoTrace[edgeIndex++] = predecessorIdx;
            }
        }

        return newPoTrace;
    }
}
