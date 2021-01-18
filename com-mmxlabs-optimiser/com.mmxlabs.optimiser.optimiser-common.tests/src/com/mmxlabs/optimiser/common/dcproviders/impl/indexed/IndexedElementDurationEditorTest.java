/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.core.impl.Resource;

public class IndexedElementDurationEditorTest {

	@NonNull
	private IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testGetSetElementDuration() {
		final IndexedElementDurationEditor provider = new IndexedElementDurationEditor();

		final Resource r1 = new Resource(index, "r1");
		final Resource r2 = new Resource(index, "r2");
		final MockSequenceElement obj = new MockSequenceElement(1);

		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj, r1));
		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj, r2));

		provider.setElementDuration(obj, r1, 10);

		Assertions.assertEquals(10, provider.getElementDuration(obj, r1));
		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj, r2));
	}

	@Test
	public void testGetSetElementDuration2() {
		final IndexedElementDurationEditor provider = new IndexedElementDurationEditor();

		final Resource r1 = new Resource(index, "r1");
		final Resource r2 = new Resource(index, "r2");
		final MockSequenceElement obj1 = new MockSequenceElement(1);
		final MockSequenceElement obj2 = new MockSequenceElement(2);

		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r1));
		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r2));

		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r1));
		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r2));

		provider.setElementDuration(obj1, r1, 10);
		provider.setElementDuration(obj2, r1, 20);

		Assertions.assertEquals(10, provider.getElementDuration(obj1, r1));
		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r2));
		Assertions.assertEquals(20, provider.getElementDuration(obj2, r1));
		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r2));
	}

	@Test
	public void testGetSetElementDuration3() {
		final IndexedElementDurationEditor provider = new IndexedElementDurationEditor();

		final Resource r1 = new Resource(index, "r1");
		final MockSequenceElement obj1 = new MockSequenceElement(1);
		final MockSequenceElement obj2 = new MockSequenceElement(2);

		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj1, r1));

		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r1));

		provider.setElementDuration(obj1, r1, 10);

		Assertions.assertEquals(10, provider.getElementDuration(obj1, r1));

		Assertions.assertEquals(provider.getDefaultValue(), provider.getElementDuration(obj2, r1));
	}

	@Test
	public void testGetSetDefaultValue() {

		final IndexedElementDurationEditor provider = new IndexedElementDurationEditor();

		Assertions.assertEquals(0, provider.getDefaultValue());

		provider.setDefaultValue(100);

		Assertions.assertEquals(100, provider.getDefaultValue());

	}
}
