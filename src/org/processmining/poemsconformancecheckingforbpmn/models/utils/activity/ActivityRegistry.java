package org.processmining.poemsconformancecheckingforbpmn.models.utils.activity;

import java.util.Optional;

public interface ActivityRegistry {
    Integer register(final Activity activity);

    Optional<Integer> getRegistered(final Activity activity);

    boolean isRegistered(final Activity activity);

    Optional<Activity> getRegistered(final Integer id);
}
