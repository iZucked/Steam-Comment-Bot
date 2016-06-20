/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.optimiser.ga.IIndividualEvaluator;
import com.mmxlabs.optimiser.ga.Individual;
import com.mmxlabs.optimiser.ga.bytearray.ByteArrayIndividual;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * The {@link IndividualEvaluator} evaluates a GA {@link Individual} using the {@link ICargoSchedulerFitnessComponent} implementation to evaluate the fitness of a scheduled {@link ISequence}. This
 * bitstring represents offsets in time from the start of each sequence element's {@link ITimeWindow} up until the end of the window. The bitstring decodes each of these offsets and generates a set of
 * arrival times at each element. The {@link IndividualEvaluator} also takes into account the time it takes to travel between elements and will ensure that the arrival times are not too close together
 * so that it would be impossible to travel there in time.
 * 
 * 
 * 
 * TODO: This implementation assumes that a {@link ITimeWindow} exists for each element in the schedule.
 * 
 * @author Simon Goodall
 * @deprecated Needs updating for various API changes in past
 */
@Deprecated
public final class IndividualEvaluator implements IIndividualEvaluator<ByteArrayIndividual> {

	private ISequenceScheduler sequenceScheduler;

	private ITimeWindowDataComponentProvider timeWindowProvider;

	private IElementDurationProvider durationsProvider;

	private IVesselProvider vesselProvider;

	private IPortProvider portProvider;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	private Collection<ICargoSchedulerFitnessComponent> fitnessComponents;

	private Map<String, Double> fitnessComponentWeights;

	private ISequence sequence;

	private IResource resource;

	/**
	 * Maximum value for the indexed component.
	 */
	private int[] ranges;

	/**
	 * Minimum time between elements. Including visit duration and travel time at maximum speed. Indexes as time to get to the sequence element.
	 */
	private int[] travelTimes;

	/**
	 * UNUSED. Array indicating the multiplier to apply to the decoded offset.
	 */
	private int[] multiplier;

	/**
	 * Cached array of {@link ITimeWindow} starts
	 */
	private int[] windowStarts;

	// @Inject
	// private VoyagePlanIterator voyagePlanIterator;
	// private ICargoSchedulerFitnessComponent[] iteratingComponents;

	// private List<ICargoSchedulerFitnessComponent> iteratingComponents =
	// new ArrayList<ICargoSchedulerFitnessComponent>();

	public IndividualEvaluator() {

	}

	public final long evaluate(final int[] arrivalTimes) {
		// // Use the sequence schedule to evaluate the arrival time profile.
		// // final Pair<Integer, List<VoyagePlan>> voyagePlans = sequenceScheduler
		// // .schedule(resource, sequence, arrivalTimes);
		//
		// // Was the set of arrival times valid?
		// if (voyagePlans == null) {
		// // Bad set of times
		// return Long.MAX_VALUE;
		// }
		//
		// // Evaluate fitness
		final long totalFitness = 0;
		//
		// // voyagePlanIterator.iterateComponents(voyagePlans.getSecond(),
		// // voyagePlans.getFirst().intValue(), resource,
		// // iteratingComponents);
		//
		// for (final ICargoSchedulerFitnessComponent component : fitnessComponents) {
		// // final long rawFitness = component.rawEvaluateSequence(resource,
		// // sequence, voyagePlans.getSecond(), voyagePlans.getFirst());
		//
		// // Enable this block once weights are set
		// if (false) {
		// final String componentName = component.getName();
		// if (fitnessComponentWeights.containsKey(componentName)) {
		// final double weight = fitnessComponentWeights
		// .get(componentName);
		// final long fitness = Math.round(weight
		// * (double) rawFitness);
		// totalFitness += fitness;
		// }
		// } else {
		// // Current just use the raw fitness value
		// totalFitness += rawFitness;
		// }
		// }
		return totalFitness;
	}

	@Override
	public final long evaluate(final ByteArrayIndividual individual) {
		// Decode into a set of start times
		final int[] arrivalTimes = new int[ranges.length];
		decode(individual, arrivalTimes);
		return evaluate(arrivalTimes);
	}

