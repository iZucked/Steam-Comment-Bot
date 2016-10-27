/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
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
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator.EvaluationMode;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
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
	private ITimeZoneToUtcOffsetProvider utcOffsetProvider;

	/**
	 * Evaluate the group value of the given cargo. This method first calculates sales prices, then purchase prices and additional P&L, then shipping costs and charter out revenue.
	 * 
	 * @param plan
	 * @param currentAllocation
	 * @return
	 */
	@Override
	public Pair<@NonNull CargoValueAnnotation, @NonNull Long> evaluate(@NonNull EvaluationMode evaluationMode, @NonNull final VoyagePlan plan, @NonNull final IAllocationAnnotation currentAllocation,
			@NonNull final IVesselAvailability vesselAvailability, final int vesselStartTime, @Nullable VolumeAllocatedSequences volumeAllocatedSequences,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		final CargoValueAnnotation cargoPNLData = new CargoValueAnnotation(currentAllocation);

		final List<IPortSlot> slots = cargoPNLData.getSlots();

		// final CargoValueAnnotation cargoPNLData = new CargoValueAnnotation(currentAllocation);
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
		final int[] slotPricesPerMMBTu = new int[slots.size()];

		for (final IPortSlot slot : slots) {

			// cargoPNLData.arrivalTimes[idx] = currentAllocation.getSlotTime(slot);
			// cargoPNLData.visitDurations[idx] = currentAllocation.getSlotDuration(slot);
			// cargoPNLData.slotVolumeInM3[idx] = currentAllocation.getSlotVolumeInM3(slot);
			// cargoPNLData.slotVolumeInMMBTu[idx] = currentAllocation.getSlotVolumeInMMBTu(slot);
			// cargoPNLData.slotCargoCV[idx] = currentAllocation.getSlotCargoCV(slot);
			//

			if (slot instanceof IDischargeOption) {
				IEntity entity = entityProvider.getEntityForSlot(slot);
				if (slot instanceof IMarkToMarketOption) {
					final IMarkToMarketOption mtmSlot = (IMarkToMarketOption) slot;
					entity = mtmSlot.getMarkToMarket().getEntity();
				}
				assert entity != null;
				cargoPNLData.setSlotEntity(slot, entity);
				seenEntities.add(entity);

				final IDetailTree portSlotDetails = portSlotDetailTreeMap == null ? null : getPortSlotDetails(portSlotDetailTreeMap, slot);

				final IDischargeOption dischargeOption = (IDischargeOption) slot;
				int slotPricePerMMBtu = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeOption, cargoPNLData, portSlotDetails);
				cargoPNLData.setSlotPricePerMMBTu(slot, slotPricePerMMBtu);
				long slotValue = Calculator.costFromConsumption(cargoPNLData.getSlotVolumeInMMBTu(slot), slotPricePerMMBtu);
				cargoPNLData.setSlotValue(slot, slotValue);
				slotPricesPerMMBTu[idx] = slotPricePerMMBtu;

			}
			// Last discharge is tax time;
			// Translate into UTC curve
			utcEquivTaxTime = utcOffsetProvider.UTC(cargoPNLData.getSlotTime(slot), slot.getPort());

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
				cargoPNLData.setSlotEntity(slot, entity);
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
					final int dischargePricePerMMBTu = cargoPNLData.getSlotPricePerMMBTu(dischargeOption);

					// FOB Purchase to DES Sale
					if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
						final ILoadSlot loadSlot = (ILoadSlot) loadOption;
						final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeOption;
						pricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateFOBPricePerMMBTu(loadSlot, dischargeSlot, dischargePricePerMMBTu, cargoPNLData, vesselAvailability, vesselStartTime,
								plan, volumeAllocatedSequences, portSlotDetails);
					} else if (loadOption instanceof ILoadSlot) {
						// FOB Sale
						pricePerMMBTu = loadOption.getLoadPriceCalculator().calculatePriceForFOBSalePerMMBTu((ILoadSlot) loadOption, dischargeOption, dischargePricePerMMBTu, cargoPNLData,
								volumeAllocatedSequences, portSlotDetails);
					} else {
						// DES Purchase
						assert dischargeOption instanceof IDischargeSlot;
						pricePerMMBTu = loadOption.getLoadPriceCalculator().calculateDESPurchasePricePerMMBTu(loadOption, (IDischargeSlot) dischargeOption, dischargePricePerMMBTu, cargoPNLData,
								volumeAllocatedSequences, portSlotDetails);
					}
				}
				cargoPNLData.setSlotPricePerMMBTu(slot, pricePerMMBTu);
				cargoPNLData.setSlotValue(slot, Calculator.costFromConsumption(cargoPNLData.getSlotVolumeInMMBTu(slot), cargoPNLData.getSlotPricePerMMBTu(slot)));
				slotPricesPerMMBTu[idx] = pricePerMMBTu;
			}
			idx++;
		}

		assert baseEntity != null;

		// Calculate additional P&L
		idx = 0;
		for (final IPortSlot slot : slots) {
			if (slot instanceof ILoadOption) {
				final IDetailTree portSlotDetails = portSlotDetailTreeMap == null ? null : getPortSlotDetails(portSlotDetailTreeMap, slot);
				final ILoadOption loadOption = (ILoadOption) slot;

				final long[] additionProfitAndLossComponents = loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, cargoPNLData, slotPricesPerMMBTu, vesselAvailability,
						vesselStartTime, plan, volumeAllocatedSequences, portSlotDetails);

				cargoPNLData.setSlotAdditionalOtherPNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_OTHER_VALUE]);
				cargoPNLData.setSlotAdditionalUpsidePNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_UPSIDE_VALUE]);
				cargoPNLData.setSlotAdditionalShippingPNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_SHIPPING_VALUE]);

				cargoPNLData.setSlotUpstreamPNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_UPSTREAM_VALUE]);
			}
			idx++;
		}

		// Calculate transfer pricing etc between entities
		final Map<IEntityBook, Long> entityPreTaxProfit = new HashMap<>();
		evaluateCargoPNL(evaluationMode, cargoPNLData, baseEntity, entityPreTaxProfit, annotatedSolution, entityBookDetailTreeMap);

		// Shipping Entity for non-cargo costings - handle any transfer pricing etc required
		IEntity shippingEntity = entityProvider.getEntityForVesselAvailability(vesselAvailability);
		if (shippingEntity == null) {
			shippingEntity = baseEntity;
		}
		seenEntities.add(shippingEntity);

		assert baseEntity != null;
		assert shippingEntity != null;
		{
			calculateShippingEntityCosts(evaluationMode, entityPreTaxProfit, vesselAvailability, plan, cargoPNLData, baseEntity, shippingEntity, false, entityBookDetailTreeMap);
		}

		// Taxed P&L
		final Map<IEntityBook, Long> entityPostTaxProfit = new HashMap<>();
		for (final Map.Entry<IEntityBook, Long> e : entityPreTaxProfit.entrySet()) {
			entityPostTaxProfit.put(e.getKey(), e.getKey().getTaxedProfit(e.getValue(), utcEquivTaxTime));
		}

		// Hook for client specific post tax stuff
		calculatePostTaxItems(evaluationMode, vesselAvailability, plan, cargoPNLData, entityPostTaxProfit, entityBookDetailTreeMap);

		// Calculate the value for the fitness function
		long result = 0L;
		for (final Map.Entry<IEntityBook, Long> e : entityPostTaxProfit.entrySet()) {
			result += e.getValue();
		}

		processProfitAndLossBooks(entityPreTaxProfit, entityPostTaxProfit);

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
							final long postTaxValue = (postTaxProfit == null ? 0 : postTaxProfit.longValue());
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
							final long postTaxValue = (postTaxProfit == null ? 0 : postTaxProfit.longValue());
							final IDetailTree entityDetails = entityBookDetailTreeMap.get(entity.getTradingBook());
							final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity.getTradingBook(), postTaxValue, preTaxValue, entityDetails);
							entries.add(entry);
						}
					}
					{
						final Long postTaxProfit = entityPostTaxProfit.get(entity.getUpstreamBook());
						final Long preTaxProfit = entityPreTaxProfit.get(entity.getUpstreamBook());
						if (preTaxProfit != null || postTaxProfit != null) {
							final long preTaxValue = preTaxProfit == null ? 0 : preTaxProfit.longValue();
							final long postTaxValue = (postTaxProfit == null ? 0 : postTaxProfit.longValue());
							final IDetailTree entityDetails = entityBookDetailTreeMap.get(entity.getUpstreamBook());
							final IProfitAndLossEntry entry = new ProfitAndLossEntry(entity.getUpstreamBook(), postTaxValue, preTaxValue, entityDetails);
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
		// {
		// final long generatedCharterOutRevenue = shippingCostHelper.getGeneratedCharterOutRevenue(plan, vesselAvailability);
		// final long generatedCharterOutCosts = shippingCostHelper.getGeneratedCharterOutCosts(plan);
		//
		// result += shippingEntity.getShippingBook().getTaxedProfit(generatedCharterOutRevenue - generatedCharterOutCosts, utcEquivTaxTime);
		//
		// if (annotatedSolution != null && exportElement != null) {
		// if (shippingCostHelper.hasGeneratedCharterOut(plan)) {
		// // Calculate P&L
		// generateCharterOutAnnotations(plan, vesselAvailability, vesselStartTime, annotatedSolution, shippingEntity, utcEquivTaxTime, generatedCharterOutRevenue, firstSlot);
		// }
		// }
		// }

		return new Pair<>(cargoPNLData, result);
	}

	/**
	 * Allow subclasses to post-process the P&L books before final data extraction occurs. E.g. allow custom books to be merged
	 * 
	 * @param entityPreTaxProfit
	 * @param entityPostTaxProfit
	 */

	protected void processProfitAndLossBooks(@NonNull Map<IEntityBook, Long> entityPreTaxProfit, @NonNull Map<IEntityBook, Long> entityPostTaxProfit) {
		// Do nothing by default.
	}

	/**
	 * Overridable method to perform transfer pricing between entities for a Cargo
	 * 
	 * @param cargoPNLData
	 * @param baseEntity
	 * @param entityProfit
	 */
	protected void evaluateCargoPNL(@NonNull EvaluationMode evaluationMode, @NonNull final ICargoValueAnnotation cargoPNLData, @NonNull final IEntity baseEntity,
			@NonNull final Map<IEntityBook, Long> entityPreTaxProfit, @Nullable final IAnnotatedSolution annotatedSolution, @Nullable final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap) {

		int idx = 0;
		for (final IPortSlot slot : cargoPNLData.getSlots()) {
			assert slot != null;
			final IEntity entity = cargoPNLData.getSlotEntity(slot);
			assert entity != null;

			final long hedgeValue = hedgesProvider.getHedgeValue(slot);
			addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), hedgeValue);
			if (hedgeValue != 0 && annotatedSolution != null) {
				annotatedSolution.getElementAnnotations().setAnnotation(slotProvider.getElement(slot), SchedulerConstants.AI_hedgingValue, new HedgingAnnotation(hedgeValue));
			}

			final long value = cargoPNLData.getSlotValue(slot);
			if (slot instanceof ILoadOption) {

				// Sum up entity p&L
				addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), -value);
				addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), cargoPNLData.getSlotAdditionalShippingPNL(slot));
				addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), cargoPNLData.getSlotAdditionalUpsidePNL(slot));
				addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), cargoPNLData.getSlotAdditionalOtherPNL(slot));

				addEntityBookProfit(entityPreTaxProfit, entity.getUpstreamBook(), cargoPNLData.getSlotUpstreamPNL(slot));

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
	public long evaluate(@NonNull EvaluationMode evaluationMode, final VoyagePlan plan, final IVesselAvailability vesselAvailability, final int planStartTime, final int vesselStartTime,
			@Nullable final VolumeAllocatedSequences volumeAllocatedSequences, @Nullable final IAnnotatedSolution annotatedSolution) {
		final IEntity shippingEntity = entityProvider.getEntityForVesselAvailability(vesselAvailability);
		if (shippingEntity == null) {
			return 0L;
		}

		final long value;
		final long revenue;
		long additionalCost = 0;
		int vesselEndTime = Integer.MAX_VALUE;
		final boolean isGeneratedCharterOutPlan;
		{
			final PortDetails firstPortDetails = (PortDetails) plan.getSequence()[0];
			final IPortSlot firstPortSlot = firstPortDetails.getOptions().getPortSlot();
			isGeneratedCharterOutPlan = firstPortSlot.getPortType() == PortType.GeneratedCharterOut;
			final long shippingCost = shippingCostHelper.getShippingCosts(plan, vesselAvailability, true, includeTimeCharterInFitness);
			if (firstPortSlot.getPortType() == PortType.CharterOut || isGeneratedCharterOutPlan) {
				final IVesselEventPortSlot vesselEventPortSlot = (IVesselEventPortSlot) firstPortSlot;
				revenue = vesselEventPortSlot.getVesselEvent().getHireOutRevenue() + vesselEventPortSlot.getVesselEvent().getRepositioning();
			} else {
				revenue = 0;
			}
			if (firstPortSlot.getPortType() == PortType.Start) {
				additionalCost += shippingCostHelper.getShippingRepositioningCost(firstPortSlot, vesselAvailability, vesselStartTime);
			}
			if (firstPortSlot.getPortType() == PortType.End) {
				vesselEndTime = utcOffsetProvider.UTC(volumeAllocatedSequences.getVesselEndTime(firstPortSlot), firstPortSlot);
				additionalCost += shippingCostHelper.getShippingBallastBonusCost(firstPortSlot, vesselAvailability, vesselEndTime);
			}

			// Calculate the value for the fitness function
			final IEntityBook shippingBook = shippingEntity.getShippingBook();
			final int utcEquivTaxTime = utcOffsetProvider.UTC(planStartTime, firstPortDetails.getOptions().getPortSlot().getPort());
			value = shippingBook.getTaxedProfit(revenue - shippingCost - additionalCost, utcEquivTaxTime);
		}
		// Solution Export branch - should called infrequently
		if (annotatedSolution != null) {

			final IPortSlot firstSlot = ((PortDetails) plan.getSequence()[0]).getOptions().getPortSlot();
			final ISequenceElement exportElement = slotProvider.getElement(firstSlot);

			// We include LNG costs here, but this may not be desirable - this depends on whether or not we consider the LNG a sunk cost...
			// Cost is zero as shipping cost is recalculated to obtain annotation

			generateShippingAnnotations(evaluationMode, plan, vesselAvailability, vesselStartTime, annotatedSolution, shippingEntity, revenue, 0, additionalCost, planStartTime, exportElement, true);

		}

		return value;
	}

	private void generateShippingAnnotations(@NonNull EvaluationMode evaluationMode, final VoyagePlan plan, final IVesselAvailability vesselAvailability, final int vesselStartTime,
			final IAnnotatedSolution annotatedSolution, final IEntity shippingEntity, final long revenue, final long cost, long additionalCost, final int utcEquivTaxTime,
			final ISequenceElement exportElement, final boolean includeLNG) {
		{
			final long shippingCosts = shippingCostHelper.getShippingCosts(plan, vesselAvailability, includeLNG, true);
			final long shippingTotalPretaxProfit = revenue /* +additionProfitAndLoss */ - cost - shippingCosts - additionalCost;
			final long shippingProfit = shippingEntity.getShippingBook().getTaxedProfit(shippingTotalPretaxProfit, utcEquivTaxTime);

			final DetailTree shippingDetails = new DetailTree();

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity.getShippingBook(), shippingProfit, shippingTotalPretaxProfit, shippingDetails);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			annotatedSolution.getElementAnnotations().setAnnotation(exportElement, SchedulerConstants.AI_profitAndLoss, annotation);
		}
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

	protected void calculateShippingEntityCosts(@NonNull EvaluationMode evaluationMode, @NonNull final Map<IEntityBook, Long> entityPreTaxProfit, @NonNull final IVesselAvailability vesselAvailability,
			@NonNull final VoyagePlan plan, @NonNull final ICargoValueAnnotation cargoPNLData, @NonNull final IEntity tradingEntity, @NonNull final IEntity shippingEntity, final boolean includeLNG,
			@Nullable final Map<IEntityBook, IDetailTree> entityDetailsMap) {

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
	protected long calculatePostTaxItems(@NonNull EvaluationMode evaluationMode, @NonNull final IVesselAvailability vesselAvailability, @NonNull final VoyagePlan plan,
			@NonNull final ICargoValueAnnotation cargoPNLData, @NonNull final Map<IEntityBook, Long> entityPostTaxProfit, @Nullable final Map<IEntityBook, IDetailTree> entityDetailTreeMap) {
		return 0;
	}

	@Override
	public long evaluateUnusedSlot(@NonNull EvaluationMode evaluationMode, @NonNull final IPortSlot portSlot, @Nullable final VolumeAllocatedSequences volumeAllocatedSequences,
			@Nullable final IAnnotatedSolution annotatedSolution) {

		final IEntity entity = entityProvider.getEntityForSlot(portSlot);

		long result = 0;
		{
			final long hedgeValue = hedgesProvider.getHedgeValue(portSlot);
			final ILongCurve cancellationCurve = cancellationFeeProvider.getCancellationExpression(portSlot);
			final long cancellationCost = cancellationCurve.getValueAtPoint(portSlot.getTimeWindow().getInclusiveStart());

			// Taxed P&L - use time window start as tax date
			final long preTaxValue = hedgeValue - cancellationCost;
			final int utcEquivTaxTime = utcOffsetProvider.UTC(portSlot.getTimeWindow().getInclusiveStart(), portSlot);
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
