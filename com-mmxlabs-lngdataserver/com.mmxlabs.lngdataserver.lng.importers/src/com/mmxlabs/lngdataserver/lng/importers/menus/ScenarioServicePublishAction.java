/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.hub.services.permissions.UserPermissionsService;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BasecaseServiceLockedException;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.ReportsServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SupportedReportFormats;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportContent;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.ReportPublisherExtensionUtil;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.ReportPublisherRegistry;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.commands.BaseCasePrePublishCommandExtensionUtil;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.commands.IBaseCasePrePublishCommandExtension;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.lngdataserver.lng.importers.menus.PublishBasecaseException.Type;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.scenario.utils.ExportCSVBundleUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioServicePublishAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServicePublishAction.class);

	private static final String MSG_ERROR_PUBLISHING = "Error publishing";
	private static final String MSG_FAILED_PUBLISHING = "Failed to publish base case";

	private ScenarioServicePublishAction() {

	}

	public static void publishScenario(final ScenarioInstance scenarioInstance) {
		boolean doPublish = false;
		String notes = null;

		final Shell activeShell = Display.getDefault().getActiveShell();
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DATAHUB_BASECASE_NOTES)) {
			final InputDialog dialog = new InputDialog(activeShell, "Confirm base case publish", String.format("Publish scenario %s as base case? Please enter notes.", scenarioInstance.getName()), "",
					null);
			doPublish = dialog.open() == Window.OK;
			if (doPublish) {
				notes = dialog.getValue();
			}
		} else {
			doPublish = MessageDialog.openQuestion(activeShell, "Confirm base case publish", String.format("Publish scenario %s as base case?", scenarioInstance.getName()));
		}
		if (doPublish) {
			ScenarioServicePublishAction.publishScenario(scenarioInstance, notes);
		}
	}

	public static void publishScenario(final ScenarioInstance scenarioInstance, final String notes) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

		try {
			dialog.run(true, false, m -> publishScenario(scenarioInstance, notes, m));
		} catch (final InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
			final Throwable cause = e.getCause();
			if (cause instanceof PublishBasecaseException publishBasecaseException) {
				String message = e.getMessage();
				if (publishBasecaseException.getMessage() != null) {
					message = publishBasecaseException.getMessage();
				}
				switch (publishBasecaseException.getType()) {
				case FAILED_UNKNOWN_ERROR:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING,
							"Failed to publish base case with unknown error. " + publishBasecaseException.getCause().getMessage());
					break;
				case FAILED_NOT_PERMITTED:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_FAILED_PUBLISHING, "Insufficient permissions to publish base case.");
					break;
				case FAILED_TO_MIGRATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING,
							"Failed to migrate the scenario to current data model version. Unable to publish as base case.");
					break;
				case FAILED_TO_EVALUATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Failed to evaluate the scenario. Unable to publish as base case.");
					break;
				case FAILED_TO_SAVE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Failed to save the base case scenario to a temporary file. Unable to publish as base case.");
					break;

				case FAILED_SERVICE_LOCKED:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_FAILED_PUBLISHING, "Base case locked by locked by another user.");
					break;
				case FAILED_TO_UPLOAD_BASECASE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Failed to upload the base case scenario. Unable to publish as base case.");
					break;
				case FAILED_TO_GENERATE_REPORT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, message + " Unable to publish as base case.");
					break;
				case FAILED_TO_UPLOAD_BACKING_DATA:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Error uploading refernce data. Unable to publish as base case.");
					break;
				case FAILED_TO_UPLOAD_REPORT:

					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, message + " Unable to publish as base case.");
					break;
				case FAILED_TO_MAKE_CURRENT:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Base case uploaded but was unable to mark as current.");
					break;
				case FAILED_TO_UPLOAD_BASECASE_CSV:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Base case uploaded but failed to upload backup archive.");
					break;
				case FAILED_TO_EVALUATE_VALIDATION_ERROR:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Base case validation failed.");
					break;
				default:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUBLISHING, "Unknown error occurred. Unable to publish as base case.");
					break;
				}
			}
		} catch (final InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	public static void publishScenario(final ScenarioInstance scenarioInstance, final String notes, final IProgressMonitor parentProgressMonitor) {

		// Check user permission
		if (UserPermissionsService.INSTANCE.hubSupportsPermissions()) {
			if (!UserPermissionsService.INSTANCE.isPermitted("basecase", "publish")) {
				throw new PublishBasecaseException("Error publishing scenario.", Type.FAILED_NOT_PERMITTED, new RuntimeException());
			}
		}

		{ // Early locked base case service check
			Boolean serviceLocked;
			try {
				serviceLocked = BaseCaseServiceClient.INSTANCE.isServiceLocked() && !BaseCaseServiceClient.INSTANCE.isServiceLockedByMe();
			} catch (final Exception ee) {
				throw new PublishBasecaseException("Error publishing scenario.", Type.FAILED_UNKNOWN_ERROR, ee);
			}
			if (serviceLocked == Boolean.TRUE) {
				throw new PublishBasecaseException("Error publishing scenario.", Type.FAILED_SERVICE_LOCKED, new RuntimeException());
			}
		}

		boolean doUnlock = false;
		try {
			if (BaseCaseServiceClient.INSTANCE.needsLocking()) {
				if (BaseCaseServiceClient.INSTANCE.isServiceLockedByMe()) {
					// Pre-locked
				} else if (BaseCaseServiceClient.INSTANCE.lock()) {
					// Lock for publish an unlock
					doUnlock = true;
				} else {
					// Else locked by someone else
					throw new PublishBasecaseException("Error publishing scenario.", Type.FAILED_SERVICE_LOCKED, new RuntimeException());
				}
			}
		} catch (final IOException e1) {
			throw new PublishBasecaseException("Error publishing scenario.", Type.FAILED_SERVICE_LOCKED, new RuntimeException());
		}

		parentProgressMonitor.beginTask("Publish base case", 1000);
		final SubMonitor progressMonitor = SubMonitor.convert(parentProgressMonitor, 1000);
		try {
			progressMonitor.subTask("Prepare base case");
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

			IScenarioDataProvider scenarioDataProvider = null;
			LNGScenarioModel scenarioModel = null;

			try (final IScenarioDataProvider o_scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
				
				if (!OptimisationHelper.validateScenario(o_scenarioDataProvider, null, false, true, true, Collections.emptySet())) {
					throw new PublishBasecaseException("Error evaluating scenario. Please fix validation errors.", Type.FAILED_TO_EVALUATE_VALIDATION_ERROR);
				}
				final LNGScenarioModel o_scenarioModel = o_scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
				
				processPrePublishCommands(progressMonitor, o_scenarioDataProvider);
				
				final EcoreUtil.Copier copier = new Copier();
				scenarioModel = (LNGScenarioModel) copier.copy(o_scenarioModel);

				copier.copyReferences();
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);

				// Strip optimisation result
				LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);

				scenarioDataProvider = SimpleScenarioDataProvider.make(EcoreUtil.copy(modelRecord.getManifest()), scenarioModel);

				// Evaluate scenario
				final OptimisationPlan optimisationPlan = OptimisationHelper.getOptimiserSettings(o_scenarioModel, true, false, false, null);
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
					throw new PublishBasecaseException("Error evaluating scenario.", Type.FAILED_TO_EVALUATE);
				}
			} catch (final PublishBasecaseException e) {
				throw e;
			} catch (final RuntimeException e) {
				if (e.getCause() instanceof ScenarioMigrationException ee ) {
					throw new PublishBasecaseException("Error evaluating scenario.", Type.FAILED_TO_MIGRATE, ee);
				}
				throw new PublishBasecaseException("Error evaluating scenario.", Type.FAILED_UNKNOWN_ERROR, e);
			}

			// CreateFile
			File tmpScenarioFile = null;

			try {
				tmpScenarioFile = ScenarioStorageUtil.getTempDirectory().createTempFile("publishScenarioUtil_", "");
			} catch (final IOException e) {
				e.printStackTrace();
				throw new PublishBasecaseException("Error saving temporary scenario.", Type.FAILED_TO_SAVE, e);
			}

			try {
				ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, tmpScenarioFile);
			} catch (final IOException e) {
				e.printStackTrace();
				throw new PublishBasecaseException("Error saving temporary scenario.", Type.FAILED_TO_SAVE, e);
			}

			progressMonitor.worked(200);

			progressMonitor.subTask("Upload base case");

			final List<DataOptions> dataTypesToUpload = new LinkedList<>();
			dataTypesToUpload.add(DataOptions.PricingData);

			if (LicenseFeatures.isPermitted("features:hub-sync-distances")) {
				dataTypesToUpload.add(DataOptions.PortData);
			}
			if (LicenseFeatures.isPermitted("features:hub-sync-vessels")) {
				dataTypesToUpload.add(DataOptions.FleetDatabase);
			}

			try {
				DataPublishUtil.uploadReferenceData(dataTypesToUpload, modelRecord, scenarioModel);
			} catch (final IOException e) {
				System.out.println("Error uploading the basecase scenario");
				e.printStackTrace();
				throw new PublishBasecaseException("Error uploading scenario.", Type.FAILED_TO_UPLOAD_BACKING_DATA, e);
			} finally {
			}

			// UploadFile
			String response = null;
			final SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				final String pricingVersion = ScenarioModelUtil.getPricingModel(scenarioDataProvider).getMarketCurvesVersionRecord().getVersion();

				response = BaseCaseServiceClient.INSTANCE.uploadBaseCase(tmpScenarioFile, scenarioInstance.getName(), notes, //
						pricingVersion, //
						WrappedProgressMonitor.wrapMonitor(uploadMonitor));
			} catch (final BasecaseServiceLockedException e) {
				throw new PublishBasecaseException("Error uploading scenario.", Type.FAILED_SERVICE_LOCKED, e);
			} catch (final IOException e) {
				System.out.println("Error uploading the basecase scenario");
				e.printStackTrace();
				throw new PublishBasecaseException("Error uploading scenario.", Type.FAILED_TO_UPLOAD_BASECASE, e);
			} finally {
				uploadMonitor.done();
			}
			if (response == null) {
				throw new RuntimeException("Error publishing base case");
			}
			final ObjectMapper mapper = new ObjectMapper();
			String uuid = null;
			try {
				final JsonNode actualObj = mapper.readTree(response);
				uuid = actualObj.get("uuid").textValue();
			} catch (final IOException e) {
				System.out.println("Cannot read server response after basecase upload");
				e.printStackTrace();
			}

			final SupportedReportFormats supportedFormats = ReportPublisherRegistry.getInstance().getSupportedVersions();

			final Iterable<IReportPublisherExtension> publishers = ReportPublisherExtensionUtil.getReportPublishers();
			final ArrayList<IReportPublisherExtension> publishersList = Lists.newArrayList(publishers);

			progressMonitor.subTask("Publish base case reports");
			final SubMonitor publishersMonitor = progressMonitor.split(300);
			try {

				publishersMonitor.beginTask("Publish base case reports", publishersList.size());
				for (final IReportPublisherExtension reportPublisherExtension : publishersList) {

					try {
						final IReportContent reportContent = reportPublisherExtension.publishReport(supportedFormats, scenarioDataProvider, ScenarioModelUtil.getScheduleModel(scenarioDataProvider));

						// Call the correct upload report endpoint
						final ReportsServiceClient reportsServiceClient = new ReportsServiceClient();

						try {
							reportsServiceClient.uploadReportData(reportContent.getReportType(), reportContent.getVersion(), reportContent.getContent(), uuid, //
									reportPublisherExtension.getEndpointURL(), reportPublisherExtension.getFileExtension());
						} catch (final IOException e) {
							e.printStackTrace();
							throw new PublishBasecaseException("Error uploading report " + reportContent.getReportType(), Type.FAILED_TO_UPLOAD_REPORT, e);
						}
					} catch (final Exception e) {
						LOG.error(e.getMessage(), e);
						throw new PublishBasecaseException("Error generating report " + reportPublisherExtension.getReportType(), Type.FAILED_TO_GENERATE_REPORT, e);
					}
					publishersMonitor.worked(1);
				}
			} finally {
				publishersMonitor.done();
			}

			try {
				BaseCaseServiceClient.INSTANCE.setCurrentBaseCase(uuid);
			} catch (final IOException e) {
				System.out.println("Error while setting the new baseCase as current");
				e.printStackTrace();
				throw new PublishBasecaseException("Error marking base case current", Type.FAILED_TO_MAKE_CURRENT, e);
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
			if (doUnlock) {
				try {
					BaseCaseServiceClient.INSTANCE.unlock();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static void processPrePublishCommands(final SubMonitor progressMonitor, final IScenarioDataProvider o_scenarioDataProvider) {
		final Iterable<IBaseCasePrePublishCommandExtension> prePublishCommandExtensions = BaseCasePrePublishCommandExtensionUtil.getCommandExtensions();
		final ArrayList<IBaseCasePrePublishCommandExtension> prePublishCommandExtensionsList = Lists.newArrayList(prePublishCommandExtensions);

		final SubMonitor publishersMonitor = progressMonitor.split(300);
		String lastCommandDescription = "";
		try {
			publishersMonitor.beginTask("Execute pre-publish commands", prePublishCommandExtensionsList.size());
			final CompoundCommand prePublishCommand = new CompoundCommand();
			for (final IBaseCasePrePublishCommandExtension commandExtension : prePublishCommandExtensionsList) {
				lastCommandDescription = commandExtension.getCommandDescription();
				final Command command = commandExtension.createPrePublishCommand(o_scenarioDataProvider);
				if (command != null) {
					prePublishCommand.append(command);
				}
			}
			if (!prePublishCommand.isEmpty()) {
				RunnerHelper.syncExecDisplayOptional(()->{
					o_scenarioDataProvider.getCommandStack().execute(prePublishCommand);
				});
			}
		} catch (Exception e) {
			throw new PublishBasecaseException("Error executing pre publish command: " + lastCommandDescription, Type.FAILED_TO_EXECUTE_PRE_PUBLISH_COMMAND);
		} finally {
			publishersMonitor.done();
		}
	}

}
