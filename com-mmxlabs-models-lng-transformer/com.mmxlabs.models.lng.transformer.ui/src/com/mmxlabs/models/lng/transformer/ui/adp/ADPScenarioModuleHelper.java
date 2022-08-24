/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class ADPScenarioModuleHelper {
	private static final String KEY_EMPTY_SOLUTION = "EMPTY_SOLUTION";

	private ADPScenarioModuleHelper() {

	}

	public static @NonNull Module createExtraDataModule(final @NonNull ADPModel adpModel) {

		return new AbstractModule() {

			@Provides
			@Named(OptimiserConstants.DEFAULT_INTERNAL_VESSEL)
			private IVesselCharter provideDefaultVessel(final ModelEntityMap modelEntityMap, final IVesselProvider vesselProvider, final IOptimisationData optimisationData) {

				final IVesselCharter va = (IVesselCharter) modelEntityMap.getNamedOptimiserObject(OptimiserConstants.DEFAULT_INTERNAL_VESSEL);
				if (va != null) {
					return va;
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

				populateThirdPartyCargoes(initialSequences, injector, modelEntityMap, scenarioDataProvider, data);

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

			private void populateThirdPartyCargoes(@NonNull final IModifiableSequences initialSequences, final Injector injector, final ModelEntityMap modelEntityMap,
					final IScenarioDataProvider scenarioDataProvider, final IOptimisationData data) {
				final LNGScenarioModel lngScenarioModel = ScenarioModelUtil.getScenarioModel(scenarioDataProvider);
				final List<@NonNull Pair<@NonNull LoadSlot, @NonNull DischargeSlot>> thirdPartyCargoes = lngScenarioModel.getCargoModel().getCargoes().stream() //
						.filter(c -> {
							final List<Slot<?>> slots = c.getSlots();
							if (slots.size() == 2) {
								final BaseLegalEntity firstSlotEntity = slots.get(0).getSlotOrDelegateEntity();
								return firstSlotEntity != null && firstSlotEntity.isThirdParty();
							}
							return false;
						}) //
						.map(c -> {
							final List<Slot<?>> sortedSlots = c.getSortedSlots();
							final LoadSlot loadSlot = (LoadSlot) sortedSlots.get(0);
							final DischargeSlot dischargeSlot = (DischargeSlot) sortedSlots.get(1);
							return Pair.of(loadSlot, dischargeSlot);
						}) //
						.toList();

				if (!thirdPartyCargoes.isEmpty()) {
					final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
					for (final @NonNull Pair<@NonNull LoadSlot, @NonNull DischargeSlot> thirdPartyCargo : thirdPartyCargoes) {
						final LoadSlot loadSlot = thirdPartyCargo.getFirst();
						final DischargeSlot dischargeSlot = thirdPartyCargo.getSecond();
						final Slot<?> resourceDefiningSlot;
						if (loadSlot.isDESPurchase()) {
							resourceDefiningSlot = loadSlot;
						} else if (dischargeSlot.isFOBSale()) {
							resourceDefiningSlot = dischargeSlot;
						} else {
							throw new IllegalStateException("Third-party cargoes should be non-shipped");
						}
						final IResource oResource = SequenceHelper.getResource(modelEntityMap, injector, resourceDefiningSlot);
						final IPortSlot oLoadSlot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
						final IPortSlot oDischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
						final ISequenceElement loadSlotSequenceElement = portSlotProvider.getElement(oLoadSlot);
						final ISequenceElement dischargeSlotSequenceElement = portSlotProvider.getElement(oDischargeSlot);
						final IModifiableSequence resourceSequence = initialSequences.getModifiableSequence(oResource);
						assert resourceSequence.size() == 2;
						resourceSequence.insert(1, dischargeSlotSequenceElement);
						resourceSequence.insert(1, loadSlotSequenceElement);
					}
				}
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
