/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseRecord;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.debug.BaseCaseDebugContants;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class BaseCaseScenarioUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCaseScenarioUpdater.class);

	private final BaseCaseServiceClient client;
	private final BaseCaseVersionsProviderService baseCaseVersionsProviderService;

	private final ExecutorService taskExecutor;

	private final Map<String, ScenarioInstance> mapping = new ConcurrentHashMap<>();
	private final Map<String, Container> pathMap = new ConcurrentHashMap<>();
	private final ScenarioService modelRoot;
	private final File basePath;
	private String lastUUID = "";
	private final IUpstreamDetailChangedListener detailChangedListener = () -> lastUUID = ""; // Reset to trigger refresh

	private Thread updateThread;

	public BaseCaseScenarioUpdater(final ScenarioService modelRoot, final File basePath, final BaseCaseServiceClient client, final BaseCaseVersionsProviderService baseCaseVersionsProviderService) {
		this.modelRoot = modelRoot;
		this.basePath = basePath;
		this.client = client;
		this.baseCaseVersionsProviderService = baseCaseVersionsProviderService;
		taskExecutor = Executors.newSingleThreadExecutor();
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(detailChangedListener);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(detailChangedListener);
		taskExecutor.shutdownNow();
	}

	public synchronized void update(final BaseCaseRecord record, final Runnable onSuccess) {

		final Set<String> existingUUIDS = new HashSet<>();
		recursiveUUIDCollector(modelRoot, existingUUIDS);

		final Set<String> existingFilenames = new HashSet<>();
		{
			for (var child : basePath.listFiles()) {
				if (child.isFile() && child.getName().endsWith(".lingo")) {
					existingFilenames.add(child.getName());
				}
			}
		}

		if (record != null) {
			final String uuid = record.uuid;
			final String name = record.originalName;

			final Container parent = modelRoot;

			taskExecutor.execute(new AddScenarioTask(parent, name, record, onSuccess));
			existingUUIDS.remove(uuid);

			existingFilenames.remove(uuid + ".lingo");

		}

		// Clean up deleted items
		for (final String uuid : existingUUIDS) {
			final ScenarioInstance scenarioInstance = mapping.get(uuid);
			if (scenarioInstance != null) {
				ScenarioServiceModelUtils.closeReferences(scenarioInstance);
				final Container parent = scenarioInstance.getParent();
				if (parent != null) {
					RunnerHelper.asyncExec(() -> parent.getElements().remove(scenarioInstance));
				}
				SSDataManager.Instance.releaseModelRecord(scenarioInstance);
				mapping.remove(uuid);

			}
		}

		// TODO: Move .lingo file into delete queue;
		existingFilenames.forEach(uuid -> taskExecutor.execute(new DeleteScenarioTask(uuid)));
	}

	private void recursiveUUIDCollector(final Container parent, final Collection<String> uuids) {
		if (parent instanceof ScenarioInstance scenarioInstance) {
			uuids.add(scenarioInstance.getExternalID());
		}
		for (final Container c : parent.getElements()) {
			recursiveUUIDCollector(c, uuids);
		}

	}

	private class AddScenarioTask implements Runnable {
		private final Container parent;
		private final BaseCaseRecord record;
		private final String name;
		private final Runnable onSuccess;

		public AddScenarioTask(final Container parent, final String name, final BaseCaseRecord record, final Runnable onSuccess) {
			this.parent = parent;
			this.name = name;
			this.record = record;
			this.onSuccess = onSuccess;
		}

		@Override
		public void run() {
			if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
				LOGGER.trace("Downloading new base case " + record.uuid);
			}
			final File f = new File(String.format("%s%s%s.lingo", basePath, File.separator, record.uuid));
			if (!f.exists()) {
				try {
					if (!downloadScenario(record.uuid, f, null)) {
						if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
							LOGGER.trace("Failed to download new base case " + record.uuid);
						}
						// Something went wrong - reset lastModified to trigger another refresh
						lastUUID = "";
						// Failed!
						return;
					}
				} catch (final Exception e) {
					if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
						LOGGER.trace("Exception downloading new base case " + record.uuid + " " + e.getMessage(), e);
					}
					// Something went wrong - reset lastModified to trigger another refresh
					lastUUID = "";
					e.printStackTrace();
					return;
				}
			}

			final ScenarioInstance instance = mapping.computeIfAbsent(record.uuid, u -> loadScenarioFrom(f, record, name));
			if (instance != null) {
				RunnerHelper.syncExecDisplayOptional(() -> {
					instance.setName(name);
					if (parent.getElements().contains(instance)) {
						// Ensure only this element
						parent.getElements().retainAll(Collections.singleton(instance));
					} else {
						// Remove other entries
						parent.getElements().clear();
						// Register this one.
						parent.getElements().add(instance);
						baseCaseVersionsProviderService.setBaseCase(instance);
					}
					if(onSuccess != null) {
						onSuccess.run();
					}
				});
			} else {
				if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
					LOGGER.trace("Error loading new base case " + record.uuid);
				}
			}
		}
	}

	private class DeleteScenarioTask implements Runnable {
		private final String uuid;

		public DeleteScenarioTask(final String uuid) {
			this.uuid = uuid;
		}

		@Override
		public void run() {
			if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
				LOGGER.trace("Removing old base case " + uuid);
			}
			final File f = new File(String.format("%s%s%s", basePath, File.separator, uuid));
			if (f.exists()) {
				final boolean secureDelete = LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE);
				try {
					FileDeleter.delete(f, secureDelete);
				} catch (Exception e) {
					LOGGER.error("Error deleting old base case file", e);
				}
			}
		}
	}

	private boolean downloadScenario(final String uuid, final File f, final IProgressListener progressListener) throws IOException {
		final boolean[] ret = new boolean[1];
		final Job background = new Job("Download base case scenario") {

			@Override
			public IStatus run(final IProgressMonitor monitor) {
				// Having a method called "main" in the stacktrace stops SpringBoot throwing an exception in the logging framework
				return main(monitor);
			}

			public IStatus main(final IProgressMonitor monitor) {
				try {
					ret[0] = client.downloadTo(uuid, f, WrappedProgressMonitor.wrapMonitor(monitor));
				} catch (final Exception e) {
					if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_DOWNLOAD)) {
						LOGGER.trace("Exception downloading base case " + e.getMessage(), e);
					}
					// return Status.
				} finally {
					if (monitor != null) {
						monitor.done();
					}

				}

				return Status.OK_STATUS;
			}
		};
		background.setSystem(false);
		background.setUser(true);
		// background.setPriority(Job.LONG);

		background.schedule();
		try {
			background.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		return ret[0];
	}

	protected ScenarioInstance loadScenarioFrom(final File f, final BaseCaseRecord record, final String scenarioname) {
		final URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f);
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(true);
			scenarioInstance.setUuid(manifest.getUUID());
			scenarioInstance.setExternalID(record.uuid);

			scenarioInstance.setRootObjectURI(archiveURI.toString());

			scenarioInstance.setName(scenarioname);
			scenarioInstance.setVersionContext(manifest.getVersionContext());
			scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

			scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
			scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

			final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
			meta.setCreator(record.creator);
			meta.setCreated(Date.from(record.creationDate));

			scenarioInstance.setMetadata(meta);
			meta.setContentType(manifest.getScenarioType());
			// Probably better pass in from service
			ServiceHelper.withOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
				final ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, true, false, true, scenarioCipherProvider);
				if (modelRecord != null) {
					modelRecord.setName(scenarioInstance.getName());
					modelRecord.setScenarioInstance(scenarioInstance);
					SSDataManager.Instance.register(scenarioInstance, modelRecord);
					scenarioInstance.setRootObjectURI(archiveURI.toString());
				}
			});
			return scenarioInstance;
		}
		return null;
	}

	public void stop() {
		if (updateThread != null) {
			updateThread.interrupt();
			updateThread = null;
		}
	}

	public void start() {
		final File f = new File(basePath.getAbsolutePath() + "/basecase.json");
		if (f.exists()) {
			String json;
			try {
				json = Files.asCharSource(f, StandardCharsets.UTF_8).read();

				final BaseCaseRecord bcRecord = client.parseScenariosJSONData(json);
				if (bcRecord != null) {
					update(bcRecord, null);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		updateThread = new Thread("Base Case Updater") {
			@Override
			public void run() {

				while (true) {
					try {
						refresh();
					} catch (final Exception e1) {
						if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
							LOGGER.trace("Exception in refresh " + e1.getMessage(), e1);
						}
						e1.printStackTrace();
					}

					try {
						Thread.sleep(10_000);
					} catch (final InterruptedException e) {
						interrupt(); // preserve interruption status
						if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
							LOGGER.trace("Base Case Updater thread interrupted");
						}
						return;
					}
				}
			}

		};
		WellKnownTriggers.WORKBENCH_POST_STARTUP.delayUntilTriggered(updateThread::start);
	}

	/**
	 * UI created folder, so register with the local map to avoid recreating it later
	 * 
	 * @param f
	 */
	public void registerFolder(final Folder f) {

		final StringBuilder sb = new StringBuilder();
		Container parent = f;
		boolean first = true;
		while (parent != null && !(parent instanceof ScenarioService)) {
			if (!first) {
				sb.insert(0, "/");
			}

			sb.insert(0, parent.getName());
			parent = parent.getParent();
			first = false;
		}
		pathMap.put(sb.toString(), f);
	}

	public void updatePath(final String oldPath, final String newPath) {

		final Container value = pathMap.get(oldPath);
		if (value != null) {
			pathMap.put(newPath, value);
		}
	}

	public void refresh() throws IOException {

		if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
			LOGGER.trace("Checking for base case updates");
		}

		final boolean available = DataHubServiceProvider.getInstance().isOnlineAndLoggedIn();
		if (!modelRoot.isOffline() != available) {
			if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
				LOGGER.trace("Base Case service is now " + (available ? "online" : "offline"));
			}
			RunnerHelper.syncExecDisplayOptional(() -> modelRoot.setOffline(!available));
		}

		if (available) {
			client.updateLockedState();

			boolean isLocked = client.isServiceLocked();
			String lockedBy = client.getLockedBy();
			if (modelRoot.isLocked() != isLocked || !Objects.equals(lockedBy, modelRoot.getLockedBy())) {
				RunnerHelper.syncExecDisplayOptional(() -> {
					baseCaseVersionsProviderService.setLockedBy(client.getLockedBy());
					modelRoot.setLockedBy(UsernameProvider.INSTANCE.getDisplayName(lockedBy));
					modelRoot.setLocked(isLocked);
				});
			}
			final String currentUUID = client.getCurrentBaseCase();
			if (currentUUID != null) {
				if (currentUUID.isEmpty()) {
					if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
						LOGGER.trace("No base case on Data Hub");
					}
					update(null, null);
					lastUUID = currentUUID;
				}
				if (!currentUUID.equals(lastUUID)) {

					if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
						LOGGER.trace("New base case on Data Hub " + currentUUID);
					}
					final String scenariosData = client.getBaseCaseDetails(currentUUID);
					final BaseCaseRecord scenariosInfo = client.parseScenariosJSONData(scenariosData);
					if (scenariosInfo != null) {
						update(scenariosInfo, () -> {
							try {
								Files.write(scenariosData, new File(basePath.getAbsolutePath() + "/basecase.json"), StandardCharsets.UTF_8);
								lastUUID = currentUUID;
							} catch (IOException e) {
								e.printStackTrace();
							}
						});

					} else {
						if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
							LOGGER.trace("Unable to determine new base case");
						}
						update(null, null);
						lastUUID = currentUUID;
					}
				} else {

					if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_POLL)) {
						LOGGER.trace("Base case already up to date");
					}
				}
			}
		}
	}
}
