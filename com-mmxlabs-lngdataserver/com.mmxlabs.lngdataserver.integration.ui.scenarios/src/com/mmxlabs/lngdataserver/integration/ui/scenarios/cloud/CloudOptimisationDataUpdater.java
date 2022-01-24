/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.scenario.actions.anonymisation.AnonymisationUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;

class CloudOptimisationDataUpdater {

	private final CloudOptimisationDataServiceClient client;

	private final ExecutorService taskExecutor;

	private final File basePath;
	private final File tasksFile;
	private File userIdFile;

	private final Set<String> warnedLoadFailures = new HashSet<>();

	private static final Logger LOG = LoggerFactory.getLogger(CloudOptimisationDataUpdater.class);

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	private final Consumer<CloudOptimisationDataResultRecord> readyCallback;
	private final ConcurrentMap<String, ScenarioInstance> installedRecords;

	private ImmutableList<CloudOptimisationDataResultRecord> currentRecords = ImmutableList.of();

	private boolean shortPoll = false;
	private boolean runUpdateThread = false;

	public CloudOptimisationDataUpdater(final File basePath, final CloudOptimisationDataServiceClient client, final Consumer<CloudOptimisationDataResultRecord> readyCallback) {
		this.basePath = basePath;
		this.tasksFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "tasks.json");
		this.userIdFile = new File(basePath.getAbsolutePath() + IPath.SEPARATOR + "userId.txt");
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
						if (cRecord.getStatus().isComplete()) {
							taskExecutor.execute(new DownloadTask(cRecord));
						}
					} catch (final Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	private class DownloadTask implements Runnable {
		private final CloudOptimisationDataResultRecord cRecord;

		public DownloadTask(final CloudOptimisationDataResultRecord cRecord) {
			this.cRecord = cRecord;
		}

		@Override
		public void run() {

			final boolean importResultXMI = true;

			final File f = new File(String.format("%s/%s.lingo", basePath, cRecord.getJobid()));
			if (!f.exists()) {
				try {
					f.getParentFile().mkdirs();
					final File temp = new File(String.format("%s/%s-temp.lingo", basePath, cRecord.getJobid()));
					if (!downloadData(cRecord, temp)) {
						// Failed!

						temp.delete();

						return;
					} else {

						System.out.println("Downloaded " + temp);

						final File anonymisationMap = new File(String.format("%s/%s.amap", basePath, cRecord.getJobid()));

						final File solutionFile = new File(String.format("%s/%s.xmi", basePath, cRecord.getJobid()));
						final URI solutionFileURI = URI.createFileURI(solutionFile.getAbsolutePath());

						ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
							final ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(URI.createFileURI(temp.getAbsolutePath()), true, true, true, scenarioCipherProvider);
							try (IScenarioDataProvider tempDP = modelRecord.aquireScenarioDataProvider("ScenarioStorageUtil:withExternalScenarioFromResourceURL")) {
								final LNGScenarioModel copy = tempDP.getTypedScenario(LNGScenarioModel.class);

								final EditingDomain editingDomain = tempDP.getEditingDomain();

								// De-anonymise scenario
								if (copy.isAnonymised() && anonymisationMap.exists()) {
									final CompoundCommand cmd = AnonymisationUtils.createAnonymisationCommand(copy, editingDomain, new HashSet<>(), new ArrayList<>(), false, anonymisationMap);

									if (cmd != null && !cmd.isEmpty()) {
										RunnerHelper.exec(() -> editingDomain.getCommandStack().execute(cmd), true);
									}
									// Save the result
									ScenarioStorageUtil.storeCopyToFile(tempDP, temp);
								}

								if (importResultXMI) {
									// Assume scenario was stripped first, we grab the first result depending on
									// query type.
									final AnalyticsModel m = ScenarioModelUtil.getAnalyticsModel(tempDP);
									AbstractSolutionSet result = null;
									if (cRecord.getType() != null) {
										final String type = cRecord.getType();
										switch (type) {
										case "SANDBOX" -> result = m.getOptionModels().get(0).getResults();
										case "OPTIMISATION" -> result = m.getOptimisations().get(0);
										case "OPTIONISER" -> result = m.getOptimisations().get(0);
										}
									}

									if (result != null) {
										// Replace the root model URI to a well known string for reloading later
										for (final var r : editingDomain.getResourceSet().getResources()) {
											if (r.getContents().get(0) instanceof LNGScenarioModel) {
												r.setURI(URI.createURI(CloudOptimisationConstants.ROOT_MODEL_URI));
											}
										}

										// Save the solution into it's own file. References to the root model will be
										// based on the URI set above.
										final Resource r = editingDomain.getResourceSet().createResource(solutionFileURI);
										r.getContents().add(result);
										r.save(null);
									}
								}
							}
						});
						if (importResultXMI) {

							// Look up the original scenario.
							final ScenarioInstance instanceRef = cRecord.getScenarioInstance();

							if (instanceRef != null) {
								final ScenarioModelRecord mr = SSDataManager.Instance.getModelRecord(instanceRef);
								try (IScenarioDataProvider sdp = mr.aquireScenarioDataProvider("DownloadTask:patchScenario")) {
									final AnalyticsModel am = ScenarioModelUtil.getAnalyticsModel(sdp);

									final EditingDomain ed = sdp.getEditingDomain();

									// Find the root object and create a URI mapping to the real URI so the result
									// xmi can resolve
									for (final var r : ed.getResourceSet().getResources()) {
										if (r.getContents().get(0) instanceof LNGScenarioModel) {
											ed.getResourceSet().getURIConverter().getURIMap().put(URI.createURI(CloudOptimisationConstants.ROOT_MODEL_URI), r.getURI());
										}
									}

									// Load in the result.
									final Resource rr = ed.getResourceSet().createResource(solutionFileURI);
									rr.load(null);
									final AbstractSolutionSet res = (AbstractSolutionSet) rr.getContents().get(0);

									if (cRecord.getResultName() != null && !cRecord.getResultName().isBlank()) {
										res.setName(cRecord.getResultName());
									}

									assert res != null;

									// "Resolve" the references. The result references to the main scenario may be
									// "proxy" objects
									EcoreUtil.resolveAll(ed.getResourceSet());

									// Remove the obsolete resource
									ed.getResourceSet().getResources().remove(rr);
									// Clear the mapping
									ed.getResourceSet().getURIConverter().getURIMap().remove(URI.createURI(CloudOptimisationConstants.ROOT_MODEL_URI));

									rr.getContents().clear();

									if (cRecord.getType() != null) {
										final String type = cRecord.getType();
										switch (type) {
										case "SANDBOX" -> {
											for (var om : am.getOptionModels()) {
												if (Objects.equals(cRecord.getComponentUUID(), om.getUuid())) {
													RunnerHelper.exec(() -> ed.getCommandStack().execute(SetCommand.create(ed, om, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS, res)),
															true);
													break;
												}
											}
										}
										case "OPTIMISATION" -> RunnerHelper
												.exec(() -> ed.getCommandStack().execute(AddCommand.create(ed, am, AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS, res)), true);

										case "OPTIONISER" -> RunnerHelper
												.exec(() -> ed.getCommandStack().execute(AddCommand.create(ed, am, AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS, res)), true);

										}
									}

//									IS THIS THE SAME INSTANCE?```
									System.out.println(res.getUuid());
									cRecord.setResultUUID(res.getUuid());

								}
							}
						}

						if (solutionFile.exists()) {
							solutionFile.delete();
						}

						// Move the temp file
						temp.renameTo(f);
						cRecord.setResult(f);

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

	public void stop() {
		runUpdateThread = false;
		if (updateThread != null) {
			updateThread.interrupt();
			updateThread = null;
		}
	}

	public void start() {
		createUserIdFile();

		if (tasksFile.exists() && tasksFile.canRead()) {
			try {
				final String json = Files.readString(tasksFile.toPath());
				final List<CloudOptimisationDataResultRecord> tasks = CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
				final List<CloudOptimisationDataResultRecord> obsoleteTasks = new LinkedList<>();
				if (tasks != null && !tasks.isEmpty()) {
					// Update downloaded state
					for (final var r : tasks) {
						final File lingoFile = new File(String.format("%s/%s.lingo", basePath, r.getJobid()));
						if (lingoFile.exists()) {
							r.setResult(lingoFile);
						}

						final ScenarioInstance[] instanceRef = new ScenarioInstance[1];
						ServiceHelper.withAllServices(IScenarioService.class, null, ss -> {
							// Really want to make sure this is the "My Scenarios" services, but local is a
							// good proxy for now.
							if (ss.getServiceModel().isLocal()) {
								// Does the UUID exist in the service?
								if (ss.exists(r.getUuid())) {
									final TreeIterator<EObject> itr = ss.getServiceModel().eAllContents();
									while (itr.hasNext()) {
										final EObject obj = itr.next();
										if (obj instanceof final ScenarioInstance si) {
											if (Objects.equals(r.getUuid(), si.getUuid())) {
												instanceRef[0] = si;
												return false;
											}
										}
									}
								}

							}
							return false;
						});
						if (instanceRef[0] == null) {
							obsoleteTasks.add(r);
						} else {
							r.setScenarioInstance(instanceRef[0]);
						}
					}

					tasks.removeAll(obsoleteTasks);

					currentRecords = ImmutableList.copyOf(tasks);
					runDownloadAndInstallTasks(currentRecords);

					currentRecords.forEach(this::setScenarioCloudLocked);

					// Trigger UI update
					readyCallback.accept(null);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
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
		updateThread.start();
	}

	public void createUserIdFile() {
		String userid = "";
		if (!userIdFile.exists()) {
			UUID userId = UUID.randomUUID();
			client.setUserId(userId.toString());
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(userIdFile))) {
				writer.write(userId.toString());
				userid = userId.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (userIdFile.canRead()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(userIdFile))) {
				userid = reader.readLine();
				client.setUserId(userid);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			LOG.error("Unable to read/write to user id file in cloud-opti folder");
		}
	}

	public synchronized void refresh() throws IOException {

		boolean changed = false;

		shortPoll = false;

		final List<CloudOptimisationDataResultRecord> newList = new LinkedList<>();
		for (final CloudOptimisationDataResultRecord originalR : currentRecords) {
			final CloudOptimisationDataResultRecord r = originalR;// .copy();

			final boolean oldRemote = r.isRemote();
			final ResultStatus oldStatus = r.getStatus();
			// What is the status?
			try {
				r.setStatus(ResultStatus.from(client.getJobStatus(r.getJobid()), oldStatus));
			} catch (final Exception e) {
				// Keep old status if there is some kind of exception.
			}
			// Is the record still available upstream?
			r.setRemote(r.getStatus() != null && !r.getStatus().isNotFound());

			if (oldStatus != null && !oldStatus.isComplete() && r.getStatus().isComplete()) {
				final Instant n = Instant.now();
				r.setCloudRuntime(n.toEpochMilli() - r.getCreationDate().toEpochMilli());
			}

			changed |= oldRemote != r.isRemote();
			if (!r.isActive()) {
				changed |= !Objects.equals(oldStatus, r.getStatus());
			}

			if (r.getStatus().isComplete() && r.getResult() == null) {
				// Result not downloaded, so mark as changed.
				changed = true;
				shortPoll = true;
			}

			newList.add(r);

			shortPoll |= r.isActive();

			if (r.isActive()) {
				readyCallback.accept(r);
			}
		}
		changed |= currentRecords.size() != newList.size();
		if (changed) {
			saveAndUpdateCurrentRecords(newList);
			runDownloadAndInstallTasks(currentRecords);

			currentRecords.forEach(this::setScenarioCloudLocked);

			// Trigger UI update
			readyCallback.accept(null);
		}
	}

	public synchronized void addNewlySubmittedOptimisationRecord(final CloudOptimisationDataResultRecord r) {

		currentRecords = new ImmutableList.Builder<CloudOptimisationDataResultRecord>() //
				.addAll(currentRecords) //
				.add(r) //
				.build();

		saveAndUpdateCurrentRecords(currentRecords);
		setScenarioCloudLocked(r);

		// Refresh all
		readyCallback.accept(null);

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

	private synchronized boolean deleteRecord(final CloudOptimisationDataResultRecord cRecord) {

		cRecord.setDeleted(true);
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

			boolean amap = false;
			try {
				final File anonymisationMap = new File(String.format("%s/%s.amap", basePath, cRecord.getJobid()));
				if (anonymisationMap.exists()) {
					amap = anonymisationMap.delete();
				}
			} catch (final Exception e) {
				LOG.error("Error deleting anonymisation map!" + e.getMessage(), e);
			}

			boolean lngFile = false;
			try {
				final File lingoFile = new File(String.format("%s/%s.lingo", basePath, cRecord.getJobid()));
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
			final int ii = 0;
			e.printStackTrace();
		}
		currentRecords = ImmutableList.copyOf(newList);
	}

	public synchronized void setLocalRuntime(final String jobId, final long runtime) {
		final CloudOptimisationDataResultRecord record = getRecord(jobId);
		if (record != null) {
			record.setLocalRuntime(runtime);
		}
		readyCallback.accept(record);
	}

	private void setScenarioCloudLocked(CloudOptimisationDataResultRecord cRecord) {
		// Look up the original scenario.
		final ScenarioInstance instanceRef = cRecord.getScenarioInstance();

		if (instanceRef != null) {
			if (instanceRef.isCloudLocked()) {
				if (cRecord.isDeleted()) {
					RunnerHelper.exec(() -> instanceRef.setCloudLocked(false), false);
				} else {
					if ((cRecord.getStatus().isComplete() && cRecord.getResult() == null) || cRecord.getStatus().isFailed()) {
						RunnerHelper.exec(() -> instanceRef.setCloudLocked(false), false);
					}
				}
			}
			if (cRecord.getStatus().isSubmitted() || cRecord.getStatus().isRunning()) {
				RunnerHelper.exec(() -> instanceRef.setCloudLocked(true), false);
			}
		}

	}
}
