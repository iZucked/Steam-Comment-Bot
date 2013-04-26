/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.annotations.IShippingCostAnnotation;

/**
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public class ShippingCostAnnotation implements IShippingCostAnnotation {
	private final int bookingTime;
	private final long shippingCost;
	private final IDetailTree details;

	public ShippingCostAnnotation(final int bookingTime, final long costNoBoiloff, final IDetailTree details) {
		super();
		this.bookingTime = bookingTime;
		this.shippingCost = costNoBoiloff;
		this.details = details;
	}

	@Override
	public int getBookingTime() {
		return bookingTime;
	}

	@Override
	public IDetailTree getDetails() {
		return details;
	}

	@Override
	public long getShippingCost() {
		return shippingCost;
	}
}
