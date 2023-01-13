/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations;

import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/***
 * A repositioning fee lump sum term annotation
 * 
 * @author FM
 *
 */
public class LumpSumRepositioningFeeTermAnnotation implements ICharterContractTermAnnotation {
	public long lumpSum;
	public IPort matchingPort;
}
