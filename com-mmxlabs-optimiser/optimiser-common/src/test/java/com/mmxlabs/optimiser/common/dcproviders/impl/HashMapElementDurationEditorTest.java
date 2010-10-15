package com.mmxlabs.optimiser.common.dcproviders.impl;


import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.common.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.core.impl.Resource;


public class HashMapElementDurationEditorTest {

	IIndexingContext index = new SimpleIndexingContext();
	
	@Test
	public void testHashMapElementDurationEditor() {

		final String name = "name";

		final HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				name);

		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testGetSetElementDuration() {
		final HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		final Resource r1 = new Resource(index);
		final Resource r2 = new Resource(index);
		final Object obj = new Object();

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r2));

		provider.setElementDuration(obj, r1, 10);

		Assert.assertEquals(10, provider.getElementDuration(obj, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r2));
	}

	@Test
	public void testGetSetElementDuration2() {
		final HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		final Resource r1 = new Resource(index);
		final Resource r2 = new Resource(index);
		final Object obj1 = new Object();
		final Object obj2 = new Object();

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj1, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj1, r2));

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj2, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj2, r2));

		provider.setElementDuration(obj1, r1, 10);
		provider.setElementDuration(obj2, r1, 20);

		Assert.assertEquals(10, provider.getElementDuration(obj1, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj1, r2));
		Assert.assertEquals(20, provider.getElementDuration(obj2, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj2, r2));
	}

	@Test
	public void testGetSetElementDuration3() {
		final HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		final Resource r1 = new Resource(index);
		final Object obj1 = new Object();
		final Object obj2 = new Object();

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj1, r1));

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj2, r1));

		provider.setElementDuration(obj1, r1, 10);

		Assert.assertEquals(10, provider.getElementDuration(obj1, r1));

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj2, r1));

	}

	@Test
	public void testDispose() {
		final HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		final Resource r1 = new Resource(index);
		final Resource r2 = new Resource(index);
		final Object obj = new Object();

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r2));
		provider.setElementDuration(obj, r1, 10);

		Assert.assertEquals(10, provider.getElementDuration(obj, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r2));

		provider.dispose();

		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r1));
		Assert.assertEquals(provider.getDefaultValue(), provider
				.getElementDuration(obj, r2));

	}

	@Test
	public void testGetSetDefaultValue() {

		final HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		Assert.assertEquals(0, provider.getDefaultValue());

		provider.setDefaultValue(100);

		Assert.assertEquals(100, provider.getDefaultValue());

	}
}
