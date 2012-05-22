package com.mmxlabs.models.lng.input.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class InputEditor extends Canvas {
	private final List<Object> tasks = new ArrayList<Object>();
	private final List<Object> unallocatedTasks = new ArrayList<Object>();
	private final List<Object> resources = new ArrayList<Object>();
	private final Map<Object, List<Object>> assignments = new HashMap<Object, List<Object>>();
	private final Comparator<Object> startDateComparator = new Comparator<Object>() {
		@Override
		public int compare(Object o1, Object o2) {
			return timingProvider.getStartDate(o1).compareTo(timingProvider.getStartDate(o2));
		}
	};
	private Date minDate;
	private ILabelProvider labelProvider;
	private ITimingProvider timingProvider;
	
	public InputEditor(Composite parent, int style) {
		super(parent, style);
		
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				InputEditor.this.paintControl(e);
			}
		});
	}

	protected void paintControl(final PaintEvent e) {
		int topOfCurrentRow = 0;
		
		int leftOffset = 0;
		
		// now paint all allocated tasks
		for (final Object resource : resources) {
			final List<Object> assignment = assignments.get(resource);
			topOfCurrentRow += paintRow(topOfCurrentRow, leftOffset, assignment);
		}
	}
	
	protected int paintRow(final int topOffset, final int leftOffset, final List<Object> objects) {
		if (objects == null) return 32;
		
		int maxRowDepth = 0;
		
		
		for (final Object o : objects) {
			final Date start = timingProvider.getStartDate(o);
			final Date end = timingProvider.getEndDate(o);
		}
		
		return 0;
	}

	private void prepare() {
		minDate = null;
		
		for (final Object task : tasks) {
			final Date tStart = timingProvider.getStartDate(task);
			if (minDate == null || minDate.after(tStart)) minDate = tStart;
		}
		
		final HashSet<Object> unallocated = new HashSet<Object>();
		unallocated.addAll(tasks);
		
		for (final List<Object> assignment : assignments.values()) {
			unallocated.removeAll(assignment);
			Collections.sort(assignment, startDateComparator);
		}
		
		unallocatedTasks.addAll(unallocated);
		Collections.sort(unallocatedTasks, startDateComparator);
	}
	
	public void setResources(final List<Object> resources) {
		this.resources.clear();
		this.resources.addAll(resources);
	}
	
	public void setTasks(final List<Object> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
	}
	
	public void setAssignments(final Map<Object, List<Object>> assignments) {
		this.assignments.clear();
		this.assignments.putAll(assignments);
	}
	
	public void setLabelProvider(final ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}
	
	public void setTimingProvider(final ITimingProvider timingProvider) {
		this.timingProvider = timingProvider;
	}
}
