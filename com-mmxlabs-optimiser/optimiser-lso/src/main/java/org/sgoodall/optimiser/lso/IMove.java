package org.sgoodall.optimiser.lso;

import java.util.Collection;

import org.sgoodall.optimiser.ISequence;
import org.sgoodall.optimiser.ISequences;

public interface IMove {
	
	Collection<ISequence> getAffectedSequences();
	
	void apply(ISequences sequences);
}
