package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.events.impl.IdleEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.JourneyEventImpl;
import com.mmxlabs.scheduler.optimiser.events.impl.PortVisitEventImpl;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;

/**
 * Simple scheduler.
 * 
 * @author Simon Goodall
 * 
 */
public final class SimpleSequenceScheduler<T> implements ISequenceScheduler<T> {

	private IElementDurationProvider<T> durationsProvider;

	private ITimeWindowDataComponentProvider timeWindowProvider;

	private IPortProvider portProvider;

	private IMatrixProvider<IPort, Integer> distanceProvider;

	@Override
	public boolean schedule(final IResource resource,
			final ISequence<T> sequence, IAnnotatedSequence<T> annotatedSequence) {

		// TODO: Get start time
		final int startTime = 0;

		// current time will be incremented after each element
		int currentTime = startTime;

		IPort prevPort = null;
		for (final T element : sequence) {
			final List<ITimeWindow> timeWindows = timeWindowProvider
					.getTimeWindows(element);

			// Find earliest start time.
			int timeWindowStart = Integer.MAX_VALUE;
			for (final ITimeWindow window : timeWindows) {
				timeWindowStart = Math.min(timeWindowStart, window.getStart());
			}

			final IPort thisPort = portProvider.getPortForElement(element);

			// TODO: Convert distance to time from speed
			final int distance = (prevPort == null) ? 0 : distanceProvider.get(
					prevPort, thisPort);

			final int travelTime = distance;

			final int left = (timeWindowStart == Integer.MAX_VALUE) ? (0)
					: (timeWindowStart);
			final int right = (prevPort == null) ? (currentTime)
					: (currentTime + travelTime);

			final int nextTime = Math.max(left, right);

			final int visitDuration = durationsProvider.getElementDuration(
					element, resource);
			final int idleTime = nextTime - currentTime - travelTime;

			final PortVisitEventImpl<T> visit = new PortVisitEventImpl<T>();
			visit.setName("visit");
			visit.setSequenceElement(element);
			visit.setPort(thisPort);
			visit.setStartTime(nextTime);
			visit.setEndTime(nextTime + visitDuration);
			visit.setDuration(visitDuration);

			annotatedSequence.setAnnotation(element,
					SchedulerConstants.AI_visitInfo, visit);

			final IdleEventImpl<T> idle = new IdleEventImpl<T>();
			idle.setName("idle");
			idle.setPort(thisPort);
			idle.setStartTime(currentTime + travelTime);
			idle.setDuration(idleTime);
			idle.setEndTime(nextTime);
			idle.setSequenceElement(element);

			annotatedSequence.setAnnotation(element,
					SchedulerConstants.AI_idleInfo, idle);

			if (prevPort != null) {
				final JourneyEventImpl<T> journey = new JourneyEventImpl<T>();

				journey.setName("journey");
				journey.setFromPort(prevPort);
				journey.setToPort(thisPort);
				journey.setSequenceElement(element);
				journey.setStartTime(currentTime);
				journey.setEndTime(currentTime + travelTime);
				journey.setDistance(distance);
				journey.setDuration(travelTime);

				annotatedSequence.setAnnotation(element,
						SchedulerConstants.AI_journeyInfo, journey);
			}

			// Setup for next iteration
			prevPort = thisPort;

			// Set current time to after this element has finished
			currentTime = nextTime + visitDuration;
		}

		return true;
	}

	public final IElementDurationProvider<T> getDurationsProvider() {
		return durationsProvider;
	}

	public final void setDurationsProvider(
			final IElementDurationProvider<T> durationsProvider) {
		this.durationsProvider = durationsProvider;
	}

	public final ITimeWindowDataComponentProvider getTimeWindowProvider() {
		return timeWindowProvider;
	}

	public final void setTimeWindowProvider(
			final ITimeWindowDataComponentProvider timeWindowProvider) {
		this.timeWindowProvider = timeWindowProvider;
	}

	public final IPortProvider getPortProvider() {
		return portProvider;
	}

	public final void setPortProvider(final IPortProvider portProvider) {
		this.portProvider = portProvider;
	}

	public final IMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public final void setDistanceProvider(
			final IMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public void init() {

		// Verify that everything is in place
		if (portProvider == null) {
			throw new IllegalStateException("No port provider set");
		}
		if (distanceProvider == null) {
			throw new IllegalStateException("No port distance provider set");
		}
		if (timeWindowProvider == null) {
			throw new IllegalStateException("No time window provider set");
		}
		if (durationsProvider == null) {
			throw new IllegalStateException("No port durations provider set");
		}
	}

	public void dispose() {

		portProvider = null;
		timeWindowProvider = null;
		distanceProvider = null;
		durationsProvider = null;
	}
}
