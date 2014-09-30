/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

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
public class PortRotationBasedReportBuilder {
	public static final String PORT_ROTATION_REPORT_TYPE_ID = "PORT_ROTATION_REPORT_TYPE_ID";
	// public static final String COLUMN_BLOCK_PNL = "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_group";

	private static final Logger log = LoggerFactory.getLogger(PortRotationBasedReportBuilder.class);

	// public static final String ROW_FILTER_LONG_CARGOES = "Longs";
	// public static final String ROW_FILTER_SHORT_CARGOES = "Shorts";
	// public static final String ROW_FILTER_VESSEL_START_ROW = "Start ballast legs";
	// public static final String ROW_FILTER_VESSEL_EVENT_ROW = "Vessel Events";
	// public static final String ROW_FILTER_CHARTER_OUT_ROW = "Charter Outs (Virt)";
	// public static final String ROW_FILTER_CARGO_ROW = "Cargoes";
	//
	// /** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
	// public static final String[] ROW_FILTER_ALL = new String[] { ROW_FILTER_CARGO_ROW, ROW_FILTER_LONG_CARGOES, ROW_FILTER_SHORT_CARGOES, ROW_FILTER_VESSEL_EVENT_ROW, ROW_FILTER_CHARTER_OUT_ROW,
	// ROW_FILTER_VESSEL_START_ROW };
	//
	// public static final String DIFF_FILTER_PINNDED_SCENARIO = "Pinned Scenario";
	// public static final String DIFF_FILTER_VESSEL_CHANGES = "Vessel Changes";
	//
	// /** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
	// public static final String[] DIFF_FILTER_ALL = new String[] { DIFF_FILTER_PINNDED_SCENARIO, DIFF_FILTER_VESSEL_CHANGES };

	// private static final String ROW_FILTER_MEMENTO = "ROW_FILTER";
	// private static final String DIFF_FILTER_MEMENTO = "DIFF_FILTER";

	// private final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
	//
	// /**
	// * Guava {@link Function} to convert a Slot to a String based on it's name;
	// *
	// */
	// private static class SlotToStringFunction implements Function<Slot, String> {
	//
	// @Override
	// public String apply(final Slot o) {
	// return o.getName();
	// }
	//
	// @Override
	// public String toString() {
	// return "toString";
	// }
	//
	// }

	// private final RelatedSlotAllocations relatedSlotAllocations = new RelatedSlotAllocations();

	// private PinDiffModeColumnManager pinDiffModeHelper;
	private EMFReportView report;

	//
	// // Dynamic table data model fields
	// private final EPackage tableDataModel;
	// private final EStructuralFeature nameObjectRef;
	// private final EStructuralFeature name2ObjectRef;
	// private final EStructuralFeature targetObjectRef;
	// private final EStructuralFeature cargoAllocationRef;
	// private final EStructuralFeature loadAllocationRef;
	// private final EStructuralFeature dischargeAllocationRef;
	// private final EStructuralFeature openSlotAllocationRef;

	// private final Set<String> rowFilterInfo = new HashSet<>();
	// private final Set<String> diffFilterInfo = new HashSet<>();

	// @Inject(optional = true)
	// private Iterable<ICustomRelatedSlotHandlerExtension> customRelatedSlotHandlers;

	public PortRotationBasedReportBuilder() {

		// // Create table data model with the given references.
		// tableDataModel = GenericEMFTableDataModel.createEPackage("target", "cargo", "load", "discharge", "openslot");
		//
		// // Get the row class
		// final EClass rowClass = GenericEMFTableDataModel.getRowClass(tableDataModel);
		// // Add additional name attributes
		// nameObjectRef = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.Literals.ESTRING, "name");
		// name2ObjectRef = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.Literals.ESTRING, "name2");
		//
		// // Get the EStructuralFeature refs created above.
		// targetObjectRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "target");
		// cargoAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "cargo");
		// loadAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "load");
		// dischargeAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "discharge");
		// openSlotAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, "openslot");
	}

	public EMFReportView getReport() {
		return report;
	}

