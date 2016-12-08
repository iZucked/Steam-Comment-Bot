/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.events.impl.DischargeEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.GeneratedCharterOutEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.IdleEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.JourneyEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.LoadEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.PortVisitEventImpl;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.portcost.impl.PortCostAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlanAnnotator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link VoyagePlanAnnotator} annotates a {@link IAnnotatedSequence} object from a sequence of {@link VoyagePlan}s.
 * 
 * @author Simon Goodall
 * 
 * @param Sequence
 *            element type.
 */
public class VoyagePlanAnnotator implements IVoyagePlanAnnotator {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	private final @NonNull FuelComponent @NonNull [] idleFuelComponents = FuelComponent.getIdleFuelComponents();
	private final @NonNull FuelComponent @NonNull [] travelFuelComponents = FuelComponent.getTravelFuelComponents();

	public void annotateFromScheduledSequences(final @NonNull VolumeAllocatedSequences scheduledSequences, final @NonNull IAnnotatedSolution solution) {
		for (final VolumeAllocatedSequence s : scheduledSequences) {
			annotateFromScheduledSequence(s, solution);
		}
		// add volume annotations
	}

	public void annotateFromScheduledSequence(final @NonNull VolumeAllocatedSequence scheduledSequence, final @NonNull IAnnotatedSolution solution) {
		annotateFromVoyagePlan(scheduledSequence, solution);
	}

	/**
	 */
	@Override
	public void annotateFromVoyagePlan(final @NonNull VolumeAllocatedSequence scheduledSequence, final @NonNull IAnnotatedSolution solution) {
		final VoyagePlanIterator vpi = new VoyagePlanIterator(scheduledSequence);
		long currentHeelInM3 = 0;
		boolean firstObject = true;
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(scheduledSequence.getResource());
		final boolean recordHeel = !(vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE);
		while (vpi.hasNextObject()) {
			final boolean resetCurrentHeel = firstObject || vpi.nextObjectIsStartOfPlan();
			firstObject = false;

			final Object e = vpi.nextObject();
			//
			final int currentTime = vpi.getCurrentTime();
			final VoyagePlan currentPlan = vpi.getCurrentPlan();

			assert currentPlan.getLNGFuelVolume() >= 0;
			assert currentPlan.getStartingHeelInM3() >= 0;
			assert currentPlan.getRemainingHeelInM3() >= 0;

			final long charterRatePerDay = currentPlan.getCharterInRatePerDay();
			if (resetCurrentHeel) {
				currentHeelInM3 = currentPlan.getStartingHeelInM3();
			}

			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot currentPortSlot = details.getOptions().getPortSlot();

				// Get element from port slot provider
				final ISequenceElement element = getPortSlotProvider().getElement(currentPortSlot);

				final int visitDuration = details.getOptions().getVisitDuration();

				// Add port annotations
				final PortVisitEventImpl visit;
				if (currentPortSlot instanceof ILoadSlot) {
					final LoadEventImpl load = new LoadEventImpl();
					visit = load;
				} else if (currentPortSlot instanceof IDischargeSlot) {
					final DischargeEventImpl discharge = new DischargeEventImpl();
					visit = discharge;
				} else if (currentPortSlot instanceof IVesselEventPortSlot) {
					visit = new PortVisitEventImpl();
				} else {
					visit = new PortVisitEventImpl();
				}

				// TODO: Set on the port details object
				// Port Costs
				{
					final long cost = details.getPortCosts();
					solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_portCostInfo, new PortCostAnnotation(cost));
				}

				for (final FuelComponent fuel : FuelComponent.values()) {
					for (final FuelUnit unit : FuelUnit.values()) {
						final long consumption = details.getFuelConsumption(fuel, unit);

						visit.setFuelConsumption(fuel, unit, consumption);
						if (unit == fuel.getPricingFuelUnit()) {

							final int fuelUnitPrice = details.getFuelUnitPrice(fuel);
							final long cost = Calculator.costFromConsumption(consumption, fuelUnitPrice);

							visit.setFuelCost(fuel, cost);
							visit.setFuelPriceUnit(fuel, unit);
							visit.setFuelUnitPrice(fuel, fuelUnitPrice);
						}
					}
				}
				// final long consumption = details.getFuelConsumption(FuelComponent.Base, FuelUnit.MT);
				// visit.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, consumption);
				// final long cost = Calculator.costFromConsumption(consumption, details.getFuelUnitPrice(FuelComponent.Base));
				// visit.setFuelCost(FuelComponent.Base, cost);
				// visit.setFuelPriceUnit(FuelComponent.Base, FuelComponent.Base.getPricingFuelUnit());
				// visit.setFuelUnitPrice(FuelComponent.Base, details.getFuelUnitPrice(FuelComponent.Base));
				visit.setName("visit");
				visit.setSequenceElement(element);
				visit.setPortSlot(currentPortSlot);

				visit.setDuration(visitDuration);
				visit.setHireCost(Calculator.quantityFromRateTime(charterRatePerDay, visitDuration) / 24);
				solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_visitInfo, visit);

