package com.mmxlabs.lingo.reports.diff;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.diff.actions.DeleteDiffAction;
import com.mmxlabs.lingo.reports.diff.actions.ExtractCycleGroupAction;
import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.provider.ScheduleReportItemProviderAdapterFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;

public class DiffGroupView extends ViewPart implements ISelectionListener, IMenuListener {
	private static final String SCHEDULE_VIEW_ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";
	private GridTreeViewer viewer;

	private IViewPart scheduleView;
	private IPartListener listener;
	private ComposedAdapterFactory adapterFactory;
	private ObservablesManager mgr;
	// private NewUserGroupAction newUserGroupAction;
	private final DataBindingContext dbc = new EMFDataBindingContext();
	private ObservableListTreeContentProvider contentProvider;
	private Table table;

	@Override
	public void createPartControl(final Composite parent) {

		mgr = new ObservablesManager();
		mgr.runAndCollect(new Runnable() {

			@Override
			public void run() {

				viewer = new GridTreeViewer(parent) {

					@Override
					public ISelection getSelection() {
						// return super.getSelection();
						final Control control = getControl();
						if (control == null || control.isDisposed()) {
							return TreeSelection.EMPTY;
						}
						final Widget[] items = getSelection(getControl());
						final Set<Object> selection = new LinkedHashSet<>();
						for (int i = 0; i < items.length; i++) {
							final Widget item = items[i];
							if (item.isDisposed()) {
								continue;
							}
							if (item.getData() instanceof UserGroup) {
								final UserGroup userGroup = (UserGroup) item.getData();
								selection.add(userGroup);
								for (final CycleGroup cg : userGroup.getGroups()) {
									selection.add(cg);
									for (final Row row : cg.getRows()) {
										selection.add(row);
										for (final Object o : row.getInputEquivalents()) {
											selection.add(o);
										}
									}

								}
							} else if (item.getData() instanceof CycleGroup) {
								final CycleGroup cg = (CycleGroup) item.getData();
								selection.add(cg);
								for (final Row row : cg.getRows()) {
									selection.add(row);
									for (final Object o : row.getInputEquivalents()) {
										selection.add(o);
									}
								}
							} else if (item.getData() instanceof Row) {
								final Row row = (Row) item.getData();
								selection.add(row);
								for (final Object o : row.getInputEquivalents()) {
									selection.add(o);
								}
							}
						}
						return new StructuredSelection(new ArrayList<>(selection));
					}

					@Override
					public void setSelection(final ISelection selection) {
						expandToLevel(((IStructuredSelection) selection).getFirstElement(), 4);
						setSelection(selection, true);
					}
				};

				viewer.getGrid().setHeaderVisible(true);
				viewer.getGrid().setLinesVisible(true);
				adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
				adapterFactory.addAdapterFactory(new ScheduleReportItemProviderAdapterFactory());
				adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

				contentProvider = new ObservableListTreeContentProvider(new ScheduleReportTreeFactoryImpl(), new ScheduleReportTreeStructureAdvisorImpl());
				viewer.setContentProvider(contentProvider);
				// viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

				initDragAndDrop(viewer);
				initContextMenu(viewer);

				createColumns(viewer);

				makeActions();
				// ... etc
				hookToScheduleView();

				viewer.setComparer(new IElementComparer() {
					@Override
					public int hashCode(final Object element) {
						return element.hashCode();
					}

					@Override
					public boolean equals(final Object a, final Object b) {

						final Set<Object> setA = expand(a);
						final Set<Object> setB = expand(b);
						return !Sets.intersection(setA, setB).isEmpty();
					}

					private Set<Object> expand(final Object o) {
						final Set<Object> s = new LinkedHashSet<>();
						s.add(o);
						if (o instanceof Row) {
							final Row row = (Row) o;
							s.addAll(row.getInputEquivalents());
						}
						return s;

					}

				});
				viewer.setComparator(new ViewerComparator() {
					@Override
					public int compare(final Viewer viewer, final Object e1, final Object e2) {

						if (e1 instanceof UserGroup && !(e2 instanceof UserGroup)) {
							return -1;
						}
						if (e2 instanceof UserGroup && !(e1 instanceof UserGroup)) {
							return 1;
						}
						if (e1 instanceof CycleGroup && !(e2 instanceof CycleGroup)) {
							return -1;
						}
						if (e2 instanceof CycleGroup && !(e1 instanceof CycleGroup)) {
							return 1;
						}

						if (e1 instanceof UserGroup && e2 instanceof UserGroup) {
							final UserGroup g1 = (UserGroup) e1;
							final UserGroup g2 = (UserGroup) e2;
							return PNLDeltaUtils.getPNLDelta(g2) - PNLDeltaUtils.getPNLDelta(g1);
						}
						if (e1 instanceof CycleGroup && e2 instanceof CycleGroup) {
							final CycleGroup g1 = (CycleGroup) e1;
							final CycleGroup g2 = (CycleGroup) e2;
							return PNLDeltaUtils.getPNLDelta(g2) - PNLDeltaUtils.getPNLDelta(g1);
						}

						return super.compare(viewer, e1, e2);
					}
				});
			}
		});

		getSite().setSelectionProvider(viewer);

		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
	}

