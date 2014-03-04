/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

@RunWith(JMock.class)
public class OptimisationDataTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetSetSequenceElements() {

		final OptimisationData data = new OptimisationData();

		final List<ISequenceElement> list = new ArrayList<ISequenceElement>();

		data.setSequenceElements(list);

		Assert.assertSame(list, data.getSequenceElements());
	}

	@Test
	public void testGetSetResources() {
		final OptimisationData data = new OptimisationData();

		final List<IResource> list = new ArrayList<IResource>();

		data.setResources(list);

		Assert.assertSame(list, data.getResources());
	}

	@Test
	public void testDispose() {
		final OptimisationData data = new OptimisationData();

		final List<ISequenceElement> elements = new ArrayList<ISequenceElement>();
		final List<IResource> resources = new ArrayList<IResource>();


		data.setSequenceElements(elements);
		data.setResources(resources);

		// Sanity check state
		Assert.assertSame(elements, data.getSequenceElements());
		Assert.assertSame(resources, data.getResources());

		data.dispose();

		Assert.assertNull(data.getSequenceElements());
		Assert.assertNull(data.getResources());

		context.assertIsSatisfied();
	}
}
