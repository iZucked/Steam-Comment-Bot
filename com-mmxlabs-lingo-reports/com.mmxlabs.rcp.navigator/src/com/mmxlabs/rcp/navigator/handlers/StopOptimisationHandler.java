package com.mmxlabs.rcp.navigator.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;
import com.mmxlabs.rcp.navigator.scenario.ScenarioTreeNodeClass;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StopOptimisationHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public StopOptimisationHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final String id = event.getCommand().getId();

		final IJobManager jmv = Activator.getDefault().getJobManager();

		final ISelection selection = HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			if (id.equals("com.mmxlabs.rcp.navigator.commands.optimisation.terminate")) {

				final Iterator<?> itr = strucSelection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof ScenarioTreeNodeClass) {

						final ScenarioTreeNodeClass node = (ScenarioTreeNodeClass) obj;
						IManagedJob job = node.getJob();

						if (job != null) {
							final JobState jobState = job.getJobState();
							if (!(jobState == JobState.CANCELLED
									|| jobState == JobState.CANCELLING || jobState == JobState.COMPLETED)) {
								job.cancel();
							}
						}
					}
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

		final ISelection selection = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService()
				.getSelection();

		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection strucSelection = (IStructuredSelection) selection;

			// if
			// (id.equals("com.mmxlabs.rcp.navigator.commands.optimisation.play"))
			// {
			final Iterator<?> itr = strucSelection.iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				if (obj instanceof ScenarioTreeNodeClass) {

					final ScenarioTreeNodeClass node = (ScenarioTreeNodeClass) obj;
					final IManagedJob job = node.getJob();

					if (job == null) {
						return false;
					}

					final JobState jobState = job.getJobState();
					return (!(jobState == JobState.CANCELLED
							|| jobState == JobState.CANCELLING || jobState == JobState.COMPLETED));
				}
			}
		}

		return false;
	}
}
