/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.components;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoAllocationFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VirtualCargo;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.trading.optimiser.IEntity;
import com.mmxlabs.trading.optimiser.TradingConstants;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.trading.optimiser.annotations.impl.ProfitAndLossAnnotation;
import com.mmxlabs.trading.optimiser.annotations.impl.ProfitAndLossEntry;
import com.mmxlabs.trading.optimiser.providers.IEntityProvider;

/**
 * Basic group P&L calculator.
 * 
 * TODO: should this be divided differently?
 * 
 * Maybe in the new design we want different P&L bits in different places, specifically charter out revenue.
 * 
 * @author hinton
 * 
 */
public class ProfitAndLossAllocationComponent implements ICargoAllocationFitnessComponent {
	private final CargoSchedulerFitnessCore core;
	private final String entityProviderKey;
	private final String slotProviderKey;
	private final String vesselProviderKey;
	private final String name;
	private IEntityProvider entityProvider;
	private IEntity shippingEntity;

	private long lastEvaluation, lastAcceptance;
	private IVesselProvider vesselProvider;
	private IPortSlotProvider slotProvider;

	private long calibrationZeroLine = Long.MIN_VALUE;

	public ProfitAndLossAllocationComponent(final String profitComponentName, final String dcpEntityprovider, final String vesselProviderKey, final String slotProviderKey,
			final CargoSchedulerFitnessCore cargoSchedulerFitnessCore) {
		this.name = profitComponentName;
		this.entityProviderKey = dcpEntityprovider;
		this.vesselProviderKey = vesselProviderKey;
		this.slotProviderKey = slotProviderKey;
		this.core = cargoSchedulerFitnessCore;
	}

	@Override
	public void init(final IOptimisationData data) {
		this.entityProvider = data.getDataComponentProvider(entityProviderKey, IEntityProvider.class);
		this.vesselProvider = data.getDataComponentProvider(vesselProviderKey, IVesselProvider.class);
		this.slotProvider = data.getDataComponentProvider(slotProviderKey, IPortSlotProvider.class);
		if (entityProvider != null) {
			this.shippingEntity = entityProvider.getShippingEntity();
		}
	}

	@Override
	public void rejectLastEvaluation() {
	}

	@Override
	public void acceptLastEvaluation() {
		lastAcceptance = lastEvaluation;
	}

