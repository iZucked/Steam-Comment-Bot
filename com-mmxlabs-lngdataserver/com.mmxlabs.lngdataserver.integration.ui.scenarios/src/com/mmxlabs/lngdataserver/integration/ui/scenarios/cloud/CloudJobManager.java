/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.IOWrappedException;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationPushException.Type;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.ScenarioServicePushToCloudAction.KeyData;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.AbstractJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.IJobManager;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.Task;
import com.mmxlabs.models.lng.transformer.ui.jobmanagers.TaskStatus;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobRegistry;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelReferenceThread;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.ui.IProgressProvider;

/**
 * Job manager to run tasks in our AWS cloud
 * 
 */
public class CloudJobManager extends AbstractJobManager {

	private static final Logger logger = LoggerFactory.getLogger(CloudJobManager.class);

	/**
	 * Singleton instance
	 */
	public static final CloudJobManager INSTANCE = new CloudJobManager();

	/**
	 * Constructor auto-registering exported services
	 */
	private CloudJobManager() {
		final Bundle bundle = FrameworkUtil.getBundle(CloudJobManager.class);
		final BundleContext bundleContext = bundle.getBundleContext();

		bundleContext.registerService(IProgressProvider.class, this, null);
		bundleContext.registerService(IJobManager.class, this, null);
	}

	/**
	 * Sub-class of main {@link Task} to include a {@link CountDownLatch} used to retain the scenario lock until the cloud report the task has finished one way or another.
	 * 
	 */
	static class CloudTask extends Task {
		private CountDownLatch latch;
		private String jobId;
	}

