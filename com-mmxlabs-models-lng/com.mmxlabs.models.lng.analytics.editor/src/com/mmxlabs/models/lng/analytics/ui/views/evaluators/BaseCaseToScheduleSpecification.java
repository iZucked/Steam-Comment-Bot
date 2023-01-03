/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ShippingType;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VoyageSpecification;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;

public class BaseCaseToScheduleSpecification {

	private final LNGScenarioModel scenarioModel;
	private final IMapperClass mapper;
	private final Set<String> usedIDs;

	public BaseCaseToScheduleSpecification(final LNGScenarioModel scenarioModel, final IMapperClass mapper) {
		this.scenarioModel = scenarioModel;
		this.mapper = mapper;
		this.usedIDs = getUsedSlotIDs(scenarioModel);
	}

	public ScheduleSpecification generate(final BaseCase baseCase) {

		final ScheduleSpecification specification = CargoFactory.eINSTANCE.createScheduleSpecification();

		final Map<EObject, VoyageSpecification> voyageSpecs = new HashMap<>();

		final Map<ShippingOption, VesselScheduleSpecification> shippingToScheduleSpec = new HashMap<>();

		for (final BaseCaseRow row : baseCase.getBaseCase()) {

			if (row.getVesselEventOption() == null && (row.getBuyOption() == null || row.getSellOption() == null)) {
				final SlotSpecification slotSpecification = CargoFactory.eINSTANCE.createSlotSpecification();

				Slot<?> slot = null;
				if (row.getBuyOption() != null) {
					slot = getOrCreate(row.getBuyOption());
				} else if (row.getSellOption() != null) {

					slot = getOrCreate(row.getSellOption());
				}
				if (slot != null) {
					slotSpecification.setSlot(slot);
					specification.getOpenEvents().add(slotSpecification);
				}
				continue;
			}

			final ShippingType shippingType = row.getVesselEventOption() != null ? ShippingType.Shipped : AnalyticsBuilder.getShippingType(row.getBuyOption(), row.getSellOption());
			if (shippingType == ShippingType.Open) {
				if (row.getBuyOption() != null && !(row.getBuyOption() instanceof OpenBuy)) {
					final SlotSpecification spec = CargoFactory.eINSTANCE.createSlotSpecification();
					spec.setSlot(getOrCreate(row.getBuyOption()));
					specification.getOpenEvents().add(spec);
				}
				if (row.getSellOption() != null && !(row.getSellOption() instanceof OpenSell)) {
					final SlotSpecification spec = CargoFactory.eINSTANCE.createSlotSpecification();
					spec.setSlot(getOrCreate(row.getSellOption()));
					specification.getOpenEvents().add(spec);
				}
			} else if (shippingType == ShippingType.NonShipped) {
				final NonShippedCargoSpecification spec = CargoFactory.eINSTANCE.createNonShippedCargoSpecification();

				DischargeSlot dischargeSlot = null;
				LoadSlot loadSlot = null;

				if (buyNeedsDate(row.getBuyOption())) {
					dischargeSlot = getOrCreate(row.getSellOption());
					loadSlot = getOrCreate(row.getBuyOption(), dischargeSlot.getWindowStart(), 0);
				} else if (sellNeedsDate(row.getSellOption())) {
					loadSlot = getOrCreate(row.getBuyOption());
					dischargeSlot = getOrCreate(row.getSellOption(), loadSlot.getWindowStart(), 0);
				} else {
					dischargeSlot = getOrCreate(row.getSellOption());
					loadSlot = getOrCreate(row.getBuyOption());
				}

				final BaseCaseRowOptions options = row.getOptions();

				final SlotSpecification loadSpec = CargoFactory.eINSTANCE.createSlotSpecification();
				loadSpec.setSlot(loadSlot);
				if (options != null && options.isSetLoadDate()) {
					loadSpec.setArrivalDate(options.getLoadDate());
				}

				final SlotSpecification dischargeSpec = CargoFactory.eINSTANCE.createSlotSpecification();
				dischargeSpec.setSlot(dischargeSlot);
				if (options != null && options.isSetDischargeDate()) {
					dischargeSpec.setArrivalDate(options.getDischargeDate());
				}

				spec.getSlotSpecifications().add(loadSpec);
				spec.getSlotSpecifications().add(dischargeSpec);

				specification.getNonShippedCargoSpecifications().add(spec);
			} else if (shippingType == ShippingType.Shipped) {
				final ShippingOption shipping = row.getShipping();

				boolean newRecord = true;
				final VesselScheduleSpecification vesselScheduleSpecification;
				if (shipping instanceof RoundTripShippingOption) {
					// Always new
					vesselScheduleSpecification = CargoFactory.eINSTANCE.createVesselScheduleSpecification();

				} else {
					if (shippingToScheduleSpec.containsKey(shipping)) {
						vesselScheduleSpecification = shippingToScheduleSpec.get(shipping);
						newRecord = false;
					} else {
						vesselScheduleSpecification = CargoFactory.eINSTANCE.createVesselScheduleSpecification();
						shippingToScheduleSpec.put(shipping, vesselScheduleSpecification);
					}
				}

				final Function<EObject, VoyageSpecification> builder = k -> {
					final VoyageSpecification spec = CargoFactory.eINSTANCE.createVoyageSpecification();
					vesselScheduleSpecification.getEvents().add(spec);
					return spec;
				};
				final TriConsumer<EObject, BaseCaseRowOptions, ScheduleSpecificationEvent> applyOptions = (target, options, slotSpec) -> {
					if (options != null) {
						if (target instanceof LoadSlot) {
							if (options.isSetLadenRoute()) {
								voyageSpecs.computeIfAbsent(target, builder).setRouteOption(options.getLadenRoute());
							}
							if (options.isSetLadenFuelChoice()) {
								voyageSpecs.computeIfAbsent(target, builder).setFuelChoice(options.getLadenFuelChoice());
							}
							if (options.getLoadDate() != null) {
								((SlotSpecification) slotSpec).setArrivalDate(options.getLoadDate());
							}

						}
						if (target instanceof DischargeSlot) {
							if (options.isSetBallastRoute()) {
								voyageSpecs.computeIfAbsent(target, builder).setRouteOption(options.getBallastRoute());
							}
							if (options.isSetBallastFuelChoice()) {
								voyageSpecs.computeIfAbsent(target, builder).setFuelChoice(options.getBallastFuelChoice());
							}
							if (options.getDischargeDate() != null) {
								((SlotSpecification) slotSpec).setArrivalDate(options.getDischargeDate());
							}
						}
						if (target instanceof VesselEvent) {
							if (options.isSetLadenRoute()) {
								voyageSpecs.computeIfAbsent(target, builder).setRouteOption(options.getLadenRoute());
							}
							if (options.isSetLadenFuelChoice()) {
								voyageSpecs.computeIfAbsent(target, builder).setFuelChoice(options.getLadenFuelChoice());
							}
							if (options.getLoadDate() != null) {
								((VesselEventSpecification) slotSpec).setArrivalDate(options.getLoadDate());
							}
						}
					}
				};

				// TODO: This should be cached!
				if (shipping instanceof RoundTripShippingOption) {
					final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shipping;
					final Vessel vessel = roundTripShippingOption.getVessel();
					final BaseLegalEntity entity = roundTripShippingOption.getEntity();
					final String hireCost = roundTripShippingOption.getHireCost();

					// TODO: Need place to store this in datamodel
					final CharterInMarket newMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
					newMarket.setCharterInRate(hireCost);
					newMarket.setVessel(vessel);
					newMarket.setEntity(entity);
					newMarket.setNominal(true);
					// { // From old sandbox code
					// final String baseName =
					// SHIPPING_OPTION_DESCRIPTION_FORMATTER.render(roundTripShippingOption);
					// @NonNull
					// final Set<String> usedIDStrings =
					// clone.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().stream().map(c
					// -> c.getName()).collect(Collectors.toSet());
					// final String id = AnalyticsBuilder.getUniqueID(baseName, usedIDStrings);
					// market.setName(id);
					// }
					newMarket.setName(vessel.getName() + " @" + hireCost);

					vesselScheduleSpecification.setVesselAllocation(newMarket);
					vesselScheduleSpecification.setSpotIndex(-1);

					mapper.addMapping(roundTripShippingOption, newMarket);
				} else if (shipping instanceof ExistingCharterMarketOption) {
					final ExistingCharterMarketOption option = (ExistingCharterMarketOption) shipping;
					vesselScheduleSpecification.setVesselAllocation(option.getCharterInMarket());
					vesselScheduleSpecification.setSpotIndex(option.getSpotIndex());
					// mapper.addMapping(option, newMarket);
				} else if (shipping instanceof OptionalSimpleVesselCharterOption) {
					final OptionalSimpleVesselCharterOption optionalAvailabilityShippingOption = (OptionalSimpleVesselCharterOption) shipping;
					VesselCharter vesselCharter = mapper.get(optionalAvailabilityShippingOption);
					if (vesselCharter == null) {
						vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
						vesselCharter.setTimeCharterRate(optionalAvailabilityShippingOption.getHireCost());
						final Vessel vessel = optionalAvailabilityShippingOption.getVessel();
						vesselCharter.setVessel(vessel);
						vesselCharter.setEntity(optionalAvailabilityShippingOption.getEntity());

						vesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
						vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());
						if (optionalAvailabilityShippingOption.isUseSafetyHeel()) {
							vesselCharter.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
							vesselCharter.getStartHeel().setCvValue(22.8);
							// vesselCharter.getStartHeel().setPriceExpression(PerMMBTU(0.1);

							vesselCharter.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
							vesselCharter.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
							vesselCharter.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
						}

						if (optionalAvailabilityShippingOption.getStart() != null) {
							vesselCharter.setStartAfter(optionalAvailabilityShippingOption.getStart().atStartOfDay());
							vesselCharter.setStartBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
						}
						if (optionalAvailabilityShippingOption.getEnd() != null) {
							vesselCharter.setEndAfter(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
							vesselCharter.setEndBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
						}
						vesselCharter.setOptional(true);
						vesselCharter.setContainedCharterContract(AnalyticsBuilder.createCharterTerms(optionalAvailabilityShippingOption.getRepositioningFee(), //
								optionalAvailabilityShippingOption.getBallastBonus()));
						if (optionalAvailabilityShippingOption.getStartPort() != null) {
							vesselCharter.setStartAt(optionalAvailabilityShippingOption.getStartPort());
						}
						if (optionalAvailabilityShippingOption.getEndPort() != null) {
							final EList<APortSet<Port>> endAt = vesselCharter.getEndAt();
							endAt.clear();
							endAt.add(optionalAvailabilityShippingOption.getEndPort());
						}

						mapper.addMapping(optionalAvailabilityShippingOption, vesselCharter);
					}
					vesselScheduleSpecification.setVesselAllocation(vesselCharter);
				} else if (shipping instanceof SimpleVesselCharterOption) {
					final SimpleVesselCharterOption fleetShippingOption = (SimpleVesselCharterOption) shipping;
					VesselCharter vesselCharter = mapper.get(fleetShippingOption);
					if (vesselCharter == null) {
						vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
						vesselCharter.setTimeCharterRate(fleetShippingOption.getHireCost());
						final Vessel vessel = fleetShippingOption.getVessel();
						vesselCharter.setVessel(vessel);
						vesselCharter.setEntity(fleetShippingOption.getEntity());

						vesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
						vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

						if (fleetShippingOption.isUseSafetyHeel()) {
							vesselCharter.getStartHeel().setMaxVolumeAvailable(vessel.getSafetyHeel());
							vesselCharter.getStartHeel().setCvValue(22.8);
							// vesselCharter.getStartHeel().setPricePerMMBTU(0.1);

							vesselCharter.getEndHeel().setMinimumEndHeel(vessel.getSafetyHeel());
							vesselCharter.getEndHeel().setMaximumEndHeel(vessel.getSafetyHeel());
							vesselCharter.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
						}
						vesselCharter.setOptional(false);

						mapper.addMapping(fleetShippingOption, vesselCharter);
					}
					vesselScheduleSpecification.setVesselAllocation(vesselCharter);
				} else if (shipping instanceof ExistingVesselCharterOption) {
					final ExistingVesselCharterOption fleetShippingOption = (ExistingVesselCharterOption) shipping;
					final VesselCharter vesselCharter = fleetShippingOption.getVesselCharter();
					mapper.addMapping(fleetShippingOption, vesselCharter);
					vesselScheduleSpecification.setVesselAllocation(vesselCharter);
				} else if (shipping instanceof FullVesselCharterOption) {
					final FullVesselCharterOption fleetShippingOption = (FullVesselCharterOption) shipping;
					final VesselCharter vesselCharter = fleetShippingOption.getVesselCharter();
					mapper.addMapping(fleetShippingOption, vesselCharter);
					vesselScheduleSpecification.setVesselAllocation(vesselCharter);
				} else {
					assert false;
				}

				if (row.getVesselEventOption() != null) {
					final VesselEventSpecification eventSpec = CargoFactory.eINSTANCE.createVesselEventSpecification();
					eventSpec.setVesselEvent(getOrCreate(row.getVesselEventOption()));
					vesselScheduleSpecification.getEvents().add(eventSpec);
					applyOptions.accept(eventSpec.getVesselEvent(), row.getOptions(), eventSpec);

				} else {
					DischargeSlot dischargeSlot = null;
					LoadSlot loadSlot = null;

					if (buyNeedsDate(row.getBuyOption())) {
						dischargeSlot = getOrCreate(row.getSellOption());
						loadSlot = getOrCreate(row.getBuyOption(), dischargeSlot.getWindowStart(), 1);
					} else if (sellNeedsDate(row.getSellOption())) {
						loadSlot = getOrCreate(row.getBuyOption());
						dischargeSlot = getOrCreate(row.getSellOption(), loadSlot.getWindowStart(), 1);
					} else {
						dischargeSlot = getOrCreate(row.getSellOption());
						loadSlot = getOrCreate(row.getBuyOption());
					}

					final SlotSpecification loadSpec = CargoFactory.eINSTANCE.createSlotSpecification();
					loadSpec.setSlot(loadSlot);
					vesselScheduleSpecification.getEvents().add(loadSpec);

					applyOptions.accept(loadSlot, row.getOptions(), loadSpec);

					final SlotSpecification dischargeSpec = CargoFactory.eINSTANCE.createSlotSpecification();
					dischargeSpec.setSlot(dischargeSlot);
					vesselScheduleSpecification.getEvents().add(dischargeSpec);

					applyOptions.accept(dischargeSlot, row.getOptions(), dischargeSpec);

				}

				if (newRecord) {
					specification.getVesselScheduleSpecifications().add(vesselScheduleSpecification);
				}
			} else {
				// ?
			}

		}

		return specification;

	}

