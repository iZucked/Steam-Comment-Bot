/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.EmissionsUtil;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

public class CargoEmissionAccountingReportJSONGenerator {

	public static List<CargoEmissionAccountingReportModelV1> createReportData(final @NonNull ScheduleModel scheduleModel, //
			final @NonNull CIIReferenceData ciiReferenceData, final boolean isPinned, final String scenarioName, //
			final boolean dis2disMode, final boolean includeEvents) {
		final List<CargoEmissionAccountingReportModelV1> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();
		if (schedule == null) {
			return models;
		}
		
		final List<CargoEmissionRecord> cargoEmissionRecords = getObjectToEventsMap(schedule, dis2disMode, includeEvents);
		
		cargoEmissionRecords.forEach( cer -> {
			final CargoEmissionAccountingReportModelV1 model = createCargoAllocationReportData(ciiReferenceData, cer);
			if (model != null) {
				model.scenarioName = scenarioName;
				model.schedule = schedule;
				model.isPinnedFlag = isPinned;
				model.eventID = cer.getName();
				model.otherID = cer.getName();
				models.add(model);
			}
		});

		return models;
	}
	
	private static List<CargoEmissionRecord> getObjectToEventsMap(final Schedule schedule,
			final boolean dis2disMode, final boolean includeEvents){
		final List<CargoEmissionRecord> records = new ArrayList<>();
		for (final Sequence sequence : schedule.getSequences()) {
			final Vessel vessel = ScheduleModelUtils.getVessel(sequence);
			if (vessel == null) {
				break;
			}

			List<Event> events = new ArrayList<>();
			VesselEvent vesselEvent = null;
			CargoAllocation lastCargoAllocation = null;
			for (final Event event : sequence.getEvents()) {
				
				if (dis2disMode) {
					// end condition for discharge to discharge mode
					if (isDischarge(event) && isLastDischarge(event)) {
						events.add(event);
						records.add(new CargoEmissionRecord(vessel, getCargoAllocation(event), null, events));
						events = new ArrayList<>();
						continue;
					}
				} else {
					// end condition for load to load mode
					if (event.getNextEvent() != null && isLoad(event.getNextEvent())) {
						if (lastCargoAllocation != null) {
							events.add(event);
							records.add(new CargoEmissionRecord(vessel, lastCargoAllocation, null, events));
							events = new ArrayList<>();
							continue;
						} else {
							// we found a start sequence
							events.add(event);
							final CargoEmissionRecord firstRecord = new CargoEmissionRecord(vessel, null, null, events);
							firstRecord.vesselStartEvent = true;
							records.add(firstRecord);
							events = new ArrayList<>();
							continue;
						}
					}
					if (isDischarge(event)) {
						lastCargoAllocation = getCargoAllocation(event);
					}
				}
				if (!includeEvents) {
					if (event instanceof final VesselEventVisit vev) {
						vesselEvent = vev.getVesselEvent();
					}
					// end condition for vessel event visit
					if (vesselEvent != null && event instanceof Idle){
						events.add(event);
						records.add(new CargoEmissionRecord(vessel, null, vesselEvent, events));
						vesselEvent = null;
						events = new ArrayList<>();
						continue;
					}
				}
				events.add(event);
				
				if(event instanceof EndEvent) {
					if (dis2disMode) {
						final CargoEmissionRecord lastRecord = new CargoEmissionRecord(vessel, null, null, events);
						lastRecord.vesselEndEvent = true;
						records.add(lastRecord);
					} else {
						if (lastCargoAllocation != null) {
							records.add(new CargoEmissionRecord(vessel, lastCargoAllocation, null, events));
						}
					}
				}
			}
		}
		
		return records;
	}
	
	/**
	 * Check if the event is on the discharge slot
	 * @param event
	 * @return
	 */
	private static boolean isDischarge(final Event event) {
		return (event instanceof final SlotVisit visit && visit.getSlotAllocation() != null && visit.getSlotAllocation().getSlot() instanceof DischargeSlot);
	}
	
	/**
	 * Check if the event is on the last discharge
	 * @param event
	 * @return
	 */
	private static boolean isLastDischarge(final Event event) {
		if (event instanceof final SlotVisit visit && visit.getSlotAllocation() != null && visit.getSlotAllocation().getSlot() instanceof final DischargeSlot ds) {
			final Cargo cargo = ds.getCargo();
			if (cargo != null) {
				return cargo.getSortedSlots().get(cargo.getSortedSlots().size() - 1) == ds;
			}
		}
		return false;
	}
	
