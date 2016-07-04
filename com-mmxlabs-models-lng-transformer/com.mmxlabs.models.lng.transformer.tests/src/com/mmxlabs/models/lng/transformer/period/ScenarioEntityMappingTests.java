/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ScenarioEntityMappingTests {

	@Test
	public void createMappingTest() {

		final ScenarioEntityMapping mapping = new ScenarioEntityMapping();

		final EObject original = Mockito.mock(EObject.class);
		final EObject copy = Mockito.mock(EObject.class);

		mapping.createMapping(original, copy);

		Assert.assertSame(original, mapping.getOriginalFromCopy(copy));
		Assert.assertSame(copy, mapping.getCopyFromOriginal(original));

		Assert.assertNull(mapping.getOriginalFromCopy(original));
		Assert.assertNull(mapping.getCopyFromOriginal(copy));

		Assert.assertEquals(Collections.singleton(original), mapping.getUsedOriginalObjects());
		Assert.assertEquals(Collections.emptySet(), mapping.getUnusedOriginalObjects());
	}

	@Test
	public void createMappingsTest() {

		final ScenarioEntityMapping mapping = new ScenarioEntityMapping();

		final EObject original = Mockito.mock(EObject.class);
		final EObject copy = Mockito.mock(EObject.class);

		final Map<EObject, EObject> map = new HashMap<>();
		map.put(original, copy);

		mapping.createMappings(map);

		Assert.assertSame(original, mapping.getOriginalFromCopy(copy));
		Assert.assertSame(copy, mapping.getCopyFromOriginal(original));

		Assert.assertNull(mapping.getOriginalFromCopy(original));
		Assert.assertNull(mapping.getCopyFromOriginal(copy));

		Assert.assertEquals(Collections.singleton(original), mapping.getUsedOriginalObjects());
		Assert.assertEquals(Collections.emptySet(), mapping.getUnusedOriginalObjects());

	}

	@Test
	public void registerRemovedOriginalTest() {

		final ScenarioEntityMapping mapping = new ScenarioEntityMapping();

		final EObject original1 = Mockito.mock(EObject.class);
		final EObject original2 = Mockito.mock(EObject.class);
		final EObject copy1 = Mockito.mock(EObject.class);
		final EObject copy2 = Mockito.mock(EObject.class);

		mapping.createMapping(original1, copy1);
		mapping.createMapping(original2, copy2);

		mapping.registerRemovedOriginal(original2);

		Assert.assertEquals(Collections.singleton(original1), mapping.getUsedOriginalObjects());
		Assert.assertEquals(Collections.singleton(original2), mapping.getUnusedOriginalObjects());

	}

}
