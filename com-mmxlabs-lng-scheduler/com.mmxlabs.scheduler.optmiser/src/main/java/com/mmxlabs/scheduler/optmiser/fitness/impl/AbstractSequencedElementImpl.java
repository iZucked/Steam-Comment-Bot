package com.mmxlabs.scheduler.optmiser.fitness.impl;

import com.mmxlabs.scheduler.optmiser.fitness.IScheduledElement;

public class AbstractSequencedElementImpl<T> implements IScheduledElement<T> {

	private String name;

	private int duration;

	private int startTime;

	private int endTime;

	private T element;

	protected AbstractSequencedElementImpl() {

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
