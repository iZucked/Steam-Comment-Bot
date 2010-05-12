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
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optmiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optmiser.fitness.ISequenceSchedulerAdditionalInfo;
import com.mmxlabs.scheduler.optmiser.providers.IPortProvider;

/**
 * Simple scheduler.
 * 
 * @author Simon Goodall
 * 
 */
public final class SimpleSequenceScheduler implements ISequenceScheduler {

	private Map<ISequenceElement, ISequenceSchedulerAdditionalInfo> additionalInfos;

	private IElementDurationProvider<ISequenceElement> durationsProvider;

	private ITimeWindowDataComponentProvider timeWindowProvider;

	private IPortProvider portProvider;

	private IMatrixProvider<IPort, Integer> distanceProvider;

	public SimpleSequenceScheduler() {
		additionalInfos = new HashMap<ISequenceElement, ISequenceSchedulerAdditionalInfo>();
	}

	@Override
	public <U> U getAdditionalInformation(final ISequenceElement element,
			final String key, final Class<U> clz) {
		if (additionalInfos.containsKey(element)) {
			return additionalInfos.get(element).get(key, clz);
		}

		return null;
	}

	@Override
	public boolean schedule(final IResource resource,
			final ISequence<ISequenceElement> sequence) {

		// TODO: Get start time
		final int startTime = 0;

		// current time will be incremented after each element
		int currentTime = startTime;

		IPort prevPort = null;
		for (final ISequenceElement element : sequence) {
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

			int visitDuration = durationsProvider.getElementDuration(element,
					resource);
			int idleTime = nextTime - currentTime - travelTime;

			// TODO: Store details
			// --> Store element start/end time
			// --> Store time/distance between elements
			// --> Store "idle" time

			VisitElementImpl visit = new VisitElementImpl();
			visit.setName("visit");
			visit.setSequenceElement(element);
			visit.setPort(thisPort);
			visit.setStartTime(nextTime);
			visit.setEndTime(nextTime + visitDuration);
			visit.setDuration(visitDuration);

			setAdditionalInformation(element, SchedulerConstants.AI_visitInfo,
					visit);

			IdleElementImpl idle = new IdleElementImpl();
			idle.setName("idle");
			idle.setPort(thisPort);
			idle.setStartTime(currentTime + travelTime);
			idle.setDuration(idleTime);
			idle.setEndTime(nextTime);
			idle.setSequenceElement(element);

			setAdditionalInformation(element, SchedulerConstants.AI_idleInfo,
					idle);

			if (prevPort != null) {
				JourneyElementImpl journey = new JourneyElementImpl();

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

	private void setAdditionalInformation(ISequenceElement element, String key,
			Object value) {
		final ISequenceSchedulerAdditionalInfo info;
		if (additionalInfos.containsKey(element)) {
			info = additionalInfos.get(element);
		} else {
			info = new SequenceSchedulerAdditionalInfo();
			additionalInfos.put(element, info);
		}
		info.put(key, value);
	}

	public final Map<ISequenceElement, ISequenceSchedulerAdditionalInfo> getAdditionalInfos() {
		return additionalInfos;
	}

	public final void setAdditionalInfos(
			Map<ISequenceElement, ISequenceSchedulerAdditionalInfo> additionalInfos) {
		this.additionalInfos = additionalInfos;
	}

	public final IElementDurationProvider<ISequenceElement> getDurationsProvider() {
		return durationsProvider;
	}

	public final void setDurationsProvider(
			IElementDurationProvider<ISequenceElement> durationsProvider) {
		this.durationsProvider = durationsProvider;
	}

	public final ITimeWindowDataComponentProvider getTimeWindowProvider() {
		return timeWindowProvider;
	}

	public final void setTimeWindowProvider(
			ITimeWindowDataComponentProvider timeWindowProvider) {
		this.timeWindowProvider = timeWindowProvider;
	}

	public final IPortProvider getPortProvider() {
		return portProvider;
	}

	public final void setPortProvider(IPortProvider portProvider) {
		this.portProvider = portProvider;
	}

	public final IMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public final void setDistanceProvider(
			IMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}
}
