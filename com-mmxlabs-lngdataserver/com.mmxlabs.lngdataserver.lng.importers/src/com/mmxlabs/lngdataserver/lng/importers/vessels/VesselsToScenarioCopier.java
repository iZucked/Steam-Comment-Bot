package com.mmxlabs.lngdataserver.lng.importers.vessels;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.vessels.IVesselsProvider;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;

public class VesselsToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsToScenarioCopier.class);

	public static Command getUpdateVesselsCommand(@NonNull final EditingDomain editingDomain, IVesselsProvider vesselsProvider, @NonNull final FleetModel fleetModel) {

		final CompoundCommand cc = new CompoundCommand("Update Vessel");
		Map<String, Vessel> vesselsMap = new HashMap<String, Vessel>();

		// Ignore vessel in current schedule without mmxId
		for (final Vessel vessel : fleetModel.getVessels()) {
			String mmxId = vessel.getMmxId();
			if (mmxId != null && !mmxId.isEmpty()) {
				vesselsMap.put(mmxId, vessel);
			}
		}

		// New vessel
		// i.e no mmxId or mmxId that can't be found in the current vessels list
		List<com.mmxlabs.lngdataservice.client.vessel.model.@NonNull Vessel> newVessels = vesselsProvider.getVessels().stream().filter((v) -> {
			if (v.getMmxId() == null || v.getMmxId().isEmpty()) {
				return true;
			}

			if (v.getMmxId() != null && !v.getMmxId().isEmpty()) {
				if (!vesselsMap.containsKey(v.getMmxId())) {
					return true;
				}
			}

			return false;
		}).collect(Collectors.toList());

		// Vessel to update
		Collection<com.mmxlabs.lngdataservice.client.vessel.model.@NonNull Vessel> toUpdateVessels = new LinkedHashSet<>(vesselsProvider.getVessels());
		toUpdateVessels.removeAll(newVessels);

		// Create new vessels commands
		for (com.mmxlabs.lngdataservice.client.vessel.model.@NonNull Vessel vessel : newVessels) {
			Vessel newVessel = FleetFactory.eINSTANCE.createVessel();
			newVessel.setLadenAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
			newVessel.setBallastAttributes(FleetFactory.eINSTANCE.createVesselStateAttributes());
			FleetFactory.eINSTANCE.createFuelConsumption();
			newVessel.getLadenAttributes().getFuelConsumption();
			cc.append(AddCommand.create(editingDomain, fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, newVessel));
			vesselsMap.put(vessel.getMmxId(), newVessel);
		}

		// Create all vessel update
		for (com.mmxlabs.lngdataservice.client.vessel.model.@NonNull Vessel vessel : vesselsProvider.getVessels()) {
			Vessel vesselToUpdate = vesselsMap.get(vessel.getMmxId());

			// Vessel Model
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE, vessel.getPilotLightRate()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MAX_SPEED, vessel.getMaxSpeed()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MIN_SPEED, vessel.getMinSpeed()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__IMO, vessel.getImo()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__FILL_CAPACITY, vessel.getFillCapacity()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY, vessel.isHasReliqCapacity()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__MMX_ID, vessel.getMmxId()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__SCNT, vessel.getScnt()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate, FleetPackage.Literals.VESSEL__CAPACITY, vessel.getCapacity()));

			// Ballast Model
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE,
					vessel.getBallastAttributes().getIdleBaseRate()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE,
					vessel.getBallastAttributes().getIdleNBORate()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, vessel.getBallastAttributes().getIdleNBORate()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED,
					vessel.getBallastAttributes().getServiceSped()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE,
					vessel.getBallastAttributes().getInPortBaseRate()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getBallastAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE,
					vessel.getBallastAttributes().getInPortNBORate()));
			cc.append(AddCommand.create(editingDomain, vesselToUpdate.getBallastAttributes().getFuelConsumption(), FleetPackage.Literals.FUEL_CONSUMPTION,
					vessel.getBallastAttributes().getFuelConsumption()));

			// Laden Model
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE,
					vessel.getLadenAttributes().getIdleBaseRate()));
			cc.append(
					SetCommand.create(editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, vessel.getLadenAttributes().getIdleNBORate()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, vessel.getLadenAttributes().getIdleNBORate()));
			cc.append(
					SetCommand.create(editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED, vessel.getLadenAttributes().getServiceSped()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE,
					vessel.getLadenAttributes().getInPortBaseRate()));
			cc.append(SetCommand.create(editingDomain, vesselToUpdate.getLadenAttributes(), FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE,
					vessel.getLadenAttributes().getInPortNBORate()));
			cc.append(AddCommand.create(editingDomain, vesselToUpdate.getLadenAttributes().getFuelConsumption(), FleetPackage.Literals.FUEL_CONSUMPTION,
					vessel.getLadenAttributes().getFuelConsumption()));

		}

		return cc;
	}
}
