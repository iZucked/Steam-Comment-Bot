/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.vesselgroups;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupDefinition;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselGroupsVersion;
import com.mmxlabs.lngdataserver.integration.models.vesselgroups.VesselTypeConstants;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class VesselGroupsToScenarioImporter {

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final FleetModel fleetModel, final VesselGroupsVersion version) {

		final CompoundCommand cmd = new CompoundCommand("Update vessel groups");

		// Gather existing curves
		final Map<String, VesselGroup> map = new HashMap<>();
		final Set<VesselGroup> existing = new HashSet<>();

		fleetModel.getVesselGroups().forEach(c -> map.put(c.getName(), c));
		fleetModel.getVesselGroups().forEach(existing::add);

		final Map<String, Object> typeMap = new HashMap<>();

		fleetModel.getVessels().forEach(c -> typeMap.put(VesselTypeConstants.VESSEL_PREFIX + c.getName(), c));
		fleetModel.getVesselGroups().forEach(c -> typeMap.put(VesselTypeConstants.VESSEL_GROUP_PREFIX + c.getName(), c));

		final Set<VesselGroup> updated = new HashSet<>();

		// Step 1, add in the groups.
		for (final VesselGroupDefinition def : version.getGroups()) {
			final String name = def.getName();

			VesselGroup existingGroup = map.get(name);
			if (existingGroup == null) {
				existingGroup = FleetFactory.eINSTANCE.createVesselGroup();
				existingGroup.setName(name);
				cmd.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSEL_GROUPS, existingGroup));
				map.put(name,  existingGroup);
			}
			typeMap.put(VesselTypeConstants.VESSEL_GROUP_PREFIX + existingGroup.getName(), existingGroup);
			updated.add(existingGroup);
		}
		// Step 2: Update groups now all registered
		for (final VesselGroupDefinition def : version.getGroups()) {
			final String name = def.getName();

			VesselGroup existingGroup = map.get(name);
			assert (existingGroup != null);
			List<Object> newContents = new LinkedList<>();
			for (String id : def.getEntries()) {
				Object obj = typeMap.get(id);
				if (obj == null) {
					throw new IllegalStateException("Unknown object: " + id);
				}
				newContents.add(obj);
			}

			// Find vessels to remove
			Set<Object> existingValues = new HashSet<>(existingGroup.getVessels());
			existingValues.removeAll(newContents);
			if (!existingValues.isEmpty()) {
				cmd.append(RemoveCommand.create(editingDomain, existingGroup, FleetPackage.Literals.VESSEL_GROUP__VESSELS, existingValues));
			}
			// Find vessel to add
			newContents.removeAll(existingGroup.getVessels());
			if (!newContents.isEmpty()) {
				cmd.append(AddCommand.create(editingDomain, existingGroup, FleetPackage.Literals.VESSEL_GROUP__VESSELS, newContents));
			}
		}

		existing.removeAll(updated);
		if (!existing.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, existing));
		}

		VersionRecord record = fleetModel.getVesselGroupVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

		return cmd;
	}
}
