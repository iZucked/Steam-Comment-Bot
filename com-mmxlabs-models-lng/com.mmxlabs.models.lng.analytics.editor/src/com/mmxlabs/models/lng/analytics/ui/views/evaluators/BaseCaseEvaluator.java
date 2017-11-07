/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselAvailability;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionalAvailabilityShippingOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class BaseCaseEvaluator {

	private static final ShippingOptionDescriptionFormatter SHIPPING_OPTION_DESCRIPTION_FORMATTER = new ShippingOptionDescriptionFormatter();

	public static LNGScenarioModel generateScenario(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model, final BaseCase baseCase,
			final BiConsumer<LNGScenarioModel, IMapperClass> callback) {

		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final EcoreUtil.Copier copier = new Copier();
			final LNGScenarioModel clone = (LNGScenarioModel) copier.copy(lngScenarioModel);
			final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(clone);
			analyticsModel.getOptionModels().clear();
			final OptionAnalysisModel clonedModel = (OptionAnalysisModel) copier.copy(model);
			final BaseCase clonedBaseCase;
			if (model.getBaseCase() == baseCase) {
				clonedBaseCase = clonedModel.getBaseCase();
			} else {
				clonedBaseCase = (BaseCase) copier.copy(baseCase);
			}
			copier.copyReferences();
			final IMapperClass mapper = new Mapper(copier);

			if (!baseCase.isKeepExistingScenario()) {
				clearData(clone, clonedModel, clonedBaseCase);
			}
			buildScenario(clone, clonedModel, clonedBaseCase, mapper);

			callback.accept(clone, mapper);

			return clone;
		}
		return null;
	}

	public static LNGScenarioModel generateFullScenario(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model,
			final TriConsumer<LNGScenarioModel, Map<ShippingOption, VesselAssignmentType>, IMapperClass> callback) {

		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final EcoreUtil.Copier copier = new Copier();
			final LNGScenarioModel clone = (LNGScenarioModel) copier.copy(lngScenarioModel);
			final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(clone);
			analyticsModel.getOptionModels().clear();
			final OptionAnalysisModel clonedModel = (OptionAnalysisModel) copier.copy(model);
			copier.copyReferences();
			final IMapperClass mapper = new Mapper(copier);

			clearData(clone, clonedModel, null);
			Map<ShippingOption, VesselAssignmentType> shippingMap = buildFullScenario(clone, clonedModel, mapper);

			callback.accept(clone, shippingMap, mapper);

			return clone;
		}
		return null;
	}

	public static void evaluate(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model, final BaseCase baseCase, final boolean fork, final String forkName,
			@Nullable final ScenarioInstance altParentForFork) {

		generateScenario(scenarioEditingLocation, model, baseCase, (clone, mapper) -> {
			final ScenarioInstance inst = scenarioEditingLocation.getScenarioInstance();
			final ScenarioInstance parentForFork = altParentForFork != null ? altParentForFork : inst;
			evaluateScenario(clone, parentForFork, fork, forkName);

			updateResults(scenarioEditingLocation, clone, baseCase);
		});

	}

	protected static void updateResults(final IScenarioEditingLocation scenarioEditingLocation, final LNGScenarioModel clone, final BaseCase baseCase) {

		final long pnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(clone.getScheduleModel().getSchedule());
		scenarioEditingLocation.getDefaultCommandHandler().handleCommand(
				SetCommand.create(scenarioEditingLocation.getEditingDomain(), baseCase, AnalyticsPackage.Literals.BASE_CASE__PROFIT_AND_LOSS, pnl), baseCase,
				AnalyticsPackage.Literals.BASE_CASE__PROFIT_AND_LOSS);
	}

	protected static void evaluateScenario(final LNGScenarioModel lngScenarioModel, final ScenarioInstance scenarioInstance, final boolean fork, final String forkName) {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withServiceConsumer(IAnalyticsScenarioEvaluator.class,
				evaluator -> evaluator.evaluate(lngScenarioModel, userSettings, scenarioInstance, fork, forkName));

	}

	protected static void buildScenario(final LNGScenarioModel clone, final OptionAnalysisModel clonedModel, final @NonNull BaseCase clonedBaseCase, final IMapperClass mapper) {

		Map<FleetShippingOption, VesselAvailability> shippingMap = createShipping(clone, clonedBaseCase);

		for (final BaseCaseRow row : clonedBaseCase.getBaseCase()) {
			final BuyOption buy = row.getBuyOption();

			// Standard sandbox only uses the original

			final LoadSlot loadSlot_original = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.ORIGINAL_SLOT);
			final LoadSlot loadSlot_breakEven = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.BREAK_EVEN_VARIANT);
			final LoadSlot loadSlot_changeable = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.CHANGE_PRICE_VARIANT);
			mapper.addMapping(buy, loadSlot_original, loadSlot_breakEven, loadSlot_changeable);

			final SellOption sell = row.getSellOption();
			final DischargeSlot dischargeSlot_original = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.ORIGINAL_SLOT);
			final DischargeSlot dischargeSlot_breakEven = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.BREAK_EVEN_VARIANT);
			final DischargeSlot dischargeSlot_changeable = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.CHANGE_PRICE_VARIANT);

			mapper.addMapping(sell, dischargeSlot_original, dischargeSlot_breakEven, dischargeSlot_changeable);

			Cargo cargo = makeCargo(clone, shippingMap, row, loadSlot_original, dischargeSlot_original);

			if (loadSlot_original != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot_original)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot_original);
			}
			if (loadSlot_breakEven != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot_breakEven)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot_breakEven);
			}
			if (loadSlot_changeable != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot_changeable)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot_changeable);
			}

			if (dischargeSlot_original != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot_original)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot_original);
			}
			if (dischargeSlot_breakEven != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot_breakEven)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot_breakEven);
			}
			if (dischargeSlot_changeable != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot_changeable)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot_changeable);
			}
			if (cargo != null && !clone.getCargoModel().getCargoes().contains(cargo)) {
				clone.getCargoModel().getCargoes().add(cargo);
			}
		}

		// Fix up cargoes
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(clone);
		final Iterator<Cargo> itr = cargoModel.getCargoes().iterator();
		while (itr.hasNext()) {
			final Cargo c = itr.next();
			if (c.getSlots().size() < 2) {
				c.getSlots().clear();
				itr.remove();
			}
		}

	}

	protected static Map<ShippingOption, VesselAssignmentType> buildFullScenario(final LNGScenarioModel clone, final OptionAnalysisModel clonedModel, final IMapperClass mapper) {

		Map<ShippingOption, VesselAssignmentType> shippingMap = createShipping(clone, clonedModel);

		for (final BuyOption buy : clonedModel.getBuys()) {
			final LoadSlot loadSlot_original = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.ORIGINAL_SLOT);
			final LoadSlot loadSlot_breakEven = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.BREAK_EVEN_VARIANT);
			final LoadSlot loadSlot_changeable = AnalyticsBuilder.makeLoadSlot(buy, clone, SlotMode.CHANGE_PRICE_VARIANT);
			mapper.addMapping(buy, loadSlot_original, loadSlot_breakEven, loadSlot_changeable);
			if (loadSlot_original != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot_original)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot_original);
			}
			if (loadSlot_breakEven != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot_breakEven)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot_breakEven);
			}
			if (loadSlot_changeable != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot_changeable)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot_changeable);
			}
		}
		for (final SellOption sell : clonedModel.getSells()) {
			final DischargeSlot dischargeSlot_original = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.ORIGINAL_SLOT);
			final DischargeSlot dischargeSlot_breakEven = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.BREAK_EVEN_VARIANT);
			final DischargeSlot dischargeSlot_changeable = AnalyticsBuilder.makeDischargeSlot(sell, clone, SlotMode.CHANGE_PRICE_VARIANT);
			mapper.addMapping(sell, dischargeSlot_original, dischargeSlot_breakEven, dischargeSlot_changeable);
			if (dischargeSlot_original != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot_original)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot_original);
			}
			if (dischargeSlot_breakEven != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot_breakEven)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot_breakEven);
			}
			if (dischargeSlot_changeable != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot_changeable)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot_changeable);
			}
		}
		return shippingMap;
	}

	public static Cargo makeCargo(final LNGScenarioModel clone, Map<FleetShippingOption, VesselAvailability> shippingMap, final BaseCaseRow row, final LoadSlot loadSlot,
			final DischargeSlot dischargeSlot) {
		Cargo cargo = null;
		if (loadSlot != null && dischargeSlot != null) {
			if (loadSlot.getCargo() != null) {
				final Cargo c = loadSlot.getCargo();
				c.getSlots().retainAll(Collections.singleton(loadSlot));
				dischargeSlot.setCargo(c);
				cargo = c;
			} else {
				cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);
			}
		}

		if (loadSlot != null && dischargeSlot != null) {
			if (loadSlot.getPort() == null && dischargeSlot.getPort() != null) {
				assert loadSlot.isDESPurchase();
				assert loadSlot instanceof SpotSlot;
				loadSlot.setPort(dischargeSlot.getPort());
			} else if (loadSlot.getPort() != null && dischargeSlot.getPort() == null) {
				assert dischargeSlot.isFOBSale();
				assert dischargeSlot instanceof SpotSlot;
				dischargeSlot.setPort(loadSlot.getPort());
			}
		}

		setShipping(loadSlot, dischargeSlot, cargo, row.getShipping(), clone, shippingMap);
		return cargo;
	}

	protected static void setShipping(final @Nullable LoadSlot loadSlot, final @Nullable DischargeSlot dischargeSlot, final @Nullable Cargo cargo, final @Nullable ShippingOption shipping,
			final @NonNull LNGScenarioModel lngScenarioModel, Map<FleetShippingOption, VesselAvailability> shippingMap) {
		final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

		boolean adjustWindows = false;

		if (shipping == null) {
			if (loadSlot != null && dischargeSlot != null) {
				if (loadSlot.getWindowStart() != null && dischargeSlot.getWindowStart() == null) {
					dischargeSlot.setWindowStart(loadSlot.getWindowStart());
					if (dischargeSlot instanceof SpotSlot) {
						dischargeSlot.setWindowStart(dischargeSlot.getWindowStart().withDayOfMonth(1));
						// Ensure other values correctly set
						dischargeSlot.setWindowSize(1);
						dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);
						dischargeSlot.setWindowStartTime(0);
					}
				} else if (loadSlot.getWindowStart() == null && dischargeSlot.getWindowStart() != null) {
					loadSlot.setWindowStart(dischargeSlot.getWindowStart());
					if (loadSlot instanceof SpotSlot) {
						loadSlot.setWindowStart(loadSlot.getWindowStart().withDayOfMonth(1));
						// Ensure other values correctly set
						loadSlot.setWindowSize(1);
						loadSlot.setWindowSizeUnits(TimePeriod.MONTHS);
						loadSlot.setWindowStartTime(0);
					}
				}
			}
		} else if (shipping instanceof NominatedShippingOption) {
			final NominatedShippingOption nominatedShippingOption = (NominatedShippingOption) shipping;
			if (loadSlot != null && loadSlot.isDESPurchase()) {
				loadSlot.setNominatedVessel(nominatedShippingOption.getNominatedVessel());
			}
			if (dischargeSlot != null && dischargeSlot.isFOBSale()) {
				dischargeSlot.setNominatedVessel(nominatedShippingOption.getNominatedVessel());
			}
			adjustWindows = true;
		} else if (shipping instanceof FleetShippingOption) {
			final FleetShippingOption fleetShippingOption = (FleetShippingOption) shipping;
			if (cargo != null) {

				final VesselAvailability vesselAvailability = shippingMap.get(fleetShippingOption);
				assert vesselAvailability != null;

				cargo.setVesselAssignmentType(vesselAvailability);

				adjustWindows = true;
			}
		} else if (shipping instanceof RoundTripShippingOption) {
			final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shipping;
			if (cargo != null) {
				final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
				final String baseName = SHIPPING_OPTION_DESCRIPTION_FORMATTER.render(roundTripShippingOption);
				@NonNull
				final Set<String> usedIDStrings = lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().stream().map(c -> c.getName()).collect(Collectors.toSet());
				final String id = AnalyticsBuilder.getUniqueID(baseName, usedIDStrings);
				market.setName(id);
				market.setCharterInRate(roundTripShippingOption.getHireCost());
				market.setVesselClass(roundTripShippingOption.getVesselClass());
				cargo.setVesselAssignmentType(market);
				cargo.setSpotIndex(-1);
				lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().add(market);

				adjustWindows = true;
			}
		} else if (shipping instanceof ExistingVesselAvailability) {
			final ExistingVesselAvailability existingVesselAvailability = (ExistingVesselAvailability) shipping;
			cargo.setVesselAssignmentType(existingVesselAvailability.getVesselAvailability());

			adjustWindows = true;
		} else if (shipping instanceof ExistingCharterMarketOption) {
			final ExistingCharterMarketOption existingCharterMarketOption = (ExistingCharterMarketOption) shipping;
			cargo.setVesselAssignmentType(existingCharterMarketOption.getCharterInMarket());
			cargo.setSpotIndex(existingCharterMarketOption.getSpotIndex());

			adjustWindows = true;
		}

		if (adjustWindows) {
			if (loadSlot.getWindowStart() != null && dischargeSlot.getWindowStart() == null) {
				final int travelHours = AnalyticsBuilder.calculateTravelHoursForDischarge(portModel, loadSlot, dischargeSlot, shipping);
				final int travelDays = (int) Math.ceil((double) travelHours / 24.0);

				dischargeSlot.setWindowStart(loadSlot.getWindowStart().plusDays(travelDays));
				if (dischargeSlot instanceof SpotSlot) {
					dischargeSlot.setWindowStart(dischargeSlot.getWindowStart().withDayOfMonth(1));
					// Ensure other values correctly set
					dischargeSlot.setWindowSize(1);
					dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);
					dischargeSlot.setWindowStartTime(0);
				}
			} else if (loadSlot.getWindowStart() == null && dischargeSlot.getWindowStart() != null) {
				final int travelHours = AnalyticsBuilder.calculateTravelHoursForLoad(portModel, loadSlot, dischargeSlot, shipping);

				final int travelDays = (int) Math.ceil((double) travelHours / 24.0);
				loadSlot.setWindowStart(dischargeSlot.getWindowStart().minusDays(travelDays));
				if (loadSlot instanceof SpotSlot) {
					loadSlot.setWindowStart(loadSlot.getWindowStart().withDayOfMonth(1));
					// Ensure other values correctly set
					loadSlot.setWindowSize(1);
					loadSlot.setWindowSizeUnits(TimePeriod.MONTHS);
					loadSlot.setWindowStartTime(0);
				}
			}
		}
	}

	protected static Map<FleetShippingOption, VesselAvailability> createShipping(final LNGScenarioModel clone, final BaseCase clonedBaseCase) {
		Map<FleetShippingOption, VesselAvailability> availabilitiesMap = new HashMap<>();
		for (final BaseCaseRow row : clonedBaseCase.getBaseCase()) {

			final ShippingOption shipping = row.getShipping();
			if (shipping instanceof OptionalAvailabilityShippingOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final OptionalAvailabilityShippingOption optionalAvailabilityShippingOption = (OptionalAvailabilityShippingOption) shipping;
				final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
				vesselAvailability.setTimeCharterRate(optionalAvailabilityShippingOption.getHireCost());
				final Vessel vessel = optionalAvailabilityShippingOption.getVessel();
				vesselAvailability.setVessel(vessel);
				vesselAvailability.setEntity(optionalAvailabilityShippingOption.getEntity());

				vesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
				vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
				if (optionalAvailabilityShippingOption.isUseSafetyHeel()) {
					vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getStartHeel().setCvValue(22.8);
					// vesselAvailability.getStartHeel().setPriceExpression(PerMMBTU(0.1);

					vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}

				vesselAvailability.setStartAfter(optionalAvailabilityShippingOption.getStart().atStartOfDay());
				vesselAvailability.setStartBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setEndAfter(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setEndBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setOptional(true);
				vesselAvailability.setFleet(false);
				vesselAvailability.setRepositioningFee(optionalAvailabilityShippingOption.getRepositioningFee());
				if (optionalAvailabilityShippingOption.getStartPort() != null) {
					vesselAvailability.setStartAt(optionalAvailabilityShippingOption.getStartPort());
				}
				if (optionalAvailabilityShippingOption.getEndPort() != null) {
					EList<APortSet<Port>> endAt = vesselAvailability.getEndAt();
					endAt.clear();
					endAt.add(optionalAvailabilityShippingOption.getEndPort());
				}
				clone.getCargoModel().getVesselAvailabilities().add(vesselAvailability);
				availabilitiesMap.put(optionalAvailabilityShippingOption, vesselAvailability);

			} else if (shipping instanceof FleetShippingOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final FleetShippingOption fleetShippingOption = (FleetShippingOption) shipping;
				final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
				vesselAvailability.setTimeCharterRate(fleetShippingOption.getHireCost());
				final Vessel vessel = fleetShippingOption.getVessel();
				vesselAvailability.setVessel(vessel);
				vesselAvailability.setEntity(fleetShippingOption.getEntity());

				vesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
				vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());

				if (fleetShippingOption.isUseSafetyHeel()) {
					vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getStartHeel().setCvValue(22.8);
					// vesselAvailability.getStartHeel().setPricePerMMBTU(0.1);

					vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}
				vesselAvailability.setOptional(false);
				vesselAvailability.setFleet(true);
				clone.getCargoModel().getVesselAvailabilities().add(vesselAvailability);
				availabilitiesMap.put(fleetShippingOption, vesselAvailability);

			}
		}
		return availabilitiesMap;
	}

	protected static Map<ShippingOption, VesselAssignmentType> createShipping(final LNGScenarioModel clone, final OptionAnalysisModel model) {
		Map<ShippingOption, VesselAssignmentType> availabilitiesMap = new HashMap<>();

		for (final ShippingOption shipping : model.getShippingTemplates()) {

			if (shipping instanceof OptionalAvailabilityShippingOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final OptionalAvailabilityShippingOption optionalAvailabilityShippingOption = (OptionalAvailabilityShippingOption) shipping;
				final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
				vesselAvailability.setTimeCharterRate(optionalAvailabilityShippingOption.getHireCost());
				final Vessel vessel = optionalAvailabilityShippingOption.getVessel();
				vesselAvailability.setVessel(vessel);
				vesselAvailability.setEntity(optionalAvailabilityShippingOption.getEntity());

				vesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
				vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());

				if (optionalAvailabilityShippingOption.isUseSafetyHeel()) {
					vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getStartHeel().setCvValue(22.8);
					// vesselAvailability.getStartHeel().setPricePerMMBTU(0.1);

					vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}

				vesselAvailability.setStartAfter(optionalAvailabilityShippingOption.getStart().atStartOfDay());
				vesselAvailability.setStartBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setEndAfter(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setEndBy(optionalAvailabilityShippingOption.getEnd().atStartOfDay());
				vesselAvailability.setOptional(true);
				vesselAvailability.setFleet(false);
				vesselAvailability.setRepositioningFee(optionalAvailabilityShippingOption.getRepositioningFee());
				if (optionalAvailabilityShippingOption.getStartPort() != null) {
					vesselAvailability.setStartAt(optionalAvailabilityShippingOption.getStartPort());
				}
				if (optionalAvailabilityShippingOption.getEndPort() != null) {
					EList<APortSet<Port>> endAt = vesselAvailability.getEndAt();
					endAt.clear();
					endAt.add(optionalAvailabilityShippingOption.getEndPort());
				}
				clone.getCargoModel().getVesselAvailabilities().add(vesselAvailability);
				availabilitiesMap.put(optionalAvailabilityShippingOption, vesselAvailability);

			} else if (shipping instanceof FleetShippingOption) {
				// Do not re-add
				if (availabilitiesMap.containsKey(shipping)) {
					continue;
				}
				final FleetShippingOption fleetShippingOption = (FleetShippingOption) shipping;
				final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
				vesselAvailability.setTimeCharterRate(fleetShippingOption.getHireCost());
				final Vessel vessel = fleetShippingOption.getVessel();
				vesselAvailability.setVessel(vessel);
				vesselAvailability.setEntity(fleetShippingOption.getEntity());

				vesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
				vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());

				if (fleetShippingOption.isUseSafetyHeel()) {
					vesselAvailability.getStartHeel().setMaxVolumeAvailable(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getStartHeel().setCvValue(22.8);
					// vesselAvailability.getStartHeel().setPricePerMMBTU(0.1);

					vesselAvailability.getEndHeel().setMinimumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setMaximumEndHeel(vessel.getVesselClass().getMinHeel());
					vesselAvailability.getEndHeel().setTankState(EVesselTankState.MUST_BE_COLD);
				}
				vesselAvailability.setOptional(false);
				vesselAvailability.setFleet(true);
				clone.getCargoModel().getVesselAvailabilities().add(vesselAvailability);
				availabilitiesMap.put(fleetShippingOption, vesselAvailability);

			} else if (shipping instanceof RoundTripShippingOption) {
				final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shipping;
				final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
				final String baseName = SHIPPING_OPTION_DESCRIPTION_FORMATTER.render(roundTripShippingOption);
				@NonNull
				final Set<String> usedIDStrings = clone.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().stream().map(c -> c.getName()).collect(Collectors.toSet());
				final String id = AnalyticsBuilder.getUniqueID(baseName, usedIDStrings);
				market.setName(id);
				market.setCharterInRate(roundTripShippingOption.getHireCost());
				market.setVesselClass(roundTripShippingOption.getVesselClass());
				clone.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().add(market);

				availabilitiesMap.put(roundTripShippingOption, market);
			}
		}

		return availabilitiesMap;
	}

	// If bc is null, assume we want all data
	protected static void clearData(final LNGScenarioModel clone, final OptionAnalysisModel model, final @Nullable BaseCase bc) {

		clone.getScheduleModel().setSchedule(null);

		final Set<LoadSlot> keepLoads = new HashSet<>();
		final Set<DischargeSlot> keepDischarges = new HashSet<>();

		for (final BuyOption buy : model.getBuys()) {
			if (buy instanceof BuyReference && (bc == null || isUsed(buy, bc))) {
				final BuyReference buyReference = (BuyReference) buy;
				keepLoads.add(buyReference.getSlot());
			}
		}
		for (final SellOption sell : model.getSells()) {
			if (sell instanceof SellReference && (bc == null || isUsed(sell, bc))) {
				final SellReference sellReference = (SellReference) sell;
				keepDischarges.add(sellReference.getSlot());
			}
		}

		clone.getCargoModel().getCargoes().clear();
		clone.getCargoModel().getCargoGroups().clear();
		clone.getCargoModel().getLoadSlots().retainAll(keepLoads);
		clone.getCargoModel().getDischargeSlots().retainAll(keepDischarges);
		clone.getCargoModel().getVesselEvents().clear();
		clone.getCargoModel().getVesselAvailabilities().clear();

		clone.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		for (final LoadSlot loadSlot : keepLoads) {
			loadSlot.setCargo(null);
		}
		for (final DischargeSlot dischargeSlot : keepDischarges) {
			dischargeSlot.setCargo(null);
		}
	}

	private static boolean isUsed(final SellOption sell, final BaseCase bc) {

		for (final BaseCaseRow r : bc.getBaseCase()) {
			if (r.getSellOption() == sell) {
				return true;
			}
		}
		return false;
	}

	private static boolean isUsed(final BuyOption buy, final BaseCase bc) {

		for (final BaseCaseRow r : bc.getBaseCase()) {
			if (r.getBuyOption() == buy) {
				return true;
			}
		}
		return false;
	}
}
