/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author berkan
 *
 */
public interface ICancellationFeeProviderEditor extends ICancellationFeeProvider {

	/**
	 * @param portSlot
	 * @param cancellationFeeCurve
	 *            expression curve
	 */
	void setCancellationExpression(@NonNull IPortSlot portSlot, @NonNull ILongCurve cancellationFeeCurve);

}
