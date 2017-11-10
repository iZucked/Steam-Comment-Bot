/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.util.StringEscaper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.util.ModelReferenceThread;

public class OptimisationJobRunner {

	private static final Logger log = LoggerFactory.getLogger(OptimisationJobRunner.class);

	public void run(@NonNull String taskName, @NonNull ScenarioInstance instance, @NonNull ModelRecord modelRecord, @Nullable BiFunction<ModelReference, LNGScenarioModel, Boolean> prepareCallback,
			@NonNull Supplier<IJobDescriptor> createJobDescriptorCallback, @Nullable TriConsumer<IJobControl, EJobState, ModelReference> jobCompletedCallback) {
		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

		// While we only keep the reference for the duration of this method call, the two current concrete implementations of IJobControl will obtain a ModelReference
		try (final ModelReference modelReference = modelRecord.aquireReference(String.format("%s:1", taskName))) {
			final EObject object = modelReference.getInstance();

			if (object instanceof LNGScenarioModel) {
				final LNGScenarioModel root = (LNGScenarioModel) object;

				final String uuid = instance.getUuid();
				// Check for existing job and return if there is one.
				{
					final IJobDescriptor job = jobManager.findJobForResource(uuid);
					if (job != null) {
						return;
					}
				}
				if (prepareCallback != null) {
					Boolean b = prepareCallback.apply(modelReference, root);
					if (Boolean.FALSE.equals(b)) {
						return;
					}
				}

				new ModelReferenceThread(taskName, modelRecord, (ref) -> {
					final ScenarioLock scenarioLock = ref.getLock();
					scenarioLock.lock();
					// Use a latch to trigger unlock in this thread
					final CountDownLatch latch = new CountDownLatch(1);

					IJobControl control = null;
					IJobDescriptor job = null;
					try {
						// create a new job
						job = createJobDescriptorCallback.get();

						boolean relaxedValidation = false;
						final ScenarioInstance scenarioInstance = modelRecord.getScenarioInstance();
						if (scenarioInstance != null) {
							relaxedValidation = "Period Scenario".equals(scenarioInstance.getName());
						}

						// New optimisation, so check there are no validation errors.
						if (!validateScenario(root, false, relaxedValidation)) {
							scenarioLock.unlock();
							return;
						}

						// Automatically clean up job when removed from manager
						jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
						control = jobManager.submitJob(job, uuid);
						// Add listener to clean up job when it finishes or has an exception.
						final IJobDescriptor finalJob = job;
						control.addListener(new IJobControlListener() {

							@Override
							public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {

								if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {

									if (jobCompletedCallback != null) {
										jobCompletedCallback.accept(jobControl, newState, ref);
									}
									latch.countDown();
									jobManager.removeJob(finalJob);
									return false;
								}
								return true;
							}

							@Override
							public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
								return true;
							}
						});
						// Start the job!
						control.prepare();
						control.start();

						latch.await();
						scenarioLock.unlock();
					} catch (final Throwable ex) {
						log.error(ex.getMessage(), ex);
						if (control != null) {
							control.cancel();
						}
						// Manual clean up incase the control listener doesn't fire
						if (job != null) {
							jobManager.removeJob(job);
						}
						// instance.setLocked(false);
						scenarioLock.unlock();

						final Display display = Display.getDefault();
						if (display != null) {
							display.asyncExec(() -> {
								final String message = StringEscaper.escapeUIString(ex.getMessage());
								MessageDialog.openError(display.getActiveShell(), "Error", "An error occured. See Error Log for more details.\n" + message);
							});
						}
					}
				}) //
						.start();
			}
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	protected boolean validateScenario(LNGScenarioModel root, boolean optimising, boolean relaxedValidation) {
		return OptimisationHelper.validateScenario(root, optimising, relaxedValidation);
	}
}
