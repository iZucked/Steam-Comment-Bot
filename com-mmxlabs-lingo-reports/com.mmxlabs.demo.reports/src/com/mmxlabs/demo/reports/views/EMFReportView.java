/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import scenario.schedule.Schedule;

import com.mmxlabs.demo.reports.ScheduleAdapter;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;

/**
 * Base class for views which show things from the EMF output model.
 * 
 * @author hinton
 * 
 */
public abstract class EMFReportView extends ViewPart implements
		ISelectionListener {
	private final List<ColumnHandler> handlers = new ArrayList<ColumnHandler>();
	boolean sortDescending = false;

	protected EMFReportView() {
		this.helpContextId = null;
	}

	protected EMFReportView(final String helpContextId) {
		this.helpContextId = helpContextId;
	}

	private class ColumnHandler {
		private static final String COLUMN_HANDLER = "COLUMN_HANDLER";
		private final IFormatter formatter;
		private final EMFPath path;
		private final String title;

		public ColumnHandler(final IFormatter formatter,
				final Object[] features, final String title) {
			super();
			this.formatter = formatter;
			this.path = new EMFPath(true, features);
			this.title = title;
		}

		public TableViewerColumn createColumn(final TableViewer viewer) {
			final TableViewerColumn column = new TableViewerColumn(viewer,
					SWT.NONE);
			column.getColumn().setText(title);
			column.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					return formatter.format(path.get((EObject) element));
				}
			});

			final TableColumn tc = column.getColumn();
			tc.setData(COLUMN_HANDLER, this);
			tc.addSelectionListener(new SelectionAdapter() {
				{
					final SelectionListener sl = this;
					tc.addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(final DisposeEvent e) {
							tc.removeSelectionListener(sl);
						}

					});
				}

				@Override
				public void widgetSelected(final SelectionEvent e) {
					// update sort order
					makeSortColumn((ColumnHandler) tc.getData(COLUMN_HANDLER));
					viewer.getTable().setSortColumn(tc);
					viewer.getTable().setSortDirection(
							sortDescending ? SWT.DOWN : SWT.UP);
				}
			});

			return column;
		}

		public int compare(final EObject one, final EObject two) {
			final Comparable c1 = formatter.getComparable(path.get(one));
			final Comparable c2 = formatter.getComparable(path.get(two));
			return c1.compareTo(c2);
		}
	}

	private void makeSortColumn(final ColumnHandler handler) {
		if (handlers.get(0).equals(handler)) {
			sortDescending = !sortDescending;
		} else {
			sortDescending = false;
			handlers.remove(handler);
			handlers.add(0, handler);
		}
		viewer.refresh();
	}

	protected interface IFormatter {
		public String format(final Object object);

		public Comparable getComparable(final Object object);
	}

	protected final IFormatter containingScheduleFormatter = new BaseFormatter() {
		@Override
		public String format(Object object) {
			while (object != null && !(object instanceof Schedule)) {
				if (object instanceof EObject) {
					object = ((EObject) object).eContainer();
				}
			}
			if (object instanceof Schedule) {
				return URI.decode(
						((Schedule) object).eResource().getURI().lastSegment())
						.replaceAll(".scenario", "");
			} else {
				return "";
			}
		}

	};
	protected final IFormatter objectFormatter = new BaseFormatter();

	public class BaseFormatter implements IFormatter {
		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			} else {
				return object.toString();
			}
		}

		@Override
		public Comparable getComparable(final Object object) {
			return format(object);
		}

	}

	public class IntegerFormatter implements IFormatter {
		public Integer getIntValue(final Object object) {
			if (object == null)
				return null;
			return ((Number) object).intValue();
		}

		@Override
		public String format(final Object object) {
			if (object == null)
				return "";
			final Integer x = getIntValue(object);
			if (x == null)
				return "";
			return String.format("%,d", x);
		}

		@Override
		public Comparable getComparable(final Object object) {
			final Integer x = getIntValue(object);
			if (x == null)
				return -Integer.MAX_VALUE;
			return x;
		}
	}

	protected final IFormatter calendarFormatter = new BaseFormatter() {
		@Override
		public String format(final Object object) {
			if (object == null)
				return "";
			final Calendar cal = (Calendar) object;
			final DateFormat df = DateFormat.getDateTimeInstance(
					DateFormat.SHORT, DateFormat.SHORT);
			df.setCalendar(cal);
			return df.format(cal.getTime()) + " ("
					+ cal.getTimeZone().getDisplayName(false, TimeZone.SHORT)
					+ ")";
		}

		@Override
		public Comparable getComparable(final Object object) {
			if (object == null)
				return new Date(-Long.MAX_VALUE);
			return ((Calendar) object).getTime();
		}
	};

	protected final IntegerFormatter integerFormatter = new IntegerFormatter();

	protected final IFormatter costFormatter = new IntegerFormatter() {
		@Override
		public Integer getIntValue(final Object object) {
			if (object == null)
				return null;
			return -super.getIntValue(object);
		}
	};

	private TableViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;
	private final String helpContextId;

	protected abstract IStructuredContentProvider getContentProvider();

	protected void addColumn(final String title, final IFormatter formatter,
			final Object... path) {
		final ColumnHandler handler = new ColumnHandler(formatter, path, title);
		handlers.add(handler);

		if (viewer != null) {
			handler.createColumn(viewer).getColumn().pack();
		}
	}

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		viewer.setContentProvider(getContentProvider());

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1,
					final Object e2) {
				final Iterator<ColumnHandler> iterator = handlers.iterator();
				int comparison = 0;
				while (iterator.hasNext() && comparison == 0) {
					comparison = iterator.next().compare((EObject) e1,
							(EObject) e2);
				}
				return sortDescending ? -comparison : comparison;
			}
		});

		for (final ColumnHandler handler : handlers) {
			handler.createColumn(viewer).getColumn().pack();
		}

		if (helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem()
					.setHelp(viewer.getControl(), helpContextId);
		}

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener("com.mmxlabs.rcp.navigator", this);

		final ISelection selection = getSite().getWorkbenchWindow()
				.getSelectionService()
				.getSelection("com.mmxlabs.rcp.navigator");

		// Trigger initial view selection
		selectionChanged(null, selection);
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				EMFReportView.this.fillContextMenu(manager);
			}
		});
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(final IMenuManager manager) {
	}

	private void fillContextMenu(final IMenuManager manager) {
		// Other plug-ins can contribute their actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("pack"));
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTableAction);
	}

	private void makeActions() {
		packColumnsAction = new PackTableColumnsAction(viewer);
		copyTableAction = new CopyTableToClipboardAction(viewer.getTable());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
	}

	@Override
	public void setFocus() {
		if (!viewer.getTable().isDisposed()) {
			viewer.getTable().setFocus();
		}
	}

	public void setInput(final Object input) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {
					viewer.setInput(input);
				}
			}
		});
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part,
			final ISelection selection) {

		final List<Schedule> schedules = ScheduleAdapter
				.getSchedules(selection);
		if (schedules.isEmpty()) {
			setInput(null);
		} else {
			setInput(schedules);
		}
	}

	public void removeColumn(final String title) {
		for (final ColumnHandler h : handlers) {
			if (h.title.equals(title)) {
				handlers.remove(h);
				break;
			}
		}
		for (final TableColumn column : viewer.getTable().getColumns()) {
			if (column.getText().equals(title))
				// TODO this might be pretty wrong.
				// TODO: This is how I usually do it - SG
				column.dispose();
		}
		// viewer.getTable().pack(true);
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(
				"com.mmxlabs.rcp.navigator", this);

		super.dispose();
	}
}
