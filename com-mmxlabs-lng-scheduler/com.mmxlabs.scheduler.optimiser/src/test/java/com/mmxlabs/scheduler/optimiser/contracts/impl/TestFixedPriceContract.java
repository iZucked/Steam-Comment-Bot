/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import org.junit.Test;

/**
 * @author Tom Hinton
 * 
 */
public class TestFixedPriceContract {
	@Test
	public void testComputeFixedPrice() {
		final FixedPriceContract contract = new FixedPriceContract(35353);
		assert (contract.calculateLoadUnitPrice(null, null, 0, 0, 0, 0, 0, null, null, null) == 35353);
	}
}
