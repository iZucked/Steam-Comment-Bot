package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.utils.CargoAllocationUtils;
import com.mmxlabs.lingo.reports.utils.PinDiffModeColumnManager;
import com.mmxlabs.lingo.reports.utils.RelatedSlotAllocations;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IFormatter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

public class ScheduleBasedReportBuilder {

	private static final Logger log = LoggerFactory.getLogger(ScheduleBasedReportBuilder.class);

	private class ToStringFunction implements Function<Slot, String> {

		@Override
		public String apply(final Slot o) {
			return o.getName();
		}

		@Override
		public String toString() {
			return "toString";
		}

	}

	private final RelatedSlotAllocations relatedSlotAllocations = new RelatedSlotAllocations();

	private final PinDiffModeColumnManager pinDiffModeHelper;
	private final EMFReportView report;

	private final EPackage tableDataModel;
	private final EStructuralFeature nameObjectRef;
	private final EStructuralFeature name2ObjectRef;
	private final EStructuralFeature targetObjectRef;
	private final EStructuralFeature cargoAllocationRef;
	private final EStructuralFeature loadAllocationRef;
	private final EStructuralFeature dischargeAllocationRef;
	private final EStructuralFeature openSlotAllocationRef;

	public static final String ROW_FILTER_LONG_CARGOES = "Longs";
	public static final String ROW_FILTER_SHORT_CARGOES = "Shorts";
	public static final String ROW_FILTER_VESSEL_START_ROW = "Start ballast legs";
	public static final String ROW_FILTER_VESSEL_EVENT_ROW = "Vessel Events";
	public static final String ROW_FILTER_CHARTER_OUT_ROW = "Charter Outs (Virt)";
	public static final String ROW_FILTER_CARGO_ROW = "Cargoes";

	// All filters
	public static final String[] ROW_FILTER_ALL = new String[] { ROW_FILTER_LONG_CARGOES, ROW_FILTER_SHORT_CARGOES, ROW_FILTER_VESSEL_START_ROW, ROW_FILTER_VESSEL_EVENT_ROW,
			ROW_FILTER_CHARTER_OUT_ROW, ROW_FILTER_CARGO_ROW };

	private final Set<String> rowFilterInfo = new HashSet<>();

