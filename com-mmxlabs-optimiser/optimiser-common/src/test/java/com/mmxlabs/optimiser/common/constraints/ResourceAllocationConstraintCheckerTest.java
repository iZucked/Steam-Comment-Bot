/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.LinkedList;
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
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

@RunWith(JMock.class)
public class ResourceAllocationConstraintCheckerTest {

	IIndexingContext index = new SimpleIndexingContext();
	Mockery context = new JUnit4Mockery();

	@Test
	public void testResourceAllocationConstraintChecker() {

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name", "key");

		Assert.assertEquals("name", checker.getName());
	}

	@Test
	public void testCheckConstraintsISequencesOfT() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		Assert.assertTrue(checker.checkConstraints(sequences));
	}

	@Test
	public void testCheckConstraintsISequencesOfT2() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		Assert.assertFalse(checker.checkConstraints(sequences));
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj3, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		final List<String> messages = new LinkedList<String>();

		Assert.assertTrue(checker.checkConstraints(sequences, messages));

		Assert.assertTrue(messages.isEmpty());
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString2() {

		final IResourceAllocationConstraintDataComponentProviderEditor provider = new ResourceAllocationConstraintProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");

		final IResource r1 = new Resource(index);
		final IResource r2 = new Resource(index);

		provider.setAllowedResources(obj1, CollectionsUtil.makeArrayList(r1));
		provider.setAllowedResources(obj2, CollectionsUtil.makeArrayList(r1, r2));
		provider.setAllowedResources(obj3, CollectionsUtil.makeArrayList(r2));

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj3, obj2));

		final ListSequence seq2 = new ListSequence(CollectionsUtil.makeArrayList(obj2, obj4));

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r1, seq1, r2, seq2);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r1, r2), map);

		final List<String> messages = new LinkedList<String>();

		Assert.assertFalse(checker.checkConstraints(sequences, messages));

		Assert.assertEquals(1, messages.size());

	}

	@Test
	public void testGetSetProvider() {

		final IResourceAllocationConstraintDataComponentProvider provider = context.mock(IResourceAllocationConstraintDataComponentProvider.class);

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name", "key");

		checker.setProvider(provider);

		Assert.assertSame(provider, checker.getProvider());
	}

	@Test
	public void testSetOptimisationData() {

		// Tell the context to return null, rather than try to create a sample
		// object of this type as it causes a class cast exception.
		context.setDefaultResultForType(IDataComponentProvider.class, null);
		final IOptimisationData data = context.mock(IOptimisationData.class);

		final String key = "key";

		final ResourceAllocationConstraintChecker checker = new ResourceAllocationConstraintChecker("name", key);

		context.checking(new Expectations() {
			{
				one(data).getDataComponentProvider(key, IResourceAllocationConstraintDataComponentProvider.class);
			}
		});

		checker.setOptimisationData(data);

		context.assertIsSatisfied();
	}

}
