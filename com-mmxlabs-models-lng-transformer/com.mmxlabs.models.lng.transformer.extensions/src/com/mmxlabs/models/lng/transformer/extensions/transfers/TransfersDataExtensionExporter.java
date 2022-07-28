package com.mmxlabs.models.lng.transformer.extensions.transfers;

import java.util.Set;

import javax.inject.Named;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.util.SlotContractHelper;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.transfers.TranferRecordAnnotation;
import com.mmxlabs.scheduler.optimiser.transfers.TransferRecordModelConstants;

public class TransfersDataExtensionExporter implements IExporterExtension {
	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private ExporterExtensionUtils exporterExtensionUtils;

	@Inject
	@Named(SchedulerConstants.PROCESS_TRANSFER_MODEL)
	private boolean processTransferModel;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		if (processTransferModel) {
			this.modelEntityMap = modelEntityMap;
			this.annotatedSolution = annotatedSolution;
			this.outputSchedule = outputSchedule;
		}
	}

	@Override
	public void finishExporting() {
		if (processTransferModel) {
			final Set<ISequenceElement> allElements = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.ALL_ELEMENTS, Set.class);
			if (allElements != null) {
				for (final ISequenceElement element : allElements) {
					final IPortSlot slot = slotProvider.getPortSlot(element);

					final ProfitAndLossContainer profitAndLossContainer = exporterExtensionUtils.findProfitAndLossContainer(element, slot, modelEntityMap, outputSchedule, annotatedSolution);

					if (profitAndLossContainer != null) {

						final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss,
								IProfitAndLossAnnotation.class);
						// Find child

						if (profitAndLoss != null) {
							for(final TranferRecordAnnotation annotation : SlotContractHelper.findAllDetailsAnnotation(profitAndLoss, TransferRecordModelConstants.TRANSFER_RECORD_ANNOTAION_KEY, TranferRecordAnnotation.class)) {

								final TransferRecordPNLDetails details = ScheduleFactory.eINSTANCE.createTransferRecordPNLDetails();
								final BaseLegalEntity fromEntity = modelEntityMap.getModelObjectNullChecked(annotation.fromEntity, BaseLegalEntity.class);
								final BaseLegalEntity toEntity = modelEntityMap.getModelObjectNullChecked(annotation.toEntity, BaseLegalEntity.class);
								final TransferRecord transferRecord = modelEntityMap.getModelObjectNullChecked(annotation.transferRecord, TransferRecord.class);

								details.setTransferRecord(transferRecord);
								details.setTransferPrice(OptimiserUnitConvertor.convertToExternalPrice(annotation.tpPrice));
								details.setFromEntity(fromEntity);
								details.setFromEntityCost(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.fromEntityCost));
								details.setFromEntityRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.fromEntityRevenue));
								details.setToEntity(toEntity);
								details.setToEntityCost(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.toEntityCost));
								details.setToEntityRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.toEntityRevenue));

								//ExporterExtensionUtils.addEntityPNLDetails(profitAndLossContainer, modelEntity, details);
								profitAndLossContainer.getGeneralPNLDetails().add(details);
							}

						}
					}
				}
			}

			// clear refs, just in case.
			modelEntityMap = null;
			outputSchedule = null;
			annotatedSolution = null;
		}
	}

}
