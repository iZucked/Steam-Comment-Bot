/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
	public Event export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations) {

		if (element == null) {
			return null;
		}

		// "element" represents an IPortSlot
		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot == null) {
			return null;
		}
		if (slot instanceof IDischargeOption || slot instanceof ILoadOption) {

			final Slot optSlot = modelEntityMap.getModelObject(slot, Slot.class);
			if (optSlot instanceof SpotSlot) {
				// Skip spot slots
				return null;
			}

			final OpenSlotAllocation eAllocation = scheduleFactory.createOpenSlotAllocation();
			eAllocation.setSlot(optSlot);
			output.getOpenSlotAllocations().add(eAllocation);
		}

		return null;
	}
}
