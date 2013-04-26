/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import org.junit.Assert;
import org.junit.Test;

public class OtherEntityTest {

	@Test
	public void testGetDownstreamTransferPrice() {
		final OtherEntity entity = new OtherEntity("name");
		Assert.assertEquals(1234, entity.getDownstreamTransferPrice(1234, 5000));
	}

	@Test
	public void testGetUpstreamTransferPrice() {
		final OtherEntity entity = new OtherEntity("name");
		Assert.assertEquals(1234, entity.getUpstreamTransferPrice(1234, 5000));
	}

	@Test
	public void testGetTaxedProfit() {
		final OtherEntity entity = new OtherEntity("name");
		Assert.assertEquals(0, entity.getTaxedProfit(1234, 5000));
	}

}
