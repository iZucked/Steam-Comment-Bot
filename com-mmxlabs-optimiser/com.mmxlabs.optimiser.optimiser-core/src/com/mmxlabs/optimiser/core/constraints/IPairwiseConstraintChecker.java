/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.constraints;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * A restricted form of constraint checker interface for determining whether a given pair of elements can feasibly be scheduled in a given order on a particular resource. Whilst not every constraint
 * can be expressed meaningfully in this form, it is useful to allow sequence builders and move generators to rapidly determine whether particular decisions are 'safe'.
 * 
 * Doesn't really <em>need</em> to extend {@link IConstraintChecker}, but it seems that for now it's a sensible association, as the idea is to provide a quick indicator of infeasibility.
 * 
 * @author hinton
 * 
 */
public interface IPairwiseConstraintChecker extends IConstraintChecker {
	/**
	 * Return whether or not {@code second} can immediately follow {@code first} in a sequence served by {@code resource}.
	 * 
	 * @param first
	 * @param second
	 * @param resource
	 * @return
	 */
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource);

	/**
	 * Return a string explaining why these two items cannot follow one another
	 * 
	 * @param first
	 * @param second
	 * @param resource
	 * @return
	 */
	@Nullable
	public String explain(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource);
}
