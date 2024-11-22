package org.processmining.sccwbpmnnpos.models.utils.activity;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class SimpleActivityRegistry implements ActivityRegistry {
    private final ArrayList<Activity> index2Activity;
    private final TObjectIntMap<ActivityLabelWrapper> activity2Index;

    public SimpleActivityRegistry() {
        index2Activity = new ArrayList<>();
        activity2Index = new TObjectIntHashMap<>(10, 0.5F, -1);
    }

    @Override
    public synchronized Integer register(Activity activity) {
        final int nextIndex = index2Activity.size();
        int prevIndex = activity2Index.putIfAbsent(new ActivityLabelWrapper(activity), nextIndex);
        if (prevIndex == activity2Index.getNoEntryValue()) {
            index2Activity.add(activity);
            return nextIndex;
        }
        return prevIndex;
    }

    @Override
    public Optional<Integer> getRegistered(Activity activity) {
        return Optional.ofNullable(activity2Index.get(new ActivityLabelWrapper(activity)));
    }

    @Override
    public boolean isRegistered(Activity activity) {
        return activity2Index.containsKey(new ActivityLabelWrapper(activity));
    }

    @Override
    public Optional<Activity> getRegistered(Integer id) {
        if (id >= index2Activity.size()) {
            return Optional.empty();
        }
        return Optional.of(index2Activity.get(id));
    }

    private static class ActivityLabelWrapper {
        public Activity getActivity() {
            return activity;
        }

        private final Activity activity;

        private ActivityLabelWrapper(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ActivityLabelWrapper)) return false;
            ActivityLabelWrapper that = (ActivityLabelWrapper) o;
            return Objects.equals(activity.getLabel(), that.activity.getLabel());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(activity.getLabel());
        }
    }
}
