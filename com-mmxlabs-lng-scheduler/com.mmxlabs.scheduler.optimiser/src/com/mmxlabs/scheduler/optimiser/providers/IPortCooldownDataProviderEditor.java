/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;

public interface IPortCooldownDataProviderEditor extends IPortCooldownDataProvider {

	void setShouldVesselsArriveCold(@NonNull IPort port, boolean arriveCold);

	void setCooldownCalculator(@NonNull IPort port, ICooldownCalculator cooldownCalculator);

}
