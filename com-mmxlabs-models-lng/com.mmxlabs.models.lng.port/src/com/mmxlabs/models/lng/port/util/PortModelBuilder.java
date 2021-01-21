/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;

public class PortModelBuilder {

	public static final String TIMEZONE_UTC = "UTC";

	private final @NonNull PortModel portModel;

	public PortModelBuilder(@NonNull final PortModel portModel) {
		this.portModel = portModel;
	}

	public @NonNull PortModel getPortModel() {
		return portModel;
	}

	/**
	 * Make all existing ports use the UTC timezone. Useful in unit testing
	 */
	public void setAllExistingPortsToUTC() {
		portModel.getPorts().forEach(p -> p.getLocation().setTimeZone(TIMEZONE_UTC));
	}

	@NonNull
	public Route createRoute(final String name, final RouteOption option) {
		final Route r = PortFactory.eINSTANCE.createRoute();
		r.setName(option.getName());
		r.setRouteOption(option);

		portModel.getRoutes().add(r);
		return r;
	}

	@NonNull
	public Port createPort(@NonNull final String name, @NonNull final String mmxId, @NonNull final String timezoneId, final int defaultWindowStartHourOfDay, final int defaultWindowSize) {
		final Location location = PortFactory.eINSTANCE.createLocation();
		location.setName(name);
		location.setMmxId(mmxId);
		location.setTimeZone(timezoneId);

		final Port port = PortFactory.eINSTANCE.createPort();
		port.setName(name);

		port.setDefaultStartTime(defaultWindowStartHourOfDay);
		port.setDefaultWindowSize(defaultWindowSize);

		port.setBerths(1);

		port.setLocation(location);

		portModel.getPorts().add(port);

		return port;
	}

	public void configureLoadPort(@NonNull final Port port, final double cv, final int defaultLoadDurationInHours) {

		port.getCapabilities().add(PortCapability.LOAD);
		port.setLoadDuration(defaultLoadDurationInHours);

		port.setCvValue(cv);
	}

	public void configureDrydockPort(@NonNull final Port port) {
		port.getCapabilities().add(PortCapability.DRYDOCK);
	}

	public void configureMaintenancePort(@NonNull final Port port) {
		port.getCapabilities().add(PortCapability.MAINTENANCE);
	}

	public void configureTransferPort(@NonNull final Port port) {
		port.getCapabilities().add(PortCapability.TRANSFER);
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
	public PortGroup makePortGroup(@NonNull final String name, @NonNull final Port... ports) {
		final PortGroup portGroup = PortFactory.eINSTANCE.createPortGroup();
		portGroup.setName(name);
		for (final Port port : ports) {
			portGroup.getContents().add(port);
		}
		portModel.getPortGroups().add(portGroup);
		return portGroup;
	}

	@NonNull
	public PortGroup makePortGroup(@NonNull final String name, @NonNull final Collection<Port> ports) {
		final PortGroup portGroup = PortFactory.eINSTANCE.createPortGroup();
		portGroup.setName(name);
		portGroup.getContents().addAll(ports);
		portModel.getPortGroups().add(portGroup);
		return portGroup;
	}

	public ContingencyMatrixEntry setContingencyDelay(final Port from, final Port to, final int duration) {
		ContingencyMatrix matrix = portModel.getContingencyMatrix();
		if (matrix == null) {
			matrix = PortFactory.eINSTANCE.createContingencyMatrix();
			portModel.setContingencyMatrix(matrix);
		}

		for (final ContingencyMatrixEntry e : matrix.getEntries()) {
			if (Objects.equals(e.getFromPort(), from) && Objects.equals(e.getToPort(), to)) {
				e.setDuration(duration);
				return e;
			}
		}

		final ContingencyMatrixEntry e = PortFactory.eINSTANCE.createContingencyMatrixEntry();
		e.setFromPort(from);
		e.setToPort(to);
		e.setDuration(duration);

		matrix.getEntries().add(e);

		return e;

	}

	public void setContingencyDelayForAllVoyages(final int duration) {

		ContingencyMatrix matrix = portModel.getContingencyMatrix();
		if (matrix == null) {
			matrix = PortFactory.eINSTANCE.createContingencyMatrix();
			portModel.setContingencyMatrix(matrix);
		} else {
			matrix.getEntries().clear();
		}

		final EList<Port> ports = portModel.getPorts();
		for (final Port from : ports) {
			for (final Port to : ports) {
				if (from != to) {
					final ContingencyMatrixEntry e = PortFactory.eINSTANCE.createContingencyMatrixEntry();
					e.setFromPort(from);
					e.setToPort(to);
					e.setDuration(duration);

					matrix.getEntries().add(e);
				}
			}
		}
	}
}
