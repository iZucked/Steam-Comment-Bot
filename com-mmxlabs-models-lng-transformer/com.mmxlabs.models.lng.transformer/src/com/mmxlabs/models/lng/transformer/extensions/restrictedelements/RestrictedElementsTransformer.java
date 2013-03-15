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

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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

	private MMXRootObject rootObject;
	private final Map<Contract, Collection<ISequenceElement>> contractMap = new HashMap<Contract, Collection<ISequenceElement>>();
	private final Map<Port, Collection<ISequenceElement>> portMap = new HashMap<Port, Collection<ISequenceElement>>();
	private final Set<ISequenceElement> allElements = new HashSet<ISequenceElement>();

	/**
	 * @since 3.0
	 */
	@Override
	public void startTransforming(final MMXRootObject rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
	}

	@Override
	public void finishTransforming() {
		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
		if (commercialModel != null) {
			// Process purchase contract restrictions - these are the follower restrictions
			for (final PurchaseContract pc : commercialModel.getPurchaseContracts()) {
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

					for (final APort p : SetUtils.getPorts(pc.getRestrictedPorts())) {
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

			// Process sales contract restrictions - these are the preceding restrictions
			for (final SalesContract sc : commercialModel.getSalesContracts()) {
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

					for (final APort p : SetUtils.getPorts(sc.getRestrictedPorts())) {
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
		}

		rootObject = null;
		contractMap.clear();
		portMap.clear();
		allElements.clear();
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	/**
	 * @since 3.0
	 */
	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(final LNGPriceCalculatorParameters priceParameters) {
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
	}

	@Override
	public Collection<EClass> getContractEClasses() {
		return Collections.emptySet();
	}

}
