/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 */
public class SchedulePnLReport extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";
	final List<String> entityColumnNames = new ArrayList<String>();

	private final EPackage tableDataModel;
	private final EStructuralFeature nameObjectRef;
	private final EStructuralFeature targetObjectRef;
	private final EStructuralFeature cargoAllocationRef;
	private final EStructuralFeature loadAllocationRef;
	private final EStructuralFeature dischargeAllocationRef;
	private final EStructuralFeature openSlotAllocationRef;

	public SchedulePnLReport() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoPnLReportView");

		tableDataModel = GenericEMFTableDataModel.createEPackage("target", "cargo", "load", "discharge", "openslot");
		final EClass rowClass = GenericEMFTableDataModel.getRowClass(tableDataModel);
		nameObjectRef = 		GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.Literals.ESTRING, "name");
//		GenericEMFTableDataModel.getRowFeature(tableDataModel, "name");
		targetObjectRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "target");
		cargoAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "cargo");
		loadAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "load");
		dischargeAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "discharge");
		openSlotAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "openslot");


		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, nameObjectRef);

		// add the total (aggregate) P&L column
		addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
		addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);

		// addPNLColumn("Asia");
		// addPNLColumn("Europe");
		//
		addColumn("Discharge Port", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPort().getName();
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPort().getName();
				}
				return "";
			}
		}, dischargeAllocationRef);
		addColumn("Sales Contract", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					final Contract contract = slotAllocation.getContract();
					if (contract != null) {
						return contract.getName();
					}
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					final Contract contract = slotAllocation.getContract();
					if (contract != null) {
						return contract.getName();
					}
				}
				return "";
			}
		}, dischargeAllocationRef);

		addColumn("Purchase Price", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return super.format(slotAllocation.getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPrice();
				}
				return 0.0;
			}
		}, loadAllocationRef);
		addColumn("Sales Price", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return super.format(slotAllocation.getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPrice();
				}
				return 0.0;
			}
		}, dischargeAllocationRef);

		addColumn("Type", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof OpenSlotAllocation ) {
					OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) object;
					String type = "Open Slot";
					final Slot slot = openSlotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						type = "Long";
					} else if (slot instanceof DischargeSlot) {
						type = "Short";
					}
					return type;
				} else if (object instanceof SlotVisit || object instanceof CargoAllocation) {
					return "Cargo";
				} else if (object instanceof StartEvent) {
					return "Start";
				} else if (object instanceof GeneratedCharterOut) {
					return "Charter Out (virt)";
				} else if (object instanceof VesselEventVisit) {
					final VesselEvent vesselEvent = ((VesselEventVisit) object).getVesselEvent();
					if (vesselEvent instanceof DryDockEvent) {
						return "Dry Dock";
					} else if (vesselEvent instanceof MaintenanceEvent) {
						return "Maintenance";
					} else if (vesselEvent instanceof CharterOutEvent) {
						return "Charter Out";
					}
				}
				return "Unknown";
			}
		}, targetObjectRef);


		// Register columns that will be displayed when in Pin/Diff mode 
		pinDiffModeHelper
			.addColumn("Prev. wiring", generatePreviousWiringColumnFormatter(cargoAllocationRef));
		
	}

	private Integer getEntityPNLEntry(final ProfitAndLossContainer container, final String entity, final EStructuralFeature bookContainmentFeature) {
		if (container == null) {
			return null;
		}

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			return null;
		}

		int groupTotal = 0;
		boolean foundValue = false;
		for (final EntityProfitAndLoss entityPNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
			if (entity == null || entityPNL.getEntity().getName().equals(entity)) {
				foundValue = true;
				final BaseEntityBook entityBook = entityPNL.getEntityBook();
				if (entityBook == null) {
					// Fall back code path for old models.
					if (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
						groupTotal += groupProfitAndLoss.getProfitAndLoss();
					}
				} else {
					if (entityBook.eContainmentFeature() == bookContainmentFeature) {
						groupTotal += entityPNL.getProfitAndLoss();
					}
				}
			}
		}
		if (foundValue) {
			return groupTotal;
		}
		// with a specific entity name, we search the upstream, shipping and downstream entities for the P&L data
		return null;
	}

	private void addPNLColumn(final EStructuralFeature bookContainmentFeature) {
		addPNLColumn("Group Total", null, bookContainmentFeature);
	}

	private void addPNLColumn(final String entityName, final EStructuralFeature bookContainmentFeature) {
		addPNLColumn(entityName, entityName, bookContainmentFeature);
	}

	private void addPNLColumn(final String entityLabel, final String entityKey, final EStructuralFeature bookContainmentFeature) {
		final String book = bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK ? "Shipping" : "Trading";
		final String title = String.format("P&L (%s - %s)", entityLabel, book);

		// HACK: don't the label to the entity column names if the column is for total group P&L
		if (entityKey != null) {
			entityColumnNames.add(title);
		}

		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				ProfitAndLossContainer container = null;

				if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut || object instanceof OpenSlotAllocation) {
					container = (ProfitAndLossContainer) object;
				}
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						container = slotVisit.getSlotAllocation().getCargoAllocation();
					}
				}

				return getEntityPNLEntry(container, entityKey, bookContainmentFeature);
			}
		}, targetObjectRef);
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();

		return new ITreeContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer.getControl().isDisposed()) {
							return;
						}

						if (newInput instanceof IScenarioViewerSynchronizerOutput) {
							final IScenarioViewerSynchronizerOutput synchronizerOutput = (IScenarioViewerSynchronizerOutput) newInput;
							final Collection<LNGScenarioModel> rootObjects = synchronizerOutput.getLNGScenarioModels();
							for (final String s : entityColumnNames) {
								removeColumn(s);
							}

							entityColumnNames.clear();

							for (final LNGScenarioModel rootObject : rootObjects) {

								final CommercialModel commercialModel = rootObject.getCommercialModel();
								if (commercialModel != null) {
									for (final BaseLegalEntity e : commercialModel.getEntities()) {
										addPNLColumn(e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
										addPNLColumn(e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);
									}
								}
							}
						}

						viewer.refresh();
					}
				});
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object object) {
				return superProvider.getElements(object);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				return superProvider.getChildren(parentElement);
			}

			@Override
			public Object getParent(final Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public boolean hasChildren(final Object element) {
				return superProvider.hasChildren(element);
			}
		};
	}

	@Override
	protected void processInputs(final Object[] result) {
		for (final Object row : result) {

			// Map our "Node" data to the CargoAllocation object
			if (row instanceof EObject) {
				final EObject eObj = (EObject) row;
				if (eObj.eIsSet(targetObjectRef)) {

					final Object a = eObj.eGet(targetObjectRef);

					// map to events
					if (a instanceof CargoAllocation) {
						final CargoAllocation allocation = (CargoAllocation) a;

						final List<Object> equivalents = new LinkedList<Object>();
						for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
							equivalents.add(slotAllocation.getSlot());
							equivalents.add(slotAllocation.getSlotVisit());
						}
						equivalents.addAll(allocation.getEvents());
						equivalents.add(allocation.getInputCargo());
						setInputEquivalents(row, equivalents);
					} else if (a instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) a;

						final CargoAllocation allocation = slotVisit.getSlotAllocation().getCargoAllocation();

						final List<Object> equivalents = new LinkedList<Object>();
						for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
							equivalents.add(slotAllocation.getSlot());
							equivalents.add(slotAllocation.getSlotVisit());
						}
						equivalents.addAll(allocation.getEvents());
						equivalents.add(allocation.getInputCargo());
						setInputEquivalents(row, equivalents);
					} else if (a instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) a;
						setInputEquivalents(row, Lists.<Object> newArrayList(vesselEventVisit, vesselEventVisit.getVesselEvent()));
					} else if (a instanceof StartEvent) {
						final StartEvent startEvent = (StartEvent) a;
						setInputEquivalents(row, Lists.<Object> newArrayList(startEvent, startEvent.getSequence().getVesselAvailability().getVessel()));
					} else if (a instanceof OpenSlotAllocation) {
						final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
						setInputEquivalents(row, Lists.<Object> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
					}
				}
			}
		}
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			private EObject dataModelInstance;

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				SchedulePnLReport.this.clearPinModeData();
				dataModelInstance = GenericEMFTableDataModel.createRootInstance(tableDataModel);

			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {

				final List<EObject> interestingEvents = new LinkedList<EObject>();
				for (final Sequence sequence : schedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof StartEvent) {
							interestingEvents.add(event);
						} else if (event instanceof VesselEventVisit) {
							interestingEvents.add(event);
						} else if (event instanceof GeneratedCharterOut) {
							interestingEvents.add(event);
						} else if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								interestingEvents.add(event);
							}
						}
					}
				} 
				interestingEvents.addAll(schedule.getOpenSlotAllocations());

				final List<EObject> nodes = generateNodes(dataModelInstance, interestingEvents);

				SchedulePnLReport.this.collectPinModeElements(nodes, isPinned);

				return nodes;
			}
		};
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	@Override
	public String getElementKey(EObject element) {

		if (element.eIsSet(cargoAllocationRef)) {
			element = (EObject) element.eGet(cargoAllocationRef);
		} else if (element.eIsSet(targetObjectRef)) {
			element = (EObject) element.eGet(targetObjectRef);
		}

		if (element instanceof CargoAllocation) {
			return ((CargoAllocation) element).getName();
		} else if (element instanceof Event) {
			return ((Event) element).name();
		}
		return super.getElementKey(element);
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return ScheduleDiffUtils.isElementDifferent((EObject) pinnedObject.eGet(targetObjectRef), (EObject) otherObject.eGet(targetObjectRef));
	}

	@Override
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		final List<Object> adaptedSelection = new ArrayList<Object>(selection.size());
		for (final Object obj : selection) {
			if (obj instanceof EObject) {
				adaptedSelection.add(((EObject) obj).eGet(targetObjectRef));
			}
		}

		return adaptedSelection;
	}

	private List<EObject> generateNodes(final EObject dataModelInstance, final List<EObject> interestingElements) {
		final List<EObject> nodes = new ArrayList<EObject>(interestingElements.size());

		for (final Object element : interestingElements) {

			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();

				// Build up list of slots assigned to cargo, sorting into loads and discharges
				final List<SlotAllocation> loadSlots = new ArrayList<SlotAllocation>();
				final List<SlotAllocation> dischargeSlots = new ArrayList<SlotAllocation>();
				for (final SlotAllocation slot : cargoAllocation.getSlotAllocations()) {
					if (slot.getSlot() instanceof LoadSlot) {
						loadSlots.add(slot);
					} else if (slot.getSlot() instanceof DischargeSlot) {
						dischargeSlots.add(slot);
					} else {
						// Assume some kind of discharge?
						// dischargeSlots.add((Slot) slot);
					}

				}

				final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);
				// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
				for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {

					final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, "cargo", cargoAllocation);
					if (i < loadSlots.size()) {
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, "load", loadSlots.get(i));
					}
					if (i < dischargeSlots.size()) {
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, "discharge", dischargeSlots.get(i));
					}
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, "target", cargoAllocation);
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, "name", slotVisit.name());
					nodes.add(node);
				}
			} else if (element instanceof OpenSlotAllocation) {

				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);
				final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, "target", openSlotAllocation);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, "openslot", openSlotAllocation);
				final Slot slot = openSlotAllocation.getSlot();
				if (slot == null) {
					
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, "name", "??");
				} else {
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, "name", slot.getName());
				}
				nodes.add(node);
				
			} else if (element instanceof Event) {
				final Event event = (Event) element;
				final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);

				final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, "target", event);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, "name", event.name());
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 */
	@Override
	protected boolean handleSelections() {
		return true;
	}
}
