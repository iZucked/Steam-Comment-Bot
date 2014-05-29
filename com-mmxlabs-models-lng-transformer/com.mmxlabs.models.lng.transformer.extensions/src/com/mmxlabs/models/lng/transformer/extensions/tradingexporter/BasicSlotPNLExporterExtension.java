/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import org.eclipse.core.commands.operations.ObjectUndoContext;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.ICancellationAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IHedgingAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class BasicSlotPNLExporterExtension implements IExporterExtension {
	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IPortSlotProvider slotProvider;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
		for (final ISequenceElement element : annotatedSolution.getContext().getOptimisationData().getSequenceElements()) {
			final IPortSlot slot = slotProvider.getPortSlot(element);

			ProfitAndLossContainer profitAndLossContainer = null;

			if (slot instanceof ILoadOption || slot instanceof IDischargeOption) {
				final Slot modelSlot = modelEntityMap.getModelObject(slot, Slot.class);
				for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
					for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
						if (slotAllocation.getSlot() == modelSlot) {
							profitAndLossContainer = allocation;
							break;
						}
					}
				}
				if (profitAndLossContainer == null) {

					OpenSlotAllocation openSlotAllocation = null;
					for (final OpenSlotAllocation allocation : outputSchedule.getOpenSlotAllocations()) {
						if (allocation.getSlot() == modelSlot) {
							openSlotAllocation = allocation;
							break;
						}
					}
					if (openSlotAllocation != null) {
						profitAndLossContainer = openSlotAllocation;
					} else {
						MarketAllocation marketAllocation = null;
						for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
							if (allocation.getSlot() == modelSlot) {
								marketAllocation = allocation;
								break;
							}
						}
						if (marketAllocation != null) {
							profitAndLossContainer = marketAllocation;
						}
					}
				}

				if (profitAndLossContainer != null) {

					final IHedgingAnnotation hedgingAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_hedgingValue, IHedgingAnnotation.class);
					final ICancellationAnnotation cancellationAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_cancellationFees,
							ICancellationAnnotation.class);
					if (hedgingAnnotation != null || cancellationAnnotation != null) {
						BasicSlotPNLDetails details = ScheduleFactory.eINSTANCE.createBasicSlotPNLDetails();
						if (hedgingAnnotation != null) {
							details.setHedgingValue(OptimiserUnitConvertor.convertToExternalFixedCost(hedgingAnnotation.getHedgingValue()));
						}
						if (cancellationAnnotation != null) {
							details.setCancellationFees(OptimiserUnitConvertor.convertToExternalFixedCost(cancellationAnnotation.getCancellationFees()));
						}

						SlotPNLDetails slotDetails = null;
						for (GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
							if (generalPNLDetails instanceof SlotPNLDetails) {
								SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
								if (slotPNLDetails.getSlot() == modelSlot) {
									slotDetails = slotPNLDetails;
								}
							}
						}
						if (slotDetails == null) {
							slotDetails = ScheduleFactory.eINSTANCE.createSlotPNLDetails();
							slotDetails.setSlot(modelSlot);
							profitAndLossContainer.getGeneralPNLDetails().add(slotDetails);
						}

						slotDetails.getGeneralPNLDetails().add(details);
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
