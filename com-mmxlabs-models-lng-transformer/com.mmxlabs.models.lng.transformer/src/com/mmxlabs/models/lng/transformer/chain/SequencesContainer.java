/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequences;

@NonNullByDefault
public final class SequencesContainer {

	private ISequences sequences;

	public SequencesContainer(final ISequences sequences) {
		this.sequences = sequences;
	}

	public ISequences getSequences() {
		return sequences;
	}

	public void setSequences(final ISequences sequences) {
		this.sequences = sequences;
	}
}
