/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.events.impl.DischargeEventImpl;
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
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlanAnnotator;

/**
 * The {@link VoyagePlanAnnotator} annotates a {@link IAnnotatedSequence} object from a sequence of {@link VoyagePlan}s.
 * 
 * @author Simon Goodall
 * 
 * @param Sequence
 *            element type.
 */
public final class VoyagePlanAnnotator implements IVoyagePlanAnnotator {

	private IPortSlotProvider portSlotProvider;

	private final FuelComponent[] idleFuelComponents = FuelComponent.getIdleFuelComponents();
	private final FuelComponent[] travelFuelComponents = FuelComponent.getTravelFuelComponents();

	public void annotateFromScheduledSequences(final ScheduledSequences scheduledSequences, final IAnnotatedSolution solution) {
		for (final ScheduledSequence s : scheduledSequences) {
			annotateFromScheduledSequence(s, solution);
		}
		// add volume annotations
	}

	public void annotateFromScheduledSequence(final ScheduledSequence scheduledSequence, final IAnnotatedSolution solution) {
		annotateFromVoyagePlan(scheduledSequence.getResource(), scheduledSequence.getVoyagePlans(), scheduledSequence.getStartTime(), solution);
	}

	@Override
	public void annotateFromVoyagePlan(final IResource resource, final List<VoyagePlan> plans, final int startTime, final IAnnotatedSolution solution) {
		final VoyagePlanIterator vpi = new VoyagePlanIterator();
		vpi.setVoyagePlans(plans, startTime);

		vpi.reset();

		// for (final VoyagePlan plan : plans) {
		// for (final Object e : plan.getSequence()) {
		int lastTime = startTime;
		while (vpi.hasNextObject()) {
			final Object e = vpi.nextObject();

			final int currentTime = vpi.getCurrentTime();

			assert currentTime >= lastTime;
			lastTime = currentTime;

			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot currentPortSlot = details.getPortSlot();

				// Get element from port slot provider
				final ISequenceElement element = getPortSlotProvider().getElement(currentPortSlot);

				final int visitDuration = details.getVisitDuration();

				// Add port annotations
				final PortVisitEventImpl visit;
				if (currentPortSlot instanceof ILoadSlot) {
					final LoadEventImpl load = new LoadEventImpl();
					// load.setLoadVolume(plan.getLoadVolume());
					// TODO: Check unit vs. actual
					// load.setPurchasePrice(plan.getPurchaseCost());

					visit = load;

					List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MIN_LOAD);
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_LOAD);
					recordCapacityViolation(details, entries, CapacityViolationType.FORCED_COOLDOWN);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

				} else if (currentPortSlot instanceof IDischargeSlot) {
					final DischargeEventImpl discharge = new DischargeEventImpl();

					// discharge.setDischargeVolume(plan.getDischargeVolume());

					// TODO: Check unit vs. actual
					// discharge.setSalesPrice(plan.getSalesRevenue());

					List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MIN_DISCHARGE);
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_DISCHARGE);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

					visit = discharge;

				} else if (currentPortSlot instanceof IVesselEventPortSlot) {

					List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_HEEL);
					recordCapacityViolation(details, entries, CapacityViolationType.FORCED_COOLDOWN);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

					visit = new PortVisitEventImpl();
				} else {
					List<ICapacityEntry> entries = new LinkedList<ICapacityEntry>();
					recordCapacityViolation(details, entries, CapacityViolationType.MAX_HEEL);
					recordCapacityViolation(details, entries, CapacityViolationType.FORCED_COOLDOWN);

					if (!entries.isEmpty()) {
						final ICapacityAnnotation annotation = new CapacityAnnotation(entries);
						solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_capacityViolationInfo, annotation);
					}

					visit = new PortVisitEventImpl();
				}

				visit.setName("visit");
				visit.setSequenceElement(element);
				visit.setPortSlot(currentPortSlot);

				visit.setDuration(visitDuration);

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

				journey.setSpeed(details.getSpeed());

				for (final FuelComponent fuel : travelFuelComponents) {
					for (final FuelUnit unit : FuelUnit.values()) {
						final long consumption = details.getFuelConsumption(fuel, unit) + details.getRouteAdditionalConsumption(fuel, unit);
						journey.setFuelConsumption(fuel, unit, consumption);
						if (unit == fuel.getDefaultFuelUnit()) {
							final long cost = Calculator.costFromConsumption(consumption, details.getFuelUnitPrice(fuel));

							journey.setFuelCost(fuel, cost);
						}
					}
				}

				journey.setVesselState(details.getOptions().getVesselState());

				// solution.getElementAnnotations().setAnnotation(element,
				// SchedulerConstants.AI_journeyInfo, journey);
				solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_journeyInfo, journey);
				final int idleTime = details.getIdleTime();

				final IdleEventImpl idle = new IdleEventImpl();
				idle.setName("idle");
				idle.setPort(currentPortSlot.getPort());

				idle.setStartTime(currentTime + travelTime);
				idle.setDuration(idleTime);
				idle.setEndTime(currentTime + travelTime + idleTime);
				idle.setSequenceElement(element);

				for (final FuelComponent fuel : idleFuelComponents) {
					for (final FuelUnit unit : FuelUnit.values()) {
						final long consumption = details.getFuelConsumption(fuel, unit);

						idle.setFuelConsumption(fuel, unit, consumption);
						// Calculate cost on default unit
						if (unit == fuel.getDefaultFuelUnit()) {
							final long cost = Calculator.costFromConsumption(consumption, details.getFuelUnitPrice(fuel));
							idle.setFuelCost(fuel, cost);
						}
					}
				}
				idle.setVesselState(details.getOptions().getVesselState());

				solution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_idleInfo, idle);

			} else {
				throw new IllegalStateException("Unexpected element " + e);
			}
		}

	}

	public void recordCapacityViolation(PortDetails plan, List<ICapacityEntry> entries, CapacityViolationType cvt) {
		{
			long quantity = plan.getCapacityViolation(cvt);
			if (quantity > 0) {
				final ICapacityEntry e = new CapacityEntry(cvt.getDisplayName(), quantity);
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
}
