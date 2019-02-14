/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LightWeightCargoDetails {
	private PortType type;
	
	public LightWeightCargoDetails(PortType type) {
		this.type = type;
	}
	
	public PortType getType() {
		return type;
	}
	
	public void setType(PortType type) {
		this.type = type;
	}
	
}
