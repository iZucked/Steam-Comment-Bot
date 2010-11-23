/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

/**
 * An annotation which is appended to both load and discharge slots
 * indicating decisions made by the cargo allocator.
 * 
 * @author hinton
 *
 */
public interface IAllocationAnnotation {
	public long getDischargeVolume();
	public long getDischargeCV(); //hmm?
	public int getUnitProfit();
	public long getTotalProfit();
}
