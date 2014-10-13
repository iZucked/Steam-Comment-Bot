/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import static com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView.SimpleEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.IFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.PriceFormatter;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class StandardScheduleColumnFactory implements IScheduleColumnFactory {

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager columnManager, final ScheduleBasedReportBuilder builder) {

		final CargoPackage c = CargoPackage.eINSTANCE;
		final SchedulePackage s = SchedulePackage.eINSTANCE;
		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		final EStructuralFeature nameObjectRef = builder.getNameObjectRef();
		final EStructuralFeature name2ObjectRef = builder.getName2ObjectRef();
		final EStructuralFeature targetObjectRef = builder.getTargetObjectRef();
		final EStructuralFeature cargoAllocationRef = builder.getCargoAllocationRef();
		final EStructuralFeature loadAllocationRef = builder.getLoadAllocationRef();
		final EStructuralFeature dischargeAllocationRef = builder.getDischargeAllocationRef();
		final EStructuralFeature openSlotAllocationRef = builder.getOpenSlotAllocationRef();

		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.schedule.schedule":
			final IFormatter containingScheduleFormatter = new BaseFormatter() {
				@Override
				public String format(final Object object) {
					return builder.getReport().getSynchronizerOutput().getScenarioInstance(object).getName();
				}

			};
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Scenario", "The scenario name. Only shown when multiple scenarios are selected", ColumnType.MULTIPLE,
					containingScheduleFormatter);
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
					new SimpleEmfBlockColumnFactory(columnID, "Cargo Type", "TEST", ColumnType.NORMAL, Formatters.objectFormatter, cargoAllocationRef, s.getCargoAllocation_InputCargo(), c
							.getCargo__GetCargoType()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.vessel":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Vessel", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String format(final Object object) {

					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						final Sequence sequence = cargoAllocation.getSequence();
						if (sequence != null) {
							if (!sequence.isFleetVessel()) {
								switch (cargoAllocation.getInputCargo().getCargoType()) {
								case DES:
									for (final Slot slot : cargoAllocation.getInputCargo().getSortedSlots()) {
										if (slot instanceof LoadSlot) {
											LoadSlot loadSlot = (LoadSlot) slot;
											if (loadSlot.isDESPurchase()) {
												final AVesselSet<? extends Vessel> assignment = slot.getAssignment();
												if (assignment != null) {
													return assignment.getName();
												}
												break;
											}
										}
									}
								case FOB:
									for (final Slot slot : cargoAllocation.getInputCargo().getSortedSlots()) {
										if (slot instanceof DischargeSlot) {
											final DischargeSlot dischargeSlot = (DischargeSlot) slot;
											if (dischargeSlot.isFOBSale()) {
												final AVesselSet<? extends Vessel> assignment = slot.getAssignment();
												if (assignment != null) {
													return assignment.getName();
												}
												break;
											}
										}
									}
								default:
									break;
								}
							}
							return sequence.getName();
						}
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

//					if (object instanceof CargoAllocation) {
//						final CargoAllocation cargoAllocation = (CargoAllocation) object;
//						final Sequence sequence = cargoAllocation.getSequence();
//						if (sequence != null) {
//							return sequence.getName();
//						}
//					} else if (object instanceof Event) {
//						final Event event = (Event) object;
//						final Sequence sequence = event.getSequence();
//						if (sequence != null) {
//							return sequence.getName();
//						}
//					}
					return format(object);
				}

			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loaddate":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Load Date", null, ColumnType.NORMAL, Formatters.datePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart()));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loadtime":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Load Time", null, ColumnType.NORMAL, Formatters.timePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargedate":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Discharge Date", null, ColumnType.NORMAL, Formatters.datePartFormatter,
					dischargeAllocationRef, s.getSlotAllocation__GetLocalEnd()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargetime":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Discharge Time", null, ColumnType.NORMAL, Formatters.timePartFormatter,
					dischargeAllocationRef, s.getSlotAllocation__GetLocalEnd()));
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

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Purchase Contract", null, ColumnType.NORMAL, Formatters.objectFormatter, loadAllocationRef, s.getSlotAllocation__GetContract(), name));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.salescontract":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sales Contract", null, ColumnType.NORMAL, Formatters.objectFormatter, dischargeAllocationRef,
					s.getSlotAllocation__GetContract(), name));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyport":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Buy Port", null, ColumnType.NORMAL, Formatters.objectFormatter, loadAllocationRef, s.getSlotAllocation__GetPort(), name));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellport":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sell Port", null, ColumnType.NORMAL, Formatters.objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetPort(), name));
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
							final double cv = ((LoadSlot) slot).getSlotOrDelegatedCV();
							return (int) Math.round(cv * (double) slotAllocation.getVolumeTransferred());
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

						double cv = 0.0;
						final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
						if (cargoAllocation != null) {
							for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
								final Slot slot = sa.getSlot();
								if (slot instanceof LoadSlot) {
									cv = ((LoadSlot) slot).getSlotOrDelegatedCV();
									break;
								}
							}
						}
						return (int) Math.round(cv * (double) slotAllocation.getVolumeTransferred());
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
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_total":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "P&L", null, ColumnType.NORMAL, new IntegerFormatter() {
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

					if (container != null) {

						final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
						if (dataWithKey != null) {
							return (int) dataWithKey.getProfitAndLoss();
						}
					}

					return null;
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
				public String format(final Object object) {
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
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. Vessel", null, ColumnType.DIFF, builder.generatePreviousVesselAssignmentColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevwiring":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. discharge", null, ColumnType.DIFF, builder.generatePreviousWiringColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Permutation", null, ColumnType.DIFF, builder.generatePermutationColumnFormatter(cargoAllocationRef));
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
}
