/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.providers.IExcessIdleTimeConstrainedSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IExcessIdleTimeConstrainedSlotProvider.eIdleDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.util.LDCargoDetailsWrapper;

/**
 * Idle time recorder
 * 
 * @author achurchill
 *
 */
public class IdleTimeChecker {
	public final static String GA_IDLE_TIME_HOURS_LOW = "info-idle-time-hours-low";
	public final static String GA_IDLE_TIME_HOURS_HIGH = "info-idle-time-hours-high";
	@Inject
	private IExcessIdleTimeComponentParameters idleTimeComponentParameters;

	/**
	 * 
	 * @param scheduledSequences
	 * @param annotatedSolution
	 */
	public void calculateIdleTime(final VolumeAllocatedSequence volumeAllocatedSequence, @Nullable final IAnnotatedSolution annotatedSolution) {
		final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans = volumeAllocatedSequence.getVoyagePlans();
		for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> planStructure : voyagePlans) {
			final VoyagePlan plan = planStructure.getFirst();
			applyPenaltyToPlan(volumeAllocatedSequence, plan, annotatedSolution);
		}
	}

	private void applyPenaltyToPlan(final VolumeAllocatedSequence volumeAllocatedSequence, final VoyagePlan plan, @Nullable final IAnnotatedSolution annotatedSolution) {
		if (LDCargoDetailsWrapper.isCargoVoyage(plan.getSequence())) {
			final LDCargoDetailsWrapper cargoDetails = new LDCargoDetailsWrapper(plan.getSequence());
			if (cargoDetails.getLoadOption() instanceof ILoadSlot) {
				final ILoadSlot load = (ILoadSlot) cargoDetails.getLoadOption();
				if (load.getLoadPriceCalculator() instanceof IExcessIdleTimeConstrainedSlotProvider) {
					final IDischargeOption discharge = cargoDetails.getDischargeOption();
					final IPortSlot returnSlot = cargoDetails.getBallast().getOptions().getToPortSlot();
					cargoDetails.getDischarge().getOptions().getPortSlot();
					final eIdleDetails idleDetails = ((IExcessIdleTimeConstrainedSlotProvider) load.getLoadPriceCalculator()).getSlotIdleConstraintDetails(load, discharge, returnSlot);
					if (idleDetails == eIdleDetails.LADEN || idleDetails == eIdleDetails.BOTH) {
						// penalty on laden
						addPenalty(volumeAllocatedSequence, cargoDetails.getLaden(), annotatedSolution);
					}
					if (idleDetails == eIdleDetails.BALLAST || idleDetails == eIdleDetails.BOTH) {
						// penalty on ballast
						addPenalty(volumeAllocatedSequence, cargoDetails.getBallast(), annotatedSolution);
					}
				}
			}
		}
	}

	private void addPenalty(final VolumeAllocatedSequence volumeAllocatedSequence, final VoyageDetails voyageDetails, @Nullable final IAnnotatedSolution annotatedSolution) {
		final int idleTimeInHours = voyageDetails.getIdleTime();
		final int violatingHours = getViolatingHours(idleTimeInHours);
		if (violatingHours > 0) {
			final IPortSlot idleTimeSlot = voyageDetails.getOptions().getFromPortSlot();
			final boolean isEnd = voyageDetails.getOptions().getToPortSlot() instanceof EndPortSlot;
			final long penalty = getIdleTimePenalty(idleTimeInHours, isEnd, annotatedSolution);
			volumeAllocatedSequence.addIdleHoursViolation(idleTimeSlot, violatingHours);
			volumeAllocatedSequence.addIdleWeightedCost(idleTimeSlot, penalty);
		}
	}

	private long getIdleTimePenalty(final int idleTimeInHours, final boolean isEnd, IAnnotatedSolution annotatedSolution) {
		final int violatingHours = Math.max(idleTimeInHours - idleTimeComponentParameters.getThreshold(Interval.LOW), 0);
		if (violatingHours > 0) {
			if (!isEnd) {
				final int lowHours = Math.min(idleTimeComponentParameters.getThreshold(Interval.HIGH) - idleTimeComponentParameters.getThreshold(Interval.LOW), violatingHours);
				final int highHours = violatingHours - lowHours;
				final long penaltyLow = lowHours * idleTimeComponentParameters.getWeight(Interval.LOW);
				final long penaltyHigh = highHours * idleTimeComponentParameters.getWeight(Interval.HIGH);
				if (annotatedSolution != null) {
					Integer gaLow = annotatedSolution.getGeneralAnnotation(GA_IDLE_TIME_HOURS_LOW, Integer.class);
					Integer gaHigh = annotatedSolution.getGeneralAnnotation(GA_IDLE_TIME_HOURS_HIGH, Integer.class);
					if (gaLow == null) {
						gaLow = 0;
					}
					if (gaHigh == null) {
						gaHigh = 0;
					}
					gaLow += lowHours;
					gaHigh += highHours;
					annotatedSolution.setGeneralAnnotation(GA_IDLE_TIME_HOURS_LOW, gaLow);
					annotatedSolution.setGeneralAnnotation(GA_IDLE_TIME_HOURS_HIGH, gaHigh);
				}
				return penaltyLow + penaltyHigh;
			} else {
				return violatingHours * idleTimeComponentParameters.getEndWeight();
			}
		} else {
			return 0L;
		}
	}

	private int getViolatingHours(final int idleTimeInHours) {
		return Math.max(idleTimeInHours - idleTimeComponentParameters.getThreshold(Interval.LOW), 0);
	}
}
