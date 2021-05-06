/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.charter;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.schedule.CharterContractFeeDetails;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.LumpSumBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.LumpSumRepositioningFeeTermDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyBallastBonusTermDetails;
import com.mmxlabs.models.lng.schedule.OriginPortRepositioningFeeTermDetails;
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
import com.mmxlabs.scheduler.optimiser.chartercontracts.CharterContractConstants;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.CharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumBallastBonusTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.NotionalJourneyBallastBonusTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.OriginPortRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class CharterContractExtensionExporter implements IExporterExtension {

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
					final CharterContractAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, CharterContractConstants.REPOSITIONING_FEE_KEY, CharterContractAnnotation.class);
					if (annotation != null) {
						final StartEvent profitAndLossContainer = ExporterExtensionUtils.findStartEvent(startElement, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
						if (profitAndLossContainer != null) {
							final CharterContractFeeDetails details = ScheduleFactory.eINSTANCE.createCharterContractFeeDetails();
							details.setFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
							profitAndLossContainer.setRepositioningFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
							if (annotation.termAnnotation instanceof OriginPortRepositioningFeeTermAnnotation) {

								final OriginPortRepositioningFeeTermAnnotation ruleA = (OriginPortRepositioningFeeTermAnnotation) annotation.termAnnotation;
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
							} else if (annotation.termAnnotation instanceof LumpSumRepositioningFeeTermAnnotation) {
								final LumpSumRepositioningFeeTermAnnotation ruleA = (LumpSumRepositioningFeeTermAnnotation) annotation.termAnnotation;
								final LumpSumRepositioningFeeTermDetails ruleDetails = ScheduleFactory.eINSTANCE.createLumpSumRepositioningFeeTermDetails();
								ruleDetails.setMatchedPort(annotation.matchedPort.getName());

								ruleDetails.setOriginPort(ruleA.originPort.getName());
								ruleDetails.setLumpSum(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
								details.setMatchingContractDetails(ruleDetails);
							}
							profitAndLossContainer.getGeneralPNLDetails().add(details);
						}
					}
				}
				{
					final ISequenceElement endElement = sequence.get(sequence.size() - 1);

					final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(endElement, SchedulerConstants.AI_profitAndLoss,
							IProfitAndLossAnnotation.class);
					final CharterContractAnnotation annotation = SlotContractHelper.findDetailsAnnotation(profitAndLoss, CharterContractConstants.BALLAST_BONUS_KEY, CharterContractAnnotation.class);
					if (annotation != null) {
						final EndEvent profitAndLossContainer = ExporterExtensionUtils.findEndEvent(endElement, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
						if (profitAndLossContainer != null) {
							final CharterContractFeeDetails details = ScheduleFactory.eINSTANCE.createCharterContractFeeDetails();
							details.setFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
							profitAndLossContainer.setBallastBonusFee(OptimiserUnitConvertor.convertToExternalFixedCost(annotation.cost));
							if (annotation.termAnnotation != null) {
								if (annotation.termAnnotation instanceof LumpSumBallastBonusTermAnnotation) {
									final LumpSumBallastBonusTermAnnotation ruleA = (LumpSumBallastBonusTermAnnotation) annotation.termAnnotation;
									final LumpSumBallastBonusTermDetails lumpSumContractDetails = ScheduleFactory.eINSTANCE.createLumpSumBallastBonusTermDetails();
									lumpSumContractDetails.setMatchedPort(annotation.matchedPort.getName());
									lumpSumContractDetails.setLumpSum(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
									details.setMatchingContractDetails(lumpSumContractDetails);
								} else if (annotation.termAnnotation instanceof NotionalJourneyBallastBonusTermAnnotation) {
									final NotionalJourneyBallastBonusTermAnnotation ruleA = (NotionalJourneyBallastBonusTermAnnotation) annotation.termAnnotation;
									final NotionalJourneyBallastBonusTermDetails notionalJourneyContractDetails = ScheduleFactory.eINSTANCE.createNotionalJourneyBallastBonusTermDetails();
									notionalJourneyContractDetails.setMatchedPort(annotation.matchedPort.getName());

									notionalJourneyContractDetails.setReturnPort(ruleA.returnPort.getName());
									notionalJourneyContractDetails.setDistance(ruleA.distance);

									notionalJourneyContractDetails.setLumpSum(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
									notionalJourneyContractDetails.setFuelPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.fuelPrice));
									notionalJourneyContractDetails.setTotalFuelUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalFuelUsed));
									notionalJourneyContractDetails.setTotalFuelCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalFuelCost));

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
			}

		}
		// clear refs, just in case.
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}

}