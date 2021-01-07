package com.mmxlabs.models.lng.adp.presentation.views;

public abstract class InventoryEvent {
	private int netVolumeIn;
	public final int minVolume;
	public final int maxVolume;
	
	public InventoryEvent(final int netVolumeIn, final int minVolume, final int maxVolume) {
		this.netVolumeIn = netVolumeIn;
		this.minVolume = minVolume;
		this.maxVolume = maxVolume;
	}
	
	public void addVolume(final int volume) {
		this.netVolumeIn += volume;
	}
	
	public int getNetVolumeIn() {
		return netVolumeIn;
	}
}
