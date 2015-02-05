/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.nio.channels.IllegalSelectorException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.INextLoadDateProviderEditor;

public class DefaultNextLoadDateProvider implements INextLoadDateProviderEditor {

	private static class DefaultNextLoadDate implements INextLoadDate {

		private final int time;
		private final ILoadOption loadOption;

		DefaultNextLoadDate(final int time, final ILoadOption loadOption) {
			this.time = time;
			this.loadOption = loadOption;
		}

		@Override
		public int getTime() {
			return time;
		}

		@Override
		@Nullable
		public ILoadOption getNextSlot() {
			return loadOption;
		}

	}

	private final Map<ILoadPriceCalculator, Integer> contractToConstantSpeedMap = new HashMap<>();
	private final Map<ILoadPriceCalculator, Rule> contractToRuleMap = new HashMap<>();
	private final Map<ILoadPriceCalculator, Set<ILoadOption>> contractToSlotsMap = new HashMap<>();

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Override
	public INextLoadDate getNextLoadDate(final ILoadOption origin, final IPort fromPort, final int time, final IVessel vessel) {

		final ILoadPriceCalculator contract = origin.getLoadPriceCalculator();
		final Rule rule = contractToRuleMap.get(contract);
		assert rule != null;
		final int speed;
		switch (rule) {
		case Constant_Speed:
			speed = contractToConstantSpeedMap.get(contract);
			break;
		case Max_Speed:
			speed = vessel.getVesselClass().getMaxSpeed();
			break;
		case Service_Speed:
			speed = vessel.getVesselClass().getServiceSpeed(VesselState.Ballast);
			break;
		default:
			throw new IllegalSelectorException();
		}

		final int distance = distanceProvider.getMinimumValue(fromPort, origin.getPort());
		final int ballastTime = Calculator.getTimeFromSpeedDistance(speed, distance);

		final int returnTime = time + ballastTime;
		// TODO: treemap?
		final Set<ILoadOption> slots = contractToSlotsMap.get(contract);
		ILoadOption nextSlot = null;

		// Find closest slot
		for (final ILoadOption o : slots) {
			if (o.getTimeWindow().getEnd() >= returnTime) {
				// First slot found after the return date
				if (nextSlot == null) {
					nextSlot = o;
				} else {
					// Is this slot closer?
					if (o.getTimeWindow().getEnd() < nextSlot.getTimeWindow().getEnd()) {
						nextSlot = o;
					}
				}
			}
		}

		if (nextSlot == null) {
			return new DefaultNextLoadDate(returnTime, null);
		} else {
			return new DefaultNextLoadDate(Math.max(returnTime, nextSlot.getTimeWindow().getStart()), nextSlot);
		}
	}

	@Override
	public void addSlotForContract(final ILoadPriceCalculator contract, final ILoadOption slot) {
		final Set<ILoadOption> slots;
		if (contractToSlotsMap.containsKey(contract)) {
			slots = contractToSlotsMap.get(contract);
		} else {
			slots = new TreeSet<>(new SlotComparator());
			contractToSlotsMap.put(contract, slots);
		}
		slots.add(slot);
	}

	@Override
	public void setRuleForNoSlot(final ILoadPriceCalculator contract, final Rule rule) {
		contractToRuleMap.put(contract, rule);
	}

	@Override
	public void setConstantSpeed(final ILoadPriceCalculator contract, final int constantSpeed) {
		contractToConstantSpeedMap.put(contract, constantSpeed);
	}

	private static final class SlotComparator implements Comparator<ILoadOption> {

		@Override
		public int compare(final ILoadOption o1, final ILoadOption o2) {

			return o1.getTimeWindow().getStart() - o2.getTimeWindow().getStart();
		}
	}
}
