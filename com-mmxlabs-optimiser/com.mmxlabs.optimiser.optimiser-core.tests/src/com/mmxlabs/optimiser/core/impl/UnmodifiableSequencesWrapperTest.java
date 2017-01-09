/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;

@SuppressWarnings("null")
public class UnmodifiableSequencesWrapperTest {

	@Test
	public void testGetResources() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		final UnmodifiableSequencesWrapper wrapper = new UnmodifiableSequencesWrapper(sequences);

		final List<IResource> resources2 = wrapper.getResources();
		Assert.assertEquals(2, resources2.size());
		Assert.assertSame(resource1, resources2.get(0));
		Assert.assertSame(resource2, resources2.get(1));
	}

	@Test
	public void testGetSequenceIResource() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		final UnmodifiableSequencesWrapper wrapper = new UnmodifiableSequencesWrapper(sequences);

		// Assert.assertSame(sequence1, wrapper.getSequence(resource1));
		// Assert.assertSame(sequence2, wrapper.getSequence(resource2));

		Assert.assertTrue(wrapper.getSequence(resource1) instanceof UnmodifiableSequenceWrapper);
		Assert.assertTrue(wrapper.getSequence(resource2) instanceof UnmodifiableSequenceWrapper);

		wrapper.getSequence(resource1).size();
		Mockito.verify(sequence1).size();

		wrapper.getSequence(resource2).size();
		Mockito.verify(sequence2).size();

	}

	@Test
	public void testGetSequenceInt() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		final UnmodifiableSequencesWrapper wrapper = new UnmodifiableSequencesWrapper(sequences);

		Assert.assertTrue(wrapper.getSequence(0) instanceof UnmodifiableSequenceWrapper);
		Assert.assertTrue(wrapper.getSequence(1) instanceof UnmodifiableSequenceWrapper);

		wrapper.getSequence(0).size();
		Mockito.verify(sequence1).size();

		wrapper.getSequence(1).size();
		Mockito.verify(sequence2).size();
	}

	@Test
	public void testGetSequences() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		final UnmodifiableSequencesWrapper wrapper = new UnmodifiableSequencesWrapper(sequences);

		final Map<IResource, ISequence> sequencesMap = wrapper.getSequences();

		Assert.assertTrue(sequencesMap.containsKey(resource1));
		Assert.assertTrue(sequencesMap.containsKey(resource2));

		sequencesMap.get(resource1).size();
		Mockito.verify(sequence1).size();

		sequencesMap.get(resource2).size();
		Mockito.verify(sequence2).size();
	}

	@Test
	public void testSetSequences() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources1 = CollectionsUtil.makeArrayList(resource1);

		final List<IResource> resources2 = CollectionsUtil.makeArrayList(resource2);

		final Map<IResource, IModifiableSequence> map1 = CollectionsUtil.makeHashMap(resource1, sequence1);

		final Map<IResource, IModifiableSequence> map2 = CollectionsUtil.makeHashMap(resource2, sequence2);

		final ModifiableSequences sequences1 = new ModifiableSequences(resources1, map1);

		final ModifiableSequences sequences2 = new ModifiableSequences(resources2, map2);

		final UnmodifiableSequencesWrapper wrapper = new UnmodifiableSequencesWrapper(sequences1);

		Assert.assertTrue(wrapper.getSequence(0) instanceof UnmodifiableSequenceWrapper);
		Assert.assertTrue(wrapper.getSequence(resource1) instanceof UnmodifiableSequenceWrapper);

		wrapper.getSequence(resource1).size();
		Mockito.verify(sequence1).size();

		wrapper.setSequences(sequences2);

		wrapper.getSequence(resource2).size();
		Mockito.verify(sequence2).size();

	}

	@Test
	public void testSize() {
		final IResource resource1 = Mockito.mock(IResource.class, "resource-1");
		final IResource resource2 = Mockito.mock(IResource.class, "resource-2");

		final IModifiableSequence sequence1 = Mockito.mock(IModifiableSequence.class, "sequence1");
		final IModifiableSequence sequence2 = Mockito.mock(IModifiableSequence.class, "sequence2");

		final List<IResource> resources = CollectionsUtil.makeArrayList(resource1, resource2);

		final Map<IResource, IModifiableSequence> map = CollectionsUtil.makeHashMap(resource1, sequence1, resource2, sequence2);

		final ModifiableSequences sequences = new ModifiableSequences(resources, map);

		final UnmodifiableSequencesWrapper wrapper = new UnmodifiableSequencesWrapper(sequences);
		Assert.assertEquals(2, wrapper.size());
	}
}
