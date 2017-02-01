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
import java.util.Set;
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
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
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

import com.google.common.base.Objects;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ChangeSetView implements IAdaptable {

	@Inject
	private EPartService partService;

	public static enum ViewMode {
		COMPARE, ACTION_SET
	}

	private GridTreeViewer viewer;

	private boolean diffToBase = false;
	private boolean showNonStructuralChanges = false;

	private GridColumnGroup vesselColumnGroup;
	private GridViewerColumn vesselColumnStub;

	private ChangeSetWiringDiagram diagram;

	/**
	 * Display textual vessel change markers - used for unit testing where graphics are not captured in data dump.
	 */
	private boolean textualVesselMarkers = false;

	@Inject
	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	private ChangeSetRoot root;

	@Inject
	private ESelectionService eSelectionService;

	@Inject
	private ScenarioComparisonService scenarioComparisonService;

	private final IScenarioComparisonServiceListener listener = new IScenarioComparisonServiceListener() {

		@Override
		public void multiDataUpdate(final ISelectedDataProvider selectedDataProvider, final Collection<ScenarioResult> others, final Table table, final List<LNGScenarioModel> rootObjects) {
			if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
				cleanUpVesselColumns();
				setEmptyData();
			}
		}

		@Override
		public void compareDataUpdate(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pin, final ScenarioResult other, final Table table,
				final List<LNGScenarioModel> rootObjects, final Map<EObject, Set<EObject>> equivalancesMap) {
			if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
				cleanUpVesselColumns();
				if (pin == null || other == null) {
					setEmptyData();
				} else {
					final Display display = PlatformUI.getWorkbench().getDisplay();
					final ProgressMonitorDialog d = new ProgressMonitorDialog(display.getActiveShell());
					try {
						d.run(true, false, new IRunnableWithProgress() {

							@Override
							public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

								final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
								scheduleDiffUtils.setCheckAssignmentDifferences(true);
								scheduleDiffUtils.setCheckSpotMarketDifferences(true);
								scheduleDiffUtils.setCheckNextPortDifferences(true);
								final ScenarioComparisonTransformer transformer = new ScenarioComparisonTransformer();
								final ChangeSetRoot newRoot = transformer.createDataModel(selectedDataProvider, equivalancesMap, table, pin, other, monitor);
								display.asyncExec(new ViewUpdateRunnable(newRoot));
							}
						});
					} catch (InvocationTargetException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			// TODO Auto-generated method stub

		}
	};

	private final Map<ScenarioResult, ScenarioInstanceDeletedListener> listenerMap = new HashMap<>();

	private Font boldFont;

	private Image imageClosedCircle;

	private Image imageHalfCircle;
	private Image imageOpenCircle;

	private ViewMode viewMode = ViewMode.COMPARE;

	private IAdditionalAttributeProvider additionalAttributeProvider;

	private GridViewerColumn violationColumn;

	private GridViewerColumn latenessColumn;

	// private MPart part;

	private final class ViewUpdateRunnable implements Runnable {
		private final ChangeSetRoot newRoot;

		private ViewUpdateRunnable(final ChangeSetRoot newRoot) {
			this.newRoot = newRoot;
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

			if (newRoot != null) {
				for (final ChangeSet cs : newRoot.getChangeSets()) {
					for (final ChangeSetRow csr : cs.getChangeSetRowsToPrevious()) {
						vesselnames.add(csr.getOriginalVesselName());
						vesselnames.add(csr.getNewVesselName());

						shortNameMap.put(csr.getOriginalVesselName(), csr.getOriginalVesselShortName());
						shortNameMap.put(csr.getNewVesselName(), csr.getNewVesselShortName());
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

			diagram.setChangeSetRoot(newRoot);
			viewer.setInput(newRoot);
			// Release after creating the new one so we increment reference counts before decrementing, which could cause a scenario unload/load cycle
			final ChangeSetRoot oldRoot = ChangeSetView.this.root;
			ChangeSetView.this.root = newRoot;
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

			public void begin() {
				textualVesselMarkers = true;
				// Need to refresh the view to trigger creation of the text labels
				ViewerHelper.refresh(viewer, true);
			}

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
					public void displayActionPlan(List<ScenarioResult> scenarios) {
						cleanUpVesselColumns();

						final ActionSetTransformer transformer = new ActionSetTransformer();
						final ChangeSetRoot newRoot = transformer.createDataModel(new NullProgressMonitor(), scenarios);
						RunnerHelper.exec(new ViewUpdateRunnable(newRoot), true);
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

	@PostConstruct
	public void createPartControl(@Optional final MPart part, final Composite parent) {

		if (part != null) {
			for (final String tag : part.getTags()) {
				if (tag.equals("action-set")) {
					viewMode = ViewMode.ACTION_SET;
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
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
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
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
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
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__LHS_NAME));
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
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
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
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__RHS_NAME));
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
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
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
							if (o instanceof ChangeSetRow) {
								o = ((ChangeSetRow) o).eContainer();
							}
							if (o instanceof ChangeSet) {
								final ChangeSet changeSet = (ChangeSet) o;
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
					{

						final Set<Object> selectedElements = new LinkedHashSet<>();
						final Iterator<?> itr = iStructuredSelection.iterator();
						while (itr.hasNext()) {
							final Object o = itr.next();
							if (o instanceof ChangeSetRow) {
								final ChangeSetRow changeSetRow = (ChangeSetRow) o;
								selectRow(selectedElements, changeSetRow);
							} else if (o instanceof ChangeSet) {
								final ChangeSet changeSet = (ChangeSet) o;
								List<ChangeSetRow> rows;
								if (diffToBase) {
									rows = changeSet.getChangeSetRowsToBase();
								} else {
									rows = changeSet.getChangeSetRowsToPrevious();
								}
								for (final ChangeSetRow changeSetRow : rows) {
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

			private void selectRow(final Set<Object> selectedElements, final ChangeSetRow changeSetRow) {
				selectedElements.add(changeSetRow);
				selectedElements.add(changeSetRow.getLoadSlot());
				selectedElements.add(changeSetRow.getNewLoadAllocation());
				selectedElements.add(changeSetRow.getOriginalLoadAllocation());
				if (changeSetRow.getNewLoadAllocation() != null) {
					selectedElements.add(changeSetRow.getNewLoadAllocation().getSlotVisit());
					selectedElements.add(changeSetRow.getNewLoadAllocation().getSlotVisit().getSequence());
				}
				if (changeSetRow.getOriginalLoadAllocation() != null) {
					selectedElements.add(changeSetRow.getOriginalLoadAllocation().getSlotVisit());
					selectedElements.add(changeSetRow.getOriginalLoadAllocation().getSlotVisit().getSequence());
				}
				selectedElements.add(changeSetRow.getNewDischargeAllocation());
				selectedElements.add(changeSetRow.getOriginalDischargeAllocation());
				if (changeSetRow.getNewDischargeAllocation() != null) {
					selectedElements.add(changeSetRow.getNewDischargeAllocation().getSlotVisit());
					selectedElements.add(changeSetRow.getNewDischargeAllocation().getSlotVisit().getSequence());
				}
				if (changeSetRow.getOriginalDischargeAllocation() != null) {
					selectedElements.add(changeSetRow.getOriginalDischargeAllocation().getSlotVisit());
					selectedElements.add(changeSetRow.getOriginalDischargeAllocation().getSlotVisit().getSequence());
				}
				selectedElements.add(changeSetRow.getNewGroupProfitAndLoss());
				selectedElements.add(changeSetRow.getOriginalGroupProfitAndLoss());
				selectedElements.add(changeSetRow.getNewEventGrouping());
				if (changeSetRow.getNewEventGrouping() instanceof Event) {
					selectedElements.add(((Event) changeSetRow.getNewEventGrouping()).getSequence());
				}
				selectedElements.add(changeSetRow.getOriginalEventGrouping());
				if (changeSetRow.getOriginalEventGrouping() instanceof Event) {
					selectedElements.add(((Event) changeSetRow.getOriginalEventGrouping()).getSequence());
				}
				if (changeSetRow.getOriginalOpenLoadAllocation() != null) {
					selectedElements.add(changeSetRow.getOriginalOpenLoadAllocation());
					selectedElements.add(changeSetRow.getOriginalOpenLoadAllocation().getSlot());
				}
				if (changeSetRow.getOriginalOpenDischargeAllocation() != null) {
					selectedElements.add(changeSetRow.getOriginalOpenDischargeAllocation());
					selectedElements.add(changeSetRow.getOriginalOpenDischargeAllocation().getSlot());
				}
				if (changeSetRow.getNewOpenLoadAllocation() != null) {
					selectedElements.add(changeSetRow.getNewOpenLoadAllocation());
					selectedElements.add(changeSetRow.getNewOpenLoadAllocation().getSlot());
				}
				if (changeSetRow.getNewOpenDischargeAllocation() != null) {
					selectedElements.add(changeSetRow.getNewOpenDischargeAllocation());
					selectedElements.add(changeSetRow.getNewOpenDischargeAllocation().getSlot());
				}
			}
		});
		//
		// // Selection listener for current selection.
		// viewer.addSelectionChangedListener(new ISelectionChangedListener() {
		//
		// @Override
		// public void selectionChanged(final SelectionChangedEvent event) {
		// final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		//
		// final Set<Object> selectedElements = new LinkedHashSet<>();
		// final Iterator<?> itr = selection.iterator();
		// while (itr.hasNext()) {
		// final Object o = itr.next();
		// if (o instanceof ChangeSetRow) {
		// final ChangeSetRow changeSetRow = (ChangeSetRow) o;
		// selectRow(selectedElements, changeSetRow);
		// } else if (o instanceof ChangeSet) {
		// final ChangeSet changeSet = (ChangeSet) o;
		// List<ChangeSetRow> rows;
		// if (diffToBase) {
		// rows = changeSet.getChangeSetRowsToBase();
		// } else {
		// rows = changeSet.getChangeSetRowsToPrevious();
		// }
		// for (final ChangeSetRow changeSetRow : rows) {
		// selectRow(selectedElements, changeSetRow);
		// }
		// }
		// }
		// while (selectedElements.remove(null))
		// ;
		//
		// // Update selected elements
		// scenarioComparisonService.setSelectedElements(selectedElements);
		//
		// // set the selection to the service
		// eSelectionService.setPostSelection(new StructuredSelection(selectedElements.toArray()));
		//
		// }
		//
		// private void selectRow(final Set<Object> selectedElements, final ChangeSetRow changeSetRow) {
		// selectedElements.add(changeSetRow.getLoadSlot());
		// selectedElements.add(changeSetRow.getNewLoadAllocation());
		// selectedElements.add(changeSetRow.getOriginalLoadAllocation());
		// if (changeSetRow.getNewLoadAllocation() != null) {
		// selectedElements.add(changeSetRow.getNewLoadAllocation().getSlotVisit());
		// selectedElements.add(changeSetRow.getNewLoadAllocation().getSlotVisit().getSequence());
		// }
		// if (changeSetRow.getOriginalLoadAllocation() != null) {
		// selectedElements.add(changeSetRow.getOriginalLoadAllocation().getSlotVisit());
		// selectedElements.add(changeSetRow.getOriginalLoadAllocation().getSlotVisit().getSequence());
		// }
		// selectedElements.add(changeSetRow.getNewDischargeAllocation());
		// selectedElements.add(changeSetRow.getOriginalDischargeAllocation());
		// if (changeSetRow.getNewDischargeAllocation() != null) {
		// selectedElements.add(changeSetRow.getNewDischargeAllocation().getSlotVisit());
		// selectedElements.add(changeSetRow.getNewDischargeAllocation().getSlotVisit().getSequence());
		// }
		// if (changeSetRow.getOriginalDischargeAllocation() != null) {
		// selectedElements.add(changeSetRow.getOriginalDischargeAllocation().getSlotVisit());
		// selectedElements.add(changeSetRow.getOriginalDischargeAllocation().getSlotVisit().getSequence());
		// }
		// selectedElements.add(changeSetRow.getNewGroupProfitAndLoss());
		// selectedElements.add(changeSetRow.getOriginalGroupProfitAndLoss());
		// selectedElements.add(changeSetRow.getNewEventGrouping());
		// if (changeSetRow.getNewEventGrouping() instanceof Event) {
		// selectedElements.add(((Event) changeSetRow.getNewEventGrouping()).getSequence());
		// }
		// selectedElements.add(changeSetRow.getOriginalEventGrouping());
		// if (changeSetRow.getOriginalEventGrouping() instanceof Event) {
		// selectedElements.add(((Event) changeSetRow.getOriginalEventGrouping()).getSequence());
		// }
		// }
		// });

		final ViewerFilter[] filters = new ViewerFilter[1];
		filters[0] = new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (!showNonStructuralChanges) {
					if (element instanceof ChangeSetRow) {
						final ChangeSetRow row = (ChangeSetRow) element;
						if (!row.isWiringChange() && !row.isVesselChange()) {
							final long delta = ChangeSetTransformerUtil.getNewRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupProfitAndLoss)
									- ChangeSetTransformerUtil.getOriginalRowProfitAndLossValue(row, ScheduleModelKPIUtils::getGroupProfitAndLoss);
							long totalPNLDelta = 0;
							if (parentElement instanceof ChangeSet) {
								final ChangeSet changeSet = (ChangeSet) parentElement;
								totalPNLDelta = changeSet.getMetricsToPrevious().getPnlDelta();
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
									ViewerCell cell = viewer.getCell(mousePoint);
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
			public void paint(final GC gc, final Object value) {
				// TODO Auto-generated method stub
				super.paint(gc, value);
				if (value instanceof GridItem) {
					final GridItem gridItem = (GridItem) value;
					final Object data = gridItem.getData();
					if (data instanceof ChangeSet) {
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

	private CellLabelProvider createStandardLabelProvider(final EStructuralFeature attrib) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;
					cell.setText((String) change.eGet(attrib));
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
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					LocalDate windowStart = null;
					boolean isDelta = false;
					int deltaHours = 0;

					if (isLoadSide) {
						final SlotAllocation originalLoadAllocation = change.getOriginalLoadAllocation();
						final SlotAllocation newLoadAllocation = change.getNewLoadAllocation();

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
						final SlotAllocation originalDischargeAllocation = change.getRhsWiringLink() != null ? change.getRhsWiringLink().getOriginalDischargeAllocation() : null;
						final SlotAllocation newDischargeAllocation = change.getNewDischargeAllocation();

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

	private CellLabelProvider createDeltaLabelProvider(final boolean asInt, final EStructuralFeature from, final EStructuralFeature to, final EStructuralFeature attrib) {

		return createLambdaLabelProvider(asInt, false, change -> getNumber(from, attrib, change), change -> getNumber(to, attrib, change));
	}

	@NonNull
	private Number getNumber(final EStructuralFeature from, final EStructuralFeature attrib, final ChangeSetRow change) {
		Number n = null;
		try {
			n = getNumberInt(from, attrib, change);
		} catch (final Exception e) {

		}
		if (n == null) {
			return Long.valueOf(0L);

		}
		return n;

	}

	@Nullable
	private Number getNumberInt(final EStructuralFeature from, final EStructuralFeature attrib, final ChangeSetRow change) {
		if (change != null && change.eClass().getEAllStructuralFeatures().contains(from)) {
			final EObject eObject = (EObject) change.eGet(from);
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
				if (element instanceof ChangeSet) {

					cell.setFont(boldFont);

					final ChangeSet changeSet = (ChangeSet) element;
					final DeltaMetrics metrics;
					if (diffToBase) {
						metrics = changeSet.getMetricsToBase();
					} else {
						metrics = changeSet.getMetricsToPrevious();
					}
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
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					long f = ChangeSetKPIUtil.getPNL(change, ResultType.ORIGINAL);
					long t = ChangeSetKPIUtil.getPNL(change, ResultType.NEW);

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
			return ChangeSetKPIUtil.getTax(change, ResultType.ORIGINAL);
		}, change -> {
			return ChangeSetKPIUtil.getTax(change, ResultType.NEW);
		});
	}

	private CellLabelProvider createLatenessDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					long[] originalLateness = ChangeSetKPIUtil.getLateness(change, ResultType.ORIGINAL);
					long[] newLateness = ChangeSetKPIUtil.getLateness(change, ResultType.NEW);

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
				if (element instanceof ChangeSet) {

					cell.setFont(boldFont);

					final ChangeSet changeSet = (ChangeSet) element;
					final Metrics scenarioMetrics = changeSet.getCurrentMetrics();
					final DeltaMetrics deltaMetrics;
					if (diffToBase) {
						deltaMetrics = changeSet.getMetricsToBase();
					} else {
						deltaMetrics = changeSet.getMetricsToPrevious();
					}
					if (deltaMetrics != null) {
						final int latenessDelta = (int) Math.round((double) deltaMetrics.getLatenessDelta() / 24.0);
						final int lateness = (int) Math.round((double) scenarioMetrics.getLateness() / 24.0);
						cell.setText(String.format("%s%d / %d", latenessDelta < 0 ? "↓" : latenessDelta == 0 ? "" : "↑", Math.abs(latenessDelta), lateness));

						if (lateness != 0) {
							cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						}
					}
				}
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					long[] originalLateness = ChangeSetKPIUtil.getLateness(change, ResultType.ORIGINAL);
					long[] newLateness = ChangeSetKPIUtil.getLateness(change, ResultType.NEW);

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
				if (element instanceof ChangeSet) {

					cell.setFont(boldFont);

					final ChangeSet changeSet = (ChangeSet) element;
					final Metrics scenarioMetrics = changeSet.getCurrentMetrics();
					final DeltaMetrics deltaMetrics;
					if (diffToBase) {
						deltaMetrics = changeSet.getMetricsToBase();
					} else {
						deltaMetrics = changeSet.getMetricsToPrevious();
					}
					if (deltaMetrics != null) {
						cell.setText(String.format("%s%d / %d", deltaMetrics.getCapacityDelta() < 0 ? "↓" : deltaMetrics.getCapacityDelta() == 0 ? "" : "↑", Math.abs(deltaMetrics.getCapacityDelta()),
								scenarioMetrics.getCapacity()));

						if (scenarioMetrics.getCapacity() != 0) {
							cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						}
					}
				}
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					long f = ChangeSetKPIUtil.getViolations(change, ResultType.ORIGINAL);
					long t = ChangeSetKPIUtil.getViolations(change, ResultType.NEW);

					long delta = t - f;
					if (delta != 0) {
						cell.setText(String.format("%s %d", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createCargoOtherPNLDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, change -> ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.ORIGINAL), change -> ChangeSetKPIUtil.getCargoOtherPNL(change, ResultType.NEW));
	}

	private CellLabelProvider createUpstreamDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.ORIGINAL), change -> ChangeSetKPIUtil.getUpstreamPNL(change, ResultType.NEW));
	}

	private CellLabelProvider createAdditionalShippingPNLDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.ORIGINAL),
				change -> ChangeSetKPIUtil.getAdditionalShippingPNL(change, ResultType.NEW));
	}

	private CellLabelProvider createAdditionalUpsidePNLDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.ORIGINAL),
				change -> ChangeSetKPIUtil.getAdditionalUpsidePNL(change, ResultType.NEW));
	}

	private CellLabelProvider createShippingDeltaLabelProvider() {
		return createLambdaLabelProvider(true, false, change -> ChangeSetKPIUtil.getShipping(change, ResultType.ORIGINAL), change -> ChangeSetKPIUtil.getShipping(change, ResultType.NEW));
	}

	private CellLabelProvider createCSLabelProvider() {

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSet) {

					cell.setFont(boldFont);

					final ChangeSet changeSet = (ChangeSet) element;
					final ChangeSetRoot root = (ChangeSetRoot) changeSet.eContainer();
					int idx = 0;
					if (root != null) {
						idx = root.getChangeSets().indexOf(changeSet);
					}
					// int changeCount = 0;
					// final List<ChangeSetRow> rows;
					// if (diffToBase) {
					// rows = changeSet.getChangeSetRowsToBase();
					// } else {
					// rows = changeSet.getChangeSetRowsToPrevious();
					// }
					// for (ChangeSetRow r : rows) {
					// if (r.isVesselChange()) {
					//// changeCount++;
					// }
					// if (r.isWiringChange() && r.getNewLoadAllocation() != null) {
					//// changeCount++;
					// }
					// if (r.getRhsWiringLink() != null ) {
					// changeCount++;
					// }
					// }

					// cell.setText(String.format("Set %d (%d)", idx + 1, changeCount));
					cell.setText(String.format("Set %d", idx + 1));
				}
			}
		};

	}

	@PostConstruct
	public void makeActions() {

	}

	public void setActionSetData(final ScenarioInstance target) {

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

						final ActionSetTransformer transformer = new ActionSetTransformer();
						final ChangeSetRoot newRoot = transformer.createDataModel(target, monitor);
						display.asyncExec(new ViewUpdateRunnable(newRoot));
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

		diagram.setChangeSetRoot(newRoot);
		ViewerHelper.setInput(viewer, true, newRoot);

		final ChangeSetRoot oldRoot = ChangeSetView.this.root;
		ChangeSetView.this.root = newRoot;
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
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;

					if (name.equals(changeSetRow.getNewVesselName())) {
						cell.setImage(imageClosedCircle);
						if (textualVesselMarkers) {
							cell.setText("●");
						}
					} else if (name.equals(changeSetRow.getOriginalVesselName())) {
						cell.setImage(imageOpenCircle);
						if (textualVesselMarkers) {
							cell.setText("○");
						}
					}
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;

					if (name.equals(changeSetRow.getNewVesselName())) {
						return name;
					}
					if (name.equals(changeSetRow.getOriginalVesselName())) {
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
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;

					if (textualVesselMarkers) {
						if (changeSetRow.isWiringChange()) {
							String left = "";
							String right = "";

							final LoadSlot load = changeSetRow.getLoadSlot();
							if (load != null) {
								left = load.isDESPurchase() ? "○" : "●";
							}
							final DischargeSlot discharge = changeSetRow.getDischargeSlot();
							if (discharge != null) {
								right = discharge.isFOBSale() ? "●" : "○";
							}

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
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;
					if (!Objects.equal(changeSetRow.getNewVesselName(), changeSetRow.getOriginalVesselName())) {
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
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;

					if (isSet(changeSetRow.getOriginalVesselName()) && isSet(changeSetRow.getNewVesselName())) {
						if (!Objects.equal(changeSetRow.getOriginalVesselName(), changeSetRow.getNewVesselName())) {
							return String.format("Changed from %s to %s", changeSetRow.getOriginalVesselName(), changeSetRow.getNewVesselName());
						} else {
							return String.format("On %s", changeSetRow.getNewVesselName());
						}
					} else if (isSet(changeSetRow.getOriginalVesselName())) {
						return String.format("Unassigned from %s", changeSetRow.getOriginalVesselName());
					} else if (isSet(changeSetRow.getNewVesselName())) {
						return String.format("Assigned to %s", changeSetRow.getNewVesselName());
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
		diagram.setChangeSetRoot(ChangesetFactory.eINSTANCE.createChangeSetRoot());
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
				if (element instanceof ChangeSetRoot) {
					return true;
				}
				if (element instanceof ChangeSet) {
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

				if (inputElement instanceof ChangeSetRoot) {
					final ChangeSetRoot changeSetRoot = (ChangeSetRoot) inputElement;
					return changeSetRoot.getChangeSets().toArray();
				}

				return new Object[0];
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof ChangeSet) {
					final ChangeSet changeSet = (ChangeSet) parentElement;
					if (diffToBase) {
						return changeSet.getChangeSetRowsToBase().toArray();
					} else {
						return changeSet.getChangeSetRowsToPrevious().toArray();
					}
				}
				return null;
			}
		};
	}

	@Inject
	@Optional
	private void handleDiffToBaseToggle(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_COMPARE_TO_BASE) final Object o) {
		diffToBase = !diffToBase;
		diagram.setDiffToBase(diffToBase);
		ViewerHelper.refresh(viewer, true);
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
	private void handleAnalyseScenario(@UIEventTopic(ChangeSetViewEventConstants.EVENT_ANALYSE_ACTION_SETS) final ScenarioInstance target) {
		if (viewMode != ViewMode.ACTION_SET) {
			return;
		}
		this.viewMode = ViewMode.ACTION_SET;
		setActionSetData(target);
	}

	private CellLabelProvider createLambdaLabelProvider(final boolean asInt, final boolean asSigFigs, final Function<ChangeSetRow, Number> calcF, final Function<ChangeSetRow, Number> calcT) {

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
				if (element instanceof ChangeSet) {
					cell.setFont(boldFont);
					final ChangeSet changeSet = (ChangeSet) element;
					final List<ChangeSetRow> rows;
					if (diffToBase) {
						rows = changeSet.getChangeSetRowsToBase();
					} else {
						rows = changeSet.getChangeSetRowsToPrevious();
					}
					if (rows != null) {
						for (final ChangeSetRow change : rows) {
							if (asInt) {
								delta += deltaIntegerUpdater.applyAsInt(calcF.apply(change), calcT.apply(change));
							} else {
								delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
							}
						}
					}
				} else if (element instanceof ChangeSetRow) {

					final ChangeSetRow change = (ChangeSetRow) element;
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

		final Function<ChangeSetRow, Number> calcF;
		final Function<ChangeSetRow, Number> calcT;
		if (isLoad) {
			calcF = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
			calcT = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
		} else {
			calcF = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
			calcT = change -> getNumber(ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE, change);
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
				if (element instanceof ChangeSet) {
					// cell.setFont(boldFont);
					final ChangeSet changeSet = (ChangeSet) element;
					final List<ChangeSetRow> rows;
					if (diffToBase) {
						rows = changeSet.getChangeSetRowsToBase();
					} else {
						rows = changeSet.getChangeSetRowsToPrevious();
					}
					if (rows != null) {
						for (final ChangeSetRow change : rows) {
							delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));
						}
					}
				} else if (element instanceof ChangeSetRow) {

					final ChangeSetRow change = (ChangeSetRow) element;
					delta += deltaDoubleUpdater.applyAsDouble(calcF.apply(change), calcT.apply(change));

					if (isLoad) {
						final SlotAllocation allocation = change.getNewLoadAllocation();
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
						final SlotAllocation allocation = change.getNewDischargeAllocation();
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
				if (element instanceof ChangeSetRow) {

					final ChangeSetRow change = (ChangeSetRow) element;

					if (isLoad) {
						final SlotAllocation allocation = change.getNewLoadAllocation();
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
						final SlotAllocation allocation = change.getNewDischargeAllocation();
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
			if (items.length > 1) {
				final Set<ChangeSet> selectedSets = new LinkedHashSet<>();
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof ChangeSet) {
						selectedSets.add((ChangeSet) obj);
					} else if (obj instanceof ChangeSetRow) {
						final ChangeSetRow changeSetRow = (ChangeSetRow) obj;
						selectedSets.add((ChangeSet) changeSetRow.eContainer());
					}
				}
				boolean showMenu = false;
				if (selectedSets.size() > 1) {
					if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {

						mgr.add(new RunnableAction("Merge changes", () -> {
							final ChangeSet firstChangeSet = selectedSets.iterator().next();
							selectedSets.remove(firstChangeSet);
							for (final ChangeSet cs : selectedSets) {
								firstChangeSet.getChangeSetRowsToBase().addAll(cs.getChangeSetRowsToBase());
								firstChangeSet.getChangeSetRowsToPrevious().addAll(cs.getChangeSetRowsToPrevious());
								if (firstChangeSet.getCurrentMetrics() != null) {
									final int pnl = firstChangeSet.getCurrentMetrics().getPnl();
									firstChangeSet.getCurrentMetrics().setPnl(pnl + cs.getCurrentMetrics().getPnl());

									final int capacity = firstChangeSet.getCurrentMetrics().getCapacity();
									firstChangeSet.getCurrentMetrics().setCapacity(capacity + cs.getCurrentMetrics().getCapacity());

									final int lateness = firstChangeSet.getCurrentMetrics().getLateness();
									firstChangeSet.getCurrentMetrics().setLateness(lateness + cs.getCurrentMetrics().getLateness());
								}
								if (firstChangeSet.getMetricsToBase() != null) {
									final int pnl = firstChangeSet.getMetricsToBase().getPnlDelta();
									firstChangeSet.getMetricsToBase().setPnlDelta(pnl + cs.getMetricsToBase().getPnlDelta());

									final int capacity = firstChangeSet.getMetricsToBase().getCapacityDelta();
									firstChangeSet.getMetricsToBase().setCapacityDelta(capacity + cs.getMetricsToBase().getCapacityDelta());

									final int lateness = firstChangeSet.getMetricsToBase().getLatenessDelta();
									firstChangeSet.getMetricsToBase().setLatenessDelta(lateness + cs.getMetricsToBase().getLatenessDelta());
								}
								if (firstChangeSet.getMetricsToPrevious() != null) {
									final int pnl = firstChangeSet.getMetricsToPrevious().getPnlDelta();
									firstChangeSet.getMetricsToPrevious().setPnlDelta(pnl + cs.getMetricsToPrevious().getPnlDelta());

									final int capacity = firstChangeSet.getMetricsToPrevious().getCapacityDelta();
									firstChangeSet.getMetricsToPrevious().setCapacityDelta(capacity + cs.getMetricsToPrevious().getCapacityDelta());

									final int lateness = firstChangeSet.getMetricsToPrevious().getLatenessDelta();
									firstChangeSet.getMetricsToPrevious().setLatenessDelta(lateness + cs.getMetricsToPrevious().getLatenessDelta());
								}
								if (cs.getCurrentScenarioRef() != null) {
									cs.getCurrentScenarioRef().close();
								}
								if (cs.getBaseScenarioRef() != null) {
									cs.getBaseScenarioRef().close();
								}
								if (cs.getPrevScenarioRef() != null) {
									cs.getPrevScenarioRef().close();
								}

							}
							final ChangeSetRoot root = (ChangeSetRoot) firstChangeSet.eContainer();
							root.getChangeSets().removeAll(selectedSets);
							viewer.refresh();
						}));
						showMenu = true;
					}

				}
				if (showMenu) {
					menu.setVisible(true);
				}
			}
		}
	}

}
