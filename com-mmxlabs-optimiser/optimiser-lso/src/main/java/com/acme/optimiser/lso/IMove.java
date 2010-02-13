package com.acme.optimiser.lso;

import java.util.Collection;

import com.acme.optimiser.ISequence;
import com.acme.optimiser.ISequences;

public interface IMove {
	
	Collection<ISequence> getAffectedSequences();
	
	void apply(ISequences sequences);
}
