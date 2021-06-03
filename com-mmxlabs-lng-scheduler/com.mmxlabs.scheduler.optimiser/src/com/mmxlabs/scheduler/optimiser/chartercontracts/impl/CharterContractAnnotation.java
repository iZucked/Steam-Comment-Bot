/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class CharterContractAnnotation implements ICharterContractAnnotation {
	public IPort matchedPort = null;
	public long cost = 0L;
	public ICharterContractTermAnnotation termAnnotation = null;
}
