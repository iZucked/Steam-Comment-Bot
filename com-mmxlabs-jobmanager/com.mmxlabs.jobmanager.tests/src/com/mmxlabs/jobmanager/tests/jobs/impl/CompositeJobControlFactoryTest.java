/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.tests.jobs.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlFactory;
import com.mmxlabs.jobmanager.jobs.impl.CompositeJobControlFactory;

public class CompositeJobControlFactoryTest {

	@Test
	public void testCompositeJobControlFactory() {

		final IJobControlFactory mock1 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock2 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock3 = Mockito.mock(IJobControlFactory.class);

		final IJobControl mockControl1 = Mockito.mock(IJobControl.class);
		final IJobControl mockControl2 = Mockito.mock(IJobControl.class);

		Mockito.when(mock2.createJobControl(null)).thenReturn(mockControl1);
		Mockito.when(mock3.createJobControl(null)).thenReturn(mockControl2);

		final CompositeJobControlFactory factory = new CompositeJobControlFactory();
		factory.addJobControlFactory(mock1);
		factory.addJobControlFactory(mock2);
		factory.addJobControlFactory(mock3);

		Assert.assertSame(mockControl1, factory.createJobControl(null));

		final List<IJobControlFactory> expectedFactories = new ArrayList<IJobControlFactory>(3);
		expectedFactories.add(mock1);
		expectedFactories.add(mock2);
		expectedFactories.add(mock3);
		Assert.assertEquals(expectedFactories, factory.getJobControlFactories());
	}

	@Test
	public void testCompositeJobControlFactoryCollectionOfIJobControlFactory() {

		final IJobControlFactory mock1 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock2 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock3 = Mockito.mock(IJobControlFactory.class);

		final IJobControl mockControl1 = Mockito.mock(IJobControl.class);
		final IJobControl mockControl2 = Mockito.mock(IJobControl.class);

		Mockito.when(mock2.createJobControl(null)).thenReturn(mockControl1);
		Mockito.when(mock3.createJobControl(null)).thenReturn(mockControl2);

		final Collection<IJobControlFactory> factories = new ArrayList<IJobControlFactory>(3);
		factories.add(mock1);
		factories.add(mock2);
		factories.add(mock3);
		final CompositeJobControlFactory factory = new CompositeJobControlFactory(factories);

		Assert.assertSame(mockControl1, factory.createJobControl(null));

		final List<IJobControlFactory> expectedFactories = new ArrayList<IJobControlFactory>(3);
		expectedFactories.add(mock1);
		expectedFactories.add(mock2);
		expectedFactories.add(mock3);
		Assert.assertEquals(expectedFactories, factory.getJobControlFactories());
	}

	@Test
	public void testCompositeJobControlFactoryIJobControlFactoryArray() {

		final IJobControlFactory mock1 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock2 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock3 = Mockito.mock(IJobControlFactory.class);

		final IJobControl mockControl1 = Mockito.mock(IJobControl.class);
		final IJobControl mockControl2 = Mockito.mock(IJobControl.class);

		Mockito.when(mock2.createJobControl(null)).thenReturn(mockControl1);
		Mockito.when(mock3.createJobControl(null)).thenReturn(mockControl2);

		final CompositeJobControlFactory factory = new CompositeJobControlFactory(mock1, mock2, mock3);

		Assert.assertSame(mockControl1, factory.createJobControl(null));

		final List<IJobControlFactory> expectedFactories = new ArrayList<IJobControlFactory>(3);
		expectedFactories.add(mock1);
		expectedFactories.add(mock2);
		expectedFactories.add(mock3);
		Assert.assertEquals(expectedFactories, factory.getJobControlFactories());
	}

	@Test
	public void testRemoveJobControlFactory() {

		final IJobControlFactory mock1 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock2 = Mockito.mock(IJobControlFactory.class);
		final IJobControlFactory mock3 = Mockito.mock(IJobControlFactory.class);

		final IJobControl mockControl1 = Mockito.mock(IJobControl.class);
		final IJobControl mockControl2 = Mockito.mock(IJobControl.class);

		Mockito.when(mock2.createJobControl(null)).thenReturn(mockControl1);
		Mockito.when(mock3.createJobControl(null)).thenReturn(mockControl2);

		final CompositeJobControlFactory factory = new CompositeJobControlFactory(mock1, mock2, mock3);

		Assert.assertSame(mockControl1, factory.createJobControl(null));

		final List<IJobControlFactory> expectedFactories = new ArrayList<IJobControlFactory>(3);
		expectedFactories.add(mock1);
		expectedFactories.add(mock2);
		expectedFactories.add(mock3);
		Assert.assertEquals(expectedFactories, factory.getJobControlFactories());

		factory.removeJobControlFactory(mock2);

		final List<IJobControlFactory> expectedFactories2 = new ArrayList<IJobControlFactory>(3);
		expectedFactories2.add(mock1);
		expectedFactories2.add(mock3);
		Assert.assertEquals(expectedFactories2, factory.getJobControlFactories());

		Assert.assertSame(mockControl2, factory.createJobControl(null));

	}
}
