package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.components.EMFReportView.EmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.utils.CargoAllocationUtils;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandlerExtension;
import com.mmxlabs.lingo.reports.utils.PinDiffModeColumnManager;
import com.mmxlabs.lingo.reports.utils.RelatedSlotAllocations;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
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
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 * Big helper class for any report based on {@link CargoAllocation}s, {@link OpenSlotAllocation}s, or other events. This builds the internal report data model and handles pin/diff comparison hooks.
 * Currently this class also some generic columns used in these reports but these should be broken out into separate classes as part of FogBugz: 51/
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduleBasedReportBuilder {
	public static final String CARGO_REPORT_TYPE_ID = "CARGO_REPORT_TYPE_ID";

	public static final String COLUMN_BLOCK_PNL = "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_group";

	private static final Logger log = LoggerFactory.getLogger(ScheduleBasedReportBuilder.class);

	public static final String ROW_FILTER_LONG_CARGOES = "Longs";
	public static final String ROW_FILTER_SHORT_CARGOES = "Shorts";
	public static final String ROW_FILTER_VESSEL_START_ROW = "Start ballast legs";
	public static final String ROW_FILTER_VESSEL_EVENT_ROW = "Vessel Events";
	public static final String ROW_FILTER_CHARTER_OUT_ROW = "Charter Outs (Virt)";
	public static final String ROW_FILTER_CARGO_ROW = "Cargoes";

	/** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
	public static final String[] ROW_FILTER_ALL = new String[] { ROW_FILTER_CARGO_ROW, ROW_FILTER_LONG_CARGOES, ROW_FILTER_SHORT_CARGOES, ROW_FILTER_VESSEL_EVENT_ROW, ROW_FILTER_CHARTER_OUT_ROW,
			ROW_FILTER_VESSEL_START_ROW };

	public static final String DIFF_FILTER_PINNDED_SCENARIO = "Pinned Scenario";
	public static final String DIFF_FILTER_VESSEL_CHANGES = "Vessel Changes";

	/** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
	public static final String[] DIFF_FILTER_ALL = new String[] { DIFF_FILTER_PINNDED_SCENARIO, DIFF_FILTER_VESSEL_CHANGES };

	private static final String ROW_FILTER_MEMENTO = "ROW_FILTER";
	private static final String DIFF_FILTER_MEMENTO = "DIFF_FILTER";

	private final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();

	/**
	 * Guava {@link Function} to convert a Slot to a String based on it's name;
	 * 
	 */
	private static class SlotToStringFunction implements Function<Slot, String> {

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

	private PinDiffModeColumnManager pinDiffModeHelper;
	private EMFReportView report;

	// Dynamic table data model fields
	private final EPackage tableDataModel;
	private final EStructuralFeature nameObjectRef;
	private final EStructuralFeature name2ObjectRef;
	private final EStructuralFeature targetObjectRef;
	private final EStructuralFeature cargoAllocationRef;
	private final EStructuralFeature loadAllocationRef;
	private final EStructuralFeature dischargeAllocationRef;
	private final EStructuralFeature openSlotAllocationRef;

	private final Set<String> rowFilterInfo = new HashSet<>();
	private final Set<String> diffFilterInfo = new HashSet<>();

	@Inject(optional = true)
	private Iterable<ICustomRelatedSlotHandlerExtension> customRelatedSlotHandlers;

	public ScheduleBasedReportBuilder() {

		// Create table data model with the given references.
		tableDataModel = GenericEMFTableDataModel.createEPackage("target", "cargo", "load", "discharge", "openslot");

		// Get the row class
		final EClass rowClass = GenericEMFTableDataModel.getRowClass(tableDataModel);
		// Add additional name attributes
		nameObjectRef = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.Literals.ESTRING, "name");
		name2ObjectRef = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.Literals.ESTRING, "name2");

		// Get the EStructuralFeature refs created above.
		targetObjectRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "target");
		cargoAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "cargo");
		loadAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "load");
		dischargeAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "discharge");
		openSlotAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "openslot");
	}

	public EMFReportView getReport() {
		return report;
	}

	public void setReport(final EMFReportView report) {
		this.report = report;
	}

	public void setPinDiffModeHelper(final PinDiffModeColumnManager pinDiffModeHelper) {
		this.pinDiffModeHelper = pinDiffModeHelper;
	}

	/**
	 * Replace the existing row filters with the following set.
	 * 
	 * @param filters
	 */
	public void setRowFilter(final String... filters) {
		rowFilterInfo.clear();
		for (final String filter : filters) {
			rowFilterInfo.add(filter);
		}
	}

	public void addRowFilters(final String... filters) {
		for (final String filter : filters) {
			rowFilterInfo.add(filter);
		}
	}

	public void removeRowFilters(final String... filters) {
		for (final String filter : filters) {
			rowFilterInfo.remove(filter);
		}
	}

	/**
	 * Replace the existing row filters with the following set.
	 * 
	 * @param filters
	 */
	public void setDiffFilter(final String... filters) {
		diffFilterInfo.clear();
		for (final String filter : filters) {
			diffFilterInfo.add(filter);
		}
	}

	public void addDiffFilters(final String... filters) {
		for (final String filter : filters) {
			diffFilterInfo.add(filter);
		}
	}

	public void removeDiffFilters(final String... filters) {
		for (final String filter : filters) {
			diffFilterInfo.remove(filter);
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
						final VesselAvailability vesselAvailability = startEvent.getSequence().getVesselAvailability();
						if (vesselAvailability != null) {
							report.setInputEquivalents(row, Lists.<Object> newArrayList(startEvent, vesselAvailability.getVessel()));
						}
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

		final List<ICustomRelatedSlotHandler> relatedSlotHandlers = getCustomRelatedSlotHandlers();

		final List<EObject> interestingEvents = new LinkedList<EObject>();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {

				// Always consider cargoes for permutations, even if filtered out
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();

					// TODO: Only required for pin/diff mode really.
					relatedSlotAllocations.updateRelatedSetsFor(cargoAllocation);

					for (final ICustomRelatedSlotHandler h : relatedSlotHandlers) {
						h.addRelatedSlots(relatedSlotAllocations, schedule, cargoAllocation);
					}
				}

				if (showEvent(event)) {
					interestingEvents.add(event);
				}
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			// Always consider open positions for permutations, even if filtered out
			for (final ICustomRelatedSlotHandler h : relatedSlotHandlers) {
				h.addRelatedSlots(relatedSlotAllocations, schedule, openSlotAllocation);
			}
			if (showOpenSlot(openSlotAllocation)) {
				interestingEvents.add(openSlotAllocation);
			}
		}

		return generateNodes(dataModelInstance, schedule, interestingEvents);
	}

	private List<ICustomRelatedSlotHandler> getCustomRelatedSlotHandlers() {
		final List<ICustomRelatedSlotHandler> l = new LinkedList<ICustomRelatedSlotHandler>();
		if (customRelatedSlotHandlers != null) {
			for (final ICustomRelatedSlotHandlerExtension h : customRelatedSlotHandlers) {
				l.add(h.getInstance());
			}
		}
		return l;
	}

	public List<EObject> generateNodes(final EObject dataModelInstance, final Schedule schedule, final List<EObject> interestingElements) {
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
					GenericEMFTableDataModel.setRowValue(tableDataModel, node, cargoAllocationRef, cargoAllocation);
					if (i < loadSlots.size()) {
						final SlotAllocation slot = loadSlots.get(i);
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, loadAllocationRef, slot);
						GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, slot.getName());
					}
					if (i < dischargeSlots.size()) {
						final SlotAllocation slot = dischargeSlots.get(i);
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

	public Set<String> getRowFilterInfo() {
		return rowFilterInfo;
	}

	public Set<String> getDiffFilterInfo() {
		return diffFilterInfo;
	}

	public void saveToMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento rowsInfo = memento.createChild(uniqueConfigKey);
		for (final String option : rowFilterInfo) {
			final IMemento optionInfo = rowsInfo.createChild(ROW_FILTER_MEMENTO);
			optionInfo.putTextData(option);
		}
		for (final String option : diffFilterInfo) {
			final IMemento optionInfo = rowsInfo.createChild(DIFF_FILTER_MEMENTO);
			optionInfo.putTextData(option);
		}
	}

	public void initFromMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento rowsInfo = memento.getChild(uniqueConfigKey);
		if (rowsInfo != null) {
			rowFilterInfo.clear();
			for (final IMemento optionInfo : rowsInfo.getChildren(ROW_FILTER_MEMENTO)) {
				rowFilterInfo.add(optionInfo.getTextData());
			}
			diffFilterInfo.clear();
			for (final IMemento optionInfo : rowsInfo.getChildren(DIFF_FILTER_MEMENTO)) {
				diffFilterInfo.add(optionInfo.getTextData());
			}
		} else {
			rowFilterInfo.addAll(Arrays.asList(ScheduleBasedReportBuilder.ROW_FILTER_ALL));
			diffFilterInfo.addAll(Arrays.asList(ScheduleBasedReportBuilder.DIFF_FILTER_ALL));
		}
		refreshDiffOptions();
	}

	// / Normal Columns

	// ////// Pin / Diff Columns

	public void refreshDiffOptions() {
		scheduleDiffUtils.setCheckAssignmentDifferences(diffFilterInfo.contains(DIFF_FILTER_VESSEL_CHANGES));
		pinDiffModeHelper.setShowPinnedData(diffFilterInfo.contains(DIFF_FILTER_PINNDED_SCENARIO));
	}

	public boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return scheduleDiffUtils.isElementDifferent((EObject) pinnedObject.eGet(targetObjectRef), (EObject) otherObject.eGet(targetObjectRef));
	}

	/**
	 * Generate a new formatter for the previous-vessel-assignment column
	 * 
	 * Used in pin/diff mode.
	 * 
	 * @param cargoAllocationRef
	 * @return
	 */
	public IFormatter generatePreviousVesselAssignmentColumnFormatter(final EStructuralFeature cargoAllocationRef) {
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
	public IFormatter generatePreviousWiringColumnFormatter(final EStructuralFeature cargoAllocationRef) {
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
									if (pinnedObject.eIsSet(cargoAllocationRef)) {
										final CargoAllocation pinnedCargoAllocation = (CargoAllocation) pinnedObject.eGet(cargoAllocationRef);
										if (pinnedCargoAllocation != null) {
											// convert this cargo's wiring of slot allocations to a string
											result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
										}
									} else if (pinnedObject.eIsSet(openSlotAllocationRef)) {
										final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) pinnedObject.eGet(openSlotAllocationRef);
										if (openSlotAllocation != null) {
											if (openSlotAllocation.getSlot() instanceof LoadSlot) {
												result = "Long";
											} else {
												result = "Short";
											}
										}
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

	public IFormatter generatePermutationColumnFormatter(final EStructuralFeature cargoAllocationRef) {
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
								} else {
									different = true;
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
								} else {
									different = true;
								}

							}
						}
						if (!different) {
							return "";
						}

						final CargoAllocation thisCargoAllocation = eObjectAsCargoAllocation.getSecond();

						final Set<Slot> buysSet = relatedSlotAllocations.getRelatedSetFor(thisCargoAllocation, true);
						final Set<Slot> sellsSet = relatedSlotAllocations.getRelatedSetFor(thisCargoAllocation, false);

						if (buysSet.isEmpty() && sellsSet.isEmpty()) {
							return "";
						}
						final Set<String> buysStringsSet = slotToStringsSet(buysSet);
						final Set<String> sellsStringsSet = slotToStringsSet(sellsSet);
						final String buysStr = setToString(buysStringsSet);
						final String sellsStr = setToString(sellsStringsSet);

						return String.format("Rewire %d x %d; Buys %s, Sells %s", buysStringsSet.size(), sellsStringsSet.size(), buysStr, sellsStr);
					} else if (eObj.eIsSet(openSlotAllocationRef)) {
						final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) eObj.eGet(openSlotAllocationRef);
						if (openSlotAllocation != null) {
							final Set<Slot> buysSet = relatedSlotAllocations.getRelatedSetFor(openSlotAllocation, true);
							final Set<Slot> sellsSet = relatedSlotAllocations.getRelatedSetFor(openSlotAllocation, false);

							if (buysSet.isEmpty() && sellsSet.isEmpty()) {
								return "";
							}

							final Set<String> buysStringsSet = slotToStringsSet(buysSet);
							final Set<String> sellsStringsSet = slotToStringsSet(sellsSet);
							final String buysStr = setToString(buysStringsSet);
							final String sellsStr = setToString(sellsStringsSet);

							return String.format("Rewire %d x %d; Buys %s, Sells %s", buysStringsSet.size(), sellsStringsSet.size(), buysStr, sellsStr);
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

			protected Set<String> slotToStringsSet(final Set<Slot> buysSet) {
				return Sets.newTreeSet(Iterables.transform(buysSet, new SlotToStringFunction()));
			}

			protected String setToString(final Set<String> stringsSet) {
				return "[ " + Joiner.on(", ").skipNulls().join(stringsSet) + " ]";
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

	// //// P&L Columns

	private final List<String> entityColumnNames = new ArrayList<String>();

	public EmfBlockColumnFactory getEmptyPNLColumnBlockFactory() {
		return new EmfBlockColumnFactory() {

			@Override
			public ColumnHandler addColumn(final EMFReportView theReport) {
				ColumnBlock block = theReport.getBlockManager().getBlockByID(COLUMN_BLOCK_PNL);
				if (block == null) {
					block = theReport.getBlockManager().createBlock(COLUMN_BLOCK_PNL, "[P&L]", ColumnType.NORMAL);
				}
				block.setPlaceholder(true);
				return null;
			}

		};
	}

	public ITreeContentProvider createPNLColumnssContentProvider(final ITreeContentProvider superProvider) {

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

						// TODO: Need to be able to register a listener for columns to react to this stuff.

						// Clear existing entity columns
						for (final String s : entityColumnNames) {
							report.removeColumn(s);
						}

						entityColumnNames.clear();
						if (newInput instanceof IScenarioViewerSynchronizerOutput) {
							final IScenarioViewerSynchronizerOutput synchronizerOutput = (IScenarioViewerSynchronizerOutput) newInput;
							final Collection<LNGScenarioModel> rootObjects = synchronizerOutput.getLNGScenarioModels();

							for (final LNGScenarioModel rootObject : rootObjects) {

								final CommercialModel commercialModel = rootObject.getCommercialModel();
								if (commercialModel != null) {
									for (final BaseLegalEntity e : commercialModel.getEntities()) {
										addPNLColumn(COLUMN_BLOCK_PNL, e.getName(), e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
										addPNLColumn(COLUMN_BLOCK_PNL, e.getName(), e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);
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

	public EmfBlockColumnFactory getTotalPNLColumnFactory(final String columnId, @Nullable final EStructuralFeature bookContainmentFeature) {
		return new EmfBlockColumnFactory() {
			@Override
			public ColumnHandler addColumn(final EMFReportView theReport) {
				final String book = bookContainmentFeature == null ? "Total" : (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK ? "Shipping" : "Trading");
				final String title = String.format("P&L (%s)", book);

				final ColumnBlock block = report.createBlock(columnId, title, ColumnType.NORMAL);
				return theReport.createColumn(block, title, new IntegerFormatter() {
					@Override
					public Integer getIntValue(final Object object) {
						ProfitAndLossContainer container = null;

						if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut
								|| object instanceof OpenSlotAllocation) {
							container = (ProfitAndLossContainer) object;
						}
						if (object instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) object;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								container = slotVisit.getSlotAllocation().getCargoAllocation();
							}
						}

						return getEntityPNLEntry(container, null, bookContainmentFeature);
					}
				}, targetObjectRef);
			}
		};

	}

	private ColumnHandler addPNLColumn(final String blockId, final String entityLabel, final String entityKey, final EStructuralFeature bookContainmentFeature) {
		final String book = bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK ? "Shipping" : "Trading";
		final String title = String.format("P&L (%s - %s)", entityLabel, book);

		// HACK: don't the label to the entity column names if the column is for total group P&L
		if (entityKey != null) {
			if (entityColumnNames.contains(title)) {
				return null;
			}
			entityColumnNames.add(title);
		}

		return report.createColumn(report.getBlockManager().getBlockByID(COLUMN_BLOCK_PNL), title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				ProfitAndLossContainer container = null;

				if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut
						|| object instanceof OpenSlotAllocation) {
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

	/**
	 * Returns the P&L. If entity is null, then the P&L for the given book across all entities. If book is null, assume trading book.
	 * 
	 * @param container
	 * @param entity
	 * @param bookContainmentFeature
	 * @return
	 */
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
		return null;
	}

}
