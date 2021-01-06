/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import java.util.List;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContractRule;

public class MonthlyBallastBonusContract extends DefaultBallastBonusContract {

	public MonthlyBallastBonusContract(List<IBallastBonusContractRule> rules) {
		super(rules);
	}

	@Override
	public long calculateBallastBonus(final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		throw new IllegalArgumentException("Use new API passing lastSlot into calculateBallastBonus.");
	}
	
	@Override
	public long calculateBallastBonus(final IPort loadPort, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		IBallastBonusContractRule matchingRule = getMatchingRule(loadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		if (matchingRule != null) {
			return matchingRule.calculateBallastBonus(loadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		}
		return 0L;
	}

	private IBallastBonusContractRule getMatchingRule(final IPort loadPort, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		for (final IBallastBonusContractRule rule : rules) {
			if (rule.match(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime)) {
				return rule;
			}
		}
		
		//No exact match find last available monthly rule.
		MonthlyBallastBonusContractRule latestRule = null;
		
		for (final IBallastBonusContractRule rule : rules) {
			if (rule instanceof MonthlyBallastBonusContractRule) {
				MonthlyBallastBonusContractRule mmRule = (MonthlyBallastBonusContractRule)rule;
				if (mmRule.matchWithoutDates(lastSlot, vesselAvailability, vesselEndTime, vesselEndTime)) {
					if (vesselStartTime >= mmRule.getMonthStartInclusive() && (latestRule == null || mmRule.getMonthStartInclusive() >= latestRule.getMonthStartInclusive())) {
						latestRule = mmRule;
					}
				}
			}
		}
		
		return latestRule;
	}
	
	
	@Override
	public BallastBonusAnnotation annotate(final IPort firstLoadPort, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		return createAnnotation(firstLoadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
	}

	private BallastBonusAnnotation createAnnotation(final IPort firstLoadPort, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		final BallastBonusAnnotation ballastBonusAnnotation = new BallastBonusAnnotation();
		IBallastBonusContractRule rule = getMatchingRule(firstLoadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		if (rule == null) {
			throw new UserFeedbackException("Missing matching monthly ballast bonus contract.");
		}
		ballastBonusAnnotation.ballastBonusFee = rule.calculateBallastBonus(firstLoadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		ballastBonusAnnotation.matchedPort = lastSlot.getPort();
		ballastBonusAnnotation.ballastBonusRuleAnnotation = rule.annotate(firstLoadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		return ballastBonusAnnotation;
	}
}
