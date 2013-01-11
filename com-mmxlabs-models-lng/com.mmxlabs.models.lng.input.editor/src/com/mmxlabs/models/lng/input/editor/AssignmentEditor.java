/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.editor;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.management.timer.Timer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.input.presentation.InputEditorPlugin;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;

public class AssignmentEditor<R, T> extends Canvas {
	private static final int EMPTY_ROW_HEIGHT = 64;
	private static final int TASK_HEIGHT = 18;
	private static final int VERTICAL_SPACE_BETWEEN_TASKS = 8;
	/**
	 * Sets the time for one pixel of horizontal scale
	 */
	private static final long SCALE_FACTOR = Timer.ONE_HOUR * 6;
	private static final int MIN_WIDTH = 16;
	private static final int LIMIT_WIDTH = 8;

	private final List<T> tasks = new ArrayList<T>();
	private final List<T> unallocatedTasks = new ArrayList<T>();

	Comparator<R> resourceComparator = new Comparator<R>() {

		@Override
		public int compare(final R r1, final R r2) {

			if (r1 == null) {
				return -1;
			}
			if (r2 == null) {
				return 1;
			}
			if (informationProvider == null) {
				return r1.hashCode() - r2.hashCode();
			}

			final String s1 = informationProvider.getResourceLabel(r1);
			final String s2 = informationProvider.getResourceLabel(r2);

			if (s1 == null) {
				return -1;
			}
			if (s2 == null) {
				return 1;
			}
			final int c = s1.compareTo(s2);
			if (c == 0) {
				return r1.hashCode() - r2.hashCode();
			}
			return c;
		}
	};

	private final Collection<R> resources = new ArrayList<R>();

	private AssignmentEditorColors colors = null;

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

	private IFilter taskFilter;
	private IFilter resourceFilter;

	private final Comparator<T> startDateComparator = new Comparator<T>() {
		@Override
		public int compare(final T o1, final T o2) {
			return informationProvider.getStartDate(o1).compareTo(informationProvider.getStartDate(o2));
		}
	};

	private Date minDate;

	private IAssignmentInformationProvider<R, T> informationProvider;