	public void setReport(final EMFReportView report) {
		this.report = report;
	}
	//
	// public void setPinDiffModeHelper(final PinDiffModeColumnManager pinDiffModeHelper) {
	// this.pinDiffModeHelper = pinDiffModeHelper;
	// }
	//
	// /**
	// * Replace the existing row filters with the following set.
	// *
	// * @param filters
	// */
	// public void setRowFilter(final String... filters) {
	// rowFilterInfo.clear();
	// for (final String filter : filters) {
	// rowFilterInfo.add(filter);
	// }
	// }
	//
	// public void addRowFilters(final String... filters) {
	// for (final String filter : filters) {
	// rowFilterInfo.add(filter);
	// }
	// }
	//
	// public void removeRowFilters(final String... filters) {
	// for (final String filter : filters) {
	// rowFilterInfo.remove(filter);
	// }
	// }
	//
	// /**
	// * Replace the existing row filters with the following set.
	// *
	// * @param filters
	// */
	// public void setDiffFilter(final String... filters) {
	// diffFilterInfo.clear();
	// for (final String filter : filters) {
	// diffFilterInfo.add(filter);
	// }
	// }
	//
	// public void addDiffFilters(final String... filters) {
	// for (final String filter : filters) {
	// diffFilterInfo.add(filter);
	// }
	// }
	//
	// public void removeDiffFilters(final String... filters) {
	// for (final String filter : filters) {
	// diffFilterInfo.remove(filter);
	// }
	// }
	//
	// public EPackage getTableDataModel() {
	// return tableDataModel;
	// }
	//
	// public EStructuralFeature getNameObjectRef() {
	// return nameObjectRef;
	// }
	//
	// public EStructuralFeature getName2ObjectRef() {
	// return name2ObjectRef;
	// }
	//
	// public EStructuralFeature getTargetObjectRef() {
	// return targetObjectRef;
	// }
	//
	// public EStructuralFeature getCargoAllocationRef() {
	// return cargoAllocationRef;
	// }
	//
	// public EStructuralFeature getLoadAllocationRef() {
	// return loadAllocationRef;
	// }
	//
	// public EStructuralFeature getDischargeAllocationRef() {
	// return dischargeAllocationRef;
	// }
	//
	// public EStructuralFeature getOpenSlotAllocationRef() {
	// return openSlotAllocationRef;
	// }

	// public IScenarioInstanceElementCollector getElementCollector() {
	// return new ScheduleElementCollector() {
	//
	// // private EObject dataModelInstance;
	//
	// @Override
	// public void beginCollecting() {
	// super.beginCollecting();
	// // relatedSlotAllocations.clear();
	// report.clearPinModeData();
	// // dataModelInstance = GenericEMFTableDataModel.createRootInstance(tableDataModel);
	// }
	//
	// @Override
	// protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {
	//
	// final List<EObject> nodes = generateNodes(dataModelInstance, schedule);
	//
	// report.collectPinModeElements(nodes, isPinned);
	//
	// return nodes;
	// }
	// };
	// }

	// public List<?> adaptSelectionFromWidget(final List<?> selection) {
	// final List<Object> adaptedSelection = new ArrayList<Object>(selection.size());
	// for (final Object obj : selection) {
	// if (obj instanceof EObject) {
	// adaptedSelection.add(((EObject) obj).eGet(targetObjectRef));
	// }
	// }
	//
	// return adaptedSelection;
	// }
	//
	// public void processInputs(final Object[] result) {
	// for (final Object row : result) {
	//
	// // Map our "Node" data to the CargoAllocation object
	// if (row instanceof EObject) {
	// final EObject eObj = (EObject) row;
	// if (eObj.eIsSet(targetObjectRef)) {
	//
	// final Object a = eObj.eGet(targetObjectRef);
	//
	// // map to events
	// if (a instanceof CargoAllocation) {
	// final CargoAllocation allocation = (CargoAllocation) a;
	//
	// final List<Object> equivalents = new LinkedList<Object>();
	// for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
	// equivalents.add(slotAllocation.getSlot());
	// equivalents.add(slotAllocation.getSlotVisit());
	// }
	// equivalents.addAll(allocation.getEvents());
	// equivalents.add(allocation.getInputCargo());
	// report.setInputEquivalents(row, equivalents);
	// } else if (a instanceof SlotVisit) {
	// final SlotVisit slotVisit = (SlotVisit) a;
	//
	// final CargoAllocation allocation = slotVisit.getSlotAllocation().getCargoAllocation();
	//
	// final List<Object> equivalents = new LinkedList<Object>();
	// for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
	// equivalents.add(slotAllocation.getSlot());
	// equivalents.add(slotAllocation.getSlotVisit());
	// }
	// equivalents.addAll(allocation.getEvents());
	// equivalents.add(allocation.getInputCargo());
	// report.setInputEquivalents(row, equivalents);
	// } else if (a instanceof VesselEventVisit) {
	// final VesselEventVisit vesselEventVisit = (VesselEventVisit) a;
	// report.setInputEquivalents(row, Lists.<Object> newArrayList(vesselEventVisit, vesselEventVisit.getVesselEvent()));
	// } else if (a instanceof StartEvent) {
	// final StartEvent startEvent = (StartEvent) a;
	// final VesselAvailability vesselAvailability = startEvent.getSequence().getVesselAvailability();
	// if (vesselAvailability != null) {
	// report.setInputEquivalents(row, Lists.<Object> newArrayList(startEvent, vesselAvailability.getVessel()));
	// }
	// } else if (a instanceof OpenSlotAllocation) {
	// final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
	// report.setInputEquivalents(row, Lists.<Object> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
	// }
	// }
	// }
	// }
	// }

