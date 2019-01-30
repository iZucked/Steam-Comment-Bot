package com.mmxlabs.lngdataserver.lng.importers.bunkerfuels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.general.model.bunkerfuels.BunkerFuelDefinition;
import com.mmxlabs.lngdataserver.integration.general.model.bunkerfuels.BunkerFuelsVersion;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class BunkerFuelsToScenarioImporter {

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final FleetModel fleetModel, final BunkerFuelsVersion version) {

		final CompoundCommand cmd = new CompoundCommand("Update bunker fuels");

		// Gather existing curves
		final Map<String, BaseFuel> map = new HashMap<>();
		final Set<BaseFuel> existing = new HashSet<>();

		fleetModel.getBaseFuels().forEach(c -> map.put(c.getName(), c));
		fleetModel.getBaseFuels().forEach(existing::add);

		final Set<BaseFuel> updated = new HashSet<>();

		// Step 1, add in the groups.
		for (final BunkerFuelDefinition def : version.getFuels()) {
			final String name = def.getName();

			BaseFuel existingFuel = map.get(name);
			if (existingFuel == null) {
				existingFuel = FleetFactory.eINSTANCE.createBaseFuel();
				existingFuel.setName(name);
				existingFuel.setEquivalenceFactor(def.getEquivalenceFactor());
				cmd.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__BASE_FUELS, existingFuel));
			} else {
				cmd.append(SetCommand.create(editingDomain, existingFuel, FleetPackage.Literals.BASE_FUEL__EQUIVALENCE_FACTOR, def.getEquivalenceFactor()));

			}
			updated.add(existingFuel);
		}

		existing.removeAll(updated);
		if (!existing.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, existing));
		}

		VersionRecord record = fleetModel.getBunkerFuelsVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

		return cmd;
	}
}
