package com.mmxlabs.optimiser.lso.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.impl.ModifiableSequences;

public class OptimiserTestUtil {
	public static IModifiableSequence<Integer> makeSequence(
			final Integer... elements) {
		final List<Integer> elementsList = CollectionsUtil
				.makeArrayList(elements);
		return new ListModifiableSequence<Integer>(elementsList);
	}

	public static IResource makeResource() {
		return new IResource() {
		};
	}

	public static IModifiableSequences<Integer> makeSequences(
			final IResource res, final Integer... elements) {

		final Map<IResource, IModifiableSequence<Integer>> map = CollectionsUtil
				.makeHashMap(res, makeSequence(elements));

		final IModifiableSequences<Integer> sequences = new ModifiableSequences<Integer>(
				Collections.singletonList(res), map);

		return sequences;
	}
}
