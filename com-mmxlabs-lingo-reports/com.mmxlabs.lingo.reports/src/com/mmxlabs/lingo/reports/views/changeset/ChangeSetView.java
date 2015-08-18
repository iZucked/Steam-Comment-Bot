package com.mmxlabs.lingo.reports.views.changeset;

import java.lang.reflect.InvocationTargetException;
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
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
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

	private GridTreeViewer viewer;

	private boolean diffToBase = false;

	private GridColumnGroup vesselColumnGroup;

	private ChangeSetWiringDiagram diagram;

	@Inject
	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	private ChangeSetRoot root;

	@Inject
	private ESelectionService eSelectionService;

	private final Map<ScenarioInstance, ScenarioInstanceDeletedListener> listenerMap = new HashMap<>();

	private Font italicFont;
	private Font italicBoldFont;

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
	public <T> T getAdapter(final Class<T> adapter) {
		if (GridTreeViewer.class.isAssignableFrom(adapter)) {
			return (T) viewer;
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			if (viewer != null) {
				return (T) viewer.getGrid();
			}
		}

		return null;
	}

	@PostConstruct
	public void createPartControl(@Optional final IWorkbenchPart legacyPart, final Composite parent) {
		final Font systemFont = Display.getDefault().getSystemFont();
		final FontData fontData = systemFont.getFontData()[0];
		italicFont = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.ITALIC));
		italicBoldFont = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.ITALIC | SWT.BOLD));
		// Create table
		viewer = new GridTreeViewer(parent, SWT.V_SCROLL);

		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);

		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		// Create content provider
		viewer.setContentProvider(createContentProvider());

		// Create columns
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("");
			gvc.getColumn().setTree(true);
			gvc.getColumn().setWidth(20);
			gvc.setLabelProvider(createCSLabelProvider());
		}

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("P&L (m)");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createPNLDeltaLabelProvider());
		}

		final GridColumnGroup loadGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		loadGroup.setText("Purchase");
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__LHS_NAME));
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("$");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
		}
		{
			final GridColumn gc = new GridColumn(loadGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Wiring");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createStubLabelProvider());
			this.diagram = createWiringDiagram(gvc);
		}
		final GridColumnGroup dischargeGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		dischargeGroup.setText("Sale");
		{
			final GridColumn gc = new GridColumn(dischargeGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("ID");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__RHS_NAME));
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("$");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
		}
		{

			final GridColumn gc = new GridColumn(dischargeGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("tBtu");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
		}
		vesselColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		vesselColumnGroup.setText("Vessels");
		// Vessel columns are dynamically created

		final GridColumnGroup pnlComponentGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		pnlComponentGroup.setText("Components");

		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Revenue (m)");
			gvc.getColumn().setWidth(85);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Cost (m)");
			gvc.getColumn().setWidth(85);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Shipping (m)");
			gvc.getColumn().setWidth(85);
			gvc.setLabelProvider(createShippingDeltaLabelProvider());
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Addn. P&L (m)");
			gvc.getColumn().setWidth(95);
			gvc.setLabelProvider(createAdditionalPNLDeltaLabelProvider());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Lateness");
			gvc.getColumn().setWidth(20);
			gvc.setLabelProvider(createStubLabelProvider());
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Capacity");
			gvc.getColumn().setWidth(20);
			gvc.setLabelProvider(createStubLabelProvider());
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
						selectedElements.add(changeSetRow.getLoadSlot());
						selectedElements.add(changeSetRow.getNewLoadAllocation());
						selectedElements.add(changeSetRow.getOriginalLoadAllocation());
						if (changeSetRow.getNewLoadAllocation() != null) {
							selectedElements.add(changeSetRow.getNewLoadAllocation().getSlotVisit());
						}
						if (changeSetRow.getOriginalLoadAllocation() != null) {
							selectedElements.add(changeSetRow.getOriginalLoadAllocation().getSlotVisit());
						}
					} else if (o instanceof ChangeSet) {
						final ChangeSet changeSet = (ChangeSet) o;
						List<ChangeSetRow> rows;
						if (diffToBase) {
							rows = changeSet.getChangeSetRowsToBase();
						} else {
							rows = changeSet.getChangeSetRowsToPrevious();
						}
						for (final ChangeSetRow changeSetRow : rows) {
							selectedElements.add(changeSetRow.getLoadSlot());
							selectedElements.add(changeSetRow.getNewLoadAllocation());
							selectedElements.add(changeSetRow.getOriginalLoadAllocation());
							if (changeSetRow.getNewLoadAllocation() != null) {
								selectedElements.add(changeSetRow.getNewLoadAllocation().getSlotVisit());
							}
							if (changeSetRow.getOriginalLoadAllocation() != null) {
								selectedElements.add(changeSetRow.getOriginalLoadAllocation().getSlotVisit());
							}
						}
					}
				}
				while (selectedElements.remove(null))
					;
				// set the selection to the service
				eSelectionService.setPostSelection(new StructuredSelection(selectedElements.toArray()));
			}
		});

		// TODO: Add scenario service listener for removed scenarios
	}

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

				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;

					setFont(cell, change);

					Number f = null;
					try {
						f = (Number) ((EObject) change.eGet(from)).eGet(attrib);
					} catch (final Exception e) {
					}

					Number t = null;
					try {
						t = (Number) ((EObject) change.eGet(to)).eGet(attrib);
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
		};
	}

	private CellLabelProvider createPNLDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();

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

	private CellLabelProvider createAdditionalPNLDeltaLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();

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
				if (element instanceof ChangeSet) {

					cell.setFont(italicBoldFont);

					final ChangeSet changeSet = (ChangeSet) element;
					final ChangeSetRoot root = (ChangeSetRoot) changeSet.eContainer();
					int idx = 0;
					if (root != null) {
						idx = root.getChangeSets().indexOf(changeSet);
					}
					if (diffToBase) {
						cell.setText(String.format("Action Set %d. P&L: %,d  Lateness: %d  Capacity: %d", idx + 1, changeSet.getMetricsToBase().getPnlDelta(), 0, 0));
					} else {
						cell.setText(String.format("Action Set %d. P&L: %,d  Lateness: %d  Capacity: %d", idx + 1, changeSet.getMetricsToPrevious().getPnlDelta(), 0, 0));
					}
					final DataVisualizer dv = viewer.getGrid().getDataVisualizer();
					dv.setColumnSpan((GridItem) cell.getItem(), cell.getColumnIndex(), viewer.getGrid().getColumnCount());
				}
			}
		};

	}

	@PostConstruct
	public void makeActions() {

	}

	public void setData(final ScenarioInstance target) {

		if (vesselColumnGroup != null && !vesselColumnGroup.isDisposed()) {
			final GridColumn[] columns = vesselColumnGroup.getColumns();
			for (final GridColumn c : columns) {
				if (!c.isDisposed()) {
					c.dispose();
				}
			}
		}

		if (target == null) {
			final ChangeSetRoot newRoot = ChangesetFactory.eINSTANCE.createChangeSetRoot();
			diagram.setChangeSetRoot(newRoot);
			viewer.setInput(newRoot);
			cleanUp(ChangeSetView.this.root);
			ChangeSetView.this.root = newRoot;
		} else {
			final Display display = PlatformUI.getWorkbench().getDisplay();
			final ProgressMonitorDialog d = new ProgressMonitorDialog(display.getActiveShell());
			try {
				d.run(true, false, new IRunnableWithProgress() {

					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						final ChangeSetViewTransformer transformer = new ChangeSetViewTransformer();
						final ChangeSetRoot newRoot = transformer.createDataModel(target, monitor);
						display.asyncExec(new Runnable() {

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
								for (final String name : vesselnames) {
									assert name != null;
									final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
									final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
									gvc.getColumn().setText(name);
									gvc.getColumn().setWidth(20);
									gvc.setLabelProvider(createVesselLabelProvider(name));
								}

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
						});
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void cleanUp(final ChangeSetRoot root) {
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

	protected void removeListener(final ScenarioInstance scenarioInstance) {
		if (scenarioInstance != null) {
			final IScenarioServiceListener listener = listenerMap.get(scenarioInstance);
			final IScenarioService scenarioService = scenarioInstance.getScenarioService();
			if (scenarioService != null && listener != null) {
				scenarioService.removeScenarioServiceListener(listener);
			}
			listenerMap.remove(scenarioInstance);

		}
	}

	protected void createListener(final ScenarioInstance scenarioInstance) {
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

				// TODO Auto-generated method stub
				final Object element = cell.getElement();
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;
					cell.setText("");

					if (name.equals(changeSetRow.getLhsVesselName())) {
						cell.setText(".");
					}
					if (name.equals(changeSetRow.getRhsVesselName())) {
						cell.setText("O");
					}
				}
			}
		};
	}

	@PreDestroy
	public void dispose() {
		cleanUp(this.root);
		this.root = null;
		if (italicFont != null) {
			italicFont.dispose();
			italicFont = null;
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
	private void handleDiffToBaseToggle(@UIEventTopic("toggle-diff-to-base") final Object o) {
		diffToBase = !diffToBase;
		diagram.setDiffToBase(diffToBase);
		viewer.refresh();
	}

	@Inject
	@Optional
	private void handleAnalyseScenario(@UIEventTopic("analyse-scenario") final ScenarioInstance target) {
		setData(target);
	}
}
