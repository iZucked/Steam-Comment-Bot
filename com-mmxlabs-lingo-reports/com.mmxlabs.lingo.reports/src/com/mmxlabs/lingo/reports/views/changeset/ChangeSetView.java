/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
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
import org.eclipse.jface.viewers.ViewerComparator;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonServiceTransformer;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetToTableTransformer.SortMode;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewColumnHelper.VesselData;
import com.mmxlabs.lingo.reports.views.changeset.InsertionPlanGrouperAndFilter.GroupMode;
import com.mmxlabs.lingo.reports.views.changeset.actions.CreateSandboxAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.ExportChangeAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.MergeChangesAction;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ChangeSetView extends ViewPart {

	public static enum ViewMode {
		COMPARE, OLD_ACTION_SET, NEW_ACTION_SET, INSERTIONS, GENERIC
	}

	public static class ViewState {

		public ChangeSetRoot root;

		public ChangeSetTableRoot tableRootAlternative;
		public ChangeSetTableRoot tableRootDefault;

		public @Nullable AnalyticsSolution lastSolution;
		public @Nullable NamedObject lastTargetSlot;
		public final Collection<Slot> allTargetSlots = new HashSet<>();

		public final SortMode displaySortMode;
		public Consumer<ChangeSetTableRoot> postProcess = (cs) -> {
		};

		public ViewState(final ChangeSetRoot root, final SortMode displaySortMode) {
			this.root = root;
			this.displaySortMode = displaySortMode;
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
			this.tableRootDefault = null;
			this.tableRootAlternative = null;
		}

	}

	private ViewState currentViewState = null;

	private static final String KEY_RESTORE_ANALYTICS_SOLUTION = "restore-analytics-solution-from-tags";

	private GridTreeViewer viewer;

	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	private ESelectionService eSelectionService;

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

					return new ViewState(newRoot, SortMode.BY_GROUP);
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

	private boolean showAlternativeChangeModel = false;
	private boolean showNonStructuralChanges = false;
	private boolean showRelatedChangesMenus = false;
	private boolean showUserFilterMenus = false;
	private boolean showNegativePNLChanges = true;

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

			final Set<VesselData> vesselnames = new LinkedHashSet<>();

			final ChangeSetTableRoot tableRootDefault = newViewState.tableRootDefault;
			if (tableRootDefault != null) {
				for (final ChangeSetTableGroup group : tableRootDefault.getGroups()) {
					for (final ChangeSetTableRow csr : group.getRows()) {
						vesselnames.add(new VesselData(csr.getBeforeVesselName(), csr.getBeforeVesselShortName(), csr.getBeforeVesselType()));
						vesselnames.add(new VesselData(csr.getAfterVesselName(), csr.getAfterVesselShortName(), csr.getAfterVesselType()));
					}
				}
			}
			final ChangeSetTableRoot tableRootAlternative = newViewState.tableRootAlternative;
			if (tableRootAlternative != null) {
				for (final ChangeSetTableGroup group : tableRootAlternative.getGroups()) {
					for (final ChangeSetTableRow csr : group.getRows()) {
						vesselnames.add(new VesselData(csr.getBeforeVesselName(), csr.getBeforeVesselShortName(), csr.getBeforeVesselType()));
						vesselnames.add(new VesselData(csr.getAfterVesselName(), csr.getAfterVesselShortName(), csr.getAfterVesselType()));
					}
				}
			}

			columnHelper.updateVesselColumns(vesselnames);

			// Force header size recalculation
			viewer.getGrid().recalculateHeader();

			final ViewState oldRoot = currentViewState;

			final ChangeSetTableRoot newTable = showAlternativeChangeModel ? tableRootAlternative : tableRootDefault;
			// Release after creating the new one so we increment reference counts before
			// decrementing, which could cause a scenario unload/load cycle

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

						final ViewState newViewState = new ViewState(null, SortMode.BY_GROUP);
						final ChangeSetRoot newRoot = new ScheduleResultListTransformer().createDataModel(scenarios, new NullProgressMonitor());
						ChangeSetToTableTransformer changeSetToTableTransformer = new ChangeSetToTableTransformer();
						final ChangeSetTableRoot tableRootDefault = changeSetToTableTransformer.createViewDataModel(newRoot, false, null, SortMode.BY_GROUP);
						final ChangeSetTableRoot tableRootAlternative = changeSetToTableTransformer.createViewDataModel(newRoot, true, null, SortMode.BY_GROUP);
						changeSetToTableTransformer.bindModels(newViewState.tableRootDefault, newViewState.tableRootAlternative);

						newViewState.postProcess.accept(tableRootDefault);
						newViewState.postProcess.accept(tableRootAlternative);
						newViewState.root = newRoot;
						newViewState.tableRootDefault = tableRootDefault;
						newViewState.tableRootAlternative = tableRootAlternative;

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
		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return (T) new IReportContentsGenerator() {

				public String getStringContents(final ScenarioResult pin, final ScenarioResult other) {
					try {
						columnHelper.setTextualVesselMarkers(true);
						// Need to refresh the view to trigger creation of the text labels
						final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
						scheduleDiffUtils.setCheckAssignmentDifferences(true);
						scheduleDiffUtils.setCheckSpotMarketDifferences(true);
						scheduleDiffUtils.setCheckNextPortDifferences(true);

						final ISelectedDataProvider selectedDataProvider = SelectedScenariosService.createTestingSelectedDataProvider(pin, other);
						final ScenarioComparisonServiceTransformer.TransformResult result = ScenarioComparisonServiceTransformer.transform(pin, Collections.singletonList(other), selectedDataProvider,
								scheduleDiffUtils, Collections.emptyList());
						result.selectedDataProvider = selectedDataProvider;
						final Table table = result.table;

						// Take a copy of current diff options
						// table.setOptions(EcoreUtil.copy(diffOptions));

						ChangeSetView.this.setNewDataData(pin, (monitor, targetSlotId) -> {

							final ScenarioComparisonTransformer transformer = new ScenarioComparisonTransformer();
							final ChangeSetRoot newRoot = transformer.createDataModel(selectedDataProvider, result.equivalancesMap, table, pin, other, monitor);

							return new ViewState(newRoot, SortMode.BY_GROUP);
						}, false, null);
						ViewerHelper.refresh(viewer, true);

						final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);

						final String contents = util.convert();

						ChangeSetView.this.setEmptyData();
						// Prefix this header for rendering purposes
						return "<meta charset=\"UTF-8\"/>" + contents;

					} finally {
						columnHelper.setTextualVesselMarkers(false);
					}
				}
			};

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

	protected boolean showToggleDiffToBaseAction = false;
	protected boolean showGroupByMenu = false;
	protected boolean showChangeTargetMenu = false;
	protected boolean showNegativePNLChangesMenu = false;

	private ActionContributionItem toggleDiffToBaseActionItem;

	@Override
	public void createPartControl(final Composite parent) {
		scenarioComparisonService = PlatformUI.getWorkbench().getService(ScenarioComparisonService.class);
		eSelectionService = PlatformUI.getWorkbench().getService(ESelectionService.class);
		scenarioSelectionProvider = PlatformUI.getWorkbench().getService(IScenarioServiceSelectionProvider.class);
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
								scenarioSelectionProvider.setPinnedPair(changeSetTableGroup.getBaseScenario(), changeSetTableGroup.getCurrentScenario(), true);
								break;
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

						while (selectedElements.remove(null));

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

				if (!showNegativePNLChanges) {

					if (element instanceof ChangeSetTableGroup) {
						final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) element;
						final long totalPNLDelta = changeSet.getDeltaMetrics().getPnlDelta();
						if (totalPNLDelta <= 0) {
							return false;
						}
					} else {

						if (parentElement instanceof ChangeSetTableGroup) {
							final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) parentElement;
							final long totalPNLDelta = changeSet.getDeltaMetrics().getPnlDelta();
							if (totalPNLDelta <= 0) {
								return false;
							}
						}
					}
				}
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

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final Object original_e1 = e1;
				final Object original_e2 = e2;

				// If both rows of the same parent group..
				if (e1 instanceof ChangeSetTableRow && e2 instanceof ChangeSetTableRow) {
					// Retain original ordering in the datamodel
					final ChangeSetTableRow r1 = (ChangeSetTableRow) e1;
					final ChangeSetTableRow r2 = (ChangeSetTableRow) e2;
					if (r1.eContainer() == r2.eContainer()) {
						final ChangeSetTableGroup g = (ChangeSetTableGroup) r1.eContainer();
						return g.getRows().indexOf(r1) - g.getRows().indexOf(r2);
					}
				}

				ChangeSetTableGroup g1 = null;
				ChangeSetTableGroup g2 = null;

				if (e1 instanceof ChangeSetTableGroup) {
					g1 = (ChangeSetTableGroup) e1;
				}
				if (e2 instanceof ChangeSetTableGroup) {
					g2 = (ChangeSetTableGroup) e2;
				}
				if (g1 != null && g2 != null) {
					if (insertionPlanFilter.getUserFilters().isEmpty()) {
						if (!Objects.equal(g1.getGroupObject(), g2.getGroupObject())) {
							return Double.compare(g2.getGroupSortValue(), g1.getGroupSortValue());

						}
					}
					return -Double.compare(g2.getSortValue(), g1.getSortValue());
				}

				return super.compare(viewer, original_e1, original_e2);
			}
		});

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

		makeActions();

		setViewMode(ViewMode.COMPARE);

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

	public void makeActions() {
		getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("start"));

		{
			copyAction = CopyToClipboardActionFactory.createCopyToHtmlClipboardAction(viewer, true);
			getViewSite().getActionBars().getToolBarManager().add(copyAction);
		}
		{
			toggleDiffToBaseAction = new RunnableAction("Show all changes to base", SWT.PUSH, () -> {
				doToggleDiffToBase();
			});
			toggleDiffToBaseAction.setToolTipText("Toggle comparing to base or previous case");
			toggleDiffToBaseAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/compare_to_base.gif"));
			final GroupMarker group = new GroupMarker("diffToBaseGroup");
			getViewSite().getActionBars().getToolBarManager().add(group);
			toggleDiffToBaseActionItem = new ActionContributionItem(toggleDiffToBaseAction);
			if (showToggleDiffToBaseAction) {
				getViewSite().getActionBars().getToolBarManager().appendToGroup("diffToBaseGroup", toggleDiffToBaseActionItem);
			}
		}

		{
			getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("filters"));
			filterMenu = new AbstractMenuAction("Filter...") {

				@Override
				protected void populate(final Menu menu) {
					// TODO Auto-generated method stub
					if (showGroupByMenu) {
						final AbstractMenuAction groupModeAction = new AbstractMenuAction("Group by") {
							@Override
							protected void populate(final Menu menu2) {
								final RunnableAction mode_Target = new RunnableAction("Target", SWT.PUSH, () -> doSwitchGroupByModel(GroupMode.Target));

								final RunnableAction mode_TargetComplexity = new RunnableAction("Target and complexity", SWT.PUSH, () -> doSwitchGroupByModel(GroupMode.TargetAndComplexity));
								final RunnableAction mode_Complextiy = new RunnableAction("Complexity", SWT.PUSH, () -> doSwitchGroupByModel(GroupMode.Complexity));

								switch (insertionPlanFilter.getGroupMode()) {
								case Complexity:
									mode_Complextiy.setChecked(true);
									break;
								case Target:
									mode_Target.setChecked(true);
									break;
								case TargetAndComplexity:
									mode_TargetComplexity.setChecked(true);
									break;
								default:
									break;
								}
								addActionToMenu(mode_Target, menu2);
								addActionToMenu(mode_TargetComplexity, menu2);
								addActionToMenu(mode_Complextiy, menu2);

								final int ii = 0;
							};
						};
						groupModeAction.setToolTipText("Change the grouping choice");
						addActionToMenu(groupModeAction, menu);
					}
					final RunnableAction toggleStructuralChanges = new RunnableAction("Show non-structural changes", () -> doShowStructuralChangesToggle());
					toggleStructuralChanges.setToolTipText("Toggling filtering of non structural changes");
					toggleStructuralChanges.setChecked(showNonStructuralChanges);
					// toggleStructuralChanges.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
					addActionToMenu(toggleStructuralChanges, menu);

					if (showNegativePNLChangesMenu) {
						final RunnableAction toggleNegativePNL = new RunnableAction("Show negative PNL Changes", () -> doShowNegativePNLToggle());
						toggleNegativePNL.setToolTipText("Toggling filtering of negative PNL");
						toggleNegativePNL.setChecked(showNegativePNLChanges);
						// item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						addActionToMenu(toggleNegativePNL, menu);
					}
					if (showChangeTargetMenu) {
						if (currentViewState != null && currentViewState.allTargetSlots.size() > 1) {

							final AbstractMenuAction targetMenu = new AbstractMenuAction("Target... ") {
								@Override
								protected void populate(final Menu menu2) {

									for (final Slot target : currentViewState.allTargetSlots) {
										final RunnableAction action = new RunnableAction(target.getName(), SWT.PUSH, () -> openAnalyticsSolution(currentViewState.lastSolution, target.getName()));
										if (currentViewState.lastTargetSlot == target) {
											action.setChecked(true);
											action.setEnabled(false);
										}
										addActionToMenu(action, menu2);
									}
								}
							};
							targetMenu.setToolTipText("Select target element");
							addActionToMenu(targetMenu, menu);
						}
					}

				}
			};
			filterMenu.setToolTipText("Change filters");
			filterMenu.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/filter.gif"));
			getViewSite().getActionBars().getToolBarManager().add(filterMenu);
		}
		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	public void setNewDataData(final Object target, final BiFunction<IProgressMonitor, @Nullable String, ViewState> action, final @Nullable String targetSlotId) {
		setNewDataData(target, action, true, targetSlotId);
	}

	public void setNewDataData(final Object target, final BiFunction<IProgressMonitor, @Nullable String, ViewState> action, final boolean runAsync, final @Nullable String targetSlotId) {

		columnHelper.cleanUpVesselColumns();

		if (target == null) {
			setEmptyData();
		} else {
			final Display display = PlatformUI.getWorkbench().getDisplay();
			final Shell activeShell = display.getActiveShell();
			try {
				final IRunnableWithProgress runnable = new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						final ViewState newViewState = action.apply(monitor, targetSlotId);
						final ChangeSetToTableTransformer changeSetToTableTransformer = new ChangeSetToTableTransformer();
						newViewState.tableRootAlternative = changeSetToTableTransformer.createViewDataModel(newViewState.root, true, newViewState.lastTargetSlot, newViewState.displaySortMode);
						newViewState.tableRootDefault = changeSetToTableTransformer.createViewDataModel(newViewState.root, false, newViewState.lastTargetSlot, newViewState.displaySortMode);

						changeSetToTableTransformer.bindModels(newViewState.tableRootDefault, newViewState.tableRootAlternative);

						newViewState.postProcess.accept(newViewState.tableRootDefault);
						newViewState.postProcess.accept(newViewState.tableRootAlternative);

						if (runAsync) {
							display.asyncExec(new ViewUpdateRunnable(newViewState));
						} else {
							display.syncExec(new ViewUpdateRunnable(newViewState));
						}

					}
				};
				if (runAsync) {
					if (activeShell != null) {
						final ProgressMonitorDialog d = new ProgressMonitorDialog(activeShell);
						d.run(true, false, runnable);
					} else {
						display.asyncExec(() -> {
							try {
								runnable.run(new NullProgressMonitor());
							} catch (InvocationTargetException | InterruptedException e) {
								e.printStackTrace();
							}
						});
					}
				} else {
					runnable.run(new NullProgressMonitor());
				}
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void setEmptyData() {
		final ChangeSetRoot newRoot = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		final ChangeSetTableRoot newTableRoot = ChangesetFactory.eINSTANCE.createChangeSetTableRoot();

		final ViewState newViewState = new ViewState(null, SortMode.BY_GROUP);
		newViewState.root = newRoot;
		newViewState.tableRootAlternative = newTableRoot;
		newViewState.tableRootDefault = newTableRoot;

		RunnerHelper.exec(new ViewUpdateRunnable(newViewState), true);
	}

	public void cleanUp(final ViewState viewState) {
		if (viewState != null) {
			viewState.dispose();
		}
	}

	@PreDestroy
	public void dispose() {
		if (lastParent != null) {
			lastParent.eAdapters().remove(adapter);
		}
		lastObject = null;
		lastParent = null;
		lastParentFeature = null;

		scenarioComparisonService.removeListener(listener);
		cleanUp(this.currentViewState);
		if (columnHelper != null) {
			columnHelper.getDiagram().setChangeSetRoot(ChangesetFactory.eINSTANCE.createChangeSetTableRoot());
		}
		this.currentViewState = null;

		columnHelper.cleanUpVesselColumns();
		columnHelper.dispose();

		// The post selection from this view can be left in the e4 context somehow.
		if (eSelectionService != null) {
			eSelectionService.setPostSelection(new StructuredSelection());
			eSelectionService = null;
		}
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
		ViewerHelper.runIfViewerValid(viewer, true, AbstractTreeViewer::expandAll);
	}

	private void doToggleDiffToBase() {
		showAlternativeChangeModel = !showAlternativeChangeModel;
		if (toggleDiffToBaseAction != null) {
			toggleDiffToBaseAction.setChecked(showAlternativeChangeModel);
		}
		final ViewState viewState = currentViewState;
		if (viewState != null) {
			if (showAlternativeChangeModel && viewState.tableRootAlternative != null) {
				columnHelper.getDiagram().setChangeSetRoot(viewState.tableRootAlternative);
				ViewerHelper.setInput(viewer, true, viewState.tableRootAlternative);
			} else {
				showAlternativeChangeModel = false;
				columnHelper.getDiagram().setChangeSetRoot(viewState.tableRootDefault);
				ViewerHelper.setInput(viewer, true, viewState.tableRootDefault);
			}
		}
	}

	private void doShowStructuralChangesToggle() {
		showNonStructuralChanges = !showNonStructuralChanges;
		ViewerHelper.refreshThen(viewer, true, AbstractTreeViewer::expandAll);
	}

	private void doShowNegativePNLToggle() {
		showNegativePNLChanges = !showNegativePNLChanges;
		ViewerHelper.refreshThen(viewer, true, AbstractTreeViewer::expandAll);
	}

	private void doSwitchGroupByModel(final GroupMode mode) {
		insertionPlanFilter.setGroupMode(mode);
		final ViewState viewState = currentViewState;

		if (viewState != null) {
			final String id = viewState.getTargetSlotID();
			openAnalyticsSolution(viewState.lastSolution, id);
		}
	}

	public void openOldStlyeActionSets(final ScenarioInstance target) {
		setPartName("Action Sets");
		setViewMode(ViewMode.OLD_ACTION_SET);
		setNewDataData(target, (monitor, targetSlotId) -> {
			final ActionSetTransformer transformer = new ActionSetTransformer();
			return new ViewState(transformer.createDataModel(target, monitor), SortMode.BY_GROUP);
		}, null);
	}

	private void openView(final @NonNull String viewId) {
		// Open, but do not focus the view.
		// FIXME: If this causes the view to be created the selection will not be
		// correct. However attempting to re-set the selection does not appear to work.
		try {
			getSite().getPage().showView(viewId, null, IWorkbenchPage.VIEW_VISIBLE);
		} catch (final PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
					if (showRelatedChangesMenus) {
						if (obj instanceof ChangeSetTableGroup) {
							final ChangeSetTableGroup changeSetTableGroup = (ChangeSetTableGroup) obj;
							if (insertionPlanFilter.getUserFilters().isEmpty()) {
								if (insertionPlanFilter.getExpandedGroups().contains(changeSetTableGroup.getGroupObject())) {
									helper.addAction(new RunnableAction("Hide related changes", () -> {
										insertionPlanFilter.getExpandedGroups().remove(changeSetTableGroup.getGroupObject());
										ViewerHelper.refreshThen(viewer, true, AbstractTreeViewer::expandAll);
									}));
								} else {
									helper.addAction(new RunnableAction("Show related changes", () -> {
										insertionPlanFilter.getExpandedGroups().add(changeSetTableGroup.getGroupObject());
										ViewerHelper.refreshThen(viewer, true, AbstractTreeViewer::expandAll);
									}));
								}
							}
						}
					}
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
						{
							final ChangeSetTableGroup changeSetTableGroup = selectedSets.iterator().next();
							int idx = 0;
							final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSetTableGroup.eContainer();
							if (root != null) {
								idx = root.getGroups().indexOf(changeSetTableGroup);
							}
							final String name = columnHelper.getChangeSetColumnLabelProvider().apply(changeSetTableGroup, idx);

							helper.addAction(new ExportChangeAction(changeSetTableGroup, name));
							showMenu = true;
						}
						// Experimental code to generate a sandbox scenario.
						if (false && ChangeSetView.this.viewMode == ViewMode.INSERTIONS) {
							// This does not work as insertion scenario is read-only. Data model is also
							// unstable (not sure if containment works right.
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

				if (showUserFilterMenus) {
					showMenu |= insertionPlanFilter.generateMenus(helper, viewer, directSelectedRows, selectedSets, currentViewState.lastTargetSlot);
				}
			} else {
				if (showUserFilterMenus) {
					if (currentViewState != null) {
						showMenu |= insertionPlanFilter.generateMenus(helper, viewer, Collections.emptySet(), Collections.emptySet(), currentViewState.lastTargetSlot);
					}
				}
			}

			if (showMenu) {
				helper.open();
			}
		}
	}

	public void openAnalyticsSolution(final AnalyticsSolution solution) {
		openAnalyticsSolution(solution, null);
	}

	private EObject lastParent = null;
	private EObject lastObject = null;
	private EStructuralFeature lastParentFeature = null;

	private final Adapter adapter = new AdapterImpl() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			if (msg.getEventType() == Notification.REMOVE) {
				if (msg.getFeature() == lastParentFeature && msg.getOldValue() == lastObject) {
					setEmptyData();
					getSite().getPage().hideView(ChangeSetView.this);
				}
			}
		};

	};

	private Action copyAction;
	private Action toggleDiffToBaseAction;

	private AbstractMenuAction filterMenu;;

	public void openAnalyticsSolution(final AnalyticsSolution solution, @Nullable final String slotId) {

		if (lastParent != null) {
			lastParent.eAdapters().remove(adapter);
		}
		lastObject = null;
		lastParent = null;
		lastParentFeature = null;

		this.viewMode = ViewMode.OLD_ACTION_SET;

		final ScenarioInstance target = solution.getScenarioInstance();
		final EObject plan = solution.getSolution();
		setPartName(solution.getTitle());

		lastParent = plan.eContainer();
		if (lastParent != null) {
			lastObject = plan;
			lastParentFeature = plan.eContainingFeature();
			lastParent.eAdapters().add(adapter);
		}

		// Do something?
		if (plan instanceof OptimisationResult) {
			setViewMode(ViewMode.GENERIC);
			setNewDataData(target, (monitor, targetSlotId) -> {
				final OptimisationResultPlanTransformer transformer = new OptimisationResultPlanTransformer();
				// Sorting by Group as the label provider uses the provided ordering for indexing
				final ViewState viewState = new ViewState(transformer.createDataModel(target, (OptimisationResult) plan, monitor), SortMode.BY_GROUP);
				viewState.lastSolution = solution;
				return viewState;
			}, slotId);
		} else if (plan instanceof ActionableSetPlan) {
			setViewMode(ViewMode.NEW_ACTION_SET);
			setNewDataData(target, (monitor, targetSlotId) -> {
				final ActionableSetPlanTransformer transformer = new ActionableSetPlanTransformer();
				final ViewState viewState = new ViewState(transformer.createDataModel(target, (ActionableSetPlan) plan, monitor), SortMode.BY_GROUP);
				viewState.lastSolution = solution;
				return viewState;
			}, slotId);
		} else if (plan instanceof SlotInsertionOptions) {
			final SlotInsertionOptions slotInsertionOptions = (SlotInsertionOptions) plan;
			setViewMode(ViewMode.INSERTIONS);
			setNewDataData(target, (monitor, targetSlotId) -> {

				NamedObject pTargetSlot = null;
				if (!slotInsertionOptions.getSlotsInserted().isEmpty()) {
					Slot targetSlot = slotInsertionOptions.getSlotsInserted().get(0);
					if (slotId != null) {
						for (final Slot s : slotInsertionOptions.getSlotsInserted()) {
							if (slotId.equals(s.getName())) {
								targetSlot = s;
								break;
							}
						}
					}
					pTargetSlot = targetSlot;
				} else if (!slotInsertionOptions.getEventsInserted().isEmpty()) {
					VesselEvent targetSlot = slotInsertionOptions.getEventsInserted().get(0);
					if (slotId != null) {
						for (final VesselEvent s : slotInsertionOptions.getEventsInserted()) {
							if (slotId.equals(s.getName())) {
								targetSlot = s;
								break;
							}
						}
					}
					pTargetSlot = targetSlot;
				}
				final ViewState viewState = new ViewState(null, SortMode.BY_PNL);
				viewState.lastSolution = solution;
				viewState.lastTargetSlot = pTargetSlot;
				viewState.allTargetSlots.clear();
				viewState.allTargetSlots.addAll(slotInsertionOptions.getSlotsInserted());

				final InsertionPlanTransformer transformer = new InsertionPlanTransformer();
				final ChangeSetRoot newRoot = transformer.createDataModel(target, slotInsertionOptions, monitor, pTargetSlot);

				viewState.postProcess = insertionPlanFilter.processChangeSetRoot(newRoot, pTargetSlot);

				viewState.root = newRoot;
				return viewState;
			}, slotId);
		}
	}

	public AnalyticsSolution getLastSolution() {
		if (currentViewState != null) {
			return currentViewState.lastSolution;
		}
		return null;
	}

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
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

		final Function<ScenarioResult, Boolean> checker = (sr) -> sr != null && (sr.getModelRecord() == modelRecord || sr.getScenarioInstance() == scenarioInstance);
		final ViewState viewState = currentViewState;
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
					if (checker.apply(changeSet.getAltBaseScenario())) {
						linked = true;
						break;
					}
					if (checker.apply(changeSet.getAltCurrentScenario())) {
						linked = true;
						break;
					}
				}
			}
			if (linked) {
				setEmptyData();
				// Close view if dynamic
				if (viewMode != ViewMode.COMPARE) {
					getSite().getPage().hideView(ChangeSetView.this);
				}
			}
		}
	}

	public ViewState getCurrentViewState() {
		return currentViewState;
	}

	private void setViewMode(final ViewMode viewMode) {
		// Defaults
		this.viewMode = viewMode;
		handleEvents = false;
		canExportChangeSet = true;
		showNegativePNLChanges = true;
		insertionPlanFilter.setInsertionModeActive(false);
		insertionPlanFilter.setMultipleSolutionView(true);
		insertionPlanFilter.setMaxComplexity(Integer.MAX_VALUE);
		columnHelper.setChangeSetColumnLabelProvider(columnHelper.getDefaultLabelProvider());
		showRelatedChangesMenus = false;
		showUserFilterMenus = false;
		showGroupByMenu = false;
		showChangeTargetMenu = false;
		showNegativePNLChangesMenu = false;
		showToggleDiffToBaseAction = false;

		switch (viewMode) {
		case COMPARE:
			showAlternativeChangeModel = false;
			handleEvents = true;
			canExportChangeSet = false;
			break;
		case INSERTIONS:
			insertionPlanFilter.setInsertionModeActive(true);
			insertionPlanFilter.setMultipleSolutionView(false);
			insertionPlanFilter.setMaxComplexity(4);
			columnHelper.setChangeSetColumnLabelProvider(insertionPlanFilter.createLabelProvider());
			showRelatedChangesMenus = true;
			showUserFilterMenus = true;
			showGroupByMenu = true;
			showChangeTargetMenu = true;
			showNegativePNLChangesMenu = true;
			showAlternativeChangeModel = false;
			break;
		case NEW_ACTION_SET:
			showToggleDiffToBaseAction = true;
			break;
		case OLD_ACTION_SET:
			showToggleDiffToBaseAction = true;
			break;
		case GENERIC:
			showAlternativeChangeModel = false;
			columnHelper.setChangeSetColumnLabelProvider(insertionPlanFilter.createLabelProvider());
			break;
		default:
			assert false;
			break;
		}

		if (toggleDiffToBaseAction != null) {
			toggleDiffToBaseAction.setChecked(showAlternativeChangeModel);
			getViewSite().getActionBars().getToolBarManager().remove(toggleDiffToBaseActionItem);
			if (showToggleDiffToBaseAction) {
				getViewSite().getActionBars().getToolBarManager().appendToGroup("diffToBaseGroup", toggleDiffToBaseActionItem);
			}
			getViewSite().getActionBars().getToolBarManager().update(true);
		}
	}
}
