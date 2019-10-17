/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
	private static final String KEY_EMPTY_SOLUTION = "EMPTY_SOLUTION";

	private ADPScenarioModuleHelper() {

	}

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
			@Named(KEY_EMPTY_SOLUTION)
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
			private ISequences provideInitialSequences(@Named(KEY_EMPTY_SOLUTION) final ISequences sequences) {
				return sequences;
			}

			@Provides
			@Singleton
			@Named(LNGInitialSequencesModule.KEY_GENERATED_SOLUTION_PAIR)
			@Exposed
			private IMultiStateResult provideSolutionPair(@Named(KEY_EMPTY_SOLUTION) final ISequences sequences) {

				return new MultiStateResult(sequences, new HashMap<>());
			}

		};
	}
}