	// /**
	// * Returns a key of some kind for the element
	// *
	// * @param element
	// * @return
	// */
	// public String getElementKey(EObject element) {
	//
	// if (element.eIsSet(cargoAllocationRef)) {
	// element = (EObject) element.eGet(cargoAllocationRef);
	// } else if (element.eIsSet(openSlotAllocationRef)) {
	// element = (EObject) element.eGet(openSlotAllocationRef);
	// } else if (element.eIsSet(targetObjectRef)) {
	// element = (EObject) element.eGet(targetObjectRef);
	// }
	//
	// if (element instanceof CargoAllocation) {
	// return ((CargoAllocation) element).getName();
	// } else if (element instanceof OpenSlotAllocation) {
	// return ((OpenSlotAllocation) element).getSlot().getName();
	// } else if (element instanceof Event) {
	// return ((Event) element).name();
	// }
	// if (element instanceof NamedObject) {
	// return ((NamedObject) element).getName();
	// }
	// return element.toString();
	// }
	//
	// private boolean showOpenSlot(final OpenSlotAllocation openSlotAllocation) {
	//
	// if (openSlotAllocation.getSlot() instanceof LoadSlot) {
	// return rowFilterInfo.contains(ROW_FILTER_LONG_CARGOES);
	// } else if (openSlotAllocation.getSlot() instanceof DischargeSlot) {
	// return rowFilterInfo.contains(ROW_FILTER_SHORT_CARGOES);
	// }
	// return false;
	// }
	//
	// private boolean showEvent(final Event event) {
	// if (event instanceof StartEvent) {
	// return rowFilterInfo.contains(ROW_FILTER_VESSEL_START_ROW);
	// } else if (event instanceof VesselEventVisit) {
	// return rowFilterInfo.contains(ROW_FILTER_VESSEL_EVENT_ROW);
	// } else if (event instanceof GeneratedCharterOut) {
	// return rowFilterInfo.contains(ROW_FILTER_CHARTER_OUT_ROW);
	// } else if (event instanceof SlotVisit) {
	// final SlotVisit slotVisit = (SlotVisit) event;
	// if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
	// return rowFilterInfo.contains(ROW_FILTER_CARGO_ROW);
	// }
	// }
	// return false;
	// }
	//
	// public List<EObject> generateNodes(final EObject dataModelInstance, final Schedule schedule) {
	//
	// final List<ICustomRelatedSlotHandler> relatedSlotHandlers = getCustomRelatedSlotHandlers();
	//
	// final List<EObject> interestingEvents = new LinkedList<EObject>();
	// for (final Sequence sequence : schedule.getSequences()) {
	// for (final Event event : sequence.getEvents()) {
	//
	// // Always consider cargoes for permutations, even if filtered out
	// if (event instanceof SlotVisit) {
	// final SlotVisit slotVisit = (SlotVisit) event;
	// final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
	//
	// // TODO: Only required for pin/diff mode really.
	// relatedSlotAllocations.updateRelatedSetsFor(cargoAllocation);
	//
	// for (final ICustomRelatedSlotHandler h : relatedSlotHandlers) {
	// h.addRelatedSlots(relatedSlotAllocations, schedule, cargoAllocation);
	// }
	// }
	//
	// if (showEvent(event)) {
	// interestingEvents.add(event);
	// }
	// }
	// }
	//
	// for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
	// // Always consider open positions for permutations, even if filtered out
	// for (final ICustomRelatedSlotHandler h : relatedSlotHandlers) {
	// h.addRelatedSlots(relatedSlotAllocations, schedule, openSlotAllocation);
	// }
	// if (showOpenSlot(openSlotAllocation)) {
	// interestingEvents.add(openSlotAllocation);
	// }
	// }
	//
	// return generateNodes(dataModelInstance, schedule, interestingEvents);
	// }

