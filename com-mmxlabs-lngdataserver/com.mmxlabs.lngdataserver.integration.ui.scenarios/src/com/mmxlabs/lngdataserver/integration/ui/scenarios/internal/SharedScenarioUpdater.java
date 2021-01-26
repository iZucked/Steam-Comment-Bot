/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SharedScenarioRecord;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SharedWorkspacePathUtils;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SharedWorkspaceServiceClient;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
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
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class SharedScenarioUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(SharedScenarioUpdater.class);

	private final SharedWorkspaceServiceClient client;

	private final ExecutorService taskExecutor;

	private final Map<String, ScenarioInstance> mapping = new ConcurrentHashMap<>();
	private final Map<String, Container> pathMap = new ConcurrentHashMap<>();
	private final ScenarioService modelRoot;
	private final File basePath;
	private Instant lastModified = Instant.EPOCH;
	private final IUpstreamDetailChangedListener detailChangedListener = () -> lastModified = Instant.EPOCH; // Reset to trigger refresh

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	// Set used to emit load failure messages once
	private Set<String> warnedLoadFailures = new HashSet<>();

	public SharedScenarioUpdater(final ScenarioService modelRoot, final File basePath, final SharedWorkspaceServiceClient client) {
		this.modelRoot = modelRoot;
		this.basePath = basePath;
		this.client = client;
		taskExecutor = Executors.newSingleThreadExecutor();
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(detailChangedListener);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(detailChangedListener);
		taskExecutor.shutdownNow();
	}

	public void update(final List<SharedScenarioRecord> scenarios) {

		final Set<String> existingUUIDS = new HashSet<>();
		recursiveUUIDCollector(modelRoot, existingUUIDS);

		if (scenarios != null) {
			for (final SharedScenarioRecord scenario : scenarios) {
				final String uuid = scenario.uuid;
				final String path = scenario.pathString;

				final Container parent = getContainer(path);
				final String name = SharedWorkspacePathUtils.getLastName(path);

				taskExecutor.execute(new AddScenarioTask(parent, name, scenario));
				existingUUIDS.remove(uuid);
			}
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

				// TODO: Move .lingo file into delete queue
			}
		}

		taskExecutor.execute(new CleanupFoldersTask(modelRoot));

	}

	private void recursiveUUIDCollector(final Container parent, final Collection<String> uuids) {
		if (parent instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) parent;
			uuids.add(scenarioInstance.getExternalID());
		}
		for (final Container c : parent.getElements()) {
			recursiveUUIDCollector(c, uuids);
		}

	}

	private Container getContainer(final String path) {
		final String[] segments = SharedWorkspacePathUtils.getSegments(path);
		Container parent = modelRoot;

		for (int i = 0; i < segments.length - 1; ++i) {
			final String segmentPath = SharedWorkspacePathUtils.compileSegments(Arrays.copyOf(segments, 1 + i));
			synchronized (pathMap) {
				if (pathMap.containsKey(segmentPath)) {
					parent = pathMap.get(segmentPath);
					// Convert folder to managed state (it may have been created by the user, but
					// now we take it over
					if (parent instanceof Folder) {
						final Folder folder = (Folder) parent;
						// Move display code into task executor to avoid deadlock
						taskExecutor.execute(() -> RunnerHelper.syncExecDisplayOptional(() -> folder.setManaged(true)));
					}
				} else {
					final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
					f.setManaged(true);
					f.setName(segments[i]);
					taskExecutor.execute(new AddFolderTask(parent, f));
					pathMap.put(segmentPath, f);
					parent = f;
				}
			}
		}
		return parent;
	}

	private class AddFolderTask implements Runnable {
		private final Container parent;
		private final Folder f;

		public AddFolderTask(final Container parent, final Folder f) {
			this.parent = parent;
			this.f = f;
		}

		@Override
		public void run() {
			RunnerHelper.syncExecDisplayOptional(() -> parent.getElements().add(f));
		}
	}

	private class AddScenarioTask implements Runnable {
		private final Container parent;
		private final SharedScenarioRecord record;
		private final String name;

		public AddScenarioTask(final Container parent, final String name, final SharedScenarioRecord record) {
			this.parent = parent;
			this.name = name;
			this.record = record;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s%s%s.lingo", basePath, File.separator, record.uuid));
			if (!f.exists()) {
				try {
					if (!downloadScenario(record.uuid, f)) {
						// Something went wrong - reset lastModified to trigger another refresh
						lastModified = Instant.EPOCH;
						// Failed!
						return;
					}
				} catch (final IOException e) {
					// Something went wrong - reset lastModified to trigger another refresh
					lastModified = Instant.EPOCH;
					e.printStackTrace();
					return;
				}
			}

			final ScenarioInstance instance = mapping.computeIfAbsent(record.uuid, u -> loadScenarioFrom(f, record, name));
			if (instance != null) {
				RunnerHelper.syncExecDisplayOptional(() -> {
					// We could already be in a container, so lets remove it first...
					if (instance.eContainer() != null) {
						((Container) instance.eContainer()).getElements().remove(instance);
					}

					// ... because this can trigger a rename request back to the hub due to the adapter in SharedWorkspaceServiceClient ...
					instance.setName(name);

					// ... then re-add it to the new (or existing) parent.
					parent.getElements().add(instance);
				});
			}
		}
	}

	private class CleanupFoldersTask implements Runnable {
		private final Container root;

		public CleanupFoldersTask(final Container root) {
			this.root = root;
		}

		@Override
		public void run() {

			RunnerHelper.syncExecDisplayOptional(() -> recursiveDelete(root));
		}

		private void recursiveDelete(final Container parent) {
			if (parent instanceof ScenarioInstance) {
				return;
			}
			// Copy list as we modify the original
			final List<Container> elements = new ArrayList<>(parent.getElements());
			for (final Container c : elements) {
				recursiveDelete(c);
			}
			if (parent instanceof Folder) {
				final Folder folder = (Folder) parent;
				if (folder.isManaged() && elements.isEmpty()) {
					final Container parent2 = folder.getParent();
					if (parent2 != null) {
						synchronized (pathMap) {
							pathMap.remove(SharedWorkspacePathUtils.getPathFor(folder));
						}
						parent2.getElements().remove(folder);
					}
				}
			}

		}
	}

	private boolean downloadScenario(final String uuid, final File f) throws IOException {
		final boolean[] ret = new boolean[1];
		final Job background = new Job("Download shared team scenario") {

			@Override
			public IStatus run(final IProgressMonitor monitor) {
				try {
					ret[0] = client.downloadTo(uuid, f, WrappedProgressMonitor.wrapMonitor(monitor));
				} catch (final Exception e) {
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

	protected ScenarioInstance loadScenarioFrom(final File f, final SharedScenarioRecord record, final String scenarioname) {
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
				try {
					final ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURIChecked(archiveURI, true, false, false, scenarioCipherProvider);
					if (modelRecord != null) {
						modelRecord.setName(scenarioInstance.getName());
						modelRecord.setScenarioInstance(scenarioInstance);
						SSDataManager.Instance.register(scenarioInstance, modelRecord);
						scenarioInstance.setRootObjectURI(archiveURI.toString());
					}
				} catch (ScenarioEncryptionException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			});
			return scenarioInstance;
		}

		if (warnedLoadFailures.add(f.getName())) {
			LOGGER.error("Error reading team scenario file {}. Check encryption certificate.", f.getName());
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
		final File f = new File(basePath.getAbsolutePath() + "/scenarios.json");
		if (f.exists()) {
			String json;
			try {
				json = Files.toString(f, Charsets.UTF_8);

				final List<SharedScenarioRecord> scenariosList = client.parseScenariosJSONData(json);
				if (scenariosList != null) {
					update(scenariosList);
				}
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		updateThread = new Thread() {
			@Override
			public void run() {

				while (true) {
					updateLock.lock();
					try {
						refresh();
					} catch (final IOException e1) {
						e1.printStackTrace();
					} finally {
						updateLock.unlock();
					}

					try {
						Thread.sleep(10_000);
					} catch (final InterruptedException e) {
						interrupt(); // preserve interruption status
						return;
					}
				}
			}

		};

		// Defer the update thread start to the task executor so the task created in the
		// #update calls at the of the method do not clash.
		taskExecutor.submit(() -> updateThread.start());

	}

	/**
	 * UI created folder, so register with the local map to avoid recreating it later
	 * 
	 * @param f
	 */
	public Container createFolderFromUI(final @NonNull Container parent, final @NonNull String name, final boolean execNow) {

		final String path = SharedWorkspacePathUtils.getPathFor(parent, name);

		synchronized (pathMap) {
			if (pathMap.containsKey(path)) {
				// Folder already exists
				return pathMap.get(path);
			} else {
				final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
				f.setName(name);
				final AddFolderTask task = new AddFolderTask(parent, f);
				if (execNow) {
					task.run();
				} else {
					taskExecutor.execute(task);
				}
				pathMap.put(path, f);
				return f;
			}
		}
	}

	public @Nullable Container updatePath(final String oldPath, final String newPath) {
		synchronized (pathMap) {
			final Container value = pathMap.get(oldPath);
			if (value != null) {
				pathMap.put(newPath, value);
			}
			return value;
		}
	}

	public @Nullable Container removePath(final String oldPath) {
		synchronized (pathMap) {
			return pathMap.remove(oldPath);
		}
	}

	public void refresh() throws IOException {
		final boolean available = DataHubServiceProvider.getInstance().isOnlineAndLoggedIn();
		if (!modelRoot.isOffline() != available) {
			RunnerHelper.syncExecDisplayOptional(() -> modelRoot.setOffline(!available));
		}

		if (available) {
			final Instant m = client.getLastModified();
			if (m != null && m.isAfter(lastModified)) {
				final Pair<String, Instant> scenariosPair = client.getScenarios();
				if (scenariosPair != null) {
					final List<SharedScenarioRecord> scenariosList = client.parseScenariosJSONData(scenariosPair.getFirst());
					update(scenariosList);
					Files.write(scenariosPair.getFirst(), new File(basePath.getAbsolutePath() + "/scenarios.json"), Charsets.UTF_8);
					lastModified = scenariosPair.getSecond();
				}
			}
		}
	};

	public void pause() {
		updateLock.lock();
	}

	public void resume() {
		updateLock.unlock();
	}
}
