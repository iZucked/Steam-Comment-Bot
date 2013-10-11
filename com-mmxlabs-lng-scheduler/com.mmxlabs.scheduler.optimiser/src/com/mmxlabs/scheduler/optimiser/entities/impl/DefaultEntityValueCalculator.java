/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.DurationDetailElement;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class DefaultEntityValueCalculator implements IEntityValueCalculator {

	private static final boolean includeTimeCharterInFitness = true;

	@Inject
	private IEntityProvider entityProvider;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private IPortCostProvider portCostProvider;

	public DefaultEntityValueCalculator() {

	}

	@Override
	public void init(final IOptimisationData data) {

	}

	@Override
	public void dispose() {

	}

	/**
	 * evaluate the group value of the given cargo
	 * 
	 * @param plan
	 * @param currentAllocation
	 * @return
	 */
	@Override
	public long evaluate(final VoyagePlan plan, final IAllocationAnnotation currentAllocation, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution) {
		final IEntity shippingEntity = entityProvider.getShippingEntity();
		// get each entity
		final List<IPortSlot> slots = currentAllocation.getSlots();

		long revenue = 0;
		long cost = 0;
		long additionProfitAndLoss = 0;

		int taxTime = -1;
		int[] dischargePricesPerMMBTu = new int[slots.size()];
		long[] slotVolumesInM3 = new long[slots.size()];
		int[] arrivalTimes = new int[slots.size()];
		// Extract data
		int idx = 0;
		for (final IPortSlot slot : slots) {
			if (slot instanceof IDischargeOption) {
				dischargePricesPerMMBTu[idx] = currentAllocation.getSlotPricePerMMBTu(slot);
			} else {
				dischargePricesPerMMBTu[idx] = Integer.MAX_VALUE;
			}
			arrivalTimes[idx] = currentAllocation.getSlotTime(slot);
			slotVolumesInM3[idx] = currentAllocation.getSlotVolumeInM3(slot);

			idx++;
		}

		for (final IPortSlot slot : slots) {

			// Determined by volume allocator
			final long volumeInM3 = currentAllocation.getSlotVolumeInM3(slot);
			final int pricePerM3 = currentAllocation.getSlotPricePerM3(slot);
			final int time = currentAllocation.getSlotTime(slot);
			// Use the last slot to set the time for tax purposes. TODO: This is valid for LD cases only
			taxTime = time;

			final long value = Calculator.convertM3ToM3Price(volumeInM3, pricePerM3);

			if (slot instanceof ILoadOption) {
				ILoadOption loadOption = (ILoadOption) slot;
				cost += value;
				additionProfitAndLoss += loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, slots, arrivalTimes, slotVolumesInM3, dischargePricesPerMMBTu, vessel, plan, null);
			} else if (slot instanceof IDischargeOption) {
				revenue += value;
			}
		}

		final long generatedCharterOutRevenue = getGeneratedCharterOutRevenue(plan, vessel);
		// Calculate the value for the fitness function
		final long result;
		{
			final long shippingCosts = getShippingCosts(plan, vessel, false, includeTimeCharterInFitness, vesselStartTime, null);
			final long shippingTotalPretaxProfit = revenue + additionProfitAndLoss - cost - shippingCosts;
			final long shippingProfit = shippingEntity.getTaxedProfit(shippingTotalPretaxProfit, taxTime);

			final long generatedCharterOutCosts = getGeneratedCharterOutCosts(plan, vessel, includeTimeCharterInFitness, vesselStartTime, null);
			final long charterOutPretaxProfit = generatedCharterOutRevenue - generatedCharterOutCosts;
			final long charterOutProfit = shippingEntity.getTaxedProfit(charterOutPretaxProfit, taxTime);
			result = shippingProfit + charterOutProfit;
		}

		// Solution Export branch - should called infrequently
		if (annotatedSolution != null) {
			final IPortSlot firstSlot = slots.get(0);
			// Try and find a real element to attach P&L data to
			ISequenceElement exportElement = null;
			for (final IPortSlot pSlot : slots) {
				exportElement = slotProvider.getElement(pSlot);
				if (exportElement != null) {
					break;
				}
			}
			{
				// Calculate P&L with TC rates
				generateAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, cost, taxTime, generatedCharterOutRevenue, firstSlot, exportElement, false, true);

				// Calculate P&L without TC rates
				generateAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, cost, taxTime, generatedCharterOutRevenue, firstSlot, exportElement, false, false);
			}
		}

		return result;
	}

	/**
	 * Evaluate the group value of this plan, which is not carrying a cargo. It may just be a big cost to shipping.
	 * 
	 * TODO implement charter out revenues
	 * 
	 * @param plan
	 * @return
	 */
	@Override
	public long evaluate(final VoyagePlan plan, final IVessel vessel, final int planStartTime, final int vesselStartTime, final IAnnotatedSolution annotatedSolution) {
		final IEntity shippingEntity = entityProvider.getShippingEntity();

		final long value;
		final long revenue;
		final long generatedCharterOutRevenue;
		{
			final long shippingCost = getShippingCosts(plan, vessel, true, includeTimeCharterInFitness, vesselStartTime, null);
			final PortDetails portDetails = (PortDetails) plan.getSequence()[0];
			if (portDetails.getOptions().getPortSlot().getPortType() == PortType.CharterOut) {
				final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) portDetails.getOptions().getPortSlot();
				revenue = vesselEventPortSlot.getVesselEvent().getHireCost() + vesselEventPortSlot.getVesselEvent().getRepositioning();
			} else {
				revenue = 0;
			}

			generatedCharterOutRevenue = getGeneratedCharterOutRevenue(plan, vessel);

			// Calculate the value for the fitness function
			value = shippingEntity.getTaxedProfit(revenue + generatedCharterOutRevenue - shippingCost, planStartTime);
		}
		// Solution Export branch - should called infrequently
		if (annotatedSolution != null) {

			final IPortSlot firstSlot = ((PortDetails) plan.getSequence()[0]).getOptions().getPortSlot();
			final ISequenceElement exportElement = slotProvider.getElement(firstSlot);

			// We include LNG costs here, but this may not be desirable - this depends on whether or not we consider the LNG a sunk cost...
			generateAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, 0, planStartTime, generatedCharterOutRevenue, firstSlot, exportElement, true, true);
			generateAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, 0, planStartTime, generatedCharterOutRevenue, firstSlot, exportElement, true, false);
		}

		return value;
	}

	/**
	 * @since 7.0
	 */
	@Override
	public long getShippingCosts(final VoyagePlan plan, final IVessel vessel, final boolean includeLNG, final boolean includeTimeCharterCosts, final int vesselStartTime, final IDetailTree[] detailsRef) {

		if (vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return 0l;
		}

		// @formatter:off
		final long shippingCosts = plan.getTotalRouteCost()
				+ plan.getTotalFuelCost(FuelComponent.Base) 
				+ plan.getTotalFuelCost(FuelComponent.Base_Supplemental)
				+ plan.getTotalFuelCost(FuelComponent.Cooldown)
				+ plan.getTotalFuelCost(FuelComponent.IdleBase)
				+ plan.getTotalFuelCost(FuelComponent.IdlePilotLight)
				+ plan.getTotalFuelCost(FuelComponent.PilotLight)
				+ (includeLNG
						? plan.getTotalFuelCost(FuelComponent.NBO) 
						+ plan.getTotalFuelCost(FuelComponent.FBO) 
						+ plan.getTotalFuelCost(FuelComponent.IdleNBO)
						: 0);
		// @formatter:on

		long portCosts = 0;
		final Object[] sequence = plan.getSequence();
		for (int i = 0; i < sequence.length - 1; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof PortDetails) {

				final PortDetails portDetails = (PortDetails) obj;

				final IPort port = portDetails.getOptions().getPortSlot().getPort();
				final PortType portType = portDetails.getOptions().getPortSlot().getPortType();
				portCosts += portCostProvider.getPortCost(port, vessel, portType);
			}

		}

		final ICurve hireRate;
		switch (vessel.getVesselInstanceType()) {
		case SPOT_CHARTER:
			hireRate = vessel.getHourlyCharterInPrice();
			break;
		case TIME_CHARTER:
			if (includeTimeCharterCosts) {
				hireRate = vessel.getHourlyCharterInPrice();
			} else {
				hireRate = null;
			}
			break;
		case CARGO_SHORTS:
			hireRate = vessel.getHourlyCharterInPrice();
			break;
		default:
			hireRate = null;
			break;
		}
		final long planDuration = getPartialPlanDuration(plan, 1); // skip off the last port details, as we don't pay for that here.
		final long hireCosts;
		if (hireRate == null) {
			hireCosts = 0;
		} else {
			final long hourlyCharterInPrice = hireRate.getValueAtPoint(vesselStartTime);
			hireCosts = hourlyCharterInPrice * planDuration;
		}
		if (detailsRef != null) {
			//

			final DetailTree details = new DetailTree("Shipping Costs", new TotalCostDetailElement(shippingCosts + hireCosts + portCosts));
			// Note: This is a hack to avoid changing the interface and the default case..... We need to decide on how to manage this whole property thing properly.
			// If length == 2 rather than 1, grab all the details...
			if (detailsRef.length == 2) {
				//
				details.addChild("Duration", new DurationDetailElement(planDuration));
				details.addChild("Route Cost", new TotalCostDetailElement(plan.getTotalRouteCost()));
				details.addChild("Base Fuel", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.Base)));
				details.addChild("Base Fuel Supplement", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.Base_Supplemental)));
				details.addChild("Cooldown", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.Cooldown)));
				details.addChild("Idle Base", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.IdleBase)));
				details.addChild("Pilot Light", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.PilotLight)));
				details.addChild("Idle Pilot Light", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.IdlePilotLight)));
				if (includeLNG) {
					details.addChild("NBO", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.NBO)));
					details.addChild("FBO", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.FBO)));
					details.addChild("Idle NBO", new TotalCostDetailElement(plan.getTotalFuelCost(FuelComponent.IdleNBO)));
				}
				details.addChild("Hire Costs", new TotalCostDetailElement(hireCosts));
				details.addChild("Port Costs", new TotalCostDetailElement(portCosts));
			}
			detailsRef[0] = details;
		}

		return shippingCosts + portCosts + hireCosts;
	}

	private long getGeneratedCharterOutCosts(final VoyagePlan plan, final IVessel vessel, final boolean includeTimeCharterRates, final int vesselStartTime, final IDetailTree[] detailsRef) {

		int planDuration = 0;
		for (final Object obj : plan.getSequence()) {

			if (obj instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().isCharterOutIdleTime()) {
					planDuration += voyageDetails.getIdleTime();
				}
			}
		}

		final ICurve hireRate;
		switch (vessel.getVesselInstanceType()) {
		case SPOT_CHARTER:
			hireRate = vessel.getHourlyCharterInPrice();
			break;
		case TIME_CHARTER:
			if (includeTimeCharterRates) {
				hireRate = vessel.getHourlyCharterInPrice();
			} else {
				hireRate = null;
			}
			break;
		case CARGO_SHORTS:
			hireRate = vessel.getHourlyCharterInPrice();
			break;
		default:
			hireRate = null;
			break;
		}
		final long hireCosts;
		if (hireRate == null) {
			hireCosts = 0;
		} else {
			final long hourlyCharterInPrice = hireRate.getValueAtPoint(vesselStartTime);
			hireCosts = hourlyCharterInPrice * planDuration;
		}

		return hireCosts;
	}

	private long getGeneratedCharterOutRevenue(final VoyagePlan plan, final IVessel vessel) {
		long charterRevenue = 0;

		for (final Object obj : plan.getSequence()) {

			if (obj instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().isCharterOutIdleTime()) {
					final long hourlyCharterOutPrice = voyageDetails.getOptions().getCharterOutHourlyRate();
					charterRevenue += Calculator.quantityFromRateTime(hourlyCharterOutPrice, voyageDetails.getIdleTime());
				}
			}
		}
		return charterRevenue;
	}

	private int getPartialPlanDuration(final VoyagePlan plan, final int skip) {
		int planDuration = 0;
		final Object[] sequence = plan.getSequence();
		final int k = sequence.length - skip;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				planDuration += voyageDetails.getTravelTime();
				if (!voyageDetails.getOptions().isCharterOutIdleTime()) {
					planDuration += voyageDetails.getIdleTime();
				}
			} else {
				planDuration += ((PortDetails) o).getOptions().getVisitDuration();
			}
		}
		return planDuration;
	}

	private void generateAnnotations(final VoyagePlan plan, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution, final IEntity shippingEntity,
			final long revenue, final long cost, final int taxTime, final long generatedCharterOutRevenue, final IPortSlot firstSlot, final ISequenceElement exportElement, final boolean includeLNG,
			final boolean includeTimeCharterRates) {
		{
			final long shippingCosts = getShippingCosts(plan, vessel, includeLNG, includeTimeCharterRates, vesselStartTime, null);
			final long shippingTotalPretaxProfit = revenue - cost - shippingCosts;
			final long shippingProfit = shippingEntity.getTaxedProfit(shippingTotalPretaxProfit, taxTime);

			final DetailTree shippingDetails = new DetailTree();

			final IDetailTree[] detailsRef = new IDetailTree[1];
			getShippingCosts(plan, vessel, true, true, vesselStartTime, detailsRef);

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, shippingProfit, shippingDetails);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			final String label = includeTimeCharterRates ? SchedulerConstants.AI_profitAndLoss : SchedulerConstants.AI_profitAndLossNoTimeCharterRate;
			annotatedSolution.getElementAnnotations().setAnnotation(exportElement, label, annotation);
		}
		if (generatedCharterOutRevenue != 0) {

			final long charterOutCosts = getGeneratedCharterOutCosts(plan, vessel, includeTimeCharterRates, vesselStartTime, null);
			final long charterOutPretaxProfit = generatedCharterOutRevenue - charterOutCosts;
			final long charterOutProfit = shippingEntity.getTaxedProfit(charterOutPretaxProfit, taxTime);

			final DetailTree details = new DetailTree();

			details.addChild(new DetailTree("Charter Out", new TotalCostDetailElement(generatedCharterOutRevenue)));

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, charterOutProfit, details);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			final ISequenceElement element = slotProvider.getElement(firstSlot);
			final String label = includeTimeCharterRates ? SchedulerConstants.AI_charterOutProfitAndLoss : SchedulerConstants.AI_charterOutProfitAndLossNoTimeCharterRate;
			annotatedSolution.getElementAnnotations().setAnnotation(element, label, annotation);
		}
	}
}
