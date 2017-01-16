/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;

public class ProfitAndLossEntryTest {

	@Test
	public void test() {

		final IEntityBook entityBook = mock(IEntityBook.class);
		final long groupValue = 1234567890L;
		final long groupValuePreTax = 2234567890L;
		final IDetailTree details = mock(IDetailTree.class);

		final ProfitAndLossEntry entry = new ProfitAndLossEntry(entityBook, groupValue, groupValuePreTax, details);

		Assert.assertSame(entityBook, entry.getEntityBook());
		Assert.assertEquals(groupValue, entry.getFinalGroupValue());
		Assert.assertSame(details, entry.getDetails());
	}

}
