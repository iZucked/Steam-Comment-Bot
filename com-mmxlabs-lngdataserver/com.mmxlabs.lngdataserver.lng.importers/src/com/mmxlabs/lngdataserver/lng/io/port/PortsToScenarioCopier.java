/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.port;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.ports.model.PortCapability;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class PortsToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortsToScenarioCopier.class);

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final PortModel portModel, final PortsVersion version) {

		final CompoundCommand cc = new CompoundCommand("Update Ports");

		final Map<String, List<Port>> locationToPort = portModel.getPorts().stream() //
				.filter(p -> p.getLocation() != null) //
				.filter(p -> p.getLocation().getMmxId() != null) //
				.collect(Collectors.groupingBy(p -> p.getLocation().getMmxId()));

		// Create all port update
		for (final com.mmxlabs.lngdataserver.integration.ports.model.Port port : version.getPorts()) {

			final String mmxID = port.getLocationMmxId();
			final List<Port> lingoPorts = locationToPort.get(mmxID);
			if (lingoPorts != null) {
				for (final Port lingo_port : lingoPorts) {

					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__ALLOW_COOLDOWN, port.getAllowCooldown()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__BERTHS, port.getBerths()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__CV_VALUE, port.getCvValue()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__LOAD_DURATION, port.getLoadDuration()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__DISCHARGE_DURATION, port.getDischargeDuration()));
					if (port.getMaxCvValue() == 0.0) {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MAX_CV_VALUE, SetCommand.UNSET_VALUE));
					} else {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MAX_CV_VALUE, port.getMaxCvValue()));
					}
					if (port.getMinCvValue() == 0.0) {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MIN_CV_VALUE, SetCommand.UNSET_VALUE));
					} else {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MIN_CV_VALUE, port.getMinCvValue()));
					}
					{
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__DEFAULT_START_TIME, port.getDefaultStartTime().getHour()));
					}
					{
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE, port.getDefaultWindowSize()));
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE_UNITS, map(port.getDefaultWindowSizeUnits())));
					}

					final Set<com.mmxlabs.models.lng.types.PortCapability> caps = new HashSet<>();
					if (port.getCapabilities() != null) {
						for (final PortCapability lingo_pc : port.getCapabilities()) {
							switch (lingo_pc) {
							case DISCHARGE:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.DISCHARGE);
								break;
							case DRYDOCK:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.DRYDOCK);
								break;
							case LOAD:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.LOAD);
								break;
							case MAINTENANCE:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.MAINTENANCE);
								break;
							default:
								break;

							}
						}
					}
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__CAPABILITIES, caps));
				}
			}
		}

		VersionRecord record = portModel.getPortVersionRecord();
		cc.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cc.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cc.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

		return cc;
	}

	private static TimePeriod map(com.mmxlabs.lngdataserver.integration.ports.model.TimePeriod period) {
		if (period == null) {
			return TimePeriod.DAYS;
		}
		switch (period) {
		case DAYS:
			return TimePeriod.DAYS;
		case HOURS:
			return TimePeriod.HOURS;
		case MONTHS:
			return TimePeriod.MONTHS;
		}
		throw new IllegalArgumentException();
	}

}