	/**
	 * Check if the event is on the load slot
	 * @param allEvents
	 * @return
	 */
	private static boolean isLoad(final Event event) {
		return (event instanceof final SlotVisit visit && visit.getSlotAllocation() != null && visit.getSlotAllocation().getSlot() instanceof LoadSlot);
	}
	
	/**
	 * Get {@link CargoAllocation} for the {@link Event} which should be a {@link SLotVisit}
	 * @param allEvents
	 * @return
	 */
	private static CargoAllocation getCargoAllocation(final Event event) {
		if (event instanceof final SlotVisit visit && visit.getSlotAllocation() != null && visit.getSlotAllocation().getCargoAllocation() != null) {
			return visit.getSlotAllocation().getCargoAllocation();
		}
		throw new IllegalStateException("Cargo emissions report generator: can not find cargo allocation on an event");
	}

	public static CargoEmissionAccountingReportModelV1 createCargoAllocationReportData(//
			final @NonNull CIIReferenceData ciiReferenceData, //
			final CargoEmissionRecord cargoEmissionRecord) {
		final CargoEmissionAccountingReportModelV1 model = new CargoEmissionAccountingReportModelV1();
		final List<Event> events = cargoEmissionRecord.getEvents();
		final Vessel vessel = cargoEmissionRecord.getVessel();
		final CargoAllocation cargoAllocation = cargoEmissionRecord.getCargoAllocation();
		final VesselEvent vesselEvent = cargoEmissionRecord.getVesselEvent();
		model.equivalents.add(events);
		if (cargoAllocation != null) {
			final EList<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
	
			final Optional<SlotAllocation> loadOptional = slotAllocations //
					.stream() //
					.filter(s -> (s.getSlot() instanceof LoadSlot)) //
					.findFirst();
	
			if (loadOptional.isEmpty()) {
				return null;
			}
	
			model.equivalents.add(cargoAllocation);
			
			for (SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
				model.equivalents.add(sa);
				if (sa.getSlot() != null) {
					model.equivalents.add(sa.getSlot());
				}
			}
		} else if (vesselEvent != null){
			model.equivalents.add(vesselEvent);
		}
		
		model.vesselName = vessel.getName();
		model.baseFuelEmission = 0L;
		model.pilotLightEmission = 0L;
		model.totalEmission = 0L;
		model.methaneSlip = 0L;
		LocalDateTime eventStart = null;

		calculatePortEmissions(cargoAllocation, model);

		for (final Event event : events) {
			if (eventStart == null) {
				eventStart = event.getStart().toLocalDateTime();
			}
			model.eventEnd = event.getEnd().toLocalDateTime();
		}

		calculateFuelEmissions(ciiReferenceData, events, model);
		calculateMethaneSlip(events, model, vessel);
		calculateCII(ciiReferenceData, events, model, vessel);

		model.eventStart = eventStart;
		model.totalEmission += model.nbo + model.fbo + model.baseFuelEmission + model.pilotLightEmission + model.upstreamEmission + model.liquefactionEmission + model.methaneSlipEmissionsCO2
				+ model.pipelineEmission;
		return model;
	}

