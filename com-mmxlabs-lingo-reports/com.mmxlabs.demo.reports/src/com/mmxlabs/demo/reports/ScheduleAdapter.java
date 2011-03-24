package com.mmxlabs.demo.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import scenario.Scenario;
import scenario.schedule.Schedule;

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
		if (selection.isEmpty()) {
			return Collections.emptyList();
		}

		final List<Schedule> schedules = new ArrayList<Schedule>();

		// TODO: just pull latest scenario

		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection sel = (IStructuredSelection) selection;
			final Iterator<?> iter = sel.iterator();
			while (iter.hasNext()) {
				final Object o = iter.next();
				// Try direct instanceof checks
				if (o instanceof Schedule) {
					schedules.add((Schedule) o);
				} else if (o instanceof Scenario) {
					Schedule schedule = getLastScheduleFromScenario((Scenario) o);
					if (schedule != null) {
						schedules.add(schedule);
					}
				} else {
					// Try IAdaptable
					if (o instanceof IAdaptable) {
						Object adapted = ((IAdaptable) o)
								.getAdapter(Schedule.class);
						if (adapted != null) {
							schedules.add((Schedule) adapted);
						} else {
							adapted = ((IAdaptable) o)
									.getAdapter(Scenario.class);
							if (adapted != null) {
								Schedule schedule = getLastScheduleFromScenario((Scenario) adapted);
								if (schedule != null) {
									schedules.add(schedule);
								}
							}
						}
					} else {
						// Finally try the platform adapter manager
						Object adapted = Platform.getAdapterManager()
								.loadAdapter(o,
										Schedule.class.getCanonicalName());
						if (adapted != null) {
							schedules.add((Schedule) adapted);
						} else {
							adapted = Platform.getAdapterManager().loadAdapter(
									o, Scenario.class.getCanonicalName());
							if (adapted != null) {
								Schedule schedule = getLastScheduleFromScenario((Scenario) adapted);
								if (schedule != null) {
									schedules.add(schedule);
								}
							}
						}
					}
				}
			}
		}

		return schedules;
	}

	public static Schedule getLastScheduleFromScenario(Scenario scenario) {

		if (scenario == null) {
			return null;
		}
		if (scenario.getScheduleModel() != null) {

			EList<Schedule> schedules = scenario.getScheduleModel()
					.getSchedules();
			if (schedules.isEmpty() == false) {
				return schedules.get(schedules.size() - 1);
			}

		}
		return null;
	}
}
