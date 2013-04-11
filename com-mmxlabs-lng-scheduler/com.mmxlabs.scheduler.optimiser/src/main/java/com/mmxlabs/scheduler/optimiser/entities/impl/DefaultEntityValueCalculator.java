/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;

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
import com.mmxlabs.scheduler.optimiser.annotations.impl.LNGTransferDetailTree;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ShippingCostAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class DefaultEntityValueCalculator implements IEntityValueCalculator {

	@Inject
	private IEntityProvider entityProvider;

	@Inject
	private IVesselProvider vesselProvider;

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
	public long evaluate(final VoyagePlan plan, final IAllocationAnnotation currentAllocation, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution) {
		final IEntity shippingEntity = entityProvider.getShippingEntity();
		// get each entity
		List<IPortSlot> slots = currentAllocation.getSlots();
		// only handle single load / discharge case
//		assert(slots.size() == 2);
		
		

		int cvValue = -1;
		
		long revenue = 0;
		long cost = 0;
		
		for (IPortSlot slot : slots) {
			
			if (slot instanceof ILoadOption) {
				cvValue = ((ILoadOption) slot).getCargoCVValue();
				
			} else if (slot instanceof IDischargeOption) {
				
			}
			final long volumeInM3 = currentAllocation.getSlotVolumeInM3(slot);
			final int pricePerM3 = currentAllocation.getSlotPricePerM3(slot);
			final int pricePerMMBTu = Calculator.costPerMMBTuFromM3(pricePerM3, cvValue);
			final int time = currentAllocation.getSlotTime(slot);
			
			
			long value =  Calculator.convertM3ToM3Price(volumeInM3, pricePerM3);
			
			if (slot instanceof ILoadOption) {
				cvValue = ((ILoadOption) slot).getCargoCVValue();
				cost += value;
			} else if (slot instanceof IDischargeOption) {
				revenue += value;
			}
		}
		
//		final ILoadOption loadOption = (ILoadOption) slots.get(0);
//		final IDischargeOption dischargeOption = (IDischargeOption) slots.get(1);
//		
////		final IEntity downstreamEntity = entityProvider.getEntityForSlot(dischargeOption);
////		final IEntity upstreamEntity = entityProvider.getEntityForSlot(loadOption);
//
////		final int cvValue = loadOption.getCargoCVValue();
//		
//		final ILoadOption loadSlot = loadOption;
//		final IDischargeOption dischargeSlot = dischargeOption;
//
//
//		final long dischargeVolumeInM3 = currentAllocation.getSlotVolumeInM3(dischargeSlot);
//		final long loadVolumeInM3 = currentAllocation.getSlotVolumeInM3(loadSlot);
//		final int loadPricePerM3 = currentAllocation.getSlotPricePerM3(loadSlot);

		// TODO should we be thinking in $/m3 or /mmbtu?
		// TODO regas comes in here.
		// TODO inter-entity taxation will also come in here.

//		final int shippingTransferPricePerM3 = downstreamEntity.getDownstreamTransferPrice(dischargePricePerM3, cvValue);
//		final long downstreamPaysShipping = Calculator.convertM3ToM3Price(dischargeVolumeInM3, shippingTransferPricePerM3);
//		final long downstreamRevenue = Calculator.convertM3ToM3Price(dischargeVolumeInM3, dischargePricePerM3);
//
//		final long downstreamTotalPretaxProfit = downstreamRevenue - downstreamPaysShipping;
//
//		final int upstreamTransferPricePerM3 = upstreamEntity.getUpstreamTransferPrice(loadPricePerM3, cvValue);
//		final long shippingPaysUpstream = Calculator.convertM3ToM3Price(loadVolumeInM3, upstreamTransferPricePerM3);
//		final long shippingGasBalance = downstreamPaysShipping - shippingPaysUpstream;
		// now we need the total non-LNG shipping cost for the whole thing, which shipping pays.

//		final long upstreamRevenue = -Calculator.convertM3ToM3Price(loadVolumeInM3, loadPricePerM3);
//		final long upstreamTotalPretaxProfit = upstreamRevenue + shippingPaysUpstream;

		final long shippingCosts = getShippingCosts(plan, vessel, false, vesselStartTime, null);

		final long shippingTotalPretaxProfit = revenue - cost - shippingCosts;
//
//		final int dischargeTime = currentAllocation.getSlotTime(dischargeSlot);
//		final int loadTime = currentAllocation.getSlotTime(loadSlot);
//		
//		final int taxTime = dischargeTime;
//		final long upstreamProfit = upstreamEntity.getTaxedProfit(upstreamTotalPretaxProfit, taxTime);
		final long shippingProfit = shippingTotalPretaxProfit ; //shippingEntity.getTaxedProfit(shippingTotalPretaxProfit, taxTime);
//		final long downstreamProfit = downstreamEntity.getTaxedProfit(downstreamTotalPretaxProfit, taxTime);

		final long charterRevenue = getCharterRevenue(plan, vessel);
		final long taxedCharterRevenue = charterRevenue;//shippingEntity.getTaxedProfit(charterRevenue, taxTime);

		final long result = shippingProfit + taxedCharterRevenue;

		if (annotatedSolution != null) {
			IPortSlot firstSlot = slots.get(0);
			int firstTime = currentAllocation.getSlotTime(firstSlot);
			{
				final ISequenceElement element = slotProvider.getElement(firstSlot);

				final LinkedList<IProfitAndLossEntry> entries = new LinkedList<IProfitAndLossEntry>();

//				final DetailTree upstreamDetails = new DetailTree();
				final DetailTree shippingDetails = new DetailTree();
//				final DetailTree downstreamDetails = new DetailTree();

//				upstreamDetails.addChild(new LNGTransferDetailTree("Upstream purchase", loadVolumeInM3, loadPricePerM3, cvValue));
//				final IDetailTree upstreamToShipping = new LNGTransferDetailTree("Shipping to upstream", loadVolumeInM3, upstreamTransferPricePerM3, cvValue);
//				upstreamDetails.addChild(upstreamToShipping);
//				shippingDetails.addChild(upstreamToShipping);
//				final IDetailTree shippingToDownstream = new LNGTransferDetailTree("Downstream to shipping", dischargeVolumeInM3, shippingTransferPricePerM3, cvValue);
//				shippingDetails.addChild(shippingToDownstream);

				final IDetailTree[] detailsRef = new IDetailTree[1];
				// final long costNoBoiloff = getCosts(plan, vessel, false, vesselStartTime, detailsRef);
				// annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCost,
				// new ShippingCostAnnotation(currentAllocation.getLoadTime(), costNoBoiloff, detailsRef[0]));
				final long costIncBoiloff = getShippingCosts(plan, vessel, true, vesselStartTime, detailsRef);
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCostWithBoilOff,
						new ShippingCostAnnotation(firstTime, costIncBoiloff, detailsRef[0]));

//				downstreamDetails.addChild(shippingToDownstream);
//				downstreamDetails.addChild(new LNGTransferDetailTree("Downstream sale", dischargeVolumeInM3, dischargePricePerM3, cvValue));

//				entries.add(new ProfitAndLossEntry(upstreamEntity, upstreamProfit, upstreamDetails));
				entries.add(new ProfitAndLossEntry(shippingEntity, shippingProfit, shippingDetails));
//				entries.add(new ProfitAndLossEntry(downstreamEntity, downstreamProfit, downstreamDetails));
				// add entry for each entity

				final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(firstTime, entries);
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_profitAndLoss, annotation);

//				if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
//					loadOption.getLoadPriceCalculator().calculateLoadUnitPrice((ILoadSlot) loadOption, (IDischargeSlot) dischargeOption, loadTime,
//							currentAllocation.getSlotTime(dischargeSlot), dischargePricePerMMBTu, loadVolumeInM3, dischargeVolumeInM3, vessel, plan, shippingDetails);
//				} else {
//					assert(currentAllocation.getSlots().size() == 2);
//					//loadOption.getLoadPriceCalculator().calculateLoadUnitPrice(loadOption, dischargeOption, loadTime, dischargePricePerMMBTu, currentAllocation.getLoadVolumeInM3(),
//					//		shippingDetails);
//					loadOption.getLoadPriceCalculator().calculateLoadUnitPrice(loadOption, dischargeOption, loadTime, dischargePricePerMMBTu, currentAllocation.getSlotVolumeInM3(loadSlot),
//							shippingDetails);
//				}
			}

			// Add in charter out revenue
			if (charterRevenue != 0) {
				final DetailTree details = new DetailTree();

				details.addChild(new DetailTree("Charter Out", new TotalCostDetailElement(charterRevenue)));

				final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, taxedCharterRevenue, details);
				final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(firstTime, Collections.singleton(entry));
				final ISequenceElement element = slotProvider.getElement(firstSlot);
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_charterOutProfitAndLoss, annotation);
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

		final long shippingCost = getShippingCosts(plan, vessel, true, vesselStartTime, null);
		final long revenue;
		final PortDetails portDetails = (PortDetails) plan.getSequence()[0];
		if (portDetails.getOptions().getPortSlot().getPortType() == PortType.CharterOut) {
			final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) portDetails.getOptions().getPortSlot();
			revenue = vesselEventPortSlot.getVesselEvent().getHireCost() + vesselEventPortSlot.getVesselEvent().getRepositioning();
		} else {
			revenue = 0;
		}

		final long value = shippingEntity.getTaxedProfit(revenue - shippingCost, planStartTime);
		if (annotatedSolution != null) {
			final ISequenceElement element = slotProvider.getElement(((PortDetails) plan.getSequence()[0]).getOptions().getPortSlot());
			final DetailTree details = new DetailTree();

			if (revenue > 0) {
				// TODO take out strings.
				details.addChild(new DetailTree("Revenue", new TotalCostDetailElement(revenue)));
			}

			final IDetailTree[] detailsRef = new IDetailTree[1];
			// final long costNoBoiloff = getCosts(plan, vessel, false, vesselStartTime, detailsRef);
			// annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCost, new ShippingCostAnnotation(planStartTime, costNoBoiloff, detailsRef[0]));
			final long costIncBoiloff = getShippingCosts(plan, vessel, true, vesselStartTime, detailsRef);
			annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCostWithBoilOff, new ShippingCostAnnotation(planStartTime, costIncBoiloff, detailsRef[0]));

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, value, details);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(planStartTime, Collections.singleton(entry));
			annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_profitAndLoss, annotation);

		}

		return value;
	}

	@Override
	public long getShippingCosts(final VoyagePlan plan, final IVessel vessel, final boolean includeLNG, final int vesselStartTime, final IDetailTree[] detailsRef) {
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
		// case TIME_CHARTER:
		// hireRate = vessel.getHourlyCharterInPrice();
		// break;
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
			final long hourlyCharterInPrice = (int) hireRate.getValueAtPoint(vesselStartTime);
			hireCosts = hourlyCharterInPrice * (long) planDuration;
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

	private long getCharterRevenue(final VoyagePlan plan, final IVessel vessel) {
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
		// TODO: Duplicated in ProfitAndLossAllocationComponent
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

}
