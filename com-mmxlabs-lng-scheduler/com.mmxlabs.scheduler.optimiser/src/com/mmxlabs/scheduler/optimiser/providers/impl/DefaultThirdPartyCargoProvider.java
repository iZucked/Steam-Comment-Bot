/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.IThirdPartyCargoProviderEditor;

@NonNullByDefault
public class DefaultThirdPartyCargoProvider implements IThirdPartyCargoProviderEditor {

	private final Set<Pair<ILoadOption, IDischargeOption>> thirdPartyCargoes = new HashSet<>();

	@Override
	public boolean isThirdPartyCargo(final ILoadOption loadOption, final IDischargeOption dischargeOption) {
		return thirdPartyCargoes.contains(Pair.of(loadOption, dischargeOption));
	}

	@Override
	public void addThirdPartyCargo(final ILoadOption loadOption, final IDischargeOption dischargeOption) {
		thirdPartyCargoes.add(Pair.of(loadOption, dischargeOption));
	}

}
