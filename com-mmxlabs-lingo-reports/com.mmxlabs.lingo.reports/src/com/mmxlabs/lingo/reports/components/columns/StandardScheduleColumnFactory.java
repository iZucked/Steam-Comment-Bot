package com.mmxlabs.lingo.reports.components.columns;

import java.text.DateFormat;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.components.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.components.EMFReportView.SimpleEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.CalendarFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class StandardScheduleColumnFactory implements IScheduleColumnFactory {
	private static final String CARGO_REPORT_TYPE_ID = "CARGO_REPORT_TYPE_ID";

	protected final IFormatter containingScheduleFormatter = new BaseFormatter() {
		@Override
		public String format(final Object object) {
			return "SCHEDULE";// view.synchronizerOutput.getScenarioInstance(object).getName();
		}

	};
	protected final IFormatter objectFormatter = new BaseFormatter();

	protected final IFormatter calendarFormatter = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), true);
	protected final IFormatter calendarFormatterNoTZ = new CalendarFormatter(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), false);

	protected final IFormatter datePartFormatter = new CalendarFormatter(DateFormat.getDateInstance(DateFormat.SHORT), false);
	protected final IFormatter timePartFormatter = new CalendarFormatter(DateFormat.getTimeInstance(DateFormat.SHORT), false);

	protected final IntegerFormatter integerFormatter = new IntegerFormatter();

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager view, final ScheduleBasedReportBuilder builder) {

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
			view.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Scenario", null, ColumnType.MULTIPLE, containingScheduleFormatter);
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.id":
			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "ID", null, ColumnType.NORMAL, objectFormatter, cargoAllocationRef, s.getCargoAllocation__GetName()));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.schedule.l-id":
			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "ID", null, ColumnType.NORMAL, objectFormatter, nameObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.d-id":
			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "D-ID", null, ColumnType.NORMAL, objectFormatter, name2ObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.cargotype":
			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Cargo Type", "TEST", ColumnType.NORMAL, objectFormatter, cargoAllocationRef,
					s.getCargoAllocation_InputCargo(), c.getCargo__GetCargoType()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_total":
			
			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "P&L", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					if (object instanceof EObject) {
						final EObject eObj = (EObject) object;

						final Object featureObj = eObj.eGet(cargoAllocationRef);

						if (featureObj instanceof CargoAllocation) {

							final CargoAllocation cargoAllocation = (CargoAllocation) featureObj;
							final GroupProfitAndLoss dataWithKey = cargoAllocation.getGroupProfitAndLoss();
							if (dataWithKey != null) {
								return (int) dataWithKey.getProfitAndLoss();
							}
						}
					}

					return null;
				}
			}));
			
			
			
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.vessel":

			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Vessel", null, ColumnType.NORMAL, objectFormatter, cargoAllocationRef,
					s.getCargoAllocation_Sequence(), SchedulePackage.eINSTANCE.getSequence__GetName()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loaddate":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Load Date", null, ColumnType.NORMAL, datePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart()));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loadtime":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Load Time", null, ColumnType.NORMAL, timePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargedate":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Discharge Date", null, ColumnType.NORMAL, datePartFormatter, dischargeAllocationRef, s.getSlotAllocation__GetLocalEnd()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargetime":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Discharge Time", null, ColumnType.NORMAL, timePartFormatter, dischargeAllocationRef, s.getSlotAllocation__GetLocalEnd()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyprice":

			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Buy Price", null, ColumnType.NORMAL, objectFormatter, loadAllocationRef, s.getSlotAllocation_Price()));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellprice":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sell Price", null, ColumnType.NORMAL, objectFormatter, dischargeAllocationRef, s.getSlotAllocation_Price()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.purchasecontract":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Purchase Contract", null, ColumnType.NORMAL, objectFormatter, loadAllocationRef, s.getSlotAllocation__GetContract(), name));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.salescontract":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sales Contract", null, ColumnType.NORMAL, objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetContract(), name));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyport":

			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Buy Port", null, ColumnType.NORMAL, objectFormatter, loadAllocationRef,
					s.getSlotAllocation__GetPort(), name));

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellport":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sell Port", null, ColumnType.NORMAL, objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetPort(), name));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyvolume_m3":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Buy Volume", null, ColumnType.NORMAL, integerFormatter, loadAllocationRef, s.getSlotAllocation_VolumeTransferred()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellvolume_m3":

			view.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sell Volume", null, ColumnType.NORMAL, integerFormatter, dischargeAllocationRef, s.getSlotAllocation_VolumeTransferred()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyvolume_mmbtu":
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellvolume_mmbtu":
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ladencost":

			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return calculateLegCost(object, cargoAllocationRef, loadAllocationRef);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballastcost":

			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return calculateLegCost(object, cargoAllocationRef, dischargeAllocationRef);
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.totalcost":

			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Total Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
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

		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_trading":
			builder.addPNLColumn(columnID, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_shipping":
			builder.addPNLColumn(columnID, CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);
			break;

		case "com.mmxlabs.lingo.reports.components.columns.schedule.type":
			view.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Type", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String format(final Object object) {
					if (object instanceof OpenSlotAllocation) {
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
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevvessel":
			view.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. Vessel", null, ColumnType.DIFF, builder.generatePreviousVesselAssignmentColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevwiring":
			view.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Prev. wiring", null, ColumnType.DIFF, builder.generatePreviousWiringColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation":
			view.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Permutation", null, ColumnType.DIFF, builder.generatePermutationColumnFormatter(cargoAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_group":
			view.registerColumn(CARGO_REPORT_TYPE_ID, builder.getEmptyPNLColumnBlockFactory());
			break;
		}
	}

	private Integer calculateLegCost(final Object object, EStructuralFeature cargoAllocationRef, EStructuralFeature allocationRef) {
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
