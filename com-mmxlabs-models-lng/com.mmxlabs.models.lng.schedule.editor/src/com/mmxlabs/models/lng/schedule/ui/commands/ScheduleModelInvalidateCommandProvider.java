/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.ui.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.common.commandservice.CancelledCommand;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.RunnerHelper;

/**
 * Command provider which detects the deletion of cargoes and kills the
 * corresponding entries in any schedule.
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
				|| deletedObject instanceof SpotMarket //
				|| deletedObject instanceof Port //
				|| deletedObject instanceof VesselCharter //
				|| deletedObject instanceof CanalBookingSlot) {
			setContext(Boolean.FALSE);
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider#objectDeleted
	 * (org.eclipse.emf.edit.domain.EditingDomain,
	 * com.mmxlabs.models.mmxcore.MMXRootObject, java.lang.Object)
	 */
	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {

		if (rootObject instanceof LNGScenarioModel scenarioModel) {
			final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

			if (getContext() == Boolean.FALSE) {
				if (System.getProperty("lingo.suppress.dialogs") == null) {
					{
						if (!analyticsModel.getOptimisations().isEmpty() //
								|| !analyticsModel.getBreakevenModels().isEmpty() //
								|| !analyticsModel.getOptionModels().isEmpty() //
								|| analyticsModel.getViabilityModel() != null
								|| analyticsModel.getMtmModel() != null) {

							boolean result = promptClearModels();
							if (!result) {
								return CancelledCommand.INSTANCE;
							}
							setContext(Boolean.TRUE);
						}
					}
				}
			}

			return createClearModelsCommand(domain, scenarioModel, analyticsModel);
		}
		return null;
	}

	// TODO: keep updates in-line with MtMScenarioEditorActionDelegate
	public static Command createClearModelsCommand(final EditingDomain domain, final LNGScenarioModel scenarioModel, final AnalyticsModel analyticsModel) {
		final List<EObject> delete = new LinkedList<>();

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
		if (scheduleModel.getSchedule() != null) {
			delete.add(scheduleModel.getSchedule());
		}
		if (analyticsModel.getViabilityModel() != null) {
			delete.add(analyticsModel.getViabilityModel());
		}
		if (analyticsModel.getMtmModel() != null) {
			delete.add(analyticsModel.getMtmModel());
		}
		if (!analyticsModel.getOptimisations().isEmpty()) {
			delete.addAll(analyticsModel.getOptimisations());
		}
		if (!analyticsModel.getBreakevenModels().isEmpty()) {
			delete.addAll(analyticsModel.getBreakevenModels());
		}
		// Clear sandbox results, but not the sandbox itself
		if (!analyticsModel.getOptionModels().isEmpty()) {
			analyticsModel.getOptionModels().forEach(m -> {
				AbstractSolutionSet r = m.getResults();
				if (r != null) {
					delete.add(r);
				}
			});
		}

		if (delete.isEmpty()) {
			return null;
		}
		return DeleteCommand.create(domain, delete);
	}

	private static boolean promptClearModels() {
		boolean result[] = new boolean[1];
		RunnerHelper.syncExec((display) -> {
			result[0] = MessageDialog.openConfirm(display.getActiveShell(), "Scenario edit",
					"This change will remove all results. Press OK to continue, otherwise press cancel and fork the scenario.");
		});
		return result[0];
	}
}
