/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.ui.tabular.columngeneration.SingleColumnFactoryBuilder;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

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
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Scenario").withTooltip("The scenario name. Only shown when multiple scenarios are selected")
							.withColumnType(ColumnType.MULTIPLE)
							.withCellRenderer(containingScheduleFormatter)
							.build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.vessel":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Vessel").withCellRenderer(new VesselAssignmentFormatter()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.type":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Type").withCellRenderer(Formatters.objectFormatter).withElementPath(sp.getEvent__Type()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.id":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "ID").withCellRenderer(Formatters.objectFormatter).withElementPath(sp.getEvent__Name()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.contract":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Contract").withCellRenderer(new BaseFormatter() {
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

			}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.startdate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Start Date")

					.withCellRenderer(new AsDateTimeFormatter(DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateTimeStringDisplay()), true) {
						@Override
						public String render(final Object o) {
							return super.render(((Event) o).getStart());
						}

						@Override
						public Comparable getComparable(final Object object) {
							return new EventStartDateComparator((Event) object);
						}
					})
					.build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.enddate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "End Date").withCellRenderer(Formatters.asDateTimeFormatterWithTZ).withElementPath(sp.getEvent_End()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.duration":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Duration").withTooltip("Duration in days (or days:hours)").withCellRenderer(new BaseFormatter() {
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

					}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.cv":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "CV").withCellRenderer(new NumberOfDPFormatter(2) {

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
			}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.price":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Price").withCellRenderer(new NumberOfDPFormatter(2) {

				@Override
				public Double getDoubleValue(final Object object) {
					if (object instanceof SlotVisit sv) {
						final SlotAllocation sa = sv.getSlotAllocation();
						if (sa == null) {
							return null;
						}
						return sa.getPrice();
					}
					return null;
				}
			}).build());// .setTooltip("$/mmBtu");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.speed":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Speed").withCellRenderer(new NumberOfDPFormatter(1)).withElementPath(sp.getJourney_Speed()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.distance":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Distance").withCellRenderer(Formatters.integerFormatter).withElementPath(sp.getJourney_Distance()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.fromport":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "From Port").withCellRenderer(Formatters.objectFormatter).withElementPath(sp.getEvent_Port(), name).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.toport": {
			final ETypedElement[][] paths = new ETypedElement[][] { //
					{ SchedulePackage.Literals.VESSEL_EVENT_VISIT__REDELIVERY_PORT }, //
					{ SchedulePackage.Literals.JOURNEY__DESTINATION } };
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "To Port").withCellRenderer(Formatters.namedObjectFormatter).withMultiElementPath(paths).build());
		}
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.atport":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "At Port").withCellRenderer(Formatters.objectFormatter).withElementPath(sp.getEvent_Port(), name).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.route":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Route").withCellRenderer(Formatters.namedObjectFormatter).withElementPath(sp.getJourney_RouteOption()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.transfervolume_m3":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Transfer Volume").withCellRenderer(new IntegerFormatter() {
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
			}).build());// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.transfervolume_mmbtu":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Transfer Energy").withCellRenderer(new IntegerFormatter() {
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
			}).build());// .setTooltip("In mmBtu");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heelstart":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Volume on board Start").withCellRenderer(new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof Event) {
						final Event pv = (Event) object;
						return pv.getHeelAtStart();
					}
					return null;
				}
			}).build());// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heelend":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Volume on board End").withCellRenderer(new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof Event) {
						final Event pv = (Event) object;
						return pv.getHeelAtEnd();
					}
					return null;
				}
			}).build());// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heel_cost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Heel Cost").withCellRenderer(new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof PortVisit) {
						final PortVisit pv = (PortVisit) object;
						return pv.getHeelCost();
					}
					return null;
				}
			}).build());// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.heel_revenue":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Heel Revenue").withCellRenderer(new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof PortVisit) {
						final PortVisit pv = (PortVisit) object;
						return pv.getHeelRevenue();
					}
					return null;
				}
			}).build());// .setTooltip("In m³");
			break;
		case PortRotationBasedReportBuilder.COLUMN_BLOCK_FUELS:
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, builder.getEmptyFuelsColumnBlockFactory());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.fuelcost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Fuel Cost").withCellRenderer(new NumberOfDPFormatter(0) {
				@Override
				public Double getDoubleValue(final Object object) {
					if (object instanceof FuelUsage) {
						return ((FuelUsage) object).getFuelCost();
					} else {
						return null;
					}
				}
			}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.chartercost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Charter Cost").withCellRenderer(new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					return (int) ((Event) object).getCharterCost();
				}
			}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.routecost":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Canal Cost").withCellRenderer(new IntegerFormatter()).withElementPath(sp.getJourney_Toll()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canalarrival":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Canal Arrival").withCellRenderer(Formatters.asDateTimeFormatterNoTz).withElementPath(sp.getJourney_CanalArrivalTime()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canaldate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Canal Date").withCellRenderer(Formatters.asDateTimeFormatterNoTz).withElementPath(sp.getJourney_CanalDateTime()).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.latestpossiblecanaldate":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID,
					new SingleColumnFactoryBuilder(columnID, "Latest Canal Date").withCellRenderer(Formatters.asDateTimeFormatterNoTz)
							.withElementPath(sp.getJourney_LatestPossibleCanalDateTime())
							.build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canalentry":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Canal Entry").withCellRenderer(new BaseFormatter() {
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
			}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.canal.booked":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Canal Booked").withCellRenderer(new BaseFormatter() {
				String getValue(final Object object) {
					if (object instanceof Journey journey) {
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
			}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.portcosts":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Port Costs").withCellRenderer(new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof PortVisit) {
						final PortVisit visit = (PortVisit) object;
						return visit.getPortCost();
					}
					return null;
				}
			}).build());
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.totalcosts":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new SingleColumnFactoryBuilder(columnID, "Total Cost").withCellRenderer(new IntegerFormatter() {
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
			}).build());
			break;
		}
	}
}
