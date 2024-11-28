package org.processmining.sccwbpmnnpos.models.trace;

import org.processmining.sccwbpmnnpos.models.utils.activity.Activity;
import org.processmining.sccwbpmnnpos.models.utils.ordered_set.OrderedSet;

import java.util.Collection;

public interface Trace<A extends Activity> extends Collection<A> {
}
