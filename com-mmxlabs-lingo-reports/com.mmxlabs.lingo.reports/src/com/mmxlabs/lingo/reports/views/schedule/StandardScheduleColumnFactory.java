/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import static com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.ViewerCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.IRowSpanProvider;
import com.mmxlabs.lingo.reports.components.MultiObjectEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.components.SimpleEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.utils.CargoAllocationUtils;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.PriceFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class StandardScheduleColumnFactory implements IScheduleColumnFactory {

	private final class PNLDeltaFormatter extends IntegerFormatter implements IRowSpanProvider {
		@Override
		public Integer getIntValue(final Object object) {

			if (object instanceof Row) {
				final Row row = (Row) object;

				// In simplePinDiff mode, we can activate row spanning, thus we always show total P&L delta. Otherwise show p&l delta only on the non-reference rows.
				final Table tbl = row.getTable();
				final boolean simplePinDiff = tbl.getScenarios().size() == 2 && tbl.getPinnedScenario() != null;

				// Disabled, see comment above.
				if (!simplePinDiff && row.isReference()) {
					return null;
				}

				final CycleGroup group = row.getCycleGroup();
				if (group != null) {
					int delta = 0;
					for (final Row groupRow : group.getRows()) {
						final Integer pnl = getElementProfitAndLoss(groupRow.getTarget());
						if (pnl == null) {
							continue;
						}
						if (groupRow.isReference()) {
							delta -= pnl.intValue();
						} else {
							// Exclude rows from other scenarios. (disabled, see comment above)
							if (!simplePinDiff && groupRow.getSchedule() != row.getSchedule()) {
								continue;
							}
							delta += pnl.intValue();
						}
					}
					return delta;
				}
			}
			return null;
		}

		@Override
		public int getRowSpan(final ViewerCell cell, final Object object) {
			if (object instanceof Row) {
				final Row row = (Row) object;

				final Table tbl = row.getTable();
				if (tbl.getScenarios().size() == 2 && tbl.getPinnedScenario() != null) {
					final CycleGroup group = row.getCycleGroup();
					if (group != null) {
						// Navigate upwards to find the number of rows spanned so far for a given cycle group.
						// Note: This *only* works for displayed cells. Cells which have not been drawn yet (ViewerCell.BELOW) or are not visible (outside of current scroll viewport) will be retuned
						// as a null neighbour.
						{
							int count = 0;
							ViewerCell neighbour = cell.getNeighbor(ViewerCell.ABOVE, false);
							while (neighbour != null && neighbour.getElement() instanceof Row) {
								final Row neighbourRow = (Row) neighbour.getElement();
								if (neighbourRow.getCycleGroup() == group) {
									count++;
									neighbour = neighbour.getNeighbor(ViewerCell.ABOVE, false);
								} else {
									break;
								}
							}
							return count;
						}
					}
				}
			}
			return 0;
		}
	}

	private static final Logger log = LoggerFactory.getLogger(StandardScheduleColumnFactory.class);

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final ScheduleBasedReportBuilder builder) {

		final CargoPackage c = CargoPackage.eINSTANCE;
		final SchedulePackage s = SchedulePackage.eINSTANCE;
		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();
		//
		final EStructuralFeature nameObjectRef = ScheduleReportPackage.Literals.ROW__NAME;
		final EStructuralFeature name2ObjectRef = ScheduleReportPackage.Literals.ROW__NAME2;
		final EStructuralFeature targetObjectRef = ScheduleReportPackage.Literals.ROW__TARGET;
		final EStructuralFeature cargoAllocationRef = ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION;
		final EStructuralFeature loadAllocationRef = ScheduleReportPackage.Literals.ROW__LOAD_ALLOCATION;
		final EStructuralFeature dischargeAllocationRef = ScheduleReportPackage.Literals.ROW__DISCHARGE_ALLOCATION;

		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.schedule.schedule":
			// final IFormatter containingScheduleFormatter = new BaseFormatter() {
			// @Override
			// public String format(final Object object) {
			// return builder.getReport().getSynchronizerOutput().getScenarioInstance(object).getName();
			// }
			//
			// };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Scenario", "The scenario name. Only shown when multiple scenarios are selected",
					ColumnType.MULTIPLE, Formatters.objectFormatter, ScheduleReportPackage.Literals.ROW__SCENARIO, ScenarioServicePackage.eINSTANCE.getContainer_Name()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.id":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "ID", "The main ID for all including discharge slots", ColumnType.NORMAL,
					Formatters.objectFormatter, cargoAllocationRef, s.getCargoAllocation__GetName()));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.schedule.l-id":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "ID", "The main ID for all except discharge slots", ColumnType.NORMAL,
					Formatters.objectFormatter, nameObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.d-id":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "D-ID", "The discharge ID for discharge slots", ColumnType.NORMAL, Formatters.objectFormatter,
					name2ObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.cargotype":
			columnManager.registerColumn(
					CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Cargo Type", "", ColumnType.NORMAL, Formatters.objectFormatter, cargoAllocationRef, s.getCargoAllocation_InputCargo(), c
							.getCargo__GetCargoType()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.vessel":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Vessel", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {

					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						// final Sequence sequence = cargoAllocation.getSequence();
						// if (sequence != null) {
						// if (!sequence.isFleetVessel()) {
						final Cargo inputCargo = cargoAllocation.getInputCargo();
						if (inputCargo == null) {
							return null;
						}
						switch (inputCargo.getCargoType()) {
						case DES:
							for (final Slot slot : inputCargo.getSortedSlots()) {
								if (slot instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) slot;
									if (loadSlot.isDESPurchase()) {
										final Vessel assignment = slot.getNominatedVessel();
										if (assignment != null) {
											return assignment.getName();
										}
										break;
									}
								}
							}
						case FOB:
							for (final Slot slot : inputCargo.getSortedSlots()) {
								if (slot instanceof DischargeSlot) {
									final DischargeSlot dischargeSlot = (DischargeSlot) slot;
									if (dischargeSlot.isFOBSale()) {
										final Vessel assignment = slot.getNominatedVessel();
										if (assignment != null) {
											return assignment.getName();
										}
										break;
									}
								}
							}
						case FLEET:
							final VesselAssignmentType vesselAssignmentType = inputCargo.getVesselAssignmentType();

							if (vesselAssignmentType instanceof VesselAvailability) {
								final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
								final Vessel vessel = vesselAvailability.getVessel();
								if (vessel != null) {
									return vessel.getName();
								}
							} else if (vesselAssignmentType instanceof CharterInMarket) {
								final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
								final VesselClass vesselClass = charterInMarket.getVesselClass();
								if (vesselClass != null) {
									return vesselClass.getName();
								}
							}
							break;
						default:
							break;
						}
						return null;

					} else if (object instanceof Event) {
						final Event event = (Event) object;
						final Sequence sequence = event.getSequence();
						if (sequence != null) {
							return sequence.getName();
						}
					}

					return null;
				}

				@Override
				public Comparable<?> getComparable(final Object object) {

					// if (object instanceof CargoAllocation) {
					// final CargoAllocation cargoAllocation = (CargoAllocation) object;
					// final Sequence sequence = cargoAllocation.getSequence();
					// if (sequence != null) {
					// return sequence.getName();
					// }
					// } else if (object instanceof Event) {
					// final Event event = (Event) object;
					// final Sequence sequence = event.getSequence();
					// if (sequence != null) {
					// return sequence.getName();
					// }
					// }
					return render(object);
				}

			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loaddate": {
			final ETypedElement[][] paths = new ETypedElement[][] { { loadAllocationRef, s.getSlotAllocation__GetLocalStart() }, { targetObjectRef, s.getEvent__GetLocalStart() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Load/Start Date", null, ColumnType.NORMAL, Formatters.datePartFormatter, paths));
		}

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loadtime": {
			final ETypedElement[][] paths = new ETypedElement[][] { { loadAllocationRef, s.getSlotAllocation__GetLocalStart() }, { targetObjectRef, s.getEvent__GetLocalStart() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Load/Start Time", null, ColumnType.NORMAL, Formatters.timePartFormatter, paths));
		}

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargedate": {
			final ETypedElement[][] paths = new ETypedElement[][] { { dischargeAllocationRef, s.getSlotAllocation__GetLocalStart() }, { targetObjectRef, s.getEvent__GetLocalEnd() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Discharge/End Date", null, ColumnType.NORMAL, Formatters.datePartFormatter, paths));
		}
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargetime": {
			final ETypedElement[][] paths = new ETypedElement[][] { { dischargeAllocationRef, s.getSlotAllocation__GetLocalStart() }, { targetObjectRef, s.getEvent__GetLocalEnd() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Discharge/End Time", null, ColumnType.NORMAL, Formatters.timePartFormatter, paths));
		}

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyprice":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Buy Price", null, ColumnType.NORMAL, new PriceFormatter(false, 3), loadAllocationRef, s.getSlotAllocation_Price()));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellprice":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sell Price", null, ColumnType.NORMAL, new PriceFormatter(false, 3), dischargeAllocationRef, s.getSlotAllocation_Price()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.purchasecontract":

			columnManager.registerColumn(
					CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Purchase Contract", null, ColumnType.NORMAL, Formatters.objectFormatter, loadAllocationRef, s.getSlotAllocation_Slot(), c
							.getSlot_Contract(), name));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.salescontract":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sales Contract", null, ColumnType.NORMAL, Formatters.objectFormatter, dischargeAllocationRef,
					s.getSlotAllocation_Slot(), c.getSlot_Contract(), name));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyport": {
			final ETypedElement[][] paths = new ETypedElement[][] { { loadAllocationRef, s.getSlotAllocation_Slot(), c.getSlot_Port(), name }, { targetObjectRef, s.getEvent_Port(), name } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Buy/Start Port", null, ColumnType.NORMAL, Formatters.objectFormatter, paths));
		}
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellport": {
			final ETypedElement[][] paths = new ETypedElement[][] { { dischargeAllocationRef, s.getSlotAllocation_Slot(), c.getSlot_Port(), name },
					{ targetObjectRef, s.getVesselEventVisit_VesselEvent(), c.getCharterOutEvent_RelocateTo(), name } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Sell/End Port", null, ColumnType.NORMAL, Formatters.objectFormatter, paths));
		}
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyvolume_m3":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Buy Volume", null, ColumnType.NORMAL, Formatters.integerFormatter, loadAllocationRef, s.getSlotAllocation_VolumeTransferred()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellvolume_m3":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sell Volume", null, ColumnType.NORMAL, Formatters.integerFormatter, dischargeAllocationRef, s.getSlotAllocation_VolumeTransferred()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyvolume_mmbtu":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Buy Volume (mmBtu)", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof SlotAllocation) {
						final SlotAllocation slotAllocation = (SlotAllocation) object;

						final Slot slot = slotAllocation.getSlot();
						if (slot instanceof LoadSlot) {
							return slotAllocation.getEnergyTransferred();
						}
					}
					return null;

				}
			}, loadAllocationRef));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellvolume_mmbtu":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sell Volume (mmBtu)", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof SlotAllocation) {
						final SlotAllocation slotAllocation = (SlotAllocation) object;
						return slotAllocation.getEnergyTransferred();
					}
					return null;

				}
			}, dischargeAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ladencost":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return calculateLegCost(object, cargoAllocationRef, loadAllocationRef);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballastcost":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return calculateLegCost(object, cargoAllocationRef, dischargeAllocationRef);
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.totalcost":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Total Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof CargoAllocation) {
						final CargoAllocation allocation = (CargoAllocation) object;

						int total = 0;
						for (final Event event : allocation.getEvents()) {

							if (event instanceof SlotVisit) {
								final SlotVisit slotVisit = (SlotVisit) event;
								total += slotVisit.getFuelCost();
								total += slotVisit.getCharterCost();
								total += slotVisit.getPortCost();
							} else if (event instanceof Journey) {
								final Journey journey = (Journey) event;
								total += journey.getFuelCost();
								total += journey.getCharterCost();
								total += journey.getToll();
							} else if (event instanceof Idle) {
								final Idle idle = (Idle) event;
								total += idle.getFuelCost();
								total += idle.getCharterCost();
							}
						}
						return total;
					}
					return null;

				}
			}, cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_additional":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Addn. P&L", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					int addnPNL = 0;

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

					if (container != null) {

						final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
						if (dataWithKey != null) {
							for (final GeneralPNLDetails generalPNLDetails : container.getGeneralPNLDetails()) {
								if (generalPNLDetails instanceof SlotPNLDetails) {
									final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
									for (GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
										if (details instanceof BasicSlotPNLDetails) {
											addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
										}
									}
								}
							}
						}
					}

					return addnPNL;
				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_total":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "P&L", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return getElementProfitAndLoss(object);

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_trading":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, builder.getTotalPNLColumnFactory(columnID, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_shipping":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, builder.getTotalPNLColumnFactory(columnID, CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.schedule.type":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Type", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					if (object instanceof OpenSlotAllocation) {
						final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) object;
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
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevvessel":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. Vessel", null, ColumnType.DIFF, generatePreviousVesselAssignmentColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevwiring":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. discharge", null, ColumnType.DIFF, generatePreviousWiringColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Permutation", null, ColumnType.DIFF, generatePermutationColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation_group":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Permutation Group", null, ColumnType.DIFF, generatePermutationGroupColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation_group_pnldelta":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "P&L Delta", null, ColumnType.DIFF, generatePermutationGroupPNLDeltaColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_changestring":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Main Change", null, ColumnType.DIFF, generateChangeStringColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_group":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, builder.getEmptyPNLColumnBlockFactory());
			break;
		}
	}

	private Integer calculateLegCost(final Object object, final EStructuralFeature cargoAllocationRef, final EStructuralFeature allocationRef) {
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			final CargoAllocation cargoAllocation = (CargoAllocation) eObject.eGet(cargoAllocationRef);
			final SlotAllocation allocation = (SlotAllocation) eObject.eGet(allocationRef);
			if (allocation != null && cargoAllocation != null) {

				boolean collecting = false;
				int total = 0;
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						if (allocation.getSlotVisit() == event) {
							collecting = true;
						} else {
							if (collecting) {
								// Finished!
								break;
							}
						}
						if (collecting) {
							total += slotVisit.getFuelCost();
							total += slotVisit.getCharterCost();
							total += slotVisit.getPortCost();
						}

					} else if (event instanceof Journey) {
						final Journey journey = (Journey) event;
						if (collecting) {
							total += journey.getFuelCost();
							total += journey.getCharterCost();
							total += journey.getToll();
						}
					} else if (event instanceof Idle) {
						final Idle idle = (Idle) event;
						if (collecting) {
							total += idle.getFuelCost();
							total += idle.getCharterCost();
						}
					}
				}

				return total;
			}

		}
		return null;
	}

	public ICellRenderer generateChangeStringColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {

			@Override
			public String render(final Object obj) {

				if (obj instanceof Row) {
					final Row row = (Row) obj;
					if (!row.isReference()) {
						final Row referenceRow = row.getReferenceRow();
						CargoAllocation ca = row.getCargoAllocation();
						if (referenceRow != null) {

							CargoAllocation ref = referenceRow.getCargoAllocation();
							if (ca != null && ref != null) {

								final String elementString = CargoAllocationUtils.getWiringAsString(ca);
								final String referenceString = CargoAllocationUtils.getWiringAsString(ref);

								if (elementString.equals(referenceString)) {

									if (!ca.getSequence().getName().equals(ref.getSequence().getName())) {
										return String.format("Vessel: %s -> %s", ref.getSequence().getName(), ca.getSequence().getName());
									}

									final int rowDuration = getEventGroupingDuration(ca);
									final int referenceDuration = getEventGroupingDuration(ref);

									if (rowDuration > referenceDuration) {
										return String.format("Shipping duration increased by %.1f days", ((double) (rowDuration - referenceDuration)) / 24.0);
									} else if (rowDuration < referenceDuration) {
										return String.format("Shipping duration decreased by %.1f days", ((double) (referenceDuration - rowDuration)) / 24.0);
									}

									return "";
								}
								if (row.getLoadAllocation().getSlot() instanceof SpotLoadSlot) {
									final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) row.getLoadAllocation().getSlot();
									return String.format("Buy spot '%s' to %s", spotLoadSlot.getMarket().getName(), CargoAllocationUtils.getSalesWiringAsString(ca));
								} else {
									return String.format("Redirect '%s' : %s -> %s", row.getLoadAllocation().getSlot().getName(), CargoAllocationUtils.getSalesWiringAsString(ref),
											CargoAllocationUtils.getSalesWiringAsString(ca));
								}
							}
							if (row.getOpenSlotAllocation() != null && referenceRow.getOpenSlotAllocation() == null) {
								return String.format("Cancelled '%s'", row.getOpenSlotAllocation().getSlot().getName());
							}
							if (row.getTarget() instanceof GeneratedCharterOut && referenceRow.getTarget() instanceof GeneratedCharterOut) {
								final GeneratedCharterOut rowTarget = (GeneratedCharterOut) row.getTarget();
								final GeneratedCharterOut referenceTarget = (GeneratedCharterOut) referenceRow.getTarget();
								if (rowTarget.getDuration() > referenceTarget.getDuration()) {
									return String.format("Charter duration increased by %.1f days", ((double) (rowTarget.getDuration() - referenceTarget.getDuration())) / 24.0);
								} else if (rowTarget.getDuration() < referenceTarget.getDuration()) {
									return String.format("Charter duration decreased by %.1f days", ((double) (referenceTarget.getDuration() - rowTarget.getDuration())) / 24.0);
								}
							} else if (referenceRow.getTarget() instanceof GeneratedCharterOut) {
								return "Removed charter out";
							}
							if (row.getTarget() instanceof EventGrouping && referenceRow.getTarget() instanceof EventGrouping) {
								final EventGrouping rowTarget = (EventGrouping) row.getTarget();
								final EventGrouping referenceTarget = (EventGrouping) referenceRow.getTarget();
								final int rowDuration = getEventGroupingDuration(rowTarget);
								final int referenceDuration = getEventGroupingDuration(referenceTarget);

								if (rowDuration > referenceDuration) {
									return String.format("Event duration increased by %.1f days", ((double) (rowDuration - referenceDuration)) / 24.0);
								} else if (rowDuration < referenceDuration) {
									return String.format("Event duration decreased by %.1f days", ((double) (referenceDuration - rowDuration)) / 24.0);
								}
							}
						} else {

							if (row.getOpenSlotAllocation() != null) {
								return String.format("Cancelled '%s'", row.getOpenSlotAllocation().getSlot().getName());
							}

							if (row.getLoadAllocation() != null && row.getLoadAllocation().getSlot() instanceof SpotLoadSlot) {
								final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) row.getLoadAllocation().getSlot();
								return String.format("Buy spot '%s' to %s", spotLoadSlot.getMarket().getName(), CargoAllocationUtils.getSalesWiringAsString(ca));
							}

							if (row.getTarget() instanceof GeneratedCharterOut) {
								return "Added charter out";
							}
						}
					} else {
						if (row.getReferringRows().isEmpty()) {
							// This is a reference row, nothing refers to it.

							// GCO in reference case, but not comparison case.
							if (row.getTarget() instanceof GeneratedCharterOut) {
								return "Removed charter out";
							}
						}
					}
				}
				return "";
			}
		};
	}

	protected int getEventGroupingDuration(final EventGrouping eventGrouping) {
		int duration = 0;
		for (final Event event : eventGrouping.getEvents()) {
			duration += event.getDuration();
		}

		return duration;
	}

	public ICellRenderer generatePermutationColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String render(final Object obj) {

				if (obj instanceof Row) {
					final Row row = (Row) obj;

					final CycleGroup group = row.getCycleGroup();
					if (group != null) {
						return group.getDescription();

					}
				}
				return "";
			}
		};
	}

	public ICellRenderer generatePermutationGroupColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String render(final Object obj) {

				if (obj instanceof Row) {
					final Row row = (Row) obj;

					final CycleGroup group = row.getCycleGroup();
					if (group != null && group.isSetIndex()) {
						return Integer.toString(group.getIndex());

					}
				}
				return "";
			}

			@Override
			public Comparable getComparable(final Object obj) {
				if (obj instanceof Row) {
					final Row row = (Row) obj;

					final CycleGroup group = row.getCycleGroup();
					if (group != null && group.isSetIndex()) {
						return group.getIndex();

					}
				}
				return Integer.MAX_VALUE;
			}
		};
	}

	public ICellRenderer generatePermutationGroupPNLDeltaColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new PNLDeltaFormatter();
	}

	/**
	 * Generate a new formatter for the previous-vessel-assignment column
	 * 
	 * Used in pin/diff mode.
	 * 
	 * @param cargoAllocationRef
	 * @return
	 */
	public ICellRenderer generatePreviousVesselAssignmentColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String render(final Object obj) {

				final Row row = (Row) obj;
				if (row.getCargoAllocation() == null) {
					return null;
				}

				final String currentAssignment = CargoAllocationUtils.getVesselAssignmentName(row.getCargoAllocation());

				String result = "";

				final Row referenceRow = row.getReferenceRow();
				if (referenceRow != null) {
					try {
						final CargoAllocation ca = (CargoAllocation) referenceRow.eGet(cargoAllocationRef);
						result = CargoAllocationUtils.getVesselAssignmentName(ca);
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
	public ICellRenderer generatePreviousWiringColumnFormatter(final EStructuralFeature cargoAllocationRef) {
		return new BaseFormatter() {
			@Override
			public String render(final Object obj) {

				if (obj instanceof Row) {
					final Row row = (Row) obj;
					if (row.getCargoAllocation() == null) {
						return null;
					}

					final Row referenceRow = row.getReferenceRow();
					if (referenceRow != null) {
						//
						if (row.getCargoAllocation() != null) {
							final String currentWiring = CargoAllocationUtils.getSalesWiringAsString(row.getCargoAllocation());
							//
							String result = "";
							// // for objects not coming from the pinned scenario,
							// // return the pinned counterpart's wiring to display as the previous wiring
							try {
								final CargoAllocation pinnedCargoAllocation = referenceRow.getCargoAllocation();
								if (pinnedCargoAllocation != null) {
									// convert this cargo's wiring of slot allocations to a string
									result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
								} else if (referenceRow.getOpenSlotAllocation() != null) {
									final OpenSlotAllocation openSlotAllocation = referenceRow.getOpenSlotAllocation();
									if (openSlotAllocation != null) {
										if (openSlotAllocation.getSlot() instanceof LoadSlot) {
											result = "Long";
										} else {
											result = "Short";
										}
									}
								}
							} catch (final Exception e) {
								log.warn("Error formatting previous wiring", e);
							}

							// Do not display if same
							if (currentWiring.equals(result)) {
								return "";
							}

							return result;
						} else if (row.getOpenSlotAllocation() != null) {
							final OpenSlotAllocation openSlotAllocation = row.getOpenSlotAllocation();
							if (openSlotAllocation != null) {

								if (referenceRow.getCargoAllocation() != null) {
									final CargoAllocation pinnedCargoAllocation = referenceRow.getCargoAllocation();
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

	private Integer getElementProfitAndLoss(final Object object) {
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

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				return (int) dataWithKey.getProfitAndLoss();
			}
		}
		return null;
	}

}
