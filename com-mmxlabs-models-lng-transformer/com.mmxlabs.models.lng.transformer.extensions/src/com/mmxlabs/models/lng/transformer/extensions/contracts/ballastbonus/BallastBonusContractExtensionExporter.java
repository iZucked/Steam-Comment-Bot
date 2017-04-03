package com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus;

import java.util.Map;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.BallastBonusFeeDetails;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.LumpSumContractDetails;
import com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
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
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.BallastBonusAnnotation;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.LumpSumBallastBonusRuleAnnotation;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.NotionalJourneyBallastBonusRuleAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class BallastBonusContractExtensionExporter implements IExporterExtension {

	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private ExporterExtensionUtils exporterExtensionUtils;

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

	public void finishExporting() {
		for (final ISequenceElement element : optimisationData.getSequenceElements()) {
			final IPortSlot slot = slotProvider.getPortSlot(element);
			if (slot instanceof IEndPortSlot) {
				EndEvent profitAndLossContainer = findEndEvent(element);
//				final Slot modelSlot = modelEntityMap.getModelObjectNullChecked(slot, Slot.class);
//				final ProfitAndLossContainer profitAndLossContainer = exporterExtensionUtils.findProfitAndLossContainer(element, slot, modelEntityMap, outputSchedule, annotatedSolution);

				if (profitAndLossContainer != null) {

					
					final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss,
							IProfitAndLossAnnotation.class);
					BallastBonusAnnotation annotation= SlotContractHelper.findDetailsAnnotation(profitAndLoss,   BallastBonusAnnotation.ANNOTATION_KEY,
							BallastBonusAnnotation.class);
					if (annotation != null) {
						final BallastBonusFeeDetails details = ScheduleFactory.eINSTANCE.createBallastBonusFeeDetails();
						details.setFee((int) OptimiserUnitConvertor.convertToExternalFixedCost(annotation.ballastBonusFee));
						if (annotation.ballastBonusRuleAnnotation != null) {
							if (annotation.ballastBonusRuleAnnotation instanceof LumpSumBallastBonusRuleAnnotation) {
								LumpSumBallastBonusRuleAnnotation ruleA = (LumpSumBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
								LumpSumContractDetails lumpSumContractDetails = ScheduleFactory.eINSTANCE.createLumpSumContractDetails();
								lumpSumContractDetails.setMatchedPort(annotation.matchedPort.getName());
								lumpSumContractDetails.setLumpSum((int) OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.lumpSum));
								details.setMatchingBallastBonusContractDetails(lumpSumContractDetails);
							} else if (annotation.ballastBonusRuleAnnotation instanceof NotionalJourneyBallastBonusRuleAnnotation) {
								NotionalJourneyBallastBonusRuleAnnotation ruleA = (NotionalJourneyBallastBonusRuleAnnotation) annotation.ballastBonusRuleAnnotation;
								NotionalJourneyContractDetails notionalJourneyContractDetails = ScheduleFactory.eINSTANCE.createNotionalJourneyContractDetails();
								notionalJourneyContractDetails.setMatchedPort(annotation.matchedPort.getName());

								notionalJourneyContractDetails.setReturnPort(ruleA.returnPort.getName());
								notionalJourneyContractDetails.setDistance(ruleA.distance);
								
								notionalJourneyContractDetails.setFuelPrice(OptimiserUnitConvertor.convertToExternalPrice(ruleA.fuelPrice));
								notionalJourneyContractDetails.setTotalFuelUsed(OptimiserUnitConvertor.convertToExternalVolume(ruleA.totalFuelUsed));
								notionalJourneyContractDetails.setTotalFuelCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.totalFuelCost));
								
								notionalJourneyContractDetails.setTotalTimeInDays(((double)ruleA.totalTimeInHours)/24.0);
								notionalJourneyContractDetails.setHireRate(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.hireRate));
								notionalJourneyContractDetails.setHireCost(OptimiserUnitConvertor.convertToExternalDailyCost(ruleA.totalHireCost));
								
								notionalJourneyContractDetails.setRouteTaken(ruleA.route.name());
								notionalJourneyContractDetails.setCanalCost(OptimiserUnitConvertor.convertToExternalFixedCost(ruleA.canalCost));
								details.setMatchingBallastBonusContractDetails(notionalJourneyContractDetails);
							}
						}
//						ExporterExtensionUtils.addSlotPNLDetails(profitAndLossContainer, modelSlot, details);
						profitAndLossContainer.getGeneralPNLDetails().add(details);
					}
				}
			}
		}

		// clear refs, just in case.
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}
	
	private EndEvent findEndEvent(final ISequenceElement element) {
		EndEvent endEvent = null;
		//
		// for (int i = 0; i < annotatedSolution.getFullSequences().size(); ++i) {
		LOOP_OUTER: for (final Map.Entry<IResource, ISequence> e : annotatedSolution.getFullSequences().getSequences().entrySet()) {
			final IResource res = e.getKey();
			final ISequence seq = e.getValue();
			if (seq.size() == 0) {
				continue;
			}
			if (seq.get(seq.size() - 1) == element) {
				for (final Sequence sequence : outputSchedule.getSequences()) {
					final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
					if (vesselAvailability == null) {
						continue;
					}
					// Find the matching
					final IVesselAvailability o_VesselAvailability = modelEntityMap.getOptimiserObject(vesselAvailability, IVesselAvailability.class);

					// Look up correct instance (NOTE: Even though IVessel extends IResource, they seem to be different instances.
					if (o_VesselAvailability == vesselProvider.getVesselAvailability(res)) {
						if (sequence.getEvents().size() > 0) {
							final Event evt = sequence.getEvents().get(sequence.getEvents().size() - 1);
							if (evt instanceof EndEvent) {
								endEvent = (EndEvent) evt;
								break LOOP_OUTER;
							}
						}
					}

				}
			}
		}
		return endEvent;
	}


}