	private void executeTask(final CloudTask task, final IProgressMonitor parentProgressMonitor) {

		final ScenarioModelRecord originalModelRecord = task.modelRecord;

		parentProgressMonitor.beginTask("Sending scenario", 1000);
		final SubMonitor progressMonitor = SubMonitor.convert(parentProgressMonitor, 1000);

		// Temporary files to clean up on failure or success. Successful upload with move the required temp files into final location
		File anonymisationMap = null;
		KeyData keyData = null;
		File tmpEncryptedScenarioFile = null;
		File zipToUpload = null;

		try { // Try for the progress monitor and cleanup

			// gateway connectivity check
			try {
				final String info = CloudOptimisationDataService.INSTANCE.getInfo();
				logger.info("gateway is reachable: " + info);
			} catch (final IOException e) {
				logger.error(e.getLocalizedMessage());
				throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_FAILED_STATUS_CHECK, Type.FAILED_STATUS_CHECK);
			}

			progressMonitor.subTask("Preparing scenario");
			final CloudOptimisationDataResultRecord cRecord = new CloudOptimisationDataResultRecord();
			cRecord.task = task;
			cRecord.job = task.job;

			IScenarioDataProvider copyScenarioDataProvider = null;
			LNGScenarioModel copyScenarioModel = null;

			// Create a copy of the scenario so we can strip out excess data and later anonymise it.
			try (IScenarioDataProvider originalScenarioDataProvider = originalModelRecord.aquireScenarioDataProvider("ScenarioServicePushToCloudAction:doUploadScenario")) {
				final LNGScenarioModel originalScenarioModel = originalScenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

				copyScenarioModel = EcoreUtil.copy(originalScenarioModel);

				ScenarioServicePushToCloudAction.stripScenario(task.job.getType(), copyScenarioModel, task.job.getComponentUUID());

				copyScenarioDataProvider = SimpleScenarioDataProvider.make(EcoreUtil.copy(originalModelRecord.getManifest()), copyScenarioModel);
			} catch (final RuntimeException e) {
				if (e.getCause() instanceof final ScenarioMigrationException ee) {
					throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_EVALUATING, Type.FAILED_TO_MIGRATE, ee);
				} else if (e.getCause() instanceof final RuntimeException ee) {
					throw e;
				}
				throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_EVALUATING, Type.FAILED_UNKNOWN_ERROR, e);
			}

			// Run the scenario anonymisation
			{
				try {
					final EditingDomain editingDomain = copyScenarioDataProvider.getEditingDomain();
					anonymisationMap = ScenarioServicePushToCloudAction.anonymiseScenario(UUID.randomUUID().toString(), progressMonitor, copyScenarioModel, editingDomain);
					cRecord.setAnonyMapFileName(anonymisationMap.getName());
				} catch (final IOException e) {
					logger.error("Failed to create temp anonymisation map file");
					throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_SAVING_ANOM_MAP, Type.FAILED_TO_SAVE, e);
				}
			}

			// // Try to evaluate the scenario - if this fails, then it would fail on the cloud server too.
			// try {
			// evaluateScenario(userSettings, progressMonitor, copyScenarioDataProvider);
			// } catch (final Exception e) {
			// LOG.error(e.getMessage(), e);
			// throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_EVALUATING, Type.FAILED_TO_EVALUATE);
			// }

			keyData = ScenarioServicePushToCloudAction.generateKeyPairs(progressMonitor, UUID.randomUUID().toString());
			tmpEncryptedScenarioFile = ScenarioServicePushToCloudAction.encryptScenarioWithCloudKey(progressMonitor, copyScenarioDataProvider, anonymisationMap, keyData);

			final List<Pair<String, Object>> filesToZip = new ArrayList<>(4);
			// Add the manifest entry
			filesToZip.add(Pair.of(ScenarioServicePushToCloudAction.MANIFEST_NAME,
					ScenarioServicePushToCloudAction.createManifest(ScenarioServicePushToCloudAction.MF_SCENARIO_NAME, task.job.getType(), Base64.getEncoder().encodeToString(keyData.keyUUID()))));

			// Add the scenario
			filesToZip.add(Pair.of(ScenarioServicePushToCloudAction.MF_SCENARIO_NAME, tmpEncryptedScenarioFile));

			// Add (unused) jvm options
			// filesToZip.add(Pair.of(ScenarioServicePushToCloudAction.MF_JVM_OPTS_NAME, createJVMOptions()));

			// // Add in the parameters
			filesToZip.add(Pair.of(ScenarioServicePushToCloudAction.MF_PARAMETERS_NAME, task.job.getTaskParameters()));

			// Create the zip file bundle to send to the opti-server
			try {
				zipToUpload = Files.createTempFile(ScenarioStorageUtil.getTempDirectory().toPath(), "archive_", ".zip").toFile();
				ScenarioServicePushToCloudAction.archive(zipToUpload, filesToZip);
			} catch (final IOException e) {
				logger.error(e.getMessage(), e);
				throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_SAVING, Type.FAILED_TO_SAVE, e);
			}

			progressMonitor.worked(200);
			progressMonitor.subTask("Sending optimisation");

			// UploadFile
			String response = null;
			final SubMonitor uploadMonitor = progressMonitor.split(500);
			try {
				response = CloudOptimisationDataService.INSTANCE.uploadData(zipToUpload, "checksum", originalModelRecord.getScenarioInstance().getName(), //
						WrappedProgressMonitor.wrapMonitor(uploadMonitor), keyData.encryptedSymmetricKey());
			} catch (final Exception e) {
				logger.error(e.getMessage(), e);
				throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD, e);
			} finally {
				uploadMonitor.done();
			}

			if (response == null) {
				throw new RuntimeException("Error sending the scenario for online optimisation");
			}
			final ObjectMapper mapper = new ObjectMapper();
			try {
				final JsonNode actualObj = mapper.readTree(response);
				final String jobid = actualObj.get("jobid").textValue();
				System.out.println(jobid);
				if (jobid != null) {
					cRecord.setJobid(jobid);
					anonymisationMap.renameTo(new File(ScenarioServicePushToCloudAction.CLOUD_OPTI_PATH + IPath.SEPARATOR + jobid + ".amap"));
					keyData.keyStore().renameTo(new File(ScenarioServicePushToCloudAction.CLOUD_OPTI_PATH + IPath.SEPARATOR + jobid + ".key" + ".p12"));
				} else {
					throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_UPLOADING, Type.FAILED_TO_UPLOAD, new IllegalStateException());
				}
			} catch (final IOException e) {
				throw new CloudOptimisationPushException(ScenarioServicePushToCloudAction.MSG_ERROR_UPLOADING, Type.FAILED_TO_SAVE_ENCRYPTION_KEY,
						new IllegalStateException("Unexpected error: " + e.getMessage()));
			}
			//
			// // Register the task
			cRecord.job = task.job;
			task.remoteJobID = cRecord.getJobid();
			CloudOptimisationDataService.INSTANCE.addRecord(task, cRecord);

		} finally {
			progressMonitor.done();
			ScenarioServicePushToCloudAction.cleanup(anonymisationMap, keyData, tmpEncryptedScenarioFile, zipToUpload);
		}

	}

	public void resumeTask(final ScenarioInstance scenarioInstance, final CloudOptimisationDataResultRecord cloudRecord) {

		final JobDataRecord job = cloudRecord.job;
		if (job == null) {
			return;
		}

		job.setScenarioInstance(scenarioInstance);

		final ScenarioModelRecord scenarioModelRecord = SSDataManager.Instance.getModelRecordChecked(job.getScenarioInstance());
		
		// We need a special unique thread for exclusive locking.
		final ScenarioModelReferenceThread thread = new ScenarioModelReferenceThread(job.getScenarioName(), scenarioModelRecord, sdp -> {
			final ScenarioLock scenarioLock = sdp.getModelReference().getLock();
			if (scenarioLock.tryLock(2_000)) {
				try {

					final CloudTask task = new CloudTask();
					task.runType = RunType.Cloud;
					task.job = job;
					task.modelRecord = scenarioModelRecord;
					task.sdp = sdp;
					task.successHandler = JobRegistry.INSTANCE.createTaskApply(job.getType(), job, sdp, scenarioInstance);
					task.errorHandler = (a, b) -> handleError(task, a, b);

					synchronized (localTasks) {
						localTasks.add(task);
						task.latch = new CountDownLatch(1);
					}
					// Fire a refresh as there is a new task
					fireListeners();
					cloudRecord.task = task;
					cloudRecord.job = job;
					task.remoteJobID = cloudRecord.getJobid();
					CloudOptimisationDataService.INSTANCE.addRecord(task, cloudRecord);

					// This should be in executeTask as an error could cause this to block
					task.latch.await();

					// Trigger refresh as task has finished
					fireListeners(task);

				} catch (final Throwable ex) {
					handleError(null, ex.getMessage(), ex);
				} finally {
					scenarioLock.unlock();
				}
			} else {
				// unable to get scenario lock
			}
		});

		// Start thread and return control
		thread.start();
	}

	public void submit(final String taskName, final JobDataRecord job, final CheckedFunction<IScenarioDataProvider, @Nullable String, Exception> parametersFactory,
			final ToBooleanFunction<IScenarioDataProvider> validationFactory, final BiConsumer<IScenarioDataProvider, AbstractSolutionSet> applyFunction) {

		final ScenarioModelRecord scenarioModelRecord = SSDataManager.Instance.getModelRecordChecked(job.getScenarioInstance());
		// We need a special unique thread for exclusive locking.
		final ScenarioModelReferenceThread thread = new ScenarioModelReferenceThread(taskName, scenarioModelRecord, sdp -> {
			final ScenarioLock scenarioLock = sdp.getModelReference().getLock();
			if (scenarioLock.tryLock(2_000)) {
				try {

					final String paramsJSON = parametersFactory.apply(sdp);
					if (paramsJSON == null) {
						return;
					}

					if (!validationFactory.accept(sdp)) {
						return;
					}

					job.setTaskParameters(paramsJSON);

					final CloudTask task = new CloudTask();
					task.runType = RunType.Cloud;
					task.job = job;
					task.modelRecord = scenarioModelRecord;
					task.sdp = sdp;
					task.successHandler = applyFunction;
					task.errorHandler = (a, b) -> handleError(task, a, b);

					synchronized (localTasks) {
						localTasks.add(task);
						task.latch = new CountDownLatch(1);
					}
					fireListeners();

					RunnerHelper.asyncExec(display -> {
						final ProgressMonitorDialog d = new ProgressMonitorDialog(display.getActiveShell()) {

							@Override
							protected void configureShell(final Shell shell) {
								super.configureShell(shell);
								shell.setText("Submitting cloud task");
							}
						};
						try {
							d.run(true, false, monitor -> executeTask(task, monitor));
						} catch (final Exception ex) {
							handleError(task, ex.getMessage(), ex);
						}
					});
					// This should be in executeTask as an error could cause this to block
					task.latch.await();
					fireListeners(task);

				} catch (final Throwable ex) {
					handleError(null, ex.getMessage(), ex);
				} finally {
					// this.localJobs.remove(job.getScenarioUUID());
					System.out.println("Unlocking!");
					scenarioLock.unlock();
				}
			} else {
				// unable to get scenario lock
			}
		});

		thread.start();

	}

	@Override
	protected void fireListeners(@Nullable final Task task) {
		if (task instanceof final CloudTask ct) {
			if (ct.latch != null && ct.latch.getCount() > 0) {
				if (task.taskStatus.isComplete() || task.taskStatus.isFailed() || task.taskStatus.isNotFound()) {
					ct.latch.countDown();
				}
			}
		}

		super.fireListeners(task);
	}

	@Override
	protected void handleError(final Task task, final String msg, Throwable ex) {
		// Unwrap exception cause
		if (ex instanceof final InvocationTargetException ite) {
			ex = ite.getCause();
		}

		if (ex instanceof final CloudOptimisationPushException ce) {
			reportException(ce);
			if (task != null) {
				updateTaskStatus(task, TaskStatus.failed(ex.getMessage()));
			}
		} else {
			super.handleError(task, msg, ex);
		}
		if (task instanceof final CloudTask ct && ct.latch != null) {
			ct.latch.countDown();
		}
	}

	public boolean solutionReady(final Task task, final File solutionFile) {

		// TODO - handle error better!
		// Make sure we are not stuck in a lock etc.

		final IScenarioDataProvider sdp = task.sdp;
		final EditingDomain ed = sdp.getEditingDomain();

		Resource rr = null;
		final URI rootModelURI = URI.createURI(CloudOptimisationConstants.ROOT_MODEL_URI);
		try {

			// Find the root object and create a URI mapping to the real URI so the result
			// xmi can resolve
			for (final var r : ed.getResourceSet().getResources()) {
				if (!r.getContents().isEmpty() && r.getContents().get(0) instanceof LNGScenarioModel) {
					ed.getResourceSet().getURIConverter().getURIMap().put(rootModelURI, r.getURI());
				}
			}

			// Create a resource to load the result into
			rr = ed.getResourceSet().createResource(URI.createFileURI(solutionFile.getAbsolutePath()));
			final Resource pRR = rr;
			// Load in the result.
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, cipherProvider -> {
				final Map<String, URIConverter.Cipher> options = new HashMap<>();
				options.put(Resource.OPTION_CIPHER, cipherProvider.getSharedCipher());
				pRR.load(options);
			});

			final AbstractSolutionSet res = (AbstractSolutionSet) rr.getContents().get(0);
			assert res != null;

			// "Resolve" the references. The result references to the main scenario may be
			// "proxy" objects
			EcoreUtil.resolveAll(ed.getResourceSet());

			// Remove result from it's resource
			rr.getContents().clear();

			// Link up the result
			task.job.setResultUUID(res.getUuid());

			// Fire the sucess handler which should attach result to the datamodel
			task.successHandler.accept(sdp, res);

			return true;
		} catch (final Exception e) {
			// Check for a specific issues of bad datamodel
			if (e.getCause() instanceof final IOWrappedException we) {
				if (we.getCause() instanceof final FeatureNotFoundException fnfe) {
					updateTaskStatus(task, TaskStatus.from("{ \"status\" : \"failed\", \"reason\" : \"Data model version issue\" }", null));
				}
			}
			task.errorHandler.accept(e.getMessage(), e);
		} finally {
			if (rr != null) {
				// Remove the obsolete resource
				ed.getResourceSet().getResources().remove(rr);
			}
			// Clear the mapping
			ed.getResourceSet().getURIConverter().getURIMap().remove(rootModelURI);
		}

		return false;

	}

	private void reportException(final CloudOptimisationPushException copException) {

		// if (cause instanceof final CloudOptimisationPushException copException) {
		switch (copException.getType()) {
		case FAILED_UNKNOWN_ERROR:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING,
					"Failed to send scenario with unknown error. " + copException.getCause().getMessage());
			break;
		case FAILED_NOT_PERMITTED:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_FAILED_PUSHING, "Insufficient permissions to send the scenario.");
			break;
		case FAILED_TO_MIGRATE:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING,
					"Failed to migrate the scenario to current data model version. Unable to send.");
			break;
		case FAILED_TO_EVALUATE:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING, "Failed to evaluate the scenario. Unable to send.");
			break;
		case FAILED_TO_SAVE:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING, "Failed to save the scenario to a temporary file. Unable to send.");
			break;
		case FAILED_TO_UPLOAD:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING, "Failed to upload the scenario. Unable to send.");
			break;
		case EVALUATION_FAILED:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING, "Fix the validation errors and send again.");
			break;
		case FAILED_UNSUPPORTED_VERSION:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING,
					copException.getMessage() + " Check the version in the Cloud Optimiser preference page.");
			break;
		case FAILED_STATUS_CHECK:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING,
					copException.getMessage() + ". Check your internet connectivity and verify the URL in the Cloud Optimiser preference page.");
			break;
		default:
			MessageDialog.openError(Display.getDefault().getActiveShell(), ScenarioServicePushToCloudAction.MSG_ERROR_PUSHING, "Unknown error occurred. Unable to send.");
			break;
		}
	}

	@Override
	public void cancelAll(final String scenarioUUID) {

		for (final var task : localTasks) {
			if (Objects.equals(scenarioUUID, task.job.getScenarioUUID())) {

				CloudOptimisationDataService.INSTANCE.delete(task);
				if (task instanceof final CloudTask ct) {
					updateTaskStatus(task, TaskStatus.aborted());
					if (ct.latch != null) {
						ct.latch.countDown();
					}
				}
			}
		}
	}

	@Override
	public void cancel(final Task task) {
		CloudOptimisationDataService.INSTANCE.delete(task);

		if (task instanceof final CloudTask ct) {
			updateTaskStatus(task, TaskStatus.aborted());
			if (ct.latch != null) {
				ct.latch.countDown();
			}
		}
	}

	@Override
	public void remove(final Task task) {
		CloudOptimisationDataService.INSTANCE.delete(task);

		synchronized (localTasks) {
			if (task instanceof final CloudTask ct) {
				updateTaskStatus(task, TaskStatus.aborted());
				if (ct.latch != null) {
					ct.latch.countDown();
				}
			}

			localTasks.remove(task);
		}
		fireListeners();

	}

	@Override
	public void removeAll(final String scenarioUUID) {
		synchronized (localTasks) {
			final Iterator<Task> itr = localTasks.iterator();
			while (itr.hasNext()) {
				final Task t = itr.next();
				if (Objects.equals(scenarioUUID, t.job.getScenarioUUID())) {
					itr.remove();
					CloudOptimisationDataService.INSTANCE.delete(t);
					if (t instanceof final CloudTask ct) {
						if (ct.latch != null) {
							ct.latch.countDown();
						}
					}
				}
			}
		}
		// Task removed
		fireListeners();
	}

}
