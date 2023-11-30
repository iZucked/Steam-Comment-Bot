package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

public abstract class Season {

	private final int northboundWaitingDays;
	private final int southboundWaitingDays;

	protected Season(final int northboundWaitingDays, final int southboundWaitingDays) {
		this.northboundWaitingDays = northboundWaitingDays;
		this.southboundWaitingDays = southboundWaitingDays;
	}

	public int getNorthboundWaitingDays() {
		return northboundWaitingDays;
	}

	public int getSouthboundWaitingDays() {
		return southboundWaitingDays;
	}
}
