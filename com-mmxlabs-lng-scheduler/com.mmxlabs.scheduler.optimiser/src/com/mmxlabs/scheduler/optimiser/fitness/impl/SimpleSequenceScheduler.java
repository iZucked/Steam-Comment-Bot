/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;

/**
 * Simple scheduler. Try to arrive at the {@link ITimeWindow} start.
 * 
 * @author Simon Goodall
 * 
 */
public final class SimpleSequenceScheduler extends AbstractLoggingSequenceScheduler {

	@Inject
	private ITimeWindowDataComponentProvider timeWindowProvider;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private ScheduleCalculator scheduleCalculator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	@NonNull
	@Named(DataComponentProviderModule.DIRECT_ROUTE)
	private String directRoute;

	@Override
	public ScheduledSequences schedule(@NonNull final ISequences sequences, @Nullable final IAnnotatedSolution solution) {

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}
		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
			calculator.prepareEvaluation(sequences);
		}

		final ScheduledSequences answer = new ScheduledSequences();

		final int[][] arrivalTimes = new int[sequences.size()][];
		int idx = 0;
		for (final Map.Entry<IResource, ISequence> entry : sequences.getSequences().entrySet()) {
			arrivalTimes[idx++] = schedule(entry.getKey(), entry.getValue());
		}

		scheduleCalculator.schedule(sequences, arrivalTimes, solution);
		return answer;
	}

	private int[] schedule(final IResource resource, final ISequence sequence) {

		final int[] arrivalTimes = new int[sequence.size()];

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			final List<ITimeWindow> timeWindows = timeWindowProvider.getTimeWindows(element);

			// Find earliest start time.
			// TODO: No time windows means time window is Integer.MAX_VALUE -
			// not really sure if this is a sane thing to use.

			// if there's no time window, setting this to max value causes a
			// capacity violation. needs something better. can't just set it to
			// the previous arrival time, because that won't allow enough travel time
			int timeWindowStart = Integer.MAX_VALUE;
			if (timeWindows.isEmpty() && (idx > 0)) {

				final int lastTimeWindowStart = arrivalTimes[idx - 1];
				timeWindowStart = lastTimeWindowStart + Calculator.getTimeFromSpeedDistance(vesselProvider.getVesselAvailability(resource).getVessel().getVesselClass().getMaxSpeed(),
						distanceProvider.get(directRoute).get(portProvider.getPortForElement(sequence.get(idx - 1)), portProvider.getPortForElement(element)));
			} else {
				for (final ITimeWindow window : timeWindows) {
					timeWindowStart = Math.min(timeWindowStart, window.getStart());
				}
			}
			arrivalTimes[idx++] = timeWindowStart;
		}
		return arrivalTimes;
	}

	// @Override
	// public void acceptLastSchedule() {
	//
	// }
}
