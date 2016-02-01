/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.entities.EntityBookType;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

public class DefaultEntityBookTest {

	@Test
	public void testGetTaxedProfit() {
		final ICurve curve = mock(ICurve.class);
		final IEntity entity = mock(IEntity.class);

		final DefaultEntityBook entityBook = new DefaultEntityBook(entity, EntityBookType.Trading, curve);

		final int time = 12345;

		// 50% * 10000
		Assert.assertEquals(10000, entityBook.getTaxedProfit(10000, time));

		verify(curve).getValueAtPoint(time);

		verifyNoMoreInteractions(curve);
	}

}
