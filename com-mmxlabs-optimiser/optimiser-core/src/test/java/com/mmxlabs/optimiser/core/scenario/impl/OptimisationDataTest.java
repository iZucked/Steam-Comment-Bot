/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

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
	public void testAddHasGetDataComponentProvider() {

		final OptimisationData data = new OptimisationData();

		final IDataComponentProvider provider = context.mock(IDataComponentProvider.class);

		Assert.assertFalse(data.hasDataComponentProvider("test"));

		data.addDataComponentProvider("test", provider);

		Assert.assertTrue(data.hasDataComponentProvider("test"));
		Assert.assertSame(provider, data.getDataComponentProvider("test", IDataComponentProvider.class));
	}

	@Test
	public void testGetDataComponentProviders() {

		final OptimisationData data = new OptimisationData();

		final IDataComponentProvider provider = context.mock(IDataComponentProvider.class);

		Assert.assertEquals(Collections.emptySet(), data.getDataComponentProviders());
		data.addDataComponentProvider("test", provider);
		Assert.assertEquals(Collections.singleton("test"), data.getDataComponentProviders());
	}

	@Test
	public void testAddHasGetDataComponentProvider2() {

		final OptimisationData data = new OptimisationData();

		final IDataComponentProvider provider = context.mock(IDataComponentProvider.class);

		Assert.assertFalse(data.hasDataComponentProvider("test"));

		data.addDataComponentProvider("test", provider);

		Assert.assertTrue(data.hasDataComponentProvider("test"));

		Assert.assertNull(data.getDataComponentProvider("test2", IDataComponentProvider.class));
	}

	@Test(expected = ClassCastException.class)
	public void testAddHasGetDataComponentProvider3() {

		final OptimisationData data = new OptimisationData();

		final IDataComponentProvider provider = context.mock(IDataComponentProvider.class);

		Assert.assertFalse(data.hasDataComponentProvider("test"));

		data.addDataComponentProvider("test", provider);

		Assert.assertTrue(data.hasDataComponentProvider("test"));

		final class A implements IDataComponentProvider {
			@Override
			public String getName() {
				return null;
			}

			@Override
			public void dispose() {
			}
		}
		data.getDataComponentProvider("test", A.class);
	}

	@Test
	public void testDispose() {
		final OptimisationData data = new OptimisationData();

		final List<ISequenceElement> elements = new ArrayList<ISequenceElement>();
		final List<IResource> resources = new ArrayList<IResource>();

		final IDataComponentProvider provider = context.mock(IDataComponentProvider.class);

		data.setSequenceElements(elements);
		data.setResources(resources);
		data.addDataComponentProvider("test", provider);

		// Sanity check state
		Assert.assertSame(elements, data.getSequenceElements());
		Assert.assertSame(resources, data.getResources());
		Assert.assertTrue(data.hasDataComponentProvider("test"));
		Assert.assertSame(provider, data.getDataComponentProvider("test", IDataComponentProvider.class));

		context.checking(new Expectations() {
			{
				// Expect nothing to happen
				one(provider).dispose();
			}
		});

		data.dispose();

		Assert.assertNull(data.getSequenceElements());
		Assert.assertNull(data.getResources());
		Assert.assertFalse(data.hasDataComponentProvider("test"));

		context.assertIsSatisfied();
	}
}
