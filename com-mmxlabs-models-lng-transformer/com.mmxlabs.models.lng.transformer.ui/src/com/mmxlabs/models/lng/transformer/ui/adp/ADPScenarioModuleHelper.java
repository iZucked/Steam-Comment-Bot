/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
	 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.adp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.AbstractModule;
import com.google.inject.Exposed;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class ADPScenarioModuleHelper {
	//
	// @Override
	// public boolean runADPModel(@Nullable final ScenarioInstance scenarioInstance, //
	// @NonNull final IScenarioDataProvider scenarioDataProvider, //
	// @NonNull final ADPModel adpModel, //
	// @NonNull final IProgressMonitor progressMonitor //
	// ) {
	// final long start = System.currentTimeMillis();
	//
	// final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
	//
	// userSettings.setCleanStateOptimisation(true);
	//
	// OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, scenarioDataProvider.getTypedScenario(LNGScenarioModel.class));
	// optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);
	//
	// // DEBUGGING
	// // DO NOT COMMIT
	// ScenarioUtils.setLSOStageIterations(optimisationPlan, 1_000);
	// ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 1_000);
	//
	// final List<String> hints = new LinkedList<>();
	// // TODO: Add hints
	// hints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);
	//
	// final String[] initialHints = hints.toArray(new String[hints.size()]);
	// // final CharterInMarket defaultMarket = adpModel.getFleetProfile().getDefaultNominalMarket();
	//
	// // Generate internal data
	// final CleanableExecutorService executorService = LNGScenarioChainBuilder.createExecutorService();
	// try {
	//
	// final LNGScenarioToOptimiserBridge bridge = new LNGScenarioToOptimiserBridge(scenarioDataProvider, //
	// scenarioInstance, //
	// userSettings, //
	// optimisationPlan.getSolutionBuilderSettings(), //
	// scenarioDataProvider.getEditingDomain(), //
	// null, // Bootstrap module
	// OptimiserInjectorServiceMaker.begin()//
	// .withModuleBindInstance(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, ADPModel.class, adpModel)//
	// .withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, createExtraDataModule(adpModel))//
	// // .withModuleOverride(IOptimiserInjectorService.ModuleType.Module_InitialSolution, createInitialSolutionModule(adpModel, defaultMarket))//
	// .make(), //
	// true, false, //
	// initialHints // Hints? No Caching?
	// );
	// // Probably need to bring in the evaluation modules
	// final Collection<IOptimiserInjectorService> services = bridge.getDataTransformer().getModuleServices();
	//
	// final ISequences initialSequences = bridge.getDataTransformer().getInitialSequences();
	//
	// // FIXME: Create ADP chain
	// final IChainRunner chainRunner = LNGScenarioChainBuilder.createADPOptimisationChain(optimisationPlan.getResultName(), bridge.getDataTransformer(), bridge, optimisationPlan,
	// executorService, new MultiStateResult(initialSequences, new HashMap<>()), initialHints);
	//
	// LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, scenarioDataProvider, scenarioInstance, bridge, chainRunner, null);
	//
	// final IMultiStateResult result = scenarioRunner.runWithProgress(progressMonitor);
	// return result != null;
	//
	// } catch (final InfeasibleSolutionException e) {
	// throw e;
	// } catch (final Exception e) {
	// e.printStackTrace();
	// throw new RuntimeException(e);
	// } finally {
	// executorService.shutdownNow();
	// System.out.println("done in:" + (System.currentTimeMillis() - start));
	// }
	// }

	public static @NonNull Module createExtraDataModule(final @NonNull ADPModel adpModel) {

		return new AbstractModule() {

			@Provides
			@Named(OptimiserConstants.DEFAULT_VESSEL)
			private IVesselAvailability provideDefaultVessel(final ModelEntityMap modelEntityMap, final IVesselProvider vesselProvider, final IOptimisationData optimisationData) {

				final CharterInMarket defaultMarket = adpModel.getFleetProfile().getDefaultNominalMarket();

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
		};
	}

	public static @NonNull Module createEmptySolutionModule() {
		return new PrivateModule() {

			@Override
			protected void configure() {
				// Nothing to do here - see provides methods
			}

			@Provides
			@Singleton
			@Named("EMPTY_SOLUTION")
			private ISequences provideSequences(final Injector injector, final ModelEntityMap modelEntityMap, final IScenarioDataProvider scenarioDataProvider, final IOptimisationData data) {
				final IModifiableSequences initialSequences = SequenceHelper.createEmptySequences(injector, data.getResources());
				// Ensure they are cleared
				initialSequences.getModifiableUnusedElements().clear();

				// Get all elements
				final List<ISequenceElement> allSequenceElements = new LinkedList<>(data.getSequenceElements());
				// ... remove the used start/end elements
				for (IResource r : initialSequences.getResources()) {
					for (ISequenceElement e : initialSequences.getSequence(r)) {
						allSequenceElements.remove(e);
					}
				}
				// ... stick rest in unused list
				initialSequences.getModifiableUnusedElements().addAll(allSequenceElements);

				return initialSequences;
			}

			@Provides
			@Singleton
			@Named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)
			@Exposed
			private ISequences provideInitialSequences(@Named("EMPTY_SOLUTION") final ISequences sequences) {
				return sequences;
			}

			@Provides
			@Singleton
			@Named(LNGInitialSequencesModule.KEY_GENERATED_SOLUTION_PAIR)
			@Exposed
			private IMultiStateResult provideSolutionPair(@Named("EMPTY_SOLUTION") final ISequences sequences) {

				return new MultiStateResult(sequences, new HashMap<>());
			}

		};
	}
}
