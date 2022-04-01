/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

public class CargoWindowData {
	
	private int startOfStartWindow;
	private int endOfStartWindow;
	private int startOfEndWindow;
	private int endOfEndWindow;

	public CargoWindowData(int startOfStartWindow,
	int endOfStartWindow,
	int startOfEndWindow,
	int endOfEndWindow) {
		this.startOfStartWindow = startOfStartWindow;
		this.endOfStartWindow = endOfStartWindow;
		this.startOfEndWindow = startOfEndWindow;
		this.endOfEndWindow = endOfEndWindow;
	}

	public int getStartOfStartWindow() {
		return startOfStartWindow;
	}

	public int getEndOfStartWindow() {
		return endOfStartWindow;
	}

	public int getStartOfEndWindow() {
		return startOfEndWindow;
	}

	public int getEndOfEndWindow() {
		return endOfEndWindow;
	}
}
