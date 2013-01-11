/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.contracts.IEntity;

public class ProfitAndLossEntryTest {

	@Test
	public void test() {

		final IEntity entity = mock(IEntity.class);
		final long groupValue = 1234567890l;
		final IDetailTree details = mock(IDetailTree.class);

		final ProfitAndLossEntry entry = new ProfitAndLossEntry(entity, groupValue, details);

		Assert.assertSame(entity, entry.getEntity());
		Assert.assertEquals(groupValue, entry.getFinalGroupValue());
		Assert.assertSame(details, entry.getDetails());
	}

}
