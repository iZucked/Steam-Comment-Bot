/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.ICancellationAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IHedgingAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class BasicSlotPNLExporterExtension implements IExporterExtension {
	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private IOptimisationData optimisationData;
	
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
		for (final ISequenceElement element : optimisationData.getSequenceElements()) {
			assert element != null;
			final IPortSlot slot = slotProvider.getPortSlot(element);

			if (slot instanceof ILoadOption || slot instanceof IDischargeOption) {
				final Slot modelSlot = modelEntityMap.getModelObjectNullChecked(slot, Slot.class);

				final ProfitAndLossContainer profitAndLossContainer = exporterExtensionUtils.findProfitAndLossContainer(element, slot, modelEntityMap, outputSchedule, annotatedSolution);

				if (profitAndLossContainer != null) {

					final ICargoValueAnnotation cargoValueAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_cargoValueAllocationInfo,
							ICargoValueAnnotation.class);

					final IHedgingAnnotation hedgingAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_hedgingValue, IHedgingAnnotation.class);
					final ICancellationAnnotation cancellationAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_cancellationFees,
							ICancellationAnnotation.class);
					if (hedgingAnnotation != null || cancellationAnnotation != null || cargoValueAnnotation != null) {
						final BasicSlotPNLDetails details = ScheduleFactory.eINSTANCE.createBasicSlotPNLDetails();

						if (cargoValueAnnotation != null) {
							details.setAdditionalPNL(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotAdditionalOtherPNL(slotProvider.getPortSlot(element))));
							details.setExtraUpsidePNL(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotAdditionalUpsidePNL(slotProvider.getPortSlot(element))));
							details.setExtraShippingPNL(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotAdditionalShippingPNL(slotProvider.getPortSlot(element))));
						}
						if (hedgingAnnotation != null) {
							details.setHedgingValue(OptimiserUnitConvertor.convertToExternalFixedCost(hedgingAnnotation.getHedgingValue()));
						}
						if (cancellationAnnotation != null) {
							details.setCancellationFees(OptimiserUnitConvertor.convertToExternalFixedCost(cancellationAnnotation.getCancellationFees()));
						}

						ExporterExtensionUtils.addSlotPNLDetails(profitAndLossContainer, modelSlot, details);
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
