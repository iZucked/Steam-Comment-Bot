/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContractRule;

public class DefaultBallastBonusContract implements IBallastBonusContract {

	protected final List<IBallastBonusContractRule> rules;

	public DefaultBallastBonusContract(final List<IBallastBonusContractRule> rules) {
		this.rules = rules;
	}

	@Override
	public long calculateBallastBonus(final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		for (final IBallastBonusContractRule rule : rules) {
			if (rule.match(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime)) {
				return rule.calculateBallastBonus(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
			}
		}
		return 0L;
	}

	@Override
	public BallastBonusAnnotation annotate(final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		return createAnnotation(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
	}

	private BallastBonusAnnotation createAnnotation(final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		final BallastBonusAnnotation ballastBonusAnnotation = new BallastBonusAnnotation();
		for (final IBallastBonusContractRule rule : rules) {
			if (rule.match(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime)) {
				ballastBonusAnnotation.ballastBonusFee = rule.calculateBallastBonus(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
				ballastBonusAnnotation.matchedPort = lastSlot.getPort();
				ballastBonusAnnotation.ballastBonusRuleAnnotation = rule.annotate(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
				break;
			}
		}
		return ballastBonusAnnotation;
	}
}
