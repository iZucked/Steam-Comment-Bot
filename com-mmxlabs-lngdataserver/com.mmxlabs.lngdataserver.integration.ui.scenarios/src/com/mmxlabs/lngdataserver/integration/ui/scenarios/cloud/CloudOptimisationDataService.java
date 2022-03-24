/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSandboxJobDescriptor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IProgressProvider;

public class CloudOptimisationDataService implements IProgressProvider {

	public static final int CURRENT_MODEL_VERSION = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudOptimisationDataService.class);
	public static final CloudOptimisationDataService INSTANCE = new CloudOptimisationDataService();

	private CloudOptimisationDataServiceClient client;
	private CloudOptimisationDataUpdater updater;

	private final ConcurrentLinkedQueue<IUpdateListener> listeners = new ConcurrentLinkedQueue<>();

	private CloudOptimisationDataService() {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CLOUD_OPTIMISATION)) {
			start();
		}
	}

	public Collection<CloudOptimisationDataResultRecord> getRecords() {
		return updater == null ? Collections.emptyList() : updater.getRecords();
	}

	private void update(final CloudOptimisationDataResultRecord cRecord) {
		fireListeners(cRecord);
	}

	private void fireListeners(@Nullable final CloudOptimisationDataResultRecord cRecord) {
		try {
			listeners.forEach(l -> l.updated(cRecord));

			final ScenarioInstance instanceRef = cRecord == null ? null : cRecord.getScenarioInstance();

			RunnerHelper.asyncExec(() -> progressListeners.forEach(p -> p.changed(instanceRef)));
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
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

	public synchronized boolean addRecord(final CloudOptimisationDataResultRecord cRecord) {
		updater.pause();
		try {
			// Mark as available upstream
			cRecord.setStatus(ResultStatus.submitted());
			cRecord.setRemote(true);

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

							updater = new CloudOptimisationDataUpdater(dataFolder, client, CloudOptimisationDataService.this::update);
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

	public interface IUpdateListener {
		void updated(@Nullable CloudOptimisationDataResultRecord cRecord);
	}

	public void addListener(final IUpdateListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeListener(final IUpdateListener l) {
		listeners.remove(l);
	}

	public void delete(final Collection<String> jobIdsToDelete) {
		updater.deleteDownloaded(jobIdsToDelete);
	}

	public void setLocalRuntime(final String jobId, final long runtime) {
		updater.setLocalRuntime(jobId, runtime);
	}

	public RSAPublicKey getOptimisationServerPublicKey(File pubkey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		return client.getOptimisationServerPublicKey(pubkey);
	}
	
	public String getInfo() throws IOException {
		return client.getInfo();
	}

	public void encryptSymmetricKey(RSAPublicKey pubkey, SecretKey symkey, File encryptedKeyFile) throws IOException, GeneralSecurityException {
		var cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		try (FileOutputStream fos = new FileOutputStream(encryptedKeyFile)) {
			fos.write(cipher.doFinal(symkey.getEncoded()));
		}
	}


	@Override
	public @Nullable Pair<Double, RunType> getProgress(final ScenarioInstance scenarioInstance, final ScenarioFragment fragment) {

		for (final var cRecord : getRecords()) {
			if (Objects.equals(cRecord.getUuid(), scenarioInstance.getUuid())) {
				if (cRecord.isActive()) {
					if (fragment == null) {
						return Pair.of(cRecord.getStatus().getProgress() / 100.0, RunType.Cloud);
					} else if (fragment.getFragment() instanceof final OptionAnalysisModel sandbox) {
						if (Objects.equals(sandbox.getUuid(), cRecord.getComponentUUID())) {
							return Pair.of(cRecord.getStatus().getProgress() / 100.0, RunType.Cloud);
						}
					}
				}
			}
		}
		// TODO; move into separate service and iterate over all services rather than
		// expecting one.
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		if (modelRecord != null) {
			return ServiceHelper.withService(IEclipseJobManager.class, jobManager -> {

				try (final ModelReference modelReference = modelRecord.aquireReferenceIfLoaded("CloudOptimisationDataService")) {
					if (modelReference != null) {
						final Object object = modelReference.getInstance();
						if (object instanceof MMXRootObject) {
							final String uuid = scenarioInstance.getUuid();

							final IJobDescriptor job = jobManager.findJobForResource(uuid);
							if (job != null) {
								boolean returnProgress = false;
								if (fragment == null) {
									returnProgress = true;
								} else if (fragment.getFragment() instanceof final OptionAnalysisModel sandbox) {
									if (job instanceof final LNGSandboxJobDescriptor desc) {
										if (sandbox == desc.getExtraValidationTarget()) {
											returnProgress = true;
										}
									}
								}
								if (returnProgress) {
									final IJobControl control = jobManager.getControlForJob(job);
									if (control != null) {
										if (control.getJobState() == EJobState.RUNNING) {
											final double p = control.getProgress();
											return Pair.of(p / 100.0, RunType.Local);
										}
									}
								}
							}
						}
					}
				}
				return null;
			});
		}
		return null;

	}

	private final ConcurrentLinkedQueue<IProgressChanged> progressListeners = new ConcurrentLinkedQueue<IProgressProvider.IProgressChanged>();

	@Override
	public void removeChangedListener(final IProgressChanged listener) {
		progressListeners.remove(listener);
	}

	@Override
	public void addChangedListener(final IProgressChanged listener) {
		progressListeners.add(listener);
	}

}