	/**
	 * Decode an {@link Individual} into an array of arrival times for each sequence element. This method uses the {@link #travelTimes} array to ensure the time between each arrival is at least the
	 * travel time.
	 * 
	 * @param individual
	 * @param arrivalTimes
	 */
	public final void decode(final ByteArrayIndividual individual, final int[] arrivalTimes) {

		// TODO: Currently the byte array can be under utilised. E.g. a 6 hour
		// window only requires 3 bits out of the byte, leaving 5 bits unused.
		// This means that while they are not used to generate the solution,
		// they are used in the GA operators, leading to many byte arrays giving
		// the same solution.
		// We could either change the decode to compress the bits so this only
		// effects the last byte in the sequence. Alternatively, we could ensure
		// these unused bits are always zero by generating a bit mask in the
		// setup and AND'ing all individuals after crossover and mutate ops.
		// At some point we will need to cache the results of an individual to
		// avoid recalculating it many times during a GA run. The former
		// solution would yield smaller byte arrays thus making comparison
		// operations quicker.

		final byte[] bytes = individual.bytes;

		int idx = 0;

		final int numElements = ranges.length;
		for (int i = 0; i < numElements; ++i) {

			if (windowStarts[i] != -1) {
				final int range = ranges[i];

				if (range == -1) {
					// No time window,
					assert false;
				} else if (range == 0) {
					arrivalTimes[i] = windowStarts[i];
				} else {

					final int numBytes = range / 8;

					// Add on all whole bytes to the offset
					int offset = 0;
					for (int j = 0; j < numBytes; ++j) {
						// TODO: Does OR apply as I expect, or should I just use
						// + ?
						offset = (offset << 8) | (bytes[idx++] & 0xff);
					}

					// For partial bytes, right shift back afterwards
					final int extraBits = range % 8;
					if (extraBits > 0) {
						// TODO: Does OR apply as I expect, or should I just use
						// + ?
						offset = (offset << 8) | (bytes[idx++] & 0xff);

						// TODO: Do we need >>>= ?
						offset >>= (8 - extraBits);
					}

					// TODO: We are not handling values outside of the range! -
					// lets just wrap!
					arrivalTimes[i] = windowStarts[i] + ((offset * multiplier[i]) % range);
				}
			} else {
				// No time window, so do nothing here. Let evaluator work out
				// best options.
				// arrivalTimes[i] = -1;

				// Set arrival time to max speed travel time
				if (i == 0) {
					arrivalTimes[i] = travelTimes[i];
				} else {
					arrivalTimes[i] = travelTimes[i] + arrivalTimes[i - 1];
				}
			}

			if (i > 0) {
				// Ensure that we have the minimum travel time between ports -
				// this may introduce lateness into the solution
				final int diff = arrivalTimes[i] - arrivalTimes[i - 1];
				if (diff < travelTimes[i]) {
					arrivalTimes[i] = travelTimes[i] + arrivalTimes[i - 1];
				}
			}
		}
	}