	@Override
	public void dispose() {
		entityProvider = null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getFitness() {
		return lastEvaluation;
	}

	@Override
	public IFitnessCore getFitnessCore() {
		return core;
	}

	@Override
	public long evaluate(final ScheduledSequences solution, final Collection<IAllocationAnnotation> allocations) {
		return evaluateAndMaybeAnnotate(solution, allocations, null);
	}

	private long evaluateAndMaybeAnnotate(final ScheduledSequences solution, final Collection<IAllocationAnnotation> allocations, final IAnnotatedSolution annotatedSolution) {
		if (entityProvider == null) {
			return 0;
		}
		final Iterator<IAllocationAnnotation> allocationIterator = allocations.iterator();
		IAllocationAnnotation currentAllocation = null;
		if (allocationIterator.hasNext()) {
			currentAllocation = allocationIterator.next();
		}
		long accumulator = 0;

		for (final ScheduledSequence sequence : solution) {
			final IVessel vessel = vesselProvider.getVessel(sequence.getResource());
			int time = sequence.getStartTime();
			for (final VoyagePlan plan : sequence.getVoyagePlans()) {
				final PortDetails firstDetails = (PortDetails) plan.getSequence()[0];
				final PortDetails lastDetails = (PortDetails) plan.getSequence()[2];
				if ((currentAllocation != null) && ((firstDetails.getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getPortSlot() == currentAllocation.getDischargeOption()))) {
					final long cargoGroupValue = evaluate(plan, currentAllocation, vessel, annotatedSolution);
					accumulator += cargoGroupValue;
					if (allocationIterator.hasNext()) {
						currentAllocation = allocationIterator.next();
					} else {
						currentAllocation = null;
					}
				} else {
					final long otherGroupValue = evaluate(plan, vessel, time, annotatedSolution);
					accumulator += otherGroupValue;
				}
				time += getPlanDuration(plan);
			}
		}

		if (calibrationZeroLine == Long.MIN_VALUE) {
			calibrationZeroLine = accumulator;
		}

		lastEvaluation = (calibrationZeroLine - accumulator) / Calculator.ScaleFactor;
		return lastEvaluation;
	}

	private final Map<String, Long> lastCargoValues = new HashMap<String, Long>();
	private final Map<String, String> lastCargoDetails = new HashMap<String, String>();

	/**
	 * evaluate the group value of the given cargo
	 * 
	 * @param plan
	 * @param currentAllocation
	 * @return
	 */
	private long evaluate(final VoyagePlan plan, final IAllocationAnnotation currentAllocation, final IVessel vessel, final IAnnotatedSolution annotatedSolution) {
		// get each entity
		final IEntity downstreamEntity = entityProvider.getEntityForSlot(currentAllocation.getDischargeOption());
		final IEntity upstreamEntity = entityProvider.getEntityForSlot(currentAllocation.getLoadOption());

		final int dischargePricePerM3 = currentAllocation.getDischargeM3Price();
		final int cvValue = currentAllocation.getLoadOption().getCargoCVValue();
		final long dischargeVolume = currentAllocation.getDischargeVolume();
		final long loadVolume = currentAllocation.getLoadVolume();
		final int loadPricePerM3 = currentAllocation.getLoadM3Price();

		// TODO should we be thinking in $/m3 or /mmbtu?
		// TODO regas comes in here.
		// TODO inter-entity taxation will also come in here.

		final int shippingTransferPricePerM3 = downstreamEntity.getDownstreamTransferPrice(dischargePricePerM3, cvValue);
		final long downstreamPaysShipping = Calculator.multiply(shippingTransferPricePerM3, dischargeVolume);
		final long downstreamRevenue = Calculator.multiply(dischargePricePerM3, dischargeVolume);

		final long downstreamTotalPretaxProfit = downstreamRevenue - downstreamPaysShipping;

		final long shippingPaysUpstream = Calculator.multiply(loadPricePerM3, loadVolume);
		final long shippingGasBalance = downstreamPaysShipping - shippingPaysUpstream;
		// now we need the total non-LNG shipping cost for the whole thing, which shipping pays.

		final int upstreamTransferPricePerM3 = upstreamEntity.getUpstreamTransferPrice(loadPricePerM3, cvValue);
		final long upstreamTotalPretaxProfit = Calculator.multiply(upstreamTransferPricePerM3, loadVolume);

		final long shippingCosts = getCosts(plan, vessel, false);

		final long shippingTotalPretaxProfit = shippingGasBalance - shippingCosts;

		final int taxTime = currentAllocation.getDischargeTime();
		final long upstreamProfit = upstreamEntity.getTaxedProfit(upstreamTotalPretaxProfit, taxTime);
		final long shippingProfit = shippingEntity.getTaxedProfit(shippingTotalPretaxProfit, taxTime);
		final long downstreamProfit = downstreamEntity.getTaxedProfit(downstreamTotalPretaxProfit, taxTime);

		final long result = upstreamProfit + shippingProfit + downstreamProfit;

		// final String key = currentAllocation.getLoadSlot().getId() + "-" + currentAllocation.getDischargeSlot().getId();

		// if (lastCargoValues.containsKey(key)) {
		// final long lastResult = lastCargoValues.get(key);
		// if (lastResult > 0 != result > 0) {
		// System.err.println("Change in: " + key);
		// System.err.println("   Before: " + lastResult + " " + lastCargoDetails.get(key));
		// System.err.println("    After: " + result + " " + currentAllocation + ", ship cost = " + shippingCosts);
		// }
		// }
		//
		// lastCargoDetails.put(key, currentAllocation + ", ship cost = " + shippingCosts);
		// lastCargoValues.put(key, result);

		if (annotatedSolution != null) {
			final LinkedList<IProfitAndLossEntry> entries = new LinkedList<IProfitAndLossEntry>();

			final DetailTree upstreamDetails = new DetailTree();
			final DetailTree shippingDetails = new DetailTree();
			final DetailTree downstreamDetails = new DetailTree();

			upstreamDetails.addChild(new LNGTransferDetailTree("Upstream purchase", loadVolume, upstreamTransferPricePerM3, cvValue));
			final IDetailTree upstreamToShipping = new LNGTransferDetailTree("Shipping to upstream", loadVolume, upstreamTransferPricePerM3, cvValue);
			upstreamDetails.addChild(upstreamToShipping);
			shippingDetails.addChild(upstreamToShipping);
			final IDetailTree shippingToDownstream = new LNGTransferDetailTree("Downstream to shipping", dischargeVolume, shippingTransferPricePerM3, cvValue);
			shippingDetails.addChild(shippingToDownstream);

			shippingDetails.addChild("Shipping Cost", shippingCosts);

			downstreamDetails.addChild(shippingToDownstream);
			downstreamDetails.addChild(new LNGTransferDetailTree("Downstream sale", dischargeVolume, dischargePricePerM3, cvValue));

			entries.add(new ProfitAndLossEntry(upstreamEntity, upstreamProfit, upstreamDetails));
			entries.add(new ProfitAndLossEntry(shippingEntity, shippingProfit, shippingDetails));
			entries.add(new ProfitAndLossEntry(downstreamEntity, downstreamProfit, downstreamDetails));
			// add entry for each entity

			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(currentAllocation.getDischargeTime(), entries);
			final ISequenceElement element = slotProvider.getElement(currentAllocation.getLoadOption());
			annotatedSolution.getElementAnnotations().setAnnotation(element, TradingConstants.AI_profitAndLoss, annotation);

			ILoadSlot loadOption = (ILoadSlot) currentAllocation.getLoadOption();
			loadOption.getLoadPriceCalculator().calculateLoadUnitPrice(loadOption, (IDischargeSlot) currentAllocation.getDischargeOption(), currentAllocation.getLoadTime(), currentAllocation.getDischargeTime(),
					currentAllocation.getDischargeM3Price(), (int)loadVolume, vessel, plan, shippingDetails);

		}

		return result;
	}

	private long getCosts(final VoyagePlan plan, final IVessel vessel, final boolean includeLNG) {
		final long shippingCosts = plan.getTotalRouteCost() + plan.getTotalFuelCost(FuelComponent.Base) + plan.getTotalFuelCost(FuelComponent.Base_Supplemental)
				+ plan.getTotalFuelCost(FuelComponent.Cooldown) + plan.getTotalFuelCost(FuelComponent.IdleBase) + plan.getTotalFuelCost(FuelComponent.IdlePilotLight)
				+ plan.getTotalFuelCost(FuelComponent.PilotLight)
				+ (includeLNG ? plan.getTotalFuelCost(FuelComponent.NBO) + plan.getTotalFuelCost(FuelComponent.IdleNBO) + plan.getTotalFuelCost(FuelComponent.FBO) : 0);

		final int hireRate;
		switch (vessel.getVesselInstanceType()) {
		case SPOT_CHARTER:
			hireRate = vessel.getVesselClass().getHourlyCharterInPrice();
			break;
		case TIME_CHARTER:
			hireRate = vessel.getHourlyCharterOutPrice();
			break;
		default:
			hireRate = 0;
			break;
		}
		final long hireCosts;
		if (hireRate == 0) {
			hireCosts = 0;
		} else {
			final long planDuration = getPartialPlanDuration(plan, 1); // skip off the last port details, as we don't pay for that here.
			hireCosts = hireRate * planDuration;
		}

		return shippingCosts + hireCosts;
	}

	private int getPlanDuration(final VoyagePlan plan) {
		return getPartialPlanDuration(plan, 0);
	}

	private int getPartialPlanDuration(final VoyagePlan plan, final int skip) {
		int planDuration = 0;
		final Object[] sequence = plan.getSequence();
		final int k = sequence.length - skip;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			planDuration += (o instanceof VoyageDetails) ? (((VoyageDetails) o).getIdleTime() + ((VoyageDetails) o).getTravelTime()) : ((PortDetails) o).getVisitDuration();
		}
		return planDuration;
	}

