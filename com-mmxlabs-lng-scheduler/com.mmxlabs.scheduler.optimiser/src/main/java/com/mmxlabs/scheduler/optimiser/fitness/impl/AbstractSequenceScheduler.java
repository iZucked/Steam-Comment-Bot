package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Abstract {@link ISequenceScheduler} implementation to manage the sequence
 * optimisation given a set of arrival times at each sequence element. This
 * class handles the construction of {@link VoyagePlan}s and uses a
 * {@link IVoyagePlanOptimiser} implementation to make the best route choices.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractSequenceScheduler<T> implements
		ISequenceScheduler<T> {

	private IElementDurationProvider<T> durationsProvider;

	private ITimeWindowDataComponentProvider timeWindowProvider;

	private IPortProvider portProvider;

	private IPortSlotProvider<T> portSlotProvider;

	private IPortTypeProvider<T> portTypeProvider;

	private IVesselProvider vesselProvider;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	private IVoyagePlanOptimiser<T> voyagePlanOptimiser;

	/**
	 * Schedule an {@link ISequence} using the given array of arrivalTimes,
	 * indexed according to sequence elements. These times will be used as the
	 * base arrival time. However is some cases the time between elements may be
	 * too short (i.e. because the vessel is already travelling at max speed).
	 * In such cases, if adjustArrivals is true, then arrival times will be
	 * adjusted in the {@link VoyagePlan}s. Otherwise null will be returned.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	public List<VoyagePlan> schedule(final IResource resource,
			final ISequence<T> sequence, final int[] arrivalTimes,
			final boolean adjustArrivals) {

		final IVessel vessel = vesselProvider.getVessel(resource);

		// Get start time
		final int startTime = arrivalTimes[0];

		// current time will be incremented after each element
		int currentTime = startTime;

		final List<VoyagePlan> voyagePlans = new LinkedList<VoyagePlan>();
		final List<Object> currentSequence = new ArrayList<Object>(5);

		IPort prevPort = null;
		IPortSlot prevPortSlot = null;
		PortType prevPortType = null;
		VesselState vesselState = VesselState.Ballast;
		VoyageOptions previousOptions = null;

		voyagePlanOptimiser.setVessel(vessel);

		boolean useNBO = false;

		int prevVisitDuration = 0;
		final Iterator<T> itr = sequence.iterator();
		for (int idx = 0; itr.hasNext(); ++idx) {
			final T element = itr.next();

			final IPort thisPort = portProvider.getPortForElement(element);
			final IPortSlot thisPortSlot = portSlotProvider
					.getPortSlot(element);

			// If this is the first port, then this will be null and there will
			// be no voyage to plan.
			if (prevPort != null) {

				if (prevPortType == PortType.Load) {
					useNBO = true;
				}

				// Available time, as determined by inputs.
				final int availableTime = arrivalTimes[idx]
						- arrivalTimes[idx - 1] - prevVisitDuration;

				// Get the min NBO travelling speed
				final int nboSpeed = vessel.getVesselClass().getMinNBOSpeed(
						vesselState);

				final VoyageOptions options = new VoyageOptions();

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

				// Enable choices only if NBO could be available.
				if (useNBO) {
					// Note ordering of choices is important = NBO must be set
					// before FBO and Idle choices, otherwise if NBO choice is
					// false, FBO and IdleNBO may still be true if set before
					// NBO

					if (vesselState == VesselState.Ballast) {
						voyagePlanOptimiser
								.addChoice(new NBOTravelVoyagePlanChoice(
										previousOptions, options));
					}

					voyagePlanOptimiser.addChoice(new FBOVoyagePlanChoice(
							options));

					voyagePlanOptimiser.addChoice(new IdleNBOVoyagePlanChoice(
							options));
				}

				// Only add route choice if there is one
				final String[] routeKeys = distanceProvider.getKeys();
				if (routeKeys.length == 1) {
					final int distance = distanceProvider.get(
							IMultiMatrixProvider.Default_Key).get(prevPort,
							thisPort);
					options.setDistance(distance);
					options.setRoute(IMultiMatrixProvider.Default_Key);
				} else {
					voyagePlanOptimiser.addChoice(new RouteVoyagePlanChoice(
							options, routeKeys, distanceProvider));
				}

				currentSequence.add(options);
				previousOptions = options;
			}

			final int visitDuration = durationsProvider.getElementDuration(
					element, resource);

			final PortDetails portDetails = new PortDetails();
			portDetails.setVisitDuration(visitDuration);
			portDetails.setPortSlot(thisPortSlot);

			currentSequence.add(portDetails);

			final PortType portType = portTypeProvider.getPortType(element);
			if (currentSequence.size() > 1 && (portType == PortType.Load)
					|| portType == PortType.CharterOut) {

				currentTime = optimiseSequence(adjustArrivals, currentTime,
						voyagePlans, currentSequence, voyagePlanOptimiser);

				if (currentTime == Integer.MAX_VALUE) {
					// Problem optimising sequence, most likely forbidden to
					// adjust arrival times. Return null to indicate problematic
					// sequence.
					return null;
				}

				// Reset useNBO flag
				useNBO = false;
				previousOptions = null;

				currentSequence.clear();

				currentSequence.add(portDetails);
			}

			// Setup for next iteration
			prevPort = thisPort;
			prevPortSlot = thisPortSlot;
			prevPortType = portType;
			prevVisitDuration = visitDuration;

			// Update vessel state
			if (portType == PortType.Load) {
				vesselState = VesselState.Laden;
			} else if (portType == PortType.Discharge
					|| portType == PortType.CharterOut) {
				vesselState = VesselState.Ballast;
			} else {
				// No change in state
			}
		}

		// TODO: Do we need to run optimiser when we only have a load port here?

		// Populate final plan details
		// if (!currentSequence.isEmpty()) {
		if (currentSequence.size() > 1) {
			currentTime = optimiseSequence(adjustArrivals, currentTime,
					voyagePlans, currentSequence, voyagePlanOptimiser);
			if (currentTime == Integer.MAX_VALUE) {
				// Problem optimising sequence, most likely forbidden to adjust
				// arrival times. Return null to indicate problematic sequence.
				return null;
			}
		}

		return voyagePlans;
	}

	public final int optimiseSequence(final boolean adjustArrivals,
			int currentTime, final List<VoyagePlan> voyagePlans,
			final List<Object> currentSequence,
			final IVoyagePlanOptimiser<T> optimiser) {

		// Run sequencer evaluation
		optimiser.setBasicSequence(currentSequence);
		optimiser.init();
		final VoyagePlan currentPlan = optimiser.optimise();

		final Object[] sequence = currentPlan.getSequence();
		for (int i = 0; i < sequence.length; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof PortDetails) {
				final PortDetails details = (PortDetails) obj;

				details.setStartTime(currentTime);

				// PortDetails can be processed twice when they are on the
				// boundary of the voyage plan.
				// Therefore we need to be careful not to increment the current
				// time twice
				final int visitDuration = details.getVisitDuration();
				if (i != sequence.length - 1) {
					// Not end element so increment by visit duration
					currentTime += visitDuration;
				}

			} else if (obj instanceof VoyageDetails) {
				@SuppressWarnings("unchecked")
				final VoyageDetails<T> details = (VoyageDetails<T>) obj;

				details.setStartTime(currentTime);

				final int availableTime = details.getOptions()
						.getAvailableTime();

				// Take voyage details time as this can be larger than
				// available time e.g. due to reaching max speed.
				final int duration = details.getTravelTime()
						+ details.getIdleTime();

				assert duration >= availableTime;

				if (duration > availableTime) {
					if (adjustArrivals) {
						// TODO: For JMock tests, the voyage calculator
						// is mocked and so does not set any values in
						// voyage details. So we use max to cover up
						// this problem.
						// TODO: Is this comment still relevant?
						currentTime += Math.max(availableTime, duration);
					} else {
						// Time was adjusted, but it's forbidden. Return
						// Integer.MAX_VALUE to say this is a bad sequence.
						return Integer.MAX_VALUE;
					}
				} else {
					currentTime += availableTime;
				}

			} else {
				throw new IllegalStateException("Unexpected element of type "
						+ obj.getClass().getCanonicalName());
			}
		}
		voyagePlans.add(currentPlan);

		// Reset VPO ready for next iteration
		optimiser.reset();

		return currentTime;
	}

	public final IPortSlotProvider<T> getPortSlotProvider() {
		return portSlotProvider;
	}

	public final void setPortSlotProvider(
			final IPortSlotProvider<T> portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
	}

	public final IPortTypeProvider<T> getPortTypeProvider() {
		return portTypeProvider;
	}

	public final void setPortTypeProvider(
			final IPortTypeProvider<T> portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
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

	public final IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public final void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	public IVoyagePlanOptimiser<T> getVoyagePlanOptimiser() {
		return voyagePlanOptimiser;
	}

	public void setVoyagePlanOptimiser(
			final IVoyagePlanOptimiser<T> voyagePlanOptimiser) {
		this.voyagePlanOptimiser = voyagePlanOptimiser;
	}

	/**
	 * Ensures all required data items are present, otherwise an
	 * {@link IllegalStateException} will be thrown.
	 */
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
		if (voyagePlanOptimiser == null) {
			throw new IllegalStateException("No voyage plan optimiser set");
		}
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
		voyagePlanOptimiser = null;
	}
}
