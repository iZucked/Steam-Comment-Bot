/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;

class InventoryDailyEvent extends InventoryEvent {
	public final LocalDate date;
	
	public InventoryDailyEvent(LocalDate date, int netVolumeIn, int minVolume, int maxVolume) {
		super(netVolumeIn, minVolume, maxVolume);
		this.date = date;
	}
}