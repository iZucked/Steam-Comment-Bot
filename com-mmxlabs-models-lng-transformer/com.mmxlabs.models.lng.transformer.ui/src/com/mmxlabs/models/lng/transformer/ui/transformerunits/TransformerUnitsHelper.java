package com.mmxlabs.models.lng.transformer.ui.transformerunits;

import java.util.Map;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;

public class TransformerUnitsHelper {
	
	/**
	 * Remove any unused slots from sequences
	 * @param multiStateResult
	 * @return
	 */
	public static IMultiStateResult removeExcessSlots(IMultiStateResult multiStateResult) {
		for (NonNullPair<ISequences, Map<String, Object>> sequenceMap : multiStateResult.getSolutions()) {
			ModifiableSequences mSequences = new ModifiableSequences(sequenceMap.getFirst());
			mSequences.getModifiableUnusedElements().clear();
			sequenceMap.setFirst(mSequences);
		}
		return multiStateResult;
	}
}
