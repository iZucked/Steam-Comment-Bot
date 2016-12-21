/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnBlockManager;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.formatters.AsDateTimeFormatter;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.provider.PinnedScheduleFormatter;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
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
					if (event.getStart().equals(other.getStart())) {
						if (event.getPreviousEvent() != null && event.getPreviousEvent().equals(other)) {
							return 1;
						} else if (event.getNextEvent() != null && event.getNextEvent().equals(other)) {
							return -1;
						}
					} else {
						return event.getStart().compareTo(other.getStart());
					}
				}
			}
			return 0;
		}
	}

	public static final String PORT_ROTATION_REPORT_TYPE_ID = "PORT_ROTATION_REPORT_TYPE_ID";

	/**
	 * Mapping between fuel and the quantity unit displayed.
	 */
	private static final Map<Fuel, FuelUnit> fuelQuanityUnits = new HashMap<Fuel, FuelUnit>();
	/**
	 * Mapping between fuel and the unit price unit displayed.
	 */
	private static final Map<Fuel, FuelUnit> fuelUnitPriceUnits = new HashMap<Fuel, FuelUnit>();

	protected static final String COLUMN_BLOCK_FUELS = "com.mmxlabs.lingo.reports.components.columns.portrotation.fuels";

	static {
		fuelQuanityUnits.put(Fuel.BASE_FUEL, FuelUnit.MT);
		fuelQuanityUnits.put(Fuel.FBO, FuelUnit.M3);
		fuelQuanityUnits.put(Fuel.NBO, FuelUnit.M3);
		fuelQuanityUnits.put(Fuel.PILOT_LIGHT, FuelUnit.MT);

		fuelUnitPriceUnits.put(Fuel.BASE_FUEL, FuelUnit.MT);
		fuelUnitPriceUnits.put(Fuel.FBO, FuelUnit.MMBTU);
		fuelUnitPriceUnits.put(Fuel.NBO, FuelUnit.MMBTU);
		fuelUnitPriceUnits.put(Fuel.PILOT_LIGHT, FuelUnit.MT);
	}

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager manager, final PortRotationBasedReportBuilder builder) {

		final SchedulePackage sp = SchedulePackage.eINSTANCE;
		final Image pinImage = Activator.getDefault().getImageRegistry().get(Activator.Implementation.IMAGE_PINNED_ROW);

		final EStructuralFeature name = MMXCorePackage.eINSTANCE.getNamedObject_Name();
		switch (columnID) {
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.schedule":
			final PinnedScheduleFormatter containingScheduleFormatter = new PinnedScheduleFormatter(pinImage) {
				@Override
				public String render(final Object object) {
					final ScenarioResult scenarioResult = builder.getReport().getScenarioInstance(object);
					if (scenarioResult != null) {
						final ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();
						if (scenarioInstance != null) {
							return scenarioInstance.getName();
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
					new AsDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), true) {
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
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.toport":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "To Port", null, ColumnType.NORMAL, Formatters.objectFormatter, sp.getJourney_Destination(), name);
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.atport":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "At Port", null, ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent_Port(), name);
			break;
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.route":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Route", null, ColumnType.NORMAL, Formatters.objectFormatter, sp.getJourney_Route(), name);
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
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Heel Start", null, ColumnType.NORMAL, new IntegerFormatter() {
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
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, columnID, "Heel End", null, ColumnType.NORMAL, new IntegerFormatter() {
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
		case "com.mmxlabs.lingo.reports.components.columns.portrotation.fuels":
			manager.registerColumn(PORT_ROTATION_REPORT_TYPE_ID, new EmfBlockColumnFactory() {

				@Override
				public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
					ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_FUELS);
					if (block == null) {
						block = blockManager.createBlock(COLUMN_BLOCK_FUELS, "[Fuels]", ColumnType.NORMAL);
					}
					block.setPlaceholder(true);
					// Create all these columns within the fuels group
					{

						for (final Fuel fuelName : Fuel.values()) {
							blockManager.createColumn(block, fuelName.toString(), new IntegerFormatter() {
								@Override
								public Integer getIntValue(final Object object) {
									if (object instanceof FuelUsage) {
										final FuelUsage mix = (FuelUsage) object;
										for (final FuelQuantity q : mix.getFuels()) {
											if (q.getFuel().equals(fuelName)) {
												final FuelUnit unit = fuelQuanityUnits.get(fuelName);
												for (final FuelAmount fa : q.getAmounts()) {
													if (fa.getUnit() == unit) {
														return fa.getQuantity();
													}
												}
											}
										}

										return 0;
									} else {
										return null;
									}
								}
							}).setTooltip("In " + fuelQuanityUnits.get(fuelName));

							blockManager.createColumn(block, fuelName + " Unit Price", new NumberOfDPFormatter(2) {
								@Override
								public Double getDoubleValue(final Object object) {
									if (object instanceof FuelUsage) {
										final FuelUsage mix = (FuelUsage) object;
										for (final FuelQuantity q : mix.getFuels()) {
											if (q.getFuel() == fuelName) {
												final FuelUnit unit = fuelUnitPriceUnits.get(fuelName);
												for (final FuelAmount fa : q.getAmounts()) {
													if (fa.getUnit() == unit) {
														return fa.getUnitPrice();
													}
												}
											}
										}
									}
									return null;
								}
							}).setTooltip("Price per " + fuelUnitPriceUnits.get(fuelName));
							blockManager.createColumn(block, fuelName + " Cost", new IntegerFormatter() {
								@Override
								public Integer getIntValue(final Object object) {
									if (object instanceof FuelUsage) {
										for (final FuelQuantity q : ((FuelUsage) object).getFuels()) {
											if (q.getFuel().equals(fuelName)) {
												return q.getCost();
											}
										}
										return 0;
									} else {
										return null;
									}
								}
							});
						}

					}

					return null;
				}

			});
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
