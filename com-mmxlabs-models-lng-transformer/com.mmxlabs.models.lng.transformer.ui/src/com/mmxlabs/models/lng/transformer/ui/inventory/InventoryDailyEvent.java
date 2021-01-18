/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;

class InventoryDailyEvent {
	LocalDate date;
	int netVolumeIn = 0;
	int minVolume = 0;
	int maxVolume = 0;
	
	public InventoryDailyEvent() {
	}
	
	public InventoryDailyEvent(LocalDate date, int netVolumeIn, int minVolume, int maxVolume) {
		this.date = date;
		this.netVolumeIn = netVolumeIn;
		this.minVolume = minVolume;
		this.maxVolume = maxVolume;
	}
	
	public void addVolume(int volume) {
		netVolumeIn += volume;
	}
}