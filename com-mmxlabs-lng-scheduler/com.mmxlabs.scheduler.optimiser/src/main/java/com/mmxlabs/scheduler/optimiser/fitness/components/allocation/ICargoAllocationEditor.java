/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author hinton
 *
 */
public interface ICargoAllocationEditor<T> extends ICargoAllocationProvider<T> {
	public void addCargoAllocationLimit(final Set<IPortSlot> slots, final int maximumQuantity);
}
