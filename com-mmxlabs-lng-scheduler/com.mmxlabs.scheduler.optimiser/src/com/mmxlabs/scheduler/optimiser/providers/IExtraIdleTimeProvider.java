/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface IExtraIdleTimeProvider extends IDataComponentProvider {

	int getContingencyIdleTimeInHours(IPortSlot fromPortSlot, IPortSlot toPortSlot);
	int getBufferIdleTimeInHours( IPortSlot toPortSlot);

//	int getExtraIdleTimeInHours(ISequenceElement fromElement, ISequenceElement toElement);

}
