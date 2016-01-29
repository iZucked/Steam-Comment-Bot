/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.capacity.impl;

import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;

public class CapacityAnnotationTest {

	@Test
	public void testCapacityAnnotation() {

		final Collection<ICapacityEntry> entries = Collections.emptySet();

		final CapacityAnnotation annotation = new CapacityAnnotation(entries);

		Assert.assertSame(entries, annotation.getEntries());
	}
}
