package com.mmxlabs.scheduler.optimiser.events.impl;

import org.junit.Assert;
import org.junit.Test;

public class AbstractScheduledEventImplTest {

	@Test
	public void testGetSetDuration() {
		final TestScheduledEvent<Object> event = new TestScheduledEvent<Object>();
		Assert.assertEquals(0, event.getDuration());
		event.setDuration(10);
		Assert.assertEquals(10, event.getDuration());
	}

	@Test
	public void testGetSetEndTime() {
		final TestScheduledEvent<Object> event = new TestScheduledEvent<Object>();
		Assert.assertEquals(0, event.getEndTime());
		event.setEndTime(10);
		Assert.assertEquals(10, event.getEndTime());
	}

	@Test
	public void testGetSetName() {
		final String name = "name";
		final TestScheduledEvent<Object> event = new TestScheduledEvent<Object>();
		Assert.assertNull(event.getName());
		event.setName(name);
		Assert.assertSame(name, event.getName());
	}

	@Test
	public void testGetSetSequenceElement() {
		final Object element = new Object();
		final TestScheduledEvent<Object> event = new TestScheduledEvent<Object>();
		Assert.assertNull(event.getSequenceElement());
		event.setSequenceElement(element);
		Assert.assertSame(element, event.getSequenceElement());
	}

	@Test
	public void testGetSetStartTime() {
		final TestScheduledEvent<Object> event = new TestScheduledEvent<Object>();
		Assert.assertEquals(0, event.getStartTime());
		event.setStartTime(10);
		Assert.assertEquals(10, event.getStartTime());
	}

	/**
	 * Private internal class to make the abstract class instantiatable
	 */
	private class TestScheduledEvent<T> extends AbstractScheduledEventImpl<T> {
	}
}
