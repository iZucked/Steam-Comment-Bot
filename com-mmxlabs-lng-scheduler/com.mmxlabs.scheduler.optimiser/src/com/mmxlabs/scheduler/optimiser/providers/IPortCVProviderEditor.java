/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Editor interface for a {@link IPortCostProvider}
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public interface IPortCVProviderEditor extends IPortCVProvider {

	void setPortCV(IPort port, int cv);
}
