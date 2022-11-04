/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.cargodiff;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.service.event.EventHandler;

import com.google.common.base.Objects;
import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IAnalyticSolutionGenerator;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContents;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.lingo.reports.views.changeset.ActionableSetPlanTransformer;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetToTableTransformer;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetToTableTransformer.SortMode;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetTransformerUtil;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetTransformerUtil.MappingModel;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewSchedulingRule;
import com.mmxlabs.lingo.reports.views.changeset.IActionPlanHandler;
import com.mmxlabs.lingo.reports.views.changeset.InsertionPlanGrouperAndFilter;
import com.mmxlabs.lingo.reports.views.changeset.InsertionPlanGrouperAndFilter.GroupMode;
import com.mmxlabs.lingo.reports.views.changeset.InsertionPlanTransformer;
import com.mmxlabs.lingo.reports.views.changeset.OptimisationResultPlanTransformer;
import com.mmxlabs.lingo.reports.views.changeset.SandboxResultPlanTransformer;
import com.mmxlabs.lingo.reports.views.changeset.ScheduleResultListTransformer;
import com.mmxlabs.lingo.reports.views.changeset.actions.CreateSandboxFromResultAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.ExportChangeAction;
import com.mmxlabs.lingo.reports.views.changeset.actions.UpdateSandboxFromRowsAction;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase;
import com.mmxlabs.models.lng.analytics.ui.ChangeDescriptionSource;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.impl.VesselCharterImpl;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl;
import com.mmxlabs.models.lng.transformer.ui.analytics.EvaluateSolutionSetHelper;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
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
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;

public class CargoDiffView extends ChangeSetView {

	/**
	 * This is used to lock the scenario when re-evaluating solutions
	 */
	private final ChangeSetViewSchedulingRule schedulingRule = new ChangeSetViewSchedulingRule(this);

	/**
	 * This is used to avoid the re-entrant code when we change the selected
	 * ScenarioResults in this view so it is not treated as a pin/diff update.
	 */
	private AtomicBoolean inChangingChangeSetSelection = new AtomicBoolean(false);

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
			if (e1 instanceof ChangeSetTableRow r1 && e2 instanceof ChangeSetTableRow r2) {
				// Retain original ordering in the datamodel
				if (r1.eContainer() == r2.eContainer()) {
					final ChangeSetTableGroup g = (ChangeSetTableGroup) r1.eContainer();
					if (sortByVesselAndDate) {
						final Pair<String, Integer> r1Vessel = getEmptyIfNull(getVesselNameAndCharterNumber(r1));
						final Pair<String, Integer> r2Vessel = getEmptyIfNull(getVesselNameAndCharterNumber(r2));

						int diffVessel = r1Vessel.getFirst().compareTo(r2Vessel.getFirst());
						if (diffVessel == 0)
							diffVessel = r1Vessel.getSecond().compareTo(r2Vessel.getSecond());
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

				if (!Objects.equal(g1.getGroupObject(), g2.getGroupObject())) {
					return Double.compare(g2.getGroupSortValue(), g1.getGroupSortValue());

				}
				return -Double.compare(g2.getSortValue(), g1.getSortValue());
			}

			return super.compare(viewer, original_e1, original_e2);
		}

		private Pair<String, Integer> getVesselNameAndCharterNumber(final ChangeSetTableRow row) {
			if (row.getAfterVesselName() != null && !row.getAfterVesselName().isEmpty()) {
				return Pair.of(row.getAfterVesselName(), row.getAfterVesselCharterNumber());
			} else if (row.getBeforeVesselName() != null && !row.getBeforeVesselName().isEmpty()) {
				return Pair.of(row.getBeforeVesselName(), row.getBeforeVesselCharterNumber());
			} else {
				return Pair.of("", 0);
			}
		}

		private Pair<String, Integer> getEmptyIfNull(final Pair<String, Integer> pair) {
			return (pair.getFirst() == null ? Pair.of("", pair.getSecond()) : pair);
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

	private ViewState currentViewState = null;

	public ChangeSetRoot createDataModel(ScenarioResult pin, ScenarioResult other, final IProgressMonitor monitor) {
		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		try {
			monitor.beginTask("Comparing solutions", 1);
			root.getChangeSets().add(buildDiffToBaseChangeSet(null, pin, other, null, null, null));
			monitor.worked(1);
		} finally {
			monitor.done();
		}
		return root;
	}

	public ChangeSet buildDiffToBaseChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current, ChangeDescription changeDescription, UserSettings userSettings,
			@Nullable NamedObject targetToSortFirst) {
		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		changeSet.setChangeDescription(changeDescription);
		changeSet.setBaseScenario(prev);
		changeSet.setCurrentScenario(current);
		generateDifferences(prev, current, changeSet, false, targetToSortFirst);
		return changeSet;
	}

	private void generateDifferences(final ScenarioResult from, final ScenarioResult to, final ChangeSet changeSet, final boolean isAlternative, @Nullable NamedObject targetToSortFirst) {
		if (from == null || to == null) {
			return;
		}

		List<Cargo> beforeCargoes = from.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class).getCargoModel().getCargoes();
		List<Cargo> afterCargoes = to.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class).getCargoModel().getCargoes();

