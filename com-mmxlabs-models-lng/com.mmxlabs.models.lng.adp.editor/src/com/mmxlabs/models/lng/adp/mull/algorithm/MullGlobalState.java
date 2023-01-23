/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public class MullGlobalState {
	private final int loadWindowInDays;
	private final int volumeFlex;
	private final int fullCargoLotValue;
	private final int cargoVolume;
	private final List<MullCargoWrapper> cargoesToKeep;

	private final int loadWindowInHours;

	private final Set<BaseLegalEntity> firstPartyEntities;

	public MullGlobalState(final int loadWindowInDays, final int volumeFlex, final int fullCargoLotValue, final int cargoVolume, final List<MullCargoWrapper> cargoesToKeep, Set<BaseLegalEntity> firstPartyEntities) {
		if (loadWindowInDays < 0) {
			throw new IllegalStateException("Load window must be non-negative");
		}
		if (volumeFlex < 0) {
			throw new IllegalStateException("Volume flex must be non-negative");
		}
		if (fullCargoLotValue < 0) {
			throw new IllegalStateException("Full cargo lot value must be non-negative");
		}
		if (cargoVolume < 0) {
			throw new IllegalStateException("Cargo volume must be non-negative");
		}
		this.loadWindowInDays = loadWindowInDays;
		this.loadWindowInHours = loadWindowInDays * 24;
		this.volumeFlex = volumeFlex;
		this.fullCargoLotValue = fullCargoLotValue;
		this.cargoVolume = cargoVolume;
		this.cargoesToKeep = cargoesToKeep;
		this.firstPartyEntities = firstPartyEntities;
	}

	public int getLoadWindowInDays() {
		return loadWindowInDays;
	}
	
	public int getLoadWindowInHours() {
		return loadWindowInHours;
	}

	public int getVolumeFlex() {
		return volumeFlex;
	}
	
	public int getFullCargoLotValue() {
		return fullCargoLotValue;
	}
	
	public int getCargoVolume() {
		return cargoVolume;
	}
	
	public List<MullCargoWrapper> getCargoesToKeep() {
		return cargoesToKeep;
	}
	
	public Set<BaseLegalEntity> getFirstPartyEntities() {
		return firstPartyEntities;
	}
}
