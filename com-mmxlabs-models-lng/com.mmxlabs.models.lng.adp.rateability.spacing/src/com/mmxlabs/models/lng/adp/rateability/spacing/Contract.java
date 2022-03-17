/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.spacing;

public class Contract {

	public Contract(String name, int contractSpacing, int maxContractSpacing, boolean fob, int interval, int maxInterval, String port) {
		super();
		this.name = name;
		this.contractSpacing = contractSpacing;
		this.maxContractSpacing = maxContractSpacing;
		this.interval = interval;
		this.maxInterval = maxInterval;
		this.fob = fob;
		this.port = port;
	}
	
	public Contract(String name, int contractSpacing, int maxContractSpacing, boolean fob, int interval, int maxInterval) {
		super();
		this.name = name;
		this.contractSpacing = contractSpacing;
		this.maxContractSpacing = maxContractSpacing;
		this.interval = interval;
		this.maxInterval = maxInterval;
		this.fob = fob;
		this.port = null;
	}
	
	public Contract(String name, int contractSpacing, boolean fob, int interval, String port) {
		super();
		this.name = name;
		this.contractSpacing = contractSpacing;
		this.interval = interval;
		this.fob = fob;
		this.port = port;
	}
	
	public Contract(String name, int contractSpacing, boolean fob, int interval)  {
		super();
		this.name = name;
		this.contractSpacing = contractSpacing;
		this.interval = interval;
		this.fob = fob;
	}

	String name;
	String port;
	int contractSpacing;
	int maxContractSpacing = Integer.MAX_VALUE;
	boolean fob;
	int interval;
	int maxInterval = Integer.MAX_VALUE;
	int turnaroundTime = Integer.MAX_VALUE;

	public String getName() {
		return this.name;
	}
}
