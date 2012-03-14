package com.mmxlabs.trading.optimiser.annotations.impl;

import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossEntry;

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
