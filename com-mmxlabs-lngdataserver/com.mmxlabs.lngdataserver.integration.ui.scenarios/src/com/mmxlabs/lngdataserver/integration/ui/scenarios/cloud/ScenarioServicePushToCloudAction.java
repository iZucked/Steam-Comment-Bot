/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.SecretKey;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationPushException.Type;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences.CloudOptimiserPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.Activator;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsBuildHelper;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolutionHelper;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.cargo.CargoModel;
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
import com.mmxlabs.models.lng.parameters.util.UserSettingsMixin;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserSettings;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxSettings;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.appversion.VersionHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.CloudOptimisationSharedCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileLoader;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;

public class ScenarioServicePushToCloudAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServicePushToCloudAction.class);

	private static final String MSG_ERROR_PUSHING = "Error sending the scenario";
	private static final String MSG_FAILED_PUSHING = "Failed to send the scenario";
	private static final String MSG_ERROR_SAVING = "Error saving temporary scenario";
	private static final String MSG_ERROR_SAVING_ANOM_MAP = "Error saving temporary anonymisation map";
	private static final String MSG_ERROR_UPLOADING = "Error uploading scenario";
	private static final String MSG_ERROR_EVALUATING = "Error evaluating scenario";
	private static final String MSG_ERROR_SAVE_ENCRYPTION_KEY = "Error saving the scenario encryption key";
	private static final String MSG_ERROR_FAILED_GETTING_PUB_KEY = "Error getting the optimisation server's public key";
	private static final String MSG_ERROR_FAILED_STATUS_CHECK = "Error reaching the cloud optimisation gateway";

	private static final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
	private static final String CLOUD_OPTI_PATH = workspaceLocation.toOSString() + IPath.SEPARATOR + "cloud-opti";
	private static final File OPTI_SERVER_PUB_KEY = new File(CLOUD_OPTI_PATH + IPath.SEPARATOR + "pubkey.pem");

	private static final String MANIFEST_NAME = "manifest.json";
	private static final String MF_PARAMETERS_NAME = "parameters.json";
	private static final String MF_JVM_OPTS_NAME = "jvm.opts";
	private static final String MF_SCENARIO_NAME = "scenario.lingo";

	private ScenarioServicePushToCloudAction() {
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, final boolean optimisation, final List<Slot<?>> targetSlots) {

		final Shell activeShell = Display.getDefault().getActiveShell();

		if (!optimisation && (targetSlots == null || targetSlots.isEmpty())) {
			MessageDialog.openError(activeShell, "Error", "No target slots provided.");
			return;
		}

		if (!MessageDialog.openQuestion(activeShell, "Confirm sending the scenario", String.format("Send scenario %s for online optimisation?", scenarioInstance.getName()))) {
			return;
		}

		boolean localOpti = false;
		if (CloudOptimisationConstants.RUN_LOCAL_BENCHMARK) {
			localOpti = MessageDialog.openQuestion(activeShell, "Confirm sending the scenario", "Run locally for runtime comparison?");
		}

		String resultName = null;
		final UserSettings userSettings;
		if (optimisation) {
			final var p = getOptimisationPlanForOptimisation(scenarioInstance);
			if (p == null) {
				return;
			}
			userSettings = p.getFirst();
			resultName = p.getSecond();
		} else {
			userSettings = getOptimisationPlanForInsertion(scenarioInstance, targetSlots);
		}
		if (userSettings != null) {
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			uploadScenario(modelRecord, resultName, userSettings, optimisation, targetSlots, null, localOpti);
		}
	}

	public static void uploadScenario(final ScenarioInstance scenarioInstance, @NonNull final OptionAnalysisModel sandboxModel) {
		boolean doPublish = false;

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

		doPublish = MessageDialog.openQuestion(activeShell, "Confirm sending the sandbox", sendMessage);

		if (doPublish) {
			final UserSettings userSettings = getSandboxUserSettings(scenarioInstance, sandboxModel);
			if (userSettings != null) {
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
				uploadScenario(modelRecord, sandboxModel.getName(), userSettings, false, null, sandboxModel, false);
			}
		}
	}

	private static @Nullable Pair<UserSettings, String> getOptimisationPlanForOptimisation(final ScenarioInstance scenarioInstance) {
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
			final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			final Set<String> existingNames = new HashSet<>();
			scenarioInstance.getFragments().forEach(f -> existingNames.add(f.getName()));
			scenarioInstance.getElements().forEach(f -> existingNames.add(f.getName()));

			final NameProvider p = new NameProvider("Optimisation", existingNames);
			final OptimisationPlan plan = OptimisationHelper.getOptimiserSettings(scenarioModel, false, "Custom", true, true, p);
			if (plan != null) {
				return Pair.of(plan.getUserSettings(), plan.getResultName());
			}
		} catch (final Exception e) {
			throw new RuntimeException("Error getting the optimisation plan: " + e.getMessage(), e);
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
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		String taskName = AnalyticsSolutionHelper.generateInsertionName(true, targetSlots);
		{

			try (final ModelReference modelReference = modelRecord.aquireReference("InsertSlotContextMenuExtension:1")) {

				final EObject object = modelReference.getInstance();

				if (object instanceof final LNGScenarioModel root) {
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
			userSettings.setCleanSlateOptimisation(false);
			userSettings.setSimilarityMode(SimilarityMode.OFF);
		}
		return userSettings;
	}

	public static void uploadScenario(final ScenarioModelRecord originalModelRecord, @Nullable final String resultName, final UserSettings userSettings, //
			final boolean optimisation, final List<Slot<?>> targetSlots, final OptionAnalysisModel sandboxModel, final boolean runLocal) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		try {
			dialog.run(true, false, m -> doUploadScenario(originalModelRecord, resultName, userSettings, optimisation, targetSlots, sandboxModel, m, runLocal));
		} catch (final InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
			final Throwable cause = e.getCause();

			// TODO: Handle gateway errors.
			// E.g. 502 - bad gateway, 500, 504 (timeout)
			// 400 - bad request
			// Hostname/dns errors / timeots

			if (cause instanceof final CloudOptimisationPushException copException) {
				switch (copException.getType()) {
				case FAILED_UNKNOWN_ERROR:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to send scenario with unknown error. " + copException.getCause().getMessage());
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
				case FAILED_TO_UPLOAD:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Failed to upload the scenario. Unable to send.");
					break;
				case EVALUATION_FAILED:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Fix the validation errors and send again.");
					break;
				case FAILED_UNSUPPORTED_VERSION:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, cause.getMessage() + " Check the version in the Cloud Optimiser preference page.");
					break;
				case FAILED_STATUS_CHECK:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING,
							cause.getMessage() + ". Check your internet connectivity and verify the URL in the Cloud Optimiser preference page.");
					break;
				default:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Unknown error occurred. Unable to send.");
					break;
				}
			} else {
				MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING, "Unknown error occurred. Unable to send. " + e.getMessage());
			}
		} catch (final InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@NonNull
	private static CloudManifestProblemType getProblemType(final boolean optimisation, @Nullable final List<Slot<?>> targetSlots, @Nullable final OptionAnalysisModel sandboxModel) {
		if (optimisation) {
			if (targetSlots != null || sandboxModel != null) {
				throw new CloudOptimisationPushException("Unable to derive cloud optimisation type", Type.FAILED_UNKNOWN_ERROR);
			}
			return CloudManifestProblemType.OPTIMISATION;
		} else if (targetSlots != null) {
			if (sandboxModel != null) {
				throw new CloudOptimisationPushException("Unable to derive cloud optimisation type", Type.FAILED_UNKNOWN_ERROR);
			}
			return CloudManifestProblemType.OPTIONISER;
		} else if (sandboxModel != null) {
			return CloudManifestProblemType.SANDBOX;
		} else {
			throw new CloudOptimisationPushException("Unable to derive cloud optimisation type", Type.FAILED_UNKNOWN_ERROR);
		}

	}

	private static void doUploadScenario(final ScenarioModelRecord originalModelRecord, final String resultName, final UserSettings userSettings, //
			final boolean optimisation, @Nullable final List<Slot<?>> targetSlots, @Nullable final OptionAnalysisModel originalSandboxModel, final IProgressMonitor parentProgressMonitor,
			final boolean runLocal) {

		parentProgressMonitor.beginTask("Sending scenario", 1000);
		final CloudManifestProblemType problemType = getProblemType(optimisation, targetSlots, originalSandboxModel);
		final SubMonitor progressMonitor = SubMonitor.convert(parentProgressMonitor, 1000);

		// Temporary files to clean up on failure or success. Successful upload with move the required temp files into final location
		File anonymisationMap = null;
		KeyData keyData = null;
		File tmpEncryptedScenarioFile = null;
		File zipToUpload = null;

		
		try { // Try for the progress monitor and cleanup

			// gateway connectivity check
			try {
				final String info = CloudOptimisationDataService.INSTANCE.getInfo();
				LOG.info("gateway is reachable: " + info);
			} catch (final IOException e) {
				LOG.error(e.getLocalizedMessage());
				throw new CloudOptimisationPushException(MSG_ERROR_FAILED_STATUS_CHECK, Type.FAILED_STATUS_CHECK);
			}

			progressMonitor.subTask("Preparing scenario");
			final CloudOptimisationDataResultRecord cRecord = new CloudOptimisationDataResultRecord();

			populateBasicRecordFields(originalModelRecord, resultName, originalSandboxModel, problemType, cRecord);

			IScenarioDataProvider copyScenarioDataProvider = null;
			LNGScenarioModel copyScenarioModel = null;

			// Create a copy of the scenario so we can strip out excess data and later anonymise it.
			try (IScenarioDataProvider originalScenarioDataProvider = originalModelRecord.aquireScenarioDataProvider("ScenarioServicePushToCloudAction:doUploadScenario")) {
				final LNGScenarioModel originalScenarioModel = originalScenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

				// TODO: User pressing cancel is not an exception!
				validateScenario(originalModelRecord, originalSandboxModel, progressMonitor, originalScenarioDataProvider);

				copyScenarioModel = EcoreUtil.copy(originalScenarioModel);

				stripScenario(problemType, copyScenarioModel, originalSandboxModel);

				copyScenarioDataProvider = SimpleScenarioDataProvider.make(EcoreUtil.copy(originalModelRecord.getManifest()), copyScenarioModel);
			} catch (final RuntimeException e) {
				if (e.getCause() instanceof final ScenarioMigrationException ee) {
					throw new CloudOptimisationPushException(MSG_ERROR_EVALUATING, Type.FAILED_TO_MIGRATE, ee);
				} else if (e.getCause() instanceof final RuntimeException ee) {
					throw e;
				}
				throw new CloudOptimisationPushException(MSG_ERROR_EVALUATING, Type.FAILED_UNKNOWN_ERROR, e);
			}

			// Run the scenario anonymisation

			List<Slot<?>> anonymisedTargetSlots = null;
			final List<VesselEvent> anonymisedEvents = null;
			{
				final EditingDomain editingDomain = copyScenarioDataProvider.getEditingDomain();

				{
					// Optioniser needs the slot id's to run. Anonymisation changes the id's, so
					// here we need to get the mapping from the original instance to the copied
					// instance prior to anoymising.
					// Note: EcoreUtil.copier is a map between old and new. We could use that
					// instead of the ID mapping.
					if (problemType == CloudManifestProblemType.OPTIONISER) {
						assert targetSlots != null;

						anonymisedTargetSlots = new ArrayList<>(targetSlots.size());
						final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(copyScenarioModel);
						final Map<String, LoadSlot> newLoadSlots = cargoModel.getLoadSlots().stream() //
								.collect(Collectors.toMap(LoadSlot::getName, Function.identity()));

						final Map<String, DischargeSlot> newDischargeSlots = cargoModel.getDischargeSlots().stream() //
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
				}

				try {
					anonymisationMap = anonymiseScenario(cRecord.getUuid(), progressMonitor, copyScenarioModel, editingDomain);
					cRecord.setAnonyMapFileName(anonymisationMap.getName());
				} catch (final IOException e) {
					LOG.error("Failed to create temp anonymisation map file");
					throw new CloudOptimisationPushException(MSG_ERROR_SAVING_ANOM_MAP, Type.FAILED_TO_SAVE, e);
				}
			}

			// Try to evaluate the scenario - if this fails, then it would fail on the cloud server too.
			try {
				evaluateScenario(userSettings, progressMonitor, copyScenarioDataProvider);
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
				throw new CloudOptimisationPushException(MSG_ERROR_EVALUATING, Type.FAILED_TO_EVALUATE);
			}

			keyData = generateKeyPairs(progressMonitor, cRecord.getUuid());
			tmpEncryptedScenarioFile = encryptScenarioWithCloudKey(progressMonitor, copyScenarioDataProvider, anonymisationMap, keyData);

			final List<Pair<String, Object>> filesToZip = new ArrayList<>(4);
			// Add the manifest entry
			filesToZip.add(Pair.of(MANIFEST_NAME, createManifest(MF_SCENARIO_NAME, problemType, Base64.getEncoder().encodeToString(keyData.keyUUID))));
			// Add the scenario
			filesToZip.add(Pair.of(MF_SCENARIO_NAME, tmpEncryptedScenarioFile));
			// Add (unused) jvm options
			filesToZip.add(Pair.of(MF_JVM_OPTS_NAME, createJVMOptions()));

			// Add in the parameters
			switch (problemType) {
			case OPTIMISATION:
				filesToZip.add(Pair.of(MF_PARAMETERS_NAME, createOptimisationSettingsJson(userSettings)));
				break;
			case OPTIONISER:
				assert anonymisedTargetSlots != null;
				filesToZip.add(Pair.of(MF_PARAMETERS_NAME, createOptioniserSettingsJson(userSettings, anonymisedTargetSlots, anonymisedEvents)));
				break;
			case SANDBOX:
				filesToZip.add(Pair.of(MF_PARAMETERS_NAME, createSandboxSettingsJson(userSettings, originalSandboxModel.getUuid())));
				break;
			default:
				throw new CloudOptimisationPushException("Unknown cloud optimisation problem type", Type.FAILED_UNKNOWN_ERROR);
			}

			// Create the zip file bundle to send to the opti-server
			try {
				zipToUpload = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), "archive_", ".zip").toFile();
				archive(zipToUpload, filesToZip);
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
				throw new CloudOptimisationPushException(MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
			}

			progressMonitor.worked(200);
			progressMonitor.subTask("Sending optimisation");

			// UploadFile
			String response = null;
			final SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				response = CloudOptimisationDataService.INSTANCE.uploadData(zipToUpload, "checksum", originalModelRecord.getScenarioInstance().getName(), //
						WrappedProgressMonitor.wrapMonitor(uploadMonitor), keyData.encryptedSymmetricKey);
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
				throw new CloudOptimisationPushException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD, e);
			} finally {
				uploadMonitor.done();
			}

			if (response == null) {
				throw new RuntimeException("Error sending the scenario for online optimisation");
			}
			final ObjectMapper mapper = new ObjectMapper();
			try {
				final JsonNode actualObj = mapper.readTree(response);
				final String jobid = actualObj.get("jobid").textValue();
				System.out.println(jobid);
				if (jobid != null) {
					cRecord.setJobid(jobid);
					anonymisationMap.renameTo(new File(CLOUD_OPTI_PATH + IPath.SEPARATOR + jobid + ".amap"));
					keyData.keyStore.renameTo(new File(CLOUD_OPTI_PATH + IPath.SEPARATOR + jobid + ".key" + ".p12"));
				} else {
					throw new CloudOptimisationPushException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD, new IllegalStateException());
				}
			} catch (final IOException e) {
				throw new CloudOptimisationPushException(MSG_ERROR_UPLOADING, Type.FAILED_TO_SAVE_ENCRYPTION_KEY, new IllegalStateException("Unexpected error: " + e.getMessage()));
			}

			// Register the task
			CloudOptimisationDataService.INSTANCE.addRecord(cRecord);

			if (CloudOptimisationConstants.RUN_LOCAL_BENCHMARK && runLocal) {
				try {
					runLocalOptimisation(originalModelRecord.getScenarioInstance(), userSettings, anonymisedTargetSlots, problemType, progressMonitor, cRecord, copyScenarioDataProvider,
							anonymisationMap);
				} catch (final Exception e) {
					// Ignore - internal debug use only
				}
			}
		} finally {
			progressMonitor.done();
			cleanup(anonymisationMap, keyData, tmpEncryptedScenarioFile, zipToUpload);
		}
	}

	private static void populateBasicRecordFields(final ScenarioModelRecord originalModelRecord, final String resultName, final OptionAnalysisModel originalSandboxModel,
			final CloudManifestProblemType problemType, final CloudOptimisationDataResultRecord cRecord) {
		cRecord.setUuid(originalModelRecord.getScenarioInstance().getUuid());
		cRecord.setCreationDate(Instant.now());
		if (resultName != null) {
			cRecord.setResultName(resultName);
		}
		// This is the Data Hub user ID. It should be different.
		cRecord.setCreator(UsernameProvider.INSTANCE.getUserID());
		cRecord.setOriginalName(originalModelRecord.getScenarioInstance().getName());
		cRecord.setType(problemType.toString());

		if (originalSandboxModel != null) {
			switch (originalSandboxModel.getMode()) {
			case SandboxModeConstants.MODE_DERIVE -> cRecord.setSubType("Define");
			case SandboxModeConstants.MODE_OPTIMISE -> cRecord.setSubType("Optimise");
			case SandboxModeConstants.MODE_OPTIONISE -> cRecord.setSubType("Optionise");
			}
		}

		if (originalSandboxModel != null) {
			cRecord.setComponentUUID(originalSandboxModel.getUuid());
		}
		cRecord.setScenarioInstance(originalModelRecord.getScenarioInstance());
	}

	private static File encryptScenarioWithCloudKey(final SubMonitor progressMonitor, IScenarioDataProvider copyScenarioDataProvider, File anonymisationMap, KeyData keyData) {
		File tmpEncryptedScenarioFile = null;
		try {
			progressMonitor.subTask("Encrypting the scenario");
			tmpEncryptedScenarioFile = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), "archive_", ".lingo").toFile();
			final CloudOptimisationSharedCipherProvider scenarioCipherProvider = new CloudOptimisationSharedCipherProvider(keyData.keyfile);
			ScenarioStorageUtil.storeCopyToFile(copyScenarioDataProvider, tmpEncryptedScenarioFile, scenarioCipherProvider);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			// Clean up file if it exists
			if (tmpEncryptedScenarioFile != null && tmpEncryptedScenarioFile.exists()) {
				tmpEncryptedScenarioFile.delete();
			}
			throw new CloudOptimisationPushException(MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
		}
		return tmpEncryptedScenarioFile;
	}

	private static void stripScenario(final CloudManifestProblemType problemType, final LNGScenarioModel copyScenarioModel, final @Nullable OptionAnalysisModel originalSandboxModel) {

		final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(copyScenarioModel);
		if (problemType != CloudManifestProblemType.SANDBOX) {
			// Strip all existing optimisation results and sandboxes
			LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);
		} else {
			OptionAnalysisModel sandboxModelCopy = null;
			// Strip all existing optimisation results and sandboxes except for the one we
			// are using.
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
	}

	private static void validateScenario(final ScenarioModelRecord originalModelRecord, final OptionAnalysisModel originalSandboxModel, final SubMonitor progressMonitor,
			final IScenarioDataProvider originalScenarioDataProvider) {
		progressMonitor.subTask("Validating scenario");
		Set<String> extraCategories = new HashSet<>();
		if (originalSandboxModel != null) {
			extraCategories = Sets.newHashSet(".cargosandbox");
		}
		final boolean relaxedValidation = "Period Scenario".equals(originalModelRecord.getName());
		final boolean valid = validateScenario(originalScenarioDataProvider, originalSandboxModel, extraCategories);
		if (!valid) {
			throw new CloudOptimisationPushException(MSG_ERROR_EVALUATING, Type.EVALUATION_FAILED, new RuntimeException("Scenario failed validation. Please fix and resubmit."));
		}
	}

	private static File anonymiseScenario(final String scenarioUUID, final SubMonitor progressMonitor, final LNGScenarioModel scenarioModel, final EditingDomain editingDomain) throws IOException {
		progressMonitor.subTask("Anonymising scenario");

		final File anonymisationMap = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), scenarioUUID, ".amap").toFile();

		final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(scenarioModel, editingDomain, new HashSet<>(), new ArrayList<>(), true, anonymisationMap);
		if (cmd != null && !cmd.isEmpty()) {
			editingDomain.getCommandStack().execute(cmd);
		}
		return anonymisationMap;
	}

	private static void evaluateScenario(final UserSettings userSettings, final SubMonitor progressMonitor, final IScenarioDataProvider scenarioDataProvider) {
		progressMonitor.subTask("Evaluating scenario");

		// Hack: Add on shipping only hint to avoid generating spot markets during eval.
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.withUserSettings(userSettings) //
				.withHints(LNGTransformerHelper.HINT_SHIPPING_ONLY) //
				.buildDefaultRunner();

		scenarioDataProvider.setLastEvaluationFailed(true);
		runnerBuilder.evaluateInitialState();
		scenarioDataProvider.setLastEvaluationFailed(false);

	}

	public static boolean validateScenario(final IScenarioDataProvider scenarioDataProvider, @Nullable final EObject extraTarget, final Set<String> extraCategories) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().endsWith(".base")) {
						return true;
					} else if (cat.getId().endsWith(".optimisation")) {
						return true;
					} else if (cat.getId().endsWith(".adp")) {
						return true;
					}

					// Any extra validation category suffixes to include e.g. for sandbox
					for (final String catSuffix : extraCategories) {
						if (cat.getId().endsWith(catSuffix)) {
							return true;
						}
					}
				}

				return false;
			}
		});
		final MMXRootObject rootObject = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, false);
			return helper.runValidation(validator, extraContext, rootObject, extraTarget);
		});

		if (status == null) {
			return false;
		}

		if (!status.isOK()) {
			return displayValidationDialog(status);
		}

		return true;
	}

	/** Returns true if the scenario doesn't contain errors & the user pressed on OK. False otherwise */
	private static boolean displayValidationDialog(final IStatus status) {
		final boolean ok = RunnerHelper.syncExecFunc(display -> {
			final ValidationStatusDialog dialog = new ValidationStatusDialog(display.getActiveShell(), status, status.getSeverity() != IStatus.ERROR);

			// Wait for use to press a button before continuing.
			return dialog.open() == Window.OK;
		});
		if (!ok) {
			LOG.error("Cancelled validation dialog");
			return false;
		}

		return status.getSeverity() != IStatus.ERROR;
	}

	private static void runLocalOptimisation(final ScenarioInstance scenarioInstance, final EObject optiPlanOrUserSettings, final List<Slot<?>> targetSlots, final CloudManifestProblemType problemType,
			final SubMonitor progressMonitor, final CloudOptimisationDataResultRecord record, final IScenarioDataProvider scenarioDataProvider, final File anonymisationMap) {
		try {
			progressMonitor.setWorkRemaining(500);
			progressMonitor.setTaskName("Running benchmark local optimisation");
			progressMonitor.subTask(problemType.toString());
			final long a = System.currentTimeMillis();

			if (problemType == CloudManifestProblemType.OPTIMISATION) {
				final LNGOptimisationBuilder optiBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, scenarioInstance) //
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
				final List<VesselEvent> targetEvents = new LinkedList<>();

				final LNGSchedulerInsertSlotJobRunner insertionRunner = new LNGSchedulerInsertSlotJobRunner(null, // ScenarioInstance
						scenarioDataProvider, scenarioDataProvider.getEditingDomain(), (UserSettings) optiPlanOrUserSettings, //
						targetSlots, targetEvents, //
						null, // Optional extra data provider.
						null, // Alternative initial solution provider
						builder -> {
							// if (options.maxWorkerThreads > 0) {
							// builder.withThreadCount(options.maxWorkerThreads);
							// }
						});

				final IMultiStateResult results = insertionRunner.runInsertion(null, progressMonitor.split(400));
				insertionRunner.exportSolutions(results, progressMonitor.split(100));

			}
			final long b = System.currentTimeMillis();
			CloudOptimisationDataService.INSTANCE.setLocalRuntime(record.getJobid(), b - a);

		} catch (final Exception e) {
			// Ignore/
		}
	}

	private static void cleanup(final File anonyMap, final KeyData keyData, File tmpEncryptedScenarioFile, File zipToUpload) {
		deleteFile(anonyMap);
		deleteFile(keyData.keyStore());
		deleteFile(keyData.encryptedSymmetricKey());
		deleteFile(tmpEncryptedScenarioFile);
		deleteFile(zipToUpload);
	}

	private static void deleteFile(final File file) {
		if (file != null && file.exists()) {
			file.delete();
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

				if (p.getSecond() instanceof final File f) {
					doArchive(zos, p.getFirst(), f);
				} else if (p.getSecond() instanceof final String str) {
					doArchive(zos, p.getFirst(), str);
				} else {
					throw new IllegalArgumentException();
				}
			}
		} catch (final Exception e) {
			throw new RuntimeException("Can't write the ZIP archive", e);
		}
	}

	private static void doArchive(final ZipOutputStream zos, final String filename, final File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			zos.putNextEntry(new ZipEntry(filename));
			ByteStreams.copy(fis, zos);
		} catch (final Exception e) {
			throw new RuntimeException(String.format("Can't add %s into the archive", file.getAbsolutePath()), e);
		} finally {
			zos.closeEntry();
		}
	}

	private static void doArchive(final ZipOutputStream zos, final String filename, final String content) throws IOException {
		try {
			zos.putNextEntry(new ZipEntry(filename));

			try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
				ByteStreams.copy(bytesIn, zos);
			}
		} catch (final Exception e) {
			throw new RuntimeException(String.format("Can't add %s into the archive", filename), e);
		} finally {
			zos.closeEntry();
		}
	}

	private static ObjectMapper createUserSettingsMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.addMixIn(UserSettingsImpl.class, UserSettingsMixin.class);
		objectMapper.addMixIn(UserSettings.class, UserSettingsMixin.class);

		return objectMapper;
	}

	private static String createOptimisationSettingsJson(final UserSettings us) {

		final ObjectMapper objectMapper = createUserSettingsMapper();
		try {
			return objectMapper.writeValueAsString(us);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String createOptioniserSettingsJson(final UserSettings us, final List<Slot<?>> targetSlots, final List<VesselEvent> targetEvents) {

		final OptioniserSettings settings = new OptioniserSettings();
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

		final ObjectMapper objectMapper = createUserSettingsMapper();
		try {
			return objectMapper.writeValueAsString(settings);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String createSandboxSettingsJson(final UserSettings us, final String sandboxModelUUID) {

		final SandboxSettings description = new SandboxSettings();
		description.setSandboxUUID(sandboxModelUUID);
		description.setUserSettings(us);
		final ObjectMapper objectMapper = createUserSettingsMapper();
		try {
			final String json = objectMapper.writeValueAsString(description);
			return json;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String createManifest(final String scenarioName, @NonNull final CloudManifestProblemType problemType, final String keyUUID) {
		final ManifestDescription md = new ManifestDescription();
		md.scenario = scenarioName;
		md.type = problemType.getManifestString();
		md.parameters = MF_PARAMETERS_NAME;
		md.jvmConfig = MF_PARAMETERS_NAME;
		md.dev = false;
		md.keyUUID = keyUUID;
		final String devVersion = Activator.getDefault().getPreferenceStore().getString(CloudOptimiserPreferenceConstants.P_DEV_VERSION);
		if (devVersion != null && !devVersion.isBlank()) {
			md.version = devVersion.trim();
			md.dev = true;
		}
		if (md.version == null) {
			md.version = VersionHelper.getInstance().getClientVersion();
		}
		if (md.version == null) {
			throw new CloudOptimisationPushException("Unable to determine LiNGO version.", Type.FAILED_UNSUPPORTED_VERSION, new IOException());
		}
		md.clientCode = VersionHelper.getInstance().getClientCode();
		if (md.clientCode == null) {
			throw new CloudOptimisationPushException("Unable to determine version code.", Type.FAILED_WRONG_CLIENT_CODE, new IOException());
		}
		if (md.keyUUID == null) {
			throw new CloudOptimisationPushException("Unable to determine key uuid", Type.FAILED_MISSING_KEY_UUID, new IOException());
		}

		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final String json = objectMapper.writeValueAsString(md);
			return json;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String createJVMOptions() {
		return "-Xms40m\n-Xmx4g\n";
	}

	record KeyData(byte[] keyUUID, KeyFileV2 keyfile, SecretKey tmpKey, File encryptedSymmetricKey, File keyStore) {
	}

	private static KeyData generateKeyPairs(IProgressMonitor progressMonitor, String scenarioUUID) {
		// create shared symmetric key
		progressMonitor.subTask("Preparing scenario encryption");

		final byte[] keyUUID = new byte[KeyFileUtil.UUID_LENGTH];
		EcoreUtil.generateUUID(keyUUID);

		final SecretKey tmpKey = KeyFileV2.generateKey(256);
		final KeyFileV2 keyfile = new KeyFileV2(keyUUID, tmpKey);

		File encryptedSymmetricKey = null;
		File keyStore = null;
		try {
			final RSAPublicKey optiServerPubKey = CloudOptimisationDataService.INSTANCE.getOptimisationServerPublicKey(OPTI_SERVER_PUB_KEY);

			if (optiServerPubKey == null) {
				throw new CloudOptimisationPushException(MSG_ERROR_FAILED_GETTING_PUB_KEY, Type.FAILED_GETTING_PUB_KEY);
			}

			// save encrypted symmetric key to a temp file
			encryptedSymmetricKey = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), scenarioUUID + "_", ".key").toFile();

			// encrypt symmetric key with public key (to send to opti server)
			CloudOptimisationDataService.INSTANCE.encryptSymmetricKey(optiServerPubKey, tmpKey, encryptedSymmetricKey);

			keyStore = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), scenarioUUID + "_", ".key.p12").toFile();
			KeyFileLoader.initalise(keyStore);
			KeyFileLoader.addToStore(keyStore, keyUUID, tmpKey, KeyFileV2.KEYFILE_TYPE);

			return new KeyData(keyUUID, keyfile, tmpKey, encryptedSymmetricKey, keyStore);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
			LOG.error(e.getMessage(), e);
			if (encryptedSymmetricKey != null) {
				encryptedSymmetricKey.delete();
			}
			if (keyStore != null) {
				keyStore.delete();
			}
			throw new CloudOptimisationPushException(MSG_ERROR_SAVE_ENCRYPTION_KEY, Type.FAILED_TO_SAVE_ENCRYPTION_KEY, new IllegalStateException("Unexpected error: " + e.getMessage()));
		} catch (IOException | GeneralSecurityException e) {
			LOG.error(e.getMessage(), e);
			if (encryptedSymmetricKey != null) {
				encryptedSymmetricKey.delete();
			}
			if (keyStore != null) {
				keyStore.delete();
			}
			throw new CloudOptimisationPushException(MSG_ERROR_FAILED_GETTING_PUB_KEY, Type.FAILED_GETTING_PUB_KEY);
		}

	}
}
