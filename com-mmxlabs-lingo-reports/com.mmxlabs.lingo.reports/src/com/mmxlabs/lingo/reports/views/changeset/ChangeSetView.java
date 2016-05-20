/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.lang.reflect.InvocationTargetException;
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
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
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
import org.eclipse.nebula.widgets.grid.internal.DefaultColumnHeaderRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.base.Objects;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

public class ChangeSetView implements IAdaptable {

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
		public void multiDataUpdate(final ISelectedDataProvider selectedDataProvider, final Collection<ScenarioInstance> others, final Table table, final List<LNGScenarioModel> rootObjects) {
			if (ChangeSetView.this.viewMode == ViewMode.COMPARE) {
				cleanUpVesselColumns();
				setEmptyData();
			}
		}

		@Override
		public void compareDataUpdate(final ISelectedDataProvider selectedDataProvider, final ScenarioInstance pin, final ScenarioInstance other, final Table table,
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

	private final Map<ScenarioInstance, ScenarioInstanceDeletedListener> listenerMap = new HashMap<>();

	private Font boldFont;

	private Image imageClosedCircle;

	private Image imageHalfCircle;
	private Image imageOpenCircle;

	private ViewMode viewMode = ViewMode.COMPARE;

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
				final Runnable r = new Runnable() {

					@Override
					public void run() {

						view.handleAnalyseScenario(null);
					}
				};
				RunnerHelper.syncExec(r);
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
					final Runnable r = new Runnable() {

						@Override
						public void run() {

							view.handleAnalyseScenario(null);
						}
					};
					RunnerHelper.syncExec(r);
				}
			}
		}
	}

	@Inject
	public ChangeSetView() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
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
			gvc.getColumn().setText("");
			gvc.getColumn().setTree(true);
			gvc.getColumn().setWidth(60);
			gvc.getColumn().setResizeable(true);
			gvc.getColumn().setMoveable(false);
			gvc.setLabelProvider(createCSLabelProvider());
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		final GridColumnGroup pnlComponentGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER | SWT.TOGGLE);
		// pnlComponentGroup.setText("P&L Components");
		createCenteringGroupRenderer(pnlComponentGroup);
		pnlComponentGroup.setExpanded(false);
		pnlComponentGroup.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(TreeEvent e) {
				pnlComponentGroup.setText("P&L Components");

			}

			@Override
			public void treeCollapsed(TreeEvent e) {
				pnlComponentGroup.setText("");

			}
		});

		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
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
			gvc.getColumn().setText("+ Cargo other");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createAdditionalPNLDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(false);
		}
		if (LicenseFeatures.isPermitted("features:report-equity-book")) {
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
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
			gvc.getColumn().setText("Late");
			gvc.getColumn().setHeaderTooltip("Lateness");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createLatenessDeltaLabelProvider());
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setText("Violations");
			gvc.getColumn().setHeaderTooltip("Capacity Violations");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createViolationsDeltaLabelProvider());
			createWordWrapRenderer(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());

		}

		// Space col
		createSpacerColumn();

		final GridColumnGroup loadGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		loadGroup.setText("Purchase");
		createCenteringGroupRenderer(loadGroup);
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__LHS_NAME));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setText("Wiring");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createStubLabelProvider());
			this.diagram = createWiringDiagram(gvc);
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		final GridColumnGroup dischargeGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		dischargeGroup.setText("Sale");
		createCenteringGroupRenderer(dischargeGroup);

		{
			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__RHS_NAME));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
			gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		// Space col
		createSpacerColumn();

		vesselColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER | SWT.TOGGLE);
		// vesselColumnGroup.setText("Vessels");
		vesselColumnGroup.setExpanded(false);
		createCenteringGroupRenderer(vesselColumnGroup);
		vesselColumnGroup.addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(TreeEvent e) {
				// TODO Auto-generated method stub
				vesselColumnGroup.setText("Vessels");

			}

			@Override
			public void treeCollapsed(TreeEvent e) {
				// TODO Auto-generated method stub
				vesselColumnGroup.setText("");

			}
		});
		// Vessel columns are dynamically created - create a stub column to lock down the position in the table
		{
			final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
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
								final ScenarioInstance other;
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

						// set the selection to the service
						eSelectionService.setPostSelection(new StructuredSelection(selectedElements.toArray()));
					}
				}
			}

			private void selectRow(final Set<Object> selectedElements, final ChangeSetRow changeSetRow) {
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

							final long delta = getPNL(row.getNewGroupProfitAndLoss()) - getPNL(row.getOriginalGroupProfitAndLoss());
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

			private long getPNL(@Nullable final ProfitAndLossContainer c) {
				if (c != null) {
					final GroupProfitAndLoss gpl = c.getGroupProfitAndLoss();
					if (gpl != null) {
						return gpl.getProfitAndLoss();
					}
				}

				return 0L;
			}
		};
		viewer.setFilters(filters);

		scenarioComparisonService.addListener(listener);
		scenarioComparisonService.triggerListener(listener);
	}

	protected void createSpacerColumn() {
		{

			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
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
						gc.setLineStyle(SWT.LINE_SOLID);
						gc.drawLine(getBounds().x, getBounds().y, getBounds().width + getBounds().x, getBounds().y);
						gc.setLineStyle(SWT.LINE_DOT);
						gc.drawLine(getBounds().x, getBounds().y + getBounds().height, getBounds().width + getBounds().x, getBounds().y + getBounds().height);
						gc.setLineStyle(s);
					}
				}

			}
		};
	}

	@SuppressWarnings("restriction")
	private void createWordWrapRenderer(final GridViewerColumn gvc) {
		final DefaultColumnHeaderRenderer renderer = new DefaultColumnHeaderRenderer();
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

						cell.setText(String.format("%s%,.3G", metrics.getPnlDelta() < 0 ? "↓" : "↑", Math.abs(delta)));
					}
				}
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					Number f = null;
					{
						final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
						if (pnlContainer != null) {
							f = ScheduleModelKPIUtils.getGroupProfitAndLoss(pnlContainer);
						}
					}
					Number t = null;
					{
						final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
						if (pnlContainer != null) {
							t = ScheduleModelKPIUtils.getGroupProfitAndLoss(pnlContainer);
						}
					}
					double delta = 0;
					if (f != null) {
						delta -= f.intValue();
					}
					if (t != null) {
						delta += t.intValue();
					}
					delta = delta / 1000000.0;
					if (delta != 0) {
						cell.setText(String.format("%s %,.3G", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createSalesRevenueLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> {
			final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
			if (pnlContainer instanceof CargoAllocation) {
				long sum = 0;
				CargoAllocation cargoAllocation = (CargoAllocation) pnlContainer;
				for (SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					if (sa.getSlot() instanceof DischargeSlot) {
						sum += sa.getVolumeValue();
					}
				}
				return sum;
			}
			return null;

		}, change -> {
			final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
			if (pnlContainer instanceof CargoAllocation) {
				long sum = 0;
				CargoAllocation cargoAllocation = (CargoAllocation) pnlContainer;
				for (SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					if (sa.getSlot() instanceof DischargeSlot) {
						sum += sa.getVolumeValue();
					}
				}
				return sum;
			}
			return null;

		});
	}

	private CellLabelProvider createTaxDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> {
			final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
			if (pnlContainer != null) {
				return ScheduleModelKPIUtils.getGroupProfitAndLoss(pnlContainer) - ScheduleModelKPIUtils.getGroupPreTaxProfitAndLoss(pnlContainer);
			}
			return null;

		}, change -> {
			final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
			if (pnlContainer != null) {
				return ScheduleModelKPIUtils.getGroupProfitAndLoss(pnlContainer) - ScheduleModelKPIUtils.getGroupPreTaxProfitAndLoss(pnlContainer);
			}
			return null;

		});
	}

	private CellLabelProvider createLatenessDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					long originalLateWithFlex = 0;
					long originalLateWithoutFlex = 0;
					{
						final EventGrouping eventGrouping = change.getOriginalEventGrouping();
						if (eventGrouping != null) {
							originalLateWithFlex = LatenessUtils.getLatenessAfterFlex(eventGrouping);
							originalLateWithoutFlex = LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						}
					}
					long newLatenessWithFlex = 0;
					long newLatenessWithoutFlex = 0;
					{
						final EventGrouping eventGrouping = change.getNewEventGrouping();
						if (eventGrouping != null) {
							newLatenessWithFlex = LatenessUtils.getLatenessAfterFlex(eventGrouping);
							newLatenessWithoutFlex = LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						}
					}

					// No lateness
					if (originalLateWithoutFlex == 0 && newLatenessWithoutFlex == 0) {
						return null;
					}

					final boolean originalInFlex = originalLateWithoutFlex > 0 && originalLateWithFlex == 0;
					final boolean newInFlex = newLatenessWithoutFlex > 0 && newLatenessWithFlex == 0;

					if (originalInFlex != newInFlex) {
						// Lateness shift between flex and non-flex times.
						final long delta = newLatenessWithoutFlex - originalLateWithoutFlex;
						final String flexStr;
						if (originalLateWithoutFlex == 0) {
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
						final long delta = newLatenessWithoutFlex - originalLateWithoutFlex;
						return String.format("Lateness %s by %d days, %d hours", delta > 0 ? "increased" : "decreased", Math.abs(delta) / 24, Math.abs(delta) % 24);
						// }
					} else {
						// if (originalLateWithoutFlex > 0 && newLatenessWithoutFlex > 0) {
						final long delta = newLatenessWithoutFlex - originalLateWithoutFlex;
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

					// cell.setFont(italicBoldFont);

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

					long originalLateWithFlex = 0;
					long originalLateWithoutFlex = 0;
					{
						final EventGrouping eventGrouping = change.getOriginalEventGrouping();
						if (eventGrouping != null) {
							originalLateWithFlex = LatenessUtils.getLatenessAfterFlex(eventGrouping);
							originalLateWithoutFlex = LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						}
					}
					long newLatenessWithFlex = 0;
					long newLatenessWithoutFlex = 0;
					{
						final EventGrouping eventGrouping = change.getNewEventGrouping();
						if (eventGrouping != null) {

							newLatenessWithFlex = LatenessUtils.getLatenessAfterFlex(eventGrouping);
							newLatenessWithoutFlex = LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						}
					}

					final boolean originalInFlex = originalLateWithoutFlex > 0 && originalLateWithFlex == 0;
					final boolean newInFlex = newLatenessWithoutFlex > 0 && newLatenessWithFlex == 0;

					String flexStr = "";
					if (originalInFlex != newInFlex) {
						// Lateness shift between flex and non-flex times.
						final long delta = newLatenessWithoutFlex - originalLateWithoutFlex;
						if (originalLateWithoutFlex == 0) {
							assert originalInFlex == false;
							flexStr = " *";
						} else if (delta > 0) {
							flexStr = "";
						} else {
							flexStr = " *";
						}
					}
					long delta = 0L;
					delta -= originalLateWithoutFlex;
					delta += newLatenessWithoutFlex;
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

					Number f = null;
					{
						final EventGrouping eventGrouping = change.getOriginalEventGrouping();
						if (eventGrouping != null) {
							f = ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
						}
					}
					Number t = null;
					{
						final EventGrouping eventGrouping = change.getNewEventGrouping();
						if (eventGrouping != null) {
							t = ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
						}
					}
					int delta = 0;
					if (f != null) {
						delta -= f.intValue();
					}
					if (t != null) {
						delta += t.intValue();
					}
					if (delta != 0) {
						cell.setText(String.format("%s %d", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createAdditionalPNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, false, change -> {

			final SlotAllocation originalLoadAllocation = change.getOriginalLoadAllocation();
			if (originalLoadAllocation != null) {
				final CargoAllocation cargoAllocation = originalLoadAllocation.getCargoAllocation();
				if (cargoAllocation != null) {
					return ScheduleModelKPIUtils.getAdditionalProfitAndLoss(cargoAllocation);
				}

			}
			return 0;
		}, change -> {
			final SlotAllocation newLoadAllocation = change.getNewLoadAllocation();
			if (newLoadAllocation != null) {
				final CargoAllocation cargoAllocation = newLoadAllocation.getCargoAllocation();
				if (cargoAllocation != null) {
					return ScheduleModelKPIUtils.getAdditionalProfitAndLoss(cargoAllocation);
				}
			}

			return 0;
		});
	}

	private CellLabelProvider createUpstreamDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> {
			final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
			if (pnlContainer != null) {
				return ScheduleModelKPIUtils.getElementUpstreamPNL(pnlContainer);
			}
			return null;

		}, change -> {
			final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
			if (pnlContainer != null) {
				return ScheduleModelKPIUtils.getElementUpstreamPNL(pnlContainer);
			}
			return null;

		});
	}

	private CellLabelProvider createAdditionalShippingPNLDeltaLabelProvider() {

		return createLambdaLabelProvider(true, true, change -> {
			final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
			if (pnlContainer != null) {
				return -ScheduleModelKPIUtils.getAdditionalShippingProfitAndLoss(pnlContainer);
			}
			return null;

		}, change -> {
			final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
			if (pnlContainer != null) {
				return -ScheduleModelKPIUtils.getAdditionalShippingProfitAndLoss(pnlContainer);
			}
			return null;

		});

	}

	private CellLabelProvider createAdditionalUpsidePNLDeltaLabelProvider() {
		return createLambdaLabelProvider(true, true, change -> {
			final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
			if (pnlContainer != null) {
				return -ScheduleModelKPIUtils.getAdditionalUpsideProfitAndLoss(pnlContainer);
			}
			return null;

		}, change -> {
			final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
			if (pnlContainer != null) {
				return -ScheduleModelKPIUtils.getAdditionalUpsideProfitAndLoss(pnlContainer);
			}
			return null;
		});
	}

	private CellLabelProvider createShippingDeltaLabelProvider() {

		return createLambdaLabelProvider(true, false, change -> {
			final EventGrouping eventGrouping = change.getOriginalEventGrouping();
			if (eventGrouping != null) {
				return ScheduleModelKPIUtils.getTotalShippingCost(eventGrouping);
			}
			return null;

		}, change -> {
			final EventGrouping eventGrouping = change.getNewEventGrouping();
			if (eventGrouping != null) {
				return ScheduleModelKPIUtils.getTotalShippingCost(eventGrouping);
			}
			return null;
		});
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

	protected void removeListener(@Nullable final ScenarioInstance scenarioInstance) {
		if (scenarioInstance != null) {
			final IScenarioServiceListener listener = listenerMap.get(scenarioInstance);
			final IScenarioService scenarioService = scenarioInstance.getScenarioService();
			if (scenarioService != null && listener != null) {
				scenarioService.removeScenarioServiceListener(listener);
			}
			listenerMap.remove(scenarioInstance);

		}
	}

	protected void createListener(@Nullable final ScenarioInstance scenarioInstance) {
		if (scenarioInstance != null) {
			final IScenarioService scenarioService = scenarioInstance.getScenarioService();
			if (scenarioService != null) {
				final ScenarioInstanceDeletedListener listener = new ScenarioInstanceDeletedListener(scenarioInstance, this);
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
					} else {
						if (isSet(changeSetRow.getNewVesselName())) {
							cell.setImage(imageClosedCircle);
							if (textualVesselMarkers) {
								cell.setText("●");
							}
						}
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
			for (final Map.Entry<ScenarioInstance, ScenarioInstanceDeletedListener> e : listenerMap.entrySet()) {
				final ScenarioInstance scenarioInstance = e.getKey();
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

	boolean isSet(@Nullable String str) {
		return str != null && !str.isEmpty();
	}
}
