/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.LumpSumContractDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
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
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.BallastBonusAnnotation;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.LumpSumBallastBonusRuleAnnotation;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.NotionalJourneyBallastBonusRuleAnnotation;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.RepositioningFeeAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class BallastBonusContractExtensionExporter implements IExporterExtension {

	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IOptimisationData optimisationData;

	@Inject
	private IVesselProvider vesselProvider;

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
			if (sequence != null && sequence.size() > 1) {

				{
					final ISequenceElement startElement = sequence.get(0);

					final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(startElement, SchedulerConstants.AI_profitAndLoss,
							IProfitAndLossAnnotation.class);
					final RepositioningFeeAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, RepositioningFeeAnnotation.ANNOTATION_KEY, RepositioningFeeAnnotation.class);
					if (annotation != null) {
						final StartEvent profitAndLossContainer = ExporterExtensionUtils.findStartEvent(startElement, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
						if (profitAndLossContainer != null) {
							profitAndLossContainer.setRepositioningFee((int) OptimiserUnitConvertor.convertToExternalFixedCost(annotation.repositioningFee));
						}
					}
				}
				{
					final ISequenceElement endElement = sequence.get(sequence.size() - 1);

					final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_profitAndLoss,
							IProfitAndLossAnnotation.class);
					final BallastBonusAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, BallastBonusAnnotation.ANNOTATION_KEY, BallastBonusAnnotation.class);
					if (annotation != null) {
						final EndEvent profitAndLossContainer = ExporterExtensionUtils.findEndEvent(endElement, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
						if (profitAndLossContainer != null) {
							final BallastBonusFeeDetails details = ScheduleFactory.eINSTANCE.createBallastBonusFeeDetails();
							details.setFee((int) OptimiserUnitConvertor.convertToExternalFixedCost(annotation.ballastBonusFee));
							profitAndLossContainer.setBallastBonusFee((int) OptimiserUnitConvertor.convertToExternalFixedCost(annotation.ballastBonusFee));
							if (annotation.ballastBonusRuleAnnotation != null) {
								if (annotation.ballastBonusRuleAnnotation instanceof LumpSumBallastBonusRuleAnnotation) {
									final LumpSumBallastBonusRuleAnnotation ruleA = (LumpSumBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
									final LumpSumContractDetails lumpSumContractDetails = ScheduleFactory.eINSTANCE.createLumpSumContractDetails();
									lumpSumContractDetails.setMatchedPort(annotation.matchedPort.getName());
									lumpSumContractDetails.setLumpSum((int) OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
									details.setMatchingBallastBonusContractDetails(lumpSumContractDetails);
								} else if (annotation.ballastBonusRuleAnnotation instanceof NotionalJourneyBallastBonusRuleAnnotation) {
									final NotionalJourneyBallastBonusRuleAnnotation ruleA = (NotionalJourneyBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
									final NotionalJourneyContractDetails notionalJourneyContractDetails = ScheduleFactory.eINSTANCE.createNotionalJourneyContractDetails();
									notionalJourneyContractDetails.setMatchedPort(annotation.matchedPort.getName());

									notionalJourneyContractDetails.setReturnPort(ruleA.returnPort.getName());
									notionalJourneyContractDetails.setDistance(ruleA.distance);

									notionalJourneyContractDetails.setLumpSum((int) OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
									notionalJourneyContractDetails.setFuelPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.fuelPrice));
									notionalJourneyContractDetails.setTotalFuelUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalFuelUsed));
									notionalJourneyContractDetails.setTotalFuelCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalFuelCost));

									notionalJourneyContractDetails.setTotalTimeInDays(((double) ruleA.totalTimeInHours) / 24.0);
									notionalJourneyContractDetails.setHireRate(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.hireRate));
									notionalJourneyContractDetails.setHireCost(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.totalHireCost));

									notionalJourneyContractDetails.setRouteTaken(ruleA.route.name());
									notionalJourneyContractDetails.setCanalCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.canalCost));
									details.setMatchingBallastBonusContractDetails(notionalJourneyContractDetails);
								}
							}
							// ExporterExtensionUtils.addSlotPNLDetails(profitAndLossContainer, modelSlot, details);
							profitAndLossContainer.getGeneralPNLDetails().add(details);
						}
					}
				}
			}

		}
		//
		// for (final ISequenceElement element : optimisationData.getSequenceElements()) {
		//
		// final IPortSlot slot = slotProvider.getPortSlot(element);
		// if (slot instanceof IEndPortSlot) {
		// EndEvent profitAndLossContainer = findEndEvent(element);
		// if (profitAndLossContainer == null) {
		// profitAndLossContainer = exporterExtensionUtils.findEndEvent(element, modelEntityMap, outputSchedule, annotatedSolution);
		// }
		// // final Slot modelSlot = modelEntityMap.getModelObjectNullChecked(slot, Slot.class);
		// // final ProfitAndLossContainer profitAndLossContainer = exporterExtensionUtils.findProfitAndLossContainer(element, slot, modelEntityMap, outputSchedule, annotatedSolution);
		//
		// if (profitAndLossContainer != null) {
		//
		// final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss,
		// IProfitAndLossAnnotation.class);
		// BallastBonusAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, BallastBonusAnnotation.ANNOTATION_KEY, BallastBonusAnnotation.class);
		// if (annotation != null) {
		// final BallastBonusFeeDetails details = ScheduleFactory.eINSTANCE.createBallastBonusFeeDetails();
		// details.setFee((int) OptimiserUnitConvertor.convertToExternalFixedCost(annotation.ballastBonusFee));
		// if (annotation.ballastBonusRuleAnnotation != null) {
		// if (annotation.ballastBonusRuleAnnotation instanceof LumpSumBallastBonusRuleAnnotation) {
		// LumpSumBallastBonusRuleAnnotation ruleA = (LumpSumBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
		// LumpSumContractDetails lumpSumContractDetails = ScheduleFactory.eINSTANCE.createLumpSumContractDetails();
		// lumpSumContractDetails.setMatchedPort(annotation.matchedPort.getName());
		// lumpSumContractDetails.setLumpSum((int) OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
		// details.setMatchingBallastBonusContractDetails(lumpSumContractDetails);
		// } else if (annotation.ballastBonusRuleAnnotation instanceof NotionalJourneyBallastBonusRuleAnnotation) {
		// NotionalJourneyBallastBonusRuleAnnotation ruleA = (NotionalJourneyBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
		// NotionalJourneyContractDetails notionalJourneyContractDetails = ScheduleFactory.eINSTANCE.createNotionalJourneyContractDetails();
		// notionalJourneyContractDetails.setMatchedPort(annotation.matchedPort.getName());
		//
		// notionalJourneyContractDetails.setReturnPort(ruleA.returnPort.getName());
		// notionalJourneyContractDetails.setDistance(ruleA.distance);
		//
		// notionalJourneyContractDetails.setFuelPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.fuelPrice));
		// notionalJourneyContractDetails.setTotalFuelUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalFuelUsed));
		// notionalJourneyContractDetails.setTotalFuelCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalFuelCost));
		//
		// notionalJourneyContractDetails.setTotalTimeInDays(((double) ruleA.totalTimeInHours) / 24.0);
		// notionalJourneyContractDetails.setHireRate(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.hireRate));
		// notionalJourneyContractDetails.setHireCost(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.totalHireCost));
		//
		// notionalJourneyContractDetails.setRouteTaken(ruleA.route.name());
		// notionalJourneyContractDetails.setCanalCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.canalCost));
		// details.setMatchingBallastBonusContractDetails(notionalJourneyContractDetails);
		// }
		// }
		// // ExporterExtensionUtils.addSlotPNLDetails(profitAndLossContainer, modelSlot, details);
		// profitAndLossContainer.getGeneralPNLDetails().add(details);
		// }
		// }
		// }
		// }

		// clear refs, just in case.
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}

}
