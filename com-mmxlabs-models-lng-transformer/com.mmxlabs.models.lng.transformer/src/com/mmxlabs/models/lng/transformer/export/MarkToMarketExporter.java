/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * Exporter for getting out the details of Mark to market details
 * 
 * @author Simon Goodall
 * @since 4.1
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
	public Event export(final ISequenceElement element, final Map<String, Object> annotations) {

		if (element == null) {
			return null;
		}

		// "element" represents an IPortSlot
		final IPortSlot slot = portSlotProvider.getPortSlot(element);

		if (slot == null) {
			return null;
		}

		final Port ePort = entities.getModelObject(slot.getPort(), Port.class);
		if (ePort == null) {
			// Port maybe null for e.g. DES Purchases.
			// return null;
		}

		if (slot instanceof IDischargeSlot || slot instanceof ILoadSlot) {

			final IMarkToMarket market = marketProvider.getMarketForElement(element);
			if (market == null) {
				return null;
			}

			// TODO this will have to look at market-generated slots.
			final Slot optSlot = entities.getModelObject(slot, Slot.class);
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
			final IAllocationAnnotation allocation = (IAllocationAnnotation) annotations.get(SchedulerConstants.AI_volumeAllocationInfo);

			final MarketAllocation eAllocation = scheduleFactory.createMarketAllocation();
			eAllocation.setSlot(optSlot);
			eAllocation.setMarket(entities.getModelObject(market, SpotMarket.class));
			allocations.put(slot, eAllocation);
			output.getMarketAllocations().add(eAllocation);
			eAllocation.setSlotAllocation(slotAllocation);

			if (slot instanceof ILoadOption) {
				final int pricePerMMBTu = Calculator.costPerMMBTuFromM3(allocation.getSlotPricePerM3(slot), ((ILoadOption) slot).getCargoCVValue());
				slotAllocation.setPrice(OptimiserUnitConvertor.convertToExternalPrice(pricePerMMBTu));
				slotAllocation.setVolumeTransferred(OptimiserUnitConvertor.convertToExternalVolume(allocation.getSlotVolumeInM3(slot)));

			} else {
				final int cargoCV = market.getCVValue();
				final int pricePerMMBTu = Calculator.costPerMMBTuFromM3(allocation.getSlotPricePerM3(slot), cargoCV);
				slotAllocation.setPrice(OptimiserUnitConvertor.convertToExternalPrice(pricePerMMBTu));
				slotAllocation.setVolumeTransferred(OptimiserUnitConvertor.convertToExternalVolume(allocation.getSlotVolumeInM3(slot)));
			}

			sv.setStart(entities.getDateFromHours(allocation.getSlotTime(slot)));
			sv.setEnd(entities.getDateFromHours(allocation.getSlotTime(slot)));

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
