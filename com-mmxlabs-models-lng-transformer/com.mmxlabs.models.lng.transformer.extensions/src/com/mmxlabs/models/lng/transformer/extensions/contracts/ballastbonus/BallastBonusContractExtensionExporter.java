package com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus;

import java.time.YearMonth;
import java.time.ZonedDateTime;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails;
import com.mmxlabs.models.lng.schedule.LumpSumContractDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.extensions.contracts.utils.ShippingAnnotation;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.BallastBonusAnnotation;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.LumpSumBallastBonusRuleAnnotation;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.NotionalJourneyBallastBonusRuleAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class BallastBonusContractExtensionExporter {

	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private ExporterExtensionUtils exporterExtensionUtils;

	@Inject
	private IOptimisationData optimisationData;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	public void finishExporting() {
		for (final ISequenceElement element : optimisationData.getSequenceElements()) {
			final IPortSlot slot = slotProvider.getPortSlot(element);

			if (slot instanceof IEndPortSlot) {
				final Slot modelSlot = modelEntityMap.getModelObjectNullChecked(slot, Slot.class);
				final ProfitAndLossContainer profitAndLossContainer = exporterExtensionUtils.findProfitAndLossContainer(element, slot, modelEntityMap, outputSchedule, annotatedSolution);

				if (profitAndLossContainer != null) {

					// Find child
					final BallastBonusAnnotation annotation = ExporterExtensionUtils.findElementAnnotation(element, profitAndLossContainer, annotatedSolution, BallastBonusAnnotation.ANNOTATION_KEY,
							BallastBonusAnnotation.class);
					if (annotation != null) {
						final BallastBonusFeeDetails details = ScheduleFactory.eINSTANCE.createBallastBonusFeeDetails();
						details.setFee((int) OptimiserUnitConvertor.convertToExternalConversionFactor(annotation.ballastBonusFee));
						if (annotation.ballastBonusRuleAnnotation != null) {
							if (annotation.ballastBonusRuleAnnotation instanceof LumpSumBallastBonusRuleAnnotation) {
								LumpSumBallastBonusRuleAnnotation ruleA = (LumpSumBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
								LumpSumContractDetails lumpSumContractDetails = ScheduleFactory.eINSTANCE.createLumpSumContractDetails();
								lumpSumContractDetails.setMatchedPort(annotation.matchedPort.getName());
								lumpSumContractDetails.setLumpSum((int) OptimiserUnitConvertor.convertToExternalConversionFactor(annotation.ballastBonusFee));
							} else if (annotation.ballastBonusRuleAnnotation instanceof NotionalJourneyBallastBonusRuleAnnotation) {
								NotionalJourneyBallastBonusRuleAnnotation ruleA = (NotionalJourneyBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
								NotionalJourneyContractDetails notionalJourneyContractDetails = ScheduleFactory.eINSTANCE.createNotionalJourneyContractDetails();
								notionalJourneyContractDetails.setReturnPort(ruleA.returnPort.getName());
								notionalJourneyContractDetails.setDistance(ruleA.distance);
								
								notionalJourneyContractDetails.setFuelPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.fuelPrice));
								notionalJourneyContractDetails.setTotalFuelUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalFuelUsed));
								notionalJourneyContractDetails.setTotalFuelCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalFuelUsed));
								
								notionalJourneyContractDetails.setTotalTimeInDays(((double)ruleA.totalTimeInHours)/24.0);
								notionalJourneyContractDetails.setHireRate(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.hireRate));
								notionalJourneyContractDetails.setHireCost(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.totalHireCost));
								
								notionalJourneyContractDetails.setRouteTaken(ruleA.route.name());
								notionalJourneyContractDetails.setCanalCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.canalCost));
							}
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
