/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
public interface IPortCVProviderEditor extends IPortCVProvider {

	void setPortCV(IPort port, int cv);
}
