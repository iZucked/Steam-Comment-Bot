/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContractRule;

public class DefaultBallastBonusContract implements IBallastBonusContract {

	List<IBallastBonusContractRule> rules;
	
	public DefaultBallastBonusContract(List<IBallastBonusContractRule> rules) {
		this.rules = rules;
	}
	
	@Override
	public long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time) {
		for (IBallastBonusContractRule rule : rules) {
			if (rule.match(lastSlot, vesselAvailability, time)) {
				return rule.calculateBallastBonus(lastSlot, vesselAvailability, time);
			}
		}
		return 0L;
	}
	
	@Override
	public BallastBonusAnnotation annotate(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time) {
		return createAnnotation(lastSlot, vesselAvailability, time);
	}
	
	private BallastBonusAnnotation createAnnotation(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time) {
		BallastBonusAnnotation ballastBonusAnnotation = new BallastBonusAnnotation();
		for (IBallastBonusContractRule rule : rules) {
			if (rule.match(lastSlot, vesselAvailability, time)) {
				ballastBonusAnnotation.ballastBonusFee = rule.calculateBallastBonus(lastSlot, vesselAvailability, time);
				ballastBonusAnnotation.matchedPort = lastSlot.getPort();
				ballastBonusAnnotation.ballastBonusRuleAnnotation = rule.annotate(lastSlot, vesselAvailability, time);
				break;
			}
		}
		return ballastBonusAnnotation;
	}
}
