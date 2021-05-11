/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/***
 * A ballast bonus contract rule
 * @author alex
 *
 */
public abstract class BallastBonusContractTerm implements ICharterContractTerm {
	private final Set<IPort> redeliveryPorts;

	public BallastBonusContractTerm(final Set<IPort> redeliveryPorts) {
		this.redeliveryPorts = redeliveryPorts;
	}
	
	public Set<IPort> getRedeliveryPorts() {
		return redeliveryPorts;
	}

}
