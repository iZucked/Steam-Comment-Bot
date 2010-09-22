package com.mmxlabs.optimiser.core.constraints;

import com.mmxlabs.optimiser.core.IResource;

/**
 * A restricted form of constraint checker interface for determining whether a given pair of elements
 * can feasibly be scheduled in a given order on a particular resource. Whilst not every constraint
 * can be expressed meaningfully in this form, it is useful to allow sequence builders and move generators
 * to rapidly determine whether particular decisions are 'safe'. 
 * 
 * Doesn't really <em>need</em> to extend {@link IConstraintChecker}, but it seems that
 * for now it's a sensible association, as the idea is to provide a quick indicator of infeasibility.
 * 
 * @author hinton
 *
 * @param <T>
 */
public interface IPairwiseConstraintChecker<T> extends IConstraintChecker<T> {
	/**
	 * Return whether or not {@code second} can immediately follow {@code first} in a sequence
	 * served by {@code resource}.
	 * @param first
	 * @param second
	 * @param resource
	 * @return
	 */
	public boolean checkPairwiseConstraint(T first, T second, IResource resource);
	
	/**
	 * Return a string explaining why these two items cannot follow one another
	 * @param first
	 * @param second
	 * @param resource
	 * @return
	 */
	public String explain(T first, T second, IResource resource);
}
