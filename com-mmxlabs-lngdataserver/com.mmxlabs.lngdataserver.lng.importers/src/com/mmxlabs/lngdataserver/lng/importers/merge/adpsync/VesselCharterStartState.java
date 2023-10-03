/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge.adpsync;

import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.port.Port;

@NonNullByDefault
public class VesselCharterStartState {

	private final VesselCharter vesselCharter;
	private final ZonedDateTime startTime;
	private final int startHeel;
	private final double startHeelPrice;
	private final @Nullable Port vesselStartPort;
	private final double heelCv;

	public VesselCharterStartState(final VesselCharter vesselCharter, final ZonedDateTime startTime, final int startHeel, final double startHeelPrice, final @Nullable Port startPort, final double heelCv) {
		this.vesselCharter = vesselCharter;
		this.startTime = startTime;
		this.startHeel = startHeel;
		this.startHeelPrice = startHeelPrice;
		this.vesselStartPort = startPort;
		this.heelCv = heelCv;
	}

	public int getStartHeel() {
		return startHeel;
	}

	public double getStartHeelPrice() {
		return startHeelPrice;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public VesselCharter getVesselCharter() {
		return vesselCharter;
	}

	public @Nullable Port getVesselStartPort() {
		return vesselStartPort;
	}

	public double getHeelCv() {
		return heelCv;
	}
}
