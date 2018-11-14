/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.ui.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * Command provider which detects the deletion of cargoes and kills the corresponding entries in any schedule.
 * 
 * @author hinton
 * 
 */
public class ScheduleModelInvalidateCommandProvider extends BaseModelCommandProvider<Object> {

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet, MMXRootObject rootObject) {
		Object context = getContext();
		if (context != null) {
			return false;
		}
		if (deletedObject instanceof VesselEvent //
				|| deletedObject instanceof Cargo //
				|| deletedObject instanceof Vessel //
				|| deletedObject instanceof Slot //
				|| deletedObject instanceof Port //
				|| deletedObject instanceof VesselAvailability) {
			setContext(Boolean.FALSE);
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
			final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

			if (getContext() == Boolean.FALSE) {
				if (System.getProperty("lingo.suppress.dialogs") == null) {
					{
						if (!analyticsModel.getOptimisations().isEmpty()) {

							boolean result[] = new boolean[1];
							RunnerHelper.syncExec((display) -> {
								result[0] = MessageDialog.openConfirm(display.getActiveShell(), "Scenario edit",
										"This change will remove all optimisation results. Press OK to continue, otherwise press cancel and fork the scenario.");
							});
							if (!result[0]) {
								return UnexecutableCommand.INSTANCE;
							}
							setContext(Boolean.TRUE);
						} else if (analyticsModel.getViabilityModel() != null) {
							boolean result[] = new boolean[1];
							RunnerHelper.syncExec((display) -> {
								result[0] = MessageDialog.openConfirm(display.getActiveShell(), "Scenario edit",
										"This change will remove all analytics results. Press OK to continue, otherwise press cancel and fork the scenario.");
							});
							if (!result[0]) {
								return UnexecutableCommand.INSTANCE;
							}
							setContext(Boolean.TRUE);
						}
					}
				}
			}

			final List<EObject> delete = new LinkedList<>();

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
			if (scheduleModel.getSchedule() != null) {
				delete.add(scheduleModel.getSchedule());
			}

			if (analyticsModel.getViabilityModel() != null) {
				delete.add(analyticsModel.getViabilityModel());
			}
			if (!analyticsModel.getOptimisations().isEmpty()) {
				delete.addAll(analyticsModel.getOptimisations());
			}

			if (delete.isEmpty()) {
				return null;
			}
			return DeleteCommand.create(domain, delete);
		}
		return null;
	}
}
