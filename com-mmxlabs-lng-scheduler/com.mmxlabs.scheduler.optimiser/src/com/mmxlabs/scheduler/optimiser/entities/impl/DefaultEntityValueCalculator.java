/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarketOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * For each VoyagePlan in the schedule the evaluate(...) methods work out: - discharge and purchase prices for the VP, then the - "additionalPnL" (e.g. upside calculations). - calculate shipping cost
 * (from VP contents) - calculates charter-out P&L (from VP contents)
 * 
 * Two evaluate methods cover cargo and non-cargo VoyagePlans.
 * 
 * @author proshun
 */
public class DefaultEntityValueCalculator implements IEntityValueCalculator {

	private static final boolean includeTimeCharterInFitness = true;

	@Inject
	private IEntityProvider entityProvider;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private ShippingCostHelper shippingCostHelper;

	protected class CargoPNLData {
		public ISequenceElement exportElement;
		public List<IPortSlot> slots;
		public final int[] slotPricePerMMBTu;
		public final long[] slotVolumeInM3;
		public final long[] slotVolumeInMMBTu;
		public final long[] slotAdditionalPNL;
		public final IEntity[] slotEntity;
		public final int[] arrivalTimes;

		public CargoPNLData(final List<IPortSlot> slots) {
			this.slots = new ArrayList<IPortSlot>(slots);
			this.slotPricePerMMBTu = new int[slots.size()];
			this.slotVolumeInM3 = new long[slots.size()];
			this.slotVolumeInMMBTu = new long[slots.size()];
			this.slotAdditionalPNL = new long[slots.size()];
			this.slotEntity = new IEntity[slots.size()];
			this.arrivalTimes = new int[slots.size()];
		}
	}

