/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author Alex
 *
 */
public interface ISimpleCostsProviderEditor extends ISimpleCostsProvider {

	void setCostsValue(IPortSlot portSlot, long cost);
	
}
