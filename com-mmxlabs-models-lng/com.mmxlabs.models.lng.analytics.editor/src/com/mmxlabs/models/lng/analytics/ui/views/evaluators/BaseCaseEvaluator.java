package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class BaseCaseEvaluator {

	public interface IMapperClass {

		LoadSlot get(BuyOption buy);

		DischargeSlot get(SellOption sell);

		void addMapping(BuyOption buy, LoadSlot load);

		void addMapping(SellOption sell, DischargeSlot discharge);

	}

	private static final ShippingOptionDescriptionFormatter SHIPPING_OPTION_DESCRIPTION_FORMATTER = new ShippingOptionDescriptionFormatter();
	static class Mapper implements IMapperClass {
		private final EcoreUtil.Copier copier;

		Map<BuyOption, LoadSlot> buyMap = new HashMap<>();
		Map<SellOption, DischargeSlot> sellMap = new HashMap<>();

		Mapper(final Copier copier) {
			this.copier = copier;
		}

		@Override
		public LoadSlot get(final BuyOption buy) {
			return buyMap.get(buy);
		}

		@Override
		public DischargeSlot get(final SellOption sell) {
			return sellMap.get(sell);
		}

		@Override
		public void addMapping(final BuyOption buy, final LoadSlot load) {
			buyMap.put(buy, load);
			if (copier.containsKey(buy)) {
				buyMap.put((BuyOption) copier.get(buy), load);
			} else {
				for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
					if (e.getValue() == buy) {
						buyMap.put((BuyOption) e.getKey(), load);
					}
				}
			}
		}

		@Override
		public void addMapping(final SellOption sell, final DischargeSlot discharge) {
			sellMap.put(sell, discharge);
			if (copier.containsKey(discharge)) {
				sellMap.put((SellOption) copier.get(sell), discharge);
			} else {
				for (final Map.Entry<EObject, EObject> e : copier.entrySet()) {
					if (e.getValue() == sell) {
						sellMap.put((SellOption) e.getKey(), discharge);

					}
				}
			}

		}
	}

	public static LNGScenarioModel generateScenario(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model, final BaseCase baseCase,
			final BiConsumer<LNGScenarioModel, IMapperClass> callback) {

		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			List<Port> dd = lngScenarioModel.getReferenceModel().getPortModel().getPorts().stream().filter(p->p.getName().contains("Darwin")).collect(Collectors.toList());

			final EcoreUtil.Copier copier = new Copier();
			final LNGScenarioModel clone = (LNGScenarioModel) copier.copy(lngScenarioModel);
			List<Port> ddd = clone.getReferenceModel().getPortModel().getPorts().stream().filter(p->p.getName().contains("Darwin")).collect(Collectors.toList());
			final OptionAnalysisModel clonedModel = (OptionAnalysisModel) copier.copy(model);
			final BaseCase clonedBaseCase;
			if (model.getBaseCase() == baseCase) {
				clonedBaseCase = clonedModel.getBaseCase();
			} else {
				clonedBaseCase = (BaseCase) copier.copy(baseCase);
			}
			copier.copyReferences();
			List<Port> dddd = clone.getReferenceModel().getPortModel().getPorts().stream().filter(p->p.getName().contains("Darwin")).collect(Collectors.toList());
			final IMapperClass mapper = new Mapper(copier);

			clearData(clone, clonedModel, clonedBaseCase);

			buildScenario(clone, clonedModel, clonedBaseCase, mapper);

			callback.accept(clone, mapper);

			return clone;

		}
		return null;
	}

	public static void evaluate(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel model, final BaseCase baseCase, final boolean fork, final String forkName) {

		generateScenario(scenarioEditingLocation, model, baseCase, (clone, mapper) -> {
			evaluateScenario(clone, scenarioEditingLocation.getScenarioInstance(), fork, forkName);

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
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		ServiceHelper.<IAnalyticsScenarioEvaluator> withService(IAnalyticsScenarioEvaluator.class, evaluator -> evaluator.evaluate(lngScenarioModel, userSettings, scenarioInstance, fork, forkName));

	}

	protected static void buildScenario(final LNGScenarioModel clone, final OptionAnalysisModel clonedModel, final BaseCase clonedBaseCase, final IMapperClass mapper) {

		createShipping(clone, clonedBaseCase);

		for (final BaseCaseRow row : clonedBaseCase.getBaseCase()) {
			final BuyOption buy = row.getBuyOption();
			final LoadSlot loadSlot = AnalyticsBuilder.makeLoadSlot(buy, clone);

			mapper.addMapping(buy, loadSlot);

			final SellOption sell = row.getSellOption();
			final DischargeSlot dischargeSlot = AnalyticsBuilder.makeDischargeSlot(sell, clone);
			mapper.addMapping(sell, dischargeSlot);

			Cargo cargo = null;
			if (loadSlot != null && dischargeSlot != null) {
				cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);
			}

			setShipping(loadSlot, dischargeSlot, cargo, row.getShipping(), clone);

			if (loadSlot != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot);
			}
			if (dischargeSlot != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot);
			}
			if (cargo != null && !clone.getCargoModel().getCargoes().contains(cargo)) {
				clone.getCargoModel().getCargoes().add(cargo);
			}
		}

	}

	protected static void setShipping(final @Nullable LoadSlot loadSlot, final @Nullable DischargeSlot dischargeSlot, final @Nullable Cargo cargo, final @Nullable ShippingOption shipping,
			final @NonNull LNGScenarioModel lngScenarioModel) {
		if (shipping instanceof NominatedShippingOption) {
			final NominatedShippingOption nominatedShippingOption = (NominatedShippingOption) shipping;
			if (loadSlot != null && loadSlot.isDESPurchase()) {
				loadSlot.setNominatedVessel(nominatedShippingOption.getNominatedVessel());
			}
			if (dischargeSlot != null && dischargeSlot.isFOBSale()) {
				dischargeSlot.setNominatedVessel(nominatedShippingOption.getNominatedVessel());
			}

			if (loadSlot.getWindowStart() != null && dischargeSlot.getWindowStart() == null) {
				dischargeSlot.setWindowStart(loadSlot.getWindowStart());
			} else if (loadSlot.getWindowStart() == null && dischargeSlot.getWindowStart() != null) {
				loadSlot.setWindowStart(dischargeSlot.getWindowStart());
			}

		} else if (shipping instanceof FleetShippingOption) {
			final FleetShippingOption fleetShippingOption = (FleetShippingOption) shipping;
			if (cargo != null) {
				final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
				vesselAvailability.setTimeCharterRate(fleetShippingOption.getHireCost());
				vesselAvailability.setVessel(fleetShippingOption.getVessel());
				vesselAvailability.setEntity(fleetShippingOption.getEntity());
				cargo.setVesselAssignmentType(vesselAvailability);
				lngScenarioModel.getCargoModel().getVesselAvailabilities().add(vesselAvailability);

				// TODO: Calculate time.
				if (loadSlot.getWindowStart() != null && dischargeSlot.getWindowStart() == null) {
					int travelDays = AnalyticsBuilder.calculateTravelDaysForDischarge(loadSlot, dischargeSlot, shipping);
					dischargeSlot.setWindowStart(loadSlot.getWindowStart().plusDays(travelDays));
				} else if (loadSlot.getWindowStart() == null && dischargeSlot.getWindowStart() != null) {
					int travelDays = AnalyticsBuilder.calculateTravelDaysForLoad(loadSlot, dischargeSlot, shipping);
					loadSlot.setWindowStart(dischargeSlot.getWindowStart().minusDays(travelDays));
				}
			}
		} else if (shipping instanceof RoundTripShippingOption) {
			final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shipping;
			if (cargo != null) {
				final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
				String baseName = SHIPPING_OPTION_DESCRIPTION_FORMATTER.render(roundTripShippingOption);
				@NonNull
				Set<String> usedIDStrings = lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets()
					.stream().map(c -> c.getName()).collect(Collectors.toSet());
				String id = AnalyticsBuilder.getUniqueID(baseName, usedIDStrings);
				market.setName(id);
				market.setCharterInRate(roundTripShippingOption.getHireCost());
				market.setVesselClass(roundTripShippingOption.getVesselClass());
				cargo.setVesselAssignmentType(market);
				cargo.setSpotIndex(-1);
				lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().add(market);

				// TODO: Calculate time.
				if (loadSlot.getWindowStart() != null && dischargeSlot.getWindowStart() == null) {
					int travelDays = AnalyticsBuilder.calculateTravelDaysForDischarge(loadSlot, dischargeSlot, shipping);
					dischargeSlot.setWindowStart(loadSlot.getWindowStart().plusDays(travelDays));
				} else if (loadSlot.getWindowStart() == null && dischargeSlot.getWindowStart() != null) {
					int travelDays = AnalyticsBuilder.calculateTravelDaysForLoad(loadSlot, dischargeSlot, shipping);
					loadSlot.setWindowStart(dischargeSlot.getWindowStart().minusDays(travelDays));
				}
			}
		}
	}

	protected static void createShipping(final LNGScenarioModel clone, final BaseCase clonedBaseCase) {
		// TODO Auto-generated method stub

	}

	protected static void clearData(final LNGScenarioModel clone, final OptionAnalysisModel model, final BaseCase bc) {

		clone.getScheduleModel().setSchedule(null);

		final Set<LoadSlot> keepLoads = new HashSet<>();
		final Set<DischargeSlot> keepDischarges = new HashSet<>();

		for (final BuyOption buy : model.getBuys()) {
			if (buy instanceof BuyReference && isUsed(buy, bc)) {
				final BuyReference buyReference = (BuyReference) buy;
				keepLoads.add(buyReference.getSlot());
			}
		}
		for (final SellOption sell : model.getSells()) {
			if (sell instanceof SellReference && isUsed(sell, bc)) {
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
