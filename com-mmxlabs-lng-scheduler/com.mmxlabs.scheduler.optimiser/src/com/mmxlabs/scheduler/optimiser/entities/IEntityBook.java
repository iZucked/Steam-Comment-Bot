/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

public interface IEntityBook {

	IEntity getEntity();
	
	EntityBookType getBookType();

	long getTaxedProfit(long pretaxValue, int time);
}
