/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

		Assertions.assertSame(entityBook, entry.getEntityBook());
		Assertions.assertEquals(groupValue, entry.getFinalGroupValue());
		Assertions.assertSame(details, entry.getDetails());
	}

}
