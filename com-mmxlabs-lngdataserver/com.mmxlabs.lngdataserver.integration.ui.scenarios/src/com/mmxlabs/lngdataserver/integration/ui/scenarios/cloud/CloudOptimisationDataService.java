/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class CloudOptimisationDataService extends AbstractScenarioService {

	public static final int CURRENT_MODEL_VERSION = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudOptimisationDataService.class);
	public static final CloudOptimisationDataService INSTANCE = new CloudOptimisationDataService();

	private File dataFolder;
	private String serviceName;

	private CloudOptimisationDataServiceClient client;
	private CloudOptimisationDataUpdater updater;

	private ConcurrentLinkedQueue<IUpdateListener> listeners = new ConcurrentLinkedQueue<>();

	private CloudOptimisationDataService() {
		super("Online Tasks");
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CLOUD_OPTIMISATION)) {
			start();
		}
	}

	public Collection<CloudOptimisationDataResultRecord> getRecords() {
		return updater == null ? Collections.emptyList() : updater.getRecords();
	}

	private void update(final CloudOptimisationDataResultRecord cRecord) {
		fireListeners();
	}

	private void fireListeners() {
		try {
			listeners.forEach(IUpdateListener::updated);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	public @Nullable File getDataFile(final String uuid) {
		return new File(String.format("%s/%s.data", dataFolder.getAbsoluteFile(), uuid));
	}

	public String uploadData(final File dataFile, //
			final String checksum, //
			final String scenarioName, //
			final IProgressListener progressListener) throws Exception {
		String response = null;
		try {

			try {
				updater.pause();
				response = //
						client.upload(dataFile, checksum, scenarioName, progressListener);
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
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	public synchronized boolean addRecord(final CloudOptimisationDataResultRecord record) {
		updater.pause();
		try {
			// Mark as available upstream
			record.setStatus(ResultStatus.submitted());
			record.setRemote(true);

			updater.addNewlySubmittedOptimisationRecord(record);
			boolean result = true;
			return result;
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

					dataFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "cloud-opti");
					if (!dataFolder.exists()) {
						dataFolder.mkdirs();
					}

					client = new CloudOptimisationDataServiceClient();

					getServiceModel();
					setReady();
					WellKnownTriggers.WORKSPACE_DATA_ENCRYPTION_CHECK.delayUntilTriggered(() -> {

						// Initial model load
						new Thread(() -> {

							getServiceModel();
							setReady();
							updater = new CloudOptimisationDataUpdater(dataFolder, client, serviceModel, CloudOptimisationDataService.this::update);
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
		void updated();
	}

	public void addListener(IUpdateListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeListener(IUpdateListener l) {
		listeners.remove(l);
	}

	public void refresh() throws IOException {
		if (updater != null) {
			updater.pause();
			try {
				updater.refresh();
			} finally {
				updater.resume();
			}
		}
	}

	public void pause() {
		if (updater != null) {
			updater.pause();
		}
	}

	public void resume() {
		if (updater != null) {
			updater.resume();
		}
	}

	@Override
	public void delete(@NonNull Container container) {
		if (serviceModel.isOffline()) {
			return;
		}
		// Note: while this is recursive, we do assume a child first deletion set of
		// calls as defined in DeleteScenarioCommandHandler
		final List<String> jobIdsToDelete = new LinkedList<>();
		recursiveDelete(container, jobIdsToDelete);
		updater.deleteDownloaded(jobIdsToDelete);
	}

	public void delete(Collection<String> jobIdsToDelete) {
		updater.deleteDownloaded(jobIdsToDelete);
	}

	public void setLocalRuntime(String jobId, long runtime) {
		updater.setLocalRuntime(jobId, runtime);
	}

	private void recursiveDelete(final Container parent, final List<String> jobIds) {
		if (parent instanceof ScenarioInstance scenarioInstance) {
			jobIds.add(scenarioInstance.getExternalID());
		}
		for (final Container c : parent.getElements()) {
			recursiveDelete(c, jobIds);
		}
	}

	@Override
	public void moveInto(@NonNull List<Container> elements, @NonNull Container destination) {
		// do nothing
	}

	@Override
	public void makeFolder(@NonNull Container parent, @NonNull String name) {
		// do nothing
	}

	@Override
	public String getSerivceID() {
		return "cloud-workspace-" + serviceName;
	}

	@Override
	public ScenarioInstance copyInto(@NonNull Container parent, @NonNull ScenarioModelRecord tmpRecord, @NonNull String name, @NonNull IProgressMonitor progressMonitor) throws Exception {
		// do nothing
		return null;
	}

	@Override
	public ScenarioInstance copyInto(@NonNull Container parent, @NonNull IScenarioDataProvider scenarioDataProvider, @NonNull String name, @NonNull IProgressMonitor progressMonitor) throws Exception {
		// do nothing
		return null;
	}

	@Override
	public void fireEvent(final ScenarioServiceEvent event, final ScenarioInstance scenarioInstance) {
		super.fireEvent(event, scenarioInstance);
	}

	@Override
	protected ScenarioService initServiceModel() {
		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("Online tasks results");
		serviceModel.setLocal(false);
		serviceModel.setOffline(false);
		serviceModel.setServiceID(getSerivceID());

		// This image needs disposing
		serviceModel.setImage(CommonImages.getImageDescriptor(IconPaths.Cloud_16, IconMode.Enabled).createImage());

		return serviceModel;
	}

	@Override
	public URI resolveURI(final String uri) {
		return URI.createURI(uri);
	}
}