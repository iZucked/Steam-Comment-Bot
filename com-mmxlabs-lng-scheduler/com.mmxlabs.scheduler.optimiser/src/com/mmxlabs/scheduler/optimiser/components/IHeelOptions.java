/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

/**
 * Stores data which a slot with heel options should provide.
 * 
 * @author hinton
 * 
 */
public interface IHeelOptions {
	public int getHeelUnitPrice();

	public long getHeelLimit();

	public int getHeelCVValue();
}
