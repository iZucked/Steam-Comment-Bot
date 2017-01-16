/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;

public class PortModelBuilder {

	public static final String TIMEZONE_UTC = "Etc/Utc";

	private final @NonNull PortModel portModel;

	public PortModelBuilder(@NonNull final PortModel portModel) {
		this.portModel = portModel;
	}

	@NonNull
	public Route createRoute(final String name, final RouteOption option) {
		final Route r = PortFactory.eINSTANCE.createRoute();
		r.setName(option.getName());
		r.setRouteOption(option);

		portModel.getRoutes().add(r);
		return r;
	}

	public void setPortToPortDistance(@NonNull final Port from, @NonNull final Port to, final int directDistance, final int suezDistance, final int panamaDistance, final boolean biDirectional) {

		for (final Route route : portModel.getRoutes()) {
			boolean foundForwardDistance = false;
			boolean foundReverseDistance = false;
			for (final RouteLine routeLine : route.getLines()) {
				if (routeLine.getFrom() == from && routeLine.getTo() == to) {
					foundForwardDistance = true;
					setDistance(directDistance, suezDistance, panamaDistance, route, routeLine);
				}
				if (biDirectional) {
					if (routeLine.getFrom() == to && routeLine.getTo() == from) {
						foundReverseDistance = true;
						setDistance(directDistance, suezDistance, panamaDistance, route, routeLine);
					}
				}
			}
			// Add missing distance lines if needed
			if (!foundForwardDistance) {
				final RouteLine line = PortFactory.eINSTANCE.createRouteLine();
				line.setFrom(from);
				line.setTo(to);
				setDistance(directDistance, suezDistance, panamaDistance, route, line);
				route.getLines().add(line);
			}
			if (biDirectional && !foundReverseDistance) {
				final RouteLine line = PortFactory.eINSTANCE.createRouteLine();
				line.setFrom(from);
				line.setTo(to);
				setDistance(directDistance, suezDistance, panamaDistance, route, line);
				route.getLines().add(line);
			}
		}
	}

	protected void setDistance(final int directDistance, final int suezDistance, final int panamaDistance, final @NonNull Route route, @NonNull final RouteLine routeLine) {
		switch (route.getRouteOption()) {
		case DIRECT:
			routeLine.setDistance(directDistance);
			break;
		case PANAMA:
			routeLine.setDistance(panamaDistance);
			break;
		case SUEZ:
			routeLine.setDistance(suezDistance);
			break;
		default:
			throw new IllegalStateException();
		}
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

	@NonNull
	public static List<APortSet<Port>> makePortSet(@NonNull final Port... ports) {
		final List<APortSet<Port>> list = new LinkedList<APortSet<Port>>();
		for (final Port port : ports) {
			list.add(port);
		}
		return list;
	}

	@NonNull
	public PortGroup makePortGroup(@NonNull String name, @NonNull final Port... ports) {
		final PortGroup portGroup = PortFactory.eINSTANCE.createPortGroup();
		portGroup.setName(name);
		for (final Port port : ports) {
			portGroup.getContents().add(port);
		}
		portModel.getPortGroups().add(portGroup);
		return portGroup;
	}

}
