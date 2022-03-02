/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.components;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * An {@link ILookupManager} records the mapping of ISequenceElement to
 * {@link IResource} and index in the {@link ISequence} (or the
 * {@link ISequences#getUnusedElements()}) list
 *
 */
@NonNullByDefault
public interface ILookupManager {

	@Nullable
	Pair<@Nullable IResource, Integer> lookup(ISequenceElement element);

	/**
	 * Initialise a new lookup table.
	 * 
	 * @param sequences
	 */
	void createLookup(ISequences sequences);

	/**
	 * Update an existing lookup table with the changed resources. If the collection
	 * is null, perform a full initialisation.
	 * 
	 * Note: This method *always* updates the unused list.
	 * 
	 * @param sequences
	 * @param changedResources
	 */
	void updateLookup(ISequences sequences, @Nullable Collection<IResource> changedResources);

	/**
	 * Update an existing lookup table with the changed resources. If the array is
	 * empty/null, perform a full initialisation.
	 * 
	 * Note: This method will *only* update the unused list if null is passed in as
	 * an argument.
	 * 
	 * @param sequences
	 * @param changedResources
	 */
	void updateLookup(ISequences sequences, IResource... changedResources);

	ISequences getRawSequences();
}