	/**
	 * Evaluate the group value of the given cargo. This method first calculates sales prices, then purchase prices and additional P&L, then shipping costs and charter out revenue.
	 * 
	 * @param plan
	 * @param currentAllocation
	 * @return
	 */
	@Override
	public long evaluate(final VoyagePlan plan, final IAllocationAnnotation currentAllocation, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution) {

		final List<IPortSlot> slots = currentAllocation.getSlots();

		final CargoPNLData cargoPNLData = new CargoPNLData(slots);
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

		final Set<IEntity> seenEntities = new LinkedHashSet<>();
		// A map of Entity to specific custom property trees.
		final Map<IEntityBook, IDetailTree> entityDetailsMap = (exportElement == null) ? null : new HashMap<IEntityBook, IDetailTree>();

		// Calculate sales/discharge prices
		int idx = 0;
		int taxTime = 0;
		for (final IPortSlot slot : slots) {

			if (slot instanceof IDischargeOption) {
				IEntity entity = entityProvider.getEntityForSlot(slot);
				if (slot instanceof IMarkToMarketOption) {
					final IMarkToMarketOption mtmSlot = (IMarkToMarketOption) slot;
					entity = mtmSlot.getMarkToMarket().getEntity();
				}
				assert entity != null;
				cargoPNLData.slotEntity[idx] = entity;
				seenEntities.add(entity);

				final IDetailTree entityBookDetails = (exportElement == null) ? null : getEntityBookDetails(entityDetailsMap, entity.getTradingBook());

				// Special case! May not be correct for LLD case
				final ILoadOption loadOption = (ILoadOption) slots.get(0);

				final IDischargeOption dischargeOption = (IDischargeOption) slot;
				cargoPNLData.slotPricePerMMBTu[idx] = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(loadOption, dischargeOption, currentAllocation.getSlotTime(loadOption),
						currentAllocation.getSlotTime(dischargeOption), currentAllocation.getSlotVolumeInMMBTu(slot), entityBookDetails);

				// Tmp hack until we sort out the API around this - AllocationAnnotation is an input to this method!
				((AllocationAnnotation) currentAllocation).setSlotPricePerMMBTu(slot, cargoPNLData.slotPricePerMMBTu[idx]);
			}
			// Last discharge is tax time;
			taxTime = cargoPNLData.arrivalTimes[idx] = currentAllocation.getSlotTime(slot);
			cargoPNLData.slotVolumeInM3[idx] = currentAllocation.getSlotVolumeInM3(slot);
			cargoPNLData.slotVolumeInMMBTu[idx] = currentAllocation.getSlotVolumeInMMBTu(slot);
			idx++;
		}

		// The first load entity
		IEntity baseEntity = null;
		// calculate load prices
		idx = 0;
		for (final IPortSlot slot : slots) {
			if (slot instanceof ILoadOption) {

				IEntity entity = entityProvider.getEntityForSlot(slot);
				if (slot instanceof IMarkToMarketOption) {
					final IMarkToMarketOption mtmSlot = (IMarkToMarketOption) slot;
					entity = mtmSlot.getMarkToMarket().getEntity();
				}
				assert entity != null;
				cargoPNLData.slotEntity[idx] = entity;
				seenEntities.add(entity);

				// First load slot is the base entity
				if (baseEntity == null) {
					baseEntity = entity;
				}

				final IDetailTree entityBookDetails = entityDetailsMap == null ? null : getEntityBookDetails(entityDetailsMap, entity.getTradingBook());
				final ILoadOption loadOption = (ILoadOption) slot;

				final long additionProfitAndLoss;
				final int pricePerMMBTu;
				// if (annotatedSolution != null && exportElement != null) {
				{
					// Hardcoded LD cargo P&L
					// TODO: Change API's to match additional P&L
					final IDischargeOption dischargeOption = (IDischargeOption) slots.get(1);
					final int loadTime = cargoPNLData.arrivalTimes[0];
					final int dischargeTime = cargoPNLData.arrivalTimes[1];
					final int dischargePricePerMMBTu = cargoPNLData.slotPricePerMMBTu[1];
					final long loadVolumeInM3 = cargoPNLData.slotVolumeInM3[0];
					final long dischargeVolumeInM3 = cargoPNLData.slotVolumeInM3[1];

					final int transferTime = dischargeTime;
					final long transferVolumeInM3 = dischargeVolumeInM3;
					// FOB Purchase to DES Sale
					if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
						final ILoadSlot loadSlot = (ILoadSlot) loadOption;
						final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeOption;
						pricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateFOBPricePerMMBTu(loadSlot, dischargeSlot, loadTime, dischargeTime, dischargePricePerMMBTu, loadVolumeInM3,
								dischargeVolumeInM3, vessel, vesselStartTime, plan, entityBookDetails);
					} else if (loadOption instanceof ILoadSlot) {
						// FOB Sale
						pricePerMMBTu = loadOption.getLoadPriceCalculator().calculatePriceForFOBSalePerMMBTu((ILoadSlot) loadOption, dischargeOption, transferTime, dischargePricePerMMBTu,
								transferVolumeInM3, entityBookDetails);
					} else {
						// DES Purchase
						assert dischargeOption instanceof IDischargeSlot;
						pricePerMMBTu = loadOption.getLoadPriceCalculator().calculateDESPurchasePricePerMMBTu(loadOption, (IDischargeSlot) dischargeOption, transferTime, dischargePricePerMMBTu,
								transferVolumeInM3, entityBookDetails);
					}
				}
				cargoPNLData.slotPricePerMMBTu[idx] = pricePerMMBTu;
				// TODO: Split into a third loop
				{
					additionProfitAndLoss = loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, slots, cargoPNLData.arrivalTimes, cargoPNLData.slotVolumeInM3,
							cargoPNLData.slotPricePerMMBTu, vessel, vesselStartTime, plan, entityBookDetails);
					cargoPNLData.slotAdditionalPNL[idx] = additionProfitAndLoss;
				}
				// Tmp hack until we sort out the API around this - AllocationAnnotation is an input to this method!
				((AllocationAnnotation) currentAllocation).setSlotPricePerMMBTu(slot, pricePerMMBTu);

			}
			idx++;
		}

		// Calculate transfer pricing etc between entities
		final Map<IEntityBook, Long> entityPreTaxProfit = new HashMap<>();
		evaluateCargoPNL(cargoPNLData, baseEntity, entityPreTaxProfit);

		// Shipping Entity for non-cargo costings
		IEntity shippingEntity = entityProvider.getEntityForVessel(vessel);
		if (shippingEntity == null) {
			shippingEntity = baseEntity;
		}

		// Calculate the value for the fitness function
		assert baseEntity != null;
		assert shippingEntity != null;
		{
			calculateShippingEntityCosts(entityPreTaxProfit, vessel, plan, baseEntity.getTradingBook(), shippingEntity.getShippingBook(), false);
		}
		long result = 0l;
		{
			final long generatedCharterOutRevenue = shippingCostHelper.getGeneratedCharterOutRevenue(plan, vessel);
			addEntityPreTaxProfit(entityPreTaxProfit, shippingEntity.getShippingBook(), generatedCharterOutRevenue);

			final long generatedCharterOutCosts = shippingCostHelper.getGeneratedCharterOutCosts(plan);
			addEntityPreTaxProfit(entityPreTaxProfit, shippingEntity.getShippingBook(), -generatedCharterOutCosts);

			if (annotatedSolution != null && exportElement != null) {
				// Calculate P&L with TC rates
				generateCharterOutAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, taxTime, generatedCharterOutRevenue, firstSlot, true);
				// Calculate P&L without TC rates
				generateCharterOutAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, taxTime, generatedCharterOutRevenue, firstSlot, false);
			}
		}

		// Taxed P&L
		for (final Map.Entry<IEntityBook, Long> e : entityPreTaxProfit.entrySet()) {
			result += e.getKey().getTaxedProfit(e.getValue(), taxTime);
		}

		if (false) {

			if (currentAllocation.getSlots().size() > 2)
				throw new IllegalStateException("Only L-D cargoes are supported for calculation of working capital");

			long totalCostOfWorkingCapital = 0;
			long workingCapital = 0;
			final Object[] sequence = plan.getSequence();
			final int k = sequence.length;
			final int rate = OptimiserUnitConvertor.convertToInternalConversionFactor(0.15);
			for (int i = 0; i < k; i++) {
				final Object o = sequence[i];
				if (o instanceof PortDetails) {
					final PortOptions po = ((PortDetails) o).getOptions();
					final IPortSlot ps = po.getPortSlot();
					if (ps.getPortType() == PortType.Load) {

						final int price = currentAllocation.getSlotPricePerMMBTu(ps);
						final long vol = currentAllocation.getSlotVolumeInMMBTu(ps);
						workingCapital += price * vol;
						// Calculator.convert...; ??
					} else if (ps.getPortType() == PortType.Discharge) {

						totalCostOfWorkingCapital += Calculator.costFromConsumption(workingCapital * (long) po.getVisitDuration(), rate) / (365l * 24l);
						// reset WC contribution - decrement for LDD or LLD cargoes!!
						workingCapital = 0;
						// int price = currentAllocation.getSlotPricePerMMBTu(ps);
						// long vol = currentAllocation.getSlotVolumeInMMBTu(ps);
						// workingCapital -= (price * vol);
						// workingCapital = (workingCapital<0) ? 0 : workingCapital;
					}
				} else {
					final VoyageDetails voyageDetails = (VoyageDetails) o;
					if (voyageDetails.getOptions().getVesselState() == VesselState.Laden) {
						totalCostOfWorkingCapital += Calculator.costFromConsumption(workingCapital * (long) (voyageDetails.getTravelTime() + voyageDetails.getIdleTime()), rate) / (365l * 24l);
					}
				}
			}
			// TODO: Post tax!
			// addEntityProfit(entityProfit, shippingEntity, -generatedCharterOutCosts);

			if (annotatedSolution != null && exportElement != null) {
				// Calculate P&L with TC rates
				generateCharterOutAnnotations(plan, vessel, vesselStartTime, annotatedSolution, shippingEntity, taxTime, workingCapital, firstSlot, true);
			}

			result += workingCapital;
		}

		// Solution Export branch - should called infrequently
		if (annotatedSolution != null && exportElement != null && entityDetailsMap != null) {
			{
				for (final boolean includeTimeCharterRates : new boolean[] { true, false }) {
					final List<IProfitAndLossEntry> entries = new LinkedList<>();
					for (final IEntity entity : seenEntities) {
						{
							final Long profit = entityPreTaxProfit.get(entity.getShippingBook());
							if (profit != null) {
								final IDetailTree entityDetails = entityDetailsMap.get(entity.getShippingBook());
								final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity.getShippingBook(), entity.getShippingBook().getTaxedProfit(profit, taxTime), profit, entityDetails);
								entries.add(entry);
							}
						}
						{
							final Long profit = entityPreTaxProfit.get(entity.getTradingBook());
							if (profit != null) {
								final IDetailTree entityDetails = entityDetailsMap.get(entity.getTradingBook());
								final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity.getTradingBook(), entity.getTradingBook().getTaxedProfit(profit, taxTime), profit, entityDetails);
								entries.add(entry);
							}
						}
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
	 * Overridable method to perform transfer pricing between entities for a Cargo
	 * 
	 * @param cargoPNLData
	 * @param baseEntity
	 * @param entityProfit
	 */
	protected void evaluateCargoPNL(final CargoPNLData cargoPNLData, final IEntity baseEntity, final Map<IEntityBook, Long> entityPreTaxProfit) {

		int idx = 0;
		for (final IPortSlot slot : cargoPNLData.slots) {

			final IEntity entity = cargoPNLData.slotEntity[idx];
			assert entity != null;

			final long value = Calculator.costFromConsumption(cargoPNLData.slotVolumeInMMBTu[idx], cargoPNLData.slotPricePerMMBTu[idx]);
			if (slot instanceof ILoadOption) {

				// Sum up entity p&L
				addEntityPreTaxProfit(entityPreTaxProfit, entity.getTradingBook(), -value);
				addEntityPreTaxProfit(entityPreTaxProfit, entity.getTradingBook(), cargoPNLData.slotAdditionalPNL[idx]);

			} else if (slot instanceof IDischargeOption) {

				// Buy/Sell at same quantity.
				// TODO: Transfer price
				addEntityPreTaxProfit(entityPreTaxProfit, entity.getTradingBook(), -value);
				addEntityPreTaxProfit(entityPreTaxProfit, entity.getTradingBook(), value);

				// Base entity gets the profit
				assert baseEntity != null;
				addEntityPreTaxProfit(entityPreTaxProfit, baseEntity.getTradingBook(), value);
			}

			idx++;
		}
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
		final IEntity shippingEntity = entityProvider.getEntityForVessel(vessel);
		// if (shippingEntity == null) {
		// shippingEntity = baseEntity;
		// }

		final long value;
		final long revenue;
		final long generatedCharterOutRevenue;
		final long generatedCharterOutCost;
		{
			final long shippingCost = shippingCostHelper.getShippingCosts(plan, vessel, true, includeTimeCharterInFitness);
			final PortDetails portDetails = (PortDetails) plan.getSequence()[0];
			if (portDetails.getOptions().getPortSlot().getPortType() == PortType.CharterOut) {
				final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) portDetails.getOptions().getPortSlot();
				revenue = vesselEventPortSlot.getVesselEvent().getHireCost() + vesselEventPortSlot.getVesselEvent().getRepositioning();
			} else {
				revenue = 0;
			}

			generatedCharterOutRevenue = shippingCostHelper.getGeneratedCharterOutRevenue(plan, vessel);
			generatedCharterOutCost = shippingCostHelper.getGeneratedCharterOutCosts(plan);

			// Calculate the value for the fitness function
			final IEntityBook shippingBook = shippingEntity.getShippingBook();
			value = shippingBook.getTaxedProfit(revenue + generatedCharterOutRevenue - generatedCharterOutCost - shippingCost, planStartTime);
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

	private void generateShippingAnnotations(final VoyagePlan plan, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution, final IEntity shippingEntity,
			final long revenue, final long cost, final int taxTime, final ISequenceElement exportElement, final boolean includeLNG, final boolean includeTimeCharterRates) {
		{
			final long shippingCosts = shippingCostHelper.getShippingCosts(plan, vessel, includeLNG, includeTimeCharterRates);
			final long shippingTotalPretaxProfit = revenue /* +additionProfitAndLoss */- cost - shippingCosts;
			final long shippingProfit = shippingEntity.getShippingBook().getTaxedProfit(shippingTotalPretaxProfit, taxTime);

			final DetailTree shippingDetails = new DetailTree();

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity.getShippingBook(), shippingProfit, shippingTotalPretaxProfit, shippingDetails);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			final String label = includeTimeCharterRates ? SchedulerConstants.AI_profitAndLoss : SchedulerConstants.AI_profitAndLossNoTimeCharterRate;
			annotatedSolution.getElementAnnotations().setAnnotation(exportElement, label, annotation);
		}
	}

	private void generateCharterOutAnnotations(final VoyagePlan plan, final IVessel vessel, final int vesselStartTime, final IAnnotatedSolution annotatedSolution, final IEntity shippingEntity,
			final int taxTime, final long generatedCharterOutRevenue, final IPortSlot firstSlot, final boolean includeTimeCharterRates) {
		if (generatedCharterOutRevenue != 0) {

			final long charterOutCosts = shippingCostHelper.getGeneratedCharterOutCosts(plan);
			final long charterOutPretaxProfit = generatedCharterOutRevenue - charterOutCosts;
			final long charterOutProfit = shippingEntity.getShippingBook().getTaxedProfit(charterOutPretaxProfit, taxTime);

			final DetailTree details = new DetailTree();

			details.addChild(new DetailTree("Charter Out", new TotalCostDetailElement(generatedCharterOutRevenue)));

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity.getShippingBook(), charterOutProfit, charterOutCosts, details);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			final ISequenceElement element = slotProvider.getElement(firstSlot);
			final String label = includeTimeCharterRates ? SchedulerConstants.AI_charterOutProfitAndLoss : SchedulerConstants.AI_charterOutProfitAndLossNoTimeCharterRate;
			annotatedSolution.getElementAnnotations().setAnnotation(element, label, annotation);
		}
	}

	private void addEntityPreTaxProfit(@NonNull final Map<IEntityBook, Long> entityProfit, @NonNull final IEntityBook entity, final long profit) {
		long totalProfit = profit;
		if (entityProfit.containsKey(entity)) {
			final Long existingProfit = entityProfit.get(entity);
			if (existingProfit != null) {
				totalProfit += existingProfit.longValue();
			}
		}
		entityProfit.put(entity, totalProfit);
	}

	private IDetailTree getEntityBookDetails(final Map<IEntityBook, IDetailTree> entityDetailsMap, final IEntityBook entityBook) {
		if (entityDetailsMap.containsKey(entityBook)) {
			return entityDetailsMap.get(entityBook);
		} else {
			final DetailTree tree = new DetailTree();
			entityDetailsMap.put(entityBook, tree);
			return tree;
		}
	}

	protected void calculateShippingEntityCosts(@NonNull final Map<IEntityBook, Long> entityPreTaxProfit, final IVessel vessel, final VoyagePlan plan, final IEntityBook costBook,
			final IEntityBook shippingBook, final boolean includeLNG) {

		if (vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return;
		}

		final long shippingCosts = shippingCostHelper.getRouteExtraCosts(plan) + shippingCostHelper.getFuelCosts(plan, includeLNG);
		final long portCosts = shippingCostHelper.getPortCosts(vessel, plan);
		final long hireCosts = shippingCostHelper.getHireCosts(plan);

		long transferPrice = 0;
		if (costBook != shippingBook) {
			// TODO:
			// transferPrice = hireCosts / 2;
		}

		final long totalShippingCosts = shippingCosts + portCosts;
		// addEntityPreTaxProfit(entityPreTaxProfit, costBook, -totalShippingCosts);
		// addEntityPreTaxProfit(entityPreTaxProfit, costBook, -transferPrice);
		// addEntityPreTaxProfit(entityPreTaxProfit, shippingBook, +transferPrice);
		// addEntityPreTaxProfit(entityPreTaxProfit, shippingBook, -hireCosts);

		addEntityPreTaxProfit(entityPreTaxProfit, costBook, -totalShippingCosts);
		addEntityPreTaxProfit(entityPreTaxProfit, costBook, -hireCosts);

	}
}
