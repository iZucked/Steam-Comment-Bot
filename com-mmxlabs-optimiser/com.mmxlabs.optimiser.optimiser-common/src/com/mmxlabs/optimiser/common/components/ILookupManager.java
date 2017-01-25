/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * An {@link ILookupManager} records the mapping of ISequenceElement to {@link IResource} and index in the {@link ISequence} (or the {@link ISequences#getUnusedElements()}) list
 *
 */
public interface ILookupManager {

	@Nullable
	Pair<@Nullable IResource, @NonNull Integer> lookup(@NonNull ISequenceElement element);

	void createLookup(@NonNull ISequences sequences);
}
