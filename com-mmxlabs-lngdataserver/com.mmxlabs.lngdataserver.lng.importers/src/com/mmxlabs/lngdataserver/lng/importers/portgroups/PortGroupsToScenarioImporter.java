package com.mmxlabs.lngdataserver.lng.importers.portgroups;

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

import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupDefinition;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortGroupsVersion;
import com.mmxlabs.lngdataserver.integration.models.portgroups.PortTypeConstants;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class PortGroupsToScenarioImporter {

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final PortModel portModel, final PortGroupsVersion version) {

		final CompoundCommand cmd = new CompoundCommand("Update port groups");

		// Gather existing curves
		final Map<String, PortGroup> map = new HashMap<>();
		final Set<PortGroup> existing = new HashSet<>();

		portModel.getPortGroups().forEach(c -> map.put(c.getName(), c));
		portModel.getPortGroups().forEach(existing::add);

		final Map<String, Object> typeMap = new HashMap<>();

		portModel.getPorts().forEach(c -> typeMap.put(PortTypeConstants.PORT_PREFIX + c.getLocation().getMmxId(), c));
		portModel.getPortCountryGroups().forEach(c -> typeMap.put(PortTypeConstants.COUNTRY_GROUP_PREFIX + c.getName(), c));
		portModel.getPortGroups().forEach(c -> typeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + c.getName(), c));

		final Set<PortGroup> updated = new HashSet<>();

		// Step 1, add in the groups.
		for (final PortGroupDefinition def : version.getGroups()) {
			final String name = def.getName();

			PortGroup existingGroup = map.get(name);
			if (existingGroup == null) {
				existingGroup = PortFactory.eINSTANCE.createPortGroup();
				existingGroup.setName(name);
				cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORT_GROUPS, existingGroup));
			}
			typeMap.put(PortTypeConstants.PORT_GROUP_PREFIX + existingGroup.getName(), existingGroup);
			updated.add(existingGroup);
		}
		// Step 2: Update groups now all registered
		for (final PortGroupDefinition def : version.getGroups()) {
			final String name = def.getName();

			PortGroup existingGroup = map.get(name);
			assert (existingGroup != null);
			if (!existingGroup.getContents().isEmpty()) {
				cmd.append(RemoveCommand.create(editingDomain, existingGroup, PortPackage.Literals.PORT_GROUP__CONTENTS, new LinkedList<>(existingGroup.getContents())));
			}
			List<Object> newContents = new LinkedList<>();
			for (String id : def.getEntries()) {
				Object obj = typeMap.get(id);
				if (obj == null) {
					throw new IllegalStateException("Unknown object: " + id);
				}
				newContents.add(obj);
			}
			if (!newContents.isEmpty()) {
				cmd.append(AddCommand.create(editingDomain, existingGroup, PortPackage.Literals.PORT_GROUP__CONTENTS, newContents));
			}
		}

		existing.removeAll(updated);
		if (!existing.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, existing));
		}


		VersionRecord record = portModel.getPortGroupVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

		return cmd;
	}
}
