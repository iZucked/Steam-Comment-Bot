package com.mmxlabs.models.lng.fleet.editor.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
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
			final LNGPortfolioModel portfolioModel = ((LNGScenarioModel) rootObject).getPortfolioModel();
			if (portfolioModel == null) {
				return null;
			}
			
			final ScenarioFleetModel fleetModel = portfolioModel.getScenarioFleetModel();
			
			EList<VesselAvailability> availabilities = fleetModel.getVesselAvailabilities();
			for (VesselAvailability availability: availabilities) {
				if (availability.getVessel() == deleted) {
					return DeleteCommand.create(domain, availability);
				}
			}
			
		}
		
		return null;
	}
	
}
