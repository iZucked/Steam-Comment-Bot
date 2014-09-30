/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.CancellationAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.HedgingAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarketOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICancellationFeeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IHedgesProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
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

	@Inject
	private IHedgesProvider hedgesProvider;

	@Inject
	private ICancellationFeeProvider cancellationFeeProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private ITimeZoneToUtcOffsetProvider utcOffsetProvider;
	
	/**
	 * Internal data structure to store handy data needed for Cargo P&L calculations. Note similarity to {@link IAllocationAnnotation} - and even {@link AllocationRecord} (which is not visible here).
	 * 
	 */
	protected class CargoPNLData {
		public ISequenceElement exportElement;
		public final List<IPortSlot> slots;
		public final IAllocationAnnotation allocationAnnotation;
		public final int[] slotPricePerMMBTu;
		public final long[] slotVolumeInM3;
		public final long[] slotVolumeInMMBTu;
		public final int[] slotCargoCV;
		public final long[] slotAdditionalPNL;
		public final IEntity[] slotEntity;
		public final int[] arrivalTimes;
		public final int[] visitDurations;

		public CargoPNLData(final IAllocationAnnotation allocationAnnotation) {
			this.allocationAnnotation = allocationAnnotation;
			this.slots = new ArrayList<IPortSlot>(allocationAnnotation.getSlots());
			this.slotPricePerMMBTu = new int[slots.size()];
			this.slotVolumeInM3 = new long[slots.size()];
			this.slotVolumeInMMBTu = new long[slots.size()];
			this.slotCargoCV = new int[slots.size()];
			this.slotAdditionalPNL = new long[slots.size()];
			this.slotEntity = new IEntity[slots.size()];
			this.arrivalTimes = new int[slots.size()];
			this.visitDurations = new int[slots.size()];
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
	public long evaluate(final VoyagePlan plan, final IAllocationAnnotation currentAllocation, final IVesselAvailability vesselAvailability, final int vesselStartTime,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		final List<IPortSlot> slots = currentAllocation.getSlots();

		final CargoPNLData cargoPNLData = new CargoPNLData(currentAllocation);
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
		final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap = (exportElement == null) ? null : new HashMap<IEntityBook, IDetailTree>();
		final Map<IPortSlot, IDetailTree> portSlotDetailTreeMap = (exportElement == null) ? null : new HashMap<IPortSlot, IDetailTree>();

		// Calculate sales/discharge prices
		int idx = 0;
		// Tax time is assumed to by the UTC equivalent of the last discharge time. This may not always be a valid assumption...
		int utcEquivTaxTime = 0;
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

				final IDetailTree portSlotDetails = portSlotDetailTreeMap == null ? null : getPortSlotDetails(portSlotDetailTreeMap, slot);

				final IDischargeOption dischargeOption = (IDischargeOption) slot;
				cargoPNLData.slotPricePerMMBTu[idx] = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeOption, currentAllocation, portSlotDetails);

				// Tmp hack until we sort out the API around this - AllocationAnnotation is an input to this method!
				((AllocationAnnotation) currentAllocation).setSlotPricePerMMBTu(slot, cargoPNLData.slotPricePerMMBTu[idx]);
			}
			// Last discharge is tax time;
			cargoPNLData.arrivalTimes[idx] = currentAllocation.getSlotTime(slot);
			cargoPNLData.visitDurations[idx] = currentAllocation.getSlotDuration(slot);
			cargoPNLData.slotVolumeInM3[idx] = currentAllocation.getSlotVolumeInM3(slot);
			cargoPNLData.slotVolumeInMMBTu[idx] = currentAllocation.getSlotVolumeInMMBTu(slot);
			cargoPNLData.slotCargoCV[idx] = currentAllocation.getSlotCargoCV(slot);

			// Translate into UTC curve
			utcEquivTaxTime = utcOffsetProvider.UTC(currentAllocation.getSlotTime(slot), slot.getPort());
			
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

				final IDetailTree portSlotDetails = portSlotDetailTreeMap == null ? null : getPortSlotDetails(portSlotDetailTreeMap, slot);
				final ILoadOption loadOption = (ILoadOption) slot;

				final int pricePerMMBTu;
				// if (annotatedSolution != null && exportElement != null) {
				{
					// Hardcoded LD cargo P&L
					// TODO: Change API's to match additional P&L
					final IDischargeOption dischargeOption = (IDischargeOption) slots.get(1);
					final int dischargePricePerMMBTu = cargoPNLData.slotPricePerMMBTu[1];

					// FOB Purchase to DES Sale
					if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
						final ILoadSlot loadSlot = (ILoadSlot) loadOption;
						final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeOption;
						pricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateFOBPricePerMMBTu(loadSlot, dischargeSlot, dischargePricePerMMBTu, currentAllocation, vesselAvailability,
								vesselStartTime, plan, portSlotDetails);
					} else if (loadOption instanceof ILoadSlot) {
						// FOB Sale
						pricePerMMBTu = loadOption.getLoadPriceCalculator().calculatePriceForFOBSalePerMMBTu((ILoadSlot) loadOption, dischargeOption, dischargePricePerMMBTu, currentAllocation,
								portSlotDetails);
					} else {
						// DES Purchase
						assert dischargeOption instanceof IDischargeSlot;
						pricePerMMBTu = loadOption.getLoadPriceCalculator().calculateDESPurchasePricePerMMBTu(loadOption, (IDischargeSlot) dischargeOption, dischargePricePerMMBTu, currentAllocation,
								portSlotDetails);
					}
				}
				cargoPNLData.slotPricePerMMBTu[idx] = pricePerMMBTu;

				// Tmp hack until we sort out the API around this - AllocationAnnotation is an input to this method!
				((AllocationAnnotation) currentAllocation).setSlotPricePerMMBTu(slot, pricePerMMBTu);

			}

			// Sanity checks for actuals DCP
			if (actualsDataProvider.hasActuals(slot)) {
				assert cargoPNLData.arrivalTimes[idx] == actualsDataProvider.getArrivalTime(slot);
				assert cargoPNLData.visitDurations[idx] == actualsDataProvider.getVisitDuration(slot);
				assert cargoPNLData.slotCargoCV[idx] == actualsDataProvider.getCVValue(slot);
				assert cargoPNLData.slotVolumeInM3[idx] == actualsDataProvider.getVolumeInM3(slot);
				assert cargoPNLData.slotVolumeInMMBTu[idx] == actualsDataProvider.getVolumeInMMBtu(slot);
				assert cargoPNLData.slotPricePerMMBTu[idx] == actualsDataProvider.getLNGPricePerMMBTu(slot);
			}

			idx++;
		}

		// Calculate additional P&L
		idx = 0;
		for (final IPortSlot slot : slots) {
			if (slot instanceof ILoadOption) {
				final IDetailTree portSlotDetails = portSlotDetailTreeMap == null ? null : getPortSlotDetails(portSlotDetailTreeMap, slot);
				final ILoadOption loadOption = (ILoadOption) slot;

				final long additionProfitAndLoss = loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, currentAllocation, cargoPNLData.slotPricePerMMBTu,
						vesselAvailability, vesselStartTime, plan, portSlotDetails);
				cargoPNLData.slotAdditionalPNL[idx] = additionProfitAndLoss;
			}
			idx++;
		}

		// Calculate transfer pricing etc between entities
		final Map<IEntityBook, Long> entityPreTaxProfit = new HashMap<>();
		evaluateCargoPNL(cargoPNLData, baseEntity, entityPreTaxProfit, annotatedSolution, entityBookDetailTreeMap);

		// Shipping Entity for non-cargo costings - handle any transfer pricing etc required
		IEntity shippingEntity = entityProvider.getEntityForVesselAvailability(vesselAvailability);
		if (shippingEntity == null) {
			shippingEntity = baseEntity;
		}
		seenEntities.add(shippingEntity);

		assert baseEntity != null;
		assert shippingEntity != null;
		{
			calculateShippingEntityCosts(entityPreTaxProfit, vesselAvailability, plan, cargoPNLData, baseEntity, shippingEntity, false, entityBookDetailTreeMap);
		}

		// Calculate the value for the fitness function
		long result = 0L;
		// Taxed P&L
		for (final Map.Entry<IEntityBook, Long> e : entityPreTaxProfit.entrySet()) {
			result += e.getKey().getTaxedProfit(e.getValue(), utcEquivTaxTime);
		}

		final Map<IEntityBook, Long> entityPostTaxProfit = new HashMap<>();
		// Hook for client specific post tax stuff
		calculatePostTaxItems(vesselAvailability, plan, cargoPNLData, entityPostTaxProfit, entityBookDetailTreeMap);

		// Non-Taxed P&L
		for (final Map.Entry<IEntityBook, Long> e : entityPostTaxProfit.entrySet()) {
			result += e.getValue();
		}

		// Solution Export branch - should called infrequently
		if (annotatedSolution != null && exportElement != null && entityBookDetailTreeMap != null && portSlotDetailTreeMap != null) {
			{
				final List<IProfitAndLossEntry> entries = new LinkedList<>();
				for (final IEntity entity : seenEntities) {
					{
						final Long postTaxProfit = entityPostTaxProfit.get(entity.getShippingBook());
						final Long preTaxProfit = entityPreTaxProfit.get(entity.getShippingBook());
						if (preTaxProfit != null || postTaxProfit != null) {

							final long preTaxValue = preTaxProfit == null ? 0 : preTaxProfit.longValue();
							final long postTaxValue = (postTaxProfit == null ? 0 : postTaxProfit.longValue()) + entity.getShippingBook().getTaxedProfit(preTaxValue, utcEquivTaxTime);
							final IDetailTree entityDetails = entityBookDetailTreeMap.get(entity.getShippingBook());
							final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity.getShippingBook(), postTaxValue, preTaxValue, entityDetails);
							entries.add(entry);
						}
					}
					{
						final Long postTaxProfit = entityPostTaxProfit.get(entity.getTradingBook());
						final Long preTaxProfit = entityPreTaxProfit.get(entity.getTradingBook());
						if (preTaxProfit != null || postTaxProfit != null) {
							final long preTaxValue = preTaxProfit == null ? 0 : preTaxProfit.longValue();
							final long postTaxValue = (postTaxProfit == null ? 0 : postTaxProfit.longValue()) + entity.getTradingBook().getTaxedProfit(preTaxValue, utcEquivTaxTime);
							final IDetailTree entityDetails = entityBookDetailTreeMap.get(entity.getTradingBook());
							final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity.getTradingBook(), postTaxValue, preTaxValue, entityDetails);
							entries.add(entry);
						}
					}
				}
				final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(entries);
				annotatedSolution.getElementAnnotations().setAnnotation(exportElement, SchedulerConstants.AI_profitAndLoss, annotation);
			}
			for (final IPortSlot portSlot : slots) {

				final IDetailTree slotDetails = portSlotDetailTreeMap.get(portSlot);
				if (slotDetails != null) {
					final IProfitAndLossSlotDetailsAnnotation annotation = new ProfitAndLossSlotDetailsAnnotation(portSlot, slotDetails);
					annotatedSolution.getElementAnnotations().setAnnotation(slotProvider.getElement(portSlot), SchedulerConstants.AI_profitAndLossSlotDetails, annotation);

				}

			}
		}

		// Finally handle any charter out generation stuff.
		{
			final long generatedCharterOutRevenue = shippingCostHelper.getGeneratedCharterOutRevenue(plan, vesselAvailability);
			final long generatedCharterOutCosts = shippingCostHelper.getGeneratedCharterOutCosts(plan);

			result += shippingEntity.getShippingBook().getTaxedProfit(generatedCharterOutRevenue - generatedCharterOutCosts, utcEquivTaxTime);

			if (annotatedSolution != null && exportElement != null) {
				if (shippingCostHelper.hasGeneratedCharterOut(plan)) {
					// Calculate P&L
					generateCharterOutAnnotations(plan, vesselAvailability, vesselStartTime, annotatedSolution, shippingEntity, utcEquivTaxTime, generatedCharterOutRevenue, firstSlot);
				}
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
	protected void evaluateCargoPNL(final CargoPNLData cargoPNLData, final IEntity baseEntity, final Map<IEntityBook, Long> entityPreTaxProfit, @Nullable final IAnnotatedSolution annotatedSolution,
			@Nullable final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap) {

		int idx = 0;
		for (final IPortSlot slot : cargoPNLData.slots) {

			final IEntity entity = cargoPNLData.slotEntity[idx];
			assert entity != null;

			final long hedgeValue = hedgesProvider.getHedgeValue(slot);
			addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), hedgeValue);
			if (hedgeValue != 0 && annotatedSolution != null) {
				annotatedSolution.getElementAnnotations().setAnnotation(slotProvider.getElement(slot), SchedulerConstants.AI_hedgingValue, new HedgingAnnotation(hedgeValue));
			}

			final long value = Calculator.costFromConsumption(cargoPNLData.slotVolumeInMMBTu[idx], cargoPNLData.slotPricePerMMBTu[idx]);
			if (slot instanceof ILoadOption) {

				// Sum up entity p&L
				addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), -value);
				addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), cargoPNLData.slotAdditionalPNL[idx]);

			} else if (slot instanceof IDischargeOption) {

				// Base entity gets the profit
				assert baseEntity != null;
				addEntityBookProfit(entityPreTaxProfit, baseEntity.getTradingBook(), value);
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
	public long evaluate(final VoyagePlan plan, final IVesselAvailability vesselAvailability, final int planStartTime, final int vesselStartTime, @Nullable final IAnnotatedSolution annotatedSolution) {
		final IEntity shippingEntity = entityProvider.getEntityForVesselAvailability(vesselAvailability);
		if (shippingEntity == null) {
			return 0L;
		}

		final long value;
		final long revenue;
		final long generatedCharterOutRevenue;
		final long generatedCharterOutCost;
		{
			final long shippingCost = shippingCostHelper.getShippingCosts(plan, vesselAvailability, true, includeTimeCharterInFitness);
			final PortDetails portDetails = (PortDetails) plan.getSequence()[0];
			if (portDetails.getOptions().getPortSlot().getPortType() == PortType.CharterOut) {
				final VesselEventPortSlot vesselEventPortSlot = (VesselEventPortSlot) portDetails.getOptions().getPortSlot();
				revenue = vesselEventPortSlot.getVesselEvent().getHireCost() + vesselEventPortSlot.getVesselEvent().getRepositioning();
			} else {
				revenue = 0;
			}

			generatedCharterOutRevenue = shippingCostHelper.getGeneratedCharterOutRevenue(plan, vesselAvailability);
			generatedCharterOutCost = shippingCostHelper.getGeneratedCharterOutCosts(plan);

			// Calculate the value for the fitness function
			final IEntityBook shippingBook = shippingEntity.getShippingBook();
			final int utcEquivTaxTime = utcOffsetProvider.UTC(planStartTime, portDetails.getOptions().getPortSlot().getPort());
			value = shippingBook.getTaxedProfit(revenue + generatedCharterOutRevenue - generatedCharterOutCost - shippingCost, utcEquivTaxTime);
		}
		// Solution Export branch - should called infrequently
		if (annotatedSolution != null) {

			final IPortSlot firstSlot = ((PortDetails) plan.getSequence()[0]).getOptions().getPortSlot();
			final ISequenceElement exportElement = slotProvider.getElement(firstSlot);

			// We include LNG costs here, but this may not be desirable - this depends on whether or not we consider the LNG a sunk cost...
			// Cost is zero as shipping cost is recalculated to obtain annotation

			generateShippingAnnotations(plan, vesselAvailability, vesselStartTime, annotatedSolution, shippingEntity, revenue, 0, planStartTime, exportElement, true);
			if (shippingCostHelper.hasGeneratedCharterOut(plan)) {
				generateCharterOutAnnotations(plan, vesselAvailability, vesselStartTime, annotatedSolution, shippingEntity, planStartTime, generatedCharterOutRevenue, firstSlot);
			}

		}

		return value;
	}

	private void generateShippingAnnotations(final VoyagePlan plan, final IVesselAvailability vesselAvailability, final int vesselStartTime, final IAnnotatedSolution annotatedSolution,
			final IEntity shippingEntity, final long revenue, final long cost, final int utcEquivTaxTime, final ISequenceElement exportElement, final boolean includeLNG) {
		{
			final long shippingCosts = shippingCostHelper.getShippingCosts(plan, vesselAvailability, includeLNG, true);
			final long shippingTotalPretaxProfit = revenue /* +additionProfitAndLoss */- cost - shippingCosts;
			final long shippingProfit = shippingEntity.getShippingBook().getTaxedProfit(shippingTotalPretaxProfit, utcEquivTaxTime);

			final DetailTree shippingDetails = new DetailTree();

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity.getShippingBook(), shippingProfit, shippingTotalPretaxProfit, shippingDetails);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			annotatedSolution.getElementAnnotations().setAnnotation(exportElement, SchedulerConstants.AI_profitAndLoss, annotation);
		}
	}

	private void generateCharterOutAnnotations(final VoyagePlan plan, final IVesselAvailability vesselAvailability, final int vesselStartTime, final IAnnotatedSolution annotatedSolution,
			final IEntity shippingEntity, final int utcEquivTaxTime, final long generatedCharterOutRevenue, final IPortSlot firstSlot) {

		final long charterOutCosts = shippingCostHelper.getGeneratedCharterOutCosts(plan);
		final long charterOutPretaxProfit = generatedCharterOutRevenue - charterOutCosts;
		final long charterOutProfit = shippingEntity.getShippingBook().getTaxedProfit(charterOutPretaxProfit, utcEquivTaxTime);

		final DetailTree details = new DetailTree();

		details.addChild(new DetailTree("Charter Out", new TotalCostDetailElement(generatedCharterOutRevenue)));

		final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity.getShippingBook(), charterOutProfit, charterOutCosts, details);
		final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
		final ISequenceElement element = slotProvider.getElement(firstSlot);
		annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_charterOutProfitAndLoss, annotation);
	}

	protected void addEntityBookProfit(@NonNull final Map<IEntityBook, Long> entityBookProfit, @NonNull final IEntityBook entityBook, final long profit) {
		assert entityBook != null;
		long totalProfit = profit;
		if (entityBookProfit.containsKey(entityBook)) {
			final Long existingProfit = entityBookProfit.get(entityBook);
			if (existingProfit != null) {
				totalProfit += existingProfit.longValue();
			}
		}
		entityBookProfit.put(entityBook, totalProfit);
	}

	protected IDetailTree getEntityBookDetails(final Map<IEntityBook, IDetailTree> entityBookDetailsMap, final IEntityBook entityBook) {
		if (entityBookDetailsMap.containsKey(entityBook)) {
			return entityBookDetailsMap.get(entityBook);
		} else {
			final DetailTree tree = new DetailTree();
			entityBookDetailsMap.put(entityBook, tree);
			return tree;
		}
	}

	protected IDetailTree getPortSlotDetails(final Map<IPortSlot, IDetailTree> portSlotSetailsMap, final IPortSlot portSlot) {
		if (portSlotSetailsMap.containsKey(portSlot)) {
			return portSlotSetailsMap.get(portSlot);
		} else {
			final DetailTree tree = new DetailTree();
			portSlotSetailsMap.put(portSlot, tree);
			return tree;
		}
	}

	/**
	 * Calculate shipping costs for the purposes of P&L. This should be overridden in subclasses to implement shipping book transfer pricing
	 * 
	 * @param entityPreTaxProfit
	 * @param vesselAvailability
	 * @param plan
	 * @param costBook
	 * @param shippingBook
	 * @param includeLNG
	 */

	protected void calculateShippingEntityCosts(final Map<IEntityBook, Long> entityPreTaxProfit, final IVesselAvailability vesselAvailability, final VoyagePlan plan, final CargoPNLData cargoPNLData,
			final IEntity tradingEntity, final IEntity shippingEntity, final boolean includeLNG, final Map<IEntityBook, IDetailTree> entityDetailsMap) {

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return;
		}

		final long shippingCosts = shippingCostHelper.getRouteExtraCosts(plan) + shippingCostHelper.getFuelCosts(plan, includeLNG);
		final long portCosts = shippingCostHelper.getPortCosts(vesselAvailability.getVessel(), plan);
		final long hireCosts = shippingCostHelper.getHireCosts(plan);

		final long totalShippingCosts = shippingCosts + portCosts + hireCosts;
		addEntityBookProfit(entityPreTaxProfit, tradingEntity.getTradingBook(), -totalShippingCosts);
	}

	/**
	 * Overridable method to populate any post tax items
	 * 
	 * @param vessel
	 * @param plan
	 * @param cargoPNLData
	 * @param entityPostTaxProfit
	 * @param entityDetailTreeMap
	 * @return
	 */
	protected long calculatePostTaxItems(@NonNull final IVesselAvailability vesselAvailability, @NonNull final VoyagePlan plan, @NonNull final CargoPNLData cargoPNLData,
			@NonNull final Map<IEntityBook, Long> entityPostTaxProfit, @Nullable final Map<IEntityBook, IDetailTree> entityDetailTreeMap) {
		return 0;
	}

	@Override
	public long evaluateUnusedSlot(@NonNull final IPortSlot portSlot, @Nullable final IAnnotatedSolution annotatedSolution) {

		final IEntity entity = entityProvider.getEntityForSlot(portSlot);

		long result = 0;
		{
			final long hedgeValue = hedgesProvider.getHedgeValue(portSlot);
			final long cancellationCost = cancellationFeeProvider.getCancellationFee(portSlot);

			// Taxed P&L - use time window start as tax date
			final long preTaxValue = hedgeValue - cancellationCost;
			final int utcEquivTaxTime = utcOffsetProvider.UTC(portSlot.getTimeWindow().getStart(), portSlot);
			final long postTaxValue = entity.getTradingBook().getTaxedProfit(preTaxValue, utcEquivTaxTime);
			result = postTaxValue;

			if (annotatedSolution != null) {
				final ISequenceElement exportElement = slotProvider.getElement(portSlot);
				assert exportElement != null;

				final DetailTree entityDetails = new DetailTree();
				// TODO: Populate

				final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity.getTradingBook(), postTaxValue, preTaxValue, entityDetails);

				final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
				annotatedSolution.getElementAnnotations().setAnnotation(exportElement, SchedulerConstants.AI_profitAndLoss, annotation);

				if (cancellationCost != 0) {
					annotatedSolution.getElementAnnotations().setAnnotation(exportElement, SchedulerConstants.AI_cancellationFees, new CancellationAnnotation(cancellationCost));
				}
				if (hedgeValue != 0) {
					annotatedSolution.getElementAnnotations().setAnnotation(exportElement, SchedulerConstants.AI_hedgingValue, new HedgingAnnotation(hedgeValue));
				}

			}
		}

		return result;
	}
}
