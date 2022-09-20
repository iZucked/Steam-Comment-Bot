/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.name.Named;
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
import com.mmxlabs.scheduler.optimiser.annotations.impl.MiscCostsAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.impl.ProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarketOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.HeelValueRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.SlotHeelVolumeRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICancellationFeeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMiscCostsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITransferModelDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.transfers.BasicTransferRecord;
import com.mmxlabs.scheduler.optimiser.transfers.TranferRecordAnnotation;
import com.mmxlabs.scheduler.optimiser.transfers.TransferRecordModelConstants;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * For each VoyagePlan in the schedule the evaluate(...) methods work out: -
 * discharge and purchase prices for the VP, then the - "additionalPnL" (e.g.
 * upside calculations). - calculate shipping cost (from VP contents) -
 * calculates charter-out P&L (from VP contents)
 * 
 * Two evaluate methods cover cargo and non-cargo VoyagePlans.
 * 
 * @author proshun
 */
@NonNullByDefault
public class DefaultEntityValueCalculator implements IEntityValueCalculator {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private IEntityProvider entityProvider;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private ShippingCostHelper shippingCostHelper;

	@Inject
	private IMiscCostsProvider miscCostsProvider;

	@Inject
	private ICancellationFeeProvider cancellationFeeProvider;

	@Inject
	private ITimeZoneToUtcOffsetProvider utcOffsetProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;
	
	@Inject
	private ITransferModelDataProvider transferModelDataProvider;
	
	@Inject
	@Named(SchedulerConstants.PROCESS_TRANSFER_MODEL)
	private boolean processTransferModel;

