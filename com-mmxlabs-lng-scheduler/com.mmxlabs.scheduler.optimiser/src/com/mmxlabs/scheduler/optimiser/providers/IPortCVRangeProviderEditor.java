/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Editor interface for a {@link IPortCostProvider}
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortCVRangeProviderEditor extends IPortCVRangeProvider {

	void setPortMinCV(IPort port, long cv);
	void setPortMaxCV(IPort port, long cv);
}
