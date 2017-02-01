package com.mmxlabs.scheduler.optimiser.lso.guided;

import com.google.inject.ImplementedBy;
import com.mmxlabs.optimiser.core.ISequences;

@ImplementedBy(GuidedMoveHelperImpl.class)
public interface IGuidedMoveHelper {

	boolean doesMovePassConstraints(final ISequences rawSequences);

}
