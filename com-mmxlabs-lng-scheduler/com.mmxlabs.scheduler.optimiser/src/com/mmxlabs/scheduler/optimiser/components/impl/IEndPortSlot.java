/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public interface IEndPortSlot extends IPortSlot {

	boolean isEndCold();

	long getTargetEndHeelInM3();

}
