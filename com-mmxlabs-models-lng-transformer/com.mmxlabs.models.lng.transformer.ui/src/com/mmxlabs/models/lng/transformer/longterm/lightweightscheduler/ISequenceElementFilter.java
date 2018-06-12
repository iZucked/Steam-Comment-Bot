package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import com.mmxlabs.optimiser.core.ISequences;

public interface ISequenceElementFilter {
	ISequences getFilteredISequences(ISequences input);
}
