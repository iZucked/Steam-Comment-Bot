/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.ui.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Command provider which detects the deletion of cargoes and kills the corresponding entries in any schedule.
 * 
 * @author hinton
 * 
 */
public class ScheduleModelInvalidateCommandProvider extends BaseModelCommandProvider<Object> {

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (getContext() != null)
			return false;
		if (deletedObject instanceof VesselEvent || deletedObject instanceof Cargo || deletedObject instanceof Vessel || deletedObject instanceof Slot || deletedObject instanceof Port
				|| deletedObject instanceof VesselClass) {
			setContext(Boolean.TRUE);
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider#objectDeleted (org.eclipse.emf.edit.domain.EditingDomain, com.mmxlabs.models.mmxcore.MMXRootObject, java.lang.Object)
	 */
	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
			if (scheduleModel == null) {
				return null;
			}

			final List<EObject> delete = new ArrayList<EObject>(2);

			if (scheduleModel.getSchedule() != null) {
				delete.add(scheduleModel.getSchedule());
			}

			if (delete.isEmpty()) {
				return null;
			}
			return DeleteCommand.create(domain, delete);
		}
		return null;
	}
}
