/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.transformerunits;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ISequenceElementFilter;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;

public class TransformerUnitsHelper {

	/**
	 * Remove any unused slots from sequences
	 * 
	 * @param multiStateResult
	 * @return
	 */
	@NonNullByDefault

	public static IMultiStateResult removeExcessSlots(final IMultiStateResult multiStateResult) {
		for (final NonNullPair<ISequences, Map<String, Object>> sequenceMap : multiStateResult.getSolutions()) {
			final ModifiableSequences mSequences = new ModifiableSequences(sequenceMap.getFirst());
			mSequences.getModifiableUnusedElements().clear();
			sequenceMap.setFirst(mSequences);
		}
		return multiStateResult;
	}

	/**
	 * Remove any unused slots from sequences using filter
	 * 
	 * @param multiStateResult
	 * @param filter
	 * @return
	 */
	@NonNullByDefault
	public static IMultiStateResult removeExcessSlots(final IMultiStateResult multiStateResult, final ISequenceElementFilter filter) {
		for (final NonNullPair<ISequences, Map<String, Object>> sequenceMap : multiStateResult.getSolutions()) {
			final ISequences filteredISequences = filter.getFilteredISequences(sequenceMap.getFirst());
			sequenceMap.setFirst(filteredISequences);
		}
		return multiStateResult;
	}

}
