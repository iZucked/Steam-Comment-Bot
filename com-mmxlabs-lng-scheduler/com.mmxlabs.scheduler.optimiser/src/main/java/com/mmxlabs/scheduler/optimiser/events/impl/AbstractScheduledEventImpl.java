/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.events.IScheduledEvent;

/**
 * Abstract implementation of {@link IScheduledEvent} intended for use as a base
 * class for {@link IScheduledEvent} based implementations.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public abstract class AbstractScheduledEventImpl<T> implements IScheduledEvent<T> {

	private String name;

	private int duration;

	private int startTime;

	private int endTime;

	private T element;

	protected AbstractScheduledEventImpl() {

	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public int getEndTime() {
		return endTime;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getSequenceElement() {
		return element;
	}

	@Override
	public int getStartTime() {
		return startTime;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDuration(final int duration) {
		this.duration = duration;
	}

	public void setStartTime(final int startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(final int endTime) {
		this.endTime = endTime;
	}

	public void setSequenceElement(final T element) {
		this.element = element;
	}
}
