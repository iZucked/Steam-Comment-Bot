/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.portcost.IPortCostAnnotation;
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
	public Event export(final ISequenceElement element, final Map<String, Object> annotations) {

		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot == null)
			return null;

		final Port ePort = entities.getModelObject(slot.getPort(), Port.class);
		if (ePort == null)
			return null;

		PortVisit portVisit = null;

		lastPortVisited = ePort;

		if (slot instanceof IDischargeOption || slot instanceof ILoadOption) {

			final SlotVisit sv = factory.createSlotVisit();
			final SlotAllocation slotAllocation = factory.createSlotAllocation();
			sv.setSlotAllocation(slotAllocation);

			output.getSlotAllocations().add(slotAllocation);
			// TODO this will have to look at market-generated slots.
			slotAllocation.setSlot(entities.getModelObject(slot, Slot.class));
			portVisit = sv;

			// Output allocation info.
			final IAllocationAnnotation allocation = (IAllocationAnnotation) annotations.get(SchedulerConstants.AI_volumeAllocationInfo);
			CargoAllocation eAllocation = allocations.get(slot);

			if (eAllocation == null) {
				eAllocation = scheduleFactory.createCargoAllocation();
				allocations.put(allocation.getLoadOption(), eAllocation);
				allocations.put(allocation.getDischargeOption(), eAllocation);
				// eAllocation.setLoadPriceM3(allocation.getLoadM3Price());
				// eAllocation.setDischargePriceM3(allocation.getDischargeM3Price());
				// eAllocation.setFuelVolume(allocation.getFuelVolume() / Calculator.ScaleFactor); // yes? no?
				eAllocation.setDischargeVolume((int) (allocation.getDischargeVolume() / Calculator.ScaleFactor));
				eAllocation.setLoadVolume((int) (allocation.getLoadVolume() / Calculator.ScaleFactor));
				// eAllocation.setSequence();

				output.getCargoAllocations().add(eAllocation);
			}
			if (slot instanceof ILoadOption)
				eAllocation.setLoadAllocation(slotAllocation);
			else
				eAllocation.setDischargeAllocation(slotAllocation);

			sv.setSlotAllocation(slotAllocation);
			slotAllocation.setCargoAllocation(eAllocation);

		} else if (slot instanceof IVesselEventPortSlot) {
			// final ICharterOutPortSlot cslot = (ICharterOutPortSlot) slot;
			final VesselEvent event = entities.getModelObject(slot, VesselEvent.class);
			if (event == null)
				return null;
			final VesselEventVisit vev;
			if (event instanceof CharterOutEvent) {
				final CharterOutEvent charterOut = (CharterOutEvent) event;
				// filter out the charter out slots at the start port (these
				// will have duration zero anyway)
				if (ePort != charterOut.getEndPort())
					return null;
				// final CharterOutVisit cov = factory.createCharterOutVisit();
				// vev = cov;
				// cov.setCharterOut(charterOut);
				vev = factory.createVesselEventVisit();
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
			portVisit.setStart(entities.getDateFromHours(visitEvent.getStartTime() - 1));
		} else {
			portVisit.setStart(entities.getDateFromHours(visitEvent.getStartTime()));
		}
		if (slot.getPortType() == PortType.End) {
			portVisit.setEnd(entities.getDateFromHours(visitEvent.getEndTime() + 1));
		} else {
			portVisit.setEnd(entities.getDateFromHours(visitEvent.getEndTime()));
		}
		
		final IPortCostAnnotation cost = (IPortCostAnnotation) annotations.get(SchedulerConstants.AI_portCostInfo);
		if (cost != null) {
			portVisit.setPortCost((int) (cost.getPortCost() / Calculator.ScaleFactor));
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
