/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

public class SequenceHelper {

	public static @NonNull IModifiableSequences createSequences(@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		@NonNull
		final IOptimisationData optimisationData = dataTransformer.getOptimisationData();
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources());

		return sequences;
	}

	public static @NonNull IModifiableSequences createSequences(final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final VesselAvailability vesselAvailability,
			final EObject... cargoes) {
		final IModifiableSequences sequences = createSequences(scenarioToOptimiserBridge);
		addSequence(sequences, scenarioToOptimiserBridge, vesselAvailability, cargoes);
		return sequences;
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge,
			final VesselAvailability vesselAvailability, final EObject... cargoes) {

		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		final IVesselAvailability o_vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);
		final IResource resource = vesselProvider.getResource(o_vesselAvailability);

		return addSequence(sequences, scenarioToOptimiserBridge, resource, o_vesselAvailability, cargoes);
	}

	public static @NonNull IModifiableSequences createSequences(final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final CharterInMarket charterInMarket, final int spotIndex,
			final EObject... cargoes) {
		final IModifiableSequences sequences = createSequences(scenarioToOptimiserBridge);
		addSequence(sequences, scenarioToOptimiserBridge, charterInMarket, spotIndex, cargoes);
		return sequences;
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge,
			final CharterInMarket charterInMarket, final int spotIndex, final EObject... cargoes) {

		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		for (final IResource o_resource : dataTransformer.getOptimisationData().getResources()) {
			final IVesselAvailability o_vesselAvailability = vesselProvider.getVesselAvailability(o_resource);
			if (o_vesselAvailability.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselAvailability.getSpotIndex() != spotIndex) {
				continue;
			}
			return addSequence(sequences, scenarioToOptimiserBridge, o_resource, o_vesselAvailability, cargoes);
		}
		Assert.fail("Unable to find spot market vessel");
		throw new IllegalStateException();
	}

	public static @NonNull IModifiableSequences createSequences(final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final @NonNull IResource o_resource,
			@NonNull final IVesselAvailability o_vesselAvailability, final EObject... cargoes) {
		final IModifiableSequences sequences = createSequences(scenarioToOptimiserBridge);
		addSequence(sequences, scenarioToOptimiserBridge, o_resource, o_vesselAvailability, cargoes);
		return sequences;
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge,
			final @NonNull IResource o_resource, @NonNull final IVesselAvailability o_vesselAvailability, final EObject... cargoes) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		for (final ISequenceElement e : convertToElements(scenarioToOptimiserBridge, cargoes)) {
			modifiableSequence.add(e);
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return modifiableSequence;
	}

	public static void addToUnused(final @NonNull IModifiableSequences sequences, final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final EObject... cargoes) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		for (final ISequenceElement e : convertToElements(scenarioToOptimiserBridge, cargoes)) {
			sequences.getModifiableUnusedElements().add(e);
		}

	}

	public static @NonNull List<@NonNull ISequenceElement> convertToElements(final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final EObject... modelObjects) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final @NonNull List<@NonNull ISequenceElement> elements = new LinkedList<>();

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);
		for (final EObject modelObject : modelObjects) {
			if (modelObject instanceof Cargo) {
				final Cargo cargo = (Cargo) modelObject;
				for (final Slot slot : cargo.getSlots()) {
					final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
					final ISequenceElement element = portSlotProvider.getElement(o_slot);
					elements.add(element);
				}
			} else if (modelObject instanceof Slot) {
				final Slot slot = (Slot) modelObject;
				final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
				final ISequenceElement element = portSlotProvider.getElement(o_slot);
				elements.add(element);
			} else if (modelObject instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) modelObject;
				final IVesselEventPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(vesselEvent, IVesselEventPortSlot.class);
				for (final ISequenceElement e : o_slot.getEventSequenceElements()) {
					elements.add(e);
				}
			} else {
				Assert.fail("Unknown object type");
			}
		}
		return elements;
	}

	public static @NonNull ISequences createFOBDESSequences(final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		@NonNull
		final IOptimisationData optimisationData = dataTransformer.getOptimisationData();
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources());

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = dataTransformer.getInjector().getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		IResource o_resource = null;
		if (loadSlot.isDESPurchase()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
			o_resource = vesselProvider.getResource(vesselAvailability);
		} else if (dischargeSlot.isFOBSale()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
			o_resource = vesselProvider.getResource(vesselAvailability);
		}

		Assert.assertNotNull(o_resource);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		{
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			modifiableSequence.add(element);
		}
		{
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			modifiableSequence.add(element);
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return sequences;
	}
}
