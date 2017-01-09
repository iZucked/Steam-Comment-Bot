/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;

@SuppressWarnings("null")
public class SequencesTest {

	@Test
	public void testSequencesListOfIResource() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Sequences sequences = new Sequences(resources);

		Assert.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));

		Assert.assertNotNull(sequences.getSequence(0));
		Assert.assertNotNull(sequences.getSequence(1));

		Assert.assertNotNull(sequences.getSequence(resource1));
		Assert.assertNotNull(sequences.getSequence(resource2));

		Assert.assertEquals(0, sequences.getSequence(0).size());
		Assert.assertEquals(0, sequences.getSequence(1).size());
	}

	@Test
	public void testSequencesListOfIResourceMapOfIResourceISequenceOfT() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final ISequence sequence1 = Mockito.mock(ISequence.class, "sequence1");
		final ISequence sequence2 = Mockito.mock(ISequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences sequences = new Sequences(resources, map);

		Assert.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));

		Assert.assertSame(sequence1, sequences.getSequence(0));
		Assert.assertSame(sequence2, sequences.getSequence(1));

		Assert.assertSame(sequence1, sequences.getSequence(resource1));
		Assert.assertSame(sequence2, sequences.getSequence(resource2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSequencesISequencesOfT() {
		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final ISequence sequence1 = Mockito.mock(ISequence.class, "sequence1");
		final ISequence sequence2 = Mockito.mock(ISequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences initialSequences = new Sequences(resources, map);

		Mockito.when(sequence1.iterator()).thenReturn(Mockito.mock(Iterator.class));
		Mockito.when(sequence2.iterator()).thenReturn(Mockito.mock(Iterator.class));

		final Sequences sequences = new Sequences(initialSequences);

		Assert.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));

		// TODO: Tighten up, Deep copies are made

		Assert.assertNotNull(sequences.getSequence(0));
		Assert.assertNotNull(sequences.getSequence(1));

		Assert.assertNotNull(sequences.getSequence(resource1));
		Assert.assertNotNull(sequences.getSequence(resource2));
	}

	@Test
	public void testGetResources() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Sequences sequences = new Sequences(resources);

		Assert.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));
	}

	@Test
	public void testGetSequenceIResource() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final ISequence sequence1 = Mockito.mock(ISequence.class, "sequence1");
		final ISequence sequence2 = Mockito.mock(ISequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences sequences = new Sequences(resources, map);

		Assert.assertEquals(2, sequences.size());

		Assert.assertSame(sequence1, sequences.getSequence(resource1));
		Assert.assertSame(sequence2, sequences.getSequence(resource2));

	}

	@Test
	public void testGetSequenceInt() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final ISequence sequence1 = Mockito.mock(ISequence.class, "sequence1");
		final ISequence sequence2 = Mockito.mock(ISequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences sequences = new Sequences(resources, map);

		Assert.assertEquals(2, sequences.size());

		Assert.assertSame(sequence1, sequences.getSequence(0));
		Assert.assertSame(sequence2, sequences.getSequence(1));

	}

	@Test
	public void testGetSequences() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final ISequence sequence1 = Mockito.mock(ISequence.class, "sequence1");
		final ISequence sequence2 = Mockito.mock(ISequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences sequences = new Sequences(resources, map);

		Assert.assertEquals(2, sequences.size());

		final Map<IResource, ISequence> sequencesMap = sequences.getSequences();

		Assert.assertTrue(sequencesMap.containsKey(resource1));
		Assert.assertTrue(sequencesMap.containsKey(resource2));
		Assert.assertSame(sequence1, sequencesMap.get(resource1));
		Assert.assertSame(sequence2, sequencesMap.get(resource2));
	}

	@Test
	public void testSize() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final ISequence sequence1 = Mockito.mock(ISequence.class, "sequence1");
		final ISequence sequence2 = Mockito.mock(ISequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences sequences = new Sequences(resources, map);

		Assert.assertEquals(2, sequences.size());
	}
}
