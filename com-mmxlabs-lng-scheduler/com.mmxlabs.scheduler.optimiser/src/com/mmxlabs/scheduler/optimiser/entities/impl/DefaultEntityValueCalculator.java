/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.DurationDetailElement;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
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

	/**
	 * evaluate the group value of the given cargo
	 * 
	 * @param plan
	 * @param currentAllocation
	 * @return
	 */
	@Override
	public long evaluate(final VoyagePlan plan, final IAllocationAnnotation currentAllocation, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution) {

		final List<IPortSlot> slots = currentAllocation.getSlots();

		// Note: Solution Export branch - should called infrequently

		// Find elements to attach export data to.
		ISequenceElement exportElement = null;
		final IPortSlot firstSlot = slots.get(0);
		if (annotatedSolution != null) {
			// Try and find a real element to attach P&L data to
			for (final IPortSlot pSlot : slots) {
				exportElement = slotProvider.getElement(pSlot);
				if (exportElement != null) {
					break;
				}
			}
		}

		int taxTime = -1;
		final int[] dischargePricesPerMMBTu = new int[slots.size()];
		final long[] slotVolumesInM3 = new long[slots.size()];
		final int[] arrivalTimes = new int[slots.size()];
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

		final Set<IEntity> seenEntities = new LinkedHashSet<>();

		final Map<IEntity, IDetailTree> entityDetailsMap = (exportElement == null) ? null : new HashMap<IEntity, IDetailTree>();

		final Map<IEntity, Long> entityProfit = new HashMap<>();
		// Entity additional costs are assigned to - typically the load slot
		IEntity baseEntity = null;
		for (final IPortSlot slot : slots) {

			// Determined by volume allocator
			final long volumeInM3 = currentAllocation.getSlotVolumeInM3(slot);
			final int pricePerM3 = currentAllocation.getSlotPricePerM3(slot);
			final int time = currentAllocation.getSlotTime(slot);
			// Use the last slot to set the time for tax purposes. TODO: This is valid for LD cases only
			taxTime = time;

			final long value = Calculator.convertM3ToM3Price(volumeInM3, pricePerM3);
			final IEntity entity = entityProvider.getEntityForSlot(slot);
			seenEntities.add(entity);
			assert entity != null;

			if (slot instanceof ILoadOption) {
				final ILoadOption loadOption = (ILoadOption) slot;

				final long additionProfitAndLoss;
				if (annotatedSolution != null && exportElement != null) {

					final IDetailTree entityDetails = getEntityDetails(entityDetailsMap, entity);
					{
						// Hardcoded LD cargo P&L
						// TODO: Change API's to match additional P&L
						final IDischargeOption dischargeOption = (IDischargeOption) slots.get(1);
						final int loadTime = arrivalTimes[0];
						final int dischargeTime = arrivalTimes[1];
						final int dischargePricePerMMBTu = dischargePricesPerMMBTu[1];
						final long loadVolumeInM3 = slotVolumesInM3[0];
						final long dischargeVolumeInM3 = slotVolumesInM3[1];

						final int transferTime = dischargeTime;
						final long transferVolumeInM3 = dischargeVolumeInM3;
						if (loadOption instanceof ILoadSlot) {
							final ILoadSlot loadSlot = (ILoadSlot) loadOption;
							loadSlot.getLoadPriceCalculator().calculateFOBPricePerMMBTu(loadSlot, (IDischargeSlot) dischargeOption, loadTime, dischargeTime, dischargePricePerMMBTu, loadVolumeInM3,
									dischargeVolumeInM3, vessel, plan, entityDetails);
						} else {
							loadOption.getLoadPriceCalculator().calculateLoadPricePerMMBTu(loadOption, dischargeOption, transferTime, dischargePricePerMMBTu, transferVolumeInM3, entityDetails);
						}
					}
					{
						additionProfitAndLoss = loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, slots, arrivalTimes, slotVolumesInM3, dischargePricesPerMMBTu, vessel,
								plan, entityDetails);
					}

				} else {
					additionProfitAndLoss = loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, slots, arrivalTimes, slotVolumesInM3, dischargePricesPerMMBTu, vessel,
							plan, null);
				}

				// Sum up entity p&L
				addEntityProfit(entityProfit, entity, -value);
				addEntityProfit(entityProfit, entity, additionProfitAndLoss);

				// First load slot is the base entity
				if (baseEntity == null) {
					baseEntity = entity;
				}
			} else if (slot instanceof IDischargeOption) {
				final IDischargeOption dischargeOption = (IDischargeOption) slot;
				// Buy/Sell at same quantity.
				// TODO: Transfer price
				addEntityProfit(entityProfit, entity, -value);
				addEntityProfit(entityProfit, entity, value);

				// Base entity gets the profit
				addEntityProfit(entityProfit, baseEntity, value);

				if (annotatedSolution != null && exportElement != null) {
					// TODO: API for this!
					// dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeOption, time, list);
				}

			}
		}
		// Shipping Entity for non-cargo costings
		final IEntity shippingEntity = entityProvider.getShippingEntity();

		// Calculate the value for the fitness function
		assert baseEntity != null;
		// final long result;
		{
			final long shippingCosts = getShippingCosts(plan, vessel, false, includeTimeCharterInFitness, vesselStartTime, null);
			addEntityProfit(entityProfit, baseEntity, -shippingCosts);
		}
		long result = 0l;
		{
			final long generatedCharterOutRevenue = getGeneratedCharterOutRevenue(plan, vessel);
			// addEntityProfit(entityProfit, shippingEntity, generatedCharterOutRevenue);
			final long generatedCharterOutCosts = getGeneratedCharterOutCosts(plan, vessel, includeTimeCharterInFitness, vesselStartTime, null);
			// addEntityProfit(entityProfit, shippingEntity, -generatedCharterOutCosts);

			result += generatedCharterOutRevenue - generatedCharterOutCosts;

			if (annotatedSolution != null && exportElement != null) {
				// Calculate P&L with TC rates
				generateCharterOutAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, taxTime, generatedCharterOutRevenue, firstSlot, true);
				// Calculate P&L without TC rates
				generateCharterOutAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, taxTime, generatedCharterOutRevenue, firstSlot, false);
			}
		}

		// Cargo (not charter out) revenue
		for (final Map.Entry<IEntity, Long> e : entityProfit.entrySet()) {
			result += e.getKey().getTaxedProfit(e.getValue(), taxTime);
		}

		// Solution Export branch - should called infrequently
		if (annotatedSolution != null && exportElement != null) {
			{
				for (boolean includeTimeCharterRates : new boolean[] { true, false }) {
					final List<IProfitAndLossEntry> entries = new LinkedList<>();
					for (final IEntity entity : seenEntities) {
						final Long profit = entityProfit.get(entity);

						final IDetailTree entityDetails = entityDetailsMap.get(entity);

						final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity, entity.getTaxedProfit(profit, taxTime), entityDetails);
						entries.add(entry);

					}
					final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(entries);
					final String label = includeTimeCharterRates ? SchedulerConstants.AI_profitAndLoss : SchedulerConstants.AI_profitAndLossNoTimeCharterRate;
					annotatedSolution.getElementAnnotations().setAnnotation(exportElement, label, annotation);
				}
				// Calculate P&L with TC rates
				// generateCargoAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, cost, additionProfitAndLoss, taxTime, exportElement, false, true,
				// currentAllocation);
				//
				// // Calculate P&L without TC rates
				// generateCargoAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, cost, additionProfitAndLoss, taxTime, exportElement, false, false,
				// currentAllocation);

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
			// Cost is zero as shipping cost is recalculated to obtain annotation

			// Calculate P&L with TC rates
			generateShippingAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, 0, planStartTime, exportElement, true, true);
			generateCharterOutAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, planStartTime, generatedCharterOutRevenue, firstSlot, true);

			// Calculate P&L without TC rates
			generateShippingAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, revenue, 0, planStartTime, exportElement, true, false);
			generateCharterOutAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, planStartTime, generatedCharterOutRevenue, firstSlot, false);

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

	public void generateShippingAnnotations(final VoyagePlan plan, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution, final IEntity shippingEntity,
			final long revenue, final long cost, final int taxTime, final ISequenceElement exportElement, final boolean includeLNG, final boolean includeTimeCharterRates) {
		{
			final long shippingCosts = getShippingCosts(plan, vessel, includeLNG, includeTimeCharterRates, vesselStartTime, null);
			final long shippingTotalPretaxProfit = revenue /* +additionProfitAndLoss */- cost - shippingCosts;
			final long shippingProfit = shippingEntity.getTaxedProfit(shippingTotalPretaxProfit, taxTime);

			final DetailTree shippingDetails = new DetailTree();

			final IDetailTree[] detailsRef = new IDetailTree[1];
			getShippingCosts(plan, vessel, true, true, vesselStartTime, detailsRef);

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, shippingProfit, shippingDetails);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			final String label = includeTimeCharterRates ? SchedulerConstants.AI_profitAndLoss : SchedulerConstants.AI_profitAndLossNoTimeCharterRate;
			annotatedSolution.getElementAnnotations().setAnnotation(exportElement, label, annotation);
		}
	}

	public void generateCargoAnnotations(final VoyagePlan plan, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution, final IEntity shippingEntity,
			final long revenue, final long cost, final long additionProfitAndLoss, final int taxTime, final ISequenceElement exportElement, final boolean includeLNG,
			final boolean includeTimeCharterRates, final IAllocationAnnotation currentAllocation) {
		{

			// Recalculate this data to avoid an even bigger method signature....

			// get each entity
			final List<IPortSlot> slots = currentAllocation.getSlots();

			final int[] dischargePricesPerMMBTu = new int[slots.size()];
			final long[] slotVolumesInM3 = new long[slots.size()];
			final int[] arrivalTimes = new int[slots.size()];
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

			final long shippingCosts = getShippingCosts(plan, vessel, includeLNG, includeTimeCharterRates, vesselStartTime, null);
			final long shippingTotalPretaxProfit = revenue + additionProfitAndLoss - cost - shippingCosts;
			final long shippingProfit = shippingEntity.getTaxedProfit(shippingTotalPretaxProfit, taxTime);

			final DetailTree shippingDetails = new DetailTree();

			final IDetailTree[] detailsRef = new IDetailTree[1];
			getShippingCosts(plan, vessel, true, true, vesselStartTime, detailsRef);

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity, shippingProfit, shippingDetails);

			for (final IPortSlot slot : slots) {

				if (slot instanceof ILoadOption) {
					final ILoadOption loadOption = (ILoadOption) slot;
					loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, slots, arrivalTimes, slotVolumesInM3, dischargePricesPerMMBTu, vessel, plan, entry.getDetails());
				}
			}

			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			final String label = includeTimeCharterRates ? SchedulerConstants.AI_profitAndLoss : SchedulerConstants.AI_profitAndLossNoTimeCharterRate;
			annotatedSolution.getElementAnnotations().setAnnotation(exportElement, label, annotation);
		}
	}

	public void generateCharterOutAnnotations(final VoyagePlan plan, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution, final IEntity shippingEntity,
			final int taxTime, final long generatedCharterOutRevenue, final IPortSlot firstSlot, final boolean includeTimeCharterRates) {
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

	private void addEntityProfit(@NonNull final Map<IEntity, Long> entityProfit, @NonNull final IEntity entity, final long profit) {
		long totalProfit = profit;
		if (entityProfit.containsKey(entity)) {
			final Long existingProfit = entityProfit.get(entity);
			if (existingProfit != null) {
				totalProfit += existingProfit.longValue();
			}
		}
		entityProfit.put(entity, totalProfit);
	}

	private IDetailTree getEntityDetails(final Map<IEntity, IDetailTree> entityDetailsMap, final IEntity entity) {
		if (entityDetailsMap.containsKey(entity)) {
			return entityDetailsMap.get(entity);
		} else {
			final DetailTree tree = new DetailTree();
			entityDetailsMap.put(entity, tree);
			return tree;
		}
	}

}
