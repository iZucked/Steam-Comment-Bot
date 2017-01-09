/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

/**
 * Interface representing a physical load slot commitment, with the features of an {@link ILoadOption} together with physical specific rules around cooldown.
 * 
 * @author Simon Goodall
 * 
 */
public interface ILoadSlot extends ILoadOption {
	/**
	 * If true, {@link #isCooldownRequired()} returns true if a cooldown is to be performed at this slot if the vessel warms up, and false if a cooldown is not to be performed at this slot if it can
	 * be avoided.
	 * 
	 * Otherwise, both cooldown options will be considered
	 * 
	 * @return
	 */
	boolean isCooldownSet();

	/**
	 * If {@link #isCooldownSet()} is true, this flag constrains the cooldown decision on arrival at this port (true => cooldown will be used if necessary, false => cooldown will be avoided if at all
	 * possible)
	 * 
	 * @return
	 */
	boolean isCooldownForbidden();
}
