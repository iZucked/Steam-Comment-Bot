/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.eclipse.manager.impl.EclipseJobManagerAdapter;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.IJobManagerListener;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

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
	}

	public static List<Schedule> getSelectedSchedules() {
		final List<Schedule> schedules = new ArrayList<Schedule>();

		final IEclipseJobManager jobManager = Activator.getDefault().getJobManager();
		final List<IJobDescriptor> selectedJobs = jobManager.getSelectedJobs();

		for (final IJobDescriptor job : selectedJobs) {
			final IJobControl control = jobManager.getControlForJob(job);
			final Object jobContext = control.getJobOutput();

			if (jobContext instanceof MMXRootObject) {
				final MMXRootObject s = (MMXRootObject) jobContext;
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

	public static Schedule getLastScheduleFromScenario(final MMXRootObject scenario) {

		if (scenario == null) {
			return null;
		}
		
		final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		if (scheduleModel != null) {
			if (scheduleModel.getOptimisedSchedule() == null) {
				return scheduleModel.getInitialSchedule();
			} else {
				return scheduleModel.getOptimisedSchedule();
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
