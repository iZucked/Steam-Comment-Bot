/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.demo.reports;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJob.JobState;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.core.impl.LNGSchedulerJob;
import com.mmxlabs.jobmanager.ui.Activator;

public class ScheduleAdapter {

	/**
	 * Method to convert a {@link ISelection} into a {@link List} of
	 * {@link Schedule}s. This method will check the selection for actual
	 * {@link Schedule} objects, otherwise if the object implements
	 * {@link IAdaptable}, {@link IAdaptable#getAdapter(Class)} will be invoked.
	 * Otherwise the {@link Platform#getAdapterManager()} will be used to try
	 * and adapt to a {@link Schedule}. In all cases, should a {@link Schedule}
	 * fail to match, a {@link Scenario} will be tried, and if successful, the
	 * last contained {@link Schedule} will be added to the returned
	 * {@link List}
	 * 
	 * @param selection
	 * @return
	 */
	public static List<Schedule> getSchedules(final ISelection selection) {

		return getSelectedSchedules();
		//
		// if (selection == null || selection.isEmpty()) {
		// return Collections.emptyList();
		// }
		//
		// // TODO: just pull latest scenario
		//
		// if (selection instanceof IStructuredSelection) {
		// final IStructuredSelection sel = (IStructuredSelection) selection;
		// final Iterator<?> iter = sel.iterator();
		// while (iter.hasNext()) {
		// final Object o = iter.next();
		// // Try direct instanceof checks
		// if (o instanceof Schedule) {
		// schedules.add((Schedule) o);
		// } else if (o instanceof Scenario) {
		// Schedule schedule = getLastScheduleFromScenario((Scenario) o);
		// if (schedule != null) {
		// schedules.add(schedule);
		// }
		// } else {
		// // Try IAdaptable
		// if (o instanceof IAdaptable) {
		// Object adapted = ((IAdaptable) o)
		// .getAdapter(Schedule.class);
		// if (adapted != null) {
		// schedules.add((Schedule) adapted);
		// } else {
		// adapted = ((IAdaptable) o)
		// .getAdapter(Scenario.class);
		// if (adapted != null) {
		// Schedule schedule = getLastScheduleFromScenario((Scenario) adapted);
		// if (schedule != null) {
		// schedules.add(schedule);
		// }
		// }
		// }
		// } else {
		// // Finally try the platform adapter manager
		// Object adapted = Platform.getAdapterManager()
		// .loadAdapter(o,
		// Schedule.class.getCanonicalName());
		// if (adapted != null) {
		// schedules.add((Schedule) adapted);
		// } else {
		// adapted = Platform.getAdapterManager().loadAdapter(
		// o, Scenario.class.getCanonicalName());
		// if (adapted != null) {
		// Schedule schedule = getLastScheduleFromScenario((Scenario) adapted);
		// if (schedule != null) {
		// schedules.add(schedule);
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		//
		// return schedules;
	}

	public static List<Schedule> getSelectedSchedules() {
		final List<Schedule> schedules = new ArrayList<Schedule>();

		final IJobManager jobManager = Activator.getDefault().getJobManager();
		final List<IManagedJob> selectedJobs = jobManager.getSelectedJobs();
		for (final IManagedJob job : selectedJobs) {

			if (job instanceof LNGSchedulerJob) {
				final Scenario s = ((LNGSchedulerJob) job).getScenario();
				final Schedule schedule = getLastScheduleFromScenario(s);
				if (schedule != null) {
					schedules.add(schedule);
				}
			}
		}

		if (schedules.isEmpty()) {
			return null;
		}
		
		return schedules;
	}

	public static Schedule getLastScheduleFromScenario(final Scenario scenario) {

		if (scenario == null) {
			return null;
		}
		if (scenario.getScheduleModel() != null) {

			final EList<Schedule> schedules = scenario.getScheduleModel()
					.getSchedules();
			if (schedules.isEmpty() == false) {
				return schedules.get(schedules.size() - 1);
			}

		}
		return null;
	}

	/**
	 * Creates a {@link IJobManagerListener} to call
	 * {@link Viewer#setInput(Object)} when the currently selected
	 * {@link IManagedJob}s state or progress changes for jobs registered with
	 * the {@link IJobManager}. Call
	 * {@link #deregisterView(IJobManagerListener)} with the listener when it is
	 * no longer required. This method will call {@link Viewer#setInput(Object)}
	 * immediately with the current job selection.
	 * 
	 * @param c
	 * @return
	 */
	public static IJobManagerListener registerView(final Viewer c) {

		final IJobManager jobManager = Activator.getDefault().getJobManager();

		final IJobManagerListener jobManagerListener;
		final IManagedJobListener jobListener = new IManagedJobListener() {

			@Override
			public boolean jobStateChanged(final IManagedJob job,
					final JobState oldState, final JobState newState) {

				return setInput(c);
			}

			@Override
			public boolean jobProgressUpdated(final IManagedJob job,
					final int progressDelta) {

				return setInput(c);
			}
		};

		jobManagerListener = new IJobManagerListener() {

			@Override
			public void jobSelected(final IJobManager jobManager,
					final IManagedJob job, final IResource resource) {
				job.addListener(jobListener);
				setInput(c);

			}

			@Override
			public void jobRemoved(final IJobManager jobManager,
					final IManagedJob job, final IResource resource) {

			}

			@Override
			public void jobDeselected(final IJobManager jobManager,
					final IManagedJob job, final IResource resource) {
				job.removeListener(jobListener);
				setInput(c);
			}

			@Override
			public void jobAdded(final IJobManager jobManager,
					final IManagedJob job, final IResource resource) {

			}
		};

		jobManager.addJobManagerListener(jobManagerListener);

		for (final IManagedJob j : jobManager.getSelectedJobs()) {
			j.addListener(jobListener);
		}

		c.setInput(getSelectedSchedules());

		return jobManagerListener;
	}

	public static void deregisterView(final IJobManagerListener l) {
		final IJobManager jobManager = Activator.getDefault().getJobManager();
		jobManager.removeJobManagerListener(l);
	}

	private static boolean setInput(final Viewer viewer) {
		if (viewer.getControl().isDisposed()) {
			return false;
		}
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				viewer.setInput(getSelectedSchedules());
			}
		});
		return true;
	}

}
