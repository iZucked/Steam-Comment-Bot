/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.charterout;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.contracts.IVesselAvailabilityTransformer;
import com.mmxlabs.models.lng.transformer.contracts.IVesselEventTransformer;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IGeneratedCharterOutSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;

public class GeneratedCharterOutTransformer implements IContractTransformer, IVesselEventTransformer, IVesselAvailabilityTransformer {
	private ISchedulerBuilder builder;

	@Inject
	private IPortSlotProviderEditor portSlotProvider;

	@Inject
	private IGeneratedCharterOutSlotProviderEditor generatedCharterOutSlotProviderEditor;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IVesselProviderEditor vesselProvider;

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.builder = builder;
	}

	@Override
	public void finishTransforming() {
		this.builder = null;
	}

	@Override
	public void vesselEventTransformed(@NonNull final VesselEvent modelEvent, @NonNull final IVesselEventPortSlot optimiserEventSlot) {
		createSlotAndSequenceElement(optimiserEventSlot);
	}

	@Override
	public void vesselEventTransformed(@NonNull final IVesselEventPortSlot optimiserEventSlot) {
		createSlotAndSequenceElement(optimiserEventSlot);
	}

	@Override
	public void slotTransformed(@NonNull final Slot modelSlot, @NonNull final IPortSlot optimiserSlot) {
		if (optimiserSlot instanceof IDischargeOption)
			createSlotAndSequenceElement(optimiserSlot);
	}

	private void createSlotAndSequenceElement(@NonNull final IPortSlot slotGeneratedFrom) {
		final boolean hasAlreadyGenerated = generatedCharterOutSlotProviderEditor.getPortSlotGeneratedByPortSlot(slotGeneratedFrom) != null;
		if (!hasAlreadyGenerated) {
			// create a VesselEventPortSlot (details will be filled in during generation)
			final IGeneratedCharterOutVesselEventPortSlot vesselEventPortSlot = builder.createGeneratedCharterOutEvent(String.format("gco-%s", slotGeneratedFrom.getId()), slotGeneratedFrom.getPort());
			// create a sequence element
			final SequenceElement sequenceElement = builder.createSequenceElement(vesselEventPortSlot.getId());

			portSlotProvider.setPortSlot(sequenceElement, vesselEventPortSlot);

			final ISequenceElement originalSequenceElement = portSlotProvider.getElement(slotGeneratedFrom);
			generatedCharterOutSlotProviderEditor.setPortSlot(sequenceElement, vesselEventPortSlot);
			generatedCharterOutSlotProviderEditor.setPortSlotGeneratedByElement(originalSequenceElement, vesselEventPortSlot);
		}
	}

	@Override
	public void vesselAvailabilityTransformed(@NonNull final VesselAvailability modelEvent, @NonNull final IVesselAvailability optimiserAvailability) {
		final IResource resource = vesselProvider.getResource(optimiserAvailability);
		if (resource != null) {
			final ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
			if (startElement != null) {
				final IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);
				if (portSlot != null) {
					createSlotAndSequenceElement(portSlot);
				}
			}
		}
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable final SalesContract salesContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(@Nullable final PurchaseContract purchaseContract, @NonNull final LNGPriceCalculatorParameters priceParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EClass> getContractEClasses() {
		// TODO Auto-generated method stub
		return new LinkedList<EClass>();
	}

	public void setInjectedMembers(final IPortSlotProviderEditor portSlotProvider, final IGeneratedCharterOutSlotProviderEditor generatedCharterOutSlotProviderEditor,
			final IStartEndRequirementProvider startEndRequirementProvider, final IVesselProviderEditor vesselProvider, final ISchedulerBuilder builder) {
		this.portSlotProvider = portSlotProvider;
		this.generatedCharterOutSlotProviderEditor = generatedCharterOutSlotProviderEditor;
		this.startEndRequirementProvider = startEndRequirementProvider;
		this.vesselProvider = vesselProvider;
		this.builder = builder;
	}

}
