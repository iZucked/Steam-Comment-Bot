/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Sets;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.views.standard.econs.EconsOptions.MarginBy;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.LumpSumBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.LumpSumRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class StandardEconsRowFactory extends AbstractEconsRowFactory {

	@Override
	public Collection<CargoEconsReportRow> createRows(@NonNull final EconsOptions options, @Nullable final Collection<Object> targets) {

		boolean containsCargo = false;
		boolean containsCharterOut = false;
		boolean containsCooldown = false;
		boolean containsPurge = false;
		boolean containsVesselEvent = false;
		boolean containsGeneratedCharterOut = false;
		boolean containsStartEvent = false;
		boolean containsEndEvent = false;
		boolean containsOpenSlot = false;
		boolean containsPaperDeals = false;
		boolean containsInCharterBallastBonus = false;
		boolean containsInCharterRepositioning = false;
		boolean complexCargo = false;
		int ladenLegs = 1;
		int ballastLegs = 1;

		int numLoads = 0;
		int numDischarges = 0;

		if (targets == null || targets.isEmpty()) {
			containsCargo = true;
		} else {
			for (final Object target : targets) {
				if (target instanceof final CargoAllocation cargoAllocation) {
					final Sequence sequence = cargoAllocation.getSequence();
					if (sequence != null && sequence.getCharterInMarket() != null && sequence.getSpotIndex() < 0) {
						final GenericCharterContract genericCharterContract = sequence.getCharterInMarket().getGenericCharterContract();
						if (genericCharterContract != null) {
							containsInCharterBallastBonus = true;
							containsInCharterRepositioning = true;
						}
					}
					containsCargo = true;
					complexCargo |= cargoAllocation.getSlotAllocations().size() > 2;
					// Assuming LDD - multiple laden legs with one final ballast leg
					ladenLegs = Math.max(ladenLegs, cargoAllocation.getSlotAllocations().size() - 1);
					ballastLegs = Math.max(ballastLegs, 1);

					for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
						if (sa.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
							numLoads++;
						} else {
							numDischarges++;
						}
					}
				}
				if (target instanceof OpenSlotAllocation) {
					containsOpenSlot = true;
				}
				if (target instanceof final StartEvent evt) {
					containsStartEvent = true;
					final Sequence sequence = evt.getSequence();
					if (sequence != null && sequence.getVesselCharter() != null) {
						final GenericCharterContract genericCharterContract = sequence.getVesselCharter().getGenericCharterContract();
						if (genericCharterContract != null) {
							containsInCharterBallastBonus = true;
							containsInCharterRepositioning = true;
						}
					}
					if (sequence != null && sequence.getCharterInMarket() != null && sequence.getSpotIndex() >= 0) {
						final GenericCharterContract genericCharterContract = sequence.getCharterInMarket().getGenericCharterContract();
						if (genericCharterContract != null) {
							containsInCharterBallastBonus = true;
							containsInCharterRepositioning = true;
						}
					}
				}
				if (target instanceof final EndEvent evt) {
					containsEndEvent = true;
					final Sequence sequence = evt.getSequence();
					if (sequence != null && sequence.getVesselCharter() != null) {
						final GenericCharterContract genericCharterContract = sequence.getVesselCharter().getGenericCharterContract();
						if (genericCharterContract != null) {
							containsInCharterBallastBonus = true;
							containsInCharterRepositioning = true;
						}
					}
					if (sequence != null && sequence.getCharterInMarket() != null && sequence.getSpotIndex() >= 0) {
						final GenericCharterContract genericCharterContract = sequence.getCharterInMarket().getGenericCharterContract();
						if (genericCharterContract != null) {
							containsInCharterBallastBonus = true;
							containsInCharterRepositioning = true;
						}
					}
				}
				if (target instanceof final VesselEventVisit vesselEventVisit) {
					if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent) {
						containsCharterOut = true;
					}
				}
				if (target instanceof final EventGrouping eg) {
					for (final Event e : eg.getEvents()) {
						if (e instanceof StartEvent) {
							containsStartEvent = true;
						}
						if (e instanceof VesselEventVisit) {
							containsVesselEvent = true;
						}
						if (e instanceof GeneratedCharterOut) {
							containsGeneratedCharterOut = true;
						}
						if (e instanceof Cooldown) {
							containsCooldown = true;
						}
						if (e instanceof Purge) {
							containsPurge = true;
						}
					}
				}
				if (target instanceof PaperDealAllocation) {
					containsPaperDeals = true;
				}
			}
		}

		final List<CargoEconsReportRow> rows = new LinkedList<>();
		if (containsCargo) {
			for (int i = 0; i < numLoads; ++i) {
				final int base = EconsRowMarkers.PURCHASES_START + 10 * i;
				rows.add(createRow(base + 0, "Purchase", true, "$", "", createBuyValuePrice(i, options, RowType.COST)));
				rows.add(createRow(base + 1, "    Price", true, "$", "", createBuyPrice(i, options, RowType.COST)));
				rows.add(createRow(base + 2, "    Volume", true, "", "", createBuyVolumeMMBTuPrice(i, options, RowType.COST)));
			}
		}
		if (containsCharterOut || containsGeneratedCharterOut) {
			rows.add(createRow(EconsRowMarkers.CHATRTER_START + 0, "Charter Revenue", true, "$", "", createShippingCharterRevenue(options, RowType.REVENUE)));
			rows.add(createRow(EconsRowMarkers.CHATRTER_START + 20, "    Hire Rate", true, "$", "/day", createMeanCharterRatePerDay(options, RowType.REVENUE)));
			rows.add(createRow(EconsRowMarkers.CHATRTER_START + 30, "    Charter Duration", true, "", "", createCharterDays(options, RowType.REVENUE)));
			rows.add(createRow(EconsRowMarkers.CHATRTER_START + 40, "Charter Repositioning", true, "$", "", createCharterOutRepositioning(options, RowType.REVENUE)));
			rows.add(createRow(EconsRowMarkers.CHATRTER_START + 50, "Charter Ballast bonus", true, "$", "", createCharterOutBallastBonus(options, RowType.REVENUE)));
		}

		final boolean containsShippingEvent = containsCargo || containsCharterOut || containsCooldown || containsGeneratedCharterOut || containsPurge || containsStartEvent || containsVesselEvent;
		if (containsShippingEvent || containsEndEvent) {
			rows.add(createRow(EconsRowMarkers.SHIPPING_START + 0, "Shipping", true, "$", "", createShippingCosts(options, RowType.COST)));
			if (containsShippingEvent) {
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 10, "    Bunkers", true, "$", "", createShippingBunkersTotal(options, RowType.COST)));
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 20, "    Port", true, "$", "", createShippingPortCosts(options, RowType.COST)));
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 30, "    Canal", true, "$", "", createShippingCanalCosts(options, RowType.COST)));
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 40, "    Boil-off", true, "$", "", createShippingBOGTotal(options, RowType.COST), createBOGColourProvider(options)));
				rows.add(
						createRow(EconsRowMarkers.SHIPPING_START + 50, "    Charter Cost", true, "$", "", createShippingCharterCosts(options, RowType.COST), createCharterFeesColourProvider(options)));
			}
			if (containsInCharterRepositioning) {
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 60, "    Repositioning", true, "$", "", createCharterInRepositioning(options, RowType.COST)));
			}
			if (containsInCharterBallastBonus) {
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 70, "    Ballast bonus", true, "$", "", createCharterInBallastBonus(options, RowType.COST)));
			}

			if (containsCargo) {
				final CargoEconsReportRow row = createRow(EconsRowMarkers.SHIPPING_START + 80, "    $/mmBtu", true, "$", "", createShippingCostsByMMBTU(options, RowType.COST));
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
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 90, "    Purge Cost", true, "$", "", createShippingPurgeCosts(options, RowType.COST)));
			}
			if (containsCooldown) {
				rows.add(createRow(EconsRowMarkers.SHIPPING_START + 100, "    Cooldown Cost", true, "$", "", createShippingCooldownCosts(options, RowType.COST)));
			}
		}
		if (containsCargo) {
			for (int i = 0; i < numDischarges; ++i) {
				final int base = EconsRowMarkers.SALES_START + 10 * i;
				rows.add(createRow(base + 1, "Sale", true, "$", "", createSellValuePrice(i, options, RowType.REVENUE)));
				rows.add(createRow(base + 2, "    Price", true, "$", "", createSellPrice(i, options, RowType.REVENUE)));
				rows.add(createRow(base + 3, "    Volume", true, "", "", createSellVolumeMMBTuPrice(i, options, RowType.REVENUE)));
			}
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_HEADLINE_EQUITY_BOOK)) {
				rows.add(createRow(EconsRowMarkers.PNL_START + 0, "Equity P&L", true, "$", "", createPNLEquity(options, RowType.REVENUE)));
			}
			rows.add(createRow(EconsRowMarkers.PNL_START + 10, "Addn. P&L", true, "$", "", createPNLAdditional(options, RowType.REVENUE)));
		}
		if (containsOpenSlot) {
			rows.add(createRow(EconsRowMarkers.PNL_START + 20, "Cancellation", true, "$", "", createCancellationCosts(options, RowType.COST)));
		}
		if (containsCargo || containsCharterOut || containsCooldown || containsGeneratedCharterOut || containsOpenSlot || containsPurge || containsStartEvent || containsVesselEvent
				|| containsEndEvent) {
			rows.add(createRow(EconsRowMarkers.PNL_START + 30, "P&L", true, "$", "", createPNLTotal(options, RowType.REVENUE)));
		}
		if (containsCargo) {
			final CargoEconsReportRow row = createRow(EconsRowMarkers.PNL_START + 40, "Margin", true, "$", "", createPNLPerMMBTU(options, RowType.REVENUE));
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

			if (options.showPnLCalcs) {
				// Pnl calcs start.
				rows.add(createRow(EconsRowMarkers.DETAILS_START, "", false, "", "", createEmptyFormatter()));

				for (int i = 0; i < numLoads; ++i) {
					final int base = EconsRowMarkers.BUY_SELLS_START + EconsRowMarkers.BUY_SELL_LENGTH * i;
					final String lbl = i > 1 ? "Buy #" + (i + 1) : "Buy";

					rows.add(createRow(base + 0, lbl + " port", false, "", "", createFirstPurchaseFormatter(sa -> sa.getPort() != null ? sa.getPort().getName() : "")));
					rows.add(createRow(base + 10, lbl + " date", false, "", "",
							createFirstPurchaseFormatter(sa -> sa.getSlotVisit().getStart().format(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter()))));
					rows.add(createRow(base + 20, lbl + " volume (m3)", false, "", "",
							createBasicFormatter(options, RowType.COST, Integer.class, VolumeM3Format::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getVolumeTransferred))));
					rows.add(createRow(base + 30, lbl + " volume (mmBtu)", false, "", "", createBasicFormatter(options, RowType.COST, Integer.class, VolumeMMBtuFormat::format,
							createFirstPurchaseTransformer(Integer.class, SlotAllocation::getEnergyTransferred))));
					// CV should have units: mmbtu/m^3, although since not shown in slot editor,
					// will not show here?
					rows.add(createRow(base + 40, lbl + " CV (mmBtu/m3)", false, "", "",
							createBasicFormatter(options, RowType.COST, Double.class, CVFormat::format, createFullLegTransformer2(Double.class, true, 0, (visit, travel, idle) -> {
								if (visit != null && visit.getSlotAllocation() != null) {
									return visit.getSlotAllocation().getCv();
								} else {
									return 0.0;
								}
							}))));
					rows.add(createRow(base + 50, "Buy price($/mmBtu)", false, "", "",
							createBasicFormatter(options, RowType.COST, Double.class, DollarsPerMMBtuFormat::format, createFirstPurchaseTransformer(Double.class, SlotAllocation::getPrice))));
				}
				for (int i = 0; i < numDischarges; ++i) {
					final int base = EconsRowMarkers.BUY_SELLS_START + EconsRowMarkers.BUY_SELL_LENGTH * (numLoads + i);
					final String lbl = i > 0 ? "Sale #" + (i + 1) : "Sale";
					rows.add(createRow(base + 0, lbl + " port", false, "", "", createSaleAllocationFormatter(1, sa -> sa.getPort() == null ? "" : sa.getPort().getName())));
					rows.add(createRow(base + 10, lbl + " date", false, "", "",
							createSaleAllocationFormatter(1, sa -> sa.getSlotVisit().getStart().format(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter()))));
					rows.add(createRow(base + 20, lbl + " price($/mmBtu)", false, "", "",
							createBasicFormatter(options, RowType.REVENUE, Double.class, DollarsPerMMBtuFormat::format, createSaleTransformer(1, Double.class, SlotAllocation::getPrice))));
					rows.add(createRow(base + 30, lbl + " volume (m3)", false, "", "",
							createBasicFormatter(options, RowType.REVENUE, Integer.class, VolumeM3Format::format, createSaleTransformer(1, Integer.class, SlotAllocation::getVolumeTransferred))));
					rows.add(createRow(base + 40, lbl + " volume (mmBtu)", false, "", "",
							createBasicFormatter(options, RowType.REVENUE, Integer.class, VolumeMMBtuFormat::format, createSaleTransformer(1, Integer.class, SlotAllocation::getEnergyTransferred))));

				}

				// Omit the following rows for now as duplicated elsewhere.
				if (false) {
					// Spacer
					rows.add(createRow(310, "", false, "", "", createEmptyFormatter()));

					rows.add(createRow(320, "Purchase cost", false, "", "",
							createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format, createFirstPurchaseTransformer(Integer.class, SlotAllocation::getVolumeValue))));
					rows.add(createRow(330, "Sales revenue", false, "", "",
							createBasicFormatter(options, RowType.REVENUE, Integer.class, DollarsFormat::format, createSaleTransformer(1, Integer.class, SlotAllocation::getVolumeValue))));
					// Equity
					rows.add(createRow(340, "Equity", false, "", "", createEmptyFormatter()));
					rows.add(createRow(350, "Theoretical shipping cost", false, "", "", createEmptyFormatter()));

					// Theoretical shipping cost
					// Real Shipping cost

					final Function<FuelUsage, Integer> fuelCostFunc = fu -> getFuelCost(fu, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
					final Function<SlotVisit, Integer> portCostFunc = SlotVisit::getPortCost;
					final Function<Object, Integer> func2 = object -> getShippingCost(object, portCostFunc, fuelCostFunc);

					rows.add(createRow(400, "Real shipping cost", false, "$", "",
							createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, func2))));
					rows.add(createRow(405, "PNL", false, "$", "",
							createBasicFormatter(options, RowType.REVENUE, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getPNLValue))));
				}

				if (containsCargo || containsVesselEvent || containsCharterOut || containsGeneratedCharterOut || containsStartEvent) {

					// Spacer
					rows.add(createRow(EconsRowMarkers.LEGS_START - 1, "", false, "", "", createEmptyFormatter()));

					final int totalLegs = ladenLegs + ballastLegs;
					for (int tlegIdx = 0; tlegIdx < totalLegs; ++tlegIdx) {
						final int base = EconsRowMarkers.LEGS_START + EconsRowMarkers.LEGS_LENGTH * tlegIdx;

						// Only show ballast leg for anything other than a cargo.
						if (containsCargo || tlegIdx == 1) {

							final boolean laden = tlegIdx + 1 < totalLegs;
							final int legIdx = laden ? tlegIdx : tlegIdx - ladenLegs;
							if (laden) {
								if (ladenLegs > 1) {
									rows.add(createRow(base + 10, "Laden leg #" + (legIdx + 1), false, "", "", createEmptyFormatter()));
								} else {
									rows.add(createRow(base + 10, "Laden leg", false, "", "", createEmptyFormatter()));
								}
							} else {
								if (containsCargo) {
									rows.add(createRow(base + 9, "", false, "", "", createEmptyFormatter()));
								}
								if (ballastLegs > 1) {
									rows.add(createRow(base + 10, "Ballast leg #" + (legIdx + 1), false, "", "", createEmptyFormatter()));
								} else {
									rows.add(createRow(base + 10, "Ballast leg", false, "", "", createEmptyFormatter()));
								}
							}

							rows.add(createRow(base + 20, "    Speed", false, "", "", createBasicFormatter(options, RowType.COST, Double.class, SpeedFormat::format,
									createFullLegTransformer2(Double.class, laden, legIdx, (visit, travel, idle) -> travel == null ? 0 : travel.getSpeed()))));
							rows.add(createRow(base + 30, "    Days", false, "", "", createDoubleDaysFormatter(options, RowType.COST, createFullLegTransformer2(Double.class, laden, legIdx,
									(visit, travel, idle) -> ((getOrZero(visit, Event::getDuration) + getOrZero(travel, Event::getDuration) + getOrZero(idle, Event::getDuration)) / 24.0)))));

							rows.add(createRow(base + 40, "    Total BO (mmBtu)", false, "", "", createBasicFormatter(options, RowType.COST, Integer.class, VolumeMMBtuFormat::format,
									createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MMBTU, Fuel.NBO, Fuel.FBO))))));
							rows.add(createRow(base + 50, "    Charter Cost", true, "$", "",
									createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format, createFullLegTransformer2(Integer.class, laden, legIdx,
											(visit, travel, idle) -> (getOrZero(visit, Event::getCharterCost) + getOrZero(travel, Event::getCharterCost) + getOrZero(idle, Event::getCharterCost))))));

							rows.add(createRow(base + 51, "    Charter Rate", true, "$", "", createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format,
									createFullLegTransformer2(Integer.class, laden, legIdx, StandardEconsRowFactory::getAverageDailyCharterRate))));

							rows.add(createRow(base + 60, "    Bunkers (MT)", false, "", "",
									createBasicFormatter(options, RowType.COST, Integer.class, VolumeM3Format::format, createFullLegTransformer2(Integer.class, laden, legIdx,
											(visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MT, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))))));
							rows.add(createRow(base + 70, "    Bunkers cost", false, "", "", createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format,
									createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> (getFuelCost(visit, travel, idle, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))))));
							rows.add(createRow(base + 80, "    Port Costs ", false, "", "", createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format,
									createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> getPortCost(visit)))));
							rows.add(createRow(base + 90, "    Route", false, "", "", createBasicFormatter(options, RowType.OTHER, String.class, Object::toString,
									createFullLegTransformer2(String.class, laden, legIdx, (visit, travel, idle) -> travel == null ? "" : getRoute(travel.getRouteOption())))));
							rows.add(createRow(base + 100, "    Canal Cost", true, "$", "", createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format,
									createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> (getOrZero(travel, Journey::getToll))))));

							rows.add(createRow(base + 110, "    Total cost ($)", true, "$", "",
									createBasicFormatter(options, RowType.COST, Long.class, DollarsFormat::format, createFullLegTransformer2(Long.class, laden, legIdx,
											(visit, travel, idle) -> (getEventShippingCost(visit) + getEventShippingCost(travel) + getEventShippingCost(idle))))));
							rows.add(
									createRow(base + 120, "    Idle days", false, "", "",
											createDoubleDaysFormatter(options, RowType.COST,
													createFullLegTransformer2(Double.class, laden, legIdx, (visit, travel, idle) -> (getOrZero(idle, Event::getDuration) / 24.0))),
											greyColourProvider));
							rows.add(createRow(base + 130, "    Idle BO", false, "", "",
									createBasicFormatter(options, RowType.COST, Integer.class, VolumeMMBtuFormat::format,
											createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> (getFuelVolume(idle, FuelUnit.MMBTU, Fuel.NBO, Fuel.FBO)))),
									greyColourProvider));
							rows.add(createRow(base + 140, "    Idle charter", true, "$", "", createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format,
									createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> (getOrZero(idle, Event::getCharterCost)))), greyColourProvider));
							rows.add(createRow(base + 150, "    Idle bunkers", false, "", "",
									createBasicFormatter(options, RowType.COST, Integer.class, VolumeM3Format::format,
											createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> (getFuelVolume(idle, FuelUnit.MT, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT)))),
									greyColourProvider));

							rows.add(createRow(base + 160, "    Idle bunkers cost", false, "", "",
									createBasicFormatter(options, RowType.COST, Integer.class, DollarsFormat::format,
											createFullLegTransformer2(Integer.class, laden, legIdx, (visit, travel, idle) -> (getFuelCost(idle, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT)))),
									greyColourProvider));

							rows.add(createRow(base + 170, "    Total idle cost ($)", false, "", "", createBasicFormatter(options, RowType.COST, Long.class, DollarsFormat::format,
									createFullLegTransformer2(Long.class, laden, legIdx, (visit, travel, idle) -> (getEventShippingCost(idle)))), greyColourProvider));
						}
					}
				}
			}
		}
		if (containsPaperDeals) {
			final int base = EconsRowMarkers.PAPER_START;
			rows.add(createRow(base + 10, "Pricing", false, "", "", createEmptyFormatter()));

			rows.add(createRow(base + 20, "    Curve", false, "", "", createPaperDealAllocationFormatter(StandardEconsRowFactory::getFloatCurve)));
			rows.add(createRow(base + 30, "    Price", false, "", "", createPaperDealAllocationFormatter(pda -> String.format("%.3f", getPaperFloatPrice(pda)))));
			rows.add(createRow(base + 40, "    MtM", false, "", "", createPaperDealAllocationFormatter(pda -> String.format("%.3f", getPaperMtMPrice(pda)))));
			rows.add(createRow(base + 50, "Quantity", true, "", "", createPaperDealVolumeMMBTu(options, RowType.REVENUE)));
			rows.add(createRow(base + 60, "P&L", true, "$", "", createPNLTotal(options, RowType.REVENUE)));
			rows.add(createRow(base + 70, "Month", false, "", "",
					createPaperDealAllocationFormatter(pda -> pda.getPaperDeal().getStartDate().format(DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter()))));
		}
		return rows;
	}

	private double getPaperMtMPrice(final PaperDealAllocation pda) {
		double price = 0.0;
		if (pda.eContainer() instanceof final Schedule schedule) {
			if (schedule.getGeneratedPaperDeals().contains(pda.getPaperDeal())) {
				if (pda.getPaperDeal() != null)
					price = pda.getPaperDeal().getPrice();
			} else {
				if (pda.getEntries() != null && !pda.getEntries().isEmpty())
					price = pda.getEntries().get(0).getPrice();
			}
		}
		return price;
	}

	private double getPaperFloatPrice(final PaperDealAllocation pda) {
		double price = 0.0;
		if (pda.eContainer() instanceof final Schedule schedule) {
			if (!schedule.getGeneratedPaperDeals().contains(pda.getPaperDeal())) {
				if (pda.getPaperDeal() != null)
					price = pda.getPaperDeal().getPrice();
			} else {
				if (pda.getEntries() != null && !pda.getEntries().isEmpty())
					price = pda.getEntries().get(0).getPrice();
			}
		}
		return price;
	}

	private static String getFloatCurve(final PaperDealAllocation pda) {
		String curveName = "N/A";
		if (pda.eContainer() instanceof final Schedule schedule) {
			if (schedule.getGeneratedPaperDeals().contains(pda.getPaperDeal())) {
				curveName = pda.getPaperDeal().getIndex();
			}
		}
		return curveName;
	}

	private static int getAverageDailyCharterRate(final Event visit, final Event travel, final Event idle) {
		final int totalCharterCost = getOrZero(visit, Event::getCharterCost) + getOrZero(travel, Event::getCharterCost) + getOrZero(idle, Event::getCharterCost);
		final int totalDuration = getOrZero(visit, Event::getDuration) + getOrZero(travel, Event::getDuration) + getOrZero(idle, Event::getDuration);
		return totalDuration == 0.0 ? 0 : (int) ((double) totalCharterCost * 24.0 / (double) totalDuration);
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

				if (element instanceof final CargoAllocation cargoAllocation) {
					final boolean isFleet = cargoAllocation.getSequence() != null && (!(cargoAllocation.getSequence().isSpotVessel() || cargoAllocation.getSequence().isTimeCharterVessel()));
					if (isFleet) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				} else if (element instanceof final VesselEventVisit vesselEventVisit) {
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

	private @NonNull ICellRenderer createBuyValuePrice(final int idx, final EconsOptions options, final RowType rowType) {

		final Function<Object, Long> helper = object -> {
			Long cost = 0L;
			int found = 0;
			if (object instanceof final CargoAllocation cargoAllocation) {

				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
						if (found == idx) {
							cost += allocation.getVolumeValue();
						}
						++found;
					}
				}
			} else if (object instanceof final MarketAllocation marketAllocation) {
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getVolumeValue();
			}
			return cost;
		};

		return createBasicFormatter(options, rowType, Long.class, DollarsFormat::format, createMappingFunction(Long.class, helper));
	}

	private @NonNull ICellRenderer createBuyPrice(final int idx, final EconsOptions options, final RowType rowType) {
		final Function<Object, Double> helper = object -> {
			if (object instanceof final CargoAllocation cargoAllocation) {
				int found = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
						if (found == idx) {
							return allocation.getPrice();
						}
						++found;
					}
				}
			} else if (object instanceof final MarketAllocation marketAllocation) {
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				return allocation.getPrice();
			}
			return 0.0;
		};

		return createBasicFormatter(options, rowType, Double.class, DollarsPerMMBtuFormat::format, createMappingFunction(Double.class, helper));
	}

	private @NonNull ICellRenderer createSellPrice(final int idx, final EconsOptions options, final RowType rowType) {
		final Function<Object, Double> helper = object -> {
			if (object instanceof final CargoAllocation cargoAllocation) {

				int found = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
						if (found == idx) {
							return allocation.getPrice();
						}
						++found;
					}
				}
			} else if (object instanceof final MarketAllocation marketAllocation) {
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				return allocation.getPrice();
			}
			return 0.0;
		};

		return createBasicFormatter(options, rowType, Double.class, DollarsPerMMBtuFormat::format, createMappingFunction(Double.class, helper));
	}

	private @NonNull ICellRenderer createSellValuePrice(final int idx, final EconsOptions options, final RowType rowType) {

		final Function<Object, @Nullable Long> helper = object -> {
			Long cost = 0L;
			if (object instanceof final CargoAllocation cargoAllocation) {

				int found = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
						if (found == idx) {
							cost += allocation.getVolumeValue();
						}
						++found;
					}
				}
			} else if (object instanceof final MarketAllocation marketAllocation) {
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getVolumeValue();
			}
			return cost;
		};

		return createBasicFormatter(options, rowType, Long.class, DollarsFormat::format, createMappingFunction(Long.class, helper));
	}

	private @NonNull ICellRenderer createBuyVolumeMMBTuPrice(final int idx, final EconsOptions options, final RowType rowType) {

		final Function<Object, @Nullable Long> helper = object -> {
			Long cost = 0L;
			if (object instanceof final CargoAllocation cargoAllocation) {
				int found = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
						if (found == idx) {
							cost += allocation.getEnergyTransferred();
						}
						++found;
					}
				}
			} else if (object instanceof final MarketAllocation marketAllocation) {
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getEnergyTransferred();
			}
			return cost;
		};

		return createBasicFormatter(options, rowType, Long.class, VolumeMMBtuFormat::format, createMappingFunction(Long.class, helper));
	}

	private @NonNull ICellRenderer createPaperDealVolumeMMBTu(final EconsOptions options, final RowType rowType) {

		final Function<Object, @Nullable Long> helper = object -> {
			Long volume = 0L;
			if (object instanceof final PaperDealAllocation paperDealAllocation) {
				for (final PaperDealAllocationEntry allocation : paperDealAllocation.getEntries()) {
					volume += (int) allocation.getQuantity();
				}
			}
			return volume;
		};

		return createBasicFormatter(options, rowType, Long.class, VolumeMMBtuFormat::format, createMappingFunction(Long.class, helper));
	}

	private @NonNull ICellRenderer createSellVolumeMMBTuPrice(final int idx, final EconsOptions options, final RowType rowType) {
		final Function<Object, @Nullable Long> helper = object -> {
			Long cost = 0L;
			if (object instanceof final CargoAllocation cargoAllocation) {
				int found = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
						if (found == idx) {
							cost += allocation.getEnergyTransferred();
						}
						++found;
					}
				}
			} else if (object instanceof final MarketAllocation marketAllocation) {
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				cost += allocation.getEnergyTransferred();
			}
			return cost;
		};

		return createBasicFormatter(options, rowType, Long.class, VolumeMMBtuFormat::format, createMappingFunction(Long.class, helper));
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

	private @NonNull ICellRenderer createPNLPerMMBTU(final EconsOptions options, final RowType rowType) {

		final Function<Object, @Nullable Double> helper = object -> {
			if (object instanceof final CargoAllocation cargoAllocation) {
				final Integer pnl = getPNLValue(cargoAllocation);
				if (pnl == null) {
					return 0.0;
				}

				final double volume = cargoAllocationPerMMBTUVolumeHelper(object, options);
				if (volume == 0.0) {
					return 0.0;
				}

				return (double) pnl / volume;
			} else if (object instanceof final MarketAllocation marketAllocation) {
				final Integer pnl = getPNLValue(marketAllocation);
				if (pnl != null) {
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					final double volume = allocation.getEnergyTransferred();
					if (volume == 0.0) {
						return 0.0;
					}
					return (double) pnl / volume;
				}
			}
			return null;
		};

		final Function<Object, @Nullable Double> transformer = object -> {
			if (object instanceof final CargoAllocation cargoAllocation) {
				return helper.apply(cargoAllocation);
			} else if (object instanceof final MarketAllocation marketAllocation) {
				return helper.apply(marketAllocation);
			} else if (object instanceof CargoAllocationPair) {
				return getFromDeltaPair(Double.class, helper, object);
			} else if (object instanceof DeltaPair) {
				return getFromDeltaPair(Double.class, helper, object);
			} else if (object instanceof List<?>) {
				final List<DeltaPair> pairs = (List<DeltaPair>) object;
				final double totalVolume = pairs.stream().mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.first(), options)).sum();
				if (totalVolume == 0.0) {
					return 0.0;
				}
				final double totalOldVolume = pairs.stream().mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.second(), options)).sum();
				if (totalOldVolume == 0.0) {
					return 0.0;
				}
				final double totalPNL = pairs.stream().mapToDouble(x -> cargoAllocationPNLPerMMBTUPNLHelper(x.first())).sum();
				final double totalOldPNL = pairs.stream().mapToDouble(x -> cargoAllocationPNLPerMMBTUPNLHelper(x.second())).sum();
				return (totalOldPNL / totalOldVolume) - (totalPNL / totalVolume);
			}
			return null;
		};

		return createBasicFormatter(options, rowType, Double.class, DollarsPerMMBtuFormat::format, transformer);
	}

	private @NonNull ICellRenderer createPNLTotal(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getPNLValue));
	}

	private @NonNull ICellRenderer createPNLEquity(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getEquityPNLValue));
	}

	private @NonNull ICellRenderer createPNLAdditional(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getAdditionalPNLValue));
	}

	private static int genericShippingBOGTotalHelper(final Object object) {
		if (object instanceof final EventGrouping grouping) {
			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof final FuelUsage fuelUsage) {
					cost += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
				}
			}
			return cost;
		}
		return 0;

	}

	private @NonNull ICellRenderer createShippingBOGTotal(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingBOGTotalHelper));
	}

	private static int genericShippingBunkersTotalHelper(final Object object) {

		if (object instanceof final EventGrouping grouping) {
			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof final FuelUsage fuelUsage) {
					cost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				}
			}

			return cost;
		}
		return 0;
	}

	private @NonNull ICellRenderer createShippingBunkersTotal(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingBunkersTotalHelper));
	}

	private static int genericShippingPortCostsHelper(final Object object) {

		if (object instanceof final EventGrouping grouping) {
			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof final PortVisit portVisit) {
					cost += portVisit.getPortCost();
				}
			}

			return cost;
		}
		return 0;
	}

	private @NonNull ICellRenderer createShippingPortCosts(final EconsOptions options, final RowType rowType) {

		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingPortCostsHelper));
	}

	private static int genericShippingCanalCostsHelper(final Object object) {
		if (object instanceof final EventGrouping grouping) {
			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof final Journey journey) {
					cost += journey.getToll();
				}
			}
			return cost;
		}
		return 0;
	}

	private @NonNull ICellRenderer createShippingCanalCosts(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingCanalCostsHelper));
	}

	private static int genericShippingCharterCostsHelper(final Object object) {

		if (object instanceof final EventGrouping grouping) {
			int cost = 0;
			for (final Event event : grouping.getEvents()) {
				cost += event.getCharterCost();
			}
			return cost;
		}

		return 0;
	}

	private @NonNull ICellRenderer createShippingCharterCosts(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::genericShippingCharterCostsHelper));
	}

	private @NonNull ICellRenderer createShippingPurgeCosts(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, object -> {
			if (object instanceof final EventGrouping eventGrouping) {

				for (final Event evt : eventGrouping.getEvents()) {
					if (evt instanceof final Purge purge) {
						return purge.getCost();
					}
				}
			}
			return 0;
		}));
	}

	private @NonNull ICellRenderer createPaperDealStartDate(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, object -> {
			if (object instanceof final EventGrouping eventGrouping) {
				for (final Event evt : eventGrouping.getEvents()) {
					if (evt instanceof final Purge purge) {
						return purge.getCost();
					}
				}
			}
			return 0;
		}));
	}

	private @NonNull ICellRenderer createCancellationCosts(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Long.class, DollarsFormat::format, createMappingFunction(Long.class, object -> {
			if (object instanceof final ProfitAndLossContainer profitAndLossContainer) {
				return ScheduleModelKPIUtils.getCancellationFees(profitAndLossContainer);
			}
			return 0L;
		}));
	}

	private @NonNull ICellRenderer createShippingCooldownCosts(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, object -> {
			if (object instanceof final EventGrouping eventGrouping) {
				for (final Event evt : eventGrouping.getEvents()) {
					if (evt instanceof final Cooldown cooldown) {
						return cooldown.getCost();
					}
				}
			}
			return 0;
		}));
	}

	private static int vesselEventVisitShippingCharterRevenueHelper(final Object object) {
		int revenue = 0;

		if (object instanceof final VesselEventVisit vev) {
			if (vev.getVesselEvent() instanceof final CharterOutEvent charterOutEvent) {
				revenue = charterOutEvent.getHireRate() * charterOutEvent.getDurationInDays();
			}
		}
		if (object instanceof final GeneratedCharterOut gco) {
			revenue = gco.getRevenue();
		}
		return revenue;
	}

	private @NonNull ICellRenderer createShippingCharterRevenue(final EconsOptions options, final RowType rowType) {

		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format,
				createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingCharterRevenueHelper));
	}

	private static long vesselEventVisitShippingBallastBonusHelper(final Object object) {
		long ballastBonus = 0;

		if (object instanceof final VesselEventVisit vev) {
			if (vev.getVesselEvent() instanceof final CharterOutEvent charterOutEvent) {
				ballastBonus = charterOutEvent.getBallastBonus();
			}
		}
		return ballastBonus;
	}

	private @NonNull ICellRenderer createCharterOutBallastBonus(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Long.class, DollarsFormat::format, createMappingFunction(Long.class, StandardEconsRowFactory::vesselEventVisitShippingBallastBonusHelper));
	}

	private @NonNull ICellRenderer createCharterInBallastBonus(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Long.class, DollarsFormat::format, createMappingFunction(Long.class, StandardEconsRowFactory::charterInBallastBonusHelper));
	}

	private static int vesselEventVisitAverageCharterRateHelper(final Object object) {
		final int durationInHours = vesselEventVisitCharterDurationInHoursHelper(object);
		final int charterRevenue = vesselEventVisitShippingCharterRevenueHelper(object);
		if (durationInHours > 0) {
			return (charterRevenue * 24) / durationInHours;
		} else {
			// Avoid divide by zero, if zero duration, there is effectively no charter out,
			// so no charter rate.
			return 0;
		}
	}

	private static int vesselEventVisitCharterDurationInHoursHelper(final Object object) {
		int durationInHours = 0;

		if (object instanceof final VesselEventVisit vev) {
			if (vev.getVesselEvent() instanceof final CharterOutEvent charterOutEvent) {
				durationInHours = charterOutEvent.getDurationInDays() * 24;
			}
		}
		if (object instanceof final GeneratedCharterOut gco) {
			durationInHours = gco.getDuration();
		}
		return durationInHours;
	}

	private @NonNull ICellRenderer createCharterDays(final EconsOptions options, final RowType rowType) {
		return createIntegerDaysFromHoursFormatter(options, rowType, createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitCharterDurationInHoursHelper));
	}

	private @NonNull ICellRenderer createMeanCharterRatePerDay(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitAverageCharterRateHelper));
	}

	private static int vesselEventVisitShippingRepositioningHelper(final Object object) {
		int revenue = 0;

		if (object instanceof final VesselEventVisit vev) {
			if (vev.getVesselEvent() instanceof final CharterOutEvent charterOutEvent) {
				revenue = charterOutEvent.getRepositioningFee();
			}
		}
		return revenue;
	}

	private static int charterInRepositiongHelper(final Object object) {
		int value = 0;

		if (object instanceof final StartEvent startEvent) {
			value += startEvent.getRepositioningFee();
		}
		if (object instanceof final CargoAllocation cargoAllocation) {
			value += cargoAllocation.getRepositioningFee();
		}
		return value;
	}

	private static long charterInBallastBonusHelper(final Object object) {
		long value = 0;

		if (object instanceof final EndEvent endEvent) {
			value += endEvent.getBallastBonusFee();

		}
		if (object instanceof final CargoAllocation cargoAllocation) {
			value += cargoAllocation.getBallastBonusFee();
		}
		return value;
	}

	private @NonNull ICellRenderer createCharterOutRepositioning(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingRepositioningHelper));
	}

	private @NonNull ICellRenderer createCharterInRepositioning(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::charterInRepositiongHelper));
	}

	private @NonNull ICellRenderer createShippingCostsByMMBTU(final EconsOptions options, final RowType rowType) {

		final Function<Object, @Nullable Double> helper = object -> getShippingCostByMMBTU(options, object);

		final Function<Object, @Nullable Double> transformer = object -> {
			if (object instanceof final CargoAllocation cargoAllocation) {
				return helper.apply(cargoAllocation);
			} else if (object instanceof final MarketAllocation marketAllocation) {
				return helper.apply(marketAllocation);
			} else if (object instanceof final VesselEventVisit eventVisit) {
				return helper.apply(eventVisit);
			} else if (object instanceof DeltaPair) {
				return getFromDeltaPair(Double.class, helper, object);
			} else if (object instanceof List<?>) {
				final List<DeltaPair> pairs = (List<DeltaPair>) object;
				final double totalVolume = pairs.stream().filter(Objects::nonNull).mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.first(), options)).sum();
				if (totalVolume == 0.0)
					return 0.0;
				final double totalOldVolume = pairs.stream().filter(Objects::nonNull).mapToDouble(x -> cargoAllocationPerMMBTUVolumeHelper(x.second(), options)).sum();
				if (totalOldVolume == 0.0)
					return 0.0;
				final double totalCost = pairs.stream().filter(Objects::nonNull).mapToDouble(x -> getShippingCost(x.first())).sum();
				final double totalOldCost = pairs.stream().filter(Objects::nonNull).mapToDouble(x -> getShippingCost(x.second())).sum();
				return (totalOldCost / totalOldVolume) - (totalCost / totalVolume);
			}
			return null;
		};

		return createBasicFormatter(options, rowType, Double.class, DollarsPerMMBtuFormat::format, transformer);
	}

	private @NonNull ICellRenderer createShippingCosts(final EconsOptions options, final RowType rowType) {
		return createBasicFormatter(options, rowType, Integer.class, DollarsFormat::format, createMappingFunction(Integer.class, StandardEconsRowFactory::getShippingCost));
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

	private static List<Event> getEvents(final Object object) {
		if (object instanceof final EventGrouping eg) {
			return eg.getEvents();
		} else if (object instanceof final Purge purge) {
			return Collections.singletonList(purge);
		} else {
			return null;
		}
	}

	private static Sequence getSequence(final Object object) {
		if (object instanceof final Event evt) {
			return evt.getSequence();
		} else if (object instanceof final CargoAllocation ca) {
			return ca.getSequence();
		} else {
			return null;
		}
	}

	private static Integer getShippingCost(final Object object) {

		if (object == null) {
			return 0;
		}

		final Sequence sequence = getSequence(object);
		final List<Event> events = getEvents(object);

		if (sequence == null || events == null) {
			return 0;
		}

		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		int charterCost = 0;
		for (final Event event : events) {

			charterCost += event.getCharterCost();

			if (event instanceof final SlotVisit slotVisit) {
				// Port Costs
				shippingCost += slotVisit.getPortCost();
			}

			if (event instanceof final Journey journey) {
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof final FuelUsage fuelUsage) {
				// Base fuel costs
				shippingCost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
			}

			if (event instanceof final Purge purge) {
				shippingCost += purge.getCost();
			}
			if (event instanceof final Cooldown cooldown) {
				shippingCost += cooldown.getCost();
			}
		}

		if (sequence.getCharterInMarket() != null && sequence.getSpotIndex() < 0) {

			if (object instanceof final CargoAllocation cargoAllocation) {

				final GenericCharterContract genericCharterContract = sequence.getCharterInMarket().getGenericCharterContract();
				if (genericCharterContract != null) {
					for (final var d : cargoAllocation.getGeneralPNLDetails()) {
						if (d instanceof final CharterContractFeeDetails feeDetails) {
							if (feeDetails.getMatchingContractDetails() instanceof NotionalJourneyBallastBonusTermDetails
									|| feeDetails.getMatchingContractDetails() instanceof LumpSumBallastBonusTermDetails) {
								// Revenue
								shippingCost -= feeDetails.getFee();
							}

//							cehck cost or revenue?

							if (feeDetails.getMatchingContractDetails() instanceof OriginPortRepositioningFeeTermDetails
									|| feeDetails.getMatchingContractDetails() instanceof LumpSumRepositioningFeeTermDetails) {
								shippingCost += feeDetails.getFee();
							}
						}
					}
				}

			}

		}

		// Add on chartering costs
		if (sequence == null || sequence.isSpotVessel() || sequence.isTimeCharterVessel()) {
			shippingCost += charterCost;
		}
		return shippingCost;

	}

	private static Integer getEquityPNLValue(final Object object) {
		if (object instanceof final ProfitAndLossContainer container) {

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

		if (object instanceof final ProfitAndLossContainer container) {
			int addnPNL = 0;
			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				for (final GeneralPNLDetails generalPNLDetails : container.getGeneralPNLDetails()) {
					if (generalPNLDetails instanceof final SlotPNLDetails slotPNLDetails) {
						for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
							if (details instanceof final BasicSlotPNLDetails basicDetails) {
								addnPNL += basicDetails.getAdditionalPNL();
								addnPNL += basicDetails.getExtraShippingPNL();
								addnPNL += basicDetails.getExtraUpsidePNL();
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
		if (object instanceof final ProfitAndLossContainer container) {
			final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
			if (groupProfitAndLoss == null) {
				return null;
			}
			// Rounding!
			return (int) groupProfitAndLoss.getProfitAndLoss();
		}
		return null;
	}

	private static Integer getShippingCost(final Object object, final Function<SlotVisit, Integer> slotVisitFunc, final Function<FuelUsage, Integer> fuelUsageFunc) {

		if (object == null) {
			return null;
		}

		final Sequence sequence = getSequence(object);
		final List<Event> events = getEvents(object);

		if (sequence == null || events == null) {
			return null;
		}
		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		int charterCost = 0;
		for (final Event event : events) {

			charterCost += event.getCharterCost();

			if (event instanceof final SlotVisit slotVisit) {
				// Port Costs
				shippingCost += slotVisitFunc.apply(slotVisit);
			}

			if (event instanceof final Journey journey) {
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof final FuelUsage fuelUsage) {
				// Base fuel costs
				shippingCost += fuelUsageFunc.apply(fuelUsage);
			}

			if (event instanceof final Purge purge) {
				shippingCost += purge.getCost();
			}
			if (event instanceof final Cooldown cooldown) {
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

			if (event instanceof final PortVisit portVisit) {
				// Port Costs
				shippingCost += portVisit.getPortCost();
			}

			if (event instanceof final Journey journey) {
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof final FuelUsage fuelUsage) {
				// Base fuel costs
				shippingCost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
			}

			if (event instanceof final Purge purge) {
				shippingCost += purge.getCost();
			}
			if (event instanceof final Cooldown cooldown) {
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

	private static <T> int getOrZero(final T object, final ToIntFunction<@NonNull T> func) {
		if (object != null) {
			return func.applyAsInt(object);
		}
		return 0;
	}

	private static Integer getPortCost(@Nullable final PortVisit visit) {
		if (visit != null) {
			return visit.getPortCost();
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
