package com.mmxlabs.scheduler.optmiser.fitness.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;
import com.mmxlabs.scheduler.optmiser.SchedulerConstants;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optmiser.fitness.ISequenceSchedulerAdditionalInfo;
import com.mmxlabs.scheduler.optmiser.providers.IPortProvider;

/**
 * Simple scheduler.
 * 
 * @author Simon Goodall
 * 
 */
public final class SimpleSequenceScheduler<T> implements ISequenceScheduler<T> {

	private final Map<T, ISequenceSchedulerAdditionalInfo> additionalInfos;

	private IElementDurationProvider<T> durationsProvider;

	private ITimeWindowDataComponentProvider timeWindowProvider;

	private IPortProvider portProvider;

	private IMatrixProvider<IPort, Integer> distanceProvider;

	public SimpleSequenceScheduler() {
		additionalInfos = new HashMap<T, ISequenceSchedulerAdditionalInfo>();
	}

	@Override
	public <U> U getAdditionalInformation(final T element,
			final String key, final Class<U> clz) {
		if (additionalInfos.containsKey(element)) {
			return additionalInfos.get(element).get(key, clz);
		}

		return null;
	}

	@Override
	public boolean schedule(final IResource resource,
			final ISequence<T> sequence) {

		// Clear additional infos
		additionalInfos.clear();

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

			final int visitDuration = durationsProvider.getElementDuration(element,
					resource);
			final int idleTime = nextTime - currentTime - travelTime;

			// TODO: Store details
			// --> Store element start/end time
			// --> Store time/distance between elements
			// --> Store "idle" time

			final VisitElementImpl<T> visit = new VisitElementImpl<T>();
			visit.setName("visit");
			visit.setSequenceElement(element);
			visit.setPort(thisPort);
			visit.setStartTime(nextTime);
			visit.setEndTime(nextTime + visitDuration);
			visit.setDuration(visitDuration);

			setAdditionalInformation(element, SchedulerConstants.AI_visitInfo,
					visit);

			final IdleElementImpl<T> idle = new IdleElementImpl<T>();
			idle.setName("idle");
			idle.setPort(thisPort);
			idle.setStartTime(currentTime + travelTime);
			idle.setDuration(idleTime);
			idle.setEndTime(nextTime);
			idle.setSequenceElement(element);

			setAdditionalInformation(element, SchedulerConstants.AI_idleInfo,
					idle);

			if (prevPort != null) {
				final JourneyElementImpl<T> journey = new JourneyElementImpl<T>();

				journey.setName("journey");
				journey.setFromPort(prevPort);
				journey.setToPort(thisPort);
				journey.setSequenceElement(element);
				journey.setStartTime(currentTime);
				journey.setEndTime(currentTime + travelTime);
				journey.setDistance(distance);
				journey.setDuration(travelTime);

				setAdditionalInformation(element,
						SchedulerConstants.AI_journeyInfo, journey);
			}

			// Setup for next iteration
			prevPort = thisPort;

			// Set current time to after this element has finished
			currentTime = nextTime + visitDuration;
		}

		return true;
	}

	private void setAdditionalInformation(final T element, final String key,
			final Object value) {
		final ISequenceSchedulerAdditionalInfo info;
		if (additionalInfos.containsKey(element)) {
			info = additionalInfos.get(element);
		} else {
			info = new SequenceSchedulerAdditionalInfo();
			additionalInfos.put(element, info);
		}
		info.put(key, value);
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
}
