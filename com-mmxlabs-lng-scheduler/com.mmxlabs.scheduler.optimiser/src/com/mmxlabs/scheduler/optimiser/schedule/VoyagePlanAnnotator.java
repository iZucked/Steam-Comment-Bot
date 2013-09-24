/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.events.impl.DischargeEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.GeneratedCharterOutEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.IdleEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.JourneyEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.LoadEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.PortVisitEventImpl;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl.CapacityAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl.CapacityEntry;
import com.mmxlabs.scheduler.optimiser.fitness.components.portcost.impl.PortCostAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlanAnnotator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
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

	@Inject
	private IPortCostProvider portCostProvider;

	@Inject
	private Provider<VoyagePlanIterator> voyagePlanIteratorProvider;

	private final FuelComponent[] idleFuelComponents = FuelComponent.getIdleFuelComponents();
	private final FuelComponent[] travelFuelComponents = FuelComponent.getTravelFuelComponents();

	public void annotateFromScheduledSequences(final ScheduledSequences scheduledSequences, final IAnnotatedSolution solution) {
		for (final ScheduledSequence s : scheduledSequences) {
			annotateFromScheduledSequence(s, solution);
		}
		// add volume annotations
	}

	public void annotateFromScheduledSequence(final ScheduledSequence scheduledSequence, final IAnnotatedSolution solution) {
		annotateFromVoyagePlan(scheduledSequence.getResource(), scheduledSequence.getVoyagePlans(), solution, scheduledSequence.getArrivalTimes());
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void annotateFromVoyagePlan(final IResource resource, final List<VoyagePlan> plans, final IAnnotatedSolution solution, final int[] arrivalTimes) {
		final VoyagePlanIterator vpi = voyagePlanIteratorProvider.get();
		final IVessel vessel = vesselProvider.getVessel(resource);
		vpi.setVoyagePlans(resource, plans, arrivalTimes);

		vpi.reset();

		final ICurve charterCostCurve;
		switch (vessel.getVesselInstanceType()) {
		case SPOT_CHARTER:
			charterCostCurve = vessel.getHourlyCharterInPrice();
			break;
		case TIME_CHARTER:
			charterCostCurve = vessel.getHourlyCharterInPrice();
			break;
		case CARGO_SHORTS:
			charterCostCurve = vessel.getHourlyCharterInPrice();
			break;
		default:
			charterCostCurve = null;
			break;
		}
		final int charterRate = charterCostCurve == null ? 0 : charterCostCurve.getValueAtPoint(arrivalTimes[0]);

		while (vpi.hasNextObject()) {
			final Object e = vpi.nextObject();

			final int currentTime = vpi.getCurrentTime();

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
					// load.setLoadVolume(plan.getLoadVolume());
					// TODO: Check unit vs. actual
					// load.setPurchasePrice(plan.getPurchaseCost());

					visit = load;

					final List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MIN_LOAD);
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_LOAD);
					recordCapacityViolation(details, entries, CapacityViolationType.FORCED_COOLDOWN);
					recordCapacityViolation(details, entries, CapacityViolationType.VESSEL_CAPACITY);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

				} else if (currentPortSlot instanceof IDischargeSlot) {
					final DischargeEventImpl discharge = new DischargeEventImpl();

					// discharge.setDischargeVolume(plan.getDischargeVolume());

					// TODO: Check unit vs. actual
					// discharge.setSalesPrice(plan.getSalesRevenue());

					final List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MIN_DISCHARGE);
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_DISCHARGE);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

					visit = discharge;

				} else if (currentPortSlot instanceof IVesselEventPortSlot) {

					final List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_HEEL);
					recordCapacityViolation(details, entries, CapacityViolationType.FORCED_COOLDOWN);
					recordCapacityViolation(details, entries, CapacityViolationType.VESSEL_CAPACITY);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

					visit = new PortVisitEventImpl();
				} else {
					final List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_HEEL);
					recordCapacityViolation(details, entries, CapacityViolationType.FORCED_COOLDOWN);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

					visit = new PortVisitEventImpl();
				}

				// Port Costs
				{
					final long cost = portCostProvider.getPortCost(currentPortSlot.getPort(), vessel, currentPortSlot.getPortType());

					solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_portCostInfo, new PortCostAnnotation(cost));

				}

				final long consumption = details.getFuelConsumption(FuelComponent.Base);
				visit.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, consumption);
				final long cost = Calculator.costFromConsumption(consumption, details.getFuelUnitPrice(FuelComponent.Base));
				visit.setFuelCost(FuelComponent.Base, cost);
				visit.setFuelPriceUnit(FuelComponent.Base, FuelComponent.Base.getPricingFuelUnit());
				visit.setFuelUnitPrice(FuelComponent.Base, details.getFuelUnitPrice(FuelComponent.Base));
				visit.setName("visit");
				visit.setSequenceElement(element);
				visit.setPortSlot(currentPortSlot);

				visit.setDuration(visitDuration);
				visit.setHireCost(Calculator.quantityFromRateTime(charterRate, visitDuration));
				solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_visitInfo, visit);

				visit.setStartTime(currentTime); // details.getStartTime()
				visit.setEndTime(currentTime + visitDuration);
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
				journey.setRoute(options.getRoute());
				journey.setRouteCost(details.getRouteCost());

				journey.setDuration(travelTime);
				journey.setHireCost(Calculator.quantityFromRateTime(charterRate, travelTime));
				journey.setSpeed(details.getSpeed());

				for (final FuelComponent fuel : travelFuelComponents) {
					for (final FuelUnit unit : FuelUnit.values()) {
						final long consumption = details.getFuelConsumption(fuel, unit) + details.getRouteAdditionalConsumption(fuel, unit);
						journey.setFuelConsumption(fuel, unit, consumption);
						if (unit == fuel.getPricingFuelUnit()) {
							int fuelUnitPrice = details.getFuelUnitPrice(fuel);
							final long cost = Calculator.costFromConsumption(consumption, fuelUnitPrice);

							journey.setFuelCost(fuel, cost);

							journey.setFuelPriceUnit(fuel, unit);
							journey.setFuelUnitPrice(fuel, fuelUnitPrice);
						}
					}
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
					charterOut.setCharterOutRevenue(Calculator.quantityFromRateTime(details.getOptions().getCharterOutHourlyRate(), idleTime));
					charterOut.setHireCost(Calculator.quantityFromRateTime(charterRate, idleTime));
					solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_generatedCharterOutInfo, charterOut);

				} else {

					final IdleEventImpl idle = new IdleEventImpl();
					idle.setName("idle");
					idle.setPort(currentPortSlot.getPort());

					idle.setStartTime(currentTime + travelTime);
					idle.setDuration(idleTime);
					idle.setHireCost(Calculator.quantityFromRateTime(charterRate, idleTime));
					idle.setEndTime(currentTime + travelTime + idleTime);
					idle.setSequenceElement(element);

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
						}
					}
					idle.setVesselState(details.getOptions().getVesselState());

					solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_idleInfo, idle);
				}
			} else {
				throw new IllegalStateException("Unexpected element " + e);
			}
		}

	}

	public void recordCapacityViolation(final PortDetails plan, final List<ICapacityEntry> entries, final CapacityViolationType cvt) {
		{
			final long quantity = plan.getCapacityViolation(cvt);
			if (quantity > 0) {
				final ICapacityEntry e = new CapacityEntry(cvt, quantity);
				entries.add(e);
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
