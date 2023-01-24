/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;

/**
 * Class to keep the sum of volume in-take and off-take
 * for a given day.
 * @author FM
 *
 */
class InventoryDailyEvent {
	LocalDate date;
	int netVolumeIn = 0;
	int netVolumeOut = 0;
	int minVolume = 0;
	int maxVolume = 0;
	boolean level;
	
	public InventoryDailyEvent() {
	}
	
	public void setLevelVolume(int volume) {
		netVolumeIn = volume;
		netVolumeOut = 0;
		level = true;
	}
	
	public void addVolume(int volume) {
		netVolumeIn += volume;
	}
	
	public void subtractVolume(int volume) {
		netVolumeOut -= volume;
	}
	
	public boolean isLevel() {
		return level;
	}
}