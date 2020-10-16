/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export.exporters;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.PortVisitLatenessType;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.FuelExportHelper;
import com.mmxlabs.models.lng.transformer.export.IPortSlotEventProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.DetailedCargoType;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.LatenessRecord;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * Exporter for getting out the details of {@link IPortVisitEvent}
 * 
 * @author hinton
 * 
 */
public class VisitEventExporter {

	@Inject
	private ModelEntityMap modelEntityMap;
	
	@Inject
	private IPortSlotEventProvider portSlotEventProvider;

	@Inject
	private CargoTypeUtil cargoTypeUtil;

	private final HashMap<IPortSlot, CargoAllocation> allocations = new HashMap<>();

	public PortVisit export(final PortDetails details, final VolumeAllocatedSequence sequence, final IAnnotatedSolution annotatedSolution, final Schedule output) {

		final IPortSlot slot = details.getOptions().getPortSlot();

		if (slot == null) {
			return null;
		}

		VoyagePlanRecord vpr = sequence.getVoyagePlanRecord(slot);

		final Port ePort = modelEntityMap.getModelObject(slot.getPort(), Port.class);
		if (ePort == null) {
			// Port maybe null for e.g. DES Purchases.
			// return null;
		}

		PortVisit portVisit = null;

		CargoAllocation eAllocation = null;
		boolean isNonShippedCargo = false;
		boolean checkEndEventLateness = false;
		if (slot instanceof IDischargeOption || slot instanceof ILoadOption) {

			final SlotVisit sv = ScheduleFactory.eINSTANCE.createSlotVisit();
			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			sv.setSlotAllocation(slotAllocation);
			slotAllocation.setSlotAllocationType(slot instanceof ILoadOption ? SlotAllocationType.PURCHASE : SlotAllocationType.SALE);
			output.getSlotAllocations().add(slotAllocation);
			// TODO this will have to look at market-generated slots.
			final Slot<?> optSlot = modelEntityMap.getModelObject(slot, Slot.class);
			if (optSlot instanceof SpotSlot) {
				slotAllocation.setSpotMarket(((SpotSlot) optSlot).getMarket());
			}
			slotAllocation.setSlot(optSlot);
			portVisit = sv;

			// Output allocation info
			final ICargoValueAnnotation allocation = vpr.getCargoValueAnnotation();

			eAllocation = allocations.get(slot);

			final DetailedCargoType type = cargoTypeUtil.getDetailedCargoType(allocation.getSlots());
			{
				switch (type) {
				case DES_PURCHASE:
				case DIVERTIBLE_DES_PURCHASE:
				case DIVERTIBLE_FOB_SALE:
				case FOB_SALE:
				case OPEN_DES_SALE:
				case OPEN_FOB_PURCHASE:
					isNonShippedCargo = true;
					break;
				default:
					break;
				}
			}
			if (eAllocation == null) {
				eAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
				for (final IPortSlot allocationSlot : allocation.getSlots()) {
					allocations.put(allocationSlot, eAllocation);
				}

				output.getCargoAllocations().add(eAllocation);

				CargoType cargoType = null;
				switch (type) {
				case DES_PURCHASE:
				case DIVERTIBLE_DES_PURCHASE:
					cargoType = CargoType.DES;
					break;
				case DIVERTIBLE_FOB_SALE:
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

		} else if (slot instanceof IVesselEventPortSlot) {
			if (slot instanceof IGeneratedCharterOutVesselEventPortSlot) {
				// GCO logic
				final GeneratedCharterOut generatedCharterOutEvent = ScheduleFactory.eINSTANCE.createGeneratedCharterOut();
				final IGeneratedCharterOutVesselEvent event = ((IGeneratedCharterOutVesselEventPortSlot) slot).getVesselEvent();
				final CharterOutMarket ecom = modelEntityMap.getModelObject(event.getCharterOutMarket(), CharterOutMarket.class);
				generatedCharterOutEvent.setCharterOutMarket(ecom);
				generatedCharterOutEvent.setRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(event.getHireOutRevenue()));
				portVisit = generatedCharterOutEvent;
				portSlotEventProvider.addEventToPortSlot(slot, GeneratedCharterOut.class, generatedCharterOutEvent);
			} else if (slot instanceof IGeneratedCharterLengthEventPortSlot) {
				final CharterLengthEvent charterLengthEvent = ScheduleFactory.eINSTANCE.createCharterLengthEvent();
				portVisit = charterLengthEvent;
				portSlotEventProvider.addEventToPortSlot(slot, CharterLengthEvent.class, charterLengthEvent);

			} else {
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
					vev = ScheduleFactory.eINSTANCE.createVesselEventVisit();
				} else {
					vev = ScheduleFactory.eINSTANCE.createVesselEventVisit();
				}
				vev.setVesselEvent(event);
				portVisit = vev;
			}
		} else {
			final PortType portType = slot.getPortType();
			if (portType == PortType.Start) {
				final StartEvent startEvent = ScheduleFactory.eINSTANCE.createStartEvent();
				portVisit = startEvent;
			} else if (portType == PortType.End) {
				final EndEvent endEvent = ScheduleFactory.eINSTANCE.createEndEvent();
				portVisit = endEvent;
				checkEndEventLateness = true;
			} else {
				portVisit = ScheduleFactory.eINSTANCE.createPortVisit();
			}
		}

		if (portVisit instanceof FuelUsage) {
			final FuelUsage fuelUsage = (FuelUsage) portVisit;
			fuelUsage.getFuels().addAll(exportFuelData(details));
		}

		portVisit.setPort(ePort);

		final int startTime = vpr.getPortTimesRecord().getSlotTime(slot);
		final int endTime = startTime + (isNonShippedCargo ? 0 : vpr.getPortTimesRecord().getSlotDuration(slot));

		portVisit.setStart(modelEntityMap.getDateFromHours(startTime, slot.getPort()));
		// Note, end port may be different for CO event!
		portVisit.setEnd(modelEntityMap.getDateFromHours(endTime, slot.getPort()));
		{
			final Collection<CapacityViolationType> capacityViolations = vpr.getCapacityViolations(slot);
			for (final CapacityViolationType violation : capacityViolations) {
				com.mmxlabs.models.lng.schedule.CapacityViolationType type = null;
				final CapacityViolationType x = violation;
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
				case MIN_HEEL:
					type = com.mmxlabs.models.lng.schedule.CapacityViolationType.MIN_HEEL;
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

				final long volume = OptimiserUnitConvertor.convertToExternalVolume(vpr.getCapacityViolationVolume(violation, slot));
				portVisit.getViolations().put(type, volume);
			}

			PortVisitLateness portVisitLateness = null;
			if (vpr.getLatenessWithoutFlex(slot) > 0 || vpr.getLatenessWithFlex(slot) > 0) {
				final Interval interval = vpr.getLatenessInterval(slot);
				portVisitLateness = createLatenessObject(interval);
				portVisitLateness.setLatenessInHours(vpr.getLatenessWithoutFlex(slot));
				portVisit.setLateness(portVisitLateness);
			}
			if (checkEndEventLateness) {
				LatenessRecord record = sequence.getMaxDurationLatenessRecord();
				if (record != null) {
					if (portVisitLateness == null) {
						portVisitLateness = createLatenessObject(record.interval);
						portVisit.setLateness(portVisitLateness);
					}
					int l = portVisitLateness.getLatenessInHours();
					portVisitLateness.setLatenessInHours(l + record.latenessWithoutFlex);
				}

			}
		}

		portVisit.setPortCost(OptimiserUnitConvertor.convertToExternalFixedCost(details.getPortCosts()));

		// Handle FOB/DES stuff
		// FOB/DES can only be a two element pairing
		if (isNonShippedCargo && eAllocation != null && eAllocation.getSlotAllocations().size() == 2) {

			// Two elements - must be load and discharge, order undefined
			SlotAllocation loadAllocation = null;
			SlotAllocation dischargeAllocation = null;

			for (final SlotAllocation slotAllocation : eAllocation.getSlotAllocations()) {
				final Slot<?> s = slotAllocation.getSlot();
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

		return portVisit;

	}

	private PortVisitLateness createLatenessObject(final Interval interval) {
		final PortVisitLateness portVisitLateness;
		portVisitLateness = ScheduleFactory.eINSTANCE.createPortVisitLateness();
		PortVisitLatenessType type = null;
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
		return portVisitLateness;
	}

	private List<FuelQuantity> exportFuelData(final PortDetails details) {
		if (details.getOptions().getVessel() == null) {
			return Collections.emptyList();
		}
		return FuelExportHelper.exportFuelData(details, details.getOptions().getVessel(), FuelExportHelper.portFuelComponentNames, PortDetails::getFuelConsumption, PortDetails::getFuelUnitPrice,
				modelEntityMap);
	}
}
