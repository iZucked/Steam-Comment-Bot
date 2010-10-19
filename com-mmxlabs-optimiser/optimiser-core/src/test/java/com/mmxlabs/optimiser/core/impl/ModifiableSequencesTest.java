/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class ModifiableSequencesTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testModifiableSequencesListOfIResource() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources);

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
	public void testModifiableSequencesListOfIResourceMapOfIResourceIModifiableSequenceOfT() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));

		Assert.assertSame(sequence1, sequences.getSequence(0));
		Assert.assertSame(sequence2, sequences.getSequence(1));

		Assert.assertSame(sequence1, sequences.getSequence(resource1));
		Assert.assertSame(sequence2, sequences.getSequence(resource2));
	}

	@Test
	public void testModifiableSequencesISequencesOfT() {
		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> initialSequences = new ModifiableSequences<Object>(
				resources, map);

		context.checking(new Expectations() {
			{
				allowing(sequence1);
				allowing(sequence2);
			}
		});
		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				initialSequences);

		Assert.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));

		// TODO: Tighten up, Deep copies are made

		Assert.assertNotNull(sequences.getSequence(0));
		Assert.assertNotNull(sequences.getSequence(1));

		Assert.assertNotNull(sequences.getSequence(resource1));
		Assert.assertNotNull(sequences.getSequence(resource2));

		context.assertIsSatisfied();
	}

	@Test
	public void testGetResources() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources);

		Assert.assertEquals(2, sequences.size());

		final List<IResource> resources2 = sequences.getResources();
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));
	}

	@Test
	public void testGetSequenceIResource() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());

		Assert.assertSame(sequence1, sequences.getSequence(resource1));
		Assert.assertSame(sequence2, sequences.getSequence(resource2));

	}

	@Test
	public void testGetSequenceInt() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());

		Assert.assertSame(sequence1, sequences.getSequence(0));
		Assert.assertSame(sequence2, sequences.getSequence(1));

	}

	@Test
	public void testGetSequences() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());

		final Map<IResource, ISequence<Object>> sequencesMap = sequences
				.getSequences();

		Assert.assertTrue(sequencesMap.containsKey(resource1));
		Assert.assertTrue(sequencesMap.containsKey(resource2));
		Assert.assertSame(sequence1, sequencesMap.get(resource1));
		Assert.assertSame(sequence2, sequencesMap.get(resource2));
	}

	@Test
	public void testGetModifiableSequences() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());

		final Map<IResource, IModifiableSequence<Object>> sequencesMap = sequences
				.getModifiableSequences();

		Assert.assertTrue(sequencesMap.containsKey(resource1));
		Assert.assertTrue(sequencesMap.containsKey(resource2));
		Assert.assertSame(sequence1, sequencesMap.get(resource1));
		Assert.assertSame(sequence2, sequencesMap.get(resource2));
	}

	@Test
	public void testSize() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());
	}

	@Test
	public void testGetModifiableSequenceIResource() {

		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());

		Assert.assertSame(sequence1, sequences.getModifiableSequence(resource1));
		Assert.assertSame(sequence2, sequences.getModifiableSequence(resource2));

	}

	@Test
	public void testGetModifiableSequenceInt() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, IModifiableSequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				resources, map);

		Assert.assertEquals(2, sequences.size());

		Assert.assertSame(sequence1, sequences.getModifiableSequence(0));
		Assert.assertSame(sequence2, sequences.getModifiableSequence(1));
	}

	@Test
	public void testEquals() {

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources1 = CollectionsUtil.makeArrayList(
				resource1, resource2);
		final List<IResource> resources2 = CollectionsUtil.makeArrayList(
				resource1, resource2);
		final List<IResource> resources3 = CollectionsUtil
				.makeArrayList(resource1);
		final List<IResource> resources4 = CollectionsUtil
				.makeArrayList(resource2);

		final Map<IResource, IModifiableSequence<Object>> map1 = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Map<IResource, IModifiableSequence<Object>> map2 = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Map<IResource, IModifiableSequence<Object>> map3 = CollectionsUtil
				.makeHashMap(resource1, sequence1);
		final Map<IResource, IModifiableSequence<Object>> map4 = CollectionsUtil
				.makeHashMap(resource1, sequence1);

		final Map<IResource, IModifiableSequence<Object>> map5 = CollectionsUtil
				.makeHashMap(resource1, sequence2, resource2, sequence1);

		final ModifiableSequences<Object> s1 = new ModifiableSequences<Object>(
				resources1, map1);
		final ModifiableSequences<Object> s2 = new ModifiableSequences<Object>(
				resources1, map1);
		final ModifiableSequences<Object> s3 = new ModifiableSequences<Object>(
				resources2, map2);

		final ModifiableSequences<Object> s4 = new ModifiableSequences<Object>(
				resources3, map3);
		final ModifiableSequences<Object> s5 = new ModifiableSequences<Object>(
				resources4, map4);

		final ModifiableSequences<Object> s6 = new ModifiableSequences<Object>(
				resources1, map5);

		Assert.assertTrue(s1.equals(s1));
		Assert.assertTrue(s1.equals(s2));
		Assert.assertTrue(s1.equals(s3));

		Assert.assertFalse(s1.equals(s4));
		Assert.assertFalse(s1.equals(s5));
		Assert.assertFalse(s1.equals(s6));

		Assert.assertTrue(s2.equals(s1));
		Assert.assertTrue(s3.equals(s1));

		Assert.assertFalse(s4.equals(s1));
		Assert.assertFalse(s5.equals(s1));
		Assert.assertFalse(s6.equals(s1));
	}
}
