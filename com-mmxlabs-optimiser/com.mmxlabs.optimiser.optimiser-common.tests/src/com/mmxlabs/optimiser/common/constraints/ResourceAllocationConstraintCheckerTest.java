/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.SimpleIndexingContext;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.ResourceAllocationConstraintProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.impl.Sequences;

public class ResourceAllocationConstraintCheckerTest {

	@NonNull
	private IIndexingContext index = new SimpleIndexingContext();

	@Test
	public void testResourceAllocationConstraintChecker() {

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name");

		Assert.assertEquals("name", checker.getName());
	}

	@Test
	public void testCheckConstraintsISequencesOfT() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider();

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = Mockito.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index, "r1");
		final IResource r2 = new Resource(index, "r2");

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = createChecker(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		Assert.assertTrue(checker.checkConstraints(sequences, null));
	}

	@Test
	public void testCheckConstraintsISequencesOfT2() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider();

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = Mockito.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index, "r1");
		final IResource r2 = new Resource(index, "r2");

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = createChecker(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		Assert.assertFalse(checker.checkConstraints(sequences, null));
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider();

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = Mockito.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index, "r1");
		final IResource r2 = new Resource(index, "r2");

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = createChecker(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		final List<String> messages = new LinkedList<String>();

		Assert.assertTrue(checker.checkConstraints(sequences, null, messages));

		Assert.assertTrue(messages.isEmpty());
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString2() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider();

		final ISequenceElement obj1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = Mockito.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index, "r1");
		final IResource r2 = new Resource(index, "r2");

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = createChecker(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		final List<String> messages = new LinkedList<String>();

		Assert.assertFalse(checker.checkConstraints(sequences, null, messages));

		Assert.assertEquals(1, messages.size());

	}

	private ResourceAllocationConstraintChecker createChecker(final IResourceAllocationConstraintDataComponentProvider provider) {
		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name");

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			public void configure() {
				bind(IResourceAllocationConstraintDataComponentProvider.class).toInstance(provider);

			}
		});
		injector.injectMembers(checker);
		return checker;
	}
}
