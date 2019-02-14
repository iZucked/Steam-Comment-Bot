/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface IExtraIdleTimeProviderEditor extends IExtraIdleTimeProvider {

	void setExtraIdleTimeOnVoyage(IPort fromPort, IPort toPort, int extraIdleTimeInHours);

	void setExtraIdleTimeAfterVisit(IPort port, PortType portType, int extraIdleTimeInHours);

	// void setExtraIdleTimeBeforeVisit(ISequenceElement element, IPortSlot portSlot, int extraIdleTimeInHours);

	// void setExtraIdleTimeAfterVisit(ISequenceElement element, IPortSlot portSlot, int extraIdleTimeInHours);

}
