package com.mmxlabs.common.delayedactions;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DelayedActionsManagerTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testPerformActions() {

		final DelayedActionsManager manager = new DelayedActionsManager();

		final Runnable r1 = context.mock(Runnable.class, "runnable1");

		final Runnable r2 = context.mock(Runnable.class, "runnable2");

		manager.queue(r1);
		manager.queue(r2);

		final Sequence sequence = context.sequence("sequence-name");
		
		context.checking(new Expectations() {
			{
				one(r1).run();
				inSequence(sequence);
				one(r2).run();
				inSequence(sequence);
			}
		});

		manager.performActions();

		// Do not expect actions to be re-run
		manager.performActions();

		context.assertIsSatisfied();
	}

	@Test
	public void testDispose() {

		final DelayedActionsManager manager = new DelayedActionsManager();

		final Runnable r1 = context.mock(Runnable.class, "runnable1");
		final Runnable r2 = context.mock(Runnable.class, "runnable2");

		manager.queue(r1);
		manager.queue(r2);

		manager.dispose();

		context.checking(new Expectations() {
			{
				// Nothing is expected to be run after dispose is called.
			}
		});

		manager.performActions();

		context.assertIsSatisfied();
	}

}
