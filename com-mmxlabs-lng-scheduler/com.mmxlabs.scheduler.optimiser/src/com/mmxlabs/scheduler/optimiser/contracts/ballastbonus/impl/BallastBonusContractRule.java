/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import java.util.List;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContractRule;

/***
 * A ballast bonus contract rule
 * @author alex
 *
 */
public abstract class BallastBonusContractRule implements IBallastBonusContractRule {
	private final Set<IPort> redeliveryPorts;

	public BallastBonusContractRule(final Set<IPort> redeliveryPorts) {
		this.redeliveryPorts = redeliveryPorts;
	}
	
	public Set<IPort> getRedeliveryPorts() {
		return redeliveryPorts;
	}

}
