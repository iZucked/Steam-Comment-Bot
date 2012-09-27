/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossAnnotation;

public class ProfitAndLossAnnotationTest {

	@Test
	public void testProfitAndLossAnnotation() {

		final int bookingTime = 123456;
		final Collection<IProfitAndLossEntry> entries = Collections.emptySet();

		final ProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(bookingTime, entries);

		Assert.assertEquals(bookingTime, annotation.getBookingTime());
		Assert.assertSame(entries, annotation.getEntries());
	}

}
