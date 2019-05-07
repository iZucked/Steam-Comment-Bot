/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.pnlcalcs;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;

public class StandardPNLCalcRowFactory extends AbstractPNLCalcRowFactory {

	@Override
	public Collection<PNLCalcsReportRow> createRows(PNLCalcsOptions options, @Nullable final Collection<Object> targets) {

		boolean containsCargo = false;
		boolean containsCooldown = false;
		if (targets == null || targets.isEmpty()) {
			containsCargo = true;
		} else {
			for (final Object target : targets) {
				if (target instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) target;
					containsCargo = true;
					for (final Event evt : cargoAllocation.getEvents()) {
						if (evt instanceof Cooldown) {
							containsCooldown = true;
							break;
						}
					}
				}
			}
		}

		final List<PNLCalcsReportRow> rows = new LinkedList<>();

		rows.add(createRow(10, "Buy port", false, "", "", false, createFirstPurchaseFormatter(sa -> sa.getPort().getName())));
		rows.add(createRow(20, "Buy date", false, "", "", false,
				createFirstPurchaseFormatter(sa -> sa.getSlotVisit().getStart().format(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter()))));
		rows.add(createRow(30, "Buy volume (m3)", false, "", "", false,
				createBasicFormatter(options, false, Integer.class, VolumeM3Format::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getVolumeTransferred))));
		rows.add(createRow(40, "Buy volume (mmBtu)", false, "", "", false,
				createBasicFormatter(options, false, Integer.class, VolumeMMBtuFormat::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getEnergyTransferred))));
		rows.add(createRow(50, "Buy price($/mmBtu)", false, "", "", false,
				createBasicFormatter(options, false, Double.class, DollarsPerMMBtuFormat::format, createFirstPurchaseTransformer(Double.class, SlotAllocation::getPrice))));

		rows.add(createRow(60, "Sale port", false, "", "", false, createFirstSaleAllocationFormatter(sa -> sa.getPort().getName())));
		rows.add(createRow(70, "Sale date", false, "", "", false,
				createFirstSaleAllocationFormatter(sa -> sa.getSlotVisit().getStart().format(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter()))));
		rows.add(createRow(80, "Sale price($/mmBtu)", false, "", "", false,
				createBasicFormatter(options, false, Double.class, DollarsPerMMBtuFormat::format, createFirstSaleTransformer(Double.class, SlotAllocation::getPrice))));
		rows.add(createRow(90, "Sale volume (m3)", false, "", "", false,
				createBasicFormatter(options, false, Integer.class, VolumeM3Format::format, createFirstSaleTransformer(Integer.class, SlotAllocation::getVolumeTransferred))));
		rows.add(createRow(100, "Sale volume (mmBtu)", false, "", "", false,
				createBasicFormatter(options, false, Integer.class, VolumeMMBtuFormat::format, createFirstSaleTransformer(Integer.class, SlotAllocation::getEnergyTransferred))));

		// Spacer
		rows.add(createRow(110, "", false, "", "", false, createEmptyFormatter()));

		rows.add(createRow(120, "Purchase cost", false, "", "", true,
				createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getVolumeValue))));
		rows.add(createRow(130, "Sales revenue", false, "", "", false,
				createBasicFormatter(options, false, Integer.class, DollarsFormat::format, createFirstSaleTransformer(Integer.class, SlotAllocation::getVolumeValue))));
		// Equity
		rows.add(createRow(140, "Equity", false, "", "", false, createEmptyFormatter()));
		rows.add(createRow(150, "Theoretical shipping cost", false, "", "", false, createEmptyFormatter()));

		// Theoretical shipping cost
		// Real Shipping cost

		// getShippingCost((fu) -> getFuelcost(fu, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT));
		// rows.add(createRow(200, "Real shipping cost", false, "$", "", true, createShippingCosts((fu) -> getShippingCost(StandardPNLCalcRowFactory::getShippingCost(fuelCostFunc) )));

		Function<FuelUsage, Integer> fuelCostFunc = (fu) -> getFuelCost(fu, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
		Function<SlotVisit, Integer> portCostFunc = SlotVisit::getPortCost;
		Function<Object, Integer> func2 = (object) -> getShippingCost(object, portCostFunc, fuelCostFunc);

		rows.add(createRow(200, "Real shipping cost", false, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, func2))));
		rows.add(createRow(205, "PNL", false, "$", "", false,
				createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardPNLCalcRowFactory::getPNLValue))));

		// Spacer
		rows.add(createRow(210, "", false, "", "", false, createEmptyFormatter()));
		rows.add(createRow(220, "", false, "", "", false, createEmptyFormatter()));

		rows.add(createRow(300, "Real shipping", false, "", "", false, createEmptyFormatter()));

		for (int legIdx = 0; legIdx < 2; ++legIdx) {
			final int base = 1000 + 1000 * legIdx;

			if (legIdx == 0) {
				rows.add(createRow(base + 10, "Laden leg", false, "", "", false, createEmptyFormatter()));
			} else {
				rows.add(createRow(base + 9, "", false, "", "", false, createEmptyFormatter()));

				rows.add(createRow(base + 10, "Ballast leg", false, "", "", false, createEmptyFormatter()));
			}

			// rows.add(createRow(base + 20, " Speed", false, "", "", false, createFullLegFormatter2(legIdx, Double.class, SpeedFormat::format, (visit, travel, idle) -> travel.getSpeed())));
			rows.add(createRow(base + 20, "    Speed", false, "", "", false,
					createBasicFormatter(options, true, Double.class, SpeedFormat::format, createFullLegTransformer2(Double.class, legIdx, (visit, travel, idle) -> travel.getSpeed()))));
			rows.add(createRow(base + 30, "    Days", false, "", "", false, createBasicFormatter(options, true, Double.class, DaysFormat::format, createFullLegTransformer2(Double.class, legIdx,
					(visit, travel, idle) -> ((getOrZero(visit, Event::getDuration) + getOrZero(travel, Event::getDuration) + getOrZero(idle, Event::getDuration)) / 24.0)))));

			// rows.add(createRow(base + 90, " Route", false, "", "", false, createFullLegFormatter(legIdx, (travel, idle) -> getRoute(travel.getRouteOption()))));
			// rows.add(createRow(base + 91, " Port duration", false, "", "", false, createFullLegFormatter(legIdx, (travel, idle) -> getPortDuration(travel))));
			// rows.add(createRow(base + 31, " Port days", false, "", "", false, createFullLegFormatter2(legIdx, Double.class, DaysFormat::format,
			// (visit, travel, idle) -> ((getOrZero(visit, Event::getDuration)) / 24.0))));

			rows.add(createRow(base + 40, "    Total BO (mmBtu)", false, "", "", false, createBasicFormatter(options, true, Integer.class, VolumeMMBtuFormat::format,
					createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MMBTU, Fuel.NBO, Fuel.FBO))))));
			rows.add(createRow(base + 50, "    Charter Cost", true, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFullLegTransformer2(Integer.class,
					legIdx, (visit, travel, idle) -> (getOrZero(visit, Event::getCharterCost) + getOrZero(travel, Event::getCharterCost) + getOrZero(idle, Event::getCharterCost))))));
			
			rows.add(createRow(base + 51, "    Charter Rate", true, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFullLegTransformer2(Integer.class,
					legIdx, (visit, travel, idle) -> getOrZero(visit, event -> { return (int) ((double)event.getCharterCost()*24/(double)event.getDuration()); }  )))));
			
			rows.add(createRow(base + 60, "    Bunkers (MT)", false, "", "", false, createBasicFormatter(options, true, Integer.class, VolumeM3Format::format,
					createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MT, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))))));
			rows.add(createRow(base + 70, "    Bunkers cost", false, "", "", false, createBasicFormatter(options, true, Integer.class, DollarsFormat::format,
					createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelCost(visit, travel, idle, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))))));
			rows.add(createRow(base + 80, "    Port Costs ", false, "", "", false,
					createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (visit.getPortCost())))));
			rows.add(createRow(base + 90, "    Route", false, "", "", false,
					createBasicFormatter(options, false, String.class, Object::toString, createFullLegTransformer(String.class, legIdx, (travel, idle) -> getRoute(travel.getRouteOption())))));
			rows.add(createRow(base + 100, "    Canal Cost", true, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format,
					createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getOrZero(travel, Journey::getToll))))));

			if (legIdx != 0) {
				rows.add(createRow(base + 109, "    Cooldown cost ($)", true, "$", "", true, createEmptyFormatter()));
			}

			rows.add(createRow(base + 110, "    Total cost ($)", true, "$", "", true, createBasicFormatter(options, true, Long.class, DollarsFormat::format,
					createFullLegTransformer2(Long.class, legIdx, (visit, travel, idle) -> (getEventShippingCost(visit) + getEventShippingCost(travel) + getEventShippingCost(idle))))));
			rows.add(createRow(base + 120, "    Idle days", false, "", "", false, createBasicFormatter(options, true, Double.class, DaysFormat::format,
					createFullLegTransformer2(Double.class, legIdx, (visit, travel, idle) -> (getOrZero(idle, Event::getDuration) / 24.0))), greyColourProvider));
			rows.add(createRow(base + 130, "    Idle BO", false, "", "", false, createBasicFormatter(options, true, Integer.class, VolumeMMBtuFormat::format,
					createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelVolume(idle, FuelUnit.MMBTU, Fuel.NBO, Fuel.FBO)))), greyColourProvider));
			rows.add(createRow(base + 140, "    Idle charter", true, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format,
					createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getOrZero(idle, Event::getCharterCost)))), greyColourProvider));
			rows.add(
					createRow(base + 150, "    Idle bunkers", false, "", "", false,
							createBasicFormatter(options, true, Integer.class, VolumeM3Format::format,
									createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelVolume(idle, FuelUnit.MT, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT)))),
							greyColourProvider));

			rows.add(createRow(base + 160, "    Idle bunkers cost", false, "", "", false, createBasicFormatter(options, true, Integer.class, DollarsFormat::format,
					createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelCost(idle, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT)))), greyColourProvider));

			rows.add(createRow(base + 170, "    Total idle cost ($)", false, "", "", true,
					createBasicFormatter(options, true, Long.class, DollarsFormat::format, createFullLegTransformer2(Long.class, legIdx, (visit, travel, idle) -> (getEventShippingCost(idle)))),
					greyColourProvider));
		}

		return rows;
	}

	private static Integer getShippingCost(final Object object, Function<SlotVisit, Integer> slotVisitFunc, Function<FuelUsage, Integer> fuelUsageFunc) {

		if (object == null) {
			return null;
		}

		Sequence sequence = null;
		List<Event> events = null;

		if (object instanceof CargoAllocation) {
			sequence = ((CargoAllocation) object).getSequence();
			events = ((CargoAllocation) object).getEvents();
		} else if (object instanceof VesselEventVisit) {
			sequence = ((VesselEventVisit) object).getSequence();
			events = ((VesselEventVisit) object).getEvents();
		}

		if (sequence == null || events == null) {
			return null;
		}
		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		int charterCost = 0;
		for (final Event event : events) {

			charterCost += event.getCharterCost();

			if (event instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) event;
				// Port Costs
				shippingCost += slotVisitFunc.apply(slotVisit);
			}

			if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof FuelUsage) {
				final FuelUsage fuelUsage = (FuelUsage) event;
				// Base fuel costs
				shippingCost += fuelUsageFunc.apply(fuelUsage);
			}

			if (event instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) event;
				shippingCost += cooldown.getCost();
			}
		}

		// Add on chartering costs
		if (sequence == null || sequence.isSpotVessel() || sequence.isTimeCharterVessel()) {
			shippingCost += charterCost;
		}
		return shippingCost;

	}

	private static long getEventShippingCost(final Event event) {

		if (event == null) {
			return 0;
		}

		final Sequence sequence = event.getSequence();

		if (sequence == null) {
			return 0;
		}
		// Bit of a double count here, but need to decide what to add to the model
		long shippingCost = 0;
		long charterCost = 0;
		{

			charterCost += event.getCharterCost();

			if (event instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) event;
				// Port Costs
				shippingCost += slotVisit.getPortCost();
			}

			if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof FuelUsage) {
				final FuelUsage fuelUsage = (FuelUsage) event;
				// Base fuel costs
				shippingCost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
			}

			if (event instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) event;
				shippingCost += cooldown.getCost();
			}
		}

		// Add on chartering costs
		if (sequence.isSpotVessel() || sequence.isTimeCharterVessel()) {
			shippingCost += charterCost;
		}
		return shippingCost;

	}

	private static int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					sum += fq.getCost();
				}
			}
		}
		return sum;
	}

	private static int getFuelVolume(final FuelUsage fuelUser, final FuelUnit unit, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					for (final FuelAmount fa : fq.getAmounts()) {
						if (fa.getUnit() == unit) {
							sum += fa.getQuantity();
						}
					}
				}
			}
		}
		return sum;
	}

	private static int getFuelCost(final FuelUsage fuelUser1, final FuelUsage fuelUser2, final FuelUsage fuelUser3, final Fuel... fuels) {
		return getFuelCost(fuelUser1, fuels) + getFuelCost(fuelUser2, fuels) + getFuelCost(fuelUser3, fuels);
	}

	private static int getFuelVolume(final FuelUsage fuelUser1, final FuelUsage fuelUser2, final FuelUsage fuelUser3, final FuelUnit unit, final Fuel... fuels) {
		return getFuelVolume(fuelUser1, unit, fuels) + getFuelVolume(fuelUser2, unit, fuels) + getFuelVolume(fuelUser3, unit, fuels);
	}

	private static <T> int getOrZero(final T object, final ToIntFunction<T> func) {
		if (object != null) {
			return func.applyAsInt(object);
		}
		return 0;
	}

	private static String getRoute(final RouteOption routeOption) {
		switch (routeOption) {

		case DIRECT:
			return "direct";
		case PANAMA:
			return "Panama";
		case SUEZ:
			return "Suez";
		default:
			break;

		}
		return null;
	}

	/**
	 * Get total cargo PNL value
	 * 
	 * @param container
	 * @param entity
	 * @return
	 */
	private static Integer getPNLValue(final Object object) {
		if (object instanceof ProfitAndLossContainer) {
			final ProfitAndLossContainer container = (ProfitAndLossContainer) object;
			final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
			if (groupProfitAndLoss == null) {
				return null;
			}
			// Rounding!
			return (int) groupProfitAndLoss.getProfitAndLoss();
		}
		return null;
	}
}
