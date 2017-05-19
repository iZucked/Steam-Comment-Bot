package com.mmxlabs.scheduler.optimiser.insertion;

import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Small state class to hold data that only needs to be computed once rather than each iteration.
 * 
 * @author Simon Goodall
 *
 */
public class SlotInsertionOptimiserInitialState {

	/**
	 * List of compulsary, but unsed slots in original solution. The final solution should not contain any additional slots meeting this criteria.
	 * 
	 */
	public final Set<ISequenceElement> initiallyUnused = new HashSet<>();

	/**
	 * The input solution.
	 * 
	 */
	public ISequences originalRawSequences;

	/**
	 * The input solution with any cargoes linked to the target elements moved to the unused list
	 * 
	 */
	public ISequences startingPointRawSequences;

	/**
	 * Initial metrics against the originalRawSequences.
	 * 
	 */
	public long[] initialMetrics;

}
