/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class SequencesToPortSlotsUtils {
	public static @NonNull Collection<ISequenceElement> getAllSequenceElements(ISequences sequences) {
		List<ISequenceElement> allElements = new LinkedList<ISequenceElement>();
		@NonNull
		List<@NonNull IResource> resources = sequences.getResources();
		for (@NonNull IResource resource : resources) {
			allElements.addAll(getSequenceElements(sequences, resource));
		}
		allElements.addAll(sequences.getUnusedElements());
		return allElements;
	}
	
	public static @NonNull Collection<IPortSlot> getAllPortSlots(ISequences sequences, IPortSlotProvider portSlotProvider) {
		Collection<ISequenceElement> allSequenceElements = getAllSequenceElements(sequences);
		return getAllPortSlots(allSequenceElements, portSlotProvider);
	}
	
	public static @NonNull Collection<IPortSlot> getAllPortSlots(@NonNull Collection<ISequenceElement> allSequenceElements, IPortSlotProvider portSlotProvider) {
		@NonNull Collection<IPortSlot> allElements = new LinkedList<>();
		allSequenceElements.forEach(e -> allElements.add(portSlotProvider.getPortSlot(e)));
		return allElements;
	}

	private static Collection<ISequenceElement> getSequenceElements(ISequences sequences, IResource resource) {
		ISequence sequence = sequences.getSequence(resource);
		return Lists.newLinkedList(sequence);
	}
}
