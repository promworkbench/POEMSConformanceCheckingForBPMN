package org.processmining.sccwbpmnnpos.models.stochastic_language;

import com.google.common.collect.Lists;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class MapStochasticLanguage<L, T extends OrderedSet<L>> implements StochasticLanguage<L, T> {
    final private Map<T, StochasticLanguageEntry<T>> map;

    public MapStochasticLanguage() {
        this.map = new HashMap<>();
    }

    @Override
    public Collection<L> getAlphabet() {
        return map.keySet().stream().flatMap(entry -> entry.getAlphabet().stream()).collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Collection<StochasticLanguageEntry<T>> entries() {
        return map.values();
    }

    @Override
    public void put(T element, Double probability) {
        map.put(element, new StochasticLanguageEntry<>(element, probability));
    }

    @Override
    public Iterator<T> iterator() {
        return map.keySet().stream().sorted().iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (StochasticLanguageEntry<T> entry :
                Lists.reverse(map.values().stream().sorted().collect(Collectors.toList()))) {
            sb.append(entry);
            sb.append("\n");
        }
        return sb.toString();
    }
}
