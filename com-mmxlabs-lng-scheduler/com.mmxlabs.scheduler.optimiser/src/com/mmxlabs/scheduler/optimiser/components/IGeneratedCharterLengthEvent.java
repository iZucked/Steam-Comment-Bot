/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

@NonNullByDefault
public interface IGeneratedCharterLengthEvent extends ICharterLengthEvent {

	void setHeelConsumer(IHeelOptionConsumer heelConsumer);

	void setHeelSupplier(IHeelOptionSupplier heelSupplier);

	IHeelOptionSupplier getHeelOptionsSupplier();

	IHeelOptionConsumer getHeelOptionsConsumer();
}
