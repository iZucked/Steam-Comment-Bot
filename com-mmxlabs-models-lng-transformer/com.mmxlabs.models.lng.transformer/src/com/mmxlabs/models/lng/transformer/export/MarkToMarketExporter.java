/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * Exporter for getting out the details of Mark to market details
 * 
 * @author Simon Goodall
 * 
 */
public class MarkToMarketExporter extends BaseAnnotationExporter {
	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IMarkToMarketProvider marketProvider;

	private final HashMap<IPortSlot, MarketAllocation> allocations = new HashMap<IPortSlot, MarketAllocation>();

	@Override
	public void init() {
		allocations.clear();
	}

	@Override
	public Event export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations) {

		if (element == null) {
			return null;
		}

		// "element" represents an IPortSlot
		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot == null) {
			return null;
		}

		final Port ePort = modelEntityMap.getModelObject(slot.getPort(), Port.class);
		if (ePort == null) {
			// Port maybe null for e.g. DES Purchases.
			// return null;
		}

		if (slot instanceof IDischargeOption || slot instanceof ILoadOption) {

			final IMarkToMarket market = marketProvider.getMarketForElement(element);
			if (market == null) {
				return null;
			}

			// TODO this will have to look at market-generated slots.
			final Slot optSlot = modelEntityMap.getModelObject(slot, Slot.class);
			if (optSlot instanceof SpotSlot) {
				// Skip spot slots
				return null;
			}

			final SlotVisit sv = factory.createSlotVisit();
			final SlotAllocation slotAllocation = factory.createSlotAllocation();
			sv.setSlotAllocation(slotAllocation);

			output.getSlotAllocations().add(slotAllocation);
			slotAllocation.setSlot(optSlot);

			// Output allocation info
			final ICargoValueAnnotation cargoValueAnnotation = (ICargoValueAnnotation) annotations.get(SchedulerConstants.AI_cargoValueAllocationInfo);

			final MarketAllocation eAllocation = scheduleFactory.createMarketAllocation();
			eAllocation.setSlot(optSlot);
			eAllocation.setSlotVisit(sv);
			eAllocation.setMarket(modelEntityMap.getModelObject(market, SpotMarket.class));
			allocations.put(slot, eAllocation);
			output.getMarketAllocations().add(eAllocation);
			eAllocation.setSlotAllocation(slotAllocation);

			if (slot instanceof ILoadOption) {
				final int pricePerMMBTu = cargoValueAnnotation.getSlotPricePerMMBTu(slot);
				slotAllocation.setPrice(OptimiserUnitConvertor.convertToExternalPrice(pricePerMMBTu));
				slotAllocation.setVolumeTransferred(OptimiserUnitConvertor.convertToExternalVolume(cargoValueAnnotation.getSlotVolumeInM3(slot)));
				slotAllocation.setVolumeValue(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotValue(slot)));
			} else {
				final int pricePerMMBTu = cargoValueAnnotation.getSlotPricePerMMBTu(slot);
				slotAllocation.setPrice(OptimiserUnitConvertor.convertToExternalPrice(pricePerMMBTu));
				slotAllocation.setVolumeTransferred(OptimiserUnitConvertor.convertToExternalVolume(cargoValueAnnotation.getSlotVolumeInM3(slot)));
				slotAllocation.setVolumeValue(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotValue(slot)));
			}

			{
				// /// Get the "fake" mtm slot data
				// Get all the allocation slots
				final Set<IPortSlot> slots = new HashSet<IPortSlot>(cargoValueAnnotation.getSlots());
				if (slots.size() != 2) {
					throw new IllegalStateException("Expected 2 slots - got " + slots.size());
				}
				// Remove the real slot to leave the fake slot
				slots.remove(slot);
				if (slots.size() != 1) {
					throw new IllegalStateException("Expected slot to be part of allocation slots");
				}

				// Look up price and add to MarketAllocation.
				final IPortSlot mtmSlot = slots.iterator().next();
				if (mtmSlot instanceof ILoadOption) {
					final int pricePerMMBTu = cargoValueAnnotation.getSlotPricePerMMBTu(mtmSlot);
					eAllocation.setPrice(OptimiserUnitConvertor.convertToExternalPrice(pricePerMMBTu));

				} else {
					final int pricePerMMBTu = cargoValueAnnotation.getSlotPricePerMMBTu(mtmSlot);
					eAllocation.setPrice(OptimiserUnitConvertor.convertToExternalPrice(pricePerMMBTu));
				}

			}
			sv.setStart(modelEntityMap.getDateFromHours(cargoValueAnnotation.getSlotTime(slot), slot.getPort()));
			sv.setEnd(modelEntityMap.getDateFromHours(cargoValueAnnotation.getSlotTime(slot), slot.getPort()));

			sv.setSlotAllocation(slotAllocation);

			final IPortVisitEvent event = (IPortVisitEvent) annotations.get(SchedulerConstants.AI_visitInfo);

			if (event != null) {
				sv.getFuels().addAll(super.createFuelQuantities(event));
			}

			sv.setPort(ePort);
			return sv;

		}

		return null;
	}
}
