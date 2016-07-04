/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;

public class ProfitAndLossAnnotationTest {

	@Test
	public void testProfitAndLossAnnotation() {

		final Collection<IProfitAndLossEntry> entries = Collections.emptySet();

		final ProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(entries);

		Assert.assertSame(entries, annotation.getEntries());
	}

}
