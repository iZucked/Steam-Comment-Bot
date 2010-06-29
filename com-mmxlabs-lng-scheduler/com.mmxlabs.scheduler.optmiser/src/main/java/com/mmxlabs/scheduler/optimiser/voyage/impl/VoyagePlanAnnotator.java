package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.events.impl.IdleEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.JourneyEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.PortVisitEventImpl;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlanAnnotator;

/**
 *The {@link VoyagePlanAnnotator} annotates a {@link IAnnotatedSequence} object
 * from a sequence of {@link IVoyagePlan}s.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class VoyagePlanAnnotator<T> implements IVoyagePlanAnnotator<T> {

	private IPortSlotProvider<T> portSlotProvider;

	@Override
	public void annonateFromVoyagePlan(final IResource resource,
			final List<IVoyagePlan> plans,
			final IAnnotatedSequence<T> annotatedSequence) {

		// TODO: Get start time
		final int startTime = 0;

		// current time will be incremented after each element
		int currentTime = startTime;

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
					final PortVisitEventImpl<T> visit = new PortVisitEventImpl<T>();
					visit.setName("visit");
					visit.setSequenceElement(element);
					visit.setPort(currentPortSlot.getPort());
					visit.setStartTime(currentTime);
					visit.setEndTime(currentTime + visitDuration);
					visit.setDuration(visitDuration);

					annotatedSequence.setAnnotation(element,
							SchedulerConstants.AI_visitInfo, visit);

					currentTime += visitDuration;

				} else if (e instanceof IVoyageDetails<?>) {
					@SuppressWarnings("unchecked")
					final IVoyageDetails<T> details = (IVoyageDetails) e;

					final IPortSlot prevPortSlot = details.getOptions()
							.getFromPortSlot();
					final IPortSlot currentPortSlot = details.getOptions()
							.getToPortSlot();

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
					journey.setDistance(details.getOptions().getDistance());

					journey.setDuration(travelTime);
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

					annotatedSequence.setAnnotation(element,
							SchedulerConstants.AI_idleInfo, idle);

					currentTime += idleTime;

				} else {
					throw new IllegalStateException("Unexpected element " + e);
				}
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
