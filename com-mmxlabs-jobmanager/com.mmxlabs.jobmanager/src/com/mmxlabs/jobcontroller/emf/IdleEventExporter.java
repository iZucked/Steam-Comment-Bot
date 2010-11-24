/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.jobcontroller.emf;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import scenario.cargo.Slot;
import scenario.schedule.FuelQuantity;
import scenario.schedule.SlotIdle;

import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * Exports idleevent annotations.
 * 
 * @author hinton
 * 
 */
public class IdleEventExporter extends BaseAnnotationExporter {
	private IPortSlotProvider<ISequenceElement> portSlotProvider;
	private EMap<Slot, SlotIdle> idles;

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		this.portSlotProvider = annotatedSolution
				.getContext()
				.getOptimisationData()
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);
		this.idles = output.getSlotIdles();
	}

	@Override
	public void exportAnnotation(final ISequenceElement element,
			final Object annotation) {
		assert annotation instanceof IIdleEvent;

		@SuppressWarnings("unchecked")
		final IIdleEvent<ISequenceElement> event = (IIdleEvent<ISequenceElement>) annotation;
		final IPortSlot slot = portSlotProvider.getPortSlot(element);
		if (slot != null) {
			final Slot eSlot = entities.getModelObject(slot, Slot.class);
			if (eSlot != null) {
				final SlotIdle idle = factory.createSlotIdle();
				idles.put(eSlot, idle);
				
				idle.setStartTime(entities.getDateFromHours(event.getStartTime()));
				idle.setEndTime(entities.getDateFromHours(event.getEndTime()));
				
				final EList<FuelQuantity> fuelUsage = idle.getFuelUsage();
				
				for (final FuelComponent fc : FuelComponent.getIdleFuelComponents()) {
					fuelUsage.add(createFuelQuantity(fc, 
							event.getFuelConsumption(fc, fc.getDefaultFuelUnit()),
							event.getFuelCost(fc)));
				}
			}
		}
	}

}
