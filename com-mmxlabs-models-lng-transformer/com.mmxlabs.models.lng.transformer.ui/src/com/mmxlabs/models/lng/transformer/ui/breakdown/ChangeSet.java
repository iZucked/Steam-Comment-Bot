package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;

/**
 * A set of changes that can be actioned together to get to a new state.
 * 
 * @author sg
 *
 */
public class ChangeSet implements Serializable {
	public final List<Change> changesList;
	public final Set<Change> changesSet;

	public long[] metric = new long[MetricType.values().length];
	public long[] metricDelta = new long[MetricType.values().length];
	public long[] metricDeltaToBase = new long[MetricType.values().length];

	private final int hashCode;

	public ChangeSet(final Collection<Change> changes) {
		this.changesList = Collections.unmodifiableList(new ArrayList<>(changes));
		// Use hash sets as we do not care about ordering
		this.changesSet = Collections.unmodifiableSet(new HashSet<>(changes));
		this.hashCode = changesSet.hashCode();
	}

	public void setMetric(MetricType metricType, long value, long delta, long deltaToBase) {
		int idx = metricType.ordinal();
		metric[idx] = value;
		metricDelta[idx] = delta;
		metricDeltaToBase[idx] = deltaToBase;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ChangeSet) {
			// Use hash sets as we do not care about ordering
			return Objects.equal(changesSet, new HashSet<>(((ChangeSet) obj).changesSet));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}
}
