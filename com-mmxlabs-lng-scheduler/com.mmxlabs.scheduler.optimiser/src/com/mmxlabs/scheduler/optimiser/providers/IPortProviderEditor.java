/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IPortProviderEditor extends IPortProvider {

	void setPortForElement(IPort port, ISequenceElement element);
}
