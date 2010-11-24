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
import scenario.schedule.Journey;

import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * @author hinton
 * 
 */
public class JourneyEventExporter extends BaseAnnotationExporter {
	private IPortSlotProvider<ISequenceElement> portSlotProvider;
	private EMap<Slot, Journey> journeys;

	@Override
	public void init() {
		this.portSlotProvider = annotatedSolution
				.getContext()
				.getOptimisationData()
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);

		this.journeys = output.getSlotJourneys();
	}

	@Override
	public void exportAnnotation(final ISequenceElement element,
			final Object annotation) {
		assert annotation instanceof IJourneyEvent;
		@SuppressWarnings("unchecked")
		final IJourneyEvent<ISequenceElement> event = (IJourneyEvent<ISequenceElement>) annotation;
		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot != null) {
			final Slot eSlot = entities.getModelObject(slot, Slot.class);
			if (eSlot != null) {
				final Journey journey = factory.createJourney();
				journeys.put(eSlot, journey);

				journey.setStartTime(entities.getDateFromHours(event
						.getStartTime()));
				journey.setEndTime(entities.getDateFromHours(event.getEndTime()));
				final EList<FuelQuantity> fuelUsage = journey.getFuelUsage();
				for (final FuelComponent fc : FuelComponent
						.getTravelFuelComponents()) {
					final long consumption = event.getFuelConsumption(fc,
							fc.getDefaultFuelUnit());
					final long cost = event.getFuelCost(fc);
					final FuelQuantity fq = createFuelQuantity(fc, consumption, cost);
					
					fuelUsage.add(fq);
				}
			}
		}
	}
}
