/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.verifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

public class OptimiserDataMapper {

	private final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	private final LNGDataTransformer dataTransformer;

	private final ModelEntityMap modelEntityMap;

	public OptimiserDataMapper(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		this.scenarioToOptimiserBridge = scenarioToOptimiserBridge;
		this.dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		this.modelEntityMap = dataTransformer.getModelEntityMap();
	}

	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	public LNGScenarioToOptimiserBridge getScenarioToOptimiserBridge() {
		return scenarioToOptimiserBridge;
	}

	public ModelEntityMap getModelEntityMap() {
		return modelEntityMap;
	}

	public IResource getResource(final Slot slot) {

		@NonNull
		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = dataTransformer.getInjector().getInstance(IVirtualVesselSlotProvider.class);
		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);

		if (slot instanceof LoadSlot && ((LoadSlot) slot).isDESPurchase()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
			return vesselProvider.getResource(vesselAvailability);
		} else if (slot instanceof DischargeSlot && ((DischargeSlot) slot).isFOBSale()) {
			final IPortSlot o_slot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
			final ISequenceElement element = portSlotProvider.getElement(o_slot);
			final IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(element);
			return vesselProvider.getResource(vesselAvailability);
		}
		throw new IllegalArgumentException();

	}

	public IVesselAvailability getVesselAvailability(final VesselAvailability vesselAvailability) {

		final IVesselAvailability o_vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(vesselAvailability, IVesselAvailability.class);

		return o_vesselAvailability;
	}

	public IVesselAvailability getVesselAvailability(final CharterInMarket charterInMarket, final int spotIndex) {
		final ISpotCharterInMarket market = modelEntityMap.getOptimiserObjectNullChecked(charterInMarket, ISpotCharterInMarket.class);
		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);

		for (final IResource o_resource : dataTransformer.getOptimisationData().getResources()) {
			final IVesselAvailability o_vesselAvailability = vesselProvider.getVesselAvailability(o_resource);
			if (o_vesselAvailability.getSpotCharterInMarket() != market) {
				continue;
			}
			if (o_vesselAvailability.getSpotIndex() == spotIndex) {
				return o_vesselAvailability;
			}
		}
		throw new IllegalArgumentException();
	}

	public Slot getSlotForElement(final ISequenceElement element) {

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);
		IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		final Slot slot;
		if (portSlot instanceof ILoadOption || portSlot instanceof IDischargeOption) {
			slot = modelEntityMap.getModelObject(portSlot, Slot.class);
		} else {
			slot = null;
		}
		return slot;
	}

	public ISequenceElement getElementFor(final Slot slot) {

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);

		final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
		return portSlotProvider.getElement(portSlot);
	}
	
	public ISequenceElement getElementFor(final VesselEvent event) {

		final IPortSlotProvider portSlotProvider = dataTransformer.getInjector().getInstance(IPortSlotProvider.class);

		final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(event, IPortSlot.class);
		return portSlotProvider.getElement(portSlot);
	}



	public List<ISequenceElement> getElementsFor(final SpotMarket spotMarket) {

		final ISpotMarket market = modelEntityMap.getOptimiserObjectNullChecked(spotMarket, ISpotMarket.class);
		final ISpotMarketSlotsProvider provider = dataTransformer.getInjector().getInstance(ISpotMarketSlotsProvider.class);
		return provider.getElementsFor(market);
	}

	public IStartRequirement getStartRequirement(final VesselAvailability vesselAvailability) {
		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		final IVesselAvailability o_vesselAvailablity = getVesselAvailability(vesselAvailability);
		final IResource o_resource = vesselProvider.getResource(o_vesselAvailablity);

		return startEndRequirementProvider.getStartRequirement(o_resource);
	}

	public IStartRequirement getStartRequirement(final CharterInMarket charterInMarket, final int spotIndex) {
		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		final IVesselAvailability o_vesselAvailablity = getVesselAvailability(charterInMarket, spotIndex);
		final IResource o_resource = vesselProvider.getResource(o_vesselAvailablity);

		return startEndRequirementProvider.getStartRequirement(o_resource);
	}

	public IEndRequirement getEndRequirement(final VesselAvailability vesselAvailability) {
		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		final IVesselAvailability o_vesselAvailablity = getVesselAvailability(vesselAvailability);
		final IResource o_resource = vesselProvider.getResource(o_vesselAvailablity);

		return startEndRequirementProvider.getEndRequirement(o_resource);
	}

	public IEndRequirement getEndRequirement(final CharterInMarket charterInMarket, final int spotIndex) {
		final IVesselProvider vesselProvider = dataTransformer.getInjector().getInstance(IVesselProvider.class);
		final IStartEndRequirementProvider startEndRequirementProvider = dataTransformer.getInjector().getInstance(IStartEndRequirementProvider.class);

		final IVesselAvailability o_vesselAvailablity = getVesselAvailability(charterInMarket, spotIndex);
		final IResource o_resource = vesselProvider.getResource(o_vesselAvailablity);

		return startEndRequirementProvider.getEndRequirement(o_resource);
	}

	public static @NonNull List<@NonNull ISequenceElement> convertToElements(final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final Collection<? extends EObject> modelObjects) {
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
				assert false : "Unknown object type";
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

	public static @NonNull Pair<IResource, IModifiableSequence> createFOBDESSequence(final @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final LoadSlot loadSlot,
			final DischargeSlot dischargeSlot) {
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
}
