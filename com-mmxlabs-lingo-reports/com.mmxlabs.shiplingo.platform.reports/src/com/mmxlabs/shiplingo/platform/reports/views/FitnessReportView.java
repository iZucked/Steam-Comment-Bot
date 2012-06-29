/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.views.FitnessContentProvider.RowData;

public class FitnessReportView extends ViewPart {
	private final ArrayList<Integer> sortColumns = new ArrayList<Integer>(4);

	private boolean inverseSort = false;

	protected void setSortColumn(final TableColumn column, final int value) {
		if (sortColumns.get(0) == value) {
			inverseSort = !inverseSort;
		} else {
			inverseSort = false;
			sortColumns.remove((Object) value);
			sortColumns.add(0, value);
		}

		viewer.getTable().setSortColumn(column);
		viewer.getTable().setSortDirection(inverseSort ? SWT.DOWN : SWT.UP);

		viewer.refresh();
	}

	private void addSortSelectionListener(final TableColumn column, final int value) {
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
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.FitnessReportView";

	private TableViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private ScenarioViewerSynchronizer jobManagerListener;

	private FitnessContentProvider contentProvider;

	private TableViewerColumn delta;

	class ViewLabelProvider extends CellLabelProvider implements ITableLabelProvider, ITableColorProvider {
		@Override
		public String getColumnText(final Object obj, final int index) {

			if (obj instanceof RowData) {
				final RowData d = (RowData) obj;
				if (index == 0) {
					return d.scenario;
				} else if (index == 1) {
					return d.component;
				} else {
					if (index == 3) {
						final Long delta = getDelta(d);
						if (delta != null) {
							return String.format("%,d", delta);
						}
					} else {
						return String.format("%,d", d.fitness);
					}
					
				}
			}
			return "";
		}
		
		private Long getDelta(final RowData d) {
			final List<RowData> pinnedData = contentProvider.getPinnedData();
			for (final RowData data : pinnedData) {
				if (d.component.equals(data.component)) {
					return data.fitness - d.fitness;
				}
			}
			return null;
		}

		@Override
		public Image getColumnImage(final Object obj, final int index) {
			return null;
		}

		@Override
		public void update(final ViewerCell cell) {

		}
		
		@Override
		public Color getForeground(Object element, int columnIndex) {
			if (columnIndex == 3 && element instanceof RowData) {
				final Long l = getDelta((RowData) element);
				if (l == null || l.longValue() == 0l) {
					return null;
				} else if (l < 0) {
					return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
				} else {
					return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
				}
			}
			return null;
		}

		@Override
		public Color getBackground(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * The constructor.
	 */
	public FitnessReportView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL) {
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
		this.contentProvider = new FitnessContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setInput(getViewSite());

		final TableViewerColumn tvc0 = new TableViewerColumn(viewer, SWT.NONE);
		tvc0.getColumn().setText("Schedule");
		tvc0.getColumn().pack();
		addSortSelectionListener(tvc0.getColumn(), 0);

		final TableViewerColumn tvc1 = new TableViewerColumn(viewer, SWT.NONE);
		tvc1.getColumn().setText("Component");
		tvc1.getColumn().pack();
		addSortSelectionListener(tvc1.getColumn(), 1);

		final TableViewerColumn tvc2 = new TableViewerColumn(viewer, SWT.NONE);
		tvc2.getColumn().setText("Fitness");
		tvc2.getColumn().pack();
		addSortSelectionListener(tvc2.getColumn(), 2);

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);

		sortColumns.add(0);
		sortColumns.add(1);
		sortColumns.add(2);

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final RowData r1 = (RowData) e1;
				final RowData r2 = (RowData) e2;

				final int d = inverseSort ? -1 : 1;
				int sort = 0;
				final Iterator<Integer> iterator = sortColumns.iterator();
				while (iterator.hasNext() && (sort == 0)) {
					switch (iterator.next()) {
					case 0:
						sort = r1.scenario.compareTo(r2.scenario);
						break;
					case 1:
						sort = r1.component.compareTo(r2.component);
						break;
					case 2:
						sort = ((Long) r1.fitness).compareTo(r2.fitness);
						break;
					}
				}

				return d * sort;
				// return super.compare(viewer, e1, e2);
			}
		});

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.demo.reports.FitnessReportView");
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		//
		// getSite().getPage().addSelectionListener("com.mmxlabs.rcp.navigator",
		// this);
		//
		// // Trigger initial view state
		// final ISelection selection = getSite().getWorkbenchWindow()
		// .getSelectionService()
		// .getSelection("com.mmxlabs.rcp.navigator");
		//
		// selectionChanged(null, selection);

		jobManagerListener = ScenarioViewerSynchronizer.registerView(viewer, new ScheduleElementCollector() {
			private boolean hasPin = false;
			@Override
			public void beginCollecting() {
				hasPin = false;
			}

			@Override
			public void endCollecting() {
				setShowDeltaColumn(hasPin);
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean pinned) {
				hasPin = hasPin || pinned;
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
				FitnessReportView.this.fillContextMenu(manager);
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

		manager.add(new GroupMarker("pack"));
		// Other plug-ins can contribute there actions here
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("pack"));
		// Other plug-ins can contribute there actions here
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

	private void setShowDeltaColumn(final boolean showDeltaColumn) {
		if (showDeltaColumn) {
			if (delta == null) {
				delta = new TableViewerColumn(viewer, SWT.NONE);
				delta.getColumn().setText("Improvement");
				delta.getColumn().pack();
				addSortSelectionListener(delta.getColumn(), 4);
				viewer.setLabelProvider(viewer.getLabelProvider());
			}
		} else {
			if (delta != null) {
				delta.getColumn().dispose();
				delta = null;
			}
		}
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
		// getSite().getPage().removeSelectionListener(
		// "com.mmxlabs.rcp.navigator", this);

		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);

		super.dispose();
	}
}