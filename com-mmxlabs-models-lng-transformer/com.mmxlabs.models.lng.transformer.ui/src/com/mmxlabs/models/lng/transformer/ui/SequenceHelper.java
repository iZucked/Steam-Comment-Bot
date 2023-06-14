/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.SequencesAttributesProviderImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

public class SequenceHelper {

	private static final String MSG_UNABLE_TO_FIND_SPOT_MARKET_VESSEL = "Unable to find spot market vessel";

	public static @NonNull IModifiableSequences createSequences(@NonNull final Injector injector) {
		final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources(), new SequencesAttributesProviderImpl());

		return sequences;
	}

	public static @NonNull IModifiableSequences createSequences(@NonNull final IOptimisationData optimisationData) {
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources(), new SequencesAttributesProviderImpl());

		return sequences;
	}

	public static @NonNull IModifiableSequences createSequences(final @NonNull Injector injector, final VesselCharter vesselCharter, final EObject... cargoes) {
		final IModifiableSequences sequences = createSequences(injector);
		addSequence(sequences, injector, vesselCharter, cargoes);
		return sequences;
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final @NonNull Injector injector, final VesselCharter vesselCharter,
			final EObject... cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final IVesselCharter o_vesselCharter = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);
		final IResource resource = vesselProvider.getResource(o_vesselCharter);

		return addSequence(sequences, injector, resource, o_vesselCharter, cargoes);
	}

	public static @NonNull IModifiableSequence makeSequence(final @NonNull Injector injector, final VesselCharter vesselCharter, final EObject... cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final IVesselCharter o_vesselCharter = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);
		final IResource resource = vesselProvider.getResource(o_vesselCharter);

		return makeSequence(injector, resource, o_vesselCharter, cargoes);
	}

	public static @NonNull IModifiableSequence makeSequence(final @NonNull Injector injector, final CharterInMarketOverride charterInMarketOverride, final EObject... cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final IVesselCharter o_vesselCharter = modelEntityMap.getOptimiserObjectNullChecked(charterInMarketOverride, IVesselCharter.class);
		final IResource resource = vesselProvider.getResource(o_vesselCharter);

		return makeSequence(injector, resource, o_vesselCharter, cargoes);
	}

	public static @NonNull Pair<IResource, IModifiableSequence> makeSequence(final @NonNull Injector injector, final VesselCharter vesselCharter,
			final Collection<? extends EObject> cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final IVesselCharter o_vesselCharter = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);
		final IResource resource = vesselProvider.getResource(o_vesselCharter);

		return makeSequence(injector, resource, o_vesselCharter, cargoes);
	}

	public static @NonNull Pair<IResource, IModifiableSequence> makeSequence(final @NonNull Injector injector, final CharterInMarketOverride charterInMarketOverride,
			final Collection<? extends EObject> cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final IVesselCharter o_vesselCharter = modelEntityMap.getOptimiserObjectNullChecked(charterInMarketOverride, IVesselCharter.class);
		final IResource resource = vesselProvider.getResource(o_vesselCharter);

		return makeSequence(injector, resource, o_vesselCharter, cargoes);
	}

	public static @NonNull IModifiableSequences createSequences(final @NonNull Injector injector, final CharterInMarket charterInMarket, final int spotIndex, final EObject... cargoes) {
		final IModifiableSequences sequences = createSequences(injector);
		addSequence(sequences, injector, charterInMarket, spotIndex, cargoes);
		return sequences;
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final @NonNull Injector injector, final CharterInMarket charterInMarket, final int spotIndex,
			final EObject... cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);

		for (final IResource o_resource : optimisationData.getResources()) {
			final IVesselCharter o_vesselCharter = vesselProvider.getVesselCharter(o_resource);
			if (o_vesselCharter.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselCharter.getSpotIndex() != spotIndex) {
				continue;
			}
			return addSequence(sequences, injector, o_resource, o_vesselCharter, cargoes);
		}
		assert false : MSG_UNABLE_TO_FIND_SPOT_MARKET_VESSEL;
		throw new IllegalStateException();
	}

	public static @NonNull IResource getResource(final @NonNull LNGDataTransformer dataTransformer, final CharterInMarket charterInMarket, final int spotIndex) {

		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		for (final IResource o_resource : dataTransformer.getOptimisationData().getResources()) {
			final IVesselCharter o_vesselCharter = vesselProvider.getVesselCharter(o_resource);
			if (o_vesselCharter.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselCharter.getSpotIndex() != spotIndex) {
				continue;
			}
			return o_resource;
		}
		assert false : MSG_UNABLE_TO_FIND_SPOT_MARKET_VESSEL;
		throw new IllegalStateException();
	}

	public static @NonNull IVesselCharter findVesselCharter(final @NonNull Injector injector, final CharterInMarket charterInMarket, final int spotIndex) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);

		for (final IResource o_resource : optimisationData.getResources()) {
			final IVesselCharter o_vesselCharter = vesselProvider.getVesselCharter(o_resource);
			if (o_vesselCharter.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselCharter.getSpotIndex() != spotIndex) {
				continue;
			}
			return o_vesselCharter;
		}
		assert false : MSG_UNABLE_TO_FIND_SPOT_MARKET_VESSEL;
		throw new IllegalStateException();
	}

	public static @NonNull IModifiableSequence makeSequence(final @NonNull Injector injector, final CharterInMarket charterInMarket, final int spotIndex, final EObject... cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);

		for (final IResource o_resource : optimisationData.getResources()) {
			final IVesselCharter o_vesselCharter = vesselProvider.getVesselCharter(o_resource);
			if (o_vesselCharter.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselCharter.getSpotIndex() != spotIndex) {
				continue;
			}
			return makeSequence(injector, o_resource, o_vesselCharter, cargoes);
		}
		assert false : MSG_UNABLE_TO_FIND_SPOT_MARKET_VESSEL;
		throw new IllegalStateException();
	}

	public static @NonNull Pair<IResource, IModifiableSequence> makeSequence(final @NonNull Injector injector, final CharterInMarket charterInMarket, final int spotIndex,
			final Collection<? extends EObject> cargoes) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);

		for (final IResource o_resource : optimisationData.getResources()) {
			final IVesselCharter o_vesselCharter = vesselProvider.getVesselCharter(o_resource);
			if (o_vesselCharter.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselCharter.getSpotIndex() != spotIndex) {
				continue;
			}
			return makeSequence(injector, o_resource, o_vesselCharter, cargoes);
		}
		assert false : MSG_UNABLE_TO_FIND_SPOT_MARKET_VESSEL;
		throw new IllegalStateException();
	}

	public static @NonNull IModifiableSequences createSequences(final @NonNull Injector injector, final @NonNull IResource o_resource, @NonNull final IVesselCharter o_vesselCharter,
			final EObject... cargoes) {
		final IModifiableSequences sequences = createSequences(injector);
		addSequence(sequences, injector, o_resource, o_vesselCharter, cargoes);
		return sequences;
	}

	public static @NonNull IModifiableSequence addSequence(final @NonNull IModifiableSequences sequences, final @NonNull Injector injector, final @NonNull IResource o_resource,
			@NonNull final IVesselCharter o_vesselCharter, final EObject... cargoes) {

		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		for (final ISequenceElement e : convertToElements(injector, cargoes)) {
			modifiableSequence.add(e);
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return modifiableSequence;
	}

	public static @NonNull IModifiableSequence makeSequence(final @NonNull Injector injector, final @NonNull IResource o_resource, @NonNull final IVesselCharter o_vesselCharter,
			final EObject... cargoes) {

		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = new ListModifiableSequence(new LinkedList<>());
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		for (final ISequenceElement e : convertToElements(injector, cargoes)) {
			modifiableSequence.add(e);
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return modifiableSequence;
	}

	public static @NonNull Pair<IResource, IModifiableSequence> makeSequence(final @NonNull Injector injector, final @NonNull IResource o_resource,
			@NonNull final IVesselCharter o_vesselCharter, final Collection<? extends EObject> cargoes) {

		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = new ListModifiableSequence(new LinkedList<>());
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));

		for (final ISequenceElement e : convertToElements(injector, cargoes)) {
			modifiableSequence.add(e);
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		return new Pair<>(o_resource, modifiableSequence);
	}

	public static void addToUnused(final @NonNull IModifiableSequences sequences, final @NonNull Injector injector, final EObject... cargoes) {

		for (final ISequenceElement e : convertToElements(injector, cargoes)) {
			sequences.getModifiableUnusedElements().add(e);
		}

	}

	public static @NonNull List<@NonNull ISequenceElement> convertToElements(final @NonNull Injector injector, final EObject... modelObjects) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final @NonNull List<@NonNull ISequenceElement> elements = new LinkedList<>();

		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		for (final EObject modelObject : modelObjects) {
			if (modelObject instanceof final Cargo cargo) {
				for (final Slot<?> slot : cargo.getSlots()) {
					final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
					final ISequenceElement element = portSlotProvider.getElement(o_slot);
					elements.add(element);
				}
			} else if (modelObject instanceof final Slot<?> slot) {
				final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
				final ISequenceElement element = portSlotProvider.getElement(o_slot);
				elements.add(element);
			} else if (modelObject instanceof final VesselEvent vesselEvent) {
				final IVesselEventPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(vesselEvent, IVesselEventPortSlot.class);
				for (final ISequenceElement e : o_slot.getEventSequenceElements()) {
					elements.add(e);
				}
			} else {
				assert false : "Unknown object type";
			}
		}
		return elements;
	}

	public static @NonNull List<@NonNull ISequenceElement> convertToElements(final @NonNull Injector injector, final Collection<? extends EObject> modelObjects) {

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final @NonNull List<@NonNull ISequenceElement> elements = new LinkedList<>();

		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		for (final EObject modelObject : modelObjects) {
			if (modelObject instanceof final Cargo cargo) {
				for (final Slot<?> slot : cargo.getSlots()) {
					final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
					final ISequenceElement element = portSlotProvider.getElement(o_slot);
					elements.add(element);
				}
			} else if (modelObject instanceof final Slot<?> slot) {
				final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
				final ISequenceElement element = portSlotProvider.getElement(o_slot);
				elements.add(element);
			} else if (modelObject instanceof final VesselEvent vesselEvent) {
				final IVesselEventPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(vesselEvent, IVesselEventPortSlot.class);
				for (final ISequenceElement e : o_slot.getEventSequenceElements()) {
					elements.add(e);
				}
			} else {
				assert false : "Unknown object type";
			}
		}
		return elements;
	}

	public static @NonNull ISequences createFOBDESSequences(final @NonNull Injector injector, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		@NonNull
		final IOptimisationData optimisationData = injector.getInstance(IOptimisationData.class);
		final ModifiableSequences sequences = new ModifiableSequences(optimisationData.getResources(), new SequencesAttributesProviderImpl());

		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = injector.getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		IResource o_resource = null;
		if (loadSlot.isDESPurchase()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		} else if (dischargeSlot.isFOBSale()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		}

		assert o_resource != null;

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

	public static @NonNull ISequences createJustFOBDESSequences(final @NonNull Injector injector, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = injector.getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		IResource o_resource = null;
		if (loadSlot.isDESPurchase()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		} else if (dischargeSlot.isFOBSale()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		}

		assert o_resource != null;
		final ModifiableSequences sequences = new ModifiableSequences(Collections.singletonList(o_resource), new SequencesAttributesProviderImpl());

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

	public static @NonNull IModifiableSequence addFOBDESSequence(IModifiableSequences sequences, final @NonNull LNGDataTransformer dataTransformer, final LoadSlot loadSlot,
			final DischargeSlot dischargeSlot) {

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
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		} else if (dischargeSlot.isFOBSale()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		}

		assert o_resource != null;

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

		return modifiableSequence;
	}

	public static @NonNull Pair<IResource, IModifiableSequence> createFOBDESSequence(final @NonNull Injector injector, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		@NonNull
		final ModelEntityMap modelEntityMap = injector.getInstance(ModelEntityMap.class);

		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = injector.getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		IResource o_resource = null;
		if (loadSlot.isDESPurchase()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		} else if (dischargeSlot.isFOBSale()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
			o_resource = vesselProvider.getResource(vesselCharter);
		}

		assert o_resource != null;

		@NonNull
		final IModifiableSequence modifiableSequence = new ListModifiableSequence(new ArrayList<>(4));
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

		return new Pair<>(o_resource, modifiableSequence);
	}

	public static @NonNull IResource getResource(final @NonNull LNGDataTransformer dataTransformer, final Slot<?> fobDesSlot) {
		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
		final Injector injector = dataTransformer.getInjector();
		return getResource(modelEntityMap, injector, fobDesSlot);
	}

	public static @NonNull IResource getResource(@NonNull final ModelEntityMap modelEntityMap, final Injector injector, final Slot<?> fobDesSlot) {
		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = injector.getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider portSlotProvider = injector.getInstance(IPortSlotProvider.class);

		final IPortSlot oSlot = modelEntityMap.getOptimiserObjectNullChecked(fobDesSlot, IPortSlot.class);
		final ISequenceElement element = portSlotProvider.getElement(oSlot);
		final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(element);
		final IResource oResource =  vesselProvider.getResource(vesselCharter);

		assert oResource != null;
		return oResource;
	}

	public static IResource getResource(final @NonNull LNGDataTransformer dataTransformer, VesselCharter vesselCharter) {
		@NonNull
		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		final IVesselCharter o_vesselCharter = modelEntityMap.getOptimiserObjectNullChecked(vesselCharter, IVesselCharter.class);
		final IResource resource = vesselProvider.getResource(o_vesselCharter);

		return resource;
	}

	public static @NonNull IModifiableSequences createEmptySequences(final @NonNull Injector injector, @NonNull final List<@NonNull IResource> resources) {
		final IVesselProvider vesselProvider = injector.getInstance(IVesselProvider.class);

		final @NonNull List<@NonNull IResource> optimisableResources = resources.stream() //
				.filter(resource -> vesselProvider.getVesselCharter(resource).getVesselInstanceType() != VesselInstanceType.NONSHIPPED_ROTATION) //
				.toList();
		final ModifiableSequences sequences = new ModifiableSequences(optimisableResources, new SequencesAttributesProviderImpl());
		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		for (@NonNull
		final IResource o_resource : optimisableResources) {
			@NonNull
			final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
			modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));
			modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));
		}
		return sequences;
	}
}
