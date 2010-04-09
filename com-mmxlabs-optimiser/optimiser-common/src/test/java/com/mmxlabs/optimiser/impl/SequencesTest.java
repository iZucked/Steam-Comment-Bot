package com.mmxlabs.optimiser.impl;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;

@RunWith(JMock.class)
@SuppressWarnings("unchecked")
public class SequencesTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testSequencesListOfIResource() {

		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Sequences<Object> sequences = new Sequences<Object>(resources);

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

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final ISequence<Object> sequence1 = context.mock(ISequence.class,
				"sequence1");
		final ISequence<Object> sequence2 = context.mock(ISequence.class,
				"sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, ISequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences<Object> sequences = new Sequences<Object>(resources,
				map);

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
	public void testSequencesISequencesOfT() {
		// Create two resources and test Sequences object is correctly
		// initialised

		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final ISequence<Object> sequence1 = context.mock(ISequence.class,
				"sequence1");
		final ISequence<Object> sequence2 = context.mock(ISequence.class,
				"sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, ISequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences<Object> initialSequences = new Sequences<Object>(
				resources, map);

		context.checking(new Expectations() {
			{
				allowing(sequence1);
				allowing(sequence2);
			}
		});
		final Sequences<Object> sequences = new Sequences<Object>(
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

		final Sequences<Object> sequences = new Sequences<Object>(resources);

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

		final ISequence<Object> sequence1 = context.mock(ISequence.class,
				"sequence1");
		final ISequence<Object> sequence2 = context.mock(ISequence.class,
				"sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, ISequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences<Object> sequences = new Sequences<Object>(resources,
				map);

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

		final ISequence<Object> sequence1 = context.mock(ISequence.class,
				"sequence1");
		final ISequence<Object> sequence2 = context.mock(ISequence.class,
				"sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, ISequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences<Object> sequences = new Sequences<Object>(resources,
				map);

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

		final ISequence<Object> sequence1 = context.mock(ISequence.class,
				"sequence1");
		final ISequence<Object> sequence2 = context.mock(ISequence.class,
				"sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, ISequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences<Object> sequences = new Sequences<Object>(resources,
				map);

		Assert.assertEquals(2, sequences.size());

		Map<IResource, ISequence<Object>> sequencesMap = sequences
				.getSequences();

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

		final ISequence<Object> sequence1 = context.mock(ISequence.class,
				"sequence1");
		final ISequence<Object> sequence2 = context.mock(ISequence.class,
				"sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(
				resource1, resource2);

		final Map<IResource, ISequence<Object>> map = CollectionsUtil
				.makeHashMap(resource1, sequence1, resource2, sequence2);

		final Sequences<Object> sequences = new Sequences<Object>(resources,
				map);

		Assert.assertEquals(2, sequences.size());
	}
}