	/**
	 * Init the fitnesses, returns number of bytes required in byte array
	 * 
	 * @param sequence
	 * @return
	 */
	public final int setup(final IResource resource, final ISequence sequence) {

		this.resource = resource;
		this.sequence = sequence;

		final int numElements = sequence.size();

		int numBytes = 0;

		ranges = new int[numElements];
		travelTimes = new int[numElements];
		windowStarts = new int[numElements];
		multiplier = new int[numElements];

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final int maxSpeed = vesselAvailability.getVessel().getVesselClass().getMaxSpeed();

		// Loop through sequence, extract time windows and determine number of
		// bite required to represent them.
		ISequenceElement prevT = null;
		final Iterator<ISequenceElement> itr = sequence.iterator();
		for (int idx = 0; itr.hasNext(); ++idx) {
			final ISequenceElement t = itr.next();

			final List<ITimeWindow> timeWindows = timeWindowProvider.getTimeWindows(t);

			// TODO: Multiple time windows can be handled by combining the all
			// time window ranges into a single value. Then use a step function
			// to switch between windows during decode. E.g. each of the window
			// are 6 hours, giving a total range of 18 hours. If the decoded
			// value is 9, then this means use use the second time window with
			// an offset of 3. We need to consider how this would interact with
			// the multiplier array should the overall range be large (thus
			// triggering the multiplier), or if the time windows themselves are
			// of vastly differing ranges (e.g. 6 hours versus 6 days).
			assert timeWindows.size() <= 1;

			final ITimeWindow tw = timeWindows.isEmpty() ? null : timeWindows.get(0);

			if (idx < (travelTimes.length - 1)) {
				// Add Visit duration to "travel" time between elements
				final int duration = durationsProvider.getElementDuration(t, resource);
				travelTimes[idx + 1] = duration;
			}

			if (prevT != null) {
				// TODO: Cache these items
				final IPort from = portProvider.getPortForElement(prevT);
				final IPort to = portProvider.getPortForElement(t);

				// Find the quickest route between the ports
				final Collection<MatrixEntry<IPort, Integer>> distances = distanceProvider.getValues(from, to);
				int distance = Integer.MAX_VALUE;
				for (final MatrixEntry<IPort, Integer> d : distances) {
					distance = Math.min(distance, d.getValue());
				}

				// Determine minimum travel time between ports
				final int minTime = Calculator.getTimeFromSpeedDistance(maxSpeed, distance);
				travelTimes[idx] += minTime;
			}

			if (tw != null) {
				// Record early arrival time as window start
				windowStarts[idx] = tw.getInclusiveStart();

				// Determine range of values between arrival and end window.
				final int r = tw.getExclusiveEnd() - tw.getInclusiveStart() - 1;
				// Should we be forced to arrive too late, i.e. past the end
				// window, then there is nothing to optimise over here, so set
				// range to zero.
				ranges[idx] = r;

				// TODO: Larger time windows should scale the time units used.
				// E.g. one or two days use to the hour, then multiple up to 2
				// hours, 4 hours, 6 hours, 12 hours, per day etc. This requires
				// another array containing a multiplier.
				multiplier[idx] = 1;
			} else {
				ranges[idx] = 0;
				windowStarts[idx] = -1;
				multiplier[idx] = 1;
			}

			prevT = t;
		}

		// The first pass constructed the GA range to match the available
		// service aim windows. However, this leads to a larger number of
		// possible states than would be allowed. The next sections of code
		// attempt to restrict the ranges based upon the minimum travel time and
		// visit time.

		// Forward pass: Starting with the fixed vessel start time, add on port
		// visit duration and the minimum travel time to get the earliest
		// arrival time at the next port. If this time is after the port visit
		// window start, then move this forward to the calculated arrival time
		// and reduce the range value accordingly. If it is less than the window
		// start, set the earliest time to the window start for use as the base
		// time for the next port visit - as we would have to idle until this
		// bound anyway. If there is no time window, then there is no check to
		// make.

		// Current earliest arrival time at port.
		int currentEarliestArrival = 0;

		for (int idx = 0; idx < windowStarts.length; ++idx) {

			// Add on travel time to get to this port. When idx == 0, this is
			// empty.
			currentEarliestArrival += travelTimes[idx];

			if (windowStarts[idx] != -1) {

				// If we can only arrive after the window start, then update
				// bounds to this value
				if (currentEarliestArrival > windowStarts[idx]) {

					// Determine range of values between arrival and end window.
					final int windowEnd = windowStarts[idx] + ranges[idx];
					final int r = windowEnd - currentEarliestArrival;

					// Should we be forced to arrive too late, i.e. past the end
					// window, then there is nothing to optimise over here, so
					// set range to zero.
					ranges[idx] = Math.max(0, r);

					// Record early arrival time as window start
					windowStarts[idx] = currentEarliestArrival;
				} else {
					// If time window has a later start, then update the current
					// time to the window
					currentEarliestArrival = windowStarts[idx];
				}
			}
		}

		// Backwards pass: Apply a similar rule, but setting the latest arrival
		// time. Starting at the last arrival, work backwards through the
		// ranges, reducing them is necessary to avoid causing lateness. Unlike
		// the forward pass where we can push arrival times forward, the
		// backwards pass will only reduce the range.

		int currentLatestArrival = Integer.MAX_VALUE;
		// Reverse loop doing a similar operation, but reducing the range vasle
		for (int idx = windowStarts.length - 1; idx > 0; --idx) {
			if (windowStarts[idx] != -1) {

				final int windowEnd = windowStarts[idx] + ranges[idx];

				// If the latest we can arrive if before the window end, then we
				// need to reduce the range to accomodate this.
				if (currentLatestArrival < windowEnd) {

					// Determine range of values between arrival and end window.
					final int r = currentLatestArrival - windowStarts[idx];

					// Should we be forced to arrive too late, i.e. past the end
					// window, then there is nothing to optimise over here, so
					// set range to zero.
					ranges[idx] = Math.max(0, r);

					// TODO: Do we want this -- need to think it through more.
					// If r is negative, we update currentLatestArrival to get
					// the actual arrival time that will be used.
					currentLatestArrival = windowStarts[idx] + ranges[idx];
				} else {
					// If time window has a earlier end, then update the current
					// time to the window
					currentLatestArrival = windowEnd;
				}
			}

			// Take off travel time to get to this port to get new latest
			// arrival at preceding port.
			currentLatestArrival -= travelTimes[idx];
		}

		// Finally, after the previous stages of range manipulation, we can
		// calculate the bit string size.
		for (int idx = 0; idx < windowStarts.length; ++idx) {
			// TODO: Larger time windows should scale the time units used.
			// E.g. one or two days use to the hour, then multiple up to 2
			// hours, 4 hours, 6 hours, 12 hours, per day etc. This requires
			// another array containing a multiplier.
			multiplier[idx] = 1;

			assert ranges[idx] >= 0;

			// Calculate number of bits required

			// Get whole number of bytes required
			numBytes += (ranges[idx] / 8);

			// Use a whole byte to represent any excess bits
			if ((ranges[idx] % 8) != 0) {
				++numBytes;
			}
		}

		return numBytes;
	}

