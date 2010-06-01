package com.mmxlabs.optimiser.scenario.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;

public class OptimisationDataTest {

	@Test
	public void testGetSetSequenceElements() {

		final OptimisationData<Integer> data = new OptimisationData<Integer>();

		final List<Integer> list = new ArrayList<Integer>();

		data.setSequenceElements(list);

		Assert.assertSame(list, data.getSequenceElements());
	}

	@Test
	public void testGetSetResources() {
		final OptimisationData<Integer> data = new OptimisationData<Integer>();

		final List<IResource> list = new ArrayList<IResource>();

		data.setResources(list);

		Assert.assertSame(list, data.getResources());
	}

	@Test
	public void testAddHasGetDataComponentProvider() {

		final OptimisationData<Integer> data = new OptimisationData<Integer>();

		final IDataComponentProvider provider = new IDataComponentProvider() {

			@Override
			public String getName() {
				return "test";
			}

			@Override
			public void dispose() {

			}
		};

		Assert.assertFalse(data.hasDataComponentProvider("test"));

		data.addDataComponentProvider("test", provider);

		Assert.assertTrue(data.hasDataComponentProvider("test"));
		Assert.assertSame(provider, data.getDataComponentProvider("test",
				IDataComponentProvider.class));
	}

	@Test
	public void testDispose() {
		final OptimisationData<Integer> data = new OptimisationData<Integer>();

		final List<Integer> elements = new ArrayList<Integer>();
		final List<IResource> resources = new ArrayList<IResource>();

		final IDataComponentProvider provider = new IDataComponentProvider() {

			@Override
			public String getName() {
				return "test";
			}

			@Override
			public void dispose() {

			}
		};

		data.setSequenceElements(elements);
		data.setResources(resources);
		data.addDataComponentProvider("test", provider);

		// Sanity check state
		Assert.assertSame(elements, data.getSequenceElements());
		Assert.assertSame(resources, data.getResources());
		Assert.assertTrue(data.hasDataComponentProvider("test"));
		Assert.assertSame(provider, data.getDataComponentProvider("test",
				IDataComponentProvider.class));

		data.dispose();

		Assert.assertNull(data.getSequenceElements());
		Assert.assertNull(data.getResources());
		Assert.assertFalse(data.hasDataComponentProvider("test"));

	}
}
