/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import scenario.schedule.Schedule;

import com.mmxlabs.demo.reports.views.actions.PackTableColumnsAction;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * @author hinton
 * 
 */
public abstract class EMFReportView extends ViewPart implements
		ISelectionListener {
	private final List<ColumnHandler> handlers = new LinkedList<ColumnHandler>();

	private class ColumnHandler {
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
				public String getText(Object element) {
					return formatter.format(path.get((EObject) element));
				}
			});
			return column;
		}
	}

	protected interface IFormatter {
		public String format(final Object object);
	}

	protected final IFormatter objectFormatter = new IFormatter() {
		@Override
		public String format(Object object) {
			if (object == null)
				return "";
			return object.toString();
		}
	};

	protected static String localizeDate(final Date date, final String tzname) {
		final TimeZone tz = TimeZone
				.getTimeZone((String) (tzname == null ? "UTC" : tzname));

		final Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(date);
		return String.format("%02d/%02d/%d %02d:%02d %s", calendar
				.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.YEAR), calendar
						.get(Calendar.HOUR_OF_DAY), calendar
						.get(Calendar.MINUTE), calendar.getTimeZone()
						.getDisplayName(false, TimeZone.SHORT));
	}

	protected final IFormatter calendarFormatter = new IFormatter() {
		@Override
		public String format(final Object object) {
			if (object == null) return "";
			final Calendar cal = (Calendar) object;
			final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
			df.setCalendar(cal);
			return df.format(cal.getTime()) + " (" + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")";
		}
	};
	
	protected final IFormatter integerFormatter = new IFormatter() {
		@Override
		public String format(Object object) {
			if (object == null)
				return "";
			return String.format("%,d", object);
		}
	};

	private TableViewer viewer;

	private PackTableColumnsAction packColumnsAction;

	protected abstract IStructuredContentProvider getContentProvider();

	protected void addColumn(final String title, final IFormatter formatter,
			final Object... path) {
		handlers.add(new ColumnHandler(formatter, path, title));
	}

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);

		viewer.setContentProvider(getContentProvider());

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		for (final ColumnHandler handler : handlers) {
			handler.createColumn(viewer).getColumn().pack();
		}

		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(), "com.mmxlabs.demo.reports.viewer");
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(this);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				EMFReportView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
	}

	private void fillContextMenu(IMenuManager manager) {
		// Other plug-ins can contribute their actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(packColumnsAction);
	}

	private void makeActions() {
		packColumnsAction = new PackTableColumnsAction(viewer);
	}

	@Override
	public void setFocus() {
		viewer.getTable().setFocus();
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
	public void selectionChanged(IWorkbenchPart arg0, ISelection selection) {
		final IStructuredSelection sel = (IStructuredSelection) selection;
		if (sel.isEmpty()) {
			setInput(null);
		} else {
			@SuppressWarnings("unchecked")
			Iterator<Object> iter = sel.iterator();
			while (iter.hasNext()) {
				final Object o = iter.next();
				if (o instanceof Schedule) {
					setInput((Schedule) o);
					return;
				}
				else if (o instanceof IAdaptable) {
					setInput((Schedule) ((IAdaptable) o).getAdapter(Schedule.class));
				}
			}
		}
	}
}
