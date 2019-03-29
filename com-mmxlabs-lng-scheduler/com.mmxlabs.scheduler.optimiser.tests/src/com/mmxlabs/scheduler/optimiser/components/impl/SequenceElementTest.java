/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;

public class SequenceElementTest {

	@NonNull
	final IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testSequenceElementStringIPortICargo() {

		final String name = "name";
		final SequenceElement element = new SequenceElement(index, name);

		Assertions.assertSame(name, element.getName());
	}
}
