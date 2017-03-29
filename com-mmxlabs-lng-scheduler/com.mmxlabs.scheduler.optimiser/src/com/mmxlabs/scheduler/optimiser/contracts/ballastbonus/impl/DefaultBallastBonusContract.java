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
	
}
