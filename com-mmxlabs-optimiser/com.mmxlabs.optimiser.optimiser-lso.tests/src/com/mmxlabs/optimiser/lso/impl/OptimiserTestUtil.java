/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Resource;

public class OptimiserTestUtil {

	@NonNull
	public static IIndexingContext index = new SimpleIndexingContext();

	/**
	 * Create a {@link IModifiableSequence} from an arbitrary list of inputs of the same type.
	 * 
	 * @param elements
	 * @return
	 */
	@NonNull
	public static IModifiableSequence makeSequence(final int... integers) {
		final List<@NonNull ISequenceElement> elementsList = makeList(integers);
		return new ListModifiableSequence(elementsList);
	}

	public static @NonNull List<@NonNull ISequenceElement> makeList(final int... integers) {
		final @NonNull List<@NonNull ISequenceElement> elementsList = new ArrayList<>();
		for (final int i : integers) {
			elementsList.add(new IntegerElement(i));
		}
		return elementsList;
	}

	/**
	 * Create a {@link IModifiableSequence} from an arbitrary list of inputs of the same type.
	 * 
	 * @param elements
	 * @return
	 */
	@NonNull
	public static IModifiableSequence makeSequence(final @NonNull ISequenceElement...  elements) {
		final List<@NonNull ISequenceElement> elementsList = CollectionsUtil.makeArrayList(elements);
		return new ListModifiableSequence(elementsList);
	}

	@NonNull
	public static IResource makeResource(@NonNull final String name) {
		return new Resource(index, name);
	}

	/**
	 * Create a {@link IModifiableSequences} instance with only a single {@link IModifiableSequence}
	 * 
	 * @param
	 * @param res
	 * @param elements
	 * @return
	 */
	@NonNull
	public static IModifiableSequences makeSequences(@NonNull final IResource res, final ISequenceElement... elements) {

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(res, makeSequence(elements));

		@SuppressWarnings("null")
		final IModifiableSequences sequences = new ModifiableSequences(Collections.singletonList(res), map);

		return sequences;
	}

	@NonNull
	public static IModifiableSequences makeSequences(@NonNull final IResource res, final int... elements) {

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(res, makeSequence(elements));

		@SuppressWarnings("null")
		final IModifiableSequences sequences = new ModifiableSequences(Collections.singletonList(res), map);

		return sequences;
	}
}
