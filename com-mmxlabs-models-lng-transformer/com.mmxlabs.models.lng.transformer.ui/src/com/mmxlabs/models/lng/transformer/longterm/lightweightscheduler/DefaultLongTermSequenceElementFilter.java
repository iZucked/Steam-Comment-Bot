/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ISequenceElementFilter;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class DefaultLongTermSequenceElementFilter implements ISequenceElementFilter {
	@Inject
	IPortTypeProvider portTypeProvider;
	
	@Inject
	IPortSlotProvider portSlotProvider;
	
	@Inject
	ISpotMarketSlotsProvider spotSlotsProvider;
	
	@Inject
	IVesselProvider vesselProvider;
	
	@Override
	public ISequences getFilteredISequences(ISequences input) {
//		ModifiableSequences sequences = new ModifiableSequences(input);
////		{
////			Iterator<@NonNull ISequenceElement> iterator = sequences.getModifiableUnusedElements().iterator();
////			while (iterator.hasNext()) {
////				ISequenceElement element = iterator.next();
////				if (!spotSlotsProvider.isSpotMarketSlot(element)) {
////					iterator.remove();
////				}
////			}
////		}
//		{
//			LinkedList<Entry<@NonNull IResource, @NonNull IModifiableSequence>> entriesList =
//					new LinkedList<>(sequences.getModifiableSequences().entrySet());
//			Collections.sort(entriesList, (a,b) -> a.getKey().getName().compareTo(b.getKey().getName()));
//			List<@NonNull IModifiableSequence> virtualSequences = entriesList.stream()
//				.filter(e -> vesselProvider
//						.getVesselAvailability(e.getKey())
//						.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP)
//				.map(e->e.getValue())
//				.collect(Collectors.toList());
//			for (IModifiableSequence s : virtualSequences) {
//				Iterator<@NonNull ISequenceElement> iterator = s.iterator();
//				while (iterator.hasNext()) {
//					ISequenceElement element = iterator.next();
//					if (portTypeProvider.getPortType(element) == PortType.Load ||
//							portTypeProvider.getPortType(element) == PortType.Discharge) {
//						sequences.getModifiableUnusedElements().add(element);
//						iterator.remove();
//					}
//				}
//			}
//		}
//		return sequences;
		return input;
	}
}