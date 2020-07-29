/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.ZonedDateTime;
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
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
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
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
import org.osgi.service.event.EventHandler;

import com.google.common.base.Objects;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.ScenarioNotEvaluatedException;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetToTableTransformer.SortMode;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewColumnHelper.VesselData;
import com.mmxlabs.lingo.reports.views.changeset.InsertionPlanGrouperAndFilter.GroupMode;
import com.mmxlabs.lingo.reports.views.changeset.actions.CreateSandboxFromResultAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.ExportChangeAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.UpdateSandboxFromRowsAction;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase;
import com.mmxlabs.models.lng.analytics.ui.ChangeDescriptionSource;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.ui.analytics.EvaluateSolutionSetHelper;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.rcp.common.menus.SubLocalMenuHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ChangeSetView extends ViewPart {

	private final ChangeSetViewSchedulingRule schedulingRule = new ChangeSetViewSchedulingRule(this);

	final class ChangeSetComparator extends ViewerComparator {

		final boolean sortByVesselAndDate;

		public ChangeSetComparator(final boolean sortByVesselAndDate) {
			this.sortByVesselAndDate = sortByVesselAndDate;
		}

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
					if (sortByVesselAndDate) {
						final String r1Vessel = getEmptyIfNull(getVesselName(r1));
						final String r2Vessel = getEmptyIfNull(getVesselName(r2));
						final int diffVessel = r1Vessel.compareTo(r2Vessel);
						if (diffVessel == 0) {
							final ZonedDateTime date1 = getDate(r1);
							final ZonedDateTime date2 = getDate(r2);
							if (date1 != null && date2 != null) {
								return date1.compareTo(date2);
							} else if (date1 != null) {
								return -1;
							} else if (date2 != null) {
								return 1;
							} else {
								return 0;
							}
						} else {
							return diffVessel;
						}
					} else {
						return g.getRows().indexOf(r1) - g.getRows().indexOf(r2);
					}
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
				// if (insertionPlanFilter.getUserFilters().isEmpty()) {
				if (!Objects.equal(g1.getGroupObject(), g2.getGroupObject())) {
					return Double.compare(g2.getGroupSortValue(), g1.getGroupSortValue());

					// }
				}
				return -Double.compare(g2.getSortValue(), g1.getSortValue());
			}

			return super.compare(viewer, original_e1, original_e2);
		}

		private String getVesselName(final ChangeSetTableRow row) {
			if (row.getAfterVesselName() != null && !row.getAfterVesselName().isEmpty()) {
				return row.getAfterVesselName();
			} else if (row.getBeforeVesselName() != null && !row.getBeforeVesselName().isEmpty()) {
				return row.getBeforeVesselName();
			} else {
				return "";
			}
		}

		private String getEmptyIfNull(final String str) {
			return (str == null ? "" : str);
		}

		private ZonedDateTime getDate(final ChangeSetTableRow tableRow) {
			SlotAllocation slotAllocation = tableRow.getLhsAfter() != null ? tableRow.getLhsAfter().getLoadAllocation() : null;
			if (slotAllocation == null) {
				slotAllocation = tableRow.getLhsBefore() != null ? tableRow.getLhsBefore().getLoadAllocation() : null;
			}
			if (slotAllocation == null) {
				slotAllocation = tableRow.getRhsBefore() != null ? tableRow.getRhsBefore().getDischargeAllocation() : null;
			}
			if (slotAllocation == null) {
				slotAllocation = tableRow.getRhsAfter() != null ? tableRow.getRhsAfter().getDischargeAllocation() : null;
			}
			if (slotAllocation != null && slotAllocation.getSlotVisit() != null) {
				return slotAllocation.getSlotVisit().getStart();
			} else {
				return null;
			}
		}
	}

	public enum ViewMode {
		COMPARE, OLD_ACTION_SET, NEW_ACTION_SET, INSERTIONS, GENERIC, SANDBOX
	}

	public static class ViewState {

		public ChangeSetRoot root;

		public ChangeSetTableRoot tableRootAlternative;
		public ChangeSetTableRoot tableRootDefault;

		public @Nullable AnalyticsSolution lastSolution;
		public @Nullable NamedObject lastTargetSlot;
		public final Collection<Slot> allTargetSlots = new HashSet<>();

		public final SortMode displaySortMode;
		public Consumer<ChangeSetTableRoot> postProcess = cs -> {
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

	private static final boolean RE_SORT_ON_FILTER = false;

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
				// columnHelper.cleanUpVesselColumns();
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
					final PinDiffResultPlanTransformer transformer = new PinDiffResultPlanTransformer();
					final ChangeSetRoot newRoot = transformer.createDataModel(pin, other, monitor);
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

	private boolean showNonStructuralChanges = false;
	private boolean sortByVesselAndDate = false;
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

			for (final GridColumn gc : viewer.getGrid().getColumns()) {
				gc.setWidth(gc.getWidth());
			}
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
						// columnHelper.cleanUpVesselColumns();

						final ViewState newViewState = new ViewState(null, SortMode.BY_GROUP);
						final ChangeSetRoot newRoot = new ScheduleResultListTransformer().createDataModel(scenarios, new NullProgressMonitor());
						final ChangeSetToTableTransformer changeSetToTableTransformer = new ChangeSetToTableTransformer();
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

						ChangeSetView.this.setNewDataData(pin, (monitor, targetSlotId) -> {
							final PinDiffResultPlanTransformer transformer = new PinDiffResultPlanTransformer();
							final ChangeSetRoot newRoot = transformer.createDataModel(pin, other, monitor);
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
					public String getHTMLContents() {
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

	private boolean showAlternativeChangeModel = false; // Is the alt P&L base mode active?
	protected boolean showToggleAltPNLBaseAction = false; // Is the alt P&L button active/visible?
	private Action toggleAltPNLBaseAction;
	private ActionContributionItem toggleAltPNLBaseActionItem;
	protected boolean showGroupByMenu = false;
	protected boolean showChangeTargetMenu = false;
	protected boolean showNegativePNLChangesMenu = false;

	private RunnableAction reEvaluateAction;
	private ActionContributionItem reEvaluateActionItem;
	private boolean reEvaluateActionItemAdded = false;

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
		insertionPlanFilter = new InsertionPlanGrouperAndFilter();
		columnHelper.makeColumns(insertionPlanFilter);

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
				selectedElements.add(rowData.getDischargeSlot());
				if (rowData.getLoadAllocation() != null) {
					selectedElements.add(rowData.getLoadAllocation());
					final SlotVisit slotVisit = rowData.getLoadAllocation().getSlotVisit();
					selectedElements.add(slotVisit);
					if (slotVisit != null) {
						selectedElements.add(slotVisit.getSequence());
					}
				}
				if (rowData.getDischargeAllocation() != null) {
					selectedElements.add(rowData.getDischargeAllocation());
					final SlotVisit slotVisit = rowData.getDischargeAllocation().getSlotVisit();
					selectedElements.add(slotVisit);
					if (slotVisit != null) {
						selectedElements.add(slotVisit.getSequence());
					}
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
		filters[1] = insertionPlanFilter;

		viewer.setFilters(filters);

		viewer.setComparator(new ChangeSetComparator(this.sortByVesselAndDate));

		viewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				if (viewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) viewer.getSelection();
					if (!structuredSelection.isEmpty()) {

						// Attempt to detect the column we clicked on.
						final IWorkbench workbench = PlatformUI.getWorkbench();
						if (workbench != null) {
							final Display display = workbench.getDisplay();
							if (display != null) {
								final Point cursorLocation = display.getCursorLocation();
								if (cursorLocation != null) {

									final Grid grid = viewer.getGrid();

									final Point mousePoint = grid.toControl(cursorLocation);
									final GridColumn targetColumn = grid.getColumn(mousePoint);
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

		setViewMode(ViewMode.COMPARE, false);

		{
			// final MenuManager mgr = new MenuManager();
			// LocalMenuHelper menuHelper = new LocalMenuHelper(viewer.getGrid());
			final ContextMenuManager listener = new ContextMenuManager();
			viewer.getGrid().addMenuDetectListener(listener);
		}

		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventHandlerCloseScenario = event -> {
			// event.getProperty(name)
			final Object o = event.getProperty("org.eclipse.e4.data");
			if (o instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
				onClosingScenario(scenarioInstance);
			}
		};
		eventBroker.subscribe(ScenarioServiceUtils.EVENT_CLOSING_SCENARIO_INSTANCE, eventHandlerCloseScenario);
		eventHandlerCloseSolution = event -> {
			// event.getProperty(name)
			final Object o = event.getProperty("org.eclipse.e4.data");
			if (o instanceof AnalyticsSolution) {
				final AnalyticsSolution sol = (AnalyticsSolution) o;
				onClosingAnalyticsSolution(sol);
			}
		};
		eventBroker.subscribe(ScenarioServiceUtils.EVENT_CLOSING_ANALYTICS_SOLUTION, eventHandlerCloseSolution);

		final String secondaryId = getViewSite().getSecondaryId();
		if (!"Dynamic".equals(secondaryId)) {
			scenarioComparisonService.addListener(listener);
			scenarioComparisonService.triggerListener(listener);
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

		// Experimental change drag and drop
		if (false && viewMode == ViewMode.SANDBOX) {
			final DragSource source = new DragSource(viewer.getControl(), DND.DROP_MOVE);
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			source.setTransfer(types);
			source.addDragListener(new BasicDragSource(viewer) {
				@Override
				public void dragStart(final DragSourceEvent event) {
					// Grab selection now as the viewer selection can change (especially if it is
					// also a drop target)
					final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					Object element = selection.getFirstElement();
					if (element instanceof ChangeSetTableRow) {
						element = ((ChangeSetTableRow) element).eContainer();
					}
					int idx = 0;
					ChangeSetTableGroup changeSetTableGroup = null;
					if (element instanceof ChangeSetTableGroup) {
						changeSetTableGroup = (ChangeSetTableGroup) element;
						final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSetTableGroup.eContainer();
						if (root != null) {
							idx = root.getGroups().indexOf(changeSetTableGroup);
						}
						element = changeSetTableGroup.getChangeSet();
					}
					if (element instanceof ChangeSet) {
						final ChangeSet changeSet = (ChangeSet) element;

						final String name = columnHelper.getChangeSetColumnLabelProvider().apply(changeSetTableGroup, idx);

						this.selection = new StructuredSelection(new ChangeDescriptionSource(name, changeSet.getChangeDescription(), changeSet.getUserSettings()));
						final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
						// if (transfer.isSupportedType(event.dataType)) {
						transfer.setSelection(this.selection);
						transfer.setSelectionSetTime(event.time & 0xFFFF);
						// }
					} else {
						event.doit = false;
					}

				}
			});
		}
	}

	public void makeActions() {
		getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("start"));

		{
			copyAction = new CopyGridToHtmlClipboardAction(viewer.getGrid(), true, () -> {
				columnHelper.setTextualVesselMarkers(true);
				ViewerHelper.refresh(viewer, true);
			}, () -> {
				columnHelper.setTextualVesselMarkers(false);
				ViewerHelper.refresh(viewer, true);
			});
			getViewSite().getActionBars().getToolBarManager().add(copyAction);
		}
		{
			toggleAltPNLBaseAction = new RunnableAction("Change Mode", SWT.PUSH, ChangeSetView.this::doToggleDiffToBase);
			toggleAltPNLBaseAction.setToolTipText("Toggle comparing to base or previous case");
			toggleAltPNLBaseAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/compare_to_base.gif"));
			final GroupMarker group = new GroupMarker("diffToBaseGroup");
			getViewSite().getActionBars().getToolBarManager().add(group);
			toggleAltPNLBaseActionItem = new ActionContributionItem(toggleAltPNLBaseAction);
			if (showToggleAltPNLBaseAction) {
				getViewSite().getActionBars().getToolBarManager().appendToGroup("diffToBaseGroup", toggleAltPNLBaseActionItem);
			}
			toggleAltPNLBaseAction.setToolTipText(altPNLToolTipBase + " Currently " + altPNLToolTipBaseMode[showAlternativeChangeModel ? 1 : 0]);

		}

		{
			getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("filters"));
			filterMenu = new AbstractMenuAction("Filter...") {

				@Override
				protected void populate(final Menu menu) {
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
							}
						};
						groupModeAction.setToolTipText("Change the grouping choice");
						addActionToMenu(groupModeAction, menu);
					}
					final RunnableAction toggleStructuralChanges = new RunnableAction("Show non-structural changes", ChangeSetView.this::doShowStructuralChangesToggle);
					toggleStructuralChanges.setToolTipText("Toggling filtering of non structural changes");
					toggleStructuralChanges.setChecked(showNonStructuralChanges);
					addActionToMenu(toggleStructuralChanges, menu);

					final RunnableAction toggleSortByVesselAndDate = new RunnableAction("Sort by vessel and date", ChangeSetView.this::doSortByVesselAndDateToggle);
					toggleSortByVesselAndDate.setToolTipText("Toggling sorting by vessel and date");
					toggleSortByVesselAndDate.setChecked(sortByVesselAndDate);
					addActionToMenu(toggleSortByVesselAndDate, menu);

					if (showNegativePNLChangesMenu) {
						final RunnableAction toggleNegativePNL = new RunnableAction("Show negative PNL Changes", ChangeSetView.this::doShowNegativePNLToggle);
						toggleNegativePNL.setToolTipText("Toggling filtering of negative PNL");
						toggleNegativePNL.setChecked(showNegativePNLChanges);
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
		{

			final Action packAction = PackActionFactory.createPackColumnsAction(viewer);

			getViewSite().getActionBars().getToolBarManager().add(packAction);
		}

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_RE_EVALUATE_SOLUTIONS)) {
			reEvaluateAction = new RunnableAction("Re-evaluate", () -> {
				final AnalyticsSolution solution = currentViewState.lastSolution;
				// solution.
				if (solution != null) {
					final UUIDObject object = solution.getSolution();
					if (object instanceof AbstractSolutionSet) {
						final AbstractSolutionSet abstractSolutionSet = (AbstractSolutionSet) object;
						final ScenarioModelRecord record = SSDataManager.Instance.getModelRecord(solution.getScenarioInstance());
						EvaluateSolutionSetHelper.recomputeSolution(record, solution.getScenarioInstance(), abstractSolutionSet, //
								false, // Generate from specification model only
								true // Open once complete
						);
					}
				}
			});
			reEvaluateAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/iu_update_obj.gif"));
			reEvaluateAction.setToolTipText("Re-evaluate the solution(s) using current scenario data");
			reEvaluateActionItem = new ActionContributionItem(reEvaluateAction);
		}

		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	public void setNewDataData(final Object target, final BiFunction<IProgressMonitor, @Nullable String, ViewState> action, final @Nullable String targetSlotId) {
		setNewDataData(target, action, true, targetSlotId);
	}

	public void setNewDataData(final Object target, final BiFunction<IProgressMonitor, @Nullable String, ViewState> action, final boolean runAsync, final @Nullable String targetSlotId) {

		// columnHelper.cleanUpVesselColumns();

		if (target == null) {
			setEmptyData();
		} else {
			final Display display = PlatformUI.getWorkbench().getDisplay();
			final Shell activeShell = display.getActiveShell();

			final ICoreRunnable runnable = (monitor) -> {
				try {
					final ViewState newViewState = action.apply(monitor, targetSlotId);
					final ChangeSetToTableTransformer changeSetToTableTransformer = new ChangeSetToTableTransformer();
					newViewState.tableRootAlternative = changeSetToTableTransformer.createViewDataModel(newViewState.root, true, newViewState.lastTargetSlot, newViewState.displaySortMode);
					newViewState.tableRootDefault = changeSetToTableTransformer.createViewDataModel(newViewState.root, false, newViewState.lastTargetSlot, newViewState.displaySortMode);

					changeSetToTableTransformer.bindModels(newViewState.tableRootDefault, newViewState.tableRootAlternative);

					newViewState.postProcess.accept(newViewState.tableRootDefault);
					newViewState.postProcess.accept(newViewState.tableRootAlternative);

					if (runAsync) {
						RunnerHelper.asyncExec(new ViewUpdateRunnable(newViewState));
					} else {
						RunnerHelper.syncExec(new ViewUpdateRunnable(newViewState));
					}
				} catch (final ScenarioNotEvaluatedException cause) {
					RunnerHelper.asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openError(activeShell, "Error opening result", cause.getMessage());
						}
					});
				}
			};

			try {

				if (runAsync) {
					final Job job = Job.create("Open solution", runnable);
					job.setUser(true);
					job.setRule(schedulingRule);
					job.schedule();
				} else {
					runnable.run(new NullProgressMonitor());
				}
			} catch (final CoreException e) {
				final Throwable cause = e.getCause();
				if (cause instanceof ScenarioNotEvaluatedException) {
					MessageDialog.openError(activeShell, "Error opening result", cause.getMessage());
				} else {
					MessageDialog.openError(activeShell, "Error opening result", e.getMessage());
					e.printStackTrace();
				}
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

	@Override
	@PreDestroy
	public void dispose() {
		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventBroker.unsubscribe(eventHandlerCloseScenario);
		eventBroker.unsubscribe(eventHandlerCloseSolution);

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
			columnHelper.cleanUpVesselColumns();
			columnHelper.dispose();
		}
		this.currentViewState = null;

		// The post selection from this view can be left in the e4 context somehow.
		if (eSelectionService != null) {
			eSelectionService.setPostSelection(new StructuredSelection());
			eSelectionService = null;
		}
	}

	@Override
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
		if (toggleAltPNLBaseAction != null) {
			toggleAltPNLBaseAction.setChecked(showAlternativeChangeModel);
			toggleAltPNLBaseAction.setToolTipText(altPNLToolTipBase + " Currently " + altPNLToolTipBaseMode[showAlternativeChangeModel ? 1 : 0]);
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
		ViewerHelper.refreshThen(viewer, true, viewer::expandAll);
	}

	private void doSortByVesselAndDateToggle() {
		sortByVesselAndDate = !sortByVesselAndDate;
		viewer.setComparator(new ChangeSetComparator(this.sortByVesselAndDate));
		ViewerHelper.refreshThen(viewer, true, viewer::expandAll);
	}

	private void doShowNegativePNLToggle() {
		showNegativePNLChanges = !showNegativePNLChanges;
		ViewerHelper.refreshThen(viewer, true, viewer::expandAll);
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
		setViewMode(ViewMode.OLD_ACTION_SET, false);
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
			viewer.getGrid().addDisposeListener(e -> helper.dispose());
		}

		@Override
		public void menuDetected(final MenuDetectEvent e) {

			helper.clearActions();

			final Grid grid = viewer.getGrid();

			final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			final GridItem[] items = grid.getSelection();
			boolean showMenu = false;
			final Set<ChangeSetTableRow> directSelectedRows = new LinkedHashSet<>();
			final Set<ChangeSetTableGroup> directSelectedGroups = new LinkedHashSet<>();
			if (items.length >= 1) {
				final Set<ChangeSetTableGroup> selectedSets = new LinkedHashSet<>();
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();

					if (obj instanceof ChangeSetTableGroup) {
						selectedSets.add((ChangeSetTableGroup) obj);
						directSelectedGroups.add((ChangeSetTableGroup) obj);
					} else if (obj instanceof ChangeSetTableRow) {
						final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) obj;
						selectedSets.add((ChangeSetTableGroup) changeSetRow.eContainer());
						directSelectedRows.add(changeSetRow);
					}
				}
				if (directSelectedGroups.size() == 1 || (selectedSets.size() == 1 && directSelectedRows.isEmpty())) {
					showMenu |= createExportSolutionMenu(selectedSets);
					// If we selected a change set group, create this here, other wise it
					showMenu |= createSandboxMenu(directSelectedGroups, directSelectedRows, selectedSets);
				}
				if (ChangeSetView.this.viewMode == ViewMode.SANDBOX && ChangeSetView.this.showToggleAltPNLBaseAction) {
					showMenu |= createComputeFullSolutionMenus(selectedSets);
				}

				if (helper.hasActions()) {
					helper.addSeparator();
				}
				showMenu |= createShowGroupRelatedChangesMenu(directSelectedRows, selectedSets);

				if (showUserFilterMenus) {
					showMenu |= insertionPlanFilter.generateMenus(helper, viewer, directSelectedRows, selectedSets, currentViewState.lastTargetSlot);
				}
				if (directSelectedGroups.isEmpty() && (selectedSets.size() == 1 && directSelectedRows.size() >= 1)) {
					if (helper.hasActions()) {
						helper.addSeparator();
					}
					// If we selected a change set group, create this here, other wise it
					showMenu |= createSandboxMenu(directSelectedGroups, directSelectedRows, selectedSets);
				}
			} else {
				if (showUserFilterMenus && currentViewState != null) {
					showMenu |= insertionPlanFilter.generateMenus(helper, viewer, Collections.emptySet(), Collections.emptySet(), currentViewState.lastTargetSlot);
				}
			}

			if (showMenu) {
				helper.open();
			}
		}

		private boolean createShowGroupRelatedChangesMenu(final Set<ChangeSetTableRow> directSelectedRows, final Set<ChangeSetTableGroup> selectedSets) {
			if (showRelatedChangesMenus && selectedSets.size() == 1 && directSelectedRows.isEmpty()) {

				final ChangeSetTableGroup changeSetTableGroup = selectedSets.iterator().next();
				if (insertionPlanFilter.getUserFilters().isEmpty()) {
					if (insertionPlanFilter.getExpandedGroups().contains(changeSetTableGroup.getGroupObject())) {
						helper.addAction(new RunnableAction("Hide similar", () -> {
							insertionPlanFilter.getExpandedGroups().remove(changeSetTableGroup.getGroupObject());
							ViewerHelper.refreshThen(viewer, true, viewer::expandAll);
						}));
						return true;
					} else {
						helper.addAction(new RunnableAction("Show similar", () -> {
							insertionPlanFilter.getExpandedGroups().add(changeSetTableGroup.getGroupObject());
							ViewerHelper.refreshThen(viewer, true, viewer::expandAll);
						}));
						return true;
					}
				}
			}
			return false;
		}

		private boolean createComputeFullSolutionMenus(final Set<ChangeSetTableGroup> selectedSets) {
			boolean showMenu;
			final ChangeSetTableGroup changeSetTableGroup = selectedSets.iterator().next();

			final Action action = new RunnableAction("Compute full change", () -> {

				final ScenarioResult scenarioResult = changeSetTableGroup.getCurrentScenario();

				final EvaluateSolutionSetHelper ssHelper = new EvaluateSolutionSetHelper(scenarioResult.getScenarioDataProvider());

				final AnalyticsSolution solution = currentViewState.lastSolution;
				if (solution != null && solution.getSolution() instanceof SandboxResult) {
					final SandboxResult sandboxResult = (SandboxResult) solution.getSolution();

					UserSettings userSettings = null;

					// OtherPNL basePNL = ScheduleFactory.eINSTANCE.createOtherPNL();
					// {
					// ScheduleModelKPIUtils.updateOtherPNL(basePNL,
					// sandboxResult.getBaseOption().getScheduleModel().getSchedule(),
					// ScheduleModelKPIUtils.Mode.INCREMENT);
					// }

					final UUIDObject object = solution.getSolution();
					ssHelper.processSolution(sandboxResult.getBaseOption().getScheduleSpecification(), sandboxResult.getBaseOption().getScheduleModel());

					if (object instanceof AbstractSolutionSet) {
						final AbstractSolutionSet abstractSolutionSet = (AbstractSolutionSet) object;
						ssHelper.processExtraData(abstractSolutionSet);

						userSettings = abstractSolutionSet.getUserSettings();
						for (final SolutionOption opt : abstractSolutionSet.getOptions()) {
							if (opt.getScheduleModel() == scenarioResult.getResultRoot()) {
								ssHelper.processSolution(opt.getScheduleSpecification(), opt.getScheduleModel());
								break;
							} else if (changeSetTableGroup.getLinkedGroup() != null && opt.getScheduleModel() == changeSetTableGroup.getLinkedGroup().getCurrentScenario().getResultRoot()) {
								ssHelper.processSolution(opt.getScheduleSpecification(), opt.getScheduleModel());
								break;
							}

							// if (opt instanceof DualModeSolutionOption) {
							// final DualModeSolutionOption dualModeSolutionOption =
							// (DualModeSolutionOption) opt;
							//
							// final SolutionOptionMicroCase base =
							// dualModeSolutionOption.getMicroBaseCase();
							// if (base != null) {
							// // Re-evaluate from schedule
							// ssHelper.processExtraData(base);
							// ssHelper.processSolution(base.getScheduleModel());
							// // (Experimental version) Re-evaluate from change specification)
							// // helper.processSolution(base.getScheduleSpecification(),
							// base.getScheduleModel());
							// }
							//
							// final SolutionOptionMicroCase target =
							// dualModeSolutionOption.getMicroTargetCase();
							// if (target != null) {
							// // Re-evaluate from schedule
							// ssHelper.processExtraData(target);
							// ssHelper.processSolution(target.getScheduleModel());
							// // (Experimental version) Re-evaluate from change specification)
							// // helper.processSolution(target.getScheduleSpecification(),
							// target.getScheduleModel());
							// }
							//
							// }
						}
					}

					if (userSettings == null) {
						userSettings = scenarioResult.getTypedRoot(LNGScenarioModel.class).getUserSettings();
					}

					final UserSettings copy = EMFCopier.copy(userSettings);
					final Job job = Job.create("Open solution", monitor -> {
						ssHelper.generateResults(scenarioResult.getScenarioInstance(), copy, scenarioResult.getScenarioDataProvider().getEditingDomain(), monitor);
						final ViewState viewState = currentViewState;

						if (viewState != null) {
							final String id = viewState.getTargetSlotID();
							RunnerHelper.asyncExec(() -> openAnalyticsSolution(viewState.lastSolution, id));
						}
					});
					job.setUser(true);
					job.setRule(schedulingRule);
					job.schedule();

				}
			});
			action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/iu_update_obj.gif"));
			helper.addAction(action);
			showMenu = true;
			return showMenu;
		}

		private boolean createExportSolutionMenu(final Set<ChangeSetTableGroup> selectedSets) {
			if (canExportChangeSet) {
				final ChangeSetTableGroup changeSetTableGroup = selectedSets.iterator().next();
				int idx = 0;
				final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSetTableGroup.eContainer();
				if (root != null) {
					idx = root.getGroups().indexOf(changeSetTableGroup);
				}
				final String name = columnHelper.getChangeSetColumnLabelProvider().apply(changeSetTableGroup, idx);
				boolean showSimple = true;
				if (showAlternativeChangeModel && ChangeSetView.this.viewMode == ViewMode.INSERTIONS) {
					// Only offer this for dual model insertions.
					final ScenarioResult scenarioResult = changeSetTableGroup.getCurrentScenario();
					if (scenarioResult.getResultRoot() instanceof ScheduleModel) {
						final ScheduleModel scheduleModel = (ScheduleModel) scenarioResult.getResultRoot();
						if (scheduleModel.eContainer() instanceof SolutionOptionMicroCase) {
							final BiConsumer<LNGScenarioModel, Schedule> modelCustomiser = EvaluateSolutionSetHelper.createModelCustomiser();
							helper.addAction(new ExportChangeAction(changeSetTableGroup, name, true, modelCustomiser));
							showSimple = false;
						}
					}
				}
				if (showSimple) {
					BiConsumer<LNGScenarioModel, Schedule> modelCustomiser = null;
					if (getLastSolution().getSolution() instanceof SandboxResult) {
						modelCustomiser = EvaluateSolutionSetHelper.createModelCustomiser();
					}

					helper.addAction(new ExportChangeAction(changeSetTableGroup, name, modelCustomiser));
				}
				return true;
			}
			return false;
		}

		private boolean createSandboxMenu(final Set<ChangeSetTableGroup> directSelectedGroups, final Set<ChangeSetTableRow> directSelectedRows, final Set<ChangeSetTableGroup> selectedSets) {
			if (ChangeSetView.this.viewMode != ViewMode.COMPARE && ChangeSetView.this.viewMode != ViewMode.OLD_ACTION_SET) {
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_SANDBOX)) {

					final ChangeSetTableGroup changeSetTableGroup;
					if (directSelectedGroups.size() == 1) {
						changeSetTableGroup = directSelectedGroups.iterator().next();
						helper.addAction(new CreateSandboxFromResultAction(changeSetTableGroup.getRows(), changeSetTableGroup.getDescription(), changeSetTableGroup.getBaseScenario()));
					} else {
						changeSetTableGroup = selectedSets.iterator().next();
						helper.addAction(new CreateSandboxFromResultAction(directSelectedRows, changeSetTableGroup.getDescription(), changeSetTableGroup.getBaseScenario()));
					}
					if (false) {
						final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(changeSetTableGroup.getBaseScenario().getScenarioDataProvider());
						if (analyticsModel.getOptionModels().size() == 1) {
							final OptionAnalysisModel sandbox = analyticsModel.getOptionModels().get(0);
							helper.addAction(new UpdateSandboxFromRowsAction(directSelectedRows, "Add rows to existing " + sandbox.getName(), changeSetTableGroup.getBaseScenario(), sandbox));
						} else if (analyticsModel.getOptionModels().size() > 1) {
							final SubLocalMenuHelper subMenu = new SubLocalMenuHelper("Add rows to existing");
							for (final OptionAnalysisModel sandbox : analyticsModel.getOptionModels()) {
								subMenu.addAction(new UpdateSandboxFromRowsAction(directSelectedRows, sandbox.getName(), changeSetTableGroup.getBaseScenario(), sandbox));
							}
							helper.addSubMenu(subMenu);
						}

					}

					return true;
				}
			}
			return false;
		}

	}

	public void openAnalyticsSolution(final AnalyticsSolution solution) {
		openAnalyticsSolution(solution, null);
	}

	private EObject lastParent = null;
	private EObject lastObject = null;
	private EStructuralFeature lastParentFeature = null;

	private final Adapter adapter = new SafeAdapterImpl() {
		@Override
		public void safeNotifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			if (msg.getEventType() == Notification.REMOVE) {
				if (msg.getFeature() == lastParentFeature && msg.getOldValue() == lastObject) {
					setEmptyData();
					getSite().getPage().hideView(ChangeSetView.this);
				}
			}
		};

	};

	private Action copyAction;

	private AbstractMenuAction filterMenu;

	private final String altPNLToolTipBase_Default = "Toggle full or simple change mode";
	private final String altPNLToolTipBase_Insertions = "Toggle full or simple change mode";
	private String altPNLToolTipBase = altPNLToolTipBase_Default;

	private final String[] altPNLToolTipBaseMode_Default = { "Default", "Alt" };
	private final String[] altPNLToolTipBaseMode_ActionPlan = { "Previous", "Base" };
	private final String[] altPNLToolTipBaseMode_Insertions = { "Full", "Simple" };
	private String[] altPNLToolTipBaseMode = altPNLToolTipBaseMode_Default;

	private EventHandler eventHandlerCloseScenario;
	private EventHandler eventHandlerCloseSolution;

	public void openAnalyticsSolution(final AnalyticsSolution solution, @Nullable final String slotId) {
		if (lastParent != null) {
			lastParent.eAdapters().remove(adapter);
		}
		lastObject = null;
		lastParent = null;
		lastParentFeature = null;

		this.viewMode = ViewMode.OLD_ACTION_SET;

		final ScenarioInstance scenarioInstance = solution.getScenarioInstance();
		final Object target = solution;
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
			setViewMode(ViewMode.GENERIC, false);
			setNewDataData(target, (monitor, targetSlotId) -> {
				final OptimisationResultPlanTransformer transformer = new OptimisationResultPlanTransformer();
				// Sorting by Group as the label provider uses the provided ordering for
				// indexing
				final ViewState viewState = new ViewState(transformer.createDataModel(scenarioInstance, (OptimisationResult) plan, monitor), SortMode.BY_GROUP);
				viewState.lastSolution = solution;
				return viewState;
			}, slotId);
		} else if (plan instanceof SandboxResult) {
			setViewMode(ViewMode.SANDBOX, ((SandboxResult) plan).isHasDualModeSolutions());
			setNewDataData(target, (monitor, targetSlotId) -> {
				final SandboxResultPlanTransformer transformer = new SandboxResultPlanTransformer();
				insertionPlanFilter.setMaxComplexity(100);

				final SandboxResult sandboxResult = (SandboxResult) plan;
				// Sorting by Group as the label provider uses the provided ordering for
				// indexing
				final ViewState viewState = new ViewState(transformer.createDataModel(scenarioInstance, sandboxResult, monitor), SortMode.BY_PNL);
				viewState.lastSolution = solution;
				viewState.allTargetSlots.clear();
				// viewState.allTargetSlots.addAll(sandboxResult.getExtraSlots());
				return viewState;
			}, slotId);
		} else if (plan instanceof ActionableSetPlan) {
			setViewMode(ViewMode.NEW_ACTION_SET, false);
			setNewDataData(target, (monitor, targetSlotId) -> {
				final ActionableSetPlanTransformer transformer = new ActionableSetPlanTransformer();
				final ViewState viewState = new ViewState(transformer.createDataModel(scenarioInstance, (ActionableSetPlan) plan, monitor), SortMode.BY_GROUP);
				viewState.lastSolution = solution;
				return viewState;
			}, slotId);
		} else if (plan instanceof SlotInsertionOptions) {
			final SlotInsertionOptions slotInsertionOptions = (SlotInsertionOptions) plan;
			setViewMode(ViewMode.INSERTIONS, slotInsertionOptions.isHasDualModeSolutions());
			final int insertedObjects = slotInsertionOptions.getSlotsInserted().size() + slotInsertionOptions.getEventsInserted().size();
			insertionPlanFilter.setMaxComplexity(2 + 2 * insertedObjects);

			setNewDataData(target, (monitor, targetSlotId) -> {

				NamedObject pTargetSlot = null;
				if (!slotInsertionOptions.getSlotsInserted().isEmpty()) {
					Slot<?> targetSlot = slotInsertionOptions.getSlotsInserted().get(0);
					if (slotId != null) {
						for (final Slot<?> s : slotInsertionOptions.getSlotsInserted()) {
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
				final ViewState viewState = new ViewState(null, SortMode.BY_PNL_PER_CHANGE);
				viewState.lastSolution = solution;
				viewState.lastTargetSlot = pTargetSlot;
				viewState.allTargetSlots.clear();
				viewState.allTargetSlots.addAll(slotInsertionOptions.getSlotsInserted());

				final InsertionPlanTransformer transformer = new InsertionPlanTransformer();
				final ChangeSetRoot newRoot = transformer.createDataModel(scenarioInstance, slotInsertionOptions, monitor, pTargetSlot);

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
	public void onClosingAnalyticsSolution(@UIEventTopic(ScenarioServiceUtils.EVENT_CLOSING_ANALYTICS_SOLUTION) final AnalyticsSolution solution) {
		final ViewState viewState = currentViewState;
		if (viewState != null && viewState.lastSolution != null //
				&& ((viewState.lastSolution == solution) || (viewState.lastSolution.getSolution() == solution.getSolution()))) {
			setEmptyData();
			// Close view if dynamic
			if (viewMode != ViewMode.COMPARE) {
				getSite().getPage().hideView(ChangeSetView.this);
			}
		}
	}

	@Inject
	@Optional
	public void onClosingScenario(@UIEventTopic(ScenarioServiceUtils.EVENT_CLOSING_SCENARIO_INSTANCE) final ScenarioInstance scenarioInstance) {

		final @NonNull ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

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

	private void setViewMode(final ViewMode viewMode, final boolean dualPNLMode) {
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
		showToggleAltPNLBaseAction = false;
		altPNLToolTipBase = altPNLToolTipBase_Default;
		altPNLToolTipBaseMode = altPNLToolTipBaseMode_Default;

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
			showToggleAltPNLBaseAction = dualPNLMode;
			altPNLToolTipBase = altPNLToolTipBase_Insertions;
			altPNLToolTipBaseMode = altPNLToolTipBaseMode_Insertions;
			break;
		case NEW_ACTION_SET:
			showToggleAltPNLBaseAction = true;
			altPNLToolTipBase = altPNLToolTipBase_Default;
			altPNLToolTipBaseMode = altPNLToolTipBaseMode_ActionPlan;
			break;
		case OLD_ACTION_SET:
			showToggleAltPNLBaseAction = true;
			altPNLToolTipBase = altPNLToolTipBase_Default;
			altPNLToolTipBaseMode = altPNLToolTipBaseMode_ActionPlan;
			break;
		case SANDBOX:
			// Show mini-view by default
			showAlternativeChangeModel = dualPNLMode;
			// columnHelper.setChangeSetColumnLabelProvider(insertionPlanFilter.createLabelProvider());
			insertionPlanFilter.setInsertionModeActive(true);
			insertionPlanFilter.setMultipleSolutionView(false);
			insertionPlanFilter.setMaxComplexity(4);
			// columnHelper.setChangeSetColumnLabelProvider(insertionPlanFilter.createLabelProvider());
			showRelatedChangesMenus = true;
			showUserFilterMenus = true;
			showGroupByMenu = true;
			// showChangeTargetMenu = true;
			showNegativePNLChangesMenu = true;
			showToggleAltPNLBaseAction = dualPNLMode;
			altPNLToolTipBase = altPNLToolTipBase_Insertions;
			altPNLToolTipBaseMode = altPNLToolTipBaseMode_Insertions;
			break;
		case GENERIC:
			showAlternativeChangeModel = false;
			columnHelper.setChangeSetColumnLabelProvider(insertionPlanFilter.createLabelProvider());
			break;
		default:
			assert false;
			break;
		}

		if (toggleAltPNLBaseAction != null) {
			toggleAltPNLBaseAction.setChecked(showAlternativeChangeModel);
			getViewSite().getActionBars().getToolBarManager().remove(toggleAltPNLBaseActionItem);
			if (showToggleAltPNLBaseAction) {
				getViewSite().getActionBars().getToolBarManager().appendToGroup("diffToBaseGroup", toggleAltPNLBaseActionItem);
				toggleAltPNLBaseAction.setToolTipText(altPNLToolTipBase + " Currently " + altPNLToolTipBaseMode[showAlternativeChangeModel ? 1 : 0]);
			}
			getViewSite().getActionBars().getToolBarManager().update(true);
		}
		columnHelper.showAlternativePNLColumn(dualPNLMode);

		if (reEvaluateAction != null) {

			if (viewMode != ViewMode.COMPARE && viewMode != ViewMode.OLD_ACTION_SET) {
				if (!reEvaluateActionItemAdded) {
					getViewSite().getActionBars().getToolBarManager().add(reEvaluateActionItem);
					reEvaluateActionItemAdded = true;
					getViewSite().getActionBars().getToolBarManager().update(true);
				}
			} else {
				if (reEvaluateActionItemAdded) {
					getViewSite().getActionBars().getToolBarManager().remove(reEvaluateActionItem);
					reEvaluateActionItemAdded = false;
					getViewSite().getActionBars().getToolBarManager().update(true);
				}
			}
		}

		if (viewMode == ViewMode.INSERTIONS) {
			columnHelper.showCompareColumns(false);
		} else {
			columnHelper.showCompareColumns(true);
		}
	}
}
