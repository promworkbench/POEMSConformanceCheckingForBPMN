package org.processmining.sccwbpmnnpos.models.utils.activity.factory;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;

public interface ActivityFactory {
    Activity create(String label);
}
