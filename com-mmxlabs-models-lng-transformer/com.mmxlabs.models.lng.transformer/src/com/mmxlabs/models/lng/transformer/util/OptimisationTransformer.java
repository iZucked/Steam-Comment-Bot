/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.utils.IAssignableElementDateProviderFactory;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.cargo.util.IExtraDataProvider;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

/**
 * Utility for taking an OptimisationSettings from the EMF and starting an optimiser accordingly. At the moment, it's pretty much just what was in TestUtils.
 * 
 * @author hinton
 * 
 */
public class OptimisationTransformer implements IOptimisationTransformer {
	private static final Logger LOG = LoggerFactory.getLogger(OptimisationTransformer.class);

	@Inject
	@NonNull
	private LNGScenarioModel scenarioModel;

	@Inject
	@NonNull
	private IScenarioDataProvider scenarioDataProvider;

	@Inject
	@NonNull
	private IInitialSequenceBuilder builder;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	@NonNull
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject(optional = true)
	@Nullable
	private IAssignableElementDateProviderFactory assignableElementComparator;

	@Inject(optional = true)
	@Nullable
	private IExtraDataProvider extraDataProvider;

	@Override
	@NonNull
	public ISequences createInitialSequences(@NonNull final IPhaseOptimisationData data, @NonNull final ModelEntityMap mem) {
		/**
		 * This sequences is passed into the initial sequence builder as a starting point. Extra elements may be added to the sequence in any position, but the existing elements will not be removed or
		 * reordered
		 */
		// Heads now contains the head of every chunk that has to go together.
		// We need to pull out all the chunks and sort out their rules
		final List<@NonNull IResource> resources = new ArrayList<>(data.getResources());
		Collections.sort(resources, (o1, o2) -> {
			final IVesselCharter vesselCharter1 = vesselProvider.getVesselCharter(o1);
			final IVesselCharter vesselCharter2 = vesselProvider.getVesselCharter(o2);
			final VesselInstanceType vit1 = vesselCharter1.getVesselInstanceType();
			final VesselInstanceType vit2 = vesselCharter2.getVesselInstanceType();

			int x = vit1.compareTo(vit2);
			if (x == 0) {
				x = ((Integer) vesselCharter1.getVessel().getMaxSpeed()).compareTo(vesselCharter2.getVessel().getMaxSpeed());
			}
			return x;
		});

		final IModifiableSequences advice = new ModifiableSequences(resources);

		/**
		 * This map will be used to try and place elements which aren't in the advice above onto particular resources, if possible.
		 */
		final Map<@NonNull ISequenceElement, @NonNull IResource> resourceAdvice = new HashMap<>();

		// Add in start elements
		for (final Entry<@NonNull IResource, @NonNull IModifiableSequence> sequence : advice.getModifiableSequences().entrySet()) {
			sequence.getValue().add(startEndRequirementProvider.getStartElement(sequence.getKey()));
		}

		final Collection<@NonNull Slot<?>> modelSlots = mem.getAllModelObjects(Slot.class);

		final Map<ISequenceElement, ISequenceElement> cargoSlotPairing = new HashMap<>();
		// Process data to find pre-linked DES Purchases and FOB Sales and construct
		// their sequences
		for (final Slot<?> slot : modelSlots) {
			if (slot instanceof LoadSlot loadSlot) {
				final Cargo cargo = loadSlot.getCargo();
				if (cargo != null) {
					final EList<Slot<?>> slots = cargo.getSortedSlots();
					// Only process this loop if slot is first in the list
					if (slots.get(0) == loadSlot) {
						ISequenceElement prevElement = null;
						for (final Slot<?> currentSlot : slots) {
							assert currentSlot != null;
							final IPortSlot slotObject = mem.getOptimiserObjectNullChecked(currentSlot, IPortSlot.class);
							final ISequenceElement slotElement = portSlotProvider.getElement(slotObject);
							if (prevElement != null) {
								cargoSlotPairing.put(prevElement, slotElement);
							}
							prevElement = slotElement;
						}
					}
				}
			}
		}

		// Process data to find pre-linked DES Purchases and FOB Sales and construct
		// their sequences
		for (final Slot<?> slot : modelSlots) {
			if (slot instanceof LoadSlot loadSlot) {
				// Note: We assume a DES Purchase is at most two slots to the cargo
				if (loadSlot.isDESPurchase()) {

					final Cargo cargo = loadSlot.getCargo();
					if (cargo != null) {

						final IPortSlot loadObject = mem.getOptimiserObjectNullChecked(loadSlot, IPortSlot.class);
						final ISequenceElement loadElement = portSlotProvider.getElement(loadObject);
						final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(loadElement);
						assert vesselCharter != null;

						final IResource resource = vesselProvider.getResource(vesselCharter);
						assert (resource != null);

						final EList<Slot<?>> slots = cargo.getSortedSlots();
						for (final Slot<?> currentSlot : slots) {
							assert currentSlot != null;
							final IPortSlot slotObject = mem.getOptimiserObjectNullChecked(currentSlot, IPortSlot.class);
							final ISequenceElement slotElement = portSlotProvider.getElement(slotObject);
							advice.getModifiableSequence(resource).add(slotElement);
						}
					}
				}
			} else if (slot instanceof DischargeSlot dischargeSlot) {
				// Note: We assume a FOB Sale is at most two slots to the cargo
				if (dischargeSlot.isFOBSale()) {

					final Cargo cargo = dischargeSlot.getCargo();

					if (cargo != null) {

						final IPortSlot dischargeObject = mem.getOptimiserObjectNullChecked(dischargeSlot, IPortSlot.class);
						final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeObject);
						final IVesselCharter vesselCharter = virtualVesselSlotProvider.getVesselCharterForElement(dischargeElement);
						assert vesselCharter != null;
						final IResource resource = vesselProvider.getResource(vesselCharter);
						assert resource != null;

						final List<Slot<?>> slots = cargo.getSortedSlots();
						for (final Slot<?> currentSlot : slots) {
							final IPortSlot slotObject = mem.getOptimiserObjectNullChecked(currentSlot, IPortSlot.class);
							assert slotObject != null;
							final ISequenceElement slotElement = portSlotProvider.getElement(slotObject);
							assert slotElement != null;
							advice.getModifiableSequence(resource).add(slotElement);
						}
					}
				}
			}
		}

