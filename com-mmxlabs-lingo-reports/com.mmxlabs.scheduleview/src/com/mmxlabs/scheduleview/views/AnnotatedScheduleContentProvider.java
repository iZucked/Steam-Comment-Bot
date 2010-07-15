package com.mmxlabs.scheduleview.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.events.IScheduledEvent;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;

public class AnnotatedScheduleContentProvider implements
		IGanttChartContentProvider {

	private Map<Object, IAnnotatedSequence> mapping = new HashMap<Object, IAnnotatedSequence>();

	@Override
	public Object[] getElements(Object inputElement) {

		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof IViewSite) {
			IViewSite site = (IViewSite)parentElement;
			ISelectionProvider selectionProvider = site.getSelectionProvider();
			if (selectionProvider == null) {
				return null;
			}
			IStructuredSelection ss = (IStructuredSelection)selectionProvider.getSelection();
			return ss.toArray();
		}
		
		if (parentElement instanceof IManagedJob) {
			
			IManagedJob job = (IManagedJob)parentElement;
			return (Object[])job.getSchedule();
		}
		
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof IAnnotatedSequence[]) {
			return (IAnnotatedSequence[]) parentElement;
		} else if (parentElement instanceof IAnnotatedSequence) {
			IAnnotatedSequence sequence = (IAnnotatedSequence) parentElement;
			List sequenceElements = sequence.getSequenceElements();

			List<Object> children = new LinkedList<Object>();
			for (Object o : sequenceElements) {

				children.add(sequence.getAnnotation(o,
						SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
				children.add(sequence.getAnnotation(o,
						SchedulerConstants.AI_idleInfo, IIdleEvent.class));
				children.add(sequence.getAnnotation(o,
						SchedulerConstants.AI_visitInfo, IPortVisitEvent.class));

				// Remove all null entries
				children.remove(null);
				children.remove(null);
				children.remove(null);
			}
			
			System.out.println(Arrays.toString(children.toArray()));
			
			return children.toArray();

			// return sequenceElements.toArray();
//		} else if (mapping.containsKey(parentElement)) {
//			IAnnotatedSequence sequence = mapping.get(parentElement);
//			List<Object> children = new ArrayList<Object>(3);
//			children.add(sequence.getAnnotation(parentElement,
//					SchedulerConstants.AI_journeyInfo, IJourneyEvent.class));
//			children.add(sequence.getAnnotation(parentElement,
//					SchedulerConstants.AI_idleInfo, IIdleEvent.class));
//			children.add(sequence.getAnnotation(parentElement,
//					SchedulerConstants.AI_visitInfo, IPortVisitEvent.class));
//
//			// Remove all null entries
//			children.remove(null);
//			children.remove(null);
//			children.remove(null);
//
//			return children.toArray();

		}

		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
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
		
		
		if (element instanceof IAnnotatedSequence) {
			return true;
//		} else if (mapping.containsKey(element)) {
//			return true;
		}

		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		mapping.clear();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		mapping.clear();
	}

	@Override
	public Calendar getElementStartTime(Object element) {

		if (element instanceof IScheduledEvent) {
			IScheduledEvent event = (IScheduledEvent) element;
			int startTime = event.getStartTime();
			// TODO: Get proper start time
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, startTime * 24);

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
			c.add(Calendar.HOUR_OF_DAY, endTime  * 24);

			return c;
		}

		return null;
	}

}
