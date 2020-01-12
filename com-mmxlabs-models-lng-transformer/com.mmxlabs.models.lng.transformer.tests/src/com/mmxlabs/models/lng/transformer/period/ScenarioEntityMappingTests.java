/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ScenarioEntityMappingTests {

	@Test
	public void createMappingTest() {

		final ScenarioEntityMapping mapping = new ScenarioEntityMapping();

		final EObject original = Mockito.mock(EObject.class);
		final EObject copy = Mockito.mock(EObject.class);

		mapping.createMapping(original, copy);

		Assertions.assertSame(original, mapping.getOriginalFromCopy(copy));
		Assertions.assertSame(copy, mapping.getCopyFromOriginal(original));

		Assertions.assertNull(mapping.getOriginalFromCopy(original));
		Assertions.assertNull(mapping.getCopyFromOriginal(copy));

		Assertions.assertEquals(Collections.singleton(original), mapping.getUsedOriginalObjects());
		Assertions.assertEquals(Collections.emptySet(), mapping.getUnusedOriginalObjects());
	}

	@Test
	public void createMappingsTest() {

		final ScenarioEntityMapping mapping = new ScenarioEntityMapping();

		final EObject original = Mockito.mock(EObject.class);
		final EObject copy = Mockito.mock(EObject.class);

		final Map<EObject, EObject> map = new HashMap<>();
		map.put(original, copy);

		mapping.createMappings(map);

		Assertions.assertSame(original, mapping.getOriginalFromCopy(copy));
		Assertions.assertSame(copy, mapping.getCopyFromOriginal(original));

		Assertions.assertNull(mapping.getOriginalFromCopy(original));
		Assertions.assertNull(mapping.getCopyFromOriginal(copy));

		Assertions.assertEquals(Collections.singleton(original), mapping.getUsedOriginalObjects());
		Assertions.assertEquals(Collections.emptySet(), mapping.getUnusedOriginalObjects());

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

		Assertions.assertEquals(Collections.singleton(original1), mapping.getUsedOriginalObjects());
		Assertions.assertEquals(Collections.singleton(original2), mapping.getUnusedOriginalObjects());

	}

}
