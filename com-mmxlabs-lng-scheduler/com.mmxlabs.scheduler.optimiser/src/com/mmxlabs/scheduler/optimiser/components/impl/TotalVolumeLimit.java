/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit;

/**
 * An implementation of {@link ITotalVolumeLimit} with a couple of utility methods.
 * 
 * @author hinton
 * 
 */
public class TotalVolumeLimit implements ITotalVolumeLimit {
	private ITimeWindow timeWindow;
	private final Set<IPortSlot> possibleSlots = new LinkedHashSet<IPortSlot>();
	private long volumeLimit;

	public TotalVolumeLimit(final ITimeWindow timeWindow, final Set<IPortSlot> possibleSlots, final long volumeLimit) {
		super();
		setTimeWindow(timeWindow);
		setPossibleSlots(possibleSlots);
		setVolumeLimit(volumeLimit);
	}

	/**
	 * 
	 */
	public TotalVolumeLimit() {
	}

	/**
	 * @param volumeLimit
	 */
	public void setVolumeLimit(final long volumeLimit) {
		this.volumeLimit = volumeLimit;
	}

	/**
	 * @param possibleSlots
	 */
	public void setPossibleSlots(final Set<IPortSlot> possibleSlots) {
		this.possibleSlots.clear();
		this.possibleSlots.addAll(possibleSlots);
	}

	/**
	 * A utility method; add the given port slot to possible slots iff the window for the given slot overlaps the time window for this limit.
	 * 
	 * @param slot
	 */
	public void addSlotIfWindowsMatch(final IPortSlot slot) {
		final ITimeWindow slotWindow = slot.getTimeWindow();
		final int min = slotWindow.getInclusiveStart();
		final int max = slotWindow.getExclusiveEnd();

		if (!((max < timeWindow.getInclusiveStart()) || (min > timeWindow.getExclusiveEnd()))) {
			possibleSlots.add(slot);
		}
	}

	/**
	 * @param timeWindow
	 */
	public void setTimeWindow(final ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit#getPossibleSlots ()
	 */
	@Override
	public Set<IPortSlot> getPossibleSlots() {
		return possibleSlots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit#getVolumeLimit ()
	 */
	@Override
	public long getVolumeLimit() {
		return volumeLimit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.ITotalVolumeLimit#getTimeWindow ()
	 */
	@Override
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}
}
