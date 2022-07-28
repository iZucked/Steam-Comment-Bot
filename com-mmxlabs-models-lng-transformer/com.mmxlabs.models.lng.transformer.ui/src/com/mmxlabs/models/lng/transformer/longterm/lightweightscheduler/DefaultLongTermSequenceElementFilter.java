/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

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
//						.getVesselCharter(e.getKey())
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