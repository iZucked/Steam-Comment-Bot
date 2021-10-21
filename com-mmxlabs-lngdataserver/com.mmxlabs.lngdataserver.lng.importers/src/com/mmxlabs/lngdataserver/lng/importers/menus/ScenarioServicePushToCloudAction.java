/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.hub.services.permissions.UserPermissionsService;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BasecaseServiceLockedException;
import com.mmxlabs.lngdataserver.lng.importers.menus.PublishBasecaseException.Type;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.scenario.utils.ExportCSVBundleUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper.NameProvider;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioServicePushToCloudAction {


	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServicePushToCloudAction.class);

	private static final String MSG_ERROR_PUSHING = "Error pushing the scenario";
	private static final String MSG_FAILED_PUSHING = "Failed to push the scenario";
	private static final String MSG_ERROR_SAVING = "Error saving temporary scenario";
	private static final String MSG_ERROR_UPLOADING = "Error uploading scenario";
	private static final String MSG_ERROR_EVALUATING = "Error evaluating scenario";

	private ScenarioServicePushToCloudAction() {
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, final boolean optimisation) {
		boolean doPublish = false;
		String notes = null;
		
		// TODO:
		// request optimiser or optioniser settings

		final Shell activeShell = Display.getDefault().getActiveShell();
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DATAHUB_BASECASE_NOTES)) {
			final InputDialog dialog = new InputDialog(activeShell, "Confirm base case push for cloud optimisation", String.format("Push scenario %s for cloud optimisation? Please enter notes.", scenarioInstance.getName()), "",
					null);
			doPublish = dialog.open() == InputDialog.OK;
			if (doPublish) {
				notes = dialog.getValue();
			}
		} else {
			doPublish = MessageDialog.openQuestion(activeShell, "Confirm base case push for cloud optimisation", String.format("Publish scenario %s  for cloud optimisation?", scenarioInstance.getName()));
		}
		if (doPublish) {
			final OptimisationPlan optimisationPlan;
			if (optimisation) {
				optimisationPlan = getOptimisationPlanForOptimisaiton(scenarioInstance);
			} else {
				throw new IllegalArgumentException("Only optimisation is supported at the moment.");
			}
			uploadScenario(scenarioInstance, notes, optimisationPlan, optimisation);
		}
	}

	private static OptimisationPlan getOptimisationPlanForOptimisaiton(final ScenarioInstance scenarioInstance) {
		Set<String> existingNames = new HashSet<>();
		scenarioInstance.getFragments().forEach(f -> existingNames.add(f.getName()));
		scenarioInstance.getElements().forEach(f -> existingNames.add(f.getName()));
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
			final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
			return OptimisationHelper.getOptimiserSettings(scenarioModel, false, "Custom", true, true, new NameProvider("Optimisation", existingNames));
		} catch (final Exception e) {
			LOG.error("Error getting the optimisation plan: " + e.getMessage(), e);
		}
		return null;
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, final String notes, final OptimisationPlan optimisationPlan, final boolean optimisation) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		try {
			dialog.run(true, false, m -> uploadScenario(scenarioInstance, notes, optimisationPlan, m));
		} catch (final InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
			final Throwable cause = e.getCause();
			if (cause instanceof PublishBasecaseException) {
				final PublishBasecaseException publishBasecaseException = (PublishBasecaseException) cause;
				String message = e.getMessage();
				if (publishBasecaseException.getMessage() != null) {
					message = publishBasecaseException.getMessage();
				}
				switch (publishBasecaseException.getType()) {
				case FAILED_UNKNOWN_ERROR:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING,
							"Failed to push scenario with unknown error. " + publishBasecaseException.getCause().getMessage());
					break;
				case FAILED_NOT_PERMITTED:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_FAILED_PUSHING, "Insufficient permissions to push scenario.");
					break;
				case FAILED_TO_MIGRATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING,
							"Failed to migrate the scenario to current data model version. Unable to push scenario.");
					break;
				case FAILED_TO_EVALUATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to evaluate the scenario. Unable to push scenario.");
					break;
				case FAILED_TO_SAVE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to save the scenario to a temporary file. Unable to push scenario.");
					break;
				case FAILED_TO_UPLOAD_BASECASE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to upload the scenario. Unable to push scenario.");
					break;
				default:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Unknown error occurred. Unable to push scenario.");
					break;
				}
			}
		} catch (final InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	private static void uploadScenario(final ScenarioInstance scenarioInstance, final String notes, final OptimisationPlan optimisationPlan, final IProgressMonitor parentProgressMonitor) {

		// Check user permission
		if (UserPermissionsService.INSTANCE.hubSupportsPermissions()) {
			if (!UserPermissionsService.INSTANCE.isPermitted("basecase", "publish")) {
				throw new PublishBasecaseException(MSG_ERROR_PUSHING, Type.FAILED_NOT_PERMITTED, new RuntimeException());
			}
		}

		parentProgressMonitor.beginTask("Push scenario", 1000);
		final SubMonitor progressMonitor = SubMonitor.convert(parentProgressMonitor, 1000);
		try {
			progressMonitor.subTask("Prepare scenario");
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

			IScenarioDataProvider scenarioDataProvider = null;
			LNGScenarioModel scenarioModel = null;

			try (IScenarioDataProvider o_scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
				final LNGScenarioModel o_scenarioModel = o_scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

				final EcoreUtil.Copier copier = new Copier();
				scenarioModel = (LNGScenarioModel) copier.copy(o_scenarioModel);

				copier.copyReferences();
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

				// Strip optimisation result
				LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);

				scenarioDataProvider = SimpleScenarioDataProvider.make(EcoreUtil.copy(modelRecord.getManifest()), scenarioModel);
				final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
				
				progressMonitor.subTask("Anonymise scenario");
				final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(scenarioModel, editingDomain, new HashSet(), new ArrayList(), true);
				if (cmd != null && !cmd.isEmpty()) {
					editingDomain.getCommandStack().execute(cmd);
				}

				progressMonitor.subTask("Evaluate scenario");

				// Evaluate scenario
				final OptimisationPlan optiPlan;
				if (optimisationPlan != null) {
					optiPlan = optimisationPlan;
				} else {
					optiPlan = OptimisationHelper.getOptimiserSettings(o_scenarioModel, true, null, false, false, null);
				}
				assert optiPlan != null;

				// Hack: Add on shipping only hint to avoid generating spot markets during eval.
				final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, scenarioInstance) //
						.withThreadCount(1) //
						.withOptimisationPlan(optiPlan) //
						.withHints(LNGTransformerHelper.HINT_SHIPPING_ONLY) //
						.buildDefaultRunner();

				try {
					scenarioDataProvider.setLastEvaluationFailed(true);
					runnerBuilder.evaluateInitialState();
					scenarioDataProvider.setLastEvaluationFailed(false);
				} catch (final Exception e) {
					throw new PublishBasecaseException(MSG_ERROR_EVALUATING, Type.FAILED_TO_EVALUATE);
				} finally {
					runnerBuilder.dispose();
				}
			} catch (final RuntimeException e) {
				if (e.getCause() instanceof ScenarioMigrationException) {
					final ScenarioMigrationException ee = (ScenarioMigrationException) e.getCause();
					throw new PublishBasecaseException(MSG_ERROR_EVALUATING, Type.FAILED_TO_MIGRATE, ee);
				}
				throw new PublishBasecaseException(MSG_ERROR_EVALUATING, Type.FAILED_UNKNOWN_ERROR, e);
			}

			// CreateFile
			File tmpScenarioFile = null;

			try {
				tmpScenarioFile = ScenarioStorageUtil.getTempDirectory().createTempFile("cloudopti_", "");
			} catch (final IOException e) {
				e.printStackTrace();
				throw new PublishBasecaseException(MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
			}

			try {
				ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, tmpScenarioFile);
			} catch (final IOException e) {
				e.printStackTrace();
				throw new PublishBasecaseException(MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
			}
			
			final List<File> filesToZip = new ArrayList<File>(4);
			filesToZip.add(tmpScenarioFile);
			filesToZip.add(createJVMOptions());
			filesToZip.add(createManifest(tmpScenarioFile.getName(), true));
			filesToZip.add(createOptimisationSettingsJson(optimisationPlan));
			File zipToUpload = null;
			
			try {
				tmpScenarioFile = ScenarioStorageUtil.getTempDirectory().createTempFile("archive_", ".zip");
			} catch (final IOException e) {
				e.printStackTrace();
				throw new PublishBasecaseException(MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
			}
			archive(zipToUpload, filesToZip);
			
			progressMonitor.worked(200);
			progressMonitor.subTask("Upload base case");

			// UploadFile
			String response = null;
			final SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				response = BaseCaseServiceClient.INSTANCE.uploadScenarioForCloudOpti(zipToUpload, "checksum" ,scenarioInstance.getName(), notes, //
						WrappedProgressMonitor.wrapMonitor(uploadMonitor));
			} catch (final BasecaseServiceLockedException e) {
				throw new PublishBasecaseException(MSG_ERROR_UPLOADING, Type.FAILED_SERVICE_LOCKED, e);
			} catch (final IOException e) {
				System.out.println(MSG_ERROR_UPLOADING);
				e.printStackTrace();
				throw new PublishBasecaseException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD_BASECASE, e);
			} finally {
				uploadMonitor.done();
			}
			
			if (response == null) {
				throw new RuntimeException("Error pushing the scenario for cloud optimisation");
			}
			final ObjectMapper mapper = new ObjectMapper();
			String uuid = null;
			try {
				final JsonNode actualObj = mapper.readTree(response);
				uuid = actualObj.get("uuid").textValue();
			} catch (final IOException e) {
				System.out.println("Cannot read server response after scenario upload");
				e.printStackTrace();
			}
		} finally {
			progressMonitor.done();
		}
	}
	
	private static void archive(final File targetFile, final List<File> files) {
		try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile))){
			for (final File file : files) {
				try(FileInputStream fis = new FileInputStream(file)){
					zos.putNextEntry(new ZipEntry(file.getName()));
					
					byte[] buffer = new byte[1024];
					int fl;
					while ((fl = fis.read(buffer)) > 0) {
						zos.write(buffer);
					}
				} catch (final Exception e) {
					LOG.error(String.format("Can't add %s into the archive", file.getAbsolutePath()), e);
				} finally {
					zos.closeEntry();
				}
				
			}
		} catch (Exception e) {
			LOG.error("Can't write the archive", e);
		}
	}
	
	private static File createOptimisationSettingsJson(final OptimisationPlan plan) {
		final UserSettings us = plan.getUserSettings();
		if (us != null) {
			OpimisationSettings settings = new OpimisationSettings();
			settings.periodStartDate = us.getPeriodStartDate();
			settings.periodEnd = us.getPeriodEnd();
			settings.shippingOnly = us.isShippingOnly();
			settings.generateCharterOuts = us.isGenerateCharterOuts();
			settings.withCharterLength = us.isWithCharterLength();
			settings.withSpotCargoMarkets = us.isWithSpotCargoMarkets();
			settings.similarityMode = us.getSimilarityMode().getLiteral();
			
			final ObjectMapper objectMapper = new ObjectMapper();
			try {
				final File file = Files.createTempFile("parameters", ".json").toFile();
				objectMapper.writeValue(file, settings);
				return file;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static File createManifest(final String scenarioName, final boolean optimisation){
		final ManifestDescription md = new ManifestDescription();
		md.scenario = scenarioName;
		md.type = optimisation ? "optimisation" : "optionise";
		md.parameters = "parameters.json";
		md.jvmConfig = "jvm.options";
		md.version = "";
		md.clientCode = "";
		
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final File file = Files.createTempFile("manifest", ".json").toFile();
			objectMapper.writeValue(file, md);
			return file;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static File createJVMOptions(){
		try{
			final File file = Files.createTempFile("jvm", ".options").toFile();
			try(PrintWriter pw = new PrintWriter(new FileOutputStream(file))){
				pw.println("-Xms40m");
				pw.println("-Xmx4g");
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return file;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
