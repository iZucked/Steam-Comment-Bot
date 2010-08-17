package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider.PortType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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

	private IPortSlotProvider<T> portSlotProvider;

	private IPortTypeProvider<T> portTypeProvider;

	private IVesselProvider vesselProvider;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	ILNGVoyageCalculator<T> voyageCalculator;

	@Override
	public List<IVoyagePlan> schedule(final IResource resource,
			final ISequence<T> sequence) {

		final IVessel vessel = vesselProvider.getVessel(resource);

		// TODO: Get start time
		final int startTime = 0;

		// current time will be incremented after each element
		int currentTime = startTime;
		int estimatedTime = currentTime;

		final List<IVoyagePlan> voyagePlans = new LinkedList<IVoyagePlan>();
		// VoyagePlan currentPlan = new VoyagePlan();
		// voyagePlans.add(currentPlan);
		final List<Object> currentSequence = new ArrayList<Object>(5);

		IPort prevPort = null;
		IPortSlot prevPortSlot = null;
		PortType prevPortType = null;
		VesselState vesselState = VesselState.Ballast;

		VoyagePlanOptimiser<T> optimiser = new VoyagePlanOptimiser<T>();
		optimiser.setVessel(vessel);
		optimiser.setVoyageCalculator(voyageCalculator);

		for (final T element : sequence) {
			final List<ITimeWindow> timeWindows = timeWindowProvider
					.getTimeWindows(element);

			// Find earliest start time.
			// TODO: No time windows means time window is Integer.MAX_VALUE -
			// not really sure if this is a sane thing to use.
			int timeWindowStart = Integer.MAX_VALUE;
			for (final ITimeWindow window : timeWindows) {
				timeWindowStart = Math.min(timeWindowStart, window.getStart());
			}

			final IPort thisPort = portProvider.getPortForElement(element);
			final IPortSlot thisPortSlot = portSlotProvider
					.getPortSlot(element);

			final int distance = (prevPort == null) ? 0 : distanceProvider.get(
					IMultiMatrixProvider.Default_Key).get(prevPort, thisPort);

			if (prevPort != null) {
				final int availableTime = Math.max(0, timeWindowStart
						- estimatedTime);

				// TODO: Populate this value from somewhere
				final int nboSpeed = vessel.getVesselClass().getMinNBOSpeed(
						vesselState);

				boolean useNBO = prevPortType != PortType.Other;

				final VoyageOptions options = new VoyageOptions();
				options.setDistance(distance);
				options.setVessel(vessel);
				options.setFromPortSlot(prevPortSlot);
				options.setToPortSlot(thisPortSlot);
				options.setNBOSpeed(nboSpeed);
				options.setVesselState(vesselState);
				options.setAvailableTime(availableTime);

				// Determined by voyage plan optimiser
				options.setUseNBOForTravel(useNBO);
				options.setUseFBOForSupplement(false);
				options.setUseNBOForIdle(false);
				options.setRoute(IMultiMatrixProvider.Default_Key);

				if (useNBO) {
					// Note ordering of choices is important = NBO must be set
					// before FBO and Idle choices, otherwise if NBO choice is
					// false, FBO and IdleNBO may still be true if set before
					// NBO

					if (vesselState == VesselState.Ballast) {
						optimiser.addChoice(new NBOTravelVoyagePlanChoice(
								options));
					}

					optimiser.addChoice(new FBOVoyagePlanChoice(options));

					optimiser.addChoice(new IdleNBOVoyagePlanChoice(options));
				}

				optimiser.addChoice(new RouteVoyagePlanChoice(options,
						distanceProvider.getKeys(), distanceProvider));

				// final VoyageDetails<T> voyageDetails = new
				// VoyageDetails<T>();
				// voyageDetails.setStartTime(currentTime);
				// voyageDetails.setOptions(options);
				//
				// voyageCalculator.calculateVoyageFuelRequirements(options,
				// voyageDetails);

				currentSequence.add(options);

				// // Take voyage details time as this can be larger than
				// available
				// // time e.g. due to reaching max speed.
				// int duration = voyageDetails.getTravelTime()
				// + voyageDetails.getIdleTime();
				//
				// assert duration >= availableTime;
				//
				// // TODO: For JMock tests, the voyage calculator is mocked and
				// so
				// // does not set any values in voyage details. So we use max
				// to
				// // cover up this problem.
				// currentTime += Math.max(availableTime, duration);
				estimatedTime += availableTime;
			}

			final int visitDuration = durationsProvider.getElementDuration(
					element, resource);

			final PortDetails portDetails = new PortDetails();
			// portDetails.setStartTime(currentTime);
			portDetails.setVisitDuration(visitDuration);
			portDetails.setPortSlot(thisPortSlot);

			currentSequence.add(portDetails);

			final PortType portType = portTypeProvider.getPortType(element);
			if (currentSequence.size() > 1 && portType == PortType.Load) {

				optimiser.setBasicSequence(currentSequence);
				optimiser.init();
				IVoyagePlan currentPlan = optimiser.optimise();

				// TODO: set arrival times

				for (Object obj : currentPlan.getSequence()) {
					if (obj instanceof IPortDetails) {
						IPortDetails details = (IPortDetails) obj;
						details.setStartTime(currentTime);

						// Set current time to after this element has finished
						currentTime += details.getVisitDuration();
						;

					} else if (obj instanceof IVoyageDetails) {
						IVoyageDetails details = (IVoyageDetails) obj;

						details.setStartTime(currentTime);

						int availableTime = details.getOptions()
								.getAvailableTime();
						// Take voyage details time as this can be larger than
						// available time e.g. due to reaching max speed.
						int duration = details.getTravelTime()
								+ details.getIdleTime();

						assert duration >= availableTime;

						// TODO: For JMock tests, the voyage calculator is
						// mocked and so does not set any values in voyage
						// details. So we use max to cover up this problem.
						currentTime += Math.max(availableTime, duration);

						estimatedTime = currentTime;
					} else {
						throw new IllegalStateException(
								"Unexpected element of type "
										+ obj.getClass().getCanonicalName());
					}
				}
				voyagePlans.add(currentPlan);

				currentSequence.clear();

				currentPlan = new VoyagePlan();
				currentSequence.add(portDetails);

				// TODO: Add a reset method?
				optimiser.reset();
			}

			estimatedTime += visitDuration;

			// Setup for next iteration
			prevPort = thisPort;
			prevPortSlot = thisPortSlot;
			prevPortType = portType;

			// Update vessel state
			if (portType == PortType.Load) {
				vesselState = VesselState.Laden;
			} else if (portType == PortType.Discharge) {
				vesselState = VesselState.Ballast;
			} else {
				// No change in state
			}
		}

		// Populate final plan details
		if (!currentSequence.isEmpty()) {
			optimiser.setBasicSequence(currentSequence);
			optimiser.init();
			IVoyagePlan currentPlan = optimiser.optimise();

			// TODO: set arrival times

			for (Object obj : currentPlan.getSequence()) {
				if (obj instanceof IPortDetails) {
					IPortDetails details = (IPortDetails) obj;
					details.setStartTime(currentTime);

					// Set current time to after this element has finished
					currentTime += details.getVisitDuration();
					;

				} else if (obj instanceof IVoyageDetails) {
					IVoyageDetails details = (IVoyageDetails) obj;

					details.setStartTime(currentTime);

					int availableTime = details.getOptions().getAvailableTime();
					// Take voyage details time as this can be larger than
					// available
					// time e.g. due to reaching max speed.
					int duration = details.getTravelTime()
							+ details.getIdleTime();

					assert duration >= availableTime;

					// TODO: For JMock tests, the voyage calculator is mocked
					// and
					// so
					// does not set any values in voyage details. So we use max
					// to
					// cover up this problem.
					currentTime += Math.max(availableTime, duration);

				} else {
					throw new IllegalStateException(
							"Unexpected element of type "
									+ obj.getClass().getCanonicalName());
				}
			}
			voyagePlans.add(currentPlan);
		}
		optimiser.dispose();

		return voyagePlans;
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

	public final IMultiMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public final void setDistanceProvider(
			final IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public void init() {

		// Verify that everything is in place
		if (portProvider == null) {
			throw new IllegalStateException("No port provider set");
		}
		if (portSlotProvider == null) {
			throw new IllegalStateException("No port slot provider set");
		}
		if (portTypeProvider == null) {
			throw new IllegalStateException("No port type provider set");
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
		if (vesselProvider == null) {
			throw new IllegalStateException("No vessel provider set");
		}
		if (voyageCalculator == null) {
			throw new IllegalStateException("No voyage calculator set");
		}
	}

	public IPortSlotProvider<T> getPortSlotProvider() {
		return portSlotProvider;
	}

	public void setPortSlotProvider(final IPortSlotProvider<T> portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
	}

	public IPortTypeProvider<T> getPortTypeProvider() {
		return portTypeProvider;
	}

	public void setPortTypeProvider(final IPortTypeProvider<T> portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	public ILNGVoyageCalculator<T> getVoyageCalculator() {
		return voyageCalculator;
	}

	public void setVoyageCalculator(
			final ILNGVoyageCalculator<T> voyageCalculator) {
		this.voyageCalculator = voyageCalculator;
	}

	@Override
	public void dispose() {

		durationsProvider = null;
		timeWindowProvider = null;
		portProvider = null;
		portSlotProvider = null;
		portTypeProvider = null;
		vesselProvider = null;
		distanceProvider = null;
		voyageCalculator = null;
	}
}
