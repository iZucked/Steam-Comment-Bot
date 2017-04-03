/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

public interface IHeelOptionConsumerPortSlot extends IPortSlot {
	@NonNull
	IHeelOptionConsumer getHeelOptionsConsumer();
}
