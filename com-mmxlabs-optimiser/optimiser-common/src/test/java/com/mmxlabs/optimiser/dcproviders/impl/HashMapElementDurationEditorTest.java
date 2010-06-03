package com.mmxlabs.optimiser.dcproviders.impl;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.optimiser.impl.Resource;

public class HashMapElementDurationEditorTest {

	@Test
	public void testHashMapElementDurationEditor() {

		String name = "name";

		HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				name);

		Assert.assertSame(name, provider.getName());
	}

	@Test
	public void testGetSetElementDuration() {
		HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		Resource r1 = new Resource();
		Resource r2 = new Resource();
		Object obj = new Object();

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
	public void testDispose() {
		HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		Resource r1 = new Resource();
		Resource r2 = new Resource();
		Object obj = new Object();

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

		HashMapElementDurationEditor<Object> provider = new HashMapElementDurationEditor<Object>(
				"name");

		Assert.assertEquals(0, provider.getDefaultValue());

		provider.setDefaultValue(100);

		Assert.assertEquals(100, provider.getDefaultValue());

	}
}
