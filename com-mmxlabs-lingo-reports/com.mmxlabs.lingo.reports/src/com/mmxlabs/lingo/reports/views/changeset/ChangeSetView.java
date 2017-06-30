/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.services.ChangeSetViewCreatorService;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.actions.CreateSandboxAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.ExportChangeAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.MergeChangesAction;
import com.mmxlabs.lingo.reports.views.changeset.handlers.SwitchGroupModeEvent;
import com.mmxlabs.lingo.reports.views.changeset.handlers.SwitchSlotEvent;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ChangeSetView implements IAdaptable {

	public static enum ViewMode {
		COMPARE, OLD_ACTION_SET, NEW_ACTION_SET, INSERTIONS
	}

	public static class ViewState {

		public ChangeSetRoot root;

		public ChangeSetTableRoot tableRootToBase;
		public ChangeSetTableRoot tableRootToPrevious;

		public @Nullable AnalyticsSolution lastSolution;
		public @Nullable NamedObject lastTargetSlot;
		public final Collection<Slot> allTargetSlots = new HashSet<>();

		public ViewState() {

		}

		public ViewState(final ChangeSetRoot root) {
			this.root = root;
		}

		public @Nullable String getTargetSlotID() {
			if (lastTargetSlot != null) {
				return lastTargetSlot.getName();
			}
			return null;
		}

		public void dispose() {
			this.allTargetSlots.clear();
			this.lastSolution = null;
			this.lastTargetSlot = null;
			this.root = null;
			this.tableRootToBase = null;
			this.tableRootToPrevious = null;
		}

	}

	private ViewState currentViewState = null;

	private static final String KEY_RESTORE_ANALYTICS_SOLUTION = "restore-analytics-solution-from-tags";

	@Inject
	private EPartService partService;

	private GridTreeViewer viewer;

	@Inject
	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	@Inject
	private ESelectionService eSelectionService;

	@Inject
	private ScenarioComparisonService scenarioComparisonService;

	private ChangeSetViewColumnHelper columnHelper;

	private final IScenarioComparisonServiceListener listener = new IScenarioComparisonServiceListener() {

		@Override
		public void multiDataUpdate(final ISelectedDataProvider selectedDataProvider, final Collection<ScenarioResult> others, final Table table, final List<LNGScenarioModel> rootObjects) {
			if (!handleEvents) {
				return;
			}
			if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
				columnHelper.cleanUpVesselColumns();
				setEmptyData();
			}
		}

		@Override
		public void compareDataUpdate(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pin, final ScenarioResult other, final Table table,
				final List<LNGScenarioModel> rootObjects, final Map<EObject, Set<EObject>> equivalancesMap) {
			if (!handleEvents) {
				return;
			}
			if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
				final ScenarioResult target = pin == null ? other : pin;
				setNewDataData(target, (monitor, targetSlotId) -> {
					final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
					scheduleDiffUtils.setCheckAssignmentDifferences(true);
					scheduleDiffUtils.setCheckSpotMarketDifferences(true);
					scheduleDiffUtils.setCheckNextPortDifferences(true);
					final ScenarioComparisonTransformer transformer = new ScenarioComparisonTransformer();
					final ChangeSetRoot newRoot = transformer.createDataModel(selectedDataProvider, equivalancesMap, table, pin, other, monitor);

					return new ViewState(newRoot);
				}, null);
			}
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {

		}
	};

	private ViewMode viewMode = ViewMode.COMPARE;
	private IAdditionalAttributeProvider additionalAttributeProvider;

	// flag to indicate whether or not to respond to data change events.
	private boolean handleEvents;

	private boolean canExportChangeSet;

	private InsertionPlanGrouperAndFilter insertionPlanFilter;

	private boolean diffToBase = false;
	private boolean showNonStructuralChanges = false;

	private boolean persistAnalyticsSolution = false;

	private final class ViewUpdateRunnable implements Runnable {
		private final ViewState newViewState;

		private ViewUpdateRunnable(final ViewState newViewState) {
			this.newViewState = newViewState;
		}

		@Override
		public void run() {
			if (viewer.getControl().isDisposed()) {
				return;
			}

			// Reset selection
			scenarioComparisonService.setSelectedElements(Collections.emptySet());

			// TODO: Extract vessel columns and generate.
			final Set<String> vesselnames = new LinkedHashSet<>();
			final Map<String, String> shortNameMap = new HashMap<>();

			final ChangeSetTableRoot csDdiffToBase = newViewState.tableRootToBase;
			if (csDdiffToBase != null) {
				for (final ChangeSetTableGroup group : csDdiffToBase.getGroups()) {
					for (final ChangeSetTableRow csr : group.getRows()) {
						vesselnames.add(csr.getBeforeVesselName());
						vesselnames.add(csr.getAfterVesselName());

						shortNameMap.put(csr.getBeforeVesselName(), csr.getBeforeVesselShortName());
						shortNameMap.put(csr.getAfterVesselName(), csr.getAfterVesselShortName());
					}
				}
			}
			final ChangeSetTableRoot csdiffToPrevious = newViewState.tableRootToPrevious;
			if (csdiffToPrevious != null) {
				for (final ChangeSetTableGroup group : csdiffToPrevious.getGroups()) {
					for (final ChangeSetTableRow csr : group.getRows()) {
						vesselnames.add(csr.getBeforeVesselName());
						vesselnames.add(csr.getAfterVesselName());

						shortNameMap.put(csr.getBeforeVesselName(), csr.getBeforeVesselShortName());
						shortNameMap.put(csr.getAfterVesselName(), csr.getAfterVesselShortName());
					}
				}
			}
			vesselnames.remove(null);
			vesselnames.remove("");

			final List<String> sortedNames = new ArrayList<>(vesselnames);
			Collections.sort(sortedNames);

			columnHelper.updateVesselColumns(sortedNames, shortNameMap);

			// Force header size recalculation
			viewer.getGrid().recalculateHeader();

			final ViewState oldRoot = currentViewState;

			final ChangeSetTableRoot newTable = diffToBase ? csDdiffToBase : csdiffToPrevious;
			// Release after creating the new one so we increment reference counts before decrementing, which could cause a scenario unload/load cycle

			currentViewState = newViewState;

			columnHelper.getDiagram().setChangeSetRoot(newTable);
			viewer.setInput(newTable);
			cleanUp(oldRoot);

			// Repack some data column
			columnHelper.packMainColumns();
		}
	}

	@Inject
	public ChangeSetView() {

		additionalAttributeProvider = new IAdditionalAttributeProvider() {

			@Override
			public void begin() {
				columnHelper.setTextualVesselMarkers(true);
				// Need to refresh the view to trigger creation of the text labels
				ViewerHelper.refresh(viewer, true);
			}

			@Override
			public void done() {
				columnHelper.setTextualVesselMarkers(false);
				// Need to refresh the view to trigger creation of the text labels
				ViewerHelper.refresh(viewer, true);
			}

			@Override
			@NonNull
			public String @Nullable [] getAdditionalHeaderAttributes(final GridColumn column) {
				// Border around all header cells.
				return new @NonNull String @Nullable [] { "style='border:1 solid #000;'" };
			}

			@Override
			public String[] getAdditionalRowHeaderAttributes(final GridItem item) {
				return new String[] { "" };
			}

			@Override
			@NonNull
			public String @Nullable [] getAdditionalAttributes(final GridItem item, final int i) {

				final StringBuilder styleBuilder = new StringBuilder();

				final GridColumn column = viewer.getGrid().getColumn(i);
				final Object data = item.getData();

				if (data instanceof ChangeSet) {
					styleBuilder.append("border-bottom: 1 dashed #000;");
					styleBuilder.append("border-top: 1 solid  #000;");

				}

				if (styleBuilder.length() != 0) {
					return new String[] { String.format("style='%s'", styleBuilder.toString()) };
				}
				return null;
			}

			@Override
			public String getTopLeftCellLowerText() {
				return "";
			}

			@Override
			public String getTopLeftCellUpperText() {

				return "";
			}

			@Override
			public String getTopLeftCellText() {
				return "";
			}

			@Override
			public String[] getAdditionalPreRows() {

				return null;
			}
		};

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IActionPlanHandler.class.isAssignableFrom(adapter)) {

			if (viewMode != ViewMode.COMPARE) {
				return (T) new IActionPlanHandler() {

					@Override
					public void displayActionPlan(final List<ScenarioResult> scenarios) {
						columnHelper.cleanUpVesselColumns();

						final ViewState newViewState = new ViewState();
						final ChangeSetRoot newRoot = new ScheduleResultListTransformer().createDataModel(scenarios, new NullProgressMonitor());
						final ChangeSetTableRoot csdiffToPrevious = new ChangeSetToTableTransformer().createViewDataModel(newRoot, false, null);
						final ChangeSetTableRoot csdiffToBase = new ChangeSetToTableTransformer().createViewDataModel(newRoot, true, null);

						newViewState.root = newRoot;
						newViewState.tableRootToBase = csdiffToBase;
						newViewState.tableRootToPrevious = csdiffToPrevious;

						RunnerHelper.exec(new ViewUpdateRunnable(newViewState), true);
					}
				};
			}
			return null;
		}
		if (IAdditionalAttributeProvider.class.isAssignableFrom(adapter)) {
			return (T) additionalAttributeProvider;
		}

		if (GridTreeViewer.class.isAssignableFrom(adapter)) {
			return (T) viewer;
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			if (viewer != null) {
				return (T) viewer.getGrid();
			}
		}

		if (IReportContents.class.isAssignableFrom(adapter)) {

			try {
				columnHelper.setTextualVesselMarkers(true);
				// Need to refresh the view to trigger creation of the text labels
				ViewerHelper.refresh(viewer, true);
				final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);

				final String contents = util.convert();
				return (T) new IReportContents() {

					@Override
					public String getStringContents() {
						// Prefix this header for rendering purposes
						return "<meta charset=\"UTF-8\"/>" + contents;
					}

				};
			} finally {
				columnHelper.setTextualVesselMarkers(false);
			}
		}
		return (T) null;
	}

	private final Queue<Runnable> postCreateActions = new ConcurrentLinkedQueue<>();
	private boolean viewCreated = false;

	@PostConstruct
	public void createPartControl(@Optional final MPart part, final Composite parent) {
		handleEvents = true;
		if (part != null) {
			for (final String tag : part.getTags()) {
				if (tag.equals(ChangeSetViewCreatorService.TAG_TYPE_OLD_ACTION_SET)) {
					viewMode = ViewMode.OLD_ACTION_SET;
				}
				if (tag.equals(ChangeSetViewCreatorService.TAG_TYPE_NEW_ACTION_SET)) {
					viewMode = ViewMode.NEW_ACTION_SET;
				}
				if (tag.equals(ChangeSetViewCreatorService.TAG_TYPE_INSERTIONS)) {
					viewMode = ViewMode.INSERTIONS;
				}
				if (tag.equals("disable-event-handlers")) {
					handleEvents = false;
				}
			}
		}
		// Create table
		viewer = new GridTreeViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);

		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);

		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);

		// Note: Only applies to setInput - #refresh() will not trigger expansion
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		// Create content provider
		viewer.setContentProvider(createContentProvider());

		columnHelper = new ChangeSetViewColumnHelper(this, viewer);
		columnHelper.makeColumns();

		// Selection listener for pin/diff driver.
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					if (viewMode != ViewMode.COMPARE) {
						final Iterator<?> itr = iStructuredSelection.iterator();
						while (itr.hasNext()) {
							Object o = itr.next();
							if (o instanceof ChangeSetTableRow) {
								o = ((ChangeSetTableRow) o).eContainer();
							}
							if (o instanceof ChangeSetTableGroup) {
								final ChangeSetTableGroup changeSetTableGroup = (ChangeSetTableGroup) o;
								final ChangeSet changeSet = changeSetTableGroup.getChangeSet();
								if (changeSet != null) {
									final ScenarioResult other;
									if (diffToBase) {
										other = changeSet.getBaseScenario();
									} else {
										other = changeSet.getPrevScenario();
									}
									scenarioSelectionProvider.setPinnedPair(other, changeSet.getCurrentScenario(), true);
									break;
								}
							}
						}
					}
					{

						final Set<Object> selectedElements = new LinkedHashSet<>();
						final Iterator<?> itr = iStructuredSelection.iterator();
						while (itr.hasNext()) {
							final Object o = itr.next();
							if (o instanceof ChangeSetTableRow) {
								final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) o;
								selectRow(selectedElements, changeSetRow);
							} else if (o instanceof ChangeSetTableGroup) {
								final ChangeSetTableGroup group = (ChangeSetTableGroup) o;
								final List<ChangeSetTableRow> rows = group.getRows();
								for (final ChangeSetTableRow changeSetRow : rows) {
									selectRow(selectedElements, changeSetRow);
								}
							}
						}

						while (selectedElements.remove(null))
							;

						// Update selected elements
						scenarioComparisonService.setSelectedElements(selectedElements);

						eSelectionService.setPostSelection(new StructuredSelection(selectedElements.toArray()));
					}
				}
			}

			private void selectRow(final Set<Object> selectedElements, final ChangeSetTableRow tableRow) {
				selectedElements.add(tableRow);
				selectRow(selectedElements, tableRow.getLhsBefore());
				selectRow(selectedElements, tableRow.getLhsAfter());
				selectRow(selectedElements, tableRow.getRhsBefore());
				selectRow(selectedElements, tableRow.getRhsAfter());
			}

			private void selectRow(final Set<Object> selectedElements, final ChangeSetRowData rowData) {
				if (rowData == null) {
					return;
				}

				selectedElements.add(rowData);
				selectedElements.add(rowData.getLoadSlot());
				if (rowData.getLoadAllocation() != null) {
					selectedElements.add(rowData.getLoadAllocation());
					selectedElements.add(rowData.getLoadAllocation().getSlotVisit());
					selectedElements.add(rowData.getLoadAllocation().getSlotVisit().getSequence());
				}
				if (rowData.getDischargeAllocation() != null) {
					selectedElements.add(rowData.getDischargeAllocation());
					selectedElements.add(rowData.getDischargeAllocation().getSlotVisit());
					selectedElements.add(rowData.getDischargeAllocation().getSlotVisit().getSequence());
				}

				selectedElements.add(rowData.getLhsGroupProfitAndLoss());
				selectedElements.add(rowData.getRhsGroupProfitAndLoss());

				selectedElements.add(rowData.getLhsEvent());
				selectedElements.add(rowData.getRhsEvent());
				//
				final EventGrouping eventGrouping = rowData.getEventGrouping();
				if (eventGrouping != null) {
					selectedElements.add(eventGrouping);
					selectedElements.addAll(eventGrouping.getEvents());
				}
				if (eventGrouping instanceof Event) {
					final Event event = (Event) eventGrouping;
					selectedElements.add(event.getSequence());
				}
				// //
				if (rowData.getOpenLoadAllocation() != null) {
					selectedElements.add(rowData.getOpenLoadAllocation());
					selectedElements.add(rowData.getOpenLoadAllocation().getSlot());
				}
				if (rowData.getOpenDischargeAllocation() != null) {
					selectedElements.add(rowData.getOpenDischargeAllocation());
					selectedElements.add(rowData.getOpenDischargeAllocation().getSlot());
				}
			}
		});

		final ViewerFilter[] filters = new ViewerFilter[2];
		filters[0] = new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (!showNonStructuralChanges) {
					if (element instanceof ChangeSetTableRow) {
						final ChangeSetTableRow row = (ChangeSetTableRow) element;
						if (!row.isWiringChange() && !row.isVesselChange()) {
							final long delta = ChangeSetKPIUtil.getRowProfitAndLossValue(row, ResultType.After, ScheduleModelKPIUtils::getGroupProfitAndLoss)
									- ChangeSetKPIUtil.getRowProfitAndLossValue(row, ResultType.Before, ScheduleModelKPIUtils::getGroupProfitAndLoss);
							long totalPNLDelta = 0;
							if (parentElement instanceof ChangeSetTableGroup) {
								final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) parentElement;
								totalPNLDelta = changeSet.getDeltaMetrics().getPnlDelta();
							}
							if (Math.abs(delta) < 250_000L) {
								// Exclude if less than 10% of PNL change.
								if ((ChangeSetView.this.viewMode == ViewMode.COMPARE) || (double) Math.abs(delta) / (double) Math.abs(totalPNLDelta) < 0.1) {
									return false;
								}
							}
						}
					}
				}
				return true;
			}
		};
		insertionPlanFilter = new InsertionPlanGrouperAndFilter();
		filters[1] = insertionPlanFilter;

		viewer.setFilters(filters);

		scenarioComparisonService.addListener(listener);
		scenarioComparisonService.triggerListener(listener);

		viewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				if (viewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) viewer.getSelection();
					if (structuredSelection.isEmpty() == false) {

						// Attempt to detect the column we clicked on.
						final GridColumn column = null;
						final IWorkbench workbench = PlatformUI.getWorkbench();
						if (workbench != null) {
							final Display display = workbench.getDisplay();
							if (display != null) {
								final Point cursorLocation = display.getCursorLocation();
								if (cursorLocation != null) {

									final Grid grid = viewer.getGrid();

									final Point mousePoint = grid.toControl(cursorLocation);
									final GridColumn targetColumn = grid.getColumn(mousePoint);
									// Point cell = grid.getCell(mousePoint);
									final ViewerCell cell = viewer.getCell(mousePoint);
									if (cell != null && !cell.getText().isEmpty()) {
										if (columnHelper.getLatenessColumn() == targetColumn) {
											openView("com.mmxlabs.shiplingo.platform.reports.views.LatenessReportView");
										} else if (columnHelper.getViolationsColumn() == targetColumn) {
											openView("com.mmxlabs.shiplingo.platform.reports.views.CapacityViolationReportView");
										}
									}
								}
							}
						}
					}
				}

			}
		});

		{
			// final MenuManager mgr = new MenuManager();
			// LocalMenuHelper menuHelper = new LocalMenuHelper(viewer.getGrid());
			final ContextMenuManager listener = new ContextMenuManager();
			viewer.getGrid().addMenuDetectListener(listener);
		}
		synchronized (postCreateActions) {
			viewCreated = true;
			while (!postCreateActions.isEmpty()) {
				final Runnable r = postCreateActions.poll();
				if (r != null) {
					r.run();
				}

			}

		}
	}

	@PostConstruct
	public void makeActions() {

	}

	public void setNewDataData(final Object target, final BiFunction<IProgressMonitor, @Nullable String, ViewState> action, final @Nullable String targetSlotId) {

		columnHelper.cleanUpVesselColumns();

		if (target == null) {
			setEmptyData();
		} else {
			final Display display = PlatformUI.getWorkbench().getDisplay();
			final ProgressMonitorDialog d = new ProgressMonitorDialog(display.getActiveShell());
			try {
				d.run(true, false, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						final ViewState newViewState = action.apply(monitor, targetSlotId);
						newViewState.tableRootToBase = new ChangeSetToTableTransformer().createViewDataModel(newViewState.root, true, newViewState.lastTargetSlot);
						newViewState.tableRootToPrevious = new ChangeSetToTableTransformer().createViewDataModel(newViewState.root, false, newViewState.lastTargetSlot);
						display.asyncExec(new ViewUpdateRunnable(newViewState));
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setEmptyData() {
		final ChangeSetRoot newRoot = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		final ChangeSetTableRoot newTableRoot = ChangesetFactory.eINSTANCE.createChangeSetTableRoot();

		final ViewState newViewState = new ViewState();
		newViewState.root = newRoot;
		newViewState.tableRootToBase = newTableRoot;
		newViewState.tableRootToPrevious = newTableRoot;

		RunnerHelper.exec(new ViewUpdateRunnable(newViewState), true);
	}

	public void cleanUp(final ViewState viewState) {
		if (viewState != null) {
			viewState.dispose();
		}
	}

	@PreDestroy
	public void dispose() {

		scenarioComparisonService.removeListener(listener);
		cleanUp(this.currentViewState);
		columnHelper.getDiagram().setChangeSetRoot(ChangesetFactory.eINSTANCE.createChangeSetTableRoot());
		this.currentViewState = null;

		columnHelper.cleanUpVesselColumns();
		columnHelper.dispose();
	}

	@Focus
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	private ITreeContentProvider createContentProvider() {
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public boolean hasChildren(final Object element) {
				if (element instanceof ChangeSetTableRoot) {
					return true;
				}
				if (element instanceof ChangeSetTableGroup) {
					return true;
				}

				return false;
			}

			@Override
			public Object getParent(final Object element) {
				if (element instanceof EObject) {
					final EObject eObject = (EObject) element;
					return eObject.eContainer();
				}
				return null;
			}

			@Override
			public Object[] getElements(final Object inputElement) {

				if (inputElement instanceof ChangeSetTableRoot) {
					final ChangeSetTableRoot changeSetRoot = (ChangeSetTableRoot) inputElement;
					return changeSetRoot.getGroups().toArray();
				}

				return new Object[0];
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof ChangeSetTableGroup) {
					final ChangeSetTableGroup group = (ChangeSetTableGroup) parentElement;
					final List<ChangeSetTableRow> rows = group.getRows();
					return rows.toArray();
				}
				return null;
			}
		};
	}

	@Inject
	@Optional
	private void handleExpandAll(@UIEventTopic(ChangeSetViewEventConstants.EVENT_EXPAND_ALL) final Object o) {
		ViewerHelper.runIfViewerValid(viewer, true, v -> v.expandAll());
	}

	@Inject
	@Optional
	private void handleDiffToBaseToggle(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_COMPARE_TO_BASE) final Object o) {
		diffToBase = !diffToBase;
		final ViewState viewState = currentViewState;
		if (viewState != null) {
			if (diffToBase) {
				columnHelper.getDiagram().setChangeSetRoot(viewState.tableRootToBase);
				ViewerHelper.setInput(viewer, true, viewState.tableRootToBase);
			} else {
				columnHelper.getDiagram().setChangeSetRoot(viewState.tableRootToPrevious);
				ViewerHelper.setInput(viewer, true, viewState.tableRootToPrevious);
			}
		}
	}

	@Inject
	@Optional
	private void handleShowStructuralChangesToggle(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_NON_STRUCTURAL_CHANGES) final MPart activePart) {
		if (activePart.getObject() == this) {
			showNonStructuralChanges = !showNonStructuralChanges;
			ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
		}
	}

	@Inject
	@Optional
	private void handleToggleInsertionPlanDuplicates(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_INSERTION_CHANGES) final MPart activePart) {
		if (activePart.getObject() == this) {
			insertionPlanFilter.toggleFilter();
			ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
		}
	}

	@Inject
	@Optional
	private void handleSwitchGroupByModel(@UIEventTopic(ChangeSetViewEventConstants.EVENT_SWITCH_GROUP_BY_MODE) final SwitchGroupModeEvent event) {
		if (event.activePart.getObject() == this) {
			insertionPlanFilter.setGroupMode(event.mode);
			final ViewState viewState = currentViewState;
			if (viewState != null) {
				final String id = viewState.getTargetSlotID();

				openAnalyticsSolution(viewState.lastSolution, id);
			}
		}
	}

	@Inject
	@Optional
	private void handleSwitchSlot(@UIEventTopic(ChangeSetViewEventConstants.EVENT_SWITCH_TARGET_SLOT) final SwitchSlotEvent event) {
		if (event.activePart.getObject() == this) {
			if (currentViewState != null) {
				openAnalyticsSolution(currentViewState.lastSolution, event.slotId);
			}
		}
	}

	@Inject
	@Optional
	private void handleAnalyseScenario(@UIEventTopic(ChangeSetViewEventConstants.EVENT_ANALYSE_ACTION_SETS) final ScenarioInstance target) {
		if (!handleEvents) {
			return;
		}
		if (viewMode != ViewMode.OLD_ACTION_SET) {
			return;
		}
		this.viewMode = ViewMode.OLD_ACTION_SET;
		setNewDataData(target, (monitor, targetSlotId) -> {
			final ActionSetTransformer transformer = new ActionSetTransformer();
			return new ViewState(transformer.createDataModel(target, monitor));
		}, null);
	}

	private void openView(final @NonNull String viewId) {
		// Open, but do not focus the view.
		// FIXME: If this causes the view to be created the selection will not be correct. However attempting to re-set the selection does not appear to work.
		partService.showPart(viewId, PartState.VISIBLE);
	}

	private class ContextMenuManager implements MenuDetectListener {

		private final LocalMenuHelper helper = new LocalMenuHelper(viewer.getGrid());

		public ContextMenuManager() {
			viewer.getGrid().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(final DisposeEvent e) {
					helper.dispose();

				}
			});

		}

		@Override
		public void menuDetected(final MenuDetectEvent e) {

			helper.clearActions();

			final Grid grid = viewer.getGrid();

			final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			final GridItem[] items = grid.getSelection();
			boolean showMenu = false;
			final Set<ChangeSetTableRow> directSelectedRows = new LinkedHashSet<>();
			if (items.length >= 1) {
				final Set<ChangeSetTableGroup> selectedSets = new LinkedHashSet<>();
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof ChangeSetTableGroup) {
						selectedSets.add((ChangeSetTableGroup) obj);
					} else if (obj instanceof ChangeSetTableRow) {
						final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) obj;
						selectedSets.add((ChangeSetTableGroup) changeSetRow.eContainer());
						directSelectedRows.add(changeSetRow);
					}
				}
				if (selectedSets.size() == 1) {
					if (canExportChangeSet) {
						if (ChangeSetView.this.viewMode == ViewMode.INSERTIONS || ChangeSetView.this.viewMode == ViewMode.NEW_ACTION_SET) {
							final ChangeSetTableGroup changeSetTableGroup = selectedSets.iterator().next();
							helper.addAction(new ExportChangeAction(changeSetTableGroup));
							showMenu = true;
						}
						// Experimental code to generate a sandbox scenario.
						if (false && ChangeSetView.this.viewMode == ViewMode.INSERTIONS) {
							// This does not work as insertion scenario is read-only. Data model is also unstable (not sure if containment works right.
							final ChangeSetTableGroup changeSetTableGroup = selectedSets.iterator().next();
							helper.addAction(new CreateSandboxAction(changeSetTableGroup));
							showMenu = true;
						}
					}
				}
				if (selectedSets.size() > 1) {
					if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
						helper.addAction(new MergeChangesAction(selectedSets, viewer));
						showMenu = true;
					}

				}

				if (directSelectedRows.size() == 1) {
				}
			}
			// Experimental user filters hook.
			if (false) {
				showMenu |= insertionPlanFilter.generateMenus(helper, viewer, directSelectedRows);
			}

			if (showMenu) {
				helper.open();
			}
		}
	}

	@OpenChangeSetHandler
	public void openAnalyticsSolution(final AnalyticsSolution solution) {
		openAnalyticsSolution(solution, null);
	}

	public void openAnalyticsSolution(final AnalyticsSolution solution, @Nullable final String slotId) {
		this.viewMode = ViewMode.OLD_ACTION_SET;
		this.persistAnalyticsSolution = true;

		final ViewState viewState = new ViewState();
		viewState.lastSolution = solution;

		final ScenarioInstance target = solution.getScenarioInstance();

		final EObject plan = solution.getSolution();
		// Do something?
		if (plan instanceof ActionableSetPlan) {
			this.viewMode = ViewMode.NEW_ACTION_SET;
			this.canExportChangeSet = true;
			setNewDataData(target, (monitor, targetSlotId) -> {
				final ActionableSetPlanTransformer transformer = new ActionableSetPlanTransformer();
				return new ViewState(transformer.createDataModel(target, (ActionableSetPlan) plan, monitor));
			}, slotId);
		} else if (plan instanceof SlotInsertionOptions) {
			final SlotInsertionOptions slotInsertionOptions = (SlotInsertionOptions) plan;
			this.viewMode = ViewMode.INSERTIONS;
			this.canExportChangeSet = true;

			setNewDataData(target, (monitor, targetSlotId) -> {

				final NamedObject pTargetSlot;
				if (!slotInsertionOptions.getSlotsInserted().isEmpty()) {
					Slot targetSlot = slotInsertionOptions.getSlotsInserted().get(0);
					if (slotId != null) {
						for (Slot s : slotInsertionOptions.getSlotsInserted()) {
							if (slotId.equals(s.getName())) {
								targetSlot = s;
								break;
							}
						}
					}
					pTargetSlot = targetSlot;
				} else {
					VesselEvent targetSlot = slotInsertionOptions.getEventsInserted().get(0);
					if (slotId != null) {
						for (VesselEvent s : slotInsertionOptions.getEventsInserted()) {
							if (slotId.equals(s.getName())) {
								targetSlot = s;
								break;
							}
						}
					}
					pTargetSlot = targetSlot;
				}
				viewState.lastTargetSlot = pTargetSlot;
				viewState.allTargetSlots.clear();
				viewState.allTargetSlots.addAll(slotInsertionOptions.getSlotsInserted());

				final InsertionPlanTransformer transformer = new InsertionPlanTransformer();

				// Returns a new sort order -- based on the assumption input data is sorted by best value first
				final ChangeSetRoot newRoot = transformer.createDataModel(target, slotInsertionOptions, monitor, pTargetSlot);
				final List<ChangeSet> processChangeSetRoot = insertionPlanFilter.processChangeSetRoot(newRoot, pTargetSlot);
				newRoot.getChangeSets().clear();
				newRoot.getChangeSets().addAll(processChangeSetRoot);
				insertionPlanFilter.setFilterActive(true);

				viewState.root = newRoot;
				return viewState;
			}, slotId);
		}
	}

	@PostConstruct
	public void restoreState(final MPart part) {
		final Map<String, String> state = part.getPersistedState();
		final String value = state.get(KEY_RESTORE_ANALYTICS_SOLUTION);
		if (value != null) {
			if (value.equalsIgnoreCase("true")) {

				String scenarioUUID = null;
				String solutionUUID = null;
				for (final String tag : part.getTags()) {
					if (tag.startsWith(ChangeSetViewCreatorService.TAG_PREFIX_SCENARIO_UUID)) {
						scenarioUUID = tag.replaceFirst(ChangeSetViewCreatorService.TAG_PREFIX_SCENARIO_UUID, "");
					}
					if (tag.startsWith(ChangeSetViewCreatorService.TAG_PREFIX_SOLUTION_UUID)) {
						solutionUUID = tag.replaceFirst(ChangeSetViewCreatorService.TAG_PREFIX_SOLUTION_UUID, "");
					}
				}

				if (scenarioUUID != null && solutionUUID != null) {
					persistAnalyticsSolution = true;

					final String pScenarioUUID = scenarioUUID;
					final String pSolutionUUID = solutionUUID;

					final ScenarioInstance[] instance = new ScenarioInstance[1];
					ServiceHelper.withAllServices(IScenarioService.class, null, ss -> {

						if (ss.exists(pScenarioUUID)) {
							final TreeIterator<EObject> itr = ss.getServiceModel().eAllContents();
							while (itr.hasNext()) {
								final EObject obj = itr.next();
								if (obj instanceof ScenarioInstance) {
									final ScenarioInstance scenarioInstance = (ScenarioInstance) obj;
									if (pScenarioUUID.equals(scenarioInstance.getUuid())) {
										instance[0] = scenarioInstance;
										return false;
									}
								}
							}
						}
						return true;
					});
					if (instance[0] != null) {
						@NonNull
						final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance[0]);
						UUIDObject solution = null;
						try (ModelReference ref = modelRecord.aquireReference("ChangeSetView:restore")) {
							final EObject modelInstance = ref.getInstance();
							if (modelInstance instanceof LNGScenarioModel) {
								final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelInstance;
								final AnalyticsModel analyticsModel = scenarioModel.getAnalyticsModel();
								for (final EObject obj : analyticsModel.eContents()) {
									if (obj instanceof UUIDObject) {
										final UUIDObject uuidObject = (UUIDObject) obj;
										if (pSolutionUUID.contentEquals(uuidObject.getUuid())) {
											solution = uuidObject;
											break;
										}
									}
								}
								if (solution != null) {

									final UUIDObject pSolution = solution;
									synchronized (postCreateActions) {
										final ModelReference ref2 = modelRecord.aquireReference("ChangeSetView:delayedLoad");
										final Runnable r = () -> {
											final AnalyticsSolution analyticsSolution = new AnalyticsSolution(instance[0], pSolution, part.getLabel());
											openAnalyticsSolution(analyticsSolution, null);
											ref2.close();
										};
										if (viewCreated) {
											r.run();
										} else {
											postCreateActions.add(r);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@PersistState
	public void persistState(final MPart part) {
		if (persistAnalyticsSolution) {
			final Map<String, String> state = part.getPersistedState();
			state.put(KEY_RESTORE_ANALYTICS_SOLUTION, "true");
		}
	}

	@GetCurrentAnalyticsSolution
	public AnalyticsSolution getLastSolution() {
		if (currentViewState != null) {
			return currentViewState.lastSolution;
		}
		return null;
	}

	@GetCurrentTargetObject
	public NamedObject getLastSlot() {
		if (currentViewState != null) {
			return currentViewState.lastTargetSlot;
		}
		return null;
	}

	@Inject
	@Optional
	public void onClosingScenario(@UIEventTopic(ScenarioServiceUtils.EVENT_CLOSING_SCENARIO_INSTANCE) final ScenarioInstance scenarioInstance) {

		@NonNull
		ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

		final Function<ScenarioResult, Boolean> checker = (sr) -> sr != null && (sr.getModelRecord() == modelRecord || sr.getScenarioInstance() == scenarioInstance);
		ViewState viewState = currentViewState;
		if (viewState != null) {
			final ChangeSetRoot pRoot = viewState.root;
			boolean linked = false;
			if (pRoot != null) {
				for (final ChangeSet changeSet : pRoot.getChangeSets()) {
					if (checker.apply(changeSet.getBaseScenario())) {
						linked = true;
						break;
					}
					if (checker.apply(changeSet.getCurrentScenario())) {
						linked = true;
						break;
					}
					if (checker.apply(changeSet.getPrevScenario())) {
						linked = true;
						break;
					}
				}
			}
			if (linked) {
				setEmptyData();
				// TODO: Close view if dynamic
			}
		}
	}

	public ViewState getCurrentViewState() {
		return currentViewState;
	}
}
