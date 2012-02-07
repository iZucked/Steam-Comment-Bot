/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

/**
 * An interface for load price calculators which depend only on the time of loading. These are used for pricing cooldown gas, where the onward travel details are not known because load to load triples
 * are processed separately.
 * 
 * @author hinton
 * 
 */
public interface ISimpleLoadPriceCalculator {
	public int calculateSimpleLoadUnitPrice(final int loadTime);
}
