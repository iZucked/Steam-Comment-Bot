/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.tests.jobs.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlFactory;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.jobs.impl.CompositeJobControlFactory;

public class CompositeJobControlFactoryTest {

	@Test
	public void testCompositeJobControlFactory() {

		final MockJobControlFactory mock1 = new MockJobControlFactory(null);

		final MockJobControl mockControl1 = new MockJobControl();
		final MockJobControl mockControl2 = new MockJobControl();

		final MockJobControlFactory mock2 = new MockJobControlFactory(mockControl1);
		final MockJobControlFactory mock3 = new MockJobControlFactory(mockControl2);

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

		final MockJobControlFactory mock1 = new MockJobControlFactory(null);

		final MockJobControl mockControl1 = new MockJobControl();
		final MockJobControl mockControl2 = new MockJobControl();

		final MockJobControlFactory mock2 = new MockJobControlFactory(mockControl1);
		final MockJobControlFactory mock3 = new MockJobControlFactory(mockControl2);

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

		final MockJobControlFactory mock1 = new MockJobControlFactory(null);

		final MockJobControl mockControl1 = new MockJobControl();
		final MockJobControl mockControl2 = new MockJobControl();

		final MockJobControlFactory mock2 = new MockJobControlFactory(mockControl1);
		final MockJobControlFactory mock3 = new MockJobControlFactory(mockControl2);

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
		final MockJobControlFactory mock1 = new MockJobControlFactory(null);

		final MockJobControl mockControl1 = new MockJobControl();
		final MockJobControl mockControl2 = new MockJobControl();

		final MockJobControlFactory mock2 = new MockJobControlFactory(mockControl1);
		final MockJobControlFactory mock3 = new MockJobControlFactory(mockControl2);

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

	/**
	 * TODO: Use JMock
	 */
	private static final class MockJobControl implements IJobControl {

		@Override
		public IJobDescriptor getJobDescriptor() {
			return null;
		}

		@Override
		public void prepare() {

		}

		@Override
		public void start() {

		}

		@Override
		public void cancel() {

		}

		@Override
		public boolean isPauseable() {
			return false;
		}

		@Override
		public void pause() {

		}

		@Override
		public void resume() {

		}

		@Override
		public EJobState getJobState() {
			return null;
		}

		@Override
		public int getProgress() {
			return 0;
		}

		@Override
		public Object getJobOutput() {
			return null;
		}

		@Override
		public void addListener(final IJobControlListener listener) {

		}

		@Override
		public void removeListener(final IJobControlListener listener) {

		}

		@Override
		public void dispose() {

		}

	}

	private static final class MockJobControlFactory implements IJobControlFactory {

		private final IJobControl control;

		public MockJobControlFactory(final IJobControl control) {
			this.control = control;
		}

		@Override
		public IJobControl createJobControl(final IJobDescriptor job) {
			return control;
		}

	}
}
