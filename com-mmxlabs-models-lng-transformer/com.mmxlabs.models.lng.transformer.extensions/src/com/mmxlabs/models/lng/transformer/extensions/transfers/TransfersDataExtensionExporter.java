package com.mmxlabs.models.lng.transformer.extensions.transfers;

import java.util.Set;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails;
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
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
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
	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
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
						{
							final TranferRecordAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, TransferRecordModelConstants.TRANSFER_RECORD_ANNOTAION_KEY, TranferRecordAnnotation.class);
							if (annotation != null) {
								final IEntity optimiserEntity = SlotContractHelper.findDetailsAnnotationEntity(profitAndLoss, TransferRecordModelConstants.TRANSFER_RECORD_ANNOTAION_KEY);
								final TransferRecordPNLDetails details = ScheduleFactory.eINSTANCE.createTransferRecordPNLDetails();
								
								details.setTransferPrice(OptimiserUnitConvertor.convertToExternalPrice(annotation.tpPrice));
								details.setFromEntityName(annotation.fromEntityName);
								details.setFromEntityCost(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.fromEntityCost));
								details.setFromEntityRevenue(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.fromEntityRevenue));
								details.setToEntityName(annotation.toEntityName);
								details.setToEntityCost(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.toEntityCost));
								details.setToEntityCost(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.toEntityRevenue));
	
								final BaseLegalEntity modelEntity = modelEntityMap.getModelObjectNullChecked(optimiserEntity, BaseLegalEntity.class);
								ExporterExtensionUtils.addEntityPNLDetails(profitAndLossContainer, modelEntity, details);
							}
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
