/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import static com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.MultiObjectEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.components.SimpleEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.diff.utils.ScheduleCostUtils;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.PriceFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.MainChangeDescriptionFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.PNLDeltaFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.PermutationDescriptionFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.PermutationGroupFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.PreviousVesselFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.PreviousWiringFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.RowTypeFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.provider.PinnedScheduleFormatter;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class StandardScheduleColumnFactory implements IScheduleColumnFactory {

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

		// TODO: Dispose!
		Image pinImage = Activator.Implementation.getImageDescriptor("icons/full/obj16/PinnedRow.gif").createImage();
		
		
		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.schedule.schedule":
			PinnedScheduleFormatter formatter= new PinnedScheduleFormatter(pinImage);
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Scenario", "The scenario name. Only shown when multiple scenarios are selected",
					ColumnType.MULTIPLE,formatter, ScheduleReportPackage.Literals.ROW__SCENARIO, ScenarioServicePackage.eINSTANCE.getContainer_Name()));
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
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Vessel", null, ColumnType.NORMAL, new VesselAssignmentFormatter(), targetObjectRef));
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

					return ScheduleCostUtils.calculateLegCost(object, cargoAllocationRef, loadAllocationRef);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballastcost":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleCostUtils.calculateLegCost(object, cargoAllocationRef, dischargeAllocationRef);
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

					return PNLDeltaUtils.getElementProfitAndLoss(object);

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
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Type", null, ColumnType.NORMAL, new RowTypeFormatter(), targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevvessel":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. Vessel", null, ColumnType.DIFF, new PreviousVesselFormatter());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevwiring":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. discharge", null, ColumnType.DIFF, new PreviousWiringFormatter());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Permutation", null, ColumnType.DIFF, new PermutationDescriptionFormatter());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation_group":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Permutation Group", null, ColumnType.DIFF, new PermutationGroupFormatter());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation_group_pnldelta":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "P&L Delta", null, ColumnType.DIFF, new PNLDeltaFormatter());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_changestring":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Main Change", null, ColumnType.DIFF, new MainChangeDescriptionFormatter());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_group":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, builder.getEmptyPNLColumnBlockFactory());
			break;
		}
	}
}
