package com.mmxlabs.models.lng.input.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.management.timer.Timer;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class AssignmentEditor<R, T> extends Canvas {
	private static final int EMPTY_ROW_HEIGHT = 64;
	private static final int TASK_HEIGHT = 24;
	private static final int VERTICAL_SPACE_BETWEEN_TASKS = 8;
	/**
	 * Sets the time for one pixel of horizontal scale
	 */
	private static final long SCALE_FACTOR = Timer.ONE_HOUR * 6;
	private static final int MIN_WIDTH = 16;
	
	private final List<T> tasks = new ArrayList<T>();
	private final List<T> unallocatedTasks = new ArrayList<T>();
	private final List<R> resources = new ArrayList<R>();
	
	private boolean coloursSet = false;
	
	Color selectedTaskGradientTop;
	Color selectedTaskGradientBottom;
	Color taskGradientTop;
	Color taskGradientBottom;
	Color backgroundColor;
	Color taskLabelTextColor;
	
	/**
	 * Maps from screen coordinates to tasks
	 */
	private final Map<Rectangle, T> tasksByLocation = new HashMap<Rectangle, T>();
	
	/**
	 * Inverse of {@link #tasksByLocation}; gives the screen coordinates where each task is drawn
	 */
	private final Map<T, Rectangle> locationsByTask = new HashMap<T, Rectangle>();
	/**
	 * Maps from upper y-coordinate to resource
	 */
	private final TreeMap<Integer, R> resourceByY = new TreeMap<Integer, R>();
	
	private final Map<T, R> resourceByTask = new HashMap<T, R>();
	
	/**
	 * If a drag is in progress, this is the selected task.
	 */
	private T selectedTask = null;
	
	private int selectedTaskDragY = 0;
	
	private int selectedTaskInternalY = 0;
	
	private int minWidth, minHeight;
	
	private final Comparator<T> startDateComparator = new Comparator<T>() {
		@Override
		public int compare(T o1, T o2) {
			return timingProvider.getStartDate(o1).compareTo(timingProvider.getStartDate(o2));
		}
	};
	
	private Date minDate;
	private ILabelProvider labelProvider;
	private ITimingProvider<T> timingProvider;
	
	public AssignmentEditor(Composite parent, int style) {
		super(parent, style);
		
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				AssignmentEditor.this.paintControl(e);
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				AssignmentEditor.this.mouseDown(e);
			}

			@Override
			public void mouseUp(MouseEvent e) {
				AssignmentEditor.this.mouseUp(e);
			}
		});
		
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				AssignmentEditor.this.mouseMove(e);
			}
		});
	}
	
	protected void mouseMove(final MouseEvent e) {
		if (selectedTask != null) {
			setToolTipText("");
			if (findInsertionPoint(e.x, e.y, selectedTask)
					|| selectedTaskDragY != e.y) {
				selectedTaskDragY = e.y;
				redraw();
			}
		} else {
			final T task = findTaskAtCoordinates(e.x, e.y);
			if (task != null) {
				setToolTipText(labelProvider.getText(task));
			} else {
				setToolTipText("");
			}
		}
		
	}

	protected void mouseDown(MouseEvent e) {
		final T task = findTaskAtCoordinates(e.x, e.y);
		if (task != null) {
			selectedTask = task;
			final Rectangle location = locationsByTask.get(task);
			selectedTaskInternalY = e.y - location.y;
			selectedTaskDragY = e.y;
			redraw();
		}
	}

	public void update() {
		prepared = false;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				redraw();
			}			
		});
	}
	
	protected void mouseUp(MouseEvent e) {
		// perform drop?
		if (insertResource != null) {
			if (insertAfter != null) {
				if (selectedTask != null) {
					if (insertAfter != selectedTask) {
						notifyDrop();
					}
				}
			} else {
				notifyDrop();
			}
		} else if (selectedTask != null) {
			notifyRemoved();
		}
		insertAfter = null;
		insertBefore = null;
		insertResource = null;
		selectedTask = null;
		redraw();
	}
	
	private void notifyRemoved() {
		if (selectedTask != null && resourceByTask.get(selectedTask) != null) {
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskUnassigned(selectedTask, resourceByTask.get(selectedTask));
			}
		}
	}

	private void notifyDrop() {
		if (selectedTask != null && insertResource != null) {
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskReassigned(selectedTask, insertBefore, insertAfter, resourceByTask.get(selectedTask), insertResource);
			}
		}
	}

	T insertAfter;
	T insertBefore;
	R insertResource;
	private Color resourceLabelTextColor;
	private Color dividerColor;
	
	/**
	 * Finds the tasks before and after the given task if it were dropped at the given y coordinate
	 * 
	 * Results are stored in {@link #insertAfter}, {@link #insertBefore} and {@link #insertResource}
	 * 
	 * @return whether the point has changed, and so needs a redraw
	 */
	protected boolean findInsertionPoint(final int xCoordinate, final int yCoordinate, final T task) {
		T insertAfter = null;
		T insertBefore = null;
		R insertResource = null;
		
		// find resource at y-coordinate
		Entry<Integer, R> floorEntry = resourceByY.floorEntry(yCoordinate);
		if (floorEntry != null) {
			// there is a resource, so get that
			insertResource = floorEntry.getValue();
			// now try and find which tasks on that resource overlap with suitable y-coordinate
			final List<T> tasks = assignmentProvider.getAssignedObjects(insertResource);
			final Rectangle currentSelectionLocation = locationsByTask.get(task);
			
			final int xMin = currentSelectionLocation.x;
			final int xMax = xMin + currentSelectionLocation.width;
			int x = xCoordinate;
			
			if (x < xMin) x = xMin;
			else if (x > xMax) x = xMax;
			
			T dropOnto = null;
			for (final T object : tasks) {
				final Rectangle r = locationsByTask.get(object);
				if (r.contains(x, yCoordinate)) {
					dropOnto = object;
					break;
				}
			}
			
			if (dropOnto != null) {
				insertAfter = dropOnto;
			}
		}
		final boolean changed = insertAfter != this.insertAfter || insertBefore != this.insertBefore || insertResource != this.insertResource;
		this.insertAfter = insertAfter;
		this.insertBefore = insertBefore;
		this.insertResource = insertResource;
		return changed;
	}
	
	protected T findTaskAtCoordinates(final int x, final int y) {
		// this would be better done with an R-tree
		for (final Map.Entry<Rectangle, T> entry : tasksByLocation.entrySet()) {
			if (entry.getKey().contains(x,y)) return entry.getValue();
		}
		return null;
	}

	protected void paintControl(final PaintEvent e) {
		prepare();
		
		if (coloursSet == false) {
			coloursSet = true;
			backgroundColor = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			taskLabelTextColor = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			resourceLabelTextColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
			dividerColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
			
			selectedTaskGradientBottom = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
			selectedTaskGradientTop = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
			
			taskGradientBottom = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
			taskGradientTop = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		}
		
		int oldMinWidth = minWidth;
		int oldMinHeight = minHeight;
		minWidth = 0;
		minHeight = 0;
		tasksByLocation.clear();
		locationsByTask.clear();
		resourceByY.clear();
		
		int leftOffset = 0;
		
		final GC gc = e.gc;

		for (final R resource : resources) {
			final String name = labelProvider.getText(resource);
			leftOffset = Math.max(leftOffset, gc.textExtent(name).x);
		}

		leftOffset += VERTICAL_SPACE_BETWEEN_TASKS*2;
		
		int topOfCurrentRow = 0;
		
		gc.setAlpha(255);
		gc.setForeground(backgroundColor);
		gc.setBackground(backgroundColor);
		gc.fillRectangle(e.x, e.y, e.width, e.height);
		
		topOfCurrentRow += paintRow(e, topOfCurrentRow, leftOffset, unallocatedTasks, true);
		// now paint all allocated tasks
		for (final R resource : resources) {
			gc.setForeground(resourceLabelTextColor);
			gc.drawString(labelProvider.getText(resource), VERTICAL_SPACE_BETWEEN_TASKS, topOfCurrentRow + VERTICAL_SPACE_BETWEEN_TASKS, true);
			
			resourceByY.put(topOfCurrentRow, resource);
			// draw a horizontal line
			gc.setForeground(dividerColor);
			gc.drawLine(leftOffset, topOfCurrentRow, getSize().x - VERTICAL_SPACE_BETWEEN_TASKS, topOfCurrentRow);
			
			final List<T> assignment = assignmentProvider.getAssignedObjects(resource);
			topOfCurrentRow += paintRow(e, topOfCurrentRow, leftOffset, assignment, false);
		}
		
		if (selectedTask != null) {
			gc.setAlpha(100);
			
			drawTask(selectedTask, gc, leftOffset, selectedTaskDragY - selectedTaskInternalY, 
					timingProvider.getStartDate(selectedTask), timingProvider.getEndDate(selectedTask));
		}
		
		minWidth += VERTICAL_SPACE_BETWEEN_TASKS ;
		minHeight = Math.max(minHeight, topOfCurrentRow + VERTICAL_SPACE_BETWEEN_TASKS * 2 + TASK_HEIGHT);
		
		if (oldMinWidth != minWidth || oldMinHeight != minHeight) {
			for (final ISizeListener l : sizeListeners.toArray(new ISizeListener[0])) {
				l.requiredSizeUpdated(minWidth, minHeight);
			}
		}
	}
	
	protected void drawTask(final T task, final GC gc,final int xoff, final int y, final Date start, final Date end) {
		String taskName = labelProvider.getText(task);
		
		final int days = (int) ((start.getTime() - minDate.getTime()) / SCALE_FACTOR);
		
		final int w = (int) Math.max(MIN_WIDTH, ((end.getTime() - start.getTime()) / SCALE_FACTOR));
		
		if (task == selectedTask) {
			gc.setBackground(selectedTaskGradientBottom);
			gc.setForeground(selectedTaskGradientTop);
		} else {
			gc.setBackground(taskGradientBottom);
			gc.setForeground(taskGradientTop);
		}
		
		gc.fillGradientRectangle(xoff+days, y, w, TASK_HEIGHT, true);
		
		gc.setForeground(resourceLabelTextColor);
		
		gc.drawRectangle(xoff+days, y, w, TASK_HEIGHT);
		
		gc.setForeground(taskLabelTextColor);
		Point textExtent = gc.textExtent(taskName, SWT.DRAW_TRANSPARENT);
		while (textExtent.x > w) {
			taskName = taskName.substring(0, taskName.length()-2);
			textExtent = gc.textExtent(taskName, SWT.DRAW_TRANSPARENT);
		}
		// draw label in the middle
		
		
		
		// position of top left corner is position of middle of task - 1/2 textextent
		final int labelx = xoff + days + w/2 - textExtent.x/2;
		final int labely = y + TASK_HEIGHT/2 - textExtent.y/2;
		
		gc.drawString(taskName, labelx, labely, true);
		
		final Rectangle rect = new Rectangle(xoff+days, y, w, TASK_HEIGHT);
		tasksByLocation.put(rect, task);
		locationsByTask.put(task, rect);
		
		minWidth = Math.max(minWidth, rect.x + rect.width);
		minHeight = Math.max(minHeight, rect.y + rect.height);
	}
	
	protected int paintRow(final PaintEvent e, final int topOffset, final int leftOffset, final List<T> objects, final boolean collapse) {
		if (objects == null) return EMPTY_ROW_HEIGHT;

		final GC gc = e.gc;
		
		final DateRangeTracker rangeTracker = new DateRangeTracker(collapse);
		
		int maxRowDepth = 0;
		
		for (final T o : objects) {
			final Date start = timingProvider.getStartDate(o);
			final Date end = timingProvider.getEndDate(o);
			final int depth = rangeTracker.addRange(start, end, o);
			
			final int taskTop = topOffset + VERTICAL_SPACE_BETWEEN_TASKS +
					depth * (TASK_HEIGHT + VERTICAL_SPACE_BETWEEN_TASKS);
			
			drawTask(o, gc, leftOffset, taskTop, start, end);
			// update max depth in this row.
			maxRowDepth = Math.max(maxRowDepth, depth);
		}
		
		return VERTICAL_SPACE_BETWEEN_TASKS + (maxRowDepth + 1) * (TASK_HEIGHT + VERTICAL_SPACE_BETWEEN_TASKS);
	}

	private boolean prepared = false;
	private IAssignmentProvider<R, T> assignmentProvider;
	private List<IAssignmentListener<R, T>> assignmentListeners = new ArrayList<IAssignmentListener<R, T>>();
	private List<ISizeListener> sizeListeners = new ArrayList<ISizeListener>();
	
	private void prepare() {
		if (prepared) return;
		resourceByTask.clear();
		unallocatedTasks.clear();
		
		prepared = true;
		minDate = null;
		
		for (final T task : tasks) {
			final Date tStart = timingProvider.getStartDate(task);
			if (minDate == null || minDate.after(tStart)) minDate = tStart;
		}
		
		final HashSet<T> unallocated = new HashSet<T>();
		unallocated.addAll(tasks);
		
		for (final R resource : resources) {
			for (final T o : assignmentProvider.getAssignedObjects(resource)) {
				resourceByTask.put(o, resource);
			}
			unallocated.removeAll(assignmentProvider.getAssignedObjects(resource));
		}
		
		unallocatedTasks.addAll(unallocated);
		Collections.sort(unallocatedTasks, startDateComparator);
	}
	
	public void setResources(final List<R> resources) {
		this.resources.clear();
		this.resources.addAll(resources);
		update();
	}
	
	public void setTasks(final List<T> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
		update();
	}
	
	public void setLabelProvider(final ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
		update();
	}
	
	public void setTimingProvider(final ITimingProvider<T> timingProvider) {
		this.timingProvider = timingProvider;
		update();
	}
	
	public void setAssignmentProvider(final IAssignmentProvider<R, T> assignmentProvider) {
		this.assignmentProvider = assignmentProvider;
		update();
	}
	
	public void addAssignmentListener(final IAssignmentListener<R, T> assignmentListener) {
		this.assignmentListeners.add(assignmentListener);
	}

	public void addSizeListener(ISizeListener sizeListener) {
		this.sizeListeners.add(sizeListener);
	}
}
