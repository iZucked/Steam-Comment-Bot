/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.optimiser.core.IElementAnnotation;

public interface ICancellationAnnotation extends IElementAnnotation {

	long getCancellationFees();
}
