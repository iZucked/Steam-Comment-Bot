package com.mmxlabs.lingo.its.tests.microcases;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class SequenceHelper {

	public static @NonNull IModifiableSequences createSequences(@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		@NonNull
		final IOptimisationData optimisationData = dataTransformer.getOptimisationData();
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources());

		return sequences;
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Cargo cargo,
			final VesselAvailability vesselAvailability1) {

		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		final IVesselAvailability o_vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability1, IVesselAvailability.class);
		final IResource resource = vesselProvider.getResource(o_vesselAvailability);

		return addSequence(sequences, scenarioToOptimiserBridge, cargo, resource, o_vesselAvailability);
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Cargo cargo,
			final CharterInMarket charterInMarket, final int spotIndex) {

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
			return addSequence(sequences, scenarioToOptimiserBridge, cargo, o_resource, o_vesselAvailability);
		}
		Assert.fail("Unable to find spot market vessel");
		throw new IllegalStateException();
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final @Nullable Cargo cargo,
			final @NonNull IResource o_resource, @NonNull final IVesselAvailability o_vesselAvailability) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		if (cargo != null) {
			for (final Slot slot : cargo.getSlots()) {
				final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
				final ISequenceElement element = portSlotProvider.getElement(o_slot);
				modifiableSequence.add(element);
			}
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return modifiableSequence;
	}

	public static void addToUnused(final @NonNull IModifiableSequences sequences, final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Cargo cargo) {
		@NonNull
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);

		for (final Slot slot : cargo.getSlots()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			sequences.getModifiableUnusedElements().add(element);
		}

	}
}
