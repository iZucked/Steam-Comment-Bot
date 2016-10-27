/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.google.common.collect.Lists;
import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
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
	
	@Override
	public Command provideAdditionalAfterCommand(EditingDomain editingDomain, MMXRootObject rootObject, Map<EObject, EObject> overrides, Set<EObject> editSet, Class<? extends Command> commandClass,
			CommandParameter parameter, Command input) {

		if (parameter.getFeature() == CargoPackage.Literals.VESSEL_AVAILABILITY__OPTIONAL) {
			int z = 0;
//			if (!(parameter.getEValue() instanceof CharterInMarket)) {
//				// Clear value
//				return SetCommand.create(editingDomain, parameter.getOwner(), CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE);
//			}
//			CargoPackage.Literals.Vessel_
			if ((parameter.getValue() instanceof Boolean && ((Boolean) parameter.getValue()) == false)) {
				// Clear value
				return new CompoundCommand(Lists.newArrayList(SetCommand.create(editingDomain, parameter.getOwner(), CargoPackage.Literals.VESSEL_AVAILABILITY__REPOSITIONING_FEE, SetCommand.UNSET_VALUE), SetCommand.create(editingDomain, parameter.getOwner(), CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS, SetCommand.UNSET_VALUE)));
			}

		}

		return null;
	}


}
