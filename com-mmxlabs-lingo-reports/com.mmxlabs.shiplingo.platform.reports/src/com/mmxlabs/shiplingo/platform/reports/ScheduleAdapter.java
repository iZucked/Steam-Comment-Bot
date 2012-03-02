/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.eclipse.manager.impl.EclipseJobManagerAdapter;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;
import com.mmxlabs.lngscheduler.ui.SerializableScenario;

public class ScheduleAdapter {

	/**
	 * Method to convert a {@link ISelection} into a {@link List} of {@link Schedule}s. This method will check the selection for actual {@link Schedule} objects, otherwise if the object implements
	 * {@link IAdaptable}, {@link IAdaptable#getAdapter(Class)} will be invoked. Otherwise the {@link Platform#getAdapterManager()} will be used to try and adapt to a {@link Schedule}. In all cases,
	 * should a {@link Schedule} fail to match, a {@link Scenario} will be tried, and if successful, the last contained {@link Schedule} will be added to the returned {@link List}
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

		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
		final List<IJobDescriptor> selectedJobs = jobManager.getSelectedJobs();

		for (final IJobDescriptor job : selectedJobs) {

			// if (job instanceof LNGSchedulerJob) {
			// // false
			// // Need to do something here with separate descriptor/control
			// final Scenario s = ((LNGSchedulerJob) job).getScenario();

			// this does not work; the job copies the scenario when it starts, rather than modifying the original.
			// What is the intended behavior here? Should the job modify the original?
			// Shouldn't this thing be able to ask the descriptor for some current state to display rather than
			// messing around with the context?

			final IJobControl control = jobManager.getControlForJob(job);
			final Object jobContext = control.getJobOutput();

			if (jobContext instanceof SerializableScenario) {
				final Scenario s = ((SerializableScenario) jobContext).scenario;
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

			final EList<Schedule> schedules = scenario.getScheduleModel().getSchedules();
			if (schedules.isEmpty() == false) {
				return schedules.get(schedules.size() - 1);
			}

		}
		return null;
	}

	/**
	 * Creates a {@link IJobManagerListener} to call {@link Viewer#setInput(Object)} when the currently selected {@link IManagedJob}s state or progress changes for jobs registered with the
	 * {@link IJobManager}. Call {@link #deregisterView(IJobManagerListener)} with the listener when it is no longer required. This method will call {@link Viewer#setInput(Object)} immediately with
	 * the current job selection.
	 * 
	 * @param c
	 * @return
	 */
	public static IEclipseJobManagerListener registerView(final Viewer c) {

		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();

		final IEclipseJobManagerListener jobManagerListener;

		final IJobControlListener jobListener = new IJobControlListener() {

			@Override
			public boolean jobStateChanged(final IJobControl control, final EJobState oldState, final EJobState newState) {

				return setInput(c);
			}

			@Override
			public boolean jobProgressUpdated(final IJobControl control, final int progressDelta) {

				return setInput(c);
			}
		};

		jobManagerListener = new EclipseJobManagerAdapter() {

			@Override
			public void jobSelected(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
				control.addListener(jobListener);
				setInput(c);

			}

			@Override
			public void jobDeselected(final IEclipseJobManager jobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
				control.removeListener(jobListener);
				setInput(c);
			}
		};

		jobManager.addEclipseJobManagerListener(jobManagerListener);

		for (final IJobDescriptor j : jobManager.getSelectedJobs()) {
			final IJobControl control = jobManager.getControlForJob(j);
			control.addListener(jobListener);
		}

		c.setInput(getSelectedSchedules());

		return jobManagerListener;
	}

	public static void deregisterView(final IEclipseJobManagerListener l) {
		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
		jobManager.removeEclipseJobManagerListener(l);
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
