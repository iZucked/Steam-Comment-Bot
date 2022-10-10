/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;

class InventoryDailyEvent {
	LocalDate date;
	int netVolumeIn = 0;
	int netVolumeOut = 0;
	int minVolume = 0;
	int maxVolume = 0;
	
	public InventoryDailyEvent() {
	}
	
	public void setVolume(int volume) {
		this.netVolumeIn = volume;
		this.netVolumeOut = volume;
	}
	
	public void addVolume(int volume) {
		netVolumeIn += volume;
	}
	
	public void subtractVolume(int volume) {
		netVolumeOut -= volume;
	}
}