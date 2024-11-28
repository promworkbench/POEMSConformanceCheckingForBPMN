package org.processmining.sccwbpmnnpos.models.utils.activity.factory;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;

import java.util.Collection;

public interface ActivityFactory {
    static ActivityFactory getInstance() {
        return new CachedActivityFactory();
    }

    Activity create(String label);

    Collection<Activity> getAlphabet();
}
