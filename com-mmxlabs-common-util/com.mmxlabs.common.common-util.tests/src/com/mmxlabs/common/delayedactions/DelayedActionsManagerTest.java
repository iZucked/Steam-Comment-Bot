/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class DelayedActionsManagerTest {

	@Test
	public void testPerformActions() {

		final DelayedActionsManager manager = new DelayedActionsManager();

		final Runnable r1 = Mockito.mock(Runnable.class, "runnable1");

		final Runnable r2 = Mockito.mock(Runnable.class, "runnable2");

		manager.queue(r1);
		manager.queue(r2);

		manager.performActions();
		manager.performActions();

		InOrder inOrder = Mockito.inOrder(r1, r2);
		inOrder.verify(r1).run();
		inOrder.verify(r2).run();

		Mockito.verifyNoMoreInteractions(r1, r2);
	}

	@Test
	public void testDispose() {

		final DelayedActionsManager manager = new DelayedActionsManager();

		final Runnable r1 = Mockito.mock(Runnable.class, "runnable1");
		final Runnable r2 = Mockito.mock(Runnable.class, "runnable2");

		manager.queue(r1);
		manager.queue(r2);

		manager.dispose();

		manager.performActions();

		Mockito.verifyNoMoreInteractions(r1, r2);

	}

}