	public ScheduleBasedReportBuilder(final EMFReportView report, final PinDiffModeColumnManager pinDiffModeHelper) {
		this.report = report;
		this.pinDiffModeHelper = pinDiffModeHelper;

		// Create table data model
		tableDataModel = GenericEMFTableDataModel.createEPackage("target", "cargo", "load", "discharge", "openslot");

		final EClass rowClass = GenericEMFTableDataModel.getRowClass(tableDataModel);
		nameObjectRef = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.Literals.ESTRING, "name");
		name2ObjectRef = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.Literals.ESTRING, "name2");
		// GenericEMFTableDataModel.getRowFeature(tableDataModel, "name");
		targetObjectRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "target");
		cargoAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "cargo");
		loadAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "load");
		dischargeAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "discharge");
		openSlotAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "openslot");
	}

	public void setRowFilter(final String... filters) {
		rowFilterInfo.clear();
		for (final String filter : filters) {
			rowFilterInfo.add(filter);
		}
	}

	public EPackage getTableDataModel() {
		return tableDataModel;
	}

	public EStructuralFeature getNameObjectRef() {
		return nameObjectRef;
	}

	public EStructuralFeature getName2ObjectRef() {
		return name2ObjectRef;
	}

	public EStructuralFeature getTargetObjectRef() {
		return targetObjectRef;
	}

	public EStructuralFeature getCargoAllocationRef() {
		return cargoAllocationRef;
	}

	public EStructuralFeature getLoadAllocationRef() {
		return loadAllocationRef;
	}

	public EStructuralFeature getDischargeAllocationRef() {
		return dischargeAllocationRef;
	}

	public EStructuralFeature getOpenSlotAllocationRef() {
		return openSlotAllocationRef;
	}

	public IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			private EObject dataModelInstance;

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				relatedSlotAllocations.clear();
				report.clearPinModeData();
				dataModelInstance = GenericEMFTableDataModel.createRootInstance(tableDataModel);

			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {

				final List<EObject> nodes = generateNodes(dataModelInstance, schedule);

				report.collectPinModeElements(nodes, isPinned);

				return nodes;
			}
		};
	}

	public List<?> adaptSelectionFromWidget(final List<?> selection) {
		final List<Object> adaptedSelection = new ArrayList<Object>(selection.size());
		for (final Object obj : selection) {
			if (obj instanceof EObject) {
				adaptedSelection.add(((EObject) obj).eGet(targetObjectRef));
			}
		}

		return adaptedSelection;
	}

	public void processInputs(final Object[] result) {
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
						report.setInputEquivalents(row, equivalents);
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
						report.setInputEquivalents(row, equivalents);
					} else if (a instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) a;
						report.setInputEquivalents(row, Lists.<Object> newArrayList(vesselEventVisit, vesselEventVisit.getVesselEvent()));
					} else if (a instanceof StartEvent) {
						final StartEvent startEvent = (StartEvent) a;
						report.setInputEquivalents(row, Lists.<Object> newArrayList(startEvent, startEvent.getSequence().getVesselAvailability().getVessel()));
					} else if (a instanceof OpenSlotAllocation) {
						final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
						report.setInputEquivalents(row, Lists.<Object> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
					}
				}
			}
		}
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	public String getElementKey(EObject element) {

		if (element.eIsSet(cargoAllocationRef)) {
			element = (EObject) element.eGet(cargoAllocationRef);
		} else if (element.eIsSet(openSlotAllocationRef)) {
			element = (EObject) element.eGet(openSlotAllocationRef);
		} else if (element.eIsSet(targetObjectRef)) {
			element = (EObject) element.eGet(targetObjectRef);
		}

		if (element instanceof CargoAllocation) {
			return ((CargoAllocation) element).getName();
		} else if (element instanceof OpenSlotAllocation) {
			return ((OpenSlotAllocation) element).getSlot().getName();
		} else if (element instanceof Event) {
			return ((Event) element).name();
		}
		if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		}
		return element.toString();
	}

	private boolean showOpenSlot(final OpenSlotAllocation openSlotAllocation) {

		if (openSlotAllocation.getSlot() instanceof LoadSlot) {
			return rowFilterInfo.contains(ROW_FILTER_LONG_CARGOES);
		} else if (openSlotAllocation.getSlot() instanceof DischargeSlot) {
			return rowFilterInfo.contains(ROW_FILTER_SHORT_CARGOES);
		}
		return false;
	}

	private boolean showEvent(final Event event) {
		if (event instanceof StartEvent) {
			return rowFilterInfo.contains(ROW_FILTER_VESSEL_START_ROW);
		} else if (event instanceof VesselEventVisit) {
			return rowFilterInfo.contains(ROW_FILTER_VESSEL_EVENT_ROW);
		} else if (event instanceof GeneratedCharterOut) {
			return rowFilterInfo.contains(ROW_FILTER_CHARTER_OUT_ROW);
		} else if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				return rowFilterInfo.contains(ROW_FILTER_CARGO_ROW);
			}
		}
		return false;
	}

	public List<EObject> generateNodes(final EObject dataModelInstance, final Schedule schedule) {

		final List<EObject> interestingEvents = new LinkedList<EObject>();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (showEvent(event)) {
					interestingEvents.add(event);
				}
			}
		}
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			if (showOpenSlot(openSlotAllocation)) {
				interestingEvents.add(openSlotAllocation);
			}
		}

		return generateNodes(dataModelInstance, interestingEvents);
	}

	public List<EObject> generateNodes(final EObject dataModelInstance, final List<EObject> interestingElements) {
		final List<EObject> nodes = new ArrayList<EObject>(interestingElements.size());

		for (final Object element : interestingElements) {

			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();

				relatedSlotAllocations.updateRelatedSetsFor(cargoAllocation);

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
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, cargoAllocationRef, cargoAllocation);
					if (i < loadSlots.size()) {
						SlotAllocation slot = loadSlots.get(i);
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, loadAllocationRef, slot);
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, slot.getName());
					}
					if (i < dischargeSlots.size()) {
						SlotAllocation slot = dischargeSlots.get(i);
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, dischargeAllocationRef, slot);
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, name2ObjectRef, slot.getName());
					}
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, targetObjectRef, cargoAllocation);
					nodes.add(node);
				}
			} else if (element instanceof OpenSlotAllocation) {

				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);
				final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, targetObjectRef, openSlotAllocation);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, openSlotAllocationRef, openSlotAllocation);
				final Slot slot = openSlotAllocation.getSlot();
				if (slot == null) {
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, "??");
				} else {
					if (slot instanceof DischargeSlot) {
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, name2ObjectRef, slot.getName());
					} else {
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, slot.getName());
					}
				}
				nodes.add(node);

			} else if (element instanceof Event) {
				final Event event = (Event) element;
				final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);

				final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, targetObjectRef, event);
				GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, event.name());
				nodes.add(node);
			}
		}
		return nodes;
	}

	// / Normal Columns

	// ////// Pin / Diff Columns

	public void createPinDiffColumns() {
		// Register columns that will be displayed when in Pin/Diff mode
		report.addColumn("Prev. wiring", ColumnType.DIFF, generatePreviousWiringColumnFormatter(cargoAllocationRef));
		report.addColumn("Prev. Vessel", ColumnType.DIFF, generatePreviousVesselAssignmentColumnFormatter(cargoAllocationRef));
		report.addColumn("Permutation", ColumnType.DIFF, generatePermutationColumnFormatter(cargoAllocationRef));
	}

	/**
	 * Generate a new formatter for the previous-vessel-assignment column
	 * 
	 * Used in pin/diff mode.
	 * 
	 * @param cargoAllocationRef
	 * @return
	 */
	private IFormatter generatePreviousVesselAssignmentColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String format(final Object obj) {
				final Pair<EObject, CargoAllocation> eObjectAsCargoAllocation = getIfCargoAllocation(obj, cargoAllocationRef);
				if (eObjectAsCargoAllocation == null) {
					return "";
				}

				final EObject eObj = eObjectAsCargoAllocation.getFirst();

				final String currentAssignment = getVesselAssignmentName(eObjectAsCargoAllocation.getSecond());

				String result = "";

				// for objects not coming from the pinned scenario,
				// return and display the vessel used by the pinned counterpart
				if (!pinDiffModeHelper.pinnedObjectsContains(eObj)) {

					try {
						final EObject pinnedObject = pinDiffModeHelper.getPinnedObjectWithTheSameKeyAsThisObject(eObj);
						if (pinnedObject != null) {
							final CargoAllocation ca = (CargoAllocation) pinnedObject.eGet(cargoAllocationRef);
							result = getVesselAssignmentName(ca);
						}
					} catch (final Exception e) {
						log.warn("Error formatting previous assignment", e);
					}
				}

				// Only show if different.
				if (currentAssignment.equals(result)) {
					return "";
				}
				return result;
			}

			protected String getVesselAssignmentName(final CargoAllocation ca) {
				if (ca == null) {
					return "";
				}
				final Cargo inputCargo = ca.getInputCargo();
				if (inputCargo == null) {
					return "";
				}
				final AVesselSet<? extends Vessel> l = inputCargo.getAssignment();
				if (l != null) {
					return l.getName();
				}
				return "";
			}
		};
	}

	/**
	 * Generate a new formatter for the previous-wiring column
	 * 
	 * Used in pin/diff mode.
	 * 
	 * @param cargoAllocationRef
	 * @return
	 */
	private IFormatter generatePreviousWiringColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String format(final Object obj) {

				if (obj instanceof EObject) {
					final EObject eObj = (EObject) obj;
					if (eObj.eIsSet(cargoAllocationRef)) {
						final Pair<EObject, CargoAllocation> eObjectAsCargoAllocation = new Pair<EObject, CargoAllocation>(eObj, (CargoAllocation) eObj.eGet(cargoAllocationRef));

						final String currentWiring = CargoAllocationUtils.getSalesWiringAsString(eObjectAsCargoAllocation.getSecond());

						String result = "";

						// for objects not coming from the pinned scenario,
						// return the pinned counterpart's wiring to display as the previous wiring
						if (!pinDiffModeHelper.pinnedObjectsContains(eObj)) {

							try {
								final EObject pinnedObject = pinDiffModeHelper.getPinnedObjectWithTheSameKeyAsThisObject(eObj);
								if (pinnedObject != null) {
									final CargoAllocation pinnedCargoAllocation = (CargoAllocation) pinnedObject.eGet(cargoAllocationRef);
									if (pinnedCargoAllocation != null) {
										// convert this cargo's wiring of slot allocations to a string
										result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
									}
								}
							} catch (final Exception e) {
								log.warn("Error formatting previous wiring", e);
							}

						}

						// Do not display if same
						if (currentWiring.equals(result)) {
							return "";
						}

						return result;
					} else if (eObj.eIsSet(openSlotAllocationRef)) {
						final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) eObj.eGet(openSlotAllocationRef);
						if (openSlotAllocation != null) {

							final EObject pinnedObject = pinDiffModeHelper.getPinnedObjectWithTheSameKeyAsThisObject(eObj);
							if (pinnedObject != null) {
								if (pinnedObject.eIsSet(cargoAllocationRef)) {
									final CargoAllocation pinnedCargoAllocation = (CargoAllocation) pinnedObject.eGet(cargoAllocationRef);
									if (pinnedCargoAllocation != null) {

										final String result;
										if (openSlotAllocation.getSlot() instanceof LoadSlot) {

											result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
										} else {
											result = CargoAllocationUtils.getPurchaseWiringAsString(pinnedCargoAllocation);
										}
										return result;
									}
								}
							}

						}
					}
				}
				return "";
			}
		};
	}

	private IFormatter generatePermutationColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String format(final Object obj) {

				if (obj instanceof EObject) {
					final EObject eObj = (EObject) obj;
					if (eObj.eIsSet(cargoAllocationRef)) {
						final Pair<EObject, CargoAllocation> eObjectAsCargoAllocation = new Pair<EObject, CargoAllocation>(eObj, (CargoAllocation) eObj.eGet(cargoAllocationRef));

						// Check to see if wiring has changed.
						boolean different = false;
						{
							final String currentWiring = CargoAllocationUtils.getSalesWiringAsString(eObjectAsCargoAllocation.getSecond());

							if (pinDiffModeHelper.pinnedObjectsContains(eObj)) {
								final Set<EObject> unpinnedObjects = pinDiffModeHelper.getUnpinnedObjectWithTheSameKeyAsThisObject(eObj);
								if (unpinnedObjects != null) {
									for (final EObject unpinnedObject : unpinnedObjects) {
										final CargoAllocation pinnedCargoAllocation = (CargoAllocation) unpinnedObject.eGet(cargoAllocationRef);
										if (pinnedCargoAllocation != null) {
											// convert this cargo's wiring of slot allocations to a string
											final String result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);

											different = !currentWiring.equals(result);
										} else {
											different = true;
										}
										if (different) {
											break;
										}
									}
								}
							} else {
								final EObject pinnedObject = pinDiffModeHelper.getPinnedObjectWithTheSameKeyAsThisObject(eObj);
								if (pinnedObject != null) {
									final CargoAllocation pinnedCargoAllocation = (CargoAllocation) pinnedObject.eGet(cargoAllocationRef);
									if (pinnedCargoAllocation != null) {
										// convert this cargo's wiring of slot allocations to a string
										final String result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
										different = !currentWiring.equals(result);
									} else {
										different = true;
									}
								}

							}
						}
						if (!different) {
							// FIXME: This excess filters out stuff with spots.
//							return "";
						}

						// EObject eObj = eObjectAsCargoAllocation.getFirst();
						final CargoAllocation thisCargoAllocation = eObjectAsCargoAllocation.getSecond();

						// FIXME: This only works as refresh is triggered multiple times. Otherwise this first call only gets this cargoes slot allocations. The set is updated with other cargo
						// allocations and
						// the second refresh call gets the correct data string as a result of the first op.
//						relatedSlotAllocations.updateRelatedSetsFor(thisCargoAllocation);

						final Set<Slot> buysSet = relatedSlotAllocations.getRelatedSetFor(thisCargoAllocation, true);
						final Set<Slot> sellsSet = relatedSlotAllocations.getRelatedSetFor(thisCargoAllocation, false);

						final String buysStr = "[ " + Joiner.on(", ").skipNulls().join(Iterables.transform(buysSet, new ToStringFunction())) + " ]";
						final String sellsStr = "[ " + Joiner.on(", ").skipNulls().join(Iterables.transform(sellsSet, new ToStringFunction())) + " ]";

						return String.format("Rewire %d x %d; Buys %s, Sells %s", buysSet.size(), sellsSet.size(), buysStr, sellsStr);
					} else if (eObj.eIsSet(openSlotAllocationRef)) {
						final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) eObj.eGet(openSlotAllocationRef);
						if (openSlotAllocation != null) {
							final Set<Slot> buysSet = relatedSlotAllocations.getRelatedSetFor(openSlotAllocation, true);
							final Set<Slot> sellsSet = relatedSlotAllocations.getRelatedSetFor(openSlotAllocation, false);

							final String buysStr = "[ " + Joiner.on(", ").skipNulls().join(Iterables.transform(buysSet, new ToStringFunction())) + " ]";
							final String sellsStr = "[ " + Joiner.on(", ").skipNulls().join(Iterables.transform(sellsSet, new ToStringFunction())) + " ]";

							return String.format("Rewire %d x %d; Buys %s, Sells %s", buysSet.size(), sellsSet.size(), buysStr, sellsStr);
						}
					} else {
						final Object target = eObj.eGet(targetObjectRef);
						if (target instanceof GeneratedCharterOut) {
							return "Charter out (Virt)";
						}
						if (target instanceof StartEvent) {
							return "Orphan Ballast";
						}
						if (target instanceof DryDockEvent) {
							return "Drydock";
						}
						if (target instanceof MaintenanceEvent) {
							return "Maintenance";
						}
						if (target instanceof CharterOutEvent) {
							return "Charter out";
						}
						if (target instanceof DryDockEvent) {
							return "Event";
						}
					}
				}
				return "";
			}

		};

	}

	protected Pair<EObject, CargoAllocation> getIfCargoAllocation(final Object obj, final EStructuralFeature cargoAllocationRef) {
		if (obj instanceof EObject) {
			final EObject eObj = (EObject) obj;
			if (eObj.eIsSet(cargoAllocationRef)) {
				return new Pair<EObject, CargoAllocation>(eObj, (CargoAllocation) eObj.eGet(cargoAllocationRef));
			}
		}
		return null;
	}

	public Set<String> getRowFilterInfo() {
		return rowFilterInfo;
	}

}
