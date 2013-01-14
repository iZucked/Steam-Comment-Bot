/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.curves.ICurve;

public class SimpleEntityTest {

	@Test
	public void testGetDownstreamTransferPrice() {

		final SimpleEntity entity = new SimpleEntity("name", 500000, null, 2000000);
		// 1234 - (scaled)500 * 2000
		Assert.assertEquals(1234 - 10000, entity.getDownstreamTransferPrice(1234, 5000));
	}

	@Test
	public void testGetUpstreamTransferPrice() {

		final SimpleEntity entity = new SimpleEntity("name", 500000, null, 2000000);
		// 1234 + (scaled)500 * 2000
		Assert.assertEquals(1234 + 10000, entity.getUpstreamTransferPrice(1234, 5000));
	}

	@Test
	public void testGetTaxedProfit() {
		final ICurve curve = mock(ICurve.class);

		final SimpleEntity entity = new SimpleEntity("name", 500000, curve, 2000000);

		final int time = 12345;

		// 50 %
		when(curve.getValueAtPoint(time)).thenReturn(500);

		// 50% * 50% * 10000
		Assert.assertEquals(2500, entity.getTaxedProfit(10000, time));

		verify(curve).getValueAtPoint(time);

		verifyNoMoreInteractions(curve);
	}

}
