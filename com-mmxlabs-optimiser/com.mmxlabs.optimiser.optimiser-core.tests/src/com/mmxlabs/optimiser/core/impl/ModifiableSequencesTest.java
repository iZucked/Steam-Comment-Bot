/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;

@SuppressWarnings("null")
public class ModifiableSequencesTest {

	@Test
	public void testModifiableSequencesListOfIResource() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final ModifiableSequences sequences = new ModifiableSequences(resources);

		Assertions.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assertions.assertSame(resource1, resources2.get(0));
		Assertions.assertSame(resource2, resources2.get(1));

		Assertions.assertNotNull(sequences.getSequence(0));
		Assertions.assertNotNull(sequences.getSequence(1));

		Assertions.assertNotNull(sequences.getSequence(resource1));
		Assertions.assertNotNull(sequences.getSequence(resource2));

		Assertions.assertEquals(0, sequences.getSequence(0).size());
		Assertions.assertEquals(0, sequences.getSequence(1).size());
	}

	@Test
	public void testModifiableSequencesListOfIResourceMapOfIResourceIModifiableSequenceOfT() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assertions.assertSame(resource1, resources2.get(0));
		Assertions.assertSame(resource2, resources2.get(1));

		Assertions.assertSame(sequence1, sequences.getSequence(0));
		Assertions.assertSame(sequence2, sequences.getSequence(1));

		Assertions.assertSame(sequence1, sequences.getSequence(resource1));
		Assertions.assertSame(sequence2, sequences.getSequence(resource2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testModifiableSequencesISequencesOfT() {
		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences initialSequences = new ModifiableSequences(resources, map);

		Mockito.when(sequence1.iterator()).thenReturn(Mockito.mock(Iterator.class));
		Mockito.when(sequence2.iterator()).thenReturn(Mockito.mock(Iterator.class));

		final ModifiableSequences sequences = new ModifiableSequences(initialSequences);

		Assertions.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assertions.assertSame(resource1, resources2.get(0));
		Assertions.assertSame(resource2, resources2.get(1));

		// TODO: Tighten up, Deep copies are made

		Assertions.assertNotNull(sequences.getSequence(0));
		Assertions.assertNotNull(sequences.getSequence(1));

		Assertions.assertNotNull(sequences.getSequence(resource1));
		Assertions.assertNotNull(sequences.getSequence(resource2));
	}

	@Test
	public void testGetResources() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final ModifiableSequences sequences = new ModifiableSequences(resources);

		Assertions.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assertions.assertSame(resource1, resources2.get(0));
		Assertions.assertSame(resource2, resources2.get(1));
	}

	@Test
	public void testGetSequenceIResource() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());

		Assertions.assertSame(sequence1, sequences.getSequence(resource1));
		Assertions.assertSame(sequence2, sequences.getSequence(resource2));

	}

	@Test
	public void testGetSequenceInt() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());

		Assertions.assertSame(sequence1, sequences.getSequence(0));
		Assertions.assertSame(sequence2, sequences.getSequence(1));

	}

	@Test
	public void testGetSequences() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());

		final Map<IResource, ISequence> sequencesMap = sequences.getSequences();

		Assertions.assertTrue(sequencesMap.containsKey(resource1));
		Assertions.assertTrue(sequencesMap.containsKey(resource2));
		Assertions.assertSame(sequence1, sequencesMap.get(resource1));
		Assertions.assertSame(sequence2, sequencesMap.get(resource2));
	}

	@Test
	public void testGetModifiableSequences() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());

		final Map<IResource, IModifiableSequence> sequencesMap = sequences.getModifiableSequences();

		Assertions.assertTrue(sequencesMap.containsKey(resource1));
		Assertions.assertTrue(sequencesMap.containsKey(resource2));
		Assertions.assertSame(sequence1, sequencesMap.get(resource1));
		Assertions.assertSame(sequence2, sequencesMap.get(resource2));
	}

	@Test
	public void testSize() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());
	}

	@Test
	public void testGetModifiableSequenceIResource() {

		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());

		Assertions.assertSame(sequence1, sequences.getModifiableSequence(resource1));
		Assertions.assertSame(sequence2, sequences.getModifiableSequence(resource2));

	}

	@Test
	public void testGetModifiableSequenceInt() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		Assertions.assertEquals(2, sequences.size());

		Assertions.assertSame(sequence1, sequences.getModifiableSequence(0));
		Assertions.assertSame(sequence2, sequences.getModifiableSequence(1));
	}

	@Test
	public void testEquals() {

		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources1 = CollectionsUtil.makeArrayList(resource1, resource2);
		final List<IResource> resources2 = CollectionsUtil.makeArrayList(resource1, resource2);
		final List<IResource> resources3 = CollectionsUtil.makeArrayList(resource1);
		final List<IResource> resources4 = CollectionsUtil.makeArrayList(resource2);

		final Map<IResource, IModifiableSequence> map1 = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Map<IResource, IModifiableSequence> map2 = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Map<IResource, IModifiableSequence> map3 = CollectionsUtil.makeHashMap(resource1, sequence1);
		final Map<IResource, IModifiableSequence> map4 = CollectionsUtil.makeHashMap(resource1, sequence1);

		final Map<IResource, IModifiableSequence> map5 = CollectionsUtil.makeHashMap(resource1, sequence2, resource2, sequence1);

		final ModifiableSequences s1 = new ModifiableSequences(resources1, map1);
		final ModifiableSequences s2 = new ModifiableSequences(resources1, map1);
		final ModifiableSequences s3 = new ModifiableSequences(resources2, map2);

		final ModifiableSequences s4 = new ModifiableSequences(resources3, map3);
		final ModifiableSequences s5 = new ModifiableSequences(resources4, map4);

		final ModifiableSequences s6 = new ModifiableSequences(resources1, map5);

		Assertions.assertTrue(s1.equals(s1));
		Assertions.assertTrue(s1.equals(s2));
		Assertions.assertTrue(s1.equals(s3));

		Assertions.assertFalse(s1.equals(s4));
		Assertions.assertFalse(s1.equals(s5));
		Assertions.assertFalse(s1.equals(s6));

		Assertions.assertTrue(s2.equals(s1));
		Assertions.assertTrue(s3.equals(s1));

		Assertions.assertFalse(s4.equals(s1));
		Assertions.assertFalse(s5.equals(s1));
		Assertions.assertFalse(s6.equals(s1));
	}
}
