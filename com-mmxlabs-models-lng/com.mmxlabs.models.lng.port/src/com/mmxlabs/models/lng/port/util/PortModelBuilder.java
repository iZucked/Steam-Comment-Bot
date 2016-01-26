/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.types.PortCapability;

public class PortModelBuilder {

	public static final String TIMEZONE_UTC = "Etc/Utc";

	private final @NonNull PortModel portModel;

	public PortModelBuilder(@NonNull final PortModel portModel) {
		this.portModel = portModel;
	}

	@NonNull
	public Port createPort(@NonNull final String name, @NonNull final String timezoneId, final int defaultWindowStartHourOfDay, final int defaultWindowSize) {
		final Port port = PortFactory.eINSTANCE.createPort();

		port.setName(name);
		port.setTimeZone(timezoneId);

		port.setDefaultStartTime(defaultWindowStartHourOfDay);
		port.setDefaultWindowSize(defaultWindowSize);

		port.setBerths(1);

		portModel.getPorts().add(port);

		return port;
	}

	public void configureLoadPort(@NonNull final Port port, final double cv, final int defaultLoadDurationInHours) {

		port.getCapabilities().add(PortCapability.LOAD);
		port.setLoadDuration(defaultLoadDurationInHours);

		port.setCvValue(cv);
	}

	public void configureDischargePort(@NonNull final Port port, final int defaultDischargeDurationInHours, @Nullable final Double minCV, @Nullable final Double maxCV) {

		port.getCapabilities().add(PortCapability.DISCHARGE);
		port.setDischargeDuration(defaultDischargeDurationInHours);

		if (minCV != null) {
			port.setMinCvValue(minCV);
		} else {
			port.unsetMinCvValue();
		}
		if (maxCV != null) {
			port.setMaxCvValue(maxCV);
		} else {
			port.unsetMaxCvValue();
		}
	}
}
