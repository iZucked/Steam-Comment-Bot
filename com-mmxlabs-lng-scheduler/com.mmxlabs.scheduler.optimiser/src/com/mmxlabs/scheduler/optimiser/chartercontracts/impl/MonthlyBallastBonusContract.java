/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.List;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.MonthlyBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class MonthlyBallastBonusContract extends DefaultCharterContract {

	public MonthlyBallastBonusContract(List<ICharterContractTerm> rules) {
		super(rules);
	}
	
	@Override
	public long calculateCost(final IPort loadPort, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		if (lastSlot.getPortType() == PortType.End) {
			ICharterContractTerm matchingRule = getMatchingRule(loadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
			if (matchingRule != null) {
				return matchingRule.calculateCost(loadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
			}
		}
		return 0L;
	}

	private ICharterContractTerm getMatchingRule(final IPort loadPort, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		for (final ICharterContractTerm rule : terms) {
			if (rule.match(loadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime)) {
				return rule;
			}
		}
		
		//No exact match find last available monthly rule.
		MonthlyBallastBonusContractTerm latestRule = null;
		
		for (final ICharterContractTerm rule : terms) {
			if (rule instanceof MonthlyBallastBonusContractTerm) {
				MonthlyBallastBonusContractTerm mmRule = (MonthlyBallastBonusContractTerm)rule;
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
	public ICharterContractAnnotation annotate(final IPort firstLoadPort, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {

		final CharterContractAnnotation ballastBonusAnnotation = new CharterContractAnnotation();
		if (lastSlot.getPortType() == PortType.End) {
			ICharterContractTerm rule = getMatchingRule(firstLoadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
			if (rule == null) {
				throw new UserFeedbackException("Missing matching monthly ballast bonus contract rule.");
			}
			ballastBonusAnnotation.cost = rule.calculateCost(firstLoadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
			ballastBonusAnnotation.matchedPort = lastSlot.getPort();
			ballastBonusAnnotation.termAnnotation = rule.annotate(firstLoadPort, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		}
		return ballastBonusAnnotation;
	}
}
