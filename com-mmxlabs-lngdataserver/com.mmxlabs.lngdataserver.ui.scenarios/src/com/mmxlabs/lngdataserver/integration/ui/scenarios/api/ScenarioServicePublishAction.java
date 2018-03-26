/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.ReportPublisherExtensionUtil;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ClonedScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioServicePublishAction {

	public static void publishScenario(ScenarioInstance scenarioInstance) {
		BaseCaseServiceClient baseCaseServiceClient = new BaseCaseServiceClient();
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

		// Copy the scenario
		// TODO

		IScenarioDataProvider scenarioDataProvider = null;
		
		try (IScenarioDataProvider o_scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
			final LNGScenarioModel o_scenarioModel = o_scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			final EcoreUtil.Copier copier = new Copier();
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) copier.copy(o_scenarioModel);

			copier.copyReferences();
			AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

			// Strip optimisation result
			analyticsModel.getOptionModels().clear();
			analyticsModel.getOptimisations().clear();

			scenarioDataProvider =  SimpleScenarioDataProvider.make(EcoreUtil.copy(modelRecord.getManifest()), scenarioModel);	
		//}
		
		// Evaluate scenario
		//try (IScenarioDataProvider o_scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
			final ExecutorService executorService = Executors.newSingleThreadExecutor();
			
			try {
				final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

				//final LNGScenarioModel o_scenarioModel = o_scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
				OptimisationPlan optimisationPlan = OptimisationHelper.getOptimiserSettings(o_scenarioModel, true, null, false, false, null);
				assert optimisationPlan != null;
				
				// Hack: Add on shipping only hint to avoid generating spot markets during eval.
				final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, scenarioDataProvider, scenarioInstance, optimisationPlan, editingDomain, null, true,
						LNGTransformerHelper.HINT_SHIPPING_ONLY);
				scenarioDataProvider.setLastEvaluationFailed(true);
				runner.evaluateInitialState();
				scenarioDataProvider.setLastEvaluationFailed(false);
			} finally {
				executorService.shutdown();
			}
		}
		
		// CreateFile
		File tmpScenarioFile = null; 
		
		try {
			tmpScenarioFile = ScenarioStorageUtil.getTempDirectory().createTempFile("publishScenarioUtil_","");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, tmpScenarioFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// UploadFile
		try {
			baseCaseServiceClient.uploadBaseCase(tmpScenarioFile);
		} catch (IOException e) {
			System.out.println("Error uploading the basecase scenario");
			e.printStackTrace();
		}
		Iterable<IReportPublisherExtension> reports  = ReportPublisherExtensionUtil.getContextMenuExtensions();
		
		for (IReportPublisherExtension reportPublisherExtension :ReportPublisherExtensionUtil.getContextMenuExtensions()) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			reportPublisherExtension.publishReport(scenarioDataProvider, ScenarioModelUtil.getScheduleModel(scenarioDataProvider), outputStream);
			String reportType = reportPublisherExtension.getReportType();
			
			// Call the correct upload report endpoint;
			outputStream.toByteArray();
		}
	}
}
