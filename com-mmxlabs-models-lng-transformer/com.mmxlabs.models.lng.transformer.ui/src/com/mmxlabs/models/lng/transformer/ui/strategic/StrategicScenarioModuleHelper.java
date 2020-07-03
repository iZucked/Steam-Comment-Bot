/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
	 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.strategic;

import java.util.Collections;
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
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
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

public class StrategicScenarioModuleHelper {
	private static final String KEY_EMPTY_SOLUTION = "EMPTY_SOLUTION";
	private static final String KEY_DEFAULT_MARKET = "DEFAULT_MARKET";

	private StrategicScenarioModuleHelper() {

	}

	public static @NonNull Module createExtraDataModule() {

		return new AbstractModule() {

			@Provides
			@Singleton
			@Named(KEY_DEFAULT_MARKET)
			private CharterInMarket provideDefaultMarket(LNGScenarioModel scenarioModel) {

				Vessel vessel = null;
				String rate = null;
				BaseLegalEntity entity = null;
				CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
				for (VesselAvailability va : cargoModel.getVesselAvailabilities()) {
					vessel = va.getVessel();
					rate = va.getTimeCharterRate();
					entity = va.getEntity();
					break;
				}
				if (vessel == null) {
					FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
					for (Vessel v : fleetModel.getVessels()) {
						vessel = v;
						break;
					}
				}
				if (entity == null) {
					CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
					for (BaseLegalEntity e : commercialModel.getEntities()) {
						entity = e;
						break;
					}
				}

				CharterInMarket marketOption = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
				marketOption.setName("Stragetic Nominal");
				marketOption.setVessel(vessel);
				marketOption.setEntity(entity);
				marketOption.setEnabled(true);
				marketOption.setNominal(true);

				if (rate != null && !rate.isEmpty()) {
					marketOption.setCharterInRate(rate);
				} else {
					marketOption.setCharterInRate("80000");
				}
				return marketOption;
			}

			@Provides
			@Named(OptimiserConstants.DEFAULT_VESSEL)
			private IVesselAvailability provideDefaultVessel(final ModelEntityMap modelEntityMap, //
					final IVesselProvider vesselProvider, //
					final IOptimisationData optimisationData, // )
					@Named(KEY_DEFAULT_MARKET) CharterInMarket market) {

				ISpotCharterInMarket o_market = modelEntityMap.getOptimiserObjectNullChecked(market, ISpotCharterInMarket.class);

				for (final IResource o_resource : optimisationData.getResources()) {
					final IVesselAvailability o_vesselAvailability = vesselProvider.getVesselAvailability(o_resource);

					if (o_vesselAvailability.getSpotCharterInMarket() != o_market) {
						continue;
					}

					if (o_vesselAvailability.getSpotIndex() == -1) {
						return o_vesselAvailability;
					}
				}
				throw new IllegalStateException();
			}

			@Provides
			@Named(LNGScenarioTransformer.EXTRA_CHARTER_IN_MARKETS)
			@Singleton
			private List<CharterInMarket> provideDefaultVessel(@Named(KEY_DEFAULT_MARKET) CharterInMarket market) {
				return Collections.singletonList(market);
			}

			@Override
			protected void configure() {
				// Nothing to bind by default
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
