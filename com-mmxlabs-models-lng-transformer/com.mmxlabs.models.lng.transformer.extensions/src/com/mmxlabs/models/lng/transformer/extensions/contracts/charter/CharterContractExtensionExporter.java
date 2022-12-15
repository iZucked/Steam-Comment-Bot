/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.charter;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.LumpSumBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.LumpSumRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.util.SlotContractHelper;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.CharterContractConstants;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.CharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumBallastBonusTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.NotionalJourneyBallastBonusTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.OriginPortRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class CharterContractExtensionExporter implements IExporterExtension {

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private IOptimisationData optimisationData;

	@Inject
	private IVesselProvider vesselProvider;

	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {

		for (final IResource resource : optimisationData.getResources()) {
			final ISequence sequence = annotatedSolution.getFullSequences().getSequences().get(resource);
			if (sequence != null) {
				for (final ISequenceElement e : sequence) {
					generateRepositioningAnnotations(resource, e);
					generateBallastBonusAnnotations(resource, e);
				}

			}
		}
		// clear refs, just in case.
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}

	private void generateBallastBonusAnnotations(final IResource resource, final ISequenceElement endElement) {

		final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_profitAndLoss, IProfitAndLossAnnotation.class);
		final CharterContractAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, CharterContractConstants.BALLAST_BONUS_KEY, CharterContractAnnotation.class);
		if (annotation != null) {
			final ProfitAndLossContainer profitAndLossContainer;
			if (vesselProvider.getVesselCharter(resource).getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				final Slot<?> modelSlot = modelEntityMap.getModelObject(slotProvider.getPortSlot(endElement), Slot.class);

				CargoAllocation cargoAllocation = null;
				for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
					for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
						if (slotAllocation.getSlot() == modelSlot) {
							cargoAllocation = allocation;
							break;
						}
					}
				}
				profitAndLossContainer = cargoAllocation;
			} else {
				profitAndLossContainer = ExporterExtensionUtils.findEndEvent(endElement, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
			}
			
			if (profitAndLossContainer != null) {
				final CharterContractFeeDetails details = ScheduleFactory.eINSTANCE.createCharterContractFeeDetails();
				details.setFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				if (profitAndLossContainer instanceof final EndEvent endEvent) {
					endEvent.setBallastBonusFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				} else if (profitAndLossContainer instanceof final CargoAllocation allocation) {
					allocation.setBallastBonusFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				}
				if (profitAndLossContainer instanceof final CargoAllocation ca) {
					ca.setBallastBonusFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				}
				if (annotation.termAnnotation != null) {
					if (annotation.termAnnotation instanceof final LumpSumBallastBonusTermAnnotation ruleA) {
						final LumpSumBallastBonusTermDetails lumpSumContractDetails = ScheduleFactory.eINSTANCE.createLumpSumBallastBonusTermDetails();
						lumpSumContractDetails.setMatchedPort(annotation.matchedPort.getName());
						lumpSumContractDetails.setLumpSum(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
						details.setMatchingContractDetails(lumpSumContractDetails);
					} else if (annotation.termAnnotation instanceof final NotionalJourneyBallastBonusTermAnnotation ruleA) {
						final NotionalJourneyBallastBonusTermDetails notionalJourneyContractDetails = ScheduleFactory.eINSTANCE.createNotionalJourneyBallastBonusTermDetails();
						notionalJourneyContractDetails.setMatchedPort(annotation.matchedPort.getName());

						notionalJourneyContractDetails.setReturnPort(ruleA.returnPort.getName());
						notionalJourneyContractDetails.setDistance(ruleA.distance);

						notionalJourneyContractDetails.setLumpSum(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
						notionalJourneyContractDetails.setFuelPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.fuelPrice));
						notionalJourneyContractDetails.setTotalFuelUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalFuelUsed));
						notionalJourneyContractDetails.setTotalFuelCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalFuelCost));
						notionalJourneyContractDetails.setLngPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.lngPrice));
						notionalJourneyContractDetails.setTotalLNGUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalLNGUsed));
						notionalJourneyContractDetails.setTotalLNGCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalLNGCost));

						notionalJourneyContractDetails.setTotalTimeInDays(((double) ruleA.totalTimeInHours) / 24.0);
						notionalJourneyContractDetails.setHireRate(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.hireRate));
						notionalJourneyContractDetails.setHireCost(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.totalHireCost));

						notionalJourneyContractDetails.setRouteTaken(ruleA.route.name());
						notionalJourneyContractDetails.setCanalCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.canalCost));
						details.setMatchingContractDetails(notionalJourneyContractDetails);
					}
				}
				profitAndLossContainer.getGeneralPNLDetails().add(details);
			}
		}
	}

	private void generateRepositioningAnnotations(final IResource resource, final ISequenceElement startElement) {

		final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_profitAndLoss, IProfitAndLossAnnotation.class);
		final CharterContractAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, CharterContractConstants.REPOSITIONING_FEE_KEY, CharterContractAnnotation.class);
		if (annotation != null) {
			final ProfitAndLossContainer profitAndLossContainer;
			if (vesselProvider.getVesselCharter(resource).getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				final Slot modelSlot = modelEntityMap.getModelObject(slotProvider.getPortSlot(startElement), Slot.class);
				CargoAllocation cargoAllocation = null;
				for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
					for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
						if (slotAllocation.getSlot() == modelSlot) {
							cargoAllocation = allocation;
							break;
						}
					}
				}
				profitAndLossContainer = cargoAllocation;
			} else {
				profitAndLossContainer = ExporterExtensionUtils.findStartEvent(startElement, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
			}

			if (profitAndLossContainer != null) {
				final CharterContractFeeDetails details = ScheduleFactory.eINSTANCE.createCharterContractFeeDetails();
				details.setFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				if (profitAndLossContainer instanceof final StartEvent startEvent) {
					startEvent.setRepositioningFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				} else if (profitAndLossContainer instanceof final CargoAllocation allocation) {
					allocation.setRepositioningFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				}
				if (profitAndLossContainer instanceof final CargoAllocation ca) {
					ca.setRepositioningFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
				}
				if (annotation.termAnnotation instanceof final OriginPortRepositioningFeeTermAnnotation ruleA) {
					final OriginPortRepositioningFeeTermDetails originPortContractDetails = ScheduleFactory.eINSTANCE.createOriginPortRepositioningFeeTermDetails();
					originPortContractDetails.setMatchedPort(annotation.matchedPort.getName());

					originPortContractDetails.setOriginPort(ruleA.originPort.getName());
					originPortContractDetails.setDistance(ruleA.distance);

					originPortContractDetails.setLumpSum(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
					originPortContractDetails.setFuelPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.fuelPrice));
					originPortContractDetails.setTotalFuelUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalFuelUsed));
					originPortContractDetails.setTotalFuelCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalFuelCost));

					originPortContractDetails.setTotalTimeInDays(((double) ruleA.totalTimeInHours) / 24.0);
					originPortContractDetails.setHireRate(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.hireRate));
					originPortContractDetails.setHireCost(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.totalHireCost));

					originPortContractDetails.setRouteTaken(ruleA.route.name());
					originPortContractDetails.setCanalCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.canalCost));
					details.setMatchingContractDetails(originPortContractDetails);
				} else if (annotation.termAnnotation instanceof final LumpSumRepositioningFeeTermAnnotation ruleA) {
					final LumpSumRepositioningFeeTermDetails ruleDetails = ScheduleFactory.eINSTANCE.createLumpSumRepositioningFeeTermDetails();
					ruleDetails.setMatchedPort(annotation.matchedPort.getName());
					ruleDetails.setLumpSum(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
					details.setMatchingContractDetails(ruleDetails);
				}
				profitAndLossContainer.getGeneralPNLDetails().add(details);
			}
		}
	}
}
