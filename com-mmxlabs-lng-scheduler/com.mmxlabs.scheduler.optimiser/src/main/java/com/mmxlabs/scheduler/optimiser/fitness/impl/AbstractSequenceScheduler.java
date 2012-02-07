/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Abstract {@link ISequenceScheduler} implementation to manage the sequence optimisation given a set of arrival times at each sequence element. This class handles the construction of
 * {@link VoyagePlan}s and uses a {@link IVoyagePlanOptimiser} implementation to make the best route choices.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractSequenceScheduler implements ISequenceScheduler {
	private static final Logger log = LoggerFactory.getLogger(AbstractSequenceScheduler.class);

	private IElementDurationProvider durationsProvider;

	private ITimeWindowDataComponentProvider timeWindowProvider;

	private IPortProvider portProvider;

	private IPortSlotProvider portSlotProvider;

	private IPortTypeProvider portTypeProvider;

	private IVesselProvider vesselProvider;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	private IVoyagePlanOptimiser voyagePlanOptimiser;

	protected IRouteCostProvider routeCostProvider;

	public IRouteCostProvider getRouteCostProvider() {
		return routeCostProvider;
	}

	public void setRouteCostProvider(final IRouteCostProvider routeCostProvider) {
		this.routeCostProvider = routeCostProvider;
	}

	public ScheduledSequences schedule(final ISequences sequences, final int[][] arrivalTimes) {
		final ScheduledSequences result = new ScheduledSequences();

		final List<IResource> resources = sequences.getResources();

		for (int i = 0; i < sequences.size(); i++) {
			final ISequence sequence = sequences.getSequence(i);
			final IResource resource = resources.get(i);
			final IVessel vessel = vesselProvider.getVessel(resource);
			if (vessel.getVesselInstanceType() == VesselInstanceType.VIRTUAL) {
				assert sequence.size() == 4 : "A virtual sequence should always contain 4 elements (Start - Load - Discharge - End)";

				result.addVirtualCargo((ILoadOption) portSlotProvider.getPortSlot(sequence.get(1)), (IDischargeOption) portSlotProvider.getPortSlot(sequence.get(2)), arrivalTimes[i][1],
						arrivalTimes[i][2]);
			} else {
				final ScheduledSequence scheduledSequence = schedule(resource, sequence, arrivalTimes[i]);
				if (scheduledSequence == null) {
					return null;
				}
				result.add(scheduledSequence);
			}
		}

		return result;
	}

	/**
	 * Schedule an {@link ISequence} using the given array of arrivalTimes, indexed according to sequence elements. These times will be used as the base arrival time. However is some cases the time
	 * between elements may be too short (i.e. because the vessel is already travelling at max speed). In such cases, if adjustArrivals is true, then arrival times will be adjusted in the
	 * {@link VoyagePlan}s. Otherwise null will be returned.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	public ScheduledSequence schedule(final IResource resource, final ISequence sequence, final int[] arrivalTimes) {
		final IVessel vessel = vesselProvider.getVessel(resource);

		if (vessel.getVesselInstanceType() == VesselInstanceType.VIRTUAL) {
			// Virtual vessels are those operated by a third party, for FOB and DES situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean much,
			// but contracts need this kind of information to make up numbers with.
			return null;
		}

		// Get start time
		final int startTime = arrivalTimes[0];

		final List<VoyagePlan> voyagePlans = new LinkedList<VoyagePlan>();
		final List<Object> currentSequence = new ArrayList<Object>(5);
		final List<Integer> currentTimes = new ArrayList<Integer>(3);
		IPort prevPort = null;
		IPortSlot prevPortSlot = null;
		PortType prevPortType = null;
		VesselState vesselState = VesselState.Ballast;
		VoyageOptions previousOptions = null;

		voyagePlanOptimiser.setVessel(vessel);

		boolean useNBO = false;

		int prevVisitDuration = 0;
		final Iterator<ISequenceElement> itr = sequence.iterator();
		for (int idx = 0; itr.hasNext(); ++idx) {
			final ISequenceElement element = itr.next();

			final IPort thisPort = portProvider.getPortForElement(element);
			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);

			// If this is the first port, then this will be null and there will
			// be no voyage to plan.
			if (prevPort != null) {
				if ((prevPortType == PortType.Load) || (prevPortType == PortType.CharterOut)) {
					useNBO = true;
				}

				// Available time, as determined by inputs.
				final int availableTime = arrivalTimes[idx] - arrivalTimes[idx - 1] - prevVisitDuration;

				// Get the min NBO travelling speed
				final int nboSpeed = vessel.getVesselClass().getMinNBOSpeed(vesselState);

				final VoyageOptions options = new VoyageOptions();

				options.setVessel(vessel);
				options.setFromPortSlot(prevPortSlot);
				options.setToPortSlot(thisPortSlot);
				options.setNBOSpeed(nboSpeed);
				options.setVesselState(vesselState);
				options.setAvailableTime(availableTime);

				if (prevPortSlot instanceof IHeelOptionsPortSlot) {
					options.setAvailableLNG(Math.min(vessel.getVesselClass().getCargoCapacity(), ((IHeelOptionsPortSlot) prevPortSlot).getHeelOptions().getHeelLimit()));
					useNBO = true;
				} else if (useNBO) {
					options.setAvailableLNG(vessel.getVesselClass().getCargoCapacity());
				} else {
					options.setAvailableLNG(0);
				}

				if (options.getAvailableLNG() == 0) {
					useNBO = false;
				}

				if (prevPortType == PortType.DryDock) {
					options.setWarm(true);
				} else {
					options.setWarm(false);
				}

				// Determined by voyage plan optimiser
				options.setUseNBOForTravel(useNBO);
				options.setUseFBOForSupplement(false);
				options.setUseNBOForIdle(false);

				if (thisPortSlot.getPortType() == PortType.Load) {
					options.setShouldBeCold(true);
					final ILoadSlot thisLoadSlot = (ILoadSlot) thisPortSlot;

					if (thisLoadSlot.isCooldownSet()) {
						if (thisLoadSlot.isCooldownForbidden()) {
							// cooldown may still happen if circumstances allow
							// no alternative.
							options.setAllowCooldown(false);
						} else {
							options.setAllowCooldown(true);
						}
					} else {
						if (useNBO) {
							if (thisPort.shouldVesselsArriveCold()) {
								// we don't want to use cooldown ever
								options.setAllowCooldown(false);
							} else {
								// we have a choice
								options.setAllowCooldown(false);
								voyagePlanOptimiser.addChoice(new CooldownVoyagePlanChoice(options));
							}
						} else {
							// we have to allow cooldown, because there is no
							// NBO.
							options.setAllowCooldown(true);
						}
					}
				} else {
					options.setShouldBeCold(false);
				}

				// Enable choices only if NBO could be available.
				if (useNBO) {
					// Note ordering of choices is important = NBO must be set
					// before FBO and Idle choices, otherwise if NBO choice is
					// false, FBO and IdleNBO may still be true if set before
					// NBO

					if (vesselState == VesselState.Ballast) {
						voyagePlanOptimiser.addChoice(new NBOTravelVoyagePlanChoice(previousOptions, options));
					}

					voyagePlanOptimiser.addChoice(new FBOVoyagePlanChoice(options));

					voyagePlanOptimiser.addChoice(new IdleNBOVoyagePlanChoice(options));
				}

				final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(prevPort, thisPort));
				// Only add route choice if there is one
				if (distances.size() == 1) {
					final MatrixEntry<IPort, Integer> d = distances.get(0);
					options.setDistance(d.getValue());
					options.setRoute(d.getKey());
				} else {
					voyagePlanOptimiser.addChoice(new RouteVoyagePlanChoice(options, distances));
				}

				currentSequence.add(options);
				previousOptions = options;
			}

			final int visitDuration = durationsProvider.getElementDuration(element, resource);

			final PortDetails portDetails = new PortDetails();
			portDetails.setVisitDuration(visitDuration);
			portDetails.setPortSlot(thisPortSlot);

			currentTimes.add(arrivalTimes[idx]);
			currentSequence.add(portDetails);

			final PortType portType = portTypeProvider.getPortType(element);
			if (((currentSequence.size() > 1) && (portType == PortType.Load)) || (portType == PortType.CharterOut) || (portType == PortType.DryDock)) {

				if (!optimiseSequence(voyagePlans, currentSequence, currentTimes, voyagePlanOptimiser)) {
					return null;
				}

				// Reset useNBO flag
				useNBO = false;
				previousOptions = null;

				currentSequence.clear();
				currentTimes.clear();

				currentSequence.add(portDetails);
				currentTimes.add(arrivalTimes[idx]);
			}

			// Setup for next iteration
			prevPort = thisPort;
			prevPortSlot = thisPortSlot;
			prevPortType = portType;
			prevVisitDuration = visitDuration;

			// Update vessel state
			if (portType == PortType.Load) {
				vesselState = VesselState.Laden;
			} else if ((portType == PortType.Discharge) || (portType == PortType.CharterOut) || (portType == PortType.DryDock)) {
				vesselState = VesselState.Ballast;
			} else {
				// No change in state
			}
		}

		// TODO: Do we need to run optimiser when we only have a load port here?

		// Populate final plan details
		// if (!currentSequence.isEmpty()) {
		if (currentSequence.size() > 1) {
			if (!optimiseSequence(voyagePlans, currentSequence, currentTimes, voyagePlanOptimiser)) {
				return null;
			}
		}

		return new ScheduledSequence(resource, startTime, voyagePlans);
	}

	public final boolean optimiseSequence(final List<VoyagePlan> voyagePlans, final List<Object> currentSequence, final List<Integer> currentTimes, final IVoyagePlanOptimiser optimiser) {
		// Run sequencer evaluation
		optimiser.setBasicSequence(currentSequence);
		optimiser.setArrivalTimes(currentTimes);
		optimiser.init();
		final VoyagePlan currentPlan = optimiser.optimise();

		final Object[] sequence = currentPlan.getSequence();
		for (int i = 0; i < sequence.length; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				final int availableTime = details.getOptions().getAvailableTime();

				// Take voyage details time as this can be larger than
				// available time e.g. due to reaching max speed.
				final int duration = details.getTravelTime() + details.getIdleTime();

				// assert duration >= (availableTime - 1) :
				// "Duration should exceed available time less one, but is "
				// + duration + " vs " + availableTime; // hack
				if (duration > availableTime) {
					return false;
				}
			}
		}
		voyagePlans.add(currentPlan);

		// Reset VPO ready for next iteration
		optimiser.reset();

		return true;
	}

	public final IPortSlotProvider getPortSlotProvider() {
		return portSlotProvider;
	}

	public final void setPortSlotProvider(final IPortSlotProvider portSlotProvider) {
		this.portSlotProvider = portSlotProvider;
	}

	public final IPortTypeProvider getPortTypeProvider() {
		return portTypeProvider;
	}

	public final void setPortTypeProvider(final IPortTypeProvider portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	public final IElementDurationProvider getDurationsProvider() {
		return durationsProvider;
	}

	public final void setDurationsProvider(final IElementDurationProvider durationsProvider) {
		this.durationsProvider = durationsProvider;
	}

	public final ITimeWindowDataComponentProvider getTimeWindowProvider() {
		return timeWindowProvider;
	}

	public final void setTimeWindowProvider(final ITimeWindowDataComponentProvider timeWindowProvider) {
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

	public final void setDistanceProvider(final IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public final IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public final void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	public IVoyagePlanOptimiser getVoyagePlanOptimiser() {
		return voyagePlanOptimiser;
	}

	public void setVoyagePlanOptimiser(final IVoyagePlanOptimiser voyagePlanOptimiser) {
		this.voyagePlanOptimiser = voyagePlanOptimiser;
	}

	/**
	 * Ensures all required data items are present, otherwise an {@link IllegalStateException} will be thrown.
	 */
	public void init() {

		// Verify that everything is in place
		if (routeCostProvider == null) {
			throw new IllegalStateException("No route cost provider set");
		}
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

	private BufferedWriter logWriter;
	private static boolean loggingEnabled = false;

	public static void setLoggingEnabled(final boolean loggingEnabled) {
		AbstractSequenceScheduler.loggingEnabled = loggingEnabled;
	}

	private static int tag = 0;

	private static synchronized int getTag() {
		return tag++;
	}

	protected final void createLog() {
		if (!loggingEnabled) {
			return;
		}
		try {
			final String name = getClass().getSimpleName() + "_log_" + getTag();
			final File f = new File("./" + name + ".py");
			logWriter = new BufferedWriter(new FileWriter(f));

			log.debug("Created scheduler log " + f.getAbsolutePath());
		} catch (final IOException ex) {
		}
	}

	protected final void startLogEntry(final int sequenceSize) {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write("Schedule(" + sequenceSize + ",[");
		} catch (final IOException e) {

		}
	}

	protected final void logValue(final long fitness) {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write(fitness + ", ");
		} catch (final IOException e) {
		}
	}

	protected final void endLogEntry() {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.write("])\n");
			logWriter.flush();
		} catch (final IOException e) {
		}
	}

	protected final void closeLog() {
		if (!loggingEnabled) {
			return;
		}
		try {
			logWriter.flush();
			logWriter.close();
		} catch (final IOException e) {
		}
		logWriter = null;
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
