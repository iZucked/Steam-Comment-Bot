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
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
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
	private final File tasksFile;
	private final File downloadedFile;
	private final File recordsFile;
	private boolean purgeCache = false;
	
	private Set<String> warnedLoadFailures = new HashSet<>();
	
	private static final Logger LOG = LoggerFactory.getLogger(CloudOptimisationDataUpdater.class);

	private final IUpstreamDetailChangedListener purgeLocalRecords = () -> purgeCache = true;

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	private final Consumer<CloudOptimisationDataResultRecord> readyCallback;
	private final ConcurrentMap<String, Instant> installedRecords;
	private final ConcurrentMap<String, Instant> downloadedRecords;

	public CloudOptimisationDataUpdater(final File basePath, final CloudOptimisationDataServiceClient client, final ScenarioService modelRoot,
			final Consumer<CloudOptimisationDataResultRecord> readyCallback) {
		this.modelRoot = modelRoot;
		this.basePath = basePath;
		this.recordsFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "records.json");
		this.tasksFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "tasks.json");
		this.downloadedFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "downloaded.json");
		this.client = client;
		this.readyCallback = readyCallback;
		taskExecutor = Executors.newSingleThreadExecutor();
		installedRecords = new ConcurrentHashMap<String, Instant>();
		downloadedRecords = new ConcurrentHashMap<String, Instant>();
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(purgeLocalRecords);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(purgeLocalRecords);
		taskExecutor.shutdownNow();
	}

	public void update(final List<CloudOptimisationDataResultRecord> records) {
		if (records != null) {
			for (final CloudOptimisationDataResultRecord record : records) {
				if (!installedRecords.containsKey(record.getJobid()) && !downloadedRecords.containsKey(record.getJobid())) {
					try {
						if (client.isJobComplete(record.getJobid())) {
							taskExecutor.execute(new DownloadTask(record));
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}
				if (!installedRecords.containsKey(record.getJobid())){
					taskExecutor.execute(new InstallTask(record));
				}
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
			if (!f.exists() && !downloadedRecords.containsKey(record.getJobid())) {
				try {
					f.getParentFile().mkdirs();
					if (!downloadData(record, f)) {
						// Failed!
						return;
					} else {
						downloadedRecords.put(record.getJobid(), record.getCreationDate());
					}
				} catch (final Exception e) {
					e.printStackTrace();
					return;
				}
			} else {
				if (f.exists()) {
					downloadedRecords.put(record.getJobid(), record.getCreationDate());
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
	
	private class InstallTask implements Runnable {
		private final CloudOptimisationDataResultRecord record;
		private final Container parent;

		public InstallTask(final CloudOptimisationDataResultRecord record) {
			this.record = record;
			this.parent = modelRoot;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s/%s.lingo", basePath, record.getJobid()));
			
			if (f.exists() && f.canRead() && !installedRecords.containsKey(record.getJobid())) {
				final ScenarioInstance instance = deanonymiseScenario(f, record);
				if (instance != null) {
					RunnerHelper.syncExecDisplayOptional(() -> {
						// We could already be in a container, so lets remove it first...
						if (instance.eContainer() != null) {
							((Container) instance.eContainer()).getElements().remove(instance);
						}

						// ... then re-add it to the new (or existing) parent.
						parent.getElements().add(instance);
						installedRecords.put(record.getJobid(), record.getCreationDate());

					});
				}
			}

			if (record.getCreationDate() != null) {
				readyCallback.accept(record);
			}
		}
	}

	public void stop() {
		if (updateThread != null) {
			updateThread.interrupt();
			updateThread = null;
		}
	}

	public void start() {
		initDownloadedRecords();
		
		if (tasksFile.exists() && tasksFile.canRead()) {
			try {
				final String json = Files.readString(tasksFile.toPath());
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

		final Pair<String, Instant> recordsPair = client.listContents(true);
		if (recordsPair != null) {
			final List<String> tasks = getJobId(recordsPair.getFirst());
			final List<CloudOptimisationDataResultRecord> records = processRecordsFromTasks(tasks);
			final String json = CloudOptimisationDataServiceClient.getJSON(records);
			Files.writeString(tasksFile.toPath(), json, Charsets.UTF_8);
			update(records);
		}
		updateDownloaded();
	}
	
	private void initDownloadedRecords() {
		if (downloadedFile.exists() && downloadedFile.canRead()) {
			try {
				final String json = Files.readString(downloadedFile.toPath());
				final List<CloudOptimisationDataResultRecord> tasks = CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
				tasks.stream().forEach(t -> downloadedRecords.put(t.getJobid(), t.getCreationDate()));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void updateDownloaded(){
		try {
			if (downloadedFile.exists() && downloadedFile.canWrite() && !downloadedRecords.isEmpty()) {
				final List<String> dTasks = new ArrayList(downloadedRecords.keySet());
				final List<CloudOptimisationDataResultRecord> recs = processRecordsFromTasks(dTasks);
				final String json = CloudOptimisationDataServiceClient.getJSON(recs);
				Files.writeString(downloadedFile.toPath(), json, Charsets.UTF_8);
			}
		} catch (Exception e) {
			LOG.error("Error saving list of downloaded records!" + e.getMessage(), e);
		}
	}
	
	public synchronized boolean deleteDownloaded(final String jobId) {
		final CloudOptimisationDataResultRecord record = getRecord(jobId);
		if (record != null) {
			pause();
			
			installedRecords.remove(record.getJobid());
			
			if (downloadedRecords.remove(record.getJobid()) != null) {
				updateDownloaded();
			}
			boolean result = deleteRecord(record);
			resume();
			return result;
		}
		return false;
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
	
	/**
	 * Returns records from the master list of records.
	 * Returns null if no record found.
	 * @param jobId - jobId if next arg is false
	 * @param isUUID
	 * @return
	 */
	private CloudOptimisationDataResultRecord getRecord(final String jobId) {
		final List<CloudOptimisationDataResultRecord> records = getRecords();
		if (records != null && !records.isEmpty()) {
			for (final CloudOptimisationDataResultRecord record : records) {
				if (record.getJobid().equalsIgnoreCase(jobId)) {
					return record;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a master list of records
	 * @return
	 */
	private List<CloudOptimisationDataResultRecord> getRecords() {
		if (recordsFile.exists() && recordsFile.canRead()) {
			String json;
			try {
				json = Files.readString(recordsFile.toPath());
				return CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return Collections.EMPTY_LIST;
	}
	
	private boolean deleteRecord(final CloudOptimisationDataResultRecord record) {
		final List<CloudOptimisationDataResultRecord> records = getRecords();
		if (records.remove(record)) {
			try {
				if (recordsFile.exists() && recordsFile.canWrite()) {
					final String json = CloudOptimisationDataServiceClient.getJSON(records);
					Files.writeString(recordsFile.toPath(), json, Charsets.UTF_8);
					boolean amap = false;
					final File anonymisationMap = new File(String.format("%s/%s.amap", basePath, record.getJobid()));
					if (anonymisationMap.exists()) {
						amap = anonymisationMap.delete();
					}
					boolean lngFile = false;
					final File lingoFile = new File(String.format("%s/%s.lingo", basePath, record.getJobid()));
					if (lingoFile.exists()) {
						lngFile = lingoFile.delete();
					}
					return amap && lngFile;
				}
			} catch (Exception e) {
				LOG.error("Error saving list of downloaded records!" + e.getMessage(), e);
			}
		}
		return false;
	}
	
	private synchronized void purgeLocalRecords() {
		if (basePath.exists() && basePath.canRead() && basePath.canWrite() && basePath.isDirectory()) {
			for (final File f : this.basePath.listFiles()) {
				if (f.exists())
					f.delete();
			}
			installedRecords.clear();
			downloadedRecords.clear();
			purgeCache = false;
		}
	}

	public void pause() {
		updateLock.lock();
	}

	public void resume() {
		updateLock.unlock();
	}
	
	protected ScenarioInstance loadScenarioFrom(final File f, final CloudOptimisationDataResultRecord record, final boolean readOnly) {
		final URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f);
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(readOnly);
			scenarioInstance.setUuid(manifest.getUUID());
			scenarioInstance.setExternalID(record.getJobid());

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
	
	protected ScenarioInstance deanonymiseScenario(final File f, final CloudOptimisationDataResultRecord record) {
		final ScenarioInstance instance = loadScenarioFrom(f, record, false);
		if (instance != null) {
			RunnerHelper.syncExecDisplayOptional(() -> {
				// We could already be in a container, so lets remove it first...
				if (instance.eContainer() != null) {
					((Container) instance.eContainer()).getElements().remove(instance);
				}
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				
				try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
					final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
					if (scenarioModel.isAnonymised()) {
						final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

						final File anonymisationMap = new File(String.format("%s/%s.amap", basePath, record.getJobid()));
						final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(scenarioModel, editingDomain, 
								new HashSet(), new ArrayList(), false, anonymisationMap);
						if (cmd != null && !cmd.isEmpty()) {
							editingDomain.getCommandStack().execute(cmd);
						}
						final File temp = new File(String.format("%s/%s-temp.lingo", basePath, record.getJobid()));
						ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, temp);
						if (f.delete()) {
							temp.renameTo(f);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		
		ScenarioInstance result = loadScenarioFrom(f, record, true);
		result.setName(record.getOriginalName());
		return result;
	}
}
