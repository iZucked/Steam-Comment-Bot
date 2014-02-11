/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

public interface IEntityBook {

	EntityBookType getBookType();

	long getTaxedProfit(long pretaxValue, int time);
}