				visit.setStartTime(currentTime); // details.getStartTime()
				visit.setEndTime(currentTime + visitDuration);

				// Heel tracking
				if (recordHeel) {
					final long startHeelInM3 = currentHeelInM3;
					if (currentPortSlot.getPortType() != PortType.End) {

						if (currentPortSlot.getPortType() == PortType.Load) {
							final IAllocationAnnotation allocationAnnotation = scheduledSequence.getAllocationAnnotation(currentPortSlot);

							assert allocationAnnotation.getStartHeelVolumeInM3() >= 0;
							assert allocationAnnotation.getFuelVolumeInM3() >= 0;
							assert allocationAnnotation.getRemainingHeelVolumeInM3() >= 0;

							assert allocationAnnotation.getStartHeelVolumeInM3() == currentPlan.getStartingHeelInM3();
							assert allocationAnnotation.getFuelVolumeInM3() == currentPlan.getLNGFuelVolume();

							// TODO: Probably should be physical here and then ignore the port BOG.
							currentHeelInM3 += allocationAnnotation.getCommercialSlotVolumeInM3(currentPortSlot);
						} else if (currentPortSlot.getPortType() == PortType.Discharge) {
							final IAllocationAnnotation allocationAnnotation = scheduledSequence.getAllocationAnnotation(currentPortSlot);

							assert allocationAnnotation.getStartHeelVolumeInM3() >= 0;
							assert allocationAnnotation.getFuelVolumeInM3() >= 0;
							assert allocationAnnotation.getRemainingHeelVolumeInM3() >= 0;

							assert allocationAnnotation.getStartHeelVolumeInM3() == currentPlan.getStartingHeelInM3();
							assert allocationAnnotation.getFuelVolumeInM3() == currentPlan.getLNGFuelVolume();

							currentHeelInM3 -= allocationAnnotation.getCommercialSlotVolumeInM3(currentPortSlot);
						}
						assert currentHeelInM3 + VoyagePlanner.ROUNDING_EPSILON >= 0;
					} else {
						if (currentPortSlot instanceof EndPortSlot) {
							final EndPortSlot endPortSlot = (EndPortSlot) currentPortSlot;
							// Assert disabled as it is not always possible to arrive with target heel (thus capacity violation should be triggered)
							// assert currentHeelInM3 >= endPortSlot.getTargetEndHeelInM3();
						}
					}
					final long endHeelInM3 = currentHeelInM3;

					visit.setStartHeelInM3(startHeelInM3);
					visit.setEndHeelInM3(endHeelInM3);
				}
			} else if (e instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) e;

				final VoyageOptions options = details.getOptions();

				final IPortSlot prevPortSlot = options.getFromPortSlot();
				final IPortSlot currentPortSlot = options.getToPortSlot();

				// Get element from port slot provider
				final ISequenceElement element = getPortSlotProvider().getElement(currentPortSlot);

				final int travelTime = details.getTravelTime();

				final JourneyEventImpl journey = new JourneyEventImpl();
				journey.setName("journey");
				journey.setFromPort(prevPortSlot.getPort());
				journey.setToPort(currentPortSlot.getPort());
				journey.setSequenceElement(element);

				journey.setStartTime(currentTime);
				journey.setEndTime(currentTime + travelTime);

				journey.setDistance(options.getDistance());
				journey.setRoute(options.getRoute().name());
				journey.setRouteCost(options.getRouteCost());

				journey.setDuration(travelTime);
				journey.setHireCost(Calculator.quantityFromRateTime(charterRatePerDay, travelTime) / 24);
				journey.setSpeed(details.getSpeed());
				if (recordHeel) {

					journey.setStartHeelInM3(currentHeelInM3);
				}
				for (final FuelComponent fuel : travelFuelComponents) {
					for (final FuelUnit unit : FuelUnit.values()) {
						final long consumption = details.getFuelConsumption(fuel, unit) + details.getRouteAdditionalConsumption(fuel, unit);
						journey.setFuelConsumption(fuel, unit, consumption);
						if (unit == fuel.getPricingFuelUnit()) {
							final int fuelUnitPrice = details.getFuelUnitPrice(fuel);
							final long cost = Calculator.costFromConsumption(consumption, fuelUnitPrice);

							journey.setFuelCost(fuel, cost);

							journey.setFuelPriceUnit(fuel, unit);
							journey.setFuelUnitPrice(fuel, fuelUnitPrice);
						}
						if (FuelComponent.isLNGFuelComponent(fuel) && unit == FuelUnit.M3) {
							currentHeelInM3 -= consumption;
						}
					}
				}
				if (recordHeel) {
					assert currentHeelInM3 >= 0;
					journey.setEndHeelInM3(currentHeelInM3);
				}
				journey.setVesselState(details.getOptions().getVesselState());
				// solution.getElementAnnotations().setAnnotation(element,
				// SchedulerConstants.AI_journeyInfo, journey);
				solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_journeyInfo, journey);

				final int idleTime = details.getIdleTime();
				if (details.getOptions().isCharterOutIdleTime()) {
					final GeneratedCharterOutEventImpl charterOut = new GeneratedCharterOutEventImpl();

					charterOut.setName("Generated Charter Out");
					charterOut.setPort(currentPortSlot.getPort());

					charterOut.setStartTime(currentTime + travelTime);
					charterOut.setDuration(idleTime);
					charterOut.setEndTime(currentTime + travelTime + idleTime);
					charterOut.setSequenceElement(element);

					// Calculate revenue
					charterOut.setCharterOutRevenue(Calculator.quantityFromRateTime(details.getOptions().getCharterOutDailyRate(), idleTime) / 24L);
					charterOut.setHireCost(Calculator.quantityFromRateTime(charterRatePerDay, idleTime) / 24);

					if (recordHeel) {
						charterOut.setStartHeelInM3(currentHeelInM3);
						charterOut.setEndHeelInM3(currentHeelInM3);
					}
					solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_generatedCharterOutInfo, charterOut);

				} else {

					final IdleEventImpl idle = new IdleEventImpl();
					idle.setName("idle");
					idle.setPort(currentPortSlot.getPort());

					idle.setStartTime(currentTime + travelTime);
					idle.setDuration(idleTime);
					idle.setHireCost(Calculator.quantityFromRateTime(charterRatePerDay, idleTime) / 24);
					idle.setEndTime(currentTime + travelTime + idleTime);
					idle.setSequenceElement(element);

					idle.setStartHeelInM3(currentHeelInM3);

					for (final FuelComponent fuel : idleFuelComponents) {
						for (final FuelUnit unit : FuelUnit.values()) {
							final long consumption = details.getFuelConsumption(fuel, unit);

							idle.setFuelConsumption(fuel, unit, consumption);
							// Calculate cost on default unit
							if (unit == fuel.getPricingFuelUnit()) {
								final long cost = Calculator.costFromConsumption(consumption, details.getFuelUnitPrice(fuel));
								idle.setFuelCost(fuel, cost);

								idle.setFuelPriceUnit(fuel, unit);
								idle.setFuelUnitPrice(fuel, details.getFuelUnitPrice(fuel));
							}
							if (FuelComponent.isLNGFuelComponent(fuel) && unit == FuelUnit.M3) {
								currentHeelInM3 -= consumption;
							}
						}
					}
					if (recordHeel) {
						assert currentHeelInM3 >= 0;
						idle.setEndHeelInM3(currentHeelInM3);
					}
					idle.setVesselState(details.getOptions().getVesselState());

					// set cooldown and cooldown cost (which has been previously calculated in LNGVoyageCalculator)
					idle.setCooldownPerformed(details.isCooldownPerformed());
					if (details.isCooldownPerformed()) {
						idle.setFuelCost(FuelComponent.Cooldown, currentPlan.getTotalFuelCost(FuelComponent.Cooldown));
					}

					solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_idleInfo, idle);
				}

			} else {
				throw new IllegalStateException("Unexpected element " + e);
			}
		}

	}

	public void setPortSlotProvider(final IPortSlotProvider portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
	}

	public IPortSlotProvider getPortSlotProvider() {
		return portSlotProvider;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

}
