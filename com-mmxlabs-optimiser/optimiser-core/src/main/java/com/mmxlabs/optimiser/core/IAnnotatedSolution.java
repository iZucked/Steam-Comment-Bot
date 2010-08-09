package com.mmxlabs.optimiser.core;


// TODO: Move into opt-core

public interface IAnnotatedSolution<T> {

	/**
	 * Returns the {@link ISequences} associated with this solution
	 * 
	 * @return
	 */
	ISequences<T> getSequences();

	/**
	 * Returns the {@link IAnnotatedSequence} for the given {@link IResource}.
	 * 
	 * @param resource
	 * @return
	 */
	IAnnotatedSequence<T> getAnnotatedSequence(IResource resource);

	/**
	 * Returns the {@link IOptimisationContext} used to create this solution.
	 * 
	 * @return
	 */
	IOptimisationContext<T> getContext();

	/**
	 * Release any internal resources.
	 */
	void dispose();
}
