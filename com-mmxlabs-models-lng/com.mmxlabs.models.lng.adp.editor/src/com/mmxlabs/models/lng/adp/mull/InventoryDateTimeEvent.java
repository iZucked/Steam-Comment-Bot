/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDateTime;

public class InventoryDateTimeEvent extends InventoryEvent {
	private final LocalDateTime dateTime;
	
	public InventoryDateTimeEvent(final LocalDateTime dateTime, final int netVolumeIn, final int minVolume, final int maxVolume) {
		super(netVolumeIn, minVolume, maxVolume);
		this.dateTime = dateTime;
	}
	
	public LocalDateTime getDateTime() {
		return this.dateTime;
	}
}
