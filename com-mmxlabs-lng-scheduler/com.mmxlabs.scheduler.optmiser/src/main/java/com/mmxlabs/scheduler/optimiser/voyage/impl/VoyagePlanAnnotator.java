package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.List;

import com.mmxlabs.optimiser.IAnnotatedSequence;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.events.impl.DischargeEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.IdleEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.JourneyEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.LoadEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.PortVisitEventImpl;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlanAnnotator;

/**
 * The {@link VoyagePlanAnnotator} annotates a {@link IAnnotatedSequence} object
 * from a sequence of {@link IVoyagePlan}s.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class VoyagePlanAnnotator<T> implements IVoyagePlanAnnotator<T> {

	private IPortSlotProvider<T> portSlotProvider;

	private FuelComponent[] idleFuelComponents = new FuelComponent[] {
			FuelComponent.IdleBase, FuelComponent.IdleNBO };
	private FuelComponent[] travelFuelComponents = new FuelComponent[] {
			FuelComponent.Base, FuelComponent.NBO,
			FuelComponent.Base_Supplemental, FuelComponent.FBO };

	@Override
	public void annonateFromVoyagePlan(final IResource resource,
			final List<IVoyagePlan> plans,
			final IAnnotatedSequence<T> annotatedSequence) {

		// TODO: Get start time
		final int startTime = 0;

		// current time will be incremented after each element
		int currentTime = startTime;

		Object lastElement = null;
		for (final IVoyagePlan plan : plans) {
			for (final Object e : plan.getSequence()) {

				if (e instanceof IPortDetails) {

					final IPortDetails details = (IPortDetails) e;
					final IPortSlot currentPortSlot = details.getPortSlot();

					// Get element from port slot provider
					final T element = getPortSlotProvider().getElement(
							currentPortSlot);

					final int visitDuration = details.getVisitDuration();

					// Add port annotations
					final PortVisitEventImpl<T> visit;
					if (currentPortSlot instanceof ILoadSlot) {
						LoadEventImpl<T> load = new LoadEventImpl<T>();
						load.setLoadVolume(plan.getLoadVolume());
						// TODO: Check unit vs. actual
						load.setPurchasePrice(plan.getPurchaseCost());

						visit = load;
					} else if (currentPortSlot instanceof IDischargeSlot) {
						DischargeEventImpl<T> discharge = new DischargeEventImpl<T>();

						discharge.setDischargeVolume(plan.getDischargeVolume());

						// TODO: Check unit vs. actual
						discharge.setSalesPrice(plan.getSalesRevenue());

						visit = discharge;

					} else {
						visit = new PortVisitEventImpl<T>();
					}

					visit.setName("visit");
					visit.setSequenceElement(element);
					visit.setPortSlot(currentPortSlot);

					visit.setDuration(visitDuration);

					annotatedSequence.setAnnotation(element,
							SchedulerConstants.AI_visitInfo, visit);

					// Consecutive voyage plans will end and start with the same
					// element. The above code will overwrite the previous
					// entry, however here we need to be careful not to
					// increment the current time, otherwise we will have a
					// "hole" in the schedule equal to visitDuration. We can't
					// skip generation of the end element as the final voyage
					// plan will, by definition, not have a follow on plan. We
					// also can't skip as the details of the visit will be
					// different. If the last element is a LoadEvent, there will
					// be no load volume set, as that is calculated are part of
					// the next voyage plan.
					if (e != lastElement) {
						visit.setStartTime(currentTime);
						visit.setEndTime(currentTime + visitDuration);
						currentTime += visitDuration;
					} else {
						// Back-out visit duration set in the previous
						// iteration.
						visit.setStartTime(currentTime - visitDuration);
						visit.setEndTime(currentTime);
					}
				} else if (e instanceof IVoyageDetails<?>) {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					final IVoyageDetails<T> details = (IVoyageDetails) e;

					final IVoyageOptions options = details.getOptions();

					final IPortSlot prevPortSlot = options.getFromPortSlot();
					final IPortSlot currentPortSlot = options.getToPortSlot();

					// Get element from port slot provider
					final T element = getPortSlotProvider().getElement(
							currentPortSlot);

					final int travelTime = details.getTravelTime();

					final JourneyEventImpl<T> journey = new JourneyEventImpl<T>();

					journey.setName("journey");
					journey.setFromPort(prevPortSlot.getPort());
					journey.setToPort(currentPortSlot.getPort());
					journey.setSequenceElement(element);
					journey.setStartTime(currentTime);
					journey.setEndTime(currentTime + travelTime);
					journey.setDistance(options.getDistance());

					journey.setDuration(travelTime);

					journey.setSpeed(details.getSpeed());

					for (final FuelComponent fuel : travelFuelComponents) {
						final long consumption = details
								.getFuelConsumption(fuel);
						final long cost = Calculator.costFromConsumption(
								consumption, details.getFuelUnitPrice(fuel));

						journey.setFuelConsumption(fuel, consumption);
						journey.setFuelCost(fuel, cost);
					}

					journey.setVesselState(details.getOptions()
							.getVesselState());

					annotatedSequence.setAnnotation(element,
							SchedulerConstants.AI_journeyInfo, journey);

					currentTime += travelTime;

					final int idleTime = details.getIdleTime();

					final IdleEventImpl<T> idle = new IdleEventImpl<T>();
					idle.setName("idle");
					idle.setPort(currentPortSlot.getPort());
					idle.setStartTime(currentTime);
					idle.setDuration(idleTime);
					idle.setEndTime(currentTime + idleTime);
					idle.setSequenceElement(element);

					for (final FuelComponent fuel : idleFuelComponents) {
						final long consumption = details
								.getFuelConsumption(fuel);
						final long cost = Calculator.costFromConsumption(
								consumption, details.getFuelUnitPrice(fuel));

						idle.setFuelConsumption(fuel, consumption);
						idle.setFuelCost(fuel, cost);
					}
					idle.setVesselState(details.getOptions().getVesselState());

					annotatedSequence.setAnnotation(element,
							SchedulerConstants.AI_idleInfo, idle);

					currentTime += idleTime;

				} else {
					throw new IllegalStateException("Unexpected element " + e);
				}

				lastElement = e;
			}
		}
	}

	public void setPortSlotProvider(final IPortSlotProvider<T> portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
	}

	public IPortSlotProvider<T> getPortSlotProvider() {
		return portSlotProvider;
	}
}
