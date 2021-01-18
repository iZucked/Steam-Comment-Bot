/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class OptimisationDataTest {

	@Test
	public void testGetSetSequenceElements() {

		final OptimisationData data = new OptimisationData();

		final List<ISequenceElement> list = new ArrayList<ISequenceElement>();

		data.setSequenceElements(list);

		Assertions.assertSame(list, data.getSequenceElements());
	}

	@Test
	public void testGetSetResources() {
		final OptimisationData data = new OptimisationData();

		final List<IResource> list = new ArrayList<IResource>();

		data.setResources(list);

		Assertions.assertSame(list, data.getResources());
	}

	@Test
	public void testDispose() {
		final OptimisationData data = new OptimisationData();

		final List<ISequenceElement> elements = new ArrayList<ISequenceElement>();
		final List<IResource> resources = new ArrayList<IResource>();

		data.setSequenceElements(elements);
		data.setResources(resources);

		// Sanity check state
		Assertions.assertSame(elements, data.getSequenceElements());
		Assertions.assertSame(resources, data.getResources());

		data.dispose();

		Assertions.assertNull(data.getSequenceElements());
		Assertions.assertNull(data.getResources());
	}
}
