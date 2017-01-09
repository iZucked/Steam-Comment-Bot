/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

public interface INextLoadDateProviderEditor extends INextLoadDateProvider {

	public enum Rule {
		Service_Speed, Max_Speed, Constant_Speed
	}

	void addSlotForContract(@NonNull ILoadPriceCalculator contract, @NonNull ILoadOption slot);

	/**
	 * When there are no feasible slots, we model based on a speed determined by the rule. If the rule is {@link Rule#Constant_Speed} then {@link #setConstantSpeed(ILoadPriceCalculator, int)} must
	 * also be called.
	 * 
	 * @param contract
	 * @param rule
	 */
	void setRuleForNoSlot(@NonNull ILoadPriceCalculator contract, @NonNull Rule rule);

	/**
	 * If the "Rule for No Slot" is {@link Rule#Constant_Speed} then this method must be called to specify the constant speed.
	 * 
	 * @param contract
	 * @param constantSpeed
	 */
	void setConstantSpeed(@NonNull ILoadPriceCalculator contract, int constantSpeed);

	/**
	 * Set an explicit date to use for the given slot
	 * 
	 * @param slot
	 * @param time
	 */
	void setExplicitTimeForSlot(@NonNull ILoadOption slot, int time);
}
