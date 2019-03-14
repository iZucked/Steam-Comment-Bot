/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedslots;

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

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
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
public class RestrictedSlotsTransformer implements IContractTransformer {

	@Inject
	private IRestrictedSlotsProviderEditor restrictedElementsProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

	private LNGScenarioModel rootObject;
	private final Map<Slot, Collection<ISequenceElement>> slotMap = new HashMap<>();

	private final List<LoadSlot> loadSlots = new LinkedList<>();
	private final List<DischargeSlot> dischargeSlots = new LinkedList<>();

	private enum RestrictionType {
		FOLLOWER, PRECEDENT
	};

	private final Set<ISequenceElement> allElements = new HashSet<ISequenceElement>();

	private Collection<ISequenceElement> findAssociatedISequenceElements(final Object obj) {
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
				final Set<ISequenceElement> permissive = new HashSet<>(allElements);
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

		final BiConsumer<Slot, RestrictionType> applyRestrictions = (slot, type) -> {
			final List<Slot> restrictedSlots = slot.getRestrictedSlots();
			boolean isPermissive = slot.isRestrictedSlotsArePermissive();

			if (!restrictedSlots.isEmpty()) {
				registerRestrictedElements(slot, restrictedSlots, isPermissive, type);
			}

		};
		// Process purchase contract restrictions - these are the follower restrictions
		for (final LoadSlot slot : loadSlots) {
			applyRestrictions.accept(slot, RestrictionType.FOLLOWER);
		}

		// Process sales contract restrictions - these are the preceding restrictions
		for (final DischargeSlot slot : dischargeSlots) {
			applyRestrictions.accept(slot, RestrictionType.PRECEDENT);
		}

		rootObject = null;
		slotMap.clear();
		allElements.clear();
	}

	@Override
	public void slotTransformed(@NonNull final Slot modelSlot, @NonNull final IPortSlot optimiserSlot) {
		final ISequenceElement sequenceElement = portSlotProvider.getElement(optimiserSlot);

		// Record spot slots as not all of them are attached to the model
		if (modelSlot instanceof LoadSlot) {
			loadSlots.add((LoadSlot) modelSlot);
		}
		if (modelSlot instanceof DischargeSlot) {
			dischargeSlots.add((DischargeSlot) modelSlot);
		}

		allElements.add(sequenceElement);
		{
			Collection<ISequenceElement> slotElements;
			if (slotMap.containsKey(modelSlot)) {
				slotElements = slotMap.get(modelSlot);
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
