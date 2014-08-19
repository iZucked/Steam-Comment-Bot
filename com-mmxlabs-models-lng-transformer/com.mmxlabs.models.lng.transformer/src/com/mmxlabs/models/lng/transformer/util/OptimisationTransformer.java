/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
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
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.cargo.editor.utils.IAssignableElementComparatorFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
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
	private static final Logger log = LoggerFactory.getLogger(OptimisationTransformer.class);

	@Inject
	private LNGScenarioModel rootObject;

	@Inject
	private IInitialSequenceBuilder builder;

	@Inject
	private IVesselProvider vp;

	@Inject
	private IPortSlotProvider psp;

	@Inject
	private IStartEndRequirementProvider serp;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	@Inject(optional = true)
	private IAssignableElementComparatorFactory assignableElementComparator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.transformer.IOptimisationTransformer#createInitialSequences(com.mmxlabs.optimiser.core.scenario.IOptimisationData,
	 * com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap)
	 */
	/**
	 */
	@Override
	public ISequences createInitialSequences(final IOptimisationData data, final ModelEntityMap mem) {
		/**
		 * This sequences is passed into the initial sequence builder as a starting point. Extra elements may be added to the sequence in any position, but the existing elements will not be removed or
		 * reordered
		 */
		log.debug("Creating advice for sequence builder");
		final IModifiableSequences advice = new ModifiableSequences(data.getResources());

		/**
		 * This map will be used to try and place elements which aren't in the advice above onto particular resources, if possible.
		 */
		final Map<ISequenceElement, IResource> resourceAdvice = new HashMap<ISequenceElement, IResource>();

		// Add in start elements
		for (final Entry<IResource, IModifiableSequence> sequence : advice.getModifiableSequences().entrySet()) {
			sequence.getValue().add(serp.getStartElement(sequence.getKey()));
		}

		final Collection<Slot> modelSlots = mem.getAllModelObjects(Slot.class);

		final Map<ISequenceElement, ISequenceElement> cargoSlotPairing = new HashMap<ISequenceElement, ISequenceElement>();
		// Process data to find pre-linked DES Purchases and FOB Sales and construct their sequences
		for (final Slot slot : modelSlots) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				final Cargo cargo = loadSlot.getCargo();
				if (cargo != null) {
					final EList<Slot> slots = cargo.getSortedSlots();
					// Only process this loop if slot is first in the list
					if (slots.get(0) == loadSlot) {
						ISequenceElement prevElement = null;
						for (final Slot currentSlot : slots) {
							final IPortSlot slotObject = mem.getOptimiserObject(currentSlot, IPortSlot.class);
							final ISequenceElement slotElement = psp.getElement(slotObject);
							if (prevElement != null) {
								cargoSlotPairing.put(prevElement, slotElement);
							}
							prevElement = slotElement;
						}
					}
				}
			}
		}

		// Process data to find pre-linked DES Purchases and FOB Sales and construct their sequences
		for (final Slot slot : modelSlots) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				// Note: We assume a DES Purchase is at most two slots to the cargo
				if (loadSlot.isDESPurchase()) {

					final Cargo cargo = loadSlot.getCargo();
					if (cargo != null) {

						final IPortSlot loadObject = mem.getOptimiserObject(loadSlot, IPortSlot.class);
						final ISequenceElement loadElement = psp.getElement(loadObject);
						final IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(loadElement);
						final IResource res = vp.getResource(vesselAvailability);
						assert (res != null);

						final IResource resource = vp.getResource(vesselAvailability);

						final EList<Slot> slots = cargo.getSortedSlots();
						for (final Slot currentSlot : slots) {
							final IPortSlot slotObject = mem.getOptimiserObject(currentSlot, IPortSlot.class);
							final ISequenceElement slotElement = psp.getElement(slotObject);
							advice.getModifiableSequence(resource).add(slotElement);
						}
					}
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				// Note: We assume a FOB Sale is at most two slots to the cargo
				if (dischargeSlot.isFOBSale()) {

					final Cargo cargo = dischargeSlot.getCargo();

					if (cargo != null) {

						final IPortSlot dischargeObject = mem.getOptimiserObject(dischargeSlot, IPortSlot.class);
						final ISequenceElement dischargeElement = psp.getElement(dischargeObject);
						final IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(dischargeElement);
						final IResource res = vp.getResource(vesselAvailability);
						assert (res != null);

						final IResource resource = vp.getResource(vesselAvailability);

						final EList<Slot> slots = cargo.getSortedSlots();
						for (final Slot currentSlot : slots) {
							final IPortSlot slotObject = mem.getOptimiserObject(currentSlot, IPortSlot.class);
							final ISequenceElement slotElement = psp.getElement(slotObject);
							advice.getModifiableSequence(resource).add(slotElement);
						}
					}
				}
			}
		}

		IVesselAvailability shortCargoVesselAvailability = null;

		// Build up spot vessel maps
		final Map<IVesselClass, List<IVesselAvailability>> spotVesselsByClass = new HashMap<>();

		for (final IResource resource : data.getResources()) {
			final IVesselAvailability vesselAvailability = vp.getVesselAvailability(resource);

			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS) {
				shortCargoVesselAvailability = vesselAvailability;
				continue;
			}

			if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.SPOT_CHARTER) {
				continue;
			}
			final IVesselClass vesselClass = vesselAvailability.getVessel().getVesselClass();

			List<IVesselAvailability> vesselsOfClass = spotVesselsByClass.get(vesselClass);
			if (vesselsOfClass == null) {
				vesselsOfClass = new LinkedList<IVesselAvailability>();
				spotVesselsByClass.put(vesselClass, vesselsOfClass);
			}

			vesselsOfClass.add(vesselAvailability);
		}

		// final Map<Vessel, VesselAvailability> vesselAvailabilityMap = new HashMap<Vessel, VesselAvailability>();
		final FleetModel fleetModel = rootObject.getFleetModel();
		final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();
		// for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
		// vesselAvailabilityMap.put(va.getVessel(), va);
		// }

		// Process initial vessel assignments list
		final List<CollectedAssignment> assignments;
		if (assignableElementComparator != null) {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, fleetModel, assignableElementComparator.create(rootObject));
		} else {
			assignments = AssignmentEditorHelper.collectAssignments(cargoModel, fleetModel);
		}
		for (final CollectedAssignment seq : assignments) {
			IVesselAvailability vesselAvailability = null;
			if (seq.getVesselAvailability() != null) {
				final VesselAvailability eVesselAvailability = (VesselAvailability) seq.getVesselAvailability();
				vesselAvailability = mem.getOptimiserObject(eVesselAvailability, IVesselAvailability.class);
			} else {
				final IVesselClass vesselClass = mem.getOptimiserObject(seq.getVesselClass(), IVesselClass.class);

				final List<IVesselAvailability> vesselsOfClass = spotVesselsByClass.get(vesselClass);
				if (!(vesselsOfClass == null || vesselsOfClass.isEmpty())) {
					// Assign to same spot index if possible
					int idx = 0;
					if (vesselsOfClass.size() > seq.getSpotIndex()) {
						idx = seq.getSpotIndex();
					}
					vesselAvailability = vesselsOfClass.get(idx);
				}
			}
			if (vesselAvailability != null) {
				final IResource resource = vp.getResource(vesselAvailability);
				final IModifiableSequence sequence = advice.getModifiableSequence(resource);

				for (final AssignableElement assignedObject : seq.getAssignedObjects()) {
					if (assignedObject instanceof Cargo && ((Cargo) assignedObject).getCargoType() != CargoType.FLEET) {
						continue;
					}
					for (final ISequenceElement element : getElements(assignedObject, psp, mem)) {
						sequence.add(element);
					}
				}
//			} else {
//				log.debug("Vessel is missing: " + seq.getVesselOrClass().getName());
			}
		}

		if (shortCargoVesselAvailability != null) {
			final IResource resource = vp.getResource(shortCargoVesselAvailability);
			final IModifiableSequence sequence = advice.getModifiableSequence(resource);
			for (final Cargo cargo : cargoModel.getCargoes()) {
				if (cargo.getAssignment() == null) {
					if (cargo.getCargoType() != CargoType.FLEET) {
						continue;
					}
					for (final ISequenceElement element : getElements(cargo, psp, mem)) {
						sequence.add(element);
					}
				}
			}
		}
		// Add in end elements
		for (final Entry<IResource, IModifiableSequence> sequence : advice.getModifiableSequences().entrySet()) {
			sequence.getValue().add(serp.getEndElement(sequence.getKey()));
		}

		return builder.createInitialSequences(data, advice, resourceAdvice, cargoSlotPairing);
	}

	/**
	 */
	protected ISequenceElement[] getElements(final AssignableElement modelObject, final IPortSlotProvider psp, final ModelEntityMap modelEntityMap) {
		if (modelObject instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) modelObject;
			final IVesselEventPortSlot eventSlot = modelEntityMap.getOptimiserObject(event, IVesselEventPortSlot.class);
			if (eventSlot != null) {
				return new ISequenceElement[] { psp.getElement(eventSlot) };
			}
		} else if (modelObject instanceof Cargo) {
			final Cargo cargo = (Cargo) modelObject;

			final List<ISequenceElement> elements = new ArrayList<ISequenceElement>(cargo.getSortedSlots().size());
			for (final Slot slot : cargo.getSortedSlots()) {
				final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
				assert portSlot != null;
				elements.add(psp.getElement(portSlot));
			}
			return elements.toArray(new ISequenceElement[elements.size()]);
		} else if (modelObject instanceof Slot) {
			final Slot slot = (Slot) modelObject;
			final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
			if (portSlot != null) {
				return new ISequenceElement[] { psp.getElement(portSlot) };
			}
		}
		return new ISequenceElement[0];
	}
}
