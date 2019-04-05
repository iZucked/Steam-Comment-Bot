/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.ui.ChangeDescriptionSource;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ChangeModelToScheduleSpecification;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.dnd.IChangeSource;
import com.mmxlabs.scenario.service.ui.dnd.IScenarioInstanceChangeHandler;

public class ChangeDescriptorChangeHandlerService implements IScenarioInstanceChangeHandler {

	@Override
	public boolean applyChange(final ScenarioInstance scenarioInstance, final IChangeSource changeSource) {

		if (changeSource instanceof ChangeDescriptionSource) {
			final ChangeDescriptionSource changeDescriptionSource = (ChangeDescriptionSource) changeSource;
			final ChangeDescription changeDescription = changeDescriptionSource.getChangeDescription();

			final ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
			try {
				dialog.run(true, false, (monitor) -> {
					monitor.beginTask("Apply change", 11);
					final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
					monitor.subTask("Open scenario");
					try (IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("ChangeDescriptorChangeHandlerService:1")) {
						monitor.worked(5);
						monitor.subTask("Applying change to scenario");

						final ChangeModelToScheduleSpecification transformer = new ChangeModelToScheduleSpecification(sdp, null);

						final EvaluateSolutionSetHelper helper = new EvaluateSolutionSetHelper(sdp);
						final OptimisationResult result = AnalyticsFactory.eINSTANCE.createOptimisationResult();
						result.setName("DND");
						if (changeDescriptionSource.getName() != null) {
							result.setName(changeDescriptionSource.getName());
						}

						final UserSettings userSettings = changeDescriptionSource.getUserSettings() != null ? EcoreUtil.copy(changeDescriptionSource.getUserSettings())
								: ScenarioUtils.createDefaultUserSettings();

						result.setUserSettings(userSettings);

						List<LoadSlot> extraLoads = new LinkedList<>();
						List<DischargeSlot> extraDischarges = new LinkedList<>();
						{
							final Pair<ScheduleSpecification, ExtraDataProvider> scheduleSpecification = transformer.buildScheduleSpecification(changeDescription);
							final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
							helper.processSolution(scheduleSpecification.getFirst(), scheduleModel);

							final SolutionOption option = AnalyticsFactory.eINSTANCE.createSolutionOption();
							option.setChangeDescription(EcoreUtil.copy(changeDescription));
							option.setScheduleSpecification(scheduleSpecification.getFirst());
							option.setScheduleModel(scheduleModel);

							extraLoads = scheduleSpecification.getSecond().extraLoads;
							extraDischarges = scheduleSpecification.getSecond().extraDischarges;
							result.getExtraSlots().addAll(extraLoads);
							result.getExtraSlots().addAll(extraDischarges);

							result.getOptions().add(option);
						}
						monitor.subTask("Generating new solution");
						helper.processExtraData(result);
						helper.generateResults(scenarioInstance, userSettings, sdp.getEditingDomain(), new SubProgressMonitor(monitor, 5));
						// monitor.worked(5);

						final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
						analyticsModel.getOptimisations().add(result);
						sdp.getModelReference().setDirty();
						monitor.worked(1);
					} finally {
						monitor.done();
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

		return false;
	}

}
