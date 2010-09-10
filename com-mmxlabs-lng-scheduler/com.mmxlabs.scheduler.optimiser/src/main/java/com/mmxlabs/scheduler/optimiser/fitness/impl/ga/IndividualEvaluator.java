package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.impl.AnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlanAnnotator;

/**
 * 
 * 
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class IndividualEvaluator<T> implements IIndividualEvaluator<T> {

	private AbstractSequenceScheduler<T> sequenceScheduler;

	private IVoyagePlanAnnotator<T> voyagePlanAnnotator;

	private ITimeWindowDataComponentProvider timeWindowProvider;

	private Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents;

	private Map<String, Double> fitnessComponentWeights;

	private ISequence<T> sequence;

	private IResource resource;

	private int[] ranges;
	private int[] multipler;
	private int[] windowStarts;

	private boolean adjustArrivalTimes = true;

	public IndividualEvaluator() {

	}

	@Override
	public final long evaluate(final Individual individual) {

		// Decode into a set of start times
		final int[] arrivalTimes = new int[ranges.length];
		decode(individual, arrivalTimes);

		// TODO: FAll back to GAScheduler to calculate the costs.
		// TOOD: Generalise the SImpleShceduler to take an array of arrival
		// times, to share between simple and GA case
		// TODO: Update the scheduler code to handle -1 as arrival time and just
		// arrav
		final List<IVoyagePlan> voyagePlans = sequenceScheduler.schedule(
				resource, sequence, arrivalTimes, adjustArrivalTimes);

		if (voyagePlans == null) {
			return Long.MAX_VALUE;
		}

		// Should this site around the sequence scheduler? - rather than
		// otherway around?

		final IAnnotatedSequence<T> annotatedSequence = new AnnotatedSequence<T>();
		voyagePlanAnnotator.annotateFromVoyagePlan(resource, voyagePlans,
				annotatedSequence);

		long totalFitness = 0;
		for (final ICargoSchedulerFitnessComponent<T> component : fitnessComponents) {
			final long rawFitness = component.rawEvaluateSequence(resource,
					sequence, annotatedSequence);
			// Enable this block once weights are set
			// final String componentName = component.getName();
			// if (fitnessComponentWeights.containsKey(componentName)) {
			// final double weight = fitnessComponentWeights
			// .get(componentName);
			// final long fitness = Math.round(weight * (double) rawFitness);
			// totalFitness += fitness;
			// }
			totalFitness += rawFitness;
		}
		return totalFitness;
	}

	public final void decode(final Individual individual, final int[] offsets) {

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
				for (int j = 0; i < numBytes; ++j) {
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

				offsets[i] = windowStarts[i] + offset * multipler[i];
			} else {
				// No time window, so do nothing here. Let evaluator work out
				// best options.
				offsets[i] = -1;
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
		windowStarts = new int[numElements];
		multipler = new int[numElements];

		// Loop through sequence, extract time windows and determine number of
		// bite required to represent them.
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
			if (tw != null) {
				ranges[idx] = tw.getEnd() - tw.getStart();

				// TODO: Larger time windows should scale the time units used.
				// E.g. one or two days use to the hour, then multiple up to 2
				// hours, 4 hours, 6 hours, 12 hours, per day etc. This requires
				// another array containing a multiplier.
				multipler[idx] = 1;

				// Calc num bits
				numBytes += (ranges[idx] / 8);
				if (ranges[idx] % 8 != 0) {
					++numBytes;
				}

				windowStarts[idx] = tw.getStart();
			} else {
				ranges[idx] = 0;
				windowStarts[idx] = -1;
				multipler[idx] = 1;
			}
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
		if (voyagePlanAnnotator == null) {
			throw new IllegalStateException("No voyage plan annotator set");
		}
		if (timeWindowProvider == null) {
			throw new IllegalStateException("No time window provider set");
		}
	}

	public final AbstractSequenceScheduler<T> getSequenceScheduler() {
		return sequenceScheduler;
	}

	public final void setSequenceScheduler(
			AbstractSequenceScheduler<T> sequenceScheduler) {
		this.sequenceScheduler = sequenceScheduler;
	}

	public final IVoyagePlanAnnotator<T> getVoyagePlanAnnotator() {
		return voyagePlanAnnotator;
	}

	public final void setVoyagePlanAnnotator(
			IVoyagePlanAnnotator<T> voyagePlanAnnotator) {
		this.voyagePlanAnnotator = voyagePlanAnnotator;
	}

	public final ITimeWindowDataComponentProvider getTimeWindowProvider() {
		return timeWindowProvider;
	}

	public final void setTimeWindowProvider(
			ITimeWindowDataComponentProvider timeWindowProvider) {
		this.timeWindowProvider = timeWindowProvider;
	}

	public final Collection<ICargoSchedulerFitnessComponent<T>> getFitnessComponents() {
		return fitnessComponents;
	}

	public final void setFitnessComponents(
			Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents) {
		this.fitnessComponents = fitnessComponents;
	}

	public final Map<String, Double> getFitnessComponentWeights() {
		return fitnessComponentWeights;
	}

	public final void setFitnessComponentWeights(
			Map<String, Double> fitnessComponentWeights) {
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
		voyagePlanAnnotator = null;

		sequence = null;
		resource = null;

		ranges = null;
		multipler = null;
		windowStarts = null;
	}

	public boolean isAdjustArrivalTimes() {
		return adjustArrivalTimes;
	}

	public void setAdjustArrivalTimes(boolean adjustArrivalTimes) {
		this.adjustArrivalTimes = adjustArrivalTimes;
	}
}
