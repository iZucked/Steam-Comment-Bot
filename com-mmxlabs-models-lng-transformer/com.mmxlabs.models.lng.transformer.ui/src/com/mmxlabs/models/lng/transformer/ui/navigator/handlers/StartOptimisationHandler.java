/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartOptimisationHandler extends AbstractOptimisationHandler {

	private static final Logger log = LoggerFactory.getLogger(StartOptimisationHandler.class);

	final boolean optimising;

	/**
	 * The constructor.
	 */
	public StartOptimisationHandler(final boolean optimising) {
		this.optimising = optimising;
	}

	public StartOptimisationHandler() {
		this(true);
	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof ScenarioInstance) {
					return evaluateScenarioInstance(jobManager, (ScenarioInstance) obj, optimising, ScenarioLock.OPTIMISER);
				}

			}
		}

		return null;
	}

	public static Object evaluateScenarioInstance(final IEclipseJobManager jobManager, final ScenarioInstance instance, final boolean optimising, final String k) {

		final IScenarioService service = instance.getScenarioService();
		if (service != null) {
			try {
				final EObject object = service.load(instance);

				if (object instanceof MMXRootObject) {
					final MMXRootObject root = (MMXRootObject) object;

					final String uuid = instance.getUuid();

					IJobDescriptor job = jobManager.findJobForResource(uuid);
					if (job == null) {
						// create a new job
						job = new LNGSchedulerJobDescriptor(instance.getName(), instance, optimising, k);
					}

					IJobControl control = jobManager.getControlForJob(job);
					// If there is a job, but it is terminated, then we need to create a new one
					if ((control != null) && ((control.getJobState() == EJobState.CANCELLED) || (control.getJobState() == EJobState.COMPLETED))) {
						jobManager.removeJob(job);
						control = null;
						job = new LNGSchedulerJobDescriptor(instance.getName(), instance, optimising, k);

					}

					if (control == null) {

						// New optimisation, so check there are no validation errors.
						if (!validateScenario(root)) {
							return null;
						}

						jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
						control = jobManager.submitJob(job, uuid);
					}

					// if (!jobManager.getSelectedJobs().contains(job)) {
					// // Clean up when job is removed from manager
					// jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
					// control = jobManager.submitJob(job, uuid);
					// }

					if (control.getJobState() == EJobState.CREATED) {
						try {
							final IJobDescriptor desc = job;
							// Add listener to unlock scenario when it stops optimising
							control.addListener(new IJobControlListener() {

								@Override
								public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {

									if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
										// instance.setLocked(false);
										instance.getLock(k).release();
										jobManager.removeJob(desc);
										return false;
									}
									return true;
								}

								@Override
								public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
									return true;
								}
							});
							// Set initial state.
							// instance.setLocked(true);
							instance.getLock(k).awaitClaim();
							control.prepare();
							control.start();
						} catch (final Throwable ex) {
							log.error(ex.getMessage(), ex);
							control.cancel();
							// instance.setLocked(false);
							instance.getLock(k).release();

							final Display display = Display.getDefault();
							if (display != null) {
								display.asyncExec(new Runnable() {

									@Override
									public void run() {
										MessageDialog.openError(display.getActiveShell(), "Error starting optimisation", ex.getMessage());
									}
								});
							}
						}
						// Resume if paused
					} else if (control.getJobState() == EJobState.PAUSED) {
						// instance.setLocked(true);
						instance.getLock(k).awaitClaim();
						control.resume();
					} else {
						// Add listener to unlock scenario when it stops optimising
						control.addListener(new IJobControlListener() {

							@Override
							public boolean jobStateChanged(final IJobControl jobControl, final EJobState oldState, final EJobState newState) {

								if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
									// instance.setLocked(false);
									instance.getLock(k).release();
									return false;
								}
								return true;
							}

							@Override
							public boolean jobProgressUpdated(final IJobControl jobControl, final int progressDelta) {
								return true;
							}
						});
						// instance.setLocked(true);
						instance.getLock(k).awaitClaim();
						control.start();
					}
				}
			} catch (final IOException e) {
				log.error(e.getMessage(), e);
			}

		}

		return null;
	}

	public static boolean validateScenario(final MMXRootObject root) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().endsWith(".base")) {
						return true;
					}
				}

				return false;
			}
		});

		final IValidationService helper = Activator.getDefault().getValidationService();
		final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(root, false);

		final IStatus status = helper.runValidation(validator, extraContext, Collections.singleton(root));

		if (status == null) {
			return false;
		}

		if (status.isOK() == false) {

			// See if this command was executed in the UI thread - if so fire up the dialog box.
			if (Display.getCurrent() != null) {

				final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getCurrent().getActiveShell(), status, status.getSeverity() != IStatus.ERROR);

				// Wait for use to press a button before continuing.
				dialog.setBlockOnOpen(true);

				if (dialog.open() == Window.CANCEL) {
					return false;
				}
			}
		}

		if (status.getSeverity() == IStatus.ERROR) {
			return false;
		}

		return true;
	}
}