	/**
	 * Evaluate the group value of this plan, which is not carrying a cargo. It may just be a big cost to shipping.
	 * 
	 * TODO implement charter out revenues
	 * 
	 * @param plan
	 * @return
	 */
	private long evaluate(final VoyagePlan plan, final IVessel vessel, final int time, final IAnnotatedSolution annotatedSolution) {
		final long shippingCost = getCosts(plan, vessel, true);
		final long revenue;
		final PortDetails portDetails = (PortDetails) plan.getSequence()[0];
		if (portDetails.getPortSlot().getPortType() == PortType.CharterOut) {
			// in the next line we're doing getPartialPlanDuration(., 2) because there might be a repositioning in the plan
			revenue = vessel.getHourlyCharterOutPrice() * getPartialPlanDuration(plan, 2);
		} else {
			revenue = 0;
		}

		final long value = shippingEntity.getTaxedProfit(revenue - shippingCost, time);
		if (annotatedSolution != null) {
			final DetailTree details = new DetailTree();

			if (revenue > 0) {
				// TODO take out strings.
				details.addChild(new DetailTree("Revenue", revenue));
			}

			details.addChild(new DetailTree("Shipping Cost", shippingCost));

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, value, details);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(time, Collections.singleton(entry));
			final ISequenceElement element = slotProvider.getElement(((PortDetails) plan.getSequence()[0]).getPortSlot());
			annotatedSolution.getElementAnnotations().setAnnotation(element, TradingConstants.AI_profitAndLoss, annotation);
		}

		return value;
	}

	@Override
	public void annotate(final ScheduledSequences solution, final IAnnotatedSolution annotatedSolution) {
		if (entityProvider == null) {
			return;
		}
		evaluateAndMaybeAnnotate(solution, solution.getAllocations(), annotatedSolution);
	}

}
