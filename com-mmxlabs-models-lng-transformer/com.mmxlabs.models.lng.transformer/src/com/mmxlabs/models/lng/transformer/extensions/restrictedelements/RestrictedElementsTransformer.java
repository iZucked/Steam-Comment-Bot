/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
 * @since 2.0
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
	
	private enum RestrictionType { FOLLOWER, PRECEDENT };
	
	private final Set<ISequenceElement> allElements = new HashSet<ISequenceElement>();

	private Collection<ISequenceElement> findAssociatedISequenceElements(Object obj) {
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
	 * @param object The contract with the restricted elements attached.
	 * @param elements The list of elements which are prohibited (or permitted).
	 * @param map A map indicating which ISequenceElements are associated with each element (e.g. Contract or Port) from the elements list
	 * @param type Whether the restriction prevents elements preceding, or following, the ISequenceElement associated with the contract.
	 */
	private void registerRestrictedElements(final EObject object, final EList<? extends EObject> elements, boolean isPermissive, final RestrictionType type) {
		// get the ISequenceElements associated with the source
		final Collection<ISequenceElement> sourceElements = findAssociatedISequenceElements(object);
		
		if (sourceElements != null) {		
			for (final EObject element : elements) {
				// get the list of ISequenceElements associated with each restricted element
				Collection<ISequenceElement> destinationElements = findAssociatedISequenceElements(element);
				if (destinationElements != null) {
					// take the complement of the list if the list is a permissive (as opposed to prohibitive) one 
					if (isPermissive) {
						final Set<ISequenceElement> permissive = new HashSet<ISequenceElement>(allElements);
						permissive.removeAll(destinationElements);
						destinationElements = permissive;
					}
					
					// register each prohibited pair
					for (final ISequenceElement source : sourceElements) {
						switch (type) {
						case FOLLOWER:
							restrictedElementsProviderEditor.setRestrictedElements(source, null, destinationElements);
							break;
						case PRECEDENT:
							restrictedElementsProviderEditor.setRestrictedElements(source, destinationElements, null);
							break;							
						}
					}
				}
			}
		}
	}
	
	/**
	 * @since 4.0
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
	}

	@Override
	public void finishTransforming() {
		final CommercialModel commercialModel = rootObject.getCommercialModel();
		if (commercialModel != null) {
			// Process purchase contract restrictions - these are the follower restrictions
			/*
			for (final PurchaseContract pc : commercialModel.getPurchaseContracts()) {
				registerRestrictedElements(pc, pc.getRestrictedContracts(), pc.isRestrictedListsArePermissive(), RestrictionType.FOLLOWER);
				registerRestrictedElements(pc, pc.getRestrictedPorts(), pc.isRestrictedListsArePermissive(), RestrictionType.FOLLOWER);
				if (contractMap.containsKey(pc)) {
					final Collection<ISequenceElement> sourceElements = contractMap.get(pc);
					for (final Contract c : pc.getRestrictedContracts()) {
						if (contractMap.containsKey(c)) {
							Collection<ISequenceElement> destinationElements = contractMap.get(c);
							if (pc.isRestrictedListsArePermissive()) {
								final Set<ISequenceElement> permissive = new HashSet<ISequenceElement>(allElements);
								permissive.removeAll(destinationElements);
								destinationElements = permissive;
							}
							for (final ISequenceElement source : sourceElements) {
								restrictedElementsProviderEditor.setRestrictedElements(source, null, destinationElements);
							}
						}
					}

					for (final Port p : SetUtils.getObjects(pc.getRestrictedPorts())) {
						if (portMap.containsKey(p)) {
							Collection<ISequenceElement> destinationElements = portMap.get(p);
							if (pc.isRestrictedListsArePermissive()) {
								final Set<ISequenceElement> permissive = new HashSet<ISequenceElement>(allElements);
								permissive.removeAll(destinationElements);
								destinationElements = permissive;
							}
							for (final ISequenceElement source : sourceElements) {
								restrictedElementsProviderEditor.setRestrictedElements(source, null, destinationElements);
							}
						}
					}
				}
			}
				*/

			// Process sales contract restrictions - these are the preceding restrictions
			/*
			for (final SalesContract sc : commercialModel.getSalesContracts()) {
				registerRestrictedElements(sc, sc.getRestrictedContracts(), sc.isRestrictedListsArePermissive(), RestrictionType.PRECEDENT);
				registerRestrictedElements(sc, sc.getRestrictedPorts(), sc.isRestrictedListsArePermissive(), RestrictionType.PRECEDENT);
				if (contractMap.containsKey(sc)) {
					final Collection<ISequenceElement> sourceElements = contractMap.get(sc);
					for (final Contract c : sc.getRestrictedContracts()) {
						if (contractMap.containsKey(c)) {
							Collection<ISequenceElement> destinationElements = contractMap.get(c);
							if (sc.isRestrictedListsArePermissive()) {
								final Set<ISequenceElement> permissive = new HashSet<ISequenceElement>(allElements);
								permissive.removeAll(destinationElements);
								destinationElements = permissive;
							}
							for (final ISequenceElement source : sourceElements) {
								restrictedElementsProviderEditor.setRestrictedElements(source, destinationElements, null);
							}
						}
					}

					for (final Port p : SetUtils.getObjects(sc.getRestrictedPorts())) {
						if (portMap.containsKey(p)) {
							Collection<ISequenceElement> destinationElements = portMap.get(p);
							if (sc.isRestrictedListsArePermissive()) {
								final Set<ISequenceElement> permissive = new HashSet<ISequenceElement>(allElements);
								permissive.removeAll(destinationElements);
								destinationElements = permissive;
							}
							for (final ISequenceElement source : sourceElements) {
								restrictedElementsProviderEditor.setRestrictedElements(source, destinationElements, null);
							}
						}
					}
				}
			}
				*/
			CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();
			
			for (LoadSlot slot: cargoModel.getLoadSlots()) {
				registerRestrictedElements(slot, slot.getSlotOrContractRestrictedContracts(), slot.getSlotOrContractRestrictedListsArePermissive(), RestrictionType.FOLLOWER);
				registerRestrictedElements(slot, slot.getSlotOrContractRestrictedPorts(), slot.getSlotOrContractRestrictedListsArePermissive(), RestrictionType.FOLLOWER);				
			}

			for (DischargeSlot slot: cargoModel.getDischargeSlots()) {
				registerRestrictedElements(slot, slot.getSlotOrContractRestrictedContracts(), slot.getSlotOrContractRestrictedListsArePermissive(), RestrictionType.PRECEDENT);
				registerRestrictedElements(slot, slot.getSlotOrContractRestrictedPorts(), slot.getSlotOrContractRestrictedListsArePermissive(), RestrictionType.PRECEDENT);				
			}
		
		}

		rootObject = null;
		contractMap.clear();
		portMap.clear();
		slotMap.clear();
		allElements.clear();
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable SalesContract salesContract, @NonNull  final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(@Nullable PurchaseContract purchaseContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {
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

}