	private LoadSlot getOrCreate(final BuyOption buyOption, final LocalDate windowStart, final int shift) {
		// Do not modify original slots
		assert !(buyOption instanceof BuyReference);
		final LoadSlot slot = getOrCreate(buyOption);
		final LocalDate monthAlignedStart = windowStart.withDayOfMonth(1).minusMonths(shift);

		if (slot.getWindowStart() == null) {
			slot.setWindowStart(monthAlignedStart);
			slot.setWindowSize(1 + shift);
			slot.setWindowSizeUnits(TimePeriod.MONTHS);
		} else {
			LocalDate originalEnd = slot.getWindowStart().plusMonths(slot.getWindowSize());
			final int originalDiff = Months.between(slot.getWindowStart(), originalEnd);
			// Sanity check calcs
			assert originalDiff == slot.getWindowSize();

			if (slot.getWindowStart().isAfter(monthAlignedStart)) {
				slot.setWindowStart(monthAlignedStart);
				// slot.setWindowSize(1 + shift);
				slot.setWindowSizeUnits(TimePeriod.MONTHS);
			} else if (monthAlignedStart.plusMonths(1 + shift).isAfter(originalEnd)) {
				originalEnd = monthAlignedStart.plusMonths(1 + shift);
			}
			int diff = Months.between(slot.getWindowStart(), originalEnd);
			if (shift + 1 > diff) {
				diff = shift + 1;
			}
			slot.setWindowSize(diff);
		}
		if (mapper.isCreateBEOptions()) {

			mapper.getBreakEven(buyOption).setWindowStart(slot.getWindowStart());
			mapper.getBreakEven(buyOption).setWindowSize(slot.getWindowSize());
			mapper.getBreakEven(buyOption).setWindowSizeUnits(slot.getWindowSizeUnits());
			mapper.getChangable(buyOption).setWindowStart(slot.getWindowStart());
			mapper.getChangable(buyOption).setWindowSize(slot.getWindowSize());
			mapper.getChangable(buyOption).setWindowSizeUnits(slot.getWindowSizeUnits());
		}
		return slot;
	}

