/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationHelper;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;
import com.mmxlabs.shiplingo.platform.models.optimisation.LNGSchedulerJobDescriptor;

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
					return evaluateScenarioInstance(jobManager, (ScenarioInstance) obj, optimising);
				}

			}
		}

		return null;
	}

	public static Object evaluateScenarioInstance(final IEclipseJobManager jobManager, final ScenarioInstance instance, final boolean optimising) {

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
						job = new LNGSchedulerJobDescriptor(instance.getName(), root, optimising);
					}

					IJobControl control = jobManager.getControlForJob(job);
					// If there is a job, but it is terminated, then we need to create a new one
					if ((control != null) && ((control.getJobState() == EJobState.CANCELLED) || (control.getJobState() == EJobState.COMPLETED))) {
						jobManager.removeJob(job);
						control = null;
						job = new LNGSchedulerJobDescriptor(instance.getName(), root, optimising);

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

							// Add listener to unlock scenario when it stops optimising
							control.addListener(new IJobControlListener() {

								@Override
								public boolean jobStateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {

									if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
										instance.setLocked(false);
										return false;
									}
									return true;
								}

								@Override
								public boolean jobProgressUpdated(final IJobControl job, final int progressDelta) {
									return true;
								}
							});
							// Set initial state.
							instance.setLocked(true);
							control.prepare();
							control.start();
						} catch (final Exception ex) {
							log.error(ex.getMessage(), ex);
							control.cancel();
						}
						// Resume if paused
					} else if (control.getJobState() == EJobState.PAUSED) {
						control.resume();
						instance.setLocked(true);
					} else {
						// Add listener to unlock scenario when it stops optimising
						control.addListener(new IJobControlListener() {

							@Override
							public boolean jobStateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {

								if (newState == EJobState.CANCELLED || newState == EJobState.COMPLETED) {
									instance.setLocked(false);
									return false;
								}
								return true;
							}

							@Override
							public boolean jobProgressUpdated(final IJobControl job, final int progressDelta) {
								return true;
							}
						});
						instance.setLocked(true);
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

		final ValidationHelper helper = new ValidationHelper();
		final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(root);
		final Collection<EObject> modelRoots = new LinkedList<EObject>();
		for (final MMXSubModel subModel : root.getSubModels()) {
			modelRoots.add(subModel.getSubModelInstance());
		}

		final IStatus status = helper.runValidation(validator, extraContext, modelRoots);

		if (status.isOK() == false) {

			// See if this command was executed in the UI thread - if so fire up the dialog box.
			if (Display.getCurrent() != null) {

				final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getCurrent().getActiveShell(), status);

				// Wait for use to press a button before continuing.
				dialog.setBlockOnOpen(true);

				if (dialog.open() == Dialog.CANCEL) {
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
