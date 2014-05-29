/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

/**
 * Simple interface describing a contractual entity.
 * 
 * @author hinton
 * 
 */
public interface IEntity {

	public String getName();

	IEntityBook getShippingBook();

	IEntityBook getTradingBook();
}
