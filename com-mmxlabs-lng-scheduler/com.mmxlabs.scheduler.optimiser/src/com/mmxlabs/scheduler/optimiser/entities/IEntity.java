/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Simple interface describing a contractual entity.
 * 
 * @author hinton
 * 
 */
@NonNullByDefault
public interface IEntity {

	public String getName();

	IEntityBook getShippingBook();

	IEntityBook getTradingBook();

	IEntityBook getUpstreamBook();

	boolean isThirdparty();
}
