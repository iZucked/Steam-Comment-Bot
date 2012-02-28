/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.Map;

import scenario.cargo.CargoType;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.fleet.CharterOut;
import scenario.fleet.VesselEvent;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;
import scenario.schedule.fleetallocation.AllocatedVessel;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Exporter for getting out the details of {@link IPortVisitEvent}
 * 
 * @author hinton
 * 
 */
public class VisitEventExporter extends BaseAnnotationExporter {
	private IPortSlotProvider portSlotProvider;
	private final HashMap<IPortSlot, CargoAllocation> allocations = new HashMap<IPortSlot, CargoAllocation>();
	private Port lastPortVisited = null;

	@Override
	public void init() {
		this.portSlotProvider = annotatedSolution.getContext().getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		allocations.clear();
	}

	@Override
	public ScheduledEvent export(final ISequenceElement element, final Map<String, Object> annotations, final AllocatedVessel vessel) {

		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot == null)
			return null;

		final Port ePort = entities.getModelObject(slot.getPort(), Port.class);
		if (ePort == null)
			return null;

		PortVisit portVisit = null;

		lastPortVisited = ePort;

		if (slot instanceof IDischargeSlot || slot instanceof ILoadSlot) {
			final SlotVisit sv = factory.createSlotVisit();
			sv.setSlot(entities.getModelObject(slot, Slot.class));
			portVisit = sv;

			// Output allocation info.
			final IAllocationAnnotation allocation = (IAllocationAnnotation) annotations.get(SchedulerConstants.AI_volumeAllocationInfo);

			CargoAllocation eAllocation = allocations.get(slot);

			if (eAllocation == null) {
				eAllocation = scheduleFactory.createCargoAllocation();
				allocations.put(allocation.getLoadSlot(), eAllocation);
				allocations.put(allocation.getDischargeSlot(), eAllocation);

				eAllocation.setCargoType(CargoType.FLEET);

				eAllocation.setLoadSlot(entities.getModelObject(allocation.getLoadSlot(), LoadSlot.class));
				eAllocation.setDischargeSlot(entities.getModelObject(allocation.getDischargeSlot(), Slot.class));

				eAllocation.setLoadDate(entities.getDateFromHours(allocation.getLoadTime()));
				eAllocation.setDischargeDate(entities.getDateFromHours(allocation.getDischargeTime()));

				eAllocation.setLoadPriceM3(allocation.getLoadM3Price());
				eAllocation.setDischargePriceM3(allocation.getDischargeM3Price());
				eAllocation.setFuelVolume(allocation.getFuelVolume() / Calculator.ScaleFactor); // yes? no?
				eAllocation.setDischargeVolume(allocation.getDischargeVolume() / Calculator.ScaleFactor);

				eAllocation.setVessel(vessel);

				output.getCargoAllocations().add(eAllocation);
			}

			sv.setCargoAllocation(eAllocation);
			if (sv.getSlot() instanceof LoadSlot) {
				eAllocation.setLoadSlotVisit(sv);
			} else {
				eAllocation.setDischargeSlotVisit(sv);
			}
		} else if (slot instanceof IVesselEventPortSlot) {
			// final ICharterOutPortSlot cslot = (ICharterOutPortSlot) slot;
			final VesselEvent event = entities.getModelObject(slot, VesselEvent.class);
			if (event == null) return null;
			final VesselEventVisit vev;
			if (event instanceof CharterOut) {
				final CharterOut charterOut = (CharterOut) event;
				// filter out the charter out slots at the start port (these
				// will have duration zero anyway)
				if (ePort != charterOut.getEffectiveEndPort())
					return null;
				final CharterOutVisit cov = factory.createCharterOutVisit();
				vev = cov;
				cov.setCharterOut(charterOut);
			} else {
				vev = factory.createVesselEventVisit();
			}
			vev.setVesselEvent(event);
			portVisit = vev;
		} else {
			portVisit = factory.createPortVisit();
		}

		portVisit.setPort(ePort);

		final IPortVisitEvent visitEvent = (IPortVisitEvent) annotations.get(SchedulerConstants.AI_visitInfo);

		assert visitEvent != null : "Every sequence element should have a visit event associated with it";

		if (slot.getPortType() == PortType.Start) {
			portVisit.setStartTime(entities.getDateFromHours(visitEvent.getStartTime() - 1));
		} else {
			portVisit.setStartTime(entities.getDateFromHours(visitEvent.getStartTime()));
		}
		if (slot.getPortType() == PortType.End) {
			portVisit.setEndTime(entities.getDateFromHours(visitEvent.getEndTime() + 1));
		} else {
			portVisit.setEndTime(entities.getDateFromHours(visitEvent.getEndTime()));
		}
		return portVisit;
	}

	/**
	 * @return
	 */
	public Port getLastPortVisited() {
		return lastPortVisited;
	}
}
