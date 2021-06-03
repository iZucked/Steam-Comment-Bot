/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/***
 * A repositioning contract rule
 * @author FM
 *
 */
public abstract class RepositioningFeeContractTerm implements ICharterContractTerm {
	private final @NonNull IPort originPort;
	
	public RepositioningFeeContractTerm(final @NonNull IPort originPort) {
		this.originPort = originPort;
	}
	
	public IPort getOriginPort() {
		return originPort;
	}
}
