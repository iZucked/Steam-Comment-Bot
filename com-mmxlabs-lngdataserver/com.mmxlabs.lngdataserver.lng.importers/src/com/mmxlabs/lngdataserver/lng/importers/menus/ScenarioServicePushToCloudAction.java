/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataResultRecord;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataServiceWrapper;
import com.mmxlabs.lngdataserver.lng.importers.menus.PublishBasecaseException.Type;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolutionHelper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper.NameProvider;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
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
			final InputDialog dialog = new InputDialog(activeShell, "Confirm sending the scenario", String.format("Send scenario %s for online optimisation? Please enter notes.", scenarioInstance.getName()), "",
					null);
			doPublish = dialog.open() == InputDialog.OK;
			if (doPublish) {
				notes = dialog.getValue();
			}
		} else {
			doPublish = MessageDialog.openQuestion(activeShell, "Confirm sending the scenario", String.format("Send scenario %s for online optimisation?", scenarioInstance.getName()));
		}
		if (doPublish) {
			final EObject optPlanOrUsrSettings;
			if (optimisation) {
				optPlanOrUsrSettings = getOptimisationPlanForOptimisaiton(scenarioInstance);
			} else {
				optPlanOrUsrSettings = getOptimisationPlanForInsertion(scenarioInstance, targetSlots);
			}
			uploadScenario(scenarioInstance, notes, optPlanOrUsrSettings, optimisation, targetSlots);
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
	
	private static UserSettings getOptimisationPlanForInsertion(final ScenarioInstance scenarioInstance, final List<Slot<?>> targetSlots) {
		UserSettings userSettings = null;
		final ScenarioModelRecord originalModelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		String taskName = AnalyticsSolutionHelper.generateInsertionName(true, targetSlots);
		{

			try (final ModelReference modelReference = originalModelRecord.aquireReference("InsertSlotContextMenuExtension:1")) {

				final EObject object = modelReference.getInstance();

				if (object instanceof LNGScenarioModel) {
					final LNGScenarioModel root = (LNGScenarioModel) object;
					Set<String> existingNames = new HashSet<>();
					scenarioInstance.getFragments().forEach(f -> existingNames.add(f.getName()));
					scenarioInstance.getElements().forEach(f -> existingNames.add(f.getName()));

					NameProvider nameProvider = new NameProvider(taskName, existingNames);
					userSettings = OptimisationHelper.promptForInsertionUserSettings(root, false, true, false, nameProvider);
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

	public static void uploadScenario(final ScenarioInstance scenarioInstance, final String notes, final EObject optiPlanOrUserSettings,//
			final boolean optimisation, final List<Slot<?>> targetSlots) {
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		try {
			dialog.run(true, false, m -> doUploadScenario(scenarioInstance, notes, optiPlanOrUserSettings, optimisation, targetSlots, m));
		} catch (final InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
			final Throwable cause = e.getCause();
			if (cause instanceof PublishBasecaseException) {
				final PublishBasecaseException publishBasecaseException = (PublishBasecaseException) cause;
				switch (publishBasecaseException.getType()) {
				case FAILED_UNKNOWN_ERROR:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING,
							"Failed to send scenario with unknown error. " + publishBasecaseException.getCause().getMessage());
					break;
				case FAILED_NOT_PERMITTED:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_FAILED_PUSHING, "Insufficient permissions to send the scenario.");
					break;
				case FAILED_TO_MIGRATE:
					MessageDialog.openError(Display.getDefault().getActiveShell(), MSG_ERROR_PUSHING,
							"Failed to migrate the scenario to current data model version. Unable to send.");
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

	private static void doUploadScenario(final ScenarioInstance scenarioInstance, final String notes, final EObject optiPlanOrUserSettings, //
			final boolean optimisation, final List<Slot<?>> targetSlots, final IProgressMonitor parentProgressMonitor) {
		
		parentProgressMonitor.beginTask("Send scenario", 1000);
		final SubMonitor progressMonitor = SubMonitor.convert(parentProgressMonitor, 1000);
		try {
			progressMonitor.subTask("Prepare scenario");
			final CloudOptimisationDataResultRecord record = new CloudOptimisationDataResultRecord();
			record.setUuid(scenarioInstance.getUuid());
			record.setCreationDate(Instant.now());
			record.setCreator("Foo Bar");
			record.setOriginalName(scenarioInstance.getName());
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

			IScenarioDataProvider scenarioDataProvider = null;
			LNGScenarioModel scenarioModel = null;
			File anonymisationMap = null;

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
				
				final String amfName = CLOUD_OPTI_PATH + IPath.SEPARATOR + scenarioInstance.getUuid() + ".amap";
				record.setAnonyMapFileName(scenarioInstance.getUuid() + ".amap");
				anonymisationMap = new File(amfName);
				
				final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(scenarioModel, editingDomain, new HashSet(), new ArrayList(), true, anonymisationMap);
				if (cmd != null && !cmd.isEmpty()) {
					editingDomain.getCommandStack().execute(cmd);
				}

				progressMonitor.subTask("Evaluate scenario");

				// Evaluate scenario
				final OptimisationPlan optiPlan;
				if (optiPlanOrUserSettings instanceof OptimisationPlan) {
					optiPlan = (OptimisationPlan) optiPlanOrUserSettings;
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
			File tmpScenarioFile = new File("scenario.lingo");
			try {
				ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, tmpScenarioFile);
			} catch (final IOException e) {
				deleteAnonyMap(anonymisationMap);
				e.printStackTrace();
				throw new PublishBasecaseException(MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
			}
			
			final List<File> filesToZip = new ArrayList<File>(4);
			filesToZip.add(tmpScenarioFile);
			filesToZip.add(createJVMOptions());
			filesToZip.add(createManifest(tmpScenarioFile.getName(), true));
			if (optimisation) {
				filesToZip.add(createOptimisationSettingsJson(optiPlanOrUserSettings));
			} else {
				filesToZip.add(createOptioniserSettingsJson(optiPlanOrUserSettings, targetSlots));
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
				throw new PublishBasecaseException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD_BASECASE, 
						new IllegalStateException("Unexpected server response: "+ e.getMessage()));
			}
			try {
				CloudOptimisationDataServiceWrapper.updateRecords(record);
			} catch (final Exception e) {
				deleteAnonyMap(anonymisationMap);
				throw new PublishBasecaseException(MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD_BASECASE, e);
			}
		} finally {
			progressMonitor.done();
		}
	}

	private static void deleteAnonyMap(File anonymisationMap) {
		if (anonymisationMap != null) {
			anonymisationMap.delete();
		}
	}
	
	/**
	 * Archives the files in the list into the target file
	 * @param targetFile
	 * @param files
	 */
	private static void archive(final File targetFile, final List<File> files) {
		try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile))){
			for (final File file : files) {
				doArchive(zos, file);
			}
		} catch (Exception e) {
			LOG.error("Can't write the ZIP archive", e);
		}
	}

	private static void doArchive(ZipOutputStream zos, final File file) throws IOException {
		try(FileInputStream fis = new FileInputStream(file)){
			zos.putNextEntry(new ZipEntry(file.getName()));
			
			byte[] data;
			try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
				ByteStreams.copy(fis, bytesOut);
				data = bytesOut.toByteArray();
			}
			
			try (ByteArrayInputStream bytesIn = new ByteArrayInputStream(data)) {
				ByteStreams.copy(bytesIn, zos);
			}
		} catch (final Exception e) {
			LOG.error(String.format("Can't add %s into the archive", file.getAbsolutePath()), e);
		} finally {
			zos.closeEntry();
		}
	}
	
	private static File createOptimisationSettingsJson(final EObject plan) {
		if (plan instanceof OptimisationPlan) {
			final UserSettings us = ((OptimisationPlan) plan).getUserSettings();
			if (us != null) {
				OptimisationSettings settings = new OptimisationSettings();
				settings.periodStartDate = us.getPeriodStartDate();
				settings.periodEnd = us.getPeriodEnd();
				settings.shippingOnly = us.isShippingOnly();
				settings.generateCharterOuts = us.isGenerateCharterOuts();
				settings.withCharterLength = us.isWithCharterLength();
				settings.withSpotCargoMarkets = us.isWithSpotCargoMarkets();
				settings.similarityMode = us.getSimilarityMode().getLiteral();
				
				final ObjectMapper objectMapper = new ObjectMapper();
				try {
					final File file = new File("parameters.json");
					objectMapper.writeValue(file, settings);
					return file;
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private static File createOptioniserSettingsJson(final EObject plan, final List<Slot<?>> targetSlots) {
		if (plan instanceof UserSettings) {
			final UserSettings us = (UserSettings) plan;
			OptioniserSettings settings = new OptioniserSettings();
			settings.periodStartDate = us.getPeriodStartDate();
			settings.periodEnd = us.getPeriodEnd();
			settings.iterations = 10000;
			settings.loadIds = new ArrayList<>();
			settings.dischargeIds = new ArrayList<>();
			settings.eventsIds = new ArrayList<>();

			for (final Slot<?> s : targetSlots) {
				if (s instanceof LoadSlot) {
					settings.loadIds.add(s.getName());
				} else if (s instanceof DischargeSlot) {
					settings.dischargeIds.add(s.getName());
				}
			}

			settings.exportResults = true;

			final ObjectMapper objectMapper = new ObjectMapper();
			try {
				final File file = new File("parameters.json");
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
		md.type = optimisation ? "optimisation" : "optioniser";
		md.parameters = "parameters.json";
		md.jvmConfig = "jvm.options";
		md.version = "4.13.1";
		md.clientCode = "v";
		md.dev = true;
		
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final File file = new File("manifest.json");
			objectMapper.writeValue(file, md);
			return file;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static File createJVMOptions(){
		try{
			final File file = new File("jvm.options");
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
