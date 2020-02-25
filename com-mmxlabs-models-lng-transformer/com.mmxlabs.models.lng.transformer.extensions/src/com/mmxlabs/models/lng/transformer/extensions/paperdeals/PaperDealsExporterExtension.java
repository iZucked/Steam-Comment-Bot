/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.paperdeals;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.google.inject.Inject;
import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.common.paperdeals.BasicInstrumentData;
import com.mmxlabs.common.paperdeals.BasicPaperDealAllocationEntry;
import com.mmxlabs.common.paperdeals.BasicPaperDealData;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;

/**
 * EMF export side for basic trading optimiser information. Model may need reworking, so this isn't exactly final.
 * 
 * @author FM
 * 
 */
public class PaperDealsExporterExtension implements IExporterExtension {
	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;
	private Map<String, BaseLegalEntity> entities = new HashMap<>();
	
	@Inject
	LNGScenarioModel lngScenarioModel;
	
	@Inject
	@Named(SchedulerConstants.COMPUTE_EXPOSURES)
	private boolean exposuresEnabled;
	
	@Inject
	@Named(SchedulerConstants.COMPUTE_PAPER_PNL)
	private boolean paperPnLEnabled;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
		if (paperPnLEnabled) {
			final ProfitAndLossSequences profitAndLossSequences = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
			
			final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealEntries = profitAndLossSequences.getPaperDealRecords();
			
			//FIXME : hack to get the entities. Simon thinks that having a provider is much better
			for (final BaseLegalEntity entity : ScenarioModelUtil.getCommercialModel(lngScenarioModel).getEntities()) {
				entities.put(entity.getName(), entity);
			}
			
			outputSchedule.getPaperDealAllocations().clear();
			outputSchedule.getGeneratedPaperDeals().clear();
			
			for (final BasicPaperDealData basicPaperDealData : paperDealEntries.keySet()) {
				final List<BasicPaperDealAllocationEntry> basicPaperDealAllocationEntries = paperDealEntries.get(basicPaperDealData);
				final PaperDeal paperDeal;
				if (!basicPaperDealData.isVirtual()) {
					paperDeal = modelEntityMap.getModelObjectNullChecked(basicPaperDealData, PaperDeal.class);
				} else {
					if (basicPaperDealData.isBuy()) {
						paperDeal = CargoFactory.eINSTANCE.createBuyPaperDeal();
					} else {
						paperDeal = CargoFactory.eINSTANCE.createSellPaperDeal();
					}
					paperDeal.setName(basicPaperDealData.getName());
					paperDeal.setQuantity(OptimiserUnitConvertor.convertToExternalVolume(basicPaperDealData.getPaperVolume()));
					paperDeal.setPrice(OptimiserUnitConvertor.convertToExternalPrice(basicPaperDealData.getPaperUnitPrice()));
					final YearMonth start = YearMonth.from(basicPaperDealData.getStart());
					paperDeal.setPricingMonth(start);
					paperDeal.setStartDate(basicPaperDealData.getStart());
					paperDeal.setEndDate(basicPaperDealData.getEnd());
					
					final PaperPricingType paperPricingType;
					SettleStrategy settleStrategy = null;
					if ("CALENDAR".equals(basicPaperDealData.getType())) {
						paperPricingType = PaperPricingType.CALENDAR;
					} else if("INSTRUMENT".equals(basicPaperDealData.getType())) {
						paperPricingType = PaperPricingType.INSTRUMENT;
						final BasicInstrumentData basicInstrumentData = basicPaperDealData.getInstrument();
						if (basicInstrumentData == null) {
							throw new IllegalStateException(String.format("No pricing instrument %s found!", basicInstrumentData.getName()));
						}
						final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(lngScenarioModel);
						for (final SettleStrategy ss : pricingModel.getSettleStrategies()) {
							if (ss.getName().equalsIgnoreCase(basicInstrumentData.getName())) {
								settleStrategy = ss;
							}
						}
						if (settleStrategy == null) {
							throw new IllegalStateException(String.format("No pricing instrument %s found!", basicInstrumentData.getName()));
						}
					} else {
						paperPricingType = PaperPricingType.PERIOD_AVG;
					}
					paperDeal.setPricingType(paperPricingType);
					paperDeal.setInstrument(settleStrategy);
					paperDeal.setIndex(basicPaperDealData.getIndexName());
					BaseLegalEntity entity = entities.get(basicPaperDealData.getEntity());
					//FIXME : define with clients a "default" entity
					if (entity == null) {
						final List<BaseLegalEntity> bles = new ArrayList<>(entities.values());
						entity = bles.get(0);
					}
					paperDeal.setEntity(entity);
					paperDeal.setYear(basicPaperDealData.getYear());
					paperDeal.setComment(basicPaperDealData.getNotes());
					
					outputSchedule.getGeneratedPaperDeals().add(paperDeal);
				}
				outputSchedule.getPaperDealAllocations().add(allocatePaperDeal(paperDeal, basicPaperDealAllocationEntries));
			}
		}
		
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}
	
	private PaperDealAllocation allocatePaperDeal(final PaperDeal paperDeal, final List<BasicPaperDealAllocationEntry> paperDealAllocations) {
		final PaperDealAllocation allocation = ScheduleFactory.eINSTANCE.createPaperDealAllocation();
		allocation.setPaperDeal(paperDeal);
		
		for (final BasicPaperDealAllocationEntry optiEntry : paperDealAllocations) {
			final PaperDealAllocationEntry entry = ScheduleFactory.eINSTANCE.createPaperDealAllocationEntry();
			entry.setDate(optiEntry.getDate());
			entry.setPrice(OptimiserUnitConvertor.convertToExternalPrice(optiEntry.getPrice()));
			entry.setQuantity(OptimiserUnitConvertor.convertToExternalVolume(optiEntry.getQuantity()));
			entry.setValue(OptimiserUnitConvertor.convertToExternalVolume(optiEntry.getValue()));
			
			if (exposuresEnabled) {
				for (final BasicExposureRecord record : optiEntry.getExposures()){
					final ExposureDetail newDetail = ScheduleFactory.eINSTANCE.createExposureDetail();
					final DealType dealType = record.getIndexName().equalsIgnoreCase("PHYSICAL") ? DealType.PHYSICAL : DealType.FINANCIAL;
					newDetail.setDealType(dealType);
					final int volumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(record.getVolumeMMBTU());
					newDetail.setVolumeInMMBTU(volumeMMBTU);
					final int volumeNative = OptimiserUnitConvertor.convertToExternalVolume(record.getVolumeNative());
					newDetail.setVolumeInNativeUnits(volumeNative);
					final int externalVolumeValueNative = (int) Calculator.costFromVolume(volumeNative, record.getUnitPrice());
					final double volumeValueNative = OptimiserUnitConvertor.convertToExternalPrice(externalVolumeValueNative);
					newDetail.setNativeValue(volumeValueNative);
					newDetail.setVolumeUnit(record.getVolumeUnit());
					newDetail.setIndexName(record.getIndexName());
					newDetail.setDate(YearMonth.from(record.getTime()));
	
					entry.getExposures().add(newDetail);
				}
				
				for (final ExposureDetail d : entry.getExposures()) {
					// This is only for unit test reload validation as -0.0 != 0.0 and we cannot
					// persist/reload -0.0
					if (d.getNativeValue() == -0.0) {
						d.setNativeValue(0.0);
					}
					if (d.getUnitPrice() == -0.0) {
						d.setUnitPrice(0.0);
					}
					if (d.getVolumeInMMBTU() == -0.0) {
						d.setVolumeInMMBTU(0.0);
					}
					if (d.getVolumeInNativeUnits() == -0.0) {
						d.setVolumeInNativeUnits(0.0);
					}
				}
			}
			
			allocation.getEntries().add(entry);
		}
		return allocation;
	}

}
