/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.apache.shiro.SecurityUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.views.standard.econs.EconsOptions.MarginBy;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class StandardEconsRowFactory extends AbstractEconsRowFactory {

	@Override
	public Collection<CargoEconsReportRow> createRows(@NonNull final EconsOptions options, @Nullable final Collection<Object> targets) {

		boolean containsCargo = false;
		boolean containsEvent = false;
		boolean containsCharterOut = false;
		boolean containsCharterLength = false;
		boolean containsCooldown = false;
		boolean containsPurge = false;
		
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
						}
						if (evt instanceof Purge) {
							containsPurge = true;
						}
					}
				}
				if (target instanceof CharterLengthEvent) {
					containsCharterLength = true;
				}
				if (target instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) target;
					containsEvent = true;
					if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent) {
						containsCharterOut = true;
					}
					for (final Event evt : vesselEventVisit.getEvents()) {
						if (evt instanceof Cooldown) {
							containsCooldown = true;
						}
						if (evt instanceof Purge) {
							containsPurge = true;
						}
					}
				}
			}
		}

		final List<CargoEconsReportRow> rows = new LinkedList<>();
		if (containsCargo) {
			rows.add(createRow(10, "Purchase", true, "$", "", true, createBuyValuePrice(options, true)));
			rows.add(createRow(20, "    Price", true, "$", "", true, createBuyPrice(options, true)));
			rows.add(createRow(30, "    Volume", true, "", "", false, createBuyVolumeMMBTuPrice(options, false)));
		}
		rows.add(createRow(40, "Shipping", true, "$", "", true, createShippingCosts(options, true)));
		rows.add(createRow(50, "    Bunkers", true, "$", "", true, createShippingBunkersTotal(options, true)));
		rows.add(createRow(60, "    Port", true, "$", "", true, createShippingPortCosts(options, true)));
		rows.add(createRow(70, "    Canal", true, "$", "", true, createShippingCanalCosts(options, true)));
		rows.add(createRow(80, "    Boil-off", true, "$", "", true, createShippingBOGTotal(options, true), createBOGColourProvider(options)));
		rows.add(createRow(90, "    Charter Cost", true, "$", "", true, createShippingCharterCosts(options, true), createCharterFeesColourProvider(options)));
		if (containsCargo) {
			final CargoEconsReportRow row = createRow(91, "    $/mmBtu", true, "$", "", true, createShippingCostsByMMBTU(options, true));
			row.tooltip = () -> {
				switch (options.marginBy) {
				case PURCHASE_VOLUME:
					return "Cost by purchase volume";
				case SALE_VOLUME:
					return "Cost by sales volume";
				default:
					return null;

				}
			};
			rows.add(row);
		}
		if (containsPurge) {
			rows.add(createRow(92, "    Purge Cost", true, "$", "", true, createShippingPurgeCosts(options, true)));
		}
		if (containsCooldown) {
			rows.add(createRow(93, "    Cooldown Cost", true, "$", "", true, createShippingCooldownCosts(options, true)));
		}
		if (containsCharterOut) {
			rows.add(createRow(100, "Charter Revenue", true, "$", "", false, createShippingCharterRevenue(options, false)));
			rows.add(createRow(110, "Repositioning", true, "$", "", true, createShippingRepositioning(options, true)));
			rows.add(createRow(120, "Ballast bonus", true, "$", "", false, createShippingBallastBonus(options, false)));
			rows.add(createRow(130, "Charter Duration", true, "", "", false, createCharterDays(options, false)));
		}
		if (containsCargo) {
			rows.add(createRow(140, "Sale", true, "$", "", false, createSellValuePrice(options, false)));
			rows.add(createRow(150, "    Price", true, "$", "", false, createSellPrice(options, false)));
			rows.add(createRow(160, "    Volume", true, "", "", false, createSellVolumeMMBTuPrice(options, false)));
			if (SecurityUtils.getSubject().isPermitted("features:report-equity-book")) {
				rows.add(createRow(170, "Equity P&L", true, "$", "", false, createPNLEquity(options, false)));
			}
			rows.add(createRow(180, "Addn. P&L", true, "$", "", false, createPNLAdditional(options, false)));
		}
		rows.add(createRow(190, "P&L", true, "$", "", false, createPNLTotal(options, false)));
		if (containsCargo) {
			final CargoEconsReportRow row = createRow(200, "Margin", true, "$", "", false, createPNLPerMMBTU(options, false));
			row.tooltip = () -> {
				switch (options.marginBy) {
				case PURCHASE_VOLUME:
					return "Margin by purchase volume";
				case SALE_VOLUME:
					return "Margin by sales volume";
				default:
					return null;

				}
			};
			rows.add(row);

			//Pnl calcs start.
			rows.add(createRow(205, "", false, "", "", false, createEmptyFormatter()));

			rows.add(createRow(206, "Buy port", false, "", "", false, createFirstPurchaseFormatter(sa -> sa.getPort() != null ? sa.getPort().getName() : "")));
			rows.add(createRow(210, "Buy date", false, "", "", false,
					createFirstPurchaseFormatter(sa -> sa.getSlotVisit().getStart().format(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter()))));
			rows.add(createRow(220, "Buy volume (m3)", false, "", "", false,
					createBasicFormatter(options, false, Integer.class, VolumeM3Format::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getVolumeTransferred))));
			rows.add(createRow(230, "Buy volume (mmBtu)", false, "", "", false,
					createBasicFormatter(options, false, Integer.class, VolumeMMBtuFormat::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getEnergyTransferred))));
			//CV should have units: mmbtu/m^3, although since not shown in slot editor, will not show here?
			rows.add(createRow(240, "Buy CV (mmBtu/m3)", false, "", "", false, createBasicFormatter(options, false, Double.class, CVFormat::format,
					createFullLegTransformer2(Double.class, 0, (visit, travel, idle) -> {
						if (visit != null && visit.getSlotAllocation() != null) {
							return visit.getSlotAllocation().getCv();
						}
						else {
							return 0.0;
						}
					}))));
			rows.add(createRow(250, "Buy price($/mmBtu)", false, "", "", false,
					createBasicFormatter(options, false, Double.class, DollarsPerMMBtuFormat::format, createFirstPurchaseTransformer(Double.class, SlotAllocation::getPrice))));

			rows.add(createRow(260, "Sale port", false, "", "", false, createFirstSaleAllocationFormatter(sa -> sa.getPort().getName())));
			rows.add(createRow(270, "Sale date", false, "", "", false,
					createFirstSaleAllocationFormatter(sa -> sa.getSlotVisit().getStart().format(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter()))));
			rows.add(createRow(280, "Sale price($/mmBtu)", false, "", "", false,
					createBasicFormatter(options, false, Double.class, DollarsPerMMBtuFormat::format, createFirstSaleTransformer(Double.class, SlotAllocation::getPrice))));
			rows.add(createRow(290, "Sale volume (m3)", false, "", "", false,
					createBasicFormatter(options, false, Integer.class, VolumeM3Format::format, createFirstSaleTransformer(Integer.class, SlotAllocation::getVolumeTransferred))));
			rows.add(createRow(300, "Sale volume (mmBtu)", false, "", "", false,
					createBasicFormatter(options, false, Integer.class, VolumeMMBtuFormat::format, createFirstSaleTransformer(Integer.class, SlotAllocation::getEnergyTransferred))));

			// Spacer
			rows.add(createRow(310, "", false, "", "", false, createEmptyFormatter()));

			rows.add(createRow(320, "Purchase cost", false, "", "", true,
					createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getVolumeValue))));
			rows.add(createRow(330, "Sales revenue", false, "", "", false,
					createBasicFormatter(options, false, Integer.class, DollarsFormat::format, createFirstSaleTransformer(Integer.class, SlotAllocation::getVolumeValue))));
			// Equity
			rows.add(createRow(340, "Equity", false, "", "", false, createEmptyFormatter()));
			rows.add(createRow(350, "Theoretical shipping cost", false, "", "", false, createEmptyFormatter()));

			// Theoretical shipping cost
			// Real Shipping cost

			// getShippingCost((fu) -> getFuelcost(fu, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT));
			// rows.add(createRow(200, "Real shipping cost", false, "$", "", true, createShippingCosts((fu) -> getShippingCost(StandardPNLCalcRowFactory::getShippingCost(fuelCostFunc) )));

			Function<FuelUsage, Integer> fuelCostFunc = (fu) -> getFuelCost(fu, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
			Function<SlotVisit, Integer> portCostFunc = SlotVisit::getPortCost;
			Function<Object, Integer> func2 = (object) -> getShippingCost(object, portCostFunc, fuelCostFunc);

			rows.add(createRow(400, "Real shipping cost", false, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, func2))));
			rows.add(createRow(405, "PNL", false, "$", "", false,
					createBasicFormatter(options, false, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getPNLValue))));

			// Spacer
			rows.add(createRow(410, "", false, "", "", false, createEmptyFormatter()));
			rows.add(createRow(420, "", false, "", "", false, createEmptyFormatter()));

			rows.add(createRow(400, "Real shipping", false, "", "", false, createEmptyFormatter()));

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
						createBasicFormatter(options, true, Double.class, SpeedFormat::format, createFullLegTransformer2(Double.class, legIdx, (visit, travel, idle) -> travel == null ? 0 : travel.getSpeed()))));
				rows.add(createRow(base + 30, "    Days", false, "", "", false, createDoubleDaysFormatter(options, true,  createFullLegTransformer2(Double.class, legIdx,
						(visit, travel, idle) -> ((getOrZero(visit, Event::getDuration) + getOrZero(travel, Event::getDuration) + getOrZero(idle, Event::getDuration)) / 24.0)))));

				// rows.add(createRow(base + 90, " Route", false, "", "", false, createFullLegFormatter(legIdx, (travel, idle) -> getRoute(travel.getRouteOption()))));
				// rows.add(createRow(base + 91, " Port duration", false, "", "", false, createFullLegFormatter(legIdx, (travel, idle) -> getPortDuration(travel))));
				// rows.add(createRow(base + 31, " Port days", false, "", "", false, createFullLegFormatter2(legIdx, Double.class, DaysFormat::format,
				// (visit, travel, idle) -> ((getOrZero(visit, Event::getDuration)) / 24.0))));

				rows.add(createRow(base + 40, "    Total BO (mmBtu)", false, "", "", false, createBasicFormatter(options, true, Integer.class, VolumeMMBtuFormat::format,
						createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MMBTU, Fuel.NBO, Fuel.FBO))))));
				rows.add(createRow(base + 50, "    Charter Cost", true, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFullLegTransformer2(Integer.class,
						legIdx, (visit, travel, idle) -> (getOrZero(visit, Event::getCharterCost) + getOrZero(travel, Event::getCharterCost) + getOrZero(idle, Event::getCharterCost))))));

				rows.add(createRow(base + 51, "    Charter Rate", true, "$", "", true,
						createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> getOrZero(visit, event -> {
							return (int) ((double) event.getCharterCost() * 24 / (double) event.getDuration());
						})))));

				rows.add(createRow(base + 60, "    Bunkers (MT)", false, "", "", false, createBasicFormatter(options, true, Integer.class, VolumeM3Format::format,
						createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MT, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))))));
				rows.add(createRow(base + 70, "    Bunkers cost", false, "", "", false, createBasicFormatter(options, true, Integer.class, DollarsFormat::format,
						createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getFuelCost(visit, travel, idle, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))))));
				rows.add(createRow(base + 80, "    Port Costs ", false, "", "", false,
						createBasicFormatter(options, true, Integer.class, DollarsFormat::format, createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (visit.getPortCost())))));
				rows.add(createRow(base + 90, "    Route", false, "", "", false,
						createBasicFormatter(options, false, String.class, Object::toString, createFullLegTransformer(String.class, legIdx, (travel, idle) -> travel == null ? "" : getRoute(travel.getRouteOption())))));
				rows.add(createRow(base + 100, "    Canal Cost", true, "$", "", true, createBasicFormatter(options, true, Integer.class, DollarsFormat::format,
						createFullLegTransformer2(Integer.class, legIdx, (visit, travel, idle) -> (getOrZero(travel, Journey::getToll))))));

				if (legIdx != 0) {
					rows.add(createRow(base + 109, "    Cooldown cost ($)", true, "$", "", true, createEmptyFormatter()));
				}

				rows.add(createRow(base + 110, "    Total cost ($)", true, "$", "", true, createBasicFormatter(options, true, Long.class, DollarsFormat::format,
						createFullLegTransformer2(Long.class, legIdx, (visit, travel, idle) -> (getEventShippingCost(visit) + getEventShippingCost(travel) + getEventShippingCost(idle))))));
				rows.add(createRow(base + 120, "    Idle days", false, "", "", false, createDoubleDaysFormatter(options, true, 
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
		}
		
		return rows;
	}

	private @Nullable IColorProvider createBOGColourProvider(final EconsOptions options) {

		return new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {

				return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

			}

			@Override
			public Color getBackground(final Object element) {
				return null;
			}
		};
	}

	private @Nullable IColorProvider createCharterFeesColourProvider(final EconsOptions options) {

		return new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {

				if (element instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) element;
					final boolean isFleet = cargoAllocation.getSequence() != null && (!(cargoAllocation.getSequence().isSpotVessel() || cargoAllocation.getSequence().isTimeCharterVessel()));
					if (isFleet) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				} else if (element instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) element;
					final boolean isFleet = vesselEventVisit.getSequence() != null && (!(vesselEventVisit.getSequence().isSpotVessel() || vesselEventVisit.getSequence().isTimeCharterVessel()));
					if (isFleet) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}

				return null;
			}

			@Override
			public Color getBackground(final Object element) {
				return null;
			}

		};
	}

	private @NonNull ICellRenderer createBuyValuePrice(final EconsOptions options, final boolean isCost) {

		final Function<Object, Long> helper = object -> {
			Long cost = 0L;
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;

				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
						cost += allocation.getVolumeValue();
					}
				}
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getVolumeValue();
			}
			return cost;
		};

		return createBasicFormatter(options, isCost, Long.class, DollarsFormat::format, createMappingFunction(Long.class, helper));
	}

	private @NonNull ICellRenderer createBuyPrice(final EconsOptions options, final boolean isCost) {
		final Function<Object, Double> helper = object -> {
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;

				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
						return allocation.getPrice();
					}
				}
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				return allocation.getPrice();
			}
			return 0.0;
		};

		return createBasicFormatter(options, isCost, Double.class, DollarsPerMMBtuFormat::format, createMappingFunction(Double.class, helper));
	}

	private @NonNull ICellRenderer createSellPrice(final EconsOptions options, final boolean isCost) {
		final Function<Object, Double> helper = object -> {
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;

				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
						return allocation.getPrice();
					}
				}
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				return allocation.getPrice();
			}
			return 0.0;
		};

		return createBasicFormatter(options, isCost, Double.class, DollarsPerMMBtuFormat::format, createMappingFunction(Double.class, helper));
	}

	private @NonNull ICellRenderer createSellValuePrice(final EconsOptions options, final boolean isCost) {

		final Function<Object, @Nullable Long> helper = object -> {
			Long cost = 0L;
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;

				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
						cost += allocation.getVolumeValue();
					}
				}
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getVolumeValue();
			}
			return cost;
		};

		return createBasicFormatter(options, isCost, Long.class, DollarsFormat::format, createMappingFunction(Long.class, helper));
	}

	private @NonNull ICellRenderer createBuyVolumeMMBTuPrice(final EconsOptions options, final boolean isCost) {

		final Function<Object, @Nullable Long> helper = object -> {
			Long cost = 0L;
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;

				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
						cost += allocation.getEnergyTransferred();
					}
				}
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getEnergyTransferred();
			}
			return cost;
		};

		return createBasicFormatter(options, isCost, Long.class, VolumeMMBtuFormat::format, createMappingFunction(Long.class, helper));
	}

	private @NonNull ICellRenderer createSellVolumeMMBTuPrice(final EconsOptions options, final boolean isCost) {
		final Function<Object, @Nullable Long> helper = object -> {
			Long cost = 0L;
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;

				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
						cost += allocation.getEnergyTransferred();
					}
				}
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getEnergyTransferred();
			}
			return cost;
		};

		return createBasicFormatter(options, isCost, Long.class, VolumeMMBtuFormat::format, createMappingFunction(Long.class, helper));
	}

	private static double cargoAllocationPerMMBTUVolumeHelper(final Object object, final EconsOptions options) {

		if (!(object instanceof CargoAllocation)) {
			return 0.0f;
		}

		if (options.marginBy != MarginBy.PURCHASE_VOLUME && options.marginBy != MarginBy.SALE_VOLUME) {
			return 0.0f;
		}

		final CargoAllocation cargoAllocation = (CargoAllocation) object;
		double volume = 0.0;

		if (options.marginBy == MarginBy.PURCHASE_VOLUME) {
			for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
				if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
					volume += allocation.getEnergyTransferred();
				}
			}
		} else if (options.marginBy == MarginBy.SALE_VOLUME) {
			for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
				if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
					volume += allocation.getEnergyTransferred();
				}
			}
		}

		if (volume == 0.0) {
			return 0.0f;
		}

		return volume;
	}

	private double cargoAllocationPNLPerMMBTUPNLHelper(final Object object) {
		if (!(object instanceof CargoAllocation)) {
			return 0.0f;
		}

		final CargoAllocation cargoAllocation = (CargoAllocation) object;
		final Integer pnl = getPNLValue(cargoAllocation);

		return (double) pnl;
	}

	private @NonNull ICellRenderer createPNLPerMMBTU(final EconsOptions options, final boolean isCost) {

		final Function<Object, @Nullable Double> helper = object -> {
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;

				final Integer pnl = getPNLValue(cargoAllocation);
				if (pnl == null) {
					return 0.0;
				}

				final double volume = cargoAllocationPerMMBTUVolumeHelper(object, options);
				if (volume == 0.0) {
					return 0.0;
				}

				return (double) pnl / volume;
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;

				final Integer pnl = getPNLValue(marketAllocation);
				if (pnl != null) {
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					final double volume = allocation.getEnergyTransferred();

					return (double) pnl / volume;
				}
			}
			return null;
		};

		final Function<Object, @Nullable Double> transformer = object -> {
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;
				return helper.apply(cargoAllocation);
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				return helper.apply(marketAllocation);
			} else if (object instanceof CargoAllocationPair) {
				return getFromCargoAllocationPair(Double.class, helper, object);
			} else if (object instanceof List<?>) {
				final List<DeltaPair> pairs = (List<DeltaPair>) object;
				final double totalVolume = pairs.stream().mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.first(), options)).sum();
				final double totalPNL = pairs.stream().mapToDouble(x -> cargoAllocationPNLPerMMBTUPNLHelper(x.first())).sum();
				final double totalOldVolume = pairs.stream().mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.second(), options)).sum();
				final double totalOldPNL = pairs.stream().mapToDouble(x -> cargoAllocationPNLPerMMBTUPNLHelper(x.second())).sum();
				final double value = (double) (totalOldPNL / totalOldVolume) - (double) (totalPNL / totalVolume);
				return value;
			}
			return null;
		};

		return createBasicFormatter(options, isCost, Double.class, DollarsPerMMBtuFormat::format, transformer);
	}

	private @NonNull ICellRenderer createPNLTotal(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getPNLValue));
	}

	private @NonNull ICellRenderer createPNLEquity(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getEquityPNLValue));
	}

	private @NonNull ICellRenderer createPNLAdditional(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getAdditionalPNLValue));
	}

	private static int genericShippingBOGTotalHelper(final Object object) {
		if (object instanceof EventGrouping) {
			int cost = 0;

			final EventGrouping grouping = (EventGrouping) object;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					cost += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
				}
			}
			return cost;
		}
		return 0;

	}

	private @NonNull ICellRenderer createShippingBOGTotal(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingBOGTotalHelper));
	}

	private static int genericShippingBunkersTotalHelper(final Object object) {

		if (object instanceof EventGrouping) {
			int cost = 0;
			final EventGrouping grouping = (EventGrouping) object;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					cost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				}
			}

			return cost;
		}
		return 0;
	}

	private @NonNull ICellRenderer createShippingBunkersTotal(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingBunkersTotalHelper));
	}

	private static int genericShippingPortCostsHelper(final Object object) {

		if (object instanceof EventGrouping) {
			final EventGrouping grouping = (EventGrouping) object;

			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof PortVisit) {
					final PortVisit portVisit = (PortVisit) event;
					cost += portVisit.getPortCost();
				}
			}

			return cost;
		}
		return 0;
	}

	private @NonNull ICellRenderer createShippingPortCosts(final EconsOptions options, final boolean isCost) {

		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingPortCostsHelper));
	}

	private static int genericShippingCanalCostsHelper(final Object object) {
		if (object instanceof EventGrouping) {
			final EventGrouping grouping = (EventGrouping) object;

			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					cost += journey.getToll();
				}
			}
			return cost;
		}
		return 0;
	}

	private @NonNull ICellRenderer createShippingCanalCosts(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingCanalCostsHelper));
	}

	private static int genericShippingCharterCostsHelper(final Object object) {

		if (object instanceof EventGrouping) {
			final EventGrouping grouping = (EventGrouping) object;
			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				cost += event.getCharterCost();
			}
			return cost;
		}

		return 0;
	}

	private @NonNull ICellRenderer createShippingCharterCosts(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingCharterCostsHelper));
	}

	private @NonNull ICellRenderer createShippingPurgeCosts(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, object -> {
			if (object instanceof EventGrouping) {
				final EventGrouping eventGrouping = (EventGrouping) object;

				for (final Event evt : eventGrouping.getEvents()) {
					if (evt instanceof Purge) {
						final Purge purge = (Purge) evt;
						final int cost = purge.getCost();
						return cost;
					}
				}
			}
			return 0;
		}));
	}
	private @NonNull ICellRenderer createShippingCooldownCosts(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, object -> {
			if (object instanceof EventGrouping) {
				final EventGrouping eventGrouping = (EventGrouping) object;
				
				for (final Event evt : eventGrouping.getEvents()) {
					if (evt instanceof Cooldown) {
						final Cooldown cooldown = (Cooldown) evt;
						final int cost = cooldown.getCost();
						return cost;
					}
				}
			}
			return 0;
		}));
	}

	private static int vesselEventVisitShippingCharterRevenueHelper(final Object object) {
		int revenue = 0;

		if (object instanceof VesselEventVisit) {
			final VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				revenue = charterOutEvent.getHireRate() * charterOutEvent.getDurationInDays();
			}
		}
		return revenue;
	}

	private @NonNull ICellRenderer createShippingCharterRevenue(final EconsOptions options, final boolean isCost) {

		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingCharterRevenueHelper));
	}

	private static int vesselEventVisitShippingBallastBonusHelper(final Object object) {
		int revenue = 0;

		if (object instanceof VesselEventVisit) {
			final VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				revenue = charterOutEvent.getBallastBonus();
			}
		}
		return revenue;
	}

	private @NonNull ICellRenderer createShippingBallastBonus(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingBallastBonusHelper));
	}

	private static int vesselEventVisitCharterDaysHelper(final Object object) {
		int days = 0;

		if (object instanceof VesselEventVisit) {
			final VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				days = charterOutEvent.getDurationInDays();
			}
		}
		return days;
	}

	private @NonNull ICellRenderer createCharterDays(final EconsOptions options, final boolean isCost) {
		return createIntegerDaysFormatter(options, isCost, createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitCharterDaysHelper));
	}

	private static int vesselEventVisitShippingRepositioningHelper(final Object object) {
		int revenue = 0;

		if (object instanceof VesselEventVisit) {
			final VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				revenue = charterOutEvent.getRepositioningFee();
			}
		}
		return revenue;
	}

	private @NonNull ICellRenderer createShippingRepositioning(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingRepositioningHelper));
	}

	private @NonNull ICellRenderer createShippingCostsByMMBTU(final EconsOptions options, final boolean isCost) {

		final Function<Object, @Nullable Double> helper = object -> getShippingCostByMMBTU(options, object);

		final Function<Object, @Nullable Double> transformer = object -> {
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;
				return helper.apply(cargoAllocation);
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				return helper.apply(marketAllocation);
			} else if (object instanceof VesselEventVisit) {
				final VesselEventVisit eventVisit = (VesselEventVisit) object;
				return helper.apply(eventVisit);
			} else if (object instanceof CargoAllocationPair) {
				return getFromCargoAllocationPair(Double.class, helper, object);
			} else if (object instanceof List<?>) {
				final List<DeltaPair> pairs = (List<DeltaPair>) object;
				final double totalVolume = pairs.stream().mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.first(), options)).sum();
				final double totalCost = pairs.stream().mapToDouble(x -> getShippingCost(x.first())).sum();

				final double totalOldVolume = pairs.stream().mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.second(), options)).sum();
				final double totalOldCost = pairs.stream().mapToDouble(x -> getShippingCost(x.second())).sum();
				final double value = (double) (totalOldCost / totalOldVolume) - (double) (totalCost / totalVolume);
				return value;
			}
			return null;
		};

		return createBasicFormatter(options, isCost, Double.class, DollarsPerMMBtuFormat::format, transformer);
	}

	private @NonNull ICellRenderer createShippingCosts(final EconsOptions options, final boolean isCost) {
		return createBasicFormatter(options, isCost, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getShippingCost));
	}

	private static Double getShippingCostByMMBTU(final EconsOptions options, final Object object) {

		if (object == null) {
			return null;
		}

		if (object instanceof CargoAllocation) {
			final double volume = cargoAllocationPerMMBTUVolumeHelper(object, options);
			if (volume == 0.0) {
				return 0.0;
			}
			final Integer shippingCost = getShippingCost(object);
			if (shippingCost != null) {
				return (double) shippingCost / volume;
			}
		}

		return null;

	}

	private static Integer getShippingCost(final Object object) {

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
		} else if (object instanceof CharterLengthEvent) {
			sequence = ((CharterLengthEvent) object).getSequence();
			events = ((CharterLengthEvent) object).getEvents();
		} else if (object instanceof StartEvent) {
			sequence = ((StartEvent) object).getSequence();
			events = ((StartEvent) object).getEvents();
		} else if (object instanceof Purge) {
			sequence = ((Purge) object).getSequence();
			events = Collections.singletonList((Purge) object);
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

			if (event instanceof Purge) {
				final Purge purge = (Purge) event;
				shippingCost += purge.getCost();
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

	private static Integer getEquityPNLValue(final Object object) {
		if (object instanceof ProfitAndLossContainer) {
			final ProfitAndLossContainer container = (ProfitAndLossContainer) object;

			int equityPNL = 0;
			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				final EList<EntityProfitAndLoss> entityProfitAndLosses = dataWithKey.getEntityProfitAndLosses();
				for (final EntityProfitAndLoss entityProfitAndLoss : entityProfitAndLosses) {
					if (entityProfitAndLoss.getEntityBook().equals(entityProfitAndLoss.getEntity().getUpstreamBook())) {
						equityPNL += entityProfitAndLoss.getProfitAndLoss();
					}
				}
			}

			return equityPNL;
		}
		return null;
	}

	private static Integer getAdditionalPNLValue(final Object object) {

		if (object instanceof ProfitAndLossContainer) {
			int addnPNL = 0;
			final ProfitAndLossContainer container = (ProfitAndLossContainer) object;
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

			return addnPNL;
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

			if (event instanceof Purge) {
				final Purge purge = (Purge) event;
				shippingCost += purge.getCost();
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

			if (event instanceof Purge) {
				final Purge purge = (Purge) event;
				shippingCost += purge.getCost();
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
}