	// private List<ICustomRelatedSlotHandler> getCustomRelatedSlotHandlers() {
	// final List<ICustomRelatedSlotHandler> l = new LinkedList<ICustomRelatedSlotHandler>();
	// if (customRelatedSlotHandlers != null) {
	// for (final ICustomRelatedSlotHandlerExtension h : customRelatedSlotHandlers) {
	// l.add(h.getInstance());
	// }
	// }
	// return l;
	// }
	//
	// public List<EObject> generateNodes(final EObject dataModelInstance, final Schedule schedule, final List<EObject> interestingElements) {
	// final List<EObject> nodes = new ArrayList<EObject>(interestingElements.size());
	//
	// for (final Object element : interestingElements) {
	//
	// if (element instanceof SlotVisit) {
	// final SlotVisit slotVisit = (SlotVisit) element;
	// final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
	//
	// // Build up list of slots assigned to cargo, sorting into loads and discharges
	// final List<SlotAllocation> loadSlots = new ArrayList<SlotAllocation>();
	// final List<SlotAllocation> dischargeSlots = new ArrayList<SlotAllocation>();
	// for (final SlotAllocation slot : cargoAllocation.getSlotAllocations()) {
	// if (slot.getSlot() instanceof LoadSlot) {
	// loadSlots.add(slot);
	// } else if (slot.getSlot() instanceof DischargeSlot) {
	// dischargeSlots.add(slot);
	// } else {
	// // Assume some kind of discharge?
	// // dischargeSlots.add((Slot) slot);
	// }
	//
	// }
	//
	// final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);
	// // Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
	// for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {
	//
	// final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, cargoAllocationRef, cargoAllocation);
	// if (i < loadSlots.size()) {
	// final SlotAllocation slot = loadSlots.get(i);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, loadAllocationRef, slot);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, slot.getName());
	// }
	// if (i < dischargeSlots.size()) {
	// final SlotAllocation slot = dischargeSlots.get(i);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, dischargeAllocationRef, slot);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, name2ObjectRef, slot.getName());
	// }
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, targetObjectRef, cargoAllocation);
	// nodes.add(node);
	// }
	// } else if (element instanceof OpenSlotAllocation) {
	//
	// final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
	//
	// final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);
	// final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, targetObjectRef, openSlotAllocation);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, openSlotAllocationRef, openSlotAllocation);
	// final Slot slot = openSlotAllocation.getSlot();
	// if (slot == null) {
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, "??");
	// } else {
	// if (slot instanceof DischargeSlot) {
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, name2ObjectRef, slot.getName());
	// } else {
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, slot.getName());
	// }
	// }
	// nodes.add(node);
	//
	// } else if (element instanceof Event) {
	// final Event event = (Event) element;
	// final EObject group = GenericEMFTableDataModel.createGroup(tableDataModel, dataModelInstance);
	//
	// final EObject node = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, group);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, targetObjectRef, event);
	// GenericEMFTableDataModel.setRowValue(tableDataModel, node, nameObjectRef, event.name());
	// nodes.add(node);
	// }
	// }
	// return nodes;
	// }

	// public Set<String> getRowFilterInfo() {
	// return rowFilterInfo;
	// }
	//
	// public Set<String> getDiffFilterInfo() {
	// return diffFilterInfo;
	// }

	// public void saveToMemento(final String uniqueConfigKey, final IMemento memento) {
	// final IMemento rowsInfo = memento.createChild(uniqueConfigKey);
	// for (final String option : rowFilterInfo) {
	// final IMemento optionInfo = rowsInfo.createChild(ROW_FILTER_MEMENTO);
	// optionInfo.putTextData(option);
	// }
	// for (final String option : diffFilterInfo) {
	// final IMemento optionInfo = rowsInfo.createChild(DIFF_FILTER_MEMENTO);
	// optionInfo.putTextData(option);
	// }
	// }
	//
	// public void initFromMemento(final String uniqueConfigKey, final IMemento memento) {
	// final IMemento rowsInfo = memento.getChild(uniqueConfigKey);
	// if (rowsInfo != null) {
	// rowFilterInfo.clear();
	// for (final IMemento optionInfo : rowsInfo.getChildren(ROW_FILTER_MEMENTO)) {
	// rowFilterInfo.add(optionInfo.getTextData());
	// }
	// diffFilterInfo.clear();
	// for (final IMemento optionInfo : rowsInfo.getChildren(DIFF_FILTER_MEMENTO)) {
	// diffFilterInfo.add(optionInfo.getTextData());
	// }
	// } else {
	// rowFilterInfo.addAll(Arrays.asList(PortRotationBasedReportBuilder.ROW_FILTER_ALL));
	// diffFilterInfo.addAll(Arrays.asList(PortRotationBasedReportBuilder.DIFF_FILTER_ALL));
	// }
	// refreshDiffOptions();
	// }

}
