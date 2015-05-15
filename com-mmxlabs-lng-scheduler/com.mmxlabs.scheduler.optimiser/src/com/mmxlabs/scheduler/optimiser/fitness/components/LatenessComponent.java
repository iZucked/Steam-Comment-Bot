/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on lateness.
 * 
 * @author Simon Goodall
 * 
 */
public final class LatenessComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private long accumulator = 0;

	@Inject
	@NonNull
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	@NonNull
	private IPromptPeriodProvider promptPeriodProvider;

	@Inject
	@NonNull
	private ILatenessComponentParameters latenessParameters;

	public LatenessComponent(@NonNull final String name, @NonNull final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			final IPortSlot portSlot = detail.getOptions().getPortSlot();
			final ITimeWindow tw;

			if (portSlot instanceof StartPortSlot) {
				final IStartEndRequirement req = startEndRequirementProvider.getStartRequirement(currentResource);
				tw = req.getTimeWindow();
			} else if (portSlot instanceof EndPortSlot) {
				final IStartEndRequirement req = startEndRequirementProvider.getEndRequirement(currentResource);
				tw = req.getTimeWindow();
			} else {
				tw = portSlot.getTimeWindow();
			}

			if ((tw != null) && (time > tw.getEnd())) {
				int latenessInHours = time - tw.getEnd();
				// We are late!
				ILatenessComponentParameters.Interval interval = Interval.BEYOND;
				if (tw.getStart() < promptPeriodProvider.getEndOfPromptPeriod()) {
					interval = Interval.PROMPT;
				} else if (tw.getStart() < (promptPeriodProvider.getEndOfPromptPeriod() + 90 * 24)) {
					interval = Interval.MID_TERM;
				}

				if (latenessInHours < latenessParameters.getThreshold(interval)) {
					// Hit low penalty value
					accumulator += latenessParameters.getLowWeight(interval) * latenessInHours;
				} else {
					accumulator += latenessParameters.getHighWeight(interval) * latenessInHours;
				}

				// addDiscountedValue(time, 1000000*(time - tw.getEnd()));
				// accumulator += getDiscountedValue(time, (time - tw.getEnd()));
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator;
	}
}
