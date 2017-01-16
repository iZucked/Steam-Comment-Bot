/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

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

	protected transient ISequences rawSequences = null;
	protected transient ISequences fullSequences = null;
	public int[][] persistedSequences;
	public int[] persistedUnusedElements;

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

	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {

		// We cannot persist the rawSequences as this is linked to external data.
		// However we can store the representation as an int array and re-create the sequences with reference to a IOptimisationData instance.
		int resourceCount = 0;
		for (final IResource r : rawSequences.getResources()) {
			++resourceCount;
		}

		persistedSequences = new int[resourceCount][];

		{
			persistedSequences = new int[resourceCount][];
			int i = 0;
			for (final IResource r : rawSequences.getResources()) {
				final ISequence s = rawSequences.getSequence(r);
				persistedSequences[i] = new int[s.size()];
				for (int j = 0; j < persistedSequences[i].length; ++j) {
					persistedSequences[i][j] = s.get(j).getIndex();
				}
				i++;
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

	public static void fixStates(final IOptimisationData data, final Map<Integer, ISequenceElement> elementCache, ChangeSet obj) {
		final int[][] persistedSequences = obj.persistedSequences;
		// Could be null if this object has been saved twice to the same object stream. Second loading of the object will have already had the rawSequences recreated and the persistedSequences array
		// nulled out.
		if (persistedSequences != null) {
			Set<ISequenceElement> seenElements = new HashSet<>();

			final IModifiableSequences sequences = new ModifiableSequences(data.getResources());
			for (int i = 0; i < persistedSequences.length; ++i) {
				final IModifiableSequence s = sequences.getModifiableSequence(i);
				for (int j = 0; j < persistedSequences[i].length; ++j) {
					ISequenceElement element = elementCache.get(persistedSequences[i][j]);
					s.add(element);
					assert (seenElements.add(element));
				}
			}
			for (int i = 0; i < obj.persistedUnusedElements.length; ++i) {
				int idx = obj.persistedUnusedElements[i];
				ISequenceElement e = elementCache.get(idx);
				sequences.getModifiableUnusedElements().add(e);
				assert (seenElements.add(e));
			}

			obj.setRawSequences(sequences);
			obj.persistedSequences = null;
			obj.persistedUnusedElements = null;
		}
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

}
