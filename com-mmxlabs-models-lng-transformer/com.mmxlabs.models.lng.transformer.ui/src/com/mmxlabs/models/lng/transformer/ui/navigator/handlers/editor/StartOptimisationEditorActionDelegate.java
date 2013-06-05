/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
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
import com.mmxlabs.models.ui.validation.ValidationHelper;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * A {@link IEditorActionDelegate} implementation to start or resume an optimisation.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartOptimisationEditorActionDelegate extends AbstractOptimisationEditorActionDelegate {

	private static final Logger log = LoggerFactory.getLogger(StartOptimisationEditorActionDelegate.class);

	protected final boolean optimising;

	/**
	 * The constructor.
	 */
	public StartOptimisationEditorActionDelegate(final boolean optimising) {
		this.optimising = optimising;
	}

	public StartOptimisationEditorActionDelegate() {
		this(true);
	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */

	@Override
	public void run(final IAction methodAction) {

		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				evaluateScenarioInstance(jobManager, scenarioServiceEditorInput.getScenarioInstance(), optimising, ScenarioLock.OPTIMISER);
			}
		}
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
								public boolean jobStateChanged(final IJobControl methodJob, final EJobState oldState, final EJobState newState) {

									if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
										// instance.setLocked(false);
										instance.getLock(k).release();
										jobManager.removeJob(desc);
										return false;
									}
									return true;
								}

								@Override
								public boolean jobProgressUpdated(final IJobControl methodJob, final int progressDelta) {
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
							public boolean jobStateChanged(final IJobControl methodJob, final EJobState oldState, final EJobState newState) {

								if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
									// instance.setLocked(false);
									instance.getLock(k).release();
									return false;
								}
								return true;
							}

							@Override
							public boolean jobProgressUpdated(final IJobControl methodJob, final int progressDelta) {
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

		final ValidationHelper helper = new ValidationHelper();
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

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {

		super.setActiveEditor(action, targetEditor);

		final boolean enabled = false;
		if (action != null && targetEditor != null && targetEditor.getEditorInput() instanceof IScenarioServiceEditorInput) {
			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

			final IScenarioServiceEditorInput iScenarioServiceEditorInput = (IScenarioServiceEditorInput) targetEditor.getEditorInput();

			final ScenarioInstance instance = iScenarioServiceEditorInput.getScenarioInstance();
			final Object object = instance.getInstance();
			if (object instanceof MMXRootObject) {
				final MMXRootObject root = (MMXRootObject) object;
				final String uuid = instance.getUuid();

				final IJobDescriptor job = jobManager.findJobForResource(uuid);
				if (job == null) {
					action.setEnabled(true);
					return;
				}

				final IJobControl control = jobManager.getControlForJob(job);
				if (control != null) {
					stateChanged(control, EJobState.UNKNOWN, control.getJobState());
					return;
				} else {

					// New optimisation, so check there are no validation errors.
					if (!validateScenario(root)) {
						action.setEnabled(false);
						return;
					}

				}

			}
		}
		if (action != null) {
			action.setEnabled(enabled);
		}
	}

	@Override
	protected void stateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {

		boolean enabled = false;
		switch (newState) {
		case CANCELLED:
			enabled = true;
			break;
		case CANCELLING:
			break;
		case COMPLETED:
			enabled = true;
			break;
		case CREATED:
			enabled = true;
			break;
		case INITIALISED:
			enabled = true;
			break;
		case INITIALISING:
			break;
		case PAUSED:
			enabled = true;
			break;
		case PAUSING:
			break;
		case RESUMING:
			break;
		case RUNNING:
			break;
		case UNKNOWN:
			break;

		}
		if (action != null) {
			action.setEnabled(enabled);
		}
	}
}
