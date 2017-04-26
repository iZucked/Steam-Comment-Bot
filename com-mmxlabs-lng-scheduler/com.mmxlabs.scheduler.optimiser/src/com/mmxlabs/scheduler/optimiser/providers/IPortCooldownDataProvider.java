/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;

public interface IPortCooldownDataProvider extends IDataComponentProvider {

	/**
	 * Some ports do not typically provide cooldown facilities to inbound vessels. These ports will return true here, indicating that vessels should arrive cold. If a vessel <em>cannot</em> arrive
	 * cold, because its earlier journey does not allow any LNG to load, cooldown may still be performed.
	 * 
	 * @return true if cooldown is not routinely provided at this port.
	 */
	boolean shouldVesselsArriveCold(@NonNull IPort port);

	/**
	 * A {@link ICooldownCalculator} used for pricing cooldown gas.
	 * 
	 * @return
	 */
	@Nullable
	ICooldownCalculator getCooldownCalculator(@NonNull IPort port);
}
