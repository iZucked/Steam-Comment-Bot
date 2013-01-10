package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class HashMapRestrictedElementsProviderEditorTest {

	@Test
	public void testSimplePrecede() {
		final String name = "name";

		final HashMapRestrictedElementsProviderEditor provider = new HashMapRestrictedElementsProviderEditor(name);

		Assert.assertEquals(name, provider.getName());

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		provider.setRestrictedElements(element1, Collections.singleton(element2), null);

		Assert.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element1));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element1));
		Assert.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element2));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element2));

		Assert.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assert.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element2));
	}

	@Test
	public void testMergePrecede() {
		final String name = "name";

		final HashMapRestrictedElementsProviderEditor provider = new HashMapRestrictedElementsProviderEditor(name);

		Assert.assertEquals(name, provider.getName());

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element3 = Mockito.mock(ISequenceElement.class);

		provider.setRestrictedElements(element1, Collections.singleton(element2), null);

		Assert.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assert.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assert.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element3));

		provider.setRestrictedElements(element1, Collections.singleton(element3), null);

		Assert.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assert.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assert.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assert.assertTrue(provider.getRestrictedPrecedingElements(element1).contains(element3));
	}

	@Test
	public void testSimpleFollower() {
		final String name = "name";

		final HashMapRestrictedElementsProviderEditor provider = new HashMapRestrictedElementsProviderEditor(name);

		Assert.assertEquals(name, provider.getName());

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);

		provider.setRestrictedElements(element1, null, Collections.singleton(element2));

		Assert.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element1));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element1));
		Assert.assertFalse(provider.getRestrictedFollowerElements(element2).contains(element2));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element2).contains(element2));

		Assert.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element2));
	}

	@Test
	public void testMergeFollower() {
		final String name = "name";

		final HashMapRestrictedElementsProviderEditor provider = new HashMapRestrictedElementsProviderEditor(name);

		Assert.assertEquals(name, provider.getName());

		final ISequenceElement element1 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element2 = Mockito.mock(ISequenceElement.class);
		final ISequenceElement element3 = Mockito.mock(ISequenceElement.class);

		provider.setRestrictedElements(element1, null, Collections.singleton(element2));

		Assert.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assert.assertFalse(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element3));

		provider.setRestrictedElements(element1, null, Collections.singleton(element3));

		Assert.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element2));
		Assert.assertTrue(provider.getRestrictedFollowerElements(element1).contains(element3));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element2));
		Assert.assertFalse(provider.getRestrictedPrecedingElements(element1).contains(element3));
	}
}
