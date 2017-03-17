/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator.EvaluationMode;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link ProfitAndLossCalculator} performs {@link ISequences} wide processing of P&L calculations.
 * 
 * 
 * @author Simon Goodall
 */
public class ProfitAndLossCalculator {

	@Inject
	private Provider<IVolumeAllocator> volumeAllocatorProvider;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject(optional = true)
	private Provider<IEntityValueCalculator> entityValueCalculatorProvider;

	@Inject(optional = true)
	private IMarkToMarketProvider markToMarketProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Nullable
	public ProfitAndLossSequences calculateProfitAndLoss(final @NonNull ISequences sequences, @NonNull final VolumeAllocatedSequences volumeAllocatedSequences,
			final @Nullable IAnnotatedSolution annotatedSolution) {

		final ProfitAndLossSequences profitAndLossSequences = new ProfitAndLossSequences(volumeAllocatedSequences);
		// Some tests cases have no EVC
		if (entityValueCalculatorProvider == null) {
			return profitAndLossSequences;
		}

		// 2014-02-12 - SG
		// Bad hack to allow a custom contract to reset cached data AFTER any charter out generation / break -even code has run.
		// TODO: Need a better phase system to notify components where we are in the process.
		{
			for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
				shippingCalculator.prepareRealSalesPNL();
			}
			for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
				calculator.prepareRealPurchasePNL();
			}
		}

		for (final VolumeAllocatedSequence sequence : profitAndLossSequences.getVolumeAllocatedSequences()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(sequence.getResource());
			assert vesselAvailability != null;

			int time = sequence.getStartTime();

			// for (final VoyagePlan plan : sequence.getVoyagePlans()) {
			for (final Pair<VoyagePlan, IPortTimesRecord> entry : sequence.getVoyagePlans()) {
				boolean cargo = false;
				final VoyagePlan plan = entry.getFirst();
				final IPortTimesRecord portTimesRecord = entry.getSecond();
				final IAllocationAnnotation currentAllocation = (portTimesRecord instanceof IAllocationAnnotation) ? (IAllocationAnnotation) portTimesRecord : null;
				if (plan.getSequence().length >= 2) {

					// Extract list of all the PortDetails encountered
					final List<@NonNull PortDetails> portDetails = new LinkedList<>();
					for (final Object obj : plan.getSequence()) {
						if (obj instanceof PortDetails) {
							portDetails.add((PortDetails) obj);
						}
					}

					// TODO: this logic looks decidedly shaky - plan sequence length could change with logic changes
					final boolean isDesFobCase = ((vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE
							|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan.getSequence().length == 2);
					if (currentAllocation != null) {
						final CargoValueAnnotation cargoValueAnnotation;// = new CargoValueAnnotation(currentAllocation);
						cargo = true;
						if (isDesFobCase) {
							// for now, only handle single load/discharge case
							assert (currentAllocation.getSlots().size() == 2);
							final ILoadOption loadSlot = (ILoadOption) currentAllocation.getSlots().get(0);
							final Pair<@NonNull CargoValueAnnotation, @NonNull Long> p = entityValueCalculatorProvider.get().evaluate(EvaluationMode.FullPNL, plan, currentAllocation,
									vesselAvailability, currentAllocation.getSlotTime(loadSlot), volumeAllocatedSequences, annotatedSolution);
							cargoValueAnnotation = p.getFirst();
							final long cargoGroupValue = p.getSecond();
							profitAndLossSequences.setVoyagePlanGroupValue(plan, cargoGroupValue);
						} else {
							final Pair<@NonNull CargoValueAnnotation, @NonNull Long> p = entityValueCalculatorProvider.get().evaluate(EvaluationMode.FullPNL, plan, currentAllocation,
									vesselAvailability, sequence.getStartTime(), volumeAllocatedSequences, annotatedSolution);
							cargoValueAnnotation = p.getFirst();
							final long cargoGroupValue = p.getSecond();
							profitAndLossSequences.setVoyagePlanGroupValue(plan, cargoGroupValue);
						}

						// Store annotations if required
						if (annotatedSolution != null) {
							// now add some more data for each load slot
							final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
							final List<@NonNull IPortSlot> slots = currentAllocation.getSlots();
							for (final IPortSlot portSlot : slots) {
								final ISequenceElement portElement = portSlotProvider.getElement(portSlot);
								elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_cargoValueAllocationInfo, cargoValueAnnotation);
							}
						}
					}
				}

				if (!cargo) {
					final long otherGroupValue = entityValueCalculatorProvider.get().evaluate(EvaluationMode.FullPNL, plan, portTimesRecord, vesselAvailability, time, sequence.getStartTime(),
							volumeAllocatedSequences, annotatedSolution);
					profitAndLossSequences.setVoyagePlanGroupValue(plan, otherGroupValue);
				}
				time += getPlanDuration(plan);
			}
		}

		calculateUnusedSlotPNL(sequences, profitAndLossSequences, annotatedSolution);

		if (annotatedSolution != null && markToMarketProvider != null) {
			// calculateMarkToMarketPNL(sequences, annotatedSolution);
		}

		return profitAndLossSequences;
	}

	protected void calculateUnusedSlotPNL(final ISequences sequences, final ProfitAndLossSequences profitAndLossSequences, @Nullable final IAnnotatedSolution annotatedSolution) {
		@NonNull
		final VolumeAllocatedSequences volumeAllocatedSequences = profitAndLossSequences.getVolumeAllocatedSequences();
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			if (portSlot instanceof ILoadOption || portSlot instanceof IDischargeOption) {
				// Calculate P&L
				final long groupValue = entityValueCalculatorProvider.get().evaluateUnusedSlot(EvaluationMode.FullPNL, portSlot, volumeAllocatedSequences, annotatedSolution);
				profitAndLossSequences.setUnusedSlotGroupValue(portSlot, groupValue);
			}
		}
	}

	protected void calculateMarkToMarketPNL(final @NonNull ISequences sequences, final @NonNull IAnnotatedSolution annotatedSolution) {
		// Mark-to-Market Calculations
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			final IMarkToMarket market = markToMarketProvider.getMarketForElement(element);
			if (market == null) {
				continue;
			}

			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

			final ILoadOption loadOption;
			final IDischargeOption dischargeOption;
			final int time;

			final IVesselAvailability vesselAvailability;
			if (portSlot instanceof ILoadOption) {
				loadOption = (ILoadOption) portSlot;
				if (loadOption instanceof ILoadSlot) {
					dischargeOption = new MarkToMarketDischargeOption(market, loadOption);
				} else {
					dischargeOption = new MarkToMarketDischargeSlot(market, loadOption);
				}
				@Nullable
				ITimeWindow timeWindow = loadOption.getTimeWindow();
				assert timeWindow != null;
				time = timeWindow.getInclusiveStart();
				vesselAvailability = new MarkToMarketVesselAvailability(market, loadOption);
			} else if (portSlot instanceof IDischargeOption) {
				dischargeOption = (IDischargeOption) portSlot;
				if (dischargeOption instanceof IDischargeSlot) {
					loadOption = new MarkToMarketLoadOption(market, dischargeOption);
				} else {
					loadOption = new MarkToMarketLoadSlot(market, dischargeOption);
				}
				@Nullable
				ITimeWindow timeWindow = dischargeOption.getTimeWindow();
				assert timeWindow != null;
				time = timeWindow.getInclusiveStart();
				vesselAvailability = new MarkToMarketVesselAvailability(market, dischargeOption);
			} else {
				continue;
			}

			final PortTimesRecord portTimesRecord = new PortTimesRecord();
			portTimesRecord.setSlotTime(loadOption, time);
			portTimesRecord.setSlotTime(dischargeOption, time);
			portTimesRecord.setSlotDuration(loadOption, 0);
			portTimesRecord.setSlotDuration(dischargeOption, 0);

			// Create voyage plan
			final VoyagePlan voyagePlan = new VoyagePlan();
			{
				final PortOptions loadOptions = new PortOptions(loadOption);
				final PortDetails loadDetails = new PortDetails(loadOptions);
				loadOptions.setVisitDuration(0);

				final PortOptions dischargeOptions = new PortOptions(dischargeOption);
				final PortDetails dischargeDetails = new PortDetails(dischargeOptions);
				dischargeOptions.setVisitDuration(0);

				voyagePlan.setSequence(new IDetailsSequenceElement[] { loadDetails, dischargeDetails });
			}
			voyagePlan.setIgnoreEnd(false);
			// Create an allocation annotation.
			final IAllocationAnnotation allocationAnnotation = volumeAllocatorProvider.get().allocate(vesselAvailability, time, voyagePlan, portTimesRecord);
			if (allocationAnnotation != null) {
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);
				// Calculate P&L
				final Pair<@NonNull CargoValueAnnotation, @NonNull Long> p = entityValueCalculatorProvider.get().evaluate(EvaluationMode.FullPNL, voyagePlan, allocationAnnotation, vesselAvailability,
						time, null, annotatedSolution);
				final CargoValueAnnotation cargoValueAnnotation = p.getFirst();

				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_cargoValueAllocationInfo, cargoValueAnnotation);
			}
		}
	}

	private int getPlanDuration(final @NonNull VoyagePlan plan) {
		return getPartialPlanDuration(plan, 0);
	}

	private int getPartialPlanDuration(final @NonNull VoyagePlan plan, final int skip) {
		int planDuration = 0;
		final IDetailsSequenceElement[] sequence = plan.getSequence();
		final int k = sequence.length - skip;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			planDuration += (o instanceof VoyageDetails) ? (((VoyageDetails) o).getIdleTime() + ((VoyageDetails) o).getTravelTime()) : ((PortDetails) o).getOptions().getVisitDuration();
		}
		return planDuration;
	}
}
