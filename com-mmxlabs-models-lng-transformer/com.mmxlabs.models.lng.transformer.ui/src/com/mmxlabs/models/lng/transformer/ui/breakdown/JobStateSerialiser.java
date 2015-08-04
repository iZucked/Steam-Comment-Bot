package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.transformer.ui.BreadthOptimiser;
import com.mmxlabs.models.lng.transformer.ui.BreadthOptimiser.JobState;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public final class JobStateSerialiser {

	public static void save(final Collection<BreadthOptimiser.JobState> states, final File f) throws Exception {
		System.out.println("Saving state " + f.getAbsolutePath());
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
			oos.writeInt(states.size());
			for (final JobState s : states) {
				oos.writeObject(s);
			}
		}

	}

	public static List<BreadthOptimiser.JobState> load(final IOptimisationData data, final File f) throws Exception {
		System.out.println("Loading state " + f.getAbsolutePath());
		final Map<Integer, ISequenceElement> elementCache = new HashMap<>();
		for (final ISequenceElement e : data.getSequenceElements()) {
			if (elementCache.put(e.getIndex(), e) != null) {
				assert false;
			}
		}

		final List<BreadthOptimiser.JobState> states = new LinkedList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
			int numStates = ois.readInt();

			JobState obj = null;
			while (numStates-- > 0 && (obj = (JobState) ois.readObject()) != null) {
				fixStates(data, elementCache, obj);
				states.add(obj);
			}
		}
		return states;
	}

	private static void fixStates(final IOptimisationData data, final Map<Integer, ISequenceElement> elementCache, final JobState obj) {
		final int[][] persistedSequences = obj.persistedSequences;
		// Could be null if this object has been saved twice to the same object stream. Second loading of the object will have already had the rawSequences recreated and the persistedSequences array
		// nulled out.
		if (persistedSequences != null) {
			final IModifiableSequences sequences = new ModifiableSequences(data.getResources());
			for (int i = 0; i < persistedSequences.length; ++i) {
				final IModifiableSequence s = sequences.getModifiableSequence(i);
				for (int j = 0; j < persistedSequences[i].length; ++j) {
					s.add(elementCache.get(persistedSequences[i][j]));
				}
			}
			obj.rawSequences = sequences;
			obj.persistedSequences = null;
		}
	}

}
