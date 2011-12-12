/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.Collections;
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
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintChecker;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

@RunWith(JMock.class)
public class OrderedSequenceElementsConstraintCheckerTest {

	IIndexingContext index = new SimpleIndexingContext();

	Mockery context = new JUnit4Mockery();

	@Test
	public void testOrderedSequenceElementsConstraintChecker() {

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		Assert.assertEquals("name", checker.getName());
	}

	@Test
	public void testCheckConstraintsISequencesOfT() {

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");
		final ISequenceElement obj5 = context.mock(ISequenceElement.class, "5");
		final ISequenceElement obj6 = context.mock(ISequenceElement.class, "6");

		provider.setElementOrder(obj1, obj2);
		provider.setElementOrder(obj3, obj4);
		provider.setElementOrder(obj4, obj5);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2, obj3, obj4, obj5, obj6));
		final IResource r = new Resource(index);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r, seq1);
		final Sequences sequences = new Sequences(Collections.singletonList(r), map);

		Assert.assertTrue(checker.checkConstraints(sequences));
	}

	@Test
	public void testCheckConstraintsISequencesOfT2() {

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");
		final ISequenceElement obj5 = context.mock(ISequenceElement.class, "5");
		final ISequenceElement obj6 = context.mock(ISequenceElement.class, "6");

		provider.setElementOrder(obj1, obj2);
		provider.setElementOrder(obj3, obj4);
		provider.setElementOrder(obj4, obj5);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj6, obj2, obj3, obj4, obj5));
		final IResource r = new Resource(index);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r, seq1);
		final Sequences sequences = new Sequences(Collections.singletonList(r), map);

		Assert.assertFalse(checker.checkConstraints(sequences));
	}

	@Test
	public void testCheckConstraintsISequencesOfT3() {

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");
		final ISequenceElement obj5 = context.mock(ISequenceElement.class, "5");
		final ISequenceElement obj6 = context.mock(ISequenceElement.class, "6");

		provider.setElementOrder(obj1, obj2);
		provider.setElementOrder(obj3, obj4);
		provider.setElementOrder(obj4, obj5);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2, obj3, obj4, obj6, obj5));
		final IResource r = new Resource(index);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r, seq1);
		final Sequences sequences = new Sequences(Collections.singletonList(r), map);

		Assert.assertFalse(checker.checkConstraints(sequences));
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString() {

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");
		final ISequenceElement obj5 = context.mock(ISequenceElement.class, "5");
		final ISequenceElement obj6 = context.mock(ISequenceElement.class, "6");

		provider.setElementOrder(obj1, obj2);
		provider.setElementOrder(obj3, obj4);
		provider.setElementOrder(obj4, obj5);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2, obj3, obj4, obj5, obj6));
		final IResource r = new Resource(index);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r, seq1);
		final Sequences sequences = new Sequences(Collections.singletonList(r), map);

		final List<String> messages = new LinkedList<String>();

		Assert.assertTrue(checker.checkConstraints(sequences, messages));

		Assert.assertTrue(messages.isEmpty());
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString2() {

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement obj3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement obj4 = context.mock(ISequenceElement.class, "4");
		final ISequenceElement obj5 = context.mock(ISequenceElement.class, "5");
		final ISequenceElement obj6 = context.mock(ISequenceElement.class, "6");

		provider.setElementOrder(obj1, obj2);
		provider.setElementOrder(obj3, obj4);
		provider.setElementOrder(obj4, obj5);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1, obj2, obj3, obj4, obj6, obj5));
		final IResource r = new Resource(index);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r, seq1);
		final Sequences sequences = new Sequences(Collections.singletonList(r), map);

		final List<String> messages = new LinkedList<String>();

		Assert.assertFalse(checker.checkConstraints(sequences, messages));

		Assert.assertEquals(1, messages.size());

	}

	/**
	 * Test that the left half of a order sequence must have it's right half
	 */
	@Test
	public void testCheckConstraintsISequencesOfTListOfString3() {

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");

		provider.setElementOrder(obj1, obj2);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj1));
		final IResource r = new Resource(index);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r, seq1);
		final Sequences sequences = new Sequences(Collections.singletonList(r), map);

		final List<String> messages = new LinkedList<String>();

		Assert.assertFalse(checker.checkConstraints(sequences, messages));

		Assert.assertEquals(1, messages.size());

	}

	/**
	 * Test that the right half of a order sequence can exist on its own.
	 */
	@Test
	public void testCheckConstraintsISequencesOfT4() {

		final OrderedSequenceElementsDataComponentProvider provider = new OrderedSequenceElementsDataComponentProvider("key");

		final ISequenceElement obj1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement obj2 = context.mock(ISequenceElement.class, "2");

		provider.setElementOrder(obj1, obj2);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

		checker.setProvider(provider);

		final ListSequence seq1 = new ListSequence(CollectionsUtil.makeArrayList(obj2));
		final IResource r = new Resource(index);

		final Map<IResource, ISequence> map = CollectionsUtil.makeHashMap(r, seq1);
		final Sequences sequences = new Sequences(Collections.singletonList(r), map);

		Assert.assertTrue(checker.checkConstraints(sequences));
	}

	@Test
	public void testGetSetProvider() {

		final IOrderedSequenceElementsDataComponentProvider provider = context.mock(IOrderedSequenceElementsDataComponentProvider.class);

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", "key");

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

		final OrderedSequenceElementsConstraintChecker checker = new OrderedSequenceElementsConstraintChecker("name", key);

		context.checking(new Expectations() {
			{
				one(data).getDataComponentProvider(key, IOrderedSequenceElementsDataComponentProvider.class);
			}
		});

		checker.setOptimisationData(data);

		context.assertIsSatisfied();
	}
}