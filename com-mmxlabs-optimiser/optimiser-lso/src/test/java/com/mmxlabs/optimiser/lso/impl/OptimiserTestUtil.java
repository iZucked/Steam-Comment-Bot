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

	/**
	 * Create a {@link IModifiableSequence} from an arbitrary list of inputs of
	 * the same type.
	 * 
	 * @param <T>
	 *            Sequence element type
	 * @param elements
	 * @return
	 */
	public static <T> IModifiableSequence<T> makeSequence(final T... elements) {
		final List<T> elementsList = CollectionsUtil.makeArrayList(elements);
		return new ListModifiableSequence<T>(elementsList);
	}

	public static IResource makeResource() {
		return new IResource() {
		};
	}

	/**
	 * Create a {@link IModifiableSequences} instance with only a single
	 * {@link IModifiableSequence}
	 * 
	 * @param <T>
	 * @param res
	 * @param elements
	 * @return
	 */
	public static <T> IModifiableSequences<T> makeSequences(
			final IResource res, final T... elements) {

		final Map<IResource, IModifiableSequence<T>> map = CollectionsUtil
				.makeHashMap(res, makeSequence(elements));

		final IModifiableSequences<T> sequences = new ModifiableSequences<T>(
				Collections.singletonList(res), map);

		return sequences;
	}
}
