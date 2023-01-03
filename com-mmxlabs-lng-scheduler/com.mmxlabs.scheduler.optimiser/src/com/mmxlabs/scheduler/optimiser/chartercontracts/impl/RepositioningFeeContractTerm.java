/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.chartercontracts.IRepositioningFeeTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/***
 * A repositioning contract rule
 * 
 * @author FM
 *
 */
@NonNullByDefault
public abstract class RepositioningFeeContractTerm implements IRepositioningFeeTerm {
	/**
	 * The collections of ports the vessel can start at to match this term.
	 */
	private final Set<IPort> startPorts;

	protected RepositioningFeeContractTerm(final Set<IPort> startPorts) {
		this.startPorts = startPorts;
	}

	public Set<IPort> getStartPorts() {
		return startPorts;
	}
}
