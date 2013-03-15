/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.input.editor.utils.CollectedAssignment;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
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
	private MMXRootObject rootObject;

	@Inject
	private IInitialSequenceBuilder builder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.transformer.IOptimisationTransformer#createInitialSequences(com.mmxlabs.optimiser.core.scenario.IOptimisationData,
	 * com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap)
	 */
	/**
	 * @since 3.0
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
		final InputModel inputModel = rootObject.getSubModel(InputModel.class);

		final IVesselProvider vp = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		final IPortSlotProvider psp = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		final IStartEndRequirementProvider serp = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
		final IVirtualVesselSlotProvider virtualVesselSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_virtualVesselSlotProvider, IVirtualVesselSlotProvider.class);

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
					final DischargeSlot dischargeSlot = cargo.getDischargeSlot();
					if (dischargeSlot != null) {

						final IPortSlot loadObject = mem.getOptimiserObject(loadSlot, IPortSlot.class);
						final ISequenceElement loadElement = psp.getElement(loadObject);

						final IPortSlot dischargeObject = mem.getOptimiserObject(dischargeSlot, IPortSlot.class);
						final ISequenceElement dischargeElement = psp.getElement(dischargeObject);

						cargoSlotPairing.put(loadElement, dischargeElement);
					}
				}
			}
		}

		// Process data to find pre-linked DES Purchases and FOB Sales and construct their sequences
		for (final Slot slot : modelSlots) {
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {

					final Cargo cargo = loadSlot.getCargo();
					if (cargo != null) {

						final DischargeSlot dischargeSlot = cargo.getDischargeSlot();
						if (dischargeSlot != null) {

							final IPortSlot loadObject = mem.getOptimiserObject(loadSlot, IPortSlot.class);
							final ISequenceElement loadElement = psp.getElement(loadObject);
							final IVessel vessel = virtualVesselSlotProvider.getVesselForElement(loadElement);
							final IResource res = vp.getResource(vessel);
							assert (res != null);

							final IResource resource = vp.getResource(vessel);

							final IPortSlot dischargeObject = mem.getOptimiserObject(dischargeSlot, IPortSlot.class);
							final ISequenceElement dischargeElement = psp.getElement(dischargeObject);
							advice.getModifiableSequence(resource).add(loadElement);
							advice.getModifiableSequence(resource).add(dischargeElement);
						}
					}
				}
			} else if (slot instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) slot;
				if (dischargeSlot.isFOBSale()) {

					final Cargo cargo = dischargeSlot.getCargo();
					if (cargo != null) {
						final LoadSlot loadSlot = cargo.getLoadSlot();
						if (loadSlot != null) {

							final IPortSlot dischargeObject = mem.getOptimiserObject(dischargeSlot, IPortSlot.class);
							final ISequenceElement dischargeElement = psp.getElement(dischargeObject);
							final IVessel vessel = virtualVesselSlotProvider.getVesselForElement(dischargeElement);
							final IResource res = vp.getResource(vessel);
							assert (res != null);

							final IResource resource = vp.getResource(vessel);

							final IPortSlot loadObject = mem.getOptimiserObject(loadSlot, IPortSlot.class);
							final ISequenceElement loadElement = psp.getElement(loadObject);
							advice.getModifiableSequence(resource).add(loadElement);
							advice.getModifiableSequence(resource).add(dischargeElement);
						}
					}
				}
			}
		}

		IVessel shortCargoVessel = null;

		// Build up spot vessel maps
		final Map<IVesselClass, List<IVessel>> spotVesselsByClass = new HashMap<IVesselClass, List<IVessel>>();
		for (final IResource resource : data.getResources()) {
			final IVessel vessel = vp.getVessel(resource);

			if (vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS) {
				shortCargoVessel = vessel;
				continue;
			}

			if (!vessel.getVesselInstanceType().equals(VesselInstanceType.SPOT_CHARTER))
				continue;

			final IVesselClass vesselClass = vessel.getVesselClass();

			List<IVessel> vesselsOfClass = spotVesselsByClass.get(vesselClass);
			if (vesselsOfClass == null) {
				vesselsOfClass = new LinkedList<IVessel>();
				spotVesselsByClass.put(vesselClass, vesselsOfClass);
			}

			vesselsOfClass.add(vessel);
		}

		// Process initial vessel assignments list
		if (!inputModel.getElementAssignments().isEmpty()) {
			final List<CollectedAssignment> assignments = AssignmentEditorHelper.collectAssignments(inputModel, rootObject.getSubModel(FleetModel.class));
			for (final CollectedAssignment seq : assignments) {
				IVessel vessel = null;
				log.debug("Processing assignment " + seq.getVesselOrClass().getName());
				if (seq.isSpotVessel()) {
					final IVesselClass vesselClass = mem.getOptimiserObject(seq.getVesselOrClass(), IVesselClass.class);

					final List<IVessel> vesselsOfClass = spotVesselsByClass.get(vesselClass);
					if (!(vesselsOfClass == null || vesselsOfClass.isEmpty())) {
						// Assign to same spot index if possible
						int idx = 0;
						if (vesselsOfClass.size() > seq.getSpotIndex()) {
							idx = seq.getSpotIndex();
						}
						vessel = vesselsOfClass.get(idx);
					}
				} else {
					vessel = mem.getOptimiserObject(seq.getVesselOrClass(), IVessel.class);
				}
				if (vessel != null) {
					final IResource resource = vp.getResource(vessel);
					final IModifiableSequence sequence = advice.getModifiableSequence(resource);

					for (final UUIDObject assignedObject : seq.getAssignedObjects()) {
						if (assignedObject instanceof Cargo && ((Cargo) assignedObject).getCargoType() != CargoType.FLEET) {
							continue;
						}
						for (final ISequenceElement element : getElements(assignedObject, psp, mem)) {
							sequence.add(element);
						}
					}
				} else {
					log.debug("Vessel is missing: " + seq.getVesselOrClass().getName());
				}
			}

			if (shortCargoVessel != null) {
				final IResource resource = vp.getResource(shortCargoVessel);
				final IModifiableSequence sequence = advice.getModifiableSequence(resource);
				for (final ElementAssignment ea : inputModel.getElementAssignments()) {
					if (ea.getAssignment() == null) {

						final UUIDObject assignedObject = ea.getAssignedObject();
						if (assignedObject instanceof Cargo && ((Cargo) assignedObject).getCargoType() != CargoType.FLEET) {
							continue;
						}
						for (final ISequenceElement element : getElements(assignedObject, psp, mem)) {
							sequence.add(element);
						}
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
	 * @since 2.0
	 */
	protected ISequenceElement[] getElements(final UUIDObject modelObject, final IPortSlotProvider psp, final ModelEntityMap mem) {
		if (modelObject instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) modelObject;
			final IVesselEventPortSlot eventSlot = mem.getOptimiserObject(event, IVesselEventPortSlot.class);
			if (eventSlot != null) {
				return new ISequenceElement[] { psp.getElement(eventSlot) };
			}
		} else if (modelObject instanceof Cargo) {
			final Cargo cargo = (Cargo) modelObject;
			final Slot loadSlot = cargo.getLoadSlot();
			final Slot dischargeSlot = cargo.getDischargeSlot();
			final IPortSlot loadPortSlot = mem.getOptimiserObject(loadSlot, IPortSlot.class);
			final IPortSlot dischargePortSlot = mem.getOptimiserObject(dischargeSlot, IPortSlot.class);
			if (loadPortSlot != null && dischargePortSlot != null) {
				return new ISequenceElement[] { psp.getElement(loadPortSlot), psp.getElement(dischargePortSlot) };
			}
		} else if (modelObject instanceof Slot) {
			final Slot slot = (Slot) modelObject;
			final IPortSlot portSlot = mem.getOptimiserObject(slot, IPortSlot.class);
			if (portSlot != null) {
				return new ISequenceElement[] { psp.getElement(portSlot) };
			}
		}
		return new ISequenceElement[0];
	}
}