	public AssignmentEditor(final Composite parent, final int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				AssignmentEditor.this.paintControl(e);
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(final MouseEvent e) {
				AssignmentEditor.this.mouseDown(e);
			}

			@Override
			public void mouseUp(final MouseEvent e) {
				AssignmentEditor.this.mouseUp(e);
			}

			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				AssignmentEditor.this.mouseUp(e);
				AssignmentEditor.this.mouseDoubleClick(e);
			}
		});

		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(final MouseEvent e) {
				AssignmentEditor.this.mouseMove(e);
			}
		});

		addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(final MenuDetectEvent e) {
				final Point local = AssignmentEditor.this.toControl(new Point(e.x, e.y));
				final T task = findTaskAtCoordinates(local.x, local.y);

				if (task == null) {
					e.doit = false;
					return;
				}

				final Action open = new Action("Edit...") {
					@Override
					public void run() {
						notifyEditEvent(task);
					}
				};

				final Action delete = new Action("Delete") {
					@Override
					public void run() {
						notifyDeleteEvent(task);
					}
				};
				Action lock;
				if (informationProvider.isLocked(task)) {
					lock = new Action("Unlock") {
						@Override
						public void run() {
							notifyUnlockEvent(task);
						}
					};
				} else {
					lock = new Action("Lock") {
						@Override
						public void run() {
							notifyLockEvent(task);
						}
					};
				}

				final Action unassign = new Action("Unassign") {
					@Override
					public void run() {
						setSelectedTask(task);
						insertResource = resourceByTask.get(task);
						if (insertResource != null) {
							notifyRemoved();
						}
						insertResource = null;
						setSelectedTask(null);
					}
				};

				final AbstractMenuAction reassign = new AbstractMenuAction("Assign to") {
					@Override
					protected void populate(final Menu menu) {
						final R currentResource = resourceByTask.get(task);
						for (final R resource : resources) {
							if (currentResource == resource)
								continue;
							final Action assignToResource = new Action(informationProvider.getResourceLabel(resource)) {
								@Override
								public void run() {
									setSelectedTask(task);
									insertResource = resource;
									notifyDrop();
									setSelectedTask(null);
									insertResource = null;
								}
							};
							addActionToMenu(assignToResource, menu);
						}
					}

				};

				final MenuManager manager = new MenuManager();

				manager.add(unassign);
				manager.add(reassign);
				manager.add(new Separator("modifiers"));
				manager.add(lock);
				manager.add(delete);
				manager.add(open);

				setMenu(manager.createContextMenu(AssignmentEditor.this));
				e.doit = true;
			}
		});

		
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent event) {
				dispose();
			}
		});
		final MenuManager manager = new MenuManager();

		setMenu(manager.createContextMenu(AssignmentEditor.this));
		
		colors = InputEditorPlugin.getPlugin().getAssignmentEditorColours();
	}

	private void notifyEditEvent(final T task) {
		if (task != null) {
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskOpened(task);
			}
		}
	}

	private void notifyDeleteEvent(final T task) {
		if (task != null) {
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskDeleted(task);
			}
		}
	}

	private void notifyLockEvent(final T task) {
		if (task != null) {
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskLocked(task, resourceByTask.get(task));
			}
		}
	}

	private void notifyUnlockEvent(final T task) {
		if (task != null) {
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskUnlocked(task, resourceByTask.get(task));
			}
		}
	}

	protected void mouseDoubleClick(final MouseEvent e) {
		final T task = findTaskAtCoordinates(e.x, e.y);
		notifyEditEvent(task);
	}

	final DateFormat tooltipDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

	protected void mouseMove(final MouseEvent e) {
		if (getSelectedTask() != null) {
			setToolTipText("");
			if (findInsertionPoint(e.x, e.y, getSelectedTask()) || selectedTaskDragY != e.y) {
				selectedTaskDragY = e.y;
				redraw();
			}
		} else {
			final T task = findTaskAtCoordinates(e.x, e.y);
			if (task != null) {
				setToolTipText(informationProvider.getTooltip(task));
			} else {
				setToolTipText("");
			}
		}

	}

	protected void mouseDown(final MouseEvent e) {
		final T task = findTaskAtCoordinates(e.x, e.y);
		if (informationProvider.isLocked(task)) {
			return;
		}
		if (task != null) {
			switch (e.button) {
			case 1:
				if (assignmentProvider.canStartEdit()) {
					findInsertionPoint(e.x, e.y, task); // so if we let go it
														// doesn't get deallocated
														// instantly, we get it as
														// its own drag destination
					setSelectedTask(task);
					final Rectangle location = locationsByTask.get(task);
					selectedTaskInternalY = e.y - location.y;
					selectedTaskDragY = e.y;
					redraw();
				}
				break;
			default:
				break;
			}
		}
	}

	public void update() {
		prepared = false;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!isDisposed())
					redraw();
			}
		});
	}

	protected void mouseUp(final MouseEvent e) {
		if (insertResource != null) {
			if (insertAfter != null) {
				if (getSelectedTask() != null) {
					if (insertAfter != getSelectedTask()) {
						notifyDrop();
					}
				}
			} else {
				notifyDrop();
			}
		} else if (getSelectedTask() != null) {
			notifyRemoved();
		}
		insertAfter = null;
		insertBefore = null;
		insertResource = null;
		setSelectedTask(null);
		redraw();
	}

	private void notifyRemoved() {
		if (getSelectedTask() != null && resourceByTask.get(getSelectedTask()) != null) {
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskUnassigned(getSelectedTask(), resourceByTask.get(getSelectedTask()));
			}
		}
	}

	private void notifyDrop() {
		if (getSelectedTask() != null && insertResource != null) {
			if (insertBefore == getSelectedTask() || insertAfter == getSelectedTask())
				return;
			for (final IAssignmentListener<R, T> l : assignmentListeners.toArray(new IAssignmentListener[0])) {
				l.taskReassigned(getSelectedTask(), insertBefore, insertAfter, resourceByTask.get(getSelectedTask()), insertResource);
			}
		}
	}

	T insertAfter;
	T insertBefore;
	R insertResource;

	/**
	 * Finds the tasks before and after the given task if it were dropped at the given y coordinate
	 * 
	 * Results are stored in {@link #insertAfter}, {@link #insertBefore} and {@link #insertResource}
	 * 
	 * @return whether the point has changed, and so needs a redraw
	 */
	protected boolean findInsertionPoint(final int xCoordinate, final int yCoordinate, final T task) {
		T insertAfter = null;
		final T insertBefore = null;
		R insertResource = null;

		// find resource at y-coordinate
		final Entry<Integer, R> floorEntry = resourceByY.floorEntry(yCoordinate);
		if (floorEntry != null) {
			// there is a resource, so get that
			insertResource = floorEntry.getValue();
			// now try and find which tasks on that resource overlap with suitable y-coordinate
			final List<T> tasks = assignmentProvider.getAssignedObjects(insertResource);
			final Rectangle currentSelectionLocation = locationsByTask.get(task);

			final int xMin = currentSelectionLocation.x;
			final int xMax = xMin + currentSelectionLocation.width;
			int x = xCoordinate;

			if (x < xMin)
				x = xMin;
			else if (x > xMax)
				x = xMax;

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
			if (entry.getKey().contains(x, y))
				return entry.getValue();
		}
		return null;
	}

	protected synchronized void paintControl(final PaintEvent e) {
		if (isDisposed())
			return;
		prepare();

		final int oldMinWidth = minWidth;
		final int oldMinHeight = minHeight;
		minWidth = 0;
		minHeight = 0;
		tasksByLocation.clear();
		locationsByTask.clear();
		resourceByY.clear();

		int leftOffset = 0;

		final GC gc = e.gc;

		for (final R resource : resources) {
			final String name = informationProvider.getResourceLabel(resource);
			leftOffset = Math.max(leftOffset, gc.textExtent(name).x);
		}

		leftOffset += VERTICAL_SPACE_BETWEEN_TASKS * 3;

		gc.setAlpha(255);
		gc.setForeground(colors.backgroundColor);
		gc.setBackground(colors.backgroundColor);
		gc.fillRectangle(e.x, e.y, e.width, e.height);

		final int[] depths = new int[tasks.size()];

		int topOfCurrentRow = VERTICAL_SPACE_BETWEEN_TASKS + paintTimeGrid(leftOffset, gc);

		{
			final int rowHeight = getRowHeight(unallocatedTasks, true, depths);
			paintRow(e, topOfCurrentRow, leftOffset, unallocatedTasks, depths);
			topOfCurrentRow += rowHeight;
		}
		// now paint all allocated tasks

		// gc.setForeground(colors.dividerColor);
		// gc.drawLine(leftOffset - VERTICAL_SPACE_BETWEEN_TASKS, topOfCurrentRow, leftOffset - VERTICAL_SPACE_BETWEEN_TASKS, getSize().y);

		int i = 0;
		for (final R resource : resources) {
			if (resourceFilter != null && !resourceFilter.select(resource)) {
				i++;
				continue;
			}

			Date startDate = resourceStartDates.get(i);
			if (startDate == null || startDate.before(minDate))
				startDate = minDate;
			final Date endDate = resourceEndDates.get(i);

			final List<T> assignment = assignmentProvider.getAssignedObjects(resource);
			final int rowHeight = getRowHeight(assignment, false, depths);

			resourceByY.put(topOfCurrentRow, resource);
			// draw a horizontal line
			if (i % 2 == 0) {
				gc.setBackground(colors.rowColorOne);
			} else {
				gc.setBackground(colors.rowColorTwo);
			}

			gc.fillRectangle(0, topOfCurrentRow + 1, getSize().x, rowHeight - 1);
			final int rowLeft = leftOffset - VERTICAL_SPACE_BETWEEN_TASKS + (minDate != null ? getX(minDate, startDate) : 0);

			// gc.setBackground(colors.limitColor);
			// gc.setAlpha(100);
			//
			// gc.fillRectangle(0, topOfCurrentRow+1, rowLeft, rowHeight-1);
			// gc.setAlpha(255);
			//
			gc.setForeground(colors.resourceLabelTextColor);
			gc.drawString(informationProvider.getResourceLabel(resource), VERTICAL_SPACE_BETWEEN_TASKS, topOfCurrentRow + VERTICAL_SPACE_BETWEEN_TASKS, true);

			gc.setForeground(colors.dividerColor);
			gc.drawLine(0, topOfCurrentRow, getSize().x, topOfCurrentRow);
			gc.drawLine(0, topOfCurrentRow + rowHeight, getSize().x, topOfCurrentRow + rowHeight);

			paintRow(e, topOfCurrentRow, leftOffset, assignment, depths);

			// draw start and end dates

			if (startDate != null) {
				gc.setForeground(colors.limitColor);
				gc.setBackground(colors.limitColor2);
				gc.setAlpha(100);
				gc.fillGradientRectangle(rowLeft - LIMIT_WIDTH, topOfCurrentRow + 1, LIMIT_WIDTH, rowHeight - 1, false);
				gc.setAlpha(255);
			}

			if (endDate != null) {
				gc.setForeground(colors.limitColor2);
				gc.setBackground(colors.limitColor);
				final int left = rowLeft + getX(startDate, endDate);
				gc.setAlpha(100);
				gc.fillGradientRectangle(left, topOfCurrentRow + 1, LIMIT_WIDTH, rowHeight - 1, false);
				gc.setAlpha(255);
			}
			i++;
			topOfCurrentRow += rowHeight;
		}

		if (getSelectedTask() != null) {
			gc.setAlpha(100);

			drawTask(getSelectedTask(), gc, leftOffset, selectedTaskDragY - selectedTaskInternalY, informationProvider.getStartDate(getSelectedTask()),
					informationProvider.getEndDate(getSelectedTask()));
		}

		paintTimeGrid(leftOffset, gc);

		minWidth += VERTICAL_SPACE_BETWEEN_TASKS;
		minHeight = Math.max(minHeight, topOfCurrentRow + VERTICAL_SPACE_BETWEEN_TASKS * 2 + TASK_HEIGHT);

		if (oldMinWidth != minWidth || oldMinHeight != minHeight) {
			for (final ISizeListener l : sizeListeners.toArray(new ISizeListener[0])) {
				l.requiredSizeUpdated(minWidth, minHeight);
			}
		}
	}

	private int getRowHeight(final List<T> objects, final boolean collapse, final int[] depths) {
		if (objects == null)
			return EMPTY_ROW_HEIGHT;
		int maxRowDepth = 0;
		final DateRangeTracker rangeTracker = new DateRangeTracker(collapse);
		int i = 0;
		for (final T o : objects) {
			if (taskFilter != null && !taskFilter.select(o))
				continue;
			final Date start = informationProvider.getStartDate(o);
			final Date end = informationProvider.getEndDate(o);
			final int depth = rangeTracker.addRange(start, end, o);
			depths[i++] = depth;
			maxRowDepth = Math.max(maxRowDepth, depth);
		}

		return VERTICAL_SPACE_BETWEEN_TASKS + (maxRowDepth + 1) * (TASK_HEIGHT + VERTICAL_SPACE_BETWEEN_TASKS);
	}

	@Override
	public void dispose() {
		colors = null;
		super.dispose();
	}

	private int paintTimeGrid(final int leftOffset, final GC gc) {
		if (minDate != null && maxDate != null) {
			// draw dates at the top
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

			final int oldAlpha = gc.getAlpha();

			final DateFormatSymbols symbols = DateFormatSymbols.getInstance();

			int textHeight = 0;

			final long spanYears = (maxDate.getTime() - minDate.getTime()) / (Timer.ONE_WEEK * 52);

			if (spanYears > 1) {
				// draw years header
				calendar.setTime(minDate);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.MONTH, 0);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);

				gc.setAlpha(255);
				gc.setForeground(colors.resourceLabelTextColor);
				while (calendar.getTime().before(maxDate)) {
					final Date date = calendar.getTime();
					calendar.add(Calendar.YEAR, 1);
					final int x = leftOffset + getX(minDate, date);
					final int width = getX(date, calendar.getTime());
					final String yearString = "" + calendar.get(Calendar.YEAR);
					final Point textExtent = gc.textExtent(yearString, SWT.DRAW_TRANSPARENT);
					final int labelPosition = x + (width - textExtent.x) / 2;
					gc.drawString(yearString, labelPosition, 0);
					textHeight = Math.max(textHeight, textExtent.y);
				}
			}

			int offset = 0;
			if (textHeight > 0) {
				offset = textHeight + VERTICAL_SPACE_BETWEEN_TASKS;
				textHeight = 0;
			}

			calendar.setTime(minDate);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);

			while (calendar.getTime().before(maxDate)) {
				final Date date = calendar.getTime();
				final String monthName = symbols.getShortMonths()[calendar.get(Calendar.MONTH)];
				calendar.add(Calendar.MONTH, 1);
				if (date.after(minDate)) {
					// draw month labels
					gc.setAlpha(255);
					gc.setForeground(colors.resourceLabelTextColor);

					final Point textExtent = gc.textExtent(monthName, SWT.DRAW_TRANSPARENT);
					textHeight = Math.max(textHeight, textExtent.y);
					final int x = leftOffset + getX(minDate, date);
					final int width = getX(date, calendar.getTime());

					gc.drawString(monthName, x + (width - textExtent.x) / 2, offset, true);

					// draw faint vertical line
					gc.setAlpha(50);
					gc.setForeground(colors.dividerColor);
					gc.drawLine(x, offset, x, getSize().y);
				}
			}
			gc.setAlpha(oldAlpha);
			return offset + textHeight;
		}
		return 0;
	}

	private int getX(final Date fst, final Date snd) {
		return (int) ((snd.getTime() - fst.getTime()) / SCALE_FACTOR);
	}

	protected void drawTask(final T task, final GC gc, final int xoff, final int y, final Date start, final Date end) {
		String taskName = informationProvider.getLabel(task);

		final int days = getX(minDate, start);
		// final int days = (int) ((start.getTime() - minDate.getTime()) / SCALE_FACTOR);

		final int w = Math.max(MIN_WIDTH, getX(start, end));
		// final int w = (int) Math.max(MIN_WIDTH, ((end.getTime() - start.getTime()) / SCALE_FACTOR));

		gc.setAlpha(255);
		if (task == getSelectedTask()) {
			gc.setBackground(colors.selectedTaskGradientBottom);
			gc.setForeground(colors.selectedTaskGradientTop);
		} else if (informationProvider.isLocked(task)) {
			gc.setAlpha(150);
			gc.setBackground(colors.lockedTaskGradientBottom);
			gc.setForeground(colors.lockedTaskGradientTop);
		} else if (getSelectedTask() != null) {
			if (canFollowSelection.contains(task) || feasibles.get(task).contains(selectedTask)) {
				gc.setBackground(colors.taskGradientBottom);
				gc.setForeground(colors.taskGradientTop);
			} else {
				gc.setBackground(colors.inFeasibleTaskGradientBottom);
				gc.setForeground(colors.inFeasibleTaskGradientTop);
			}
		} else {
			gc.setBackground(colors.taskGradientBottom);
			gc.setForeground(colors.taskGradientTop);
		}

		gc.fillGradientRectangle(xoff + days, y, w, TASK_HEIGHT, true);

		gc.setForeground(colors.resourceLabelTextColor);

		gc.drawRectangle(xoff + days, y, w, TASK_HEIGHT);

		gc.setForeground(colors.taskLabelTextColor);
		Point textExtent = gc.textExtent(taskName, SWT.DRAW_TRANSPARENT);
		while (textExtent.x > w) {
			taskName = taskName.substring(0, taskName.length() - 2);
			textExtent = gc.textExtent(taskName, SWT.DRAW_TRANSPARENT);
		}

		// position of top left corner is position of middle of task - 1/2 textextent
		final int labelx = xoff + days + w / 2 - textExtent.x / 2;
		final int labely = y + TASK_HEIGHT / 2 - textExtent.y / 2;

		gc.drawString(taskName, labelx, labely, true);

		final Rectangle rect = new Rectangle(xoff + days, y, w, TASK_HEIGHT);
		tasksByLocation.put(rect, task);
		locationsByTask.put(task, rect);

		minWidth = Math.max(minWidth, rect.x + rect.width);
		minHeight = Math.max(minHeight, rect.y + rect.height);
	}

	protected void paintRow(final PaintEvent e, final int topOffset, final int leftOffset, final List<T> objects, final int[] depths) {
		final GC gc = e.gc;
		int index = 0;
		for (final T o : objects) {
			if (taskFilter != null && !taskFilter.select(o))
				continue;

			final Date start = informationProvider.getStartDate(o);
			final Date end = informationProvider.getEndDate(o);
			final int depth = depths[index++];

			final int taskTop = topOffset + VERTICAL_SPACE_BETWEEN_TASKS + depth * (TASK_HEIGHT + VERTICAL_SPACE_BETWEEN_TASKS);

			drawTask(o, gc, leftOffset, taskTop, start, end);
		}
	}

	private boolean prepared = false;
	private IAssignmentProvider<R, T> assignmentProvider;
	private final List<IAssignmentListener<R, T>> assignmentListeners = new ArrayList<IAssignmentListener<R, T>>();
	private final List<ISizeListener> sizeListeners = new ArrayList<ISizeListener>();
	private Date maxDate;

	private final List<Date> resourceStartDates = new ArrayList<Date>();
	private final List<Date> resourceEndDates = new ArrayList<Date>();
	private final Map<T, Set<T>> feasibles = new HashMap<T, Set<T>>();
	private Set<T> canFollowSelection;

	private void prepare() {
		if (prepared)
			return;
		resourceByTask.clear();
		unallocatedTasks.clear();

		resourceStartDates.clear();
		resourceEndDates.clear();
		feasibles.clear();

		prepared = true;
		minDate = null;
		maxDate = null;

		for (final T task : tasks) {
			final Date tStart = informationProvider.getStartDate(task);
			final Date tEnd = informationProvider.getEndDate(task);
			if (minDate == null || minDate.after(tStart))
				minDate = tStart;
			if (maxDate == null || maxDate.before(tEnd))
				maxDate = tEnd;

			final HashSet<T> follows = new HashSet<T>();
			feasibles.put(task, follows);
			for (final T task2 : tasks) {
				if (informationProvider.isSensibleSequence(task, task2)) {
					follows.add(task2);
				}
			}
		}

		
		if (minDate == null) {
			minDate = new Date();
		}
		final HashSet<T> unallocated = new HashSet<T>();
		unallocated.addAll(tasks);

		for (final R resource : resources) {
			resourceStartDates.add(informationProvider.getResourceStartDate(resource));
			resourceEndDates.add(informationProvider.getResourceEndDate(resource));
			for (final T o : assignmentProvider.getAssignedObjects(resource)) {
				resourceByTask.put(o, resource);
			}
			unallocated.removeAll(assignmentProvider.getAssignedObjects(resource));
		}

		unallocatedTasks.addAll(unallocated);
		Collections.sort(unallocatedTasks, startDateComparator);
	}

	public synchronized void setResources(final List<R> resources) {
		this.resources.clear();
		this.resources.addAll(resources);
		update();
	}

	public synchronized void setTasks(final List<T> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
		update();
	}

	public void setInformationProvider(final IAssignmentInformationProvider<R, T> timingProvider) {
		this.informationProvider = timingProvider;
		update();
	}

	public void setAssignmentProvider(final IAssignmentProvider<R, T> assignmentProvider) {
		this.assignmentProvider = assignmentProvider;
		update();
	}

	public void addAssignmentListener(final IAssignmentListener<R, T> assignmentListener) {
		this.assignmentListeners.add(assignmentListener);
	}

	public void addSizeListener(final ISizeListener sizeListener) {
		this.sizeListeners.add(sizeListener);
	}

	public IFilter getTaskFilter() {
		return taskFilter;
	}

	public void setTaskFilter(final IFilter taskFilter) {
		this.taskFilter = taskFilter;
	}

	public IFilter getResourceFilter() {
		return resourceFilter;
	}

	public void setResourceFilter(final IFilter resourceFilter) {
		this.resourceFilter = resourceFilter;
	}

	private T getSelectedTask() {
		return selectedTask;
	}

	private void setSelectedTask(final T selectedTask) {
		this.selectedTask = selectedTask;
		canFollowSelection = feasibles.get(selectedTask);
		if (selectedTask == null) assignmentProvider.finishEdit();
	}
}
