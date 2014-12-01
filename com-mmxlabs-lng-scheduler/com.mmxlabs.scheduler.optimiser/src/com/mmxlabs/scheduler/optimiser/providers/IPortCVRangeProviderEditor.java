/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Editor interface for a {@link IPortCVRangeProvider}
 * 
 * @author Alex Churchill
 * 
 */
public interface IPortCVRangeProviderEditor extends IPortCVRangeProvider {

	void setPortMinCV(IPort port, int cv);
	void setPortMaxCV(IPort port, int cv);
}
