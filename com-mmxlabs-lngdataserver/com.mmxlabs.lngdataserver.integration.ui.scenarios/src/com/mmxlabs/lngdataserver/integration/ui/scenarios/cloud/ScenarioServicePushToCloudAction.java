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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.SecretKey;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationPushException.Type;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences.CloudOptimiserPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.Activator;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.nominations.NominationsFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.rcp.common.appversion.VersionHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.impl.CloudOptimisationSharedCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileLoader;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;

public class ScenarioServicePushToCloudAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScenarioServicePushToCloudAction.class);

	public static final String MSG_ERROR_PUSHING = "Error sending the scenario";
	public static final String MSG_FAILED_PUSHING = "Failed to send the scenario";
	public static final String MSG_ERROR_SAVING = "Error saving temporary scenario";
	public static final String MSG_ERROR_SAVING_ANOM_MAP = "Error saving temporary anonymisation map";
	public static final String MSG_ERROR_UPLOADING = "Error uploading scenario";
	public static final String MSG_ERROR_EVALUATING = "Error evaluating scenario";
	public static final String MSG_ERROR_SAVE_ENCRYPTION_KEY = "Error saving the scenario encryption key";
	public static final String MSG_ERROR_FAILED_GETTING_PUB_KEY = "Error getting the optimisation server's public key";
	public static final String MSG_ERROR_FAILED_STATUS_CHECK = "Error reaching the cloud optimisation gateway";

	public static final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
	public static final String CLOUD_OPTI_PATH = workspaceLocation.toOSString() + IPath.SEPARATOR + "cloud-opti";
	public static final File OPTI_SERVER_PUB_KEY = new File(CLOUD_OPTI_PATH + IPath.SEPARATOR + "pubkey.pem");

	public static final String MANIFEST_NAME = "manifest.json";
	public static final String MF_PARAMETERS_NAME = "parameters.json";
	public static final String MF_JVM_OPTS_NAME = "jvm.opts";
	public static final String MF_SCENARIO_NAME = "scenario.lingo";

	private ScenarioServicePushToCloudAction() {
	}

	public static File encryptScenarioWithCloudKey(final SubMonitor progressMonitor, IScenarioDataProvider copyScenarioDataProvider, File anonymisationMap, KeyData keyData) {
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

	public static void stripScenario(final String problemType, final LNGScenarioModel copyScenarioModel, final @Nullable String sandboxUUID) {

		// Nominations are not currently used in the optimisation
		if (copyScenarioModel.getNominationsModel() != null) {
			copyScenarioModel.setNominationsModel(NominationsFactory.eINSTANCE.createNominationsModel());
		}

		// Clear base schedule model
		copyScenarioModel.getScheduleModel().setSchedule(null);

		final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(copyScenarioModel);
		if (!Objects.equals(problemType, "sandbox")) {
			// Strip all existing optimisation results and sandboxes
			LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);
		} else {
			OptionAnalysisModel sandboxModelCopy = null;
			// Strip all existing optimisation results and sandboxes except for the one we
			// are using.
			for (final OptionAnalysisModel optionAnalysisModel : analyticsModel.getOptionModels()) {
				if (optionAnalysisModel.getUuid().equals(sandboxUUID)) {
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

	public static File anonymiseScenario(final String scenarioUUID, final SubMonitor progressMonitor, final LNGScenarioModel scenarioModel, final EditingDomain editingDomain, boolean stripComments)
			throws IOException {
		progressMonitor.subTask("Anonymising scenario");

		final File anonymisationMap = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), scenarioUUID, ".amap").toFile();

		final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(scenarioModel, editingDomain, new HashSet<>(), new ArrayList<>(), true, anonymisationMap, stripComments);
		if (cmd != null && !cmd.isEmpty()) {
			editingDomain.getCommandStack().execute(cmd);
		}
		return anonymisationMap;
	}

	public static void evaluateScenario(final UserSettings userSettings, final SubMonitor progressMonitor, final IScenarioDataProvider scenarioDataProvider) {
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

	public static void cleanup(final File anonyMap, final KeyData keyData, File tmpEncryptedScenarioFile, File zipToUpload) {
		deleteFile(anonyMap);
		if (keyData != null) {
			deleteFile(keyData.keyStore());
			deleteFile(keyData.encryptedSymmetricKey());
		}
		deleteFile(tmpEncryptedScenarioFile);
		deleteFile(zipToUpload);
	}

	public static void deleteFile(final File file) {
		if (file != null && file.exists()) {
			try {
				FileDeleter.delete(file, LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE));
			} catch (Exception e) {
				LOG.error("Error deleting file " + file.getAbsolutePath() + ". " + e.getMessage(), e);
			}
		}
	}

	/**
	 * Archives the files in the list into the target file
	 * 
	 * @param targetFile
	 * @param files
	 */
	public static void archive(final File targetFile, final List<Pair<String, Object>> files) {
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

	public static void doArchive(final ZipOutputStream zos, final String filename, final File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			zos.putNextEntry(new ZipEntry(filename));
			ByteStreams.copy(fis, zos);
		} catch (final Exception e) {
			throw new RuntimeException(String.format("Can't add %s into the archive", file.getAbsolutePath()), e);
		} finally {
			zos.closeEntry();
		}
	}

	public static void doArchive(final ZipOutputStream zos, final String filename, final String content) throws IOException {
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

	public static String createManifest(final String scenarioName, @NonNull final String problemType, final String keyUUID) {
		final ManifestDescription md = new ManifestDescription();
		md.scenario = scenarioName;
		md.type = problemType;
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

	public static record KeyData(byte[] keyUUID, KeyFileV2 keyfile, SecretKey tmpKey, File encryptedSymmetricKey, File keyStore) {
	}

	public static KeyData generateKeyPairs(IProgressMonitor progressMonitor, String scenarioUUID) {
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
