/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author berkan
 *
 */
public interface ICancellationFeeProviderEditor extends ICancellationFeeProvider {

	/**
	 * @param portSlot
	 * @param cost
	 */
	public void setCancellationFee(final IPortSlot portSlot, final long cost);

}
