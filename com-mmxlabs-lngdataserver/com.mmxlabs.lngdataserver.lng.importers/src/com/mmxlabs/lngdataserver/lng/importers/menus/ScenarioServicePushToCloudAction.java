/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataResultRecord;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataService;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataServiceWrapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences.CloudOptimiserPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.Activator;
import com.mmxlabs.lngdataserver.lng.importers.menus.PublishBasecaseException.Type;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsBuildHelper;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolutionHelper;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper.NameProvider;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessSandboxOptions;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
import com.mmxlabs.rcp.common.appversion.VersionHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

public class ScenarioServicePushToCloudAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServicePushToCloudAction.class);

	private static final String MSG_ERROR_PUSHING = "Error sending the scenario";
	private static final String MSG_FAILED_PUSHING = "Failed to send the scenario";
	private static final String MSG_ERROR_SAVING = "Error saving temporary scenario";
	private static final String MSG_ERROR_UPLOADING = "Error uploading scenario";
	private static final String MSG_ERROR_EVALUATING = "Error evaluating scenario";

	private static final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
	private static final String CLOUD_OPTI_PATH = workspaceLocation.toOSString() + IPath.SEPARATOR + "cloud-opti";

	private ScenarioServicePushToCloudAction() {
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, final boolean optimisation, final List<Slot<?>> targetSlots) {
		boolean doPublish = false;
		String notes = null;

		final Shell activeShell = Display.getDefault().getActiveShell();

		if (!optimisation && (targetSlots == null || targetSlots.isEmpty())) {
			MessageDialog.openError(activeShell, "Error", "No target slots provided.");
			return;
		}

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DATAHUB_BASECASE_NOTES)) {
			final InputDialog dialog = new InputDialog(activeShell, "Confirm sending the scenario",
					String.format("Send scenario %s for online optimisation? Please enter notes.", scenarioInstance.getName()), "", null);
			doPublish = dialog.open() == InputDialog.OK;
			if (doPublish) {
				notes = dialog.getValue();
			}
		} else {
			doPublish = MessageDialog.openQuestion(activeShell, "Confirm sending the scenario", String.format("Send scenario %s for online optimisation?", scenarioInstance.getName()));
		}
		boolean localOpti = false;
		if (doPublish && optimisation && CloudOptimisationConstants.RUN_LOCAL_BENCHMARK) {
			localOpti = MessageDialog.openQuestion(activeShell, "Confirm sending the scenario", "Run locally for runtime comparison?");
		}

		if (doPublish) {
			final UserSettings userSettings;
			if (optimisation) {
				userSettings = getOptimisationPlanForOptimisation(scenarioInstance);
			} else {
				userSettings = getOptimisationPlanForInsertion(scenarioInstance, targetSlots);
			}
			if (userSettings != null) {
				uploadScenario(scenarioInstance, notes, userSettings, optimisation, targetSlots, null, localOpti);
			}
		}
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, @NonNull final OptionAnalysisModel sandboxModel) {
		boolean doPublish = false;
		String notes = null;

		final Shell activeShell = Display.getDefault().getActiveShell();

		final String sandboxModeStr;
		switch (sandboxModel.getMode()) {
		case SandboxModeConstants.MODE_OPTIONISE:
			sandboxModeStr = "optionisation";
			break;
		case SandboxModeConstants.MODE_OPTIMISE:
			sandboxModeStr = "optimisation";
			break;
		case SandboxModeConstants.MODE_DERIVE:
		default:
			sandboxModeStr = "define";
		}
		final String sendMessage = String.format("Send %s for cloud sandbox %s?", sandboxModel.getName(), sandboxModeStr);
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DATAHUB_BASECASE_NOTES)) {
			final InputDialog dialog = new InputDialog(activeShell, "Confirm sending the sandbox", String.format("%s Please enter notes.", sendMessage), "", null);
			doPublish = dialog.open() == InputDialog.OK;
			if (doPublish) {
				notes = dialog.getValue();
			}
		} else {
			doPublish = MessageDialog.openQuestion(activeShell, "Confirm sending the sandbox", sendMessage);
		}
		if (doPublish) {
			final UserSettings userSettings = getSandboxUserSettings(scenarioInstance, sandboxModel);
			if (userSettings != null) {
				uploadScenario(scenarioInstance, notes, userSettings, false, null, sandboxModel, false);
			}
		}
	}

	private static UserSettings getOptimisationPlanForOptimisation(final ScenarioInstance scenarioInstance) {
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
			final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
			OptimisationPlan plan = OptimisationHelper.getOptimiserSettings(scenarioModel, false, "Custom", true, true, null);
			if (plan != null) {
				return plan.getUserSettings();
			}
		} catch (final Exception e) {
			LOG.error("Error getting the optimisation plan: " + e.getMessage(), e);
		}
		return null;
	}

	private static UserSettings getSandboxUserSettings(final ScenarioInstance scenarioInstance, final OptionAnalysisModel model) {
		final Set<String> existingNames = new HashSet<>();
		scenarioInstance.getFragments().forEach(f -> existingNames.add(f.getName()));
		scenarioInstance.getElements().forEach(f -> existingNames.add(f.getName()));
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
			final LNGScenarioModel root = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
			final UserSettings previousSettings = AnalyticsBuildHelper.getSandboxPreviousSettings(model, root);
			final boolean promptUser = System.getProperty("lingo.suppress.dialogs") == null;
			switch (model.getMode()) {
			case SandboxModeConstants.MODE_OPTIONISE:
				return UserSettingsHelper.promptForInsertionUserSettings(root, false, promptUser, true, null, previousSettings);
			case SandboxModeConstants.MODE_OPTIMISE:
				return UserSettingsHelper.promptForUserSettings(root, false, promptUser, true, null, previousSettings);
			case SandboxModeConstants.MODE_DERIVE:
			default:
				return UserSettingsHelper.promptForSandboxUserSettings(root, false, promptUser, true, null, previousSettings);
			}
		}
	}

	private static UserSettings getOptimisationPlanForInsertion(final ScenarioInstance scenarioInstance, final List<Slot<?>> targetSlots) {
		UserSettings userSettings = null;
		final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		String taskName = AnalyticsSolutionHelper.generateInsertionName(true, targetSlots);
		{

			try (final ModelReference modelReference = originalModelRecord.aquireReference("InsertSlotContextMenuExtension:1")) {

				final EObject object = modelReference.getInstance();

				if (object instanceof LNGScenarioModel) {
					final LNGScenarioModel root = (LNGScenarioModel) object;
					final Set<String> existingNames = new HashSet<>();
					scenarioInstance.getFragments().forEach(f -> existingNames.add(f.getName()));
					scenarioInstance.getElements().forEach(f -> existingNames.add(f.getName()));

					final NameProvider nameProvider = new NameProvider(taskName, existingNames);
					userSettings = UserSettingsHelper.promptForInsertionUserSettings(root, false, true, false, nameProvider);
					taskName = nameProvider.getNameSuggestion();
				}
			}
		}
		if (userSettings != null) {
			// Reset settings not supplied to the user
			userSettings.setShippingOnly(false);
			userSettings.setBuildActionSets(false);
			userSettings.setCleanSlateOptimisation(false);
			userSettings.setSimilarityMode(SimilarityMode.OFF);
		}
		return userSettings;
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, final String notes, final UserSettings userSettings, //
			final boolean optimisation, final List<Slot<?>> targetSlots, final OptionAnalysisModel sandboxModel, boolean runLocal) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		try {
			dialog.run(true, false, m -> doUploadScenario(scenarioInstance, notes, userSettings, optimisation, targetSlots, sandboxModel, m, runLocal));
		} catch (final InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
			final Throwable cause = e.getCause();

			// TODO: Handle gateway errors.
			// E.g. 502 - bad gateway
			// 400 - bad request
			// Hostname/dns errors / timeots

			if (cause instanceof PublishBasecaseException publishBasecaseException) {
				switch (publishBasecaseException.getType()) {
				case FAILED_UNKNOWN_ERROR:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING,
							"Failed to send scenario with unknown error. " + publishBasecaseException.getCause().getMessage());
					break;
				case FAILED_NOT_PERMITTED:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_FAILED_PUSHING, "Insufficient permissions to send the scenario.");
					break;
				case FAILED_TO_MIGRATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to migrate the scenario to current data model version. Unable to send.");
					break;
				case FAILED_TO_EVALUATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to evaluate the scenario. Unable to send.");
					break;
				case FAILED_TO_SAVE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to save the scenario to a temporary file. Unable to send.");
					break;
				case FAILED_TO_UPLOAD_BASECASE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to upload the scenario. Unable to send.");
					break;
				default:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Unknown error occurred. Unable to send.");
					break;
				}
			}
		} catch (final InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@NonNull
	private static CloudManifestProblemType getProblemType(final boolean optimisation, @Nullable final List<Slot<?>> targetSlots, @Nullable final OptionAnalysisModel sandboxModel) {
		if (optimisation) {
			if (targetSlots != null || sandboxModel != null) {
				throw new PublishBasecaseException("Unable to derive cloud optimisation type", Type.FAILED_UNKNOWN_ERROR);
			}
			return CloudManifestProblemType.OPTIMISATION;
		} else if (targetSlots != null) {
			if (sandboxModel != null) {
				throw new PublishBasecaseException("Unable to derive cloud optimisation type", Type.FAILED_UNKNOWN_ERROR);
			}
			return CloudManifestProblemType.OPTIONISER;
		} else if (sandboxModel != null) {
			return CloudManifestProblemType.SANDBOX;
		} else {
			throw new PublishBasecaseException("Unable to derive cloud optimisation type", Type.FAILED_UNKNOWN_ERROR);
		}

	}

	private static void doUploadScenario(final ScenarioInstance scenarioInstance, final String notes, final UserSettings userSettings, //
			final boolean optimisation, @Nullable final List<Slot<?>> targetSlots, @Nullable final OptionAnalysisModel originalSandboxModel, final IProgressMonitor parentProgressMonitor,
			boolean runLocal) {

		parentProgressMonitor.beginTask("Send scenario", 1000);
		final CloudManifestProblemType problemType = getProblemType(optimisation, targetSlots, originalSandboxModel);
		final SubMonitor progressMonitor = SubMonitor.convert(parentProgressMonitor, 1000);
		try {
			progressMonitor.subTask("Prepare scenario");
			final CloudOptimisationDataResultRecord record = new CloudOptimisationDataResultRecord();
			record.setUuid(scenarioInstance.getUuid());
			record.setCreationDate(Instant.now());
			// This is the Data Hub user ID. It should be different.
			record.setCreator(UsernameProvider.INSTANCE.getUserID());
			record.setOriginalName(scenarioInstance.getName());

			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

			IScenarioDataProvider scenarioDataProvider = null;
			LNGScenarioModel scenarioModel = null;
			File anonymisationMap = null;

			List<Slot<?>> anonymisedTargetSlots = null;
			List<VesselEvent> anonymisedEvents = null;
			OptionAnalysisModel sandboxModelCopy = null;

			try (IScenarioDataProvider o_scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
				final LNGScenarioModel o_scenarioModel = o_scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

				final EcoreUtil.Copier copier = new Copier();
				scenarioModel = (LNGScenarioModel) copier.copy(o_scenarioModel);
				if (problemType == CloudManifestProblemType.OPTIONISER) {
					anonymisedTargetSlots = new ArrayList<>(targetSlots.size());
					final Map<String, LoadSlot> newLoadSlots = ScenarioModelUtil.getCargoModel(scenarioModel).getLoadSlots().stream().collect(Collectors.toMap(LoadSlot::getName, Function.identity()));
					final Map<String, DischargeSlot> newDischargeSlots = ScenarioModelUtil.getCargoModel(scenarioModel).getDischargeSlots().stream()
							.collect(Collectors.toMap(DischargeSlot::getName, Function.identity()));
					for (final Slot<?> slot : targetSlots) {
						final Slot<?> replacementSlot;
						if (slot instanceof LoadSlot) {
							replacementSlot = newLoadSlots.get(slot.getName());
						} else {
							replacementSlot = newDischargeSlots.get(slot.getName());
						}
						anonymisedTargetSlots.add(replacementSlot);
					}
				}

				copier.copyReferences();
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);
				if (problemType != CloudManifestProblemType.SANDBOX) {
					// Strip optimisation result
					LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);
				} else {
					for (final OptionAnalysisModel optionAnalysisModel : analyticsModel.getOptionModels()) {
						if (optionAnalysisModel.getUuid().equals(originalSandboxModel.getUuid())) {
							sandboxModelCopy = optionAnalysisModel;
							break;
						}
					}
					assert sandboxModelCopy != null;
					// Strip everything but model of interest
					analyticsModel.getOptimisations().clear();
					analyticsModel.getBreakevenModels().clear();

					analyticsModel.setViabilityModel(null);
					analyticsModel.setMtmModel(null);

					// clear option models and re-add the model we want to keep
					analyticsModel.getOptionModels().clear();
					analyticsModel.getOptionModels().add(sandboxModelCopy);

					// clear results since sending is unnecessary
					sandboxModelCopy.setResults(null);
				}

				scenarioDataProvider = SimpleScenarioDataProvider.make(EcoreUtil.copy(modelRecord.getManifest()), scenarioModel);
				final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

				progressMonitor.subTask("Anonymise scenario");

				final String amfName = CLOUD_OPTI_PATH + IPath.SEPARATOR + scenarioInstance.getUuid() + ".amap";
				record.setAnonyMapFileName(scenarioInstance.getUuid() + ".amap");
				anonymisationMap = new File(amfName);

				final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(scenarioModel, editingDomain, new HashSet<>(), new ArrayList<>(), true, anonymisationMap);
				if (cmd != null && !cmd.isEmpty()) {
					editingDomain.getCommandStack().execute(cmd);
				}

				progressMonitor.subTask("Evaluate scenario");

				// Hack: Add on shipping only hint to avoid generating spot markets during eval.
				final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, scenarioInstance) //
						.withThreadCount(1) //
						.withUserSettings(userSettings) //
						.withHints(LNGTransformerHelper.HINT_SHIPPING_ONLY) //
						.buildDefaultRunner();

				try {
					scenarioDataProvider.setLastEvaluationFailed(true);
					runnerBuilder.evaluateInitialState();
					scenarioDataProvider.setLastEvaluationFailed(false);
				} catch (final Exception e) {
					deleteAnonyMap(anonymisationMap);
					throw new PublishBasecaseException(MSG_ERROR_EVALUATING, Type.FAILED_TO_EVALUATE);
				}
			} catch (final RuntimeException e) {
				deleteAnonyMap(anonymisationMap);
				if (e.getCause() instanceof ScenarioMigrationException) {
					final ScenarioMigrationException ee = (ScenarioMigrationException) e.getCause();
					throw new PublishBasecaseException(MSG_ERROR_EVALUATING, Type.FAILED_TO_MIGRATE, ee);
				}
				throw new PublishBasecaseException(MSG_ERROR_EVALUATING, Type.FAILED_UNKNOWN_ERROR, e);
			}

			// CreateFile
			final File tmpScenarioFile = new File("scenario.lingo");
			try {
				ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, tmpScenarioFile);
			} catch (final IOException e) {
				deleteAnonyMap(anonymisationMap);
				e.printStackTrace();
				throw new PublishBasecaseException(MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
			}

			final List<Pair<String, Object>> filesToZip = new ArrayList<>(4);
			filesToZip.add(Pair.of("scenario.lingo", tmpScenarioFile));
			filesToZip.add(Pair.of("jvm.options", createJVMOptions()));

			try {
				filesToZip.add(Pair.of("manifest.json", createManifest(tmpScenarioFile.getName(), problemType)));
			} catch (final PublishBasecaseException e) {
				throw e;
			}
			switch (problemType) {
			case OPTIMISATION:
				filesToZip.add(Pair.of("parameters.json", createOptimisationSettingsJson(userSettings)));
				break;
			case OPTIONISER:
				assert anonymisedTargetSlots != null;
				filesToZip.add(Pair.of("parameters.json", createOptioniserSettingsJson(userSettings, anonymisedTargetSlots, anonymisedEvents)));
				break;
			case SANDBOX:
				filesToZip.add(Pair.of("parameters.json", createSandboxSettingsJson(userSettings, sandboxModelCopy)));
				break;
			default:
				throw new PublishBasecaseException("Unknown cloud optimisation problem type", Type.FAILED_UNKNOWN_ERROR);
			}
			File zipToUpload = null;
			try {
				zipToUpload = ScenarioStorageUtil.getTempDirectory().createTempFile("archive_", ".zip");
			} catch (final IOException e) {
				deleteAnonyMap(anonymisationMap);
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
				response = CloudOptimisationDataServiceWrapper.uploadData(zipToUpload, "checksum", scenarioInstance.getName(), //
						WrappedProgressMonitor.wrapMonitor(uploadMonitor));
			} catch (final Exception e) {
				deleteAnonyMap(anonymisationMap);
				System.out.println(MSG_ERROR_UPLOADING);
				e.printStackTrace();
				throw new PublishBasecaseException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD_BASECASE, e);
			} finally {
				uploadMonitor.done();
			}

			if (response == null) {
				deleteAnonyMap(anonymisationMap);
				throw new RuntimeException("Error sending the scenario for online optimisation");
			}
			final ObjectMapper mapper = new ObjectMapper();
			try {
				final JsonNode actualObj = mapper.readTree(response);
				final String jobid = actualObj.get("jobid").textValue();
				System.out.println(jobid);
				if (jobid != null) {
					record.setJobid(jobid);
					final File temp = new File(CLOUD_OPTI_PATH + IPath.SEPARATOR + jobid + ".amap");
					anonymisationMap.renameTo(temp);
				} else {
					deleteAnonyMap(anonymisationMap);
					throw new PublishBasecaseException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD_BASECASE, new IllegalStateException());
				}
			} catch (final IOException e) {
				deleteAnonyMap(anonymisationMap);
				throw new PublishBasecaseException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD_BASECASE, new IllegalStateException("Unexpected server response: " + e.getMessage()));
			}
			CloudOptimisationDataServiceWrapper.updateRecords(record);

			if (CloudOptimisationConstants.RUN_LOCAL_BENCHMARK && runLocal) {
				try {
					runLocalOptimisation(scenarioInstance, userSettings, targetSlots, problemType, progressMonitor, record, scenarioDataProvider, anonymisationMap);
				} catch (Exception e) {
					// Ignore - internal debug use only
				}
			}
		} finally {
			progressMonitor.done();
		}
	}

	private static void runLocalOptimisation(final ScenarioInstance scenarioInstance, final EObject optiPlanOrUserSettings, final List<Slot<?>> targetSlots, final CloudManifestProblemType problemType,
			final SubMonitor progressMonitor, final CloudOptimisationDataResultRecord record, IScenarioDataProvider scenarioDataProvider, File anonymisationMap) {
		try {
			progressMonitor.setWorkRemaining(500);
			progressMonitor.setTaskName("Running benchmark local optimisation");
			progressMonitor.subTask(problemType.toString());
			final long a = System.currentTimeMillis();

			if (problemType == CloudManifestProblemType.OPTIMISATION) {
				final LNGOptimisationBuilder optiBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, scenarioInstance) //
						.withThreadCount(1) //
						.withOptimiseHint() //
				;
				if (optiPlanOrUserSettings instanceof final UserSettings us) {
					optiBuilder.withUserSettings(us);//
				} else if (optiPlanOrUserSettings instanceof final OptimisationPlan op) {
					optiBuilder.withOptimisationPlan(op);//

				}

				final LNGOptimisationRunnerBuilder runnerBuilder = optiBuilder.buildDefaultRunner();
				runnerBuilder.getScenarioRunner().runWithProgress(progressMonitor.split(500));
			} else if (problemType == CloudManifestProblemType.OPTIONISER) {
				// Not yet supported

//				final List<Slot<?>> atargetSlots = new LinkedList<>();
//				final List<VesselEvent> atargetEvents = new LinkedList<>();
//
//				final CargoModelFinder cargoFinder = new CargoModelFinder(ScenarioModelUtil.getCargoModel(scenarioDataProvider));
////					options.loadIds.forEach(id -> atargetSlots.add(cargoFinder.findLoadSlot(id)));
////					options.dischargeIds.forEach(id -> atargetSlots.add(cargoFinder.findDischargeSlot(id)));
////					options.eventsIds.forEach(id -> atargetEvents.add(cargoFinder.findVesselEvent(id)));
////
//				final LNGSchedulerInsertSlotJobRunner insertionRunner = new LNGSchedulerInsertSlotJobRunner(null, // ScenarioInstance
//						scenarioDataProvider, scenarioDataProvider.getEditingDomain(), (UserSettings) optiPlanOrUserSettings, //
//						targetSlots, targetEvents, //
//						null, // Optional extra data provider.
//						null, // Alternative initial solution provider
//						builder -> {
////						if (options.maxWorkerThreads > 0) {
////							builder.withThreadCount(options.maxWorkerThreads);
////						}
//						});
//
//				final IMultiStateResult results = insertionRunner.runInsertion(null, progressMonitor.split(500));

			}
			final long b = System.currentTimeMillis();
			CloudOptimisationDataService.INSTANCE.setLocalRuntime(record.getJobid(), b - a);

		} catch (final Exception e) {
			// Ignore/
		}
	}

	private static void deleteAnonyMap(final File anonymisationMap) {
		if (anonymisationMap != null) {
			anonymisationMap.delete();
		}
	}

	/**
	 * Archives the files in the list into the target file
	 * 
	 * @param targetFile
	 * @param files
	 */
	private static void archive(final File targetFile, final List<Pair<String, Object>> files) {
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile))) {
			for (final var p : files) {

				if (p.getSecond() instanceof File f) {
					doArchive(zos, p.getFirst(), f);
				} else if (p.getSecond() instanceof String str) {
					doArchive(zos, p.getFirst(), str);
				} else {
					throw new IllegalArgumentException();
				}
			}
		} catch (final Exception e) {
			LOG.error("Can't write the ZIP archive", e);
		}
	}

	private static void doArchive(final ZipOutputStream zos, String filename, final File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			zos.putNextEntry(new ZipEntry(filename));
			ByteStreams.copy(fis, zos);
		} catch (final Exception e) {
			LOG.error(String.format("Can't add %s into the archive", file.getAbsolutePath()), e);
		} finally {
			zos.closeEntry();
		}
	}

	private static void doArchive(final ZipOutputStream zos, String filename, String content) throws IOException {
		try {
			zos.putNextEntry(new ZipEntry(filename));

			try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
				ByteStreams.copy(bytesIn, zos);
			}
		} catch (final Exception e) {
			LOG.error(String.format("Can't add %s into the archive", filename), e);
		} finally {
			zos.closeEntry();
		}
	}

	private static String createOptimisationSettingsJson(final UserSettings us) {

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.addMixIn(UserSettingsImpl.class, UserSettingsMixin.class);
		objectMapper.addMixIn(UserSettings.class, UserSettingsMixin.class);
		try {
			return objectMapper.writeValueAsString(us);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String createOptioniserSettingsJson(final UserSettings us, final List<Slot<?>> targetSlots, final List<VesselEvent> targetEvents) {

		// TODO: Replace with HeadlessOptioniserOptions when merged with master
		OptioniserSettings settings = new OptioniserSettings();
		settings.userSettings = us;
		settings.loadIds = new ArrayList<>();
		settings.dischargeIds = new ArrayList<>();
		settings.eventsIds = new ArrayList<>();

		if (targetSlots != null) {
			for (final Slot<?> s : targetSlots) {
				if (s instanceof LoadSlot) {
					settings.loadIds.add(s.getName());
				} else if (s instanceof DischargeSlot) {
					settings.dischargeIds.add(s.getName());
				}
			}
		}
		if (targetEvents != null) {
			for (final VesselEvent vesselEvent : targetEvents) {
				settings.eventsIds.add(vesselEvent.getName());
			}
		}

		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(settings);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String createSandboxSettingsJson(final UserSettings us, final OptionAnalysisModel sandboxModel) {

		final HeadlessSandboxOptions description = new HeadlessSandboxOptions();
		description.sandboxUUID = sandboxModel.getUuid();
		description.userSettings = (UserSettingsImpl) us;
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.addMixIn(UserSettingsImpl.class, UserSettingsMixin.class);
		objectMapper.addMixIn(UserSettings.class, UserSettingsMixin.class);
		try {
			String json = objectMapper.writeValueAsString(description);
			return json;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String createManifest(final String scenarioName, @NonNull final CloudManifestProblemType problemType) {
		final ManifestDescription md = new ManifestDescription();
		md.scenario = scenarioName;
		md.type = problemType.getManifestString();
		md.parameters = "parameters.json";
		md.jvmConfig = "jvm.options";
		md.dev = false;
		String devVersion = Activator.getDefault().getPreferenceStore().getString(CloudOptimiserPreferenceConstants.P_DEV_VERSION);
		if (devVersion != null && !devVersion.isBlank()) {
			md.version = devVersion.trim();
			md.dev = true;
		}
		if (md.version == null) {
			md.version = VersionHelper.getInstance().getClientVersion();
		}
		if (md.version == null) {
			throw new PublishBasecaseException("Unable to determine LiNGO version.", Type.FAILED_UNKNOWN_ERROR, new IOException());
		}
		md.clientCode = VersionHelper.getInstance().getClientCode();
		if (md.clientCode == null) {
			throw new PublishBasecaseException("Unable to determine version code.", Type.FAILED_UNKNOWN_ERROR, new IOException());
		}

		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(md);
			return json;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String createJVMOptions() {
		return "-Xms40m\n-Xmx4g\n";
	}

}
