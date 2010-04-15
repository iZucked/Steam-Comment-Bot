package com.mmxlabs.optimiser;

import java.util.List;

/**
 * {@link IConstraintChecker} implementations check {@link ISequences} for
 * constraint violations. For example this could be to ensure sequence elements
 * are assigned to particular resources or to enforce rules on sequence element
 * order. Such constraints are assumed to be hard, i.e. a constraint violation
 * means the {@link ISequences} object is in an invalid state and should never
 * be allowed. This differs from a soft constraint which would be an allowed
 * state, but penalised for it. Such soft constraints are likely to be
 * implemented as a Fitness Function.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IConstraintChecker<T> {

	/**
	 * Check the {@link ISequences} object for constraint violations. Returns
	 * true if all constraints are satisfied. Returns false on a constraint
	 * violation.
	 * 
	 * @param sequences
	 * @return Returns true if all constraints are satisfied.
	 */
	boolean checkConstraints(ISequences<T> sequences);

	/**
	 * Check the {@link ISequences} object for constraint violations. Returns
	 * true if all constraints are satisfied. Returns false on a constraint
	 * violation. This version of {@link #checkConstraints} takes an input list
	 * which may be used to store informative messages about any constraint
	 * violations.
	 * 
	 * @param sequences
	 * @param messages
	 *            List which may be used to store constraint violation messages.
	 * @return Returns true if all constraints are satisfied.
	 */
	boolean checkConstraints(ISequences<T> sequences, List<String> messages);
}
