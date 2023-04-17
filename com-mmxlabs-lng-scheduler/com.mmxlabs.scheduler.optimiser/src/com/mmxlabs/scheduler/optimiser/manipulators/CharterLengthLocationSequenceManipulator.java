/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.CharterLengthPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ICharterLengthElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

/**
 * 
 */
public class CharterLengthLocationSequenceManipulator implements ISequencesManipulator {

	@Inject
	private IPortSlotProvider portSlotProvider;
	
	@Inject
	private IPortProvider portProvider;
	
	@Inject
	private ICharterLengthElementProvider charterLengthElementProvider;

	@Override
	public void manipulate(@NonNull IModifiableSequences sequences) {
		sequences.getModifiableSequences().entrySet().stream().forEach(entry -> manipulate(entry.getValue()));

	}

	private void manipulate(@NonNull IModifiableSequence sequence) {
		for (int i = sequence.size() - 2; i >= 0; i--) {
			final ISequenceElement current = sequence.get(i);
			final @NonNull IPortSlot currentSlot = portSlotProvider.getPortSlot(current);
			if (currentSlot instanceof CharterLengthPortSlot charterLengthSlot && charterLengthSlot.getPort() == portProvider.getAnywherePort()) {
				final @NonNull ISequenceElement next = sequence.get(i + 1);
				final @NonNull IPortSlot nextSlot = portSlotProvider.getPortSlot(next);
				if(nextSlot.getPort() != portProvider.getAnywherePort()) {
					final ISequenceElement replacementSlot = charterLengthElementProvider.getCharterLengthLocationElement(charterLengthSlot.getVesselEvent(), nextSlot.getPort());
					assert replacementSlot != null;
					sequence.set(i, replacementSlot);
				}
			}
		}
	}

}
