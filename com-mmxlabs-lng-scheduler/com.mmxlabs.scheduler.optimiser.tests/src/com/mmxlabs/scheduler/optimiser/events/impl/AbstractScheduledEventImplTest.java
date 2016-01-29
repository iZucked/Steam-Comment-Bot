/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class AbstractScheduledEventImplTest {


	@Test
	public void testGetSetDuration() {
		final TestScheduledEvent event = new TestScheduledEvent();
		Assert.assertEquals(0, event.getDuration());
		event.setDuration(10);
		Assert.assertEquals(10, event.getDuration());
	}

	@Test
	public void testGetSetEndTime() {
		final TestScheduledEvent event = new TestScheduledEvent();
		Assert.assertEquals(0, event.getEndTime());
		event.setEndTime(10);
		Assert.assertEquals(10, event.getEndTime());
	}

	@Test
	public void testGetSetName() {
		final String name = "name";
		final TestScheduledEvent event = new TestScheduledEvent();
		Assert.assertNull(event.getName());
		event.setName(name);
		Assert.assertSame(name, event.getName());
	}

	@Test
	public void testGetSetSequenceElement() {
		final ISequenceElement element = Mockito.mock(ISequenceElement.class);
		final TestScheduledEvent event = new TestScheduledEvent();
		Assert.assertNull(event.getSequenceElement());
		event.setSequenceElement(element);
		Assert.assertSame(element, event.getSequenceElement());
	}

	@Test
	public void testGetSetStartTime() {
		final TestScheduledEvent event = new TestScheduledEvent();
		Assert.assertEquals(0, event.getStartTime());
		event.setStartTime(10);
		Assert.assertEquals(10, event.getStartTime());
	}

	/**
	 * Private internal class to make the abstract class instantiatable
	 */
	private class TestScheduledEvent extends AbstractScheduledEventImpl {
	}
}
