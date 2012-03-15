/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.annotations.impl;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.trading.optimiser.IEntity;

@RunWith(JMock.class)
public class ProfitAndLossEntryTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void test() {

		final IEntity entity = context.mock(IEntity.class);
		final long groupValue = 1234567890l;
		final IDetailTree details = context.mock(IDetailTree.class);

		final ProfitAndLossEntry entry = new ProfitAndLossEntry(entity, groupValue, details);

		Assert.assertSame(entity, entry.getEntity());
		Assert.assertEquals(groupValue, entry.getFinalGroupValue());
		Assert.assertSame(details, entry.getDetails());
	}

}
