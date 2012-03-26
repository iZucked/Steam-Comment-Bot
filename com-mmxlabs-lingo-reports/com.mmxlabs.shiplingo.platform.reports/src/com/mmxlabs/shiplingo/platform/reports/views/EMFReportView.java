/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.models.util.emfpath.CompiledEMFPath;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.shiplingo.platform.reports.ScheduleAdapter;

/**
 * Base class for views which show things from the EMF output model.
 * 
 * This shares a lot of function with EObjectTableViewer
 * 
 * @author hinton
 * 
 */
public abstract class EMFReportView extends ViewPart implements ISelectionListener {
	private final List<ColumnHandler> handlers = new ArrayList<ColumnHandler>();
	private final List<ColumnHandler> handlersInOrder = new ArrayList<ColumnHandler>();
	private FilterField filterField;
	boolean sortDescending = false;

	protected EMFReportView() {
		this.helpContextId = null;
	}

	protected EMFReportView(final String helpContextId) {
		this.helpContextId = helpContextId;
	}

	private final ICellManipulator noEditing = new ICellManipulator() {
		@Override
		public void setValue(final Object object, final Object value) {

		}

		@Override
		public Object getValue(final Object object) {
			return null;
		}

		@Override
		public CellEditor getCellEditor(final Composite parent, final Object object) {
			return null;
		}

		@Override
		public boolean canEdit(final Object object) {
			return false;
		}
	};

	protected class ColumnHandler {
		private static final String COLUMN_HANDLER = "COLUMN_HANDLER";
		private final IFormatter formatter;
		private final EMFPath path;
		private final String title;
		public GridViewerColumn column;

		public ColumnHandler(final IFormatter formatter, final Object[] features, final String title) {
			super();
			this.formatter = formatter;
			this.path = new CompiledEMFPath(getClass().getClassLoader(), true, features);
			this.title = title;
		}

		public GridViewerColumn createColumn(final EObjectTableViewer viewer) {
			final GridViewerColumn column = viewer.addColumn(title, new ICellRenderer() {
				@Override
				public String render(final Object object) {
					return formatter.format(object);
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
					// TODO fix this
					return Collections.emptySet();
				}

				@Override
				public Comparable getComparable(final Object object) {
					return formatter.getComparable(object);
				}

				@Override
				public Object getFilterValue(final Object object) {
					return formatter.getFilterable(object);
				}
			}, noEditing, path);

			final GridColumn tc = column.getColumn();
			tc.setData(COLUMN_HANDLER, this);
			this.column = column;
			return column;
		}
	}

	protected interface IFormatter {
		public String format(final Object object);

		public Comparable getComparable(final Object object);

		public Object getFilterable(final Object object);
	}

	protected final IFormatter containingScheduleFormatter = new BaseFormatter() {
		@Override
		public String format(Object object) {
			while ((object != null) && !(object instanceof Schedule)) {
				if (object instanceof EObject) {
					object = ((EObject) object).eContainer();
				}
			}
			if (object instanceof Schedule) {
				final MMXRootObject s = (MMXRootObject) ((Schedule) object).eContainer().eContainer();
				return s.getName();
//				return "TODO";
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

		@Override
		public Object getFilterable(final Object object) {
			return getComparable(object);
		}
	}

	public class IntegerFormatter implements IFormatter {
		public Integer getIntValue(final Object object) {
			if (object == null) {
				return null;
			}
			return ((Number) object).intValue();
		}

		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			}
			final Integer x = getIntValue(object);
			if (x == null) {
				return "";
			}
			return String.format("%,d", x);
		}

		@Override
		public Comparable getComparable(final Object object) {
			final Integer x = getIntValue(object);
			if (x == null) {
				return -Integer.MAX_VALUE;
			}
			return x;
		}

		@Override
		public Object getFilterable(final Object object) {
			return getComparable(object);
		}
	}

	protected class CalendarFormatter extends BaseFormatter {
		final DateFormat dateFormat;
		final boolean showZone;

		public CalendarFormatter(final DateFormat dateFormat, final boolean showZone) {
			this.dateFormat = dateFormat;
			this.showZone = showZone;
		}

		@Override
		public String format(final Object object) {
			if (object == null) {
				return "";
			}
			final Calendar cal = (Calendar) object;

			dateFormat.setCalendar(cal);
			return dateFormat.format(cal.getTime()) + (showZone ? (" (" + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")") : "");
		}

		@Override
		public Comparable getComparable(final Object object) {
			if (object == null) {
				return new Date(-Long.MAX_VALUE);
			}
			return ((Calendar) object).getTime();
		}

		@Override
		public Object getFilterable(final Object object) {
			if (object instanceof Calendar) {
				return object;
			}
			return object;
		}
	}

