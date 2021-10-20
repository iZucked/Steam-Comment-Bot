/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;

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
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.scenario.utils.ExportCSVBundleUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
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

	public static void uploadScenario(final ScenarioInstance scenarioInstance) {
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
			uploadScenario(scenarioInstance, notes);
		}
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, final String notes) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

		try {
			dialog.run(true, false, m -> uploadScenario(scenarioInstance, notes, m));
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

	private static void uploadScenario(final ScenarioInstance scenarioInstance, final String notes, final IProgressMonitor parentProgressMonitor) {

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
				final OptimisationPlan optimisationPlan = OptimisationHelper.getOptimiserSettings(o_scenarioModel, true, null, false, false, null);
				assert optimisationPlan != null;

				// Hack: Add on shipping only hint to avoid generating spot markets during eval.
				final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, scenarioInstance) //
						.withThreadCount(1) //
						.withOptimisationPlan(optimisationPlan) //
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
				tmpScenarioFile = ScenarioStorageUtil.getTempDirectory().createTempFile("publishScenarioUtil_", "");
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

			
			progressMonitor.worked(200);
			progressMonitor.subTask("Upload base case");

			// UploadFile
			String response = null;
			final SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				response = BaseCaseServiceClient.INSTANCE.uploadScenarioForCloudOpti(tmpScenarioFile, "checksum" ,scenarioInstance.getName(), notes, //
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

			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DATAHUB_BASECASE_ARCHIVE)) {
				try {
					final File tempFile = Files.createTempFile(uuid, ".zip").toFile();
					try {
						ExportCSVBundleUtil.exportScenarioToZip(scenarioDataProvider, tempFile);
						BaseCaseServiceClient.INSTANCE.uploadBaseCaseArchive(tempFile, uuid, null);
					} finally {
						// Delete temporary file
						final boolean secureDelete = LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE);
						FileDeleter.delete(tempFile, secureDelete);
					}

				} catch (final IOException ex) {
					System.out.println("Error while uploading base case archive");
					ex.printStackTrace();
					throw new PublishBasecaseException("Error while uploading base case archive", Type.FAILED_TO_UPLOAD_BASECASE_CSV, ex);
				}
			}

		} finally {
			progressMonitor.done();
		}
	}

}