	private static void calculateFuelEmissions(final @NonNull CIIReferenceData ciiReferenceData, //
			final List<Event> events, final CargoEmissionAccountingReportModelV1 model) {
		double baseFuelAccumulator = 0.0;
		double nboAccumulator = 0.0;
		double fboAccumulator = 0.0;
		double pilotLightAccumulator = 0.0;
		for (final Event event : events) {
			if (event instanceof final FuelUsage fuelUsageEvent) {

				for (final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {

					final Fuel fuel = fuelQuantity.getFuel();
					final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
					double er = 0.0;
					if (baseFuel != null) {
						final FuelEmissionReference fer = baseFuel.getEmissionReference();
						if (fer != null) {
							er = fer.getCf();
						}
					}
					final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);

					switch (fuel) {
					case BASE_FUEL:
						if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
							throw new IllegalStateException();
						}
						baseFuelAccumulator += fuelAmount.getQuantity() * er;
						break;
					case PILOT_LIGHT:
						if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
							throw new IllegalStateException();
						}
						pilotLightAccumulator += fuelAmount.getQuantity() * er;
						break;
					case FBO:
						fboAccumulator += EmissionsUtil.consumedCarbonEquivalentEmissionLNG(ciiReferenceData, fuelQuantity);
						break;
					case NBO:
						nboAccumulator += EmissionsUtil.consumedCarbonEquivalentEmissionLNG(ciiReferenceData, fuelQuantity);
						break;
					default:
					}
				}
			}
		}
		model.baseFuelEmission = Math.round(baseFuelAccumulator);
		model.pilotLightEmission = Math.round(pilotLightAccumulator);
		model.nbo = Math.round(nboAccumulator);
		model.fbo = Math.round(fboAccumulator);
	}

	private static void calculateMethaneSlip(final List<Event> events, final CargoEmissionAccountingReportModelV1 model, final Vessel vessel) {
		double methaneSlipAccumulator = 0.0;
		for (final Event event : events) {
			if (event instanceof final SlotVisit sv) {
				final SlotAllocation sa = sv.getSlotAllocation();
				if (sa != null) {
					final Slot<?> slot = sa.getSlot();
					if (slot instanceof LoadSlot) {
						methaneSlipAccumulator += EmissionsUtil.GRAMS_TO_TONS * sa.getPhysicalEnergyTransferred() * vessel.getVesselOrDelegateMethaneSlipRate();
					}
				}
			}
		}
		model.methaneSlip = Math.round(methaneSlipAccumulator);
		model.methaneSlipEmissionsCO2 = EmissionsUtil.METHANE_CO2_EQUIVALENT * model.methaneSlip;
	}

	private static void calculateCII(final @NonNull CIIReferenceData ciiReferenceData, final List<Event> events, final CargoEmissionAccountingReportModelV1 model, final Vessel vessel) {
		double distanceAccumulator = 0.0;
		double extraEmissions = 0.0;
		Year eventYear = null;
		for (final Event event : events) {
			eventYear = Year.from(event.getStart().toLocalDate());
			if (event instanceof final Journey journeyEvent) {
				distanceAccumulator += journeyEvent.getDistance();
			}
			if (event instanceof final StartEvent startEvent) { 
				final VesselCharter vesselCharter = startEvent.getSequence().getVesselCharter();
				if (vesselCharter != null) {
					final CIIStartOptions ciiStartOptions = vesselCharter.getCiiStartOptions();
					if (ciiStartOptions != null) {
						extraEmissions = ciiStartOptions.getYearToDateEmissions();
						distanceAccumulator += ciiStartOptions.getYearToDateDistance();
					}
				}
			}
		}
		final double emission = Math.round(extraEmissions + model.nbo + model.fbo + model.baseFuelEmission + model.pilotLightEmission);
		final double distance = Math.round(distanceAccumulator);
		model.setCII(ciiReferenceData, vessel, emission, distance, eventYear);
	}

	private static void calculatePortEmissions(final CargoAllocation cargoAllocation, final CargoEmissionAccountingReportModelV1 model) {
		model.upstreamEmission = 0L;
		model.pipelineEmission = 0L;
		model.liquefactionEmission = 0L;
		if (cargoAllocation != null) {
			final Slot<?> slot = cargoAllocation.getSlotAllocations().stream().map(it -> it.getSlot()).findAny().orElse(null);
			if (slot != null) {
				final Port port = ScheduleModelUtils.getPortFromSlot(slot);
				if (port != null) {
					final SlotAllocation loadSlotAllocation = ScheduleModelUtils.getLoadAllocation(cargoAllocation);
					if (loadSlotAllocation != null) {
						model.upstreamEmission = Math.round(port.getUpstreamEmissionRate() * loadSlotAllocation.getPhysicalVolumeTransferred());
						model.pipelineEmission = Math.round(port.getPipelineEmissionRate() * loadSlotAllocation.getPhysicalVolumeTransferred());
						model.liquefactionEmission = Math.round(port.getLiquefactionEmissionRate() * loadSlotAllocation.getPhysicalVolumeTransferred());
					}
				}
			}
		}
	}

	public static File jsonOutput(final List<CargoEmissionAccountingReportModelV1> models) {
		final ObjectMapper objectMapper = new ObjectMapper();
		final File file = new File("/temp/emissions.json");
		try {
			objectMapper.writeValue(file, models);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return file;
	}
}
