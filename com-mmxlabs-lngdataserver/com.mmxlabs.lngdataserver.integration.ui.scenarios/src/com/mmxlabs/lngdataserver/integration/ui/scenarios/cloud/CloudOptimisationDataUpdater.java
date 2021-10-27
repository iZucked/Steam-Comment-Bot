/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IDataHubStateChangeListener;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;

public class CloudOptimisationDataUpdater {

	private final CloudOptimisationDataServiceClient client;

	private final ExecutorService taskExecutor;
	private final ScenarioService modelRoot;

	private final File basePath;
	private Instant lastModified = Instant.EPOCH;
	private boolean purgeCache = false;
	
	private Set<String> warnedLoadFailures = new HashSet<>();
	
	private static final Logger LOG = LoggerFactory.getLogger(CloudOptimisationDataUpdater.class);

	private final IUpstreamDetailChangedListener purgeLocalRecords = () -> purgeCache = true;
	private final IDataHubStateChangeListener dataHubStateChangeListener = new IDataHubStateChangeListener() {
		
		@Override
		public void hubStateChanged(boolean online, boolean loggedin, boolean changedToOnlineAndLoggedIn) {
			purgeCache = changedToOnlineAndLoggedIn;
		}
		
		@Override
		public void hubPermissionsChanged() {
			
		}
	};

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	private final Consumer<CloudOptimisationDataResultRecord> readyCallback;
	private final ConcurrentMap<String, Instant> oldRecords;

	public CloudOptimisationDataUpdater(final File basePath, final CloudOptimisationDataServiceClient client, final ScenarioService modelRoot,
			final Consumer<CloudOptimisationDataResultRecord> readyCallback) {
		this.modelRoot = modelRoot;
		this.basePath = basePath;
		this.client = client;
		this.readyCallback = readyCallback;
		taskExecutor = Executors.newSingleThreadExecutor();
		oldRecords = new ConcurrentHashMap<String, Instant>();
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(purgeLocalRecords);
		DataHubServiceProvider.getInstance().addDataHubStateListener(dataHubStateChangeListener);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(purgeLocalRecords);
		DataHubServiceProvider.getInstance().removeDataHubStateListener(dataHubStateChangeListener);
		taskExecutor.shutdownNow();
	}

	public void update(final List<CloudOptimisationDataResultRecord> records) {

		if (records != null) {
			for (final CloudOptimisationDataResultRecord record : records) {
				taskExecutor.execute(new DownloadTask(record));
			}
		}
	}

	private class DownloadTask implements Runnable {
		private final CloudOptimisationDataResultRecord record;
		private final Container parent;

		public DownloadTask(final CloudOptimisationDataResultRecord record) {
			this.record = record;
			this.parent = modelRoot;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s/%s.lingo", basePath, record.getJobid()));
			final Instant creationDate = oldRecords.get(record.getUuid());
			if (!Objects.equals(record.getCreationDate(), creationDate) && record.getCreationDate() != null) {
				try {
					f.getParentFile().mkdirs();
					if (!downloadData(record, f)) {
						// Something went wrong - reset lastModified to trigger another refresh
						lastModified = Instant.EPOCH;
						// Failed!
						return;
					} else {
						oldRecords.put(record.getUuid(), record.getCreationDate());
					}
				} catch (final Exception e) {
					// Something went wrong - reset lastModified to trigger another refresh
					lastModified = Instant.EPOCH;
					e.printStackTrace();
					return;
				}
			}
			
			if (f.exists() && f.canRead()) {
				final ScenarioInstance instance = loadScenarioFrom(f, record);
				if (instance != null) {
					RunnerHelper.syncExecDisplayOptional(() -> {
						// We could already be in a container, so lets remove it first...
						if (instance.eContainer() != null) {
							((Container) instance.eContainer()).getElements().remove(instance);
						}

						// ... because this can trigger a rename request back to the hub due to the adapter in SharedWorkspaceServiceClient ...
						instance.setName(record.getOriginalName());

						// ... then re-add it to the new (or existing) parent.
						parent.getElements().add(instance);
					});
				}
			}

			if (record.getCreationDate() != null) {
				readyCallback.accept(record);
			}
		}

