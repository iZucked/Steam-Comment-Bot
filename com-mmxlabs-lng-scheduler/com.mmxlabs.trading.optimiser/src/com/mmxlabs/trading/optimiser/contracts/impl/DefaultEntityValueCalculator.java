package com.mmxlabs.trading.optimiser.contracts.impl;

import java.util.Collections;
import java.util.LinkedList;

import javax.inject.Inject;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.CurrencyDetailElement;
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
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IEntity;
import com.mmxlabs.scheduler.optimiser.contracts.IEntityValueCalculator;
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

/**
 * @since 2.0
 */
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
		final IDischargeOption dischargeOption = currentAllocation.getDischargeOption();
		final IEntity downstreamEntity = entityProvider.getEntityForSlot(dischargeOption);
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

		final int upstreamTransferPricePerM3 = upstreamEntity.getUpstreamTransferPrice(loadPricePerM3, cvValue);
		final long shippingPaysUpstream = Calculator.multiply(upstreamTransferPricePerM3, loadVolume);
		final long shippingGasBalance = downstreamPaysShipping - shippingPaysUpstream;
		// now we need the total non-LNG shipping cost for the whole thing, which shipping pays.

		final long upstreamRevenue = -Calculator.multiply(loadPricePerM3, loadVolume);
		final long upstreamTotalPretaxProfit = upstreamRevenue + shippingPaysUpstream;

		final long shippingCosts = getCosts(plan, vessel, false, vesselStartTime, null);

		final long shippingTotalPretaxProfit = shippingGasBalance - shippingCosts;

		final int taxTime = currentAllocation.getDischargeTime();
		final long upstreamProfit = upstreamEntity.getTaxedProfit(upstreamTotalPretaxProfit, taxTime);
		final long shippingProfit = shippingEntity.getTaxedProfit(shippingTotalPretaxProfit, taxTime);
		final long downstreamProfit = downstreamEntity.getTaxedProfit(downstreamTotalPretaxProfit, taxTime);

		final long charterRevenue = getCharterRevenue(plan, vessel);
		final long taxedCharterRevenue = shippingEntity.getTaxedProfit(charterRevenue, taxTime);

		final long result = upstreamProfit + shippingProfit + downstreamProfit + taxedCharterRevenue;

		if (annotatedSolution != null) {
			{
				final ISequenceElement element = slotProvider.getElement(currentAllocation.getLoadOption());

				final LinkedList<IProfitAndLossEntry> entries = new LinkedList<IProfitAndLossEntry>();

				final DetailTree upstreamDetails = new DetailTree();
				final DetailTree shippingDetails = new DetailTree();
				final DetailTree downstreamDetails = new DetailTree();

				upstreamDetails.addChild(new LNGTransferDetailTree("Upstream purchase", loadVolume, loadPricePerM3, cvValue));
				final IDetailTree upstreamToShipping = new LNGTransferDetailTree("Shipping to upstream", loadVolume, upstreamTransferPricePerM3, cvValue);
				upstreamDetails.addChild(upstreamToShipping);
				shippingDetails.addChild(upstreamToShipping);
				final IDetailTree shippingToDownstream = new LNGTransferDetailTree("Downstream to shipping", dischargeVolume, shippingTransferPricePerM3, cvValue);
				shippingDetails.addChild(shippingToDownstream);

				final IDetailTree[] detailsRef = new IDetailTree[1];
				// final long costNoBoiloff = getCosts(plan, vessel, false, vesselStartTime, detailsRef);
				// annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCost,
				// new ShippingCostAnnotation(currentAllocation.getLoadTime(), costNoBoiloff, detailsRef[0]));
				final long costIncBoiloff = getCosts(plan, vessel, true, vesselStartTime, detailsRef);
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCostWithBoilOff,
						new ShippingCostAnnotation(currentAllocation.getLoadTime(), costIncBoiloff, detailsRef[0]));

				downstreamDetails.addChild(shippingToDownstream);
				downstreamDetails.addChild(new LNGTransferDetailTree("Downstream sale", dischargeVolume, dischargePricePerM3, cvValue));

				entries.add(new ProfitAndLossEntry(upstreamEntity, upstreamProfit, upstreamDetails));
				entries.add(new ProfitAndLossEntry(shippingEntity, shippingProfit, shippingDetails));
				entries.add(new ProfitAndLossEntry(downstreamEntity, downstreamProfit, downstreamDetails));
				// add entry for each entity

				final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(currentAllocation.getDischargeTime(), entries);
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_profitAndLoss, annotation);

				final ILoadOption loadOption = currentAllocation.getLoadOption();
				if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
					loadOption.getLoadPriceCalculator().calculateLoadUnitPrice((ILoadSlot) loadOption, (IDischargeSlot) dischargeOption, currentAllocation.getLoadTime(),
							currentAllocation.getDischargeTime(), currentAllocation.getDischargeM3Price(), (int) loadVolume, vessel, plan, shippingDetails);
				} else {
					loadOption.getLoadPriceCalculator().calculateLoadUnitPrice(loadOption, dischargeOption, currentAllocation.getLoadTime(), currentAllocation.getDischargeTime(),
							currentAllocation.getDischargeM3Price(), shippingDetails);
				}
			}

			// Add in charter out revenue
			if (charterRevenue != 0) {
				final DetailTree details = new DetailTree();

				details.addChild(new DetailTree("Charter Out", new CurrencyDetailElement(charterRevenue)));

				final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, taxedCharterRevenue, details);
				final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(currentAllocation.getLoadTime(), Collections.singleton(entry));
				final ISequenceElement element = slotProvider.getElement(currentAllocation.getLoadOption());
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
	public long evaluate(final VoyagePlan plan, final IVessel vessel, final int planStartTime, final int vesselStartTime, final IAnnotatedSolution annotatedSolution) {
		final IEntity shippingEntity = entityProvider.getShippingEntity();

		final long shippingCost = getCosts(plan, vessel, true, vesselStartTime, null);
		final long revenue;
		final PortDetails portDetails = (PortDetails) plan.getSequence()[0];
		if (portDetails.getPortSlot().getPortType() == PortType.CharterOut) {
			final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) portDetails.getPortSlot();
			revenue = vesselEventPortSlot.getVesselEvent().getHireCost() + vesselEventPortSlot.getVesselEvent().getRepositioning();
		} else {
			revenue = 0;
		}

		final long value = shippingEntity.getTaxedProfit(revenue - shippingCost, planStartTime);
		if (annotatedSolution != null) {
			final ISequenceElement element = slotProvider.getElement(((PortDetails) plan.getSequence()[0]).getPortSlot());
			final DetailTree details = new DetailTree();

			if (revenue > 0) {
				// TODO take out strings.
				details.addChild(new DetailTree("Revenue", new CurrencyDetailElement(revenue)));
			}

			final IDetailTree[] detailsRef = new IDetailTree[1];
			// final long costNoBoiloff = getCosts(plan, vessel, false, vesselStartTime, detailsRef);
			// annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCost, new ShippingCostAnnotation(planStartTime, costNoBoiloff, detailsRef[0]));
			final long costIncBoiloff = getCosts(plan, vessel, true, vesselStartTime, detailsRef);
			annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_shippingCostWithBoilOff, new ShippingCostAnnotation(planStartTime, costIncBoiloff, detailsRef[0]));

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, value, details);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(planStartTime, Collections.singleton(entry));
			annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_profitAndLoss, annotation);

		}

		return value;
	}

	private long getCosts(final VoyagePlan plan, final IVessel vessel, final boolean includeLNG, final int vesselStartTime, final IDetailTree[] detailsRef) {
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

				final IPort port = portDetails.getPortSlot().getPort();
				final PortType portType = portDetails.getPortSlot().getPortType();
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
		final long hireCosts;
		if (hireRate == null) {
			hireCosts = 0;
		} else {
			final long planDuration = getPartialPlanDuration(plan, 1); // skip off the last port details, as we don't pay for that here.
			final long hourlyCharterInPrice = (int) hireRate.getValueAtPoint(vesselStartTime);
			hireCosts = hourlyCharterInPrice * (long) planDuration;
		}
		if (detailsRef != null) {
			//
			final DetailTree details = new DetailTree("Shipping Costs", new CurrencyDetailElement(shippingCosts + hireCosts + portCosts));
			//
			// details.addChild("Route Cost", new CurrencyDetailElement(plan.getTotalRouteCost()));
			// details.addChild("Base Fuel", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.Base)));
			// details.addChild("Base Fuel Supplement", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.Base_Supplemental)));
			// details.addChild("Cooldown", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.Cooldown)));
			// details.addChild("Idle Base", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.IdleBase)));
			// details.addChild("Pilot Light", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.PilotLight)));
			// details.addChild("Idle Pilot Light", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.IdlePilotLight)));
			// if (includeLNG) {
			// details.addChild("NBO", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.NBO)));
			// details.addChild("FBO", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.FBO)));
			// details.addChild("Idle NBO", new CurrencyDetailElement(plan.getTotalFuelCost(FuelComponent.IdleNBO)));
			// }
			// details.addChild("Hire Costs", new CurrencyDetailElement(hireCosts));
			// details.addChild("Port Costs", new CurrencyDetailElement(portCosts));
			//
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
					charterRevenue += hourlyCharterOutPrice * (long) voyageDetails.getIdleTime();
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
				planDuration += ((PortDetails) o).getVisitDuration();
			}
		}
		return planDuration;
	}

}
