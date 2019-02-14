/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import com.mmxlabs.optimiser.core.ISequences;

public interface ISequenceElementFilter {
	ISequences getFilteredISequences(ISequences input);
}
