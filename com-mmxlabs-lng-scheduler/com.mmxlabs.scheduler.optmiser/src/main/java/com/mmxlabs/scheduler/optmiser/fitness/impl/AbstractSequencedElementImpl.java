package com.mmxlabs.scheduler.optmiser.fitness.impl;

import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optmiser.fitness.IScheduledElement;

public class AbstractSequencedElementImpl implements IScheduledElement {

	private String name;

	private int duration;

	private int startTime;

	private int endTime;

	private ISequenceElement element;

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
	public ISequenceElement getSequenceElement() {
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

	public void setSequenceElement(final ISequenceElement element) {
		this.element = element;
	}
}
