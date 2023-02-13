/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.debug.CloudOptiDebugContants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse.IGatewayResponse;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.Task;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.TaskStatus;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.CloudOptimisationSharedCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileLoader;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;

class CloudOptimisationDataUpdater {
	private static final Logger LOG = LoggerFactory.getLogger(CloudOptimisationDataUpdater.class);

	private final CloudOptimisationDataServiceClient client;

	private final ExecutorService taskExecutor;

	private final File basePath;
	private final File tasksFile;
	private final File userIdFile;

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	private ImmutableList<CloudOptimisationDataResultRecord> currentRecords = ImmutableList.of();

	private boolean shortPoll = false;
	private boolean runUpdateThread = false;

	private CloudJobManager mgr;

	public CloudOptimisationDataUpdater(final File basePath, final CloudOptimisationDataServiceClient client, CloudJobManager mgr) {
		this.basePath = basePath;
		this.mgr = mgr;
		this.tasksFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "tasks.json");
		this.userIdFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "userId.txt");
		this.client = client;
		taskExecutor = Executors.newSingleThreadExecutor();
	}

	public void dispose() {
		taskExecutor.shutdownNow();
	}

	public void runDownloadTasks(final List<CloudOptimisationDataResultRecord> records) {
		if (records != null) {
			for (final CloudOptimisationDataResultRecord cRecord : records) {
				// Task status is complete, but record is not complete
				if (!cRecord.isComplete() && cRecord.getStatus().isComplete()) {
					try {
						taskExecutor.execute(new DownloadTask(cRecord.task, cRecord));
					} catch (final Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	private class DownloadTask implements Runnable {
		private final CloudOptimisationDataResultRecord cRecord;
		private Task task;

		public DownloadTask(Task task, final CloudOptimisationDataResultRecord cRecord) {
			this.task = task;
			this.cRecord = cRecord;
		}

		@Override
		public void run() {

			if (cRecord.isComplete()) {
				return;
			}
			if (!cRecord.getStatus().isComplete()) {
				// Not ready to download yet
				return;
			}

			int batchSize = cRecord.getBatchSize() == 0 ? 1 : cRecord.getBatchSize();
			// References so the finally block can clean up
			File[] temp = new File[batchSize];
			File[] solutionFile = new File[batchSize];
			try {

				for (int i = 0; i < batchSize; ++i) {

					// Put this in the temp folder as delete() doesn't always seem to it
					temp[i] = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), cRecord.getJobid(), ".solution").toFile();

					final IGatewayResponse downloadResult = downloadData(cRecord, temp[i], i);
					if (downloadResult == null) {
						// Failed, probably offline, we can try again

						if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
							LOG.trace("Download Result (%s): Null gateway response", cRecord.getJobid());
						}

						return;
					} else if (!downloadResult.isResultDownloaded()) {
						// Download failed, but we got a response from our gateway.
						// Should we try again? Some errors are temporary issues, some are permanent
						if (!downloadResult.shouldPollAgain()) {
							// Mark record as complete as we will not attempt anything further
							cRecord.setComplete(true);
							// Record the failure state
							mgr.updateTaskStatus(task, TaskStatus.failed("Unable to download result"));

							if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
								LOG.trace("Download Result (%s): Download failed - no further retries", cRecord.getJobid());
							}
						}
						return;

					} else {

						if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
							LOG.trace("Download Result (%s): Result downloaded to %s", cRecord.getJobid(), temp[i].getAbsolutePath());
						}

						// Successful download. Now we need to de-crypt the result and import it.
						final File keyfileFile = new File(String.format("%s/%s.key.p12", basePath, cRecord.getJobid()));
						if (!keyfileFile.exists()) {
							mgr.updateTaskStatus(task, TaskStatus.failed("Unable to find decryption key"));
							cRecord.setComplete(true);
							throw new RuntimeException(String.format("Failed to get the result decryption key for job %s", cRecord.getJobid()));
						}
						final KeyFileV2 keyfilev2 = KeyFileLoader.loadKeyFile(keyfileFile);
						if (keyfilev2 == null) {
							mgr.updateTaskStatus(task, TaskStatus.failed("Unable to load decryption key"));
							cRecord.setComplete(true);
							throw new RuntimeException(String.format("Failed to load keyfile from key store for job %s", cRecord.getJobid()));
						}

						final CloudOptimisationSharedCipherProvider scenarioCipherProvider = new CloudOptimisationSharedCipherProvider(keyfilev2);
						final Cipher cloudCipher = scenarioCipherProvider.getSharedCipher();

						// Target file to save into
						solutionFile[i] = new File(String.format("%s/%s-%d.xmi", basePath, cRecord.getJobid(), i));

						File pSolutionFile = solutionFile[i];
						File pTempFile = temp[i];

						if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
							try (final FileInputStream fin = new FileInputStream(pTempFile)) {
								final byte[] initialBytes;
								try {
									initialBytes = fin.readNBytes(CloudOptiDebugContants.NUM_TEMP_FILE_BYTES_TO_PRINT);
									final String initialString = new String(initialBytes);
									LOG.trace("Download Result (%s): reading solution into LiNGO. Initial characters: %s", cRecord.getJobid(), initialString);
								} catch (final IOException e) {
									LOG.trace("Download Result (%s): Failed to read solution file", cRecord.getJobid());
								} catch (final Exception e) {
									LOG.trace("Download Result (%s): Could not print initial solution file contents", cRecord.getJobid());
								}

							}
						}
						// Re-encrypt the results file.
						ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, cipherProvider -> {
							final Cipher localCipher = cipherProvider.getSharedCipher();
							try (FileOutputStream fout = new FileOutputStream(pSolutionFile)) {
								try (FileInputStream fin = new FileInputStream(pTempFile)) {
									// Data needs to be decrypted then encrypted
									try (InputStream is = cloudCipher.decrypt(fin)) {
										try (OutputStream os = localCipher.encrypt(fout)) {
											ByteStreams.copy(is, os);
										}
									}
								}
							}
						});

					}
					if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
						LOG.trace("Download Result (%s): Result re-encrypted to %s", cRecord.getJobid(), solutionFile[i].getAbsolutePath());
					}
				}

				// Nothing further for this task to do regarding upstream.
				cRecord.setComplete(true);
				deleteRecord(cRecord);

				boolean success = mgr.solutionReady(task, solutionFile);

				if (success) {
					mgr.updateTaskStatus(task, TaskStatus.complete());
				} else {
					mgr.updateTaskStatus(task, TaskStatus.failed("Solution import failed"));
				}

			} catch (final Exception e) {
				task.errorHandler.accept("Error importing solution " + e.getMessage(), e);
				mgr.updateTaskStatus(task, TaskStatus.failed(e.getMessage()));
			} finally {
				if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_DOWNLOAD)) {
					for (int i = 0; i < batchSize; ++i) {
						if (solutionFile[i] != null && solutionFile[i].exists()) {
							solutionFile[i].delete();
						}
						if (temp[i] != null && temp[i].exists()) {
							temp[i].delete();
						}
					}
				}
			}

		}

		private IGatewayResponse downloadData(final CloudOptimisationDataResultRecord rtd, final File f, int resultIdx) {
			final IGatewayResponse[] ret = new IGatewayResponse[1];
			final Job background = new Job("Downloading scenario") {

				@Override
				public IStatus run(final IProgressMonitor monitor) {
					try {
						ret[0] = client.downloadTo(rtd.getJobid(), resultIdx, f, WrappedProgressMonitor.wrapMonitor(monitor));
					} catch (final Exception e) {
						LOG.error("Unable to read/write to user id file in cloud-opti folder", e);
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
		runUpdateThread = false;
		if (updateThread != null) {
			updateThread.interrupt();
			updateThread = null;
		}
	}

	public void start() {
		createUserIdFile();

		runUpdateThread = true;
		updateThread = new Thread("CloudOptimisationUpdaterThread") {
			@Override
			public void run() {

				while (runUpdateThread) {
					updateLock.lock();
					try {
						refresh();
					} catch (final Throwable e1) {
						e1.printStackTrace();
					} finally {
						updateLock.unlock();
					}

					try {
						if (shortPoll) {
							// There is an active task, poll on a short interval
							Thread.sleep(1_000);
						} else {
							// No currently active tasks, so poll less frequently
							// .. do we need to poll at all?
							Thread.sleep(30_000);
						}
					} catch (final InterruptedException e) {
						// We can interrupt to wake up the thread or to kill it.
						if (!runUpdateThread) {
							interrupt(); // preserve interruption status
							return;
						}
					}
				}
			}

		};

		if (tasksFile.exists() && tasksFile.canRead()) {
			try {
				final String json = Files.readString(tasksFile.toPath());
				final List<CloudOptimisationDataResultRecord> tasks = CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
				if (tasks != null && !tasks.isEmpty()) {
					// Update downloaded state
					for (final var r : tasks) {
						if (r.job == null) {
							continue;
						}
						if (r.isComplete()) {
							continue;
						}
						ServiceHelper.withAllServices(IScenarioService.class, null, ss -> {
							// Really want to make sure this is the "My Scenarios" services, but local is a
							// good proxy for now.
							if (ss.getServiceModel().isLocal()) {
								// Does the UUID exist in the service?
								ScenarioInstance si = ss.getScenarioInstance(r.job.getScenarioUUID());
								if (si != null) {
									CloudJobManager.INSTANCE.resumeTask(si, r);
								}
							}
							return false;
						});
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		updateThread.start();
	}

	public void createUserIdFile() {
		String userid = "";
		if (!userIdFile.exists()) {
			final UUID userId = UUID.randomUUID();
			client.setUserId(userId.toString());
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(userIdFile))) {
				writer.write(userId.toString());
				userid = userId.toString();
			} catch (final IOException e) {
				e.printStackTrace();
				LOG.error("Unable to read/write to user id file in cloud-opti folder", e);
			}
		} else if (userIdFile.canRead()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(userIdFile))) {
				userid = reader.readLine();
				client.setUserId(userid);
			} catch (final IOException e) {
				e.printStackTrace();
				LOG.error("Unable to read/write to user id file in cloud-opti folder", e);
			}
		} else {
			LOG.error("Unable to read/write to user id file in cloud-opti folder");
		}
	}

	public synchronized void refresh() throws IOException {

		boolean changed = false;

		shortPoll = false;

		final List<CloudOptimisationDataResultRecord> newList = new LinkedList<>();
		for (final CloudOptimisationDataResultRecord r : currentRecords) {

			// Do not request an update for complete jobs
			if (r.isComplete()) {
				// Clean up
				deleteRecord(r);
				// Record will not be added to the updated list
				continue;
			}

			final ResultStatus oldStatus = r.getStatus();
			// What is the status?
			try {
				r.setStatus(ResultStatus.from(client.getJobStatus(r.getJobid()), oldStatus));
			} catch (final Exception e) {
				// Keep old status if there is some kind of exception.

				if (Platform.getDebugBoolean(CloudOptiDebugContants.DEBUG_POLL)) {
					LOG.trace(String.format("Status Result (%s): Exception getting statues %s", r.getJobid(), e.getMessage()), e);
				}
			}

			if (r.getStatus().isNotFound()) {
				// Record is not available upstream
				r.setComplete(true);
				// Clean up
				deleteRecord(r);
				continue;
			}

			// Has the upstream status changed in some way?
			if (!Objects.equals(oldStatus, r.getStatus())) {
				changed = true;

				// Check upstream status and update the task accordingly.
				ResultStatus rs = r.getStatus();
				if (rs.isSubmitted()) {
					mgr.updateTaskStatus(r.task, TaskStatus.submitted());
				} else if (rs.isRunning()) {
					mgr.updateTaskStatus(r.task, TaskStatus.running(rs.getProgress() / 100.0));
				} else if (rs.isComplete()) {
					mgr.updateTaskStatus(r.task, TaskStatus.running(1.0));
				} else if (rs.isFailed()) {
					mgr.updateTaskStatus(r.task, TaskStatus.failed(rs.getReason()));

					r.setComplete(true);
					deleteRecord(r);
					continue;
				}
			}

			if (!r.isComplete()) {
				// Result not downloaded, so mark as changed.
				changed = true;
				shortPoll = true;
			}

			newList.add(r);

			shortPoll |= r.isActive();
		}
		changed |= currentRecords.size() != newList.size();

		if (changed) {
			saveAndUpdateCurrentRecords(newList);
			runDownloadTasks(currentRecords);
		}
	}

	public synchronized void addNewlySubmittedOptimisationRecord(final CloudOptimisationDataResultRecord r) {

		currentRecords = new ImmutableList.Builder<CloudOptimisationDataResultRecord>() //
				.addAll(currentRecords) //
				.add(r) //
				.build();

		saveAndUpdateCurrentRecords(currentRecords);

		// Tell refresh job to wake up
		shortPoll = true;
		updateThread.interrupt();
	}

	public synchronized void deleteDownloaded(final Collection<String> jobIds) {
		pause();
		try {
			for (final String jobId : jobIds) {
				final CloudOptimisationDataResultRecord cRecord = getRecord(jobId);
				if (cRecord != null) {
					deleteRecord(cRecord);
				}
			}
		} finally {
			resume();
		}
	}

	public void deleteDownloaded(Task task) {
		pause();
		try {

			final List<CloudOptimisationDataResultRecord> records = currentRecords;
			if (records != null && !records.isEmpty()) {
				for (final CloudOptimisationDataResultRecord cRecord : records) {
					if (cRecord.task == task) {
						deleteRecord(cRecord);
						break;
					}
				}
			}
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
		final List<CloudOptimisationDataResultRecord> records = currentRecords;
		if (records != null && !records.isEmpty()) {
			for (final CloudOptimisationDataResultRecord record : records) {
				if (record.getJobid().equalsIgnoreCase(jobId)) {
					return record;
				}
			}
		}
		return null;
	}

	private synchronized void deleteRecord(final CloudOptimisationDataResultRecord cRecord) {

		if (currentRecords.contains(cRecord)) {
			final List<CloudOptimisationDataResultRecord> l = new LinkedList<>(currentRecords);
			while (l.remove(cRecord))
				;
			currentRecords = ImmutableList.copyOf(l);
			try {
				final String json = CloudOptimisationDataServiceClient.getJSON(currentRecords);
				Files.writeString(tasksFile.toPath(), json, StandardCharsets.UTF_8);
			} catch (final Exception e) {
				LOG.error("Error saving list of downloaded records!" + e.getMessage(), e);
			}
		}

		cleanup(basePath, cRecord.getJobid(), null);
	}

	private boolean cleanup(final File basePath, final String jobid, final File resultFile) {
		final boolean amap = deleteFile(basePath, jobid, "amap");
		final boolean kStore = deleteFile(basePath, jobid, "key.p12");

		boolean lngFile = resultFile == null;
		{
			if (resultFile != null && resultFile.exists()) {
				lngFile = resultFile.delete();
			}
		}

		return amap && lngFile && kStore;
	}

	private boolean deleteFile(final File basePath, final String jobid, final String extension) {
		boolean deleted = false;
		try {
			final File file = new File(String.format("%s/%s.%s", basePath, jobid, extension));
			if (file.exists()) {
				deleted = file.delete();
			}
		} catch (final Exception e) {
			LOG.error("Error deleting file!" + e.getMessage(), e);
		}
		return deleted;
	}

	public void pause() {
		updateLock.lock();
	}

	public void resume() {
		updateLock.unlock();
	}

	private synchronized void saveAndUpdateCurrentRecords(final List<CloudOptimisationDataResultRecord> newList) {
		try {
			final String json = CloudOptimisationDataServiceClient.getJSON(newList);
			Files.writeString(tasksFile.toPath(), json, StandardCharsets.UTF_8);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		currentRecords = ImmutableList.copyOf(newList);
	}
}