	private DischargeSlot getOrCreate(final SellOption sellOption, final LocalDate windowStart, final int shift) {

		// Do not modify original slots
		assert !(sellOption instanceof SellReference);
		final DischargeSlot slot = getOrCreate(sellOption);

		if (slot.getWindowStart() == null) {
			slot.setWindowStart(windowStart.withDayOfMonth(1));
			slot.setWindowSize(1 + shift);
			slot.setWindowSizeUnits(TimePeriod.MONTHS);
		} else {
			LocalDate originalEnd = slot.getWindowStart().plusMonths(slot.getWindowSize());
			final int originalDiff = Months.between(slot.getWindowStart(), originalEnd);
			// Sanity check calcs
			assert originalDiff == slot.getWindowSize();

			if (slot.getWindowStart().isAfter(windowStart)) {
				slot.setWindowStart(windowStart.withDayOfMonth(1));
				// slot.setWindowSize(1 + shift);
				slot.setWindowSizeUnits(TimePeriod.MONTHS);
			} else if (windowStart.withDayOfMonth(1).plusMonths(1 + shift).isAfter(originalEnd)) {
				originalEnd = windowStart.withDayOfMonth(1).plusMonths(1 + shift);
			}
			int diff = Months.between(slot.getWindowStart(), originalEnd);
			if (shift + 1 > diff) {
				diff = shift + 1;
			}
			slot.setWindowSize(diff);
		}

		if (mapper.isCreateBEOptions()) {
			mapper.getBreakEven(sellOption).setWindowStart(slot.getWindowStart());
			mapper.getBreakEven(sellOption).setWindowSize(slot.getWindowSize());
			mapper.getBreakEven(sellOption).setWindowSizeUnits(slot.getWindowSizeUnits());
			mapper.getChangable(sellOption).setWindowStart(slot.getWindowStart());
			mapper.getChangable(sellOption).setWindowSize(slot.getWindowSize());
			mapper.getChangable(sellOption).setWindowSizeUnits(slot.getWindowSizeUnits());
		}
		return slot;

	}

