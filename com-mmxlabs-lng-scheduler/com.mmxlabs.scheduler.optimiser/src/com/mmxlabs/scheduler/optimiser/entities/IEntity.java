/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Simple interface describing a contractual entity.
 * 
 * @author hinton
 * 
 */
public interface IEntity {

	public String getName();

	@NonNull
	IEntityBook getShippingBook();

	@NonNull
	IEntityBook getTradingBook();

	@NonNull
	IEntityBook getUpstreamBook();
}
