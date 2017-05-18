/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortCooldownDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProviderEditor;

/**
 * Implementation of {@link IElementPortProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapPortCooldownDataProviderEditor implements IPortCooldownDataProviderEditor {

	private final Map<@NonNull IPort, Boolean> arriveCold = new HashMap<>();
	private final Map<@NonNull IPort, ICooldownCalculator> cooldownCalculators = new HashMap<>();

	@Override
	public boolean shouldVesselsArriveCold(@NonNull IPort port) {
		return arriveCold.getOrDefault(port, false);
	}

	@Override
	public void setShouldVesselsArriveCold(@NonNull IPort port, final boolean arriveCold) {
		this.arriveCold.put(port, arriveCold);
	}

	@Override
	public ICooldownCalculator getCooldownCalculator(@NonNull IPort port) {
		return cooldownCalculators.get(port);
	}

	@Override
	public void setCooldownCalculator(@NonNull IPort port, final ICooldownCalculator cooldownCalculator) {
		this.cooldownCalculators.put(port, cooldownCalculator);
	}
}