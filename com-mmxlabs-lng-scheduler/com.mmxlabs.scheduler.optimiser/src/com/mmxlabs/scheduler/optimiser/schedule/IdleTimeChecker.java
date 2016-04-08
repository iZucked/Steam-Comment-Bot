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
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
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
 * @author achurchill
 *
 */
public class IdleTimeChecker {

	@Inject
	IExcessIdleTimeComponentParameters idleTimeComponentParameters;
	
	/**
	 * 
	 * @param scheduledSequences
	 * @param annotatedSolution
	 */
	public void calculateIdleTime(final ScheduledSequences scheduledSequences, @Nullable final IAnnotatedSolution annotatedSolution) {
		for (ScheduledSequence scheduledSequence : scheduledSequences) {
			List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans = scheduledSequence.getVoyagePlans();
			for (Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> planStructure : voyagePlans) {
				VoyagePlan plan = planStructure.getFirst();
				applyPenaltyToPlan(scheduledSequences, plan);
			}
		}
	}

	public void applyPenaltyToPlan(final ScheduledSequences scheduledSequences, VoyagePlan plan) {
		if (LDCargoDetailsWrapper.isCargoVoyage(plan.getSequence())) {
			LDCargoDetailsWrapper cargoDetails = new LDCargoDetailsWrapper(plan.getSequence());
			if (cargoDetails.getLoadOption() instanceof ILoadSlot) {
				ILoadSlot load = (ILoadSlot) cargoDetails.getLoadOption();
				if (load.getLoadPriceCalculator() instanceof IExcessIdleTimeConstrainedSlotProvider) {
					IDischargeOption discharge = cargoDetails.getDischargeOption();
					IPortSlot returnSlot = cargoDetails.getBallast().getOptions().getToPortSlot();
					cargoDetails.getDischarge().getOptions().getPortSlot();
					eIdleDetails idleDetails = ((IExcessIdleTimeConstrainedSlotProvider) load.getLoadPriceCalculator()).getSlotIdleConstraintDetails(load, discharge, returnSlot);
					if (idleDetails == eIdleDetails.LADEN || idleDetails == eIdleDetails.BOTH) {
						// penalty on laden
						addPenalty(scheduledSequences, cargoDetails.getLaden());
					}
					if (idleDetails == eIdleDetails.BALLAST || idleDetails == eIdleDetails.BOTH) {
						// penalty on ballast
						addPenalty(scheduledSequences, cargoDetails.getBallast());
					}
				}
			}
		}
	}

	public void addPenalty(final ScheduledSequences scheduledSequences, VoyageDetails voyageDetails) {
		int idleTimeInHours = voyageDetails.getIdleTime();
		int violatingHours = getViolatingHours(idleTimeInHours);
		if (violatingHours > 0) {
			IPortSlot idleTimeSlot = voyageDetails.getOptions().getFromPortSlot();
			boolean isEnd = voyageDetails.getOptions().getToPortSlot() instanceof EndPortSlot;
			long penalty = getIdleTimePenalty(idleTimeInHours, isEnd);
			scheduledSequences.addIdleHoursViolation(idleTimeSlot, violatingHours);
			scheduledSequences.addIdleWeightedCost(idleTimeSlot, penalty);
		}
	}

	private long getIdleTimePenalty(int idleTimeInHours, boolean isEnd) {
		int violatingHours = Math.max(idleTimeInHours - idleTimeComponentParameters.getThreshold(Interval.LOW), 0);
		if (violatingHours > 0) {
			if (!isEnd) {
				int lowHours = Math.min(idleTimeComponentParameters.getThreshold(Interval.HIGH) - idleTimeComponentParameters.getThreshold(Interval.LOW), violatingHours);
				int highHours = violatingHours - lowHours;
				long penaltyLow = lowHours * idleTimeComponentParameters.getWeight(Interval.LOW);
				long penaltyHigh = highHours * idleTimeComponentParameters.getWeight(Interval.HIGH);
				return penaltyLow + penaltyHigh;
			} else {
				return violatingHours * idleTimeComponentParameters.getEndWeight();
			}
		} else {
			return 0L;
		}
	}

	private int getViolatingHours(int idleTimeInHours) {
		return Math.max(idleTimeInHours - idleTimeComponentParameters.getThreshold(Interval.LOW), 0);
	}
}
