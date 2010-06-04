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
import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class UnmodifiableSequencesWrapperTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetResources() {
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

		final UnmodifiableSequencesWrapper<Object> wrapper = new UnmodifiableSequencesWrapper<Object>(
				sequences);

		final List<IResource> resources2 = wrapper.getResources();
		Assert.assertEquals(2, resources2.size());
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));
	}

	@Test
	public void testGetSequenceIResource() {
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

		final UnmodifiableSequencesWrapper<Object> wrapper = new UnmodifiableSequencesWrapper<Object>(
				sequences);

		// Assert.assertSame(sequence1, wrapper.getSequence(resource1));
		// Assert.assertSame(sequence2, wrapper.getSequence(resource2));

		Assert
				.assertTrue(wrapper.getSequence(resource1) instanceof UnmodifiableSequenceWrapper);
		Assert
				.assertTrue(wrapper.getSequence(resource2) instanceof UnmodifiableSequenceWrapper);

		context.checking(new Expectations() {
			{
				oneOf(sequence1).size();
			}
		});

		wrapper.getSequence(resource1).size();

		context.checking(new Expectations() {
			{
				oneOf(sequence2).size();
			}
		});

		wrapper.getSequence(resource2).size();

	}

	@Test
	public void testGetSequenceInt() {
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

		final UnmodifiableSequencesWrapper<Object> wrapper = new UnmodifiableSequencesWrapper<Object>(
				sequences);

		Assert
				.assertTrue(wrapper.getSequence(0) instanceof UnmodifiableSequenceWrapper);
		Assert
				.assertTrue(wrapper.getSequence(1) instanceof UnmodifiableSequenceWrapper);

		context.checking(new Expectations() {
			{
				oneOf(sequence1).size();
			}
		});

		wrapper.getSequence(0).size();

		context.checking(new Expectations() {
			{
				oneOf(sequence2).size();
			}
		});

		wrapper.getSequence(1).size();

		context.assertIsSatisfied();
	}

	@Test
	public void testGetSequences() {
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

		final UnmodifiableSequencesWrapper<Object> wrapper = new UnmodifiableSequencesWrapper<Object>(
				sequences);

		final Map<IResource, ISequence<Object>> sequencesMap = wrapper
				.getSequences();

		Assert.assertTrue(sequencesMap.containsKey(resource1));
		Assert.assertTrue(sequencesMap.containsKey(resource2));

		context.checking(new Expectations() {
			{
				oneOf(sequence1).size();
			}
		});

		sequencesMap.get(resource1).size();

		context.checking(new Expectations() {
			{
				oneOf(sequence2).size();
			}
		});

		sequencesMap.get(resource2).size();

		context.assertIsSatisfied();
	}

	@Test
	public void testSetSequences() {
		final IResource resource1 = context.mock(IResource.class, "resource-1");
		final IResource resource2 = context.mock(IResource.class, "resource-2");

		final IModifiableSequence<Object> sequence1 = context.mock(
				IModifiableSequence.class, "sequence1");
		final IModifiableSequence<Object> sequence2 = context.mock(
				IModifiableSequence.class, "sequence2");

		final List<IResource> resources1 = CollectionsUtil
				.makeArrayList(resource1);

		final List<IResource> resources2 = CollectionsUtil
				.makeArrayList(resource2);

		final Map<IResource, IModifiableSequence<Object>> map1 = CollectionsUtil
				.makeHashMap(resource1, sequence1);

		final Map<IResource, IModifiableSequence<Object>> map2 = CollectionsUtil
				.makeHashMap(resource2, sequence2);

		final ModifiableSequences<Object> sequences1 = new ModifiableSequences<Object>(
				resources1, map1);

		final ModifiableSequences<Object> sequences2 = new ModifiableSequences<Object>(
				resources2, map2);

		final UnmodifiableSequencesWrapper<Object> wrapper = new UnmodifiableSequencesWrapper<Object>(
				sequences1);

		Assert
				.assertTrue(wrapper.getSequence(0) instanceof UnmodifiableSequenceWrapper);
		Assert
				.assertTrue(wrapper.getSequence(resource1) instanceof UnmodifiableSequenceWrapper);

		context.checking(new Expectations() {
			{
				oneOf(sequence1).size();
			}
		});

		wrapper.getSequence(resource1).size();

		wrapper.setSequences(sequences2);

		context.checking(new Expectations() {
			{
				oneOf(sequence2).size();
			}
		});

		wrapper.getSequence(resource2).size();

	}

	@Test
	public void testSize() {
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

		final UnmodifiableSequencesWrapper<Object> wrapper = new UnmodifiableSequencesWrapper<Object>(
				sequences);
		Assert.assertEquals(2, wrapper.size());
	}
}
