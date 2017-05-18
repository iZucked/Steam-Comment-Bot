/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;

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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
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
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
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
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ChangeSetView implements IAdaptable {

	public static enum ViewMode {
		COMPARE, ACTION_SET
	}

	/**
	 * Data key to store selected vessel column
	 */
	private static final String DATA_SELECTED_COLUMN = "selected-vessel-column";

	private static final String KEY_RESTORE_ANALYTICS_SOLUTION = "restore-analytics-solution-from-tags";

	@Inject
	private EPartService partService;

	private GridTreeViewer viewer;

	private GridColumnGroup vesselColumnGroup;
	private GridViewerColumn vesselColumnStub;

	private GridViewerColumn violationColumn;

	private GridViewerColumn latenessColumn;

	private ChangeSetWiringDiagram diagram;

	/**
	 * Display textual vessel change markers - used for unit testing where graphics are not captured in data dump.
	 */
	private boolean textualVesselMarkers = false;

	@Inject
	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	@Inject
	private ESelectionService eSelectionService;

	@Inject
	private ScenarioComparisonService scenarioComparisonService;

	private final IScenarioComparisonServiceListener listener = new IScenarioComparisonServiceListener() {

		@Override
		public void multiDataUpdate(final ISelectedDataProvider selectedDataProvider, final Collection<ScenarioResult> others, final Table table, final List<LNGScenarioModel> rootObjects) {
			if (!handleEvents) {
				return;
			}
			if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
				cleanUpVesselColumns();
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
				setNewDataData(target, monitor -> {
					final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
					scheduleDiffUtils.setCheckAssignmentDifferences(true);
					scheduleDiffUtils.setCheckSpotMarketDifferences(true);
					scheduleDiffUtils.setCheckNextPortDifferences(true);
					final ScenarioComparisonTransformer transformer = new ScenarioComparisonTransformer();
					final ChangeSetRoot newRoot = transformer.createDataModel(selectedDataProvider, equivalancesMap, table, pin, other, monitor);
					return newRoot;
				}, null);
			}
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {

		}
	};

	private final Map<ScenarioResult, ScenarioInstanceDeletedListener> listenerMap = new HashMap<>();

	private Font boldFont;

	private Image imageClosedCircle;

	private Image imageHalfCircle;
	private Image imageOpenCircle;

	private ViewMode viewMode = ViewMode.COMPARE;
	private IAdditionalAttributeProvider additionalAttributeProvider;

	private ChangeSetRoot root;

	public ChangeSetTableRoot tableRootToBase;
	public ChangeSetTableRoot tableRootToPrevious;

	// flag to indicate whether or not to respond to data change events.
	private boolean handleEvents;

	private boolean canExportChangeSet;

	private InsertionPlanGrouperAndFilter insertionPlanFilter;

	private @Nullable AnalyticsSolution lastSolution;
	private @Nullable Slot lastTargetSlot;

	private boolean diffToBase = false;
	private boolean showNonStructuralChanges = false;

	private boolean persistAnalyticsSolution = false;

	private final class ViewUpdateRunnable implements Runnable {
		private final ChangeSetRoot newRoot;
		private final ChangeSetTableRoot csdiffToPrevious;
		private final ChangeSetTableRoot csDdiffToBase;

		private ViewUpdateRunnable(final ChangeSetRoot newRoot, final ChangeSetTableRoot diffToPrevious, final ChangeSetTableRoot diffToBase) {
			this.newRoot = newRoot;
			this.csdiffToPrevious = diffToPrevious;
			this.csDdiffToBase = diffToBase;
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
			for (final String name : sortedNames) {
				assert name != null;
				final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				gvc.getColumn().setText(shortNameMap.get(name));
				gvc.getColumn().setHeaderTooltip(name);
				gvc.getColumn().setWidth(22);
				gvc.getColumn().setResizeable(false);
				gvc.setLabelProvider(createVesselLabelProvider(name));
				gvc.getColumn().setHeaderRenderer(new VesselNameColumnHeaderRenderer());
				gvc.getColumn().setCellRenderer(createCellRenderer());
				gvc.getColumn().setDetail(true);
				gvc.getColumn().setSummary(false);
				gvc.getColumn().addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						if (viewer.getData(DATA_SELECTED_COLUMN) == gc) {
							viewer.setData(DATA_SELECTED_COLUMN, null);
						} else {
							viewer.setData(DATA_SELECTED_COLUMN, gc);
						}
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			}

			{
				final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				gvc.getColumn().setText("");
				gvc.getColumn().setHeaderTooltip("Vessel assignment changed");
				gvc.getColumn().setWidth(22);
				gvc.getColumn().setResizeable(false);
				gvc.setLabelProvider(createGroupVesselLabelProvider());
				gvc.getColumn().setHeaderRenderer(new VesselNameColumnHeaderRenderer());
				gvc.getColumn().setCellRenderer(createCellRenderer());
				gvc.getColumn().setDetail(false);
				gvc.getColumn().setSummary(true);
			}

			// Force header size recalculation
			viewer.getGrid().recalculateHeader();

			if (newRoot != null) {
				for (final ChangeSet cs : newRoot.getChangeSets()) {
					createListener(cs.getBaseScenario());
					createListener(cs.getCurrentScenario());
					createListener(cs.getPrevScenario());
				}
			}
			final ChangeSetTableRoot newTable = diffToBase ? csDdiffToBase : csdiffToPrevious;

			diagram.setChangeSetRoot(newTable);
			viewer.setInput(newTable);
			// Release after creating the new one so we increment reference counts before decrementing, which could cause a scenario unload/load cycle
			final ChangeSetRoot oldRoot = ChangeSetView.this.root;
			ChangeSetView.this.root = newRoot;
			ChangeSetView.this.tableRootToBase = csDdiffToBase;
			ChangeSetView.this.tableRootToPrevious = csdiffToPrevious;
			cleanUp(oldRoot);
		}
	}

	private static class ScenarioInstanceDeletedListener extends ScenarioServiceListener {
		private final ScenarioInstance scenarioInstance;
		private final ChangeSetView view;

		public ScenarioInstanceDeletedListener(@NonNull final ScenarioInstance scenarioInstance, final ChangeSetView view) {
			this.scenarioInstance = scenarioInstance;
			this.view = view;
		}

		@Override
		public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			boolean linkedScenario = false;
			final ChangeSetRoot root = view.root;
			if (root != null) {
				for (final ChangeSet cs : root.getChangeSets()) {
					if (scenarioInstance == cs.getBaseScenario()) {
						linkedScenario = true;
					} else if (scenarioInstance == cs.getCurrentScenario()) {
						linkedScenario = true;
					} else if (scenarioInstance == cs.getPrevScenario()) {
						linkedScenario = true;
					}
				}
			}
			if (linkedScenario) {
				RunnerHelper.syncExec(() -> view.handleAnalyseScenario(null));
			}
		}

		@Override
		public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			if (scenarioInstance == this.scenarioInstance) {
				boolean linkedScenario = false;
				final ChangeSetRoot root = view.root;
				if (root != null) {
					for (final ChangeSet cs : root.getChangeSets()) {
						if (scenarioInstance == cs.getBaseScenario()) {
							linkedScenario = true;
						} else if (scenarioInstance == cs.getCurrentScenario()) {
							linkedScenario = true;
						} else if (scenarioInstance == cs.getPrevScenario()) {
							linkedScenario = true;
						}
					}
				}
				if (linkedScenario) {
					RunnerHelper.syncExec(() -> view.handleAnalyseScenario(null));
				}
			}
		}
	}

	@Inject
	public ChangeSetView() {

		additionalAttributeProvider = new IAdditionalAttributeProvider() {

			@Override
			public void begin() {
				textualVesselMarkers = true;
				// Need to refresh the view to trigger creation of the text labels
				ViewerHelper.refresh(viewer, true);
			}

			@Override
			public void done() {
				textualVesselMarkers = false;
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

			if (viewMode == ViewMode.ACTION_SET) {
				return (T) new IActionPlanHandler() {

					@Override
					public void displayActionPlan(final List<ScenarioResult> scenarios) {
						cleanUpVesselColumns();

						final ChangeSetRoot newRoot = new ScheduleResultListTransformer().createDataModel(scenarios, new NullProgressMonitor());
						final ChangeSetTableRoot csdiffToPrevious = new ChangeSetToTableTransformer().createViewDataModel(newRoot, false, null);
						final ChangeSetTableRoot csdiffToBase = new ChangeSetToTableTransformer().createViewDataModel(newRoot, true, null);
						RunnerHelper.exec(new ViewUpdateRunnable(newRoot, csdiffToPrevious, csdiffToBase), true);
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
				textualVesselMarkers = true;
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
				textualVesselMarkers = false;
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
				if (tag.equals("action-set")) {
					viewMode = ViewMode.ACTION_SET;
				}
				if (tag.equals("disable-event-handlers")) {
					handleEvents = false;
				}
			}
		}

		final ImageDescriptor openCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/open-circle.png");
		final ImageDescriptor closedCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/closed-circle.png");
		final ImageDescriptor halfCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/half-circle.png");

		imageOpenCircle = openCircleDescriptor.createImage();
		imageClosedCircle = closedCircleDescriptor.createImage();
		imageHalfCircle = halfCircleDescriptor.createImage();

		final Font systemFont = Display.getDefault().getSystemFont();
		final FontData fontData = systemFont.getFontData()[0];
		boldFont = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		// Create table
		viewer = new GridTreeViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);

		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);

		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);
		// viewer.getGrid().setAutoHeight(true);
		// viewer.getGrid().setRowHeaderRenderer(new DefaultRowHeaderRenderer());
		// viewer.getGrid().setHeaderRowHeaderRenderer(new DefaultRowHeCaderRenderer());

		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		// Create content provider
		viewer.setContentProvider(createContentProvider());

		// Create columns
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setTree(true);
			gvc.getColumn().setWidth(60);
			gvc.getColumn().setResizeable(true);
			gvc.getColumn().setMoveable(false);
			gvc.setLabelProvider(createCSLabelProvider());
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		final GridColumnGroup pnlComponentGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER | SWT.TOGGLE);
		pnlComponentGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());

		// pnlComponentGroup.setText("P&L Components");
		createCenteringGroupRenderer(pnlComponentGroup);
		pnlComponentGroup.setExpanded(false);
		pnlComponentGroup.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(final TreeEvent e) {
				pnlComponentGroup.setText("P&L Components");

			}

			@Override
			public void treeCollapsed(final TreeEvent e) {
				pnlComponentGroup.setText("");

			}
		});

		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("P&L (m)");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createPNLDeltaLabelProvider());
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);

		}
		// {
		// final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
		// final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
		// gvc.getColumn().setText("");
		// gvc.getColumn().setHeaderTooltip("P&&L Components");
		// gvc.getColumn().setWidth(20);
		// gvc.setLabelProvider(createStubLabelProvider());
		// // gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
		// // ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
		// createWordWrapRenderer(gvc);
		// gvc.getColumn().setCellRenderer(createCellRenderer());
		// gvc.getColumn().setDetail(false);
		// gvc.getColumn().setSummary(true);
		// }
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Sales");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createDeltaLabelProvider(true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Purchase");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createDeltaLabelProvider(true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Ship FOB");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createShippingDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Ship DES");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createAdditionalShippingPNLDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("- Upside");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createAdditionalUpsidePNLDeltaLabelProvider());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Cargo other");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createCargoOtherPNLDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		if (LicenseFeatures.isPermitted("features:report-equity-book")) {
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Equity");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createUpstreamDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("+ Tax, etc.");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createTaxDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);

			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		// Space col
		createSpacerColumn();
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Late");
			gvc.getColumn().setHeaderTooltip("Lateness");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createLatenessDeltaLabelProvider());
			gvc.getColumn().setCellRenderer(createCellRenderer());

			this.latenessColumn = gvc;
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Violations");
			gvc.getColumn().setHeaderTooltip("Capacity Violations");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createViolationsDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());

			this.violationColumn = gvc;

		}

		// Space col
		createSpacerColumn();

		final GridColumnGroup loadGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		loadGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
		loadGroup.setText("Purchase");
		createCenteringGroupRenderer(loadGroup);
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createIDLabelProvider(true));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Date");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDateLabelProvider(true));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createPriceLabelProvider(true));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(createDeltaLabelProvider(true, true, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Wiring");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringLabelProvider());
			this.diagram = createWiringDiagram(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		final GridColumnGroup dischargeGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		dischargeGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
		dischargeGroup.setText("Sale");
		createCenteringGroupRenderer(dischargeGroup);

		{
			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createIDLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Date");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDateLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createPriceLabelProvider(false));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(
					createDeltaLabelProvider(true, false, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		// Space col
		createSpacerColumn();

		vesselColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER | SWT.TOGGLE);
		vesselColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());

		// vesselColumnGroup.setText("Vessels");
		vesselColumnGroup.setExpanded(false);
		createCenteringGroupRenderer(vesselColumnGroup);
		vesselColumnGroup.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(final TreeEvent e) {
				// TODO Auto-generated method stub
				vesselColumnGroup.setText("Vessels");

			}

			@Override
			public void treeCollapsed(final TreeEvent e) {
				// TODO Auto-generated method stub
				vesselColumnGroup.setText("");

			}
		});
		// Vessel columns are dynamically created - create a stub column to lock down the position in the table
		{
			final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setVisible(false);
			gvc.getColumn().setWidth(0);
			gvc.setLabelProvider(createStubLabelProvider());
			vesselColumnStub = gvc;
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		// Create sorter
		// TODO: ?

		// Selection listener for pin/diff driver.
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					if (viewMode == ViewMode.ACTION_SET) {
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
										if (latenessColumn != null && latenessColumn.getColumn() == targetColumn) {
											openView("com.mmxlabs.shiplingo.platform.reports.views.LatenessReportView");
										} else if (violationColumn != null && violationColumn.getColumn() == targetColumn) {
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
			final MenuManager mgr = new MenuManager();
			final ContextMenuManager listener = new ContextMenuManager(mgr);
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

	protected void createSpacerColumn() {
		{

			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(5);
			gvc.setLabelProvider(createStubLabelProvider());
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
	}

	protected DefaultCellRenderer createCellRenderer() {
		return new DefaultCellRenderer() {

			@Override
			public boolean isSelected() {
				if (viewer.getData(DATA_SELECTED_COLUMN) == viewer.getGrid().getColumn(getColumn())) {
					return true;
				}

				return super.isSelected();
			}

			@Override
			public void paint(final GC gc, final Object value) {
				// TODO Auto-generated method stub
				super.paint(gc, value);
				if (value instanceof GridItem) {
					final GridItem gridItem = (GridItem) value;
					final Object data = gridItem.getData();
					if (data instanceof ChangeSetTableGroup) {
						gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
						final int s = gc.getLineStyle();
						gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
						// gc.setLineStyle(SWT.LINE_DOT);
						// gc.drawLine(getBounds().x, getBounds().y, getBounds().width + getBounds().x, getBounds().y);
						gc.setLineStyle(SWT.LINE_DOT);
						// gc.setLineWidth(1);
						gc.drawLine(getBounds().x, getBounds().y + getBounds().height, getBounds().width + getBounds().x, getBounds().y + getBounds().height);
						gc.setLineStyle(s);
					}
				}

			}
		};
	}

	@SuppressWarnings("restriction")
	private void createWordWrapRenderer(final GridViewerColumn gvc) {
		final WrappingColumnHeaderRenderer renderer = new WrappingColumnHeaderRenderer();
		renderer.setWordWrap(true);
		gvc.getColumn().setHeaderRenderer(renderer);
	}

	private void createCenteringGroupRenderer(final GridColumnGroup gcg) {
		final CenteringColumnGroupHeaderRenderer renderer = new CenteringColumnGroupHeaderRenderer();
		gcg.setHeaderRenderer(renderer);
	}

	// @PostConstruct
	// private void updateToolItems(@Optional final MPart part) {
	// updateToolItem(part, viewMode);
	// }

	private ChangeSetWiringDiagram createWiringDiagram(final GridViewerColumn gvc) {

		final ChangeSetWiringDiagram d = new ChangeSetWiringDiagram(viewer.getGrid(), gvc);

		return d;

	}

	private CellLabelProvider createStubLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				// Do nothing
			}
		};
	}

	private CellLabelProvider createIDLabelProvider(final boolean lhs) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow d = (ChangeSetTableRow) element;
					if (lhs) {
						if (d.getLhsName() != null && !d.getLhsName().isEmpty()) {
							cell.setText((String) d.getLhsName());
							return;
						}
					} else {
						if (d.getRhsName() != null && !d.getRhsName().isEmpty()) {
							cell.setText((String) d.getRhsName());
							return;
						}
					}
				}
			}

		};
	}

	private CellLabelProvider createDateLabelProvider(final boolean isLoadSide) {
		return new CellLabelProvider() {

			private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow tableRow = (ChangeSetTableRow) element;

					LocalDate windowStart = null;
					boolean isDelta = false;
					int deltaHours = 0;

					if (isLoadSide) {
						final SlotAllocation originalLoadAllocation = tableRow.getLhsBefore() != null ? tableRow.getLhsBefore().getLoadAllocation() : null;
						final SlotAllocation newLoadAllocation = tableRow.getLhsAfter() != null ? tableRow.getLhsAfter().getLoadAllocation() : null;

						if (newLoadAllocation != null) {
							final Slot slot = newLoadAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						} else if (originalLoadAllocation != null) {
							final Slot slot = originalLoadAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						}

						if (newLoadAllocation != null && originalLoadAllocation != null) {
							deltaHours = Hours.between(originalLoadAllocation.getSlotVisit().getStart(), newLoadAllocation.getSlotVisit().getStart());
							if (deltaHours != 0) {
								isDelta = true;
							}
						}

						if (windowStart != null) {
							if (isDelta) {
								cell.setText(String.format("%s (%s%.1f)", windowStart.format(formatter), deltaHours < 0 ? "↓" : "↑", Math.abs(deltaHours / 24.0)));
							} else {
								cell.setText(windowStart.format(formatter));
							}
						}

					} else {
						// Unlike other RHS colums where we want to diff against the old and new slot linked to the cargo, we want to show the date diff for this slot.
						// final SlotAllocation originalDischargeAllocation = tableRow.getRhsBefore() != null ? tableRow.getRhsBefore().getDischargeAllocation() : null;
						// DO NOT COMMIT - UNIT TEST COMPATIBILITY
						SlotAllocation originalDischargeAllocation = null;
						if (tableRow.getPreviousRHS() != null) {
							if (tableRow.getPreviousRHS().getLhsBefore() != null) {
								originalDischargeAllocation = tableRow.getPreviousRHS().getLhsBefore().getDischargeAllocation();
							}
						}
						final SlotAllocation newDischargeAllocation = tableRow.getRhsAfter() != null ? tableRow.getRhsAfter().getDischargeAllocation() : null;

						if (newDischargeAllocation != null) {
							final Slot slot = newDischargeAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						} else if (originalDischargeAllocation != null) {
							final Slot slot = originalDischargeAllocation.getSlot();
							if (slot != null) {
								windowStart = slot.getWindowStart();
							}
						}

						if (newDischargeAllocation != null && originalDischargeAllocation != null) {
							deltaHours = Hours.between(originalDischargeAllocation.getSlotVisit().getStart(), newDischargeAllocation.getSlotVisit().getStart());
							if (deltaHours != 0) {
								// DO NOT COMMIT - TO COMPARE WITH EXISTING UNIT TESTS
								isDelta = true;
							}
						}

						if (windowStart != null) {
							if (isDelta) {
								cell.setText(String.format("%s (%s%.1f)", windowStart.format(formatter), deltaHours < 0 ? "↓" : "↑", Math.abs(deltaHours / 24.0)));
							} else {
								cell.setText(windowStart.format(formatter));
							}
						}
					}
				}
			}
		};
	}

	private CellLabelProvider createDeltaLabelProvider(final boolean asInt, final boolean isLHS, final EStructuralFeature field, final EStructuralFeature attrib) {

		final EReference from = isLHS ? ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE : ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE;
		final EReference to = isLHS ? ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER : ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER;

		return createLambdaLabelProvider(asInt, false, change -> getNumber(from, field, attrib, change), change -> getNumber(to, field, attrib, change));
	}

	@NonNull
	private Number getNumber(final EReference from, final EStructuralFeature field, final EStructuralFeature attrib, final ChangeSetTableRow row) {
		Number n = null;
		try {
			final ChangeSetRowData change = (ChangeSetRowData) row.eGet(from);
			if (change != null) {
				n = getNumberInt(field, attrib, change);
			}
		} catch (final Exception e) {
			final int ii = 0;
		}
		if (n == null) {
			return Long.valueOf(0L);

		}
		return n;

	}

	@Nullable
	private Number getNumberInt(final EStructuralFeature field, final EStructuralFeature attrib, final ChangeSetRowData change) {
		if (change != null && change.eClass().getEAllStructuralFeatures().contains(field)) {
			final EObject eObject = (EObject) change.eGet(field);
			if (eObject != null) {
				return (Number) eObject.eGet(attrib);
			}
		}
		return null;

	}

	private CellLabelProvider createPNLDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) element;
					final DeltaMetrics metrics = changeSet.getDeltaMetrics();
					if (metrics != null) {
						double delta = metrics.getPnlDelta();

						delta = delta / 1000000.0;
						if (Math.abs(delta) < 0.0001) {
							delta = 0;
						}
						if (delta == 0) {
							cell.setText("0.00");
						} else {
							cell.setText(String.format("%s%,.3G", metrics.getPnlDelta() < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long f = ChangeSetKPIUtil.getPNL(change, ResultType.Before);
					final long t = ChangeSetKPIUtil.getPNL(change, ResultType.After);

					double delta = t - f;
					delta = delta / 1000000.0;
					if (Math.abs(delta) < 0.0001) {
						delta = 0;
					}
					if (delta != 0) {
						cell.setText(String.format("%s %,.3G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createTaxDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> {
			return ChangeSetKPIUtil.getTax(change, ResultType.Before);
		}, change -> {
			return ChangeSetKPIUtil.getTax(change, ResultType.After);
		});
	}

	private CellLabelProvider createLatenessDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long[] originalLateness = ChangeSetKPIUtil.getLateness(change, ResultType.Before);
					final long[] newLateness = ChangeSetKPIUtil.getLateness(change, ResultType.After);

					// No lateness
					if (originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0 && newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0) {
						return null;
					}

					final boolean originalInFlex = originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && originalLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;
					final boolean newInFlex = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && newLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;

					if (originalInFlex != newInFlex) {
						// Lateness shift between flex and non-flex times.
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						final String flexStr;
						if (originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0) {
							assert originalInFlex == false;
							flexStr = "within";
						} else if (delta > 0) {
							flexStr = "out of";
						} else {
							flexStr = "within";
						}
						// CHECK -- IF Original was zero, then we have moved into the felx time.
						return String.format("Lateness %s by %d days, %d hours %s flex time", delta > 0 ? "increased" : "decreased", Math.abs(delta) / 24, Math.abs(delta) % 24, flexStr);
					}
					if (!originalInFlex) {
						// if (originalLateWithoutFlex > 0 && newLatenessWithoutFlex > 0) {
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						return String.format("Lateness %s by %d days, %d hours", delta > 0 ? "increased" : "decreased", Math.abs(delta) / 24, Math.abs(delta) % 24);
						// }
					} else {
						// if (originalLateWithoutFlex > 0 && newLatenessWithoutFlex > 0) {
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						return String.format("Lateness (within flex time) %s by %d days, %d hours", delta > 0 ? "increased" : "decreased", Math.abs(delta) / 24, Math.abs(delta) % 24);
						// }
					}

				}
				return null;
			}

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final Metrics scenarioMetrics = group.getCurrentMetrics();
					final DeltaMetrics deltaMetrics = group.getDeltaMetrics();

					if (deltaMetrics != null) {
						final int latenessDelta = (int) Math.round((double) deltaMetrics.getLatenessDelta() / 24.0);
						final int lateness = (int) Math.round((double) scenarioMetrics.getLateness() / 24.0);
						cell.setText(String.format("%s%d / %d", latenessDelta < 0 ? "↓" : latenessDelta == 0 ? "" : "↑", Math.abs(latenessDelta), lateness));

						if (lateness != 0) {
							cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long[] originalLateness = ChangeSetKPIUtil.getLateness(change, ResultType.Before);
					final long[] newLateness = ChangeSetKPIUtil.getLateness(change, ResultType.After);

					final boolean originalInFlex = originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && originalLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;
					final boolean newInFlex = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] > 0 && newLateness[ChangeSetKPIUtil.FlexType.WithFlex.ordinal()] == 0;

					String flexStr = "";
					if (originalInFlex != newInFlex) {
						// Lateness shift between flex and non-flex times.
						final long delta = newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] - originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
						if (originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()] == 0) {
							assert originalInFlex == false;
							flexStr = " *";
						} else if (delta > 0) {
							flexStr = "";
						} else {
							flexStr = " *";
						}
					}
					long delta = 0L;
					delta -= originalLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
					delta += newLateness[ChangeSetKPIUtil.FlexType.WithoutFlex.ordinal()];
					final long originalDelta = delta;
					delta = (int) Math.round((double) delta / 24.0);
					if (delta != 0L) {
						cell.setText(String.format("%s %d%s", delta < 0 ? "↓" : "↑", Math.abs(delta), flexStr));
					} else if (originalDelta != 0L) {
						cell.setText(String.format("%s %s%s", originalDelta < 0 ? "↓" : "↑", "<1", flexStr));
					}
				}
			}
		};

	}

	private CellLabelProvider createViolationsDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final Metrics scenarioMetrics = group.getCurrentMetrics();
					final DeltaMetrics deltaMetrics = group.getDeltaMetrics();
					if (deltaMetrics != null) {
						cell.setText(String.format("%s%d / %d", deltaMetrics.getCapacityDelta() < 0 ? "↓" : deltaMetrics.getCapacityDelta() == 0 ? "" : "↑", Math.abs(deltaMetrics.getCapacityDelta()),
								scenarioMetrics.getCapacity()));

						if (scenarioMetrics.getCapacity() != 0) {
							cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						}
					}
				}
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					final long f = ChangeSetKPIUtil.getViolations(change, ResultType.Before);
					final long t = ChangeSetKPIUtil.getViolations(change, ResultType.After);

					final long delta = t - f;
					if (delta != 0) {
						cell.setText(String.format("%s %d", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createCargoOtherPNLDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, change -> ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.Before), change -> ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.After));
	}

	private CellLabelProvider createUpstreamDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.Before), change -> ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.After));
	}

	private CellLabelProvider createAdditionalShippingPNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.Before),
				change -> ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.After));
	}

	private CellLabelProvider createAdditionalUpsidePNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.Before),
				change -> ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.After));
	}

	private CellLabelProvider createShippingDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, change -> ChangeSetKPIUtil.getShipping(change, ResultType.Before), change -> ChangeSetKPIUtil.getShipping(change, ResultType.After));
	}

	private CellLabelProvider createCSLabelProvider() {

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetTableGroup) {

					cell.setFont(boldFont);

					final ChangeSetTableGroup changeSet = (ChangeSetTableGroup) element;

					if (changeSet.getDescription() != null && !changeSet.getDescription().isEmpty()) {
						cell.setText(changeSet.getDescription());
					} else {
						final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSet.eContainer();
						int idx = 0;
						if (root != null) {
							idx = root.getGroups().indexOf(changeSet);
						}

						cell.setText(String.format("Set %d", idx + 1));
					}
				}
			}
		};

	}

	@PostConstruct
	public void makeActions() {

	}

	public void setNewDataData(final Object target, final Function<IProgressMonitor, ChangeSetRoot> action, final @Nullable Slot targetToSortFirst) {

		cleanUpVesselColumns();

		if (target == null) {
			setEmptyData();
		} else {
			final Display display = PlatformUI.getWorkbench().getDisplay();
			final ProgressMonitorDialog d = new ProgressMonitorDialog(display.getActiveShell());
			try {
				d.run(true, false, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						final ChangeSetRoot newRoot = action.apply(monitor);
						final ChangeSetTableRoot newTablePrevious = new ChangeSetToTableTransformer().createViewDataModel(newRoot, false, targetToSortFirst);
						final ChangeSetTableRoot newTableBase = new ChangeSetToTableTransformer().createViewDataModel(newRoot, true, targetToSortFirst);
						display.asyncExec(new ViewUpdateRunnable(newRoot, newTablePrevious, newTableBase));
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

		diagram.setChangeSetRoot(newTableRoot);
		ViewerHelper.setInput(viewer, true, newTableRoot);

		final ChangeSetRoot oldRoot = ChangeSetView.this.root;
		ChangeSetView.this.root = newRoot;
		ChangeSetView.this.tableRootToBase = newTableRoot;
		ChangeSetView.this.tableRootToPrevious = newTableRoot;
		cleanUp(oldRoot);
	}

	private void cleanUpVesselColumns() {
		if (vesselColumnGroup != null && !vesselColumnGroup.isDisposed()) {
			final GridColumn[] columns = vesselColumnGroup.getColumns();
			for (final GridColumn c : columns) {
				// Quick hack - do not dispose the hidden col
				if (vesselColumnStub.getColumn() == c) {
					continue;
				}
				if (!c.isDisposed()) {
					c.dispose();
				}
			}
		}
	}

	private void cleanUp(@Nullable final ChangeSetRoot root) {
		if (root != null) {
			for (final ChangeSet cs : root.getChangeSets()) {
				release(cs.getBaseScenarioRef());
				release(cs.getPrevScenarioRef());
				release(cs.getCurrentScenarioRef());
				removeListener(cs.getBaseScenario());
				removeListener(cs.getCurrentScenario());
				removeListener(cs.getPrevScenario());
			}
		}
	}

	protected void removeListener(@Nullable final ScenarioResult scenarioInstance) {
		if (scenarioInstance != null) {
			final IScenarioServiceListener listener = listenerMap.get(scenarioInstance);
			final IScenarioService scenarioService = scenarioInstance.getScenarioInstance().getScenarioService();
			if (scenarioService != null && listener != null) {
				scenarioService.removeScenarioServiceListener(listener);
			}
			listenerMap.remove(scenarioInstance);

		}
	}

	protected void createListener(@Nullable final ScenarioResult scenarioInstance) {
		if (scenarioInstance != null) {
			final IScenarioService scenarioService = scenarioInstance.getScenarioInstance().getScenarioService();
			if (scenarioService != null) {
				final ScenarioInstanceDeletedListener listener = new ScenarioInstanceDeletedListener(scenarioInstance.getScenarioInstance(), this);
				listenerMap.put(scenarioInstance, listener);
				scenarioService.addScenarioServiceListener(listener);
			}

		}
	}

	private void release(final ModelReference ref) {
		if (ref != null) {
			ref.close();
		}
	}

	private CellLabelProvider createVesselLabelProvider(@NonNull final String name) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				cell.setImage(null);
				final Object element = cell.getElement();
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (name.equals(changeSetRow.getAfterVesselName())) {
						cell.setImage(imageClosedCircle);
						if (textualVesselMarkers) {
							cell.setText("●");
						}
					} else if (name.equals(changeSetRow.getBeforeVesselName())) {
						cell.setImage(imageOpenCircle);
						if (textualVesselMarkers) {
							cell.setText("○");
						}
					}
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (name.equals(changeSetRow.getAfterVesselName())) {
						return name;
					}
					if (name.equals(changeSetRow.getBeforeVesselName())) {
						return name;
					}
				}
				return null;
			}
		};
	}

	private CellLabelProvider createWiringLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				cell.setImage(null);
				final Object element = cell.getElement();
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (textualVesselMarkers) {
						if (changeSetRow.isWiringChange()) {
							String left = "";
							String right = "";
							if (changeSetRow.isLhsSlot()) {
								left = changeSetRow.isLhsNonShipped() ? "○" : "●";
							}
							if (changeSetRow.isRhsSlot()) {
								right = !changeSetRow.isRhsNonShipped() ? "○" : "●";
							}

							if (left.isEmpty() && right.isEmpty()) {
								final int ii = 0;
							} else

								cell.setText(String.format("%s      %s", left, right));
						}
					}
				}
			}
		};
	}

	private CellLabelProvider createGroupVesselLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				cell.setImage(null);
				final Object element = cell.getElement();
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;
					if (!Objects.equals(changeSetRow.getAfterVesselName(), changeSetRow.getBeforeVesselName())) {
						cell.setImage(imageHalfCircle);
						if (textualVesselMarkers) {
							cell.setText("○");
						}
						// } else {
						// if (isSet(changeSetRow.getNewVesselName())) {
						// cell.setImage(imageClosedCircle);
						// if (textualVesselMarkers) {
						// cell.setText("●");
						// }
						// }
					}
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {
					final ChangeSetTableRow changeSetRow = (ChangeSetTableRow) element;

					if (isSet(changeSetRow.getBeforeVesselName()) && isSet(changeSetRow.getAfterVesselName())) {
						if (!Objects.equals(changeSetRow.getBeforeVesselName(), changeSetRow.getAfterVesselName())) {
							return String.format("Changed from %s to %s", changeSetRow.getBeforeVesselName(), changeSetRow.getAfterVesselName());
						} else {
							return String.format("On %s", changeSetRow.getAfterVesselName());
						}
					} else if (isSet(changeSetRow.getBeforeVesselName())) {
						return String.format("Unassigned from %s", changeSetRow.getBeforeVesselName());
					} else if (isSet(changeSetRow.getAfterVesselName())) {
						return String.format("Assigned to %s", changeSetRow.getAfterVesselName());
					}
				}
				return null;
			}
		};
	}

	@PreDestroy
	public void dispose() {

		scenarioComparisonService.removeListener(listener);
		cleanUp(this.root);
		diagram.setChangeSetRoot(ChangesetFactory.eINSTANCE.createChangeSetTableRoot());
		this.root = null;
		{
			for (final Map.Entry<ScenarioResult, ScenarioInstanceDeletedListener> e : listenerMap.entrySet()) {
				final ScenarioResult scenarioResult = e.getKey();
				final ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();
				final IScenarioService scenarioService = scenarioInstance.getScenarioService();
				final ScenarioInstanceDeletedListener listener = e.getValue();
				if (scenarioService != null && listener != null) {
					scenarioService.removeScenarioServiceListener(listener);
				}
			}
			listenerMap.clear();
		}

		cleanUpVesselColumns();
		if (vesselColumnGroup != null && !vesselColumnGroup.isDisposed()) {
			vesselColumnGroup.dispose();

		}

		if (boldFont != null) {
			boldFont.dispose();
			boldFont = null;
		}
		if (imageHalfCircle != null) {
			imageHalfCircle.dispose();
			imageHalfCircle = null;
		}
		if (imageOpenCircle != null) {
			imageOpenCircle.dispose();
			imageOpenCircle = null;
		}
		if (imageClosedCircle != null) {
			imageClosedCircle.dispose();
			imageClosedCircle = null;
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
	private void handleDiffToBaseToggle(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_COMPARE_TO_BASE) final Object o) {
		diffToBase = !diffToBase;
		if (diffToBase) {
			diagram.setChangeSetRoot(tableRootToBase);
			ViewerHelper.setInput(viewer, true, tableRootToBase);
		} else {
			diagram.setChangeSetRoot(tableRootToPrevious);
			ViewerHelper.setInput(viewer, true, tableRootToPrevious);
		}
	}

	@Inject
	@Optional
	private void handleShowStructuralChangesToggle(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_NON_STRUCTURAL_CHANGES) final MPart activePart) {
		if (activePart.getObject() == this) {
			showNonStructuralChanges = !showNonStructuralChanges;
			ViewerHelper.refresh(viewer, true);
		}
	}

	@Inject
	@Optional
	private void handleToggleInsertionPlanDuplicates(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_INSERTION_CHANGES) final MPart activePart) {
		if (activePart.getObject() == this) {
			insertionPlanFilter.toggleFilter();
			ViewerHelper.refresh(viewer, true);
		}
	}

	@Inject
	@Optional
	private void handleSwitchGroupByModel(@UIEventTopic(ChangeSetViewEventConstants.EVENT_SWITCH_GROUP_BY_MODE) final SwitchGroupModeEvent event) {
		if (event.activePart.getObject() == this) {
			insertionPlanFilter.setGroupMode(event.mode);
			String id = lastTargetSlot == null ? null : lastTargetSlot.getName();

			openAnalyticsSolution(lastSolution, id);
			// this.solution = solution;
			// ViewerHelper.refresh(viewer, true);
		}
	}

	@Inject
	@Optional
	private void handleSwitchSlot(@UIEventTopic(ChangeSetViewEventConstants.EVENT_SWITCH_TARGET_SLOT) final SwitchSlotEvent event) {
		if (event.activePart.getObject() == this) {
			openAnalyticsSolution(lastSolution, event.slotId);
			// this.solution = solution;
			// ViewerHelper.refresh(viewer, true);
		}
	}

	@Inject
	@Optional
	private void handleAnalyseScenario(@UIEventTopic(ChangeSetViewEventConstants.EVENT_ANALYSE_ACTION_SETS) final ScenarioInstance target) {
		if (!handleEvents) {
			return;
		}
		if (viewMode != ViewMode.ACTION_SET) {
			return;
		}
		this.viewMode = ViewMode.ACTION_SET;
		setNewDataData(target, monitor -> {
			final ActionSetTransformer transformer = new ActionSetTransformer();
			return transformer.createDataModel(target, monitor);
		}, null);
	}

	private CellLabelProvider createLambdaLabelProvider(final boolean asInt, final boolean asSigFigs, final Function<ChangeSetTableRow, Number> calcF,
			final Function<ChangeSetTableRow, Number> calcT) {

		final ToDoubleBiFunction<Number, Number> deltaDoubleUpdater = (f, t) -> {
			double delta = 0.0;
			if (f != null) {
				delta -= f.doubleValue();
			}
			if (t != null) {
				delta += t.doubleValue();
			}
			return delta;
		};
		final ToIntBiFunction<Number, Number> deltaIntegerUpdater = (f, t) -> {
			int delta = 0;
			if (f != null) {
				delta -= f.intValue();
			}
			if (t != null) {
				delta += t.intValue();
			}
			return delta;
		};

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setFont(null);
				double delta = 0;
				if (element instanceof ChangeSetTableGroup) {
					cell.setFont(boldFont);
					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final List<ChangeSetTableRow> rows = group.getRows();
					if (rows != null) {
						for (final ChangeSetTableRow change : rows) {
							if (asInt) {
								delta += deltaIntegerUpdater.applyAsInt(calcF.apply(change), calcT.apply(change));
							} else {
								delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
							}
						}
					}
				} else if (element instanceof ChangeSetTableRow) {

					final ChangeSetTableRow change = (ChangeSetTableRow) element;
					if (asInt) {
						delta += deltaIntegerUpdater.applyAsInt(calcF.apply(change), calcT.apply(change));
					} else {
						delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
					}
				}

				if (asInt) {
					delta = delta / 1000000.0;
					if (Math.abs(delta) > 0.001) {
						if (asSigFigs) {
							cell.setText(String.format("%s %,.3G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						} else {
							cell.setText(String.format("%s %,.3f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					}
				} else {
					if (Math.abs(delta) > 0.009) {
						if (asSigFigs) {
							cell.setText(String.format("%s %,.2G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						} else {
							cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					}
				}
			}
		};
	}

	boolean isSet(@Nullable final String str) {
		return str != null && !str.isEmpty();
	}

	private void openView(final @NonNull String viewId) {
		// Open, but do not focus the view.
		// FIXME: If this causes the view to be created the selection will not be correct. However attempting to re-set the selection does not appear to work.
		partService.showPart(viewId, PartState.VISIBLE);
	}

	private CellLabelProvider createPriceLabelProvider(final boolean isLoad) {

		final Function<ChangeSetTableRow, Number> calcF;
		final Function<ChangeSetTableRow, Number> calcT;
		if (isLoad) {
			calcF = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
			calcT = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
		} else {
			calcF = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_BEFORE, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
			calcT = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW__LHS_AFTER, ChangesetPackage.Literals.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
		}
		final ToDoubleBiFunction<Number, Number> deltaDoubleUpdater = (f, t) -> {
			double delta = 0.0;
			if (f != null) {
				delta -= f.doubleValue();
			}
			if (t != null) {
				delta += t.doubleValue();
			}
			return delta;
		};

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setFont(null);
				double delta = 0;
				if (element instanceof ChangeSetTableGroup) {
					// cell.setFont(boldFont);
					final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
					final List<ChangeSetTableRow> rows = group.getRows();
					if (rows != null) {
						for (final ChangeSetTableRow change : rows) {
							delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
						}
					}
				} else if (element instanceof ChangeSetTableRow) {

					final ChangeSetTableRow change = (ChangeSetTableRow) element;
					delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));

					if (isLoad) {
						final SlotAllocation allocation = change.getLhsAfter() != null ? change.getLhsAfter().getLoadAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									cell.setText(String.format("=%,.2f", allocation.getPrice()));
									return;
								}
							}
						}
					} else {
						final SlotAllocation allocation = change.getRhsAfter() != null ? change.getRhsAfter().getDischargeAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									cell.setText(String.format("=%,.2f", allocation.getPrice()));
									return;
								}
							}
						}
					}
				}
				if (Math.abs(delta) > 0.009) {
					cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetTableRow) {

					final ChangeSetTableRow change = (ChangeSetTableRow) element;

					if (isLoad) {
						final SlotAllocation allocation = change.getLhsAfter() != null ? change.getLhsAfter().getLoadAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									return String.format("Break-even price is %,.2f", allocation.getPrice());
								}
							}
						}
					} else {
						final SlotAllocation allocation = change.getRhsAfter() != null ? change.getRhsAfter().getDischargeAllocation() : null;
						if (allocation != null) {
							final Slot slot = allocation.getSlot();
							if (slot != null) {
								final String expr = slot.getPriceExpression();
								if (expr != null && expr.contains("?")) {
									return String.format("Break-even price is %,.2f", allocation.getPrice());
								}
							}
						}
					}
				}

				return null;
			}
		};
	}

	private class ContextMenuManager implements MenuDetectListener {

		private final @NonNull MenuManager mgr;

		private Menu menu;

		public ContextMenuManager(@NonNull final MenuManager mgr) {
			this.mgr = mgr;
		}

		@Override
		public void menuDetected(final MenuDetectEvent e) {
			final Grid grid = viewer.getGrid();
			if (menu == null) {
				menu = mgr.createContextMenu(grid);
			}
			mgr.removeAll();

			final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			final GridItem[] items = grid.getSelection();
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
					}
				}
				boolean showMenu = false;
				if (selectedSets.size() == 1) {
					if (canExportChangeSet) {
						if (ChangeSetView.this.viewMode == ViewMode.ACTION_SET) {
							final ChangeSetTableGroup changeSetTableGroup = selectedSets.iterator().next();
							mgr.add(new ExportChangeAction(changeSetTableGroup));
							showMenu = true;
						}
					}
				}
				if (selectedSets.size() > 1) {
					if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
						mgr.add(new MergeChangesAction(selectedSets, viewer));
						showMenu = true;
					}

				}
				if (showMenu) {
					menu.setVisible(true);
				}
			}
		}
	}

	@OpenChangeSetHandler
	public void openAnalyticsSolution(final AnalyticsSolution solution) {
		openAnalyticsSolution(solution, null);
	}

	public void openAnalyticsSolution(final AnalyticsSolution solution, @Nullable String slotId) {
		this.lastSolution = solution;
		this.viewMode = ViewMode.ACTION_SET;

		persistAnalyticsSolution = true;

		final ScenarioInstance target = solution.getScenarioInstance();
		final EObject plan = solution.getSolution();
		// Do something?
		if (plan instanceof ActionableSetPlan) {
			this.viewMode = ViewMode.ACTION_SET;
			this.canExportChangeSet = true;
			setNewDataData(target, monitor -> {
				final ActionableSetPlanTransformer transformer = new ActionableSetPlanTransformer();
				return transformer.createDataModel(target, (ActionableSetPlan) plan, monitor);
			}, null);
		} else if (plan instanceof SlotInsertionOptions) {
			final SlotInsertionOptions slotInsertionOptions = (SlotInsertionOptions) plan;
			this.viewMode = ViewMode.ACTION_SET;
			this.canExportChangeSet = true;

			Slot targetSlot = slotInsertionOptions.getSlotsInserted().get(0);
			if (slotId != null) {
				for (Slot s : slotInsertionOptions.getSlotsInserted()) {
					if (slotId.equals(s.getName())) {
						targetSlot = s;
						break;
					}
				}
			}
			Slot pTargetSlot = targetSlot;
			lastTargetSlot = pTargetSlot;

			setNewDataData(target, monitor -> {
				final InsertionPlanTransformer transformer = new InsertionPlanTransformer();

				// Returns a new sort order -- based on the assumption input data is sorted by best value first

				final ChangeSetRoot newRoot = transformer.createDataModel(target, slotInsertionOptions, monitor, pTargetSlot);
				final List<ChangeSet> processChangeSetRoot = insertionPlanFilter.processChangeSetRoot(newRoot, pTargetSlot);
				newRoot.getChangeSets().clear();
				newRoot.getChangeSets().addAll(processChangeSetRoot);
				insertionPlanFilter.setFilterActive(true);
				return newRoot;
			}, pTargetSlot);
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
					if (tag.startsWith("ScenarioUUID-")) {
						scenarioUUID = tag.replaceFirst("ScenarioUUID-", "");
					}
					if (tag.startsWith("SolutionUUID-")) {
						solutionUUID = tag.replaceFirst("SolutionUUID-", "");
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
						UUIDObject solution = null;
						try (ModelReference ref = instance[0].getReference("ChangeSetView:restore")) {
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
										final ModelReference ref2 = instance[0].getReference("ChangeSetView:delayedLoad");
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
		return lastSolution;
	}

	@GetCurrentTargetSlot
	public Slot getLastSlot() {
		return lastTargetSlot;
	}
}
