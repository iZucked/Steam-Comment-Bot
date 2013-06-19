/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.utils.ScheduleDiffUtils;

/**
 * @since 3.0
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

	public SchedulePnLReport() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoPnLReportView");

		tableDataModel = GenericEMFTableDataModel.createEPackage("target", "name", "cargo", "load", "discharge");
		nameObjectRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "name");
		targetObjectRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "target");
		cargoAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "cargo");
		loadAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "load");
		dischargeAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "discharge");

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, nameObjectRef, s.getEvent__Name());

		// add the total (aggregate) P&L column
		addPNLColumn();

		// CommercialModel.getEntities();

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
		// addColumn("Shipping Cost", new BaseFormatter() {
		//
		// Double getValue(final SlotVisit visit) {
		// final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
		// if (cargoAllocation == null) {
		// return null;
		// }
		// final Cargo inputCargo = cargoAllocation.getInputCargo();
		// if (inputCargo == null) {
		// return null;
		// }
		// if (inputCargo.getCargoType() != CargoType.FLEET) {
		// return null;
		// }
		// // TODO: Fixed (other) port costs?
		// // TODO: Boil-off included?
		//
		// final ExtraData dataWithKey = cargoAllocation.getDataWithKey(TradingConstants.ExtraData_ShippingCostIncBoilOff);
		// if (dataWithKey != null) {
		// final Integer v = dataWithKey.getValueAs(Integer.class);
		// if (v != null) {
		// final SlotAllocation loadAllocation = cargoAllocation.getLoadAllocation();
		// if (loadAllocation == null) {
		// return null;
		// }
		// final double dischargeVolumeInMMBTu = (double) cargoAllocation.getDischargeVolume() * ((LoadSlot) loadAllocation.getSlot()).getSlotOrPortCV();
		// if (dischargeVolumeInMMBTu == 0.0) {
		// return 0.0;
		// }
		// final double shipping = (double) v.doubleValue() / dischargeVolumeInMMBTu;
		// return shipping;
		// }
		// }
		// return 0.0;
		// }
		//
		// @Override
		// public String format(final Object object) {
		// if (object instanceof SlotVisit) {
		// final SlotVisit slotVisit = (SlotVisit) object;
		// final Double value = getValue(slotVisit);
		// if (value != null) {
		// return String.format("%,.2f", value);
		// }
		//
		// }
		// return null;
		// }
		//
		// @Override
		// public Comparable getComparable(final Object object) {
		// if (object instanceof SlotVisit) {
		// final SlotVisit slotVisit = (SlotVisit) object;
		// final Double value = getValue(slotVisit);
		// if (value != null) {
		// return value;
		// }
		// }
		// return 0.0;
		// }
		// });

		addColumn("Type", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
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

	}

	//
	// @Override
	// protected boolean handleSelections() {
	// return true;
	// }
	//
	// @Override
	// protected Class<?> getSelectionAdaptionClass() {
	// return Event.class;
	// }

	private Integer getEntityPNLEntry(final ProfitAndLossContainer container, final String entity) {
		if (container == null) {
			return null;
		}

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			return null;
		}

		// supplying null for the entity name indicates that the total group P&L should be returned
		if (entity == null) {
			return (int) groupProfitAndLoss.getProfitAndLoss();
		}
		// with a specific entity name, we search the upstream, shipping and downstream entities for the P&L data
		else {
			int groupTotal = 0;
			boolean foundValue = false;
			for (final EntityProfitAndLoss ePnl : groupProfitAndLoss.getEntityProfitAndLosses()) {
				if (ePnl.getEntity().getName().equals(entity)) {

					groupTotal += ePnl.getProfitAndLoss();
					foundValue = true;
				}
			}
			if (foundValue) {
				return groupTotal;
			}
		}
		return null;
	}

	private void addPNLColumn() {
		addPNLColumn("Group Total", null);
	}

	private void addPNLColumn(final String entityName) {
		addPNLColumn(entityName, entityName);
	}

	private void addPNLColumn(final String entityLabel, final String entityKey) {
		final String title = String.format("P&L (%s)", entityLabel);

		// HACK: don't the label to the entity column names if the column is for total group P&L
		if (entityKey != null) {
			entityColumnNames.add(title);
		}

		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				ProfitAndLossContainer container = null;

				if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut) {
					container = (ProfitAndLossContainer) object;
				}
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						container = slotVisit.getSlotAllocation().getCargoAllocation();
					}
				}

				return getEntityPNLEntry(container, entityKey);
			}
		}, targetObjectRef);
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		final IStructuredContentProvider superProvider = super.getContentProvider();

		return new IStructuredContentProvider() {
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
									for (final LegalEntity e : commercialModel.getEntities()) {
										addPNLColumn(e.getName());
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
						setInputEquivalents(row, Lists.<Object> newArrayList(vesselEventVisit.getVesselEvent()));
					} else if (a instanceof StartEvent) {
						final StartEvent startEvent = (StartEvent) a;
						setInputEquivalents(row, Lists.<Object> newArrayList(startEvent.getSequence().getVesselAvailability().getVessel()));
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

				final List<Event> interestingEvents = new LinkedList<Event>();
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
	 * @since 1.1
	 */
	@Override
	protected String getElementKey(EObject element) {

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
		return ScheduleDiffUtils.isElementDifferent(pinnedObject, otherObject);
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

	private List<EObject> generateNodes(final EObject dataModelInstance, final List<Event> interestingElements) {
		final List<EObject> nodes = new ArrayList<EObject>(interestingElements.size());

		for (Object element : interestingElements) {

			if (element instanceof SlotVisit) {
				SlotVisit slotVisit = (SlotVisit) element;
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
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, "name", element);
					nodes.add(node);
				}
			} else {
				final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);

				final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, "target", element);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, "name", element);
				nodes.add(node);
			}
		}
		return nodes;
	}
	
	@Override
	protected boolean handleSelections() {
		return true;
	}
}
