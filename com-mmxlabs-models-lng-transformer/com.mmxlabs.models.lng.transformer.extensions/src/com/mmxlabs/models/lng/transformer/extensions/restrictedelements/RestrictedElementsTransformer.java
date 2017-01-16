/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.omg.PortableInterceptor.SUCCESSFUL;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
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
	private final Map<Contract, Collection<ISequenceElement>> contractMap = new HashMap<>();
	private final Map<Port, Collection<ISequenceElement>> portMap = new HashMap<>();
	private final Map<Slot, Collection<ISequenceElement>> slotMap = new HashMap<>();

	private final List<Slot> spotSlots = new LinkedList<>();

	private enum RestrictionType {
		FOLLOWER, PRECEDENT
	};

	private final Set<ISequenceElement> allElements = new HashSet<ISequenceElement>();

	private Collection<ISequenceElement> findAssociatedISequenceElements(final Object obj) {
		if (obj instanceof Contract) {
			return contractMap.get(obj);
		}
		if (obj instanceof Contract) {
			return contractMap.get(obj);
		}
		if (obj instanceof Port) {
			return portMap.get(obj);
		} else if (obj instanceof APortSet<?>) {
			final APortSet<?> aPortSet = (APortSet<?>) obj;
			final Set<Port> objects = (Set<Port>)SetUtils.getObjects(aPortSet);
			final Set<ISequenceElement> elements = new HashSet<>();
			for (final Port p  : objects) {
				elements.addAll(portMap.getOrDefault(p, Collections.emptySet()));
			}
			return elements;
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

			// take the complement of the list if the list is a permissive (as opposed to prohibitive) one
			if (isPermissive) {
				final Set<ISequenceElement> permissive = new HashSet<ISequenceElement>(allElements);
				permissive.removeAll(restrictedElements);
				restrictedElements = permissive;
			} else {
				if (restrictedElements.isEmpty()) {
					return;
				}
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
		// Set of slots to make sure we do not process spot market slots twice
		final Set<Slot> seenSlots = new HashSet<>();
		final CommercialModel commercialModel = rootObject.getReferenceModel().getCommercialModel();
		if (commercialModel != null) {
			final CargoModel cargoModel = rootObject.getCargoModel();
			final BiConsumer<Slot, RestrictionType> applyRestrictions = (slot, type) -> {
				final List<Contract> restrictedContracts;
				final List<? extends EObject> restrictedPorts;
				boolean isPermissive = false;
				if (slot.isOverrideRestrictions()) {
					isPermissive = slot.isRestrictedListsArePermissive();
					restrictedContracts = slot.getRestrictedContracts();
					restrictedPorts = slot.getRestrictedPorts();
				} else {
					final Contract contract = slot.getContract();
					if (contract == null) {
						if (slot instanceof SpotSlot) {
							final SpotSlot spotSlot = (SpotSlot) slot;
							final SpotMarket spotMarket = spotSlot.getMarket();

							isPermissive = spotMarket.isRestrictedListsArePermissive();
							restrictedContracts = spotMarket.getRestrictedContracts();
							restrictedPorts = spotMarket.getRestrictedPorts();
						} else {
							isPermissive = false;
							restrictedContracts = Collections.emptyList();
							restrictedPorts = Collections.emptyList();
						}
					} else {

						isPermissive = contract.isRestrictedListsArePermissive();
						restrictedContracts = contract.getRestrictedContracts();
						restrictedPorts = contract.getRestrictedPorts();
					}
				}
				registerRestrictedElements(slot, restrictedContracts, isPermissive, type);
				registerRestrictedElements(slot, restrictedPorts, isPermissive, type);

			};
			// Process purchase contract restrictions - these are the follower restrictions
			for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				applyRestrictions.accept(slot, RestrictionType.FOLLOWER);
			}

			// Process sales contract restrictions - these are the preceding restrictions
			for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
				applyRestrictions.accept(slot, RestrictionType.PRECEDENT);
			}
			for (final Slot slot : spotSlots) {
				if (slot instanceof LoadSlot) {
					applyRestrictions.accept(slot, RestrictionType.FOLLOWER);
				} else if (slot instanceof DischargeSlot) {
					applyRestrictions.accept(slot, RestrictionType.PRECEDENT);
				}
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

		// Record spot slots as not all of them are attached to the model
		if (modelSlot instanceof SpotSlot) {
			spotSlots.add(modelSlot);
		}

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