	@Override
	public void dispose() {
		if (listener != null) {
			getViewSite().getPage().removePartListener(listener);
		}
		super.dispose();
	}

	protected void makeActions() {

		final IActionBars actionBars = getViewSite().getActionBars();
		actionBars.getToolBarManager().add(PackActionFactory.createPackColumnsAction(viewer));

		// newUserGroupAction = new NewUserGroupAction();
		// actionBars.getToolBarManager().add(newUserGroupAction);

		final DeleteDiffAction deleteAction = new DeleteDiffAction();
		viewer.addSelectionChangedListener(deleteAction);
		actionBars.getToolBarManager().add(deleteAction);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);

		final Action collapseAllAction = new Action("Collapse All") {
			@Override
			public void run() {
				viewer.collapseAll();
			}
			// }
		};
		actionBars.getToolBarManager().add(collapseAllAction);

		actionBars.getToolBarManager().update(true);

	}

	protected void initContextMenu(final StructuredViewer viewer) {
		final MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(this);
		final Menu menu = contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu, new UnwrappingSelectionProvider(viewer));

	}

	protected void initDragAndDrop(final StructuredViewer viewer) {

		final int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		final Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
		// viewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(editingDomain, viewer));
		viewer.addDropSupport(dndOperations, transfers, new DiffViewDropTargetAdapter());
	}

	protected void hookToScheduleView() {
		listener = new IPartListener() {

			@Override
			public void partOpened(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = viewPart;
						observeInput((Table) scheduleView.getAdapter(Table.class));
					}
				}
			}

			private void observeInput(final Table table) {
				if (viewer.getInput() != table) {
					viewer.setInput(table);
					DiffGroupView.this.table = table;
					//
					// if (newUserGroupAction != null) {
					// newUserGroupAction.setTable(table);
					// }
				}
			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {

			}

			@Override
			public void partClosed(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = null;
						observeInput(null);
					}
				}

			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = viewPart;
						observeInput((Table) scheduleView.getAdapter(Table.class));
					}
				}
			}
		};
		getViewSite().getPage().addPartListener(listener);
	}

	protected void createColumns(final GridTreeViewer viewer) {
		final DataModelCellEditingSupport editingSupport = new DataModelCellEditingSupport(viewer, dbc);
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setTree(true);

			gvc.setEditingSupport(editingSupport);
			final IObservableMap[] attributeMaps = new IObservableMap[3];
			attributeMaps[0] = EMFProperties.value(ScheduleReportPackage.Literals.USER_GROUP__COMMENT).observeDetail(contentProvider.getKnownElements());
			attributeMaps[1] = EMFProperties.value(ScheduleReportPackage.Literals.CYCLE_GROUP__INDEX).observeDetail(contentProvider.getKnownElements());
			attributeMaps[2] = EMFProperties.value(ScheduleReportPackage.Literals.ROW__NAME).observeDetail(contentProvider.getKnownElements());
			gvc.setLabelProvider(new DataModelLabelProvider(adapterFactory, attributeMaps));

			// editingSupport.create(viewer, dbc, cellEditor, cellEditorProperty, elementProperty)
			// gvc.setLabelProvider(new DataModelLabelProvider(adapterFactory));
			gvc.getColumn().setWidth(150);

		}
		// {
		// final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
		// // gvc.getColumn().setTree(true);
		//
		// // gvc.setEditingSupport(editingSupport);
		// IObservableMap[] attributeMaps = new IObservableMap[1];
		// attributeMaps[0] = EMFProperties.value(ScheduleReportPackage.Literals.USER_GROUP__COMMENT).observeDetail(contentProvider.getKnownElements());
		// // attriuteMaps[1] = EMFProperties.value(ScheduleReportPackage.Literals.CYCLE_GROUP__INDEX).observeDetail(contentProvider.getKnownElements());
		// // attributeMaps[2] = EMFProperties.value(ScheduleReportPackage.Literals.ROW__NAME).observeDetail(contentProvider.getKnownElements());
		// gvc.setLabelProvider(new ObservableMapCellLabelProvider(attributeMaps[0]));
		//
		// // editingSupport.create(viewer, dbc, cellEditor, cellEditorProperty, elementProperty)
		// // gvc.setLabelProvider(new DataModelLabelProvider(adapterFactory));
		// gvc.getColumn().setWidth(100);
		//
		// }
		// {
		// final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
		// gvc.getColumn().setText("Group Delta");
		// gvc.getColumn().setWidth(50);
		// gvc.setLabelProvider(new UserGroupDeltaCellLabelProvider());
		// }
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Delta");

			final IObservableMap[] attributeMaps = new IObservableMap[2];
			attributeMaps[0] = EMFProperties.value(ScheduleReportPackage.Literals.USER_GROUP__DELTA).observeDetail(contentProvider.getKnownElements());
			attributeMaps[1] = EMFProperties.value(ScheduleReportPackage.Literals.CYCLE_GROUP__DELTA).observeDetail(contentProvider.getKnownElements());
			// attributeMaps[2] = EMFProperties.value(ScheduleReportPackage.Literals.ROW__NAME).observeDetail(contentProvider.getKnownElements());
			gvc.setLabelProvider(new MixedDeltaCellLabelProvider(attributeMaps));
			gvc.getColumn().setWidth(100);
		}
		// {
		// final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
		// gvc.getColumn().setText("Delta");
		// gvc.setLabelProvider(new CycleGroupDeltaCellLabelProvider());
		// gvc.getColumn().setWidth(50);
		// }
		// {
		// final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
		// gvc.getColumn().setText("Main Change");
		// gvc.setLabelProvider(new MainChangeCellLabelProvider());
		// gvc.getColumn().setWidth(50);
		// }
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		if (part == this || part instanceof PropertySheet) {
			return;
		}
		if (viewer.getInput() != null) {
			viewer.setSelection(selection, true);
		}
	}

	@Override
	public void menuAboutToShow(final IMenuManager manager) {

		final ISelection selection = viewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			if (structuredSelection.getFirstElement() instanceof CycleGroup) {
				ExtractCycleGroupAction action = new ExtractCycleGroupAction();
				manager.add(action);
				action.selectionChanged(structuredSelection);

			}
			if (structuredSelection.size() > 1) {
				manager.add(new MergeAction(structuredSelection, table));
			}
			if (structuredSelection.getFirstElement() instanceof UserGroup) {
				final DeleteDiffAction action = new DeleteDiffAction();
				action.selectionChanged(structuredSelection);
				manager.add(action);
			}
		}
	}

}
