/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;

public class ProfitAndLossAnnotationTest {

	@Test
	public void testProfitAndLossAnnotation() {

		final Collection<IProfitAndLossEntry> entries = Collections.emptySet();

		final ProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(entries);

		Assertions.assertSame(entries, annotation.getEntries());
	}

}
