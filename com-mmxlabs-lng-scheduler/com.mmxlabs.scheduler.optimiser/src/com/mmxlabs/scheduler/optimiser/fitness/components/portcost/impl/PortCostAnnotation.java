/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.portcost.impl;

import com.mmxlabs.scheduler.optimiser.fitness.components.portcost.IPortCostAnnotation;

public class PortCostAnnotation implements IPortCostAnnotation {
	private final long cost;

	public PortCostAnnotation(long cost) {
		super();
		this.cost = cost;
	}
	
	@Override
	public long getPortCost() {
		return cost;
	}
}
