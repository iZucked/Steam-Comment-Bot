/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class OpenSlotExporter extends BaseAnnotationExporter {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Override
	public void init() {

	}

	@Override
	public Event export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations) {

		if (element == null) {
			return null;
		}

		// "element" represents an IPortSlot
		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot == null) {
			return null;
		}
		//
		// final Port ePort = modelEntityMap.getModelObject(slot.getPort(), Port.class);
		// if (ePort == null) {
		// // Port maybe null for e.g. DES Purchases.
		// // return null;
		// }
		//
		if (slot instanceof IDischargeOption || slot instanceof ILoadOption) {
			//
			// final IMarkToMarket market = marketProvider.getMarketForElement(element);
			// if (market == null) {
			// return null;
			// }

			// TODO this will have to look at market-generated slots.
			final Slot optSlot = modelEntityMap.getModelObject(slot, Slot.class);
			if (optSlot instanceof SpotSlot) {
				// Skip spot slots
				return null;
			}

			// final SlotVisit sv = factory.createSlotVisit();
			// final SlotAllocation slotAllocation = factory.createSlotAllocation();
			// sv.setSlotAllocation(slotAllocation);
			//
			// output.getSlotAllocations().add(slotAllocation);
			// slotAllocation.setSlot(optSlot);

			// Output allocation info
			// final IAllocationAnnotation allocation = (IAllocationAnnotation) annotations.get(SchedulerConstants.AI_volumeAllocationInfo);

			final OpenSlotAllocation eAllocation = scheduleFactory.createOpenSlotAllocation();
			eAllocation.setSlot(optSlot);
//			allocations.put(slot, eAllocation);
			output.getOpenSlotAllocations().add(eAllocation);
			// eAllocation.setSlot(slot);
			// sv.setStart(modelEntityMap.getDateFromHours(slot.getTimeWindow().getStart()));
			// sv.setEnd(modelEntityMap.getDateFromHours(slot.getTimeWindow().getStart()));
			//
			// sv.setSlotAllocation(slotAllocation);
			//
			// sv.setPort(ePort);
			// return sv;

		}

		return null;
	}
}
