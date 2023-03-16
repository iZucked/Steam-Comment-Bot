/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.valuematrix;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Sets;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.Mapper;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.SlotMode;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.SwapValueMatrixUnit;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelReferenceThread;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;

public class ValueMatrixTask {

	@NonNull
	public static Pair<@NonNull LoadSlot, @NonNull DischargeSlot> buildFullScenario(@NonNull final LNGScenarioModel root, @NonNull final SwapValueMatrixModel model, final IMapperClass mapper) {
		final Set<String> usedIDs = getUsedSlotIDs(root);
		final BuyReference loadReference = model.getParameters().getBaseLoad();
		final ExistingVesselCharterOption vesselCharterOption = model.getParameters().getBaseVesselCharter();
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
		final SellReference dischargeReference = model.getParameters().getBaseDischarge();
		if (dischargeReference == null) {
			throw new IllegalStateException("Swap model missing discharge slot information");
		}
		final DischargeSlot dischargeSlot = dischargeReference.getSlot();
		if (dischargeSlot == null) {
			throw new IllegalStateException("Swap model missing discharge slot information");
		}
		final BuyMarket desPurchaseMarketContainer = model.getParameters().getSwapLoadMarket();
		if (desPurchaseMarketContainer == null || desPurchaseMarketContainer.getMarket() == null || desPurchaseMarketContainer.getMonth() == null) {
			throw new IllegalStateException("Swap model missing DES purchase market information");
		}
		final SellMarket desSalesMarketContainer = model.getParameters().getSwapDischargeMarket();
		if (desSalesMarketContainer == null || desSalesMarketContainer.getMarket() == null || desSalesMarketContainer.getMonth() == null) {
			throw new IllegalStateException("Swap model missing DES sales market information");
		}
		final LoadSlot desPurchaseLoadSlot = AnalyticsBuilder.makeLoadSlot(desPurchaseMarketContainer, root, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);
		mapper.addMapping(desPurchaseMarketContainer.getMarket(), desPurchaseMarketContainer.getMonth(), desPurchaseLoadSlot, null, null);
		final DischargeSlot desSalesDischargeSlot = AnalyticsBuilder.makeDischargeSlot(desSalesMarketContainer, root, SlotMode.CHANGE_PRICE_VARIANT, usedIDs);
		mapper.addMapping(desSalesMarketContainer.getMarket(), desSalesMarketContainer.getMonth(), desSalesDischargeSlot, null, null);
		return Pair.of(loadSlot, dischargeSlot);
	}

	public static void submit(final ScenarioInstance scenarioInstance, final SwapValueMatrixModel valueMatrixModel) {
		submit(scenarioInstance, valueMatrixModel, true);
	}

	public static void submit(final ScenarioInstance scenarioInstance, final SwapValueMatrixModel valueMatrixModel, boolean withUI) {
		final ScenarioModelRecord scenarioModelRecord = SSDataManager.Instance.getModelRecordChecked(scenarioInstance);
		submit(scenarioModelRecord, valueMatrixModel, withUI);
	}

	public static void submit(final ScenarioModelRecord scenarioModelRecord, final SwapValueMatrixModel valueMatrixModel, boolean withUI) {
		final ScenarioModelReferenceThread thread = new ScenarioModelReferenceThread("Value Matrix", scenarioModelRecord, sdp -> {
			final ScenarioLock scenarioLock = sdp.getModelReference().getLock();
			scenarioLock.withTryLock(2_000, () -> {
				final boolean useDialogs = withUI && System.getProperty("lingo.suppress.dialogs") == null;
				final UserSettings userSettings;
				if (useDialogs) {
					userSettings = RunnerHelper.syncExecFunc(display -> OptimisationHelper.getEvaluationSettings(sdp.getTypedScenario(LNGScenarioModel.class), useDialogs, true));
				} else {
					userSettings = OptimisationHelper.getEvaluationSettings(sdp.getTypedScenario(LNGScenarioModel.class), useDialogs, true);
				}

				if (userSettings == null) {
					return;
				}
				RunnerHelper.syncExecDisplayOptional(() -> {
					final EditingDomain editingDomain = sdp.getEditingDomain();
					final CompoundCommand cmd = new CompoundCommand("Update settings");
					cmd.append(SetCommand.create(editingDomain, sdp.getTypedScenario(LNGScenarioModel.class), LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(),
							EMFCopier.copy(userSettings)));
				});

				boolean relaxedValidation = "Period Scenario".equals(scenarioModelRecord.getName());
				if (!OptimisationHelper.validateScenario(sdp, valueMatrixModel, false, true, relaxedValidation, Sets.newHashSet(".valuematrix"))) {
					return;
				}
				final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);
				final IMapperClass mapper = new Mapper(lngScenarioModel, false);
				@NonNull
				final Pair<@NonNull LoadSlot, @NonNull DischargeSlot> swapCargo = buildFullScenario(lngScenarioModel, valueMatrixModel, mapper);

				final ScheduleSpecificationHelper helper = new ScheduleSpecificationHelper(sdp);
				helper.processExtraDataProvider(mapper.getExtraDataProvider());

				final List<String> hints = new LinkedList<>();
				hints.add(SchedulerConstants.HINT_DISABLE_CACHES);
				final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings(false);

				final JobExecutorFactory jobExecutorFactory = LNGScenarioChainBuilder.createExecutorService();
				helper.generateWith(scenarioModelRecord.getScenarioInstance(), userSettings, sdp.getEditingDomain(), hints, bridge -> {
					final LNGDataTransformer dataTransformer = bridge.getDataTransformer();
					bridge.getFullDataTransformer().getLifecyleManager().startPhase("value-matrix", hints);
					final SwapValueMatrixUnit unit = new SwapValueMatrixUnit(sdp, dataTransformer, "swap-value-matrix", userSettings, constraintAndFitnessSettings, jobExecutorFactory,
							dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), dataTransformer.getHints());
					unit.run(valueMatrixModel, swapCargo, mapper, new NullProgressMonitor(), bridge, new DefaultValueMatrixPairsGenerator(valueMatrixModel));
				});
			});
		});
		thread.start();
	}

	private static Set<String> getUsedSlotIDs(final LNGScenarioModel lngScenarioModel) {
		final Set<String> usedIDs = new HashSet<>();
		usedIDs.addAll(lngScenarioModel.getCargoModel().getLoadSlots().stream().map(LoadSlot::getName).collect(Collectors.toSet()));
		usedIDs.addAll(lngScenarioModel.getCargoModel().getDischargeSlots().stream().map(DischargeSlot::getName).collect(Collectors.toSet()));
		return usedIDs;
	}
}
