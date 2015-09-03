/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.nebula.widgets.grid.internal.DefaultColumnHeaderRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.omg.CORBA.VM_ABSTRACT;

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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
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
		public void diffOptionChanged(EDiffOption d, Object oldValue, Object newValue) {
			// TODO Auto-generated method stub

		}
	};

	private final Map<ScenarioInstance, ScenarioInstanceDeletedListener> listenerMap = new HashMap<>();

	// TODO - Rename, remove italic, remove first option
	private Font italicFont;
	private Font italicBoldFont;

	private Image imageClosedCircle;

	private Image imageOpenCircle;

	private ViewMode viewMode = ViewMode.COMPARE;

	private MPart part;

	private final class ViewUpdateRunnable implements Runnable {
		private final ChangeSetRoot newRoot;

		private ViewUpdateRunnable(final ChangeSetRoot newRoot) {
			this.newRoot = newRoot;
		}

		@Override
		public void run() {
			// TODO: Extract vessel columns and generate.
			final Set<String> vesselnames = new LinkedHashSet<>();
			if (newRoot != null) {
				for (final ChangeSet cs : newRoot.getChangeSets()) {
					for (final ChangeSetRow csr : cs.getChangeSetRowsToPrevious()) {
						vesselnames.add(csr.getLhsVesselName());
						vesselnames.add(csr.getRhsVesselName());
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
				gvc.getColumn().setText(name);
				gvc.getColumn().setWidth(22);
				gvc.getColumn().setResizeable(false);
				gvc.setLabelProvider(createVesselLabelProvider(name));
				gvc.getColumn().setHeaderRenderer(new VesselNameColumnHeaderRenderer());
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
			cleanUp(ChangeSetView.this.root);
			ChangeSetView.this.root = newRoot;
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
		public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			if (scenarioInstance == this.scenarioInstance) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {

						view.handleAnalyseScenario(null);
					}
				});
			}
		}
	}

	@Inject
	public ChangeSetView() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(final Class adapter) {
		if (GridTreeViewer.class.isAssignableFrom(adapter)) {
			return viewer;
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			if (viewer != null) {
				return viewer.getGrid();
			}
		}

		return null;
	}

	@PostConstruct
	public void createPartControl(@Optional final MPart part, final Composite parent) {

		if (part != null) {
			for (String tag : part.getTags()) {
				if (tag.equals("action-set")) {
					viewMode = ViewMode.ACTION_SET;
				}
			}
		}

		final ImageDescriptor openCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/open-circle.png");
		final ImageDescriptor closedCircleDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.reports", "icons/closed-circle.png");

		imageOpenCircle = openCircleDescriptor.createImage();
		imageClosedCircle = closedCircleDescriptor.createImage();

		final Font systemFont = Display.getDefault().getSystemFont();
		final FontData fontData = systemFont.getFontData()[0];
		italicFont = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.NONE));
		italicBoldFont = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		// Create table
		viewer = new GridTreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL);
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
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setMoveable(false);
			gvc.setLabelProvider(createCSLabelProvider());
		}

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setText("P&L (m)");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createPNLDeltaLabelProvider());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setText("Late");
			gvc.getColumn().setHeaderTooltip("Lateness");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createLatenessDeltaLabelProvider());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setText("Violations");
			gvc.getColumn().setHeaderTooltip("Capacity Violations");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createViolationsDeltaLabelProvider());
			DefaultColumnHeaderRenderer renderer = new DefaultColumnHeaderRenderer();
			renderer.setWordWrap(true);
			gvc.getColumn().setHeaderRenderer(renderer);

		}
		vesselColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		vesselColumnGroup.setText("Vessels");
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
		}
		final GridColumnGroup loadGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		loadGroup.setText("Purchase");
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__LHS_NAME));
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setText("Wiring");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createStubLabelProvider());
			this.diagram = createWiringDiagram(gvc);
		}
		final GridColumnGroup dischargeGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		dischargeGroup.setText("Sale");
		{
			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__RHS_NAME));
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(55);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
		}

		final GridColumnGroup pnlComponentGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		pnlComponentGroup.setText("Components");

		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Sales Revenue");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			DefaultColumnHeaderRenderer renderer = new DefaultColumnHeaderRenderer();
			renderer.setWordWrap(true);
			gvc.getColumn().setHeaderRenderer(renderer);

		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Purchase Cost");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
			DefaultColumnHeaderRenderer renderer = new DefaultColumnHeaderRenderer();
			renderer.setWordWrap(true);
			gvc.getColumn().setHeaderRenderer(renderer);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Shipping Cost");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createShippingDeltaLabelProvider());
			DefaultColumnHeaderRenderer renderer = new DefaultColumnHeaderRenderer();
			renderer.setWordWrap(true);
			gvc.getColumn().setHeaderRenderer(renderer);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Other Cargo P&L");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createAdditionalPNLDeltaLabelProvider());
			DefaultColumnHeaderRenderer renderer = new DefaultColumnHeaderRenderer();
			renderer.setWordWrap(true);
			gvc.getColumn().setHeaderRenderer(renderer);
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Other Entity P&L");
			gvc.getColumn().setWidth(70);
			gvc.setLabelProvider(createTaxDeltaLabelProvider());
			DefaultColumnHeaderRenderer renderer = new DefaultColumnHeaderRenderer();
			renderer.setWordWrap(true);
			gvc.getColumn().setHeaderRenderer(renderer);
		}

		// Create sorter
		// TODO: ?

		// Selection listener for pin/diff driver.
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					final Iterator<?> itr = iStructuredSelection.iterator();
					while (itr.hasNext()) {
						Object o = itr.next();
						if (o instanceof ChangeSetRow) {
							o = ((ChangeSetRow) o).eContainer();
						}
						if (o instanceof ChangeSet) {
							final ChangeSet changeSet = (ChangeSet) o;
							if (diffToBase) {
								scenarioSelectionProvider.setPinnedInstance(changeSet.getBaseScenario());
							} else {
								scenarioSelectionProvider.setPinnedInstance(changeSet.getPrevScenario());
							}
							scenarioSelectionProvider.select(changeSet.getCurrentScenario());
						}
						return;
					}
				}

			}
		});

		// Selection listener for current selection.
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final IStructuredSelection selection = (IStructuredSelection) event.getSelection();

				final Set<Object> selectedElements = new LinkedHashSet<>();
				final Iterator<?> itr = selection.iterator();
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

			private void selectRow(final Set<Object> selectedElements, final ChangeSetRow changeSetRow) {
				selectedElements.add(changeSetRow.getLoadSlot());
				selectedElements.add(changeSetRow.getNewLoadAllocation());
				selectedElements.add(changeSetRow.getOriginalLoadAllocation());
				if (changeSetRow.getNewLoadAllocation() != null) {
					selectedElements.add(changeSetRow.getNewLoadAllocation().getSlotVisit());
				}
				if (changeSetRow.getOriginalLoadAllocation() != null) {
					selectedElements.add(changeSetRow.getOriginalLoadAllocation().getSlotVisit());
				}
				selectedElements.add(changeSetRow.getNewDischargeAllocation());
				selectedElements.add(changeSetRow.getOriginalDischargeAllocation());
				if (changeSetRow.getNewDischargeAllocation() != null) {
					selectedElements.add(changeSetRow.getNewDischargeAllocation().getSlotVisit());
				}
				if (changeSetRow.getOriginalDischargeAllocation() != null) {
					selectedElements.add(changeSetRow.getOriginalDischargeAllocation().getSlotVisit());
				}
				selectedElements.add(changeSetRow.getNewGroupProfitAndLoss());
				selectedElements.add(changeSetRow.getOriginalGroupProfitAndLoss());
				selectedElements.add(changeSetRow.getNewEventGrouping());
				selectedElements.add(changeSetRow.getOriginalEventGrouping());
			}
		});

		final ViewerFilter[] filters = new ViewerFilter[1];
		filters[0] = new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (!showNonStructuralChanges) {
					if (element instanceof ChangeSetRow) {
						final ChangeSetRow row = (ChangeSetRow) element;
						if (!row.isWiringChange() && !row.isVesselChange()) {
							return false;
						}
					}
				}
				return true;
			}
		};
		viewer.setFilters(filters);

		scenarioComparisonService.addListener(listener);
		scenarioComparisonService.triggerListener(listener);
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

					setFont(cell, change);

				}
			}

		};
	}

	protected void setFont(final ViewerCell cell, final ChangeSetRow change) {
		if (!change.isVesselChange() && !change.isWiringChange()) {
			cell.setFont(italicFont);
		} else {
			cell.setFont(null);
		}
	}

	private CellLabelProvider createDeltaLabelProvider(final boolean asInt, final EStructuralFeature from, final EStructuralFeature to, final EStructuralFeature attrib) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					setFont(cell, change);

					Number f = null;
					try {
						f = getNumber(from, attrib, change);
					} catch (final Exception e) {
					}

					Number t = null;
					try {
						t = getNumber(to, attrib, change);
					} catch (final Exception e) {
					}

					if (asInt) {
						double delta = 0;
						if (f != null) {
							delta -= f.intValue();
						}
						if (t != null) {
							delta += t.intValue();
						}
						delta = delta / 1000000.0;
						if (delta != 0) {
							cell.setText(String.format("%s %,.3f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					} else {
						double delta = 0;
						if (f != null) {
							delta -= f.doubleValue();
						}
						if (t != null) {
							delta += t.doubleValue();
						}
						if (delta != 0) {
							cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
						}
					}
				}
			}

			@Nullable
			private Number getNumber(final EStructuralFeature from, final EStructuralFeature attrib, final ChangeSetRow change) {
				if (change != null && change.eClass().getEAllStructuralFeatures().contains(from)) {
					final EObject eObject = (EObject) change.eGet(from);
					if (eObject != null) {
						return (Number) eObject.eGet(attrib);
					}
				}
				return null;

			}
		};
	}

	private CellLabelProvider createPNLDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				if (element instanceof ChangeSet) {

					cell.setFont(italicBoldFont);

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
					setFont(cell, change);

					Number f = null;
					{
						final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
						if (pnlContainer != null) {
							f = ChangeSetUtils.getGroupProfitAndLoss(pnlContainer);
						}
					}
					Number t = null;
					{
						final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
						if (pnlContainer != null) {
							t = ChangeSetUtils.getGroupProfitAndLoss(pnlContainer);
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

	private CellLabelProvider createTaxDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				// if (element instanceof ChangeSet) {
				//
				// cell.setFont(italicBoldFont);
				//
				// final ChangeSet changeSet = (ChangeSet) element;
				// final DeltaMetrics metrics;
				// if (diffToBase) {
				// metrics = changeSet.getMetricsToBase();
				// } else {
				// metrics = changeSet.getMetricsToPrevious();
				// }
				// if (metrics != null) {
				// double delta = metrics.getPnlDelta();
				//
				// delta = delta / 1000000.0;
				//
				// cell.setText(String.format("%s%,.3G", metrics.getPnlDelta() < 0 ? "↓" : "↑", Math.abs(delta)));
				// }
				// }
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;
					setFont(cell, change);

					Number f = null;
					{
						final ProfitAndLossContainer pnlContainer = change.getOriginalGroupProfitAndLoss();
						if (pnlContainer != null) {
							f = ChangeSetUtils.getGroupProfitAndLoss(pnlContainer) - ChangeSetUtils.getGroupPreTaxProfitAndLoss(pnlContainer);
						}
					}
					Number t = null;
					{
						final ProfitAndLossContainer pnlContainer = change.getNewGroupProfitAndLoss();
						if (pnlContainer != null) {
							t = ChangeSetUtils.getGroupProfitAndLoss(pnlContainer) - ChangeSetUtils.getGroupPreTaxProfitAndLoss(pnlContainer);
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

	private CellLabelProvider createLatenessDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");
				cell.setForeground(null);
				if (element instanceof ChangeSet) {

					cell.setFont(italicBoldFont);

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
					setFont(cell, change);

					Number f = null;
					{
						final EventGrouping eventGrouping = change.getOriginalEventGrouping();
						if (eventGrouping != null) {
							f = ChangeSetUtils.getLateness(eventGrouping);
						}
					}
					Number t = null;
					{
						final EventGrouping eventGrouping = change.getNewEventGrouping();
						if (eventGrouping != null) {
							t = ChangeSetUtils.getLateness(eventGrouping);
						}
					}
					int delta = 0;
					if (f != null) {
						delta -= f.intValue();
					}
					if (t != null) {
						delta += t.intValue();
					}
					delta = (int) Math.round((double) delta / 24.0);
					if (delta != 0) {
						cell.setText(String.format("%s %d", delta < 0 ? "↓" : "↑", Math.abs(delta)));
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

					cell.setFont(italicBoldFont);

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
					setFont(cell, change);

					Number f = null;
					{
						final EventGrouping eventGrouping = change.getOriginalEventGrouping();
						if (eventGrouping != null) {
							f = ChangeSetUtils.getCapacityViolationCount(eventGrouping);
						}
					}
					Number t = null;
					{
						final EventGrouping eventGrouping = change.getNewEventGrouping();
						if (eventGrouping != null) {
							t = ChangeSetUtils.getCapacityViolationCount(eventGrouping);
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
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;
					setFont(cell, change);

					Number f = null;
					{
						final SlotAllocation originalLoadAllocation = change.getOriginalLoadAllocation();
						if (originalLoadAllocation != null) {
							final CargoAllocation cargoAllocation = originalLoadAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								final long addnPNL = ChangeSetUtils.getAdditionalProfitAndLoss(cargoAllocation);
								f = addnPNL;
							}
						}
					}
					Number t = null;
					{
						final SlotAllocation newLoadAllocation = change.getNewLoadAllocation();
						if (newLoadAllocation != null) {
							final CargoAllocation cargoAllocation = newLoadAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								final long addnPNL = ChangeSetUtils.getAdditionalProfitAndLoss(cargoAllocation);
								t = addnPNL;
							}
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
						cell.setText(String.format("%s %,.3f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};

	}

	private CellLabelProvider createShippingDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;
					setFont(cell, change);

					Number f = null;
					{
						final EventGrouping eventGrouping = change.getOriginalEventGrouping();
						if (eventGrouping != null) {
							f = ChangeSetUtils.getTotalShippingCost(eventGrouping);
						}
					}
					Number t = null;
					{
						final EventGrouping eventGrouping = change.getNewEventGrouping();
						if (eventGrouping != null) {
							t = ChangeSetUtils.getTotalShippingCost(eventGrouping);
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
						cell.setText(String.format("%s %,.3f", delta < 0 ? "↓" : "↑", Math.abs(delta)));
					}

				}
			}
		};
	}

	private CellLabelProvider createCSLabelProvider() {

		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();
				cell.setText("");

				if (element instanceof ChangeSet) {

					cell.setFont(italicBoldFont);

					final ChangeSet changeSet = (ChangeSet) element;
					final ChangeSetRoot root = (ChangeSetRoot) changeSet.eContainer();
					int idx = 0;
					if (root != null) {
						idx = root.getChangeSets().indexOf(changeSet);
					}
//					int changeCount = 0;
//					final List<ChangeSetRow> rows;
//					if (diffToBase) {
//						rows = changeSet.getChangeSetRowsToBase();
//					} else {
//						rows = changeSet.getChangeSetRowsToPrevious();
//					}
//					for (ChangeSetRow r : rows) {
//						if (r.isVesselChange()) {
////							changeCount++;
//						}
//						if (r.isWiringChange() && r.getNewLoadAllocation() != null) {
////							changeCount++;
//						}
//						if (r.getRhsWiringLink() != null ) {
//							changeCount++;
//						}
//					}

//					cell.setText(String.format("Set %d (%d)", idx + 1, changeCount));
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
		viewer.setInput(newRoot);
		cleanUp(ChangeSetView.this.root);
		ChangeSetView.this.root = newRoot;
	}

	// public void setChangeSetData(final ScenarioInstance pin, final ScenarioInstance diff) {
	//
	// cleanUpVesselColumns();
	//
	// if (pin == null || diff == null) {
	// setEmptyData();
	//
	// } else {
	// final Display display = PlatformUI.getWorkbench().getDisplay();
	// final ProgressMonitorDialog d = new ProgressMonitorDialog(display.getActiveShell());
	// try {
	// d.run(true, false, new IRunnableWithProgress() {
	//
	// @Override
	// public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
	//
	// final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
	// scheduleDiffUtils.setCheckAssignmentDifferences(true);
	// scheduleDiffUtils.setCheckSpotMarketDifferences(true);
	// scheduleDiffUtils.setCheckNextPortDifferences(true);
	// final ChangeSetTransformer transformer = new ChangeSetTransformer(scheduleDiffUtils, Collections.<ICustomRelatedSlotHandler> emptyList());
	// final ChangeSetRoot newRoot = transformer.createDataModel(pin, diff, monitor);
	// display.asyncExec(new ViewUpdateRunnable(newRoot));
	// }
	// });
	// } catch (InvocationTargetException | InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// }

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

					if (name.equals(changeSetRow.getLhsVesselName())) {
						cell.setImage(imageOpenCircle);
						// cell.setText("○");
					}
					if (name.equals(changeSetRow.getRhsVesselName())) {
						cell.setImage(imageClosedCircle);
						// cell.setText("●");
					}
				}
			}

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;

					if (name.equals(changeSetRow.getLhsVesselName())) {
						return name;
					}
					if (name.equals(changeSetRow.getRhsVesselName())) {
						return name;
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
		this.root = null;
		if (italicFont != null) {
			italicFont.dispose();
			italicFont = null;
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
		viewer.getControl().setFocus();
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
		viewer.refresh();
	}

	@Inject
	@Optional
	private void handleShowStructuralChangesToggle(@UIEventTopic(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_NON_STRUCTURAL_CHANGES) final Object o) {
		showNonStructuralChanges = !showNonStructuralChanges;
		viewer.refresh();
	}

	@Inject
	@Optional
	private void handleAnalyseScenario(@UIEventTopic(ChangeSetViewEventConstants.EVENT_ANALYSE_ACTION_SETS) final ScenarioInstance target) {
		if (viewMode != ViewMode.ACTION_SET) {
			return;
		}
		this.viewMode = ViewMode.ACTION_SET;
		// updateToolItem(part, viewMode);
		setActionSetData(target);
	}

	// @Inject
	// @Optional
	// private void handleChangeSetsScenario(@UIEventTopic(ChangeSetViewEventConstants.EVENT_ANALYSE_CHANGE_SETS) final Object _unused_) {
	//
	// final ScenarioInstance pin = scenarioSelectionProvider.getPinnedInstance();
	// ScenarioInstance diff = null;
	//
	// for (final ScenarioInstance s : scenarioSelectionProvider.getSelection()) {
	// if (s != pin) {
	// diff = s;
	// break;
	// }
	// }
	// setChangeSetData(pin, diff);
	// }

	// @Inject
	// @Optional
	// public void handleViewMode(@UIEventTopic(ChangeSetViewEventConstants.EVENT_SET_VIEW_MODE) final Object _unused_) {
	//
	// final ViewMode newMode = ViewMode.COMPARE;
	// if (this.viewMode != newMode) {
	// this.viewMode = newMode;
	//
	//// updateToolItem(part, viewMode);
	//
	// if (viewMode == ViewMode.COMPARE) {
	// // Reload current compare state
	// scenarioComparisonService.triggerListener(listener);
	// } else {
	// // Wait for action sets to come through
	// handleAnalyseScenario(null);
	// }
	// }
	// }

	// private void updateToolItem(final MPart part, final ViewMode viewMode) {
	// if (part == null || part.getToolbar() == null) {
	// return;
	// }
	// final List<MToolBarElement> children = part.getToolbar().getChildren();
	// for (final MToolBarElement e : children) {
	// if ("com.mmxlabs.lingo.reports.directtoolitem.viewmode".equals(e.getElementId())) {
	// if (e instanceof MUILabel) {
	// final MUILabel mLabel = (MUILabel) e;
	// mLabel.setLabel("Mode: " + getModeName(viewMode));
	//
	// }
	// }
	// }
	// }

	// private String getModeName(final ViewMode mode) {
	// switch (mode) {
	// case ACTION_SET:
	// return "Action Set";
	// case COMPARE:
	// return "Compare";
	// }
	// return mode.name();
	// }
}
