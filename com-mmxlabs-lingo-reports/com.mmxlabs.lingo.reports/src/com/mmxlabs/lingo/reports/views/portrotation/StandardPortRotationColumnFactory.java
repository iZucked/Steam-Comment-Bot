/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.time.format.DateTimeFormatter;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.lingo.reports.views.PinnedScheduleFormatter;
import com.mmxlabs.lingo.reports.views.formatters.AsDateTimeFormatter;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.models.ui.tabular.columngeneration.MultiObjectEmfBlockColumnFactory;
import com.mmxlabs.models.ui.tabular.columngeneration.SimpleEmfBlockColumnFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class StandardPortRotationColumnFactory implements IPortRotationColumnFactory {

	private static class EventStartDateComparator implements Comparable<EventStartDateComparator> {
		Event event;

		public EventStartDateComparator(final Event event) {
			this.event = event;
		}

		@Override
		public int compareTo(final EventStartDateComparator o) {
			if (o != null) {
				final Event other = o.event;
				if (other != null) {
					if (event.getPreviousEvent() != null && event.getPreviousEvent().equals(other)) {
						return 1;
					} else if (event.getNextEvent() != null && event.getNextEvent().equals(other)) {
						return -1;
					}
					if (event.getStart().equals(other.getStart())) {
						return event.getEnd().compareTo(other.getEnd());
					} else {
						return event.getStart().compareTo(other.getStart());
					}
				}
			}
			return 0;
		}
	}

	public static final String PORT_ROTATION_REPORT_TYPE_ID = "PORT_ROTATION_REPORT_TYPE_ID";

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager manager, final PortRotationBasedReportBuilder builder) {

		final SchedulePackage sp = SchedulePackage.eINSTANCE;
		final Image pinImage = Activator.getDefault().getImageRegistry().get(Activator.Implementation.IMAGE_PINNED_ROW);

		final EStructuralFeature name = MMXCorePackage.eINSTANCE.getNamedObject_Name();
		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.schedule":
			final PinnedScheduleFormatter containingScheduleFormatter = new PinnedScheduleFormatter(pinImage) {

				@Override
				public Image getImage(Object element) {
					TransformedSelectedDataProvider selectedDataProvider = builder.getReport().getCurrentSelectedDataProvider();
					if (selectedDataProvider != null) {
						if (selectedDataProvider.isPinnedObject(element)) {
							return pinImage;
						}
					}

					return null;
				}

				@Override
				public String render(final Object object) {
					final ScenarioResult scenarioResult = builder.getReport().getScenarioResult(object);
					if (scenarioResult != null) {
						final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
						if (modelRecord != null) {
							return modelRecord.getName();
						}
					}
					return null;
				}

			};
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Scenario", "The scenario name. Only shown when multiple scenarios are selected", ColumnType.MULTIPLE,
					containingScheduleFormatter);
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.vessel":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Vessel", null, ColumnType.NORMAL, new VesselAssignmentFormatter());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.type":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Type", null, ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent__Type());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.id":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "ID", null, ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent__Name());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.contract":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Contract", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					final Event se = (Event) object;
					if (se instanceof SlotVisit) {
						final SlotVisit sv = (SlotVisit) se;
						final Contract c = sv.getSlotAllocation().getContract();
						if (c != null)
							return c.getName();
					}
					return null;
				}

				@Override
				public Comparable getComparable(final Object object) {
					final String str = render(object);
					if (str == null) {
						return "";
					}
					return str;
				}

			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.startdate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Start Date", null, ColumnType.NORMAL,
					new AsDateTimeFormatter(DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateTimeStringDisplay()), true) {
						@Override
						public String render(final Object o) {
							return super.render(((Event) o).getStart());
						}

						@Override
						public Comparable getComparable(final Object object) {
							return new EventStartDateComparator((Event) object);
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.enddate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "End Date", null, ColumnType.NORMAL, Formatters.asDateTimeFormatterWithTZ, sp.getEvent_End());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.duration":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Duration", "Duration in days (or days:hours)", ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public String render(final Object object) {
					final Event se = (Event) object;
					final int duration = se.getDuration();
					return Formatters.formatAsDays(duration);
				}

				@Override
				public Comparable getComparable(final Object object) {
					return ((Event) object).getDuration();
				}

			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.cv":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "CV", null, ColumnType.NORMAL, new NumberOfDPFormatter(2) {

				@Override
				public Double getDoubleValue(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit sv = (SlotVisit) object;
						final SlotAllocation sa = sv.getSlotAllocation();
						if (sa == null) {
							return null;
						}
						return sa.getCv();
					}
					return null;
				}
			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.price":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Price", null, ColumnType.NORMAL, new NumberOfDPFormatter(2) {

				@Override
				public Double getDoubleValue(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit sv = (SlotVisit) object;
						final SlotAllocation sa = sv.getSlotAllocation();
						if (sa == null) {
							return null;
						}
						return sa.getPrice();
					}
					return null;
				}
			});// .setTooltip("$/mmBtu");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.speed":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Speed", null, ColumnType.NORMAL, new NumberOfDPFormatter(1), sp.getJourney_Speed());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.distance":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Distance", null, ColumnType.NORMAL, Formatters.integerFormatter, sp.getJourney_Distance());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.fromport":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "From Port", null, ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent_Port(), name);
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.toport": {
			final ETypedElement[][] paths = new ETypedElement[][] { //
					{ SchedulePackage.Literals.VESSEL_EVENT_VISIT__REDELIVERY_PORT }, //
					{ SchedulePackage.Literals.JOURNEY__DESTINATION } };
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new MultiObjectEmfBlockColumnFactory(columnID, "To Port", null, ColumnType.NORMAL, Formatters.namedObjectFormatter, paths));
		}
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.atport":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "At Port", null, ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent_Port(), name);
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.route":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Route", null, ColumnType.NORMAL, Formatters.namedObjectFormatter, sp.getJourney_RouteOption());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.transfervolume_m3":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Transfer Volume", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit sv = (SlotVisit) object;
						final SlotAllocation sa = sv.getSlotAllocation();
						if (sa == null) {
							return null;
						}
						return sa.getVolumeTransferred();
					}
					return null;
				}
			});// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.transfervolume_mmbtu":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Transfer Energy", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit sv = (SlotVisit) object;
						final SlotAllocation sa = sv.getSlotAllocation();
						if (sa == null) {
							return null;
						}
						return sa.getEnergyTransferred();
					}
					return null;
				}
			});// .setTooltip("In mmBtu");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heelstart":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Volume on board Start", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof Event) {
						final Event pv = (Event) object;
						return pv.getHeelAtStart();
					}
					return null;
				}
			});// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heelend":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Volume on board End", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof Event) {
						final Event pv = (Event) object;
						return pv.getHeelAtEnd();
					}
					return null;
				}
			});// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heel_cost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Heel Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof PortVisit) {
						final PortVisit pv = (PortVisit) object;
						return pv.getHeelCost();
					}
					return null;
				}
			});// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heel_revenue":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Heel Revenue", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof PortVisit) {
						final PortVisit pv = (PortVisit) object;
						return pv.getHeelRevenue();
					}
					return null;
				}
			});// .setTooltip("In m³");
			break;
		case PortRotationBasedReportBuilder.COLUMN_BLOCK_FUELS:
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, builder.getEmptyFuelsColumnBlockFactory());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.fuelcost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Fuel Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof FuelUsage) {
						return ((FuelUsage) object).getFuelCost();
					} else {
						return null;
					}
				}
			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.chartercost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Charter Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					return (int) ((Event) object).getCharterCost();
				}
			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.routecost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Canal Cost", null, ColumnType.NORMAL, new IntegerFormatter(), sp.getJourney_Toll());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canalarrival":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Canal Arrival", null, ColumnType.NORMAL, Formatters.asDateTimeFormatterNoTz, sp.getJourney_CanalArrivalTime()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canaldate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Canal Date", null, ColumnType.NORMAL, Formatters.asDateTimeFormatterNoTz, sp.getJourney_CanalDateTime()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.latestpossiblecanaldate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SimpleEmfBlockColumnFactory(columnID, "Latest Canal Date", null, ColumnType.NORMAL, Formatters.asDateTimeFormatterNoTz, sp.getJourney_LatestPossibleCanalDateTime()));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canalentry":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Canal Entry", null, ColumnType.NORMAL, new BaseFormatter() {
				@Override
				public @Nullable String render(final Object object) {

					if (object instanceof Journey) {
						final Journey journey = (Journey) object;
						final ScenarioResult scenarioResult = builder.getReport().getScenarioResult(journey);
						if (scenarioResult != null) {
							final IScenarioDataProvider scenarioDataProvider = scenarioResult.getScenarioDataProvider();
							if (scenarioDataProvider != null) {
								final ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
								return modelDistanceProvider.getCanalEntranceName(journey.getRouteOption(), journey.getCanalEntrance());
							}
						}
					}

					return "";
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canal.booked":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SimpleEmfBlockColumnFactory(columnID, "Canal Booked", null, ColumnType.NORMAL, new BaseFormatter() {
				String getValue(final Object object) {
					if (object instanceof Journey) {
						final Journey journey = (Journey) object;
						final RouteOption routeOption = journey.getRouteOption();
						if (routeOption == RouteOption.PANAMA) {
							final CanalBookingSlot canalBooking = journey.getCanalBooking();

							if (canalBooking != null) {
								return "Yes";
							} else {
								if (journey.getSequence().getSequenceType() == SequenceType.ROUND_TRIP) {
									return "-";
								}
								return "No";
							}
							// return canalBooking != null;
						}
					}
					return null;
				}

				@Override
				public @Nullable String render(final Object object) {
					final String value = getValue(object);
					if (value != null) {
						return value;// ? "Yes" : "No";
					}
					return "";
				}

				@Override
				public Comparable getComparable(final Object object) {
					return getValue(object);
				}
			}));
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.portcosts":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Port Costs", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof PortVisit) {
						final PortVisit visit = (PortVisit) object;
						return visit.getPortCost();
					}
					return null;
				}
			});
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.totalcosts":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Total Cost", null, ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					long total = 0;
					if (object instanceof FuelUsage) {
						total += ((FuelUsage) object).getFuelCost();
					}
					if (object instanceof Event) {
						total += ((Event) object).getCharterCost();
					}
					if (object instanceof Journey) {
						total += ((Journey) object).getToll();
					}
					if (object instanceof Purge) {
						total += ((Purge) object).getCost();
					}
					if (object instanceof Cooldown) {
						total += ((Cooldown) object).getCost();
					}
					if (object instanceof PortVisit) {
						total += ((PortVisit) object).getPortCost();
					}

					return (int) total;
				}
			});
			break;
		}
	}
}
