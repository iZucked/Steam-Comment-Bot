/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 */
public class RestrictedElementsTransformer implements IContractTransformer {

	@Inject
	private IRestrictedElementsProviderEditor restrictedElementsProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

	private LNGScenarioModel rootObject;
	// TODO: these maps probably don't need to be separate from one another - it would simplify matters to use just one Map<EObject, Collection<ISequenceElement>>
	private final Map<Contract, Collection<ISequenceElement>> contractMap = new HashMap<Contract, Collection<ISequenceElement>>();
	private final Map<Port, Collection<ISequenceElement>> portMap = new HashMap<Port, Collection<ISequenceElement>>();
	private final Map<Slot, Collection<ISequenceElement>> slotMap = new HashMap<Slot, Collection<ISequenceElement>>();

	private enum RestrictionType {
		FOLLOWER, PRECEDENT
	};

	private final Set<ISequenceElement> allElements = new HashSet<ISequenceElement>();

	private Collection<ISequenceElement> findAssociatedISequenceElements(final Object obj) {
		if (obj instanceof Contract) {
			return contractMap.get(obj);
		}
		if (obj instanceof Port) {
			return portMap.get(obj);
		}
		if (obj instanceof Slot) {
			return slotMap.get(obj);
		}

		return null;
	}

	/**
	 * Registers restrictions on which elements can follow or precede one another, using a restrictedElementsProviderEditor.
	 * 
	 * @param object
	 *            The contract with the restricted elements attached.
	 * @param elements
	 *            The list of elements which are prohibited (or permitted).
	 * @param map
	 *            A map indicating which ISequenceElements are associated with each element (e.g. Contract or Port) from the elements list
	 * @param type
	 *            Whether the restriction prevents elements preceding, or following, the ISequenceElement associated with the contract.
	 */
	private void registerRestrictedElements(final EObject object, final List<? extends EObject> elements, final boolean isPermissive, final RestrictionType type) {
		// get the ISequenceElements associated with the source
		final Collection<ISequenceElement> sourceElements = findAssociatedISequenceElements(object);

		if (elements != null && sourceElements != null) {

			Set<ISequenceElement> restrictedElements = new HashSet<>();

			for (final EObject element : elements) {
				// get the list of ISequenceElements associated with each restricted element
				final Collection<ISequenceElement> destinationElements = findAssociatedISequenceElements(element);
				if (destinationElements != null) {
					restrictedElements.addAll(destinationElements);
				}
			}
			if (restrictedElements.isEmpty()) {
				return;
			}

			// take the complement of the list if the list is a permissive (as opposed to prohibitive) one
			if (isPermissive) {
				final Set<ISequenceElement> permissive = new HashSet<ISequenceElement>(allElements);
				permissive.removeAll(restrictedElements);
				restrictedElements = permissive;
			}

			// register each prohibited pair
			for (final ISequenceElement source : sourceElements) {
				switch (type) {
				case FOLLOWER:
					restrictedElementsProviderEditor.addRestrictedElements(source, null, restrictedElements);
					break;
				case PRECEDENT:
					restrictedElementsProviderEditor.addRestrictedElements(source, restrictedElements, null);
					break;
				}
			}
		}
	}

	/**
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
	}

	@Override
	public void finishTransforming() {
		final CommercialModel commercialModel = rootObject.getReferenceModel().getCommercialModel();
		if (commercialModel != null) {
			final CargoModel cargoModel = rootObject.getCargoModel();

			// Process purchase contract restrictions - these are the follower restrictions
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {

				final List<Contract> restrictedContracts;
				final List<? extends EObject> restrictedPorts;
				boolean isPermissive = false;
				// if (slot.isSetRestrictedContracts() || slot.isSetRestrictedListsArePermissive() || slot.isSetRestrictedPorts()) {
				if (slot.isOverrideRestrictions()) {
					isPermissive = slot.isRestrictedListsArePermissive();
					restrictedContracts = slot.getRestrictedContracts();
					restrictedPorts = slot.getRestrictedPorts();
				} else {
					final Contract contract = slot.getContract();
					if (contract == null) {
						isPermissive = false;
						restrictedContracts = Collections.emptyList();
						restrictedPorts = Collections.emptyList();
					} else {
						isPermissive = contract.isRestrictedListsArePermissive();
						restrictedContracts = contract.getRestrictedContracts();
						restrictedPorts = contract.getRestrictedPorts();
					}
				}

				registerRestrictedElements(slot, restrictedContracts, isPermissive, RestrictionType.FOLLOWER);
				registerRestrictedElements(slot, restrictedPorts, isPermissive, RestrictionType.FOLLOWER);
			}

			// Process sales contract restrictions - these are the preceding restrictions
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {

				final List<Contract> restrictedContracts;
				final List<? extends EObject> restrictedPorts;
				boolean isPermissive = false;
				// if (slot.isSetRestrictedContracts() || slot.isSetRestrictedListsArePermissive() || slot.isSetRestrictedPorts()) {
				if (slot.isOverrideRestrictions()) {
					isPermissive = slot.isRestrictedListsArePermissive();
					restrictedContracts = slot.getRestrictedContracts();
					restrictedPorts = slot.getRestrictedPorts();
				} else {
					final Contract contract = slot.getContract();
					if (contract == null) {
						isPermissive = false;
						restrictedContracts = Collections.emptyList();
						restrictedPorts = Collections.emptyList();
					} else {
						isPermissive = contract.isRestrictedListsArePermissive();
						restrictedContracts = contract.getRestrictedContracts();
						restrictedPorts = contract.getRestrictedPorts();
					}
				}
				registerRestrictedElements(slot, restrictedContracts, isPermissive, RestrictionType.PRECEDENT);
				registerRestrictedElements(slot, restrictedPorts, isPermissive, RestrictionType.PRECEDENT);
			}

		}

		rootObject = null;
		contractMap.clear();
		portMap.clear();
		slotMap.clear();
		allElements.clear();
	}

	@Override
	public void slotTransformed(@NonNull final Slot modelSlot, @NonNull final IPortSlot optimiserSlot) {
		final ISequenceElement sequenceElement = portSlotProvider.getElement(optimiserSlot);
		allElements.add(sequenceElement);
		{
			final Port port = modelSlot.getPort();
			Collection<ISequenceElement> portElements;
			if (portMap.containsKey(port)) {
				portElements = portMap.get(port);
			} else {
				portElements = new HashSet<ISequenceElement>();
				portMap.put(port, portElements);
			}
			portElements.add(sequenceElement);
		}
		{
			final Contract contract = modelSlot.getContract();
			Collection<ISequenceElement> contractElements;
			if (contractMap.containsKey(contract)) {
				contractElements = contractMap.get(contract);
			} else {
				contractElements = new HashSet<ISequenceElement>();
				contractMap.put(contract, contractElements);
			}
			contractElements.add(sequenceElement);
		}
		{
			Collection<ISequenceElement> slotElements;
			if (slotMap.containsKey(modelSlot)) {
				slotElements = contractMap.get(modelSlot);
			} else {
				slotElements = new HashSet<ISequenceElement>();
				slotMap.put(modelSlot, slotElements);
			}
			slotElements.add(sequenceElement);
		}
	}

	@Override
	public Collection<EClass> getContractEClasses() {
		return Collections.emptySet();
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(final SalesContract salesContract, final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(final PurchaseContract purchaseContract, final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}
}