	private VesselEvent getOrCreate(final VesselEventOption vesselEventOption) {
		final VesselEvent original = mapper.getOriginal(vesselEventOption);
		if (original != null) {
			return original;
		}
		final VesselEvent newVesselEvent = AnalyticsBuilder.makeVesselEvent(vesselEventOption, scenarioModel);

		mapper.addMapping(vesselEventOption, newVesselEvent);

		return newVesselEvent;
	}

	private DischargeSlot getOrCreate(final SellOption sellOption) {
		final DischargeSlot original = mapper.getOriginal(sellOption);
		if (original != null) {
			return original;
		}
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraDischarges.stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraLoads.stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		final DischargeSlot dischargeSlot_original = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.ORIGINAL_SLOT, this.usedIDs);

		if (mapper.isCreateBEOptions()) {
			final DischargeSlot dischargeSlot_breakEven = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.BREAK_EVEN_VARIANT, this.usedIDs);
			final DischargeSlot dischargeSlot_changeable = AnalyticsBuilder.makeDischargeSlot(sellOption, scenarioModel, SlotMode.CHANGE_PRICE_VARIANT, this.usedIDs);
			mapper.addMapping(sellOption, dischargeSlot_original, dischargeSlot_breakEven, dischargeSlot_changeable);
		} else {
			mapper.addMapping(sellOption, dischargeSlot_original, null, null);
		}

		return dischargeSlot_original;
	}

	private LoadSlot getOrCreate(final BuyOption buyOption) {

		final LoadSlot original = mapper.getOriginal(buyOption);
		if (original != null) {
			return original;
		}
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraDischarges.stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		this.usedIDs.addAll(mapper.getExtraDataProvider().extraLoads.stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		final LoadSlot loadSlot_original = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.ORIGINAL_SLOT, this.usedIDs);
		if (mapper.isCreateBEOptions()) {
			final LoadSlot loadSlot_breakEven = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.BREAK_EVEN_VARIANT, this.usedIDs);
			final LoadSlot loadSlot_changeable = AnalyticsBuilder.makeLoadSlot(buyOption, scenarioModel, SlotMode.CHANGE_PRICE_VARIANT, this.usedIDs);
			mapper.addMapping(buyOption, loadSlot_original, loadSlot_breakEven, loadSlot_changeable);
		} else {
			mapper.addMapping(buyOption, loadSlot_original, null, null);
		}
		return loadSlot_original;
	}

	private boolean sellNeedsDate(final SellOption sellOption) {
		if (sellOption instanceof SellMarket sellMarket) {
			return !sellMarket.isSetMonth() || sellMarket.getMonth() == null;
		}
		if (sellOption instanceof SellReference) {
			return false;
		}
		if (sellOption instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) sellOption;
			return sellOpportunity.getDate() == null;
		}
		throw new IllegalArgumentException();
	}

	private boolean buyNeedsDate(final BuyOption buyOption) {
		if (buyOption instanceof BuyMarket buyMarket) {
			return !buyMarket.isSetMonth() || buyMarket.getMonth() == null;
		}
		if (buyOption instanceof BuyReference) {
			return false;
		}
		if (buyOption instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) buyOption;
			return buyOpportunity.getDate() == null;
		}
		throw new IllegalArgumentException();
	}

	private static Set<String> getUsedSlotIDs(final LNGScenarioModel lngScenarioModel) {
		final Set<String> usedIDs = new HashSet<>();
		usedIDs.addAll(lngScenarioModel.getCargoModel().getLoadSlots().stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		usedIDs.addAll(lngScenarioModel.getCargoModel().getDischargeSlots().stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		return usedIDs;
	}

}