	protected final IFormatter calendarFormatter = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), true);

	protected final IFormatter datePartFormatter = new CalendarFormatter(DateFormat.getDateInstance(DateFormat.LONG), false);
	protected final IFormatter timePartFormatter = new CalendarFormatter(DateFormat.getTimeInstance(DateFormat.SHORT), false);

	protected final IntegerFormatter integerFormatter = new IntegerFormatter();

	protected final IFormatter costFormatter = new IntegerFormatter() {
		@Override
		public Integer getIntValue(final Object object) {
			if (object == null) {
				return null;
			}
			return -super.getIntValue(object);
		}
	};

	private EObjectTableViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;
	private final String helpContextId;
	private IEclipseJobManagerListener jobManagerListener;

	protected abstract IStructuredContentProvider getContentProvider();

	protected ColumnHandler addColumn(final String title, final IFormatter formatter, final Object... path) {
		final ColumnHandler handler = new ColumnHandler(formatter, path, title);
		handlers.add(handler);
		handlersInOrder.add(handler);

		if (viewer != null) {
			handler.createColumn(viewer).getColumn().pack();
		}

		return handler;
	}

	private final HashMap<Object, Object> equivalents = new HashMap<Object, Object>();
	private final HashSet<Object> contents = new HashSet<Object>();

	protected void setInputEquivalents(final Object input, final Collection<Object> objectEquivalents) {
		for (final Object o : objectEquivalents) {
			equivalents.put(o, input);
		}
		contents.add(input);
	}

	protected void clearInputEquivalents() {
		equivalents.clear();
		contents.clear();
	}

	@Override
	public void createPartControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		filterField = new FilterField(container);
		final GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 0;
		container.setLayout(layout);

		viewer = new EObjectTableViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION) {
			@Override
			protected void inputChanged(final Object input, final Object oldInput) {
				super.inputChanged(input, oldInput);

				final boolean inputEmpty = (input == null) || ((input instanceof Collection) && ((Collection<?>) input).isEmpty());
				final boolean oldInputEmpty = (oldInput == null) || ((oldInput instanceof Collection) && ((Collection<?>) oldInput).isEmpty());

				if (inputEmpty != oldInputEmpty) {
					// Disabled because running this takes up 50% of the runtime when displaying a new schedule (!)
					// if (packColumnsAction != null) {
					// packColumnsAction.run();
					// }
				}
			};
		};

		filterField.setViewer(viewer);

		viewer.getGrid().setLayoutData(new GridData(GridData.FILL_BOTH));

		// this is very slow on refresh
		viewer.setDisplayValidationErrors(false);

		if (handleSelections()) {
			viewer.setComparer(new IElementComparer() {
				@Override
				public int hashCode(final Object element) {
					return element.hashCode();
				}

				@Override
				public boolean equals(Object a, Object b) {
					if (!contents.contains(a) && equivalents.containsKey(a)) {
						a = equivalents.get(a);
					}
					if (!contents.contains(b) && equivalents.containsKey(b)) {
						b = equivalents.get(b);
					}
					return Equality.isEqual(a, b);
				}
			});
		}

		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);

		for (final ColumnHandler handler : handlersInOrder) {
			handler.createColumn(viewer).getColumn().pack();
		}

		if (helpContextId != null) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), helpContextId);
		}

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		viewer.init(getContentProvider());

		getSite().setSelectionProvider(viewer);
		if (handleSelections()) {
			getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
		}

		jobManagerListener = ScheduleAdapter.registerView(viewer);
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
		manager.add(new GroupMarker("filter"));
		manager.add(new GroupMarker("pack"));
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("filter", filterField.getContribution());
		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTableAction);
	}

	private void makeActions() {
		packColumnsAction = new PackGridTableColumnsAction(viewer);
		copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
	}

	@Override
	public void setFocus() {
		if (!viewer.getGrid().isDisposed()) {
			viewer.getGrid().setFocus();
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
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		if (part == this) {
			return;
		}

		viewer.setSelection(selection, true);
	}

	protected void handleAdaptedSelection(final List<Object> adaptedSelection) {
		viewer.setSelection(new StructuredSelection(adaptedSelection), true);
	}

	protected boolean handleSelections() {
		return false;
	}

	protected Class<?> getSelectionAdaptionClass() {
		return null;
	}

	public void removeColumn(final String title) {
		for (final ColumnHandler h : handlers) {
			if (h.title.equals(title)) {
				viewer.removeColumn(h.column);
				handlers.remove(h);
				handlersInOrder.remove(h);
				break;
			}
		}

	}

	@Override
	public void dispose() {
		ScheduleAdapter.deregisterView(jobManagerListener);

		super.dispose();
	}
}
