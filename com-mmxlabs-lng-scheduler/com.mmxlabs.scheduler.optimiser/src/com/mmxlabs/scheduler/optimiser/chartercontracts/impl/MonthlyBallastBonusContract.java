/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.List;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.scheduler.optimiser.chartercontracts.IBallastBonusTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.IRepositioningFeeTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.MonthlyBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class MonthlyBallastBonusContract extends DefaultCharterContract {

	public MonthlyBallastBonusContract(List<IBallastBonusTerm> bbTerms, List<IRepositioningFeeTerm> rfTerms) {
		super(bbTerms, rfTerms);
	}
	
	@Override
	public long calculateBBCost(final IPortTimesRecord portTimesRecord, final IPortSlot portSlot, final IVesselCharter vesselCharter, 
			final int vesselStartTime, final IPort firstLoadPort) {
		if (portSlot.getPortType() == PortType.End) {
			IBallastBonusTerm matchingRule = getMatchingRule(portTimesRecord, firstLoadPort, portSlot, vesselCharter, vesselStartTime);
			if (matchingRule != null) {
				return matchingRule.calculateCost(portTimesRecord, vesselCharter, vesselStartTime, firstLoadPort);
			}
		}
		return 0L;
	}

	private IBallastBonusTerm getMatchingRule(final IPortTimesRecord portTimesRecord, final IPort firstLoadPort, final IPortSlot portSlot, 
			final IVesselCharter vesselCharter, final int vesselStartTime) {
		for (final IBallastBonusTerm rule : bbTerms) {
			if (rule.match(portTimesRecord, vesselCharter, vesselStartTime, firstLoadPort)) {
				return rule;
			}
		}
		
		//No exact match find last available monthly rule.
		MonthlyBallastBonusContractTerm latestRule = null;
		
		for (final IBallastBonusTerm rule : bbTerms) {
			if (rule instanceof MonthlyBallastBonusContractTerm mmRule) {
				if (mmRule.matchWithoutDates(portSlot)) {
					if (vesselStartTime >= mmRule.getMonthStartInclusive() && (latestRule == null || mmRule.getMonthStartInclusive() >= latestRule.getMonthStartInclusive())) {
						latestRule = mmRule;
					}
				}
			}
		}
		
		return latestRule;
	}
	
	
	@Override
	public ICharterContractAnnotation annotateBB(final IPortTimesRecord portTimesRecord, final IPortSlot portSlot, final IVesselCharter vesselCharter, 
			final int vesselStartTime, final IPort firstLoadPort) {

		final CharterContractAnnotation ballastBonusAnnotation = new CharterContractAnnotation();
		if (portSlot.getPortType() == PortType.End) {
			IBallastBonusTerm rule = getMatchingRule(portTimesRecord, firstLoadPort, portSlot, vesselCharter, vesselStartTime);
			if (rule == null) {
				throw new UserFeedbackException("Missing matching monthly ballast bonus contract rule.");
			}
			ballastBonusAnnotation.cost = rule.calculateCost(portTimesRecord, vesselCharter, vesselStartTime, firstLoadPort);
			ballastBonusAnnotation.matchedPort = portSlot.getPort();
			ballastBonusAnnotation.termAnnotation = rule.annotate(portTimesRecord, vesselCharter, vesselStartTime, firstLoadPort);
		}
		return ballastBonusAnnotation;
	}
}
