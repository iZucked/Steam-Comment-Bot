package com.mmxlabs.lingo.reports.views.changeset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;

import com.mmxlabs.lingo.reports.diff.utils.ScheduleCostUtils;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

public class ChangeSetView implements IAdaptable {

	private GridTreeViewer viewer;

	// TODO: Needs some shared state with the action handler.
	private final boolean diffToBase = false;

	private GridColumnGroup vesselColumnGroup;
	private final List<GridViewerColumn> vesselColumns = new LinkedList<>();

	private ChangeSetWiringDiagram diagram;

	@Inject
	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	@Inject
	private ISelectionService iSelectionSerice;

	private final IScenarioServiceSelectionChangedListener listener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> selected, final boolean block) {
			// TODO Auto-generated method stub

		}

		@Override
		public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioInstance oldPin, final ScenarioInstance newPin, final boolean block) {
			// TODO Auto-generated method stub
			// setData(newPin);
		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected, final boolean block) {
			// TODO Auto-generated method stub

		}
	};

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
	public void postConstruct(final Composite parent) {
		// Create table
		viewer = new GridTreeViewer(parent, SWT.V_SCROLL);

		viewer.getGrid().setRowHeaderVisible(true);
		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLinesVisible(true);

		// Create content provider
		viewer.setContentProvider(createContentProvider());
		// Create label provider

		// Create columns
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("CS");
			gvc.getColumn().setTree(true);
			gvc.getColumn().setWidth(20);
			gvc.setLabelProvider(createCSLabelProvider());
		}

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("L-ID");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__LHS_NAME));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("L-Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("L-mmBtu");
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
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("D-ID");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createStandardLabelProvider(ChangesetPackage.Literals.CHANGE_SET_ROW__RHS_NAME));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("D-Price");
			gvc.getColumn().setWidth(50);
			gvc.setLabelProvider(createDeltaLabelProvider(false, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("D-mmBtu");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__ENERGY_TRANSFERRED));
		}
		vesselColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		vesselColumnGroup.setText("Vessels");
		// Vessel columns are dynamically created
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("PNL");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createPNLDeltaLabelProvider());
		}

		final GridColumnGroup pnlComponentGroup = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		pnlComponentGroup.setText("Components");

		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Revenue");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION,
					ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Cost");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION,
					SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_VALUE));
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Shipping");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createShippingDeltaLabelProvider());
		}
		{
			final GridColumn gc = new GridColumn(pnlComponentGroup, SWT.NONE);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setText("Addn. P&L");
			gvc.getColumn().setWidth(75);
			gvc.setLabelProvider(createAdditionalPNLDeltaLabelProvider());// (true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION,
			// gvc.setLabelProvider(createDeltaLabelProvider(true, ChangesetPackage.Literals.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION,
			// ChangesetPackage.Literals.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION, SchedulePackage.Literals.SLOT_ALLOCATION__CARGO_ALLOCATIONVOLUME_VALUE));
		}

		// Create sorter
		// TODO: ?

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

		// Listen to navigator selection changes
		scenarioSelectionProvider.addSelectionChangedListener(listener);
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
				}
			}
		};
	}

	private CellLabelProvider createDeltaLabelProvider(final boolean asInt, final EStructuralFeature from, final EStructuralFeature to, final EStructuralFeature attrib) {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();

				if (element instanceof ChangeSetRow) {
					final ChangeSetRow change = (ChangeSetRow) element;
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
						int delta = 0;
						if (f != null) {
							delta -= f.intValue();
						}
						if (t != null) {
							delta += t.intValue();
						}
						delta /= 1000;
						if (delta != 0) {
							cell.setText(String.format("%s %,dK", delta < 0 ? "↓" : "↑", delta));
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
							cell.setText(String.format("%s %,.2f", delta < 0 ? "↓" : "↑", delta));
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

					Number f = null;
					{
						final SlotAllocation originalLoadAllocation = change.getOriginalLoadAllocation();
						if (originalLoadAllocation != null) {
							final CargoAllocation cargoAllocation = originalLoadAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								f = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
							}
						}
					}
					Number t = null;
					{
						final SlotAllocation newLoadAllocation = change.getNewLoadAllocation();
						if (newLoadAllocation != null) {
							final CargoAllocation cargoAllocation = newLoadAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								t = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
							}
						}
					}
					int delta = 0;
					if (f != null) {
						delta -= f.intValue();
					}
					if (t != null) {
						delta += t.intValue();
					}
					delta /= 1000;
					if (delta != 0) {
						cell.setText(String.format("%s %,dK", delta < 0 ? "↓" : "↑", delta));
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

					Number f = null;
					{
						final SlotAllocation originalLoadAllocation = change.getOriginalLoadAllocation();
						if (originalLoadAllocation != null) {
							final CargoAllocation cargoAllocation = originalLoadAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								// f = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								long addnPNL = 0;
								final GroupProfitAndLoss dataWithKey = cargoAllocation.getGroupProfitAndLoss();
								if (dataWithKey != null) {
									for (final GeneralPNLDetails generalPNLDetails : cargoAllocation.getGeneralPNLDetails()) {
										if (generalPNLDetails instanceof SlotPNLDetails) {
											final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
											for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
												if (details instanceof BasicSlotPNLDetails) {
													addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
												}
											}
										}
									}
								}
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
								// t = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								long addnPNL = 0;
								final GroupProfitAndLoss dataWithKey = cargoAllocation.getGroupProfitAndLoss();
								if (dataWithKey != null) {
									for (final GeneralPNLDetails generalPNLDetails : cargoAllocation.getGeneralPNLDetails()) {
										if (generalPNLDetails instanceof SlotPNLDetails) {
											final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
											for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
												if (details instanceof BasicSlotPNLDetails) {
													addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
												}
											}
										}
									}
								}
								t = addnPNL;

							}
						}
					}
					int delta = 0;
					if (f != null) {
						delta -= f.intValue();
					}
					if (t != null) {
						delta += t.intValue();
					}
					delta /= 1000;
					if (delta != 0) {
						cell.setText(String.format("%s %,dK", delta < 0 ? "↓" : "↑", delta));
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

					Number f = null;
					{

						final SlotAllocation originalLoadAllocation = change.getOriginalLoadAllocation();
						if (originalLoadAllocation != null) {
							final CargoAllocation cargoAllocation = originalLoadAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								f = ScheduleCostUtils.calculateLegCost(cargoAllocation, originalLoadAllocation)
										+ ScheduleCostUtils.calculateLegCost(cargoAllocation, change.getOriginalDischargeAllocation());
							}
						}
					}
					Number t = null;
					{
						final SlotAllocation newLoadAllocation = change.getNewLoadAllocation();
						if (newLoadAllocation != null) {
							final CargoAllocation cargoAllocation = newLoadAllocation.getCargoAllocation();

							if (cargoAllocation != null) {

								t = ScheduleCostUtils.calculateLegCost(cargoAllocation, newLoadAllocation) + ScheduleCostUtils.calculateLegCost(cargoAllocation, change.getNewDischargeAllocation());
							}
						}
					}
					int delta = 0;
					if (f != null) {
						delta -= f.intValue();
					}
					if (t != null) {
						delta += t.intValue();
					}
					delta /= 1000;
					if (delta != 0) {
						cell.setText(String.format("%s %,dK", delta < 0 ? "↓" : "↑", delta));
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
					final ChangeSet changeSet = (ChangeSet) element;
					if (diffToBase) {
						cell.setText(String.format("P&L: %,d Lateness: %d Capacity: %d", changeSet.getMetricsToBase().getPnlDelta(), 0, 0));
					} else {
						cell.setText(String.format("P&L: %,d Lateness: %d Capacity: %d", changeSet.getMetricsToPrevious().getPnlDelta(), 0, 0));
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

	@PostConstruct
	public void setInitialData() {

		// Set initial data
	}

	public void setData(final ScenarioInstance target) {
		// // Clean up existing cols
		//
		// for (GridViewerColumn c : vesselColumns ) {
		// if (!c.getColumn().isDisposed()) {
		// c.getColumn().dispose();
		// }
		// }
		// vesselColumns.clear();
		//
		if (vesselColumnGroup != null && !vesselColumnGroup.isDisposed()) {
			final GridColumn[] columns = vesselColumnGroup.getColumns();
			for (final GridColumn c : columns) {
				if (!c.isDisposed()) {
					c.dispose();
				}
			}
		}

		// if (existing != null) {
		// // Clean up model references
		// }

		if (target == null) {

		} else {

			final ChangeSetViewTransformer transformer = new ChangeSetViewTransformer();
			final ChangeSetRoot root = transformer.createDataModel(target, new NullProgressMonitor());

			// TODO: Extract vessel columns and generate.
			final Set<String> vesselnames = new LinkedHashSet<>();
			for (final ChangeSet cs : root.getChangeSets()) {
				for (final ChangeSetRow csr : cs.getChangeSetRowsToPrevious()) {
					vesselnames.add(csr.getLhsVesselName());
					vesselnames.add(csr.getRhsVesselName());
				}
			}
			vesselnames.remove(null);
			for (final String name : vesselnames) {
				final GridColumn gc = new GridColumn(vesselColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				gvc.getColumn().setText(name);
				gvc.getColumn().setWidth(20);
				gvc.setLabelProvider(createVesselLabelProvider(name, new ArrayList<>(vesselnames)));
			}

			// Sort similar to vert report
			diagram.setTable(root);
			viewer.setInput(root);
			viewer.refresh();
		}

	}

	private CellLabelProvider createVesselLabelProvider(final String name, final List<String> vesselOrder) {
		final int thisIdx = vesselOrder.indexOf(name);
		// TODO Auto-generated method stub
		return new CellLabelProvider() {
			// final Color red = new Color(Display.getCurrent(), new RGB(255, 0, 0));
			// final Color green = new Color(Display.getCurrent(), new RGB(0, 255, 0));

			@Override
			public void update(final ViewerCell cell) {

				// TODO Auto-generated method stub
				final Object element = cell.getElement();
				if (element instanceof ChangeSetRow) {
					final ChangeSetRow changeSetRow = (ChangeSetRow) element;

					final int fromIdx = vesselOrder.indexOf(changeSetRow.getLhsVesselName());
					final int toIdx = vesselOrder.indexOf(changeSetRow.getRhsVesselName());
					if (fromIdx >= 0 && toIdx >= 0) {
						if (fromIdx > toIdx) {
							if (thisIdx >= toIdx && thisIdx <= fromIdx) {
								cell.setText("<");
							}
						} else if (toIdx > fromIdx) {
							if (thisIdx >= fromIdx && thisIdx <= toIdx) {
								cell.setText(">");
							}
						}
					}
					//
					if (name.equals(changeSetRow.getLhsVesselName())) {
						// // cell.setBackground(red);
						cell.setText(".");
					}
					if (name.equals(changeSetRow.getRhsVesselName())) {
						// // cell.setBackground(green);
						cell.setText("O");
					}
				}
			}
		};
	}

	@PreDestroy
	public void dispose() {
		scenarioSelectionProvider.removeSelectionChangedListener(listener);
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	void linkToScenarioNavigator() {
		// Find selected scenario.

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
	private void handleAnalyseScenario(@UIEventTopic("analyse-scenario") final ScenarioInstance target) {
		setData(target);
	}
}
