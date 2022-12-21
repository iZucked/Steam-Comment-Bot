/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.Task;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;

public class CloudOptimisationDataService {

	public static final int CURRENT_MODEL_VERSION = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudOptimisationDataService.class);
	public static final CloudOptimisationDataService INSTANCE = new CloudOptimisationDataService();

	private CloudOptimisationDataServiceClient client;
	private CloudOptimisationDataUpdater updater;

	private CloudOptimisationDataService() {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CLOUD_OPTIMISATION)) {
			start();
		}
	}

	public String uploadData(final File dataFile, //
			final String checksum, //
			final String scenarioName, //
			final IProgressListener progressListener, //
			final File encryptedSymmetricKey) throws Exception {
		String response = null;
		try {
			updater.pause();
			response = client.upload(dataFile, checksum, scenarioName, progressListener, encryptedSymmetricKey);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			updater.resume();
		}
		try {
			updater.pause();
			updater.refresh();
		} finally {
			updater.resume();
		}
		return response;
	}

	public synchronized boolean addRecord(Task task, final CloudOptimisationDataResultRecord cRecord) {
		updater.pause();
		try {
			// Mark as available upstream
			cRecord.setStatus(ResultStatus.submitted());
			updater.addNewlySubmittedOptimisationRecord(cRecord);
			return true;
		} finally {
			updater.resume();
		}
	}

	public void start() {

		final Bundle bundle = FrameworkUtil.getBundle(CloudOptimisationDataResultRecord.class);
		final BundleContext bundleContext = bundle.getBundleContext();

		new ServiceTracker<IWorkspace, IWorkspace>(bundleContext, IWorkspace.class, null) {
			private boolean started = false;

			@Override
			public IWorkspace addingService(final ServiceReference<IWorkspace> reference) {
				final IWorkspace workspace = super.addingService(reference);

				if (!started) {
					started = true;
					final IPath workspaceLocation = workspace.getRoot().getLocation();
					final File workspaceLocationFile = workspaceLocation.toFile();

					final File dataFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "cloud-opti");
					if (!dataFolder.exists()) {
						dataFolder.mkdirs();
					}

					client = new CloudOptimisationDataServiceClient();

					WellKnownTriggers.WORKSPACE_DATA_ENCRYPTION_CHECK.delayUntilTriggered(() -> {

						// Initial model load
						new Thread(() -> {

							updater = new CloudOptimisationDataUpdater(dataFolder, client, CloudJobManager.INSTANCE);
							updater.start();
						}).start();
					});
				}
				this.close();
				return workspace;
			}
		}.open();
	}

	public void stop() {

		if (updater != null) {
			updater.stop();
		}
	}

	public void delete(final Collection<String> jobIdsToDelete) throws IOException, URISyntaxException {
		updater.deleteDownloaded(jobIdsToDelete);
		for (String jobid : jobIdsToDelete) {
			client.abort(jobid);
		}
	}

	public RSAPublicKey getOptimisationServerPublicKey(File pubkey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {
		return client.getOptimisationServerPublicKey(pubkey);
	}

	public String getInfo() throws IOException, URISyntaxException {
		return client.getInfo();
	}

	public static void encryptSymmetricKey(RSAPublicKey pubkey, SecretKey symkey, File encryptedKeyFile) throws IOException, GeneralSecurityException {
		var cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		try (FileOutputStream fos = new FileOutputStream(encryptedKeyFile)) {
			fos.write(cipher.doFinal(symkey.getEncoded()));
		}
	}

	public void delete(Task task) {
		updater.deleteDownloaded(task);
	}
}
