/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.impl;

import org.junit.Assert;
import org.junit.Test;

public class EntityTest {

	@Test
	public void testEntity() {
		final String name = "name";

		final Entity entity = new Entity(name) {

			@Override
			public int getUpstreamTransferPrice(final int loadPricePerM3, final int cvValue) {
				Assert.fail("Not part of test");
				return 0;
			}

			@Override
			public long getTaxedProfit(final long downstreamTotalPretaxProfit, final int time) {
				Assert.fail("Not part of test");
				return 0;
			}

			@Override
			public int getDownstreamTransferPrice(final int dischargePricePerM3, final int cvValue) {
				Assert.fail("Not part of test");
				return 0;
			}
		};

		Assert.assertSame(name, entity.getName());
	}

}
