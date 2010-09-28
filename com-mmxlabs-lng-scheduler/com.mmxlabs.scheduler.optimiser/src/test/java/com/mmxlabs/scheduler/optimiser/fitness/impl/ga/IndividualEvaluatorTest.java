package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@RunWith(JMock.class)
public class IndividualEvaluatorTest {

	private static final class MockSequenceScheduler extends
			AbstractSequenceScheduler<Object> {
		@Override
		public List<VoyagePlan> schedule(final IResource resource,
				final ISequence<Object> sequence) {
			throw new UnsupportedOperationException();
		}
	}

	private final Mockery context = new JUnit4Mockery();

	@Test
	public void testEvaluate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecode() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetup() {
		fail("Not yet implemented");
	}

	@Test
	public void testInit1() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);
		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit2() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setDistanceProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit3() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setDurationsProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit4() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setFitnessComponents(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit5() {
		Assert.fail("Not implemented");

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setFitnessComponentWeights(null);

		evaluator.init();

	}

	@Test(expected = IllegalStateException.class)
	public void testInit6() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setPortProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit7() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setSequenceScheduler(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit8() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setTimeWindowProvider(null);

		evaluator.init();
	}

	@Test(expected = IllegalStateException.class)
	public void testInit9() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		createFullyInitialisedEvaluator(evaluator);

		evaluator.setVesselProvider(null);

		evaluator.init();
	}

	/**
	 * For use in initXXX() tests. Add in all required elements for init().
	 * initXXX() tests should null one entry to test failure.
	 * 
	 * @param evaluator
	 */
	private void createFullyInitialisedEvaluator(
			final IndividualEvaluator<Object> evaluator) {

		final AbstractSequenceScheduler<Object> scheduler = new MockSequenceScheduler();
		evaluator.setSequenceScheduler(scheduler);

		final Collection<ICargoSchedulerFitnessComponent<Object>> fitnessComponents = Collections
				.emptyList();
		evaluator.setFitnessComponents(fitnessComponents);

		@SuppressWarnings("unchecked")
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = context
				.mock(IMultiMatrixProvider.class);
		evaluator.setDistanceProvider(distanceProvider);

		@SuppressWarnings("unchecked")
		final IElementDurationProvider<Object> durationsProvider = context
				.mock(IElementDurationProvider.class);
		evaluator.setDurationsProvider(durationsProvider);

		final IPortProvider portProvider = context.mock(IPortProvider.class);
		evaluator.setPortProvider(portProvider);

		final ITimeWindowDataComponentProvider timeWindowProvider = context
				.mock(ITimeWindowDataComponentProvider.class);
		evaluator.setTimeWindowProvider(timeWindowProvider);

		final IVesselProvider vesselProvider = context
				.mock(IVesselProvider.class);
		evaluator.setVesselProvider(vesselProvider);
	}

	@Test
	public void testGetSetSequenceScheduler() {

		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final AbstractSequenceScheduler<Object> value = new MockSequenceScheduler();

		Assert.assertNull(evaluator.getSequenceScheduler());
		evaluator.setSequenceScheduler(value);
		Assert.assertSame(value, evaluator.getSequenceScheduler());

	}

	@Test
	public void testGetSetTimeWindowProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final ITimeWindowDataComponentProvider value = context
				.mock(ITimeWindowDataComponentProvider.class);
		Assert.assertNull(evaluator.getTimeWindowProvider());
		evaluator.setTimeWindowProvider(value);
		Assert.assertSame(value, evaluator.getTimeWindowProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getTimeWindowProvider());
	}

	@Test
	public void testGetSetFitnessComponents() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final Collection<ICargoSchedulerFitnessComponent<Object>> value = Collections
				.emptyList();
		Assert.assertNull(evaluator.getFitnessComponents());
		evaluator.setFitnessComponents(value);
		Assert.assertSame(value, evaluator.getFitnessComponents());

		evaluator.dispose();
		Assert.assertNull(evaluator.getFitnessComponents());

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetSetFitnessComponentWeights() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		Assert.assertNull(evaluator.getFitnessComponentWeights());
		evaluator.setFitnessComponentWeights(null);

	}

	@Test
	public void testGetSetAdjustArrivalTimes() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		Assert.assertFalse(evaluator.isAdjustArrivalTimes());
		evaluator.setAdjustArrivalTimes(true);
		Assert.assertTrue(evaluator.isAdjustArrivalTimes());
		evaluator.setAdjustArrivalTimes(false);
		Assert.assertFalse(evaluator.isAdjustArrivalTimes());
	}

	@Test
	public void testGetSetVesselProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final IVesselProvider value = context.mock(IVesselProvider.class);
		Assert.assertNull(evaluator.getVesselProvider());
		evaluator.setVesselProvider(value);
		Assert.assertSame(value, evaluator.getVesselProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getVesselProvider());
	}

	@Test
	public void testGetSetPortProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		final IPortProvider value = context.mock(IPortProvider.class);
		Assert.assertNull(evaluator.getPortProvider());
		evaluator.setPortProvider(value);
		Assert.assertSame(value, evaluator.getPortProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getPortProvider());
	}

	@Test
	public void testGetSetDistanceProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		@SuppressWarnings("unchecked")
		final IMultiMatrixProvider<IPort, Integer> value = context
				.mock(IMultiMatrixProvider.class);
		Assert.assertNull(evaluator.getDistanceProvider());
		evaluator.setDistanceProvider(value);
		Assert.assertSame(value, evaluator.getDistanceProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getDistanceProvider());
	}

	@Test
	public void testGetSetDurationsProvider() {
		final IndividualEvaluator<Object> evaluator = new IndividualEvaluator<Object>();
		@SuppressWarnings("unchecked")
		final IElementDurationProvider<Object> value = context
				.mock(IElementDurationProvider.class);
		Assert.assertNull(evaluator.getDurationsProvider());
		evaluator.setDurationsProvider(value);
		Assert.assertSame(value, evaluator.getDurationsProvider());

		evaluator.dispose();
		Assert.assertNull(evaluator.getDurationsProvider());

	}
}
