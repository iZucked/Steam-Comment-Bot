/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class VesselAvailabilityCommandProvider extends BaseModelCommandProvider<Object> {

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return (deletedObject instanceof Vessel);
	}

	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (rootObject instanceof LNGScenarioModel) {
			LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

			final CargoModel cargoModel = scenarioModel.getCargoModel();

			EList<VesselAvailability> availabilities = cargoModel.getVesselAvailabilities();
			for (VesselAvailability availability : availabilities) {
				if (availability.getVessel() == deleted) {
					return DeleteCommand.create(domain, availability);
				}
			}

		}

		return null;
	}

}
