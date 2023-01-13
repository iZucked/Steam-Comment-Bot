/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.chartercontracts.IBallastBonusTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/***
 * A ballast bonus contract rule
 * 
 * @author alex
 *
 */
@NonNullByDefault
public abstract class BallastBonusContractTerm implements IBallastBonusTerm {
	private final Set<IPort> redeliveryPorts;

	protected BallastBonusContractTerm(final Set<IPort> redeliveryPorts) {
		this.redeliveryPorts = redeliveryPorts;
	}

	public Set<IPort> getRedeliveryPorts() {
		return redeliveryPorts;
	}

}
