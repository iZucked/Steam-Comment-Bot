/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.optimiser.core.ISequences;

@NonNullByDefault
public final class SequencesContainer {

	private NonNullPair<ISequences, Map<String, Object>> sequences;

	public SequencesContainer(final NonNullPair<ISequences, Map<String, Object>> sequences) {
		this.sequences = sequences;
	}

	public NonNullPair<ISequences, Map<String, Object>> getSequencesPair() {
		return sequences;
	}

	public ISequences getSequences() {
		return sequences.getFirst();
	}

	public void setSequencesPair(final NonNullPair<ISequences, Map<String, Object>> sequences) {
		this.sequences = sequences;
	}
}
