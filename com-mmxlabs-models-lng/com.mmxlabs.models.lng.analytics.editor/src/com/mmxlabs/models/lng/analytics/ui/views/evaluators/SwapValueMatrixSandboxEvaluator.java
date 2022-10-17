package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.ServiceHelper;

public class SwapValueMatrixSandboxEvaluator {
	
	@NonNull
	protected static Pair<@NonNull LoadSlot, @NonNull DischargeSlot> buildFullScenario(@NonNull final LNGScenarioModel root, @NonNull final SwapValueMatrixModel model, final IMapperClass mapper) {
		final Set<String> usedIDs = getUsedSlotIDs(root);
		final BuyReference loadReference = model.getBaseLoad();
		final ExistingVesselCharterOption vesselCharterOption = model.getBaseVesselCharter();
		if (vesselCharterOption == null) {
			throw new IllegalStateException("Swap model missing vessel charter information");
		}
		if (loadReference == null) {
			throw new IllegalStateException("Swap model missing load slot information");
		}
		final LoadSlot loadSlot = loadReference.getSlot();
		if (loadSlot == null) {
			throw new IllegalStateException("Swap model missing load slot information");
		}
		final SellReference dischargeReference = model.getBaseDischarge();
		if (dischargeReference == null) {
			throw new IllegalStateException("Swap model missing discharge slot information");
		}
		final DischargeSlot dischargeSlot = dischargeReference.getSlot();
		if (dischargeSlot == null) {
			throw new IllegalStateException("Swap model missing discharge slot information");
		}
		final BuyMarket desPurchaseMarketContainer = model.getSwapLoadMarket();
		if (desPurchaseMarketContainer == null || desPurchaseMarketContainer.getMarket() == null || desPurchaseMarketContainer.getMonth() == null) {
			throw new IllegalStateException("Swap model missing DES purchase market information");
		}
		final SellMarket desSalesMarketContainer = model.getSwapDischargeMarket();
		if (desSalesMarketContainer == null || desSalesMarketContainer.getMarket() == null || desSalesMarketContainer.getMonth() == null) {
			throw new IllegalStateException("Swap model missing DES sales market information");
		}
		final LoadSlot desPurchaseLoadSlot = AnalyticsBuilder.makeLoadSlot(desPurchaseMarketContainer, root, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);
		mapper.addMapping(desPurchaseMarketContainer.getMarket(), desPurchaseMarketContainer.getMonth(), desPurchaseLoadSlot, null, null);
		final DischargeSlot desSalesDischargeSlot = AnalyticsBuilder.makeDischargeSlot(desSalesMarketContainer, root, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);
		mapper.addMapping(desSalesMarketContainer.getMarket(), desSalesMarketContainer.getMonth(), desSalesDischargeSlot, null, null);
		return Pair.of(loadSlot, dischargeSlot);
	}

	public static void doValueMatrixSandbox(IScenarioEditingLocation scenarioEditingLocation, @NonNull final SwapValueMatrixModel swapValueMatrixModel) {
		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (rootObject instanceof @NonNull final LNGScenarioModel lngScenarioModel) {
			final IMapperClass mapper = new Mapper(lngScenarioModel, false);
			@NonNull
			final Pair<@NonNull LoadSlot, @NonNull DischargeSlot> swapCargo = buildFullScenario(lngScenarioModel, swapValueMatrixModel, mapper);
			
			final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
			userSettings.setGenerateCharterOuts(false);
			userSettings.setShippingOnly(false);
			userSettings.setWithSpotCargoMarkets(true);
			userSettings.setSimilarityMode(SimilarityMode.OFF);

			@NonNull
			final Consumer<@NonNull IAnalyticsScenarioEvaluator> f1 = evaluator -> evaluator.evaluateSwapValueMatrixSandbox(scenarioEditingLocation.getScenarioDataProvider(),
					scenarioEditingLocation.getScenarioInstance(), userSettings, swapValueMatrixModel, swapCargo, mapper);
			ServiceHelper.withServiceConsumer(IAnalyticsScenarioEvaluator.class, f1);
		}
	}

	private static Set<String> getUsedSlotIDs(final LNGScenarioModel lngScenarioModel) {
		final Set<String> usedIDs = new HashSet<>();
		usedIDs.addAll(lngScenarioModel.getCargoModel().getLoadSlots().stream()
				.map(LoadSlot::getName)
				.collect(Collectors.toSet()));
		usedIDs.addAll(lngScenarioModel.getCargoModel().getDischargeSlots().stream()
				.map(DischargeSlot::getName)
				.collect(Collectors.toSet()));
		return usedIDs;
	}
}
