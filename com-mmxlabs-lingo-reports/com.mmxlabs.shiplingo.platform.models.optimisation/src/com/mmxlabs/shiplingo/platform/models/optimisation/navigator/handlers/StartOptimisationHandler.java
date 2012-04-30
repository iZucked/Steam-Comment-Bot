/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.impl.DisposeOnRemoveEclipseListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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
	final boolean optimising;
	/**
	 * The constructor.
	 */
	public StartOptimisationHandler(boolean optimising) {
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
					return evaluateScenarioInstance(jobManager, (ScenarioInstance) obj);
				}
				
			}
		}

		return null;
	}

	public Object evaluateScenarioInstance(final IEclipseJobManager jobManager, final ScenarioInstance instance) {
//		final IStatus status = (IStatus) resource.getAdapter(IStatus.class);
//		if (status.matches(IStatus.ERROR)) {
//			Platform.getLog(Activator.getDefault().getBundle()).log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Validation errors were found in the resource " + resource.getName()));
//			final MMXRootObject root = (MMXRootObject) resource.getAdapter(MMXRootObject.class);
//			final ScheduleModel scheduleModel = root.getSubModel(ScheduleModel.class);
//			if (scheduleModel != null) {
//				// no solution, so clear state.
//				scheduleModel.setInitialSchedule(null);
//				scheduleModel.setOptimisedSchedule(null);
//				scheduleModel.setDirty(false);
//			}
//			try {
//				final ResourceSet rs = root.eResource().getResourceSet();
//				// temporarily mess with joint model resource set's uri converter
//				// so that all of the URIs appear to be the same, for marker util.
//				
//				// this may need improving later.
//				final URIConverter temp = rs.getURIConverter();
//				rs.setURIConverter(new ExtensibleURIConverterImpl() {
//					@Override
//					public URI normalize(URI uri) {
//						return URI.createPlatformResourceURI(resource.getFullPath().toString(), true);
//					}
//				});
//				MarkerUtil.updateMarkers(status);
//				
//				rs.setURIConverter(temp);
//				Display.getDefault().asyncExec(
//						new Runnable() {
//							@Override
//							public void run() {
//								try {
//									PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.ProblemView" // TODO find where this lives
//											, null, IWorkbenchPage.VIEW_VISIBLE);
//								} catch (final PartInitException e) {}
//							}							
//						});
//			} catch (final Throwable e) {
//				Platform.getLog(Activator.getDefault().getBundle()).log(
//						new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An error occurred when creating validtion markers for an invalid scenario", e));
//				
//			}
//
//			return null;
//		} else {
		
		final IScenarioService service = (IScenarioService) instance.getAdapters().get(IScenarioService.class);
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
						jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
						control = jobManager.submitJob(job, uuid);
					}
					
//					if (!jobManager.getSelectedJobs().contains(job)) {
//						// Clean up when job is removed from manager
//						jobManager.addEclipseJobManagerListener(new DisposeOnRemoveEclipseListener(job));
//						control = jobManager.submitJob(job, uuid);
//					}
					
					if (control.getJobState() == EJobState.CREATED) {
						try {
							control.prepare();
							control.start();
						} catch (final Exception ex) {
							Platform.getLog(Activator.getDefault().getBundle()).log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, "An error ocurred starting the optimisation", ex));
							control.cancel();
						}
						// Resume if paused
					} else if (control.getJobState() == EJobState.PAUSED) {
						control.resume();
					} else {
						control.start();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
//			final ResourceSet rs = root.eResource().getResourceSet();
			// temporarily mess with joint model resource set's uri converter
			// so that all of the URIs appear to be the same, for marker util.
//			
//			// this may need improving later.
//			final URIConverter temp = rs.getURIConverter();
//			rs.setURIConverter(new ExtensibleURIConverterImpl() {
//				@Override
//				public URI normalize(URI uri) {
//					return URI.createPlatformResourceURI(resource.getFullPath().toString(), true);
//				}
//			});
//			try {
//				MarkerUtil.updateMarkers(status);
//			} catch (CoreException e) {
//			}
//			
//			rs.setURIConverter(temp);
//		}


		return null;
	}

	@Override
	public boolean isEnabled() {

		// We could do some of this in plugin.xml - but not been able to
		// configure it properly.
		// Plugin.xml will make it enabled if the resource can be a Scenario.
		// But need finer grained control depending on optimisation state.
		if (!super.isEnabled()) {
			return false;
		}

		final ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();

				if (obj instanceof ScenarioInstance) {
					final ScenarioInstance instance = (ScenarioInstance) obj;

					final IJobDescriptor job = jobManager.findJobForResource(instance.getUuid());
					final IJobControl control = jobManager.getControlForJob(job);

					if (control == null) {
						return true;
					}

					return ((control.getJobState() != EJobState.RUNNING) && (control.getJobState() != EJobState.CANCELLING));
				}
			}
		}

		return false;
	}
}
