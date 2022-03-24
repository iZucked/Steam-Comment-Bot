/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.profile.IMullProfile;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class AlgorithmState {
	private final IMullProfile mullProfile;
	private final Map<Vessel, LocalDateTime> vesselUsageLookBehind;
	private final Map<Vessel, LocalDateTime> vesselUsageLookAhead;
	private final Set<Vessel> firstPartyVessels;

	public AlgorithmState(final IMullProfile mullProfile, final Map<Vessel, LocalDateTime> vesselUsageLookBehind, final Map<Vessel, LocalDateTime> vesselUsageLookAhead, final Set<Vessel> firstPartyVessels) {
		this.mullProfile = mullProfile;
		this.vesselUsageLookBehind = vesselUsageLookBehind;
		this.vesselUsageLookAhead = vesselUsageLookAhead;
		this.firstPartyVessels = firstPartyVessels;
	}

	public Map<Vessel, LocalDateTime> getVesselUsageLookBehind() {
		return vesselUsageLookBehind;
	}

	public Map<Vessel, LocalDateTime> getVesselUsageLookAhead() {
		return vesselUsageLookAhead;
	}

	public Set<Vessel> getFirstPartyVessels() {
		return firstPartyVessels;
	}

	public IMullProfile getMullProfile() {
		return mullProfile;
	}
}
