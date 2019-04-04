/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequences;

@NonNullByDefault
public interface ISequenceElementFilter {
	ISequences getFilteredISequences(ISequences input);
}
