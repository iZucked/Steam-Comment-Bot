/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IMemento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnBlockManager;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandlerExtension;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

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

	public static final OptionInfo ROW_FILTER_LONG_CARGOES = new OptionInfo("ROW_FILTER_LONG_CARGOES", "Longs");
	public static final OptionInfo ROW_FILTER_SHORT_CARGOES = new OptionInfo("ROW_FILTER_SHORT_CARGOES", "Shorts");
	public static final OptionInfo ROW_FILTER_VESSEL_START_ROW = new OptionInfo("ROW_FILTER_VESSEL_START_ROW", "Start ballast legs");
	public static final OptionInfo ROW_FILTER_VESSEL_EVENT_ROW = new OptionInfo("ROW_FILTER_VESSEL_EVENT_ROW", "Vessel Events");
	public static final OptionInfo ROW_FILTER_CHARTER_OUT_ROW = new OptionInfo("ROW_FILTER_CHARTER_OUT_ROW", "Charter Outs (Virtual)");
	public static final OptionInfo ROW_FILTER_CARGO_ROW = new OptionInfo("ROW_FILTER_CARGO_ROW", "Cargoes");

	/** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
	public static final OptionInfo[] ROW_FILTER_ALL = new OptionInfo[] { ROW_FILTER_CARGO_ROW, ROW_FILTER_LONG_CARGOES, ROW_FILTER_SHORT_CARGOES, ROW_FILTER_VESSEL_EVENT_ROW,
			ROW_FILTER_CHARTER_OUT_ROW, ROW_FILTER_VESSEL_START_ROW };

	public static final OptionInfo DIFF_FILTER_PINNDED_SCENARIO = new OptionInfo("DIFF_FILTER_PINNDED_SCENARIO", "Show Pinned Scenario");
	public static final OptionInfo DIFF_FILTER_VESSEL_CHANGES = new OptionInfo("DIFF_FILTER_VESSEL_CHANGES", "Show Vessel Changes");

	/** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
	public static final OptionInfo[] DIFF_FILTER_ALL = new OptionInfo[] { DIFF_FILTER_PINNDED_SCENARIO, DIFF_FILTER_VESSEL_CHANGES };

	private static final String ROW_FILTER_MEMENTO = "ROW_FILTER";
	private static final String DIFF_FILTER_MEMENTO = "DIFF_FILTER";

	private final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
	private ScheduleTransformer transformer;

	private ColumnBlockManager blockManager;

	private final Set<String> rowFilterInfo = new HashSet<>();
	private final Set<String> diffFilterInfo = new HashSet<>();

	@Inject(optional = true)
	private Iterable<ICustomRelatedSlotHandlerExtension> customRelatedSlotHandlers;

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

	public List<?> adaptSelectionFromWidget(final List<?> selection) {
		final List<Object> adaptedSelection = new ArrayList<Object>(selection.size()  * 2);
		for (final Object obj : selection) {
			if (obj instanceof EObject) {
				adaptedSelection.add(((EObject) obj).eGet(ScheduleReportPackage.Literals.ROW__TARGET));
			}
			adaptedSelection.add(obj);
		}

		return adaptedSelection;
	}

	// /**
	// * Returns a key of some kind for the element
	// *
	// * @param element
	// * @return
	// */
	// public String getElementKey(EObject element) {
	//
	// if (element.eIsSet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION)) {
	// element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION);
	// } else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__OPEN_SLOT_ALLOCATION)) {
	// element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__OPEN_SLOT_ALLOCATION);
	// } else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__TARGET)) {
	// element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__TARGET);
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

	public boolean showOpenSlot(final OpenSlotAllocation openSlotAllocation) {

		if (openSlotAllocation.getSlot() instanceof LoadSlot) {
			return rowFilterInfo.contains(ROW_FILTER_LONG_CARGOES.id);
		} else if (openSlotAllocation.getSlot() instanceof DischargeSlot) {
			return rowFilterInfo.contains(ROW_FILTER_SHORT_CARGOES.id);
		}
		return false;
	}

	public boolean showEvent(final Event event) {
		if (event instanceof StartEvent) {
			return rowFilterInfo.contains(ROW_FILTER_VESSEL_START_ROW.id);
		} else if (event instanceof VesselEventVisit) {
			return rowFilterInfo.contains(ROW_FILTER_VESSEL_EVENT_ROW.id);
		} else if (event instanceof GeneratedCharterOut) {
			return rowFilterInfo.contains(ROW_FILTER_CHARTER_OUT_ROW.id);
		} else if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				return rowFilterInfo.contains(ROW_FILTER_CARGO_ROW.id);
			}
		}
		return false;
	}

	public List<ICustomRelatedSlotHandler> getCustomRelatedSlotHandlers() {
		final List<ICustomRelatedSlotHandler> l = new LinkedList<ICustomRelatedSlotHandler>();
		if (customRelatedSlotHandlers != null) {
			for (final ICustomRelatedSlotHandlerExtension h : customRelatedSlotHandlers) {
				l.add(h.getInstance());
			}
		}
		return l;
	}

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
			rowFilterInfo.addAll(OptionInfo.getIds(ScheduleBasedReportBuilder.ROW_FILTER_ALL));
			diffFilterInfo.addAll(OptionInfo.getIds(ScheduleBasedReportBuilder.DIFF_FILTER_ALL));
		}
		refreshDiffOptions();
	}

	// / Normal Columns

	// ////// Pin / Diff Columns

	public void refreshDiffOptions() {
		scheduleDiffUtils.setCheckAssignmentDifferences(diffFilterInfo.contains(DIFF_FILTER_VESSEL_CHANGES.id));
		// pinDiffModeHelper.setShowPinnedData(diffFilterInfo.contains(DIFF_FILTER_PINNDED_SCENARIO.id));
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
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_PNL);
				if (block == null) {
					block = blockManager.createBlock(COLUMN_BLOCK_PNL, "[P&L]", ColumnType.NORMAL);
				}
				block.setPlaceholder(true);
				return null;
			}

		};
	}

	public void refreshPNLColumns(List<LNGScenarioModel> rootObjects) {

		// Clear existing entity columns
		for (final String s : entityColumnNames) {
			blockManager.removeColumn(s);
		}

		entityColumnNames.clear();

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

	public EmfBlockColumnFactory getTotalPNLColumnFactory(final String columnId, @Nullable final EStructuralFeature bookContainmentFeature) {
		return new EmfBlockColumnFactory() {
			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				final String book = bookContainmentFeature == null ? "Total" : (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK ? "Shipping" : "Trading");
				final String title = String.format("P&L (%s)", book);

				final ColumnBlock block = blockManager.createBlock(columnId, title, ColumnType.NORMAL);
				return blockManager.createColumn(block, title, new IntegerFormatter() {
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
				}, ScheduleReportPackage.Literals.ROW__TARGET);
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

		return blockManager.createColumn(blockManager.getBlockByID(COLUMN_BLOCK_PNL), title, new IntegerFormatter() {
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
		}, ScheduleReportPackage.Literals.ROW__TARGET);
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

	public ScheduleDiffUtils getScheduleDiffUtils() {
		return scheduleDiffUtils;
	}

	public ColumnBlockManager getBlockManager() {
		return blockManager;
	}

	public void setBlockManager(ColumnBlockManager blockManager) {
		this.blockManager = blockManager;
	}
}
