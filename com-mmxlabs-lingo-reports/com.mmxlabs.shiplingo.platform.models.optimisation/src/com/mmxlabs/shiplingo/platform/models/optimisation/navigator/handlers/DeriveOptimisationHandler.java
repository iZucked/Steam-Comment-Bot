/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.shiplingo.platform.models.optimisation.Activator;
import com.mmxlabs.shiplingo.platform.models.optimisation.SaveJobUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class DeriveOptimisationHandler extends AbstractOptimisationHandler {

	private final static Logger log = LoggerFactory.getLogger(DeriveOptimisationHandler.class);
	
	/**
	 * The constructor.
	 */
	public DeriveOptimisationHandler() {

	}

	/**
	 * the command has been executed, so extract extract the needed information from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

		if ((selection != null) && (selection instanceof IStructuredSelection)) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final JointModel jm = (JointModel) resource.getAdapter(JointModel.class);
					final MMXRootObject root = jm.getRootObject();
					final IPath newPath = SaveJobUtil.saveRootObject(root, "scn", resource);
					final IResource newResource = ResourcesPlugin.getWorkspace().getRoot().getFile(newPath);
					final JointModel njm = (JointModel) newResource.getAdapter(JointModel.class);
					// process njm
					final InputModel input = njm.getRootObject().getSubModel(InputModel.class);
					final ScheduleModel schedule = njm.getRootObject().getSubModel(ScheduleModel.class);
					if (input != null && schedule != null) {
						final Schedule opt = schedule.getOptimisedSchedule();
						if (opt != null) {
							input.getAssignments().clear();
							for (final Sequence sequence : opt.getSequences()) {
								final Assignment a = InputFactory.eINSTANCE.createAssignment();
								input.getAssignments().add(a);
								
								if (sequence.isSetVessel()) {
									a.getVessels().add(sequence.getVessel());
								} else if (sequence.isSetVesselClass()) {
									a.getVessels().add(sequence.getVesselClass());
									a.setAssignToSpot(true);
								}
								
								for (final Event e : sequence.getEvents()) {
									if (e instanceof SlotVisit) {
										a.getAssignedObjects().add(((SlotVisit) e).getSlotAllocation().getSlot());
									} else if (e instanceof VesselEventVisit) {
										a.getAssignedObjects().add(((VesselEventVisit) e).getVesselEvent());
									}
								}
							}
						}
					}
					njm.consolidate();
				}
			}
		}

		// Update button state?

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

			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof IResource) {
					final IResource resource = (IResource) obj;
					final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
					final IJobDescriptor job = jobManager.findJobForResource(resource);
					final IJobControl control = jobManager.getControlForJob(job);

					return (control != null);

				}
			}
		}

		return false;
	}
}
