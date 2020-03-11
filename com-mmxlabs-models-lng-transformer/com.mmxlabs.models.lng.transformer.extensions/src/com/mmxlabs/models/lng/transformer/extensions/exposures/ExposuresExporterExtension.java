/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.exposures;

import java.time.YearMonth;

import javax.inject.Inject;
import javax.inject.Named;

import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences.OptimiserExposureRecords;

/**
 * EMF export side for basic trading optimiser information. Model may need reworking, so this isn't exactly final.
 * 
 * @author FM
 * 
 */
public class ExposuresExporterExtension implements IExporterExtension {
	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;
	
	@Inject
	@Named(SchedulerConstants.COMPUTE_EXPOSURES)
	private boolean exposuresEnabled;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
		if (exposuresEnabled) {
			final ProfitAndLossSequences profitAndLossSequences = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
			
			for (final SlotAllocation slotAllocation : outputSchedule.getSlotAllocations()) {
				
				final Slot<?> modelSlot = slotAllocation.getSlot();
				if (modelSlot != null) {
					final IPortSlot slot = modelEntityMap.getOptimiserObjectNullChecked(modelSlot, IPortSlot.class);
					final OptimiserExposureRecords exposureRecord = profitAndLossSequences.getPortExposureRecord(slot);
					setExposuresEntries(slotAllocation, exposureRecord, modelSlot);
				}
			}
		}
		// open slot allocations
		
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}
	
	private void setExposuresEntries(final SlotAllocation slotAllocation, final OptimiserExposureRecords exposureRecords, final Slot<?> slot) {

		if (slotAllocation == null) {
			return;
		}
		if (exposureRecords == null) {
			return;
		}
		if (slot == null) {
			return;
		}
		
		// Clearing all previous exposures
		slotAllocation.getExposures().clear();

		for (final BasicExposureRecord record : exposureRecords.records){
			final ExposureDetail newDetail = ScheduleFactory.eINSTANCE.createExposureDetail();
			final DealType dealType = record.getIndexName().equalsIgnoreCase("PHYSICAL") ? DealType.PHYSICAL : DealType.FINANCIAL;
			newDetail.setDealType(dealType);
			final int volumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(record.getVolumeMMBTU());
			newDetail.setVolumeInMMBTU(volumeMMBTU);
			final int volumeNative = OptimiserUnitConvertor.convertToExternalVolume(record.getVolumeNative());
			newDetail.setVolumeInNativeUnits(volumeNative);
			final double volumeValueNative = (double) OptimiserUnitConvertor.convertToExternalFixedCost(record.getVolumeValueNative());
			final double unitPrice = OptimiserUnitConvertor.convertToExternalPrice(record.getUnitPrice());
			newDetail.setNativeValue(volumeValueNative);
			newDetail.setVolumeUnit(record.getVolumeUnit());
			newDetail.setIndexName(record.getIndexName());
			newDetail.setDate(YearMonth.from(record.getTime()));
			newDetail.setUnitPrice(unitPrice);

			slotAllocation.getExposures().add(newDetail);
		}

		for (final ExposureDetail d : slotAllocation.getExposures()) {
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
}