		private boolean downloadData(final CloudOptimisationDataResultRecord rtd, final File f) {
			final boolean[] ret = new boolean[1];
			final Job background = new Job("Downloading scenario") {

				@Override
				public IStatus run(final IProgressMonitor monitor) {
					try {
						ret[0] = client.downloadTo(rtd.getJobid(), f, WrappedProgressMonitor.wrapMonitor(monitor));
					} catch (final Exception e) {
						// return Status.
						e.printStackTrace();
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
			background.schedule();
			try {
				background.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			return ret[0];
		}
	}

	public void stop() {
		if (updateThread != null) {
			updateThread.interrupt();
			updateThread = null;
		}
	}

	public void start() {
		final File f = new File(basePath.getAbsolutePath() + "/tasks.json");
		if (f.exists() && f.canRead()) {
			String json;
			try {
				json = Files.readString(f.toPath());
				final List<CloudOptimisationDataResultRecord> tasks = CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
				if (tasks != null && !tasks.isEmpty()) {
					update(tasks);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		updateThread = new Thread("GenericDataUpdaterThread") {
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
		updateThread.start();
	}

	public synchronized void refresh() throws IOException {
		if (purgeCache) {
			purgeLocalRecords();
		}
		final boolean available = DataHubServiceProvider.getInstance().isOnlineAndLoggedIn();

		if (available) {

			//final Instant m = client.getLastSuccessfulAccess();
			//if (m != null && m.isAfter(lastModified)) {
				final Pair<String, Instant> recordsPair = client.listContents(true);
				if (recordsPair != null) {
					final File tasksFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "tasks.json");
					final File tasksFileBKP = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "tasks.json.bkp");
					
					if (tasksFile.exists()) {
						if (tasksFileBKP.exists()) {
							tasksFileBKP.delete();
						}
						tasksFile.renameTo(tasksFileBKP);
					}
					
					final List<String> tasks = getJobId(recordsPair.getFirst());
					final List<CloudOptimisationDataResultRecord> records = processRecordsFromTasks(tasks);
					update(records);
					final String json = CloudOptimisationDataServiceClient.getJSON(records);
					Files.writeString(tasksFile.toPath(), json, Charsets.UTF_8);
					lastModified = recordsPair.getSecond();
				}
			//}
		}
	}
	
	private List<String> getJobId(final String jobIds){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jobIds, List.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_LIST;
	}
	
	private List<CloudOptimisationDataResultRecord> processRecordsFromTasks(final List<String> tasks){
		final List<CloudOptimisationDataResultRecord> results = new ArrayList(tasks.size());
		for (final String jobId : tasks) {
			final CloudOptimisationDataResultRecord temp = getRecord(jobId);
			if (temp != null) {
				results.add(temp);
			}
		}
		return results;
	}
	
	private CloudOptimisationDataResultRecord getRecord(final String jobid) {
		final File recordsFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "records.json");
		String json;
		try {
			json = Files.readString(recordsFile.toPath());
			final List<CloudOptimisationDataResultRecord> records = CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
			if (records != null && !records.isEmpty()) {
				for (final CloudOptimisationDataResultRecord record : records) {
					if (record.getJobid().equalsIgnoreCase(jobid)) {
						return record;
					}
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private synchronized void purgeLocalRecords() {
		if (basePath.exists() && basePath.canRead() && basePath.canWrite() && basePath.isDirectory()) {
			for (final File f : this.basePath.listFiles()) {
				if (f.exists())
					f.delete();
			}
			oldRecords.clear();
			purgeCache = false;
			lastModified = Instant.EPOCH;
		}
	}

	public void pause() {
		updateLock.lock();
	}

	public void resume() {
		updateLock.unlock();
	}
	
	protected ScenarioInstance loadScenarioFrom(final File f, final CloudOptimisationDataResultRecord record) {
		final URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f);
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(true);
			scenarioInstance.setUuid(manifest.getUUID());
			scenarioInstance.setExternalID(record.getUuid());

			scenarioInstance.setRootObjectURI(archiveURI.toString());

			scenarioInstance.setName(record.getOriginalName());
			scenarioInstance.setVersionContext(manifest.getVersionContext());
			scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

			scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
			scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

			final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
			meta.setCreator(record.getCreator());
			meta.setCreated(Date.from(record.getCreationDate()));

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
					LOG.error(e.getMessage(), e);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			});
			return scenarioInstance;
		}

		if (warnedLoadFailures.add(f.getName())) {
			LOG.error("Error reading team scenario file {}. Check encryption certificate.", f.getName());
		}
		return null;
	}
}
