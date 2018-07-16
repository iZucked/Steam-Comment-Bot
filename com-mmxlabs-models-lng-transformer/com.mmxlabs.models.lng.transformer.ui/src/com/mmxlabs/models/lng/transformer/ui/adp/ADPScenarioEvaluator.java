/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
	 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.adp;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Exposed;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPModelResult;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.services.IADPScenarioEvaluator;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.ui.ExportScheduleHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.transformer.ui.analytics.AnalyticsScenarioEvaluator;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotCharterInMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

public class ADPScenarioEvaluator implements IADPScenarioEvaluator {

	@Override
	public ADPModelResult runADPModel(@Nullable final ScenarioInstance scenarioInstance, //
			@NonNull final IScenarioDataProvider scenarioDataProvider, //
			@NonNull final ADPModel adpModel, //
			@NonNull final IProgressMonitor progressMonitor //
	) {
		final long start = System.currentTimeMillis();

		final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();

		userSettings.setCleanStateOptimisation(true);

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, scenarioDataProvider.getTypedScenario(LNGScenarioModel.class));
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// DEBUGGING
		ScenarioUtils.setLSOStageIterations(optimisationPlan, 1_000);
		ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 1_000);

		final List<String> hints = new LinkedList<>();
		// TODO: Add hints
		hints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);

		final String[] initialHints = hints.toArray(new String[hints.size()]);
		final CharterInMarket defaultMarket = createDefaultMarket(adpModel.getFleetProfile());

		// Generate internal data
		final CleanableExecutorService executorService = LNGScenarioChainBuilder.createExecutorService();
		try {

			final LNGScenarioToOptimiserBridge bridge = new LNGScenarioToOptimiserBridge(scenarioDataProvider, //
					scenarioInstance, //
					userSettings, //
					optimisationPlan.getSolutionBuilderSettings(), //
					scenarioDataProvider.getEditingDomain(), //
					null, // Bootstrap module
					OptimiserInjectorServiceMaker.begin()//
							.withModuleBindInstance(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, ADPModel.class, adpModel)//
							.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, createExtraDataModule(adpModel, defaultMarket))//
							.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_InitialSolution, createInitialSolutionModule(adpModel, defaultMarket))//
							.make(), //
					true, false, //
					initialHints // Hints? No Caching?
			);
			// Probably need to bring in the evaluation modules
			final Collection<IOptimiserInjectorService> services = bridge.getDataTransformer().getModuleServices();

			final ISequences initialSequences = bridge.getDataTransformer().getInitialSequences();// injector.getInstance(Key.get(IMultiStateResult.class,
			// Names.named(OptimiserConstants.SEQUENCE_TYPE_SEQUENCE_BUILDER)));

			// FIXME: Create ADP chain
			final IChainRunner chainRunner = LNGScenarioChainBuilder.createADPOptimisationChain(optimisationPlan.getResultName(), bridge.getDataTransformer(), bridge, optimisationPlan,
					executorService, new MultiStateResult(initialSequences, new HashMap<>()), initialHints);

			final IMultiStateResult result = chainRunner.run(progressMonitor);

			final NonNullPair<ISequences, Map<String, Object>> bestSolution = result.getBestSolution();
			final Schedule schedule = bridge.createSchedule(bestSolution.getFirst(), bestSolution.getSecond());

			final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
			scheduleModel.setSchedule(schedule);
			scheduleModel.setDirty(false);

			final ADPModelResult adpResult = ADPFactory.eINSTANCE.createADPModelResult();
			adpResult.setScheduleModel(scheduleModel);

			// Add new markets to extra bit
			for (final Sequence seq : schedule.getSequences()) {
				final CharterInMarket charterInMarket = seq.getCharterInMarket();
				if (charterInMarket != null) {
					if (charterInMarket.eContainer() == null) {
						adpResult.getExtraSpotCharterMarkets().add(charterInMarket);
					}
				}
			}

			// New spot slots etc will need to be contained here.
			for (final SlotAllocation a : schedule.getSlotAllocations()) {
				final Slot slot = a.getSlot();
				if (slot != null && slot.eContainer() == null) {
					adpResult.getExtraSlots().add(slot);
				}
			}
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {

				for (final SlotAllocation a : ca.getSlotAllocations()) {
					final Slot slot = a.getSlot();
					if (slot != null && slot.eContainer() == null) {
						adpResult.getExtraSlots().add(slot);
					}
				}
			}
			for (final OpenSlotAllocation ca : schedule.getOpenSlotAllocations()) {
				final Slot slot = ca.getSlot();
				if (slot != null && slot.eContainer() == null) {
					assert false;
				}
			}

			return adpResult;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			executorService.shutdownNow();
			System.out.println("done in:" + (System.currentTimeMillis() - start));
		}
	}

	private Module createInitialSolutionModule(final @NonNull ADPModel adpModel, final CharterInMarket defaultMarket) {
		return new PrivateModule() {

			@Override
			protected void configure() {
				// Nothing to do here - see provides methods
			}

			@Provides
			@Singleton
			@Named("ADP_SOLUTION")
			private ISequences provideSequences(final Injector injector, final ModelEntityMap modelEntityMap, final IScenarioDataProvider scenarioDataProvider, final IOptimisationData data) {
				final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
				final List<IResource> resources = new LinkedList<>();

				final List<ISequenceElement> allSequenceElements = new LinkedList<>();

				final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
				for (final PurchaseContractProfile p : adpModel.getPurchaseContractProfiles()) {
					if (p.isEnabled()) {
						for (final SubContractProfile<LoadSlot> sp : p.getSubProfiles()) {
							for (final Slot s : sp.getSlots()) {
								final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class);
								allSequenceElements.add(portSlotProvider.getElement(portSlot));
							}
						}
					}
				}
				for (final SalesContractProfile p : adpModel.getSalesContractProfiles()) {
					if (p.isEnabled()) {
						for (final SubContractProfile<DischargeSlot> sp : p.getSubProfiles()) {
							for (final Slot s : sp.getSlots()) {
								final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(s, IPortSlot.class);
								allSequenceElements.add(portSlotProvider.getElement(portSlot));
							}
						}
					}
				}

				final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioDataProvider);

				if (adpModel.getSpotMarketsProfile().isIncludeEnabledSpotMarkets()) {
					final List<SpotMarketGroup> groups = Lists.newArrayList( //
							spotMarketsModel.getFobPurchasesSpotMarket(), //
							spotMarketsModel.getDesPurchaseSpotMarket(), //
							spotMarketsModel.getDesSalesSpotMarket(), //
							spotMarketsModel.getFobSalesSpotMarket() //
					);
					for (final SpotMarketGroup group : groups) {
						for (final SpotMarket market : group.getMarkets()) {
							if (market.isEnabled()) {
								final ISpotMarket o_market = modelEntityMap.getOptimiserObjectNullChecked(market, ISpotMarket.class);
								final ISpotMarketSlotsProvider spotMarketSlotsProvider = injector.getInstance(ISpotMarketSlotsProvider.class);
								final List<@NonNull ISequenceElement> elements = spotMarketSlotsProvider.getElementsFor(o_market);
								allSequenceElements.addAll(elements);
							}
						}
					}
				}

				final FleetProfile fleetProfile = adpModel.getFleetProfile();
				{
					for (final VesselEvent vesselEvent : fleetProfile.getVesselEvents()) {
						final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(vesselEvent, IPortSlot.class);
						if (portSlot instanceof IVesselEventPortSlot) {
							final IVesselEventPortSlot eventPortSlot = (IVesselEventPortSlot) portSlot;
							allSequenceElements.addAll(eventPortSlot.getEventSequenceElements());
						} else {
							final ISequenceElement element = portSlotProvider.getElement(portSlot);
							allSequenceElements.add(element);
						}
					}
				}

				if (fleetProfile.isIncludeEnabledCharterMarkets()) {
					final List<CharterInMarket> charterInMarkets = spotMarketsModel.getCharterInMarkets();
					final ISpotCharterInMarketProvider spotCharterInMarketProvider = injector.getInstance(ISpotCharterInMarketProvider.class);

					for (final CharterInMarket charterInMarket : charterInMarkets) {
						if (charterInMarket.isNominal()) {
							final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);
							final IVesselAvailability o_availability = spotCharterInMarketProvider.getSpotMarketAvailability(market, -1);
							resources.add(vesselProvider.getResource(o_availability));
						}
						if (charterInMarket.isEnabled() && charterInMarket.getSpotCharterCount() > 0) {
							final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);
							final int spotCharterInMarketCount = spotCharterInMarketProvider.getSpotCharterInMarketCount(market);
							for (int i = 0; i < spotCharterInMarketCount; ++i) {
								final IVesselAvailability o_availability = spotCharterInMarketProvider.getSpotMarketAvailability(market, i);
								resources.add(vesselProvider.getResource(o_availability));
							}
						}
					}
				}
				{
					final ISpotCharterInMarketProvider spotCharterInMarketProvider = injector.getInstance(ISpotCharterInMarketProvider.class);

					{
						final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(defaultMarket, ISpotCharterInMarket.class);
						final IVesselAvailability o_availability = spotCharterInMarketProvider.getSpotMarketAvailability(market, -1);
						resources.add(vesselProvider.getResource(o_availability));
					}
				}

				for (final VesselAvailability vesselAvailability : fleetProfile.getVesselAvailabilities()) {
					final IVesselAvailability o_availability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);
					resources.add(vesselProvider.getResource(o_availability));
				}

				final IVirtualVesselSlotProvider virtualVesselSlotProvider = injector.getInstance(IVirtualVesselSlotProvider.class);

				for (final ISequenceElement e : allSequenceElements) {
					final IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(e);
					if (vesselAvailability != null) {
						final IResource o_resource = vesselProvider.getResource(vesselAvailability);
						resources.add(o_resource);
					}
				}

				final IModifiableSequences initialSequences = SequenceHelper.createEmptySequences(injector, resources);
				initialSequences.getModifiableUnusedElements().addAll(allSequenceElements);

				return initialSequences;
			}

			@Provides
			@Singleton
			@Named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)
			@Exposed
			private ISequences provideInitialSequences(@Named("ADP_SOLUTION") final ISequences sequences) {
				return sequences;
			}

			@Provides
			@Singleton
			@Named(LNGInitialSequencesModule.KEY_GENERATED_SOLUTION_PAIR)
			@Exposed
			private IMultiStateResult provideSolutionPair(@Named("ADP_SOLUTION") final ISequences sequences) {

				return new MultiStateResult(sequences, new HashMap<>());
			}

		};
	}

	private Module createExtraDataModule(final @NonNull ADPModel adpModel, final CharterInMarket defaultMarket) {
		final List<VesselAvailability> extraAvailabilities = new LinkedList<>();
		final List<VesselEvent> extraVesselEvents = new LinkedList<>();
		final List<CharterInMarketOverride> extraCharterInMarketOverrides = new LinkedList<>();
		final List<CharterInMarket> extraCharterInMarkets = new LinkedList<>();

		final List<LoadSlot> extraLoads = new LinkedList<>();
		final List<DischargeSlot> extraDischarges = new LinkedList<>();

		for (final PurchaseContractProfile p : adpModel.getPurchaseContractProfiles()) {
			if (p.isEnabled()) {
				for (final SubContractProfile<LoadSlot> sp : p.getSubProfiles()) {
					extraLoads.addAll(sp.getSlots());
				}
			}
		}
		for (final SalesContractProfile p : adpModel.getSalesContractProfiles()) {
			if (p.isEnabled()) {
				for (final SubContractProfile<DischargeSlot> sp : p.getSubProfiles()) {
					extraDischarges.addAll(sp.getSlots());
				}
			}
		}

		final FleetProfile fleetProfile = adpModel.getFleetProfile();
		if (fleetProfile != null) {
			extraAvailabilities.addAll(fleetProfile.getVesselAvailabilities());
			extraVesselEvents.addAll(fleetProfile.getVesselEvents());

			extraCharterInMarkets.add(defaultMarket);
		} else {
			throw new IllegalStateException();
		}

		return new AbstractModule() {

			@Provides
			@Named(OptimiserConstants.DEFAULT_VESSEL)
			private IVesselAvailability provideDefaultVessel(final ModelEntityMap modelEntityMap, final IVesselProvider vesselProvider, final IOptimisationData optimisationData) {
				final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(defaultMarket, ISpotCharterInMarket.class);

				for (final IResource o_resource : optimisationData.getResources()) {
					final IVesselAvailability o_vesselAvailability = vesselProvider.getVesselAvailability(o_resource);
					if (o_vesselAvailability.getSpotCharterInMarket() != market) {
						continue;
					}
					if (o_vesselAvailability.getSpotIndex() == -1) {
						return o_vesselAvailability;
					}

				}
				throw new IllegalStateException();
			}

			@Override
			protected void configure() {
				bind(ADPModel.class).toInstance(adpModel);
			}

			@Provides
			@Named(LNGScenarioTransformer.EXTRA_VESSEL_AVAILABILITIES)
			private List<VesselAvailability> provideExtraAvailabilities() {
				return extraAvailabilities;
			}

			@Provides
			@Named(LNGScenarioTransformer.EXTRA_VESSEL_EVENTS)
			private List<VesselEvent> provideExtraVesselEvents() {
				return extraVesselEvents;
			}

			@Provides
			@Named(LNGScenarioTransformer.EXTRA_CHARTER_IN_MARKET_OVERRIDES)
			private List<CharterInMarketOverride> provideCharterInMarketOverrides() {
				return extraCharterInMarketOverrides;
			}

			@Provides
			@Named(LNGScenarioTransformer.EXTRA_CHARTER_IN_MARKETS)
			private List<CharterInMarket> provideCharterInMarkets() {
				return extraCharterInMarkets;
			}

			@Provides
			@Named(LNGScenarioTransformer.EXTRA_LOAD_SLOTS)
			private List<LoadSlot> provideLoadSlots() {
				return extraLoads;
			}

			@Provides
			@Named(LNGScenarioTransformer.EXTRA_DISCHARGE_SLOTS)
			private List<DischargeSlot> provideDischargeSlots() {
				return extraDischarges;
			}
		};
	}

	private CharterInMarket createDefaultMarket(final FleetProfile fleetProfile) {
		final CharterInMarket defaultMarket;
		defaultMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();

		defaultMarket.setVessel(fleetProfile.getDefaultVessel());
		defaultMarket.setCharterInRate(fleetProfile.getDefaultVesselCharterInRate());
		defaultMarket.setNominal(true);
		defaultMarket.setEnabled(true);
		defaultMarket.setName("ADP Default Vessel");
		return defaultMarket;
	}

	@Override
	public ScenarioInstance export(final ScenarioResult scenarioResult, final String name) throws Exception {
		return ExportScheduleHelper.export(scenarioResult, name, false, (scenario, input_schedule) -> {
			scenario.getAdpModels().clear();
			AnalyticsScenarioEvaluator.createModelCustomiser().accept(scenario, input_schedule);
		});
	}
}