	/**
	 * Ensures all required data items are present, otherwise an {@link IllegalStateException} will be thrown.
	 */
	public void init() {

		// Verify that everything is in place
		if (sequenceScheduler == null) {
			throw new IllegalStateException("No sequence scheduler set");
		}
		if (fitnessComponents == null) {
			throw new IllegalStateException("No fitness components set");
		}
		if (fitnessComponentWeights == null) {
			throw new IllegalStateException("No fitness component weights set");
		}
		if (timeWindowProvider == null) {
			throw new IllegalStateException("No time window provider set");
		}
		if (portProvider == null) {
			throw new IllegalStateException("No port provider set");
		}
		if (distanceProvider == null) {
			throw new IllegalStateException("No distance provider set");
		}
		if (vesselProvider == null) {
			throw new IllegalStateException("No vessel provider set");
		}
		if (durationsProvider == null) {
			throw new IllegalStateException("No durations provider set");
		}
	}

	public final ISequenceScheduler getSequenceScheduler() {
		return sequenceScheduler;
	}

	public final void setSequenceScheduler(final ISequenceScheduler sequenceScheduler) {
		this.sequenceScheduler = sequenceScheduler;
	}

	public final ITimeWindowDataComponentProvider getTimeWindowProvider() {
		return timeWindowProvider;
	}

	public final void setTimeWindowProvider(final ITimeWindowDataComponentProvider timeWindowProvider) {
		this.timeWindowProvider = timeWindowProvider;
	}

	public final Collection<ICargoSchedulerFitnessComponent> getFitnessComponents() {
		return fitnessComponents;
	}

	public final void setFitnessComponents(final Collection<ICargoSchedulerFitnessComponent> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
		// iteratingComponents = VoyagePlanIterator
		// .filterIteratingComponents(fitnessComponents);
	}

	public final Map<String, Double> getFitnessComponentWeights() {
		return fitnessComponentWeights;
	}

	public final void setFitnessComponentWeights(final Map<String, Double> fitnessComponentWeights) {
		this.fitnessComponentWeights = fitnessComponentWeights;
	}

	@Override
	public void dispose() {

		sequenceScheduler = null;
		timeWindowProvider = null;
		durationsProvider = null;
		vesselProvider = null;
		portProvider = null;
		distanceProvider = null;
		fitnessComponents = null;
		fitnessComponentWeights = null;

		sequence = null;
		resource = null;

		ranges = null;
		travelTimes = null;
		multiplier = null;
		windowStarts = null;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	public IPortProvider getPortProvider() {
		return portProvider;
	}

	public void setPortProvider(final IPortProvider portProvider) {
		this.portProvider = portProvider;
	}

	public IMultiMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public void setDistanceProvider(final IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public IElementDurationProvider getDurationsProvider() {
		return durationsProvider;
	}

	public void setDurationsProvider(final IElementDurationProvider durationsProvider) {
		this.durationsProvider = durationsProvider;
	}

	public final ISequence getSequence() {
		return sequence;
	}

	public final void setSequence(final ISequence sequence) {
		this.sequence = sequence;
	}

	public final IResource getResource() {
		return resource;
	}

	public final void setResource(final IResource resource) {
		this.resource = resource;
	}

	public final int[] getRanges() {
		return ranges;
	}

	public final void setRanges(final int[] ranges) {
		this.ranges = ranges;
	}

	public final int[] getTravelTimes() {
		return travelTimes;
	}

	public final void setTravelTimes(final int[] travelTimes) {
		this.travelTimes = travelTimes;
	}

	public final int[] getMultiplier() {
		return multiplier;
	}

	public final void setMultiplier(final int[] multiplier) {
		this.multiplier = multiplier;
	}

	public final int[] getWindowStarts() {
		return windowStarts;
	}

	public final void setWindowStarts(final int[] windowStarts) {
		this.windowStarts = windowStarts;
	}
}