		final MappingModel beforeDifferences = generateMappingModel(beforeCargoes);
		final MappingModel afterDifferences = generateMappingModel(afterCargoes);

		final List<ChangeSetRow> rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeDifferences, afterDifferences);

		if (isAlternative) {
			changeSet.getChangeSetRowsToAlternativeBase().addAll(rows);
		} else {
			changeSet.getChangeSetRowsToDefaultBase().addAll(rows);
		}

		ChangeSetTransformerUtil.setRowFlags(rows);

		calculateMetrics(changeSet, isAlternative);
	}

	public static void calculateMetrics(@NonNull final ChangeSet changeSet, final boolean isAlternative) {
		final Metrics currentMetrics = ChangesetFactory.eINSTANCE.createMetrics();
		final DeltaMetrics deltaMetrics = ChangesetFactory.eINSTANCE.createDeltaMetrics();
		if (isAlternative) {
			changeSet.setMetricsToAlternativeBase(deltaMetrics);
		} else {
			changeSet.setMetricsToDefaultBase(deltaMetrics);
		}
		changeSet.setCurrentMetrics(currentMetrics);
	}

	public static @NonNull MappingModel generateMappingModel(List<Cargo> cs) {

		Set<LoadSlot> lsset = new HashSet<>();
		final MappingModel mappingModel = new MappingModel();

		for (final Cargo cargo : cs) {

			final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
			mappingModel.groups.add(group);

			final List<LoadSlot> loadslots = new LinkedList<>();
			final List<DischargeSlot> dischargeslots = new LinkedList<>();

			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot ls) {
					loadslots.add(ls);
					lsset.add(ls);
				} else if (slot instanceof DischargeSlot ds) {
					dischargeslots.add(ds);
				} else {
					assert false;
				}
			}

			for (int i = 0; i < Math.max(loadslots.size(), dischargeslots.size()); ++i) {
				final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
				group.getMembers().add(row);

				row.setPrimaryRecord(i == 0);

				if (i == 0) {

					LoadSlot ls = loadslots.get(i);
					VesselAssignmentType vat = ls.getCargo().getVesselAssignmentType();
					if (vat instanceof VesselCharterImpl vci) {
						row.setVesselName(vci.getVessel().getName());
						row.setVesselShortName(vci.getVessel().getName());

					}
					if (vat instanceof CharterInMarketImpl cimi) {
						row.setVesselName(cimi.getVessel().getName());
						row.setVesselShortName(cimi.getVessel().getName());
					}

				}

				if (i < loadslots.size()) {

					final LoadSlot slot = loadslots.get(i);

					final String key = slot.getName();
					final String name = slot.getName();

					row.setLhsName(name);
					row.setLoadSlot(slot);

					if (slot instanceof SpotLoadSlot) {
						final String mKey = getMarketSlotKey((SpotLoadSlot) slot);
						mappingModel.lhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
					}
					mappingModel.lhsRowMap.put(key, row);

				}

				if (i < dischargeslots.size()) {

					final DischargeSlot slot = dischargeslots.get(i);

					final String key = slot.getName();
					final String name = slot.getName();

					row.setRhsName(name);
					row.setDischargeSlot(slot);
					if (slot instanceof SpotDischargeSlot) {
						final String mKey = getMarketSlotKey((SpotDischargeSlot) slot);
						mappingModel.rhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
					}
					mappingModel.rhsRowMap.put(key, row);

				}

			}

		}

		return mappingModel;
	}

	public static <T extends SpotSlot & Slot> String getMarketSlotKey(final T slot) {
		final SpotMarket market = slot.getMarket();
		return String.format("%s-%s-%04d-%02d", getSlotTypePrefix(slot), market.getName(), slot.getWindowStart().getYear(), slot.getWindowStart().getMonthValue());
	}

	protected static @NonNull String getSlotTypePrefix(final Slot slot) {
		String prefix;
		if (slot instanceof LoadSlot loadSlot) {
			if (loadSlot.isDESPurchase()) {
				prefix = "des-purchase";
			} else {
				prefix = "fob-purchase";
			}
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (dischargeSlot.isFOBSale()) {
				prefix = "fob-sale";
			} else {
				prefix = "des-sale";
			}
		}
		return prefix;
	}

	private final ISelectedScenariosServiceListener listener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {
			if (!inChangingChangeSetSelection.get()) {
				setViewMode(ViewMode.COMPARE, false);
				setPartName("CargoDiff");

				final ScenarioResult pin = selectedDataProvider.getPinnedScenarioResult();

				if (pin == null || selectedDataProvider.getOtherScenarioResults().isEmpty()) {
					setEmptyData();
				} else {
					final ScenarioResult other = selectedDataProvider.getOtherScenarioResults().get(0);
					setNewDataData(pin, (monitor, targetSlotId) -> {

						final ChangeSetRoot newRoot = createDataModel(pin, other, monitor);
						return new ViewState(newRoot, SortMode.BY_GROUP);
					}, !block, null);

				}
			}
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			// Nothing to do here
		}
	};

	private ViewMode viewMode = ViewMode.COMPARE;
	private IAdditionalAttributeProvider additionalAttributeProvider;

	private boolean canExportChangeSet;

	private InsertionPlanGrouperAndFilter insertionPlanFilter;

	private boolean showMinorChanges = false;
	private boolean sortByVesselAndDate = false;
	private boolean showRelatedChangesMenus = false;
	private boolean showUserFilterMenus = false;

	private boolean showNegativePNLChanges = true;

	@Inject
	public CargoDiffView() {

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

				public IReportContents getReportContents(final ScenarioResult pin, final ScenarioResult other, @Nullable List<Object> selectedObjects) {
					try {
						columnHelper.setTextualVesselMarkers(true);

						final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
						if (pin != null) {
							provider.addScenario(pin);
							provider.setPinnedScenarioInstance(pin);
						}
						if (other != null) {
							provider.addScenario(other);
						}
						// Request a blocking update ...
						listener.selectedDataProviderChanged(provider, true);

						final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);

						final String contents = util.convert();

						CargoDiffView.this.setEmptyData();
						// Prefix this header for rendering purposes
						return ReportContents.makeHTML("<meta charset=\"UTF-8\"/>" + contents);

					} finally {
						columnHelper.setTextualVesselMarkers(false);
					}
				}
			};
		}
		if (IAnalyticSolutionGenerator.class.isAssignableFrom(adapter)) {
			return (T) new IAnalyticSolutionGenerator() {

				public IReportContents getReportContents(final AnalyticsSolution solution) {
					try {
						columnHelper.setTextualVesselMarkers(true);

						openAnalyticsSolution(solution, false, null);

						final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);

						final String contents = util.convert();

						CargoDiffView.this.setEmptyData();
						// Prefix this header for rendering purposes
						return ReportContents.makeHTML("<meta charset=\"UTF-8\"/>" + contents);

					} finally {
						columnHelper.setTextualVesselMarkers(false);
					}
				}
			};
		}
		return (T) null;
	}

	private final Queue<Runnable> postCreateActions = new ConcurrentLinkedQueue<>();

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

		// Create table
		viewer = new GridTreeViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer, false);

		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);

		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);

		// Note: Only applies to setInput - #refresh() will not trigger expansion
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		// Create content provider
		viewer.setContentProvider(createContentProvider());

		columnHelper = new CargoDiffViewColumnHelper(this, viewer);
		insertionPlanFilter = new InsertionPlanGrouperAndFilter();
		columnHelper.makeColumns(insertionPlanFilter);

		// Selection listener for pin/diff driver.
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection iStructuredSelection) {

					// Extract out the selected ChangeSet, ChangeSet group and explicitly selected
					// rows
					ISelection newSelection;
					{
						final Set<Object> selectedElements = new LinkedHashSet<>();
						final Iterator<?> itr = iStructuredSelection.iterator();
						while (itr.hasNext()) {
							final Object o = itr.next();
							if (o instanceof ChangeSetTableRow changeSetRow) {
								selectRow(selectedElements, changeSetRow);
							} else if (o instanceof ChangeSetTableGroup group) {
								final List<ChangeSetTableRow> rows = group.getRows();
								for (final ChangeSetTableRow changeSetRow : rows) {
									selectRow(selectedElements, changeSetRow);
								}
							}
						}

						while (selectedElements.remove(null))
							;

						newSelection = new StructuredSelection(new ArrayList<>(selectedElements));
					}

					if (viewMode != ViewMode.COMPARE) {
						final Iterator<?> itr = iStructuredSelection.iterator();
						while (itr.hasNext()) {
							Object o = itr.next();
							if (o instanceof ChangeSetTableRow row) {
								o = row.eContainer();
							}
							if (o instanceof ChangeSetTableGroup changeSetTableGroup) {

								// Set the flag so we don't end up in compare mode.
								if (inChangingChangeSetSelection.compareAndSet(false, true)) {
									try {
										// This is a bit inefficient if we are just changing selected row(s) rather than
										// change set.
										scenarioComparisonService.setSelection(newSelection);
										scenarioComparisonService.setPinnedPair(changeSetTableGroup.getBaseScenario(), changeSetTableGroup.getCurrentScenario(), newSelection, true);
									} finally {
										inChangingChangeSetSelection.set(false);
									}
								}
								break;
							}
						}
					} else {
						// Update selected elements
						scenarioComparisonService.setSelectionWithChangeSet(newSelection);
					}
				}
			}

		});

		final ViewerFilter[] filters = new ViewerFilter[2];
		filters[0] = new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (!showNegativePNLChanges) {

					if (element instanceof ChangeSetTableGroup changeSet) {
						final long totalPNLDelta = changeSet.getDeltaMetrics().getPnlDelta();
						if (totalPNLDelta <= 0) {
							return false;
						}
					} else {

						if (parentElement instanceof ChangeSetTableGroup changeSet) {
							final long totalPNLDelta = changeSet.getDeltaMetrics().getPnlDelta();
							if (totalPNLDelta <= 0) {
								return false;
							}
						}
					}
				}

				if (element instanceof ChangeSetTableRow row) {
					if (!row.isWiringOrVesselChange()) {

						return false;

					}

				}

				if (element instanceof ChangeSetRow row) {
					if (!row.isWiringOrVesselChange()) {
						return false;
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
				if (viewer.getSelection() instanceof IStructuredSelection structuredSelection) {
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
			viewer.getGrid().addMenuDetectListener(new ContextMenuManager());
		}

		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		eventHandlerCloseScenario = event -> {

			final Object o = event.getProperty("org.eclipse.e4.data");
			if (o instanceof ScenarioInstance scenarioInstance) {
				onClosingScenario(scenarioInstance);
			}
		};
		eventBroker.subscribe(ScenarioServiceUtils.EVENT_CLOSING_SCENARIO_INSTANCE, eventHandlerCloseScenario);
		eventHandlerCloseSolution = event -> {

			final Object o = event.getProperty("org.eclipse.e4.data");
			if (o instanceof AnalyticsSolution sol) {
				onClosingAnalyticsSolution(sol);
			}
		};
		eventBroker.subscribe(ScenarioServiceUtils.EVENT_CLOSING_ANALYTICS_SOLUTION, eventHandlerCloseSolution);

		final String secondaryId = getViewSite().getSecondaryId();
		if (!"Dynamic".equals(secondaryId)) {
			scenarioComparisonService.addListener(listener);
			listener.selectedDataProviderChanged(scenarioComparisonService.getCurrentSelectedDataProvider(), false);
		}
		synchronized (postCreateActions) {
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

						transfer.setSelection(this.selection);
						transfer.setSelectionSetTime(event.time & 0xFFFF);

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
			toggleAltPNLBaseAction = new RunnableAction("Change Mode", SWT.PUSH, CargoDiffView.this::doToggleDiffToBase);
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
					final RunnableAction toggleMinorChanges = new RunnableAction("Show minor changes", CargoDiffView.this::doShowMinorChangesToggle);
					toggleMinorChanges.setToolTipText("Toggling filtering of minor changes");
					toggleMinorChanges.setChecked(showMinorChanges);
					addActionToMenu(toggleMinorChanges, menu);

					final RunnableAction toggleSortByVesselAndDate = new RunnableAction("Sort by vessel and date", CargoDiffView.this::doSortByVesselAndDateToggle);
					toggleSortByVesselAndDate.setToolTipText("Toggling sorting by vessel and date");
					toggleSortByVesselAndDate.setChecked(sortByVesselAndDate);
					addActionToMenu(toggleSortByVesselAndDate, menu);

					if (showNegativePNLChangesMenu) {
						final RunnableAction toggleNegativePNL = new RunnableAction("Show negative PNL Changes", CargoDiffView.this::doShowNegativePNLToggle);
						toggleNegativePNL.setToolTipText("Toggling filtering of negative PNL");
						toggleNegativePNL.setChecked(showNegativePNLChanges);
						addActionToMenu(toggleNegativePNL, menu);
					}
					if (showChangeTargetMenu) {
						if (currentViewState != null && currentViewState.allTargetElements.size() > 1) {

							final AbstractMenuAction targetMenu = new AbstractMenuAction("Target... ") {
								@Override
								protected void populate(final Menu menu2) {

									for (final NamedObject target : currentViewState.allTargetElements) {
										final RunnableAction action = new RunnableAction(target.getName(), SWT.PUSH,
												() -> openAnalyticsSolution(currentViewState.lastSolution, true, target.getName()));
										if (currentViewState.lastTargetElement == target) {
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
			filterMenu.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
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
					if (object instanceof AbstractSolutionSet abstractSolutionSet) {
						final ScenarioModelRecord record = SSDataManager.Instance.getModelRecord(solution.getScenarioInstance());
						EvaluateSolutionSetHelper.recomputeSolution(record, solution.getScenarioInstance(), abstractSolutionSet, //
								false, // Generate from specification model only
								true // Open once complete
						);
					}
				}
			});
			CommonImages.setImageDescriptors(reEvaluateAction, IconPaths.ReEvaluate_16);
			reEvaluateAction.setToolTipText("Re-evaluate the solution(s) using current scenario data");
			reEvaluateActionItem = new ActionContributionItem(reEvaluateAction);
		}

		getViewSite().getActionBars().getToolBarManager().update(true);
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

	}

	@Override
	@Focus
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	private ITreeContentProvider createContentProvider() {
		return new ITreeContentProvider() {

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
				if (element instanceof EObject eObject) {
					return eObject.eContainer();
				}
				return null;
			}

			@Override
			public Object[] getElements(final Object inputElement) {

				if (inputElement instanceof ChangeSetTableRoot changeSetRoot) {
					return changeSetRoot.getGroups().toArray();
				}

				return new Object[0];
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof ChangeSetTableGroup group) {
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

	private void doShowMinorChangesToggle() {
		showMinorChanges = !showMinorChanges;
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
			openAnalyticsSolution(viewState.lastSolution, true, id);
		}
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
			if (false && items.length == 2) {
				// Experimental code. Select two change set and right-click
				// Pin/diff against the selected two change-sets.

				ChangeSetTableGroup groupA = null;
				ChangeSetTableGroup groupB = null;

				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();

					if (obj instanceof ChangeSetTableGroup) {
						ChangeSetTableGroup changeSetTableGroup = (ChangeSetTableGroup) obj;
						if (groupA == null) {
							groupA = changeSetTableGroup;
						} else {
							groupB = changeSetTableGroup;
							break;
						}
					}
				}

				if (groupA != null && groupB != null) {

					// Extract out the selected ChangeSet, ChangeSet group and explicitly selected
					// rows
					ISelection newSelection;
					{
						final Set<Object> selectedElements = new LinkedHashSet<>();
						{
							final List<ChangeSetTableRow> rows = groupA.getRows();
							for (final ChangeSetTableRow changeSetRow : rows) {
								selectAfterRows(selectedElements, changeSetRow);
							}
						}
						{
							final List<ChangeSetTableRow> rows = groupB.getRows();
							for (final ChangeSetTableRow changeSetRow : rows) {
								selectAfterRows(selectedElements, changeSetRow);
							}
						}
						while (selectedElements.remove(null))
							;

						newSelection = new StructuredSelection(new ArrayList<>(selectedElements));
					}

					// Set the flag so we don't end up in compare mode.
					if (inChangingChangeSetSelection.compareAndSet(false, true)) {
						try {
							// This is a bit inefficient if we are just changing selected row(s) rather than
							// change set.
							scenarioComparisonService.setSelection(newSelection);
							scenarioComparisonService.setPinnedPair(groupA.getCurrentScenario(), groupB.getCurrentScenario(), newSelection, true);
						} finally {
							inChangingChangeSetSelection.set(false);
						}
					}

				}

			}

			if (items.length >= 1) {
				final Set<ChangeSetTableGroup> selectedSets = new LinkedHashSet<>();
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();

					if (obj instanceof ChangeSetTableGroup) {
						selectedSets.add((ChangeSetTableGroup) obj);
						directSelectedGroups.add((ChangeSetTableGroup) obj);
					} else if (obj instanceof ChangeSetTableRow changeSetRow) {
						selectedSets.add((ChangeSetTableGroup) changeSetRow.eContainer());
						directSelectedRows.add(changeSetRow);
					}
				}
				if (directSelectedGroups.size() == 1 || (selectedSets.size() == 1 && directSelectedRows.isEmpty())) {
					showMenu |= createExportSolutionMenu(selectedSets);
					// If we selected a change set group, create this here, other wise it
					showMenu |= createSandboxMenu(directSelectedGroups, directSelectedRows, selectedSets);
				}
				if (CargoDiffView.this.viewMode == ViewMode.SANDBOX && CargoDiffView.this.showToggleAltPNLBaseAction) {
					showMenu |= createComputeFullSolutionMenus(selectedSets);
				}

				if (helper.hasActions()) {
					helper.addSeparator();
				}
				showMenu |= createShowGroupRelatedChangesMenu(directSelectedRows, selectedSets);

				if (showUserFilterMenus) {
					showMenu |= insertionPlanFilter.generateMenus(helper, viewer, directSelectedRows, selectedSets, currentViewState.lastTargetElement);
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
					showMenu |= insertionPlanFilter.generateMenus(helper, viewer, Collections.emptySet(), Collections.emptySet(), currentViewState.lastTargetElement);
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
				if (solution != null && solution.getSolution() instanceof SandboxResult sandboxResult) {

					UserSettings userSettings = null;

					final UUIDObject object = solution.getSolution();
					ssHelper.processSolution(sandboxResult.getBaseOption().getScheduleSpecification(), sandboxResult.getBaseOption().getScheduleModel());

					if (object instanceof AbstractSolutionSet abstractSolutionSet) {
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
							RunnerHelper.asyncExec(() -> openAnalyticsSolution(viewState.lastSolution, true, id));
						}
					});
					job.setUser(true);
					job.setRule(schedulingRule);
					job.schedule();

				}
			});
			CommonImages.setImageDescriptors(action, IconPaths.ReEvaluate_16);
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
				if (showAlternativeChangeModel && CargoDiffView.this.viewMode == ViewMode.INSERTIONS) {
					// Only offer this for dual model insertions.
					final ScenarioResult scenarioResult = changeSetTableGroup.getCurrentScenario();
					if (scenarioResult.getResultRoot() instanceof ScheduleModel scheduleModel) {
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
			if (CargoDiffView.this.viewMode != ViewMode.COMPARE) {
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
		openAnalyticsSolution(solution, true, null);
	}

	private EObject lastParent = null;
	private EObject lastObject = null;
	private EStructuralFeature lastParentFeature = null;

	private final Adapter adapter = new SafeAdapterImpl() {
		@Override
		public void safeNotifyChanged(final org.eclipse.emf.common.notify.Notification msg) {
			if (msg.getEventType() == Notification.REMOVE) {
				if (msg.getFeature() == lastParentFeature && msg.getOldValue() == lastObject) {
					RunnerHelper.asyncExec(CargoDiffView.this::resetView);
				}
			}
		}
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

	public void openAnalyticsSolution(final AnalyticsSolution solution, boolean runAsync, @Nullable final String slotId) {

		// Set the flag so we do not end up in compare mode
		inChangingChangeSetSelection.set(true);
		try {
			if (lastParent != null) {
				lastParent.eAdapters().remove(adapter);
			}
			lastObject = null;
			lastParent = null;
			lastParentFeature = null;

			this.viewMode = ViewMode.GENERIC;

			final ScenarioInstance scenarioInstance = solution.getScenarioInstance();
			final Object target = solution;
			final EObject plan = solution.getSolution();
			final @Nullable ScenarioModelRecord modelRecord = solution.getModelRecord();
			setPartName(solution.getTitle());

			lastParent = plan.eContainer();
			if (lastParent != null) {
				lastObject = plan;
				lastParentFeature = plan.eContainingFeature();
				lastParent.eAdapters().add(adapter);
			}

			// Do something?
			if (plan instanceof OptimisationResult result) {
				setViewMode(ViewMode.GENERIC, false);
				setNewDataData(target, (monitor, targetSlotId) -> {
					final OptimisationResultPlanTransformer transformer = new OptimisationResultPlanTransformer();
					// Sorting by Group as the label provider uses the provided ordering for
					// indexing
					final ViewState viewState = new ViewState(transformer.createDataModel(scenarioInstance, modelRecord, result, monitor, null), SortMode.BY_GROUP);
					viewState.lastSolution = solution;
					return viewState;
				}, runAsync, slotId);
			} else if (plan instanceof SandboxResult result) {
				setViewMode(ViewMode.SANDBOX, result.isHasDualModeSolutions());
				setNewDataData(target, (monitor, targetSlotId) -> {
					final SandboxResultPlanTransformer transformer = new SandboxResultPlanTransformer();
					insertionPlanFilter.setMaxComplexity(100);

					// Sorting by Group as the label provider uses the provided ordering for
					// indexing

					SortMode sortMode = SortMode.BY_PNL;
					if (result.isPortfolioBreakEvenMode()) {
						sortMode = SortMode.BY_GROUP;
					}

					final ViewState viewState = new ViewState(transformer.createDataModel(scenarioInstance, modelRecord, result, monitor, null), sortMode);
					viewState.lastSolution = solution;
					viewState.allTargetElements.clear();
					return viewState;
				}, runAsync, slotId);
			} else if (plan instanceof ActionableSetPlan result) {
				setViewMode(ViewMode.NEW_ACTION_SET, false);
				setNewDataData(target, (monitor, targetSlotId) -> {
					final ActionableSetPlanTransformer transformer = new ActionableSetPlanTransformer();
					final ViewState viewState = new ViewState(transformer.createDataModel(scenarioInstance, modelRecord, result, monitor, null), SortMode.BY_GROUP);
					viewState.lastSolution = solution;
					return viewState;
				}, runAsync, slotId);
			} else if (plan instanceof SlotInsertionOptions slotInsertionOptions) {
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
					viewState.lastTargetElement = pTargetSlot;
					viewState.allTargetElements.clear();
					viewState.allTargetElements.addAll(slotInsertionOptions.getSlotsInserted());
					viewState.allTargetElements.addAll(slotInsertionOptions.getEventsInserted());

					final InsertionPlanTransformer transformer = new InsertionPlanTransformer();
					final ChangeSetRoot newRoot = transformer.createDataModel(scenarioInstance, modelRecord, slotInsertionOptions, monitor, pTargetSlot);

					viewState.postProcess = insertionPlanFilter.processChangeSetRoot(newRoot, pTargetSlot);

					viewState.root = newRoot;
					return viewState;
				}, runAsync, slotId);
			}

		} finally {
			inChangingChangeSetSelection.set(false);
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
			return currentViewState.lastTargetElement;
		}
		return null;
	}

	@Inject
	@Optional
	public void onClosingAnalyticsSolution(@UIEventTopic(ScenarioServiceUtils.EVENT_CLOSING_ANALYTICS_SOLUTION) final AnalyticsSolution solution) {
		final ViewState viewState = currentViewState;
		if (viewState != null && viewState.lastSolution != null //
				&& ((viewState.lastSolution == solution) || (viewState.lastSolution.getSolution() == solution.getSolution()))) {
			resetView();
		}
	}

	@Inject
	@Optional
	public void onClosingScenario(@UIEventTopic(ScenarioServiceUtils.EVENT_CLOSING_SCENARIO_INSTANCE) final ScenarioInstance scenarioInstance) {

		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);

		if (modelRecord == null) {
			return;
		}

		final Function<ScenarioResult, Boolean> checker = sr -> sr != null && (sr.getModelRecord() == modelRecord || sr.getScenarioInstance() == scenarioInstance);
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
				resetView();
			}
		}
	}

	private void resetView() {
		setViewMode(ViewMode.COMPARE, false);
		setPartName("CargoDiff");
		setEmptyData();
	}

	public ViewState getCurrentViewState() {
		return currentViewState;
	}

	private void setViewMode(final ViewMode viewMode, final boolean dualPNLMode) {

		// Defaults
		this.viewMode = viewMode;
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
		case SANDBOX:
			// Show mini-view by default
			showAlternativeChangeModel = dualPNLMode;
			insertionPlanFilter.setInsertionModeActive(true);
			insertionPlanFilter.setMultipleSolutionView(false);
			insertionPlanFilter.setMaxComplexity(4);
			showRelatedChangesMenus = true;
			showUserFilterMenus = true;
			showGroupByMenu = true;
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

			if (viewMode != ViewMode.COMPARE) {
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

	private void selectRow(final Set<Object> selectedElements, final ChangeSetTableRow tableRow) {
		selectedElements.add(tableRow);
		selectRow(selectedElements, tableRow.getLhsBefore());
		selectRow(selectedElements, tableRow.getLhsAfter());
		selectRow(selectedElements, tableRow.getRhsBefore());
		selectRow(selectedElements, tableRow.getRhsAfter());
	}

	private void selectAfterRows(final Set<Object> selectedElements, final ChangeSetTableRow tableRow) {
		selectedElements.add(tableRow);
		selectRow(selectedElements, tableRow.getLhsAfter());
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

		final EventGrouping eventGrouping = rowData.getEventGrouping();
		if (eventGrouping != null) {
			selectedElements.add(eventGrouping);
			selectedElements.addAll(eventGrouping.getEvents());
		}
		if (eventGrouping instanceof Event event) {
			selectedElements.add(event.getSequence());
		}
		if (eventGrouping instanceof CargoAllocation ca) {
			final Sequence sequence = ca.getSequence();
			if (sequence != null) {
				selectedElements.add(ca.getSequence());
			}
		}

		if (rowData.getOpenLoadAllocation() != null) {
			selectedElements.add(rowData.getOpenLoadAllocation());
			selectedElements.add(rowData.getOpenLoadAllocation().getSlot());
		}
		if (rowData.getOpenDischargeAllocation() != null) {
			selectedElements.add(rowData.getOpenDischargeAllocation());
			selectedElements.add(rowData.getOpenDischargeAllocation().getSlot());
		}
	}
}
