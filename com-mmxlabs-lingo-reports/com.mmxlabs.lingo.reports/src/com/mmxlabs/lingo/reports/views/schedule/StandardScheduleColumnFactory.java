/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import static com.mmxlabs.lingo.reports.views.schedule.ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.OptionalInt;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.PinnedScheduleFormatter;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.lingo.reports.views.formatters.PriceFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.RowTypeFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.CapacityUtils;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.TotalType;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.models.ui.tabular.columngeneration.MultiObjectEmfBlockColumnFactory;
import com.mmxlabs.models.ui.tabular.columngeneration.SimpleEmfBlockColumnFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

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

		final Image pinImage = Activator.getDefault().getImageRegistry().get(Activator.Implementation.IMAGE_PINNED_ROW);

		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.schedule.schedule":
			final PinnedScheduleFormatter formatter = new PinnedScheduleFormatter(pinImage);
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Scenario", "The scenario name. Only shown when multiple scenarios are selected",
					ColumnType.MULTIPLE, formatter, ScheduleReportPackage.Literals.ROW__SCENARIO_NAME));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.id":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "ID", "The main ID for all including discharge slots", ColumnType.NORMAL, Formatters.objectFormatter, nameObjectRef));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.schedule.l-id":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "L-ID", "The main ID for all except discharge slots", ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {

					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
						if (openSlotAllocation != null) {
							final Slot slot = openSlotAllocation.getSlot();
							if (slot != null) {
								return slot.getName();
							}
						} else {
							return row.getName();
						}
					}
					return "";
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.d-id":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "D-ID", "The discharge ID for discharge slots", ColumnType.NORMAL, Formatters.objectFormatter, name2ObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.cargotype":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Cargo Type", "", ColumnType.NORMAL, Formatters.objectFormatter, cargoAllocationRef, s.getCargoAllocation_CargoType()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.vessel":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Vessel", null, ColumnType.NORMAL, new VesselAssignmentFormatter(), targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loadwindow": {

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Load/Start Window", "Load or vessel event window start date", ColumnType.NORMAL, new BaseFormatter() {

						@Override
						public Comparable getComparable(Object object) {
							if (object instanceof Row) {
								final Row row = (Row) object;
								final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
								if (openSlotAllocation != null) {
									final Slot slot = openSlotAllocation.getSlot();
									if (slot != null) {
										return slot.getWindowStart();
									}
								}
								final SlotAllocation slotAllocation = row.getLoadAllocation();
								if (slotAllocation != null) {
									final Slot slot = slotAllocation.getSlot();
									if (slot instanceof LoadSlot) {
										return slot.getWindowStart();
									}
								}
								if (row.getTarget() instanceof VesselEventVisit) {
									final VesselEventVisit visit = (VesselEventVisit) row.getTarget();
									final VesselEvent event = visit.getVesselEvent();
									if (event != null) {
										return event.getStartAfter().toLocalDate();

									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							if (object instanceof Row) {
								final Row row = (Row) object;
								final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
								if (openSlotAllocation != null) {
									final Slot slot = openSlotAllocation.getSlot();
									if (slot != null) {
										return Formatters.asLocalDateFormatter.render(slot.getWindowStart());
									}
								}
								final SlotAllocation slotAllocation = row.getLoadAllocation();
								if (slotAllocation != null) {
									final Slot slot = slotAllocation.getSlot();
									if (slot instanceof LoadSlot) {
										return Formatters.asLocalDateFormatter.render(slot.getWindowStart());
									}
								}
								if (row.getTarget() instanceof VesselEventVisit) {
									final VesselEventVisit visit = (VesselEventVisit) row.getTarget();
									final VesselEvent event = visit.getVesselEvent();
									if (event != null) {
										return Formatters.asLocalDateFormatter.render(event.getStartAfter());

									}
								}
							}
							return "";
						}
					}));

			break;
		}
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargewindow": {
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Discharge Window", "Discharge window start date", ColumnType.NORMAL,

					new BaseFormatter() {
						@Override
						public String render(final Object object) {

							if (object instanceof Row) {
								final Row row = (Row) object;
								final OpenSlotAllocation openSlotAllocation = row.getOpenDischargeSlotAllocation();
								if (openSlotAllocation != null) {
									final Slot slot = openSlotAllocation.getSlot();
									if (slot != null) {
										return Formatters.asLocalDateFormatter.render(slot.getWindowStart());
									}
								}
								final SlotAllocation slotAllocation = row.getDischargeAllocation();
								if (slotAllocation != null) {
									final Slot slot = slotAllocation.getSlot();
									if (slot instanceof DischargeSlot) {
										return Formatters.asLocalDateFormatter.render(slot.getWindowStart());
									}
								}
							}
							return "";
						}
					}));
			break;
		}
		case "com.mmxlabs.lingo.reports.components.columns.schedule.load-operationaltolerance": {

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Load Op. Tol.", "Load volume operational tolerance", ColumnType.NORMAL, new BaseFormatter() {

				@Override
				public Comparable getComparable(Object object) {
					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
						if (openSlotAllocation != null) {
							return getValue(openSlotAllocation.getSlot());
						}
						final SlotAllocation slotAllocation = row.getLoadAllocation();
						if (slotAllocation != null) {
							final Slot slot = slotAllocation.getSlot();
							if (slot instanceof LoadSlot) {
								return getValue(slot);
							}
						}
					}
					return null;
				}

				private Double getValue(final Slot slot) {
					if (slot != null) {
						if (slot.isSetOperationalTolerance()) {
							return slot.getOperationalTolerance();
						}
						Contract c = slot.getContract();
						if (c != null) {
							if (c.isSetOperationalTolerance()) {
								return c.getOperationalTolerance();
							}
						}
					}
					return null;
				}

				@Override
				public String render(final Object object) {

					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
						if (openSlotAllocation != null) {
							final Slot slot = openSlotAllocation.getSlot();
							Double v = getValue(slot);
							if (v != null) {
								return new NumberOfDPFormatter(1).render(v * 100.0);
							}
						}
						final SlotAllocation slotAllocation = row.getLoadAllocation();
						if (slotAllocation != null) {
							final Slot slot = slotAllocation.getSlot();
							if (slot instanceof LoadSlot) {
								Double v = getValue(slot);
								if (v != null) {
									return new NumberOfDPFormatter(1).render(v * 100.0);
								}
							}
						}
					}
					return "";
				}
			}));

			break;
		}
		case "com.mmxlabs.lingo.reports.components.columns.schedule.discharge-operationaltolerance": {
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Discharge Op. Tol.", "Discharge volume operational tolerance", ColumnType.NORMAL,

					new BaseFormatter() {

						private Double getValue(final Slot slot) {
							if (slot != null) {
								if (slot.isSetOperationalTolerance()) {
									return slot.getOperationalTolerance();
								}
								Contract c = slot.getContract();
								if (c != null) {
									if (c.isSetOperationalTolerance()) {
										return c.getOperationalTolerance();
									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							if (object instanceof Row) {
								final Row row = (Row) object;
								final OpenSlotAllocation openSlotAllocation = row.getOpenDischargeSlotAllocation();
								if (openSlotAllocation != null) {
									final Slot slot = openSlotAllocation.getSlot();
									if (slot != null) {
										Double v = getValue(slot);
										if (v != null) {
											return new NumberOfDPFormatter(1).render(v * 100.0);
										}
									}
								}
								final SlotAllocation slotAllocation = row.getDischargeAllocation();
								if (slotAllocation != null) {
									final Slot slot = slotAllocation.getSlot();
									if (slot instanceof DischargeSlot) {
										Double v = getValue(slot);
										if (v != null) {
											return new NumberOfDPFormatter(1).render(v * 100.0);
										}
									}
								}
							}
							return "";
						}
					}));
			break;
		}
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loaddate": {
			final ETypedElement[][] paths = new ETypedElement[][] { { loadAllocationRef, s.getSlotAllocation_SlotVisit(), s.getEvent_Start() }, { targetObjectRef, s.getEvent_Start() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Load/Start Date", null, ColumnType.NORMAL, Formatters.asLocalDateFormatter, paths));
		}

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.loadtime": {
			final ETypedElement[][] paths = new ETypedElement[][] { { loadAllocationRef, s.getSlotAllocation_SlotVisit(), s.getEvent_Start() }, { targetObjectRef, s.getEvent_Start() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Load/Start Time", null, ColumnType.NORMAL, Formatters.asLocalTimeFormatter, paths));
		}

			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargedate": {
			final ETypedElement[][] paths = new ETypedElement[][] { { dischargeAllocationRef, s.getSlotAllocation_SlotVisit(), s.getEvent_Start() }, { targetObjectRef, s.getEvent_End() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Discharge/End Date", null, ColumnType.NORMAL, Formatters.asLocalDateFormatter, paths));
		}
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.dischargetime": {
			final ETypedElement[][] paths = new ETypedElement[][] { { dischargeAllocationRef, s.getSlotAllocation_SlotVisit(), s.getEvent_Start() }, { targetObjectRef, s.getEvent_End() } };
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "Discharge/End Time", null, ColumnType.NORMAL, Formatters.asLocalTimeFormatter, paths));
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
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Purchase Contract", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {

					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
						if (openSlotAllocation != null) {
							final Slot slot = openSlotAllocation.getSlot();
							if (slot != null) {
								final Contract contract = slot.getContract();
								if (contract != null) {
									return contract.getName();
								}
							}
						}
						final SlotAllocation slotAllocation = row.getLoadAllocation();
						if (slotAllocation != null) {
							final Slot slot = slotAllocation.getSlot();
							if (slot instanceof LoadSlot) {
								final Contract contract = slot.getContract();
								if (contract != null) {
									return contract.getName();
								}
							}
						}
					}
					return "";
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.salescontract":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sales Contract", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {

					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenDischargeSlotAllocation();
						if (openSlotAllocation != null) {
							final Slot slot = openSlotAllocation.getSlot();
							if (slot != null) {
								final Contract contract = slot.getContract();
								if (contract != null) {
									return contract.getName();
								}
							}
						}
						final SlotAllocation slotAllocation = row.getDischargeAllocation();
						if (slotAllocation != null) {
							final Slot slot = slotAllocation.getSlot();
							if (slot instanceof DischargeSlot) {
								final Contract contract = slot.getContract();
								if (contract != null) {
									return contract.getName();
								}
							}
						}
					}
					return "";
				}
			}));
			break;	
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buyport": {
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Buy/Start Port", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {

					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
						if (openSlotAllocation != null) {
							final Slot slot = openSlotAllocation.getSlot();
							if (slot != null) {
								final Port port = slot.getPort();
								if (port != null) {
									return port.getName();
								}
							}
						}
						final SlotAllocation slotAllocation = row.getLoadAllocation();
						if (slotAllocation != null) {
							final Slot slot = slotAllocation.getSlot();
							if (slot instanceof LoadSlot) {
								final Port port = slot.getPort();
								if (port != null) {
									return port.getName();
								}
							}
						}
						if (row.getTarget() instanceof Event) {
							final Event event = (Event) row.getTarget();
							final Port port = event.getPort();
							if (port != null) {
								return port.getName();
							}
						}
					}
					return "";
				}
			}));
		}
		break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_load_port":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Load Port", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Load port");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_discharge_port": 
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Discharge Port", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Discharge port");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_buy_window":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Buy Window", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Buy window");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_sell_window":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Sell Window", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Sell window");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_vessel":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Vessel", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Vessel");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_load_volume": 
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Load Volume", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Load volume");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_discharge_volume":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Discharge Volume", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Discharge volume");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_cancellation":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Cancellation", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Cancellation");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_credit":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Credit", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Credit");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.nominated_surveyor":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Nominated Surveyor", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					return getNominationValue(object, "Surveyor");
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sellport": {
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sell/End Port", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {

					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenDischargeSlotAllocation();
						if (openSlotAllocation != null) {
							final Slot slot = openSlotAllocation.getSlot();
							if (slot != null) {
								final Port port = slot.getPort();
								if (port != null) {
									return port.getName();
								}
							}
						}
						final SlotAllocation slotAllocation = row.getDischargeAllocation();
						if (slotAllocation != null) {
							final Slot slot = slotAllocation.getSlot();
							if (slot instanceof DischargeSlot) {
								final Port port = slot.getPort();
								if (port != null) {
									return port.getName();
								}
							}
						}
						if (row.getTarget() instanceof VesselEventVisit) {
							final VesselEventVisit visit = (VesselEventVisit) row.getTarget();
							final VesselEvent event = visit.getVesselEvent();
							if (event instanceof CharterOutEvent) {
								final CharterOutEvent charterOutEvent = (CharterOutEvent) event;
								final Port port = charterOutEvent.getRelocateTo();
								if (port != null) {
									return port.getName();
								}
							}
							if (event != null) {
								final Port port = event.getPort();
								if (port != null) {
									return port.getName();
								}
							}
						}
					}
					return "";
				}
			}));
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
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buy_value":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Purchase Cost", null, ColumnType.NORMAL, Formatters.integerFormatter, loadAllocationRef, s.getSlotAllocation_VolumeValue()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden.canal_date":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Laden Canal Date", "Estimated or booked canal entry date", ColumnType.NORMAL, new BaseFormatter() {

						private LocalDateTime getDate(final Object object) {
							if (object instanceof EObject) {
								final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
								if (portVisit != null) {
									final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
									if (journey != null) {
										return journey.getCanalDateTime();
									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							final LocalDateTime date = getDate(object);
							if (date != null) {
								return Formatters.asLocalDateFormatter.render(date);
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final LocalDateTime date = getDate(object);
							if (date != null) {
								return date;
							}
							return null;
						}
					}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden.canal_latest_date":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Laden Canal latest Date", "Latest canal arrival date", ColumnType.NORMAL, new BaseFormatter() {

						private LocalDateTime getDate(final Object object) {
							if (object instanceof EObject) {
								final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
								if (portVisit != null) {
									final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
									if (journey != null) {
										return journey.getLatestPossibleCanalDateTime();
									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							final LocalDateTime date = getDate(object);
							if (date != null) {
								return Formatters.asLocalDateFormatter.render(date);
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final LocalDateTime date = getDate(object);
							if (date != null) {
								return date;
							}
							return null;
						}
					}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden.canal_entry":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden Canal Entry", "The canal entry port", ColumnType.NORMAL, new BaseFormatter() {

				private String getValue(Object object) {

					IScenarioDataProvider scenarioDataProvider = null;
					if (object instanceof Row) {
						Row row = (Row) object;
						scenarioDataProvider = row.getScenarioDataProvider();
						object = ((EObject) object).eGet(targetObjectRef);
					}

					if (object instanceof EObject && scenarioDataProvider != null) {
						final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
						if (portVisit != null) {
							final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
							if (journey != null) {
								ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
								return modelDistanceProvider.getCanalEntranceName(journey.getRouteOption(), journey.getCanalEntrance());
							}
						}
					}
					return null;
				}

				@Override
				public String render(final Object object) {

					final String value = getValue(object);
					if (value != null) {
						return value;
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final String value = getValue(object);
					if (value != null) {
						return value;
					}
					return "";
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden.canal_booked":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Laden Canal Booked", "Canal voyage used a pre-booked slot", ColumnType.NORMAL, new BaseFormatter() {

						private String getValue(final Object object) {
							if (object instanceof EObject) {
								final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
								if (portVisit != null) {
									final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
									if (journey != null) {
										final RouteOption routeOption = journey.getRouteOption();
										if (routeOption == RouteOption.PANAMA) {
											final CanalBookingSlot canalBooking = journey.getCanalBooking();

											if (canalBooking != null) {
												return "Yes";
											} else {
												final Sequence sequence = journey.getSequence();
												if (sequence != null && sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
													return "-";
												}
												return "No";
											}
										}
									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							final String value = getValue(object);
							if (value != null) {
								return value;
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final String value = getValue(object);
							if (value != null) {
								return value;
							}
							return "";
						}
					}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast.canal_date":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Ballast Canal Date", "Estimated or booked canal entry date", ColumnType.NORMAL, new BaseFormatter() {

						private LocalDateTime getDate(final Object object) {
							if (object instanceof EObject) {
								final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
								if (portVisit != null) {
									final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
									if (journey != null) {
										return journey.getCanalDateTime();
									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							final LocalDateTime date = getDate(object);
							if (date != null) {
								return Formatters.asLocalDateFormatter.render(date);
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final LocalDateTime date = getDate(object);
							if (date != null) {
								return date;
							}
							return null;
						}
					}, dischargeAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast.canal_latest_date":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Ballast Canal Latest Date", "Latest canal arrival date", ColumnType.NORMAL, new BaseFormatter() {

						private LocalDateTime getDate(final Object object) {
							if (object instanceof EObject) {
								final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
								if (portVisit != null) {
									final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
									if (journey != null) {
										return journey.getLatestPossibleCanalDateTime();
									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							final LocalDateTime date = getDate(object);
							if (date != null) {
								return Formatters.asLocalDateFormatter.render(date);
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final LocalDateTime date = getDate(object);
							if (date != null) {
								return date;
							}
							return null;
						}
					}, dischargeAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast.canal_entry":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Canal Entry", "The canal entry port", ColumnType.NORMAL, new BaseFormatter() {

				private String getValue(Object object) {

					IScenarioDataProvider scenarioDataProvider = null;
					if (object instanceof Row) {
						Row row = (Row) object;
						scenarioDataProvider = row.getScenarioDataProvider();
						object = ((EObject) object).eGet(dischargeAllocationRef);
					}

					if (object instanceof EObject && scenarioDataProvider != null) {
						final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
						if (portVisit != null) {
							final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
							if (journey != null) {
								ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
								return modelDistanceProvider.getCanalEntranceName(journey.getRouteOption(), journey.getCanalEntrance());
							}
						}
					}
					return null;
				}

				@Override
				public String render(final Object object) {

					final String value = getValue(object);
					if (value != null) {
						return value;
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final String value = getValue(object);
					if (value != null) {
						return value;
					}
					return "";
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast.canal_booked":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Ballast Canal Entry", "Canal voyage used a pre-booked slot", ColumnType.NORMAL, new BaseFormatter() {

						private String getValue(final Object object) {
							if (object instanceof EObject) {
								final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
								if (portVisit != null) {
									final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
									if (journey != null) {
										if (journey.getRouteOption() == RouteOption.PANAMA) {
											final CanalBookingSlot canalBooking = journey.getCanalBooking();

											if (canalBooking != null) {
												return "Yes";
											} else {
												final Sequence sequence = journey.getSequence();
												if (sequence != null && sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
													return "-";
												}
												return "No";
											}
										}
									}
								}
							}
							return null;
						}

						@Override
						public String render(final Object object) {

							final String value = getValue(object);
							if (value != null) {
								return value;
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final String value = getValue(object);
							if (value != null) {
								return value;
							}
							return "";
						}
					}, dischargeAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sell_value":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Sales Revenue", null, ColumnType.NORMAL, Formatters.integerFormatter, dischargeAllocationRef, s.getSlotAllocation_VolumeValue()));
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

					return ScheduleModelKPIUtils.calculateLegCost(object, cargoAllocationRef, loadAllocationRef);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden_lng_cost":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden LNG Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegFuel(object, cargoAllocationRef, loadAllocationRef, ShippingCostType.LNG_COSTS, TotalType.COST);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden_lng_m3":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden LNG (m³)", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegFuel(object, cargoAllocationRef, loadAllocationRef, ShippingCostType.LNG_COSTS, TotalType.QUANTITY_M3);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden_lng_mmbtu":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden LNG (mmBtu)", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegFuel(object, cargoAllocationRef, loadAllocationRef, ShippingCostType.LNG_COSTS, TotalType.QUANTITY_MMBTU);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden_speed":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden Speed", null, ColumnType.NORMAL, new NumberOfDPFormatter(1) {
				@Override
				public Double getDoubleValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegSpeed(object, cargoAllocationRef, loadAllocationRef);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballastcost":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegCost(object, cargoAllocationRef, dischargeAllocationRef);
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast_lng_cost":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast LNG Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegFuel(object, cargoAllocationRef, dischargeAllocationRef, ShippingCostType.LNG_COSTS, TotalType.COST);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast_lng_m3":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast LNG (m³)", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegFuel(object, cargoAllocationRef, dischargeAllocationRef, ShippingCostType.LNG_COSTS, TotalType.QUANTITY_M3);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast_lng_mmbtu":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast LNG (mmBtu)", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegFuel(object, cargoAllocationRef, dischargeAllocationRef, ShippingCostType.LNG_COSTS, TotalType.QUANTITY_MMBTU);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast_speed":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Speed", null, ColumnType.NORMAL, new NumberOfDPFormatter(1) {
				@Override
				public Double getDoubleValue(final Object object) {

					return ScheduleModelKPIUtils.calculateLegSpeed(object, cargoAllocationRef, dischargeAllocationRef);
				}

			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.totalcost":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Total Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, true, true, ShippingCostType.ALL);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_cost":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Shipping Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.ALL);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_bunkers":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Bunker Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.BUNKER_COSTS);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_port":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Port Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.PORT_COSTS);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.heel_cost":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Heel Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.HEEL_COST);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.heel_revenue":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Heel Revenue", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = -ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.HEEL_REVENUE);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_canal":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Canal Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.CANAL_COSTS);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_hire":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Hire Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.HIRE_COSTS);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_lng":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "LNG Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.LNG_COSTS);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_cooldown":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Cooldown Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof EventGrouping) {
						final EventGrouping grouping = (EventGrouping) object;
						final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.COOLDOWN_COSTS);
						return (int) total;
					}
					return null;

				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_other":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Other Cost", "Other costs (including charter event revenue)", ColumnType.NORMAL, new IntegerFormatter() {
						@Override
						public Integer getIntValue(final Object object) {
							if (object instanceof EventGrouping) {
								final EventGrouping grouping = (EventGrouping) object;
								final long total = ScheduleModelKPIUtils.calculateEventShippingCost(grouping, false, false, ShippingCostType.OTHER_COSTS);
								return (int) total;
							}
							return null;

						}
					}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_additional":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Addn. P&L", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					int addnPNL = 0;

					ProfitAndLossContainer container = ScheduleModelUtils.getProfitAndLossContainer(object);

					if (container != null) {

						final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
						if (dataWithKey != null) {
							for (final GeneralPNLDetails generalPNLDetails : container.getGeneralPNLDetails()) {
								if (generalPNLDetails instanceof SlotPNLDetails) {
									final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
									for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
										if (details instanceof BasicSlotPNLDetails) {
											addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
											addnPNL += ((BasicSlotPNLDetails) details).getExtraShippingPNL();
											addnPNL += ((BasicSlotPNLDetails) details).getExtraUpsidePNL();
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
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_upstream":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, builder.getTotalPNLColumnFactory(columnID, CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK));
			break;

		case "com.mmxlabs.lingo.reports.components.columns.schedule.type":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Type", null, ColumnType.NORMAL, new RowTypeFormatter(), targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_group":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, builder.getEmptyPNLColumnBlockFactory());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.load-notes":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Purchase Notes", "The notes for the load slot", ColumnType.NORMAL, Formatters.objectFormatter,
					loadAllocationRef, s.getSlotAllocation_Slot(), c.getSlot_Notes()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.discharge-notes":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sale Notes", "The notes for the discharge slot", ColumnType.NORMAL,
					Formatters.objectFormatter, dischargeAllocationRef, s.getSlotAllocation_Slot(), c.getSlot_Notes()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden-idle-days":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden Idle", "Laden idle days", ColumnType.NORMAL, new BaseFormatter() {

				private OptionalInt getDuration(final Object object) {
					if (object instanceof EObject) {
						final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
						if (portVisit != null) {
							final Idle idle = ScheduleModelUtils.getLinkedIdleEvent(portVisit);
							if (idle != null) {
								return OptionalInt.of(idle.getDuration());
							}
						}
					}
					return OptionalInt.empty();
				}

				@Override
				public String render(final Object object) {

					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return Formatters.formatAsDays(duration.getAsInt());
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return duration.getAsInt();
					}
					return 0;
				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast-idle-days":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Idle", "Ballast idle days", ColumnType.NORMAL, new BaseFormatter() {
				private OptionalInt getDuration(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) object;
						final Event event = ScheduleModelUtils.getLinkedIdleEvent(slotVisit);
						if (event != null) {
							return OptionalInt.of(event.getDuration());
						}
					}
					return OptionalInt.empty();
				}

				@Override
				public String render(final Object object) {

					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return Formatters.formatAsDays(duration.getAsInt());
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return duration.getAsInt();
					}
					return 0;
				}
			}, dischargeAllocationRef, s.getSlotAllocation_SlotVisit()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.laden-travel-days":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Laden Travel", "Laden travel days", ColumnType.NORMAL, new BaseFormatter() {

				private OptionalInt getDuration(final Object object) {
					if (object instanceof EObject) {
						final PortVisit portVisit = ScheduleModelUtils.getMainEvent((EObject) object);
						if (portVisit != null) {
							final Journey journey = ScheduleModelUtils.getLinkedJourneyEvent(portVisit);
							if (journey != null) {
								return OptionalInt.of(journey.getDuration());
							}
						}
					}
					return OptionalInt.empty();
				}

				@Override
				public String render(final Object object) {

					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return Formatters.formatAsDays(duration.getAsInt());
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return duration.getAsInt();
					}
					return 0;
				}
			}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.ballast-travel-days":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Ballast Travel", "Ballast travel days", ColumnType.NORMAL, new BaseFormatter() {

				private OptionalInt getDuration(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) object;
						final Event event = ScheduleModelUtils.getLinkedJourneyEvent(slotVisit);
						if (event != null) {
							return OptionalInt.of(event.getDuration());
						}
					}
					return OptionalInt.empty();
				}

				@Override
				public String render(final Object object) {

					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return Formatters.formatAsDays(duration.getAsInt());
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return duration.getAsInt();
					}
					return 0;
				}
			}, dischargeAllocationRef, s.getSlotAllocation_SlotVisit()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.duration":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Load/Event Duration", "Duration of load or other event", ColumnType.NORMAL, new BaseFormatter() {

						private OptionalInt getDuration(final Object object) {
							if (object instanceof EObject) {
								final Event event = ScheduleModelUtils.getMainEvent((EObject) object);
								if (event != null) {
									return OptionalInt.of(event.getDuration());
								}
							}

							return OptionalInt.empty();

						}

						@Override
						public String render(final Object object) {

							final OptionalInt duration = getDuration(object);
							if (duration.isPresent()) {
								return Formatters.formatAsDays(duration.getAsInt());
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final OptionalInt duration = getDuration(object);
							if (duration.isPresent()) {
								return duration.getAsInt();
							}
							return 0;
						}
					}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.discharge-duration":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Discharge Duration", "Duration of discharge event", ColumnType.NORMAL, new BaseFormatter() {

				private OptionalInt getDuration(final Object object) {
					if (object instanceof SlotAllocation) {
						final SlotAllocation slotAllocation = (SlotAllocation) object;
						final Slot slot = slotAllocation.getSlot();
						if (slot instanceof DischargeSlot) {
							return OptionalInt.of(slotAllocation.getSlotVisit().getDuration());
						}
					}
					return OptionalInt.empty();

				}

				@Override
				public String render(final Object object) {

					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return Formatters.formatAsDays(duration.getAsInt());
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final OptionalInt duration = getDuration(object);
					if (duration.isPresent()) {
						return duration.getAsInt();
					}
					return 0;
				}
			}, dischargeAllocationRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.next-event-date":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Next event date", "Date of the next event in the schedule", ColumnType.NORMAL, new BaseFormatter() {

						ZonedDateTime getNextEventDate(Object object) {
							if (object instanceof CargoAllocation) {
								final CargoAllocation cargoAllocation = (CargoAllocation) object;
								object = cargoAllocation.getSlotAllocations().get(0).getSlotVisit();
							}
							if (object instanceof PortVisit) {
								final PortVisit portVisit = (PortVisit) object;
								final Sequence seq = portVisit.getSequence();
								if (seq.getSequenceType() == SequenceType.VESSEL || seq.getSequenceType() == SequenceType.SPOT_VESSEL || seq.getSequenceType() == SequenceType.ROUND_TRIP) {
									Event evt = portVisit;
									// Find last element in segment
									evt = ScheduleModelUtils.getSegmentEnd(evt);
									if (evt == null) {
										return null;
									}
									evt = evt.getNextEvent();
									if (evt != null) {
										return evt.getStart();
									}
								}
							}
							return null;

						}

						@Override
						public String render(final Object object) {

							final ZonedDateTime date = getNextEventDate(object);
							if (date != null) {
								return Formatters.asLocalDateFormatter.render(date.toLocalDateTime());
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final ZonedDateTime date = getNextEventDate(object);
							return date;
						}
					}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.next-event-port":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Next event port", "Port of the next event in the schedule", ColumnType.NORMAL, new BaseFormatter() {

						Port getNextEventPort(Object object) {
							if (object instanceof CargoAllocation) {
								final CargoAllocation cargoAllocation = (CargoAllocation) object;
								object = cargoAllocation.getSlotAllocations().get(0).getSlotVisit();
							}

							if (object instanceof PortVisit) {
								final PortVisit portVisit = (PortVisit) object;
								final Sequence seq = portVisit.getSequence();
								if (seq.getSequenceType() == SequenceType.VESSEL || seq.getSequenceType() == SequenceType.SPOT_VESSEL || seq.getSequenceType() == SequenceType.ROUND_TRIP) {
									Event evt = portVisit;
									// Find last element in segment
									evt = ScheduleModelUtils.getSegmentEnd(evt);
									if (evt == null) {
										return null;
									}
									evt = evt.getNextEvent();
									if (evt != null) {
										return evt.getPort();
									}
								}
							}
							return null;

						}

						@Override
						public String render(final Object object) {

							final Port port = getNextEventPort(object);
							if (port != null) {
								return port.getName();
							}

							return null;
						}

						@Override
						public Comparable getComparable(final Object object) {
							final Port port = getNextEventPort(object);
							if (port != null) {
								final String portName = port.getName();
								if (portName != null) {
									return portName;
								}
							}
							return "";
						}
					}, targetObjectRef));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.lateness":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Lateness", null, ColumnType.NORMAL, new BaseFormatter() {
				int getViolationCount(final Object object) {
					if (object instanceof Row) {
						final Row row = (Row) object;
						if (row.getTarget() instanceof EventGrouping) {
							return LatenessUtils.getLatenessAfterFlex((EventGrouping) row.getTarget());
						}
					}

					return 0;
				}

				@Override
				public String render(final Object object) {

					final int lateness = getViolationCount(object);
					if (lateness != 0) {
						return LatenessUtils.formatLatenessHours(lateness);
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					return getViolationCount(object);
				}
			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.capacity_violation":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, columnID, "Violation", null, ColumnType.NORMAL, new BaseFormatter() {
				int getViolationCount(final Object object) {
					if (object instanceof Row) {
						final Row row = (Row) object;
						if (row.getTarget() instanceof EventGrouping) {
							return CapacityUtils.getViolationCount((EventGrouping) row.getTarget());
						}
					}

					return 0;
				}

				@Override
				public String render(final Object object) {

					final int count = getViolationCount(object);
					if (count != 0) {
						return String.format("%,d", count);
					}

					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					return getViolationCount(object);
				}
			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.buy_entity":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Buy Entity", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {

					if (object instanceof Row) {
						final Row row = (Row) object;
						final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
						if (openSlotAllocation != null) {
							final Slot slot = openSlotAllocation.getSlot();
							if (slot != null) {
								final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
								if (entity != null) {
									return entity.getName();
								}
							}
						}
						final SlotAllocation slotAllocation = row.getLoadAllocation();
						if (slotAllocation != null) {
							final Slot slot = slotAllocation.getSlot();
							if (slot instanceof LoadSlot) {
								final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
								if (entity != null) {
									return entity.getName();
								}
							}
						}
					}
					return "";
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sell_entity":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sell Entity", null, ColumnType.NORMAL,

					new BaseFormatter() {
						@Override
						public String render(final Object object) {

							if (object instanceof Row) {
								final Row row = (Row) object;
								final OpenSlotAllocation openSlotAllocation = row.getOpenDischargeSlotAllocation();
								if (openSlotAllocation != null) {
									final Slot slot = openSlotAllocation.getSlot();
									if (slot != null) {
										final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
										if (entity != null) {
											return entity.getName();
										}
									}
								}
								final SlotAllocation slotAllocation = row.getDischargeAllocation();
								if (slotAllocation != null) {
									final Slot slot = slotAllocation.getSlot();
									if (slot instanceof DischargeSlot) {
										final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
										if (entity != null) {
											return entity.getName();
										}
									}
								}
							}
							return "";
						}
					}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.shipping_entity":

			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Shipping Entity", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public @Nullable String render(final Object object) {
					if (object instanceof Row) {
						final Row row = (Row) object;
						final Sequence sequence = row.getSequence();
						if (sequence == null) {
							return null;
						}
						final VesselCharter va = sequence.getVesselCharter();
						if (va != null) {
							final BaseLegalEntity entity = va.getCharterOrDelegateEntity();
							if (entity != null) {
								return entity.getName();
							}
						} else {
							final CargoAllocation cargoAllocation = row.getCargoAllocation();
							if (cargoAllocation != null && (sequence.getSequenceType() != SequenceType.DES_PURCHASE && sequence.getSequenceType() != SequenceType.FOB_SALE)) {

								final SlotAllocation loadAllocation = row.getLoadAllocation();
								if (loadAllocation != null) {
									final Slot slot = loadAllocation.getSlot();
									if (slot != null) {
										final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
										if (entity != null) {
											return entity.getName();
										}
									}
								}
							}

						}
						return null;
					}

					return super.render(object);
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.purchase_counterparty":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Purchase Counterparty", null, ColumnType.NORMAL, new BaseFormatter(), loadAllocationRef,
					s.getSlotAllocation_Slot(), c.getSlot__GetSlotOrDelegateCounterparty()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sale_counterparty":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sale Counterparty", null, ColumnType.NORMAL, new BaseFormatter(), dischargeAllocationRef,
					s.getSlotAllocation_Slot(), c.getSlot__GetSlotOrDelegateCounterparty()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.purchase_cn":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Purchase CN", null, ColumnType.NORMAL, new BaseFormatter(), loadAllocationRef,
					s.getSlotAllocation_Slot(), c.getSlot__GetSlotOrDelegateCN()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.schedule.sale_cn":
			columnManager.registerColumn(CARGO_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Sale CN", null, ColumnType.NORMAL, new BaseFormatter(), dischargeAllocationRef,
					s.getSlotAllocation_Slot(), c.getSlot__GetSlotOrDelegateCN()));
			break;
		}
	}
	
	private IScenarioDataProvider getScenarioDataProvider(final Object object) {
		if (object instanceof Row) {
			final Row row = (Row) object;
			if (row.getScenarioDataProvider() != null) {
				return row.getScenarioDataProvider();
			}
		}
		return null;
	}
	
	private Slot<?> getLoadSlot(final Object object) {
		if (object instanceof Row) {
			final Row row = (Row) object;
			Slot<?> slot = null;
			final OpenSlotAllocation openSlotAllocation = row.getOpenLoadSlotAllocation();
			if (openSlotAllocation != null) {
				slot = openSlotAllocation.getSlot();
			}
			final SlotAllocation slotAllocation = row.getLoadAllocation();
			if (slotAllocation != null) {
				slot = slotAllocation.getSlot();
			}
			return slot;
		}
		return null;
	}

	private Slot<?> getDischargeSlot(final Object object) {
		if (object instanceof Row) {
			final Row row = (Row) object;
			Slot<?> slot = null;
			final OpenSlotAllocation openSlotAllocation = row.getOpenDischargeSlotAllocation();
			if (openSlotAllocation != null) {
				slot = openSlotAllocation.getSlot();
			}
			final SlotAllocation slotAllocation = row.getDischargeAllocation();
			if (slotAllocation != null) {
				slot = slotAllocation.getSlot();
			}
			return slot;
		}
		return null;
	}
	

	private String getNominationValue(final Object object, final String nominationType) {
		String loadNominatedValue = getBuyNominationValue(object, nominationType);
		if (loadNominatedValue != null && !loadNominatedValue.isBlank()) { 
			return loadNominatedValue;
		}
		String dischargeNominatedValue = getSellNominationValue(object, nominationType);
		if (dischargeNominatedValue != null && !dischargeNominatedValue.isBlank()) {
			return dischargeNominatedValue;
		}
		return "";
	}

	private String getBuyNominationValue(final Object object, final String nominationType) {
		Slot<?> slot = getLoadSlot(object);
		if (slot != null) {
			IScenarioDataProvider sdp = getScenarioDataProvider(object);
			if (sdp != null) {
				List<AbstractNomination> nominations = NominationsModelUtils.findNominationsForSlot(sdp, slot);
				for (AbstractNomination nomination : nominations) {
					if (nomination.getType() != null && nomination.getType().toLowerCase().equals(nominationType.toLowerCase())) {
						return NominationsModelUtils.getNominatedValue(nomination);
					}
				}
			}
		}
		return "";
	}
	
	private String getSellNominationValue(final Object object, final String nominationType) {
		Slot<?> slot = getDischargeSlot(object);
		if (slot != null) {
			IScenarioDataProvider sdp = getScenarioDataProvider(object);
			if (sdp != null) {
				List<AbstractNomination> nominations = NominationsModelUtils.findNominationsForSlot(sdp, slot);
				for (AbstractNomination nomination : nominations) {
					if (nomination.getType() != null && nomination.getType().toLowerCase().equals(nominationType.toLowerCase())) {
						return NominationsModelUtils.getNominatedValue(nomination);
					}
				}
			}
		}
		return "";
	}
}
