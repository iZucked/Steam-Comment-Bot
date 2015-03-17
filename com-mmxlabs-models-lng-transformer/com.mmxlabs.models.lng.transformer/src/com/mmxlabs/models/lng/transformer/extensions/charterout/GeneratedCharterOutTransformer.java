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
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
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


public class GeneratedCharterOutTransformer implements IContractTransformer, IVesselEventTransformer, IVesselAvailabilityTransformer{
	private ModelEntityMap modelEntityMap;
	private ISchedulerBuilder builder;
	private LNGScenarioModel rootObject;

	@Inject
	private IPortSlotProviderEditor portSlotProvider;

	@Inject
	private IGeneratedCharterOutSlotProviderEditor generatedCharterOutSlotProviderEditor;
	
	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IVesselProviderEditor vesselProvider;

	@Override
	public void startTransforming(LNGScenarioModel rootObject, ModelEntityMap modelEntityMap, ISchedulerBuilder builder) {
		this.modelEntityMap = modelEntityMap;
		this.builder = builder;
		this.rootObject = rootObject;
	}

	@Override
	public void finishTransforming() {
		this.modelEntityMap = null;
		this.builder = null;
		this.rootObject = null;
	}

	@Override
	public void vesselEventTransformed(@NonNull VesselEvent modelEvent, @NonNull IVesselEventPortSlot optimiserEventSlot) {
		createSlotAndSequenceElement(optimiserEventSlot);
	}

	@Override
	public void vesselEventTransformed( @NonNull IVesselEventPortSlot optimiserEventSlot) {
		createSlotAndSequenceElement(optimiserEventSlot);
	}
	
	@Override
	public void slotTransformed(@NonNull Slot modelSlot, @NonNull IPortSlot optimiserSlot) {
		if (optimiserSlot instanceof IDischargeOption)
			createSlotAndSequenceElement(optimiserSlot);
	}

	private void createSlotAndSequenceElement(@NonNull IPortSlot slotGeneratedFrom) {
		boolean hasAlreadyGenerated = generatedCharterOutSlotProviderEditor.getPortSlotGeneratedByPortSlot(slotGeneratedFrom) != null;
		if (!hasAlreadyGenerated) {
			// create a VesselEventPortSlot (details will be filled in during generation)
			IGeneratedCharterOutVesselEventPortSlot vesselEventPortSlot = builder.createGeneratedCharterOutEvent(String.format("gco-%s",slotGeneratedFrom.getId()),  null, slotGeneratedFrom.getPort(), null, 0, 0, 0, 0, 0, 0);
			// create a sequence element 
			SequenceElement sequenceElement = builder.createSequenceElement(vesselEventPortSlot.getId());
			
			portSlotProvider.setPortSlot(sequenceElement, vesselEventPortSlot);
			
			ISequenceElement originalSequenceElement = portSlotProvider.getElement(slotGeneratedFrom);
			generatedCharterOutSlotProviderEditor.setPortSlot(sequenceElement, vesselEventPortSlot);
			generatedCharterOutSlotProviderEditor.setPortSlotGeneratedByElement(originalSequenceElement, vesselEventPortSlot);
		}
	}

	@Override
	public void vesselAvailabilityTransformed(@NonNull VesselAvailability modelEvent, @NonNull IVesselAvailability optimiserAvailability) {
		IResource resource = vesselProvider.getResource(optimiserAvailability);
		if (resource != null) {
			ISequenceElement startElement = startEndRequirementProvider.getStartElement(resource);
			if (startElement != null) {
				IPortSlot portSlot = portSlotProvider.getPortSlot(startElement);
				if (portSlot != null) {
					createSlotAndSequenceElement(portSlot);
				}
			}
		}
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable SalesContract salesContract, @NonNull LNGPriceCalculatorParameters priceParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(@Nullable PurchaseContract purchaseContract, @NonNull LNGPriceCalculatorParameters priceParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EClass> getContractEClasses() {
		// TODO Auto-generated method stub
		return new LinkedList<EClass>();
	}
	
	public void setInjectedMembers(IPortSlotProviderEditor portSlotProvider, IGeneratedCharterOutSlotProviderEditor generatedCharterOutSlotProviderEditor,
			IStartEndRequirementProvider startEndRequirementProvider, IVesselProviderEditor vesselProvider,
			ISchedulerBuilder builder) {
		this.portSlotProvider = portSlotProvider;
		this.generatedCharterOutSlotProviderEditor = generatedCharterOutSlotProviderEditor;
		this.startEndRequirementProvider = startEndRequirementProvider;
		this.vesselProvider = vesselProvider;
		this.builder = builder;
	}
	
}
