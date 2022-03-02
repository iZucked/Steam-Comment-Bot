/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelProviderEditor;

public class DefaultBaseFuelProvider implements IBaseFuelProviderEditor {

	private final @NonNull List<@NonNull IBaseFuel> baseFuels = new LinkedList<>();

	private int numberOfBaseFuels;

	@Override
	public void registerBaseFuel(final @NonNull IBaseFuel baseFuel) {
		baseFuels.add(baseFuel);
		numberOfBaseFuels = Math.max(numberOfBaseFuels, baseFuel.getIndex() + 1);
	}

	@Override
	public @NonNull Collection<@NonNull IBaseFuel> getBaseFuels() {
		return baseFuels;
	}

	@Override
	public int getNumberOfBaseFuels() {
		return numberOfBaseFuels;
	}
}
