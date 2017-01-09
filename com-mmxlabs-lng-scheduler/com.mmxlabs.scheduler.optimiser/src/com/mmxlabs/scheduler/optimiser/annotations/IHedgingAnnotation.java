/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.optimiser.core.IElementAnnotation;

public interface IHedgingAnnotation extends IElementAnnotation {

	long getHedgingValue();
}
