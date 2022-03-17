/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedslots;

import static org.mockito.Mockito.mock;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class HashMapRestrictedSlotsProviderEditorTest {

	@Test
	public void testSimplePrecede() {
		final String name = "name";

		final HashMapRestrictedSlotsProviderEditor provider = new HashMapRestrictedSlotsProviderEditor();

		final ISequenceElement element1 = mock(ISequenceElement.class);
		final ISequenceElement element2 = mock(ISequenceElement.class);

		provider.addRestrictedElements(element1, Collections.singleton(element2), null);

		Assertions.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element1));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element1));
		Assertions.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element2));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element2));

		Assertions.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assertions.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element2));
	}

	@Test
	public void testMergePrecede() {
		final String name = "name";

		final HashMapRestrictedSlotsProviderEditor provider = new HashMapRestrictedSlotsProviderEditor();

		final ISequenceElement element1 = mock(ISequenceElement.class);
		final ISequenceElement element2 = mock(ISequenceElement.class);
		final ISequenceElement element3 = mock(ISequenceElement.class);

		provider.addRestrictedElements(element1, Collections.singleton(element2), null);

		Assertions.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assertions.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assertions.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element3));

		provider.addRestrictedElements(element1, Collections.singleton(element3), null);

		Assertions.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assertions.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assertions.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assertions.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element3));
	}

	@Test
	public void testSimpleFollower() {
		final String name = "name";

		final HashMapRestrictedSlotsProviderEditor provider = new HashMapRestrictedSlotsProviderEditor();

		final ISequenceElement element1 = mock(ISequenceElement.class);
		final ISequenceElement element2 = mock(ISequenceElement.class);

		provider.addRestrictedElements(element1, null, Collections.singleton(element2));

		Assertions.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element1));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element1));
		Assertions.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element2));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element2));

		Assertions.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element2));
	}

	@Test
	public void testMergeFollower() {

		final HashMapRestrictedSlotsProviderEditor provider = new HashMapRestrictedSlotsProviderEditor();

		final ISequenceElement element1 = mock(ISequenceElement.class);
		final ISequenceElement element2 = mock(ISequenceElement.class);
		final ISequenceElement element3 = mock(ISequenceElement.class);

		provider.addRestrictedElements(element1, null, Collections.singleton(element2));

		Assertions.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assertions.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element3));

		provider.addRestrictedElements(element1, null, Collections.singleton(element3));

		Assertions.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assertions.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assertions.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element3));
	}
}