		// Build up spot vessel maps
		final Map<ISpotCharterInMarket, Map<Integer, IVesselCharter>> spotAvailabilitiesByMarket = new HashMap<>();

		for (final IResource resource : data.getResources()) {
			assert resource != null;

			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			assert vesselCharter != null;

			if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP || vesselCharter.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER) {
				final ISpotCharterInMarket spotCharterInMarket = vesselCharter.getSpotCharterInMarket();
				assert spotCharterInMarket != null;

				Map<Integer, IVesselCharter> vesselsOfClass = spotAvailabilitiesByMarket.get(spotCharterInMarket);
				if (vesselsOfClass == null) {
					vesselsOfClass = new HashMap<>();
					spotAvailabilitiesByMarket.put(spotCharterInMarket, vesselsOfClass);
				}
				vesselsOfClass.put(vesselCharter.getSpotIndex(), vesselCharter);
			}

		}

		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

		// Process initial vessel assignments list
		ModelDistanceProvider modelDistanceProvider = ScenarioModelUtil.getModelDistanceProvider(scenarioDataProvider);
		final List<CollectedAssignment> assignments;
		if (assignableElementComparator != null) {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, ScenarioModelUtil.getPortModel(scenarioModel), spotMarketsModel, modelDistanceProvider,
					assignableElementComparator.create(scenarioModel), extraDataProvider);
		} else {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, ScenarioModelUtil.getPortModel(scenarioModel), spotMarketsModel, modelDistanceProvider, null, extraDataProvider);
		}
		for (final CollectedAssignment seq : assignments) {

			IVesselCharter vesselCharter = null;
			if (seq.getVesselCharter() != null) {
				final VesselCharter eVesselCharter = seq.getVesselCharter();
				assert eVesselCharter != null;

				vesselCharter = mem.getOptimiserObject(eVesselCharter, IVesselCharter.class);
			} else {
				final ISpotCharterInMarket charterInMarket = mem.getOptimiserObject(seq.getCharterInMarket(), ISpotCharterInMarket.class);

				final Map<Integer, IVesselCharter> availabilitiesForMarket = spotAvailabilitiesByMarket.get(charterInMarket);
				if (!(availabilitiesForMarket == null || availabilitiesForMarket.isEmpty())) {
					// Assign to same spot index if possible
					int idx = -1;
					if (availabilitiesForMarket.size() > seq.getSpotIndex()) {
						idx = seq.getSpotIndex();
					}
					vesselCharter = availabilitiesForMarket.get(idx);
				}
			}

			if (vesselCharter != null) {
				final IResource resource = vesselProvider.getResource(vesselCharter);
				assert resource != null;
				final IModifiableSequence sequence = advice.getModifiableSequence(resource);

				for (final AssignableElement assignedObject : seq.getAssignedObjects()) {
					if (assignedObject instanceof Cargo && ((Cargo) assignedObject).getCargoType() != CargoType.FLEET) {
						continue;
					}

					for (final ISequenceElement element : getElements(assignedObject, portSlotProvider, mem)) {
						assert element != null;
						sequence.add(element);
					}
				}
			} else {
				continue;
			}
		}

		// Add in end elements
		for (final Entry<IResource, IModifiableSequence> sequence : advice.getModifiableSequences().entrySet()) {
			sequence.getValue().add(startEndRequirementProvider.getEndElement(sequence.getKey()));
		}

		return builder.createInitialSequences(data, advice, resourceAdvice, cargoSlotPairing);
	}

	/**
	 */
	protected ISequenceElement[] getElements(@NonNull final AssignableElement modelObject, @NonNull final IPortSlotProvider psp, @NonNull final ModelEntityMap modelEntityMap) {
		if (modelObject instanceof VesselEvent event) {
			final IVesselEventPortSlot eventSlot = modelEntityMap.getOptimiserObject(event, IVesselEventPortSlot.class);
			if (eventSlot != null) {
				@NonNull
				final List<@NonNull ISequenceElement> eventSequenceElements = eventSlot.getEventSequenceElements();
				return eventSequenceElements.toArray(new ISequenceElement[eventSequenceElements.size()]);
			}
		} else if (modelObject instanceof Cargo cargo) {

			final List<ISequenceElement> elements = new ArrayList<>(cargo.getSortedSlots().size());
			for (final Slot<?> slot : cargo.getSortedSlots()) {
				assert slot != null;
				final IPortSlot portSlot = modelEntityMap.getOptimiserObjectNullChecked(slot, IPortSlot.class);
				assert portSlot != null;
				elements.add(psp.getElement(portSlot));
			}
			return elements.toArray(new ISequenceElement[elements.size()]);
		} else if (modelObject instanceof Slot<?> slot) {
			final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
			if (portSlot != null) {
				return new ISequenceElement[] { psp.getElement(portSlot) };
			}
		}
		return new ISequenceElement[0];
	}
}
