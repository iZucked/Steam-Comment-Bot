/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
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
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
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
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;

public class CloudOptimisationDataUpdater {

	private final CloudOptimisationDataServiceClient client;

	private final ExecutorService taskExecutor;
	private final ScenarioService modelRoot;

	private final File basePath;
	private final File tasksFile;

	private final Set<String> warnedLoadFailures = new HashSet<>();

	private static final Logger LOG = LoggerFactory.getLogger(CloudOptimisationDataUpdater.class);

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	private final Consumer<CloudOptimisationDataResultRecord> readyCallback;
	private final ConcurrentMap<String, ScenarioInstance> installedRecords;

	private ImmutableList<CloudOptimisationDataResultRecord> currentRecords = ImmutableList.of();

	public CloudOptimisationDataUpdater(final File basePath, final CloudOptimisationDataServiceClient client, final ScenarioService modelRoot,
			final Consumer<CloudOptimisationDataResultRecord> readyCallback) {
		this.modelRoot = modelRoot;
		this.basePath = basePath;
		this.tasksFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "tasks.json");
		this.client = client;
		this.readyCallback = readyCallback;
		taskExecutor = Executors.newSingleThreadExecutor();
		installedRecords = new ConcurrentHashMap<>();
	}

	public void dispose() {
		taskExecutor.shutdownNow();
	}

	public void runDownloadAndInstallTasks(final List<CloudOptimisationDataResultRecord> records) {
		if (records != null) {
			for (final CloudOptimisationDataResultRecord cRecord : records) {
				if (!installedRecords.containsKey(cRecord.getJobid()) && cRecord.getResult() == null) {
					try {
						if (client.isJobComplete(cRecord.getJobid())) {
							taskExecutor.execute(new DownloadTask(cRecord));
						}
					} catch (final Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}
				if (!installedRecords.containsKey(cRecord.getJobid())) {
					taskExecutor.execute(new InstallTask(cRecord));
				}
			}
		}
	}

	private class DownloadTask implements Runnable {
		private final CloudOptimisationDataResultRecord cRecord;
		private final Container parent;

		public DownloadTask(final CloudOptimisationDataResultRecord cRecord) {
			this.cRecord = cRecord;
			this.parent = modelRoot;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s/%s.lingo", basePath, cRecord.getJobid()));
			if (!f.exists()) {
				try {
					f.getParentFile().mkdirs();
					final File temp = new File(String.format("%s/%s-temp.lingo", basePath, cRecord.getJobid()));
					if (!downloadData(cRecord, temp)) {
						// Failed!
						return;
					} else {

						final File anonymisationMap = new File(String.format("%s/%s.amap", basePath, cRecord.getJobid()));
						if (anonymisationMap.exists()) {

							ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
								final ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(URI.createFileURI(temp.getAbsolutePath()), true, true, true, scenarioCipherProvider);
								try (IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
									final LNGScenarioModel origScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
									if (origScenarioModel.isAnonymised()) {
										final LNGScenarioModel copy = EcoreUtil.copy(origScenarioModel);
										final IScenarioDataProvider tempDP = SimpleScenarioDataProvider.make(ModelsLNGVersionMaker.createDefaultManifest(), copy);

										final EditingDomain editingDomain = tempDP.getEditingDomain();
										final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(copy, editingDomain, new HashSet<>(), new ArrayList<>(), false, anonymisationMap);

										if (cmd != null && !cmd.isEmpty()) {
											editingDomain.getCommandStack().execute(cmd);
										}
										ScenarioStorageUtil.storeCopyToFile(tempDP, temp);
									}
								}
							});
						}
						// Move the temp file
						temp.renameTo(f);
						cRecord.setResult(f);

						// trigger install straight away
						new InstallTask(cRecord).run();
						readyCallback.accept(cRecord);
					}
				} catch (final Exception e) {
					e.printStackTrace();
					return;
				}
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
				final ScenarioInstance instance = loadScenarioFrom(f, record, true);
				if (instance != null) {
					RunnerHelper.syncExecDisplayOptional(() -> {
						instance.setName(record.getOriginalName());
//						// We could already be in a container, so lets remove it first...
//						if (instance.eContainer() != null) {
//							((Container) instance.eContainer()).getElements().remove(instance);
//						}

						// ... then re-add it to the new (or existing) parent.
						parent.getElements().add(instance);
						installedRecords.put(record.getJobid(), instance);

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

		if (tasksFile.exists() && tasksFile.canRead()) {
			try {
				final String json = Files.readString(tasksFile.toPath());
				final List<CloudOptimisationDataResultRecord> tasks = CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
				if (tasks != null && !tasks.isEmpty()) {
					// Update downloaded state
					for (final var r : tasks) {
						final File lingoFile = new File(String.format("%s/%s.lingo", basePath, r.getJobid()));
						if (lingoFile.exists()) {
							r.setResult(lingoFile);
						}
					}

					currentRecords = ImmutableList.copyOf(tasks);
					runDownloadAndInstallTasks(currentRecords);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		updateThread = new Thread("CloudOptimisationUpdaterThread") {
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
						// Set to 60_000 for any OneShot or Beta builds
						// During release - two to five minutes
						Thread.sleep(1_000);
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

		boolean changed = false;

		final List<CloudOptimisationDataResultRecord> newList = new LinkedList<>();
		for (final CloudOptimisationDataResultRecord originalR : currentRecords) {
			final CloudOptimisationDataResultRecord r = originalR.copy();

			final boolean oldRemote = r.isRemote();
			final ResultStatus oldStatus = r.getStatus();
			// What is the status?
			try {
				r.setStatus(ResultStatus.from(client.getJobStatus(r.getJobid()), oldStatus));
			} catch (Exception e) {
				// Keep old status if there is some kind of exception.
			}
			// Is the record still available upstream?
			r.setRemote(!r.getStatus().isNotFound());

			if (oldStatus != null && !oldStatus.isComplete() && r.getStatus().isComplete()) {
				Instant n = Instant.now();
				r.setCloudRuntime(n.toEpochMilli() - r.getCreationDate().toEpochMilli());
			}

			changed |= oldRemote != r.isRemote();
			changed |= !Objects.equals(oldStatus, r.getStatus());

			newList.add(r);
		}
		changed |= currentRecords.size() != newList.size();
		if (changed) {
			saveAndUpdateCurrentRecords(newList);
			runDownloadAndInstallTasks(currentRecords);
			// Trigger UI update
			readyCallback.accept(null);
		}
	}

	public synchronized void addNewlySubmittedOptimisationRecord(final CloudOptimisationDataResultRecord r) {

		currentRecords = new ImmutableList.Builder<CloudOptimisationDataResultRecord>() //
				.addAll(currentRecords) //
				.add(r) //
				.build();
		readyCallback.accept(r);

	}

	public synchronized void deleteDownloaded(final Collection<String> jobIds) {
		pause();
		try {
			for (final String jobId : jobIds) {
				final CloudOptimisationDataResultRecord cRecord = getRecord(jobId);
				if (cRecord != null) {
					final ScenarioInstance instance = installedRecords.remove(cRecord.getJobid());
					if (instance != null) {
						RunnerHelper.syncExecDisplayOptional(() -> {
							// We could already be in a container, so lets remove it first...
							if (instance.eContainer() != null) {
								((Container) instance.eContainer()).getElements().remove(instance);
							}
						});
					}
					final boolean result = deleteRecord(cRecord);
				}
			}
			readyCallback.accept(null);
		} finally {
			resume();
		}
	}

	/**
	 * Returns records from the master list of records. Returns null if no record
	 * found.
	 * 
	 * @param jobId  - jobId if next arg is false
	 * @param isUUID
	 * @return
	 */
	private synchronized CloudOptimisationDataResultRecord getRecord(final String jobId) {
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
	 * 
	 * @return
	 */
	public ImmutableList<CloudOptimisationDataResultRecord> getRecords() {
		return currentRecords;
	}

	private synchronized boolean deleteRecord(final CloudOptimisationDataResultRecord record) {

		record.setDeleted(true);
		if (currentRecords.contains(record)) {
			final List<CloudOptimisationDataResultRecord> l = new LinkedList<>(currentRecords);
			while (l.remove(record))
				;
			currentRecords = ImmutableList.copyOf(l);
			try {
				final String json = CloudOptimisationDataServiceClient.getJSON(currentRecords);
				Files.writeString(tasksFile.toPath(), json, StandardCharsets.UTF_8);
			} catch (final Exception e) {
				LOG.error("Error saving list of downloaded records!" + e.getMessage(), e);
			}

			boolean amap = false;
			try {
				final File anonymisationMap = new File(String.format("%s/%s.amap", basePath, record.getJobid()));
				if (anonymisationMap.exists()) {
					amap = anonymisationMap.delete();
				}
			} catch (final Exception e) {
				LOG.error("Error deleting anonymisation map!" + e.getMessage(), e);
			}

			boolean lngFile = false;
			try {
				final File lingoFile = new File(String.format("%s/%s.lingo", basePath, record.getJobid()));
				if (lingoFile.exists()) {
					lngFile = lingoFile.delete();
				}
			} catch (final Exception e) {
				LOG.error("Error deleting scenario result!" + e.getMessage(), e);
			}
			return amap && lngFile;
		}
		return false;
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
				} catch (final ScenarioEncryptionException e) {
					LOG.error(e.getMessage(), e);
				} catch (final Exception e) {
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

	private synchronized void saveAndUpdateCurrentRecords(final List<CloudOptimisationDataResultRecord> newList) {
		try {
			final String json = CloudOptimisationDataServiceClient.getJSON(newList);
			Files.writeString(tasksFile.toPath(), json, StandardCharsets.UTF_8);
		} catch (final Exception e) {
			int ii = 0;
		}
		currentRecords = ImmutableList.copyOf(newList);
	}

	public synchronized void setLocalRuntime(String jobId, long runtime) {
		CloudOptimisationDataResultRecord record = getRecord(jobId);
		if (record != null) {
			record.setLocalRuntime(runtime);
		}
		readyCallback.accept(record);
	}
}
