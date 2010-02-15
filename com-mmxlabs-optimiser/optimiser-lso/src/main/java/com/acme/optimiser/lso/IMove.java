package com.acme.optimiser.lso;

import java.util.Collection;

import com.acme.optimiser.IModifiableSequences;
import com.acme.optimiser.IResource;
import com.acme.optimiser.ISequences;

/**
 * Interface defining a Move as used in the Local Search Optimiser.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IMove<T> {

	/**
	 * Returns the collection of {@link IResource}s this move affects.
	 * 
	 * @return
	 */
	Collection<IResource> getAffectedResources();

	/**
	 * Apply the move to the {@link IResources} listed in
	 * {@link #getAffectedResources()}.
	 * 
	 * @param sequences
	 */
	void apply(IModifiableSequences<T> sequences);

	/**
	 * Validate the move parameters are valid for the given {@link ISequences}
	 * object.
	 * 
	 * @return True on valid parameters.
	 */
	boolean validate(ISequences<T> sequences);
}
