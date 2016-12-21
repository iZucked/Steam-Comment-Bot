/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.PortVisitLatenessType;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.DetailedCargoType;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;
import com.mmxlabs.scheduler.optimiser.fitness.components.portcost.IPortCostAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

/**
 * Exporter for getting out the details of {@link IPortVisitEvent}
 * 
 * @author hinton
 * 
 */
public class VisitEventExporter extends BaseAnnotationExporter {
	@Inject
	private IPortSlotProvider portSlotProvider;
	@Inject
	private IPortTypeProvider portTypeProvider;
	@Inject
	private IPortSlotEventProvider portSlotEventProvider;

	@Inject
	private CargoTypeUtil cargoTypeUtil;

	private final HashMap<IPortSlot, CargoAllocation> allocations = new HashMap<IPortSlot, CargoAllocation>();
	private Port lastPortVisited = null;

	@Override
	public void init() {
		allocations.clear();
	}

	@Override
	public Event export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations) {

		// "element" represents an IPortSlot
		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot == null) {
			return null;
		}

		final Port ePort = modelEntityMap.getModelObject(slot.getPort(), Port.class);
		if (ePort == null) {
			// Port maybe null for e.g. DES Purchases.
			// return null;
		}

		PortVisit portVisit = null;

		lastPortVisited = ePort;
		CargoAllocation eAllocation = null;
		if (slot instanceof IDischargeOption || slot instanceof ILoadOption) {

			final SlotVisit sv = factory.createSlotVisit();
			final SlotAllocation slotAllocation = factory.createSlotAllocation();
			sv.setSlotAllocation(slotAllocation);
			slotAllocation.setSlotAllocationType(slot instanceof ILoadOption ? SlotAllocationType.PURCHASE : SlotAllocationType.SALE);
			output.getSlotAllocations().add(slotAllocation);
			// TODO this will have to look at market-generated slots.
			final Slot optSlot = modelEntityMap.getModelObject(slot, Slot.class);
			if (optSlot instanceof SpotSlot) {
				slotAllocation.setSpotMarket(((SpotSlot) optSlot).getMarket());
			}
			slotAllocation.setSlot(optSlot);
			portVisit = sv;

			// Output allocation info
			// TODO: Break up IAllocationAnnotation in separate instances for the load and discharge.
			// TODO: Break up IAllocationAnnotation to pull out fuel use as a separate chunk.
			final ICargoValueAnnotation allocation = (ICargoValueAnnotation) annotations.get(SchedulerConstants.AI_cargoValueAllocationInfo);

			eAllocation = allocations.get(slot);

			if (eAllocation == null) {
				eAllocation = scheduleFactory.createCargoAllocation();
				for (final IPortSlot allocationSlot : allocation.getSlots()) {
					allocations.put(allocationSlot, eAllocation);
				}

				output.getCargoAllocations().add(eAllocation);

				@NonNull
				final DetailedCargoType type = cargoTypeUtil.getDetailedCargoType(allocation.getSlots());
				
				CargoType cargoType = null;
				switch (type) {
				case DES_PURCHASE:
					cargoType = CargoType.DES;
					break;
				case DIVERTABLE_DES_PURCHASE:
					cargoType = CargoType.DES;
					break;
				case DIVERTABLE_FOB_SALE:
					cargoType = CargoType.FOB;
					break;
				case FOB_SALE:
					cargoType = CargoType.FOB;
					break;
				case SHIPPED:
					cargoType = CargoType.FLEET;
					break;
				case OPEN_DES_SALE:
				case OPEN_FOB_PURCHASE:
				case UNKNOWN:
				default:
					break;

				}
				assert cargoType != null;
				eAllocation.setCargoType(cargoType);
			}
			eAllocation.getSlotAllocations().add(slotAllocation);

			final int pricePerMMBTu = allocation.getSlotPricePerMMBTu(slot);
			slotAllocation.setPrice(OptimiserUnitConvertor.convertToExternalPrice(pricePerMMBTu));
			slotAllocation.setVolumeTransferred(OptimiserUnitConvertor.convertToExternalVolume(allocation.getCommercialSlotVolumeInM3(slot)));
			slotAllocation.setVolumeValue(OptimiserUnitConvertor.convertToExternalFixedCost(allocation.getSlotValue(slot)));
			slotAllocation.setEnergyTransferred(OptimiserUnitConvertor.convertToExternalVolume(allocation.getCommercialSlotVolumeInMMBTu(slot)));
			slotAllocation.setCv(OptimiserUnitConvertor.convertToExternalConversionFactor(allocation.getSlotCargoCV(slot)));
			slotAllocation.setPhysicalVolumeTransferred(OptimiserUnitConvertor.convertToExternalVolume(allocation.getPhysicalSlotVolumeInM3(slot)));
			slotAllocation.setPhysicalEnergyTransferred(OptimiserUnitConvertor.convertToExternalVolume(allocation.getPhysicalSlotVolumeInMMBTu(slot)));

			sv.setSlotAllocation(slotAllocation);
			slotAllocation.setCargoAllocation(eAllocation);

			final IPortVisitEvent event = (IPortVisitEvent) annotations.get(SchedulerConstants.AI_visitInfo);

			if (event != null) {
				sv.getFuels().addAll(super.createFuelQuantities(event));
			}

		} else if (slot instanceof IVesselEventPortSlot) {
			if (slot instanceof IGeneratedCharterOutVesselEventPortSlot) {
				// GCO logic
				final GeneratedCharterOut generatedCharterOutEvent = factory.createGeneratedCharterOut();
				final IGeneratedCharterOutVesselEvent event = ((IGeneratedCharterOutVesselEventPortSlot) slot).getVesselEvent();
				generatedCharterOutEvent.setRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(event.getHireOutRevenue()));

				portVisit = generatedCharterOutEvent;
				portSlotEventProvider.addEventToPortSlot(slot, GeneratedCharterOut.class, generatedCharterOutEvent);

			} else {
				// final ICharterOutPortSlot cslot = (ICharterOutPortSlot) slot;
				final VesselEvent event = modelEntityMap.getModelObject(slot, VesselEvent.class);
				if (event == null)
					return null;
				final VesselEventVisit vev;
				if (event instanceof CharterOutEvent) {
					final CharterOutEvent charterOut = (CharterOutEvent) event;
					// filter out the charter out slots at the start port (these
					// will have duration zero anyway)
					if (ePort != charterOut.getEndPort()) {
						return null;
					}
					// final CharterOutVisit cov = factory.createCharterOutVisit();
					// vev = cov;
					// cov.setCharterOut(charterOut);
					vev = factory.createVesselEventVisit();
				} else {
					vev = factory.createVesselEventVisit();
				}
				vev.setVesselEvent(event);
				portVisit = vev;
			}
		} else {

			final PortType portType = portTypeProvider.getPortType(element);
			if (portType == PortType.Start) {
				final StartEvent startEvent = ScheduleFactory.eINSTANCE.createStartEvent();

				final IPortVisitEvent event = (IPortVisitEvent) annotations.get(SchedulerConstants.AI_visitInfo);
				if (event != null) {
					startEvent.getFuels().addAll(super.createFuelQuantities(event));
				}

				portVisit = startEvent;
			} else if (portType == PortType.End) {
				final EndEvent endEvent = ScheduleFactory.eINSTANCE.createEndEvent();

				final IPortVisitEvent event = (IPortVisitEvent) annotations.get(SchedulerConstants.AI_visitInfo);
				if (event != null) {
					endEvent.getFuels().addAll(super.createFuelQuantities(event));
				}

				portVisit = endEvent;
			} else {
				portVisit = factory.createPortVisit();
			}
		}

		portVisit.setPort(ePort);

		final IPortVisitEvent visitEvent = (IPortVisitEvent) annotations.get(SchedulerConstants.AI_visitInfo);

		assert visitEvent != null : "Every sequence element should have a visit event associated with it";

		portVisit.setStart(modelEntityMap.getDateFromHours(visitEvent.getStartTime(), slot.getPort()));
		portVisit.setEnd(modelEntityMap.getDateFromHours(visitEvent.getEndTime(), slot.getPort()));

		final ICapacityAnnotation capacityViolationAnnotation = (ICapacityAnnotation) annotations.get(SchedulerConstants.AI_capacityViolationInfo);
		if (capacityViolationAnnotation != null) {
			final Collection<ICapacityEntry> capacityViolations = capacityViolationAnnotation.getEntries();
			for (final ICapacityEntry violation : capacityViolations) {
				com.mmxlabs.models.lng.schedule.CapacityViolationType type = null;
				final CapacityViolationType x = violation.getType();
				switch (x) {
				case FORCED_COOLDOWN:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.FORCED_COOLDOWN;
					break;
				case MAX_DISCHARGE:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.MAX_DISCHARGE;
					break;
				case MIN_DISCHARGE:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.MIN_DISCHARGE;
					break;
				case MAX_LOAD:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.MAX_LOAD;
					break;
				case MIN_LOAD:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.MIN_LOAD;
					break;
				case MAX_HEEL:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.MAX_HEEL;
					break;
				case VESSEL_CAPACITY:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.VESSEL_CAPACITY;
					break;
				case LOST_HEEL:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.LOST_HEEL;
					break;
				}

				final long volume = OptimiserUnitConvertor.convertToExternalVolume(violation.getVolume());
				portVisit.getViolations().put(type, volume);
			}
		}
		final ILatenessAnnotation latenessAnnotation = (ILatenessAnnotation) annotations.get(SchedulerConstants.AI_latenessInfo);
		if (latenessAnnotation != null) {
			final PortVisitLateness portVisitLateness = ScheduleFactory.eINSTANCE.createPortVisitLateness();
			PortVisitLatenessType type = null;
			final Interval interval = latenessAnnotation.getIntervalWithoutFlex();
			switch (interval) {
			case PROMPT:
				type = PortVisitLatenessType.PROMPT;
				break;
			case MID_TERM:
				type = PortVisitLatenessType.MID_TERM;
				break;
			case BEYOND:
				type = PortVisitLatenessType.BEYOND;
				break;
			}
			portVisitLateness.setType(type);
			portVisitLateness.setLatenessInHours(latenessAnnotation.getlatenessWithoutFlex());
			portVisit.setLateness(portVisitLateness);
		}

		final IPortCostAnnotation cost = (IPortCostAnnotation) annotations.get(SchedulerConstants.AI_portCostInfo);
		if (cost != null) {
			portVisit.setPortCost(OptimiserUnitConvertor.convertToExternalFixedCost(cost.getPortCost()));
		}

		// Handle FOB/DES stuff
		if (eAllocation != null) {
			// FOB/DES can only be a two element pairing
			if (eAllocation.getSlotAllocations().size() == 2) {

				// Two elements - must be load and discharge, order undefined
				SlotAllocation loadAllocation = null;
				SlotAllocation dischargeAllocation = null;

				for (final SlotAllocation slotAllocation : eAllocation.getSlotAllocations()) {
					final Slot s = slotAllocation.getSlot();
					if (s instanceof LoadSlot) {
						if (loadAllocation != null) {
							throw new IllegalStateException("Multiple load slots found in LD cargo");
						}
						loadAllocation = slotAllocation;
					} else if (s instanceof DischargeSlot) {
						if (dischargeAllocation != null) {
							throw new IllegalStateException("Multiple discharge slots found in LD cargo");
						}
						dischargeAllocation = slotAllocation;
					}
				}

				assert loadAllocation != null;
				assert dischargeAllocation != null;

				if (((LoadSlot) loadAllocation.getSlot()).isDESPurchase()) {
					loadAllocation.getSlotVisit().setPort(dischargeAllocation.getSlotVisit().getPort());
					loadAllocation.getSlotVisit().setStart(dischargeAllocation.getSlotVisit().getStart());
					loadAllocation.getSlotVisit().setEnd(dischargeAllocation.getSlotVisit().getEnd());
				} else if (((DischargeSlot) dischargeAllocation.getSlot()).isFOBSale()) {
					dischargeAllocation.getSlotVisit().setPort(loadAllocation.getSlotVisit().getPort());
					dischargeAllocation.getSlotVisit().setStart(loadAllocation.getSlotVisit().getStart());
					dischargeAllocation.getSlotVisit().setEnd(loadAllocation.getSlotVisit().getEnd());
				}
			}
		}

		// set up hire cost
		portVisit.setCharterCost(OptimiserUnitConvertor.convertToExternalFixedCost(visitEvent.getCharterCost()));

		// Output allocation info
		// TODO: Break up IAllocationAnnotation in separate instances for the load and discharge.
		// TODO: Break up IAllocationAnnotation to pull out fuel use as a separate chunk.
		final IHeelLevelAnnotation heelLevel = (IHeelLevelAnnotation) annotations.get(SchedulerConstants.AI_heelLevelInfo);

		if (heelLevel != null) {
			assert visitEvent.getStartHeelInM3() == heelLevel.getStartHeelInM3();
			assert visitEvent.getEndHeelInM3() == heelLevel.getEndHeelInM3();
		}

		portVisit.setHeelAtStart(OptimiserUnitConvertor.convertToExternalVolume(visitEvent.getStartHeelInM3()));
		portVisit.setHeelAtEnd(OptimiserUnitConvertor.convertToExternalVolume(visitEvent.getEndHeelInM3()));

		return portVisit;
	}

	/**
	 * @return
	 */
	public Port getLastPortVisited() {
		return lastPortVisited;
	}
}
