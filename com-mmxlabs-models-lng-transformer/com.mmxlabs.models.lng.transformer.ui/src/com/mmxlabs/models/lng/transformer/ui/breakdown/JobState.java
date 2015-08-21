package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * The current state of a particular track of search. This contains the list of changesets and the current working set of changes. It also include the current sequences state and P&L/lateness total
 * and last changes set deltas (if available).
 * 
 * @author sg
 *
 */
public class JobState implements Serializable{
	protected transient ISequences rawSequences = null;
	protected transient ISequences fullSequences = null;
	public int[][] persistedSequences;
	public int[] persistedUnusedElements;

	public final List<Change> changesAsList;
	public final Set<Change> changesAsSet;
	public final List<ChangeSet> changeSetsAsList;
	public final Set<ChangeSet> changeSetsAsSet;
	public final int hashCode;

	public long[] metric = new long[MetricType.values().length];
	public long[] metricDelta = new long[MetricType.values().length];
	public long[] metricDeltaToBase = new long[MetricType.values().length];

	public JobStateMode mode = JobStateMode.BRANCH;

	private List<Difference> differencesList = null;
	
	public JobState(final ISequences rawSequences, final List<ChangeSet> changeSets, final List<Change> changes, final List<Difference> differencesList) {
		this.rawSequences = rawSequences;

		this.changesAsList = Collections.unmodifiableList(new ArrayList<>(changes));
		this.changesAsSet = Collections.unmodifiableSet(new HashSet<>(changes));
		this.changeSetsAsList = Collections.unmodifiableList(new ArrayList<>(changeSets));
		this.changeSetsAsSet = Collections.unmodifiableSet(new HashSet<>(changeSets));
		this.hashCode = Objects.hashCode(changes, changeSets);
		this.differencesList = differencesList;
	}

	public JobState(final ISequences rawSequences, final List<ChangeSet> changeSets, final List<Change> changes) {
					this(rawSequences, changeSets, changes, null);
			 	}

	public void setMetric(MetricType metricType, long value, long delta, long deltaToBase) {
		int idx = metricType.ordinal();
		metric[idx] = value;
		metricDelta[idx] = delta;
		metricDeltaToBase[idx] = deltaToBase;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof JobState) {
			return changesAsSet.equals(((JobState) obj).changesAsSet) && changeSetsAsSet.equals(((JobState) obj).changeSetsAsSet) && Objects.equal(rawSequences, ((JobState) obj).rawSequences);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {

		// We cannot persist the rawSequences as this is linked to external data.
		// However we can store the representation as an int array and re-create the sequences with reference to a IOptimisationData instance.
		persistedSequences = new int[rawSequences.getResources().size()][];
		for (int i = 0; i < persistedSequences.length; ++i) {
			final ISequence s = rawSequences.getSequence(i);
			persistedSequences[i] = new int[s.size()];
			for (int j = 0; j < persistedSequences[i].length; ++j) {
				persistedSequences[i][j] = s.get(j).getIndex();
			}
		}
		persistedUnusedElements = new int[rawSequences.getUnusedElements().size()];
		for (int i = 0; i < persistedUnusedElements.length; ++i) {
			persistedUnusedElements[i] = rawSequences.getUnusedElements().get(i).getIndex();
		}

		out.defaultWriteObject();

	}

	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

		in.defaultReadObject();

		// Do nothing with the raw sequence as we do not have the information here to generate it from the int array.
		// @see JobStateSerialiser
	}

	public ISequences getRawSequences() {
		return rawSequences;
	}

	public void setRawSequences(ISequences rawSequences) {
		this.rawSequences = rawSequences;
	}

	public ISequences getFullSequences() {
		return fullSequences;
	}

	public void setFullSequences(ISequences fullSequences) {
		this.fullSequences = fullSequences;
	}

	// private void readObjectNoData() throws ObjectStreamException;
}