	/**
	 * Evaluate the group value of the given cargo. This method first calculates
	 * sales prices, then purchase prices and additional P&L, then shipping costs
	 * and charter out revenue.
	 * 
	 * @param plan
	 * @param currentAllocation
	 * @return
	 */
	@Override
	public Pair<CargoValueAnnotation, Long> evaluate(final VoyagePlan plan, final IAllocationAnnotation currentAllocation, final IVesselCharter vesselCharter,
			@Nullable final ProfitAndLossSequences volumeAllocatedSequences, @Nullable final IAnnotatedSolution annotatedSolution) {

		final CargoValueAnnotation cargoPNLData = new CargoValueAnnotation(currentAllocation);

		final List<IPortSlot> slots = cargoPNLData.getSlots();

		// final CargoValueAnnotation cargoPNLData = new
		// CargoValueAnnotation(currentAllocation);
		// Note: Solution Export branch - should called infrequently

		// Find elements to attach export data to.
		ISequenceElement exportElement = null;
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
		final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap = (exportElement == null) ? null : new HashMap<>();
		final Map<IPortSlot, IDetailTree> portSlotDetailTreeMap = (exportElement == null) ? null : new HashMap<>();

		// Calculate sales/discharge prices
		int idx = 0;
		// Tax time is assumed to by the UTC equivalent of the last discharge time. This
		// may not always be a valid assumption...
		int utcEquivTaxTime = 0;
		final int[] slotPricesPerMMBTu = new int[slots.size()];

		for (final IPortSlot slot : slots) {
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

				final int slotPricePerMMBtu = dischargeOption.getDischargePriceCalculator().calculateSalesUnitPrice(vesselCharter, dischargeOption, cargoPNLData, plan, portSlotDetails);
				cargoPNLData.setSlotPricePerMMBTu(slot, slotPricePerMMBtu);

				final long slotValue = Calculator.costFromConsumption(cargoPNLData.getCommercialSlotVolumeInMMBTu(slot), slotPricePerMMBtu);
				cargoPNLData.setSlotValue(slot, slotValue);
				slotPricesPerMMBTu[idx] = slotPricePerMMBtu;
			}

			// Translate into UTC curve
			utcEquivTaxTime = utcOffsetProvider.UTC(cargoPNLData.getSlotTime(slot), slot.getPort());

			idx++;
		}

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
						pricePerMMBTu = loadSlot.getLoadPriceCalculator().calculateFOBPricePerMMBTu(loadSlot, dischargeSlot, dischargePricePerMMBTu, cargoPNLData, vesselCharter, plan,
								volumeAllocatedSequences, portSlotDetails);
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
				cargoPNLData.setSlotValue(slot, Calculator.costFromConsumption(cargoPNLData.getCommercialSlotVolumeInMMBTu(slot), cargoPNLData.getSlotPricePerMMBTu(slot)));
				slotPricesPerMMBTu[idx] = pricePerMMBTu;
			}
			idx++;
		}

		// Calculate additional P&L
		idx = 0;
		for (final IPortSlot slot : slots) {
			if (slot instanceof ILoadOption loadOption) {
				final IDetailTree portSlotDetails = portSlotDetailTreeMap == null ? null : getPortSlotDetails(portSlotDetailTreeMap, slot);

				final long[] additionProfitAndLossComponents = loadOption.getLoadPriceCalculator().calculateAdditionalProfitAndLoss(loadOption, cargoPNLData, slotPricesPerMMBTu, vesselCharter,
						plan, volumeAllocatedSequences, portSlotDetails);

				cargoPNLData.setSlotAdditionalOtherPNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_OTHER_VALUE]);
				cargoPNLData.setSlotAdditionalUpsidePNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_UPSIDE_VALUE]);
				cargoPNLData.setSlotAdditionalShippingPNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_SHIPPING_VALUE]);

				cargoPNLData.setSlotUpstreamPNL(slot, additionProfitAndLossComponents[ILoadPriceCalculator.IDX_UPSTREAM_VALUE]);
			}
			idx++;
		}

		// Calculate transfer pricing etc between entities
		final Map<IEntityBook, Long> entityPreTaxProfit = new HashMap<>();
		evaluateCargoPNL(vesselCharter, plan, cargoPNLData, entityPreTaxProfit, annotatedSolution, entityBookDetailTreeMap);
		// Calculate actual transfer pricing between entities based on transfer records
		if (processTransferModel && annotatedSolution != null && entityBookDetailTreeMap != null) {
			evaluateTransferRecordPNL(vesselCharter, plan, cargoPNLData, entityPreTaxProfit, annotatedSolution, entityBookDetailTreeMap);
		}

		// The first load entity
		IEntity baseEntity = null;
		for (final IPortSlot slot : slots) {
			if (slot instanceof ILoadOption) {
				// First load slot is the base entity
				baseEntity = cargoPNLData.getSlotEntity(slot);
				break;
			}
		}

		assert baseEntity != null;

		// Shipping Entity for non-cargo costings - handle any transfer pricing etc
		// required
		IEntity shippingEntity = entityProvider.getEntityForVesselCharter(vesselCharter);
		if (shippingEntity == null) {
			shippingEntity = baseEntity;
		}
		seenEntities.add(shippingEntity);

		assert baseEntity != null;
		assert shippingEntity != null;
		{
			calculateCargoShippingEntityCosts(entityPreTaxProfit, vesselCharter, plan, cargoPNLData, baseEntity, shippingEntity, entityBookDetailTreeMap);
		}

		{
			final IPortSlot returnSlot = currentAllocation.getReturnSlot();
			if (returnSlot != null) {

				if (returnSlot.getPortType() == PortType.Round_Trip_Cargo_End) {
					final IPortSlot firstPortSlot = currentAllocation.getFirstSlot();
					final int vesselStartTime = currentAllocation.getFirstSlotTime();
					// Repos
					final long additionalCost1 = shippingCostHelper.calculateRFRevenue(currentAllocation, firstPortSlot, vesselCharter);
					// Ballast
					final long additionalCost2 = shippingCostHelper.calculateBBCost(currentAllocation, returnSlot, vesselCharter, vesselStartTime, returnSlot.getPort());

					addEntityBookProfit(entityPreTaxProfit, baseEntity.getTradingBook(), -additionalCost1);
					addEntityBookProfit(entityPreTaxProfit, baseEntity.getTradingBook(), -additionalCost2);

					if (annotatedSolution != null) {

						final DetailTree shippingDetails = new DetailTree();
						entityBookDetailTreeMap.put(baseEntity.getTradingBook(), shippingDetails);
						// Add in positioning costs
						shippingCostHelper.annotateRF(currentAllocation, shippingDetails, firstPortSlot, vesselCharter);

						// Add in ballast bonus
						shippingCostHelper.annotateBB(currentAllocation, shippingDetails, returnSlot, vesselCharter, vesselStartTime, returnSlot.getPort());
					}
				}

			}
		}

		calculateExtraPreTaxItems(entityPreTaxProfit, vesselCharter, plan, cargoPNLData, baseEntity, shippingEntity, entityBookDetailTreeMap);

		// Taxed P&L
		final Map<IEntityBook, Long> entityPostTaxProfit = new HashMap<>();
		for (final Map.Entry<IEntityBook, Long> e : entityPreTaxProfit.entrySet()) {
			entityPostTaxProfit.put(e.getKey(), e.getKey().getTaxedProfit(e.getValue(), utcEquivTaxTime));
		}

		// Hook for client specific post tax stuff
		calculatePostTaxItems(vesselCharter, plan, cargoPNLData, entityPostTaxProfit, entityBookDetailTreeMap);

		// Calculate the value for the fitness function
		processProfitAndLossBooks(true, entityPreTaxProfit, entityPostTaxProfit);

		long result = 0L;
		for (final Map.Entry<IEntityBook, Long> e : entityPostTaxProfit.entrySet()) {
			if (!e.getKey().getEntity().isThirdparty()) {
				result += e.getValue();
			}
		}

		// Solution Export branch - should called infrequently
		if (annotatedSolution != null && exportElement != null && entityBookDetailTreeMap != null && portSlotDetailTreeMap != null) {
			// Add remaining entities as we may use some outside of this method.
			seenEntities.addAll(entityProvider.getEntities());
			{
				final List<IProfitAndLossEntry> entries = new LinkedList<>();
				for (final IEntity entity : seenEntities) {
					final boolean thirdparty = entity.isThirdparty();

					final List<IEntityBook> books = Lists.newArrayList(entity.getShippingBook(), entity.getTradingBook(), entity.getUpstreamBook());
					for (final IEntityBook book : books) {
						final Long postTaxProfit = entityPostTaxProfit.get(book);
						final Long preTaxProfit = entityPreTaxProfit.get(book);
						final IDetailTree entityDetails = entityBookDetailTreeMap.get(book);

						if (preTaxProfit != null || postTaxProfit != null || (entityDetails != null && !entityDetails.getChildren().isEmpty())) {
							final long preTaxValue = (thirdparty || preTaxProfit == null) ? 0 : preTaxProfit.longValue();
							final long postTaxValue = (thirdparty || postTaxProfit == null) ? 0 : postTaxProfit.longValue();

							if (!thirdparty || (entityDetails != null && !entityDetails.getChildren().isEmpty())) {
								final IProfitAndLossEntry entry = new ProfitAndLossEntry(book, postTaxValue, preTaxValue, entityDetails);
								entries.add(entry);
							}
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

		cargoPNLData.setTotalProfitAndLoss(result);
		return new Pair<>(cargoPNLData, result);
	}

	protected void calculateExtraPreTaxItems(final Map<IEntityBook, Long> entityPreTaxProfit, final IVesselCharter vesselCharter, final VoyagePlan plan,
			final CargoValueAnnotation cargoPNLData, final IEntity baseEntity, final IEntity shippingEntity, @Nullable final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap) {
		// Do nothing by default

	}

	/**
	 * Allow subclasses to post-process the P&L books before final data extraction
	 * occurs. E.g. allow custom books to be merged
	 * 
	 * @param entityPreTaxProfit
	 * @param entityPostTaxProfit
	 */

	protected void processProfitAndLossBooks(final boolean isCargo, final Map<IEntityBook, Long> entityPreTaxProfit, final Map<IEntityBook, Long> entityPostTaxProfit) {
		// Do nothing by default.
	}

	/**
	 * Overridable method to perform transfer pricing between entities for a Cargo
	 * 
	 * @param cargoPNLData
	 * @param baseEntity
	 * @param entityProfit
	 */
	protected void evaluateCargoPNL(final IVesselCharter vesselCharter, final VoyagePlan plan, final CargoValueAnnotation cargoPNLData, final Map<IEntityBook, Long> entityPreTaxProfit,
			@Nullable final IAnnotatedSolution annotatedSolution, @Nullable final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap) {

		IEntity baseEntity = null;

		for (final IPortSlot slot : cargoPNLData.getSlots()) {
			assert slot != null;
			final IEntity entity = cargoPNLData.getSlotEntity(slot);
			assert entity != null;
			if (baseEntity == null) {
				baseEntity = entity;
			}

			final long miscCostsValue = miscCostsProvider.getCostsValue(slot);
			addEntityBookProfit(entityPreTaxProfit, entity.getTradingBook(), miscCostsValue);
			if (miscCostsValue != 0 && annotatedSolution != null) {
				annotatedSolution.getElementAnnotations().setAnnotation(slotProvider.getElement(slot), SchedulerConstants.AI_miscCostsValue, new MiscCostsAnnotation(miscCostsValue));
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
		}
	}
	
	protected void evaluateTransferRecordPNL(final IVesselCharter vesselCharter, final VoyagePlan plan, final CargoValueAnnotation cargoPNLData,
			final Map<IEntityBook, Long> entityPreTaxProfit, @Nullable final IAnnotatedSolution annotatedSolution, @Nullable final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap) {

		if (cargoPNLData.getSlots().size() > SchedulerConstants.COMPLEX_CARGO_SLOTS_THRESHOLD) {
			throw new RuntimeException("Complex cargoes not supported");
		}

		IEntity loadEntity = null;
		IEntity dischargeEntity = null;
		
		ILoadOption loadOption = null;
		IDischargeOption dischargeOption = null;

		for (final IPortSlot slot : cargoPNLData.getSlots()) {
			// Hacky first slot check...
			if (slot instanceof ILoadOption ls) {
				loadOption = ls;
				loadEntity = cargoPNLData.getSlotEntity(slot);
			}
			if (slot instanceof IDischargeOption ds) {
				dischargeOption = ds;
				dischargeEntity = cargoPNLData.getSlotEntity(slot);
			}
		}
		
		assert loadOption != null;
		assert dischargeOption != null;
		assert loadEntity != null;
		assert dischargeEntity != null;
		
		boolean isTransferred = (transferModelDataProvider.isSlotTransferred(loadOption) || transferModelDataProvider.isSlotTransferred(dischargeOption));
		
		final List<BasicTransferRecord> records = getSortedTransferRecords(loadOption, dischargeOption, loadEntity, dischargeEntity);
		
		if (isTransferred && !records.isEmpty()) {
			
			// initial info
			final long volumeInMMBTu = cargoPNLData.getCommercialSlotVolumeInMMBTu(loadOption);
			final long loadPurchaseCost = cargoPNLData.getSlotValue(loadOption);
			final long dischargeSaleRevenue = cargoPNLData.getSlotValue(dischargeOption);
			final int internalSalesPrice = cargoPNLData.getSlotPricePerMMBTu(dischargeOption);
			
			// Negate the original cost and revenue from the trading book
			// Since these are already added in evaluateCargoPnL for the corresponding slots
			addEntityBookProfit(entityPreTaxProfit, loadEntity.getTradingBook(), loadPurchaseCost);
			addEntityBookProfit(entityPreTaxProfit, loadEntity.getTradingBook(), -dischargeSaleRevenue);
			
			BasicTransferRecord previousRecord = null;
			TranferRecordAnnotation prevAnnotation = null;
			for(final BasicTransferRecord currentRecord : records) {
				
				int tpPrice = getTransferPrice(currentRecord, internalSalesPrice);
				
				TranferRecordAnnotation annotation = new TranferRecordAnnotation();
				annotation.transferRecord = currentRecord;
				annotation.fromEntity = currentRecord.getFromEntity();
				annotation.toEntity = currentRecord.getToEntity();
				annotation.tpPrice = tpPrice;
				
				long purchaseCost = loadPurchaseCost;
				long saleRevenue = dischargeSaleRevenue;
				long volumeTPValue = Calculator.costFromVolume(volumeInMMBTu, tpPrice);
				if (previousRecord != null && prevAnnotation != null) {
					purchaseCost =  volumeTPValue;
					prevAnnotation.toEntityRevenue = volumeTPValue;
				}
				
				annotation.fromEntityCost = purchaseCost;
				annotation.fromEntityRevenue = volumeTPValue;
				
				annotation.toEntityCost = volumeTPValue;
				annotation.toEntityRevenue = saleRevenue;
				
				// Add previous annotation into the book
				if (prevAnnotation != null && previousRecord != null) {
					addAnnotationToTheBooks(entityPreTaxProfit, entityBookDetailTreeMap, prevAnnotation);
				}
				
				previousRecord = currentRecord;
				prevAnnotation = annotation;
			}
			
			// Add the last annotation into the book
			if (prevAnnotation != null && previousRecord != null) {
				addAnnotationToTheBooks(entityPreTaxProfit, entityBookDetailTreeMap, prevAnnotation);
			} else {
				throw new IllegalStateException(String.format("Load %s and Discharge %s pair annotated with transfer record but that seems to be missing", //
						loadOption.getId(), dischargeOption.getId()));
			}
		}
	}
	
	/**
	 * Override-able method to add any back-market or contingency time
	 * @param record
	 * @return
	 */
	protected int getTransferPrice(final BasicTransferRecord record, int internalSalesPrice) {
		return transferModelDataProvider.getTransferPrice(record.getPriceExpression(), record.getPricingDate(), internalSalesPrice, record.isBasis());
	}
	
	private void addAnnotationToTheBooks(final Map<IEntityBook, Long> entityPreTaxProfit, @Nullable final Map<IEntityBook, IDetailTree> entityBookDetailTreeMap,
			final TranferRecordAnnotation annotation) {
		addEntityBookProfit(entityPreTaxProfit, annotation.toEntity.getTradingBook(), -annotation.toEntityCost);
		addEntityBookProfit(entityPreTaxProfit, annotation.toEntity.getTradingBook(), annotation.toEntityRevenue);
		addEntityBookProfit(entityPreTaxProfit, annotation.fromEntity.getTradingBook(), -annotation.fromEntityCost);
		addEntityBookProfit(entityPreTaxProfit, annotation.fromEntity.getTradingBook(), annotation.fromEntityRevenue);
		
		if (entityBookDetailTreeMap != null) {
			final IDetailTree detailTree = getEntityBookDetails(entityBookDetailTreeMap, annotation.fromEntity.getTradingBook());
			detailTree.addChild(TransferRecordModelConstants.TRANSFER_RECORD_ANNOTAION_KEY, annotation);
		}
	}
	
	private List<BasicTransferRecord> getSortedTransferRecords(final ILoadOption loadOption, final IDischargeOption dischargeOption, final IEntity loadEntity, final IEntity dischargeEntity){
		final List<BasicTransferRecord> unsorted = new ArrayList<>();
		unsorted.addAll(transferModelDataProvider.getTransferRecordsForSlot(loadOption));
		unsorted.addAll(transferModelDataProvider.getTransferRecordsForSlot(dischargeOption));
		if (unsorted.size() <= 1 ) {
			return unsorted;
		}
		
		final List<BasicTransferRecord> sorted = new ArrayList<>(unsorted.size());
		IEntity toEntity = loadEntity;
		for (int i = 0; i < unsorted.size(); i++) {
			for (final BasicTransferRecord r : unsorted) {
				if (toEntity.equals(r.getFromEntity())) {
					sorted.add(r);
					toEntity = r.getToEntity();
					break;
				}
			}
		}
		if (sorted.size() == unsorted.size()) {
			if (!sorted.get(unsorted.size() - 1).getToEntity().equals(dischargeEntity)) {
				// something might have gone wrong
				// according to K, nothing is wrong
				// according to P, should be a chain of entities
				// throw new IllegalStateException
			}
		}
				
		return sorted;
	}

	/**
	 * Evaluate the group value of this plan, which is not carrying a cargo. It may
	 * just be a big cost to shipping.
	 * 
	 * TODO implement charter out revenues
	 * 
	 * @param plan
	 * @return
	 */
	@Override
	public Pair<Map<IPortSlot, HeelValueRecord>, Long> evaluateNonCargoPlan(final VoyagePlan plan, final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter,
			final int vesselStartTime, final int planStartTime, @Nullable final IPort firstLoadPort, final int lastHeelPricePerMMBTU, final Map<IPortSlot, SlotHeelVolumeRecord> heelRecords,
			@Nullable final IAnnotatedSolution annotatedSolution) {
		final IEntity shippingEntity = entityProvider.getEntityForVesselCharter(vesselCharter);
		if (shippingEntity == null) {
			// May be null during unit tests
			return Pair.of(Collections.emptyMap(), 0L);
		}

		final long revenue;
		long additionalCost = 0;
		final boolean isGeneratedCharterOutPlan;

		DetailTree shippingDetails = null;
		if (annotatedSolution != null) {
			shippingDetails = new DetailTree();
		}

		final Map<IEntityBook, Long> entityPreTaxProfit = new HashMap<>();
		final Map<IEntityBook, Long> entityPostTaxProfit = new HashMap<>();

		final Map<IPortSlot, HeelValueRecord> heelMap = new HashMap<>();
		{

			final PortDetails firstPortDetails = (PortDetails) plan.getSequence()[0];
			final IPortSlot firstPortSlot = firstPortDetails.getOptions().getPortSlot();
			isGeneratedCharterOutPlan = firstPortSlot.getPortType() == PortType.GeneratedCharterOut;
			final long shippingCost = shippingCostHelper.getShippingCosts(plan, vesselCharter, true);
			if (firstPortSlot.getPortType() == PortType.CharterOut || isGeneratedCharterOutPlan) {
				final ICharterOutVesselEventPortSlot vesselEventPortSlot = (ICharterOutVesselEventPortSlot) firstPortSlot;
				revenue = vesselEventPortSlot.getVesselEvent().getHireOutRevenue() //
						+ vesselEventPortSlot.getVesselEvent().getRepositioning() //
						+ vesselEventPortSlot.getVesselEvent().getBallastBonus();
			} else {
				revenue = 0;
			}

			// Repositioning on a start event
			if (firstPortSlot.getPortType() == PortType.Start) {
				additionalCost += shippingCostHelper.calculateRFRevenue(portTimesRecord, firstPortSlot, vesselCharter);
				if (annotatedSolution != null) {
					shippingCostHelper.annotateRF(portTimesRecord, shippingDetails, firstPortSlot, vesselCharter);
				}
			}
			// Ballast bonus on end event
			if (firstPortSlot.getPortType() == PortType.End) {
				additionalCost += shippingCostHelper.calculateBBCost(portTimesRecord, firstPortSlot, vesselCharter, vesselStartTime, firstLoadPort);
				if (annotatedSolution != null) {
					shippingCostHelper.annotateBB(portTimesRecord, shippingDetails, firstPortSlot, vesselCharter, vesselStartTime, firstLoadPort);
				}
			}

			final long[] heel = computeHeelValue(plan, heelRecords, portTimesRecord, heelMap, lastHeelPricePerMMBTU);
			final long heelCost = heel[0];
			final long heelRevenue = heel[1];

			// Calculate the value for the fitness function
			final boolean thirdparty = shippingEntity.isThirdparty();
			if (!thirdparty) {
				final IEntityBook shippingBook = shippingEntity.getShippingBook();
				final int utcEquivTaxTime = utcOffsetProvider.UTC(planStartTime, firstPortDetails.getOptions().getPortSlot().getPort());
				final long preTaxValue = revenue + heelRevenue - shippingCost - additionalCost - heelCost;
				final long postTaxValue = shippingBook.getTaxedProfit(preTaxValue, utcEquivTaxTime);
				entityPreTaxProfit.put(shippingBook, preTaxValue);
				entityPostTaxProfit.put(shippingBook, postTaxValue);
			}
		}

		processProfitAndLossBooks(false, entityPreTaxProfit, entityPostTaxProfit);

		long preTaxValue = 0L;
		for (final Map.Entry<IEntityBook, Long> e : entityPreTaxProfit.entrySet()) {
			preTaxValue += e.getValue();
		}
		long postTaxValue = 0L;
		for (final Map.Entry<IEntityBook, Long> e : entityPostTaxProfit.entrySet()) {
			if (!e.getKey().getEntity().isThirdparty()) {
				postTaxValue += e.getValue();
			}
		}

		// Solution Export branch - should called infrequently
		if (annotatedSolution != null) {
			assert shippingDetails != null;
			final IPortSlot firstSlot = ((PortDetails) plan.getSequence()[0]).getOptions().getPortSlot();
			final ISequenceElement exportElement = slotProvider.getElement(firstSlot);

			// We include LNG costs here, but this may not be desirable - this depends on
			// whether or not we consider the LNG a sunk cost...
			// Cost is zero as shipping cost is recalculated to obtain annotation

			generateShippingAnnotations(plan, vesselCharter, annotatedSolution, shippingDetails, shippingEntity, preTaxValue, postTaxValue, planStartTime, exportElement, true);

		}

		return Pair.of(heelMap, postTaxValue);
	}

	protected void generateShippingAnnotations(final VoyagePlan plan, final IVesselCharter vesselCharter, final IAnnotatedSolution annotatedSolution, final IDetailTree shippingDetails,
			final IEntity shippingEntity, final long preTaxValue, final long postTaxValue, final int utcEquivTaxTime, final ISequenceElement exportElement, final boolean includeLNG) {
		{
			final long shippingTotalPretaxProfit = preTaxValue;
			final long shippingProfit = shippingEntity.getShippingBook().getTaxedProfit(shippingTotalPretaxProfit, utcEquivTaxTime);

			final IProfitAndLossEntry entry = new ProfitAndLossEntry(shippingEntity.getShippingBook(), shippingProfit, shippingTotalPretaxProfit, shippingDetails);
			final IProfitAndLossAnnotation annotation = new ProfitAndLossAnnotation(Collections.singleton(entry));
			annotatedSolution.getElementAnnotations().setAnnotation(exportElement, SchedulerConstants.AI_profitAndLoss, annotation);
		}
	}

	protected void addEntityBookProfit(final Map<IEntityBook, Long> entityBookProfit, final IEntityBook entityBook, final long profit) {
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
	 * Calculate shipping costs for the purposes of P&L. This should be overridden
	 * in subclasses to implement shipping book transfer pricing
	 * 
	 * @param entityPreTaxProfit
	 * @param vesselCharter
	 * @param plan
	 * @param costBook
	 * @param shippingBook
	 * @param includeLNG
	 */

	protected void calculateCargoShippingEntityCosts(final Map<IEntityBook, Long> entityPreTaxProfit, final IVesselCharter vesselCharter, final VoyagePlan plan,
			final ICargoValueAnnotation cargoPNLData, final IEntity tradingEntity, final IEntity shippingEntity, @Nullable final Map<IEntityBook, IDetailTree> entityDetailsMap) {

		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return;
		}

		long capacityCosts = 0;
		long crewBonusCosts = 0;
		long insuranceCosts = 0;

		// Add in actuals
		for (final IPortSlot slot : cargoPNLData.getSlots()) {

			if (actualsDataProvider.hasActuals(slot)) {
				capacityCosts += actualsDataProvider.getCapacityCosts(slot);
				crewBonusCosts += actualsDataProvider.getCrewBonusCosts(slot);
				insuranceCosts += actualsDataProvider.getInsuranceCosts(slot);
			}
		}

		final long shippingCosts = shippingCostHelper.getRouteExtraCosts(plan) + shippingCostHelper.getFuelCosts(plan);
		final long portCosts = shippingCostHelper.getPortCosts(plan);
		final long hireCosts = shippingCostHelper.getHireCosts(plan);

		final long totalShippingCosts = shippingCosts + portCosts + hireCosts + capacityCosts + crewBonusCosts + insuranceCosts;
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
	protected long calculatePostTaxItems(final IVesselCharter vesselCharter, final VoyagePlan plan, final ICargoValueAnnotation cargoPNLData,
			final Map<IEntityBook, Long> entityPostTaxProfit, @Nullable final Map<IEntityBook, IDetailTree> entityDetailTreeMap) {
		return 0;
	}

	@Override
	public long evaluateUnusedSlot(final IPortSlot portSlot, @Nullable final IAnnotatedSolution annotatedSolution) {

		final IEntity entity = entityProvider.getEntityForSlot(portSlot);

		long result = 0;
		{
			final ILongCurve cancellationCurve = cancellationFeeProvider.getCancellationExpression(portSlot);
			final int pricingTime = timeZoneToUtcOffsetProvider.UTC(portSlot.getTimeWindow().getInclusiveStart(), portSlot);
			final long cancellationCost = cancellationCurve.getValueAtPoint(pricingTime);

			// Taxed P&L - use time window start as tax date
			final long preTaxValue = -cancellationCost; // note: cancellation cost positive
			final int utcEquivTaxTime = pricingTime;
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
			}
		}

		return result;
	}

	protected long[] computeHeelValue(final VoyagePlan vp, final Map<IPortSlot, SlotHeelVolumeRecord> heelRecords, final IPortTimesRecord portTimesRecord,
			final Map<IPortSlot, HeelValueRecord> heelMap, final int lastHeelPricePerMMBTU) {

		long heelRevenue = 0;
		long heelCost = 0;

		if (vp != null) {
			for (final IDetailsSequenceElement e : vp.getSequence()) {
				//
				if (vp.isIgnoreEnd() && e == vp.getSequence()[vp.getSequence().length - 1]) {
					continue;
				}

				if (e instanceof final PortDetails portDetails) {
					final IPortSlot slot = portDetails.getOptions().getPortSlot();

					if (!portTimesRecord.getSlots().contains(slot)) {
						continue;
					}

					if (slot instanceof final IHeelOptionConsumerPortSlot heelOptionConsumerPortSlot) {
						final IHeelOptionConsumer heelOptions = heelOptionConsumerPortSlot.getHeelOptionsConsumer();
						long currentHeelInM3 = heelRecords.get(slot).portHeelRecord.getHeelAtStartInM3();
						if (portTimesRecord instanceof final IAllocationAnnotation iAllocationAnnotation) {
							currentHeelInM3 = iAllocationAnnotation.getStartHeelVolumeInM3();
						}

						final int heelTime = portTimesRecord.getSlotTime(slot);

						final int pricePerMMBTU = heelOptions.isUseLastPrice() //
								? lastHeelPricePerMMBTU //
								: heelOptions.getHeelPriceCalculator().getHeelPrice(currentHeelInM3, heelTime, slot.getPort());

						final int cv = portDetails.getOptions().getCargoCVValue();
						final long heelInMMBTU = Calculator.convertM3ToMMBTu(currentHeelInM3, cv);
						final HeelValueRecord hvRecord = HeelValueRecord.withRevenue(Calculator.costFromConsumption(heelInMMBTU, pricePerMMBTU), pricePerMMBTU);
						heelMap.merge(slot, hvRecord, HeelValueRecord::merge);
						heelRevenue += hvRecord.getHeelRevenue();
						heelCost += hvRecord.getHeelCost();

					}

					if (slot instanceof final IHeelOptionSupplierPortSlot heelOptionSupplierPortSlot) {
						final IHeelOptionSupplier heelOptions = heelOptionSupplierPortSlot.getHeelOptionsSupplier();

						// This is wrong! should be vp.getStarintHeel in this case.
						long currentHeelInM3 = heelRecords.get(slot).portHeelRecord.getHeelAtEndInM3();

						// long currentHeelInM3 = vp.getRemainingHeelInM3();

						if (portTimesRecord instanceof final IAllocationAnnotation iAllocationAnnotation) {
							currentHeelInM3 = iAllocationAnnotation.getRemainingHeelVolumeInM3();
						}
						final int heelTime = portTimesRecord.getSlotTime(slot) + portTimesRecord.getSlotDuration(slot);

						final int pricePerMMBTU = heelOptions.getHeelPriceCalculator().getHeelPrice(currentHeelInM3, heelTime, slot.getPort());

						final int cv = heelOptions.getHeelCVValue();
						final long heelInMMBTU = Calculator.convertM3ToMMBTu(currentHeelInM3, cv);
						final HeelValueRecord hvRecord = HeelValueRecord.withCost(Calculator.costFromConsumption(heelInMMBTU, pricePerMMBTU), pricePerMMBTU);
						heelMap.merge(slot, hvRecord, HeelValueRecord::merge);
						heelRevenue += hvRecord.getHeelRevenue();
						heelCost += hvRecord.getHeelCost();
					}
				}

			}

		}
		return new long[] { heelCost, heelRevenue };
	}
}
