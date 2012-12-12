/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.views.ExposureContentProvider.ColumnManager;
import com.mmxlabs.shiplingo.platform.reports.views.ExposureContentProvider.IndexExposureData;

/**
 * @since 2.0
 */
public class ExposureReportView extends ViewPart {
	private final ArrayList<ColumnManager<IndexExposureData>> sortColumns = new ArrayList<ColumnManager<IndexExposureData>>();

	private boolean inverseSort = false;
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.TotalsReportView";

	private GridTableViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private ScenarioViewerSynchronizer viewerSynchronizer;

	private ExposureContentProvider contentProvider;

	class ViewLabelProvider<T> extends CellLabelProvider implements ITableLabelProvider, IFontProvider, ITableColorProvider {
		private List<ColumnManager<T>> managers;
		
		public ViewLabelProvider(List<ColumnManager<T>> managers) {
			this.managers = managers;
		}

		@Override
		public void dispose() {
			for (ColumnManager<T> manager: managers) {
				manager.dispose();
			}
			
			super.dispose();
		}

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(final Object obj, final int index) {
			return managers.get(index).getColumnText((T) obj);				
		}

		@SuppressWarnings("unchecked")
		@Override
		public Image getColumnImage(final Object obj, final int index) {
			return managers.get(index).getColumnImage((T) obj);
		}

		@Override
		public void update(final ViewerCell cell) {

		}

		@Override
		public Font getFont(final Object element) {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getBackground(final Object obj, final int index) {
			return managers.get(index).getBackground((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getForeground(Object obj, int index) {
			// TODO Auto-generated method stub
			return managers.get(index).getForeground((T) obj);
		}
	}

	/**
	 * The constructor.
	 */
	public ExposureReportView() {
	}

	private void addSortSelectionListener(final GridColumn column, final ColumnManager<IndexExposureData> value) {
		column.addSelectionListener(new SelectionAdapter() {
			{
				final SelectionAdapter self = this;
				column.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						column.removeSelectionListener(self);
					}
				});
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				setSortColumn(column, value);
			}
		});
		if (!sortColumns.contains(value)) {
			sortColumns.add(value);
		}
	}

	protected void setSortColumn(final GridColumn column, final ColumnManager<IndexExposureData> value) {
		if (sortColumns.get(0) == value) {
			inverseSort = !inverseSort;
		} else {
			inverseSort = false;
			sortColumns.remove((Object) value);
			sortColumns.add(0, value);
		}

		viewer.refresh();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new GridTableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION) {
			@Override
			protected void inputChanged(final Object input, final Object oldInput) {
				super.inputChanged(input, oldInput);

				final boolean inputEmpty = (input == null) || ((input instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) input).getCollectedElements().isEmpty());
				final boolean oldInputEmpty = (oldInput == null)
						|| ((oldInput instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) oldInput).getCollectedElements().isEmpty());

				if (inputEmpty != oldInputEmpty) {
					if (packColumnsAction != null) {
						packColumnsAction.run();
					}
				}
			};
		};
		this.contentProvider = new ExposureContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setInput(getViewSite());
		
		List<ColumnManager<IndexExposureData>> columnManagers = contentProvider.getColumnManagers();
		
		for (ColumnManager<IndexExposureData> cv: columnManagers) {
			String name = cv.getName();
			GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			GridColumn gc = gvc.getColumn();
			gc.setText(name);
			gc.pack();
			addSortSelectionListener(gc, cv);
		}
			
		viewer.setLabelProvider(new ViewLabelProvider<IndexExposureData>(columnManagers));

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final int d = inverseSort ? -1 : 1;
				for (ColumnManager<IndexExposureData> cm: sortColumns) {
					int sort = cm.compare((IndexExposureData) e1, (IndexExposureData) e2);
					if (sort != 0) { 
						return d * sort;
					}
				}

				return 0;
			}
		});

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.demo.reports.TotalsReportView");
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		viewerSynchronizer = ScenarioViewerSynchronizer.registerView(viewer, new ScheduleElementCollector() {
			private boolean hasPin = false;
			private int numberOfSchedules;

			@Override
			public void beginCollecting() {
				hasPin = false;
				numberOfSchedules = 0;
			}

			@Override
			public void endCollecting() {
				setShowColumns(hasPin, numberOfSchedules);
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean pinned) {
				hasPin = hasPin || pinned;
				++numberOfSchedules;
				return Collections.singleton(schedule);
			}
		});
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				fillContextMenu(manager);
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
		manager.add(new Separator());
	}

	private void fillContextMenu(final IMenuManager manager) {
		// Other plug-ins can contribute there actions here
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
		packColumnsAction = new PackGridTableColumnsAction(viewer);
		copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void refresh() {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {
					viewer.refresh();
				}
			}
		});
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
	public void dispose() {

		ScenarioViewerSynchronizer.deregisterView(viewerSynchronizer);
		super.dispose();
	}

	private void setShowColumns(final boolean showDeltaColumn, int numberOfSchedules) {

	}
}