/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.Resource;

public class HashMapElementDurationEditorTest {

	@NonNull
	private IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testGetSetElementDuration() {
		final HashMapElementDurationEditor provider = new HashMapElementDurationEditor();

		final Resource r1 = new Resource(index, "r1");
		final Resource r2 = new Resource(index, "r2");
		final ISequenceElement obj = Mockito.mock(ISequenceElement.class, "1");

		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj, r2));

		provider.setElementDuration(obj, r1, 10);

		Assert.assertEquals(10, provider.getElementDuration(obj, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj, r2));
	}

	@Test
	public void testGetSetElementDuration2() {
		final HashMapElementDurationEditor provider = new HashMapElementDurationEditor();

		final Resource r1 = new Resource(index, "r1");
		final Resource r2 = new Resource(index, "r2");
		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");

		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r2));

		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r2));

		provider.setElementDuration(obj1, r1, 10);
		provider.setElementDuration(obj2, r1, 20);

		Assert.assertEquals(10, provider.getElementDuration(obj1, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r2));
		Assert.assertEquals(20, provider.getElementDuration(obj2, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r2));
	}

	@Test
	public void testGetSetElementDuration3() {
		final HashMapElementDurationEditor provider = new HashMapElementDurationEditor();

		final Resource r1 = new Resource(index, "r1");
		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r1));

		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r1));

		provider.setElementDuration(obj1, r1, 10);

		Assert.assertEquals(10, provider.getElementDuration(obj1, r1));

		Assert.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r1));
	}

	@Test
	public void testGetSetDefaultValue() {

		final HashMapElementDurationEditor provider = new HashMapElementDurationEditor();

		Assert.assertEquals(0, provider.getDefaultValue());

		provider.setDefaultValue(100);

		Assert.assertEquals(100, provider.getDefaultValue());

	}
}
