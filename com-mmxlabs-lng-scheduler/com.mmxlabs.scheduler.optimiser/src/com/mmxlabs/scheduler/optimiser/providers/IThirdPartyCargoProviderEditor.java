/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

@NonNullByDefault
public interface IThirdPartyCargoProviderEditor extends IThirdPartyCargoProvider {
	void addThirdPartyCargo(ILoadOption loadOption, IDischargeOption dischargeOption);
}
