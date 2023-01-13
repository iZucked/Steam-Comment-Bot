/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface IExtraIdleTimeProviderEditor extends IExtraIdleTimeProvider {
	void setExtraIdleTimeOnVoyage(IPort fromPort, IPort toPort, int extraIdleTimeInHours);
	void setExtraIdleTimeBeforeVisit(ISequenceElement element, IPortSlot toPortSlot, int extraIdleTimeInHours);
	
	//Below are redundant as can to with setExtraIdelTimeOnVoyage:
	// void setExtraIdleTimeAfterVisit(IPort port, PortType portType, int extraIdleTime`nHours);	
	// void setExtraIdleTimeAfterVisit(ISequenceElement element, IPortSlot portSlot, int extraIdleTimeInHours);
}
