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
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link IndividualEvaluator} evaluates a GA {@link Individual} using the
 * {@link ICargoSchedulerFitnessComponent} implementation to evaluate the
 * fitness of a scheduled {@link ISequence}. This bitstring represents offsets
 * in time from the start of each sequence element's {@link ITimeWindow} up
 * until the end of the window. The bitstring decodes each of these offsets and
 * generates a set of arrival times at each element. The
 * {@link IndividualEvaluator} also takes into account the time it takes to
 * travel between elements and will ensure that the arrival times are not too
 * close together so that it would be impossible to travel there in time.
 * 
 * 
 * 
 * TODO: This implementation assumes that a {@link ITimeWindow} exists for each
 * element in the schedule.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class IndividualEvaluator<T> implements IIndividualEvaluator<T> {

	private AbstractSequenceScheduler<T> sequenceScheduler;


	private ITimeWindowDataComponentProvider timeWindowProvider;

	private IElementDurationProvider<T> durationsProvider;

	private IVesselProvider vesselProvider;

	private IPortProvider portProvider;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	private Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents;

	private Map<String, Double> fitnessComponentWeights;

	private ISequence<T> sequence;

	private IResource resource;

	/**
	 * Maximum value for the indexed component.
	 */
	private int[] ranges;

	/**
	 * Minimum time between elements. Including visit duration and travel time
	 * at maximum speed. Indexes as time to get to the sequence element.
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

	/**
	 * Flag passed to the {@link GASequenceScheduler} to indicate whether or not
	 * it is allowed to adjust arrival times.
	 */
	private boolean adjustArrivalTimes = false;

	public IndividualEvaluator() {

	}

	@SuppressWarnings("unused")
	@Override
	public final long evaluate(final Individual individual) {

		// Decode into a set of start times
		final int[] arrivalTimes = new int[ranges.length];
		decode(individual, arrivalTimes);

		// Use the sequence schedule to evaluate the arrival time profile.
		final List<VoyagePlan> voyagePlans = sequenceScheduler.schedule(
				resource, sequence, arrivalTimes, adjustArrivalTimes);

		// Was the set of arrival times valid?
		if (voyagePlans == null) {
			// Bad set of times
			return Long.MAX_VALUE;
		}


		// Evaluate fitness
		long totalFitness = 0;
		for (final ICargoSchedulerFitnessComponent<T> component : fitnessComponents) {
			final long rawFitness = component.rawEvaluateSequence(resource,
					sequence, voyagePlans);

			// Enable this block once weights are set
			if (false) {
				final String componentName = component.getName();
				if (fitnessComponentWeights.containsKey(componentName)) {
					final double weight = fitnessComponentWeights
							.get(componentName);
					final long fitness = Math.round(weight
							* (double) rawFitness);
					totalFitness += fitness;
				}
			} else {
				// Current just use the raw fitness value
				totalFitness += rawFitness;
			}
		}
		return totalFitness;
	}

	/**
	 * Decode an {@link Individual} into an array of arrival times for each
	 * sequence element. This method uses the {@link #travelTimes} array to
	 * ensure the time between each arrival is at least the travel time.
	 * 
	 * @param individual
	 * @param arrivalTimes
	 */
	public final void decode(final Individual individual,
			final int[] arrivalTimes) {

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
				}

				final int numBytes = range / 8;

				// Add on all whole bytes to the offset
				int offset = 0;
				for (int j = 0; j < numBytes; ++j) {
					offset <<= 8;
					// TODO: Does OR apply as I expect, or should I just use + ?
					offset |= bytes[idx++];
				}

				// For partial bytes, right shift back afterwards
				final int extraBits = range % 8;
				if (extraBits > 0) {
					offset <<= 8;
					// TODO: Does OR apply as I expect, or should I just use + ?
					offset |= bytes[idx++];

					// TODO: Do we need >>>= ?
					offset >>= (8 - extraBits);
				}

				arrivalTimes[i] = windowStarts[i] + offset * multiplier[i];
			} else {
//				 No time window, so do nothing here. Let evaluator work out
//				 best options.
//				arrivalTimes[i] = -1;
				
				// Set arrival time to max speed travel time
				arrivalTimes[i] = travelTimes[i] + arrivalTimes[i - 1];
			}

			if (i > 0) {
				// Ensure that we have the minimum travel time between ports
				int diff = arrivalTimes[i] - arrivalTimes[i - 1];
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
	public final int setup(final IResource resource, final ISequence<T> sequence) {

		this.resource = resource;
		this.sequence = sequence;

		final int numElements = sequence.size();

		int numBytes = 0;

		ranges = new int[numElements];
		travelTimes = new int[numElements];
		windowStarts = new int[numElements];
		multiplier = new int[numElements];

		final IVessel vessel = vesselProvider.getVessel(resource);
		final int maxSpeed = vessel.getVesselClass().getMaxSpeed();

		// Loop through sequence, extract time windows and determine number of
		// bite required to represent them.
		T prevT = null;
		final Iterator<T> itr = sequence.iterator();
		for (int idx = 0; itr.hasNext(); ++idx) {
			final T t = itr.next();

			final List<ITimeWindow> timeWindows = timeWindowProvider
					.getTimeWindows(t);

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

			final ITimeWindow tw = timeWindows.isEmpty() ? null : timeWindows
					.get(0);

			// Add Visit duration to "travel" time between elements
			final int duration = durationsProvider.getElementDuration(t,
					resource);
			travelTimes[idx] = duration;

			if (prevT != null) {
				// TODO: Cache these items
				final IPort from = portProvider.getPortForElement(prevT);
				final IPort to = portProvider.getPortForElement(t);
				// Determine minimum travel time between ports
				// TODO: find quickest route - canals may have additional time
				// constraints
				final int distance = distanceProvider.get(
						IMultiMatrixProvider.Default_Key).get(from, to);
				final int minTime = Calculator.getTimeFromSpeedDistance(
						maxSpeed, distance);
				travelTimes[idx] += minTime;
			}

			if (tw != null) {
				ranges[idx] = tw.getEnd() - tw.getStart();

				// TODO: Larger time windows should scale the time units used.
				// E.g. one or two days use to the hour, then multiple up to 2
				// hours, 4 hours, 6 hours, 12 hours, per day etc. This requires
				// another array containing a multiplier.
				multiplier[idx] = 1;

				// Calc num bits
				numBytes += (ranges[idx] / 8);
				if (ranges[idx] % 8 != 0) {
					++numBytes;
				}

				windowStarts[idx] = tw.getStart();
			} else {
				ranges[idx] = 0;
				windowStarts[idx] = -1;
				multiplier[idx] = 1;
			}

			prevT = t;
		}

		return numBytes;
	}

	/**
	 * Ensures all required data items are present, otherwise an
	 * {@link IllegalStateException} will be thrown.
	 */
	public void init() {

		// Verify that everything is in place
		if (sequenceScheduler == null) {
			throw new IllegalStateException("No sequence scheduler set");
		}
		if (fitnessComponents == null) {
			throw new IllegalStateException("No fitness components set");
		}
		// TODO: Enable once this is set.
		// if (fitnessComponentWeights == null) {
		// throw new IllegalStateException("No fitness component weights set");
		// }
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

	public final AbstractSequenceScheduler<T> getSequenceScheduler() {
		return sequenceScheduler;
	}

	public final void setSequenceScheduler(
			final AbstractSequenceScheduler<T> sequenceScheduler) {
		this.sequenceScheduler = sequenceScheduler;
	}


	public final ITimeWindowDataComponentProvider getTimeWindowProvider() {
		return timeWindowProvider;
	}

	public final void setTimeWindowProvider(
			final ITimeWindowDataComponentProvider timeWindowProvider) {
		this.timeWindowProvider = timeWindowProvider;
	}

	public final Collection<ICargoSchedulerFitnessComponent<T>> getFitnessComponents() {
		return fitnessComponents;
	}

	public final void setFitnessComponents(
			final Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
	}

	public final Map<String, Double> getFitnessComponentWeights() {
		return fitnessComponentWeights;
	}

	public final void setFitnessComponentWeights(
			final Map<String, Double> fitnessComponentWeights) {
		this.fitnessComponentWeights = fitnessComponentWeights;
		// TODO: Currently there is no mechanism to get this data from outside,
		// so we will assume a weight of 1 internally.
		// So this ever get set, then throw an exception to complete the API.
		throw new UnsupportedOperationException(
				"TODO: Re-enable init() check for this component");
	}

	@Override
	public void dispose() {

		sequenceScheduler = null;
		timeWindowProvider = null;
		fitnessComponents = null;
		fitnessComponentWeights = null;

		sequence = null;
		resource = null;

		ranges = null;
		travelTimes = null;
		multiplier = null;
		windowStarts = null;
	}

	public boolean isAdjustArrivalTimes() {
		return adjustArrivalTimes;
	}

	public void setAdjustArrivalTimes(final boolean adjustArrivalTimes) {
		this.adjustArrivalTimes = adjustArrivalTimes;
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

	public void setDistanceProvider(
			final IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}

	public IElementDurationProvider<T> getDurationsProvider() {
		return durationsProvider;
	}

	public void setDurationsProvider(
			final IElementDurationProvider<T> durationsProvider) {
		this.durationsProvider = durationsProvider;
	}
}
