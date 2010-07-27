package com.mmxlabs.scheduleview.views;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.optimiser.IAnnotatedSequence;
import com.mmxlabs.optimiser.IAnnotatedSolution;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.events.IScheduledEvent;

public class AnnotatedScheduleContentProvider implements
		IGanttChartContentProvider {

	private IAnnotatedSolution<ISequenceElement> solution;

	@Override
	public Object[] getElements(Object inputElement) {

		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof IAnnotatedSolution) {
			IAnnotatedSolution solution = (IAnnotatedSolution) parentElement;

			return solution.getContext().getOptimisationData().getResources().toArray();
		} else if (parentElement instanceof IResource) {
			IResource resource = (IResource)parentElement;
			ISequence sequence = solution.getSequences().getSequence(resource);
			IAnnotatedSequence annotatedSequence = solution.getAnnotatedSequence(resource);

			List<Object> children = new LinkedList<Object>();
			for (Object o : sequence) {

				children.add(annotatedSequence.getAnnotation(o,
						SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
				children.add(annotatedSequence.getAnnotation(o,
						SchedulerConstants.AI_idleInfo, IIdleEvent.class));
				children.add(annotatedSequence.getAnnotation(o,
						SchedulerConstants.AI_visitInfo, IPortVisitEvent.class));

				// Remove all null entries
				children.remove(null);
				children.remove(null);
				children.remove(null);
			}

			return children.toArray();
		}

		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof IViewSite) {
			return true;
		}

		if (element instanceof IManagedJob) {

			return true;
		}

		if (element instanceof IResource) {
			return true;
			}
		
		if (element instanceof IAnnotatedSequence) {
			return true;
			// } else if (mapping.containsKey(element)) {
			// return true;
		}

		return false;
	}

	@Override
	public void dispose() {
		solution = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		solution = null;

		if (newInput instanceof IManagedJob) {
			IManagedJob job = (IManagedJob) newInput;
			solution = job.getSchedule();
		} else if (newInput instanceof IAnnotatedSolution) {
			solution = (IAnnotatedSolution<ISequenceElement>) newInput;
		}
	}

	@Override
	public Calendar getElementStartTime(Object element) {

		if (element instanceof IScheduledEvent) {
			IScheduledEvent event = (IScheduledEvent) element;
			int startTime = event.getStartTime();
			// TODO: Get proper start time
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, startTime);

			return c;
		}

		return null;
	}

	@Override
	public Calendar getElementEndTime(Object element) {

		if (element instanceof IScheduledEvent) {
			IScheduledEvent event = (IScheduledEvent) element;
			int endTime = event.getEndTime();
			// TODO: Get proper end time
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, endTime);

			return c;
		}

		return null;
	}

}
